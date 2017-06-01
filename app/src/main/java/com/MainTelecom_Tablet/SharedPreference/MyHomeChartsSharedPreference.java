package com.MainTelecom_Tablet.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by MOHAMED on 18/04/2016.
 */
public class MyHomeChartsSharedPreference {

    static final String FIRST_Time = "1111";

    static final String PAGER_1 = "Department Activities";
    static final String PAGER_2 = "Department Effort";
    static final String PAGER_3 = "Activities Achieved";
    static final String PAGER_4 = "Activities Planned";
    static final String PAGER_5 = "Effort Planned";
    static final String PAGER_6 = "Effort Achieved";

    //list for Pager_1 & pager_2
    static final String LIST_1_ITEM_0 ="Incoming Calls Agent";
    static final String LIST_1_ITEM_1 ="Outgoing Calls Agent";
    static final String LIST_1_ITEM_2 ="Hourly Incoming Calls Agent";
    static final String LIST_1_ITEM_3 ="Hourly Outgoing Calls Agent";
    static final String LIST_1_ITEM_4 ="Incoming Calls Queue";
    static final String LIST_1_ITEM_5 ="Outgoing Calls Queue";
    static final String LIST_1_ITEM_6 ="Hourly Incoming Calls Queue";
    static final String LIST_1_ITEM_7 ="Hourly Outgoing Calls Queue";
    static final String LIST_1_ITEM_8 ="Working Hours";
    static final String LIST_1_ITEM_9 ="Contact Center Gauges";
    static final String LIST_1_ITEM_10 ="Leads/Industry";

    //List for Pager_3 & Pager_4 & Pager_5 & Pager_6
    static final String LIST_2_ITEM_0 ="Activities";
    static final String LIST_2_ITEM_1 ="Allocation";
    static final String LIST_2_ITEM_2 ="Invoices";
    static final String LIST_2_ITEM_3 ="Leads";
    static final String LIST_2_ITEM_4 ="Workforce Management";


    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setFIRST_Time(Context ctx, String check)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(FIRST_Time,check );
        editor.commit();
    }

    public static String getFIRST_Time(Context ctx )
    {
        return getSharedPreferences(ctx).getString(FIRST_Time, "");
    }


    public static void ChangePagerName(Context ctx, String pagerName ,String pagerPotion)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(pagerPotion, pagerName);
        editor.commit();
    }

    public static String getPagerName(Context ctx ,String pagerPotion)
    {
        return getSharedPreferences(ctx).getString(pagerPotion, "");
    }

    public static void ChangeItemName(Context ctx,String ItemName , String Item)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Item, ItemName);
        editor.commit();
    }

    public static String getItemName(Context ctx , String Item)
    {
        return getSharedPreferences(ctx).getString(Item, "");
    }

}

