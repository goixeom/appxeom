package goixeom.com.activities;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.Html;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.eralp.circleprogressview.CircleProgressView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import carbon.widget.Button;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import goixeom.com.CustomTextView;
import goixeom.com.MainApplication;
import goixeom.com.R;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.ApiUtils;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.apis.CallBackCustomNoDelay;
import goixeom.com.apis.GoiXeOmAPI;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.maputils.DirectionCustomModel;
import goixeom.com.models.BookingTrip;
import goixeom.com.models.Discount;
import goixeom.com.models.MyPlace;
import goixeom.com.models.Routes;
import goixeom.com.models.TripInforModel;
import goixeom.com.models.User;
import goixeom.com.socket.LocationBearing;
import goixeom.com.socket.SocketConstants;
import goixeom.com.socket.SocketResponse;
import goixeom.com.utils.CommonUtils;
import goixeom.com.utils.Constants;
import goixeom.com.utils.FileUtils;
import retrofit2.Call;

public class ExecuteBookingActivity extends BaseActivity
        implements OnMapReadyCallback {
    @BindView(R.id.maker_imb)
    ImageButton makerImb;
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.img_avt)
    RoundedImageView imgAvt;
    @BindView(R.id.tv_rate_driver)
    CustomTextView tvRateDriver;
    @BindView(R.id.tv_name_driver)
    CustomTextView tvNameDriver;
    @BindView(R.id.tv_bike)
    CustomTextView tvBike;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.tv_bike_name)
    CustomTextView tvBikeName;
    @BindView(R.id.tv_from)
    CustomTextView tvFrom;
    @BindView(R.id.tv_des)
    CustomTextView tvDes;

    @BindView(R.id.tv_total_cost)
    CustomTextView tvTotalCost;

    @BindView(R.id.ll_root_dialog_booking)
    RelativeLayout llRootDialog;
    Unbinder unbinder;
    @BindView(R.id.content)
    RippleBackground content;

    @BindView(R.id.ll_root_cancel_booking)
    LinearLayout llRootCancelBooking;
    @BindView(R.id.tv_time)
    CustomTextView tvTime;
    @BindView(R.id.img_call)
    ImageView imgCall;
    @BindView(R.id.img_avt_bottom)
    CircleImageView imgAvtBottom;
    @BindView(R.id.tv_rate_driver_bottom)
    CustomTextView tvRateDriverBottom;
    @BindView(R.id.tv_driver_name_bottom)
    CustomTextView tvDriverNameBottom;
    @BindView(R.id.tv_code_bike_bottom)
    CustomTextView tvCodeBikeBottom;
    @BindView(R.id.rating_bar_bottom)
    RatingBar ratingBarBottom;
    @BindView(R.id.ll_root_time_booking_bottom)
    LinearLayout llRootTimeBookingBottom;
    @BindView(R.id.ll_from)
    LinearLayout llFrom;
    @BindView(R.id.tv_cancel)
    CustomTextView tvCancle;
    @BindView(R.id.ll_cancle)
    RelativeLayout llCancle;


    @BindView(R.id.rating_bar_driver)
    RatingBar ratingBarDriver;


    @BindView(R.id.contentview)
    RelativeLayout llRoot;
    @BindView(R.id.progress)
    CircleProgressView progressBooking;
    @BindView(R.id.tv_sentto_driver)
    CustomTextView tvSenttoDriver;
    @BindView(R.id.tv_ok)
    CustomTextView tvOk;
    @BindView(R.id.img_infor)
    ImageView imgInfor;
    @BindView(R.id.tv_name_driver_vote)
    CustomTextView tvNameDriverVote;


    @BindView(R.id.rad_funy)
    RadioButton radFuny;
    @BindView(R.id.rad_pro)
    RadioButton radPro;
    @BindView(R.id.rad_right_rule)
    RadioButton radRightRule;
    @BindView(R.id.rad_group)
    RadioGroup radGroup;
    @BindView(R.id.btn_vote)
    Button btnVote;
    @BindView(R.id.img_avt_vote)
    RoundedImageView imgAvtVote;


    @BindView(R.id.tv_add_from_info)
    TextView tvAddFromInfo;
    @BindView(R.id.tv_add_des_info)
    TextView tvAddDesInfo;
    @BindView(R.id.ll_diver_bottom)
    LinearLayout llDiverBottom;
    @BindView(R.id.ll_add_bottom)
    LinearLayout llAddBottom;
    @BindView(R.id.tv_add_from_call_booking)
    CustomTextView tvAddFromCallBooking;
    @BindView(R.id.tv_add_des_call_booking)
    CustomTextView tvAddDesCallBooking;
    @BindView(R.id.tv_distance_call_booking)
    CustomTextView tvDistanceCallBooking;
    @BindView(R.id.llcontent)
    LinearLayout llcontent;
    @BindView(R.id.tv_date_dialog_booking)
    CustomTextView tvDateDialogBooking;
    @BindView(R.id.tv_time_dialog_booking)
    CustomTextView tvTimeDialogBooking;
    @BindView(R.id.tv_number_seat)
    CustomTextView tvNumberSeat;
    @BindView(R.id.tv_ms)
    CustomTextView tvMs;
    @BindView(R.id.ll_map)
    RelativeLayout llMap;
    @BindView(R.id.img_type_vote)
    ImageView imgTypeVote;
    @BindView(R.id.tv_type_vote)
    CustomTextView tvTypeVote;
    @BindView(R.id.tv_time_bottom)
    CustomTextView tvTimeBottom;
    @BindView(R.id.tv_distance_bottom)
    CustomTextView tvDistanceBottom;
    @BindView(R.id.tv_bike_rate_vote)
    CustomTextView tvBikeRateVote;
    @BindView(R.id.tv_rate_driver_rate_vote)
    CustomTextView tvRateDriverRateVote;
    @BindView(R.id.ll_root_dialog_booking_rate_vote)
    RelativeLayout llRootDialogBookingRateVote;

    private Marker markerFrom;
    private Marker markerDes;
    private GoogleMap mMap;
    private MyPlace mAddressFrom;
    private MyPlace mAddressDes;
    //    private MyPlace mAddressDriver;
    private DirectionCustomModel directionCustomModel = new DirectionCustomModel();
    private Discount promotionCost;
    private int distanceCost;
    private int promotionID;
    private double distance;
    private String duration;
    private int idDriver;
    private TripInforModel mTripInfor;
    SupportMapFragment mapFragment;
    private CountDownTimer timer;
    private boolean isFocusFisrtTime = true;
    private Polyline polyline;
    boolean isTimeoutCountdown = false;
    public boolean needToListenRoom = true;
    private boolean isActivityActived;
    MaterialDialog dialogVote;
    //    int resource= R.drawable.ic_motorcycle;
    String resource;
    private CountDownTimer timerAutoCancel = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long l) {
            MainApplication.getInstance().setTimeExist((int) l / 1000);
        }

        @Override
        public void onFinish() {
            if (!isJoined) {
//                getmSocket().cancleBooking(MainApplication.getInstance().getmIdBooking(), getmSetting().getString(Constants.ID));
                cancelBooking(SocketConstants.STATUS_SERVER_TIMEOUT);
                isTimeoutCountdown = true;
            }
        }
    };
    private boolean isJoined = false;
    //    private CustomTextView mTvName;
//    private CircleImageView mAvt;
    private boolean isGoing;
    private User mUser;
    private boolean isBookingFinish = false;
    public ValueAnimator va = ValueAnimator.ofFloat(0, 3);
    Handler countdownHandler = new Handler();
    LocationBearing locationBearingDes = new LocationBearing();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String jsonResponse = intent.getStringExtra(Constants.BOOKING);
                SocketResponse response = new Gson().fromJson(jsonResponse, SocketResponse.class);
                if (response != null && response.getIdUser() == Integer.parseInt(getmSetting().getString(Constants.ID))) {
                    switch (response.getStatus()) {
                        case SocketConstants.STATUS_JOIN: {
                            isJoined = true;
//                            ((MapsActivity)getActivity()).showSnackBar("Có tài xế nhận chuyến");
                            idDriver = response.getIdDriver();
                            MainApplication.getInstance().setmIdDriver(idDriver);
                            content.stopRippleAnimation();
                            getDetailBooking(idDriver, MainApplication.getInstance().getmIdBooking());
                            LatLng latLngDes = new LatLng(mAddressDes.getLat(), mAddressDes.getLng());
                            LatLng latLngFrom = new LatLng(mAddressFrom.getLat(), mAddressFrom.getLng());
//                            List<LatLng> list = new ArrayList<>();
//                            list.add(latLngDes);
//                            list.add(latLngFrom);
                            mMap.clear();
//                            CommonUtils.focusAllMarkers(list, mMap, getApplicationContext());
                            CommonUtils.focusCurrentLocation(latLngFrom, Constants.ZOOM, mMap);
                            if (Build.VERSION.SDK_INT >= 20) {
                                markerFrom = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_local_pink_map, null), mMap, latLngFrom);
                            } else {
                                markerFrom = CommonUtils.addMarker(AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_local_pink_map), mMap, latLngFrom);
                            }
//                            CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_motorcycle, null), mMap, latLngFrom);
                            timerAutoCancel.cancel();
                            llRootCancelBooking.setVisibility(View.GONE);

                            break;
                        }
                        case SocketConstants.STATUS_ONGOING: {
                            if (mTripInfor == null) return;
                            if (!isGoing) {
                                showSnackBar("Tài xế đang đến");
                                isGoing = true;
                            }
//                            llRootTimeBookingBottom.setVisibility(View.VISIBLE);
                            tvRateDriverBottom.setText(mTripInfor.getDriver().getVote() + "");
                            tvDriverNameBottom.setText(mTripInfor.getDriver().getName());
                            tvCodeBikeBottom.setText(mTripInfor.getDriver().getNumber());
                            ratingBarBottom.setRating(mTripInfor.getDriver().getVote());
                            LatLng latLngDes = new LatLng(response.getLat(), response.getLng());         // driver location realtime
                            LatLng latLngFrom = new LatLng(mAddressFrom.getLat(), mAddressFrom.getLng());   // my location
                            if (markerDes != null) {
                                locationBearingDes.addToStack(latLngDes);
//                                MapUtils.animateMarkerNew(latLngDes, markerDes, MapUtils.getBearing(latLngFrom, latLngDes));
                            } else {
                                locationBearingDes.setLongitude(latLngDes.longitude);
                                locationBearingDes.setLatitude(latLngDes.latitude);
//                                if (Build.VERSION.SDK_INT >= 20) {
//                                    markerDes = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), resource, null), mMap, latLngDes);
//                                } else {
//                                    markerDes = CommonUtils.addMarker(AppCompatDrawableManager.get().getDrawable(getApplicationContext(), resource), mMap, latLngDes);
//                                }
                                //// TODO: 8/29/2017 Change icon from server
                                Drawable dCar = Drawable.createFromPath(resource);
                                dCar = CommonUtils.resizeCar(dCar, ExecuteBookingActivity.this);
                                markerDes = CommonUtils.addMarker(dCar, mMap, latLngDes);
                                locationBearingDes.setMarker(markerDes);
                                locationBearingDes.addToStack(latLngDes);
                            }
                            if (directionCustomModel.getPolyline() != null) {
                                directionCustomModel.getPolyline().remove();
                            }
                            if (markerFrom != null) markerFrom.remove();

                            if (Build.VERSION.SDK_INT >= 20) {
                                markerFrom = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_local_pink_map, null), mMap, latLngFrom);
                            } else {
                                markerFrom = CommonUtils.addMarker(AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_local_pink_map), mMap, latLngFrom);
                            }
                            isFocusDirection = false;
                            showDirection(latLngDes, latLngFrom, "Tài xế sẽ gặp bạn trong ");
                            Picasso.with(getApplicationContext()).load("https://" + mTripInfor.getDriver().getAvatar()).into(imgAvtBottom);
                            mTripInfor.getTrip_info().setStatus(SocketConstants.STATUS_ONGOING);
                            String json = new Gson().toJson(mTripInfor);
                            getmSetting().put(Constants.BOOKING, json);
                            break;
                        }
                        case SocketConstants.STATUS_SERVER_TIMEOUT: {
                            isJoined = true;    // need to be true because of disable timer
                            showSnackBar("Không có tài xế nào quanh bạn");
                            mTripInfor.getTrip_info().setStatus(SocketConstants.STATUS_SERVER_TIMEOUT);
                            removeDataBooking();

                            break;
                        }
                        case SocketConstants.STATUS_START: {
                            //llRootTimeBookingBottom.setVisibility(View.GONE);
                            // markerFrom is Destination / markerDes is driver on the street
                            if (response.getLat() == 0 || response.getLng() == 0 || mAddressDes == null || mAddressFrom == null)
                                return;
                            LatLng latLngFrom = new LatLng(mAddressDes.getLat(), mAddressDes.getLng());
                            LatLng latLngDes = new LatLng(response.getLat(), response.getLng());
                            List<LatLng> list = new ArrayList<>();
                            list.add(latLngFrom);
                            list.add(latLngDes);
                            llDiverBottom.setVisibility(View.GONE);
                            llAddBottom.setVisibility(View.VISIBLE);
                            if (isFocusFisrtTime) {
                                mMap.clear();
                                locationBearingDes.setLongitude(latLngDes.longitude);
                                locationBearingDes.setLatitude(latLngDes.latitude);
                                if (Build.VERSION.SDK_INT >= 20) {
                                    markerFrom = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_local_pink_map, null), mMap, latLngFrom);
//                                    markerDes = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), resource, null), mMap, latLngDes);
                                } else {
                                    markerFrom = CommonUtils.addMarker(AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_local_pink_map), mMap, latLngFrom);
//                                    markerDes = CommonUtils.addMarker(AppCompatDrawableManager.get().getDrawable(getApplicationContext(), resource), mMap, latLngDes);
                                }

                                //// TODO: 8/29/2017 Change icon from server
                                Drawable dCar = Drawable.createFromPath(resource);
                                dCar = CommonUtils.resizeCar(dCar, ExecuteBookingActivity.this);
                                markerDes = CommonUtils.addMarker(dCar, mMap, latLngDes);

                                locationBearingDes.setMarker(markerDes);
                                locationBearingDes.addToStack(latLngDes);
                                showSnackBar("Đang di chuyển");
                                isFocusFisrtTime = false;
                                CommonUtils.focusAllMarkers(list, mMap, getApplicationContext());
                            } else {
                                if (markerFrom != null && markerDes != null) {
                                    locationBearingDes.addToStack(latLngDes);
//                                    MapUtils.animateMarkerNew(latLngDes, markerDes, MapUtils.getBearing(latLngFrom, latLngDes));
                                    markerFrom.setPosition(latLngFrom);
                                    markerFrom.setVisible(true);
                                    markerDes.setVisible(true);
                                } else {
                                    mMap.clear();
                                    locationBearingDes.setLongitude(latLngDes.longitude);
                                    locationBearingDes.setLatitude(latLngDes.latitude);
                                    if (Build.VERSION.SDK_INT >= 20) {
                                        markerFrom = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_local_pink_map, null), mMap, latLngFrom);
//                                        markerDes = CommonUtils.addMarker(ResourcesCompat.getDrawable(getResources(), resource, null), mMap, latLngDes);
                                    } else {
                                        markerFrom = CommonUtils.addMarker(AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_local_pink_map), mMap, latLngFrom);
//                                        markerDes = CommonUtils.addMarker(AppCompatDrawableManager.get().getDrawable(getApplicationContext(), resource), mMap, latLngDes);
                                    }
                                    //// TODO: 8/29/2017 Change icon from server
                                    Drawable dCar = Drawable.createFromPath(resource);
                                    dCar = CommonUtils.resizeCar(dCar, ExecuteBookingActivity.this);
                                    markerDes = CommonUtils.addMarker(dCar, mMap, latLngDes);

                                    locationBearingDes.setMarker(markerDes);
                                    locationBearingDes.addToStack(latLngDes);
                                }
                            }
                            if (directionCustomModel.getPolyline() != null) {
                                directionCustomModel.getPolyline().remove();
                            }
                            if (polyline != null) polyline.remove();
                            isFocusDirection = false;
                            showDirection(latLngDes, latLngFrom, "Thời gian đến nơi dự kiến ");
//                            String url = getDirectionsUrl(latLngFrom, latLngDes);
//                            new DirectionAsynTaskBooking(getApplicationContext(), directionCustomModel, mMap, tvTime, polyline).execute(url);
                            mTripInfor.getTrip_info().setStatus(SocketConstants.STATUS_START);
                            String json = new Gson().toJson(mTripInfor);
                            getmSetting().put(Constants.BOOKING, json);
                            break;
                        }
                        case SocketConstants.STATUS_FINISH: {
                            Crouton.cancelAllCroutons();
                            isBookingFinish = true;
                            getmSocket().isContinuteBooking = false;
//                            llRootRating.setVisibility(View.VISIBLE);
                            content.setVisibility(View.GONE);
                            makerImb.setVisibility(View.GONE);
                            llRootDialog.setVisibility(View.GONE);
                            llRootCancelBooking.setVisibility(View.GONE);
                            llRootTimeBookingBottom.setVisibility(View.GONE);
                            changeViewToVoting();

                            if (mTripInfor == null) return;
                            Picasso.with(getApplicationContext()).load("https://" + mTripInfor.getDriver().getAvatar()).into(imgAvtVote);

//                            tvVoteRating.setText(mTripInfor.getDriver().getVote() + "");
                            MainApplication.getInstance().setmIdBooking(0);
                            MainApplication.getInstance().setmIdDriver(0);
                            if (promotionCost != null) {
                                int total = mTripInfor.getTrip_info().getPrice() - Integer.parseInt(promotionCost.getValue());
                                if (total < 0) total = 0;
                            } else {
                            }
                            getmSocket().showNotificationInStack();
                            mTripInfor.getTrip_info().setStatus(SocketConstants.STATUS_FINISH);
                            String json = new Gson().toJson(mTripInfor);
                            getmSetting().put(Constants.BOOKING, json);
                            break;
                        }


                    }
                    mMap.setPadding(0, 0, 0, 0);

                    return;
                }

                String nameDriver = intent.getStringExtra(Constants.SEND_TO_DRIVER);
                if (nameDriver != null) {
                    tvSenttoDriver.setText(Html.fromHtml(String.format(getString(R.string.dang_doi), nameDriver)));
                    return;
                }

                boolean timeout = intent.getBooleanExtra(Constants.TIME_OUT_BOOKING, false);
                if (timeout) {
                    isJoined = false;
                    LogUtils.e("receive noti from broadcast TIME OUT BOOKING");
                    showSnackBar("Không có tài xế nào quanh bạn");
                    if (!isTimeoutCountdown)
                        cancelBooking(SocketConstants.STATUS_SERVER_TIMEOUT);
                    removeDataBooking();
                    return;
                }
                boolean needToRequestList = intent.getBooleanExtra(Constants.NEED_TO_REQUEST_LIST_DRIVER, false);
                if (needToRequestList) {
                    tvSenttoDriver.setText("Đang tìm tài xế gần bạn");
                    requestListDriverThenSend();
                    return;
                }
            }
        }
    };
    private boolean isFocusDirection = false;
    private int voteType = 1;

    private void changeViewToVoting() {
        if (mTripInfor == null) return;
        llRootDialogBookingRateVote.setVisibility(View.VISIBLE);
        Picasso.with(getApplicationContext()).load("https://" + mTripInfor.getDriver().getAvatar()).into(imgAvtVote);
        tvNameDriverVote.setText(mTripInfor.getDriver().getName());
        tvBikeRateVote.setText(mTripInfor.getDriver().getNumber());
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rad_funy: {
                        voteType = 1;
                        break;
                    }
                    case R.id.rad_group: {
                        voteType = 2;

                        break;
                    }
                    case R.id.rad_right_rule: {
                        voteType = 3;

                        break;
                    }
                }
            }
        });
        radFuny.setChecked(true);

    }

    private void removeDataBooking() {
        getmSetting().put(Constants.BOOKING, "");
        getmSetting().put(Constants.PROMOTION_BOOKING, "");
        getmSetting().put(Constants.BOOK_ADDRESS_DES, "");
        getmSetting().put(Constants.BOOK_ADDRESS_FROM, "");
    }

    private void getStringFormat(String text, long time) {

    }

    private void showDirection(final LatLng latLngOrgin, final LatLng latLngDestination, final String text) {
        Call<ApiResponse<List<Routes>>> direction = ApiUtils.getAPIPLACE().create(GoiXeOmAPI.class)
                .getDirection(latLngOrgin.latitude + "," + latLngOrgin.longitude
                        , latLngDestination.latitude + "," + latLngDestination.longitude, getString(R.string.google_map_id));
        direction.enqueue(new CallBackCustomNoDelay<ApiResponse<List<Routes>>>(ExecuteBookingActivity.this, new OnResponse<ApiResponse<List<Routes>>>() {
            @Override
            public void onResponse(ApiResponse<List<Routes>> object) {
                if (object.getRoutes() != null && object.getRoutes().size() > 0) {
                    tvTime.setText(Html.fromHtml(
                            String.format(
                                    getString(R.string.txt_duration)
                                    , text, object.getRoutes().get(0).getLegs().get(0).getDuration().getText().replace("day", "ngày").replace("min", "phút").replace("hour", "giờ").replace("s", ""))
                            )
                    );
                    tvTimeBottom.setText(object.getRoutes().get(0).getLegs().get(0).getDuration().getText().replace("day", "ngày").replace("min", "phút").replace("hour", "giờ").replace("s", ""));
                    tvDistanceBottom.setText(object.getRoutes().get(0).getLegs().get(0).getDistance().getText());
                    PolylineOptions line = new PolylineOptions();
                    line.width((int) getResources().getDimension(R.dimen.poliline_width)).color(ContextCompat.getColor(ExecuteBookingActivity.this, R.color.black));
                    int n = object.getRoutes().get(0).getLegs().get(0).getSteps().size();
                    line.add(latLngOrgin);
                    if (polyline != null) polyline.remove();

                    for (int i = 0; i < n; i++) {
                        line.add(new LatLng(
                                object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStart_location().getLat()
                                , object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStart_location().getLng()
                        ));
                        line.addAll(decodePoly(object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getPolyline().getPoints()));
                        line.add(new LatLng(
                                object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getEnd_location().getLat()
                                , object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getEnd_location().getLng()
                        ));
                    }
                    line.add(latLngDestination);
                    polyline = mMap.addPolyline(line);
                    if (!isFocusDirection) {
                        CommonUtils.focusAllMarkers(polyline.getPoints(), mMap, ExecuteBookingActivity.this);
                        isFocusDirection = true;
                    }
                    mMap.setPadding(0, 0, 0, 100);

//                    polyline = temp;
                } else {
                    LogUtils.e("direction null or no routes");
                }
            }
        }));

    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    @Override
    public void pingNotification(String title, String content) {
    }

    @Override
    protected void onSoketConnected() {
        if (getmSetting().getString(Constants.BOOKING) != null && !getmSetting().getString(Constants.BOOKING).isEmpty()) {
            reconnectBooking();
            return;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_booking);
        ButterKnife.bind(this);
        resource = FileUtils.getFolder(this) + getString(R.string.bike);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        view = llRoot;
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        View header = navigationView.getHeaderView(0);
//        this.mTvName = (CustomTextView) header.findViewById(R.id.tv_name);
//        this.mAvt = (CircleImageView) header.findViewById(R.id.img_avt);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mUser = MainApplication.getInstance().getmUser();
//        llRootDialog.setBackgroundResource(R.drawable.ic_no_line);
        if (mUser == null) {
            LogUtils.e("Usser nulllll");
            startActivity(new Intent(ExecuteBookingActivity.this, MapsActivity.class));
            finishAffinity();
            return;
        }
//        Menu m = navigationView.getMenu();
//        for (int i = 0; i < m.size(); i++) {
//            MenuItem mi = m.getItem(i);
//            //for aapplying a font to subMenu ...
//            SubMenu subMenu = mi.getSubMenu();
//            if (subMenu != null && subMenu.size() > 0) {
//                for (int j = 0; j < subMenu.size(); j++) {
//                    MenuItem subMenuItem = subMenu.getItem(j);
//                    applyFontToMenuItem(subMenuItem);
//                }
//            }
//            //the method we have create in activity
//            applyFontToMenuItem(mi);
//        }

//        mTvName.setText(mUser.getName());
//        if (mUser.getAvatarSocial() != null && !mUser.getAvatarSocial().isEmpty())
//            mUser.setU_avatar(mUser.getAvatarSocial());
//        if (mUser.getU_avatar() != null && !mUser.getU_avatar().isEmpty())
//            Glide.with(getApplicationContext()).load("https://" + mUser.getU_avatar()).asBitmap().into(mAvt);
        registerReceiver(receiver, new IntentFilter(SocketConstants.EVENT_CONNECTION));
        imgInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llRootTimeBookingBottom.setVisibility(View.GONE);
                llRootDialog.setVisibility(View.VISIBLE);
            }
        });
        tvNameDriver.setSelected(true);
        tvDes.setSelected(true);
        tvFrom.setSelected(true);
        tvDriverNameBottom.setSelected(true);
        if (getmSetting().getString(Constants.BOOKING) != null && !getmSetting().getString(Constants.BOOKING).isEmpty()) {
            reconnectBooking();
            return;
        }
        if (getIntent() != null) {
            Bundle b = getIntent().getBundleExtra(Constants.BUNDLE);
            if (b != null) {
                mAddressDes = b.getParcelable(Constants.DES);
                mAddressFrom = b.getParcelable(Constants.FROM);
                distanceCost = b.getInt(Constants.COST);
                distance = b.getDouble(Constants.DISTANCE);
                promotionCost = b.getParcelable(Constants.PROMOTION);
                duration = b.getString(Constants.DURATION);


                tvAddDesCallBooking.setText(mAddressDes.getDescription().replace(", Hanoi", "").replace(", HaNoi", "").replace(", Hà Nội", "").replace(", Hà nội", "").replace(", Ha noi", ""));
                tvAddFromCallBooking.setText(mAddressFrom.getDescription().replace(", Hanoi", "").replace(", HaNoi", "").replace(", Hà Nội", "").replace(", Hà nội", "").replace(", Ha noi", ""));
                tvDistanceCallBooking.setText(CommonUtils.round(distance, 1) + " km");
                boolean isNeedToRequestListDriver = b.getBoolean(Constants.NEED_TO_REQUEST_LIST_DRIVER);
//                if (isNeedToRequestListDriver) {
//                    requestListDriverThenSend();
//                }
            }
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llRootDialog.setVisibility(View.GONE);
                content.setVisibility(View.GONE);
                llcontent.setVisibility(View.GONE);
                llRootTimeBookingBottom.setVisibility(View.VISIBLE);
                makerImb.setVisibility(View.GONE);
                if (!isBookingFinish)
                    showSnackBar("Tài xế đang đến");
            }
        });
        content.startRippleAnimation();
        progressBooking.setTextEnabled(false);
        progressBooking.setInterpolator(new AccelerateDecelerateInterpolator());
        progressBooking.setStartAngle(270);
        progressBooking.setProgress(100);
        timer = new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long l) {
                l -= 1000;
//                l-=1000;
//                float progress = l / 100;
//                progressBooking.setProgressWithAnimation((progress), 500);
//                if (tvCancle != null && progress % 10 == 0)
//                    tvCancle.setText(String.format("Hủy %d", (int) progress / 10));
                float progress = l / 1000;
                progressBooking.setProgressWithAnimation((l * 100) / 10000, 300);
                LogUtils.e("progress = " + (((l * 100) / 10000) / 10) * 10);
                if (l / 1000 == 0) {
                    progressBooking.setProgressWithAnimation(0, 300);
                }
                if (tvCancle != null)
                    tvCancle.setText(String.format("Hủy %d", (int) progress));
            }

            @Override
            public void onFinish() {
                tvCancle.setText(String.format("Hủy %d", 0));
                progressBooking.setProgressWithAnimation(0, 300);
                llCancle.setVisibility(View.INVISIBLE);
                requestListDriverThenSend();
                timerAutoCancel.start();
            }
        };
        timer.start();

        tvAddDesInfo.setSelected(true);
        tvAddFromInfo.setSelected(true);
        tvAddFromCallBooking.setSelected(true);
        tvAddDesCallBooking.setSelected(true);
        radFuny.setChecked(true);

//        if (MainApplication.getInstance().getmDriver() != null && !MainApplication.getInstance().getmDriver().isEmpty()) {
//            tvSenttoDriver.setText("Đang đợi " + MainApplication.getInstance().getmDriver() + " ...");
//        }
    }

    @OnClick(R.id.btn_vote)
    public void clickVote() {
        getDialogProgress().showDialog();
        Call<ApiResponse> rateDriver = getmApi().voteDriver(ApiConstants.API_KEY, mTripInfor.getTrip_info().getIdTrip(), voteType, (int) ratingBarDriver.getRating());
        rateDriver.enqueue(new CallBackCustom<ApiResponse>(getApplicationContext(), getDialogProgress(), new OnResponse<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    setResult(RESULT_OK);
                    if (ratingBarDriver.getRating() >= 1)
                        getmSocket().rateDriver(mTripInfor.getDriver().getId(), (int) ratingBarDriver.getRating());
                    removeDataBooking();
                    finish();
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    private void requestListDriverThenSend() {
        Call<ApiResponse<BookingTrip>> request = getmApi().getListDriverBooking(ApiConstants.API_KEY, MainApplication.getInstance().getmIdBooking());
        request.enqueue(new CallBackCustomNoDelay<ApiResponse<BookingTrip>>(this, new OnResponse<ApiResponse<BookingTrip>>() {
            @Override
            public void onResponse(ApiResponse<BookingTrip> object) {
                LogUtils.e("Load more driver for booking now" + object.getData().getT_id() + "----" + object.getData().getList_driver().size());
                if (!isTimeoutCountdown && !isJoined) {
                    MainApplication.getInstance().setTimeOut(false);
                    MainApplication.getInstance().setmIdBooking(object.getData().getT_id());
                    getmSocket().creatBooking(mAddressFrom.getDescription(), mAddressDes.getDescription()
                            , distanceCost, promotionCost != null ? Integer.parseInt(promotionCost.getPr_id()) : 0, distance, directionCustomModel.getDuration(), object.getData());
                    if (needToListenRoom) {
                        getmSocket().listenToRoomBooking(mAddressFrom.getDescription(), mAddressDes.getDescription()
                                , distanceCost, promotionCost != null ? Integer.parseInt(promotionCost.getPr_id()) : 0, distance, directionCustomModel.getDuration());
                        needToListenRoom = false;
                    }
                }
            }
        }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDetailBooking(int idDriver, int idBooking) {
        Call<ApiResponse<TripInforModel>> getDetailBooking = getmApi().getDetailBooking(ApiConstants.API_KEY, idBooking, idDriver);
        getDetailBooking.enqueue(new CallBackCustom<ApiResponse<TripInforModel>>(this, new OnResponse<ApiResponse<TripInforModel>>() {
            @Override
            public void onResponse(ApiResponse<TripInforModel> object) {
                if (object.getData() != null) {
                    mTripInfor = object.getData();
                    timer.onFinish();
                    if (mTripInfor.getTrip_info() != null && mTripInfor.getTrip_info().getTypeVichle() != 0) {
                        resource = FileUtils.getFolder(ExecuteBookingActivity.this) + getString(R.string.car);
                        tvNumberSeat.setText(4 + "");
                    } else {
                        tvNumberSeat.setText(2 + "");

                    }
//                    content.setVisibility(View.GONE);
                    mapFragment.getView().setClickable(true);
                    llRootDialog.setVisibility(View.VISIBLE);
                    llRootCancelBooking.setVisibility(View.GONE);
                    tvBike.setText(object.getData().getDriver().getNumber());
                    tvBikeName.setText(Html.fromHtml(String.format(getString(R.string.txt_driver), object.getData().getDriver().getBrand(), object.getData().getDriver().getModel())));
                    tvDes.setText(CommonUtils.getGreateAddressStr(object.getData().getTrip_info().getEnd()));
                    tvFrom.setText(CommonUtils.getGreateAddressStr(object.getData().getTrip_info().getStart()));
                    tvDes.setSelected(true);
                    tvFrom.setSelected(true);
                    tvNameDriver.setSelected(true);
                    tvAddDesInfo.setSelected(true);
                    tvAddFromInfo.setSelected(true);
                    tvAddDesInfo.setText(CommonUtils.getGreateAddressStr(mTripInfor.getTrip_info().getEnd()));
                    tvAddFromInfo.setText(CommonUtils.getGreateAddressStr(mTripInfor.getTrip_info().getStart()));
                    tvMs.setText(mTripInfor.getTrip_info().getIdTrip() + "");
                    tvBikeRateVote.setText(object.getData().getDriver().getNumber());
                    tvNameDriver.setText(object.getData().getDriver().getName());
                    showSnackBar(object.getData().getDriver().getName() + " đã nhận chuyến");
                    tvRateDriver.setText(Html.fromHtml(String.format(getString(R.string.xep_hang), object.getData().getDriver().getVote())));
                    tvRateDriverRateVote.setText(Html.fromHtml(String.format(getString(R.string.xep_hang), object.getData().getDriver().getVote())));

                    if (promotionCost != null) {
                        int total = object.getData().getTrip_info().getPrice() - Integer.parseInt(promotionCost.getValue());
                        if (total < 0) total = 0;
                        tvTotalCost.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.GERMAN).format(total) + " vnđ"));
                        getmSetting().put(Constants.PROMOTION_BOOKING, new Gson().toJson(promotionCost));
                    } else {
                        tvTotalCost.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.GERMAN).format(object.getData().getTrip_info().getPrice()) + " vnđ"));
                    }
                    ratingBar.setRating(object.getData().getDriver().getVote());
//                    Glide.with(getApplicationContext()).load("https://" + object.getData().getDriver().getAvatar()).asBitmap().into(target);
                    Picasso.with(getApplicationContext()).load("https://" + object.getData().getDriver().getAvatar()).into(imgAvt);

//                    Glide.with(getApplicationContext()).load("https://" + object.getData().getDriver().getAvatar()).asBitmap().into(imgAvtBottom);
                    Picasso.with(getApplicationContext()).load("https://" + object.getData().getDriver().getAvatar()).into(imgAvtBottom);

                    tvRateDriverBottom.setText(mTripInfor.getDriver().getVote() + "");
                    tvDriverNameBottom.setText(mTripInfor.getDriver().getName());
                    tvCodeBikeBottom.setText(mTripInfor.getDriver().getNumber());
                    ratingBarBottom.setRating(mTripInfor.getDriver().getVote());
                    mTripInfor.getTrip_info().setStatus(SocketConstants.STATUS_JOIN);

                    String json = new Gson().toJson(mTripInfor);
                    getmSetting().put(Constants.BOOKING, json);
                    getmSetting().put(Constants.BOOK_ADDRESS_FROM, new Gson().toJson(mAddressFrom));
                    getmSetting().put(Constants.BOOK_ADDRESS_DES, new Gson().toJson(mAddressDes));
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }
////R.id.dang_xuat,
//    @OnClick({ R.id.term})
//    public void onViewMenuClicked(View view) {
//        Intent i = new Intent(ExecuteBookingActivity.this, ActivityPrivacy.class);
//        if (view.getId() == R.id.dang_xuat) {
//            i.putExtra(Constants.MSG, true);
//        } else {
//            i.putExtra(Constants.MSG, false);
//
//        }
//        startActivity(i);
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (getmSetting().getString(Constants.BOOKING) != null && !getmSetting().getString(Constants.BOOKING).isEmpty()) {
            reconnectBooking();
            return;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            mMap.setPadding(0, 0, 0, 100);
            mMap.getUiSettings().setZoomGesturesEnabled(false);
            mMap.getUiSettings().setAllGesturesEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
            mMap.setMapStyle(style);
            if (mAddressFrom != null) {
                CommonUtils.focusCurrentLocation(new LatLng(mAddressFrom.getLat(), mAddressFrom.getLng()), Constants.ZOOM, mMap);
//                if (Build.VERSION.SDK_INT >= 20) {
//                    CommonUtils.addMarker(ContextCompat.getDrawable(this, R.drawable.ic_pin_green2), mMap, new LatLng(mAddressFrom.getLat(), mAddressFrom.getLng()));
//                } else {
//                    CommonUtils.addMarker(AppCompatDrawableManager.get().getDrawable(getApplicationContext(), R.drawable.ic_pin_green2), mMap, new LatLng(mAddressFrom.getLat(), mAddressFrom.getLng()));
//                }
            }
            if (getmSetting().getString(Constants.BOOKING) != null && !getmSetting().getString(Constants.BOOKING).isEmpty()) {
                reconnectBooking();
                return;
            }
        }
    }

    @OnClick({R.id.img_call, R.id.ll_cancle})
    public void onViewBottomClicked(View view) {
        if (view.getId() == R.id.img_call)
            CommonUtils.intentToCall(mTripInfor.getDriver().getPhone(), this);
        else {
            cancelBooking(SocketConstants.STATUS_USER_CANCLE);
        }

    }

    private void cancelBooking(final int status) {
        timer.cancel();
        LogUtils.e(" cancle booking");
        try {
            getDialogProgress().showDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }
        getmSocket().isContinuteBooking = false;
        getmSocket().showNotificationInStack();
        Call<ApiResponse> cancelBooking = getmApi().cancleBooking(ApiConstants.API_KEY, MainApplication.getInstance().getmIdBooking(), status);
        cancelBooking.enqueue(new CallBackCustom<ApiResponse>(this, getDialogProgress(), new OnResponse<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    llCancle.setVisibility(View.INVISIBLE);

                    if (status == SocketConstants.STATUS_SERVER_TIMEOUT) {
                        getmSocket().cancleBooking(MainApplication.getInstance().getmIdBooking());
                        showDialogWithCallback();
                        MainApplication.getInstance().setTimeOut(true);
                    } else {
                        finish();
                    }
                    MainApplication.getInstance().setmIdBooking(0);
                    MainApplication.getInstance().setmIdDriver(0);
                    removeDataBooking();
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    public void showDialogWithCallback() {
        if (!ExecuteBookingActivity.this.isFinishing()) {
            new MaterialDialog.Builder(ExecuteBookingActivity.this).title(getString(R.string.error))
                    .content("Không có tài xế nào quanh bạn.")
                    .positiveText("Thử lại")
                    .positiveColor(Color.GRAY)
                    .cancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityActived = true;
//        if (getmSocket() != null) {
//            reconnectBooking();
//
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityActived = false;
    }

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

//    @OnClick(R.id.btn_rate)
//    public void onViewRateClicked() {
//        getDialogProgress().showDialog();
//        Call<ApiResponse> rateDriver = getmApi().rateDriver(ApiConstants.API_KEY, mTripInfor.getTrip_info().getIdTrip(), (int) ratingBarDriver.getRating());
//        rateDriver.enqueue(new CallBackCustom<ApiResponse>(getApplicationContext(), getDialogProgress(), new OnResponse<ApiResponse>() {
//            @Override
//            public void onResponse(ApiResponse object) {
//                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
//                    if (ratingBarDriver.getRating() >= 1)
//                        getmSocket().rateDriver(mTripInfor.getDriver().getId(), (int) ratingBarDriver.getRating());
////                    setResult(RESULT_OK);
////                    removeDataBooking();
////                    finish();
//                } else {
//                    showDialogErrorContent(object.getMessage());
//                }
//            }
//        }));
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //  super.onBackPressed();
        }
    }

    Handler handlerSnackBar = new Handler();

    public void showSnackBar(String content) {
//        Crouton.cancelAllCroutons();
//        Style croutonStyle = new Style.Builder().setBackgroundColorValue(Color.parseColor("#25b45b")).build();
//        Crouton.makeText(this, content, croutonStyle, view).setConfiguration(new Configuration.Builder().setDuration(Configuration.DURATION_INFINITE).build()).show();
        final String oldMess = tvTime.getText().toString();
        tvTime.setText(content);
        if (oldMess.isEmpty()) return;
        handlerSnackBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvTime.setText(Html.fromHtml(oldMess));
            }
        }, 2000);
    }


//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.nav_saleoff) {
//            startActivity(new Intent(ExecuteBookingActivity.this, PromotionActivity.class));
//
//            // Handle the camera action
//        } else if (id == R.id.nav_help) {
//            startActivity(new Intent(ExecuteBookingActivity.this, FAQActivity.class));
//
//        } else if (id == R.id.nav_history) {
//            startActivity(new Intent(ExecuteBookingActivity.this, HistoryActivity.class));
//
//
//        } else if (id == R.id.nav_notification) {
//            startActivity(new Intent(ExecuteBookingActivity.this, AboutUsActivity.class));
//
//
//        } else if (id == R.id.nav_list_notification) {
//            startActivity(new Intent(ExecuteBookingActivity.this, NotificationActivity.class));
//
//
//        } else if (id == R.id.nav_setting) {
//            startActivityForResult(new Intent(ExecuteBookingActivity.this, SettingActivity.class), Constants.CODE_REQ_SETTING);
//
//        } else if (id == R.id.nav_view) {
//
//        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        KeyboardUtils.hideSoftInput(this);
//        return true;
//    }

    @OnClick({R.id.tv_ok, R.id.btn_close})
    public void onViewOkClicked() {
        llRootDialog.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
        llcontent.setVisibility(View.GONE);
        makerImb.setVisibility(View.GONE);
        llRootTimeBookingBottom.setVisibility(View.VISIBLE);
        if (!isBookingFinish)
            showSnackBar("Đang kết nối với tài xế");
    }

    @AddTrace(name = "ReconnectBooking")
    private void reconnectBooking() {
        String jsonBooking = getmSetting().getString(Constants.BOOKING);
        if (jsonBooking != null && !jsonBooking.isEmpty() && getmSocket() != null && mMap != null) {
            final TripInforModel tripInforModel = new Gson().fromJson(jsonBooking, TripInforModel.class);
            if (tripInforModel != null) {
                showSnackBar("Đang trong chuyến đi");
                mTripInfor = tripInforModel;
                content.stopRippleAnimation();
                llRootCancelBooking.setVisibility(View.GONE);
                content.setVisibility(View.GONE);
                llcontent.setVisibility(View.GONE);


                if (getmSetting().getString(Constants.PROMOTION_BOOKING) != null && !getmSetting().getString(Constants.PROMOTION_BOOKING).isEmpty()) {
                    promotionCost = new Gson().fromJson(getmSetting().getString(Constants.PROMOTION_BOOKING), Discount.class);
                }
                if (getmSetting().getString(Constants.BOOK_ADDRESS_DES) != null && getmSetting().getString(Constants.BOOK_ADDRESS_FROM) != null) {
                    mAddressDes = new Gson().fromJson(getmSetting().getString(Constants.BOOK_ADDRESS_DES), MyPlace.class);
                    mAddressFrom = new Gson().fromJson(getmSetting().getString(Constants.BOOK_ADDRESS_FROM), MyPlace.class);
                    if (mAddressFrom == null && mAddressDes == null) {
                        showDialogErrorContent("Không thể tiếp tục chuyến đi");
                        removeDataBooking();
                        return;
                    }
                    if (mMap != null) {
                        CommonUtils.focusCurrentLocation(new LatLng(mAddressFrom.getLat(), mAddressFrom.getLng()), Constants.ZOOM, mMap);
                    }
                }
                getDialogProgress().showDialog();
                Call<ApiResponse<TripInforModel>> getDetailBooking = getmApi().getDetailBooking(ApiConstants.API_KEY, tripInforModel.getTrip_info().getIdTrip(), tripInforModel.getDriver().getId());
                getDetailBooking.enqueue(new CallBackCustomNoDelay<ApiResponse<TripInforModel>>(ExecuteBookingActivity.this, getDialogProgress(), new OnResponse<ApiResponse<TripInforModel>>() {
                    @Override
                    public void onResponse(ApiResponse<TripInforModel> object) {
                        if (object.getData() != null) {
                            mTripInfor = object.getData();
                            tvMs.setText(object.getData().getTrip_info().getIdTrip() + "");
                            addViewReconnection();
                        } else {
                            addViewReconnection();
                        }
                    }
                }));

            }
        }
    }

    @AddTrace(name = "Add Reconnect View")
    private void addViewReconnection() {
        content.setVisibility(View.GONE);
        llcontent.setVisibility(View.GONE);
        llRootCancelBooking.setVisibility(View.GONE);
        llRootDialog.setVisibility(View.GONE);
        llRootTimeBookingBottom.setVisibility(View.VISIBLE);
        tvRateDriverBottom.setText(mTripInfor.getDriver().getVote() + "");
        tvDriverNameBottom.setText(mTripInfor.getDriver().getName());
        tvCodeBikeBottom.setText(mTripInfor.getDriver().getNumber());
        ratingBarBottom.setRating(mTripInfor.getDriver().getVote());
        Picasso.with(getApplicationContext()).load("https://" + mTripInfor.getDriver().getAvatar()).into(imgAvtBottom);
        tvBike.setText(mTripInfor.getDriver().getNumber());
        tvBikeName.setText(Html.fromHtml(String.format(getString(R.string.txt_driver), mTripInfor.getDriver().getBrand(), mTripInfor.getDriver().getModel())));
        tvDes.setText(CommonUtils.getGreateAddressStr(mTripInfor.getTrip_info().getEnd()));
        tvFrom.setText(CommonUtils.getGreateAddressStr(mTripInfor.getTrip_info().getStart()));
        tvDes.setSelected(true);
        tvFrom.setSelected(true);
        tvAddDesInfo.setSelected(true);
        tvAddFromInfo.setSelected(true);
        tvAddDesInfo.setText(CommonUtils.getGreateAddressStr(mTripInfor.getTrip_info().getEnd()));
        tvAddFromInfo.setText(CommonUtils.getGreateAddressStr(mTripInfor.getTrip_info().getStart()));
        tvNameDriver.setText(mTripInfor.getDriver().getName());
        if (mTripInfor.getTrip_info() != null && mTripInfor.getTrip_info().getTypeVichle() != 0) {
//            resource = R.drawable.ic_car_view_icon;
            resource = FileUtils.getFolder(this) + getString(R.string.car);

            tvNumberSeat.setText(4 + "");

        } else {
            tvNumberSeat.setText(2 + "");

        }
        if (mTripInfor.getTrip_info().getStatus() == SocketConstants.STATUS_ONGOING) {
            llDiverBottom.setVisibility(View.VISIBLE);
            llAddBottom.setVisibility(View.GONE);
        } else {
            llDiverBottom.setVisibility(View.GONE);
            llAddBottom.setVisibility(View.VISIBLE);
        }
        tvRateDriver.setText(Html.fromHtml(String.format(getString(R.string.xep_hang), mTripInfor.getDriver().getVote())));
        tvRateDriverRateVote.setText(Html.fromHtml(String.format(getString(R.string.xep_hang), mTripInfor.getDriver().getVote())));

        if (promotionCost != null) {
            int total = mTripInfor.getTrip_info().getPrice() - Integer.parseInt(promotionCost.getValue());
            if (total < 0) total = 0;
            tvTotalCost.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.GERMAN).format(total) + " vnđ"));
            getmSetting().put(Constants.PROMOTION_BOOKING, new Gson().toJson(promotionCost));
        } else {
            tvTotalCost.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.GERMAN).format(mTripInfor.getTrip_info().getPrice()) + " vnđ"));

        }
        ratingBar.setRating(mTripInfor.getDriver().getVote());
//        Glide.with(getApplicationContext()).load("https://" + mTripInfor.getDriver().getAvatar()).asBitmap().into(target);
        Picasso.with(getApplicationContext()).load("https://" + mTripInfor.getDriver().getAvatar()).into(imgAvt);
//        Glide.with(getApplicationContext()).load("https://" + mTripInfor.getDriver().getAvatar()).asBitmap().into(imgAvtBottom);
        Picasso.with(getApplicationContext()).load("https://" + mTripInfor.getDriver().getAvatar()).into(imgAvtBottom);

        tvRateDriverBottom.setText(mTripInfor.getDriver().getVote() + "");
        tvDriverNameBottom.setText(mTripInfor.getDriver().getName());
        tvCodeBikeBottom.setText(mTripInfor.getDriver().getNumber());
        ratingBarBottom.setRating(mTripInfor.getDriver().getVote());
//                getmSetting().put(Constants.BOOKING,"");
        getmSocket().reconnectlistenToRoomBooking(mTripInfor.getTrip_info().getIdTrip());
        if (mTripInfor.getTrip_info().getStatus() == SocketConstants.STATUS_FINISH) {
            Crouton.cancelAllCroutons();
            isBookingFinish = true;
            getmSocket().isContinuteBooking = false;
            content.setVisibility(View.GONE);
            llcontent.setVisibility(View.GONE);
            makerImb.setVisibility(View.GONE);
            llRootDialog.setVisibility(View.GONE);
            llRootDialogBookingRateVote.setVisibility(View.VISIBLE);
            llRootTimeBookingBottom.setVisibility(View.GONE);
            changeViewToVoting();
            MainApplication.getInstance().setmIdBooking(0);
            MainApplication.getInstance().setmIdDriver(0);
            if (promotionCost != null) {
                int total = mTripInfor.getTrip_info().getPrice() - Integer.parseInt(promotionCost.getValue());
                if (total < 0) total = 0;
//                tvCostRating.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.GERMAN).format(total) + " vnđ"));
            } else {
//                tvCostRating.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.GERMAN).format(mTripInfor.getTrip_info().getPrice()) + " vnđ"));
            }
            getmSocket().showNotificationInStack();

        }
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            // do something with the bitmap
            // for demonstration purposes, let's just set it to an ImageView
            imgAvt.setImageBitmap(bitmap);
        }
    };
}
