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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by MOHAMED on 02/05/2016.
 */
public class IncomingQueuesCalls_BarChart extends Fragment {

    BarChart Incoming_Queue_Calls_chart;

    private ProgressDialog pDialog;


    private JSONParser jParser = new JSONParser();
    private JSONParser jParser2 = new JSONParser();
    private JSONParser jParser_Queue = new JSONParser();


    private String completed_calls  ;
    private String missed_calls ;
    private String cancelled_calls  ;
    private String abandoned_calls  ;
    private String Queue_Name  ;
    ArrayList<String> xAxis = new ArrayList<String>();
    ArrayList<Float> data_bar1 = new ArrayList<Float>();
    ArrayList<Float> data_bar2 = new ArrayList<Float>();
    ArrayList<Float> data_bar3 = new ArrayList<Float>();
    ArrayList<Float> data_bar4 = new ArrayList<Float>();

    int[] colors1 = new int[]{Color.rgb(69, 144, 166)};
    int[] colors2 = new int[]{Color.rgb(169,70,67)};
    int[] colors3 = new int[]{Color.rgb(0,100,0)};
    int[] colors4 = new int[]{Color.rgb(148,0,211)};

    String[] params_data;
    String[] xAxis_data;
    int search_check = 0;
    int loop =0;

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

        View Incoming_Queue_Calls = inflater.inflate(R.layout.incoming_queues_calls_barchart, container, false);

        Incoming_Queue_Calls_chart = (BarChart) Incoming_Queue_Calls.findViewById(R.id.incoming_queues_calls_chart);

        // Loading products in Background Thread
        new LoadAllData().execute();


        return Incoming_Queue_Calls;
    }





    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;

        Float[] b1 = new Float [data_bar1.size()];
        Float[] b2 = new Float [data_bar2.size()];
        Float[] b3 = new Float [data_bar3.size()];
        Float[] b4 = new Float [data_bar4.size()];
        b1 = data_bar1.toArray(b1);
        b2 = data_bar2.toArray(b2);
        b3 = data_bar3.toArray(b3);
        b4 = data_bar4.toArray(b4);
        float[] bar1 = ArrayUtils.toPrimitive(b1);
        float[] bar2 = ArrayUtils.toPrimitive(b2);
        float[] bar3 = ArrayUtils.toPrimitive(b3);
        float[] bar4 = ArrayUtils.toPrimitive(b4);

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        ArrayList<BarEntry> valueSet4 = new ArrayList<>();

        for(int j=0 ;j < loop; j++) {
            BarEntry v1e1 = new BarEntry(bar1[j], j);
            valueSet1.add(v1e1);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1,"Completed Calls");
        //barDataSet1.setValueTextColor(Color.WHITE);
        barDataSet1.setColors(colors1);


        for(int d=0 ;d < loop; d++){
            BarEntry v1e2 = new BarEntry(bar2[d], d);
            valueSet2.add(v1e2);

        }

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Missed Calls");
        //barDataSet2.setValueTextColor(Color.WHITE);
        barDataSet2.setColors(colors2);


        for(int dd=0 ;dd < loop; dd++){
            BarEntry v1e3 = new BarEntry(bar3[dd], dd);
            valueSet3.add(v1e3);

        }

        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Cancelled Calls");
        //barDataSet3.setValueTextColor(Color.WHITE);
        barDataSet3.setColors(colors3);


        for(int jj=0 ;jj < loop; jj++){
            BarEntry v1e4 = new BarEntry(bar4[jj], jj);
            valueSet4.add(v1e4);

        }

        BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Abandoned Calls");
        //barDataSet4.setValueTextColor(Color.WHITE);
        barDataSet4.setColors(colors4);



        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        dataSets.add(barDataSet4);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Admin");
        xAxis.add("Marketing Max");
        xAxis.add("Max");

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

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                if (search_check == 1) {
                    params.clear();
                    xAxis.clear();
                    params.add(new BasicNameValuePair("FromDate", params_data[0]));
                    params.add(new BasicNameValuePair("ToDate", params_data[1]));
                    for (int j = 2; j < params_data.length; j++) {
                        params.add(new BasicNameValuePair("Select1[]", params_data[j]));
                    }
                    JSONArray json1 = jParser.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Incoming_Calls_Queue, "POST", params, "ROLES");

                    Log.d("Incoming queue: ", json1.toString());
                    System.out.println("Incoming queue: "+ json1.toString());

                    data_bar1.clear();
                    data_bar2.clear();
                    data_bar3.clear();
                    data_bar4.clear();

                    JSONObject c = json1.getJSONObject(0);
                    int max1 = Integer.parseInt(c.getString("no_rows"));
                    loop = max1;
                    for (int i = 1; i <= max1; i++) {

                        c = json1.getJSONObject(i);

                        completed_calls = c.getString("completed_calls");
                        missed_calls = c.getString("missed_calls");
                        cancelled_calls = c.getString("cancelled_calls");
                        Queue_Name = c.getString("Queue_Name");
                        abandoned_calls = c.getString("abandoned_calls");
                        data_bar1.add(Float.parseFloat(completed_calls));
                        data_bar2.add(Float.parseFloat(missed_calls));
                        data_bar3.add(Float.parseFloat(cancelled_calls));
                        data_bar4.add(Float.parseFloat(abandoned_calls));
                        xAxis.add(Queue_Name);
                    }
                } else {
                    List<NameValuePair> params_Agents = new ArrayList<NameValuePair>();
                    JSONArray json_Queue = jParser_Queue.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Queues_Data_List, "GET", params_Agents, "ROLES");
                    Log.d("All Data: ", json_Queue.toString());
                    params.clear();
                    xAxis.clear();
                    params.add(new BasicNameValuePair("FromDate", "2013-10-1"));
                    params.add(new BasicNameValuePair("ToDate", "2016-6-13"));

                    JSONObject ccc = json_Queue.getJSONObject(0);
                    int max3 = Integer.parseInt(ccc.getString("no_row"));
                    for (int i = 1; i <= max3; i++) {
                        ccc = json_Queue.getJSONObject(i);
                        String Queue_id = ccc.getString("Queue_id");
                        params.add(new BasicNameValuePair("Select1[]", Queue_id));

                    }
                    JSONArray json2 = jParser2.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Incoming_Calls_Queue, "POST", params, "ROLES");
                    Log.d("Incoming queue: ", json2.toString());
                    System.out.println("Incoming queue: "+ json2.toString());
                    data_bar1.clear();
                    data_bar2.clear();
                    data_bar3.clear();
                    data_bar4.clear();

                    JSONObject c2 = json2.getJSONObject(0);
                    int max2 = Integer.parseInt(c2.getString("no_rows"));
                    loop = max2;
                    for (int i = 1; i <= max2; i++) {

                        c2 = json2.getJSONObject(i);

                        completed_calls = c2.getString("completed_calls");
                        missed_calls = c2.getString("missed_calls");
                        cancelled_calls = c2.getString("cancelled_calls");
                        Queue_Name = ccc.getString("Queue_Name");
                        abandoned_calls = c2.getString("abandoned_calls");
                        data_bar1.add(Float.parseFloat(completed_calls));
                        data_bar2.add(Float.parseFloat(missed_calls));
                        data_bar3.add(Float.parseFloat(cancelled_calls));
                        data_bar4.add(Float.parseFloat(abandoned_calls));
                        xAxis.add(Queue_Name);
                    }
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
                BarData data = new BarData(xAxis, getDataSet());
                data.setHighlightEnabled(true);

//            Legend l = chart.getLegend();
//            l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
//            l.setFormSize(8f);
//            l.setFormToTextSpace(4f);
//            l.setXEntrySpace(6f);

                Incoming_Queue_Calls_chart.setData(data);
                Incoming_Queue_Calls_chart.setDrawValueAboveBar(true);
          //      Incoming_Queue_Calls_chart.getXAxis().setTextColor(Color.WHITE);
            //    Incoming_Queue_Calls_chart.getLegend().setTextColor(Color.WHITE);
                Incoming_Queue_Calls_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                Incoming_Queue_Calls_chart.setDescription("");
                Incoming_Queue_Calls_chart.animateXY(2000, 2000);
                Incoming_Queue_Calls_chart.invalidate();

            }catch (Exception e){

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
    public IncomingQueuesCalls_BarChart(){}
    public static IncomingQueuesCalls_BarChart newInstance(String[]ids,String[]names){

        IncomingQueuesCalls_BarChart incomingQueuesCallsBarChart = new IncomingQueuesCalls_BarChart();
        final Bundle args = new Bundle(2);
        args.putStringArray("IDS",ids);
        args.putStringArray("NAMES",names);
        incomingQueuesCallsBarChart.setArguments(args);
        return incomingQueuesCallsBarChart;

    }
}
