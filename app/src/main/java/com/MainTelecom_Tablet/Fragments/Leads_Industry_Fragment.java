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
import java.util.Calendar;
import java.util.List;



/**
 * Created by MOHAMED on 30/04/2016.
 */
public class Leads_Industry_Fragment extends Fragment {
    BarChart Leads_Industry_chart;
    private int Year ,Month , Day;

    private ProgressDialog pDialog;

    private JSONParser jParser = new JSONParser();
    private JSONParser jParser2 = new JSONParser();
    private JSONParser jParser_industry = new JSONParser();

    String RoleName;
    private String All_leads ;
    private String Interested_leads ;
    private String Industry_Name ;
    ArrayList<String> xAxis = new ArrayList<String>();
    ArrayList<Float> data_bar1 = new ArrayList<Float>();
    ArrayList<Float> data_bar2 = new ArrayList<Float>();

    int[] colors1 = new int[]{Color.rgb(69, 144, 166)};
    int[] colors2 = new int[]{Color.rgb(169,70,67)};

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

        View leads_industry = inflater.inflate(R.layout.leads_industry_layout, container, false);

        Leads_Industry_chart = (BarChart) leads_industry.findViewById(R.id.leads_industry_chart);

        /** get currnt date from calender*/
        final Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH);
        Day = c.get(Calendar.DAY_OF_MONTH);

        // Loading products in Background Thread
        new LoadAllData().execute();


        return leads_industry;
    }





    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;

        Float[] b1 = new Float [data_bar1.size()];
        Float[] b2 = new Float [data_bar2.size()];
        b1 = data_bar1.toArray(b1);
        b2 = data_bar2.toArray(b2);
        float[] bar1 = ArrayUtils.toPrimitive(b1);
        float[] bar2 = ArrayUtils.toPrimitive(b2);

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();

        for(int j=0 ;j < loop; j++) {
            BarEntry v1e1 = new BarEntry(bar1[j], j);
            valueSet1.add(v1e1);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1,"All Leads");
        //barDataSet1.setValueTextColor(Color.WHITE);
        barDataSet1.setColors(colors1);


        for(int d=0 ;d < loop; d++){
            BarEntry v1e2 = new BarEntry(bar2[d], d);
            valueSet2.add(v1e2);

        }

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Interested Leads");
        //barDataSet2.setValueTextColor(Color.WHITE);
        barDataSet2.setColors(colors2);


        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("--None--");
        xAxis.add("Media");
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
                    if (!params_data[2].equalsIgnoreCase("")) {
                        for (int j = 2; j < params_data.length; j++) {
                            params.add(new BasicNameValuePair("Select1[]", params_data[j]));
                        }
                    }
                    JSONArray json1 = jParser.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Leads_Industry, "POST", params, "ROLES");

                    Log.d("All Data: ", json1.toString());
                    System.out.println("Leads Industry :"+json1.toString());

                    data_bar1.clear();
                    data_bar2.clear();

                    JSONObject c = json1.getJSONObject(0);
                    int max1 = Integer.parseInt(c.getString("no_rows"));
                    loop = max1;
                    for (int i = 1; i <= max1; i++) {

                        c = json1.getJSONObject(i);

                        All_leads = c.getString("All_Leads");
                        Interested_leads = c.getString("Interested_Leads");
                        Industry_Name = c.getString("Industry");

                        data_bar1.add(Float.parseFloat(All_leads));
                        data_bar2.add(Float.parseFloat(Interested_leads));

                        xAxis.add(Industry_Name);
                    }
                } else {
                    //List<NameValuePair> params_industry = new ArrayList<NameValuePair>();
                    //JSONArray json_industry = jParser_industry.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Industry_Data_List, "GET", params_industry, "ROLES");
                    //Log.d("All Data: ", json_industry.toString());
                    //System.out.println("Industries :"+json_industry.toString());
                    params.clear();
                    //params.add(new BasicNameValuePair("FromDate", 2013+"-"+(Month+1)+"-"+"1"));
                    params.add(new BasicNameValuePair("FromDate", "2016-1-1"));
                    params.add(new BasicNameValuePair("ToDate", "2016-7-1"));
                    //params.add(new BasicNameValuePair("Select1[]", "Construction"));
                    //params.add(new BasicNameValuePair("Select1[]", "Hotels"));

                    //JSONObject ccc = json_industry.getJSONObject(0);
                    //int max3 = Integer.parseInt(ccc.getString("no_rows"));
                    //for (int i = 1; i <= max3; i++) {
                    //    ccc = json_industry.getJSONObject(i);
                    //    String Industry = ccc.getString("Industry");
                    //    params.add(new BasicNameValuePair("", Industry));

                    //}
                    JSONArray json2 = jParser2.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Leads_Industry, "POST", params, "ROLES");
                    Log.d("All Data: ", json2.toString());
                    System.out.println("Leads Industry :"+json2.toString());
                    data_bar1.clear();
                    data_bar2.clear();
                    xAxis.clear();

                    JSONObject c2 = json2.getJSONObject(0);
                    int max2 = Integer.parseInt(c2.getString("no_rows"));
                    loop = max2;
                    for (int i = 1; i <= max2; i++) {

                        c2 = json2.getJSONObject(i);

                        All_leads = c2.getString("All_Leads");
                        Interested_leads = c2.getString("Interested_Leads");
                        Industry_Name = c2.getString("Industry");

                        data_bar1.add(Float.parseFloat(All_leads));
                        data_bar2.add(Float.parseFloat(Interested_leads));

                        xAxis.add(Industry_Name);
                    }
                }
//

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

                Leads_Industry_chart.setData(data);
          //      Leads_Industry_chart.getLegend().setTextColor(Color.WHITE);
            //    Leads_Industry_chart.getXAxis().setTextColor(Color.WHITE);

                //chart.setDrawValuesForWholeStack(false);
                Leads_Industry_chart.setDrawValueAboveBar(true);

                Leads_Industry_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                Leads_Industry_chart.setDescription("");

                Leads_Industry_chart.animateXY(2000, 2000);

                Leads_Industry_chart.invalidate();
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

    public Leads_Industry_Fragment(){}
    public static Leads_Industry_Fragment newInstance(String[]ids,String[]names){

        Leads_Industry_Fragment leadsIndustryFragment = new Leads_Industry_Fragment();
        final Bundle args = new Bundle(2);
        args.putStringArray("IDS",ids);
        args.putStringArray("NAMES",names);
        leadsIndustryFragment.setArguments(args);
        return leadsIndustryFragment;

    }
}

