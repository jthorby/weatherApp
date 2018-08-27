package justin.weatherapp;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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

public class WeatherIconAdapter extends BaseAdapter {
    final String apiKey = "97b29ff78ac13197be0271410939a9f6";
    final String christchurchID = "2192362";
    private final int NUM_COLS = 4;
    private Context context;
    private Integer[] gridIcons = {
            R.drawable.rain,
            R.drawable.cloudy,
            R.drawable.clearsky,
            R.drawable.ic_launcher_foreground
    };
    private JSONArray jsonObject;
    Map<String, Integer> weatherIconMap = new HashMap<>();

    public WeatherIconAdapter(Context context) {
        this.context = context;
        setupIconMap();
        handleRequest();
    }

    @Override
    public int getCount() {
        return NUM_COLS;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ImageView imageView;
        if (view == null) {
            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) view;
        }

        addForecastToGridItem(imageView, i);
        imageView.setMaxHeight(parent.getHeight());

        return imageView;
    }

    private void addForecastToGridItem(ImageView imageView, int i) {
        JSON
    }

    private void handleRequest() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String baseURL = "http://api.openweathermap.org/data/2.5/";
        String cityId = christchurchID;
        String forecastURL = baseURL + "forecast?id=" + cityId + "&appid=" + apiKey;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, forecastURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            handleResponse(response);
                        } catch (JSONException e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        queue.add(stringRequest);
    }

    private void handleResponse(String response) throws JSONException {
        jsonObject = new JSONObject(response);
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
