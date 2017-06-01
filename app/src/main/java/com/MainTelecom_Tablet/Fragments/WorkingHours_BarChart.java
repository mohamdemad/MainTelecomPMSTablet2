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
import com.MainTelecom_Tablet.Formatter.StackedValueFormatter;
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
public class WorkingHours_BarChart extends Fragment {

    BarChart Working_Hours_chart;
    private int Year ,Month , Day;

    private ProgressDialog pDialog;

    private JSONParser jParser = new JSONParser();
    private JSONParser jParser2 = new JSONParser();
    int Len;


    private String not_allocated_hours;
    private String allocated_hours;
    private String EmployeeName;
    ArrayList<String> xAxis = new ArrayList<String>();
    ArrayList<Float> data_bar1 = new ArrayList<Float>();

    int[] colors1 = new int[]{Color.rgb(169,70,67),Color.rgb(69, 144, 166),Color.rgb(237,237,237)};
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

        View working_hours = inflater.inflate(R.layout.working_hours_layout, container, false);

        Working_Hours_chart = (BarChart) working_hours.findViewById(R.id.working_hours_chart);

        /** get currnt date from calender*/
        final Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH);
        Day = c.get(Calendar.DAY_OF_MONTH);
        // Loading products in Background Thread
        new LoadAllData().execute();


        return working_hours;
    }





    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;
        int iii =0;

        Float[] b1 = new Float [data_bar1.size()];
        b1 = data_bar1.toArray(b1);
        float[] bar1 = ArrayUtils.toPrimitive(b1);

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        for(int j=0 ;j < (loop)*2; j+=2) {
            BarEntry v1e1 = new BarEntry(new float[]{bar1[j],bar1[j+1],0.00000001f}, iii);
            valueSet1.add(v1e1);
            iii++;
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1,"");
        //barDataSet1.setValueTextColor(Color.WHITE);
        barDataSet1.setStackLabels(new String[]{"Allocated Hours","Not Allocated Hours",""});
        barDataSet1.setColors(colors1);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Administrator");
        xAxis.add("Hesham  Ali");
        xAxis.add("Racha Attia");
        xAxis.add("Said YAHIA");
        xAxis.add("Wafaa Mansour");
        xAxis.add("Ahmed Bakr");
        xAxis.add("Alaa Mohamed");
        xAxis.add("Ehab Ali");
        xAxis.add("Hatem Abdelsalam");
        xAxis.add("Amr Salah");
        xAxis.add("Asmaa Mohie");
        xAxis.add("Kamelya Elmallah");
        xAxis.add("Moataz");
        xAxis.add("M.Emad");
        xAxis.add("Amr Hossam");
        xAxis.add("Ahmed Esmael");
        xAxis.add("Mohamed Yehia");
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

                    JSONArray json1 = jParser.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Working_Hours, "POST", params, "ROLES");

                    Log.d("Working Hours: ", json1.toString());
                    System.out.println("Working Hours :"+json1.toString());

                    data_bar1.clear();

                    JSONObject c = json1.getJSONObject(0);
                    int max1 = Integer.parseInt(c.getString("no_rows"));
                    loop = max1;
                    for (int i = 1; i <= max1; i++) {

                        c = json1.getJSONObject(i);

                        not_allocated_hours= c.getString("not_allocated_hours");
                        allocated_hours = c.getString("allocated_hours");
                        EmployeeName = c.getString("employee_name");

                        data_bar1.add(Float.parseFloat(allocated_hours));
                        data_bar1.add(Float.parseFloat(not_allocated_hours));

                        xAxis.add(EmployeeName);
                    }
                } else {
                    //List<NameValuePair> params_industry = new ArrayList<NameValuePair>();
                    //JSONArray json_industry = jParser_industry.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Industry_Data_List, "GET", params_industry, "ROLES");
                    //Log.d("All Data: ", json_industry.toString());
                    //System.out.println("Industries :"+json_industry.toString());
                    params.clear();
                    //params.add(new BasicNameValuePair("FromDate", Year+"-"+(Month+1)+"-"+"1"));
                    //params.add(new BasicNameValuePair("ToDate", Year+"-"+(Month+1)+"-"+"28"));

                    //JSONObject ccc = json_industry.getJSONObject(0);
                    //int max3 = Integer.parseInt(ccc.getString("no_rows"));
                    //for (int i = 1; i <= max3; i++) {
                    //    ccc = json_industry.getJSONObject(i);
                    //    String Industry = ccc.getString("Industry");
                    //    params.add(new BasicNameValuePair("", Industry));

                    //}
                    JSONArray json2 = jParser2.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_Working_Hours, "POST", params, "ROLES");
                    Log.d("Working hours: ", json2.toString());
                    System.out.println("Working hours :"+json2.toString());
                    data_bar1.clear();
                    xAxis.clear();

                    JSONObject c2 = json2.getJSONObject(0);
                    int max2 = Integer.parseInt(c2.getString("no_rows"));
                    loop = max2;
                    for (int i = 1; i <= max2; i++) {

                        c2 = json2.getJSONObject(i);

                        not_allocated_hours= c2.getString("not_allocated_hours");
                        allocated_hours = c2.getString("allocated_hours");
                        EmployeeName = c2.getString("employee_name");

                        data_bar1.add(Float.parseFloat(allocated_hours));
                        data_bar1.add(Float.parseFloat(not_allocated_hours));

                        xAxis.add(EmployeeName);
                    }
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
                BarData data = new BarData(xAxis, getDataSet());

                data.setHighlightEnabled(true);


            /*Legend l = chart.getLegend();
            l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
            l.setFormSize(8f);
            l.setFormToTextSpace(4f);
            l.setXEntrySpace(6f);*/
                data.setValueFormatter(new StackedValueFormatter(false, "||||", 1));
                Working_Hours_chart.setData(data);
          //      Working_Hours_chart.getXAxis().setTextColor(Color.WHITE);
            //    Working_Hours_chart.getLegend().setTextColor(Color.WHITE);

                //chart.setDrawValuesForWholeStack(false);
                Working_Hours_chart.setDrawValueAboveBar(true);

                Working_Hours_chart.setDescription("");
                Working_Hours_chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

                Working_Hours_chart.animateXY(2000, 2000);

                Working_Hours_chart.invalidate();
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
    public WorkingHours_BarChart(){}
    public static WorkingHours_BarChart newInstance(String[]ids,String[]names){

        WorkingHours_BarChart workingHoursBarChart = new WorkingHours_BarChart();
        final Bundle args = new Bundle(2);
        args.putStringArray("IDS",ids);
        args.putStringArray("NAMES",names);
        workingHoursBarChart.setArguments(args);
        return workingHoursBarChart;

    }

}

