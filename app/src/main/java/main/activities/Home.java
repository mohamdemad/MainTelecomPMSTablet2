package main.activities;


import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.MainTelecom_Tablet.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

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
import main.database_connections.Agents_Connection;
import main.database_connections.Projects_Connection;
import main.database_connections.Queues_Connection;
import main.fragments.Agent_Monitor;
import main.fragments.Invoices;

public class Home extends Fragment implements OnClickListener {

	Toolbar toolbar;
	//NavigationDrawerFragment navigationDrawerFragment;
	ViewPager pager_speedmeter,pager_invoices,pager_leads;
	ViewPagerAdapter adapter_speedmeter,adapter_invoices,adapter_leads;
	//SlidingTabLayout tabs;
	CharSequence Titles[] = { "" };
	int Numboftabs = 1;
	public static Queues_Connection queue_con;
	public static Agents_Connection agent_con;
	SharedPreferences sp;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


	View fragment_home_pms = inflater.inflate(
				R.layout.home_pms_layout, container, false);

		sp = getActivity().getSharedPreferences("custom_home_2", Context.MODE_PRIVATE);
		int sp_size = sp.getAll().size();



		//get project`s name
		Projects_Connection project_con=new Projects_Connection();
		project_con.execute("http://"+Login.enteredIP+"/Android/Incoming_Queue/Projects.php");

		String url_getqueue="http://"+Login.enteredIP+"/Android/Incoming_Queue/Queues.php";
		String url_getagent="http://"+Login.enteredIP+"/Android/Incoming_Queue/Agents.php";
		 queue_con=new Queues_Connection(getActivity().getBaseContext(), false,"0");
		queue_con.execute(url_getqueue);
		
		 agent_con=new Agents_Connection(getActivity().getBaseContext(), "");
		 agent_con.execute(url_getagent);
		// navigationDrawerFragment=(NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.fragment_drawer);
		// navigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout)
		// findViewById(R.id.drawer_layout), toolbar);


		if (sp_size==0){


		// Creating The ViewPagerAdapter and Passing Fragment Manager, Titles
		// fot the Tabs and Number Of Tabs.
		adapter_speedmeter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
				Numboftabs,"speedometer","");
		adapter_invoices = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
				Numboftabs,"invoices","");
		adapter_leads = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
				Numboftabs,"leads","");}
		else{

			String item_top=sp.getString("top", "");
			String item_left=sp.getString("left", "");
			String item_right=sp.getString("right", "");

			adapter_speedmeter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
					Numboftabs,"speedometer",item_top);
			adapter_invoices = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
					Numboftabs,"invoices",item_left);
			adapter_leads = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), Titles,
					Numboftabs,"leads",item_right);
		}

		// Assigning ViewPager View and setting the adapter
		pager_speedmeter = (ViewPager)fragment_home_pms.findViewById(R.id.pager_speedmeter);
		pager_invoices = (ViewPager)fragment_home_pms.findViewById(R.id.pager_invoices);
		pager_leads = (ViewPager)fragment_home_pms.findViewById(R.id.pager_leads);

		final Handler handler = new Handler();




		pager_speedmeter.setAdapter(adapter_speedmeter);
		pager_invoices.setAdapter(adapter_invoices);
		pager_leads.setAdapter(adapter_leads);




		
	                                 	//////////////create floating buttons///////////////
		
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
		ImageView dashboard_icon = new ImageView(getActivity());
		dashboard_icon.setImageResource(R.mipmap.dashboard_icon);

		SubActionButton dashboard_action_button = itemBuilder.setContentView(dashboard_icon).build();
		dashboard_action_button.setOnClickListener(this);
		dashboard_action_button.setId(R.id.dashboard_floating_id);

		dashboard_action_button.setBackgroundColor(Color.parseColor("#00000000"));
		dashboard_action_button.getLayoutParams().height=100;
		dashboard_action_button.getLayoutParams().width=100;

		
		//add sub float buttons in ui
		FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
				.addSubActionView(dashboard_action_button)
				.attachTo(menu_action_button).build();

		return fragment_home_pms;

	}


	


	/*
	 * @Override public void onNavigationDrawerItemSelected(int position) { //
	 * TODO Auto-generated method stub
	 * 
	 * System.out.println(position);
	 * 
	 * }
	 */

	
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

			if(arg0==0) {
				switch (position_name) {

					case "speedometer":

						switch (item_selected){

							case "Working Hour":

								Barchart_Working barchart_working = new Barchart_Working();
								Bundle args_1 = new Bundle();
								args_1.putString("location", "top_right");
								barchart_working.setArguments(args_1);

								return barchart_working;

							case "Today ":
							Speedometer speedometer = new Speedometer();
								Bundle args_2 = new Bundle();
								args_2.putString("location", "top_right");
								speedometer.setArguments(args_2);
								return speedometer;

							case "Department Activities":
								Department_Activities_BarChart department_activities_barChart = new Department_Activities_BarChart();
								Bundle args_3 = new Bundle();
								args_3.putString("location", "top_right");
								department_activities_barChart.setArguments(args_3);
								return department_activities_barChart;

							case "Department Effort":
								Department_Effort_BarChart department_effort_barChart = new Department_Effort_BarChart();
								Bundle args_4 = new Bundle();
								args_4.putString("location", "top_right");
								department_effort_barChart.setArguments(args_4);
								return department_effort_barChart;

						default:
							Speedometer speedometer_6 = new Speedometer();
							Bundle args_6 = new Bundle();
							args_6.putString("location", "top_right");
							speedometer_6.setArguments(args_6);
							return speedometer_6;

						}

					case "invoices":

						switch (item_selected){
							case "Allocation":
								PieChart_Allocation pieChart_allocation = new PieChart_Allocation();

								Bundle args_1 = new Bundle();
								args_1.putString("location", "right_left");
								pieChart_allocation.setArguments(args_1);

								return pieChart_allocation;


							case "Activities":
								PieChart_Activities pieChart_activities = new PieChart_Activities();

								Bundle args_2 = new Bundle();
								args_2.putString("location", "right_left");
								pieChart_activities.setArguments(args_2);

								return  pieChart_activities;


							case "Leads":
								PieChart_Leads pie_leads = new PieChart_Leads();

								Bundle args_3 = new Bundle();
								args_3.putString("location", "right_left");
								pie_leads.setArguments(args_3);

								return pie_leads;


							case "Invoices":
								PieChart_Invoices pia_invoices = new PieChart_Invoices();

								Bundle args_4 = new Bundle();
								args_4.putString("location", "right_left");
								pia_invoices.setArguments(args_4);

								return pia_invoices;

							case "Campany Invoices":
								Invoices invoices=new Invoices();

								Bundle args_5 = new Bundle();
								args_5.putString("location", "right_left");
								invoices.setArguments(args_5);

								return invoices;

							case "Activities Achieved":
								Activities_Achieved activities_achieved=new Activities_Achieved();

								Bundle args_6 = new Bundle();
								args_6.putString("location", "right_left");
								activities_achieved.setArguments(args_6);

								return activities_achieved;

							case "Activities Planned":
								Activities_Planned activities_planned=new Activities_Planned();

								Bundle args_7 = new Bundle();
								args_7.putString("location", "right_left");
								activities_planned.setArguments(args_7);

								return activities_planned;

							case "Effort Achieved":
								Effort_Achieved effort_achieved=new Effort_Achieved();

								Bundle args_8 = new Bundle();
								args_8.putString("location", "right_left");
								effort_achieved.setArguments(args_8);

								return effort_achieved;

							case "Effort Planned":
								Effort_Planned effort_planned=new Effort_Planned();

								Bundle args_9 = new Bundle();
								args_9.putString("location", "right_left");
								effort_planned.setArguments(args_9);

								return effort_planned;

							case "WorkForce Management":
								WorkForce workForce=new WorkForce();

								Bundle args_11 = new Bundle();
								args_11.putString("location", "right_left");
								workForce.setArguments(args_11);

								return workForce;

							case "Agents Monitor":

								Agent_Monitor agent_monitor=new Agent_Monitor();

								Bundle args_12 = new Bundle();
								args_12.putString("location", "right_left");
								agent_monitor.setArguments(args_12);

								return agent_monitor;


							default:
								PieChart_Invoices pia_invoices_1 = new PieChart_Invoices();

								Bundle args_10 = new Bundle();
								args_10.putString("location", "right_left");
								pia_invoices_1.setArguments(args_10);

								return pia_invoices_1;

						}

					case "leads":

					case "activities":

						switch (item_selected){
							case "Allocation":
								PieChart_Allocation pieChart_allocation = new PieChart_Allocation();

								Bundle args_1 = new Bundle();
								args_1.putString("location", "right_right");
								pieChart_allocation.setArguments(args_1);

								return pieChart_allocation;


							case "Activities":
								PieChart_Activities pieChart_activities = new PieChart_Activities();

								Bundle args_2 = new Bundle();
								args_2.putString("location", "right_right");
								pieChart_activities.setArguments(args_2);

								return  pieChart_activities;


							case "Leads":
								PieChart_Leads pie_leads = new PieChart_Leads();

								Bundle args_3 = new Bundle();
								args_3.putString("location", "right_right");
								pie_leads.setArguments(args_3);

								return pie_leads;


							case "Invoices":
								PieChart_Invoices pia_invoices = new PieChart_Invoices();

								Bundle args_4 = new Bundle();
								args_4.putString("location", "right_right");
								pia_invoices.setArguments(args_4);

								return pia_invoices;

							case "Campany Invoices":
								Invoices invoices=new Invoices();

								Bundle args_5 = new Bundle();
								args_5.putString("location", "right_right");
								invoices.setArguments(args_5);

								return invoices;


							case "Activities Achieved":
								Activities_Achieved activities_achieved=new Activities_Achieved();

								Bundle args_6 = new Bundle();
								args_6.putString("location", "right_right");
								activities_achieved.setArguments(args_6);

								return activities_achieved;

							case "Activities Planned":
								Activities_Planned activities_planned=new Activities_Planned();

								Bundle args_7 = new Bundle();
								args_7.putString("location", "right_right");
								activities_planned.setArguments(args_7);

								return activities_planned;

							case "Effort Achieved":
								Effort_Achieved effort_achieved=new Effort_Achieved();

								Bundle args_8 = new Bundle();
								args_8.putString("location", "right_right");
								effort_achieved.setArguments(args_8);

								return effort_achieved;

							case "Effort Planned":
								Effort_Planned effort_planned=new Effort_Planned();

								Bundle args_9 = new Bundle();
								args_9.putString("location", "right_right");
								effort_planned.setArguments(args_9);

								return effort_planned;

							case "WorkForce Management":
								WorkForce workForce=new WorkForce();

								Bundle args_11 = new Bundle();
								args_11.putString("location", "right_right");
								workForce.setArguments(args_11);

								return workForce;

							case "Agents Monitor":

								Agent_Monitor agent_monitor=new Agent_Monitor();

								Bundle args_12 = new Bundle();
								args_12.putString("location", "right_right");
								agent_monitor.setArguments(args_12);

								return agent_monitor;

							default:
								PieChart_Leads pie_leads_1 = new PieChart_Leads();

								Bundle args_10 = new Bundle();
								args_10.putString("location", "right_right");
								pie_leads_1.setArguments(args_10);

								return pie_leads_1;



						}

					default:

						return null;

				}
			}else{return  null;}
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
		case R.id.dashboard_floating_id:
			startActivity(new Intent(getActivity(), Dashboard.class));
			getActivity().finish();
			break;

		default:
			break;
		}
	}
}
