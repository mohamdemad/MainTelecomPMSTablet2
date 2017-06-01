package main.charts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
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


public class Barchart_Working extends Fragment {
	
	private BarChart chart;
	Handler handler;
	private ArrayList<String> employee_name_array=new ArrayList<String>();
	private ArrayList<Float> not_allocated_hours_array=new ArrayList<Float>();
	private ArrayList<Float> allocated_hours_array=new ArrayList<Float>();
	  ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();

	private int con_checker;
	String location;
	//private ArrayList<String> bar_name_array =new ArrayList<String>();;
	//private ArrayList<ArrayList> bar_arrayvalues_array=new ArrayList<ArrayList>();
	
	public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
		
		View fragment_barchart_working_hours = inflater.inflate(
				R.layout.barchart_working_hours_layout, container, false);

		location = getArguments().getString("location");

		String url="http://"+ Login.enteredIP+"/Android/Team/Working_Hours.php";

		Barchart_Working_Hours_Connection barchart_working=new Barchart_Working_Hours_Connection(getContext());
		barchart_working.execute(url);
		
		
		 chart = (BarChart) fragment_barchart_working_hours.findViewById(R.id.chart_working_hours);
		
       
		return fragment_barchart_working_hours;
        
       
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
      
 		
            ArrayList<BarEntry> values = new ArrayList<BarEntry>();
           

            	

            for (int j = 0; j < employee_name_array.size(); j++) {
    			
            	BarEntry value = new BarEntry(new float[] { not_allocated_hours_array.get(j), allocated_hours_array.get(j) }, j); 
            	values.add(value);
            	
            	
            	
    		}
            
            BarDataSet barDataSet = new BarDataSet(values, "");
    		barDataSet.setStackLabels(new String[] { "Not Allocated", "Allocated"});

            	
			barDataSet.setColors(new int[] {Color.rgb(69, 114, 167),Color.rgb(170,70,67)});
			
					
            dataSets.add(barDataSet);
            
            System.out.println(dataSets);
		
        
        return dataSets;
        
    }
 
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<String>();
        
      xAxis=employee_name_array;
       
       
        return xAxis;
    }
		
		
		
    public class Barchart_Working_Hours_Connection extends AsyncTask<String,Void, String>	{

		Context context;
		Dialog dialog;
		int no_rows;
		
    	public Barchart_Working_Hours_Connection(Context context) {
			// TODO Auto-generated constructor stub
    		
			this.context = context;
		}
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		dialog = new Dialog(context,R.style.custom_dialog);
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
	        	 con_checker=1;

	         } catch (IOException e) {

	             e.printStackTrace();
	        	 System.out.println(e.toString());
	        	 con_checker=1;

	         }catch (Exception e) {
				// TODO: handle exception
	        	 System.out.println(e.toString());
	        	 con_checker=1;
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
				ArrayList<Float> json_result = returnParsedJsonObject(result);
				
				System.out.println(employee_name_array);
				System.out.println(json_result);


				if (no_rows==0) {
					chart.clear();
				}else {
					
					barchart();
				}
				
				

				
			} catch (Exception e) {
				// TODO: handle exception
				if (con_checker==1) {
					
				}else {
				Toast.makeText(context, "Server connection failed",
						Toast.LENGTH_LONG).show();
				System.out.println(e.toString());
				con_checker=1;
				}
				
			}

			
			super.onPostExecute(result);
		}
		
		
		public ArrayList<Float> returnParsedJsonObject(String result){

			ArrayList<Float> result_array = new ArrayList<Float>();
			
			JSONObject resultObject_0  = null;
	        JSONObject resultObject_1  = null;
	        
	        try {
	            JSONArray myJsonArray = new JSONArray(result);
	            
	            resultObject_0=myJsonArray.getJSONObject(0);
	            
	            no_rows =Integer.valueOf( resultObject_0.getString("no_rows"));
			    System.out.println("no bar:  "+no_rows);
			    	
			    if (no_rows>0) {
					
			    for (int i = 1; i <= no_rows; i++) {
					
			    	resultObject_1 = myJsonArray.getJSONObject(i);
	            
	            	String    employee_name =resultObject_1.getString("employee_name");
	            	float    not_allocated_hours=Float.valueOf( resultObject_1.getString("not_allocated_hours"));
	            	float    allocated_hours = Float.valueOf(resultObject_1.getString("allocated_hours"));

	            	employee_name_array.add(employee_name);
	            	not_allocated_hours_array.add(not_allocated_hours);
	            	allocated_hours_array.add(allocated_hours);
	            	
			    }
			    }else {
					chart.clear();
				}
	            
	        
	        
	            Log.i("parseeeed result",result_array.toString());

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
