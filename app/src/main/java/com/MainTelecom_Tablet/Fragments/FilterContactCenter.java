package com.MainTelecom_Tablet.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.MainTelecom_Tablet.Adapter.FilterChartsAdapter;
import com.MainTelecom_Tablet.App.AppConfig;
import com.MainTelecom_Tablet.JSON.JSONParser;
import com.MainTelecom_Tablet.R;
import com.MainTelecom_Tablet.SharedPreference.SaveSharedPreference;
import com.doodle.android.chips.ChipsView;
import com.doodle.android.chips.model.Contact;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by MOHAMED on 06/05/2016.
 */
public class FilterContactCenter extends Fragment {

    CharSequence Titles[] = {"Working\nHours"};
    private int Year ,Month , Day;
    private EditText FromDate, ToDate;
    private TextView textView_Title , textView_FromDate ,textView_toDate ,textView_Queue ,textView_Project;
    private TextView textView_SelectAgent ;
    private AutoCompleteTextView mQueue , mProject ,mSelectAgent ;
    private Button search_bt;
    private Space space_2;
    private RelativeLayout fromDate_layout ,toDate_layout , queue_layout,project_layout,SelectAgent_layout;

    static final int FROM_DATE_PICKER_ID = 0;
    static final int TO_DATE_PICKER_ID = 1;

    ProgressDialog pDialog;

     ArrayList<String> Queue_L = new ArrayList<String>();
     ArrayList<String> SelectAgent_L = new ArrayList<String>() ;
     ArrayList<String> Project_L = new ArrayList<String>() ;

    ArrayAdapter<String> adapterQueue ;
    ArrayAdapter<String> adapterProject ;
    ArrayAdapter<String> adapterSelectAgent;


    private ArrayList<String> Queue_L_ID = new ArrayList<String>();
    private ArrayList<String> SelectAgent_L_ID = new ArrayList<String>();
    private ArrayList<String> Project_L_ID = new ArrayList<String>();

    private Boolean mRoleCheck = false;
    private Boolean mProjectCheck = false;
    private Boolean mEmployeeCheck = false;
    private Boolean isSelectQueues = false;

    private ChipsView mChipsView_SelectAgent;
    private ViewPager viewPager_filter;
    private  RelativeLayout.LayoutParams params;

    private JSONParser jParser_Projects = new JSONParser();
    private JSONParser jParser_Queue = new JSONParser();
    private JSONParser jParser_Agents = new JSONParser();
    private JSONParser xcxc= new JSONParser();
    private JSONParser xcxc2= new JSONParser();
    List<NameValuePair> params_projects = new ArrayList<NameValuePair>();
    List<NameValuePair> params_queue = new ArrayList<NameValuePair>();
    List<NameValuePair> params_agents = new ArrayList<NameValuePair>();
    List<NameValuePair> params_search = new ArrayList<NameValuePair>();



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Toast.makeText(getActivity(), SaveSharedPreference.getTab3Check(getActivity()),Toast.LENGTH_SHORT).show();

        View Eva_filter = inflater.inflate(R.layout.filter_contact_center_layout, container, false);

        adapterQueue = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Queue_L);
        adapterProject = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Project_L);
        adapterSelectAgent = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,SelectAgent_L);

        params_queue.add(new BasicNameValuePair("",""));
        params_agents.add(new BasicNameValuePair("",""));
        params_agents.add(new BasicNameValuePair("",""));

        new Connection().execute();

        FromDate = (EditText)Eva_filter.findViewById(R.id.Eva_FromDate_EditTxt);
        ToDate = (EditText)Eva_filter.findViewById(R.id.Eva_ToDate_EditTxt);
        mSelectAgent = (AutoCompleteTextView) Eva_filter.findViewById(R.id.select_agent_AutoFill);
        mQueue = (AutoCompleteTextView) Eva_filter.findViewById(R.id.queue_AutoFill);
        mProject = (AutoCompleteTextView) Eva_filter.findViewById(R.id.Eva_Project_AutoFill);


        textView_FromDate = (TextView) Eva_filter.findViewById(R.id.textView5);
        textView_toDate = (TextView) Eva_filter.findViewById(R.id.textView6);
        textView_SelectAgent = (TextView) Eva_filter.findViewById(R.id.textView8);
        textView_Project = (TextView) Eva_filter.findViewById(R.id.textView10);
        textView_Queue = (TextView) Eva_filter.findViewById(R.id.textView7);
        textView_Title = (TextView) Eva_filter.findViewById(R.id.textView18);
        search_bt = (Button) Eva_filter.findViewById(R.id.button);
        space_2 = (Space) Eva_filter.findViewById(R.id.space);


        fromDate_layout = (RelativeLayout)Eva_filter.findViewById(R.id.fromDate_layout);
        toDate_layout = (RelativeLayout)Eva_filter.findViewById(R.id.toDate_layout);
        queue_layout = (RelativeLayout)Eva_filter.findViewById(R.id.queue_layout);
        project_layout = (RelativeLayout)Eva_filter.findViewById(R.id.project_layout);
        SelectAgent_layout = (RelativeLayout)Eva_filter.findViewById(R.id.select_agent_layout);


        mChipsView_SelectAgent = (ChipsView) Eva_filter.findViewById(R.id.select_agent_Contant);

        viewPager_filter =(ViewPager)getActivity().findViewById(R.id.fillter_pager);
        if (SaveSharedPreference.getTab3Check(getActivity()).equalsIgnoreCase("Contact Center Home"))
            viewPager_filter.setVisibility(View.GONE);
        params =(RelativeLayout.LayoutParams)viewPager_filter.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_BOTTOM,0);

        if(SaveSharedPreference.getTab3Check(getActivity()).equals("Incoming Calls Queue")||
                SaveSharedPreference.getTab3Check(getActivity()).equals("Outgoing Calls Queue")||
                SaveSharedPreference.getTab3Check(getActivity()).equals("Hourly Incoming Calls Queue")||
                SaveSharedPreference.getTab3Check(getActivity()).equals("Hourly Outgoing Calls Queue")) {
            isSelectQueues=true;
        }


        textView_Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(textView_Title.getText().equals("Click To Filter Data")){
                    //Animation emerge = AnimationUtils.loadAnimation(getActivity(), R.anim.emerge);
                    //viewPager_filter.startAnimation(emerge);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.space8);
                    viewPager_filter.setLayoutParams(params);


                    textView_Title.setText("click to move");

                    if (SaveSharedPreference.getTab3Check(getActivity()).equals("Incoming Calls Agent")||
                            SaveSharedPreference.getTab3Check(getActivity()).equals("Outgoing Calls Agent")||
                            SaveSharedPreference.getTab3Check(getActivity()).equals("Hourly Incoming Calls Agent")||
                            SaveSharedPreference.getTab3Check(getActivity()).equals("Hourly Outgoing Calls Agent"))
                    {
                        space_2.setVisibility(View.VISIBLE);
                        fromDate_layout.setVisibility(View.VISIBLE);
                        toDate_layout.setVisibility(View.VISIBLE);
                        project_layout.setVisibility(View.VISIBLE);
                        queue_layout.setVisibility(View.VISIBLE);
                        SelectAgent_layout.setVisibility(View.VISIBLE);
                        search_bt.setVisibility(View.VISIBLE);

                        /***********************************************/


                    }else if(SaveSharedPreference.getTab3Check(getActivity()).equals("Incoming Calls Queue")||
                            SaveSharedPreference.getTab3Check(getActivity()).equals("Outgoing Calls Queue")||
                            SaveSharedPreference.getTab3Check(getActivity()).equals("Hourly Incoming Calls Queue")||
                            SaveSharedPreference.getTab3Check(getActivity()).equals("Hourly Outgoing Calls Queue")) {
                        space_2.setVisibility(View.VISIBLE);
                        fromDate_layout.setVisibility(View.VISIBLE);
                        toDate_layout.setVisibility(View.VISIBLE);
                        textView_SelectAgent.setText("Select Queues :-");
                        SelectAgent_layout.setVisibility(View.VISIBLE);
                        project_layout.setVisibility(View.VISIBLE);
                        search_bt.setVisibility(View.VISIBLE);

                        isSelectQueues = true;
                    }

                        /**********************************************************/


                }else {

                    space_2.setVisibility(View.GONE);
                    fromDate_layout.setVisibility(View.GONE);
                    toDate_layout.setVisibility(View.GONE);
                    queue_layout.setVisibility(View.GONE);
                    SelectAgent_layout.setVisibility(View.GONE);
                    project_layout.setVisibility(View.GONE);
                    search_bt.setVisibility(View.GONE);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM,0);
                    textView_Title.setText("Click To Filter Data");
                }

            }
        });




        // change EditText config
        mChipsView_SelectAgent.getEditText().setCursorVisible(false);
        mChipsView_SelectAgent.setFocusable(false);
        mChipsView_SelectAgent.setFocusableInTouchMode(false);
        mChipsView_SelectAgent.getEditText().setFocusable(false);
        mChipsView_SelectAgent.getEditText().setFocusableInTouchMode(false);
       //mChipsView_SelectAgent.getEditText().setKeyListener(null);
       //mChipsView_SelectAgent.getEditText().setInputType(InputType.TYPE_NULL);


        /** get currnt date from calender*/
        final Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH);
        Day = c.get(Calendar.DAY_OF_MONTH);

        FromDate.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), frompickerListener, Year, Month, Day).show();
            }
        });
        ToDate.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), topickerListener, Year, Month, Day).show();
            }
        });

//        mChipsView_Employee.setChipsValidator(new ChipsView.ChipValidator() {
//            @Override
//            public boolean isValid(Contact contact) {
//                if (contact.getDisplayName().equals("asd@qwe.de")) {
//                    return false;
//                }
//                return true;
//            }
//        });
/**
 * (FOR CHIPS_VIEW CHECK )
 for (ChipsView.Chip chipItem : mChipsView_Employee.getChips()) {
 Log.d("ChipList", "chip: " + chipItem.toString());
 }*/




        return Eva_filter;
    }

    /** when dialog box is closed, below method will be called.*/

    private DatePickerDialog.OnDateSetListener frompickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

//            Year  = selectedYear;
//            Month = selectedMonth;
//            Day   = selectedDay;

            FromDate.setText(new StringBuilder().append(selectedYear)
                    .append("-").append(selectedMonth + 1).append("-").append(selectedDay));
        }
    };
    private DatePickerDialog.OnDateSetListener topickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

//            Year  = selectedYear;
//            Month = selectedMonth;
//            Day   = selectedDay;

            ToDate.setText(new StringBuilder().append(selectedYear)
                    .append("-").append(selectedMonth + 1).append("-").append(selectedDay));
        }
    };

    /** Get Lists Data from Server*/

    public class Connection extends AsyncTask<String, String, String> {




        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * getting All Data from url
         */
        protected String doInBackground(String... args) {

            Project_L.clear();
            Project_L_ID.clear();
            Queue_L.clear();
            Queue_L_ID.clear();
            SelectAgent_L.clear();
            SelectAgent_L_ID.clear();



        try {

            List<NameValuePair> xxx = new ArrayList<NameValuePair>();
//            params_queue.add(new BasicNameValuePair("projID","43"));
//            params_agents.add(new BasicNameValuePair("projID","43"));
//            params_agents.add(new BasicNameValuePair("queueID","13"));
            xxx.add(new BasicNameValuePair("FromDate","2015-6-1"));
            xxx.add(new BasicNameValuePair("ToDate","2016-6-15"));
            //xxx.add(new BasicNameValuePair("Select1[]","16"));
            //xxx.add(new BasicNameValuePair("Select1[]","49599"));
            //xxx.add(new BasicNameValuePair("Industry","--None--"));

            //String sese = "http://"+ SaveSharedPreference.getUserIP(getActivity())+ AppConfig.url_Projects_Data_List;
            //Connection connection =new Connection(sese,params_projects,jParser_Projects);
            //Project_L = connection.arrayList;

            // getting JSON string from URL
            JSONArray json_projects = jParser_Projects.makeHttpRequest("http://"+SaveSharedPreference.getUserIP(getActivity())+AppConfig.url_Projects_Data_List, "GET",params_projects,"EVALUATION");
            JSONArray json_queues = jParser_Queue.makeHttpRequest("http://"+SaveSharedPreference.getUserIP(getActivity())+AppConfig.url_Queues_Data_List, "GET",params_queue,"EVALUATION");
            JSONArray json_agents = jParser_Agents.makeHttpRequest("http://"+SaveSharedPreference.getUserIP(getActivity())+AppConfig.url_Agents_Data_List, "GET",params_agents,"EVALUATION");
            //JSONArray json_xcxc = xcxc.makeHttpRequest("http://197.45.55.244/Android/Incoming_Queue/Incoming_Agent_Hours.php", "POST",xxx,"EVALUATION");
            //JSONArray json_xcxc = xcxc.makeHttpRequest("http://197.45.55.244/Android/Incoming_Queue/Incoming_Agent.php", "POST",xxx,"EVALUATION");
            //JSONArray json_xcxc2 = xcxc2.makeHttpRequest("http://197.45.55.244/Android/Incoming_Queue/Outgoing_Agent.php", "POST",xxx,"EVALUATION");
            //JSONArray json_xcxc = xcxc.makeHttpRequest("http://192.168.1.150/Android/Incoming_Queue/Outgoing_Agent_Hours.php", "POST",xxx,"EVALUATION");
            //JSONArray json_xcxc = xcxc.makeHttpRequest("http://197.45.55.244/Android/Incoming_Queue/Incoming_Queue.php", "POST",xxx,"EVALUATION");
            //JSONArray json_xcxc2 = xcxc2.makeHttpRequest("http://197.45.55.244/Android/Incoming_Queue/Outgoing_Queue.php", "POST",xxx,"EVALUATION");
            //JSONArray json_xcxc = xcxc.makeHttpRequest("http://197.45.55.244/Android/Incoming_Queue/Outgoing_Queue_Hours.php", "POST",xxx,"EVALUATION");
            //JSONArray json_xcxc2 = xcxc.makeHttpRequest("http://197.45.55.244/Android/Incoming_Queue/Incoming_Queue_Hours.php", "POST",xxx,"EVALUATION");
            //JSONArray json_xcxc = xcxc.makeHttpRequest("http://197.45.55.244/Android/Team/Industries.php", "POST",xxx,"EVALUATION");
            JSONArray json_xcxc2 = xcxc2.makeHttpRequest("http://197.45.55.244/Android/Team/Leads_Industry.php", "POST",xxx,"EVALUATION");
            Log.d("Projects list: ", json_projects.toString());
            System.out.println("Projects list: "+ json_projects.toString());
            Log.d("Queues List: ", json_queues.toString());
            System.out.println("Queues List: "+ json_queues.toString());
            Log.d("Agent List: ", json_agents.toString());
            System.out.println("Agent List: "+ json_agents.toString());
//            Log.d("All Data: xcxc ", json_xcxc.toString());
            Log.d("All Data: xcxc2 ", json_xcxc2.toString());
            System.out.println("All Data: xcxc2 "+ json_xcxc2.toString());

            JSONObject c = json_projects.getJSONObject(0);
            int max1 = Integer.parseInt(c.getString("no_row"));
            for (int i = 1; i <= max1; i++){
                c = json_projects.getJSONObject(i);
                String Project_Name = c.getString("Project_Name");
                String Project_id = c.getString("Project_id");

                Project_L.add(Project_Name);
                Project_L_ID.add(Project_id);

            }

            JSONObject cc = json_queues.getJSONObject(0);
            int max2 = Integer.parseInt(cc.getString("no_row"));
            for (int i = 1 ; i<= max2;i++) {
                cc = json_queues.getJSONObject(i);
                String Queue_Name = cc.getString("Queue_Name");
                String Queue_id = cc.getString("Queue_id");

                Queue_L.add(Queue_Name);
                Queue_L_ID.add(Queue_id);
            }

            JSONObject ccc = json_agents.getJSONObject(0);
            int max3 = Integer.parseInt(ccc.getString("no_row"));
            for (int i = 1 ; i<= max3;i++){
                ccc = json_agents.getJSONObject(i);
                String Agent_Name = ccc.getString("Agent_Name");
                String Agent_id = ccc.getString("Agent_id");

                SelectAgent_L.add(Agent_Name);
                SelectAgent_L_ID.add(Agent_id);
            }

        }catch (ExceptionInInitializerError e){
            Log.d("Error -_- :::" ,e.toString());
            System.out.println("Error -_- :::" +e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            mQueue.setAdapter(adapterQueue);
            mProject.setAdapter(adapterProject);
            if (isSelectQueues){
                mSelectAgent.setAdapter(adapterQueue);
            }else {
                mSelectAgent.setAdapter(adapterSelectAgent);
            }

            mQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterQueue = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Queue_L);
                    mQueue.setAdapter(adapterQueue);
                    mQueue.setText("");
                    if (mRoleCheck) {
                        mQueue.dismissDropDown();
                        mRoleCheck = false;
                    } else {
                        mQueue.showDropDown();
                        mRoleCheck = true;

                    }
                }
            });


            mQueue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int potion, long id) {
                    params_agents.set(1,new BasicNameValuePair("queueID",Queue_L_ID.get(potion)));
                    new Connection().execute();

                }
            });

            mProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterProject = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Project_L);
                    mProject.setAdapter(adapterProject);
                    mProject.setText("");
                    if (mProjectCheck) {
                        mProject.dismissDropDown();
                        mProjectCheck=false;
                    } else {
                        mProject.showDropDown();
                        mProjectCheck=true;

                    }
                }
            });

            mProject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int potion, long id) {
                   // params_queue.clear();
                    //params_agents.clear();
                    params_queue.set(0,new BasicNameValuePair("projID",Project_L_ID.get(potion)));
                    params_agents.set(0,new BasicNameValuePair("projID",Project_L_ID.get(potion)));
                    new Connection().execute();
                }
            });


/***************************************************************************************************/
            /** Select --> (Agent) AND (Queue) Option*/

            mSelectAgent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSelectAgent.setText("");
                    if(isSelectQueues){
                        adapterQueue = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Queue_L);
                        mSelectAgent.setAdapter(adapterQueue);
                    }else {
                      adapterSelectAgent = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, SelectAgent_L);
                      mSelectAgent.setAdapter(adapterSelectAgent);
                    }

                    if (mEmployeeCheck) {

                        mSelectAgent.dismissDropDown();
                        mEmployeeCheck=false;
                    } else {
                        mSelectAgent.showDropDown();
                        mEmployeeCheck=true;

                    }
                }
            });

            mSelectAgent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String email = adapterView.getItemAtPosition(i).toString();
                    String idd = null;
                    if(isSelectQueues){
                        idd = Queue_L_ID.get(i);
                    }else {
                        idd = SelectAgent_L_ID.get(i);
                    }
                    Uri imgUrl = Math.random() > .7d ? null : Uri.parse("https://robohash.org/" + Math.abs(email.hashCode()));
                    Contact contact = new Contact(idd, null, null, email, imgUrl);
                    mChipsView_SelectAgent.addChip(email, imgUrl, contact);
                    mSelectAgent.setText("");
                    if (isSelectQueues){
                        Queue_L.remove(i);
                        Queue_L_ID.remove(i);
                    }else {
                        SelectAgent_L.remove(i);
                        SelectAgent_L_ID.remove(i);
                    }
                    adapterSelectAgent.notifyDataSetChanged();
                    adapterQueue.notifyDataSetChanged();


                }
            });
            mChipsView_SelectAgent.setChipsListener(new ChipsView.ChipsListener() {
                @Override
                public void onChipAdded(ChipsView.Chip chip) {

                }

                @Override
                public void onChipDeleted(ChipsView.Chip chip) {

                    String add = chip.getContact().getEmailAddress();
                    String id = chip.getContact().getFirstName();
                    if (isSelectQueues){
                        Queue_L.add(add);
                        Queue_L_ID.add(id);
                    }else {
                        SelectAgent_L.add(add);
                        SelectAgent_L_ID.add(id);
                    }
                }

                @Override
                public void onTextChanged(CharSequence text) {

                }
            });


            search_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            search_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(FromDate.getText().toString().equals("")||ToDate.getText().toString().equals(""))
                        Toast.makeText(getActivity(),"Enter the Date you want to filter",Toast.LENGTH_SHORT).show();
                    else if(mChipsView_SelectAgent.getChips().isEmpty())
                        Toast.makeText(getActivity(),"Select Agents",Toast.LENGTH_SHORT).show();
                    else {

                        String[] ids = new String[mChipsView_SelectAgent.getChips().size()+2];
                        String[] names = new String[mChipsView_SelectAgent.getChips().size()];
                        //params_search.add(new BasicNameValuePair("FromDate",FromDate.getText().toString()));
                        //params_search.add(new BasicNameValuePair("ToDate",ToDate.getText().toString()));

                        ids[0] = FromDate.getText().toString();
                        ids[1] = ToDate.getText().toString();
                        for (int i= 2 ; i<mChipsView_SelectAgent.getChips().size()+2;i++){
                            ids[i] = mChipsView_SelectAgent.getChips().get(i-2).getContact().getFirstName();
                        }
                        for (int i= 0 ; i<mChipsView_SelectAgent.getChips().size();i++){
                            names[i] = mChipsView_SelectAgent.getChips().get(i).getContact().getEmailAddress();
                        }

                        ViewPager viewPager_TAB3 = (ViewPager) getActivity().findViewById(R.id.tab3pager);
                        FilterChartsAdapter filterChartsAdapter = new FilterChartsAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_1",ids,names,getActivity());
                        viewPager_TAB3.setAdapter(filterChartsAdapter);
                        //ViewPagerAdapter adapter_1 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_1",getActivity());
                        //viewPager_TAB3.setAdapter(adapter_1);

                        space_2.setVisibility(View.GONE);
                        fromDate_layout.setVisibility(View.GONE);
                        toDate_layout.setVisibility(View.GONE);
                        queue_layout.setVisibility(View.GONE);
                        SelectAgent_layout.setVisibility(View.GONE);
                        project_layout.setVisibility(View.GONE);
                        search_bt.setVisibility(View.GONE);
                        params.addRule(RelativeLayout.ALIGN_BOTTOM,0);
                        textView_Title.setText("Click To Filter Data");


                    }
                }
            });
        }
    }
}

