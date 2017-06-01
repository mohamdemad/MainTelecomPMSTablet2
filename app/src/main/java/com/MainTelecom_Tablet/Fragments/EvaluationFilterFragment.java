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
 * Created by MOHAMED on 09/04/2016.
 */
public class EvaluationFilterFragment extends Fragment {


    CharSequence Titles[] = {"Working\nHours"};
    ProgressDialog pDialog;
    private int Year ,Month , Day;
    private EditText FromDate, ToDate;
    private TextView textView_Title , textView_FromDate ,textView_toDate ,textView_Role ,textView_Project;
    //private TextView textView_NoData ;
    private TextView textView_Phase , textView_Employeee , textView_Customer;
    private AutoCompleteTextView mRole , mProject , mPhase,mEmployee ,mCustomer;
    private Button search_bt;
    private Space space_2,space_1;
    private RelativeLayout fromDate_layout ,toDate_layout , role_layout,customer_layout,project_layout,employee_layout,phase_layout;

    static final int FROM_DATE_PICKER_ID = 0;
    static final int TO_DATE_PICKER_ID = 1;

    private ArrayAdapter<String> adapterRole;
    private ArrayAdapter<String> adapterProject;
    private ArrayAdapter<String> adapterPhase;
    private ArrayAdapter<String> adapterEmployee;
    private ArrayAdapter<String> adapterCustomer;
    private ArrayAdapter<String> adapterIndustry;

    private ArrayList<String> Role_L = new ArrayList<String>() ;
    private ArrayList<String> Employee_L = new ArrayList<String>();
    private ArrayList<String> Customer_L = new ArrayList<String>();
    private ArrayList<String> Phase_L = new ArrayList<String>() ;
    private ArrayList<String> Project_L = new ArrayList<String>() ;
    private ArrayList<String> Industry_L = new ArrayList<String>() ;

    private ArrayList<String> Role_L_ID = new ArrayList<String>() ;
    private ArrayList<String> Employee_L_ID = new ArrayList<String>();
    private ArrayList<String> Customer_L_ID = new ArrayList<String>();
    private ArrayList<String> Phase_L_ID = new ArrayList<String>() ;
    private ArrayList<String> Project_L_ID = new ArrayList<String>() ;
    private ArrayList<String> Industry_L_ID = new ArrayList<String>() ;

    private JSONParser jParser_Roles = new JSONParser();
    private JSONParser jParser_Employees = new JSONParser();
    private JSONParser jParser_Customers = new JSONParser();
    private JSONParser jParser_Phase = new JSONParser();
    private JSONParser jParser_Projects = new JSONParser();
    private JSONParser jParser_Industry = new JSONParser();

    List<NameValuePair> params_Roles = new ArrayList<NameValuePair>();
    List<NameValuePair> params_Employee = new ArrayList<NameValuePair>();
    List<NameValuePair> params_Customer = new ArrayList<NameValuePair>();
    List<NameValuePair> params_Phase = new ArrayList<NameValuePair>();
    List<NameValuePair> params_projects = new ArrayList<NameValuePair>();
    List<NameValuePair> params_Industry = new ArrayList<NameValuePair>();

    private Boolean mRoleCheck = false;
    private Boolean mProjectCheck = false;
    private Boolean mPhaseCheck = false;
    private Boolean mEmployeeCheck = false;
    private Boolean mCustomerCheck = false;

    private Boolean isLead_Industry = false;
    private Boolean isDepartment = false;
    private Boolean isEvaluation = false;
    private Boolean isWorkHours = false;
    private Boolean isOpen_Closed_Ticket = false;

    private ChipsView mChipsView_Employee , mChipsView_Customer;
    private ViewPager viewPager_filter;
    private  RelativeLayout.LayoutParams params;
    String RoleID ="";
    String IndustryID ="";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getActivity(), SaveSharedPreference.getTab3Check(getActivity()),Toast.LENGTH_SHORT).show();

        View Eva_filter = inflater.inflate(R.layout.filter_team_layout, container, false);

        adapterRole = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Role_L);
        adapterProject = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Project_L);
        adapterPhase = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Phase_L);
        adapterEmployee = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Employee_L);
        adapterCustomer = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Customer_L);
        adapterIndustry = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Industry_L);

        params_Employee.add(new BasicNameValuePair("",""));

        if (SaveSharedPreference.getTab3Check(getActivity()).equals("Department Activities")||
                SaveSharedPreference.getTab3Check(getActivity()).equals("Department Effort"))
        {
            isDepartment = true;
        }else if(SaveSharedPreference.getTab3Check(getActivity()).equals("Time Evaluation")||
                SaveSharedPreference.getTab3Check(getActivity()).equals("Activities Evaluation"))
        {
            isEvaluation = true;
        }else if(SaveSharedPreference.getTab3Check(getActivity()).equals("Working Hours")){
            isWorkHours =true;
        }else if(SaveSharedPreference.getTab3Check(getActivity()).equals("Leads/Industry")){
            isLead_Industry = true;
        }else if(SaveSharedPreference.getTab3Check(getActivity()).equals("Open/Closed Tickets")){
            isOpen_Closed_Ticket = true;
        }


        new Connection().execute();

        FromDate = (EditText)Eva_filter.findViewById(R.id.Eva_FromDate_EditTxt);
        ToDate = (EditText)Eva_filter.findViewById(R.id.Eva_ToDate_EditTxt);
        mEmployee = (AutoCompleteTextView) Eva_filter.findViewById(R.id.Eva_Employee_AutoFill);
        mCustomer = (AutoCompleteTextView) Eva_filter.findViewById(R.id.Eva_Customer_AutoFill);
        mRole = (AutoCompleteTextView) Eva_filter.findViewById(R.id.Eva_Role_AutoFill);
        mProject = (AutoCompleteTextView) Eva_filter.findViewById(R.id.Eva_Project_AutoFill);
        mPhase = (AutoCompleteTextView) Eva_filter.findViewById(R.id.Eva_Phase_AutoFill);

      //  textView_NoData = (TextView) Eva_filter.findViewById(R.id.textView17);
        textView_FromDate = (TextView) Eva_filter.findViewById(R.id.textView5);
        textView_toDate = (TextView) Eva_filter.findViewById(R.id.textView6);
        textView_Employeee = (TextView) Eva_filter.findViewById(R.id.textView8);
        textView_Customer = (TextView) Eva_filter.findViewById(R.id.textView9);
        textView_Project = (TextView) Eva_filter.findViewById(R.id.textView10);
        textView_Phase = (TextView) Eva_filter.findViewById(R.id.textView11);
        textView_Role = (TextView) Eva_filter.findViewById(R.id.textView7);
        textView_Title = (TextView) Eva_filter.findViewById(R.id.textView18);
        search_bt = (Button) Eva_filter.findViewById(R.id.button);
        space_2 = (Space) Eva_filter.findViewById(R.id.space);
//        space_1 = (Space) Eva_filter.findViewById(R.id.space2);

        fromDate_layout = (RelativeLayout)Eva_filter.findViewById(R.id.fromDate_layout);
        toDate_layout = (RelativeLayout)Eva_filter.findViewById(R.id.toDate_layout);
        role_layout = (RelativeLayout)Eva_filter.findViewById(R.id.role_layout);
        customer_layout = (RelativeLayout)Eva_filter.findViewById(R.id.customer_layout);
        project_layout = (RelativeLayout)Eva_filter.findViewById(R.id.project_layout);
        employee_layout = (RelativeLayout)Eva_filter.findViewById(R.id.employee_layout);
        phase_layout = (RelativeLayout)Eva_filter.findViewById(R.id.phase_layout);
        //textView_Title.setText(SaveSharedPreference.getTab3Check(getActivity()));

        mChipsView_Employee = (ChipsView) Eva_filter.findViewById(R.id.Eva_Employee_Contant);
        mChipsView_Customer = (ChipsView) Eva_filter.findViewById(R.id.Eva_Customer_Contant);

        viewPager_filter =(ViewPager)getActivity().findViewById(R.id.fillter_pager);
        if (SaveSharedPreference.getTab3Check(getActivity()).equalsIgnoreCase("Team WFM Home"))
            viewPager_filter.setVisibility(View.GONE);
        params =(RelativeLayout.LayoutParams)viewPager_filter.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_BOTTOM,0);


        textView_Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(textView_Title.getText().equals("Click To Filter Data")){

                    params.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.space8);
                    viewPager_filter.setLayoutParams(params);
                    textView_Title.setText("click to move");

                if (SaveSharedPreference.getTab3Check(getActivity()).equals("Department Activities")||
                        SaveSharedPreference.getTab3Check(getActivity()).equals("Department Effort"))
                {
                    space_2.setVisibility(View.VISIBLE);
                    fromDate_layout.setVisibility(View.VISIBLE);
                    toDate_layout.setVisibility(View.VISIBLE);
                    role_layout.setVisibility(View.VISIBLE);
                    search_bt.setVisibility(View.VISIBLE);
                    isDepartment = true;
//                    textView_NoData.setVisibility(View.GONE);
//                    textView_Employeee.setVisibility(View.GONE);
//                    mEmployee.setVisibility(View.GONE);
//                    textView_Customer.setVisibility(View.GONE);
//                    mCustomer.setVisibility(View.GONE);
//                    textView_Project.setVisibility(View.GONE);
//                    mProject.setVisibility(View.GONE);
//                    textView_Phase.setVisibility(View.GONE);
//                    mPhase.setVisibility(View.GONE);
//                    mChipsView_Customer.setVisibility(View.GONE);
//                    mChipsView_Employee.setVisibility(View.GONE);
//                    space_1.setVisibility(View.VISIBLE);
                    /***********************************************/
//                    space_2.setVisibility(View.VISIBLE);
//                    textView_FromDate.setVisibility(View.VISIBLE);
//                    FromDate.setVisibility(View.VISIBLE);
//                    textView_toDate.setVisibility(View.VISIBLE);
//                    ToDate.setVisibility(View.VISIBLE);
//                    textView_Role.setVisibility(View.VISIBLE);
//                    mRole.setVisibility(View.VISIBLE);
//                    search_bt.setVisibility(View.VISIBLE);

                }else if(SaveSharedPreference.getTab3Check(getActivity()).equals("Time Evaluation")||
                        SaveSharedPreference.getTab3Check(getActivity()).equals("Activities Evaluation"))
                {
                    space_2.setVisibility(View.VISIBLE);
                    fromDate_layout.setVisibility(View.VISIBLE);
                    toDate_layout.setVisibility(View.VISIBLE);
                    role_layout.setVisibility(View.VISIBLE);
                    employee_layout.setVisibility(View.VISIBLE);
                    customer_layout.setVisibility(View.VISIBLE);
                    project_layout.setVisibility(View.VISIBLE);
                    phase_layout.setVisibility(View.VISIBLE);
                    search_bt.setVisibility(View.VISIBLE);
                    isEvaluation = true;
//                    space_1.setVisibility(View.VISIBLE);
//                    space_2.setVisibility(View.VISIBLE);
//                    textView_Title.setVisibility(View.VISIBLE);
//                    textView_FromDate.setVisibility(View.VISIBLE);
//                    FromDate.setVisibility(View.VISIBLE);
//                    textView_toDate.setVisibility(View.VISIBLE);
//                    ToDate.setVisibility(View.VISIBLE);
//                    textView_Role.setVisibility(View.VISIBLE);
//                    mRole.setVisibility(View.VISIBLE);
//                    textView_Employeee.setVisibility(View.VISIBLE);
//                    mEmployee.setVisibility(View.VISIBLE);
//                    textView_Customer.setVisibility(View.VISIBLE);
//                    mCustomer.setVisibility(View.VISIBLE);
//                    textView_Project.setVisibility(View.VISIBLE);
//                    mProject.setVisibility(View.VISIBLE);
//                    textView_Phase.setVisibility(View.VISIBLE);
//                    mPhase.setVisibility(View.VISIBLE);
//                    mChipsView_Customer.setVisibility(View.VISIBLE);
//                    mChipsView_Employee.setVisibility(View.VISIBLE);
//                    search_bt.setVisibility(View.VISIBLE);
                    /**********************************************************/
                    //  textView_NoData.setVisibility(View.VISIBLE);
//                    textView_Title.setVisibility(View.GONE);
//                    textView_FromDate.setVisibility(View.GONE);
//                    FromDate.setVisibility(View.GONE);
//                    textView_toDate.setVisibility(View.GONE);
//                    ToDate.setVisibility(View.GONE);
//                    textView_Role.setVisibility(View.GONE);
//                    mRole.setVisibility(View.GONE);
//                    textView_Employeee.setVisibility(View.GONE);
//                    mEmployee.setVisibility(View.GONE);
//                    textView_Customer.setVisibility(View.GONE);
//                    mCustomer.setVisibility(View.GONE);
//                    textView_Project.setVisibility(View.GONE);
//                    mProject.setVisibility(View.GONE);
//                    textView_Phase.setVisibility(View.GONE);
//                    mPhase.setVisibility(View.GONE);
//                    mChipsView_Customer.setVisibility(View.GONE);
//                    mChipsView_Employee.setVisibility(View.GONE);
//                    search_bt.setVisibility(View.GONE);
                }else if(SaveSharedPreference.getTab3Check(getActivity()).equals("Working Hours")){

                    space_2.setVisibility(View.VISIBLE);
                    fromDate_layout.setVisibility(View.VISIBLE);
                    toDate_layout.setVisibility(View.VISIBLE);
                    role_layout.setVisibility(View.VISIBLE);
                    employee_layout.setVisibility(View.VISIBLE);
                    search_bt.setVisibility(View.VISIBLE);
                    isWorkHours =true;
//                    space_1.setVisibility(View.VISIBLE);
//                    space_2.setVisibility(View.VISIBLE);
//                    textView_FromDate.setVisibility(View.VISIBLE);
//                    FromDate.setVisibility(View.VISIBLE);
//                    textView_toDate.setVisibility(View.VISIBLE);
//                    ToDate.setVisibility(View.VISIBLE);
//                    textView_Role.setVisibility(View.VISIBLE);
//                    mRole.setVisibility(View.VISIBLE);
//                    textView_Employeee.setVisibility(View.VISIBLE);
//                    mEmployee.setVisibility(View.VISIBLE);
//                    search_bt.setVisibility(View.VISIBLE);
//                    mChipsView_Employee.setVisibility(View.VISIBLE);

                }else if(SaveSharedPreference.getTab3Check(getActivity()).equals("Leads/Industry")){

                    space_2.setVisibility(View.VISIBLE);
                    fromDate_layout.setVisibility(View.VISIBLE);
                    toDate_layout.setVisibility(View.VISIBLE);
                    textView_Customer.setText("Industry");
                    customer_layout.setVisibility(View.VISIBLE);
                    search_bt.setVisibility(View.VISIBLE);
                    isLead_Industry = true;

//                    space_1.setVisibility(View.VISIBLE);
//                    space_2.setVisibility(View.VISIBLE);
//                    textView_FromDate.setVisibility(View.VISIBLE);
//                    FromDate.setVisibility(View.VISIBLE);
//                    textView_toDate.setVisibility(View.VISIBLE);
//                    ToDate.setVisibility(View.VISIBLE);
//                    textView_Role.setText("Industry");
//                    textView_Role.setVisibility(View.VISIBLE);
//                    mRole.setVisibility(View.VISIBLE);
//                    search_bt.setVisibility(View.VISIBLE);

                }else if(SaveSharedPreference.getTab3Check(getActivity()).equals("Open/Closed Tickets")){

                    space_2.setVisibility(View.VISIBLE);
                    fromDate_layout.setVisibility(View.VISIBLE);
                    toDate_layout.setVisibility(View.VISIBLE);
                    role_layout.setVisibility(View.VISIBLE);
                    employee_layout.setVisibility(View.VISIBLE);
                    customer_layout.setVisibility(View.VISIBLE);
                    search_bt.setVisibility(View.VISIBLE);
                    isOpen_Closed_Ticket = true;


                }
                }else {
//                    space_1.setVisibility(View.GONE);
                    space_2.setVisibility(View.GONE);
                    fromDate_layout.setVisibility(View.GONE);
                    toDate_layout.setVisibility(View.GONE);
                    role_layout.setVisibility(View.GONE);
                    employee_layout.setVisibility(View.GONE);
                    customer_layout.setVisibility(View.GONE);
                    project_layout.setVisibility(View.GONE);
                    phase_layout.setVisibility(View.GONE);
                    search_bt.setVisibility(View.GONE);
                    //textView_Title.setVisibility(View.GONE);
//                    textView_FromDate.setVisibility(View.GONE);
//                    FromDate.setVisibility(View.GONE);
//                    textView_toDate.setVisibility(View.GONE);
//                    ToDate.setVisibility(View.GONE);
//                    textView_Role.setVisibility(View.GONE);
//                    mRole.setVisibility(View.GONE);
//                    textView_Employeee.setVisibility(View.GONE);
//                    mEmployee.setVisibility(View.GONE);
//                    textView_Customer.setVisibility(View.GONE);
//                    mCustomer.setVisibility(View.GONE);
//                    textView_Project.setVisibility(View.GONE);
//                    mProject.setVisibility(View.GONE);
//                    textView_Phase.setVisibility(View.GONE);
//                    mPhase.setVisibility(View.GONE);
//                    mChipsView_Customer.setVisibility(View.GONE);
//                    mChipsView_Employee.setVisibility(View.GONE);
//                    search_bt.setVisibility(View.GONE);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM,0);
                    textView_Title.setText("Click To Filter Data");
                }

            }
        });


        // change EditText config
        mChipsView_Employee.getEditText().setCursorVisible(false);
        mChipsView_Employee.setFocusable(false);
        mChipsView_Employee.setFocusableInTouchMode(false);
        mChipsView_Employee.getEditText().setFocusable(false);
        mChipsView_Employee.getEditText().setFocusableInTouchMode(false);

        mChipsView_Customer.getEditText().setCursorVisible(false);
        mChipsView_Customer.setFocusable(false);
        mChipsView_Customer.setFocusableInTouchMode(false);
        mChipsView_Customer.getEditText().setFocusable(false);
        mChipsView_Customer.getEditText().setFocusableInTouchMode(false);


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

//        Role_L =new ArrayList<String>(Arrays.asList(roleList));
//        Project_L =new ArrayList<String>(Arrays.asList(projectList));
//        Employee_L =new ArrayList<String>(Arrays.asList(employeeList));
//        Phase_L =new ArrayList<String>(Arrays.asList(phaseList));
//        Customer_L =new ArrayList<String>(Arrays.asList(customerList));
//        Industry_L =new ArrayList<String>(Arrays.asList(industryList));


        return Eva_filter;
    }

    /** when dialog box is closed, below method will be called.*/

    private DatePickerDialog.OnDateSetListener frompickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

//            Year  = selectedYear;
//            Month = selectedMonth;
//            Day   = selectedDay;

            FromDate.setText(new StringBuilder().append(selectedYear)
                    .append("/").append(selectedMonth + 1).append("/").append(selectedDay));
        }
    };
    private DatePickerDialog.OnDateSetListener topickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

//            Year  = selectedYear;
//            Month = selectedMonth;
//            Day   = selectedDay;

            ToDate.setText(new StringBuilder().append(selectedYear)
                    .append("/").append(selectedMonth + 1).append("/").append(selectedDay));
        }
    };


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

            Role_L.clear();
            Role_L_ID.clear();
            Employee_L.clear();
            Employee_L_ID.clear();
            Customer_L.clear();
            Customer_L_ID.clear();
            Phase_L.clear();
            Phase_L_ID.clear();
            Project_L.clear();
            Project_L_ID.clear();
            Industry_L.clear();
            Industry_L_ID.clear();

            try {

                //List<NameValuePair> xxx = new ArrayList<NameValuePair>();
                //params_queue.add(new BasicNameValuePair("projID","43"));
                //params_agents.add(new BasicNameValuePair("projID","43"));
                //params_agents.add(new BasicNameValuePair("queueID","13"));
                //xxx.add(new BasicNameValuePair("FromDate","2015-6-1"));
                //xxx.add(new BasicNameValuePair("ToDate","2016-6-15"));
                //xxx.add(new BasicNameValuePair("Select1[]","16"));
                //xxx.add(new BasicNameValuePair("Select1[]","49599"));
                //xxx.add(new BasicNameValuePair("Select1[]","16"));

                /**Load Roles List Data */
            if(isDepartment||isEvaluation||isWorkHours||isOpen_Closed_Ticket) {

                JSONArray json_Roles = jParser_Roles.makeHttpRequest("http://"+SaveSharedPreference.getUserIP(getActivity())+ AppConfig.url_roles, "GET",params_Roles,"EVALUATION");
                Log.d("All Role list: ", json_Roles.toString());
                System.out.println("All Role list: "+ json_Roles.toString());

                JSONObject c = json_Roles.getJSONObject(0);
                int max1 = Integer.parseInt(c.getString("no_rows"));
                for (int i = 1; i <= max1; i++) {
                    c = json_Roles.getJSONObject(i);
                    String Role_Name = c.getString("Role_Name");
                    String Role_Id = c.getString("Role_Id");

                    Role_L.add(Role_Name);
                    Role_L_ID.add(Role_Id);

                }
            }

                /**Load Employee List Data */
            if(isEvaluation||isOpen_Closed_Ticket||isWorkHours) {

                JSONArray json_Employee = jParser_Employees.makeHttpRequest("http://"+SaveSharedPreference.getUserIP(getActivity())+ AppConfig.url_Employee_Data_List, "GET",params_Employee,"EVALUATION");
                Log.d("All Employee list: ", json_Employee.toString());
                System.out.println("All Employee list: "+ json_Employee.toString());

                JSONObject cc = json_Employee.getJSONObject(0);
                int max2 = Integer.parseInt(cc.getString("no_rows"));
                for (int i = 1; i <= max2; i++) {
                    cc = json_Employee.getJSONObject(i);
                    String Employee_Name = cc.getString("Emp_Name");
                    String Employee_id = cc.getString("Emp_ID");

                    Employee_L.add(Employee_Name);
                    Employee_L_ID.add(Employee_id);

                }
            }

                /**Load Customer List Data */
            if(isEvaluation||isOpen_Closed_Ticket) {

//                JSONArray json_Customer = jParser_Customers.makeHttpRequest("http://"+SaveSharedPreference.getUserIP(getActivity())+ AppConfig, "GET",params_Customer,"EVALUATION");
//                Log.d("All Customer list: ", json_Customer.toString());
//                System.out.println("All Customer list: "+ json_Customer.toString());

//                JSONObject ccc = json_Customer.getJSONObject(0);
//                int max3 = Integer.parseInt(ccc.getString("no_row"));
//                for (int i = 1; i <= max3; i++){
//                    ccc= json_Customer.getJSONObject(i);
//                    String Customer_Name = ccc.getString("Project_Name");
//                    String Customer_id = ccc.getString("Project_id");
//
//                    Customer_L.add(Customer_Name);
//                    Customer_L_ID.add(Customer_id);
//
//                }
            }

                /**Load Projects List Data */
            if(isEvaluation) {
                JSONArray json_projects = jParser_Projects.makeHttpRequest("http://"+SaveSharedPreference.getUserIP(getActivity())+ AppConfig.url_ProjectsTeam_Data_List, "GET",params_projects,"EVALUATION");
                Log.d("All Project list: ", json_projects.toString());
                System.out.println("All Project list: "+ json_projects.toString());

                JSONObject cccc = json_projects.getJSONObject(0);
                int max4 = Integer.parseInt(cccc.getString("no_row"));
                for (int i = 1; i <= max4; i++) {
                    cccc = json_projects.getJSONObject(i);
                    String Project_Name = cccc.getString("Project_Name");
                    String Project_id = cccc.getString("Project_id");

                    Project_L.add(Project_Name);
                    Project_L_ID.add(Project_id);

                }
            }

                /**Load Phase List Data */
            if(isEvaluation) {
//                JSONArray json_Phase = jParser_Phase.makeHttpRequest("http://"+SaveSharedPreference.getUserIP(getActivity())+ AppConfig, "GET",params_Phase,"EVALUATION");
//                Log.d("All Phase list: ", json_Phase.toString());
//                System.out.println("All Phase list: "+ json_Phase.toString());

//                JSONObject c5 = json_Phase.getJSONObject(0);
//                int max5 = Integer.parseInt(c5.getString("no_row"));
//                for (int i = 1; i <= max5; i++){
//                    c5= json_Phase.getJSONObject(i);
//                    String Phase_Name = c5.getString("Project_Name");
//                    String Phase_id = c5.getString("Project_id");
//
//                    Phase_L.add(Phase_Name);
//                    Phase_L_ID.add(Phase_id);
//
//                }
            }

                /**Load Industry List Data */
            if(isLead_Industry) {
                JSONArray json_Industry = jParser_Industry.makeHttpRequest("http://"+SaveSharedPreference.getUserIP(getActivity())+ AppConfig.url_Industry_Data_List, "GET",params_Industry,"EVALUATION");
                Log.d("All Industry list: ", json_Industry.toString());
                System.out.println("All Industry list: "+ json_Industry.toString());

                JSONObject c6 = json_Industry.getJSONObject(0);
                int max6 = Integer.parseInt(c6.getString("no_rows"));
                for (int i = 1; i <= max6; i++) {
                    c6 = json_Industry.getJSONObject(i);
                    String Industry_Name = c6.getString("Industry");
                    //String Industry_id = c6.getString("Project_id");

                    Industry_L.add(Industry_Name);
                    //Industry_L_ID.add(Industry_id);

                }
            }

            }catch (ExceptionInInitializerError e){
                Log.d("Error -_- :::" ,e.toString());
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

            /**adapter for search spinner*/


            mRole.setAdapter(adapterRole);
            mProject.setAdapter(adapterProject);
            mPhase.setAdapter(adapterPhase);
            mEmployee.setAdapter(adapterEmployee);
            if(isLead_Industry) {
                mCustomer.setAdapter(adapterIndustry);
            }else{
                mCustomer.setAdapter(adapterCustomer);
            }

            mRole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        RoleID = "";
                        adapterRole = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Role_L);
                        mRole.setAdapter(adapterRole);

                    mRole.setText("");
                    if (mRoleCheck) {
                        mRole.dismissDropDown();
                        mRoleCheck = false;
                    } else {
                        mRole.showDropDown();
                        mRoleCheck = true;

                    }
                }
            });

            mRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (isDepartment) {
                        RoleID = Role_L_ID.get(i);
                    }
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

            mPhase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterPhase = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Phase_L);
                    mPhase.setAdapter(adapterPhase);
                    mPhase.setText("");
                    if (mPhaseCheck) {
                        mPhase.dismissDropDown();
                        mPhaseCheck=false;
                    } else {
                        mPhase.showDropDown();
                        mPhaseCheck=true;

                    }
                }
            });
/***************************************************************************************************/
            /** Employee Option*/
            mChipsView_Employee.setChipsListener(new ChipsView.ChipsListener() {
                @Override
                public void onChipAdded(ChipsView.Chip chip) {

                }

                @Override
                public void onChipDeleted(ChipsView.Chip chip) {

                    Employee_L.add(chip.getContact().getEmailAddress());
                    Employee_L_ID.add(chip.getContact().getFirstName());
                }

                @Override
                public void onTextChanged(CharSequence text) {

                }
            });


            mEmployee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterEmployee = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Employee_L);
                    mEmployee.setAdapter(adapterEmployee);
                    mEmployee.setText("");

                    if (mEmployeeCheck) {

                        mEmployee.dismissDropDown();
                        mEmployeeCheck=false;
                    } else {
                        mEmployee.showDropDown();
                        mEmployeeCheck=true;

                    }
                }
            });

            mEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String email = adapterView.getItemAtPosition(i).toString();
                    String E_id = Employee_L_ID.get(i);
                    Uri imgUrl = Math.random() > .7d ? null : Uri.parse("https://robohash.org/" + Math.abs(email.hashCode()));
                    Contact contact = new Contact(E_id, null, null, email, imgUrl);
                    mChipsView_Employee.addChip(email, imgUrl, contact);
                    mEmployee.setText("");
                    Employee_L.remove(i);
                    Employee_L_ID.remove(i);
                    adapterEmployee.notifyDataSetChanged();
//                if (selection.isChecked()) {
//                    mChipsView.addChip(email, imgUrl, contact);
//                } else {
//                    mChipsView.removeChipBy(contact);
//                }

                }
            });
/***************************************************************************************************/
            /** Customer Option*/
            mChipsView_Customer.setChipsListener(new ChipsView.ChipsListener() {
                @Override
                public void onChipAdded(ChipsView.Chip chip) {

                }

                @Override
                public void onChipDeleted(ChipsView.Chip chip) {
                    if(isLead_Industry){
                        Industry_L.add(chip.getContact().getEmailAddress());
                    }else {
                        Customer_L.add(chip.getContact().getEmailAddress());
                        Customer_L_ID.add(chip.getContact().getFirstName());
                    }
                }

                @Override
                public void onTextChanged(CharSequence text) {

                }
            });

            mCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isLead_Industry){
                        adapterIndustry = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Industry_L);
                        mCustomer.setAdapter(adapterIndustry);
                    }else {
                        adapterCustomer = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Customer_L);
                        mCustomer.setAdapter(adapterCustomer);
                    }
                    mCustomer.setText("");
                    if (mCustomerCheck) {

                        mCustomer.dismissDropDown();
                        mCustomerCheck = false;
                    } else {
                        mCustomer.showDropDown();
                        mCustomerCheck = true;

                    }
                }
            });

            mCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    String email = adapterView.getItemAtPosition(i).toString();
                    String C_id = null;
                    if(!isLead_Industry){
                        C_id = Customer_L_ID.get(i);
                    }
                    Uri imgUrl = Math.random() > .7d ? null : Uri.parse("https://robohash.org/" + Math.abs(email.hashCode()));
                    Contact contact = new Contact(C_id, null, null, email, imgUrl);
                    mChipsView_Customer.addChip(email, imgUrl, contact);
                    mCustomer.setText("");
                    if (isLead_Industry){
                        Industry_L.remove(i);
                        adapterIndustry.notifyDataSetChanged();
                    }else {
                        Customer_L.remove(i);
                        Customer_L_ID.remove(i);
                        adapterCustomer.notifyDataSetChanged();
                    }

//                if (selection.isChecked()) {
//                    mChipsView.addChip(email, imgUrl, contact);
//                } else {
//                    mChipsView.removeChipBy(contact);
//                }

                }
            });

            /***********************************************************************************************************************************************************************/

            search_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(FromDate.getText().toString().equals("")||ToDate.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Enter the Date you want to filter", Toast.LENGTH_SHORT).show();
                    }else {
                        if(isDepartment){
                            String[] ids = new String[3];
                            String[] names = new String[Role_L.size()];
                            ids[0]= FromDate.getText().toString();
                            ids[1]= ToDate.getText().toString();
                            ids[2] = RoleID;

                            for (int i = 0 ;i<Role_L.size();i++){
                                names[i] = Role_L.get(i);
                            }
                            ViewPager viewPager_TAB3 = (ViewPager) getActivity().findViewById(R.id.tab3pager);
                            FilterChartsAdapter filterChartsAdapter = new FilterChartsAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_1",ids,names,getActivity());
                            viewPager_TAB3.setAdapter(filterChartsAdapter);
                            RoleID = "";
                        }else if(isLead_Industry){
                            if(mChipsView_Customer.getChips().isEmpty()){
                                String[] ids = new String[3];
                                String[] names = new String[Industry_L.size()];
                                ids[0]= FromDate.getText().toString();
                                ids[1]= ToDate.getText().toString();
                                ids[2]= "";

                                for (int i = 0 ;i<Industry_L.size();i++){
                                    names[i] = Industry_L.get(i);
                                }
                                ViewPager viewPager_TAB3 = (ViewPager) getActivity().findViewById(R.id.tab3pager);
                                FilterChartsAdapter filterChartsAdapter = new FilterChartsAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_1",ids,names,getActivity());
                                viewPager_TAB3.setAdapter(filterChartsAdapter);

                            }else {
                                String[] ids = new String[mChipsView_Customer.getChips().size() + 2];
                                String[] names = new String[Industry_L.size()];
                                ids[0] = FromDate.getText().toString();
                                ids[1] = ToDate.getText().toString();
                                for (int i = 2; i < mChipsView_Customer.getChips().size() + 2; i++) {
                                    ids[i] = mChipsView_Customer.getChips().get(i - 2).getContact().getEmailAddress();
                                }

                                for (int i = 0; i < Industry_L.size(); i++) {
                                    names[i] = Industry_L.get(i);
                                }
                                ViewPager viewPager_TAB3 = (ViewPager) getActivity().findViewById(R.id.tab3pager);
                                FilterChartsAdapter filterChartsAdapter = new FilterChartsAdapter(getActivity().getSupportFragmentManager(), Titles, 1, "part_1", ids, names, getActivity());
                                viewPager_TAB3.setAdapter(filterChartsAdapter);
                            }
                        }
                    }

//                        String[] ids = new String[mChipsView_SelectAgent.getChips().size()+2];
//                        String[] names = new String[mChipsView_SelectAgent.getChips().size()];
//                        //params_search.add(new BasicNameValuePair("FromDate",FromDate.getText().toString()));
//                        //params_search.add(new BasicNameValuePair("ToDate",ToDate.getText().toString()));
//
//                        ids[0] = FromDate.getText().toString();
//                        ids[1] = ToDate.getText().toString();
//                        for (int i= 2 ; i<mChipsView_SelectAgent.getChips().size()+2;i++){
//                            ids[i] = mChipsView_SelectAgent.getChips().get(i-2).getContact().getFirstName();
//                        }
//                        for (int i= 0 ; i<mChipsView_SelectAgent.getChips().size();i++){
//                            names[i] = mChipsView_SelectAgent.getChips().get(i).getContact().getEmailAddress();
//                        }
//
//                        ViewPager viewPager_TAB3 = (ViewPager) getActivity().findViewById(R.id.tab3pager);
//                        FilterChartsAdapter filterChartsAdapter = new FilterChartsAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_1",ids,names,getActivity());
//                        viewPager_TAB3.setAdapter(filterChartsAdapter);
                        ///ViewPagerAdapter adapter_1 = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),Titles,1,"part_1",getActivity());
                        /////viewPager_TAB3.setAdapter(adapter_1);

                        space_2.setVisibility(View.GONE);
                        fromDate_layout.setVisibility(View.GONE);
                        toDate_layout.setVisibility(View.GONE);
                        role_layout.setVisibility(View.GONE);
                        employee_layout.setVisibility(View.GONE);
                        customer_layout.setVisibility(View.GONE);
                        project_layout.setVisibility(View.GONE);
                        phase_layout.setVisibility(View.GONE);
                        search_bt.setVisibility(View.GONE);
                        params.addRule(RelativeLayout.ALIGN_BOTTOM,0);
                        textView_Title.setText("Click To Filter Data");


//                    }
                }
            });
        }
    }
}