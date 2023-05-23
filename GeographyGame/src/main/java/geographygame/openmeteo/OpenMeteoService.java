package geographygame.openmeteo;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import geographygame.exceptions.OpenMeteoException;
import geographygame.model.Country;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OpenMeteoService {

    private final HttpClient HTTP_CLIENT;
    public OpenMeteoService() {
        HTTP_CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
    }

    private double[] getPosition(String capital, String iso2) throws OpenMeteoException {
        System.out.printf("--- getPosition(%s, %s) ---\n", capital, iso2);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://geocoding-api.open-meteo.com/v1/search?name=" + capital.replace(" ", "%20")))
                    .GET().build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            LocationResponse responseObj = new Gson().fromJson(response.body(), LocationResponse.class);
            List<LocationResult> results = Arrays.asList(responseObj.results);
            Optional<LocationResult> result = results.stream().filter(res -> res.countryCode.equals(iso2)).findFirst();
            if (result.isEmpty()) return new double[]{};
            return new double[]{result.get().latitude, result.get().longitude};
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new OpenMeteoException("Error when calling OpenMeteo: " + e.getMessage(), e);
        }
    }

    public int getCurrentWeather(Country country) throws OpenMeteoException {
        System.out.printf("--- getCurrentWeather(%s) ---\n", country.getIso3());
        try {
            double[] latitudeLongitude = getPosition(country.getCapital(), country.getIso2());
            if (latitudeLongitude.length == 0) return 100;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(String.format("https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current_weather=true", latitudeLongitude[0], latitudeLongitude[1])))
                    .GET().build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            WeatherResponse responseObj = new Gson().fromJson(response.body(), WeatherResponse.class);
            return (int) Math.ceil(responseObj.currentWeather.temperature);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new OpenMeteoException("Error when calling OpenMeteo: " + e.getMessage(), e);
        }
    }

}

class LocationResponse {
    public LocationResult[] results;
}

class LocationResult {
    public String name;
    @SerializedName(value = "country_code")
    public String countryCode;
    public double latitude;
    public double longitude;
}

class WeatherResponse {
    @SerializedName(value = "current_weather")
    public CurrentWeather currentWeather;
}

class CurrentWeather {
    public double temperature;
}
