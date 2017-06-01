package com.MainTelecom_Tablet.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by MOHAMED on 12/04/2016.
 */
public class SaveSharedPreference {

    static final String USER_NAME= "username";
    static final String USER_PASSWORD= "123456";
    static final String USER_IP= "192.168.1.1";
    static final String USER_REMEMBER= "ckecked";
    static final String USER_KEEP_LOGIN= "checkedd";
    static final String INTRO_CHECK= "checkedddd";
    static final String TAB3_CHECK= "def";


    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

/***************************************************************************************************/

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(USER_NAME, "");
    }

/**************************************************************************************************/

    public static void setUserPassword(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_PASSWORD, userName);
        editor.commit();
    }

    public static String getUserPassword(Context ctx)
    {
        return getSharedPreferences(ctx).getString(USER_PASSWORD, "");
    }

/***************************************************************************************************/

    public static void setUserIP(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_IP, userName);
        editor.commit();
    }

    public static String getUserIP(Context ctx)
    {
        return getSharedPreferences(ctx).getString(USER_IP, "");
    }

/***************************************************************************************************/

    public static void setUserRemember(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_REMEMBER, userName);
        editor.commit();
    }

    public static String getUserRemember(Context ctx)
    {
        return getSharedPreferences(ctx).getString(USER_REMEMBER, "uncheck");
    }

/***************************************************************************************************/

    public static void setUserKeepLogin(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_KEEP_LOGIN, userName);
        editor.commit();
    }

    public static String getUserKeepLogin(Context ctx)
    {
        return getSharedPreferences(ctx).getString(USER_KEEP_LOGIN, "uncheck");
    }

/***************************************************************************************************/

     public static void setIntroCheck(Context ctx, String userName)
     {
         SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
         editor.putString(INTRO_CHECK, userName);
         editor.commit();
     }

    public static String getIntroCheck(Context ctx)
    {
        return getSharedPreferences(ctx).getString(INTRO_CHECK, "uncheck");
    }

/***************************************************************************************************/

    public static void setTab3Check(Context ctx, String check)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(TAB3_CHECK, check);
        editor.commit();
    }

    public static String getTab3Check(Context ctx)
    {
        return getSharedPreferences(ctx).getString(TAB3_CHECK, "");
    }

}
