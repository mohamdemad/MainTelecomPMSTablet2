package main.activities;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.MainTelecom_Tablet.R;


public class About extends ActionBarActivity {
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		

		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//getMenuInflater().inflate(R.menu.home, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	

}
