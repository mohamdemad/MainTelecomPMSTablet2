package com.MainTelecom_Tablet.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.MainTelecom_Tablet.App.AppConfig;
import com.MainTelecom_Tablet.JSON.JSONParser;
import com.MainTelecom_Tablet.R;
import com.MainTelecom_Tablet.SharedPreference.SaveSharedPreference;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.util.Calendar;
import java.util.List;


/**
 * Created by MOHAMED on 02/05/2016.
 */
public class Company_Invoices_Fragment extends Fragment {
    TextView backlog_value,allinvoices_value,due_value,rate_value,backlog_text,allinvoices_text,due_text,rate_text;
    Handler handler;
    String location;
    String url;
    boolean a = true;
    boolean b = false;
    boolean c = false;
    boolean d = false;

    JSONParser jParser =new JSONParser();

    private String AVG_Working_Hours_value;
    private String AVG_Tickets_time_solving_value;
    private String AVG_Sales_value;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment_invoices = inflater.inflate(R.layout.company_invoices_layout, container, false);



        backlog_value=(TextView)fragment_invoices.findViewById(R.id.backlog_value_id);
        allinvoices_value=(TextView)fragment_invoices.findViewById(R.id.allinvoices_value_id);
        due_value=(TextView)fragment_invoices.findViewById(R.id.due_value_id);
        rate_value=(TextView)fragment_invoices.findViewById(R.id.rate_value_id);

        backlog_text=(TextView)fragment_invoices.findViewById(R.id.backlog_id);
        allinvoices_text=(TextView)fragment_invoices.findViewById(R.id.allinvoices_id);
        due_text=(TextView)fragment_invoices.findViewById(R.id.due_id);
        rate_text=(TextView)fragment_invoices.findViewById(R.id.rate_id);

        url="http://"+ SaveSharedPreference.getUserIP(getActivity())+ AppConfig.url_company_invoices;
        Invoices_Connection connection=new Invoices_Connection(getActivity());
        connection.execute(url);

        return fragment_invoices;
    }


    public class Invoices_Connection extends AsyncTask<String,Void, String> {

        Context context;
        Dialog dialog;
        int no_bars;
        int con_lossed=0;

        public Invoices_Connection(Context context) {
            // TODO Auto-generated constructor stub
            this.context = context;

        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog = new Dialog(context, R.style.custom_dialog);
            dialog.setContentView(R.layout.progress_spinner);
            dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String jsonResult = "";
            try {
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 7000);
                HttpConnectionParams.setSoTimeout(httpParameters, 7000);
                HttpClient httpClient = new DefaultHttpClient(httpParameters);
                HttpPost httpPost = new HttpPost(params[0]);
                HttpResponse response = httpClient.execute(httpPost);

                //get data from the server
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

            } catch (ClientProtocolException e) {
                e.printStackTrace();
                System.out.println(e.toString());
                con_lossed=1;
            } catch (IOException e) {

                e.printStackTrace();
                System.out.println(e.toString());
                con_lossed=1;

            }catch (Exception e) {
                // TODO: handle exception
                System.out.println(e.toString());
                con_lossed=1;
            }
            try {
                List<NameValuePair> params_invoices_need = new ArrayList<NameValuePair>();
                JSONArray json = jParser.makeHttpRequest("http://"+ SaveSharedPreference.getUserIP(getActivity())+AppConfig.url_company_invoices_need, "GET", params_invoices_need,"ROLES");

                Log.d("All Data: ", json.toString());
                System.out.println("Invoices Need :"+json.toString());

                JSONObject c = json.getJSONObject(0);

                AVG_Working_Hours_value = c.getString("AVG Working Hours");
                AVG_Tickets_time_solving_value = c.getString("AVG Tickets time solving");
                AVG_Sales_value = c.getString("AVG Sales");
            }catch (Exception e99){

            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            dialog.dismiss();

            Calendar cal=Calendar.getInstance();
            final int current_year=cal.get(Calendar.YEAR);


            try {

               final ArrayList<String> json_result = returnParsedJsonObject(result);

                backlog_value.setText(json_result.get(0)+" $");
                allinvoices_value.setText("6 (%)");//AVG_Working_Hours_value
                due_value.setText("21 (hours)");//AVG_Tickets_time_solving_value
                rate_value.setText("39000 (£)");//AVG_Sales_value

                backlog_text.setText("Back Log");
                allinvoices_text.setText("AVG Working Hours");
                due_text.setText("AVG Tickets time solving");
                rate_text.setText("AVG Sales");

                final Handler h = new Handler();
                final Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if(a) {
                            backlog_value.setText(json_result.get(0) + " $");
                            backlog_text.setText("Back Log");
                            a = false;
                            b = true;
                            c = false;
                            d = false;
                        }else if(b){
                            backlog_value.setText(json_result.get(1) + " $");
                            backlog_text.setText(current_year+" All Invoices");
                            a = false;
                            b = false;
                            c = true;
                            d = false;
                        }else if(c){
                            backlog_value.setText(json_result.get(2) + " $");
                            backlog_text.setText(current_year+" DUE");
                            a = false;
                            b = false;
                            c = false;
                            d = true;
                        }else if(d){
                            backlog_value.setText(json_result.get(3) + " $");
                            backlog_text.setText(current_year+" Collection Rate");
                            a = true;
                            b = false;
                            c = false;
                            d = false;
                        }
                        h.postDelayed(this, 3000); //ms
                    }
                };
                h.postDelayed(r, 3000); // one second in ms


            } catch (Exception e) {
                // TODO: handle exception

                Toast.makeText(context, "Server connection failed",
                        Toast.LENGTH_LONG).show();
                System.out.println(e.toString());

            }
            super.onPostExecute(result);
        }

        public ArrayList<String> returnParsedJsonObject(String result){

            ArrayList<String> result_array = new ArrayList<String>();

            JSONObject resultObject_0  = null;

            try {

                JSONArray myJsonArray = new JSONArray(result);

                resultObject_0=myJsonArray.getJSONObject(0);

                String back_log =resultObject_0.getString("Back_Log");
                String all_invoices =resultObject_0.getString("All_Invoices");
                String due =resultObject_0.getString("DUE");
                String collection_rates =resultObject_0.getString("Collection_Rates");

                result_array.add(back_log);
                result_array.add(all_invoices);
                result_array.add(due);
                result_array.add(collection_rates);

                Log.i("parseeeed result", result_array.toString());
            }catch (JSONException e){
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

