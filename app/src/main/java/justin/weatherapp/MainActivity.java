package justin.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final String christchurchID = "2192362";
    final String darwinID = "2073124";
    final String dunedinID = "2191562";
    final String apiKey = "97b29ff78ac13197be0271410939a9f6";

    TextView weatherText;
    JSONObject jsonObject;
    Map<String, Integer> weatherIconMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupForecastGrid();
        setupIconMap();
        showWeatherData();
    }

    private void showWeatherData() {
        weatherText = findViewById(R.id.weatherData);
        handleRequest(christchurchID);
    }

    private void setupForecastGrid() {
        GridView gridView = findViewById(R.id.forecastGrid);
        gridView.setAdapter(new WeatherIconAdapter(this));
    }

    private void handleRequest(String cityId) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String baseURL = "http://api.openweathermap.org/data/2.5/";
        String forecastURL = baseURL + "forecast?id=" + cityId + "&appid=" + apiKey;
        String weatherURL = baseURL + "weather?id=" + cityId + "&appid=" + apiKey;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, weatherURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            handleResponse(response);
                        } catch (JSONException e) {
                            weatherText.setText("Error loading weather data");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                weatherText.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void handleResponse(String response) throws JSONException {
        jsonObject = new JSONObject(response);
        JSONObject mainData = jsonObject.getJSONObject("main");
        double tempKelvin = mainData.getDouble("temp");
        double tempCelsius = tempKelvin - 273.15;

        String cityName = jsonObject.getString("name");
        TextView cityText = findViewById(R.id.cityText);
        cityText.setText(cityName);

        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weather = (JSONObject) weatherArray.get(0);
        String weatherConditionIcon = weather.getString("icon");

        String tempCelsius1dp = String.format("%.1f\u00b0C", tempCelsius);
        weatherText.setText(tempCelsius1dp);

        ImageView weatherIconView = findViewById(R.id.weatherIconView);
        weatherIconView.setImageResource(weatherIconMap.get(weatherConditionIcon));
    }

    private void setupIconMap() {
        int clear = R.drawable.clearsky;
        int cloudy = R.drawable.cloudy;
        int rain = R.drawable.rain;
        int unknown = R.drawable.ic_launcher_foreground;

        weatherIconMap.put("01d", clear);
        weatherIconMap.put("02d", clear);
        weatherIconMap.put("03d", cloudy);
        weatherIconMap.put("04d", cloudy);
        weatherIconMap.put("09d", rain);
        weatherIconMap.put("10d", rain);
        weatherIconMap.put("11d", unknown);
        weatherIconMap.put("13d", rain);
        weatherIconMap.put("50d", unknown);

        weatherIconMap.put("01n", clear);
        weatherIconMap.put("02n", clear);
        weatherIconMap.put("03n", cloudy);
        weatherIconMap.put("04n", cloudy);
        weatherIconMap.put("09n", rain);
        weatherIconMap.put("10n", rain);
        weatherIconMap.put("11n", unknown);
        weatherIconMap.put("13n", rain);
        weatherIconMap.put("50n", unknown);
    }

}
