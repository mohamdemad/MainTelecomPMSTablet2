package main.charts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.MainTelecom_Tablet.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import main.activities.Home_Team;
import main.others.JSONParser;

/**
 * Created by MOHAMED on 05/02/2016.
 */
public class Effort_Achieved extends Fragment {

    PieChart chart;
    JSONParser jParser = new JSONParser();
    private String serverUrl,location;
    String TasksCompleted,TicketsCompleted,EnhancementsCompleted, EventsCompleted ;
    private RelativeLayout mainLayout;

    int[] colors = new int[]{Color.rgb(69, 114, 166),Color.rgb(169,70,67),Color.rgb(136,164,78), Color.rgb(218, 131, 61)};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View effort_achieved = inflater.inflate(R.layout.piechart_leads, container, false);

        location = getArguments().getString("location");

        mainLayout = (RelativeLayout) effort_achieved
                .findViewById(R.id.layout_piechart_leads);

        new Activities_Achieved_Connection(getActivity()).execute();

        serverUrl = new StringBuilder("http://"+ "192.168.1.150"+"/Android/Team/Department_Activities.php").toString();


        return effort_achieved;
    }

    private PieDataSet getDataSet() {

        PieDataSet PieDataSet2 = new PieDataSet(null, "");

        ArrayList<Entry> valueSet2 = new ArrayList<>();


        Entry v1e2 = new Entry(Integer.parseInt(EventsCompleted), 0); // Completed
        Entry v2e2 = new Entry(Integer.parseInt(TasksCompleted), 1); // Completed
        Entry v3e2 = new Entry(Integer.parseInt(TicketsCompleted), 2); // Completed
        Entry v4e2 = new Entry(Integer.parseInt(EnhancementsCompleted), 3); // Completed
        valueSet2.add(v1e2);
        valueSet2.add(v2e2);
        valueSet2.add(v3e2);
        valueSet2.add(v4e2);

        PieDataSet2 = new PieDataSet(valueSet2, "");
        PieDataSet2.setColors(colors);
        System.out.println("haten   hatem  :  " + EventsCompleted);
        return PieDataSet2;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Events");
        xAxis.add("Tasks");
        xAxis.add("Tickets");
        xAxis.add("Enhancements");
        return xAxis;
    }

    private void piechart(){


        chart = new PieChart(getActivity());

        // add pie chart to layout
        mainLayout.addView(chart, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 300));

         System.out.println(getXAxisValues());
         PieData achieved_data = new PieData(getXAxisValues(), getDataSet());

        achieved_data.setValueFormatter(new PercentFormatter());

        chart.setDescription(" Effort Achieved");
        chart.setUsePercentValues(true);
        chart.animateXY(2000, 2000);

        // enable hole and configure

        chart.setHoleRadius(15);
        chart.setTransparentCircleRadius(20);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                // display Msg when value selected

                if (e == null)
                    return;
                Toast.makeText(getActivity(),
                         getXAxisValues().get(e.getXIndex()) + "=" + e.getVal() + "%",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });


        chart.setOnTouchListener(new View.OnTouchListener() {
            final ArrayList list = new ArrayList();

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    long eventDuration = event.getEventTime() - event.getDownTime();
                    if (eventDuration >= 1000) {
                        list.clear();
                        list.add("Allocation");
                        list.add("Activities");
                        list.add("Invoices");
                        list.add("Leads");
                        list.add("Campany Invoices");
                        list.add("Activities Achieved");
                        list.add("Activities Planned");
                        list.add("Effort Planned");
                        list.add("Effort Achieved");
                        list.add("WorkForce Management");
                        list.add("Agents Monitor");

                        eventDuration = 0;
                        Home_Team.charts_dialog(getActivity(), location, list);
                    }
                }

                return false;
            }
        });




        // enable rotation of the chart by touch

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);

        chart.setData(achieved_data);
        chart.invalidate();

    }



    class Activities_Achieved_Connection extends AsyncTask<String, String, String> {

        Dialog dialog;
        Context context;

        //public AsyncReturn delegate = null;

        public Activities_Achieved_Connection(Context context) {
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(context,R.style.custom_dialog);
            dialog.setContentView(R.layout.progress_spinner);
            dialog.setCancelable(false);
            dialog.show();
        }

        /**
         * getting All Data from url
         * */
        protected String doInBackground(String... args) {

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                // getting JSON string from URL
                JSONArray json = jParser.makeHttpRequest(serverUrl, "POST", params,"EVALUATION");

                // Check your log cat for JSON reponse
                Log.d("All Data: ", json.toString());
                // looping through All Products

                JSONObject c = json.getJSONObject(1);

                // Storing each json item in variable
                //effort Completed

                EventsCompleted = c.getString("Events_Completed");
                TasksCompleted = c.getString("Tasks_Completed");
                TicketsCompleted = c.getString("Tickets_Completed");
                EnhancementsCompleted = c.getString("Enhancements_Completed");


            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getActivity(), "This is Last Old Data Because There Is No Enternet Connection", Toast.LENGTH_LONG).show();
            }

            return null;
        }
        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
                e.printStackTrace();
            }
            return answer;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products

            try {
                dialog.dismiss();
            }catch (Exception e){
                System.out.println("error dialog" + e);

            }

            try {
                piechart();
            } catch (Exception e){
                Toast.makeText(context, "Server connection failed",
                        Toast.LENGTH_LONG).show();
                System.out.println(e.toString());

            }

        }
    }


}
