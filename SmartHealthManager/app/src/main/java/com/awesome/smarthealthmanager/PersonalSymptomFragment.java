package com.awesome.smarthealthmanager;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

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
    private Dialog levelDialog;
    private Dialog moreDialog;
    HttpAsyncTask httpasynctask;

    final String[ /* For UI */][ /* For Naver Maps */] st_place = {
            {"머리", "정신과"},
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
            {"발바닥", ""},
            {"손가락", ""},
            {"혀", ""},
            {"척추", ""},
            {"귀", ""},
            {"팔근육", ""}
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
        find_hospital = (Button) levelDialog.findViewById(R.id.level_hospital);

        more_confirm = (Button) moreDialog.findViewById(R.id.more_confirm);

        symptom_main_btn1.setOnClickListener(this);
        symptom_main_btn2.setOnClickListener(this);
        symptom_main_btn3.setOnClickListener(this);
        find_hospital.setOnClickListener(this);

        more_confirm.setOnClickListener(this);

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

        //       Button level_more = (Button) levelDialog.findViewById(R.id.level_more);

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
            case R.id.level_hospital:
                GPSHelper.initiateGPSservice(getContext(), getActivity(),
                        st_place[current_position][1]);
                levelDialog.dismiss();
                break;
            case R.id.more_confirm:
                levelDialog.dismiss();
                Person.st_main = tmp_st_main;
                Person.st_place = st_place[current_position][0];
                Person.st_scale = tmp_st_scale;
                Person.st_sub = tmp_st_sub;
                httpasynctask = new HttpAsyncTask();
                httpasynctask.execute("http://igrus.mireene.com/applogin/stchange.php");
                moreDialog.dismiss();
                break;
            default:
                break;
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

    public String POST(String url, Person person) {
        return null;
    }

    public String convertInputStreamToString(InputStream inputStream) {
        return null;
    }
}
