package main.charts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MainTelecom_Tablet.R;
import com.cardiomood.android.controls.gauge.SpeedometerGauge;

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


public class Speedometer extends Fragment implements OnRefreshListener {

	private SpeedometerGauge speedometer_target, speedometer_qos,
			speedometer_SL;
	int x, value = 100;
	Handler handler;
	private long delay = 10000;
	int time = 0; // second
	Thread thread;
	int startAngle,refresh_checker=0;
	private String serverUrl,serverUrl2,serverUrl3,location;
	TextView text_target_value,text_qos_value,text_SL_value;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View fragment_speedometer = inflater.inflate(R.layout.speedometer_layout,
				container, false);

				location = getArguments().getString("location");

		text_target_value=(TextView)fragment_speedometer.findViewById(R.id.text_target_value);
		text_qos_value=(TextView)fragment_speedometer.findViewById(R.id.text_qos_value);
		text_SL_value=(TextView)fragment_speedometer.findViewById(R.id.text_SL_value);
		LinearLayout layout=(LinearLayout)fragment_speedometer.findViewById(R.id.speedometer_layout_id);


		layout.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				final ArrayList list = new ArrayList();
				list.clear();
				list.add("Working Hour");
				list.add("Today ");
				list.add("Department Activities");
				list.add("Department Effort");

				Home_Team.charts_dialog(getActivity(), location, list);

				return false;
			}
		});

		
		refresh_checker = 0;
		// for refresh activity when scroll down


		// Customize SpeedometerView
		speedometer_target = (SpeedometerGauge) fragment_speedometer
				.findViewById(R.id.speedometer_target_id);
		speedometer_qos = (SpeedometerGauge) fragment_speedometer
				.findViewById(R.id.speedometer_qos_id);
		speedometer_SL = (SpeedometerGauge) fragment_speedometer
				.findViewById(R.id.speedometer_sl_id);


		 serverUrl = new StringBuilder("http://").append("41.32.29.164")
				.append("/Android/Today_Target.php").toString();
		 serverUrl2 = new StringBuilder("http://")
				.append("41.32.29.164").append("/Android/Today_QOS.php")
				.toString();
		 serverUrl3 = new StringBuilder("http://")
				.append("41.32.29.164")
				.append("/Android/Today_ServiceLevel.php").toString();

		// speedometer _target
		Speedometer_Connection cone = new Speedometer_Connection(getActivity(),
				"today_Target");
		cone.execute(serverUrl);

		// speedometer_qos
		Speedometer_Connection cone2 = new Speedometer_Connection(
				getActivity(), "today_QOS");
		cone2.execute(serverUrl2);

		// Speedometer_service
		Speedometer_Connection cone3 = new Speedometer_Connection(
				getActivity(), "today_ServiceLevel");
		cone3.execute(serverUrl3);

		return fragment_speedometer;
	}

	private void Speed_Target(final SpeedometerGauge speedometer,  int value,TextView textview) {

		double value1=0;
		textview.setText(value + "%");
		
		if (value<=50) {
			textview.setTextColor(Color.parseColor("#ff0000"));
		}else if (value>50 && value<75) {
			textview.setTextColor(Color.parseColor("#ffff00"));
		}else if (value>75) {
			textview.setTextColor(Color.parseColor("#00ff00"));
		}
		
		
		
		
		 // Add label converter
		  speedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
			  @Override
			  public String getLabelFor(double progress, double maxProgress) {
				  return String.valueOf((int) Math.round(progress));
			  }
		  });

		 // configure value range and ticks
		  speedometer.setMaxSpeed(100);
		  speedometer.setMajorTickStep(25);
		  speedometer.setMinorTicks(4);
		  speedometer.setLabelTextSize(13);



		  // Configure value range colors
		  speedometer.addColoredRange(0, 50, Color.RED);
		  speedometer.addColoredRange(50, 75, Color.YELLOW);
		  speedometer.addColoredRange(75, 100, Color.GREEN);
		  if (value>=55 && value<75) {
			value1=value-2;
		}else if (value<=45) {
			value1=value-3;
			
			if (value1<=0) {
				value1=0;
			}
			
			
		}else {
			value1=value;
		}
		 // speedometer.setSpeed(value1);
		speedometer.setSpeed(value1,true);

	}

	private class Speedometer_Connection extends
			AsyncTask<String, Void, String> {

		Dialog dialog;
		Context context;
		String key;
		//public AsyncReturn delegate = null;

		public Speedometer_Connection(Context context, String key) {
			this.context = context;
			this.key = key;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			if (refresh_checker == 0) {

				dialog = new Dialog(context,R.style.custom_dialog);
				dialog.setContentView(R.layout.progress_spinner);
				dialog.setCancelable(false);
			    dialog.show();
			} else {

			}

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

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

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			try {
				dialog.dismiss();
			}catch (Exception e){
				System.out.println("error dialog" + e);
			}

			try {

				Log.i("result is hereeeee", result);
				System.out.println("Resulted Value: " + result);

				/*
				if (result.equals("") || result == null) {

					Toast.makeText(context, "Server connection failed",
							Toast.LENGTH_LONG).show();

					return;
				}
				*/
				String jsonResult = returnParsedJsonObject(result);
				// Log.i("parsed resultttt", String.valueOf(jsonResult));

				int jsonResult_int = Integer.valueOf(jsonResult);
				// System.out.println("int= " + jsonResult);

				switch (key) {
				case "today_Target":

					Speed_Target(speedometer_target, jsonResult_int,text_target_value);
					break;

				case "today_QOS":

					Speed_Target(speedometer_qos, jsonResult_int,text_qos_value);
					break;

				case "today_ServiceLevel":

					Speed_Target(speedometer_SL, jsonResult_int,text_SL_value);
					break;

				}

			} catch (Exception e) {
				// TODO: handle exception
				
				
				Toast.makeText(context, "Server connection failed",
						Toast.LENGTH_LONG).show();
				System.out.println(e.toString());

			}

			super.onPostExecute(result);
		}

		public String returnParsedJsonObject(String result) {

			JSONObject resultObject = null;

			String loginResult = null;
			try {
				JSONArray myJsonArray = new JSONArray(result);
				resultObject = myJsonArray.getJSONObject(0);
				loginResult = resultObject.getString(key);
				Log.i("parseeeed result", loginResult);

			} catch (JSONException e) {
				Log.e("errrrrror", e.toString());
			}

			return loginResult;
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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		refresh_checker = 1;

		// speedometer _target
		Speedometer_Connection cone = new Speedometer_Connection(getActivity(),
				"today_Target");
		cone.execute(serverUrl);

		// speedometer_qos
		Speedometer_Connection cone2 = new Speedometer_Connection(
				getActivity(), "today_QOS");
		cone2.execute(serverUrl2);

		// Speedometer_service
		Speedometer_Connection cone3 = new Speedometer_Connection(
				getActivity(), "today_ServiceLevel");
		cone3.execute(serverUrl3);
	}

}
