package com.MainTelecom_Tablet.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.MainTelecom_Tablet.Activities.HomeActivity;
import com.MainTelecom_Tablet.Fragments.ChangingListFragment;
import com.MainTelecom_Tablet.Fragments.Tab3_Fragment;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by MOHAMED on 05/02/2016.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    private Map<Integer,String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private Context mContext;
    private String mString;


    public HomePagerAdapter(FragmentManager fm, int NumOfTabs ,Context context ,String check) {
        super(fm);
        mFragmentManager = fm;
        this.mNumOfTabs = NumOfTabs;
        mFragmentTags = new HashMap<Integer,String>();
        mContext = context;
        mString = check;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (mString.equals("main")) {
                    //ChangingListFragment tab1 = new ChangingListFragment();
                    HomeActivity tab1 = new HomeActivity();
                    return tab1;

                }else if(mString.equals("list")){
                    ChangingListFragment list_tab1 = new ChangingListFragment();
                    return list_tab1;
                }


            case 1:
                if (mString.equals("main")) {
                    //   ChangingListFragment tab2 = new ChangingListFragment();
                    Tab3_Fragment tab2 = new Tab3_Fragment();
                    return tab2;
                }
//                else if (mString.equals("list")){
//                    EvaluationFilterFragment evaluationFilterFragment =new EvaluationFilterFragment();
//                    return evaluationFilterFragment;
//                }



            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null)
            return null;
        return mFragmentManager.findFragmentByTag(tag);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
