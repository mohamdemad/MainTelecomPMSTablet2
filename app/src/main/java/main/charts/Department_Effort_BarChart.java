package main.charts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.MainTelecom_Tablet.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import main.activities.Home_Team;
import main.activities.Login;


/**
 * Created by MOHAMED on 06/02/2016.
 */
public class Department_Effort_BarChart extends Fragment {

    private BarChart chart;
    Handler handler;
    private ArrayList<String> role_name_array=new ArrayList<String>();

    private ArrayList<Float> events_planned_array=new ArrayList<Float>();
    private ArrayList<Float> tasks_planned_array=new ArrayList<Float>();
    private ArrayList<Float> tickets_planned_array=new ArrayList<Float>();
    private ArrayList<Float> enhancements_planned_array=new ArrayList<Float>();

    private ArrayList<Float> events_completed_array=new ArrayList<Float>();
    private ArrayList<Float> tasks_completed_array=new ArrayList<Float>();
    private ArrayList<Float> tickets_completed_array=new ArrayList<Float>();
    private ArrayList<Float> enhancements_completed_array=new ArrayList<Float>();
    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();

    int[] colors1 = new int[]{Color.rgb(69,144,166),Color.rgb(169,70,67),Color.rgb(136,164,78),Color.rgb(127,105,154),Color.rgb(237,237,237)};
    int[] colors2 = new int[]{Color.rgb(61,149,173),Color.rgb(218,131,61),Color.rgb(145,167,204),Color.rgb(163,125,124),Color.rgb(237,237,237)};


    private int con_checker;
    String location;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


    View fragment_barchart_department_activities = inflater.inflate(
            R.layout.barchart_working_hours_layout, container, false);

    location = getArguments().getString("location");

    String url="http://"+ Login.enteredIP+"/Android/Team/Department_Effort.php";


         role_name_array=new ArrayList<String>();

         events_planned_array.clear();
         tasks_planned_array.clear();
         tickets_planned_array.clear();
         enhancements_planned_array.clear();

         events_completed_array.clear();
         tasks_completed_array.clear();
         tickets_completed_array.clear();
        enhancements_completed_array.clear();
         dataSets.clear();


        // Loading products in Background Thread
        Department_Activities_Connection department_activities_connection=new Department_Activities_Connection(getActivity());
        department_activities_connection.execute(url);

        chart = (BarChart) fragment_barchart_department_activities.findViewById(R.id.chart_working_hours);


        return fragment_barchart_department_activities;
    }

    private void barchart() {



        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(2500, 2500);
        chart.setDrawGridBackground(false);
        //chart.setDrawValuesForWholeStack(true);
        chart.setDrawValueAboveBar(false);



        chart.setOnTouchListener(new View.OnTouchListener() {

            final ArrayList list = new ArrayList();


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction()==MotionEvent.ACTION_UP){

                    long eventDuration = event.getEventTime() - event.getDownTime();
                    if(eventDuration>=1000) {
                        list.clear();
                        list.add("Working Hour");
                        list.add("Today ");
                        list.add("Department Activities");
                        list.add("Department Effort");
                        Home_Team.charts_dialog(getActivity(), location, list);

                    }}

                return false;
            }
        });

        chart.invalidate();

        //handle click on chart selected
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // TODO Auto-generated method stub

                BarEntry entry = (BarEntry) e;

                if (entry.getVals() != null) {
                    Toast toast= Toast.makeText(getActivity(),
                            entry.getVals()[h.getStackIndex()]+"", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }else {
                    Toast toast= Toast.makeText(getActivity(),
                            entry.getVals()[h.getStackIndex()]+"", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }

            }

            @Override
            public void onNothingSelected() {
                // TODO Auto-generated method stub

            }
        });


    }

    private ArrayList<IBarDataSet> getDataSet() {


        ArrayList<BarEntry> values_plan = new ArrayList<BarEntry>();
        ArrayList<BarEntry> values_comp = new ArrayList<BarEntry>();





        for (int j = 0; j < role_name_array.size(); j++) {

            BarEntry value_planned = new BarEntry(new float[] { tasks_planned_array.get(j), events_planned_array.get(j),tickets_planned_array.get(j),enhancements_planned_array.get(j) }, j);
            BarEntry value_completed = new BarEntry(new float[] { tasks_completed_array.get(j), events_completed_array.get(j),tickets_completed_array.get(j),enhancements_completed_array.get(j) }, j);

            values_plan.add(value_planned);
            values_comp.add(value_completed);



        }

        BarDataSet barDataSet_plan = new BarDataSet(values_plan, "");
        barDataSet_plan.setStackLabels(new String[]{"Events Planned", "Tasks Planned", "Tickets_Planned", "Enhancements_Planned"});

        BarDataSet barDataSet_completed = new BarDataSet(values_plan, "");
        barDataSet_completed.setStackLabels(new String[]{"Events_Completed", "Tasks_Completed", "Tickets_Completed", "Enhancements_Completed"});





        barDataSet_plan.setColors(colors1);
        barDataSet_completed.setColors(colors2);


        dataSets.add(barDataSet_plan);
        dataSets.add(barDataSet_completed);


        System.out.println(dataSets);


        return dataSets;

    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<String>();

        xAxis=role_name_array;

        return xAxis;
    }


    /**
     * Background Async Task to Load all Data by making HTTP Request
     * */
    class Department_Activities_Connection extends AsyncTask<String, String, String> {


        Context context;
        Dialog dialog;
        int no_rows;


        public Department_Activities_Connection(Context context) {
            this.context = context;

        }

        @Override
        protected void onPreExecute() {


            dialog = new Dialog(context, R.style.custom_dialog);
            dialog.setContentView(R.layout.progress_spinner);
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        /**
         * getting All Data from url
         */
        protected String doInBackground(String... args) {

            String jsonResult = "";

            try {
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 7000);
                HttpConnectionParams.setSoTimeout(httpParameters, 7000);


                HttpClient httpClient = new DefaultHttpClient(httpParameters);

                HttpPost httpPost = new HttpPost(args[0]);


                HttpResponse response = httpClient.execute(httpPost);

                //get data from the server
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();


            } catch (ClientProtocolException e) {

                e.printStackTrace();
                System.out.println(e.toString());
                con_checker = 1;

            } catch (IOException e) {

                e.printStackTrace();
                System.out.println(e.toString());
                con_checker = 1;

            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e.toString());
                con_checker = 1;
            }
            return jsonResult;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String result) {
            // dismiss the dialog after getting all products

            try {
                dialog.dismiss();
            } catch (Exception e) {
                System.out.println("error dialog" + e);}
                try {
                    ArrayList<Float> json_result = returnParsedJsonObject(result);

                    System.out.println(json_result);

                    if (no_rows == 0) {
                        chart.clear();
                    } else {

                        barchart();
                    }


                } catch (Exception ex) {
                    // TODO: handle exception
                    if (con_checker == 1) {

                    } else {
                        Toast.makeText(context, "Server connection failed",
                                Toast.LENGTH_LONG).show();
                        System.out.println(ex.toString());
                        con_checker = 1;
                    }

                }



            super.onPostExecute(result);
        }

        public ArrayList<Float> returnParsedJsonObject(String result) {

            ArrayList<Float> result_array = new ArrayList<Float>();

            JSONObject resultObject_0 = null;
            JSONObject resultObject_1 = null;

            try {
                JSONArray myJsonArray = new JSONArray(result);

                resultObject_0 = myJsonArray.getJSONObject(0);

               no_rows = Integer.valueOf(resultObject_0.getString("no_rows"));
                System.out.println("no bar:  " + no_rows);

                if (no_rows > 0) {

                    for (int i = 1; i <= no_rows; i++) {

                        resultObject_1 = myJsonArray.getJSONObject(i);

                        String role_name = resultObject_1.getString("Role_Name");

                        float events_planned = Float.valueOf(resultObject_1.getString("Events_Planned"));
                        float tasks_planned = Float.valueOf(resultObject_1.getString("Tasks_Planned"));
                        float tickets_planned = Float.valueOf(resultObject_1.getString("Tickets_Planned"));
                        float enhancements_planned = Float.valueOf(resultObject_1.getString("Enhancements_Planned"));

                        float events_completed = Float.valueOf(resultObject_1.getString("Events_Completed"));
                        float tasks_completed = Float.valueOf(resultObject_1.getString("Tasks_Completed"));
                        float tickets_completed = Float.valueOf(resultObject_1.getString("Tickets_Completed"));
                        float enhancements_completed = Float.valueOf(resultObject_1.getString("Enhancements_Completed"));

                        role_name_array.add(role_name);

                        events_planned_array.add(events_planned);
                        tasks_planned_array.add(tasks_planned);
                        tickets_planned_array.add(tickets_planned);
                        enhancements_planned_array.add(enhancements_planned);

                        events_completed_array.add(events_completed);
                        tasks_completed_array.add(tasks_completed);
                        tickets_completed_array.add(tickets_completed);
                        enhancements_completed_array.add(enhancements_completed);

                    }
                } else {
                    chart.clear();
                }

                Log.i("parseeeed result", result_array.toString());

            } catch (JSONException e) {
                Log.e("errrrrror", e.toString());
            }

            return result_array;
        }


        private StringBuilder inputStreamToString(InputStream is) {

            String readLine = "";

            StringBuilder answer = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {

                while ((readLine = br.readLine()) != null) {

                    answer.append(readLine);

                }

            } catch (IOException e) {

                // TODO Auto-generated catch block

                e.printStackTrace();

            }

            return answer;

        }

    }
    }

