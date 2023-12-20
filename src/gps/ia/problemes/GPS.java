package gps.ia.problemes;

import gps.Distance;
import gps.ia.framework.common.Action;
import gps.ia.framework.common.State;
import gps.ia.framework.recherche.Problem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class GPS extends Problem {

    public static List<GPSState> MOST_POPULATED_CITIES;
    public static GPSState DEPARTURE;
    public static GPSState ARRIVAL;
    public static List<Action> ACTIONS;
    public Distance distance;

    public GPS() throws IOException, InterruptedException {
        this.distance = new Distance();
        MOST_POPULATED_CITIES = new ArrayList<>(100);
        String urlDeparture = "https://geo.api.gouv.fr/communes?nom=Algrange&fields=nom,centre,population";
        String urlArrival = "https://geo.api.gouv.fr/communes?nom=Nimes&fields=nom,centre,population";
        String url = "https://geo.api.gouv.fr/communes?fields=nom,centre,population";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlDeparture))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var departure = new JSONObject(response.body());

        String name = departure.getString("nom");
        double population = departure.getInt("population");
        double latitude = (double) departure.getJSONObject("centre").getJSONArray("coordinates").get(1);
        double longitude = (double) departure.getJSONObject("centre").getJSONArray("coordinates").get(0);
        DEPARTURE = new GPSState(new City(name, population, latitude, longitude), 0);

        request = HttpRequest.newBuilder()
                .uri(URI.create(urlArrival))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var arrival = new JSONObject(response.body());

        name = arrival.getString("nom");
        population = arrival.getInt("population");
        latitude = (double) arrival.getJSONObject("centre").getJSONArray("coordinates").get(1);
        longitude = (double) arrival.getJSONObject("centre").getJSONArray("coordinates").get(0);
        ARRIVAL = new GPSState(new City(name, population, latitude, longitude), 0);

        request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var cities = new JSONArray(response.body());

        List<JSONObject> jsonList = IntStream.range(0, cities.length())
                .mapToObj(cities::getJSONObject)
                .sorted(Comparator.comparing((JSONObject obj) -> obj.getInt("population")).reversed())
                .limit(100)
                .toList();
        cities = new JSONArray(jsonList);

        for (int i = 0; i < cities.length(); i++) {

            JSONObject jsonObject = cities.getJSONObject(i);

            name = jsonObject.getString("nom");
            population = jsonObject.getInt("population");
            latitude = (double) jsonObject.getJSONObject("centre").getJSONArray("coordinates").get(1);
            longitude = (double) jsonObject.getJSONObject("centre").getJSONArray("coordinates").get(0);

            double distToGoal = distance.calculate(latitude, longitude, DEPARTURE.getCity().latitude(), DEPARTURE.getCity().longitude());

            var city = new GPSState(new City(name, population, latitude, longitude), distToGoal);
            MOST_POPULATED_CITIES.add(city);
        }

        ACTIONS = new ArrayList<>(MOST_POPULATED_CITIES.size()+2);
        ACTIONS.add(new Action("goto Algrange"));
        ACTIONS.add(new Action("goto Nimes"));
        MOST_POPULATED_CITIES
                .forEach(city -> new Action("goto " + city.getCity().name()));
    }

    @Override
    public boolean isGoalState(State s) {
        return s.equals(ARRIVAL);
    }
}
