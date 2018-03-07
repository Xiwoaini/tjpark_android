package tjpark.tjsinfo.com.tjpark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.entity.Park;

/**
 * Created by panning on 2018/1/25.
 */

/**
 * listview自定义格式的适配器
 */
public class ParkAdapter extends ArrayAdapter<Park> {

    private int resourceId;

    public ParkAdapter(Context context, int textViewResourceId,
                        List<Park> objects) {
        super(context, textViewResourceId, objects);

        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Park park = getItem(position);
        //为子项动态加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView parkName = (TextView) view.findViewById(R.id.parkName);
        TextView parkDistance = (TextView) view.findViewById(R.id.parkDistance);
        TextView parkAddress = (TextView) view.findViewById(R.id.parkAddress);
        TextView spaceNum = (TextView) view.findViewById(R.id.spaceNum);
        parkName.setText(park.getPlace_name());
        parkDistance.setText(park.getDistance());
        parkAddress.setText(park.getPlace_address());
        spaceNum.setText(park.getSpace_num());
        return view;
    }

}

