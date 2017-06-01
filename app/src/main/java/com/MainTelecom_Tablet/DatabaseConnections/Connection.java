package com.MainTelecom_Tablet.DatabaseConnections;

import android.os.AsyncTask;
import android.util.Log;

import com.MainTelecom_Tablet.JSON.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOHAMED on 13/05/2016.
 */
public class Connection extends AsyncTask<String, String, String> {

    public ArrayList<String> arrayList;
    String mURL;
    List<NameValuePair> mParams;
    JSONParser mjParser;


    public Connection(String URL ,List<NameValuePair> params ,JSONParser jParser){
        mURL = URL;
        mParams = params;
        mjParser = jParser;
    }


        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * getting All Data from url
         */
        protected String doInBackground(String... args) {


            JSONArray json = mjParser.makeHttpRequest(mURL,"GET",mParams,"EVALUATION");
            Log.d("All Data: ", json.toString());

            try {
                for (int i = 1 ; i< json.length()-1;i++){
                    JSONObject c = json.getJSONObject(i);
                    arrayList.clear();
                    String Project_Name = c.getString("Project_Name");
                    String Project_id = c.getString("Project_id");

                    arrayList.add(Project_Name);


                }



            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {


        }
    }