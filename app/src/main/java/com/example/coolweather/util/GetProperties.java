package com.example.coolweather.util;

import android.content.Context;
import android.util.Property;

import java.util.Properties;

/**
 * Created by Administrator on 2017/6/20.
 */

public class GetProperties {
    public static String getPropertiesString(Context context, String str){
        String str_return = null;
        Properties properties = new Properties();
        try{
            properties.load(context.getAssets().open("url.prop"));
            str_return = properties.getProperty(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return str_return;
    }
}
