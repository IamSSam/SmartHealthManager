package com.awesome.smarthealthmanager;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    static EditText phone, name, birth;
    static String sex, month;
    public EditText userId;
    public EditText userPwd;
    public EditText comparePwd;
    public EditText day;
    public Spinner spinner;
    private ArrayAdapter<CharSequence> spinnerAdapter = null;
    private Button manButton, womanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.user_name);
        phone = (EditText) findViewById(R.id.user_phoneNumber);
        birth = (EditText) findViewById(R.id.user_birth);

        userId = (EditText) findViewById(R.id.user_account);
        userPwd = (EditText) findViewById(R.id.user_password);
        comparePwd = (EditText) findViewById(R.id.user_compare_password);
        day = (EditText) findViewById(R.id.user_day);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setSelection(0);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Resources res = getResources();
        final String[] month_array = res.getStringArray(R.array.month);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                parent.getItemAtPosition(position);
                spinner.setOnItemSelectedListener(this);
                month = month_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });


        Button signUpButton = (Button) findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);

                if (userId.getText().length() == 0 || userPwd.getText().length() < 8) {
                    Toast.makeText(SignUpActivity.this, "아이디와 비밀번호를 확인해주세요. 비밀번호는 8자 이상으로 입력해주세요.", Toast.LENGTH_LONG).show();
                } else if (!userPwd.getText().toString().equals(comparePwd.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "입력한 비밀번호가 같지 않습니다.", Toast.LENGTH_SHORT).show();
                    comparePwd.hasFocus();
                } else if (getName().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (getSex().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else if (getAge().length() == 0 || day.getText().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (phone.getText().length() == 0) {
                    Toast.makeText(SignUpActivity.this, "휴대전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else {
                    // TODO: 회원가입하면 DB에 등록되는 부분. 이 정보들을 서버로 넘겨야함.
                    new HttpAsyncTask().execute("http://igrus.mireene.com/applogin/register.php");
                    Log.d("Sign up Cliked", "Success");
                }

            }
        });

        manButton = (Button) findViewById(R.id.user_man);
        manButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!manButton.isSelected()) {
                    manButton.setSelected(true);
                    womanButton.setSelected(false);
                    sex = "남자";

                } else {
                    manButton.setSelected(false);
                    sex = null;
                }
            }
        });

        womanButton = (Button) findViewById(R.id.user_woman);
        womanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!womanButton.isSelected()) {
                    womanButton.setSelected(true);
                    manButton.setSelected(false);
                    sex = "여자";

                } else {
                    womanButton.setSelected(false);
                    sex = null;
                }
            }
        });
    }

    public static String getName() {
        return name.getText().toString();
    }

    public static String getAge() {
        return birth.getText().toString();
    }

    public String getSex() {
        return sex;
    }

    static String getMonth() {
        if (month.length() == 2) {
            return month.substring(0, 1);
        } else {
            return month.substring(0, 2);
        }
    }

    public class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Did not work!")) {
                Toast.makeText(SignUpActivity.this, "로그인 실패 인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONObject jobj = new JSONObject(result);
                if (jobj.getString("error").equals("true")) {
                    Toast.makeText(SignUpActivity.this, "이미 있는 아이디이거나 서버 오류입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
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

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(6);
            nameValuePair.add(new BasicNameValuePair("userid", userId.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("password", userPwd.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("name", name.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("phonenumber", phone.getText().toString()));

            Log.d("monthandday", month + " " + day.getText().toString());

            String monthtmp;
            if (getMonth().length() == 1) {
                monthtmp = "0" + getMonth();
            } else monthtmp = getMonth();
            String daytmp;
            if (day.getText().toString().length() == 1) {
                daytmp = "0" + day.getText().toString();
            } else daytmp = day.getText().toString();
            nameValuePair.add(new BasicNameValuePair("birth", birth.getText().toString() + monthtmp + daytmp));
            String sextmp;
            if (sex.equals("남자")) sextmp = "1";
            else sextmp = "0";
            nameValuePair.add(new BasicNameValuePair("sex", sextmp));

            // 5. set json to StringEntity
            //StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, "utf-8"));

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
            Log.d("InputStream", e.getLocalizedMessage());
        }
        Log.d("http", result);

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "", result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}
