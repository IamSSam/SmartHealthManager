package com.awesome.smarthealthmanager;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    static EditText phone, name, birth;
    static String sex, month;
    public Bundle bundle;
    public EditText userId;
    public EditText userPwd;
    public EditText comparePwd;
    public EditText day;
    public Spinner spinner;
    private ArrayAdapter<CharSequence> spinnerAdapter = null;

    private Button manButton, womanButton;

    //private GoogleApiClient client;

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

                if (userId.getText().length() == 0 || userPwd.getText().length() < 10) {
                    Toast.makeText(SignUpActivity.this, "아이디와 비밀번호를 확인해주세요. 비밀번호는 10자 이상으로 입력해주세요.", Toast.LENGTH_LONG).show();
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
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
