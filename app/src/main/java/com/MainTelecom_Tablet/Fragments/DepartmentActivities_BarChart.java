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
 * Created by MOHAMED on 12/04/2016.
 */
public class DepartmentActivities_BarChart extends Fragment {

    BarChart chart;


    private ProgressDialog pDialog;

    private JSONParser jParser_Roles = new JSONParser();
    private JSONParser jParser_Department1 = new JSONParser();
    private JSONParser jParser_Department2 = new JSONParser();

    private int Year ,Month , Day;


    private SwipeRefreshLayout swipeContainer;

    String RoleName;
    private String EventsPlanned  ;
    private String TasksPlanned ;
    private String TicketsPlanned  ;
    private String EnhancementPlanned  ;
    private String EventsCompleted ;
    private String TasksCompleted ;
    private String TicketsCompleted ;
    private String EnhancementsCompleted ;
    ArrayList<String> xAxis = new ArrayList<String>();
    ArrayList<Float> data_bar1 = new ArrayList<Float>();
    ArrayList<Float> data_bar2 = new ArrayList<Float>();

    int[] colors1 = new int[]{Color.rgb(69, 144, 166),Color.rgb(169,70,67),Color.rgb(136,164,78),Color.rgb(127,105,154),Color.rgb(237,237,237)};
    int[] colors2 = new int[]{Color.rgb(61,149,173),Color.rgb(218,131,61),Color.rgb(145,167,204),Color.rgb(163,125,124),Color.rgb(237,237,237)};

    List<NameValuePair> params_department = new ArrayList<NameValuePair>();
    String[] params_data;
    String[] xAxis_data;
    int search_check = 0;
    int loop = 0;

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

        View Dep_activities = inflater.inflate(R.layout.department_activities_barchart, container, false);

        chart = (BarChart) Dep_activities.findViewById(R.id.chart);

        /** get currnt date from calender*/
        final Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH);
        Day = c.get(Calendar.DAY_OF_MONTH);

        // Loading products in Background Thread
        new LoadAllData().execute();


        return Dep_activities;
    }





    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;

        int count1 =0;
        int count2 =0;

        Float[] b1 = new Float [data_bar1.size()];
        Float[] b2 = new Float [data_bar2.size()];
        b1 = data_bar1.toArray(b1);
        b2 = data_bar2.toArray(b2);
        float[] bar1 = ArrayUtils.toPrimitive(b1);
        float[] bar2 = ArrayUtils.toPrimitive(b2);

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();

        for(int j=0 ;j < (loop)*5; j+=5) {
                BarEntry v1e1 = new BarEntry(new float[]{bar1[j], bar1[j+1], bar1[j+2],bar1[j+3],bar1[j+4]}, count1);
                valueSet1.add(v1e1);
                count1++;

        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "");
        //barDataSet1.setValueTextColor(Color.WHITE);
        barDataSet1.setColors(colors1);
        barDataSet1.setStackLabels(new String[]{"Events Planned","Tasks Planned","Tickets Planned","Enhancements Planned",""});


        for(int d=0 ;d < (loop)*5; d+=5){
                BarEntry v1e2 = new BarEntry(new float[]{bar2[d], bar2[d+1], bar2[d+2],bar2[d+3],bar2[d+4]}, count2);
                valueSet2.add(v1e2);
                count2++;


        }

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "");
        //barDataSet2.setValueTextColor(Color.WHITE);
        barDataSet2.setColors(colors2);
        barDataSet2.setStackLabels(new String[]{"Events Completed", "Tasks Completed","Tickets Completed","Enhancements Completed",""});


        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("August");
        xAxis.add("Spet");
        xAxis.add("Octoer");
        xAxis.add("Nov");
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

                if (search_check==1){
                    params_department.clear();
                    data_bar1.clear();
                    data_bar2.clear();
                    params_department.add(new BasicNameValuePair("FromDate",params_data[0]));
                    params_department.add(new BasicNameValuePair("ToDate",params_data[1]));
                    if(!params_data[2].equalsIgnoreCase("")) {
                        params_department.add(new BasicNameValuePair("RoleID[]", params_data[2]));
                    }
                    JSONArray json_department = jParser_Department1.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_department_activity, "POST", params_department, "DEPARTMENT");
                    Log.d("department Activities: ", json_department.toString());
                    System.out.println("department Activities: "+ json_department.toString());

                    JSONObject c = json_department.getJSONObject(0);
                    int max1 = Integer.parseInt(c.getString("no_rows"));
                    loop = max1;
                    for (int i = 1; i <= max1; i++) {
                        c = json_department.getJSONObject(i);
                        EventsPlanned = c.getString("Events_Planned");
                        TasksPlanned = c.getString("Tasks_Planned");
                        TicketsPlanned = c.getString("Tickets_Planned");
                        EnhancementPlanned = c.getString("Enhancements_Planned");

                        EventsCompleted = c.getString("Events_Completed");
                        TasksCompleted = c.getString("Tasks_Completed");
                        TicketsCompleted = c.getString("Tickets_Completed");
                        EnhancementsCompleted = c.getString("Enhancements_Completed");
                        RoleName = c.getString("Role_Name");
                        data_bar1.add(Float.parseFloat(EventsPlanned));
                        data_bar1.add(Float.parseFloat(TasksPlanned));
                        data_bar1.add(Float.parseFloat(TicketsPlanned));
                        data_bar1.add(Float.parseFloat(EnhancementPlanned));
                        data_bar1.add(0.00000001f);

                        data_bar2.add(Float.parseFloat(EventsCompleted));
                        data_bar2.add(Float.parseFloat(TasksCompleted));
                        data_bar2.add(Float.parseFloat(TicketsCompleted));
                        data_bar2.add(Float.parseFloat(EnhancementsCompleted));
                        data_bar2.add(0.00000001f);
                        xAxis.add(RoleName);
                    }
                }else {
                    params_department.clear();
                    data_bar1.clear();
                    data_bar2.clear();
                    params_department.add(new BasicNameValuePair("FromDate", 2015+"-"+(Month+1)+"-"+"1"));
                    params_department.add(new BasicNameValuePair("ToDate", Year+"-"+(Month+1)+"-"+"28"));

                    // getting JSON string from URL
                    //JSONArray json_roles = jParser_Roles.makeHttpRequest("http://"+ SaveSharedPreference.getUserIP(getActivity())+ AppConfig.url_roles, "POST",params_roles,"ROLES");
                    JSONArray json_department = jParser_Department2.makeHttpRequest("http://" + SaveSharedPreference.getUserIP(getActivity()) + AppConfig.url_department_activity, "POST", params_department, "DEPARTMENT");

                    // Check your log cat for JSON reponse
                    //Log.d("All Data: ", json_roles.toString());
                    Log.d("department Activities: ", json_department.toString());
                    System.out.println("department Activities: "+ json_department.toString());

                    JSONObject c = json_department.getJSONObject(0);
                    int max1 = Integer.parseInt(c.getString("no_rows"));
                    loop = max1;
                    for (int i = 1; i <= max1; i++) {
                        c = json_department.getJSONObject(i);
                        EventsPlanned = c.getString("Events_Planned");
                        TasksPlanned = c.getString("Tasks_Planned");
                        TicketsPlanned = c.getString("Tickets_Planned");
                        EnhancementPlanned = c.getString("Enhancements_Planned");

                        EventsCompleted = c.getString("Events_Completed");
                        TasksCompleted = c.getString("Tasks_Completed");
                        TicketsCompleted = c.getString("Tickets_Completed");
                        EnhancementsCompleted = c.getString("Enhancements_Completed");
                        RoleName = c.getString("Role_Name");
                        data_bar1.add(Float.parseFloat(EventsPlanned));
                        data_bar1.add(Float.parseFloat(TasksPlanned));
                        data_bar1.add(Float.parseFloat(TicketsPlanned));
                        data_bar1.add(Float.parseFloat(EnhancementPlanned));
                        data_bar1.add(0.00000001f);

                        data_bar2.add(Float.parseFloat(EventsCompleted));
                        data_bar2.add(Float.parseFloat(TasksCompleted));
                        data_bar2.add(Float.parseFloat(TicketsCompleted));
                        data_bar2.add(Float.parseFloat(EnhancementsCompleted));
                        data_bar2.add(0.00000001f);
                        xAxis.add(RoleName);
                    }
                }
//

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

                //data.setHighlightEnabled(true);

            /*Legend l = chart.getLegend();
            l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
            l.setFormSize(8f);
            l.setFormToTextSpace(4f);
            l.setXEntrySpace(6f);*/


                data.setValueFormatter(new StackedValueFormatter(false, "||||", 1));

                chart.setData(data);
                //chart.getXAxis().setTextColor(Color.WHITE);
                //chart.getLegend().setTextColor(Color.WHITE);

                //chart.setDrawValuesForWholeStack(false);
                chart.setDrawValueAboveBar(true);

                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.setDescription("");

                chart.animateXY(2000, 2000);

                chart.invalidate();
            }catch (Exception e){

//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                alertDialog.setTitle("Error...");
//                alertDialog.setMessage("Check Your Connection...."+e);
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

    public DepartmentActivities_BarChart(){}
    public static DepartmentActivities_BarChart newInstance(String[]ids,String[]names){

        DepartmentActivities_BarChart departmentActivitiesBarChart = new DepartmentActivities_BarChart();
        final Bundle args = new Bundle(2);
        args.putStringArray("IDS",ids);
        args.putStringArray("NAMES",names);
        departmentActivitiesBarChart.setArguments(args);
        return departmentActivitiesBarChart;

    }

}


