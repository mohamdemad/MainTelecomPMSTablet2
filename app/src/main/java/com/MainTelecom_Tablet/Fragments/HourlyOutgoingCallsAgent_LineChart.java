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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by MOHAMED on 02/05/2016.
 */
public class HourlyOutgoingCallsAgent_LineChart extends Fragment {

    LineChart Outgoing_Agent_Calls_chart;

    private int Year ,Month , Day;

    private ProgressDialog pDialog;
    private JSONParser jParser = new JSONParser();
    private JSONParser jParser_agents = new JSONParser();

    String RoleName;
    private String completed_calls ;
    private String cancelled_calls ;
    private String Not_completed_calls ;
    private String Hour ;
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

        View Outgoing_Agent_Calls = inflater.inflate(R.layout.hourly_outgoing_calls_agent, container, false);

        Outgoing_Agent_Calls_chart = (LineChart) Outgoing_Agent_Calls.findViewById(R.id.hourly_outgoing_calls_agent_chart);

        /** get currnt date from calender*/
        final Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH);
        Day = c.get(Calendar.DAY_OF_MONTH);
        // Loading products in Background Thread
        new LoadAllData().execute();


        return Outgoing_Agent_Calls;
    }





    private ArrayList<ILineDataSet> getDataSet() {
        ArrayList<ILineDataSet> dataSets = null;


        Float[] b1 = new Float [data_bar1.size()];
        Float[] b2 = new Float [data_bar2.size()];
        Float[] b3 = new Float [data_bar3.size()];
        b1 = data_bar1.toArray(b1);
        b2 = data_bar2.toArray(b2);
        b3 = data_bar3.toArray(b3);
        float[] bar1 = ArrayUtils.toPrimitive(b1);
        float[] bar2 = ArrayUtils.toPrimitive(b2);
        float[] bar3 = ArrayUtils.toPrimitive(b3);

        ArrayList<Entry> valueSet1 = new ArrayList<>();
        ArrayList<Entry> valueSet2 = new ArrayList<>();
        ArrayList<Entry> valueSet3 = new ArrayList<>();

        for(int j=0 ;j < loop; j++) {
            Entry v1e1 = new BarEntry(bar1[j], j);
            valueSet1.add(v1e1);
        }

        LineDataSet lineDataSet1 = new LineDataSet(valueSet1,"Completed Calls");
        //lineDataSet1.setValueTextColor(Color.WHITE);
        lineDataSet1.setColors(colors1);


        for(int d=0 ;d < loop; d++){
            Entry v1e2 = new Entry(bar2[d], d);
            valueSet2.add(v1e2);

        }

        LineDataSet lineDataSet2 = new LineDataSet(valueSet2, "Cancelled Calls");
        //lineDataSet2.setValueTextColor(Color.WHITE);
        lineDataSet2.setColors(colors2);


        for(int dd=0 ;dd < loop; dd++){
            Entry v1e3 = new Entry(bar3[dd], dd);
            valueSet3.add(v1e3);

        }

        LineDataSet lineDataSet3 = new LineDataSet(valueSet3, "Not Completed Calls");
        //lineDataSet3.setValueTextColor(Color.WHITE);
        lineDataSet3.setColors(colors3);



        dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        dataSets.add(lineDataSet3);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("0.00");
        xAxis.add("1.00");
        xAxis.add("2.00");
        xAxis.add("3.00");
        xAxis.add("4.00");
        xAxis.add("5.00");
        xAxis.add("6.00");
        xAxis.add("7.00");
        xAxis.add("8.00");
        xAxis.add("9.00");
        xAxis.add("10.00");
        xAxis.add("11.00");
        xAxis.add("12.00");
        xAxis.add("13.00");
        xAxis.add("14.00");
        xAxis.add("15.00");
        xAxis.add("16.00");
        xAxis.add("17.00");
        xAxis.add("18.00");
        xAxis.add("19.00");
        xAxis.add("20.00");
        xAxis.add("21.00");
        xAxis.add("22.00");
        xAxis.add("23.00");
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

//               // params.add(new BasicNameValuePair("Select1[]","49604"));
                try {

                    List<NameValuePair> params = new ArrayList<NameValuePair>();

                    if (search_check == 1) {
                        params.clear();
                        params.add(new BasicNameValuePair("FromDate", params_data[0]));
                        params.add(new BasicNameValuePair("ToDate", params_data[1]));
                        for (int j = 2; j < params_data.length; j++) {
                            params.add(new BasicNameValuePair("Select1[]", params_data[j]));
                        }
                        JSONArray json1 = jParser.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Hourly_Outgoing_Calls_Agent, "POST", params, "ROLES");

                        Log.d("All Data: ", json1.toString());
                        System.out.println("H.O.A :"+json1.toString());

                        data_bar1.clear();
                        data_bar2.clear();
                        data_bar3.clear();
                        xAxis.clear();
                        loop= json1.length();
                        for (int i = 0; i < json1.length(); i++) {

                            JSONObject c = json1.getJSONObject(i);

                            completed_calls = c.getString("completed_calls");
                            Not_completed_calls = c.getString("not_completed_calls");
                            cancelled_calls = c.getString("cancelled_calls");
                            Hour = c.getString("Hour");
                            //abandoned_calls = c.getString("abandoned_calls");
                            data_bar1.add(Float.parseFloat(completed_calls));
                            data_bar2.add(Float.parseFloat(cancelled_calls));
                            data_bar3.add(Float.parseFloat(Not_completed_calls));
                            xAxis.add(Hour);
                        }
                    } else {
                        List<NameValuePair> params_Agents = new ArrayList<NameValuePair>();
                        JSONArray json_agents = jParser_agents.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Agents_Data_List, "GET", params_Agents, "ROLES");
                        Log.d("All Data: ", json_agents.toString());
                        System.out.println("A.L :"+json_agents.toString());
                        params.clear();
                        params.add(new BasicNameValuePair("FromDate", "2013-10-1"));
                        params.add(new BasicNameValuePair("ToDate", "2016-6-13"));

                        JSONObject ccc = json_agents.getJSONObject(0);
                        int max3 = Integer.parseInt(ccc.getString("no_row"));
                        for (int i = 1; i <= max3; i++) {
                            ccc = json_agents.getJSONObject(i);
                            String Agent_id = ccc.getString("Agent_id");
                            params.add(new BasicNameValuePair("Select1[]", Agent_id));

                        }
                        JSONArray json2 = jParser.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Hourly_Outgoing_Calls_Agent, "POST", params, "ROLES");
                        Log.d("All Data: ", json2.toString());
                        System.out.println("H.O.A :"+json2.toString());
                        data_bar1.clear();
                        data_bar2.clear();
                        data_bar3.clear();
                        xAxis.clear();
                        loop = json2.length();
                        for (int i = 0; i < json2.length(); i++) {

                            JSONObject c2 = json2.getJSONObject(i);

                            completed_calls = c2.getString("completed_calls");
                            cancelled_calls = c2.getString("cancelled_calls");
                            Not_completed_calls = c2.getString("not_completed_calls");
                            Hour = c2.getString("Hour");
                            //abandoned_calls = c.getString("abandoned_calls");
                            data_bar1.add(Float.parseFloat(completed_calls));
                            data_bar2.add(Float.parseFloat(cancelled_calls));
                            data_bar3.add(Float.parseFloat(Not_completed_calls));
                            xAxis.add(Hour);
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
                LineData data = new LineData(xAxis, getDataSet());
                data.setHighlightEnabled(true);

//            Legend l = chart.getLegend();
//            l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
//            l.setFormSize(8f);
//            l.setFormToTextSpace(4f);
//            l.setXEntrySpace(6f);

                Outgoing_Agent_Calls_chart.setData(data);
             //   Outgoing_Agent_Calls_chart.getXAxis().setTextColor(Color.WHITE);
              //  Outgoing_Agent_Calls_chart.getLegend().setTextColor(Color.WHITE);
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

    public HourlyOutgoingCallsAgent_LineChart (){}
    public static HourlyOutgoingCallsAgent_LineChart newInstance(String[] ids,String[] names){
        HourlyOutgoingCallsAgent_LineChart hourlyOutgoingCallsAgentLineChart = new HourlyOutgoingCallsAgent_LineChart();
        final Bundle args = new Bundle(2);
        args.putStringArray("IDS",ids);
        args.putStringArray("NAMES",names);
        hourlyOutgoingCallsAgentLineChart.setArguments(args);
        return hourlyOutgoingCallsAgentLineChart;
    }
}
