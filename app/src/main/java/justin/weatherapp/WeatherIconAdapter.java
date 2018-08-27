package justin.weatherapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class WeatherIconAdapter extends BaseAdapter {
    private final int NUM_COLS = 4;
    private Context context;
    private Integer[] gridIcons = {
            R.drawable.rain,
            R.drawable.cloudy,
            R.drawable.clearsky,
            R.drawable.ic_launcher_foreground
    };

    public WeatherIconAdapter(Context context) {
        this.context = context;
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
        } else {
            imageView = (ImageView) view;
        }

        imageView.setImageResource(gridIcons[0]);
        return imageView;
    }
}
