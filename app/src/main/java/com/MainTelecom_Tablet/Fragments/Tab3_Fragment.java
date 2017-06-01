package com.MainTelecom_Tablet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MainTelecom_Tablet.Adapter.ViewPagerAdapter;
import com.MainTelecom_Tablet.R;


/**
 * Created by MOHAMED on 13/04/2016.
 */
public class Tab3_Fragment extends Fragment {


    ViewPager pager_1 ,pager_2;
    ViewPagerAdapter adapter_1 ,adapter_2;
    CharSequence Titles[] = {"Working\nHours"};
    int Numboftabs = 1;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View tab3 = inflater.inflate(R.layout.tab3_layout, container, false);




        pager_1 = (ViewPager) tab3.findViewById(R.id.tab3pager);
        pager_2 = (ViewPager)tab3.findViewById(R.id.fillter_pager);

        adapter_1 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"part_1",getActivity());
        adapter_2 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,Numboftabs,"part_2",getActivity());
        pager_1.setAdapter(adapter_1);
        pager_2.setAdapter(adapter_2);





        return tab3;
    }


//    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
//
//        CharSequence Titles[]; // This will Store the Titles of the Tabs which are
//        // Going to be passed when ViewPagerAdapter is
//        // created
//        int NumbOfTabs; // Store the number of tabs, this will also be passed when
//        // the ViewPagerAdapter is created
//        String position;
//        public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb,String position) {
//            super(fm);
//            this.Titles = mTitles;
//            this.NumbOfTabs = mNumbOfTabsumb;
//            this.position=position;
//            int i =0;
//        }
//
//        @Override
//        public Fragment getItem(int arg0) {
//            // TODO Auto-generated method stub
//
//            switch (arg0) {
//
//                case 0:
//
//                    if(position.equalsIgnoreCase("part_1")) {
//
//                        String s = SaveSharedPreference.getTab3Check(getActivity());
//                        if(s.equals("Home")) {
//                            EvaluationFilterFragment home_team = new EvaluationFilterFragment();
//                            return home_team;
//                        }else {
//                            ChangingListFragment m = new ChangingListFragment();
//                            return  m;
//                        }
//
//                    }
//
//
//
//
//                    /*else if(position.equalsIgnoreCase("part_2")){
//
//                        Home home_pms=new Home();
//                        return home_pms;
//                    }*/
//
//
//
//                default :
//
//                    return null;
//
//            }
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            // TODO Auto-generated method stub
//            return Titles[position];
//        }
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return NumbOfTabs;
//        }
//
//    }


}
