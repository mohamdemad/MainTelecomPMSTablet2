package com.MainTelecom_Tablet;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MainActivity extends ViewPager {

    private boolean mLastPageEnabled = false;
    private int mLastPageIndex = 0;

    public MainActivity(Context context) {
        super(context);
    }

    public MainActivity(Context context, AttributeSet attrs) {
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
