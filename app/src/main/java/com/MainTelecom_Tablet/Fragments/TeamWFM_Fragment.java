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
 * Created by MOHAMED on 14/04/2016.
 */
public class TeamWFM_Fragment extends Fragment {

    ViewPager pager_1,pager_2,pager_3;
    ViewPagerAdapter adapter_1,adapter_2 , adapter_3;
    CharSequence Titles[] = {"Working\nHours"};
    int Numboftabs = 1;

    private SwipeRefreshLayout swipeContainer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View teamWFM = inflater.inflate(R.layout.team_wfm_home_layout, container, false);

        pager_1 = (ViewPager) teamWFM.findViewById(R.id.Working_hours_pager);
        pager_2 = (ViewPager) teamWFM.findViewById(R.id.Activities_pager);
        pager_3 = (ViewPager) teamWFM.findViewById(R.id.Allocation_pager);

        adapter_1 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"part_1");
        adapter_2 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"Part_2");
        adapter_3 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"Part_3");
        pager_1.setAdapter(adapter_1);
        pager_2.setAdapter(adapter_2);
        pager_3.setAdapter(adapter_3);



        swipeContainer = (SwipeRefreshLayout) teamWFM.findViewById(R.id.team_wfm_home_swipe_container);

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



        pager_1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        return teamWFM;
    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        CharSequence Titles[];
        int NumbOfTabs;
        String position;

        public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb,String position) {
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
                        WorkingHours_BarChart workingHours_barChart = new WorkingHours_BarChart();
                        return workingHours_barChart;
                    }
                     else if(position.equalsIgnoreCase("part_2")){
                        Org_Activities_PieChart org_activities_pieChart=new Org_Activities_PieChart();
                        return org_activities_pieChart;
                    }else if(position.equalsIgnoreCase("part_3")){
                        Org_Allocation_PieChart org_allocation_pieChart = new Org_Allocation_PieChart();
                        return org_allocation_pieChart;
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

