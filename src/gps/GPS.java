package gps;

import games.ia.framework.recherche.Problem;
import games.ia.framework.common.Action;
import games.ia.framework.common.State;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class GPS extends Problem {

    public static GPSState DEPARTURE;
    public static GPSState ARRIVAL;
    public static List<Action> ACTIONS;
    public Distance distance;

    public GPS() throws IOException, InterruptedException {
        this.distance = new Distance();
        STATES = new State[102];
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

        STATES[0] = DEPARTURE;

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

        STATES[1] = ARRIVAL;

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
            STATES[i+2] = city;
        }

        ACTIONS = new ArrayList<>(STATES.length);
        ACTIONS.add(new Action("goto Algrange"));
        ACTIONS.add(new Action("goto Nimes"));
        Arrays.stream(STATES)
                .forEach(city -> new Action("goto " + ((GPSState) city).getCity().name()));

        for (int i = 0; i < STATES.length; i++) {
            for (int j = 0; j < STATES.length; j++) {
                if (i != j) {
                    double cost = distance.calculate(((GPSState) STATES[i]).getCity().latitude(), ((GPSState) STATES[i]).getCity().longitude(), ((GPSState) STATES[j]).getCity().latitude(), ((GPSState) STATES[j]).getCity().longitude());
                    TRANSITIONS.addTransition(STATES[i], ACTIONS.get(j), STATES[j], cost);
                }
            }
        }
    }

    @Override
    public boolean isGoalState(State s) {
        return s.equals(ARRIVAL);
    }
}
