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

import tjpark.tjsinfo.com.tjpark.BlueYuYueActivity;
import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.entity.GreenPark;

/**
 * Created by panning on 2018/1/25.
 */
/**
 * listview自定义格式的适配器
 */
public class GreenParkAdapter extends ArrayAdapter<GreenPark> {
    private Context mContext;
    private int resourceId;
    private LayoutInflater bsman = null;

    public GreenParkAdapter(Context context, int textViewResourceId,
                            List<GreenPark> objects) {
        super(context, textViewResourceId, objects);

        resourceId = textViewResourceId;
        this.mContext = context;
        bsman = LayoutInflater.from(context);
    }



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

