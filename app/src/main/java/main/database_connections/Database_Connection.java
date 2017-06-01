package main.database_connections;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import main.activities.App_Home;


public class Database_Connection extends AsyncTask<String, Void, String> {

	Dialog dialog;
	Context context;

	public Database_Connection(Context context) {
		this.context=context;
	//	 linlaHeaderProgress = (RelativeLayout)((Activity) context).findViewById(R.id.linlaHeaderProgress);
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		// linlaHeaderProgress.setVisibility(View.VISIBLE);
		 
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
        	 HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        	 HttpConnectionParams.setSoTimeout(httpParameters, 5000);
        	 
        	 
        	 HttpClient httpClient = new DefaultHttpClient(httpParameters);
        	 
        	 HttpPost httpPost = new HttpPost(params[0]);
        	 

             List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

             nameValuePairs.add(new BasicNameValuePair("username", params[1]));

             nameValuePairs.add(new BasicNameValuePair("password", params[2]));

             httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
             HttpResponse response = httpClient.execute(httpPost);

			 Log.d("Know:::::",httpPost.toString());

             //get data from the server
             jsonResult = inputStreamToString(response.getEntity().getContent()).toString();


         } catch (ClientProtocolException e) {

             e.printStackTrace();
        	 System.out.println(e.toString());


         } catch (IOException e) {

             e.printStackTrace();
        	 System.out.println(e.toString());

         }catch (Exception e) {
			// TODO: handle exception
        	 System.out.println(e.toString());
		}
         return jsonResult;

		
		
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		

		//linlaHeaderProgress.setVisibility(View.GONE);
		
		try {
		
		  Log.i("result is hereeeee", result);
          System.out.println("Resulted Value: " + result);

          if (result.equals("") || result == null) {
			  dialog.dismiss();

             Toast.makeText(context,"Server connection failed",Toast.LENGTH_LONG).show();

              return;
          }
          String jsonResult = returnParsedJsonObject(result);
          Log.i("parsed resultttt",String.valueOf(jsonResult));

             if(jsonResult.equalsIgnoreCase("valid")){
                  Toast.makeText(context,"Welcome",Toast.LENGTH_LONG).show();
                  Intent intent = new Intent(context, App_Home.class);
				  intent.addCategory(Intent.CATEGORY_HOME);
				 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 dialog.dismiss();
				 context.startActivity(intent);



                  return;
              }
              else {
				 dialog.dismiss();
                  Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show();
              }

         	
		} catch (Exception e) {
			// TODO: handle exception
			dialog.dismiss();
            Toast.makeText(context,"Server connection failed",Toast.LENGTH_LONG).show();
            System.out.println(e.toString());
		}
		
		super.onPostExecute(result);
	}
	
	  public String returnParsedJsonObject(String result){

	        JSONObject resultObject  = null;

	        String loginResult = null  ;
	        try {
	            JSONArray myJsonArray = new JSONArray(result);
	            resultObject = myJsonArray.getJSONObject(0);
	            loginResult = resultObject.getString("login");
	            Log.i("parseeeed result",loginResult);

	        }catch (JSONException e){
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
