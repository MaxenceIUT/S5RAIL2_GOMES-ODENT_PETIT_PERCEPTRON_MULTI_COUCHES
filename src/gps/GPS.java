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
import java.util.*;
import java.util.stream.IntStream;

public class GPS extends Problem {

    public static GPSState DEPARTURE;
    public static GPSState ARRIVAL;

    public GPS() throws IOException, InterruptedException {
        STATES = new State[100];
        String url = "https://geo.api.gouv.fr/communes?fields=nom,centre,population";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var cities = new JSONArray(response.body());

        List<JSONObject> jsonList = IntStream.range(0, cities.length())
                .mapToObj(cities::getJSONObject)
                .filter(jsonObject -> jsonObject.has("population"))
                .sorted(Comparator.comparing((JSONObject obj) -> obj.getInt("population")).reversed())
                .limit(100)
                .toList();
        cities = new JSONArray(jsonList);

        for (int i = 0; i < cities.length(); i++) {

            JSONObject jsonObject = cities.getJSONObject(i);

            String name = jsonObject.getString("nom");
            double population = jsonObject.getInt("population");
            double latitude = jsonObject.getJSONObject("centre").getJSONArray("coordinates").getBigDecimal(1).doubleValue();
            double longitude = jsonObject.getJSONObject("centre").getJSONArray("coordinates").getBigDecimal(0).doubleValue();

            var city = new GPSState(new City(name, population, latitude, longitude), 0);
            STATES[i] = city;
        }

        var random = new Random();
        DEPARTURE = (GPSState) STATES[random.nextInt(STATES.length)];
        ARRIVAL = (GPSState) STATES[random.nextInt(STATES.length)];

        Arrays.stream(STATES).forEach(state -> {

            double latitude = ((GPSState) state).getCity().latitude();
            double longitude = ((GPSState) state).getCity().longitude();
            double latitudeArrival = ARRIVAL.getCity().latitude();
            double longitudeArrival = ARRIVAL.getCity().longitude();

            double distToGoal = Distance.calculate(latitude, longitude, latitudeArrival, longitudeArrival);

            ((GPSState) state).setHeuristic(distToGoal);
        });

        ACTIONS = new Action[STATES.length];
        for (int i = 0; i < ACTIONS.length; i++) {
            String cityName = ((GPSState) STATES[i]).getCity().name();
            var action = new Action("goto " + cityName);
            ACTIONS[i] = action;
        }

        for (int i = 0; i < STATES.length; i++) {
            for (int j = 0; j < STATES.length; j++) {
                if (i != j) {
                    GPSState cityI = (GPSState) STATES[i];
                    GPSState cityJ = (GPSState) STATES[j];

                    double latitudeI = cityI.getCity().latitude();
                    double longitudeI = cityI.getCity().longitude();
                    double latitudeJ = cityJ.getCity().latitude();
                    double longitudeJ = cityJ.getCity().longitude();

                    double cost = Distance.calculate(latitudeI, longitudeI, latitudeJ, longitudeJ);

                    Road.Type roadType = Road.Type.B_ROAD;

                    if (i < 50 && j < 50) {
                        roadType = Road.Type.HIGHWAY;
                    } else if (i < 100) {
                        roadType = Road.Type.FAST_ROAD;
                    }

                    Road road = new Road(cityI.getCity(), cityJ.getCity(), roadType, cost);
                    cityI.addRoad(road);
                    cityJ.addRoad(road);
                    TRANSITIONS.addTransition(STATES[i], ACTIONS[j], STATES[j], cost);
                }
            }
        }
    }

    @Override
    public boolean isGoalState(State s) {
        return s.equals(ARRIVAL);
    }

}
