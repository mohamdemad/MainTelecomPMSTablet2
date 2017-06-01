package main.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.MainTelecom_Tablet.R;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Locale;

import main.database_connections.Database_Connection;

//import database_connections.Database_Connection;
//import database_connections.Queues_Connection;

public class Login extends ActionBarActivity implements View.OnClickListener {

	ConnectivityManager connManager;
	Button login_button;
	SharedPreferences sp_data, sp_remember,sp_keep_loging;
	EditText user_name, pass_word, ip_address;
	int sp_size,close;
	private final String url_end = "/Android/login.php";
	protected String enteredUsername, enteredPassword;
	public static String enteredIP;
	private final String url_bigen = "http://";
	CheckBox check_box,check_box_keeplogin;
	Toolbar toolbar;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		close=0;
		toolbar = (Toolbar) findViewById(R.id.app_bar_id);
		setSupportActionBar(toolbar);

		//get queues form server 


		login_button = (Button) findViewById(R.id.login_id);
		user_name = (EditText) findViewById(R.id.txt_user_name);
		pass_word = (EditText) findViewById(R.id.txt_pass_word);
		ip_address = (EditText) findViewById(R.id.txt_ip);
		check_box = (CheckBox) findViewById(R.id.checkBox_rememberme);
		check_box_keeplogin = (CheckBox) findViewById(R.id.checkBox_keeplogin);

		check_box_keeplogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked){
					check_box.setChecked(true);
				}
			}
		});

		sp_data = getSharedPreferences("3G", Context.MODE_PRIVATE);
		sp_size = sp_data.getAll().size();

		connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		check_internet_conn();

		try {

			sp_remember = getSharedPreferences("remember_me",
					Context.MODE_PRIVATE);

			sp_keep_loging = getSharedPreferences("keep_loging",
					Context.MODE_PRIVATE);
			System.out.println(sp_remember.getAll().size());
			System.out.println(sp_remember.getAll().size());

			if (sp_remember.getAll().size() == 0) {

				System.out.println(0);

			} else {

				user_name.setText(sp_remember.getString("username", ""));
				pass_word.setText(sp_remember.getString("password", ""));
				ip_address.setText(sp_remember.getString("ip", ""));
				String checkbox_state = sp_remember.getString("checkbox", "");

				if (checkbox_state.equals("true")) {
					check_box.setChecked(true);

				} else {
					check_box.setChecked(false);

				}
				if (sp_keep_loging.getAll().size() == 0){
					System.out.println("keep_login" + 0);}else {

					String username=sp_keep_loging.getString("username", "");
					String password=sp_keep_loging.getString("password", "");
					String ip=sp_keep_loging.getString("ip", "");
					String checkbox_state_keeplogin = sp_keep_loging.getString("checkbox", "");

					String serverUrl = new StringBuilder(url_bigen).append(ip)
							.append(url_end).toString();
					Database_Connection con = new Database_Connection(this);
					con.execute(serverUrl, username, password);

					if (checkbox_state_keeplogin.equals("true")) {
						check_box_keeplogin.setChecked(true);

					} else {
						check_box_keeplogin.setChecked(false);

					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

		// ////////////create floating buttons///////////////


		// create main float button
		ImageView contact_icon = new ImageView(this); // Create an icon
		contact_icon.setImageResource(R.drawable.contact_us_floating_icon);

		FloatingActionButton contact_action_button = new FloatingActionButton.Builder(
				this).setContentView(contact_icon).build();
		//set size of button
		contact_action_button.getLayoutParams().height = 120;
		contact_action_button.getLayoutParams().width = 120;
		contact_action_button.requestLayout();
		contact_action_button.setBackgroundColor(Color.parseColor("#00000000"));


		// create sub float buttons
		SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
		// repeat many times:
		ImageView facebook_icon = new ImageView(this);
		facebook_icon.setImageResource(R.mipmap.facebook_floating_icon);
		SubActionButton facebook_button = itemBuilder.setContentView(
				facebook_icon).build();
		facebook_button.setBackgroundColor(Color.parseColor("#00000000"));
		facebook_button.setId(R.id.facebook_floating_id);
		//set size of button
		facebook_button.getLayoutParams().height = 100;
		facebook_button.getLayoutParams().width = 100;

		ImageView youtube_icon = new ImageView(this);
		youtube_icon.setImageResource(R.mipmap.youtube_floating_icon);
		SubActionButton youtube_button = itemBuilder.setContentView(
				youtube_icon).build();
		youtube_button.setBackgroundColor(Color.parseColor("#00000000"));
		youtube_button.setId(R.id.youtube_floating_id);
		//set size of button
		youtube_button.getLayoutParams().height = 100;
		youtube_button.getLayoutParams().width = 100;

		ImageView call_icon = new ImageView(this);
		call_icon.setImageResource(R.mipmap.call_floating_icon);
		SubActionButton call_button = itemBuilder.setContentView(call_icon)
				.build();
		call_button.setBackgroundColor(Color.parseColor("#00000000"));
		call_button.setId(R.id.call_floating_id);
		//set size of button
		call_button.getLayoutParams().height = 100;
		call_button.getLayoutParams().width = 100;

		ImageView linkedin_icon = new ImageView(this);
		linkedin_icon.setImageResource(R.mipmap.linkedin_floating_icon);
		SubActionButton linkedin_button = itemBuilder.setContentView(linkedin_icon)
				.build();
		linkedin_button.setBackgroundColor(Color.parseColor("#00000000"));
		linkedin_button.setId(R.id.linkedin_floating_id);
		//set size of button
		linkedin_button.getLayoutParams().height = 100;
		linkedin_button.getLayoutParams().width = 100;

		// add sub float buttons in ui
		FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
				.addSubActionView(facebook_button)
				.addSubActionView(youtube_button)
				.addSubActionView(call_button)
				.addSubActionView(linkedin_button)
				.attachTo(contact_action_button).build();

		call_button.setOnClickListener(this);
		facebook_button.setOnClickListener(this);
		youtube_button.setOnClickListener(this);

	}

	@Override
	public void onBackPressed() {
		close++;
		if(close<=1){
			Toast.makeText(this,"Please click Back again to exit",Toast.LENGTH_LONG).show();
			return;
		}else {
			super.onBackPressed();
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
			case R.id.language_shift_id:

				Locale locale_ar = new Locale("ar");
				Locale locale_en = new Locale("en");
				Locale.setDefault(locale_ar);
				Configuration config = getBaseContext().getResources()
						.getConfiguration();

				if (!config.locale.equals(locale_ar)) {
					config.locale = locale_ar;
					getBaseContext().getResources().updateConfiguration(config,
							null);

				} else {
					config.locale = locale_en;
					getBaseContext().getResources().updateConfiguration(config,
							null);
				}
				finish();
				overridePendingTransition(0, 0);

				startActivity(getIntent());

				break;

		}

		return super.onOptionsItemSelected(item);
	}

	private void check_internet_conn() {

		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo m3G = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		// check for Wi-Fi status
		if (mWifi.isConnectedOrConnecting()) {

			// check for 3G status
		} else if (m3G.isConnectedOrConnecting()) {

			G_dialog(true);

		} else {

			// login_button.setEnabled(false);

			Builder built = new Builder(this);

			built.setTitle("No access to internet");
			built.setMessage("DO you want to turn on WiFi or 3G ? ");
			built.setIcon(R.mipmap.noaccess);
			built.setCancelable(false);

			built.setNegativeButton("Cancel", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					dialog.dismiss();

				}
			});
			built.setPositiveButton("Turn on WiFi", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
					wifimanager.setWifiEnabled(true);

					login_button.setEnabled(true);
					Toast.makeText(Login.this, "WiFi is Turning on",
							Toast.LENGTH_LONG).show();

				}
			});
			built.setNeutralButton("Turn on 3G", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					login_button.setEnabled(true);

					if (sp_size == 0) {
						G_dialog(false);
					}else{
						Toast.makeText(Login.this, "3G is Turning on",
								Toast.LENGTH_LONG).show();
					}
				}
			});

			built.show();

		}

	}

	private void G_dialog(final boolean checker) {

		if (sp_size == 0) {

			Builder built2 = new Builder(this);
			built2.setTitle("Attention!");
			built2.setMessage("Data charges may apply." + '\n'
					+ "It's recommended to use a wifi connection.");
			built2.setIcon(R.mipmap.attention);

			// ToDo SP
			final CheckBox checkbox = new CheckBox(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			checkbox.setLayoutParams(lp);
			checkbox.setText("Don`t remind me again");
			built2.setView(checkbox);

			built2.setCancelable(false);

			built2.setNegativeButton("Ok", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					if (checkbox.isChecked()) {

						  SharedPreferences sp=getSharedPreferences("3G",
						  Context.MODE_PRIVATE); Editor edit=sp.edit();
						  edit.putString("key", "1"); edit.commit();

					}

					if (checker == true) {

						dialog.dismiss();
						System.out.println("true");

					} else if (checker == false) {

						System.out.println("false");
						try {

							final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
							final Class conmanClass = Class.forName(conman
									.getClass().getName());
							final java.lang.reflect.Field iConnectivityManagerField = conmanClass
									.getDeclaredField("mService");
							iConnectivityManagerField.setAccessible(true);
							final Object iConnectivityManager = iConnectivityManagerField
									.get(conman);
							final Class iConnectivityManagerClass = Class
									.forName(iConnectivityManager.getClass()
											.getName());
							final Method setMobileDataEnabledMethod = iConnectivityManagerClass
									.getDeclaredMethod("setMobileDataEnabled",
											Boolean.TYPE);
							setMobileDataEnabledMethod.setAccessible(true);
							setMobileDataEnabledMethod.invoke(
									iConnectivityManager, true);

						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Toast.makeText(Login.this, "3G is Turning on",
								Toast.LENGTH_LONG).show();

						dialog.dismiss();

					}

				}
			});
			built2.setPositiveButton("Switch to Wifi", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					if (checkbox.isChecked()) {

						SharedPreferences sp = getSharedPreferences("test",
								Context.MODE_PRIVATE);
						Editor edit = sp.edit();
						edit.putString("key", "1");
						edit.commit();

					}

					WifiManager wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
					wifimanager.setWifiEnabled(true);

					try {

						final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
						final Class conmanClass = Class.forName(conman
								.getClass().getName());
						final java.lang.reflect.Field iConnectivityManagerField = conmanClass
								.getDeclaredField("mService");
						iConnectivityManagerField.setAccessible(true);
						final Object iConnectivityManager = iConnectivityManagerField
								.get(conman);
						final Class iConnectivityManagerClass = Class
								.forName(iConnectivityManager.getClass()
										.getName());
						final Method setMobileDataEnabledMethod = iConnectivityManagerClass
								.getDeclaredMethod("setMobileDataEnabled",
										Boolean.TYPE);
						setMobileDataEnabledMethod.setAccessible(true);
						setMobileDataEnabledMethod.invoke(iConnectivityManager,
								false);

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			built2.show();
		}

	}

	public void log_in_click(View v) {

		enteredUsername = user_name.getText().toString();
		enteredPassword = pass_word.getText().toString();
		enteredIP = ip_address.getText().toString();

		sp_remember = getSharedPreferences("remember_me", Context.MODE_PRIVATE);
		Editor edit = sp_remember.edit();

		sp_keep_loging = getSharedPreferences("keep_loging", Context.MODE_PRIVATE);
		Editor edit_keeplogin = sp_keep_loging.edit();

		// startActivity(new Intent(getApplicationContext(),Home.class));

		if (enteredUsername.equals("") || enteredPassword.equals("")
				|| enteredIP.equals("")) {

			Toast.makeText(Login.this,
					"Username or Password or Ip must be filled",
					Toast.LENGTH_LONG).show();

			return;

		}

		if (enteredUsername.length() <= 1 || enteredPassword.length() <= 1) {

			Toast.makeText(Login.this,
					"Username or password length must be greater than one",
					Toast.LENGTH_LONG).show();

			return;

		}
		if (enteredIP.length() <= 9) {
			Toast.makeText(Login.this, "IP address must be bigger than 9 nu.",
					Toast.LENGTH_LONG).show();
			return;
		}

		String serverUrl = new StringBuilder(url_bigen).append(enteredIP)
				.append(url_end).toString();

		// asyncRequestObject.execute(serverUrl, enteredUsername,
		// enteredPassword);
		if (check_box.isChecked()) {
			edit.putString("username", enteredUsername);
			edit.putString("password", enteredPassword);
			edit.putString("ip", enteredIP);
			edit.putString("checkbox", "true");
			edit.commit();

		} else {
			edit.clear();
			edit.commit();
		}

		if (check_box_keeplogin.isChecked()){
			check_box.setChecked(true);
			edit_keeplogin.putString("username", enteredUsername);
			edit_keeplogin.putString("password", enteredPassword);
			edit_keeplogin.putString("ip", enteredIP);
			edit_keeplogin.putString("checkbox", "true");
			edit_keeplogin.commit();

		}else {
			edit_keeplogin.clear();
			edit_keeplogin.commit();
		}

		Database_Connection con = new Database_Connection(this);
		con.execute(serverUrl, enteredUsername, enteredPassword);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
			case R.id.facebook_floating_id:

				Intent facebook_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + "facebook.com/maintel"));
				startActivity(facebook_intent);

				break;

			case R.id.youtube_floating_id:
				Intent youtube_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + "youtube.com/user/maintelecom"));
				startActivity(youtube_intent);

				break;

			case R.id.call_floating_id:

				Calendar c = Calendar.getInstance();
				int hour = c.get(Calendar.HOUR);

				if (hour >= 9 && hour <= 17) {

					Intent call_intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02-3990990"));
					if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    public void requestPermissions(@NonNull String[] permissions, int requestCode)
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for Activity#requestPermissions for more details.
						Toast.makeText(this,"Turn on call permission to can use this feature",Toast.LENGTH_LONG);
						return;
					}else {

						startActivity(call_intent);

					}


			}else {
				
				Toast toast=Toast.makeText(this,"Time of work from 9 to 17", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				
			}
			
			break;
			
		case R.id.linkedin_floating_id:
			
			Intent linkedin_intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+"linkedin.com/company/main-telecom"));
			startActivity(linkedin_intent);
			
			break;	

		default:
			break;
		}
	}
}
