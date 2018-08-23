package justin.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    final String christchurchID = "2192362";
    final String apiKey = "97b29ff78ac13197be0271410939a9f6";

    TextView weatherText;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showWeatherData();
    }

    private void showWeatherData() {
        weatherText = findViewById(R.id.weatherData);
        handleRequest();
    }

    private void handleRequest() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String baseURL = "http://api.openweathermap.org/data/2.5/";
        String forecastURL = baseURL + "forecast?id=" + christchurchID + "&appid=" + apiKey;
        String weatherURL = baseURL + "weather?id=" + christchurchID + "&appid=" + apiKey;

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
        JSONObject cloudData = jsonObject.getJSONObject("clouds");
        int cloudiness = cloudData.getInt("all");

        String tempCelsius1dp = String.format("The temperature is %.1f\u00b0C",
                                               tempCelsius);


        weatherText.setText(String.format("%s and cloudiness is %d", tempCelsius1dp, cloudiness));
//        weatherText.setText(jsonObject.toString());
    }

}
