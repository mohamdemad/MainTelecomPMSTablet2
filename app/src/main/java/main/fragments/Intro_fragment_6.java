package main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.MainTelecom_Tablet.Activities.LoginActivity;
import com.MainTelecom_Tablet.R;
import com.MainTelecom_Tablet.SharedPreference.SaveSharedPreference;


public class Intro_fragment_6 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View intro_fragment_6 = inflater.inflate(R.layout.fragment_intro_fragment_6, container, false);
        // Inflate the layout for this fragment

        Button button=(Button)intro_fragment_6.findViewById(R.id.register_id);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSharedPreference.setIntroCheck(getActivity(),"checked");

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        return intro_fragment_6;
    }
}
