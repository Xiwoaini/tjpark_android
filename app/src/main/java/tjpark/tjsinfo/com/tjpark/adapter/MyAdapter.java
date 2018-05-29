package tjpark.tjsinfo.com.tjpark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tjpark.tjsinfo.com.tjpark.R;

/**
 * Created by panning on 2018/2/28.
 * */
/**
 * listview自定义格式的适配器
 */

public class MyAdapter extends BaseAdapter {
    List<String> datas = new ArrayList<>();
    Context mContext;
    public MyAdapter(Context context) {
        this.mContext = context;
    }



    public void setDatas(List<String> data) {
        this.datas = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas==null?0:datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas==null?null:datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler = null;
        if (convertView == null) {
            hodler = new ViewHodler();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.support_simple_spinner_dropdown_item, null);
            hodler.mTextView = (TextView) convertView;
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

        hodler.mTextView.setText(datas.get(position));

        return convertView;
    }

    private static class ViewHodler{
        TextView mTextView;
    }
}
