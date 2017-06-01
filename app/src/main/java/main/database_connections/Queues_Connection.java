package main.database_connections;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.MainTelecom_Tablet.R;

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
import java.util.List;


public class Queues_Connection extends AsyncTask<String, Void, String> {

	Context context;
	boolean start_ProgressBar;
	String projectID;
	 ArrayList<String> array_of_queues;
	 ArrayList<String> array_of_queues_id;
	 ArrayList<String> Result_id;
	Dialog dialog;


	public  ArrayList<String> getArray_of_queues_id() {
		return array_of_queues_id;
	}


	public void setArray_of_queues_id(ArrayList<String> array_of_queues_id) {
		this.array_of_queues_id = array_of_queues_id;
	}


	public  ArrayList<String> getArray_of_queues() {
		return array_of_queues;
	}


	public void setArray_of_queues(ArrayList<String> array_of_queues) {
		this.array_of_queues = array_of_queues;
	}


	public Queues_Connection(Context context, Boolean start_ProgressBar,String projectID){
		
		this.context = context;
		this.start_ProgressBar = start_ProgressBar;
		this.projectID=projectID;
		
	}
	
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub


		if (start_ProgressBar==true) {

			dialog = new Dialog(context, R.style.custom_dialog);
			dialog.setContentView(R.layout.progress_spinner);
			dialog.setCancelable(false);
			dialog.show();
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

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

			nameValuePairs.add(new BasicNameValuePair("projID",projectID)); //sent project id


			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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

		if (start_ProgressBar==true){dialog.dismiss();}
		try {

			Log.i("result is hereeeee", result);
			System.out.println("Resulted Value: " + result);

			if (result.equals("") || result == null) {

				Toast.makeText(context, "Server connection failed",
						Toast.LENGTH_LONG).show();

				return;
			}
			ArrayList<String> jsonResult = returnParsedJsonObject(result);
			ArrayList<String> jsonResult_id = Result_id;

			setArray_of_queues(jsonResult);
			setArray_of_queues_id(jsonResult_id);
			// Log.i("parsed resultttt", String.valueOf(jsonResult));

			 System.out.println("int= " + jsonResult_id);

			

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, "Server connection failed",
					Toast.LENGTH_LONG).show();

		}
		
		super.onPostExecute(result);
	}

	public ArrayList<String> returnParsedJsonObject(String result) {

		JSONObject no_row_Object = null,queue_object=null;

		ArrayList<String> Result = new ArrayList<String>() ;
		 Result_id = new ArrayList<String>() ;
		try {
			
			JSONArray myJsonArray = new JSONArray(result);
			no_row_Object = myJsonArray.getJSONObject(0);
		String	no_row = no_row_Object.getString("no_row");
		int no_row_int=Integer.valueOf(no_row);
		System.out.println("cccc"+no_row_int);
		
		for (int i = 1; i <= no_row_int; i++) {
			
			queue_object = myJsonArray.getJSONObject(i);
			String queue_name=queue_object.getString("Queue_Name");
			String queue_id=queue_object.getString("Queue_id");
			
			Result.add(queue_name); 
			Result_id.add(queue_id);
			System.out.println(queue_id);
		}
		System.out.println(Result);

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
