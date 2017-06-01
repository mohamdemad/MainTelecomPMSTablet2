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
 * Created by MOHAMED on 01/05/2016.
 */
public class Org_Allocation_PieChart extends Fragment {

    PieChart Org_Allocation_chart;

    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();

    String Allocated ;
    String Not_Allocated ;



    int[] colors = new int[]{Color.rgb(69, 114, 166),Color.rgb(169,70,67),Color.rgb(136,164,78), Color.rgb(218, 131, 61)};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View Org_Allocation = inflater.inflate(R.layout.organization_allocation_layout, container, false);
        Org_Allocation_chart = (PieChart) Org_Allocation.findViewById(R.id.org_allocation_chart);

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



        return Org_Allocation;
    }

    private PieDataSet getDataSet() {

        PieDataSet PieDataSet1 = new PieDataSet(null, "");

        ArrayList<Entry> valueSet1 = new ArrayList<>();

        Entry v1e1 = new Entry(Integer.parseInt(Allocated), 0);
        Entry v2e1 = new Entry(Integer.parseInt(Not_Allocated), 1);
        valueSet1.add(v1e1);
        valueSet1.add(v2e1);

        PieDataSet1 = new PieDataSet(valueSet1, "");
//        PieDataSet1.setValueTextColor(Color.WHITE);
        PieDataSet1.setColors(colors);
        return PieDataSet1;

    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Allocated");
        xAxis.add("Not Allocated");

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
                JSONArray json = jParser.makeHttpRequest("http://"+ SaveSharedPreference.getUserIP(getActivity())+AppConfig.url_Org_Allocation, "POST", params,"EVALUATION");
                Log.d("All Data: ", json.toString());

                JSONObject c = json.getJSONObject(0);
                // Storing each json item in variable
                Allocated = c.getString("Allocated");
                Not_Allocated = c.getString("Not_Allocated");

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

                Org_Allocation_chart.setData(planned_data);
  //              Org_Allocation_chart.getLegend().setTextColor(Color.WHITE);
                Org_Allocation_chart.setDescription("");
                Org_Allocation_chart.setUsePercentValues(true);
                Org_Allocation_chart.animateXY(2000, 2000);
                Org_Allocation_chart.setHoleRadius(15);
                Org_Allocation_chart.setTransparentCircleRadius(20);
                Org_Allocation_chart.invalidate();

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

