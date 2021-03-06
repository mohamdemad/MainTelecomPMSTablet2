package main.database_connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Projects_Connection extends AsyncTask<String, Void, String> {

	Context context;
	//String key;
	//public AsyncReturn delegate = null;
	static ArrayList<String> array_of_projects;

	public static ArrayList<String> getArray_of_projects_id() {
		return array_of_projects_id;
	}

	public static void setArray_of_projects_id(ArrayList<String> array_of_projects_id) {
		Projects_Connection.array_of_projects_id = array_of_projects_id;
	}

	static ArrayList<String> array_of_projects_id;


	public static ArrayList<String> getArray_of_projects() {
		return array_of_projects;
	}


	public void setArray_of_projects(ArrayList<String> array_of_projects) {
		this.array_of_projects = array_of_projects;
	}


	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
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

			Log.i("result is hereeeee", result);
			System.out.println("Resulted Value: " + result);


			ArrayList<String> jsonResult = returnParsedJsonObject(result);
			setArray_of_projects(jsonResult);
			// Log.i("parsed resultttt", String.valueOf(jsonResult));

			 System.out.println("int= " + jsonResult);

			

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
		ArrayList<String> Result_id = new ArrayList<String>() ;

		try {
			
			JSONArray myJsonArray = new JSONArray(result);
			no_row_Object = myJsonArray.getJSONObject(0);
		String	no_row = no_row_Object.getString("no_row");
		int no_row_int=Integer.valueOf(no_row);
		System.out.println("cccc"+no_row_int);
		
		for (int i = 1; i <= no_row_int; i++) {
			
			queue_object = myJsonArray.getJSONObject(i);
			String queue_name=queue_object.getString("Project_Name");
			String queue_id=queue_object.getString("Project_id");

			Result.add(queue_name);
			Result_id.add(queue_id);
			System.out.println(queue_name);
		}
			setArray_of_projects_id(Result_id);
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
