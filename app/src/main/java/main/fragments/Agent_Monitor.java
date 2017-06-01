package main.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.MainTelecom_Tablet.R;

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
 * Created by Hatem on 3/27/2016.
 */
public class Agent_Monitor extends Fragment  {

    TextView away_break,away_break_values,talking,taking_values,ringing,ringing_values,available,available_values;
    LinearLayout layout;
    String location;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View workforce_fragment=inflater.inflate(R.layout.workforce_layout, container, false);

        location = getArguments().getString("location");
        String url="http://"+ Login.enteredIP+"/Android/agents_monitor.php";

        away_break=(TextView)workforce_fragment.findViewById(R.id.taskes_vac_id);
        away_break_values=(TextView)workforce_fragment.findViewById(R.id.taskes_vac_value_id);

        talking=(TextView)workforce_fragment.findViewById(R.id.meeting_visits_id);
        taking_values=(TextView)workforce_fragment.findViewById(R.id.meeting_visits_value_id);

        ringing=(TextView)workforce_fragment.findViewById(R.id.tickets_enhanc_id);
        ringing_values=(TextView)workforce_fragment.findViewById(R.id.tickets_enhanc_value_id);

        available=(TextView)workforce_fragment.findViewById(R.id.total_free_id);
        available_values=(TextView)workforce_fragment.findViewById(R.id.total_free_value_id);

        layout=(LinearLayout)workforce_fragment.findViewById(R.id.layout_workforce);

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final ArrayList list = new ArrayList();
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

                Home_Team.charts_dialog(getActivity(), location, list);

                return false;
            }
        });

        WorkForce_Connection workForce_connection=new WorkForce_Connection(getActivity());
        workForce_connection.execute(url);

        return workforce_fragment;
    }


    private class WorkForce_Connection extends AsyncTask<String ,Void,String>{

        Dialog dialog;
        Context context;

        public WorkForce_Connection(Context context){
            this.context=context;

        }

        @Override
        protected void onPreExecute() {

            try {


                dialog = new Dialog(context, R.style.custom_dialog);
                dialog.setContentView(R.layout.progress_spinner);
                dialog.setCancelable(false);
                dialog.show();
            }catch (Exception e){

            }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String jsonResult = "";
            try {
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 12000);
                HttpConnectionParams.setSoTimeout(httpParameters, 12000);

                HttpClient httpClient = new DefaultHttpClient(httpParameters);

                HttpPost httpPost = new HttpPost(params[0]);

                HttpResponse response = httpClient.execute(httpPost);

                // get data from the server
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();

            } catch (ClientProtocolException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }


            return jsonResult;
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


        @Override
        protected void onPostExecute(String s) {

            try {
                dialog.dismiss();


                returnParsedJsonObject(s);




            }catch (Exception e){

            }
            System.out.println("woooooooook         :"+s.toString());










            super.onPostExecute(s);
        }

        public ArrayList<String> returnParsedJsonObject(String result){

            ArrayList<String> result_array = new ArrayList<String>();

            JSONObject resultObject_0  = null;

            try {

                JSONArray myJsonArray = new JSONArray(result);

                resultObject_0=myJsonArray.getJSONObject(0);

                String _away =resultObject_0.getString("Away");
                String _available =resultObject_0.getString("Available");
                String _ringing =resultObject_0.getString("Ringing");
                String _talking =resultObject_0.getString("Talking");
                String _break =resultObject_0.getString("Break");



                away_break.setText("Away   "+" Break");
                away_break_values.setText(_away+"           "+" "+_break);

                ringing.setText("Ringing");
                ringing_values.setText(_ringing);

                talking.setText("Talking");
                taking_values.setText(_talking);

                available.setText("Available");
                available_values.setText(_available);


                Log.i("parseeeed result", result_array.toString());

            }catch (JSONException e){
                Log.e("errrrrror", e.toString());
            }

            return result_array;
        }


    }

}
