package gps.ia.problemes;

import gps.Distance;
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
    public static GPSState ALGRANGE;
    public Distance distance;

    public GPS() throws IOException, InterruptedException {
        this.distance = new Distance();
        MOST_POPULATED_CITIES = new ArrayList<>(100);
        String urlAlgrange = "https://geo.api.gouv.fr/communes?nom=Algrange&fields=nom,centre,population";
        String url = "https://geo.api.gouv.fr/communes?fields=nom,centre,population";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlAlgrange))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var algrange = new JSONObject(response.body());

        String name = algrange.getString("nom");
        double population = algrange.getInt("population");
        double latitude = (double) algrange.getJSONObject("centre").getJSONArray("coordinates").get(1);
        double longitude = (double) algrange.getJSONObject("centre").getJSONArray("coordinates").get(0);
        ALGRANGE = new GPSState(new City(name, population, latitude, longitude), 0);

        request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var jsonArray = new JSONArray(response.body());

        List<JSONObject> jsonList = IntStream.range(0, jsonArray.length())
                .mapToObj(jsonArray::getJSONObject)
                .sorted(Comparator.comparing((JSONObject obj) -> obj.getInt("population")).reversed())
                .limit(100)
                .toList();
        jsonArray = new JSONArray(jsonList);

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            name = jsonObject.getString("nom");
            population = jsonObject.getInt("population");
            latitude = (double) jsonObject.getJSONObject("centre").getJSONArray("coordinates").get(1);
            longitude = (double) jsonObject.getJSONObject("centre").getJSONArray("coordinates").get(0);

            double distToGoal = distance.calculate(latitude, longitude, ALGRANGE.getCity().latitude(), ALGRANGE.getCity().longitude());

            var city = new GPSState(new City(name, population, latitude, longitude), distToGoal);
            MOST_POPULATED_CITIES.add(city);
        }
    }

    @Override
    public boolean isGoalState(State s) {
        return s.equals(ALGRANGE);
    }
}
