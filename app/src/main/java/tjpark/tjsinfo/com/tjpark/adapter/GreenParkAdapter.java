package tjpark.tjsinfo.com.tjpark.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import tjpark.tjsinfo.com.tjpark.BlueParkActivity;
import tjpark.tjsinfo.com.tjpark.BlueYuYueActivity;
import tjpark.tjsinfo.com.tjpark.GreenParkActivity;
import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.entity.GreenPark;
import tjpark.tjsinfo.com.tjpark.entity.Park;

/**
 * Created by panning on 2018/1/25.
 */

public class GreenParkAdapter extends ArrayAdapter<GreenPark> {
    private Context mContext;
    private int resourceId;
    private LayoutInflater bsman = null;
    /**
     *context:当前活动上下文
     *textViewResourceId:ListView子项布局的ID
     *objects：要适配的数据
     */
    public GreenParkAdapter(Context context, int textViewResourceId,
                            List<GreenPark> objects) {
        super(context, textViewResourceId, objects);
        //拿取到子项布局ID
        resourceId = textViewResourceId;
        this.mContext = context;
        bsman = LayoutInflater.from(context);
    }


    /**
     * LIstView中每一个子项被滚动到屏幕的时候调用
     * position：滚到屏幕中的子项位置，可以通过这个位置拿到子项实例
     * convertView：之前加载好的布局进行缓存
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  GreenPark park = getItem(position);
        //为子项动态加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView CWH = (TextView) view.findViewById(R.id.CWH);
        TextView JTSJ = (TextView) view.findViewById(R.id.JTSJ);
        TextView JTFY = (TextView) view.findViewById(R.id.JTFY);

        Button yuYueBtn = (Button)view.findViewById(R.id.yuYueBtn);
        CWH.setText(park.getPark_num());
        JTSJ.setText(park.getStart_time()+" - "+park.getEnd_time());
        JTFY.setText(park.getPark_fee());
        //预约按钮监听
        yuYueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, BlueYuYueActivity.class);

                intent.putExtra("parkId",park.getPlace_id().toString());
                intent.putExtra("bluePark_placeName",park.getPlace_name().toString());
                intent.putExtra("bluePark_distance",park.getDistance().toString());

                intent.putExtra("bluePark_label","公共停车场");
                intent.putExtra("bluePark_address",park.getAddress().toString());
                mContext.startActivity(intent);


            }

        });

        return view;
    }

}

