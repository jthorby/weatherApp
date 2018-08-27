package justin.weatherapp;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class WeatherIconAdapter extends BaseAdapter {
    final String apiKey = "97b29ff78ac13197be0271410939a9f6";
    final String christchurchID = "2192362";
    final String darwinID = "2073124";
    final String dunedinID = "2191562";
    final String minneapolisID = "5037649";
    private final int NUM_COLS = 4;
    private Context context;
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
        File directory = context.getFilesDir();
        File file = new File(directory, "forecast.json");
        JSONArray jsonArray = null;

        try {
            String content = FileUtilities.getFileContents(file);
            jsonArray = new JSONArray(content);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        if (jsonArray != null) {
            try {
                JSONObject object = (JSONObject) jsonArray.get((i * 8) + 8);
                JSONObject mainData = object.getJSONObject("main");
                JSONArray weatherArray = object.getJSONArray("weather");
                JSONObject weather = (JSONObject) weatherArray.get(0);
                String forecastIcon = weather.getString("icon");
                imageView.setImageResource(weatherIconMap.get(forecastIcon));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRequest() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String baseURL = "http://api.openweathermap.org/data/2.5/";
        String cityId = minneapolisID;
        String forecastURL = baseURL + "forecast?id=" + cityId + "&appid=" + apiKey;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, forecastURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            handleResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(stringRequest);
    }

    private void handleResponse(String response) throws JSONException {
        JSONObject json = new JSONObject(response);
        JSONArray jsonArray= json.getJSONArray("list");
        String filename = "forecast.json";
        String fileContents = jsonArray.toString();
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
