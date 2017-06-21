package com.example.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;
import com.example.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class Utility {

    private static final String TAG = "Utility";

    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0;i < allProvinces.length();i++)
                {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static  boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for (int i = 0;i < allCities.length();i++){
                    JSONObject citiesObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(citiesObject.getString("name"));
                    city.setCityCode(citiesObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static  boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0;i < allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    Log.d(TAG,countyObject.getString("name"));
                    List<County> countyList = DataSupport.where("countyname = ? and cityid = ?",
                            countyObject.getString("name"),String.valueOf(cityId)).find(County.class);
                    if(countyList.size() > 0){

                        for(County county1:countyList)
                        {
                                int updateid = county1.getId();
                                County county = new County();
                                county.setWeatherId(countyObject.getString("weather_id"));
                                county.update(updateid);
                        }
                    }else {
                        County county = new County();
                        county.setCountyName(countyObject.getString("name"));
                        county.setWeatherId(countyObject.getString("weather_id"));
                        county.setCityId(cityId);
                        county.save();
                    }
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
