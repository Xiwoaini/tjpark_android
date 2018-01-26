package tjpark.tjsinfo.com.tjpark.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.entity.Order;
import tjpark.tjsinfo.com.tjpark.entity.Park;

/**
 * Created by panning on 2018/1/25.
 */

//扩展类，listview数据源ArrayAdapter
public class OrderAdapter extends ArrayAdapter<Order> {

    private int resourceId;
    /**
     *context:当前活动上下文
     *textViewResourceId:ListView子项布局的ID
     *objects：要适配的数据
     */
    public OrderAdapter(Context context, int textViewResourceId,
                        List<Order> objects) {
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
        Order order = getItem(position);  //获取当前项的Fruit实例
        //为子项动态加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView order_inTime = (TextView) view.findViewById(R.id.order_inTime);
        TextView order_placeName = (TextView) view.findViewById(R.id.order_placeName);
        TextView order_parkTime = (TextView) view.findViewById(R.id.order_parkTime);
        TextView order_status = (TextView) view.findViewById(R.id.order_status);
        TextView order_realParkFee = (TextView) view.findViewById(R.id.order_realParkFee);
        TextView order_placeNumber = (TextView) view.findViewById(R.id.order_placeNumber);
        order_inTime.setText(order.getIn_time());
        order_placeName.setText(order.getPlace_name());
        order_parkTime.setText(order.getPark_time());
        order_status.setText(order.getStatus());
        order_realParkFee.setText(order.getReal_park_fee());
        order_placeNumber.setText(order.getPlace_number());

        return view;
    }

}

