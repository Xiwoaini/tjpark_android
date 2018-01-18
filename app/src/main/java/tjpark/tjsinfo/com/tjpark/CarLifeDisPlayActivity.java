package tjpark.tjsinfo.com.tjpark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by panning on 2018/1/16.
 */

//车生活具体展示
public class CarLifeDisPlayActivity  extends AppCompatActivity {

    public static String strUrl = "http://m.weizhang8.cn/";

    private WebView webView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carlifedisplay);
        webView = (WebView)findViewById(R.id.webView);
        webView.loadUrl(strUrl);
    }




}
