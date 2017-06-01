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
public class Eva_Activities_Planned_Fragment extends Fragment {

    PieChart chart;

    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();



    String EventsPlanned ;
    String TasksPlanned ;
    String TicketsPlanned ;
    String EnhancementPlanned  ;


    int[] colors = new int[]{Color.rgb(69, 114, 166),Color.rgb(169,70,67),Color.rgb(136,164,78), Color.rgb(218, 131, 61)};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View effort_planned = inflater.inflate(R.layout.activities_planned, container, false);
        chart = (PieChart) effort_planned.findViewById(R.id.chart);

        new LoadAllData().execute();

//        SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) effort_planned.findViewById(R.id.swipeContainer);
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



        return effort_planned;
    }

    private PieDataSet getDataSet() {

        PieDataSet PieDataSet1 = new PieDataSet(null, "");

        ArrayList<Entry> valueSet1 = new ArrayList<>();

        Entry v1e1 = new Entry(Integer.parseInt(EventsPlanned), 0); // Planned
        Entry v2e1 = new Entry(Integer.parseInt(TasksPlanned), 1); // Planned
        Entry v3e1 = new Entry(Integer.parseInt(TicketsPlanned), 2); // Planned
        Entry v4e1 = new Entry(Integer.parseInt(EnhancementPlanned), 3); // Planned
        valueSet1.add(v1e1);
        valueSet1.add(v2e1);
        valueSet1.add(v3e1);
        valueSet1.add(v4e1);

        PieDataSet1 = new PieDataSet(valueSet1, "");
        //PieDataSet1.setValueTextColor(Color.WHITE);
        PieDataSet1.setColors(colors);
        return PieDataSet1;

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
                // looping through All Products
                JSONObject c = json.getJSONObject(0);

                // Storing each json item in variable
                //effort Planned

                EventsPlanned = c.getString("Events_Planned");
                TasksPlanned = c.getString("Tasks_Planned");
                TicketsPlanned = c.getString("Tickets_Planned");
                EnhancementPlanned = c.getString("Enhancements_Planned");



            } catch (Exception e) {
                e.printStackTrace();
                // Toast.makeText(getActivity(), "This is Last Old Data Because There Is No Enternet Connection", Toast.LENGTH_LONG).show();
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


                PieData planned_data = new PieData(getXAxisValues(), getDataSet());

                planned_data.setValueFormatter(new PercentFormatter());

                chart.setData(planned_data);

                chart.setDescription("");

          //      chart.getLegend().setTextColor(Color.WHITE);
                chart.setUsePercentValues(true);

                chart.animateXY(2000, 2000);
                chart.setHoleRadius(15);
                chart.setTransparentCircleRadius(20);

                chart.invalidate();


                // enable hole and configure
            /*chart.setHoleRadius(15);
            chart.setTransparentCircleRadius(20);


            // enable rotation of the chart by touch
            chart.setRotationAngle(0);
            chart.setRotationEnabled(true);
            chart.highlightValues(null);*/

            }catch(Exception e){

//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                alertDialog.setTitle("Error...");
//                alertDialog.setMessage("Check your Internet Connection ");
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
