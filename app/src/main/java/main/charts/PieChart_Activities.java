package main.charts;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

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


public class PieChart_Activities extends Fragment  {

	private RelativeLayout mainLayout;
	private PieChart chart;
	private float[] yData = new float[4];
	private String[] xData = { "Events", "Tasks","Tickets","Enhancements"};
	int refresh_checker = 0;
	private String serverUrl;
	String location;


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View fragment_pie_activities = inflater.inflate(R.layout.piechart_invoices, container, false);

		location = getArguments().getString("location");

		refresh_checker = 0;
		// for refresh activity when scroll down


		mainLayout = (RelativeLayout) fragment_pie_activities
				.findViewById(R.id.layout_piechart_invoices);

		 serverUrl = new StringBuilder("http://"+ Login.enteredIP+"/Android/Team/Organization_Activities.php").toString();

		 Organization_Activities_Connection invoices_connection = new Organization_Activities_Connection(getContext());
		invoices_connection.execute(serverUrl);

		return fragment_pie_activities;

	}

	private void pie_chart() {

		chart = new PieChart(getActivity().getApplicationContext());


		chart.setOnTouchListener(new View.OnTouchListener() {
			final ArrayList list = new ArrayList();

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction()==MotionEvent.ACTION_UP){

					long eventDuration = event.getEventTime() - event.getDownTime();
				if(eventDuration>=1000){
					eventDuration=0;
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

					Home_Team.charts_dialog(getActivity(), location,list);

				}}


				return false;
			}
		});

		// add pie chart to layout
		mainLayout.addView(chart,new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 300));

		// configure pie chart
		chart.setUsePercentValues(true);

		// enable hole and configure
		chart.setDrawHoleEnabled(true);// to set center of pie empty(true) /
										// not(false)
		chart.setHoleRadius(7);
		chart.setTransparentCircleRadius(10);

		// rotate chart by touch
		chart.setRotationAngle(0);
		chart.setRotationEnabled(true);
		chart.setDescription("Organization Activities");


		// set chart value selected listener
		chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

			@Override
			public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

				// display Msg when value selected

				if (e == null)
					return;
				Toast.makeText(getActivity(),
						xData[e.getXIndex()] + "=" + e.getVal() + "%",
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onNothingSelected() {

			}
		});

		// add data
		addData();

		// customizq legends
		Legend l = chart.getLegend();
		l.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
		l.setXEntrySpace(7);
		l.setYEntrySpace(5);

	}

	private void addData() {
		ArrayList<Entry> yarray = new ArrayList<Entry>();

		for (int i = 0; i < yData.length; i++) {

			yarray.add(new Entry(yData[i], i));

		}

		ArrayList<String> xarray = new ArrayList<String>();

		for (int i = 0; i < xData.length; i++) {

			xarray.add(xData[i]);
		}
		// create pie data set
		PieDataSet dataset = new PieDataSet(yarray, "");
		dataset.setSliceSpace(3);
		dataset.setSelectionShift(5);

		// add many colors

		ArrayList<Integer> colors = new ArrayList<Integer>();
		for (int c : ColorTemplate.PASTEL_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.JOYFUL_COLORS)
			colors.add(c);

		colors.add(ColorTemplate.getHoloBlue());
		dataset.setColors(colors);

		// instantiate pie data object now
		PieData data = new PieData(xarray, dataset);

		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(15f);
		chart.setData(data);

		// undo all highlights
		chart.highlightValues(null);

		// update pir chart
		chart.invalidate();

	}

	private class Organization_Activities_Connection extends AsyncTask<String, Void, String> {

		Dialog dialog;
		Context context;
		
		//public AsyncReturn delegate = null;

		public Organization_Activities_Connection(Context context) {
			this.context = context;
			
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
				HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
				HttpConnectionParams.setSoTimeout(httpParameters, 5000);

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

				ArrayList<Float> array_result = returnParsedJsonObject(result);

				yData[0] = array_result.get(0);
				yData[1] = array_result.get(1);
				yData[2] = array_result.get(2);
				yData[3] = array_result.get(3);

				System.out.println("float  :  " + array_result.get(1));

				pie_chart();
				
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(context, "Server connection failed",
						Toast.LENGTH_LONG).show();
				System.out.println(e.toString());
				//yData[0] = 0;
				//yData[1] = 0;
			}

			super.onPostExecute(result);
		}

		public ArrayList<Float> returnParsedJsonObject(String result) {

			JSONObject resultObject = null;

			ArrayList<Float> Result = new ArrayList<Float>();
			try {
				JSONArray myJsonArray = new JSONArray(result);

				resultObject = myJsonArray.getJSONObject(0);
				float events = Float.valueOf(resultObject.getString("Events"));
				float tasks = Float.valueOf(resultObject.getString("Tasks"));
				float tickets = Float.valueOf(resultObject.getString("Tickets"));
				float enhancements = Float.valueOf(resultObject.getString("Enhancements"));
				
				
				Result.add(Float.valueOf(events));
				Result.add(Float.valueOf(tasks));
				Result.add(Float.valueOf(tickets));
				Result.add(Float.valueOf(enhancements));

				

			} catch (JSONException e) {
				Log.e("errrrrror", e.toString());
			}

			return Result;
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
	public void onResume() {
		// TODO Auto-generated method stub
		System.out.println("resume invoi");

		super.onResume();
	}

}
