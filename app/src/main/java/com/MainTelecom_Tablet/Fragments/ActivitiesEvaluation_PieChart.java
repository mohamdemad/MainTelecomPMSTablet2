package com.MainTelecom_Tablet.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
 * Created by MOHAMED on 12/04/2016.
 */
public class ActivitiesEvaluation_PieChart extends Fragment {


    PieChart chart;
    PieChart chart2;


    //Context context = this;

    private ProgressDialog pDialog;

    private JSONParser jParser = new JSONParser();

    private SwipeRefreshLayout swipeContainer;


    private String EventsPlanned  ;
    private String TasksPlanned ;
    private String TicketsPlanned ;
    private String EnhancementPlanned  ;
    private String EventsCompleted ;
    private String TasksCompleted ;
    private String TicketsCompleted ;
    private String EnhancementsCompleted ;

    int[] colors = new int[]{Color.rgb(69, 114, 166),Color.rgb(169,70,67),Color.rgb(136,164,78), Color.rgb(218, 131, 61)};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View Activities_Eva = inflater.inflate(R.layout.activities_evaluation_piechart, container, false);

        new LoadAllData().execute();

        chart = (PieChart) Activities_Eva.findViewById(R.id.chart);
        chart2 = (PieChart) Activities_Eva.findViewById(R.id.chart2);


        return Activities_Eva;


    }

    private PieDataSet getDataSet(String string) {
        //ArrayList<Entry> dataSets = null;
        PieDataSet PieDataSet1 = new PieDataSet(null, "");
        PieDataSet PieDataSet2 = new PieDataSet(null, "");

        ArrayList<Entry> valueSet1 = new ArrayList<>();
        ArrayList<Entry> valueSet2 = new ArrayList<>();

        if(string.equals("Effort_Planned")) {

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

        if(string.equals("Effort_Achieved")){

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

        return null;
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
                Log.d("activity_evalution: ", json.toString());
                System.out.println("activity_evalution: "+ json.toString());
                // looping through All Products
                for (int i = 0; i < json.length(); i++) {
                    JSONObject c = json.getJSONObject(i);

                    // Storing each json item in variable
                    //effort Planned
                    if(i==0){
                        EventsPlanned = c.getString("Events_Planned");
                        TasksPlanned = c.getString("Tasks_Planned");
                        TicketsPlanned = c.getString("Tickets_Planned");
                        EnhancementPlanned = c.getString("Enhancements_Planned");}

                    //effort Achieved
                    if(i==1){
                        EventsCompleted = c.getString("Events_Completed");
                        TasksCompleted =c.getString("Tasks_Completed");
                        TicketsCompleted =c.getString("Tickets_Completed");
                        EnhancementsCompleted =c.getString("Enhancements_Completed");}

                }
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), "This is Last Old Data Because There Is No Enternet Connection", Toast.LENGTH_LONG).show();
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


                PieData planned_data = new PieData(getXAxisValues(), getDataSet("Effort_Planned"));
                PieData achieved_data = new PieData(getXAxisValues(), getDataSet("Effort_Achieved"));

                planned_data.setValueFormatter(new PercentFormatter());
                achieved_data.setValueFormatter(new PercentFormatter());

                chart.setData(planned_data);
                chart2.setData(achieved_data);

                //chart.getLegend().setTextColor(Color.WHITE);
                //chart2.getLegend().setTextColor(Color.WHITE);

                chart.setDescription("");
                chart2.setDescription("");

                chart.setUsePercentValues(true);
                chart2.setUsePercentValues(true);

                chart.animateXY(2000, 2000);
                chart2.animateXY(2000, 2000);


                chart.invalidate();
                chart2.invalidate();

                // enable hole and configure
                chart.setHoleRadius(15);
                chart.setTransparentCircleRadius(20);
                chart2.setHoleRadius(15);
                chart2.setTransparentCircleRadius(20);

            }catch (Exception e){
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
//                        //finish();
//                        //startActivity(getIntent());
//                    }
//                });
//
//                AlertDialog alertD = alertDialog.create();
//                alertD.show();
            }


        }
    }

}