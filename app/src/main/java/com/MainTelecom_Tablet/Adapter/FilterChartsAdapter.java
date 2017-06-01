package com.MainTelecom_Tablet.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.MainTelecom_Tablet.Fragments.ActivitiesEvaluation_PieChart;
import com.MainTelecom_Tablet.Fragments.Company_Invoices_Fragment;
import com.MainTelecom_Tablet.Fragments.ContactCenter_Home;
import com.MainTelecom_Tablet.Fragments.DepartmentActivities_BarChart;
import com.MainTelecom_Tablet.Fragments.DepartmentEffort_BarChart;
import com.MainTelecom_Tablet.Fragments.EvaluationFilterFragment;
import com.MainTelecom_Tablet.Fragments.FilterContactCenter;
import com.MainTelecom_Tablet.Fragments.HourlyIncomingCallsAgent_LineChart;
import com.MainTelecom_Tablet.Fragments.HourlyIncomingCallsQueue_BarChart;
import com.MainTelecom_Tablet.Fragments.HourlyOutgoingCallsAgent_LineChart;
import com.MainTelecom_Tablet.Fragments.HourlyOutgoingCallsQueue_BarChart;
import com.MainTelecom_Tablet.Fragments.IncomingAgentCalls_Barchart;
import com.MainTelecom_Tablet.Fragments.IncomingQueuesCalls_BarChart;
import com.MainTelecom_Tablet.Fragments.Leads_Industry_Fragment;
import com.MainTelecom_Tablet.Fragments.MyHomeChartsFragment;
import com.MainTelecom_Tablet.Fragments.Open_Closed_Tickets_PieChart;
import com.MainTelecom_Tablet.Fragments.OutgoingAgentCalls_BarChart;
import com.MainTelecom_Tablet.Fragments.OutgoingQueuesCalls_BarChart;
import com.MainTelecom_Tablet.Fragments.TeamWFM_Fragment;
import com.MainTelecom_Tablet.Fragments.TimeEvaluation_PieChart;
import com.MainTelecom_Tablet.Fragments.WorkingHours_BarChart;
import com.MainTelecom_Tablet.SharedPreference.SaveSharedPreference;

/**
 * Created by MOHAMED on 14/04/2016.
 */
public class FilterChartsAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are
    // Going to be passed when ViewPagerAdapter is
    // created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when
    // the ViewPagerAdapter is created
    String mTab;
    Context mContext;
    String[] Mids;
    String[] Mnames;
    public FilterChartsAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb,String tab ,String[]ids,String[]names ,Context context) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mTab=tab;
        this.Mids= ids;
        this.Mnames=names;
        mContext = context;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub

        switch (arg0) {

            case 0:

                String s = SaveSharedPreference.getTab3Check(mContext);
                /** Tab2 Fragments */
                if(mTab.equalsIgnoreCase("part_1")) {

                    Log.d("Mohamed : ", "GOOOOOOOD" + mContext);

                    if(s.equals("Team WFM Home")) {
                        TeamWFM_Fragment home_team = new TeamWFM_Fragment();
                        return home_team;
                    }
                    else if(s.equals("Activities Evaluation")) {
                        ActivitiesEvaluation_PieChart m = new ActivitiesEvaluation_PieChart();
                        return  m;
                    }
                    else if(s.equals("Time Evaluation")){
                        TimeEvaluation_PieChart TimeE =new TimeEvaluation_PieChart();
                        return TimeE;
                    }
                    else if(s.equals("Department Activities")){
                        DepartmentActivities_BarChart Dep_A =DepartmentActivities_BarChart.newInstance(Mids,Mnames);
                        return  Dep_A;
                    }
                    else if(s.equals("Department Effort")){
                        DepartmentEffort_BarChart Dep_E = DepartmentEffort_BarChart.newInstance(Mids,Mnames);
                        return Dep_E;
                    }
                    else if(s.equals("Leads/Industry")){
                        Leads_Industry_Fragment leads_industry_fragment = Leads_Industry_Fragment.newInstance(Mids,Mnames);
                        return leads_industry_fragment;
                    }
                    else if(s.equals("Working Hours")){
                        WorkingHours_BarChart workingHours_barChart = new WorkingHours_BarChart();
                        return workingHours_barChart;
                    }
                    else if(s.equals("Open/Closed Tickets")){
                        Open_Closed_Tickets_PieChart openClosedTicketsPieChart = new Open_Closed_Tickets_PieChart();
                        return openClosedTicketsPieChart;
                    }

                    /**Contact center fragments*/
                    else if(s.equals("Contact Center Home")){
                        ContactCenter_Home contactCenter_home = new ContactCenter_Home();
                        return contactCenter_home;
                    }
                    else if(s.equals("Incoming Calls Agent")){
                        IncomingAgentCalls_Barchart incomingAgentCallsBarchart = IncomingAgentCalls_Barchart.newInstance(Mids,Mnames);
                        return incomingAgentCallsBarchart;
                    }
                    else if(s.equals("Outgoing Calls Agent")){
                        OutgoingAgentCalls_BarChart outgoingAgentCallsBarChart = OutgoingAgentCalls_BarChart.newInstance(Mids,Mnames);
                        return outgoingAgentCallsBarChart;
                    }
                    else if(s.equals("Hourly Incoming Calls Agent")){
                        HourlyIncomingCallsAgent_LineChart hourlyIncomingCallsAgentLineChart = HourlyIncomingCallsAgent_LineChart.newInstance(Mids,Mnames);
                        return hourlyIncomingCallsAgentLineChart;
                    }
                    else if(s.equals("Hourly Outgoing Calls Agent")){
                        HourlyOutgoingCallsAgent_LineChart hourlyOutgoingCallsAgentLineChart = HourlyOutgoingCallsAgent_LineChart.newInstance(Mids,Mnames);
                        return hourlyOutgoingCallsAgentLineChart;
                    }
                    else if(s.equals("Incoming Calls Queue")){
                        IncomingQueuesCalls_BarChart incomingQueuesCallsBarChart = IncomingQueuesCalls_BarChart.newInstance(Mids,Mnames);
                        return incomingQueuesCallsBarChart;
                    }
                    else if(s.equals("Outgoing Calls Queue")){
                        OutgoingQueuesCalls_BarChart outgoingQueuesCallsBarChart = OutgoingQueuesCalls_BarChart.newInstance(Mids,Mnames);
                        return outgoingQueuesCallsBarChart;
                    }
                    else if(s.equals("Hourly Incoming Calls Queue")){
                        HourlyIncomingCallsQueue_BarChart hourlyIncomingCallsQueueBarChart =  HourlyIncomingCallsQueue_BarChart.newInstance(Mids,Mnames);
                        return hourlyIncomingCallsQueueBarChart;
                    }
                    else if(s.equals("Hourly Outgoing Calls Queue")){
                        HourlyOutgoingCallsQueue_BarChart hourlyOutgoingCallsQueueBarChart = HourlyOutgoingCallsQueue_BarChart.newInstance(Mids,Mnames);
                        return hourlyOutgoingCallsQueueBarChart;
                    }
                    else{
                        EvaluationFilterFragment home_team = new EvaluationFilterFragment();
                        return home_team;
                    }

                }else if (mTab.equalsIgnoreCase("part_2")) {

                    if(s.equals("Team WFM Home")){
                        Company_Invoices_Fragment companyInvoicesFragment = new Company_Invoices_Fragment();
                        return companyInvoicesFragment;
                    }
                    else if (s.equals("Team WFM Home") || s.equals("Activities Evaluation") || s.equals("Time Evaluation") ||
                            s.equals("Department Activities") || s.equals("Department Effort") ||
                            s.equals("Leads/Industry") || s.equals("Working Hours")) {

                        EvaluationFilterFragment filter = new EvaluationFilterFragment();
                        return filter;

                    }else {
                        FilterContactCenter filterContactCenter = new FilterContactCenter();
                        return filterContactCenter;
                    }
                }

/***************************************************************************************************/
                /** Tab1 Fragments*/
                else if(mTab.equalsIgnoreCase("tab1")){


                    MyHomeChartsFragment home = new MyHomeChartsFragment();

                    return home;

                }



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
