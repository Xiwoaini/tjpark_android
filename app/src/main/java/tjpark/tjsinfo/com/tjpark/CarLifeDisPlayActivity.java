package tjpark.tjsinfo.com.tjpark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by panning on 2018/1/16.
 */

//车生活具体展示
public class CarLifeDisPlayActivity  extends AppCompatActivity {

    public static String strUrl = "";

    private WebView webView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carlifedisplay);
        Log.v("值为:",strUrl);
        if (strUrl.equals("")){
            strUrl="http://m.weizhang8.cn/";
        }

        webView = (WebView)findViewById(R.id.webView);
        webView.loadUrl(strUrl);
    }




}
