package com.MainTelecom_Tablet.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.MainTelecom_Tablet.Adapter.ViewPagerAdapter;
import com.MainTelecom_Tablet.R;


public class HomeActivity extends Fragment  {

    ImageButton image_button,image_button_team,image_button_pms;
    Toolbar toolbar;
    ViewPager my_home_pager;
    ViewPagerAdapter my_home_adapter;
    CharSequence Titles[] = {"Working\nHours"};
    int Numboftabs = 1;
    SwipeRefreshLayout swipeLayout;
    int refresh_checker = 0;

    private SwipeRefreshLayout swipeContainer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View home = inflater.inflate(R.layout.home, container, false);




        my_home_pager = (ViewPager) home.findViewById(R.id.my_home_pager);

        my_home_adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"tab1",getActivity());

        my_home_pager.setAdapter(my_home_adapter);

        swipeContainer = (SwipeRefreshLayout) home.findViewById(R.id.swipe_container2);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                my_home_adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"tab1",getActivity());
                my_home_pager.setAdapter(my_home_adapter);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);





      return home;
    }





}
