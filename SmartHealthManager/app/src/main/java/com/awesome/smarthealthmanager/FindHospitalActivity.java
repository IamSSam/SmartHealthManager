package com.awesome.smarthealthmanager;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;

public class FindHospitalActivity extends AppCompatActivity {

    public static boolean findHospitalViewActive = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.findHospitalViewActive = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.findHospitalViewActive = true;
        setContentView(R.layout.activity_find_hospital);

        WebView webView = (WebView) findViewById(R.id.webView);

        // Do not use the global default application settings and force the MapView to load everything
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //String loadUrl = "http://arter97.com/hackathon/geocode.php?lng=" + GPSHelper.longitude + "&lat=" + GPSHelper.latitude + "&hospital=";
        String loadUrl = "http://igrus.mireene.com/PHR/mapview/geocode.php?lng=" + GPSHelper.longitude + "&lat=" + GPSHelper.latitude + "&hospital=";

        try {
            loadUrl += URLEncoder.encode(GPSHelper.hospital_type, "UTF-8");
        } catch (Exception e) {
            loadUrl += URLEncoder.encode(GPSHelper.hospital_type);
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        loadUrl += "&width=" + (size.x / 2);
        loadUrl += "&height=" + (size.y / 2);

        Log.v(getString(R.string.app_name), "loadUrl : " + loadUrl);

        webView.loadUrl(loadUrl);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setInitialScale(200);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false); // Naver Maps supports zoom
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
    }
}
