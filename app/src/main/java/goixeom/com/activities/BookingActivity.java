package goixeom.com.activities;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import carbon.widget.Button;
import goixeom.com.CustomTextView;
import goixeom.com.MainApplication;
import goixeom.com.R;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.apis.CallBackCustomNoDelay;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.maputils.DirectionCustomModel;
import goixeom.com.maputils.DirectionsJSONParser;
import goixeom.com.maputils.MapAnimator;
import goixeom.com.models.BookingTrip;
import goixeom.com.models.Discount;
import goixeom.com.models.MyPlace;
import goixeom.com.models.Price;
import goixeom.com.socket.LocationBearing;
import goixeom.com.utils.CommonUtils;
import goixeom.com.utils.Constants;
import goixeom.com.utils.FileUtils;
import retrofit2.Call;

public class BookingActivity extends BaseActivity implements OnMapReadyCallback {
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.tv_time)
    CustomTextView tvTime;
    @BindView(R.id.tv_distance)
    CustomTextView tvDistance;

    @BindView(R.id.tv_type_cash)
    CustomTextView tvTypeCash;
    @BindView(R.id.tv_prizepetrol)
    CustomTextView tvPrizepetrol;
    @BindView(R.id.call)
    Button call;
    @BindView(R.id.img_back)
    ImageView imgBack;
    //    @BindView(R.id.tv_discount_booking)
//    CustomTextView tvDiscountBooking;

    @BindView(R.id.ll_go_root)
    RelativeLayout llGoRoot;
    @BindView(R.id.tv_add_marker)
    CustomTextView tvDialogAddressFrom;
    @BindView(R.id.tv_time_marker)
    CustomTextView tvDialogTimeFrom;
    @BindView(R.id.tv_add_marker_des)
    CustomTextView tvAddMarkerDes;
    @BindView(R.id.tv_price_marker_des)
    CustomTextView tvPriceMarkerDes;
    @BindView(R.id.ll_des_root)
    RelativeLayout llDesRoot;
    @BindView(R.id.rad_car)
    RadioButton radCar;
    @BindView(R.id.rad_bike)
    RadioButton radBike;
    @BindView(R.id.rad_group)
    RadioGroup radGroup;
    @BindView(R.id.tv_prizepetrol_time_update)
    CustomTextView tvPrizepetrolTimeUpdate;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.ll_promotion_booking)
    LinearLayout llPromotionBooking;
    @BindView(R.id.discount)
    ImageView discount;
    @BindView(R.id.img_couponn)
    ImageView imgCouponn;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.ll_root)
    RelativeLayout llRoot;

    private CustomTextView txt_bike;
    private MyPlace mAddressFrom;
    private MyPlace mAddressDes;
    private GoogleMap mMap;
    private DirectionCustomModel directionCustomModel;
    private Discount promotionCost;
    private Price distanceCost;
    private Handler handler = new Handler();
    Trace myTrace = FirebasePerformance.getInstance().newTrace("booking");
    private Point pFrom;
    private Point pDes;
    private boolean isFirstTimeDialogFrom = true;
    private boolean isFirstTimeDialogDes = true;
    private Timer timerTask;
    HashMap<Integer, LocationBearing> hashMapMarker = new HashMap<>();
    private Random rand = new Random();
    private String typeAddress = "";


    @Override
    public void pingNotification(String title, String content) {

    }

    @Override
    protected void onSoketConnected() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.PROMOTION, promotionCost);
        outState.putParcelable(Constants.DES, mAddressDes);
        outState.putParcelable(Constants.FROM, mAddressFrom);
    }

    float density;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

        density = getResources().getDisplayMetrics().densityDpi;
        LogUtils.e(density);
//        view = llRoot;
        imgCouponn.setEnabled(false);
        llPromotionBooking.setClickable(true);
        typeVehicle = getmSetting().getInt(Constants.TYPE_VEHICLE, 0);
        if (typeVehicle == 0) {
            radBike.setChecked(true);
        } else {
            radCar.setChecked(true);
        }
        discount = (ImageView) findViewById(R.id.discount);
        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, DiscountActivity.class);
                intent.putExtra(Constants.TYPE_VEHICLE, typeVehicle);
                startActivityForResult(intent, Constants.CODE_REQ_PROMO);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getIntent() != null) {
            Bundle b = getIntent().getBundleExtra(Constants.BUNDLE);
            mAddressDes = b.getParcelable(Constants.DES);
            mAddressFrom = b.getParcelable(Constants.FROM);
            typeAddress = b.getParcelable(Constants.TYPE_ADDRESS);
            if (mAddressDes != null && mAddressDes.getDescription() != null) {
                mAddressDes.setDescription(mAddressDes.getDescription().replace(", Vietnam", "").replace(", Việt Nam", "").replace(", ViệtNam", "").replace(", Viet Nam", ""));
            }
            if (mAddressFrom != null && mAddressFrom.getDescription() != null) {
                mAddressFrom.setDescription(mAddressFrom.getDescription().replace(", Vietnam", "").replace(", Việt Nam", "").replace(", ViệtNam", "").replace(", Viet Nam", ""));
            }
//            mAddressFrom.setLat(mAddressFrom.getLat()-0.001);
//            mAddressFrom.setLng(mAddressFrom.getLng()+0.001);
//            Location.distanceBetween(m);
        }
        getCostOils();
        tvTime.setSelected(true);
        tvDistance.setSelected(true);
        tvTypeCash.setSelected(true);
        tvDialogAddressFrom.setSelected(true);
        tvAddMarkerDes.setSelected(true);
        if (savedInstanceState != null) {
            promotionCost = savedInstanceState.getParcelable(Constants.PROMOTION);
            mAddressDes = savedInstanceState.getParcelable(Constants.DES);
            mAddressFrom = savedInstanceState.getParcelable(Constants.FROM);
        }
        if (promotionCost != null)
            bindingPromotionToView();
        getPromotionFirstTime(typeVehicle);
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rad_bike) {
                    typeVehicle = 0;
                } else {
                    typeVehicle = 1;
                }
                getPromotionFirstTime(typeVehicle);
                getmSetting().put(Constants.TYPE_VEHICLE, typeVehicle);
                MainApplication.getInstance().setType(typeVehicle);
//                tvPriceMarkerDes.setText(getTotalCost(typeVehicle) / 1000 + "k");
                setResult(Constants.RESULT_CHANGE_TYPE_VEHICLE);
                if (mAddressFrom != null) {
                    clearMarker();
                    getDriverNearby(new LatLng(mAddressFrom.getLat(), mAddressFrom.getLng()));
                }
            }
        });
    }

    private void getPromotionFirstTime(int type) {
        Call<ApiResponse<List<Discount>>> getPromotions = getmApi().getPromotions(ApiConstants.API_KEY, getmSetting().getString(Constants.ID), type);
        getPromotions.enqueue(new CallBackCustom<ApiResponse<List<Discount>>>(this, new OnResponse<ApiResponse<List<Discount>>>() {
            @Override
            public void onResponse(ApiResponse<List<Discount>> object) {
                loading.setVisibility(View.GONE);
                if (object.getData() != null && object.getData().size() > 0) {
                    discount.setImageResource(R.drawable.ic_active_coupon);
                } else {
                    discount.setImageResource(R.drawable.ic_no_coupon_booking);
                }
            }
        }));
    }

    int typeVehicle = 0;
    Marker markerDes;
    Marker markerfrom;
    Marker markerDotFrom;

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

    public void showMarker(LatLng latLng, String address, String time, boolean isFrom) {
        if (isFrom) {
            if (markerfrom != null)
//                markerfrom.setIcon(BitmapDescriptorFactory.fromBitmap(createFromMarker(address, time)));
                createFromMarker(address, time);
            else
//                markerfrom = mMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .icon(BitmapDescriptorFactory.fromBitmap(createFromMarker(address, time))));
                if (Build.VERSION.SDK_INT >= 20) {
                    markerfrom = CommonUtils.addMarker(getResources().getDrawable(R.drawable.ic_here_pin, null), mMap, latLng);
                } else {
                    markerfrom = CommonUtils.addMarker(AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_here_pin), mMap, latLng);
                }
        } else {
            if (markerDes != null)
//                markerDes.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromViewAddressDes(address, time)));
                createDrawableFromViewAddressDes(address, time);
            else
//                markerDes = mMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromViewAddressDes(address, time))));
                if (Build.VERSION.SDK_INT >= 20) {
                    markerDes = CommonUtils.addMarker(getResources().getDrawable(R.drawable.ic_shape, null), mMap, latLng);

                } else {
                    markerDes = CommonUtils.addMarker(AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_shape), mMap, latLng);
                }
        }
    }


    private void setOverlayPosition(LatLng latLng) {
        Point p = mMap.getProjection().toScreenLocation(latLng);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.size_w_dialog), (int) getResources().getDimension(R.dimen.size_h_dialog));
        llGoRoot.setTranslationX(p.x);
        llGoRoot.setTranslationY((float) (p.y));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = 2 * size.y / 3;
        LogUtils.e("X-Y ->>>>>>>>>>: " + (p.x + llGoRoot.getWidth()) + "-----" + p.y + "---SCREENSIZE----" + width + "-----" + height);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) llGoRoot.getLayoutParams();
        int totalWidth = (p.x + llGoRoot.getWidth());
        int totalHeight = (p.y + llGoRoot.getHeight());
        int mL, mT, mR, mB;
        TranslateAnimation anim = null;
        if (isFirstTimeDialogFrom) {
            mL = 10;
            mT = (int) getResources().getDimension(R.dimen.margin_top_dialog_booking);
            mR = (int) getResources().getDimension(R.dimen.margin_bottom_dialog_booking);
            mB = 10;
            isFirstTimeDialogFrom = false;
        } else {
            mL = lp.leftMargin;
            mT = lp.topMargin;
            mR = lp.rightMargin;
            mB = lp.bottomMargin;
        }
        if (totalWidth >= width) {
            //animate to left
            if (density > 320 && density < 480) {
                mL = (int) getResources().getDimension(R.dimen.margin_left_420_dialog_booking);
            } else {
                mL = (int) getResources().getDimension(R.dimen.margin_left_dialog_booking);
            }
            mR = 10;
        } else if (p.x < llGoRoot.getWidth()) {
            //animate to right
            mL = (int) getResources().getDimension(R.dimen.margin_left_right_dialog_booking);
            mR = (int) getResources().getDimension(R.dimen.margin_right_dialog_booking);
        }
        if (totalHeight >= height - (int) getResources().getDimension(R.dimen.size_h_compare_booking)) {
            //animate to top
            mT = (int) getResources().getDimension(R.dimen.margin_top_dialog_booking);
            mB = 10;

        } else if (p.y < (llGoRoot.getHeight() * 2)) {
            //animate to bottom
            mT = (int) getResources().getDimension(R.dimen.margin_bottom_dialog_booking);
            mB = 0;
        }
        params.setMargins(mL, mT, mR, mB);
//        llGoRoot.setLayoutParams(params);
        llGoRoot.setVisibility(View.VISIBLE);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", lp.topMargin, mT);
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", lp.leftMargin, mL);
        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(pvhLeft, pvhTop);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.topMargin = (Integer) valueAnimator.getAnimatedValue("top");
                params.leftMargin = (Integer) valueAnimator.getAnimatedValue("left");
                llGoRoot.setLayoutParams(params);
            }
        });
        animator.setDuration(300);
        animator.start();
    }

    private void setOverlayPositionDes(LatLng latLng) {
        final Point p = mMap.getProjection().toScreenLocation(latLng);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.size_w_dialog), (int) getResources().getDimension(R.dimen.size_h_dialog));
        llDesRoot.setTranslationX(p.x);
        llDesRoot.setTranslationY((float) (p.y));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = 2 * size.y / 3;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) llDesRoot.getLayoutParams();
        int totalWidth = (p.x + llDesRoot.getWidth());
        int totalHeight = (p.y + llDesRoot.getHeight());
        int mL, mT, mR, mB;
        TranslateAnimation anim = null;
        if (isFirstTimeDialogDes) {
            if (density > 320 && density < 480) {
                mL = (int) getResources().getDimension(R.dimen.margin_left_420_dialog_booking);
            } else {
                mL = (int) getResources().getDimension(R.dimen.margin_left_dialog_booking);
            }
            mT = (int) getResources().getDimension(R.dimen.margin_top_dialog_booking);
            mR = 10;
            mB = 10;
            isFirstTimeDialogDes = false;
        } else {
            mL = lp.leftMargin;
            mT = lp.topMargin;
            mR = lp.rightMargin;
            mB = lp.bottomMargin;
        }
        if (totalWidth >= width) {
//            //animate to left
            if (density > 320 && density < 480) {
                mL = (int) getResources().getDimension(R.dimen.margin_left_420_dialog_booking);
            } else {
                mL = (int) getResources().getDimension(R.dimen.margin_left_dialog_booking);
            }
//            mR = 10;
        } else if (p.x < llDesRoot.getWidth()) {
            //animate to right
            mL = (int) getResources().getDimension(R.dimen.margin_left_right_dialog_booking);
            mR = (int) getResources().getDimension(R.dimen.margin_right_dialog_booking);
        }
        if (totalHeight >= height - (int) getResources().getDimension(R.dimen.size_h_compare_booking)) {
            //animate to top
            mT = (int) getResources().getDimension(R.dimen.margin_top_dialog_booking);
            mB = 10;

        } else if (p.y <= (llGoRoot.getHeight() * 2)) {
            //animate to bottom
            mT = (int) getResources().getDimension(R.dimen.margin_bottom_dialog_booking);
            mB = 10;
        }
        params.setMargins(mL, mT, mR, mB);
//        llDesRoot.setLayoutParams(params);
        llDesRoot.setVisibility(View.VISIBLE);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", lp.topMargin, mT);
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", lp.leftMargin, mL);
        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(pvhLeft, pvhTop);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.topMargin = (Integer) valueAnimator.getAnimatedValue("top");
                params.leftMargin = (Integer) valueAnimator.getAnimatedValue("left");
//                params.width = 250;
//                params.height=90;
                llDesRoot.setLayoutParams(params);
                LogUtils.e("X-Y ->>>>>>>>>>: " + (p.x + llDesRoot.getWidth()) + "-----" + p.y + "---SCREENSIZE----" + params.width + "-----" + params.height);

            }
        });
        animator.setDuration(300);
        animator.start();
    }

    private void getCostOils() {
        Call<ApiResponse<Integer>> getCost = getmApi().getConfig(ApiConstants.API_KEY);
        getCost.enqueue(new CallBackCustom<ApiResponse<Integer>>(this, new OnResponse<ApiResponse<Integer>>() {
            @Override
            public void onResponse(ApiResponse<Integer> object) {
                if (object.getData() != null) {
                    int date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    tvPrizepetrol.setText(String.format("%s đ/lít", NumberFormat.getNumberInstance(Locale.GERMAN).format(object.getData().intValue()), date + "-" + month + "-" + year));
                    tvPrizepetrolTimeUpdate.setText(String.format("Cập nhật ngày %s", date + "-" + month + "-" + year));
                    tvPrizepetrolTimeUpdate.setSelected(true);
                }
            }
        }));
    }

    // Convert a view to bitmap
    CustomTextView tvAddressMarker;
    CustomTextView tvTimeMarker;


    public void createFromMarker(String address, String time) {
        time = time.replace("mins", "m");
        time = time.replace("hour", "h");
        time = time.replace(" ", "");
        try {
            tvDialogAddressFrom.setText(address.substring(0, address.indexOf(",")));
        } catch (Exception e) {
            e.printStackTrace();
            tvDialogAddressFrom.setText(address);
        }
        tvDialogTimeFrom.setText(time);
        llGoRoot.setVisibility(View.GONE);

    }


    public void createDrawableFromViewAddressDes(String address, String time) {
        try {
            tvAddMarkerDes.setText(address.substring(0, address.indexOf(",")));
        } catch (Exception e) {
            tvAddMarkerDes.setText(address);
            e.printStackTrace();
        }
        tvPriceMarkerDes.setText(time);
        llDesRoot.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        call.setEnabled(true);

    }

    @Override
    public void onBackPressed() {
        setResult(Constants.RESULT_CHANGE_TYPE_VEHICLE);
        super.onBackPressed();
    }

    @OnClick({R.id.ll_promotion_booking, R.id.img_close, R.id.call, R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_promotion_booking:
                Intent intent = new Intent(BookingActivity.this, InputCodeActivity.class);
                intent.putExtra(Constants.TYPE_VEHICLE, typeVehicle);
                startActivityForResult(intent, Constants.CODE_REQ_PROMO);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;
            case R.id.img_close:
                onBackPressed();
                break;
            case R.id.call: {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(2);
                if (directionCustomModel == null || distanceCost==null) {
                    showDialogErrorContent("Đang tính toán cước phí. Vui lòng chờ...");
                    return;
                }
                if (getmSocket() == null) {
                    showDialogErrorContent("Không thể đặt chuyến lúc này. Vui lòng thử lại sau");
                    return;
                }
                int typeVichel = getmSetting().getInt(Constants.TYPE_VEHICLE, 0);
                int cost = 0;
                if (typeVichel == 0) {
                    cost = distanceCost.getBike();
                } else {
                    cost = distanceCost.getCar();
                }
                final double distance = (double) directionCustomModel.getDistance() / 1000;
                getDialogProgress().showDialog();
                myTrace.start();
                typeAddress = mAddressDes.getType();
                Call<ApiResponse<BookingTrip>> createBooking = getmApi().createBooking(ApiConstants.API_KEY
                        , getmSetting().getString(Constants.ID)
                        , mAddressFrom.getDescription()
                        , mAddressDes.getDescription()
                        , cost
                        , promotionCost != null ? Integer.parseInt(promotionCost.getPr_id()) : 0
                        , distance
                        , directionCustomModel.getDuration()
                        , mAddressFrom.getLat()
                        , mAddressDes.getLng()
                        , mAddressDes.getLat()
                        , mAddressDes.getLng()
                        , typeAddress == null ? "" : typeAddress
                        , typeVichel
                        , mAddressDes.getDescription().substring(0, mAddressDes.getDescription().indexOf(","))
                );
                call.setEnabled(false);
                MainApplication.getInstance().setRequestBooking(createBooking);
                final int finalCost = cost;
                createBooking.enqueue(new CallBackCustomNoDelay<ApiResponse<BookingTrip>>(this, getDialogProgress(), new OnResponse<ApiResponse<BookingTrip>>() {
                    @Override
                    public void onResponse(ApiResponse<BookingTrip> object) {
                        myTrace.stop();
                        Intent intent = new Intent(BookingActivity.this, ExecuteBookingActivity.class);
                        Bundle b = new Bundle();
                        LogUtils.e(object.getData().getT_id());
                        MainApplication.getInstance().setTimeOut(false);
                        MainApplication.getInstance().setmIdBooking(object.getData().getT_id());
                        if (object.getData().getList_driver() != null && object.getData().getList_driver().size() != 0) {
                        } else {
                            b.putBoolean(Constants.NEED_TO_REQUEST_LIST_DRIVER, true);
                        }
                        b.putParcelable(Constants.FROM, mAddressFrom);
                        b.putParcelable(Constants.DES, mAddressDes);
                        b.putInt(Constants.COST, finalCost);
                        b.putParcelable(Constants.PROMOTION, promotionCost);
                        b.putDouble(Constants.DISTANCE, (double) directionCustomModel.getDistance() / 1000);
                        b.putString(Constants.DURATION, directionCustomModel.getDuration());
                        intent.putExtra(Constants.BUNDLE, b);
                        LogUtils.e("start now");
                        try {
                            startActivityForResult(intent, Constants.CODE_REQ_BOOKING);
                        } catch (Exception e) {
                            LogUtils.e(e.getMessage());
                        }
                        LogUtils.e("start end");

                    }
                }));
                break;
            }
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    private int getTotalCost(int type) {
        if(distanceCost == null) {
            finish();
        }
        int cost = 0;
        if (type == 0) {
            cost = distanceCost.getBike();
        } else {
            cost = distanceCost.getCar();
        }
        if (promotionCost == null) return cost;
        int total = cost - Integer.parseInt(promotionCost.getValue());
        return total < 0 ? 0 : total;
    }

    private void getDistanceCost(double distance) {
        Call<ApiResponse<Price>> getCost = getmApi().getDistanceCost(ApiConstants.API_KEY, distance);
        getCost.enqueue(new CallBackCustom<ApiResponse<Price>>(this, new OnResponse<ApiResponse<Price>>() {
            @Override
            public void onResponse(ApiResponse<Price> object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS && object.getData() != null) {
                    loading.setVisibility(View.GONE);
                    distanceCost = object.getData();
                    radBike.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(getTotalCost(0)) + " vnđ");
                    radCar.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(getTotalCost(1)) + " vnđ");
                    LatLng latLngDes = new LatLng(mAddressDes.getLat(), mAddressDes.getLng());
                    showMarker(latLngDes, "      " + mAddressDes.getDescription(), getTotalCost(typeVehicle) / 1000 + "k", false);
                    call.setEnabled(true);
                } else {
                    loading.setVisibility(View.GONE);
                    call.setEnabled(false);
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    List<LatLng> latLngs;
    private boolean isFirstTimeFocusWhenUserTouch = true;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            pathName = FileUtils.getFolder(BookingActivity.this) + getString(R.string.bike);
            dBike = Drawable.createFromPath(pathName);
            dBike = CommonUtils.resize(dBike, this);
            pathName = FileUtils.getFolder(BookingActivity.this) + getString(R.string.car);
            dCar = Drawable.createFromPath(pathName);
            dCar = CommonUtils.resizeCar(dCar, this);


            mMap.getUiSettings().setRotateGesturesEnabled(false);
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
            mMap.setMapStyle(style);
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    Point p = mMap.getProjection().toScreenLocation(markerDes.getPosition());
                    final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.size_w_dialog), (int) getResources().getDimension(R.dimen.size_h_dialog));
                    params.setMargins((int) getResources().getDimension(R.dimen.margin_left_dialog_booking), (int) getResources().getDimension(R.dimen.margin_top_dialog_booking), 10, 10);
                    llDesRoot.setTranslationX(p.x);
                    llDesRoot.setTranslationY((float) (p.y));

                    Point pFrom = mMap.getProjection().toScreenLocation(markerfrom.getPosition());
                    final RelativeLayout.LayoutParams paramspFrom = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.size_w_dialog), (int) getResources().getDimension(R.dimen.size_h_dialog));
                    params.setMargins(10, (int) getResources().getDimension(R.dimen.margin_top_dialog_booking), (int) getResources().getDimension(R.dimen.margin_bottom_dialog_booking), 10);
                    llGoRoot.setTranslationX(pFrom.x);
                    llGoRoot.setTranslationY((float) (pFrom.y));

                }
            });
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    if (markerfrom != null && markerDes != null && !tvDialogAddressFrom.getText().toString().isEmpty()) {
                        setOverlayPosition(markerfrom.getPosition());
                        setOverlayPositionDes(markerDes.getPosition());
                    }

                    if (isFirstTimeFocusWhenUserTouch && latLngs != null && !latLngs.isEmpty()) {
                        isFirstTimeFocusWhenUserTouch = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CommonUtils.focusAllMarkers(latLngs, mMap, BookingActivity.this);
                                isFirstTimeFocusWhenUserTouch = true;
                            }
                        }, 2500);
                    }
                }
            });

            if (mAddressFrom != null) {
                final LatLng latLngFrom = new LatLng(mAddressFrom.getLat(), mAddressFrom.getLng());
                LatLng latLngDes = new LatLng(mAddressDes.getLat(), mAddressDes.getLng());

                showMarker(latLngDes, mAddressDes.getDescription(), "", false);
                showMarker(latLngFrom, mAddressFrom.getDescription(), "", true);
                CommonUtils.focusCurrentLocation(latLngFrom, 15, mMap);

                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        if (mAddressDes != null && mAddressFrom != null) {
                            mMap.getUiSettings().setRotateGesturesEnabled(false);
                            mMap.getUiSettings().setCompassEnabled(false);
                            LatLng latLngDes = new LatLng(mAddressDes.getLat(), mAddressDes.getLng());
//                CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_end_dirc, null), mMap, latLngDes);
//                CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dialog_svg, null), mMap, latLngFrom);


//                if (latLngFrom.latitude < latLngDes.latitude) { //below
//                    showMarker(new LatLng(latLngDes.latitude, latLngDes.longitude));
//                } else {  //Above
//                    showMarker(new LatLng(latLngDes.latitude, latLngDes.longitude));
//                }
                            latLngs = new ArrayList<>();
                            latLngs.add(latLngDes);
                            latLngs.add(latLngFrom);
//                        mMap.setPadding(200, 0, 200, 600);
//                        CommonUtils.focusAllMarkers(latLngs, mMap, BookingActivity.this);
                            String url = getDirectionsUrl(latLngFrom, latLngDes);
                            DownloadTask downloadTask = new DownloadTask();
                            downloadTask.execute(url);

                            timerTask = new Timer();
                            timerTask.schedule(new TimerTask() {
                                @Override
                                public void run() {
//                LogUtils.e("request driver now");
                                    if (latLngFrom != null)
                                        getDriverNearby(latLngFrom);
                                }
                            }, 0, getmSetting().getLong(Constants.TIME_UPDATE,Constants.TIME_GET_DRIVER));
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CODE_REQ_PROMO && resultCode == RESULT_OK && data != null) {
            Bundle b = data.getBundleExtra(Constants.BUNDLE);
            promotionCost = b.getParcelable(Constants.PROMOTION_VALUE);
            bindingPromotionToView();
            if (promotionCost.getTypePr() == 0)
                radBike.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(getTotalCost(0)) + " vnđ");
            else if (promotionCost.getTypePr() == 1)
                radCar.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(getTotalCost(1)) + " vnđ");
            LatLng latLngDes = new LatLng(mAddressDes.getLat(), mAddressDes.getLng());
            showMarker(latLngDes, "      " + mAddressDes.getDescription(), getTotalCost(typeVehicle) / 1000 + "k", false);
            CommonUtils.enable(llPromotionBooking);
            llPromotionBooking.setClickable(false);
            imgCouponn.setEnabled(true);
            return;
        }
        if (requestCode == Constants.CODE_REQ_BOOKING && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void bindingPromotionToView() {
//        tvDiscountBooking.setText(R.string.txt_promotion_valid);
//        tvDiscountBooking.setTextColor(Color.RED);
//        discount.setClickable(false);
    }

    //    Polyline
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_map_id);

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    /**
     * A class to download data from Google Directions URL
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Directions in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                directionCustomModel = parser.parse(jObject);
                String duration = directionCustomModel.getDuration().replace("min", "phút");
                duration.replace("hour", "giờ");
                duration.replace("s", "");
                duration.replace(" ", "");
                routes = directionCustomModel.getMapPolyline();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            if (result == null) return;
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                points.add(new LatLng(mAddressFrom.getLat(), mAddressFrom.getLng()));
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    latLngs.add(position);
                    points.add(position);
                }
                points.add(new LatLng(mAddressDes.getLat(), mAddressDes.getLng()));
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width((int) getResources().getDimension(R.dimen.poliline__default_width));
//                lineOptions.color(ContextCompat.getColor(BookingActivity.this, R.color.green));
                lineOptions.color(Color.BLACK);
            }

            // Drawing polyline in the Google Map for the i-th route
//            mMap.cl ear();
//            if (lineOptions != null) mMap.addPolyline(lineOptions);
//            else showDialogErrorContent("Hệ thống chưa xác định được đường đi tới địa điểm này");
            getDistanceCost((double) directionCustomModel.getDistance() / 1000);
            tvDistance.setText(Html.fromHtml(String.format(getString(R.string.distance_booking_format), directionCustomModel.getDistanceTxt())));
            tvTime.setText(directionCustomModel.getDuration().replace("day", "ngày").replace("min", "phút").replace("hour", "giờ").replace("s", ""));
//            tvTimeMarker.setText(directionCustomModel.getDuration());
            LatLng latLngFrom = new LatLng(mAddressFrom.getLat(), mAddressFrom.getLng());
            showMarker(latLngFrom, mAddressFrom.getDescription(), directionCustomModel.getDuration(), true);
            if (directionCustomModel.getDistance() > 0)
                call.setEnabled(true);
            PolylineOptions optionsBackground = new PolylineOptions().add().width(10);
            if (lineOptions == null) return;
//            if(getmSetting().getBoolean(Constants.DIRECTION,true)){
                lines = mMap.addPolyline(lineOptions);
                startAnim(points, lines);
//            }
            final ArrayList<LatLng> finalPoints = points;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CommonUtils.focusAllMarkers(latLngs, mMap, BookingActivity.this);
                }
            }, 500);
        }
    }
    String pathName;
    Drawable dBike;
    Drawable dCar;
    private void getDriverNearby(LatLng location) {
        if (!NetworkUtils.isConnected()) return;
        int type = getmSetting().getInt(Constants.TYPE_VEHICLE, 0);
        Call<ApiResponse<List<LocationBearing>>> getDrivers = getmApi().getDriver(ApiConstants.API_KEY, location.latitude, location.longitude, type);
        getDrivers.enqueue(new CallBackCustomNoDelay<ApiResponse<List<LocationBearing>>>(this, false, new OnResponse<ApiResponse<List<LocationBearing>>>() {
            @Override
            public void onResponse(final ApiResponse<List<LocationBearing>> object) {
                if (object.getData() != null && object.getData().size() > 0) {
                    for (Iterator<Map.Entry<Integer, LocationBearing>> it = hashMapMarker.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<Integer, LocationBearing> entry = it.next();
                        int key = entry.getKey().intValue();
                        boolean isExist = false;
                        for (LocationBearing locationBearing : object.getData()) {
                            LatLng newLatlng = new LatLng(locationBearing.getLatitude(), locationBearing.getLongitude());
                            if (locationBearing.getD_id() == key && entry.getValue() != null) {
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
                            Drawable drawable = null;
                            if (typeVehicle == 0) {
//                                if (Build.VERSION.SDK_INT >= 20) {
//                                    drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_motorcycle);
//                                } else {
//                                    drawable = AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_motorcycle);
//                                }
                                drawable =  dBike;

                            } else {
//                                if (Build.VERSION.SDK_INT >= 20) {
//                                    drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_car_view_icon);
//                                } else {
//                                    drawable = AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_car_view_icon);
//                                }

                                drawable =  dCar;
                            }
                            marker = CommonUtils.addMarker(drawable, mMap, newLatlng, rand.nextInt(360) + 1, locationBearing.getD_id() + "");
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

    Polyline lines;

    private void startAnim(List<LatLng> bangaloreRoute, Polyline lines) {
        if (mMap != null) {
            MapAnimator.getInstance(this).animateRoute(mMap, bangaloreRoute, lines);
        } else {
            Toast.makeText(getApplicationContext(), "Map not ready", Toast.LENGTH_LONG).show();
        }
    }
}
