package com.MainTelecom_Tablet.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.MainTelecom_Tablet.Adapter.MyHomeChartsAdapter;
import com.MainTelecom_Tablet.R;
import com.MainTelecom_Tablet.SharedPreference.MyHomeChartsSharedPreference;

import java.util.ArrayList;



/**
 * Created by MOHAMED on 17/04/2016.
 */
public class MyHomeChartsFragment extends Fragment  {


    private ViewPager Pager_1,Pager_2,Pager_3,Pager_4,Pager_5,Pager_6,Pager_7;
    private MyHomeChartsAdapter AdapterPager_1 , AdapterPager_2 , AdapterPager_3 , AdapterPager_4 , AdapterPager_5 , AdapterPager_6;
    private MyHomeChartsAdapter AdapterPager_7;

    private ArrayList BarCharts_list = new ArrayList();
    private ArrayList PieCharts_list = new ArrayList();


    private String name_item_selected="";
    private int mPotion =0;
    private String mListName = "";
    private String mLocation = "";
    private Context mContext;
    ListView singel_list ;
    ArrayAdapter adapter;

    int Numboftabs = 1;

    final Handler handler = new Handler();



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View home_charts = inflater.inflate(R.layout.my_home_layout, container, false);

         String Name_Pager_1 = MyHomeChartsSharedPreference.getPagerName(getContext(),"PAGER_1");
        String Name_Pager_2 = MyHomeChartsSharedPreference.getPagerName(getActivity(),"PAGER_2");
         String Name_Pager_3 = MyHomeChartsSharedPreference.getPagerName(getActivity(),"PAGER_3");
         String Name_Pager_4 = MyHomeChartsSharedPreference.getPagerName(getActivity(),"PAGER_4");
         String Name_Pager_5 = MyHomeChartsSharedPreference.getPagerName(getActivity(),"PAGER_5");
         String Name_Pager_6 = MyHomeChartsSharedPreference.getPagerName(getActivity(), "PAGER_6");

        Log.d("Mohamed : ", "GOOOOOOOD : " + Name_Pager_1);
        Log.d("Mohamed : ", "GOOOOOOOD : " + Name_Pager_2);
        Log.d("Mohamed : ", "GOOOOOOOD : " + Name_Pager_3);
        Log.d("Mohamed : ", "GOOOOOOOD : " + Name_Pager_4);
        Log.d("Mohamed : ", "GOOOOOOOD : " + Name_Pager_5);
        Log.d("Mohamed : ", "GOOOOOOOD : " + Name_Pager_6);


//
//        toolbar = (Toolbar) home.findViewById(R.id.app_bar_id);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // for refresh activity when scroll down

        Pager_1 = (ViewPager) home_charts.findViewById(R.id.pager_1);
        Pager_2 = (ViewPager) home_charts.findViewById(R.id.pager_2);
        Pager_3 = (ViewPager) home_charts.findViewById(R.id.pager_3);
        Pager_4 = (ViewPager) home_charts.findViewById(R.id.pager_4);
        Pager_5 = (ViewPager) home_charts.findViewById(R.id.pager_5);
        Pager_6 = (ViewPager) home_charts.findViewById(R.id.pager_6);
        Pager_7 = (ViewPager) home_charts.findViewById(R.id.invoices_pager);

        ImageView imageView_pager1 = (ImageView) home_charts.findViewById(R.id.imageView_pager_1);
        ImageView imageView_pager2 = (ImageView) home_charts.findViewById(R.id.imageView_pager_2);
        ImageView imageView_pager3 = (ImageView) home_charts.findViewById(R.id.imageView_pager_3);
        ImageView imageView_pager4 = (ImageView) home_charts.findViewById(R.id.imageView_pager_4);
        ImageView imageView_pager5 = (ImageView) home_charts.findViewById(R.id.imageView_pager_5);
        ImageView imageView_pager6 = (ImageView) home_charts.findViewById(R.id.imageView_pager_6);

        AdapterPager_1 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(),Numboftabs,Name_Pager_1,getActivity());
        AdapterPager_2 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(),Numboftabs,Name_Pager_2,getActivity());
        AdapterPager_3 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(),Numboftabs,Name_Pager_3,getActivity());
        AdapterPager_4 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(),Numboftabs,Name_Pager_4,getActivity());
        AdapterPager_5 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(),Numboftabs,Name_Pager_5,getActivity());
        AdapterPager_6 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(),Numboftabs,Name_Pager_6,getActivity());
        AdapterPager_7 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(),Numboftabs,"Invoices Company",getActivity());

        Pager_1.setAdapter(AdapterPager_1);
        Pager_2.setAdapter(AdapterPager_2);
        Pager_3.setAdapter(AdapterPager_3);
        Pager_4.setAdapter(AdapterPager_4);
        Pager_5.setAdapter(AdapterPager_5);
        Pager_6.setAdapter(AdapterPager_6);
        Pager_7.setAdapter(AdapterPager_7);



        imageView_pager1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                charts_dialog(getActivity(), "PAGER_1", getBarCharts_list());

                return false;
            }
        });

        imageView_pager2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                charts_dialog(getActivity(), "PAGER_2", getBarCharts_list());


                return false;
            }
        });

//
        imageView_pager3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view){
                charts_dialog(getActivity(), "PAGER_3", getPieCharts_list());

                return false;
            }
        });
        imageView_pager4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                charts_dialog(getActivity(),"PAGER_4",getPieCharts_list());
                return false;
            }
        });

        imageView_pager5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                charts_dialog(getActivity(),"PAGER_5",getPieCharts_list());

                return false;
            }
        });

        imageView_pager6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                charts_dialog(getActivity(),"PAGER_6",getPieCharts_list());

                return false;
            }
        });




        return home_charts;
    }

    public void charts_dialog(final Context context, final String location, ArrayList list ) {

        mLocation=location;
        mContext=context;
        name_item_selected="";



        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("select one ");
        builder.setCancelable(false);

        singel_list = new ListView(context);
        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_single_choice,list);
        singel_list.setAdapter(adapter);

        singel_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        singel_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name_item_selected = parent.getItemAtPosition(position).toString();
                mPotion = position;
        //        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);

            }
        });

        builder.setView(singel_list);

        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ToDo
                //onclick
                if (!name_item_selected.equals("")) {
                    if (mLocation.equalsIgnoreCase("PAGER_1") || mLocation.equalsIgnoreCase("PAGER_2")) {
                        if (mPotion == 0) {
                            String s0 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, s0, "LIST_1_ITEM_0");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);

                        } else if (mPotion == 1) {
                            String s1 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, s1, "LIST_1_ITEM_1");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);

                        } else if (mPotion == 2) {
                            String s2 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, s2, "LIST_1_ITEM_2");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);

                        } else if (mPotion == 3) {
                            String s3 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, s3, "LIST_1_ITEM_3");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);
                        } else if (mPotion == 4) {
                            String s4 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, s4, "LIST_1_ITEM_4");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);
                        } else if (mPotion == 5) {
                            String s5 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, s5, "LIST_1_ITEM_5");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);
                        } else if (mPotion == 6) {
                            String s6 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, s6, "LIST_1_ITEM_6");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);
                        } else if (mPotion == 7) {
                            String s7 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, s7, "LIST_1_ITEM_7");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);
                        } else if (mPotion == 8) {
                            String s8 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, s8, "LIST_1_ITEM_8");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);
                        } else if (mPotion == 9) {
                            String s9 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, s9, "LIST_1_ITEM_9");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);
                        } else if (mPotion == 10) {
                            String s10 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, s10, "LIST_1_ITEM_10");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);
                        }
                        if (mLocation.equalsIgnoreCase("PAGER_1")) {
                            AdapterPager_1 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(), Numboftabs, name_item_selected, getActivity());
                            Pager_1.setAdapter(AdapterPager_1);

                        } else if (mLocation.equalsIgnoreCase("PAGER_2")) {
                            AdapterPager_2 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(), Numboftabs, name_item_selected, getActivity());
                            Pager_2.setAdapter(AdapterPager_2);
                        }


                        adapter.notifyDataSetChanged();
                    } else if (mLocation.equalsIgnoreCase("PAGER_3") || mLocation.equalsIgnoreCase("PAGER_4")
                            || mLocation.equalsIgnoreCase("PAGER_5") || mLocation.equalsIgnoreCase("PAGER_6")) {
                        if (mPotion == 0) {
                            String ss0 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, ss0, "LIST_2_ITEM_0");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);

                        } else if (mPotion == 1) {
                            String ss1 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, ss1, "LIST_2_ITEM_1");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);

                        } else if (mPotion == 2) {
                            String ss2 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, ss2, "LIST_2_ITEM_2");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);

                        } else if (mPotion == 3) {
                            String ss3 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, ss3, "LIST_2_ITEM_3");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);

                        } else if (mPotion == 4) {
                            String ss4 = MyHomeChartsSharedPreference.getPagerName(mContext, mLocation);
                            MyHomeChartsSharedPreference.ChangeItemName(mContext, ss4, "LIST_2_ITEM_4");
                            MyHomeChartsSharedPreference.ChangePagerName(mContext, name_item_selected, mLocation);

                        }
                        if (mLocation.equalsIgnoreCase("PAGER_3")) {
                            AdapterPager_3 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(), Numboftabs, name_item_selected, getActivity());
                            Pager_3.setAdapter(AdapterPager_3);

                        } else if (mLocation.equalsIgnoreCase("PAGER_4")) {
                            AdapterPager_4 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(), Numboftabs, name_item_selected, getActivity());
                            Pager_4.setAdapter(AdapterPager_4);

                        } else if (mLocation.equalsIgnoreCase("PAGER_5")) {
                            AdapterPager_5 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(), Numboftabs, name_item_selected, getActivity());
                            Pager_5.setAdapter(AdapterPager_5);

                        } else if (mLocation.equalsIgnoreCase("PAGER_6")) {
                            AdapterPager_6 = new MyHomeChartsAdapter(getActivity().getSupportFragmentManager(), Numboftabs, name_item_selected, getActivity());
                            Pager_6.setAdapter(AdapterPager_6);
                        }

                    }
                }else {
                    Toast.makeText(getActivity(),"You didn't Choose Chart",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
            }
                }

            );

//        dialog = builder.create();
//        dialog.show();
//        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);

            builder.show();


        }


    public ArrayList getBarCharts_list(){

        BarCharts_list.clear();
        BarCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_1_ITEM_0"));
        BarCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_1_ITEM_1"));
        BarCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_1_ITEM_2"));
        BarCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_1_ITEM_3"));
        BarCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_1_ITEM_4"));
        BarCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_1_ITEM_5"));
        BarCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_1_ITEM_6"));
        BarCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_1_ITEM_7"));
        BarCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_1_ITEM_8"));
        BarCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_1_ITEM_9"));
        BarCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_1_ITEM_10"));

        return BarCharts_list;
    }

    public ArrayList getPieCharts_list(){
        PieCharts_list.clear();
        PieCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_2_ITEM_0"));
        PieCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_2_ITEM_1"));
        PieCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(), "LIST_2_ITEM_2"));
        PieCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(),"LIST_2_ITEM_3"));
        PieCharts_list.add(MyHomeChartsSharedPreference.getItemName(getActivity(),"LIST_2_ITEM_4"));

        return PieCharts_list;
    }

    }


