package com.MainTelecom_Tablet.CustomClasses;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by MOHAMED on 29/06/2016.
 */
public class C_ViewPager extends ViewPager {

    private boolean mLastPageEnabled = false;
    private int mLastPageIndex = 0;

    public C_ViewPager(Context context) {
        super(context);
    }

    public C_ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLastPageEnabled(boolean enabled) {
        mLastPageEnabled = enabled;
    }

    public void setLastPageIndex(int index) {
        mLastPageIndex = index;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){

        if(!mLastPageEnabled && getCurrentItem() >= (mLastPageIndex - 1)) {
            // Always return false to disable user swipes
            return false;
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!mLastPageEnabled && getCurrentItem() >= (mLastPageIndex - 1)) {
            // Always return false to disable user swipes
            return false;
        }

        return true;
    }
}
