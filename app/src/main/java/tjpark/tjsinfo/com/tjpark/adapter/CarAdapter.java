package tjpark.tjsinfo.com.tjpark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.entity.Car;
import tjpark.tjsinfo.com.tjpark.entity.Park;

/**
 * Created by panning on 2018/1/25.
 */
/**
 * listview自定义格式的适配器
 */
public class CarAdapter extends ArrayAdapter<Car> {

    private int resourceId;

    public CarAdapter(Context context, int textViewResourceId,
                      List<Car> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Car car = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView plate = (TextView) view.findViewById(R.id.plate);

        plate.setText(car.getPlace_number());
        return view;
    }

}

