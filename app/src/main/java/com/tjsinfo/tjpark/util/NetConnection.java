package com.tjsinfo.tjpark.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
                JsonParser parse =new JsonParser();
                object = (JsonObject) parse.parse(res);
            }
        }catch(IOException e){
            e.printStackTrace();
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
}
