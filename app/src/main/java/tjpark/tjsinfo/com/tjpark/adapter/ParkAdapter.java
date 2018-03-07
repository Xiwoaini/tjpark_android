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


public class ParkAdapter extends ArrayAdapter<Park> {

    private int resourceId;
    /**
     *context:当前活动上下文
     *textViewResourceId:ListView子项布局的ID
     *objects：要适配的数据
     */
    public ParkAdapter(Context context, int textViewResourceId,
                        List<Park> objects) {
        super(context, textViewResourceId, objects);
        //拿取到子项布局ID
        resourceId = textViewResourceId;
    }

    /**
     * LIstView中每一个子项被滚动到屏幕的时候调用
     * position：滚到屏幕中的子项位置，可以通过这个位置拿到子项实例
     * convertView：之前加载好的布局进行缓存
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Park park = getItem(position);
        //为子项动态加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
//        ImageView fruitImage = (ImageView) view.findViewById(R.id.fruid_image);
        //        fruitImage.setImageResource(R.drawable.csh2));
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

