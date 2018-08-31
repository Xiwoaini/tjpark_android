package com.tjsinfo.tjpark.wxapi;

/**
 * Created by panning on 2018/6/15.
 */

import android.util.Log;

import com.tjsinfo.tjpark.entity.REQ;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;

import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLUtil {


    //普通解析
    /**
     * 功能描述：根据提供的xml信息解析xml字符串
     * @param  info        信息

     * @return
     */
    public static  REQ parseXmlString(String info) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();   //创建解析器工厂
        DocumentBuilder db = dbf.newDocumentBuilder();    //通过解析器工厂创建一个解析器
        InputSource is = new InputSource(new StringReader(info));
        Document document = db.parse(is);     //将InputStream流转化为Document
        //将xml串解析出来的信息加入相应的实体Bean
            NodeList appid = document.getElementsByTagName("appid");
            NodeList partnerid = document.getElementsByTagName("mch_id");
             NodeList prepayid = document.getElementsByTagName("prepay_id");
             NodeList noncestr = document.getElementsByTagName("nonce_str");
                NodeList timestamp = document.getElementsByTagName("timestamp");
                 NodeList sign = document.getElementsByTagName("sign");
            REQ jobnotice = new REQ();
            jobnotice.setAppid(appid.item(0).getTextContent());
        jobnotice.setPartnerid(partnerid.item(0).getTextContent());
        jobnotice.setPrepayid(prepayid.item(0).getTextContent());
        jobnotice.setPackages("Sign=WXPay");
        jobnotice.setNoncestr(noncestr.item(0).getTextContent());
        jobnotice.setTimestamp(String.valueOf(System.currentTimeMillis()).toString().substring(0,10));
        jobnotice.setSign(sign.item(0).getTextContent());
        return  jobnotice;
    }


}
