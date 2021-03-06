package com.awesome.smarthealthmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private SharedPreferences.Editor editor;
    private SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setting = getSharedPreferences("pref", MODE_PRIVATE) ;
        if(setting.getBoolean("auto", false)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        */
        setContentView(R.layout.activity_login);

        // Set up the login form.

        // Grid View Memory 를 위해서 먼저 불러옴
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // 마켓에 포팅하실땐 빼주세요.
                .build();
        ImageLoader.getInstance().init(config);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        Person.userId = mEmailView.getText().toString();
        Person.password = mPasswordView.getText().toString();
        Log.d("texter", Person.userId);
        Log.d("texter", Person.password);
        new LoginAsyncTask().execute("http://igrus.mireene.com/applogin/login.php");
//        new GetInfoAsyncTask().execute("http://igrus.mireene.com/applogin/getPersonInfo.php/?userid=" + Person.userId);
    }

    public class LoginAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Did not work!")) {
                Toast.makeText(LoginActivity.this, "로그인 실패 인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();
            }
            try {

                JSONObject jobj = new JSONObject(result);
                Person.name = jobj.getString("name");
                Person.birth.setYear(Integer.parseInt(jobj.getString("birth").substring(0, 4)));
                Person.birth.setMonth(Integer.parseInt(jobj.getString("birth").substring(4, 6)));
                Person.birth.setDate(Integer.parseInt(jobj.getString("birth").substring(6, 8)));


                Person.phonenumber = jobj.getString("phonenumber");
                Person.sex = jobj.getInt("sex");

                Log.d("check", "" + Person.name + " " + Person.phonenumber + " " + Person.sex + " ");
                Log.d("check", "" + Person.d_weight + " " + Person.d_height + " " + Person.d_history + " ");
                Person.initPerson();
                new GetInfoAsyncTask().execute("http://igrus.mireene.com/applogin/getPersonInfo.php/?userid=" + Person.userId);
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public static String POST(String url) {
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

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("userid", Person.userId));
            nameValuePair.add(new BasicNameValuePair("password", Person.password));

            // 5. set json to StringEntity
            //StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

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

    public class GetInfoAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST2(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Did not work!")) {
                Toast.makeText(LoginActivity.this, "로그인 실패 인터넷 연결을 확인하세요", Toast.LENGTH_SHORT).show();
            }
            try {
//                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                Log.d("Result", result);
                JSONObject jobj = new JSONObject(result);
                Person.d_height = jobj.getString("height");
                Person.d_weight = jobj.getString("weight");
                Person.d_abo = jobj.getString("abo");
                Person.d_medicine = jobj.getString("medicine");
                Person.d_allergy = jobj.getString("allergy");
                Person.d_history = jobj.getString("history");
                Person.d_sleeptime = jobj.getString("sleeptime");
                Person.d_dailystride = jobj.getString("dailystride");


            } catch (JSONException e) {
//                Toast.makeText(LoginActivity.this, "정보가져오기 실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public static String POST2(String url) {
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
            Log.d("InputStream", e.getLocalizedMessage());
        }
        Log.d("http", result);

        // 11. return result
        return result;
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "", result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}

