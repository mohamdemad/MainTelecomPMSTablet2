package main.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.MainTelecom_Tablet.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

import main.charts.Activities_Achieved;
import main.charts.Activities_Planned;
import main.charts.Barchart_Working;
import main.charts.Department_Activities_BarChart;
import main.charts.Department_Effort_BarChart;
import main.charts.Effort_Achieved;
import main.charts.Effort_Planned;
import main.charts.PieChart_Activities;
import main.charts.PieChart_Allocation;
import main.charts.PieChart_Invoices;
import main.charts.PieChart_Leads;
import main.charts.Speedometer;
import main.charts.WorkForce;
import main.fragments.Agent_Monitor;
import main.fragments.Invoices;
import main.swapingTabs.SlidingTabLayout;

public class Home_Team extends Fragment implements OnClickListener{

	Toolbar toolbar;
	ViewPager pager_working,pager_allocation,pager_activities;
	ViewPagerAdapter adapter_working,adapter_allocation,adapter_activities;
	SlidingTabLayout tabs;
	CharSequence Titles[] = {""};
	int Numboftabs = 1;
	static ArrayList<String> chart_filter=new ArrayList<String>();
	static String singel_item_selected="";
	SharedPreferences sp;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View fragment_home_team = inflater.inflate(
				R.layout.home_team_layout, container, false);

	// Creating The ViewPagerAdapter and Passing Fragment Manager, Titles
		// fot the Tabs and Number Of Tabs.
		sp = getActivity().getSharedPreferences("custom_home_1", Context.MODE_PRIVATE);
		int sp_size = sp.getAll().size();

		if (sp_size==0){
		adapter_working = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
				Numboftabs,"working","");

		adapter_allocation = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
				Numboftabs,"allocation","");

		adapter_activities = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
				Numboftabs,"activities","");}
		else{

			String item_top=sp.getString("top","");
			String item_left=sp.getString("left","");
			String item_right=sp.getString("right","");

			adapter_working = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
					Numboftabs,"working",item_top);

			adapter_allocation = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
					Numboftabs,"allocation",item_left);

			adapter_activities = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
					Numboftabs,"activities",item_right);

		}
		// Assigning ViewPager View and setting the adapter
		pager_working = (ViewPager)fragment_home_team.findViewById(R.id.pager_working);
		pager_allocation = (ViewPager)fragment_home_team.findViewById(R.id.pager_allocation);
		pager_activities = (ViewPager)fragment_home_team.findViewById(R.id.pager_activities);

		final Handler handler = new Handler();


		pager_working.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				final ArrayList list = new ArrayList();
				final Runnable mLongPressed = new Runnable() {
					public void run() {
						Log.i("", "Long press!");
					String item=charts_dialog(getActivity(), "top_left", list);

					}
				};

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					list.clear();
					list.add("Working Hour");
					list.add("Today");
					handler.postDelayed(mLongPressed, 1000);

				}
				if ((event.getAction() == MotionEvent.ACTION_MOVE) || (event.getAction() == MotionEvent.ACTION_UP))
					handler.removeCallbacks(mLongPressed);


				return false;
			}
		});
		pager_allocation.setOnTouchListener(new View.OnTouchListener() {


			final ArrayList list = new ArrayList();
			final Runnable mLongPressed = new Runnable() {
				public void run() {
					Log.i("", "Long press!");
					charts_dialog(getActivity(), "", list);

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
		pager_activities.setOnTouchListener(new View.OnTouchListener() {

			final ArrayList list = new ArrayList();
			final Runnable mLongPressed = new Runnable() {
				public void run() {
					Log.i("", "Long press!");
					charts_dialog(getActivity(), "", list);

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

		pager_working.setAdapter(adapter_working);
		pager_allocation.setAdapter(adapter_allocation);
		pager_activities.setAdapter(adapter_activities);




								// ////////////create floating buttons///////////////

		//create main float button
				ImageView menu_icon = new ImageView(getActivity()); // Create an icon
				menu_icon.setImageResource(R.drawable.menu_icon);
				
				

				FloatingActionButton menu_action_button = new FloatingActionButton.Builder(
						getActivity()).setContentView(menu_icon).build();
				
				menu_action_button.getLayoutParams().height=120;
				menu_action_button.getLayoutParams().width=120;
				menu_action_button.requestLayout();
				menu_action_button.setBackgroundColor(Color.parseColor("#00000000"));

				
				//create sub float buttons
				SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
				// repeat many times:
				ImageView home_icon = new ImageView(getActivity());
				home_icon.setImageResource(R.mipmap.home_icon);

				SubActionButton home_action_button = itemBuilder.setContentView(home_icon).build();
				home_action_button.setOnClickListener(this);
				home_action_button.setId(R.id.home_floating_id);

				home_action_button.setBackgroundColor(Color.parseColor("#00000000"));
				home_action_button.getLayoutParams().height=100;
				home_action_button.getLayoutParams().width=100;

				
				//add sub float buttons in ui
				FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
						.addSubActionView(home_action_button)
						.attachTo(menu_action_button).build();

		return fragment_home_team;

	}


	public static String charts_dialog(final Context context, final String location, ArrayList list) {
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle("select one ");
		 singel_item_selected="";
		builder.setCancelable(false);
		ListView singel_list = new ListView(context);

		ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_single_choice,list);

		singel_list.setAdapter(adapter);
		singel_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		singel_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				singel_item_selected = (String) parent.getItemAtPosition(position);

			}
		});

		builder.setView(singel_list);

		builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//ToDo
				//onclick

				if (singel_item_selected.equalsIgnoreCase("")){
					Toast.makeText(context,"Nothing Selected",Toast.LENGTH_SHORT).show();
				}else{

					Toast.makeText(context, singel_item_selected, Toast.LENGTH_LONG).show();
				switch (location) {

					case "top_right":

						SharedPreferences sp_1 = context.getSharedPreferences("custom_home_2", Context.MODE_PRIVATE);
						SharedPreferences.Editor edit_1 = sp_1.edit();
						edit_1.putString("top", singel_item_selected);
						edit_1.commit();
						Intent i_1 = new Intent(context, App_Home.class);

						context.startActivity(i_1);

						break;
					case "top_left":

						SharedPreferences sp_2 = context.getSharedPreferences("custom_home_1", Context.MODE_PRIVATE);
						SharedPreferences.Editor edit_2 = sp_2.edit();
						edit_2.putString("top", singel_item_selected);
						edit_2.commit();
						Intent i_2 = new Intent(context, App_Home.class);

						context.startActivity(i_2);
						break;
					case "left_left":

						SharedPreferences sp_3 = context.getSharedPreferences("custom_home_1", Context.MODE_PRIVATE);
						SharedPreferences.Editor edit_3 = sp_3.edit();
						edit_3.putString("left", singel_item_selected);
						edit_3.commit();
						Intent i_3 = new Intent(context, App_Home.class);

						context.startActivity(i_3);

						break;
					case "right_left":

						SharedPreferences sp_4 = context.getSharedPreferences("custom_home_2", Context.MODE_PRIVATE);
						SharedPreferences.Editor edit_4 = sp_4.edit();
						edit_4.putString("left", singel_item_selected);
						edit_4.commit();
						Intent i_4 = new Intent(context, App_Home.class);

						context.startActivity(i_4);

						break;
					case "right_right":

						SharedPreferences sp_5 = context.getSharedPreferences("custom_home_2", Context.MODE_PRIVATE);
						SharedPreferences.Editor edit_5 = sp_5.edit();
						edit_5.putString("right", singel_item_selected);
						edit_5.commit();
						Intent i_5 = new Intent(context, App_Home.class);

						context.startActivity(i_5);

						break;
					case "left_right":

						SharedPreferences sp_6 = context.getSharedPreferences("custom_home_1", Context.MODE_PRIVATE);
						SharedPreferences.Editor edit_6 = sp_6.edit();
						edit_6.putString("right", singel_item_selected);
						edit_6.commit();
						Intent i_6 = new Intent(context, App_Home.class);

						context.startActivity(i_6);

						break;
				}


				}


			}
		});

		builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});



		builder.show();

		return singel_item_selected;
	}
	

	
	private class ViewPagerAdapter extends FragmentStatePagerAdapter {

		CharSequence Titles[]; // This will Store the Titles of the Tabs which are
								// Going to be passed when ViewPagerAdapter is
								// created
		int NumbOfTabs; // Store the number of tabs, this will also be passed when
						// the ViewPagerAdapter is created
		String position_name,item_selected;

		public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb,String position_name,String item_selected) {
			super(fm);
			this.Titles = mTitles;
			this.NumbOfTabs = mNumbOfTabsumb;
			this.position_name=position_name;
			this.item_selected=item_selected;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub

			if (arg0==0){
			switch (position_name) {
			
			case "working":

				switch (item_selected){

					case "Working Hour":

						Barchart_Working barchart_working = new Barchart_Working();
						Bundle args_1 = new Bundle();
						args_1.putString("location", "top_left");
						barchart_working.setArguments(args_1);
						return barchart_working;

					case "Today ":
						Speedometer speedometer = new Speedometer();
						Bundle args_2 = new Bundle();
						args_2.putString("location", "top_left");
						speedometer.setArguments(args_2);
						return speedometer;

					case "Department Activities":
						Department_Activities_BarChart department_activities_barChart = new Department_Activities_BarChart();
						Bundle args_3 = new Bundle();
						args_3.putString("location", "top_left");
						department_activities_barChart.setArguments(args_3);
						return department_activities_barChart;

							case "Department Effort":
								Department_Effort_BarChart department_effort_barChart = new Department_Effort_BarChart();
								Bundle args_4 = new Bundle();
								args_4.putString("location", "top_right");
								department_effort_barChart.setArguments(args_4);
								return department_effort_barChart;

					default:
						Barchart_Working barchartWorking = new Barchart_Working();
						Bundle args_6 = new Bundle();
						args_6.putString("location", "top_left");
						barchartWorking.setArguments(args_6);
						return barchartWorking;

				}

			case "allocation":

				switch (item_selected){
					case "Allocation":
						PieChart_Allocation pieChart_allocation = new PieChart_Allocation();

						Bundle args_1 = new Bundle();
						args_1.putString("location", "left_left");
						pieChart_allocation.setArguments(args_1);

						return pieChart_allocation;


					case "Activities":
						PieChart_Activities pieChart_activities = new PieChart_Activities();

						Bundle args_2 = new Bundle();
						args_2.putString("location", "left_left");
						pieChart_activities.setArguments(args_2);

						return  pieChart_activities;


					case "Leads":
						PieChart_Leads pie_leads = new PieChart_Leads();

						Bundle args_3 = new Bundle();
						args_3.putString("location", "left_left");
						pie_leads.setArguments(args_3);

						return pie_leads;


					case "Invoices":
						PieChart_Invoices pia_invoices = new PieChart_Invoices();

						Bundle args_4 = new Bundle();
						args_4.putString("location", "left_left");
						pia_invoices.setArguments(args_4);

						return pia_invoices;

					case "Campany Invoices":
						Invoices invoices=new Invoices();

						Bundle args_5 = new Bundle();
						args_5.putString("location", "left_left");
						invoices.setArguments(args_5);

						return invoices;

					case "Activities Achieved":
						Activities_Achieved activities_achieved=new Activities_Achieved();

						Bundle args_6 = new Bundle();
						args_6.putString("location", "left_left");
						activities_achieved.setArguments(args_6);

						return activities_achieved;

					case "Activities Planned":
						Activities_Planned activities_planned=new Activities_Planned();

						Bundle args_7 = new Bundle();
						args_7.putString("location", "left_left");
						activities_planned.setArguments(args_7);

						return activities_planned;

					case "Effort Achieved":
						Effort_Achieved effort_achieved=new Effort_Achieved();

						Bundle args_8 = new Bundle();
						args_8.putString("location", "left_left");
						effort_achieved.setArguments(args_8);

						return effort_achieved;

					case "Effort Planned":
						Effort_Planned effort_planned=new Effort_Planned();

						Bundle args_9 = new Bundle();
						args_9.putString("location", "left_left");
						effort_planned.setArguments(args_9);

						return effort_planned;
					case "WorkForce Management":

						WorkForce workForce=new WorkForce();

						Bundle args_11 = new Bundle();
						args_11.putString("location", "left_left");
						workForce.setArguments(args_11);

						return workForce;

					case "Agents Monitor":

						Agent_Monitor agent_monitor=new Agent_Monitor();

						Bundle args_12 = new Bundle();
						args_12.putString("location", "left_left");
						agent_monitor.setArguments(args_12);

						return agent_monitor;

					default:
						PieChart_Allocation pieChart_allocation_1 = new PieChart_Allocation();

						Bundle args_10 = new Bundle();
						args_10.putString("location", "left_left");
						pieChart_allocation_1.setArguments(args_10);

						return pieChart_allocation_1;



				}

				case "activities":

					switch (item_selected){
						case "Allocation":
							PieChart_Allocation pieChart_allocation = new PieChart_Allocation();

							Bundle args_1 = new Bundle();
							args_1.putString("location", "left_right");
							pieChart_allocation.setArguments(args_1);

							return pieChart_allocation;


						case "Activities":
							PieChart_Activities pieChart_activities = new PieChart_Activities();

							Bundle args_2 = new Bundle();
							args_2.putString("location", "left_right");
							pieChart_activities.setArguments(args_2);

							return  pieChart_activities;


						case "Leads":
							PieChart_Leads pie_leads = new PieChart_Leads();

							Bundle args_3 = new Bundle();
							args_3.putString("location", "left_right");
							pie_leads.setArguments(args_3);

							return pie_leads;


						case "Invoices":
							PieChart_Invoices pia_invoices = new PieChart_Invoices();

							Bundle args_4 = new Bundle();
							args_4.putString("location", "left_right");
							pia_invoices.setArguments(args_4);

							return pia_invoices;

						case "Campany Invoices":
							Invoices invoices=new Invoices();

							Bundle args_5 = new Bundle();
							args_5.putString("location", "left_right");
							invoices.setArguments(args_5);

							return invoices;


						case "Activities Achieved":
							Activities_Achieved activities_achieved=new Activities_Achieved();

							Bundle args_6 = new Bundle();
							args_6.putString("location", "left_right");
							activities_achieved.setArguments(args_6);
							return activities_achieved;

						case "Activities Planned":
							Activities_Planned activities_planned=new Activities_Planned();

							Bundle args_7 = new Bundle();
							args_7.putString("location", "left_right");
							activities_planned.setArguments(args_7);

							return activities_planned;

						case "Effort Achieved":
							Effort_Achieved effort_achieved=new Effort_Achieved();

							Bundle args_8 = new Bundle();
							args_8.putString("location", "left_right");
							effort_achieved.setArguments(args_8);

							return effort_achieved;

						case "Effort Planned":
							Effort_Planned effort_planned=new Effort_Planned();

							Bundle args_9 = new Bundle();
							args_9.putString("location", "left_right");
							effort_planned.setArguments(args_9);

							return effort_planned;

						case "WorkForce Management":

							WorkForce workForce=new WorkForce();

							Bundle args_11 = new Bundle();
							args_11.putString("location", "left_right");
							workForce.setArguments(args_11);

							return workForce;

						case "Agents Monitor":

							Agent_Monitor agent_monitor=new Agent_Monitor();

							Bundle args_12 = new Bundle();
							args_12.putString("location", "left_right");
							agent_monitor.setArguments(args_12);

							return agent_monitor;

						default:
							PieChart_Activities pieChart_activities_1 = new PieChart_Activities();

							Bundle args_10 = new Bundle();
							args_10.putString("location", "left_right");
							pieChart_activities_1.setArguments(args_10);

							return  pieChart_activities_1;



					}




				default :
				
				return null;

			}
			}else {

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
			
		//	startActivity(new Intent(getApplicationContext(), Home.class));
		//	finish();
			break;


		default:
			break;
		}
		
	}
	

	}


