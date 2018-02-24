package tjpark.tjsinfo.com.tjpark.util;

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
import tjpark.tjsinfo.com.tjpark.entity.Person;

/**
 * Created by panning on 2018/1/25.
 */

//扩展类，listview数据源ArrayAdapter
public class PersonAdapter extends ArrayAdapter<Person>  {

    private int resourceId;
    /**
     *context:当前活动上下文
     *textViewResourceId:ListView子项布局的ID
     *objects：要适配的数据
     */
    public PersonAdapter(Context context, int textViewResourceId,
                         List<Person> objects) {
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
        Person person = getItem(position);
        //为子项动态加载布局
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView personIcon = (ImageView) view.findViewById(R.id.personIcon);
        TextView personItem = (TextView) view.findViewById(R.id.personItem);
        personItem.setText(person.getPersonItem());
        personIcon.setBackgroundResource(person.getPersonImg());
        return view;
    }

}

