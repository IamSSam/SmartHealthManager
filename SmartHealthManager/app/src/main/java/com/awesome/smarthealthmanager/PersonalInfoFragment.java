package com.awesome.smarthealthmanager;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;
import java.util.Calendar;

/**
 * Created by yoonjae on 29/11/2016.
 */

public class PersonalInfoFragment extends Fragment {

    static TextView tab1_name;
    static TextView tab1_age;
    static TextView tab1_sex;
    static Button saveButton;

    static EditText tab1_abo;
    static EditText tab1_allergy;
    static EditText tab1_dailyStride;
    static EditText tab1_height;
    static EditText tab1_history;
    static EditText tab1_medicine;
    static EditText tab1_sleepTime;
    static EditText tab1_weight;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int age = year - Person.birth.getYear();


        int cmonth = Calendar.getInstance().get(Calendar.MONTH);
        int myMonth = Person.birth.getMonth();

        final View view = inflater.inflate(R.layout.fragment_personal_info, container, false);


        tab1_name = (TextView) view.findViewById(R.id.tab1_name);
        tab1_age = (TextView) view.findViewById(R.id.tab1_age);
        tab1_sex = (TextView) view.findViewById(R.id.tab1_sex);

        tab1_abo = (EditText) view.findViewById(R.id.tab1_abo);
        tab1_allergy = (EditText) view.findViewById(R.id.tab1_allergy);
        tab1_dailyStride = (EditText) view.findViewById(R.id.tab1_dailyStride);
        tab1_height = (EditText) view.findViewById(R.id.tab1_height);
        tab1_history = (EditText) view.findViewById(R.id.tab1_history);
        tab1_medicine = (EditText) view.findViewById(R.id.tab1_medicine);
        tab1_sleepTime = (EditText) view.findViewById(R.id.tab1_sleepTime);
        tab1_weight = (EditText) view.findViewById(R.id.tab1_weight);

        tab1_height.setNextFocusDownId(R.id.tab1_weight);
        //TODO : EditText에 적혀있는 내용을 서버로 옮겨야함

        saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.root, new EditedPersonalInfoFragment());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        String user_name = Person.name;

        tab1_name.setText(user_name);

        String user_age = "" + age;


        if (((cmonth + 1) - myMonth) >= 0) {
            tab1_age.setText("만 " + age + "세" + ". " + (cmonth - myMonth + 1) + "개월");
            System.out.println("=============================================" + cmonth);

        } else {
            tab1_age.setText("만 " + (age - 1) + "세" + ". " + (cmonth - myMonth + 13) + "개월");
            System.out.println("=============================================" + cmonth);

        }

        String user_sex;
        if (Person.sex == 1)
            user_sex = "남";
        else
            user_sex = "여";
        tab1_sex.setText(user_sex);

        return view;
    }
}