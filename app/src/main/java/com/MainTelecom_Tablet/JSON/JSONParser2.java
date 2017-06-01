package com.MainTelecom_Tablet.JSON;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by MOHAMED on 12/04/2016.
 */
public class JSONParser2 {
static InputStream is = null;
static JSONArray jObj = null;
static JSONArray jObj1 = null;
static JSONArray jObj2 = null;
static JSONArray jObj3 = null;
static String json1 = "";
static String json2 = "";
static String json3 = "";


        // constructor
        public JSONParser2() {

        }

        // function get json from url
        // by making HTTP POST or GET mehtod
        public JSONArray makeHttpRequest(String url, String method, List<NameValuePair> params ,String check) {

            // Making HTTP request
            try {

                // check for request method
                if(method == "POST"){
                    // request method is POST
                    // defaultHttpClient
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new UrlEncodedFormEntity(params));

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();

                }else if(method == "GET"){
                    // request method is GET
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                    HttpGet httpGet = new HttpGet(url);

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    is = httpEntity.getContent();
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                if(check== "TIME_EVALUATION"){ json1 = sb.toString();}
                if(check== "ROLES"){json2 = sb.toString();}
                if(check== "DEPARTMENT"){json3 = sb.toString();}

            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());


            }

            // try parse the string to a JSON object
            try {
                if(check== "TIME_EVALUATION"){jObj1 = new JSONArray(json1);return jObj1;}
                if(check== "ROLES"){jObj2 = new JSONArray(json2);return jObj2;}
                if(check== "DEPARTMENT"){jObj3 = new JSONArray(json3);return jObj3;}

            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            // return JSON String
            return jObj;

        }
}
