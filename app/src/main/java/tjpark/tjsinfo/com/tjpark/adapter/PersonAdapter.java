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
import tjpark.tjsinfo.com.tjpark.entity.Person;

/**
 * Created by panning on 2018/1/25.
 */
/**
 * listview自定义格式的适配器
 */
public class PersonAdapter extends ArrayAdapter<Person>  {

    private int resourceId;

    public PersonAdapter(Context context, int textViewResourceId,
                         List<Person> objects) {
        super(context, textViewResourceId, objects);

        resourceId = textViewResourceId;
    }


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

