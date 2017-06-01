package main.charts;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import com.github.mikephil.charting.components.Legend.LegendPosition;
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


public class PieChart_Invoices extends Fragment implements OnRefreshListener {

	private RelativeLayout mainLayout;
	private PieChart chart;
	private float[] yData = new float[2];
	private String[] xData = { "Invoices", "Incoming Calls" };
	int refresh_checker = 0;
	private String serverUrl,location;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View fragment_pie_invoices = inflater.inflate(
				R.layout.piechart_invoices, container, false);

		refresh_checker = 0;
		// for refresh activity when scroll down
		location = getArguments().getString("location");


		mainLayout = (RelativeLayout) fragment_pie_invoices
				.findViewById(R.id.layout_piechart_invoices);

		 serverUrl = new StringBuilder("http://").append("41.32.29.164")
				.append("/Android/Home_Charts/Invoice_InCalls.php").toString();

		Invoices_Connection invoices_connection = new Invoices_Connection(
				getContext(), "Invoices", "Incoming_Calls");
		invoices_connection.execute(serverUrl);

		return fragment_pie_invoices;

	}

	private void pie_chart() {

		chart = new PieChart(getActivity().getApplicationContext());

		// add pie chart to layout
		mainLayout.addView(chart, new RelativeLayout.LayoutParams(
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
		chart.setDescription("Invoices/Incoming Calls");


		chart.setOnTouchListener(new View.OnTouchListener() {
			final ArrayList list = new ArrayList();

			@Override
			public boolean onTouch(View v, MotionEvent event) {


				if(event.getAction()==MotionEvent.ACTION_UP){
				long eventDuration = event.getEventTime() - event.getDownTime();
				if(eventDuration>1000) {
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

					Toast.makeText(getActivity(),location,Toast.LENGTH_SHORT).show();
					Home_Team.charts_dialog(getActivity(), location, list);
				}}

				return false;
			}
		});


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
		l.setPosition(LegendPosition.ABOVE_CHART_LEFT);
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

	private class Invoices_Connection extends AsyncTask<String, Void, String> {

		Dialog dialog;
		Context context;
		String key_invoice;
		String key_incoming_calls;
		//public AsyncReturn delegate = null;

		public Invoices_Connection(Context context, String key_invoice,
				String key_incoming_calls) {
			this.context = context;
			this.key_invoice = key_invoice;
			this.key_incoming_calls = key_incoming_calls;

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
				String invoice_Result = resultObject.getString(key_invoice);
				String incoming_Result = resultObject
						.getString(key_incoming_calls);
				Result.add(Float.valueOf(invoice_Result));
				Result.add(Float.valueOf(incoming_Result));
				// addData(Result);
				// Log.i("parseeeed result", invoice_Result);

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
	public void onRefresh() {
		// TODO Auto-generated method stub

		refresh_checker = 1;
		Invoices_Connection invoices_connection = new Invoices_Connection(
				getContext(), "Invoices", "Incoming_Calls");
		invoices_connection.execute(serverUrl);

	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		System.out.println("resume invoi");

		super.onResume();
	}

}
