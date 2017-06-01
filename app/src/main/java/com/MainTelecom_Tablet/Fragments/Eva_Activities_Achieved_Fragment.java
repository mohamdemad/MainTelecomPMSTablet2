package com.MainTelecom_Tablet.Fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MainTelecom_Tablet.App.AppConfig;
import com.MainTelecom_Tablet.JSON.JSONParser;
import com.MainTelecom_Tablet.R;
import com.MainTelecom_Tablet.SharedPreference.SaveSharedPreference;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by MOHAMED on 20/04/2016.
 */
public class Eva_Activities_Achieved_Fragment extends Fragment {

    PieChart chart;

    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();



    String EventsCompleted ;
    String TasksCompleted ;
    String TicketsCompleted ;
    String EnhancementsCompleted ;

    int[] colors = new int[]{Color.rgb(69, 114, 166),Color.rgb(169,70,67),Color.rgb(136,164,78), Color.rgb(218, 131, 61)};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View effort_achieved = inflater.inflate(R.layout.activities_achieved, container, false);
        chart = (PieChart) effort_achieved.findViewById(R.id.chart);

        new LoadAllData().execute();
//        SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) effort_achieved.findViewById(R.id.swipeContainer);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getActivity().finish();
//                startActivity(getActivity().getIntent());
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);



        return effort_achieved;
    }

    private PieDataSet getDataSet() {

        PieDataSet PieDataSet2 = new PieDataSet(null, "");

        ArrayList<Entry> valueSet2 = new ArrayList<>();


        Entry v1e2 = new Entry(Integer.parseInt(EventsCompleted), 0); // Completed
        Entry v2e2 = new Entry(Integer.parseInt(TasksCompleted), 1); // Completed
        Entry v3e2 = new Entry(Integer.parseInt(TicketsCompleted), 2); // Completed
        Entry v4e2 = new Entry(Integer.parseInt(EnhancementsCompleted), 3); // Completed
        valueSet2.add(v1e2);
        valueSet2.add(v2e2);
        valueSet2.add(v3e2);
        valueSet2.add(v4e2);

        PieDataSet2 = new PieDataSet(valueSet2, "");
        //PieDataSet2.setValueTextColor(Color.WHITE);
        PieDataSet2.setColors(colors);
        return PieDataSet2;

    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Events");
        xAxis.add("Tasks");
        xAxis.add("Tickets");
        xAxis.add("Enhancements");
        return xAxis;
    }

    /**
     * Background Async Task to Load all Data by making HTTP Request
     * */
    class LoadAllData extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All Data from url
         * */
        protected String doInBackground(String... args) {

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("FromDate","2015-7-1"));
                params.add(new BasicNameValuePair("ToDate","2016-7-1"));
                // getting JSON string from URL
                JSONArray json = jParser.makeHttpRequest("http://"+ SaveSharedPreference.getUserIP(getActivity())+AppConfig.url_activity_evalution, "POST", params,"EVALUATION");

                // Check your log cat for JSON reponse
                Log.d("All Data: ", json.toString());
                System.out.println("Activity achieve :"+json.toString());
                // looping through All Products

                JSONObject c = json.getJSONObject(1);

                // Storing each json item in variable
                //effort Completed

                EventsCompleted = c.getString("Events_Completed");
                TasksCompleted = c.getString("Tasks_Completed");
                TicketsCompleted = c.getString("Tickets_Completed");
                EnhancementsCompleted = c.getString("Enhancements_Completed");


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error activity achieve : "+e.toString());
                //Toast.makeText(getActivity(), "This is Last Old Data Because There Is No Enternet Connection", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            try {
                PieData achieved_data = new PieData(getXAxisValues(), getDataSet());

                achieved_data.setValueFormatter(new PercentFormatter());

                chart.setData(achieved_data);
          //      chart.getLegend().setTextColor(Color.WHITE);
                chart.setDescription("");
                chart.setUsePercentValues(true);
                chart.animateXY(2000, 2000);
                chart.setHoleRadius(15);
                chart.setTransparentCircleRadius(20);

                chart.invalidate();

                // enable hole and configure
            /*
            chart.setHoleRadius(15);
            chart.setTransparentCircleRadius(20);

            // enable rotation of the chart by touch

            chart.setRotationAngle(0);
            chart.setRotationEnabled(true);
            chart.highlightValues(null);*/
            } catch (Exception e){
//
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                alertDialog.setTitle("Error...");
//                alertDialog.setMessage("Check your Internet Connection "+e.toString());
//                alertDialog.setCancelable(false);
//
//                alertDialog.setPositiveButton("Back", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                });
//
//                alertDialog.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                });
//
//                AlertDialog alertD = alertDialog.create();
//                alertD.show();

            }

        }
    }
}
