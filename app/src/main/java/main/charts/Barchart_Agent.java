package main.charts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.MainTelecom_Tablet.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

import main.activities.Home;
import main.activities.Home_Team;
import main.database_connections.Projects_Connection;

public class Barchart_Agent extends Fragment implements OnItemSelectedListener, View.OnClickListener ,DatePickerDialog.OnDateSetListener, SwipeRefreshLayout.OnRefreshListener {

	private HorizontalBarChart chart;
	private ArrayList<String> filter_list;
	private String item_selectd,project_spinner_itemSelected,option_spinner_itemSelected;
	private ArrayList<Float> completed_calls_array=new ArrayList<Float>();
	private ArrayList<Float> missed_calls_array=new ArrayList<Float>();
	private ArrayList<Float> cancelled_calls_array=new ArrayList<Float>();
	private ArrayList<Float> abandoned_calls_array=new ArrayList<Float>();
	ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
	public static ArrayList selected_queues=new ArrayList();
	Handler handler;

	private int con_checker;
	private ArrayList<String> bar_name_array =new ArrayList<String>();;
	private ArrayList<ArrayList> bar_arrayvalues_array=new ArrayList<ArrayList>();
	private ArrayList selected_queues_id=new ArrayList();

	EditText editText_fromDate,editText_toDate,editText_queues;
	Spinner spinner_queue,spinner_option,spinner_project;
	SwipeRefreshLayout swipeLayout;
	int refresh_checker = 0;


	public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {

		View fragment_barchart_agent = inflater.inflate(
				R.layout.barchart_agent_layout, container, false);

		swipeLayout = (SwipeRefreshLayout)fragment_barchart_agent.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		Button filter_dialog=(Button)fragment_barchart_agent.findViewById(R.id.filter_button_agent);
		filter_dialog.setOnClickListener(this);

		filter_list=new ArrayList<String>();
		
		filter_list.add("Select option");
		filter_list.add("Incoming Agent");
		filter_list.add("Outgoing Agent");
		
		System.out.println(filter_list);
		Spinner spinner=(Spinner)fragment_barchart_agent.findViewById(R.id.spinner_queue_id);
		
	ArrayAdapter array_adapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, filter_list);
	
		spinner.setAdapter(array_adapter);
		spinner.setOnItemSelectedListener(this);
		
		
		 chart = (HorizontalBarChart) fragment_barchart_agent.findViewById(R.id.chart_agent);
		
       
		return fragment_barchart_agent;
        
       
    }
	
	private void barchart() {
		
		
		
		 BarData data = new BarData(getXAxisValues(), getDataSet());
	        chart.setData(data);
	        chart.setDescription("Agent bar");
	        chart.animateXY(2500, 2500);
	        chart.invalidate();
	        chart.setDrawGridBackground(false);
	       // chart.setDrawValuesForWholeStack(false);
	        chart.setDrawValueAboveBar(false);
		chart.setOnTouchListener(new View.OnTouchListener() {
			final ArrayList list = new ArrayList();
			final Runnable mLongPressed = new Runnable() {
				public void run() {
					Log.i("", "Long press!");
					Home_Team.charts_dialog(getActivity(), "", list);

				}
			};
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					list.clear();
					list.add("Allocation");
					list.add("Activities");
					list.add("Invoices");
					list.add("Leads");

					handler.postDelayed(mLongPressed, 1000);}
				if((event.getAction() == MotionEvent.ACTION_MOVE)||(event.getAction() == MotionEvent.ACTION_UP))
					handler.removeCallbacks(mLongPressed);
				return false;
			}
		});
	        
	        //handle click on chart selected
	        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
				
				@Override
				public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
					// TODO Auto-generated method stub
					
					Toast toast= Toast.makeText(getActivity(), 
							e.getVal()+"", Toast.LENGTH_SHORT);  
							toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_HORIZONTAL, 0, 0);
							toast.show();
							
				}
				
				@Override
				public void onNothingSelected() {
					// TODO Auto-generated method stub
					
				}
			});
		
		
	}
 
    private ArrayList<IBarDataSet> getDataSet() {
      
 
        System.out.println("bar size:  " + bar_name_array);
        
        
        
        for (int i = 0; i < bar_name_array.size(); i++) {
			
            ArrayList<BarEntry> calls_values = new ArrayList<BarEntry>();
            ArrayList list=new ArrayList();
             list=bar_arrayvalues_array.get(i);
            System.out.println(list);
            	

            for (int j = 0; j < list.size(); j++) {
    			
            	System.out.println(list.get(j).getClass());
            	BarEntry value = new BarEntry((Float) list.get(j), j); 
            	calls_values.add(value);
    		}
            
            
            BarDataSet barDataSet = new BarDataSet(calls_values, bar_name_array.get(i));
           
            		switch (i) {
					case 0:
	            		barDataSet.setColor(Color.rgb(137, 165, 78));//green color for completed calls

						break;
					case 1:
			            barDataSet.setColor(Color.rgb(170,70,67));//red color for missed calls

						break;
					case 2:
			            barDataSet.setColor(Color.rgb(69, 114, 167));//blue color for non completed calls 

						break;
					case 3:
			        	barDataSet.setColor(Color.rgb(128, 105, 155));//purple color for abandoned calls

						break;
					case 4:
			        	barDataSet.setColor(Color.rgb(255, 255, 0));//yellow color for abandoned calls

						break;
					case 5:
						
						break;
					case 6:
						
						break;
					case 7:
						
						break;
						
					default:
						break;
					}
            
            dataSets.add(barDataSet);
            
            System.out.println(dataSets);
		}
        
        return dataSets;
        
    }
 
    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<String>();
        
      xAxis= Home.agent_con.getArray_of_agent();
       
       
        return xAxis;
    }

	private void dialog_filter(){

		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.dialog_filter_agent, null);

		final AlertDialog.Builder builder =new AlertDialog.Builder(getContext());

		builder.setTitle("Filtering");
		builder.setView(v);

		builder.setNegativeButton("Cancal", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});

		builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {


				if (editText_fromDate.getText().toString().equalsIgnoreCase("") || editText_toDate.getText().toString().equalsIgnoreCase("") || editText_queues.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(getActivity(), "Please fill out all fields ", Toast.LENGTH_LONG).show();
				} else {
					chart.clear();
					con_checker = 0;
					completed_calls_array.clear();
					missed_calls_array.clear();
					cancelled_calls_array.clear();
					abandoned_calls_array.clear();
					bar_arrayvalues_array.clear();
					dataSets.clear();
					bar_name_array.clear();

					// list.clear();


					String url = "http://" + "41.32.29.164" + "/Android/Incoming_Queue/Incoming_Agent.php";

					String url_out = "http://" + "41.32.29.164" + "/Android/Incoming_Queue/Outgoing_Agent.php";

					System.out.println("size" + selected_queues_id);

					if (option_spinner_itemSelected.equalsIgnoreCase("Incoming Agent")) {


						for (int i = 0; i < selected_queues_id.size(); i++) {

							if (con_checker == 1) {
								break;
							} else {


								//Barchart_Agent_Connection con = new Barchart_Agent_Connection(getContext(), "Incoming Agent", i, selected_queues_id.size(), true);
								//con.execute(url, selected_queues_id.get(i).toString());


							}

						}
					} else if (option_spinner_itemSelected.equalsIgnoreCase("Outgoing Agent")) {

						for (int i = 0; i < selected_queues_id.size(); i++) {

							if (con_checker == 1) {
								break;
							} else {


								//Barchart_Agent_Connection con = new Barchart_Agent_Connection(getContext(), "Outgoing Agent", i, selected_queues_id.size(), true);
								//con.execute(url_out, selected_queues_id.get(i).toString());


							}

						}


					} else {
						Toast.makeText(getActivity(), "Select option", Toast.LENGTH_SHORT).show();
					}
				}

			}
		});


		spinner_project=(Spinner)v.findViewById(R.id.spiner_id);
		editText_fromDate=(EditText)v.findViewById(R.id.fromDate_id);
		editText_toDate=(EditText)v.findViewById(R.id.toDate_id);
		editText_queues=(EditText)v.findViewById(R.id.queue_id);
		spinner_option=(Spinner)v.findViewById(R.id.dialog_filter_spinner);
		spinner_queue=(Spinner)v.findViewById(R.id.spiner_queue_id);



		System.out.println("arr222 :: " + Home.queue_con.getArray_of_queues());
		ArrayList spinner_projects_list=new ArrayList();

		spinner_projects_list.add("Select Project");
		for (int i=0;i< Projects_Connection.getArray_of_projects().size();i++){spinner_projects_list.add(Projects_Connection.getArray_of_projects().get(i));}

		ArrayAdapter adapt= new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,spinner_projects_list );
		ArrayAdapter array_adapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, filter_list);

		spinner_queue.setAdapter(adapt);
		spinner_queue.setOnItemSelectedListener(this);
		spinner_queue.setPrompt("Select Project");

		spinner_option.setAdapter(array_adapter);
		spinner_option.setOnItemSelectedListener(this);


		editText_fromDate.setOnClickListener(this);
		editText_toDate.setOnClickListener(this);
		editText_queues.setOnClickListener(this);



		builder.show();
	}

	private void multiSelect_dialog(){

		selected_queues_id.clear();
		selected_queues.clear();

		final AlertDialog.Builder build=new AlertDialog.Builder(getActivity());
		build.setTitle("Select one or more");



		if(project_spinner_itemSelected.equalsIgnoreCase("Select Project")) {
			boolean defult_check[] = new boolean[Home.agent_con.getArray_of_agent().size()];
			//set all defult_check false
			for (int i = 0; i < defult_check.length; i++) {
				defult_check[i] = false;
			}

			build.setMultiChoiceItems(Home.agent_con.getArray_of_agent().toArray(new CharSequence[Home.agent_con.getArray_of_agent().size()]), defult_check, new DialogInterface.OnMultiChoiceClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {


					if(isChecked){

						selected_queues.add(Home.agent_con.getArray_of_agent().get(which));
						selected_queues_id.add(Home.agent_con.getArray_of_agent_id().get(which));

					}else if (selected_queues.contains(which)){
						selected_queues.remove(Integer.valueOf(which));
						selected_queues_id.remove(Integer.valueOf(which));


					}

				}
			});

		}else {
/*
			boolean defult_check[] = new boolean[queues_con.getArray_of_queues().size()];
			//set all defult_check false
			for (int i = 0; i < defult_check.length; i++) {
				defult_check[i] = false;
			}

			build.setMultiChoiceItems(queues_con.getArray_of_queues().toArray(new CharSequence[queues_con.getArray_of_queues().size()]), defult_check, new DialogInterface.OnMultiChoiceClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {


					if(isChecked){

						selected_queues.add(queues_con.getArray_of_queues().get(which));
						selected_queues_id.add(queues_con.getArray_of_queues_id().get(which));
					}else if (selected_queues.contains(which)){
						selected_queues.remove(Integer.valueOf(which));
						selected_queues_id.remove(Integer.valueOf(which));

					}

				}
			});
*/
		}



		build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {


			}
		});

		build.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (selected_queues.size()==0){Toast.makeText(getActivity(),"Nothing Selected",Toast.LENGTH_SHORT).show();build.show();}
				else {
					for (int i = 0; i < selected_queues.size(); i++) {
						editText_queues.append(selected_queues.get(i) + ",");
					}
				}
			}
		});

		build.show();

	}

	private void datepicker_dialog(final String selected){


		Calendar now = Calendar.getInstance();
		DatePickerDialog dpd = DatePickerDialog.newInstance(
				this,
				now.get(Calendar.YEAR),
				now.get(Calendar.MONTH),
				now.get(Calendar.DAY_OF_MONTH)
		);

		dpd.dismissOnPause(true);
		dpd.vibrate(false);
		dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

		dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});

		dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
				String date;
				date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
				if (selected.equalsIgnoreCase("fromDate")) {
					editText_fromDate.setText(date);
				} else if (selected.equalsIgnoreCase("toDate")) {
					editText_toDate.setText(date);
				}
			}
		});

	}


	@Override
	public void onClick(View v) {

		switch (v.getId()){

			case R.id.fromDate_id :

				datepicker_dialog("fromDate");

				break;

			case R.id.toDate_id :


				datepicker_dialog("toDate");

				break;

			case R.id.queue_id:
				selected_queues.clear();
				editText_queues.setText("");
				multiSelect_dialog();
				break;

			case R.id.filter_button_agent:

				dialog_filter();

				break;





		}
	}

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

	}

	@Override
	public void onRefresh() {
		swipeLayout.setRefreshing(false);
		switch (item_selectd) {

			case "Select option":

				chart.clear();
				//Toast.makeText(getContext(), "Choose one", Toast.LENGTH_SHORT).show();


				break;

			case "Incoming Agent":

				chart.clear();
				con_checker=0;
				completed_calls_array.clear();
				missed_calls_array.clear();
				cancelled_calls_array.clear();
				abandoned_calls_array.clear();
				dataSets.clear();
				bar_name_array.clear();
				bar_arrayvalues_array.clear();
				refresh_checker=1;

				//String url_in="http://"+Login.enteredIP+"/Android/test/Incoming_Agent.php";
				String url_in="http://"+"41.32.29.164"+"/Android/Incoming_Queue/Incoming_Agents.php";

				System.out.println("size"+Home.agent_con.getArray_of_agent_id());




				for (int i = 0; i <Home.agent_con.getArray_of_agent_id().size() ; i++) {

					if (con_checker==1) {
						break;
					}else {
						Barchart_Agent_Connection con=new Barchart_Agent_Connection(getContext(),"Incoming Agent",i);

						con.execute(url_in,Home.agent_con.getArray_of_agent_id().get(i));

					}

				}


				break;

			case "Outgoing Agent":

				chart.clear();
				con_checker=0;
				completed_calls_array.clear();
				missed_calls_array.clear();
				cancelled_calls_array.clear();
				abandoned_calls_array.clear();
				bar_name_array.clear();
				dataSets.clear();
				bar_arrayvalues_array.clear();

				refresh_checker=1;

				//String url_in="http://"+Login.enteredIP+"/Android/test/Incoming_Agent.php";
				String url_out="http://"+ "41.32.29.164"+"/Android/Incoming_Queue/Outgoing_Agent.php";
				System.out.println("size"+Home.agent_con.getArray_of_agent_id());




				for (int i = 0; i <Home.agent_con.getArray_of_agent_id().size() ; i++) {

					if (con_checker==1) {
						break;
					}else {

						Barchart_Agent_Connection con=new Barchart_Agent_Connection(getContext(),"Outgoing Agent",i);

						con.execute(url_out,Home.agent_con.getArray_of_agent_id().get(i));

					}

				}


				break;

			default:
				break;
		}

	}


	public class Barchart_Agent_Connection extends AsyncTask<String,Void, String>	{

		Context context;
		String option;
		Dialog dialog;
		int loop_no;
		int no_bars;
		int con_lossed=0;
		
    	public Barchart_Agent_Connection(Context context,String option,int loop_no) {
			// TODO Auto-generated constructor stub
    		
			this.context = context;
			this.option=option;
    		this.loop_no=loop_no;
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
	        	 

	             List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

					
	           //  nameValuePairs.add(new BasicNameValuePair("username", params[1]));//sent from data

	           //  nameValuePairs.add(new BasicNameValuePair("password", params[2]));// sent to data
	             
	             nameValuePairs.add(new BasicNameValuePair("Select1", params[1]));//sent agent id
	             
	             
	             httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	             HttpResponse response = httpClient.execute(httpPost);

	             //get data from the server
	             jsonResult = inputStreamToString(response.getEntity().getContent()).toString();


	         } catch (ClientProtocolException e) {

	             e.printStackTrace();
	        	 System.out.println(e.toString());
	        	 con_checker=1;
	        	 con_lossed=1;

	         } catch (IOException e) {

	             e.printStackTrace();
	        	 System.out.println(e.toString());
	        	 con_checker=1;
	        	 con_lossed=1;

	         }catch (Exception e) {
				// TODO: handle exception
	        	 System.out.println(e.toString());
	        	 con_checker=1;
	        	 con_lossed=1;
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

				switch (no_bars) {
				case 3:
					
					 completed_calls_array.add(json_result.get(0));
					 missed_calls_array.add(json_result.get(1)); 
					 cancelled_calls_array.add(json_result.get(2));
					
					 
					 bar_arrayvalues_array.add(completed_calls_array);
					 bar_arrayvalues_array.add(missed_calls_array);
					 bar_arrayvalues_array.add(cancelled_calls_array);
					 
					
					break;
				case 4:
					
					 completed_calls_array.add(json_result.get(0));
					 missed_calls_array.add(json_result.get(1)); 
					 cancelled_calls_array.add(json_result.get(2));
					 abandoned_calls_array.add(json_result.get(3));
					
					 
					 bar_arrayvalues_array.add(completed_calls_array);
					 bar_arrayvalues_array.add(missed_calls_array);
					 bar_arrayvalues_array.add(cancelled_calls_array);
					 bar_arrayvalues_array.add(abandoned_calls_array);
					 
					break;
				case 5:
					 
					break;
				case 6:
					
					
					
					break;
					
					

				default:
					break;
				}

				
				
				if ((loop_no+1)==Home.agent_con.getArray_of_agent_id().size()&&con_checker!=1) {
					
				 barchart();
				}
				
				 System.out.println("array " +cancelled_calls_array);

				
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
	            
	            no_bars =Integer.valueOf( resultObject_0.getString("no_bars"));
			    System.out.println("no bar:  "+no_bars);
			    if (loop_no==0) {
					
			    for (int i = 1; i <= no_bars; i++) {
					
			    String    bar_name = resultObject_0.getString("bar"+i+"_name");
			    bar_name_array.add(bar_name);

				}
			    System.out.println(bar_name_array);
			    
			    }
	            
	            resultObject_1 = myJsonArray.getJSONObject(1);
	            
	            if (option=="Incoming Agent") {
	            	
	            	float    completed_calls =Float.valueOf( resultObject_1.getString("completed_calls"));
	            	float    cancelled_calls =Float.valueOf( resultObject_1.getString("missed_calls"));
	            	float    missed_calls = Float.valueOf(resultObject_1.getString("cancelled_calls"));
	            	float    abandoned_calls = Float.valueOf(resultObject_1.getString("abandoned_calls"));

	            	result_array.add(completed_calls);
	            	result_array.add(missed_calls);
	            	result_array.add(cancelled_calls);
	            	result_array.add(abandoned_calls);
	            	
	            }else if(option=="Outgoing Agent") {

					float    completed_calls =Float.valueOf( resultObject_1.getString("completed_calls"));
					float    cancelled_calls =Float.valueOf( resultObject_1.getString("cancelled_calls"));
					float    missed_calls = Float.valueOf(resultObject_1.getString("missed_calls"));

					System.out.println("arraaaaaaaay   :"+completed_calls);

					result_array.add(completed_calls);
					result_array.add(missed_calls);
					result_array.add(cancelled_calls);

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


		 

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		

			item_selectd=(String)parent.getItemAtPosition(position);
			switch (item_selectd) {

				case "Select option":

					chart.clear();
					//Toast.makeText(getContext(), "Choose one", Toast.LENGTH_SHORT).show();

					break;

				case "Incoming Agent":

					chart.clear();
					con_checker=0;
					completed_calls_array.clear();
					missed_calls_array.clear();
					cancelled_calls_array.clear();
					abandoned_calls_array.clear();
					dataSets.clear();
					bar_name_array.clear();
					bar_arrayvalues_array.clear();

					//String url_in="http://"+Login.enteredIP+"/Android/test/Incoming_Agent.php";
					String url_in="http://"+"41.32.29.164"+"/Android/Incoming_Queue/Incoming_Agent.php";
					System.out.println("size"+Home.agent_con.getArray_of_agent_id());




					for (int i = 0; i <Home.agent_con.getArray_of_agent_id().size() ; i++) {

						if (con_checker==1) {
							break;
						}else {
							Barchart_Agent_Connection con=new Barchart_Agent_Connection(getContext(),"Incoming Agent",i);

							con.execute(url_in,Home.agent_con.getArray_of_agent_id().get(i));

						}

					}


					break;

				case "Outgoing Agent":

					chart.clear();
					con_checker=0;
					completed_calls_array.clear();
					missed_calls_array.clear();
					cancelled_calls_array.clear();
					abandoned_calls_array.clear();
					bar_name_array.clear();
					dataSets.clear();
					bar_arrayvalues_array.clear();

					//String url_in="http://"+Login.enteredIP+"/Android/test/Incoming_Agent.php";
					String url_out="http://"+ "41.32.29.164"+"/Android/Incoming_Queue/Outgoing_Agent.php";
					System.out.println("size"+Home.agent_con.getArray_of_agent_id());




					for (int i = 0; i <Home.agent_con.getArray_of_agent_id().size() ; i++) {

						if (con_checker==1) {
							break;
						}else {

							Barchart_Agent_Connection con=new Barchart_Agent_Connection(getContext(),"Outgoing Agent",i);

							con.execute(url_out,Home.agent_con.getArray_of_agent_id().get(i));

						}

					}


					break;

				default:
					break;
			}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}


		
		
		

    
	

}
