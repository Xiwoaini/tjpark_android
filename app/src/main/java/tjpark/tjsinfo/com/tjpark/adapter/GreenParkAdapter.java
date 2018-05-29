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

import tjpark.tjsinfo.com.tjpark.activity.BlueYuYueActivity;
import tjpark.tjsinfo.com.tjpark.R;
import tjpark.tjsinfo.com.tjpark.entity.Park;

/**
 * Created by panning on 2018/1/25.
 */
/**
 * listview自定义格式的适配器
 */
public class GreenParkAdapter extends ArrayAdapter<Park> {
    private Context mContext;
    private int resourceId;
    private LayoutInflater bsman = null;

    public GreenParkAdapter(Context context, int textViewResourceId,
                            List<Park> objects) {
        super(context, textViewResourceId, objects);

        resourceId = textViewResourceId;
        this.mContext = context;
        bsman = LayoutInflater.from(context);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final  Park park = getItem(position);
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
                park.setId(park.getPlace_id());
                intent.putExtra("park",park);

                mContext.startActivity(intent);


            }

        });

        return view;
    }

}

