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
public class CarAdapter extends ArrayAdapter<Car> {

    private int resourceId;
    /**
     *context:当前活动上下文
     *textViewResourceId:ListView子项布局的ID
     *objects：要适配的数据
     */
    public CarAdapter(Context context, int textViewResourceId,
                      List<Car> objects) {
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
        Car car = getItem(position);  //获取当前项的Fruit实例
        //为子项动态加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
//        ImageView carIcon = (ImageView) view.findViewById(R.id.carIcon);
        TextView plate = (TextView) view.findViewById(R.id.plate);

        plate.setText(car.getPlace_number());
        return view;
    }

}

