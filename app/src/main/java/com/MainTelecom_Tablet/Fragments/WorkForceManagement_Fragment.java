package com.MainTelecom_Tablet.Fragments;

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

import com.MainTelecom_Tablet.App.AppConfig;
import com.MainTelecom_Tablet.R;
import com.MainTelecom_Tablet.SharedPreference.SaveSharedPreference;

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



/**
 * Created by MOHAMED on 03/05/2016.
 */
public class WorkForceManagement_Fragment extends Fragment {

    TextView taskes_vac,taskes_vac_values,visites_meeting,visites_meeting_values,tickets_enhanc,tickets_enhanc_values,total_free,total_free_values;
    LinearLayout layout;
    String location;
    String url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View workforce_fragment=inflater.inflate(R.layout.workforce_layout, container, false);


        taskes_vac=(TextView)workforce_fragment.findViewById(R.id.taskes_vac_id);
        taskes_vac_values=(TextView)workforce_fragment.findViewById(R.id.taskes_vac_value_id);

        visites_meeting=(TextView)workforce_fragment.findViewById(R.id.meeting_visits_id);
        visites_meeting_values=(TextView)workforce_fragment.findViewById(R.id.meeting_visits_value_id);

        tickets_enhanc=(TextView)workforce_fragment.findViewById(R.id.tickets_enhanc_id);
        tickets_enhanc_values=(TextView)workforce_fragment.findViewById(R.id.tickets_enhanc_value_id);

        total_free=(TextView)workforce_fragment.findViewById(R.id.total_free_id);
        total_free_values=(TextView)workforce_fragment.findViewById(R.id.total_free_value_id);

        url="http://"+ SaveSharedPreference.getUserIP(getActivity())+ AppConfig.url_workforce_mangment;
        WorkForce_Connection workForce_connection=new WorkForce_Connection(getActivity());
        workForce_connection.execute(url);

        return workforce_fragment;
    }


    private class WorkForce_Connection extends AsyncTask<String ,Void,String> {

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

                String free =resultObject_0.getString("Free");
                String total =resultObject_0.getString("Total");

                String tasked =resultObject_0.getString("Tasked");
                String enhancements =resultObject_0.getString("Enhancements");

                String tickets =resultObject_0.getString("Tickets");
                String visitors =resultObject_0.getString("Visitors");

                String meetings =resultObject_0.getString("Meetings");
                String vacations =resultObject_0.getString("Vacations");

                total_free.setText("Total   "+" Free");
                total_free_values.setText(total+"           "+" "+free);

                taskes_vac.setText("Taskes   "+" Vacation");
                taskes_vac_values.setText(tasked+"          "+" "+vacations);

                tickets_enhanc.setText("Tickets   "+" Enhancement");
                tickets_enhanc_values.setText(tickets+"             "+" "+enhancements);

                visites_meeting.setText("Visites   "+" Meeting");
                visites_meeting_values.setText(visitors + "             " + "   " + meetings);


                Log.i("parseeeed result", result_array.toString());

            }catch (JSONException e){
                Log.e("errrrrror", e.toString());
            }

            return result_array;
        }


    }

}

