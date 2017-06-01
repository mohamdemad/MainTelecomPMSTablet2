package com.MainTelecom_Tablet.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.MainTelecom_Tablet.Fragments.Company_Invoices_Fragment;
import com.MainTelecom_Tablet.Fragments.ContactCenter_DataGauges;
import com.MainTelecom_Tablet.Fragments.DepartmentActivities_BarChart;
import com.MainTelecom_Tablet.Fragments.DepartmentEffort_BarChart;
import com.MainTelecom_Tablet.Fragments.Eva_Activities_Achieved_Fragment;
import com.MainTelecom_Tablet.Fragments.Eva_Activities_Planned_Fragment;
import com.MainTelecom_Tablet.Fragments.Eva_Effort_Achieved_Fragment;
import com.MainTelecom_Tablet.Fragments.Eva_Effort_Planned_Fragment;
import com.MainTelecom_Tablet.Fragments.HourlyIncomingCallsAgent_LineChart;
import com.MainTelecom_Tablet.Fragments.HourlyIncomingCallsQueue_BarChart;
import com.MainTelecom_Tablet.Fragments.HourlyOutgoingCallsAgent_LineChart;
import com.MainTelecom_Tablet.Fragments.HourlyOutgoingCallsQueue_BarChart;
import com.MainTelecom_Tablet.Fragments.IncomingAgentCalls_Barchart;
import com.MainTelecom_Tablet.Fragments.IncomingQueuesCalls_BarChart;
import com.MainTelecom_Tablet.Fragments.InvoicesIncomingCalls_PieChart;
import com.MainTelecom_Tablet.Fragments.LeadsOutgoingCalls_PieChart;
import com.MainTelecom_Tablet.Fragments.Leads_Industry_Fragment;
import com.MainTelecom_Tablet.Fragments.Org_Activities_PieChart;
import com.MainTelecom_Tablet.Fragments.Org_Allocation_PieChart;
import com.MainTelecom_Tablet.Fragments.OutgoingAgentCalls_BarChart;
import com.MainTelecom_Tablet.Fragments.OutgoingQueuesCalls_BarChart;
import com.MainTelecom_Tablet.Fragments.WorkForceManagement_Fragment;
import com.MainTelecom_Tablet.Fragments.WorkingHours_BarChart;

/**
 * Created by MOHAMED on 17/04/2016.
 */
public class MyHomeChartsAdapter extends FragmentStatePagerAdapter {

    // Going to be passed when ViewPagerAdapter is
    // created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when
    // the ViewPagerAdapter is created
    String mPagerName;
    Context mContext;
    public MyHomeChartsAdapter(FragmentManager fm, int mNumbOfTabsumb,String PagerName , Context context) {
        super(fm);
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mPagerName=PagerName;
        mContext = context;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub

        switch (arg0) {

            case 0:

                /** my home Fragments */
                Log.d("Mohamed : ", "GOOOOOOOD" + mPagerName +"--");

                    if(mPagerName.equals("Department Activities")) {
                        DepartmentActivities_BarChart Dep_Ac = new DepartmentActivities_BarChart();
                        //ChangingListFragment Dep_Ac =new ChangingListFragment();
                        return Dep_Ac;

                    }else if(mPagerName.equals("Department Effort")) {
                        DepartmentEffort_BarChart Dep_Ef = new DepartmentEffort_BarChart();
                        return Dep_Ef;

                    }else if(mPagerName.equals("Incoming Calls Agent")){
                        IncomingAgentCalls_Barchart incomingAgentCallsBarchart = new IncomingAgentCalls_Barchart();
                        return incomingAgentCallsBarchart;
                    }
                    else if(mPagerName.equals("Outgoing Calls Agent")){
                        OutgoingAgentCalls_BarChart outgoingAgentCallsBarChart = new OutgoingAgentCalls_BarChart();
                        return outgoingAgentCallsBarChart;
                    }
                    else if(mPagerName.equals("Hourly Incoming Calls Agent")){
                        HourlyIncomingCallsAgent_LineChart hourlyIncomingCallsAgentLineChart = new HourlyIncomingCallsAgent_LineChart();
                        return hourlyIncomingCallsAgentLineChart;
                    }
                    else if(mPagerName.equals("Hourly Outgoing Calls Agent")){
                        HourlyOutgoingCallsAgent_LineChart hourlyOutgoingCallsAgentLineChart = new HourlyOutgoingCallsAgent_LineChart();
                        return hourlyOutgoingCallsAgentLineChart;
                    }
                    else if(mPagerName.equals("Incoming Calls Queue")){
                        IncomingQueuesCalls_BarChart incomingQueuesCallsBarChart = new IncomingQueuesCalls_BarChart();
                        return incomingQueuesCallsBarChart;
                    }
                    else if(mPagerName.equals("Outgoing Calls Queue")){
                        OutgoingQueuesCalls_BarChart outgoingQueuesCallsBarChart = new OutgoingQueuesCalls_BarChart();
                        return outgoingQueuesCallsBarChart;
                    }
                    else if(mPagerName.equals("Hourly Incoming Calls Queue")){
                        HourlyIncomingCallsQueue_BarChart hourlyIncomingCallsQueueBarChart = new HourlyIncomingCallsQueue_BarChart();
                        return hourlyIncomingCallsQueueBarChart;
                    }
                    else if(mPagerName.equals("Hourly Outgoing Calls Queue")){
                        HourlyOutgoingCallsQueue_BarChart hourlyOutgoingCallsQueueBarChart = new HourlyOutgoingCallsQueue_BarChart();
                        return hourlyOutgoingCallsQueueBarChart;

                    }else if(mPagerName.equals("Working Hours")){
                        WorkingHours_BarChart working = new WorkingHours_BarChart();
                        return working;

                    }else if(mPagerName.equals("Contact Center Gauges")){
                        ContactCenter_DataGauges today = new ContactCenter_DataGauges();
                        return today;

                    }else if(mPagerName.equals("Leads/Industry")){
                        Leads_Industry_Fragment leadsIndustryFragment = new Leads_Industry_Fragment();
                        return leadsIndustryFragment;

                    }else if(mPagerName.equals("Activities Achieved")) {
                        Eva_Activities_Achieved_Fragment activities_achieved = new Eva_Activities_Achieved_Fragment();
                        return activities_achieved;

                    }else if(mPagerName.equals("Activities Planned")) {
                        Eva_Activities_Planned_Fragment activities_planned = new Eva_Activities_Planned_Fragment();
                        return activities_planned;

                    }else if(mPagerName.equals("Effort Achieved")) {
                        Eva_Effort_Achieved_Fragment effort_achieved = new Eva_Effort_Achieved_Fragment();
                        return effort_achieved;

                    }else if(mPagerName.equals("Effort Planned")) {
                        Eva_Effort_Planned_Fragment effort_planned = new Eva_Effort_Planned_Fragment();
                        return effort_planned;

                    }else if(mPagerName.equals("Organization Activities")) {
                        Org_Activities_PieChart pieChart_activities = new Org_Activities_PieChart();
                        return pieChart_activities;

                    }else if(mPagerName.equals("Organization Allocation")) {
                        Org_Allocation_PieChart pieChart_allocation = new Org_Allocation_PieChart();
                        return pieChart_allocation;

                    }else if(mPagerName.equals("Invoices/Incoming Calls")) {
                        InvoicesIncomingCalls_PieChart invoices = new InvoicesIncomingCalls_PieChart();
                        return invoices;

                    }else if(mPagerName.equals("Leads/Outgoing Calls")) {
                        LeadsOutgoingCalls_PieChart leads = new LeadsOutgoingCalls_PieChart();
                        return leads;

                    }else if(mPagerName.equals("Invoices Company")) {
                        Company_Invoices_Fragment companyInvoicesFragment = new Company_Invoices_Fragment();
                        return companyInvoicesFragment;
                    }


                    else if (mPagerName.equalsIgnoreCase("Workforce Management")) {
                        WorkForceManagement_Fragment workForceManagementFragment = new WorkForceManagement_Fragment();
                        return workForceManagementFragment;

                    }


            default :

                return null;

        }
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return NumbOfTabs;
    }

    }


