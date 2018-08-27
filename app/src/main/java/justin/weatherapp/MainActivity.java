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
    final String minneapolisID = "5037649";
    final String apiKey = "97b29ff78ac13197be0271410939a9f6";

    TextView weatherText;
    JSONObject jsonObject;
    Map<String, Integer> weatherIconMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupIconMap();
        showWeatherData();
        setupForecastGrid();
    }

    private void showWeatherData() {
        weatherText = findViewById(R.id.weatherData);
        handleRequest(minneapolisID);
    }

    private void setupForecastGrid() {
        GridView gridView = findViewById(R.id.forecastGrid);
        gridView.setAdapter(new WeatherIconAdapter(this));
    }

    private void handleRequest(String cityId) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String baseURL = "http://api.openweathermap.org/data/2.5/";
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
        weatherIconMap.put("01d", R.drawable.clearsky);
        weatherIconMap.put("02d", R.drawable.few_clouds);
        weatherIconMap.put("03d", R.drawable.scattered_clouds);
        weatherIconMap.put("04d", R.drawable.broken_clouds);
        weatherIconMap.put("09d", R.drawable.shower_rain);
        weatherIconMap.put("10d", R.drawable.rain);
        weatherIconMap.put("11d", R.drawable.thunder);
        weatherIconMap.put("13d", R.drawable.snow);
        weatherIconMap.put("50d", R.drawable.mist);

        weatherIconMap.put("01n", R.drawable.clearsky);
        weatherIconMap.put("02n", R.drawable.few_clouds);
        weatherIconMap.put("03n", R.drawable.scattered_clouds);
        weatherIconMap.put("04n", R.drawable.broken_clouds);
        weatherIconMap.put("09n", R.drawable.shower_rain);
        weatherIconMap.put("10n", R.drawable.rain);
        weatherIconMap.put("11n", R.drawable.thunder);
        weatherIconMap.put("13n", R.drawable.snow);
        weatherIconMap.put("50n", R.drawable.mist);
    }
}
