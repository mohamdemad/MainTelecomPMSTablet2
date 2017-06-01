package com.MainTelecom_Tablet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MainTelecom_Tablet.R;


/**
 * Created by MOHAMED on 02/05/2016.
 */
public class ContactCenter_Home extends Fragment {

    ViewPager pager_1,pager_2,pager_3;
    ViewPagerAdapter adapter_1,adapter_2 , adapter_3;
    CharSequence Titles[] = {"Working\nHours"};
    int Numboftabs = 1;

    private SwipeRefreshLayout swipeContainer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View Contact_center = inflater.inflate(R.layout.contact_center_home_layout, container, false);

        pager_1 = (ViewPager) Contact_center.findViewById(R.id.data_gauges_pager);
        pager_2 = (ViewPager) Contact_center.findViewById(R.id.invoives_incoming_pager);
        pager_3 = (ViewPager) Contact_center.findViewById(R.id.leads_outgoing_pager);

        adapter_1 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"part_1");
        adapter_2 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"Part_2");
        adapter_3 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"Part_3");
        pager_1.setAdapter(adapter_1);
        pager_2.setAdapter(adapter_2);
        pager_3.setAdapter(adapter_3);

        swipeContainer = (SwipeRefreshLayout) Contact_center.findViewById(R.id.contact_center_home_swipe_container);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter_1 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"part_1");
                adapter_2 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"Part_2");
                adapter_3 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"Part_3");
                pager_1.setAdapter(adapter_1);
                pager_2.setAdapter(adapter_2);
                pager_3.setAdapter(adapter_3);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        return Contact_center;
    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        CharSequence Titles[];
        int NumbOfTabs;
        String position;

        public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, String position) {
            super(fm);
            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;
            this.position=position;
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub

            switch (arg0) {

                case 0:

                    if(position.equalsIgnoreCase("part_1")) {
                        ContactCenter_DataGauges contactCenter_dataGauges = new ContactCenter_DataGauges();
                        return contactCenter_dataGauges;
                    }
                    else if(position.equalsIgnoreCase("part_2")){
                        InvoicesIncomingCalls_PieChart invoicesIncomingCalls_pieChart=new InvoicesIncomingCalls_PieChart();
                        return invoicesIncomingCalls_pieChart;
                    }else if(position.equalsIgnoreCase("part_3")){
                        LeadsOutgoingCalls_PieChart leadsOutgoingCalls_pieChart = new LeadsOutgoingCalls_PieChart();
                        return leadsOutgoingCalls_pieChart;
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
}
