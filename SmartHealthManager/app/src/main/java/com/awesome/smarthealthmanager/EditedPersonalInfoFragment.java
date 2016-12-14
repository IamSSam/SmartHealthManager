package com.awesome.smarthealthmanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by yoonjae on 06/12/2016.
 */

public class EditedPersonalInfoFragment extends Fragment {
    TextView name;
    TextView age;
    TextView sex;
    TextView edit_height;
    TextView edit_weight;
    TextView edit_abo;
    TextView edit_medicine;
    TextView edit_allergy;
    TextView edit_history;
    TextView edit_sleepTime;
    TextView edit_dailyStride;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info_edited, container, false);

        Button editButton = (Button) view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  FragmentTransaction transaction = getFragmentManager().beginTransaction();
                  transaction.replace(R.id.root, new PersonalInfoFragment());
                  transaction.addToBackStack(null);
                  transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                  transaction.commit();
              }
          }
        );


        name = (TextView) view.findViewById(R.id.tab1_name);
        name.setText(PersonalInfoFragment.tab1_name.getText().toString());

        age = (TextView) view.findViewById(R.id.tab1_age);
        age.setText(PersonalInfoFragment.tab1_age.getText().toString());

        sex = (TextView) view.findViewById(R.id.tab1_sex);
        sex.setText(PersonalInfoFragment.tab1_sex.getText().toString());

        edit_height = (TextView) view.findViewById(R.id.tab1_height_edited);
        edit_height.setText(PersonalInfoFragment.tab1_height.getText().toString());

        edit_weight = (TextView) view.findViewById(R.id.tab1_weight_edited);
        edit_weight.setText(PersonalInfoFragment.tab1_weight.getText().toString());

        edit_abo = (TextView) view.findViewById(R.id.tab1_abo_edited);
        edit_abo.setText(PersonalInfoFragment.tab1_abo.getText().toString());

        edit_medicine = (TextView) view.findViewById(R.id.tab1_medicine_edited);
        edit_medicine.setText(PersonalInfoFragment.tab1_medicine.getText().toString());

        edit_allergy = (TextView) view.findViewById(R.id.tab1_allergy_edited);
        edit_allergy.setText(PersonalInfoFragment.tab1_allergy.getText().toString());

        edit_history = (TextView) view.findViewById(R.id.tab1_history_edited);
        edit_history.setText(PersonalInfoFragment.tab1_history.getText().toString());

        edit_sleepTime = (TextView) view.findViewById(R.id.tab1_sleepTime_edited);
        edit_sleepTime.setText(PersonalInfoFragment.tab1_sleepTime.getText().toString());

        edit_dailyStride = (TextView) view.findViewById(R.id.tab1_dailyStride_edited);
        edit_dailyStride.setText(PersonalInfoFragment.tab1_dailyStride.getText().toString());

        return view;
    }
}
