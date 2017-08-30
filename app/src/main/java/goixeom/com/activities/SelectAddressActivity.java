package goixeom.com.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goixeom.com.R;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustomNoDelay;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.MyPlace;
import goixeom.com.socket.LocationBearing;
import goixeom.com.utils.CommonUtils;
import goixeom.com.utils.Constants;
import goixeom.com.utils.FileUtils;
import retrofit2.Call;

public class SelectAddressActivity extends BaseActivity implements OnMapReadyCallback, Runnable {
    GoogleMap mMap;
    @BindView(R.id.img_back)
    ImageView imgBack;

    @BindView(R.id.img_pin)
    ImageView imgPin;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.ll_root_chose_address)
    LinearLayout llRootChoseAddress;
    MyPlace place;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.tv_chose)
    TextView tvChose;
    @BindView(R.id.ll_chose)
    LinearLayout llChose;
    LatLng mLatlng;
    Handler handler = new Handler();
    HashMap<Integer, LocationBearing> hashMapMarker = new HashMap<>();
    @BindView(R.id.ll_root)
    FrameLayout llRoot;
    private Timer timerTask = new Timer();
    String pathName;
    Drawable dBike;
    Drawable dCar;

    @Override
    public void pingNotification(String title, String content) {

    }

    @Override
    protected void onSoketConnected() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        ButterKnife.bind(this);
        view= llRoot;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        place = new MyPlace();
        if (getIntent() != null) {
            boolean isFrom = getIntent().getExtras().getBoolean(Constants.MSG);
            latLng = new LatLng(getIntent().getExtras().getDouble(Constants.LAT), getIntent().getExtras().getDouble(Constants.LNG));
            if (isFrom) {
                imgPin.setImageResource(R.drawable.ic_pin);
            } else {
                imgPin.setImageResource(R.drawable.ic_pin);
                llChose.setBackgroundColor(Color.parseColor("#BC3F2C"));
            }
        }
    }

    LatLng latLng;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            pathName = FileUtils.getFolder(SelectAddressActivity.this) + getString(R.string.bike);
            dBike = Drawable.createFromPath(pathName);
            dBike = CommonUtils.resize(dBike, this);
            pathName = FileUtils.getFolder(SelectAddressActivity.this) + getString(R.string.car);
            dCar = Drawable.createFromPath(pathName);
            dCar = CommonUtils.resizeCar(dCar, this);
            if (latLng != null) {
                CommonUtils.focusCurrentLocation(latLng, Constants.ZOOM, mMap);
            }
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
            mMap.setMapStyle(style);
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    if (!NetworkUtils.isConnected()) return;
                    mLatlng = mMap.getCameraPosition().target;
                    new GeoTask(getApplicationContext(), address, place, loading).execute(mMap.getCameraPosition().target);
                    timerTask.cancel();
                    timerTask = new Timer();
                    handler.postDelayed(SelectAddressActivity.this, 1000);
                }
            });
        }
    }

    @OnClick(R.id.ll_chose)
    public void onViewClicked() {
        if (place.getLat() == 0 && place.getLng() == 0) return;
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putParcelable(Constants.MSG, place);
        i.putExtra(Constants.BUNDLE, b);
        setResult(RESULT_OK, i);
        finish();
    }

    @OnClick(R.id.img_back)
    public void onViewBackClicked() {
        onBackPressed();
    }

    @Override
    public void run() {
        timerTask.schedule(new TimerTask() {
            @Override
            public void run() {
                LogUtils.e("request driver now");
                if (mLatlng != null)
                    getDriverNearby(mLatlng);
            }
        }, 0, getmSetting().getLong(Constants.TIME_UPDATE,15000));
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerTask.cancel();
    }

    private void getDriverNearby(final LatLng location) {

        int type = getmSetting().getInt(Constants.TYPE_VEHICLE,0);

        Call<ApiResponse<List<LocationBearing>>> getDrivers = getmApi().getDriver(ApiConstants.API_KEY, location.latitude, location.longitude,type);
        getDrivers.enqueue(new CallBackCustomNoDelay<ApiResponse<List<LocationBearing>>>(this, new OnResponse<ApiResponse<List<LocationBearing>>>() {
            @Override
            public void onResponse(final ApiResponse<List<LocationBearing>> object) {
                if (object.getData() != null && object.getData().size() > 0) {
                    LogUtils.e("request + " + object.getData().size());

//                    Executors.newSingleThreadExecutor().execute(new MarkerHandler(object.getData()));
                    for (Iterator<Map.Entry<Integer, LocationBearing>> it = hashMapMarker.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<Integer, LocationBearing> entry = it.next();
                        int key = entry.getKey().intValue();
                        boolean isExist = false;

                        for (LocationBearing locationBearing : object.getData()) {
                            LatLng newLatlng = new LatLng(locationBearing.getLatitude(), locationBearing.getLongitude());
                            if (locationBearing.getD_id() == key && entry.getValue() != null) {
//                                MapUtils.animateMarkerNew(newLatlng, entry.getValue(), (float) locationBearing.getAngle());
//                                locationBearing.addToStack(newLatlng);
                                entry.getValue().addToStack(newLatlng);
//                                getDirectionToLatlng(entry.getValue(),entry.getValue().getMarker().getPosition(),newLatlng);
                                entry.getValue().getMarker().setVisible(true);
                                isExist = true;
                                break;
                            }
                        }
                        if (!isExist) {
                            entry.getValue().getMarker().remove();
                            it.remove();
                            entry.getValue().getMarker().setVisible(false);
                        }
                    }
                    for (LocationBearing locationBearing : object.getData()) {
                        LatLng newLatlng = new LatLng(locationBearing.getLatitude(), locationBearing.getLongitude());
                        if (hashMapMarker.containsKey(locationBearing.getD_id()) && hashMapMarker.get(locationBearing.getD_id()) != null) {
                        } else {
                            Marker marker;
                            int typeVehicle  = getmSetting().getInt(Constants.TYPE_VEHICLE,0);
                            Drawable drawable;
                            if(typeVehicle ==0 ){
//                                if (Build.VERSION.SDK_INT >= 20) {
//                                    drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_motorcycle);
//                                }else{
//                                    drawable = AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_motorcycle);
//                                }
                                drawable = dBike;
                            }else{
//                                if (Build.VERSION.SDK_INT >= 20) {
//                                    drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_car_view_icon);
//                                }else{
//                                    drawable = AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_car_view_icon);
//                                }
                                drawable = dCar;

                            }
                            marker = CommonUtils.addMarker(drawable, mMap, newLatlng, (float) locationBearing.getAngle(), locationBearing.getD_id() + "");
                            locationBearing.setMarker(marker);
                            hashMapMarker.put(locationBearing.getD_id(), locationBearing);
                        }
                    }
                } else {
                    for (Iterator<Map.Entry<Integer, LocationBearing>> it = hashMapMarker.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<Integer, LocationBearing> entry = it.next();
                        entry.getValue().getMarker().setVisible(false);
                        entry.getValue().getMarker().remove();
                        it.remove();
                    }
                }
            }
        }));
    }

    static class GeoTask extends AsyncTask<LatLng, Void, String> {
        WeakReference<TextView> TextWeakReference;
        WeakReference<Context> contextWeakReference;
        WeakReference<MyPlace> placeWeakReference;
        WeakReference<ProgressBar> loadingWeakReference;
        LatLng latLng;

        GeoTask(Context context, TextView textView, MyPlace place, ProgressBar progressBar) {
            TextWeakReference = new WeakReference<TextView>(textView);
            contextWeakReference = new WeakReference<Context>(context);
            placeWeakReference = new WeakReference<MyPlace>(place);
            loadingWeakReference = new WeakReference<ProgressBar>(progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(LatLng... latLngs) {
            latLng = latLngs[0];
            if (contextWeakReference.get() != null)
                return CommonUtils.getCompleteAddressString(contextWeakReference.get(), latLng.latitude, latLng.longitude);
            return "";
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (loadingWeakReference.get() != null)
                loadingWeakReference.get().setVisibility(View.GONE);
            if (TextWeakReference.get() != null) TextWeakReference.get().setText(s);
            if (placeWeakReference.get() != null) {
                placeWeakReference.get().setDescription(s);
                placeWeakReference.get().setLng(latLng.longitude);
                placeWeakReference.get().setLat(latLng.latitude);
            }
        }
    }
}
