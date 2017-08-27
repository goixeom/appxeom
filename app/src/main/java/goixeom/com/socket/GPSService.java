package goixeom.com.socket;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import goixeom.com.utils.Constants;

public class GPSService extends Service implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    public static final String ACTION_LOCATION_UPDATE = "1";
    private static Location _location;
    private SensorManager mSensorManager;
    private Sensor sensor;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private GoogleApiClient _gac;
    String TAG = "SENSOR";
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    public void onLocationChanged(Location arg0) {
        if (arg0 == null) return;
        _location = arg0;
        Intent i = new Intent(ACTION_LOCATION_UPDATE);
        i.putExtra(Constants.LOCATION, arg0);
        LogUtils.e("Update location : " + arg0.getLatitude() + " - " + arg0.getLongitude() + "- bearing : " + mCurrentDegree);
        sendBroadcast(i);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest lr = LocationRequest.create();
        lr.setPriority(100);
        lr.setInterval(1000);
        Location location = LocationServices.FusedLocationApi.getLastLocation(_gac);
        onLocationChanged(location);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationServices.FusedLocationApi.requestLocationUpdates(this._gac, lr, (LocationListener) this);
        }
    }

    public void onConnectionSuspended(int arg0) {
        LocationServices.FusedLocationApi.removeLocationUpdates(this._gac, (LocationListener) this);
    }

    private void stopLocationUpdate() {
        if (this._gac != null) {
            this._gac.disconnect();
        }
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        switch (connectionResult.getErrorCode()) {
        }
    }

    public void onCreate() {
        super.onCreate();
        if (this._gac == null) {
            this._gac = new Builder(getApplicationContext()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
        this._gac.connect();
//        mSensorManager =  (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);
//        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
//        if(sensor!=null) {
//            // for the system's orientation sensor registered listeners
//            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);//SensorManager.SENSOR_DELAY_Fastest
//        }else{
//            Toast.makeText(getApplicationContext(),"Not Supported", Toast.LENGTH_SHORT).show();
//        }
//        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME); //SensorManager.SENSOR_DELAY_Fastest
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void onDestroy() {
        stopLocationUpdate();
//        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor == mAccelerometer) {
//            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
//            mLastAccelerometerSet = true;
//        } else if (event.sensor == mMagnetometer) {
//            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
//            mLastMagnetometerSet = true;
//        }
//        if (mLastAccelerometerSet && mLastMagnetometerSet) {
//            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
//            SensorManager.getOrientation(mR, mOrientation);
//            float azimuthInRadians = mOrientation[0];
//            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
//            RotateAnimation ra = new RotateAnimation(
//                    mCurrentDegree,
//                    -azimuthInDegress,
//                    Animation.RELATIVE_TO_SELF, 0.5f,
//                    Animation.RELATIVE_TO_SELF,
//                    0.5f);
//
//            ra.setDuration(250);
//
//            ra.setFillAfter(true);
//
//
//            mCurrentDegree = -azimuthInDegress;
//            Intent i = new Intent(ACTION_LOCATION_UPDATE);
//            i.putExtra(Constants.LOCATION, _location);
//            i.putExtra(Constants.ANGLE_FROM, 0);
//            i.putExtra(Constants.ANGLE_TO, mCurrentDegree);
//            LogUtils.e("Update location : " + _location.getLatitude() + " - " + _location.getLongitude() + " - " + mCurrentDegree);
//            MainApplication.getInstance().setmLocation(_location);
//            sendBroadcast(i);
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }
}
