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
public class OutgoingAgentCalls_BarChart extends Fragment {

    BarChart Outgoing_Agent_Calls_chart;

    private ProgressDialog pDialog;
    private JSONParser jParser = new JSONParser();
    private JSONParser jParser2 = new JSONParser();
    private JSONParser jParser_agents = new JSONParser();


    String RoleName;
    private String completed_calls ;
    private String cancelled_calls ;
    private String Not_completed_calls ;
    private String Agent_Name ;
    ArrayList<String> xAxis = new ArrayList<String>();
    ArrayList<Float> data_bar1 = new ArrayList<Float>();
    ArrayList<Float> data_bar2 = new ArrayList<Float>();
    ArrayList<Float> data_bar3 = new ArrayList<Float>();

    int[] colors1 = new int[]{Color.rgb(69, 144, 166)};
    int[] colors2 = new int[]{Color.rgb(169,70,67)};
    int[] colors3 = new int[]{Color.rgb(0,100,0)};

    String[] params_data;
    String[] xAxis_data;
    int search_check = 0;
    int loop = 0;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            params_data = getArguments().getStringArray("IDS");
            xAxis_data = getArguments().getStringArray("NAMES");
            search_check =1;
        }catch (Exception e){}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View Outgoing_Agent_Calls = inflater.inflate(R.layout.outgoing_agent_calls_layout, container, false);

        Outgoing_Agent_Calls_chart = (BarChart) Outgoing_Agent_Calls.findViewById(R.id.outgoing_agent_calls_chart);

        // Loading products in Background Thread
        new LoadAllData().execute();


        return Outgoing_Agent_Calls;
    }





    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;

        Float[] b1 = new Float [data_bar1.size()];
        Float[] b2 = new Float [data_bar2.size()];
        Float[] b3 = new Float [data_bar3.size()];
        b1 = data_bar1.toArray(b1);
        b2 = data_bar2.toArray(b2);
        b3 = data_bar3.toArray(b3);
        float[] bar1 = ArrayUtils.toPrimitive(b1);
        float[] bar2 = ArrayUtils.toPrimitive(b2);
        float[] bar3 = ArrayUtils.toPrimitive(b3);

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        ArrayList<BarEntry> valueSet3 = new ArrayList<>();

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

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Cancelled Calls");
        //barDataSet2.setValueTextColor(Color.WHITE);
        barDataSet2.setColors(colors2);


        for(int dd=0 ;dd < loop; dd++){
            BarEntry v1e3 = new BarEntry(bar3[dd], dd);
            valueSet3.add(v1e3);

        }

        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Not Completed Calls");
        //barDataSet3.setValueTextColor(Color.WHITE);
        barDataSet3.setColors(colors3);



        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        xAxis = new ArrayList<>();
        xAxis.add("Administrator");
        xAxis.add("Hesham Ali");
        xAxis.add("Racha Attia");
        xAxis.add("Said YAHIA");
        xAxis.add("Wafaa Mansour");
        xAxis.add("Ahmed Bakr");
        xAxis.add("Alaa Mohamed");
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
                    JSONArray json1 = jParser.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Outgoing_Calls_Agent, "POST", params, "ROLES");

                    Log.d("outgoing Agent: ", json1.toString());
                    System.out.println("outgoing Agent: "+ json1.toString());

                    data_bar1.clear();
                    data_bar2.clear();
                    data_bar3.clear();

                    JSONObject c = json1.getJSONObject(0);
                    int max1 = Integer.parseInt(c.getString("no_rows"));
                    loop = max1;
                    for (int i = 1; i <= max1; i++) {

                        c = json1.getJSONObject(i);

                        completed_calls = c.getString("completed_calls");
                        cancelled_calls = c.getString("cancelled_calls");
                        Not_completed_calls = c.getString("not_completed_calls");
                        Agent_Name = c.getString("Agent_Name");
                        //abandoned_calls = c.getString("abandoned_calls");
                        data_bar1.add(Float.parseFloat(completed_calls));
                        data_bar2.add(Float.parseFloat(cancelled_calls));
                        data_bar3.add(Float.parseFloat(Not_completed_calls));
                        xAxis.add(Agent_Name);
                    }

                } else {
                    List<NameValuePair> params_Agents = new ArrayList<NameValuePair>();
                    JSONArray json_agents = jParser_agents.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Agents_Data_List, "GET", params_Agents, "ROLES");
                    Log.d("Agent list: ", json_agents.toString());
                    System.out.println("Agent list: "+ json_agents.toString());
                    params.clear();
                    xAxis.clear();
                    params.add(new BasicNameValuePair("FromDate", "2013-10-1"));
                    params.add(new BasicNameValuePair("ToDate", "2016-6-13"));

                    JSONObject ccc = json_agents.getJSONObject(0);
                    int max3 = Integer.parseInt(ccc.getString("no_row"));
                    for (int i = 1; i <= max3; i++) {
                        ccc = json_agents.getJSONObject(i);
                        String Agent_id = ccc.getString("Agent_id");
                        params.add(new BasicNameValuePair("Select1[]", Agent_id));

                    }
                    JSONArray json2 = jParser2.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Outgoing_Calls_Agent, "POST", params, "ROLES");
                    Log.d("outgoing Agent: ", json2.toString());
                    System.out.println("outgoing Agent: "+ json2.toString());
                    data_bar1.clear();
                    data_bar2.clear();
                    data_bar3.clear();

                    JSONObject c2 = json2.getJSONObject(0);
                    int max2 = Integer.parseInt(c2.getString("no_rows"));
                    loop = max2;
                    for (int i = 1; i <= max2; i++) {

                        c2 = json2.getJSONObject(i);

                        completed_calls = c2.getString("completed_calls");
                        cancelled_calls = c2.getString("cancelled_calls");
                        Not_completed_calls = c2.getString("not_completed_calls");
                        Agent_Name = c2.getString("Agent_Name");
                        //abandoned_calls = c.getString("abandoned_calls");
                        data_bar1.add(Float.parseFloat(completed_calls));
                        data_bar2.add(Float.parseFloat(cancelled_calls));
                        data_bar3.add(Float.parseFloat(Not_completed_calls));
                        xAxis.add(Agent_Name);
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

                Outgoing_Agent_Calls_chart.setData(data);
               // Outgoing_Agent_Calls_chart.getXAxis().setTextColor(Color.WHITE);
               // Outgoing_Agent_Calls_chart.getLegend().setTextColor(Color.WHITE);
                Outgoing_Agent_Calls_chart.setDrawValueAboveBar(true);
                Outgoing_Agent_Calls_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                Outgoing_Agent_Calls_chart.setDescription("");
                Outgoing_Agent_Calls_chart.animateXY(2000, 2000);
                Outgoing_Agent_Calls_chart.invalidate();

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
//
//                    }
//                });
//
//                AlertDialog alertD = alertDialog.create();
//                alertD.show();

            }


        }
    }

    public OutgoingAgentCalls_BarChart (){}
    public static OutgoingAgentCalls_BarChart newInstance(String[] ids,String[] names){
        OutgoingAgentCalls_BarChart outgoingAgentCallsBarChart = new OutgoingAgentCalls_BarChart();
        final Bundle args = new Bundle(2);
        args.putStringArray("IDS",ids);
        args.putStringArray("NAMES",names);
        outgoingAgentCallsBarChart.setArguments(args);
        return outgoingAgentCallsBarChart;
    }
}