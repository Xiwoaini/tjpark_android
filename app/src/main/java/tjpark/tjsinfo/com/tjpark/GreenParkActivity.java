package tjpark.tjsinfo.com.tjpark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by panning on 2018/1/12.
 */

public class GreenParkActivity extends AppCompatActivity {

    private TextView greenPark_placeName,greenPark_distance,greenPark_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greenpark);
        Button exitBtn=(Button)findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });
        greenPark_placeName = (TextView)findViewById(R.id.greenPark_placeName);
        greenPark_distance = (TextView)findViewById(R.id.greenPark_distance);
        greenPark_address = (TextView)findViewById(R.id.greenPark_address);

        Intent getIntent = getIntent();
        greenPark_placeName.setText(getIntent.getStringExtra("place_name"));
        greenPark_distance.setText(getIntent.getStringExtra("place_distance"));
        greenPark_address.setText(getIntent.getStringExtra("place_address"));
    }



}