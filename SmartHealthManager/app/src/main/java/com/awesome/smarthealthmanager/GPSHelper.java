package com.awesome.smarthealthmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by yoonjae on 29/11/2016.
 */

public class GPSHelper {

    private static Activity prevActivity;
    public static double longitude = -1;
    public static double latitude = -1;
    public static String hospital_type;


    public void initiateGPSservice(Context context, Activity activity, String hospital) {
        try {
            prevActivity = activity;
            hospital_type = hospital;
            Toast.makeText(context, "현재 위치를 받아오는 중 입니다...", Toast.LENGTH_SHORT).show();

            final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, locationListener);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            Log.v(TAG, Double.toString(longitude));
            latitude = location.getLatitude();
            Log.v(TAG, Double.toString(latitude));

            if (FindHospitalActivity.findHospitalViewActive) {
                //// TODO: 08/12/2016 MapView is already active, pass the updated location


            } else {
                Intent intent = new Intent(prevActivity, FindHospitalActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                prevActivity.startActivity(intent);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
