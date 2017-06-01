package com.MainTelecom_Tablet.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.MainTelecom_Tablet.R;
import com.MainTelecom_Tablet.SharedPreference.SaveSharedPreference;

import main.database_connections.Database_Connection;


/**
 * Created by MOHAMED on 03/05/2016.
 */
public class LoginActivity extends ActionBarActivity {

    ConnectivityManager connManager;
    Button login_button;
    EditText user_name, pass_word, ip_address;
    int close;
    private final String url_end = "/Android/login.php";
    public static String enteredIP;
    private final String url_bigen = "http://";
    CheckBox check_box_remember,check_box_keeplogin;
    Toolbar toolbar;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        close=0;
//        toolbar = (Toolbar) findViewById(R.id.app_bar_id);
//        setSupportActionBar(toolbar);

        //get queues form server

        login_button = (Button) findViewById(R.id.login_id);
        user_name = (EditText) findViewById(R.id.txt_user_name);
        pass_word = (EditText) findViewById(R.id.txt_pass_word);
        ip_address = (EditText) findViewById(R.id.txt_ip);
        check_box_remember = (CheckBox) findViewById(R.id.checkBox_rememberme);
        check_box_keeplogin = (CheckBox) findViewById(R.id.checkBox_keeplogin);

        if(SaveSharedPreference.getUserRemember(LoginActivity.this).equals("check")){
            check_box_remember.setChecked(true);
            user_name.setText( SaveSharedPreference.getUserName(LoginActivity.this));
            pass_word.setText(SaveSharedPreference.getUserPassword(LoginActivity.this));
            ip_address.setText(SaveSharedPreference.getUserIP(LoginActivity.this));
        }
        if (SaveSharedPreference.getUserKeepLogin(LoginActivity.this).equals("check")) {
            check_box_keeplogin.setChecked(true);
        }


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user_name.getText().toString().equals(""))
                    Toast.makeText(LoginActivity.this,"USER NAME IS EMPTY",Toast.LENGTH_SHORT).show();
                else if(pass_word.getText().toString().equals(""))
                    Toast.makeText(LoginActivity.this,"PASSWORD IS EMPTY",Toast.LENGTH_SHORT).show();
                else if(ip_address.getText().toString().equals(""))
                    Toast.makeText(LoginActivity.this,"IP_ADDRESS IS EMPTY",Toast.LENGTH_SHORT).show();


                else{
                    Log.d("WHAT : ",pass_word.getText().toString());
                    connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(check_internet_conn()) {

                        if (check_box_remember.isChecked() && check_box_keeplogin.isChecked()) {
                            SaveSharedPreference.setUserRemember(LoginActivity.this, "check");
                            SaveSharedPreference.setUserKeepLogin(LoginActivity.this, "check");
                            SaveSharedPreference.setUserName(LoginActivity.this, user_name.getText().toString());
                            SaveSharedPreference.setUserPassword(LoginActivity.this, pass_word.getText().toString());
                            SaveSharedPreference.setUserIP(LoginActivity.this, ip_address.getText().toString());

                        } else if (check_box_remember.isChecked()) {
                            SaveSharedPreference.setUserRemember(LoginActivity.this, "check");
                            SaveSharedPreference.setUserName(LoginActivity.this, user_name.getText().toString());
                            SaveSharedPreference.setUserPassword(LoginActivity.this, pass_word.getText().toString());
                            SaveSharedPreference.setUserIP(LoginActivity.this, ip_address.getText().toString());

                        } else if (check_box_keeplogin.isChecked()) {
                            SaveSharedPreference.setUserKeepLogin(LoginActivity.this, "check");
                            SaveSharedPreference.setUserName(LoginActivity.this, user_name.getText().toString());
                            SaveSharedPreference.setUserPassword(LoginActivity.this, pass_word.getText().toString());
                            SaveSharedPreference.setUserIP(LoginActivity.this, ip_address.getText().toString());

                        } else {

                            SaveSharedPreference.setUserRemember(LoginActivity.this, "uncheck");
                            SaveSharedPreference.setUserKeepLogin(LoginActivity.this, "uncheck");
                            SaveSharedPreference.setUserName(LoginActivity.this, "");
                            SaveSharedPreference.setUserPassword(LoginActivity.this, "");
                            SaveSharedPreference.setUserIP(LoginActivity.this, ip_address.getText().toString());
                        }

                        String serverUrl = new StringBuilder(url_bigen).append(ip_address.getText().toString()).append(url_end).toString();
                        Database_Connection con = new Database_Connection(LoginActivity.this);
                        con.execute(serverUrl, user_name.getText().toString(), pass_word.getText().toString());

//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.addCategory(Intent.CATEGORY_HOME);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);

                    }else {
                        AlertDialog.Builder built = new AlertDialog.Builder(LoginActivity.this);
                        built.setTitle("No access to internet");
                        built.setMessage("DO you want to turn on WiFi? ");
                        built.setIcon(R.mipmap.noaccess);
                        built.setCancelable(false);
                        built.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });
                        built.setPositiveButton("Turn on WiFi", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                                wifimanager.setWifiEnabled(true);
                                Toast.makeText(LoginActivity.this, "WiFi is Turning on", Toast.LENGTH_LONG).show();

                            }
                        });
//            built.setNeutralButton("Turn on 3G", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // TODO Auto-generated method stub
//                    if (sp_size == 0) {
//                        G_dialog(false);
//                    }else{
//                        Toast.makeText(LoginActivity.this, "3G is Turning on", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
                        built.show();
                    }
                }

            }
        });


//        // ////////////create floating buttons///////////////
//
//
//        // create main float button
//        ImageView contact_icon = new ImageView(this); // Create an icon
//        contact_icon.setImageResource(R.drawable.contact_us_floating_icon);
//
//        FloatingActionButton contact_action_button = new FloatingActionButton.Builder(
//                this).setContentView(contact_icon).build();
//        //set size of button
//        contact_action_button.getLayoutParams().height = 120;
//        contact_action_button.getLayoutParams().width = 120;
//        contact_action_button.requestLayout();
//        contact_action_button.setBackgroundColor(Color.parseColor("#00000000"));
//
//
//        // create sub float buttons
//        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
//        // repeat many times:
//        ImageView facebook_icon = new ImageView(this);
//        facebook_icon.setImageResource(R.mipmap.facebook_floating_icon);
//        SubActionButton facebook_button = itemBuilder.setContentView(
//                facebook_icon).build();
//        facebook_button.setBackgroundColor(Color.parseColor("#00000000"));
//        facebook_button.setId(R.id.facebook_floating_id);
//        //set size of button
//        facebook_button.getLayoutParams().height = 100;
//        facebook_button.getLayoutParams().width = 100;
//
//        ImageView youtube_icon = new ImageView(this);
//        youtube_icon.setImageResource(R.mipmap.youtube_floating_icon);
//        SubActionButton youtube_button = itemBuilder.setContentView(
//                youtube_icon).build();
//        youtube_button.setBackgroundColor(Color.parseColor("#00000000"));
//        youtube_button.setId(R.id.youtube_floating_id);
//        //set size of button
//        youtube_button.getLayoutParams().height = 100;
//        youtube_button.getLayoutParams().width = 100;
//
//        ImageView call_icon = new ImageView(this);
//        call_icon.setImageResource(R.mipmap.call_floating_icon);
//        SubActionButton call_button = itemBuilder.setContentView(call_icon)
//                .build();
//        call_button.setBackgroundColor(Color.parseColor("#00000000"));
//        call_button.setId(R.id.call_floating_id);
//        //set size of button
//        call_button.getLayoutParams().height = 100;
//        call_button.getLayoutParams().width = 100;
//
//        ImageView linkedin_icon = new ImageView(this);
//        linkedin_icon.setImageResource(R.mipmap.linkedin_floating_icon);
//        SubActionButton linkedin_button = itemBuilder.setContentView(linkedin_icon)
//                .build();
//        linkedin_button.setBackgroundColor(Color.parseColor("#00000000"));
//        linkedin_button.setId(R.id.linkedin_floating_id);
//        //set size of button
//        linkedin_button.getLayoutParams().height = 100;
//        linkedin_button.getLayoutParams().width = 100;
//
//        // add sub float buttons in ui
//        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
//                .addSubActionView(facebook_button)
//                .addSubActionView(youtube_button)
//                .addSubActionView(call_button)
//                .addSubActionView(linkedin_button)
//                .attachTo(contact_action_button).build();
//
//        call_button.setOnClickListener(this);
//        facebook_button.setOnClickListener(this);
//        youtube_button.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        close++;
        if(close<=1){
            Toast.makeText(this,"Please click Back again to exit",Toast.LENGTH_LONG).show();
            return;
        }else {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }



    private boolean check_internet_conn() {

        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        // check for Wi-Fi status
        if (mWifi.isConnectedOrConnecting()) {
            return true;
        } else if (m3G.isConnectedOrConnecting()) {
            return true;

        }
        return false;
    }

//    private void G_dialog(final boolean checker) {
//
//        if (sp_size == 0) {
//
//            AlertDialog.Builder built2 = new AlertDialog.Builder(this);
//            built2.setTitle("Attention!");
//            built2.setMessage("Data charges may apply." + "\n" + "It's recommended to use a wifi connection.");
//            built2.setIcon(R.mipmap.attention);
//
//            // ToDo SP
//            final CheckBox checkbox = new CheckBox(this);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            checkbox.setLayoutParams(lp);
//            checkbox.setText("Don`t remind me again");
//            built2.setView(checkbox);
//            built2.setCancelable(false);
//            built2.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//
//                public void onClick(DialogInterface dialog, int which) {
//                    // TODO Auto-generated method stub
//
//                    if (checkbox.isChecked()) {
//
//                        SharedPreferences sp=getSharedPreferences("3G", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor edit=sp.edit();
//                        edit.putString("key", "1"); edit.commit();
//
//                    }
//
//                    if (checker == true) {
//
//                        dialog.dismiss();
//                        System.out.println("true");
//
//                    } else if (checker == false) {
//
//                        System.out.println("false");
//                        try {
//
//                            final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                            final Class conmanClass = Class.forName(conman.getClass().getName());
//                            final java.lang.reflect.Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
//                            iConnectivityManagerField.setAccessible(true);
//                            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
//                            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
//                            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
//                            setMobileDataEnabledMethod.setAccessible(true);
//                            setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
//
//                        } catch (IllegalArgumentException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } catch (ClassNotFoundException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } catch (IllegalAccessException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } catch (NoSuchFieldException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } catch (NoSuchMethodException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } catch (InvocationTargetException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//
//                        Toast.makeText(Login.this, "3G is Turning on", Toast.LENGTH_LONG).show();
//
//                        dialog.dismiss();
//
//                    }
//
//                }
//            });
//            built2.setPositiveButton("Switch to Wifi", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // TODO Auto-generated method stub
//
//                    if (checkbox.isChecked()) {
//
//                        SharedPreferences sp = getSharedPreferences("test",
//                                Context.MODE_PRIVATE);
//                        SharedPreferences.Editor edit = sp.edit();
//                        edit.putString("key", "1");
//                        edit.commit();
//
//                    }
//
//                    WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//                    wifimanager.setWifiEnabled(true);
//
//                    try {
//
//                        final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                        final Class conmanClass = Class.forName(conman
//                                .getClass().getName());
//                        final java.lang.reflect.Field iConnectivityManagerField = conmanClass
//                                .getDeclaredField("mService");
//                        iConnectivityManagerField.setAccessible(true);
//                        final Object iConnectivityManager = iConnectivityManagerField
//                                .get(conman);
//                        final Class iConnectivityManagerClass = Class
//                                .forName(iConnectivityManager.getClass()
//                                        .getName());
//                        final Method setMobileDataEnabledMethod = iConnectivityManagerClass
//                                .getDeclaredMethod("setMobileDataEnabled",
//                                        Boolean.TYPE);
//                        setMobileDataEnabledMethod.setAccessible(true);
//                        setMobileDataEnabledMethod.invoke(iConnectivityManager,
//                                false);
//
//                    } catch (IllegalArgumentException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (ClassNotFoundException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (NoSuchFieldException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (NoSuchMethodException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//            built2.show();
//        }
//
//    }


//    @Override
//    public void onClick(View v) {
//        // TODO Auto-generated method stub
//
//        switch (v.getId()) {
//            case R.id.facebook_floating_id:
//
//                Intent facebook_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + "facebook.com/maintel"));
//                startActivity(facebook_intent);
//
//                break;
//
//            case R.id.youtube_floating_id:
//                Intent youtube_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + "youtube.com/user/maintelecom"));
//                startActivity(youtube_intent);
//
//                break;
//
//            case R.id.call_floating_id:
//
//                Calendar c = Calendar.getInstance();
//                int hour = c.get(Calendar.HOUR);
//
//                if (hour >= 9 && hour <= 17) {
//
//                    Intent call_intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02-3990990"));
//                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for Activity#requestPermissions for more details.
//                        Toast.makeText(this,"Turn on call permission to can use this feature",Toast.LENGTH_LONG);
//                        return;
//                    }else {
//
//                        startActivity(call_intent);
//
//                    }
//
//
//                }else {
//
//                    Toast toast=Toast.makeText(this,"Time of work from 9 to 17", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//
//                }
//
//                break;
//
//            case R.id.linkedin_floating_id:
//
//                Intent linkedin_intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+"linkedin.com/company/main-telecom"));
//                startActivity(linkedin_intent);
//
//                break;
//
//            default:
//                break;
//        }
//    }
}

