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

import java.util.ArrayList;
import java.util.List;

import main.activities.Home_Team;
import main.activities.Login;
import main.others.JSONParser;

/**
 * Created by MOHAMED on 05/02/2016.
 */
public class Activities_Planned extends Fragment {

    PieChart chart;


    JSONParser jParser = new JSONParser();

    String TicketsPlanned, EnhancementPlanned,TasksPlanned,EventsPlanned;
    private String serverUrl,location;
    private RelativeLayout mainLayout;

    int[] colors = new int[]{Color.rgb(69, 114, 166),Color.rgb(169,70,67),Color.rgb(136,164,78), Color.rgb(218, 131, 61)};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View effort_planned = inflater.inflate(R.layout.piechart_leads, container, false);

        location = getArguments().getString("location");

        mainLayout = (RelativeLayout) effort_planned
                .findViewById(R.id.layout_piechart_leads);

        new Activities_Planned_Connection(getActivity()).execute();

        serverUrl = new StringBuilder("http://"+ Login.enteredIP+"/Android/Team/Activities_Evaluation.php").toString();


        return effort_planned;
    }

    private PieDataSet getDataSet() {

        PieDataSet PieDataSet1 = new PieDataSet(null, "");

        ArrayList<Entry> valueSet1 = new ArrayList<>();

            Entry v1e1 = new Entry(Integer.parseInt(EventsPlanned), 0); // Planned
            Entry v2e1 = new Entry(Integer.parseInt(TasksPlanned), 1); // Planned
            Entry v3e1 = new Entry(Integer.parseInt(TicketsPlanned), 2); // Planned
            Entry v4e1 = new Entry(Integer.parseInt(EnhancementPlanned), 3); // Planned
            valueSet1.add(v1e1);
            valueSet1.add(v2e1);
            valueSet1.add(v3e1);
            valueSet1.add(v4e1);

            PieDataSet1 = new PieDataSet(valueSet1, "");
            PieDataSet1.setColors(colors);
            return PieDataSet1;

    }

    private void piechart(){

        chart = new PieChart(getActivity());

        // add pie chart to layout
        mainLayout.addView(chart, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 300));


        PieData planned_data = new PieData(getXAxisValues(), getDataSet());

        planned_data.setValueFormatter(new PercentFormatter());

        chart.setData(planned_data);

        chart.setDescription("");

        chart.setUsePercentValues(true);

        chart.animateXY(2000, 2000);

        chart.invalidate();


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


        // enable hole and configure
            chart.setHoleRadius(15);
            chart.setTransparentCircleRadius(20);


            // enable rotation of the chart by touch
            chart.setRotationAngle(0);
            chart.setRotationEnabled(true);
            chart.highlightValues(null);

    }
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Events");
        xAxis.add("Tasks");
        xAxis.add("Tickets");
        xAxis.add("Enhancements");
        return xAxis;
    }


    class Activities_Planned_Connection extends AsyncTask<String, String, String> {


        Dialog dialog;
        Context context;

        //public AsyncReturn delegate = null;

        public Activities_Planned_Connection(Context context) {
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
                    JSONObject c = json.getJSONObject(0);

                    // Storing each json item in variable
                    //effort Planned

                        EventsPlanned = c.getString("Events_Planned");
                        TasksPlanned = c.getString("Tasks_Planned");
                        TicketsPlanned = c.getString("Tickets_Planned");
                        EnhancementPlanned = c.getString("Enhancements_Planned");



            } catch (Exception e) {
                e.printStackTrace();
               // Toast.makeText(getActivity(), "This is Last Old Data Because There Is No Enternet Connection", Toast.LENGTH_LONG).show();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products

            try {
                dialog.dismiss();
            }catch (Exception e){
                System.out.println("error dialog" + e);

            }



            try {

                piechart();
            }catch(Exception e){

                Toast.makeText(context, "Server connection failed",
                        Toast.LENGTH_LONG).show();
                System.out.println(e.toString());

            }



        }
    }
}