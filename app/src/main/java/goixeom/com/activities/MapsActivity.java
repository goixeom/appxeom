package goixeom.com.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import goixeom.com.CustomTextView;
import goixeom.com.MainApplication;
import goixeom.com.R;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.apis.CallBackCustomNoDelay;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.MyPlace;
import goixeom.com.models.User;
import goixeom.com.socket.GPSService;
import goixeom.com.socket.LocationBearing;
import goixeom.com.utils.CommonUtils;
import goixeom.com.utils.Constants;
import goixeom.com.views.CustomTypefaceSpan;
import goixeom.com.views.Tooltip;
import retrofit2.Call;

//import com.balysv.materialmenu.MaterialMenuDrawable;

public class MapsActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, Runnable {
    @BindView(R.id.edt_from)
    TextView edtFrom;
    @BindView(R.id.dang_xuat)
    CustomTextView dangXuat;

    @BindView(R.id.contentview)
    RelativeLayout llRoot;

    RelativeLayout llTooltip;
    @BindView(R.id.ll_main)
    CoordinatorLayout llMain;
    @BindView(R.id.nhap_add)
    LinearLayout nhapAdd;


    private GoogleMap mMap;
    private Button button;
    private LatLngBounds.Builder mBounds = new LatLngBounds.Builder();
    private CustomTextView mTvName;
    private CircleImageView mAvt;
    private MyPlace mAddressFrom;
    private MyPlace mAddressDest;
    private FragmentManager mFragmentManager;
    //    HashMap<Integer, Marker> hashMapMarker = new HashMap<>();
    HashMap<Integer, LocationBearing> hashMapMarker = new HashMap<>();
    Trace myTrace = FirebasePerformance.getInstance().newTrace("test_trace");
    private Marker mMarker;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Location location = intent.getParcelableExtra(Constants.LOCATION);
                if (location == null && mMap == null) return;
                if (getmSocket() != null)
                    getmSocket().updateLatlng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude, getCurrentIdUser());
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                if (NetworkUtils.isConnected()) new GeoTask().execute(ll);
                mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (!isFocused) {
                    if (Build.VERSION.SDK_INT >= 20) {
                        mMarker = CommonUtils.addMarker(getResources().getDrawable(R.drawable.ic_pin, null), mMap, mLatLng);
                    } else {
                        mMarker = CommonUtils.addMarker(AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_pin), mMap, mLatLng);
                    }
                    CommonUtils.focusCurrentLocation(ll, Constants.ZOOM, mMap);
                    isFocused = true;
//                    mMap.addCircle(new CircleOptions().center(mLatLng).radius(location.getAccuracy()).fillColor(Color.parseColor("#272DB25D")).strokeWidth(0.5f).strokeColor(Color.parseColor("#272DB25D")));
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(mapRipple == null ){
//                                mapRipple = new MapRipple(mMap, mLatLng, MapsActivity.this);
//                                mapRipple.withNumberOfRipples(3);
//                                mapRipple.withFillColor(Color.parseColor("#2db25d"));
//                                mapRipple.withStrokeColor(Color.parseColor("#2db25d"));
//                                mapRipple.withStrokewidth(5);      // 10dp
//                                mapRipple.withDistance(1500);      // 2000 metres radius
//                                mapRipple.withRippleDuration(5000);    //12000ms
//                                mapRipple.withTransparency(0.8f);
//                            }else{
//                                mapRipple.withLatLng(mLatLng);
//                            }
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mapRipple.startRippleMapAnimation();
//                                }
//                            });
//                        }
//                    });
                }
                getDriverNearby(ll);

            }
        }
    };
    private Handler handler;
    private CountDownTimer timer;
    private Timer timerTask;
    private Runnable mExecute;
    private boolean doubleBackToExitPressedOnce;
    private Tooltip dialogTootltip;
    private int toolTipStep = 0;
    private Random rand = new Random();
    DrawerLayout drawer;

    protected int getCurrentIdUser() {
        String id = getmSetting().getString(Constants.ID);
        if (id == null || id.isEmpty()) {
            logoutNow();
            return -1;
        }
        return Integer.parseInt(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myTrace.start();
        getWindow().setNavigationBarColor(Color.parseColor("#20111111"));
        setContentView(R.layout.activity_maps);
        if(CommonUtils.isNavigationBarAvailable(this)){
            LogUtils.e("Has navibar");
        }
        LogUtils.e("Navi bar size : "+ CommonUtils.getSoftButtonsBarSizePort(this));
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFragmentManager = getSupportFragmentManager();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow2_16);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setNavigationIcon(null);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        this.mTvName = (CustomTextView) header.findViewById(R.id.tv_name);
        this.mAvt = (CircleImageView) header.findViewById(R.id.img_avt);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        edtFrom.setSelected(true);
        if (getIntent() != null) {
            int type = getIntent().getIntExtra(Constants.NOTIFICATION_SOCKET, -1);
            if (type == Constants.PROMOTION_TYPE) {
                startActivity(new Intent(MapsActivity.this, DiscountActivity.class));
            } else if (type == Constants.NOTIFICATION_NORMAL_TYPE) {
                startActivity(new Intent(MapsActivity.this, NotificationActivity.class));
            }
        }

        view = nhapAdd;
//        getmSetting().put(Constants.TOOLTIP_MAIN,false);

//        dialogTootltip = new Tooltip(this);
//        dialogTootltip.showDialog();
//        if (getmSetting().getString(Constants.BOOKING) != null && !getmSetting().getString(Constants.BOOKING).isEmpty()) {
//            if (NetworkUtils.isConnected()) {
//                startActivityForResult(new Intent(MapsActivity.this, ExecuteBookingActivity.class), Constants.CODE_REQ_BOOKING);
//                return;
//            } else {
////                AlertDialogC/ustom.dialogMessage(MapsActivity.this).show();
//            }
//        }
        myTrace.stop();

    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "MyriadPro-Regular.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String id = getmSetting().getString(Constants.ID);
        if (id != null && !id.isEmpty()) {
            getInfor(id);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CODE_REQ_ADDRESS) {
            if (resultCode == RESULT_OK) {
                showSnackBar("Cám ơn bạn đã sử dụng dịch vụ");

//                mAddressDest = data.getBundleExtra(Constants.BUNDLE).getParcelable(Constants.DES);
//                mAddressFrom = data.getBundleExtra(Constants.BUNDLE).getParcelable(Constants.FROM);
//                edtFrom.setText(mAddressFrom.getDescription());
//                if (mAddressDest != null && mAddressFrom != null) {
//                    Intent i = new Intent(MapsActivity.this, BookingActivity.class);
//                    Bundle b = new Bundle();
//                    b.putParcelable(Constants.DES, mAddressDest);
//                    b.putParcelable(Constants.FROM, mAddressFrom);
//                    i.putExtra(Constants.BUNDLE, b);
//                    startActivityForResult(i, Constants.CODE_REQ_BOOKING);
//                }
            }
        } else if (requestCode == Constants.CODE_REQ_BOOKING) {
            if (resultCode == RESULT_OK) {
//                bBooking = data.getBundleExtra(Constants.BUNDLE);
//                if (bBooking != null) {
//                    final MyPlace mAddressDes = bBooking.getParcelable(Constants.DES);
//                    final MyPlace mAddressFrom = bBooking.getParcelable(Constants.FROM);
//                    final int distanceCost = bBooking.getInt(Constants.COST);
//                    final double distance = bBooking.getDouble(Constants.DISTANCE);
//                    final Discount promotionCost = bBooking.getParcelable(Constants.PROMOTION);
//                    final String duration = bBooking.getString(Constants.DURATION);
//
//                    Call<ApiResponse<Integer>> createBooking = getmApi().createBooking(ApiConstants.API_KEY
//                            , getmSetting().getString(Constants.ID)
//                            , mAddressFrom.getDescription()
//                            , mAddressDes.getDescription()
//                            , distanceCost
//                            , promotionCost != null ? Integer.parseInt(promotionCost.getPr_id()) : 0
//                            , distance
//                            , duration);
//                    createBooking.enqueue(new CallBackCustom<ApiResponse<Integer>>(this, new OnResponse<ApiResponse<Integer>>() {
//                        @Override
//                        public void onResponse(ApiResponse<Integer> object) {
//                            LogUtils.e(object.getData().intValue());
//                            MainApplication.getInstance().setmIdBooking(object.getData().intValue());
//                            getmSocket().creatBooking(mAddressFrom.getDescription(), mAddressDes.getDescription()
//                                    , distanceCost, promotionCost != null ? Integer.parseInt(promotionCost.getPr_id()) : 0, distance, duration, object.getData());
//                            goixeom.com.activities.CallDriverActivity callDriverActivity = new CallDriverActivity();
////                            callDriverActivity.setArguments(bBooking);
////                            trantoTab(callDriverActivity);
//                            isCanBack = false;
//                        }
//                    }));
//                }
                showSnackBar("Cám ơn bạn đã sử dụng dịch vụ");
            } else if (resultCode == Constants.RESULT_CHANGE_TYPE_VEHICLE) {
                clearMarker();
                if (mLatLng != null)
                    MainApplication.getInstance().setType(getmSetting().getInt(Constants.TYPE_VEHICLE,0));
                    getDriverNearby(mLatLng);
            }
        } else if (requestCode == Constants.CODE_REQ_SETTING) {
            if (resultCode == Constants.RESULT_LOGOUT) {
                logoutNow();
            }
        }
    }

    private void clearMarker() {
        if (hashMapMarker != null && !hashMapMarker.isEmpty()) {
            for (Iterator<Map.Entry<Integer, LocationBearing>> it = hashMapMarker.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, LocationBearing> entry = it.next();
                if (entry.getValue() != null && entry.getValue().getMarker() != null) {
                    entry.getValue().getMarker().remove();
                }
            }
            hashMapMarker.clear();
        }
    }

    public Bundle bBooking;
    public boolean isCanBack = true;

    public void continuteBooking() {
        if (bBooking != null) {
            Intent i = new Intent(MapsActivity.this, BookingActivity.class);
            i.putExtra(Constants.BUNDLE, bBooking);
            startActivityForResult(i, Constants.CODE_REQ_BOOKING);
        }
    }

    public void trantoTab(Fragment tab) {
        mFragmentManager.beginTransaction().replace(R.id.contentview, tab).addToBackStack(null).commit();
    }

    public void trantoTabNoBackStack(Fragment tab) {
        mFragmentManager.beginTransaction().replace(R.id.contentview, tab).commit();
        mFragmentManager.popBackStack();
    }

    public void showSnackBar(String content) {
        Crouton.cancelAllCroutons();
        Style croutonStyle = new Style.Builder().setBackgroundColorValue(Color.parseColor("#25b45b")).build();
        Crouton.makeText(this, content, croutonStyle, llRoot).show();
    }

    public void removeAllTab() {
        for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
            mFragmentManager.popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!isCanBack) return;
            if (mFragmentManager.getBackStackEntryCount() > 0) {
                removeAllTab();
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Nhấn Back 2 lần thể thoát", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_saleoff) {
            startActivity(new Intent(MapsActivity.this, DiscountActivity.class));

            // Handle the camera action
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(MapsActivity.this, FAQActivity.class));

        } else if (id == R.id.nav_history) {
            startActivity(new Intent(MapsActivity.this, HistoryActivity.class));


        } else if (id == R.id.nav_notification) {
            startActivity(new Intent(MapsActivity.this, AboutUsActivity.class));


        } else if (id == R.id.nav_list_notification) {
            startActivity(new Intent(MapsActivity.this, NotificationActivity.class));


        } else if (id == R.id.nav_news) {
            startActivity(new Intent(MapsActivity.this, NewsActivity.class));
        } else if (id == R.id.nav_setting) {
            startActivityForResult(new Intent(MapsActivity.this, SettingActivity.class), Constants.CODE_REQ_SETTING);

        } else if (id == R.id.nav_view) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        KeyboardUtils.hideSoftInput(this);
        return true;
    }

    private void goHome() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
//                .getString(R.string.style_json)));
//
//        if (!success) {
//            LogUtils.e( "Style parsing failed.");
//        }
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
        mMap.setMapStyle(style);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        button = (Button) findViewById(R.id.test);
        CommonUtils.focusCurrentLocation(new LatLng(20.995004, 105.867435), 10, mMap);
        button.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mMap.setPadding(100, button.getHeight(), 100, 100);
                    }
                }
        );
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10000);
            return;
        }
        // This timer task will be executed every 1 sec.
        timerTask = new Timer();
        timerTask.schedule(new TimerTask() {
            @Override
            public void run() {
//                LogUtils.e("request driver now");
                if (mLatLng != null)
                    getDriverNearby(mLatLng);
            }
        }, 0, Constants.TIME_GET_DRIVER);
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
//                if(getmSocket()!=null)
//                    getmSocket().updateLatlng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude, getCurrentIdUser());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10000 && grantResults[0] == PackageManager.PERMISSION_GRANTED && mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String imei = PhoneUtils.getIMEI();
            getmSocket().updateNewImei(imei, MainApplication.getInstance().getmUser().getId());
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
//        if (mapRipple!=null && mapRipple.isAnimationRunning()) {
//            mapRipple.stopRippleMapAnimation();
//        }
    }

    LatLng mLatLng;
    boolean isFocused = false;

    private void addPointToViewPort(LatLng newPoint) {
        mBounds.include(newPoint);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBounds.build(),
                findViewById(R.id.test).getHeight() + 200));
    }


    private void getInfor(String id) {
        Call<ApiResponse<User>> apiGetInfo = getmApi().getInfor(ApiConstants.API_KEY, id);
        apiGetInfo.enqueue(new CallBackCustom<ApiResponse<User>>(this, new OnResponse<ApiResponse<User>>() {
            @Override
            public void onResponse(ApiResponse<User> object) {
                if (object.getData() != null
                        && object.getData().getEmail() != null && !object.getData().getEmail().isEmpty()
                        && object.getData().getPhone() != null && !object.getData().getPhone().isEmpty()
                        && object.getData().getName() != null && !object.getData().getName().isEmpty()) {

                    LogUtils.e("Updated infor user");
                    MainApplication.getInstance().setmUser(object.getData());
                    getmSetting().put(Constants.TYPE_USER_STR, object.getData().getType());
                    mTvName.setText(object.getData().getName());
                    if (object.getData().getAvatarSocial() != null && !object.getData().getAvatarSocial().isEmpty())
                        object.getData().setU_avatar(object.getData().getAvatarSocial());
                    if (object.getData().getU_avatar() != null && !object.getData().getU_avatar().isEmpty())
                        Glide.with(getApplicationContext()).load("https://" + object.getData().getU_avatar()).asBitmap().into(mAvt);
//                    else
//                        Glide.with(getApplicationContext()).load("https://" + object.getData().getU_avatar()).asBitmap().into(mAvt);

//                    if (object.getData().getChange_password() == 0 && !getmSetting().getBoolean(Constants.TYPE_PASSWORD_MAP)) {
//                        getmSetting().put(Constants.TYPE_PASSWORD_MAP, true);
//                        startActivity(new Intent(MapsActivity.this, ChangePasswordActivity.class));
//                    }

                    if (getmSocket() != null) {
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                            return;
                        }
                        String imei = PhoneUtils.getIMEI();
                        getmSocket().updateNewImei(imei, object.getData().getId());
                    }
                } else {
                    logoutNow();
                    LogUtils.e("Counld not Updated infor user");
                }
            }
        }));
    }


    @Override
    public void pingNotification(String title, String content) {

    }

    @Override
    protected void onSoketConnected() {
        if (getmSetting().getString(Constants.BOOKING) != null && !getmSetting().getString(Constants.BOOKING).isEmpty()) {
            if (NetworkUtils.isConnected()) {
                startActivityForResult(new Intent(MapsActivity.this, ExecuteBookingActivity.class), Constants.CODE_REQ_BOOKING);
            }
        }
    }

    private Handler mPostHandler = new Handler();
    long sec;
    private void getDriverNearby(LatLng location) {
        if (!NetworkUtils.isConnected()) return;
        Call<ApiResponse<List<LocationBearing>>> getDrivers = getmApi().getDriver(ApiConstants.API_KEY, location.latitude, location.longitude, MainApplication.getInstance().getType());
        getDrivers.enqueue(new CallBackCustomNoDelay<ApiResponse<List<LocationBearing>>>(this, false, new OnResponse<ApiResponse<List<LocationBearing>>>() {
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
                            int typeVehicle = MainApplication.getInstance().getType();
                            ;
                            Drawable drawable;
                            if (typeVehicle == 0) {
                                if (Build.VERSION.SDK_INT >= 20) {
                                    drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_motorcycle);
                                } else {
                                    drawable = AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_motorcycle);
                                }
                            } else {
                                if (Build.VERSION.SDK_INT >= 20) {
                                    drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_car_view_icon);
                                } else {
                                    drawable = AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_car_view_icon);
                                }
                            }
                            marker = CommonUtils.addMarker(drawable
                                    , mMap, newLatlng, rand.nextInt(360) + 1, locationBearing.getD_id() + "");
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

    public void logoutNow() {
        getmSetting().put(Constants.PHONE, "");
        getmSetting().put(Constants.ID, "");
        getmSetting().put(Constants.TYPE_PASSWORD_MAP, false);
        getmSetting().put(Constants.TYPE_USER_STR, -1);
        MainApplication.getInstance().setmUser(null);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
        startActivity(new Intent(MapsActivity.this, MainActivity.class));
        finishAffinity();
    }

    @OnClick({R.id.dang_xuat, R.id.term})
    public void onViewMenuClicked(View view) {
        Intent i = new Intent(MapsActivity.this, ActivityPrivacy.class);
        if (view.getId() == R.id.dang_xuat) {
            i.putExtra(Constants.MSG, true);
        } else {
            i.putExtra(Constants.MSG, false);

        }
        startActivity(i);
    }

    @OnClick({R.id.ll_add_from, R.id.nhap_add})
    public void onViewClicked(View view) {
        Intent i = new Intent(MapsActivity.this, SearchLocationActivity.class);
        switch (view.getId()) {
            case R.id.ll_add_from:
                i.putExtra(Constants.IS_FROM_FOCUS, false);
                break;
            case R.id.ll_add_des:
                i.putExtra(Constants.IS_FROM_FOCUS, false);
                break;
            case R.id.nhap_add:
                i.putExtra(Constants.IS_FROM_FOCUS, false);
                break;
            default: {
                i.putExtra(Constants.IS_FROM_FOCUS, false);
            }
        }
        if (mAddressDest != null) i.putExtra(Constants.DES, mAddressDest.getDescription());
        if (mAddressFrom != null) {
            i.putExtra(Constants.LAT, mAddressFrom.getLat());
            i.putExtra(Constants.LNG, mAddressFrom.getLng());
            i.putExtra(Constants.FROM, mAddressFrom.getDescription());
        }
        startActivityForResult(i, Constants.CODE_REQ_ADDRESS);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    @Override
    public void run() {
        getDriverNearby(mMap.getCameraPosition().target);
    }

    @OnClick(R.id.btn_mylocation)
    public void onViewClicked() {
        if (mLatLng != null && mMap != null)
            CommonUtils.focusCurrentLocation(mLatLng, Constants.ZOOM, mMap);

    }

    @OnClick(R.id.img_logo_toolbar)
    public void onViewMenuClicked() {
        drawer.openDrawer(Gravity.LEFT);
    }

//    class MarkerHandler implements Runnable {
//        List<LocationBearing> ListlocationBearing;
//
//        public MarkerHandler(List<LocationBearing> locationBearing) {
//            this.ListlocationBearing = locationBearing;
//        }
//
//        @Override
//        public void run() {
//            for (final LocationBearing locationBearing : ListlocationBearing) {
//                final LatLng newLatlng = new LatLng(locationBearing.getLatitude(), locationBearing.getLongitude());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (hashMapMarker.containsKey(locationBearing.getD_id()) && hashMapMarker.get(locationBearing.getD_id()) != null) {
//                            LatLng oldLatlng = new LatLng(hashMapMarker.get(locationBearing.getD_id()).getPosition().latitude, hashMapMarker.get(locationBearing.getD_id()).getPosition().longitude);
//                            if (oldLatlng.latitude == newLatlng.latitude && oldLatlng.longitude == newLatlng.longitude)
//                                return;
//                            MapUtils.animateMarkerNew(newLatlng, hashMapMarker.get(locationBearing.getD_id()), mMap, locationBearing.getAngle());
//                            MapUtils.rotateMarker(hashMapMarker.get(locationBearing.getD_id()), 360 - MapUtils.bearingBetweenLocations(oldLatlng, newLatlng));
//
//                        } else {
//                            Marker marker = CommonUtils.addMarker(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_motorcycle), mMap, newLatlng);
//                            hashMapMarker.put(locationBearing.getD_id(), marker);
//                        }
//                    }
//                });
//            }
//
//            // locationBearing.notifyAnimation(MapsActivity.this, mMap, (int) sec);
//
//
//            sec++;
//            mPostHandler.postDelayed(mExecute, 3);
//
//        }
//    }

    class GeoTask extends AsyncTask<LatLng, Void, String> {
        private Animation anim;

        GeoTask() {
        }

        protected String doInBackground(LatLng... latLngs) {
            LatLng latLng = latLngs[0];
            if (mAddressFrom == null) mAddressFrom = new MyPlace();
            mAddressFrom.setLng(latLng.longitude);
            mAddressFrom.setLat(latLng.latitude);
            return CommonUtils.getCompleteAddressString(getApplicationContext(), latLng.latitude, latLng.longitude);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            edtFrom.setText(s);
            mAddressFrom.setDescription(s);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter i = new IntentFilter();
        i.addAction(GPSService.ACTION_LOCATION_UPDATE);
        registerReceiver(receiver, i);
        startService(new Intent(this, GPSService.class));
        timerTask = new Timer();
        timerTask.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mLatLng != null)
                    getDriverNearby(mLatLng);
            }
        }, 0, Constants.TIME_GET_DRIVER);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        timerTask.cancel();
        stopService(new Intent(this, GPSService.class));
    }
}
