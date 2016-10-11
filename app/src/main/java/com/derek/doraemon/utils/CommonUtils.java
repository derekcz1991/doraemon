package com.derek.doraemon.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.derek.doraemon.MyApplication;

import java.util.List;

/**
 * Created by derek on 2016/9/29.
 */
public class CommonUtils {
    private static final String TAG = "CommonUtils";

    private static double latitude;
    private static double longitude;

    public static void toast(String msg) {
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showProgress(Context context, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public static double[] getLocation() {
        double[] latlong = new double[2];
        try {
            LocationManager e = (LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setCostAllowed(false);
            criteria.setAccuracy(2);
            String providerName = e.getBestProvider(criteria, true);
            if (providerName != null) {
                if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), "android.permission.ACCESS_FINE_LOCATION") != 0
                    && ActivityCompat.checkSelfPermission(MyApplication.getContext(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
                    Log.e(TAG, "no permission");
                }

                e.getLastKnownLocation(providerName);
                Location location = getLastKnownLocation();//e.getLastKnownLocation(providerName);
                if (null == location) {
                    latlong[0] = -1;
                    latlong[1] = -1;
                    return latlong;
                }

                latlong[0] = location.getLatitude();
                latlong[1] = location.getLongitude();
            }
        } catch (Exception var9) {
            Log.e(TAG, "geoLocation ->geoLocation>>>", var9);
        }

        return latlong;
    }

    private static Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager)MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
