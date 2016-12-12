package com.awesome.smarthealthmanager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoonjae on 29/11/2016.
 */

public class PersonalSymptomFragment extends Fragment implements View.OnClickListener {

    GridView gridView;
    Button symptom_main_btn1;
    Button symptom_main_btn2;
    Button symptom_main_btn3;
    Button find_hospital;
    Button more_confirm;
    private String tmp_st_main;
    private int current_position;
    private int tmp_st_scale;
    private String tmp_st_sub = "";
    private String st_comment = "";
    private EditText tmp_st_comment;
    private Dialog levelDialog;
    private Dialog moreDialog;
    HttpAsyncTask httpAsyncTask;
    Context context;
    private final static int REQUEST_LOCATION = 1;
    final String[ /* For UI */][ /* For Naver Maps */] st_place = {
            {"머리", "내과"},
            {"얼굴", "외과"},
            {"식도", "내과"},
            //{ "가슴", "" },
            //{ "배", "" },
            {"등", "정형외과"},
            {"다리", "정형외과"},
            {"팔", "정형외과"},
            {"발", "정형외과"},
            {"위", "내과"},
            {"폐", "내과"},
            {"손", "외과"},
            {"간", "내과"},
            //{ "엉덩이", "비뇨기과" },
            {"두개골", "내과"},
            {"치아", "치과"},
            //{ "생식기 (남자)", "" },
            //{ "생식기 (여자)", "" },
            {"목", "소아과"},
            {"코", "소아과"},
            {"발바닥", "피부과"},
            {"손가락", "정형외과"},
            {"혀", "내과"},
            {"척추", "정형외과"},
            {"귀", "이비인후과"},
            {"팔근육", "정형외과"}
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getContext();

         /* Dialog 부분 */
        levelDialog = new Dialog(getContext());
        levelDialog.setTitle("Select level:");
        levelDialog.setContentView(R.layout.dialog_evaluation);

        moreDialog = new Dialog(getContext());
        moreDialog.setContentView(R.layout.dialog_more);

        int image[] = {
                R.drawable.head,
                R.drawable.face,
                R.drawable.neck,
                //R.drawable.breast,
                //R.drawable.belly,
                R.drawable.back,
                R.drawable.leg,
                R.drawable.arm,
                R.drawable.ankle,
                R.drawable.digestive,
                R.drawable.respiratory,
                R.drawable.hand,
                R.drawable.heart,
                //R.drawable.hip,
                R.drawable.jaw,
                R.drawable.teeth,
                //R.drawable.man,
                //R.drawable.woman,
                R.drawable.neck2,
                R.drawable.nouse,
                R.drawable.sole,
                R.drawable.finger,
                R.drawable.tongue,
                R.drawable.spine,
                R.drawable.ear,
                R.drawable.elbow
        };

        View view = inflater.inflate(R.layout.fragment_personal_symptom, container, false);

        GridAdapter gridAdapter = new GridAdapter(getActivity(), R.layout.row, image);

        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.invalidateViews();
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(),
                        "position:" + position + " , " +
                                "krplacename: " + st_place[position][0] + " , " +
                                "hospital: " + st_place[position][1],
                        Toast.LENGTH_SHORT).show();
                ((TextView) levelDialog.findViewById(R.id.evaluation_title)).setText(st_place[position][0]);
                current_position = position;
                levelDialog.show();
            }
        });

        symptom_main_btn1 = (Button) levelDialog.findViewById(R.id.symptom_main_btn1);
        symptom_main_btn2 = (Button) levelDialog.findViewById(R.id.symptom_main_btn2);
        symptom_main_btn3 = (Button) levelDialog.findViewById(R.id.symptom_main_btn3);
        find_hospital = (Button) levelDialog.findViewById(R.id.find_hospital);

        more_confirm = (Button) moreDialog.findViewById(R.id.more_confirm);
        more_confirm.setOnClickListener(this);

        symptom_main_btn1.setOnClickListener(this);
        symptom_main_btn2.setOnClickListener(this);
        symptom_main_btn3.setOnClickListener(this);
        find_hospital.setOnClickListener(this);


        /* SeekBar 부분, 통증 선택하기 */
/*
        final TextView levelTxt = (TextView) levelDialog.findViewById(R.id.level_txt);
        final SeekBar levelSeek = (SeekBar) levelDialog.findViewById(R.id.level_seek);

        levelSeek.setMax(10);
        levelSeek.setProgress(10);

        levelSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //change to progress
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                levelTxt.setText(Integer.toString(progress));
            }

            //methods to implement but not necessary to amend
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
*/
        final TextView moreTxt = (TextView) moreDialog.findViewById(R.id.more_txt);
        final SeekBar moreSeek = (SeekBar) moreDialog.findViewById(R.id.more_seek);

        moreSeek.setMax(10);
        moreSeek.setProgress(10);

        moreSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //change to progress
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                moreTxt.setText(Integer.toString(progress));
                tmp_st_scale = progress;
            }

            //methods to implement but not necessary to amend
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button okBtn = (Button) levelDialog.findViewById(R.id.level_cancel);
        Button goDialogBtn;

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //respond to level

                //int chosenLevel = levelSeek.getProgress();
                levelDialog.dismiss();
            }
        });
/*
        level_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreDialog.show();
            }
        });
*/

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.symptom_main_btn1:
                tmp_st_main = symptom_main_btn1.getText().toString();
                moreDialog.show();
                break;

            case R.id.symptom_main_btn2:
                tmp_st_main = symptom_main_btn2.getText().toString();
                moreDialog.show();
                break;

            case R.id.symptom_main_btn3:
                tmp_st_main = symptom_main_btn3.getText().toString();
                moreDialog.show();
                break;

            case R.id.find_hospital:
                int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else { // 다음부터 물어보지 않고 바로 실행하는 부분
                    Toast.makeText(getActivity(), "location permission authorized", Toast.LENGTH_LONG);
                }
                GPSHelper gpsHelper = new GPSHelper();
                gpsHelper.initiateGPSservice(getContext(), getActivity(),
                        st_place[current_position][1]);
                levelDialog.dismiss();
                break;

            case R.id.more_confirm:
                levelDialog.dismiss();
                Person.st_main = tmp_st_main;
                Person.st_place = st_place[current_position][0];
                Person.st_scale = tmp_st_scale;
                Person.st_sub = tmp_st_sub;
                //Person.st_comment = ((EditText)v.findViewById(R.id.symptom_comment)).getText().toString();

                httpAsyncTask = new HttpAsyncTask();
                httpAsyncTask.execute("http://igrus.mireene.com/applogin/stchange.php");
                moreDialog.dismiss();
                break;

            default:
                break;
        }
    }

    /***
     * 권한요청 응답 처리하기
     **/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) { // 고객이 permission 접근을 승인할 시
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "location permission authorized", Toast.LENGTH_LONG);
                        } else {// 고객이 permission 접근을 거부할 시
                            Toast.makeText(getActivity(), "location permission denied", Toast.LENGTH_LONG);
                        }
                    }
                    break;
                }
            default:
                break;
        }
    }

    public class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("check result", result);
            if (result.equals("Did not work!")) {
                Toast.makeText(context, "데이터 전송 실패, 인터넷 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
            } else {
                //TODO : 전송완료 후 할일
                Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show();
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

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(7);
            nameValuePair.add(new BasicNameValuePair("userid", Person.userId));
            nameValuePair.add(new BasicNameValuePair("st_place", Person.st_place));
            nameValuePair.add(new BasicNameValuePair("st_main", Person.st_main));
            nameValuePair.add(new BasicNameValuePair("st_scale", " " + Person.st_scale));
            nameValuePair.add(new BasicNameValuePair("st_sub", Person.st_sub + " "));
            //TODO: Send st_comment data from android to DB
            nameValuePair.add(new BasicNameValuePair("st_comment", Person.st_comment + " "));

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

    public String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "", result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}
