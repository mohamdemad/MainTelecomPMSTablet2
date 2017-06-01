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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOHAMED on 19/06/2016.
 */
public class Open_Closed_Tickets_PieChart extends Fragment {

    PieChart Open_Closed_Tickets_chart;

    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();

    String OpenTickets ;
    String CloseTickets ;
    List<NameValuePair> params = new ArrayList<NameValuePair>();



    int[] colors = new int[]{Color.rgb(69, 114, 166),Color.rgb(169,70,67),Color.rgb(136,164,78), Color.rgb(218, 131, 61)};

    String[] params_data;
    String[] xAxis_data;
    int search_check = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            params_data = getArguments().getStringArray("IDS");
            xAxis_data = getArguments().getStringArray("NAMES");
            search_check =1;
        }catch (Exception e){}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View Open_Closed_Tickets = inflater.inflate(R.layout.open_closed_tickets_piechart, container, false);
        Open_Closed_Tickets_chart = (PieChart) Open_Closed_Tickets.findViewById(R.id.open_closed_tickets_chart);

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



        return Open_Closed_Tickets;
    }

    private PieDataSet getDataSet() {

        PieDataSet PieDataSet1 = new PieDataSet(null, "");

        ArrayList<Entry> valueSet1 = new ArrayList<>();

        Entry v1e1 = new Entry(Integer.parseInt(OpenTickets), 0);
        Entry v2e1 = new Entry(Integer.parseInt(CloseTickets), 1);
        valueSet1.add(v1e1);
        valueSet1.add(v2e1);

        PieDataSet1 = new PieDataSet(valueSet1, "");
        //PieDataSet1.setValueTextColor(Color.WHITE);
        PieDataSet1.setColors(colors);
        return PieDataSet1;

    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Open Tickets");
        xAxis.add("Close Tickets");

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
                if(search_check==1){
                    params.clear();
                    JSONArray json = jParser.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_open_closed_tickets, "POST", params, "EVALUATION");
                    Log.d("All Data: ", json.toString());

                    JSONObject c = json.getJSONObject(0);
                    // Storing each json item in variable
                    OpenTickets = c.getString("Open_Tickets");
                    CloseTickets = c.getString("Closed_Tickets");

                }else {
                    params.clear();
                    JSONArray json = jParser.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_open_closed_tickets, "POST", params, "EVALUATION");
                    Log.d("All Data: ", json.toString());

                    JSONObject c = json.getJSONObject(0);
                    // Storing each json item in variable
                    OpenTickets = c.getString("Open_Tickets");
                    CloseTickets = c.getString("Closed_Tickets");
                }

            } catch (Exception e) {
                e.printStackTrace();

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


                PieData data = new PieData(getXAxisValues(), getDataSet());

                data.setValueFormatter(new PercentFormatter());

                Open_Closed_Tickets_chart.setData(data);
          //      Open_Closed_Tickets_chart.getLegend().setTextColor(Color.WHITE);
                Open_Closed_Tickets_chart.setDescription("");
                Open_Closed_Tickets_chart.setUsePercentValues(true);
                Open_Closed_Tickets_chart.animateXY(2000, 2000);
                Open_Closed_Tickets_chart.setHoleRadius(15);
                Open_Closed_Tickets_chart.setTransparentCircleRadius(20);
                Open_Closed_Tickets_chart.invalidate();

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
    public Open_Closed_Tickets_PieChart(){}
    public static Open_Closed_Tickets_PieChart newInstance(String[]ids,String[]names){

        Open_Closed_Tickets_PieChart openClosedTicketsPieChart = new Open_Closed_Tickets_PieChart();
        final Bundle args = new Bundle(2);
        args.putStringArray("IDS",ids);
        args.putStringArray("NAMES",names);
        openClosedTicketsPieChart.setArguments(args);
        return openClosedTicketsPieChart;

    }
}

