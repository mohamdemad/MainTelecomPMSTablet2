package main.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.MainTelecom_Tablet.Adapter.HomePagerAdapter;
import com.MainTelecom_Tablet.CustomClasses.C_ViewPager;
import com.MainTelecom_Tablet.R;
import com.MainTelecom_Tablet.SharedPreference.MyHomeChartsSharedPreference;
import com.MainTelecom_Tablet.SharedPreference.SaveSharedPreference;



public class App_Home extends ActionBarActivity  {

//	TabLayout tabLayout;
//	ViewPager viewPager;
//	TabLayout list_tabs;
//	ViewPager list_viewPager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);

		if(!MyHomeChartsSharedPreference.getFIRST_Time(this).equalsIgnoreCase("done"))
		{
			MyHomeChartsSharedPreference.ChangePagerName(this,"Department Activities","PAGER_1");
			MyHomeChartsSharedPreference.ChangePagerName(this,"Department Effort","PAGER_2");
			MyHomeChartsSharedPreference.ChangePagerName(this,"Activities Achieved","PAGER_3");
			MyHomeChartsSharedPreference.ChangePagerName(this,"Activities Planned","PAGER_4");
			MyHomeChartsSharedPreference.ChangePagerName(this,"Effort Planned","PAGER_5");
			MyHomeChartsSharedPreference.ChangePagerName(this,"Effort Achieved","PAGER_6");

			MyHomeChartsSharedPreference.ChangeItemName(this, "Incoming Calls Agent",        "LIST_1_ITEM_0");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Outgoing Calls Agent",        "LIST_1_ITEM_1");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Hourly Incoming Calls Agent", "LIST_1_ITEM_2");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Hourly Outgoing Calls Agent", "LIST_1_ITEM_3");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Incoming Calls Queue",        "LIST_1_ITEM_4");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Outgoing Calls Queue",        "LIST_1_ITEM_5");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Hourly Incoming Calls Queue", "LIST_1_ITEM_6");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Hourly Outgoing Calls Queue", "LIST_1_ITEM_7");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Working Hours",               "LIST_1_ITEM_8");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Contact Center Gauges",       "LIST_1_ITEM_9");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Leads/Industry",              "LIST_1_ITEM_10");

			MyHomeChartsSharedPreference.ChangeItemName(this, "Organization Activities", "LIST_2_ITEM_0");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Organization Allocation", "LIST_2_ITEM_1");
			MyHomeChartsSharedPreference.ChangeItemName(this, "Invoices/Incoming Calls", "LIST_2_ITEM_2");
			MyHomeChartsSharedPreference.ChangeItemName(this,"Leads/Outgoing Calls",     "LIST_2_ITEM_3");
			MyHomeChartsSharedPreference.ChangeItemName(this,"Workforce Management",     "LIST_2_ITEM_4");

			SaveSharedPreference.setTab3Check(this,"Team WFM Home");

			MyHomeChartsSharedPreference.setFIRST_Time(this,"done");
		}


		/*******************************************************************************************/

		/** Main Tabs & pagers*/
		//main tab
		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		tabLayout.addTab(tabLayout.newTab().setText("My Home"));
		tabLayout.addTab(tabLayout.newTab().setText(SaveSharedPreference.getTab3Check(this)));
		tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
		final C_ViewPager viewPager = (C_ViewPager) findViewById(R.id.pager);

		try {


			// main pager
			//final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
			viewPager.setOffscreenPageLimit(3);
			final HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), 2, this, "main");
			viewPager.setAdapter(adapter);
			viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//		viewPager.setOnTouchListener(new View.OnTouchListener() {
//
//      @Override
//      public boolean onTouch(View v, MotionEvent event) {
//		         if (viewPager.getCurrentItem() <= 1) {
//		         viewPager.onTouchEvent(event);
//		         viewPager.setCurrentItem(viewPager.getCurrentItem());
//		         viewPager.onTouchEvent(event);
//		}
//
//		return false;
//
//		}
//		});
		}catch (Exception e){}

		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				//

				viewPager.setCurrentItem(tab.getPosition());
//                 if(i==0){
//                tabLayout.addTab(tabLayout.newTab().setText("ooooooooooo"));
//                final HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),getApplicationContext());
//                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//                     viewPager.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                 i++;}
				//i = tab.getPosition();
			}
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}
			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

		/***********************************************************************************************/

		/** List tabs & pagers**/
		//list tab
		TabLayout list_tabs = (TabLayout) findViewById(R.id.tab_list_layout);
		list_tabs.addTab(list_tabs.newTab().setText("List"));
		list_tabs.setTabGravity(list_tabs.GRAVITY_FILL);

		// list pager
		final ViewPager list_viewPager = (ViewPager) findViewById(R.id.list_pager);
		final HomePagerAdapter list_adapter = new HomePagerAdapter(getSupportFragmentManager(), 1, this,"list");
		list_viewPager.setAdapter(list_adapter);
		list_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(list_tabs));

		list_tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {

			}
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
                if(list_viewPager.getVisibility()== View.GONE){
                    list_viewPager.setVisibility(View.VISIBLE);
                }else{
                    list_viewPager.setVisibility(View.GONE);
                }
			}
		});


		/***********************************************************************************************/
	}

		/** public ViewPager getViewPager (){
		 //Fragment fragment = ((HomePagerAdapter)viewPager.getAdapter()).getFragment(2);

		 return viewPager;
		 }

		 public Fragment getFrag (){
		 Fragment fragment = ((HomePagerAdapter)viewPager.getAdapter()).getFragment(2);

		 return fragment;
		 }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);


    }
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//
//			switch (v.getId()) {
//
//
//				default:
//					break;
//			}
//
//		}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_logout, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.logout_id:


				finish();

				startActivity(new Intent(this,Login.class));

				break;

		}
		return super.onOptionsItemSelected(item);
	}


}


