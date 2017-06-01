package com.MainTelecom_Tablet.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.MainTelecom_Tablet.Activities.LoginActivity;
import com.MainTelecom_Tablet.Adapter.ExpandableListAdapter;
import com.MainTelecom_Tablet.Adapter.ViewPagerAdapter;
import com.MainTelecom_Tablet.Helper.Helper;
import com.MainTelecom_Tablet.R;
import com.MainTelecom_Tablet.SharedPreference.SaveSharedPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;




/**
 * Created by MOHAMED on 08/04/2016.
 */
public class ChangingListFragment extends Fragment {


    String s = "";
    private int lastExpandedPosition = -1;
    ViewGroup mContainer;
    CharSequence Titles[] = {"Working\nHours"};
    ImageButton help_bt;
    ExpandableListView team_EXlist_v , contact_EXlist_v,contact_EXlist2_v;
    ExpandableListAdapter team_EX_listAdapter,contact_EX_listAdapter;
    List<String> EX_list_team_Header,EX_list_contact_Header;
    HashMap<String, List<String>> EX_list_team_Child,EX_list_contact_Child;
    ViewGroup.LayoutParams pp_dashbords;
    ViewGroup.LayoutParams pp_agent_queue;

    ProgressDialog pDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View changingList = inflater.inflate(R.layout.test, container, false);

        //ScrollView v = (ScrollView)changingList.findViewById(R.id.scroll1);
        //v.requestFocus();

        help_bt = (ImageButton) changingList.findViewById(R.id.Help_bt);

        /** Help Button */
        help_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**************************************************************************************/


                /*************************************************************************************/

                List<String> mhelp = new ArrayList<String>();
                mhelp.add("Sent Ticket");
                mhelp.add("Call Us");
                mhelp.add("Email Us");
                //Create sequence of items
                final String [] help_bt = mhelp.toArray(new String[mhelp.size()]);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setTitle("Chose What you need....");
                dialogBuilder.setItems(help_bt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if(item==0){

                            final Dialog builder=new Dialog(getActivity());
                            builder.setContentView(R.layout.webpage);
                            builder.setTitle("Ticket");
                            builder.setCancelable(false);

                             builder.show();
                            WebView webView = (WebView) builder.findViewById(R.id.webView);
                            Button cancel = (Button) builder.findViewById(R.id.button2);
                            webView.loadUrl("http://"+SaveSharedPreference.getUserIP(getActivity())+"/Android/ticket.php");
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    builder.dismiss();
                                }
                            });

//

                        }else if(item==1){

                            Calendar c = Calendar.getInstance();
                            int hour = c.get(Calendar.HOUR);

                            if (hour >= 9 && hour <= 17) {

                                Intent call_intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02-3990990"));
                                startActivity(call_intent);
                            }else {

                                AlertDialog.Builder builder2=new AlertDialog.Builder(getActivity());
                                builder2.setTitle("Emergency Call ");
                                builder2.setMessage("-The Work Hours Is FinishedBut If This Emergency Call\nPlease Click (OK)If Not Click (Cancel)");
                                builder2.setCancelable(false);
                                builder2.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent call_intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02-3990990"));
                                        startActivity(call_intent);
                                    }
                                });

                                builder2.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder2.show();

                            }


                        }else if (item==2){
                            String[] recipients = {"hesham@Maintelecom.com"};

                            Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("hesham@Maintelecom.com"));
                            // prompts email clients only
                            email.setType("message/rfc822");
                            email.putExtra(Intent.EXTRA_EMAIL,recipients );
                            email.putExtra(Intent.EXTRA_SUBJECT, "");
                            email.putExtra(Intent.EXTRA_TEXT, "");
                            try {
                                // the user can choose the email client
                                startActivity(Intent.createChooser(email, "Choose an email client from..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getActivity(), "No email client installed.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                //Create alert dialog object via builder
                AlertDialog alertDialogObject = dialogBuilder.create();
                //Show the dialog
                alertDialogObject.show();

            }

        });

/***************************************************************************************************/
        /**Lists Configration*/

        /** team list */
        team_EXlist_v = (ExpandableListView)changingList.findViewById(R.id.expandableListView);
        String[] headers_name_team = getResources().getStringArray(R.array.item_for_teamWFM_EX_List_headers);
        String[] childs_name_team = getResources().getStringArray(R.array.item_for_teamWFM_EX_List);
        EX_list_team_Header = new ArrayList<String>(Arrays.asList(headers_name_team));
        List<String> childs_team = new ArrayList<String>(Arrays.asList(childs_name_team));
        EX_list_team_Child = new HashMap<String, List<String>>();
        EX_list_team_Child.put(EX_list_team_Header.get(0),childs_team);
        team_EX_listAdapter = new ExpandableListAdapter(getActivity(),EX_list_team_Header,EX_list_team_Child);
        team_EXlist_v.setAdapter(team_EX_listAdapter);

         ListView listTeam= (ListView) changingList.findViewById(R.id.listView1);
        String[] listTeam_names = getResources().getStringArray(R.array.item_for_teamWFM_List);
        ArrayAdapter<String> team_list_arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item2,listTeam_names);
        listTeam.setAdapter(team_list_arrayAdapter);
//         Helper.getListViewSize(listTeam);

        /** Contact center list */
        contact_EXlist_v = (ExpandableListView)changingList.findViewById(R.id.expandableListView2);
        String[] headers_name_contact = getResources().getStringArray(R.array.item_for_ContactCenter_EX_List_headers);
        String[] childsAgent_name_contact = getResources().getStringArray(R.array.item_for_ContactCenter_EX_AgentList);
        String[] childsQueue_name_contact = getResources().getStringArray(R.array.item_for_ContactCenter_EX_QueueList);
        EX_list_contact_Header = new ArrayList<String>(Arrays.asList(headers_name_contact));
        List<String> Agentchilds_contact = new ArrayList<String>(Arrays.asList(childsAgent_name_contact));
        List<String> Queuechilds_contact = new ArrayList<String>(Arrays.asList(childsQueue_name_contact));
        EX_list_contact_Child = new HashMap<String, List<String>>();
        EX_list_contact_Child.put(EX_list_contact_Header.get(0),Agentchilds_contact);
        EX_list_contact_Child.put(EX_list_contact_Header.get(1),Queuechilds_contact);
        contact_EX_listAdapter = new ExpandableListAdapter(getActivity(),EX_list_contact_Header,EX_list_contact_Child);
        contact_EXlist_v.setAdapter(contact_EX_listAdapter);

        ListView listContactCenter= (ListView) changingList.findViewById(R.id.listView2);
        String[] listContact_names = getResources().getStringArray(R.array.item_for_ContactCenter_List);
        ArrayAdapter<String> contact_list_arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item2,listContact_names);
        listContactCenter.setAdapter(contact_list_arrayAdapter);
  //      Helper.getListViewSize(listContactCenter);

         ListView mmmm= (ListView) changingList.findViewById(R.id.listView3);
         Helper.getListViewSize(mmmm);

        ListView listSetting= (ListView) changingList.findViewById(R.id.listView4);
        String[] listSetting_names = getResources().getStringArray(R.array.item_for_Setting);
        ArrayAdapter<String> setting_list_arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item2,listSetting_names);
        listSetting.setAdapter(setting_list_arrayAdapter);
    //    Helper.getListViewSize(listSetting);

/**************************************************************************************************/
        /** TeamWFM Listview **/
        listTeam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int potion, long id) {
                team_EXlist_v.collapseGroup(0);
                contact_EXlist_v.collapseGroup(lastExpandedPosition);

                ViewPager viewPager_HOME = (ViewPager) getActivity().findViewById(R.id.pager);
                ViewPager viewPager_TAB3 = (ViewPager) getActivity().findViewById(R.id.tab3pager);
                ViewPager viewPager_FILTER = (ViewPager) getActivity().findViewById(R.id.fillter_pager);

                TabLayout tabLayout_HOME = (TabLayout) getActivity().findViewById(R.id.tab_layout);

                String team_F_name = adapterView.getItemAtPosition(potion).toString();
                SaveSharedPreference.setTab3Check(getActivity(), team_F_name);
                tabLayout_HOME.getTabAt(1).setText(team_F_name);

                ViewPagerAdapter adapter_1 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_1",getActivity());
                ViewPagerAdapter adapter_Filter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_2",getActivity());
                viewPager_TAB3.setAdapter(adapter_1);
                viewPager_FILTER.setVisibility(View.VISIBLE);
                viewPager_FILTER.setAdapter(adapter_Filter);
//                if(team_F_name.equalsIgnoreCase("Team WFM Home")){
//                    viewPager_FILTER.setAdapter(adapter_Filter);
//                }else {
//                    viewPager_FILTER.setVisibility(View.VISIBLE);
//                    viewPager_FILTER.setAdapter(adapter_Filter);
//                }

                viewPager_HOME.setCurrentItem(1);

                //final HomePagerAdapter adapter = new HomePagerAdapter(getActivity().getSupportFragmentManager(), 3,getActivity().getApplicationContext());
                //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                //viewPager.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
                //tabLayout.addTab(tabLayout.newTab().setText(F_name));



//                s = ((TextView)view).getText().toString();
//                if(potion==0){
//                   /* HomeActivity h = new HomeActivity();
//                    Fragment fragment = h.getFrag();
//                    viewPager = h.getViewPager();*/
//
//                    //if (fragment != null)
//
//                        //do the data changes. In this case, I am refreshing the arrayList cart_list and then calling the listview to refresh.
//                        //viewPager.setCurrentItem(2);
//                        EvaluationFilterFragment fragment2 = new EvaluationFilterFragment();
//                        //EvaluationFilterFragment fragment1 = new EvaluationFilterFragment();
//                        FragmentManager fragmentManager = getFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.pager, fragment2);
//                        fragmentTransaction.commit();
//
//                }
            }
        });
        /** TeamWFM EX_Listview **/
        team_EXlist_v.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                contact_EXlist_v.collapseGroup(lastExpandedPosition);

                return false;
            }
        });
        team_EXlist_v.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                pp_dashbords = team_EXlist_v.getLayoutParams();
                pp_dashbords.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                team_EXlist_v.setLayoutParams(pp_dashbords);
                team_EXlist_v.requestLayout();

            }
        });
        team_EXlist_v.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                pp_dashbords = team_EXlist_v.getLayoutParams();
                pp_dashbords.height = 470;
                team_EXlist_v.setLayoutParams(pp_dashbords);
                team_EXlist_v.requestLayout();

            }
        });
        team_EXlist_v.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                ViewPager viewPager_HOME = (ViewPager) getActivity().findViewById(R.id.pager);
                ViewPager viewPager_TAB3 = (ViewPager) getActivity().findViewById(R.id.tab3pager);
                ViewPager viewPager_FILTER = (ViewPager) getActivity().findViewById(R.id.fillter_pager);

                TabLayout tabLayout_HOME = (TabLayout) getActivity().findViewById(R.id.tab_layout);

                String team_F_name =EX_list_team_Child.get(EX_list_team_Header.get(groupPosition)).get(childPosition);
                SaveSharedPreference.setTab3Check(getActivity(), team_F_name);
                tabLayout_HOME.getTabAt(1).setText(team_F_name);

                ViewPagerAdapter adapter_1 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_1",getActivity());
                ViewPagerAdapter adapter_Filter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_2",getActivity());
                viewPager_TAB3.setAdapter(adapter_1);
                if (team_F_name.equals("Activities Evaluation") || team_F_name.equals("Time Evaluation") || team_F_name.equals("Working Hours")) {
                    viewPager_FILTER.setVisibility(View.GONE);
                }else {
                    viewPager_FILTER.setVisibility(View.VISIBLE);
                }
                viewPager_FILTER.setAdapter(adapter_Filter);
                viewPager_HOME.setCurrentItem(1);
                return false;
            }
        });

/****************************************************************************************************/
        /** Contact Center List */
        listContactCenter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int potion, long id) {

                team_EXlist_v.collapseGroup(0);
                contact_EXlist_v.collapseGroup(lastExpandedPosition);

                ViewPager viewPager_HOME = (ViewPager) getActivity().findViewById(R.id.pager);
                ViewPager viewPager_TAB3 = (ViewPager) getActivity().findViewById(R.id.tab3pager);
                ViewPager viewPager_FILTER = (ViewPager) getActivity().findViewById(R.id.fillter_pager);

                TabLayout tabLayout_HOME = (TabLayout) getActivity().findViewById(R.id.tab_layout);

                String contact_F_name = adapterView.getItemAtPosition(potion).toString();

                SaveSharedPreference.setTab3Check(getActivity(), contact_F_name);

                tabLayout_HOME.getTabAt(1).setText(contact_F_name);

                ViewPagerAdapter adapter_2 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_1",getActivity());
                ViewPagerAdapter adapter_filter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_2",getActivity());

                viewPager_TAB3.setAdapter(adapter_2);
                viewPager_FILTER.setVisibility(View.GONE);
                viewPager_FILTER.setAdapter(adapter_filter);

//                if(contact_F_name.equalsIgnoreCase("Contact Center Home")){
//                    viewPager_FILTER.setVisibility(View.GONE);
//                }else {
//                    viewPager_FILTER.setVisibility(View.VISIBLE);
//                    viewPager_FILTER.setAdapter(adapter_filter);
//                }
//                viewPager_FILTER.setAdapter(adapter_filter);
                viewPager_HOME.setCurrentItem(1);

            }
        });
        /** contact center EX_Listview **/
        contact_EXlist_v.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                team_EXlist_v.collapseGroup(0);
                return false;
            }
        });
        contact_EXlist_v.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                pp_agent_queue = contact_EXlist_v.getLayoutParams();
                pp_agent_queue.height = 115;
                contact_EXlist_v.setLayoutParams(pp_agent_queue);
                contact_EXlist_v.requestLayout();


            }
        });
        contact_EXlist_v.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    contact_EXlist_v.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                pp_agent_queue = contact_EXlist_v.getLayoutParams();
                pp_agent_queue.height = 400;
                contact_EXlist_v.setLayoutParams(pp_agent_queue);
                contact_EXlist_v.requestLayout();
            }
        });
        contact_EXlist_v.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                ViewPager viewPager_HOME = (ViewPager) getActivity().findViewById(R.id.pager);
                ViewPager viewPager_TAB3 = (ViewPager) getActivity().findViewById(R.id.tab3pager);
                ViewPager viewPager_FILTER = (ViewPager) getActivity().findViewById(R.id.fillter_pager);

                TabLayout tabLayout_HOME = (TabLayout) getActivity().findViewById(R.id.tab_layout);

                String contact_F_name = EX_list_contact_Child.get(EX_list_contact_Header.get(groupPosition)).get(childPosition);

                SaveSharedPreference.setTab3Check(getActivity(), contact_F_name);

                tabLayout_HOME.getTabAt(1).setText(contact_F_name);

                ViewPagerAdapter adapter_2 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_1",getActivity());
                ViewPagerAdapter adapter_filter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_2",getActivity());
                viewPager_TAB3.setAdapter(adapter_2);
                viewPager_FILTER.setVisibility(View.VISIBLE);
                viewPager_FILTER.setAdapter(adapter_filter);

//                if(contact_F_name.equalsIgnoreCase("Contact Center Home")){
//                    viewPager_FILTER.setVisibility(View.GONE);
//                }else {
//                    viewPager_FILTER.setVisibility(View.VISIBLE);
//                    viewPager_FILTER.setAdapter(adapter_filter);
//                }
//                viewPager_FILTER.setAdapter(adapter_filter);
               viewPager_HOME.setCurrentItem(1);
                return false;
            }
        });

/**************************************************************************************************/
        /** Setting List */
        listSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int potion, long id) {

                String itemName = adapterView.getItemAtPosition(potion).toString();
                if(itemName.equalsIgnoreCase("About Us")){

                    LayoutInflater factory = LayoutInflater.from(getActivity());
                    final View textEntryView = factory.inflate(R.layout.about_layout, null);
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("About Us");
                    builder.setCancelable(false);
                    ImageButton imageButton_youtube =(ImageButton) textEntryView.findViewById(R.id.imageButton2);
                    ImageButton imageButton_facebook =(ImageButton) textEntryView.findViewById(R.id.imageButton1);

                    imageButton_youtube.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                try {
                                    Intent intent_y = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:maintelecom"));
                                    startActivity(intent_y);
                                } catch (ActivityNotFoundException ex) {
                                    Intent intent_y = new Intent(Intent.ACTION_VIEW, Uri.parse("http://youtube.com/user/maintelecom"));
                                    startActivity(intent_y);
                                }
                        }
                    });

                    imageButton_facebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {
                                Intent intent_F = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/maintel"));
                                startActivity(intent_F);
                            } catch (ActivityNotFoundException ex) {
                                Intent intent_F = new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/maintel"));
                                startActivity(intent_F);
                            }

                        }
                    });

                    builder.setView(textEntryView);

                    builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    builder.show();

                }else if (itemName.equalsIgnoreCase("Logout")){
                    Intent intent_L = new Intent(getActivity(), LoginActivity.class);
                    intent_L.addCategory(Intent.CATEGORY_HOME);
                    intent_L.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent_L);

                }


            }
        });


        return changingList;
    }

}
