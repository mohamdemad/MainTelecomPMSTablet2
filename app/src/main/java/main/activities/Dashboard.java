package main.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.MainTelecom_Tablet.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import main.charts.Barchart_Agent;
import main.charts.Barchart_Queue;
import main.swapingTabs.SlidingTabLayout;

public class Dashboard extends ActionBarActivity implements OnClickListener{

	Toolbar toolbar;
	ViewPager pager;
	ViewPagerAdapter adapter;
	SlidingTabLayout tabs;
	CharSequence Titles[] = {"Agent", "Queue"};
	int Numboftabs = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_layout);

		toolbar = (Toolbar) findViewById(R.id.app_bar_id);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayShowHomeEnabled(true);


		// Creating The ViewPagerAdapter and Passing Fragment Manager, Titles
		// fot the Tabs and Number Of Tabs.
		adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles,
				Numboftabs);

		// Assigning ViewPager View and setting the adapter
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		// Assiging the Sliding Tab Layout View
		tabs = (SlidingTabLayout) findViewById(R.id.tabs);
		tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
										// This makes the tabs Space Evenly in
										// Available width

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});

		// Setting the ViewPager For the SlidingTabsLayout
		tabs.setViewPager(pager);
		
		
								// ////////////create floating buttons///////////////

		//create main float button
				ImageView menu_icon = new ImageView(this); // Create an icon
				menu_icon.setImageResource(R.drawable.menu_icon);
				
				

				FloatingActionButton menu_action_button = new FloatingActionButton.Builder(
						this).setContentView(menu_icon).build();
				
				menu_action_button.getLayoutParams().height=120;
				menu_action_button.getLayoutParams().width=120;
				menu_action_button.requestLayout();
				menu_action_button.setBackgroundColor(Color.parseColor("#00000000"));

				
				//create sub float buttons
				SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
				// repeat many times:
				ImageView home_icon = new ImageView(this);
				home_icon.setImageResource(R.mipmap.home_icon);

				SubActionButton home_action_button = itemBuilder.setContentView(home_icon).build();
				home_action_button.setOnClickListener(this);
				home_action_button.setId(R.id.home_floating_id);

				home_action_button.setBackgroundColor(Color.parseColor("#00000000"));
				home_action_button.getLayoutParams().height=100;
				home_action_button.getLayoutParams().width=100;

				
				//add sub float buttons in ui
				FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
						.addSubActionView(home_action_button)
						.attachTo(menu_action_button).build();

		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		startActivity(new Intent(this,App_Home.class));
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

	private class ViewPagerAdapter extends FragmentStatePagerAdapter {

		CharSequence Titles[]; // This will Store the Titles of the Tabs which are
								// Going to be passed when ViewPagerAdapter is
								// created
		int NumbOfTabs; // Store the number of tabs, this will also be passed when
						// the ViewPagerAdapter is created

		public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
			super(fm);
			this.Titles = mTitles;
			this.NumbOfTabs = mNumbOfTabsumb;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub

			switch (arg0) {
			
			case 0:
				Barchart_Agent barchart_agent = new Barchart_Agent();
				return barchart_agent;

			case 1:
				Barchart_Queue barchart_queue = new Barchart_Queue();
				return barchart_queue;

				
			default :
				
				return null;

			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return Titles[position];
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return NumbOfTabs;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.home_floating_id:
			
			startActivity(new Intent(getApplicationContext(), App_Home.class));
			finish();
			break;

		default:
			break;
		}
		
	}
	

	}


