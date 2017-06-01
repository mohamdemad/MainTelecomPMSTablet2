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
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are
    // Going to be passed when ViewPagerAdapter is
    // created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when
    // the ViewPagerAdapter is created
    String mTab;
    Context mContext;
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb,String tab , Context context) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mTab=tab;
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
                        DepartmentActivities_BarChart Dep_A =new DepartmentActivities_BarChart();
                        return  Dep_A;
                    }
                    else if(s.equals("Department Effort")){
                        DepartmentEffort_BarChart Dep_E = new DepartmentEffort_BarChart();
                        return Dep_E;
                    }
                    else if(s.equals("Leads/Industry")){
                        Leads_Industry_Fragment leads_industry_fragment = new Leads_Industry_Fragment();
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
                        IncomingAgentCalls_Barchart incomingAgentCallsBarchart = new IncomingAgentCalls_Barchart();
                        return incomingAgentCallsBarchart;
                    }
                    else if(s.equals("Outgoing Calls Agent")){
                        OutgoingAgentCalls_BarChart outgoingAgentCallsBarChart = new OutgoingAgentCalls_BarChart();
                        return outgoingAgentCallsBarChart;
                    }
                    else if(s.equals("Hourly Incoming Calls Agent")){
                        HourlyIncomingCallsAgent_LineChart hourlyIncomingCallsAgentLineChart = new HourlyIncomingCallsAgent_LineChart();
                        return hourlyIncomingCallsAgentLineChart;
                    }
                    else if(s.equals("Hourly Outgoing Calls Agent")){
                        HourlyOutgoingCallsAgent_LineChart hourlyOutgoingCallsAgentLineChart = new HourlyOutgoingCallsAgent_LineChart();
                        return hourlyOutgoingCallsAgentLineChart;
                    }
                    else if(s.equals("Incoming Calls Queue")){
                        IncomingQueuesCalls_BarChart incomingQueuesCallsBarChart = new IncomingQueuesCalls_BarChart();
                        return incomingQueuesCallsBarChart;
                    }
                    else if(s.equals("Outgoing Calls Queue")){
                        OutgoingQueuesCalls_BarChart outgoingQueuesCallsBarChart = new OutgoingQueuesCalls_BarChart();
                        return outgoingQueuesCallsBarChart;
                    }
                    else if(s.equals("Hourly Incoming Calls Queue")){
                        HourlyIncomingCallsQueue_BarChart hourlyIncomingCallsQueueBarChart = new HourlyIncomingCallsQueue_BarChart();
                        return hourlyIncomingCallsQueueBarChart;
                    }
                    else if(s.equals("Hourly Outgoing Calls Queue")){
                        HourlyOutgoingCallsQueue_BarChart hourlyOutgoingCallsQueueBarChart = new HourlyOutgoingCallsQueue_BarChart();
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
                    else if (s.equals("Team WFM Home") || s.equals("Activities Evaluationn") || s.equals("Time Evaluationn") ||
                            s.equals("Department Activities") || s.equals("Department Effort") ||
                            s.equals("Leads/Industry") || s.equals("Working Hourss")||s.equals("Open/Closed Tickets")) {

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
