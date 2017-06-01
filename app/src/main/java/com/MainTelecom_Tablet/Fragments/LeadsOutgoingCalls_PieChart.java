package com.MainTelecom_Tablet.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MainTelecom_Tablet.JSON.JSONParser2;
import com.MainTelecom_Tablet.R;
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
 * Created by MOHAMED on 02/05/2016.
 */
public class LeadsOutgoingCalls_PieChart extends Fragment {


    PieChart leads_outgoing_chart;

    private ProgressDialog pDialog;

    JSONParser2 jParser = new JSONParser2();

    String Leads ;
    String Outgoing_Calls ;



    int[] colors = new int[]{Color.rgb(69, 114, 166),Color.rgb(169,70,67),Color.rgb(136,164,78), Color.rgb(218, 131, 61)};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View Leads_Outgoing = inflater.inflate(R.layout.leads_outgoing_calls_layout, container, false);
        leads_outgoing_chart = (PieChart) Leads_Outgoing.findViewById(R.id.leads_outgoing_calls_chart);

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



        return Leads_Outgoing;
    }

    private PieDataSet getDataSet() {

        PieDataSet PieDataSet1 = new PieDataSet(null, "");

        ArrayList<Entry> valueSet1 = new ArrayList<>();

        Entry v1e1 = new Entry(Integer.parseInt(Leads), 0);
        Entry v2e1 = new Entry(Integer.parseInt(Outgoing_Calls), 1);
        valueSet1.add(v1e1);
        valueSet1.add(v2e1);

        PieDataSet1 = new PieDataSet(valueSet1, "");
        //PieDataSet1.setValueTextColor(Color.WHITE);
        PieDataSet1.setColors(colors);
        return PieDataSet1;

    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Leads");
        xAxis.add("Outgoing Calls");

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
                // getting JSON string from URL
                JSONArray json = jParser.makeHttpRequest("http://41.32.29.164/Android/LeadsOutCalls.php", "GET", params,"TIME_EVALUATION");
                Log.d("LeadsOutCalls: ", json.toString());
                System.out.println("LeadsOutCalls: "+ json.toString());

                JSONObject c = json.getJSONObject(0);
                // Storing each json item in variable
                //effort Planned
                Leads = c.getString("Leads");
                Outgoing_Calls = c.getString("Out_Calls");
               // TicketsPlanned = c.getString("Tickets_Planned");
                //EnhancementPlanned = c.getString("Enhancements_Planned");

                //Leads = "37";
                //Outgoing_Calls= "62";




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


                PieData planned_data = new PieData(getXAxisValues(), getDataSet());

                planned_data.setValueFormatter(new PercentFormatter());

                leads_outgoing_chart.setData(planned_data);
          //      leads_outgoing_chart.getLegend().setTextColor(Color.WHITE);
                leads_outgoing_chart.setDescription("");
                leads_outgoing_chart.setUsePercentValues(true);
                leads_outgoing_chart.animateXY(2000, 2000);
                leads_outgoing_chart.setHoleRadius(15);
                leads_outgoing_chart.setTransparentCircleRadius(20);
                leads_outgoing_chart.invalidate();

                // enable hole and configure
            /*chart.setHoleRadius(15);
            chart.setTransparentCircleRadius(20);


            // enable rotation of the chart by touch
            chart.setRotationAngle(0);
            chart.setRotationEnabled(true);
            chart.highlightValues(null);*/

            }catch(Exception e){

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Error...");
                alertDialog.setMessage("Check your Internet Connection ");
                alertDialog.setCancelable(false);

                alertDialog.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                alertDialog.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog alertD = alertDialog.create();
                alertD.show();

            }



        }
    }
}

