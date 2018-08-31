package com.tjsinfo.tjpark.util;


import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by panning on 2018/1/15.
 */

public class NetConnection  {

    /**
     * 发起http请求并获取结果
     * @param requestUrl 请求地址
     */

        public static JsonObject getXpath(String requestUrl){

        String res="";
        JsonObject object = null;
        StringBuffer buffer = new StringBuffer();
        try{
            URL url = new URL(TjParkUtils.windowIp+requestUrl);
            HttpURLConnection urlCon= (HttpURLConnection)url.openConnection();

            if(200==urlCon.getResponseCode()){
                InputStream is = urlCon.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"utf-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                while((str = br.readLine())!=null){
                    buffer.append(str);
                }
                br.close();
                isr.close();
                is.close();
                res = buffer.toString();
                if (res.equals("")){
                    return null;
                }
                if (res.equals("\"1\"")){
                    return null;
                }
                JsonParser parse =new JsonParser();

                object = (JsonObject) parse.parse(res);
            }
        }catch(IOException e){
            return null;
        }
        return object;
    }
    /**
     * 发起http请求并获取结果
     * @param requestUrl 请求地址
     */

    public static JsonArray getJsonArray(String requestUrl){

        String res="";
        JsonArray jsonArray = null;
        StringBuffer buffer = new StringBuffer();
        try{
            URL url = new URL(TjParkUtils.windowIp+requestUrl);
            HttpURLConnection urlCon= (HttpURLConnection)url.openConnection();

            if(200==urlCon.getResponseCode()){
                InputStream is = urlCon.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"utf-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                while((str = br.readLine())!=null){
                    buffer.append(str);
                }

                br.close();
                isr.close();
                is.close();
                res = buffer.toString();
                if (res.equals("")){
                    return null;
                }
                JsonParser parse =new JsonParser();
                jsonArray = (JsonArray) parse.parse(res);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * 发起http请求并获取结果，百度地图地址搜索专用
     * @param requestUrl 请求地址
     */

    public static JsonObject getAddressStatus(String requestUrl){

        String res="";
        JsonObject object = null;
        StringBuffer buffer = new StringBuffer();
        try{
            URL url = new URL(requestUrl);
            HttpURLConnection urlCon= (HttpURLConnection)url.openConnection();

            if(200==urlCon.getResponseCode()){
                InputStream is = urlCon.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"utf-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                while((str = br.readLine())!=null){
                    buffer.append(str);
                }
                br.close();
                isr.close();
                is.close();
                res = buffer.toString();
                JsonParser parse =new JsonParser();
                object = (JsonObject) parse.parse(res);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 发起http请求并获取结果，百度地图地址搜索专用
     * @param requestUrl 请求地址
     */

    public static String getHttpString(String requestUrl){

        String res="";
        JsonObject object = null;
        StringBuffer buffer = new StringBuffer();
        try{
            URL url = new URL(requestUrl);
            HttpURLConnection urlCon= (HttpURLConnection)url.openConnection();

            if(200==urlCon.getResponseCode()){
                InputStream is = urlCon.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"utf-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                while((str = br.readLine())!=null){
                    buffer.append(str);
                }
                br.close();
                isr.close();
                is.close();
                res = buffer.toString();
                if (res.equals("")){
                    return "";
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return res;
    }


public static HttpResponse post(String s,String p) {

    HttpClient client = new DefaultHttpClient();
    String urlStr=TjParkUtils.windowIp+"/tjpark/app/UploadWebService/imgVerification";
    HttpPost httpPost = new HttpPost(urlStr);
    httpPost.addHeader("charset", HTTP.UTF_8);
    httpPost.setHeader("Content-Type",
            "application/x-www-form-urlencoded; charset=utf-8");
    HttpResponse response = null;
    List<NameValuePair> nameValuepairs = new ArrayList<NameValuePair>();
    nameValuepairs.add(new BasicNameValuePair("imgStr", s));
    nameValuepairs.add(new BasicNameValuePair("plateId", p));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuepairs,
                    HTTP.UTF_8));
            response = client.execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        try {
            response = client.execute(httpPost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    return response;

}





}

