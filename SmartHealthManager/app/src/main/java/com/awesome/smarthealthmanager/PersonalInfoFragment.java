package com.awesome.smarthealthmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yoonjae on 29/11/2016.
 */

public class PersonalInfoFragment extends Fragment {
    Context context;
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

    String currentDateTime;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = getContext();

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int age = year - Person.birth.getYear();

        int cmonth = Calendar.getInstance().get(Calendar.MONTH);
        int myMonth = Person.birth.getMonth();

        final View view = inflater.inflate(R.layout.fragment_personal_info, container, false);

        tab1_name = (TextView) view.findViewById(R.id.tab1_name);
        tab1_age = (TextView) view.findViewById(R.id.tab1_age);
        tab1_sex = (TextView) view.findViewById(R.id.tab1_sex);

        tab1_height = (EditText) view.findViewById(R.id.tab1_height);
        tab1_weight = (EditText) view.findViewById(R.id.tab1_weight);
        tab1_abo = (EditText) view.findViewById(R.id.tab1_abo);
        tab1_medicine = (EditText) view.findViewById(R.id.tab1_medicine);
        tab1_allergy = (EditText) view.findViewById(R.id.tab1_allergy);
        tab1_history = (EditText) view.findViewById(R.id.tab1_history);
        tab1_sleepTime = (EditText) view.findViewById(R.id.tab1_sleepTime);
        tab1_dailyStride = (EditText) view.findViewById(R.id.tab1_dailyStride);
        tab1_height.setNextFocusDownId(R.id.tab1_weight);

        setInformationIfExist();

        saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("0yyyy-MM-dd");
                currentDateTime = sdfNow.format(date);

                new HttpAsyncTask().execute("http://igrus.mireene.com/applogin/personInfo.php/?userid=" + Person.userId + "&height=" + PersonalInfoFragment.tab1_height.getText().toString()
                        + "&weight=" + PersonalInfoFragment.tab1_weight.getText().toString() + "&abo=" + PersonalInfoFragment.tab1_abo.getText().toString() + "&medicine=" + PersonalInfoFragment.tab1_medicine.getText().toString()
                        + "&allergy=" + PersonalInfoFragment.tab1_allergy.getText().toString() + "&history=" + PersonalInfoFragment.tab1_history.getText().toString() + "&sleeptime=" + PersonalInfoFragment.tab1_sleepTime.getText().toString()
                        + "&dailystride=" + PersonalInfoFragment.tab1_dailyStride.getText().toString() + "&date=" + currentDateTime.toString());


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

    private void setInformationIfExist() {
        if(!Person.d_height.equals("-1")) {
            tab1_height.setText(Person.d_height, TextView.BufferType.EDITABLE);
        }
        if(!Person.d_weight.equals("-1")) {
            tab1_weight.setText(Person.d_weight,TextView.BufferType.EDITABLE);
        }
        if(!Person.d_abo.equals("-1")) {
            tab1_abo.setText(Person.d_abo,TextView.BufferType.EDITABLE);
        }
        if(!Person.d_medicine.equals("-1")) {
            tab1_medicine.setText(Person.d_medicine,TextView.BufferType.EDITABLE);
        }
        if(!Person.d_allergy.equals("-1")) {
            tab1_allergy.setText(Person.d_allergy,TextView.BufferType.EDITABLE);
        }
        if(!Person.d_history.equals("-1")) {
            tab1_history.setText(Person.d_history,TextView.BufferType.EDITABLE);
        }
        if(!Person.d_sleeptime.equals("-1")) {
            tab1_sleepTime.setText(Person.d_sleeptime,TextView.BufferType.EDITABLE);
        }
        if(!Person.d_dailystride.equals("-1")) {
            tab1_dailyStride.setText(Person.d_dailystride,TextView.BufferType.EDITABLE);
        }
    }


    public class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//            Log.d("check result", result);
            if (result.equals("Did not work!")) {
                Toast.makeText(context, "데이터 저장 실패, 인터넷 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
            } else {
                //TODO : 전송완료 후 할일
                Toast.makeText(context, "데이터가 저장되었습니다.", Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jobj = new JSONObject(result);

                    Log.d("RESULT" , result);

                    Person.d_height = jobj.getString("height");
                    Person.d_weight = jobj.getString("weight");
                    Person.d_abo = jobj.getString("abo");
                    Person.d_medicine = jobj.getString("medicine");
                    Person.d_allergy = jobj.getString("allergy");
                    Person.d_history = jobj.getString("history");
                    Person.d_sleeptime = jobj.getString("sleeptime");
                    Person.d_dailystride = jobj.getString("dailystride");
                } catch (JSONException e) {
                    Log.d("ERROR" , result);
                    e.printStackTrace();
                }
//                Person.name = jobj.getString("name");
//                Person.birth.setYear(Integer.parseInt(jobj.getString("birth").substring(0, 4)));
//
//                tab1_abo.setText();
//                tab1_allergy = (EditText) view.findViewById(R.id.tab1_allergy);
//                tab1_dailyStride = (EditText) view.findViewById(R.id.tab1_dailyStride);
//                tab1_height = (EditText) view.findViewById(R.id.tab1_height);
//                tab1_history = (EditText) view.findViewById(R.id.tab1_history);
//                tab1_medicine = (EditText) view.findViewById(R.id.tab1_medicine);
//                tab1_sleepTime = (EditText) view.findViewById(R.id.tab1_sleepTime);
//                tab1_weight = (EditText) view.findViewById(R.id.tab1_weight);
//                tab1_height.setNextFocusDownId(R.id.tab1_weight);
            }
        }
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            //jsonObject.accumulate("name", person.getName());
            //jsonObject.accumulate("country", person.getCountry());
            //jsonObject.accumulate("twitter", person.getTwitter());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            //StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            //httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, "utf-8"));
            // 7. Set some headers to inform server about the type of the content
            //httpPost.setHeader("Accept", "application/json");
            //httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
        }
//        Log.d("http", result);

        // 11. return result
        return result;
    }

    public String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "", result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }


}