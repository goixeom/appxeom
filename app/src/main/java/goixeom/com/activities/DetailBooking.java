package goixeom.com.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import goixeom.com.CustomTextView;
import goixeom.com.R;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.TripInforModel;
import goixeom.com.utils.CommonUtils;
import goixeom.com.utils.Constants;
import retrofit2.Call;

public class DetailBooking extends BaseActivity {


    int id;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_from)
    CustomTextView tvFrom;
    @BindView(R.id.tv_des)
    CustomTextView tvDes;
    @BindView(R.id.tv_discount_booking)
    CustomTextView tvDiscountBooking;


    @BindView(R.id.tv_total)
    CustomTextView tvTotal;

    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.tv_name_driver)
    CustomTextView tvNameDriver;
    @BindView(R.id.tv_number_driver)
    CustomTextView tvNumberDriver;
    @BindView(R.id.tv_type_driver)
    CustomTextView tvTypeDriver;
    @BindView(R.id.tv_vote_driver)
    CustomTextView tvVoteDriver;
    @BindView(R.id.discount)
    LinearLayout discount;
    @BindView(R.id.img_call)
    ImageView imgCall;
    @BindView(R.id.tv_id_booking)
    CustomTextView tvIdBooking;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.tv_date)
    CustomTextView tvDate;
    @BindView(R.id.tv_time)
    CustomTextView tvTime;
    @BindView(R.id.tv_rate)
    CustomTextView tvRate;
    @BindView(R.id.ll_rate)
    LinearLayout llRate;
    @BindView(R.id.ll_from)
    LinearLayout llFrom;
    @BindView(R.id.tv_go_detail)
    CustomTextView tvGoDetail;

    private GoogleMap mMap;
    private String urlStatic = "https://maps.googleapis.com/maps" +
            "/api/staticmap?" +
            "size=500x780" +
            "&zoom=13" +
            "&markers=458%20Minh%20Khai,%20Khu%20%C4%91%C3%B4%20th%E1%BB%8B%20Times%20City,%20V%C4%A9nh%20Tuy,%20Hai%20B%C3%A0%20Tr%C6%B0ng,%20H%C3%A0%20N%E1%BB%99i,%20Vi%E1%BB%87t%20Nam" +
            "&markers=291%20Hu%E1%BA%BF,%20L%C3%AA%20%C4%90%E1%BA%A1i%20H%C3%A0nh" +
            "&path=enc:{ec_CimdeS?`DC@KFENDLLFF?EpGWV}J@mE?e@DwC~@o@PKLlClKr@jC?PALENGNOHaIn@a@FBfAyBHeERwNt@qGVmMp@sHb@kBJkAAaAKg@GATEnDKlNAhHHfAz@pHZfCAd@BjE?d@ITIRERWRYJuCKqDM" +
            "&key=AIzaSyBkqHz9U6vIFYhBtmSWOajaC05zF97YKuQ";

    @Override
    public void pingNotification(String title, String content) {

    }

    @Override
    protected void onSoketConnected() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_booking);
        ButterKnife.bind(this);
        view = llRoot;
        tvDes.setSelected(true);
        tvFrom.setSelected(true);
        tvNameDriver.setSelected(true);
        if (getIntent() != null) {
            id = getIntent().getIntExtra(Constants.BOOKING, -1);
            if (id != -1) {
                getDetailBooking(id);
            }
        }
    }


    private void getDetailBooking(int idBooking) {
        getDialogProgress().showDialog();
        Call<ApiResponse<TripInforModel>> getDetailBooking = getmApi().getDetailBooking(ApiConstants.API_KEY, idBooking);
        getDetailBooking.enqueue(new CallBackCustom<ApiResponse<TripInforModel>>(this, getDialogProgress(), new OnResponse<ApiResponse<TripInforModel>>() {
            @Override
            public void onResponse(ApiResponse<TripInforModel> object) {
                if (object.getData() != null) {
                    tvFrom.setText(CommonUtils.getGreateAddressStr(object.getData().getTrip_info().getStart()));
                    tvDes.setText(CommonUtils.getGreateAddressStr(object.getData().getTrip_info().getEnd()));
                    if (object.getData().getTrip_info().getCode() == null || object.getData().getTrip_info().getCode().isEmpty()) {
                        discount.setVisibility(View.GONE);
                    } else {
                        tvDiscountBooking.setText(object.getData().getTrip_info().getCode());
                    }
                    tvTime.setText(object.getData().getTrip_info().getDate());
                    String[] timeSplit = object.getData().getTrip_info().getDate().split("-");
                    tvDate.setText(timeSplit[0]);
                     tvTime.setText("Lúc " + timeSplit[1]);
                    tvTotal.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(object.getData().getTrip_info().getPrice()) + " vnđ");
                    tvNameDriver.setText(object.getData().getDriver().getName());
                    tvNumberDriver.setText(object.getData().getDriver().getNumber());
                    tvTypeDriver.setText(object.getData().getDriver().getModel());
                    tvGoDetail.setText(CommonUtils.round((double) (object.getData().getTrip_info().getDistance()),1)+" Km");
                    tvVoteDriver.setText(object.getData().getDriver().getVote() + "");
                    tvIdBooking.setText(object.getData().getTrip_info().getIdTrip() + "");
                    Glide.with(getApplicationContext()).load("https://" + object.getData().getDriver().getAvatar()).asBitmap().into(profileImage);
                    int rate =object.getData().getTrip_info().getVote();
                    if (rate >= 4) {
                        llRate.setBackgroundResource(R.drawable.ic_rectangle_rated);
                    } else if (rate == 3) {
                        llRate.setBackgroundResource(R.drawable.ic_rectangle_rated_gray);
                    } else {
                        llRate.setBackgroundResource(R.drawable.ic_rectangle_rated_black);
                    }
                   tvRate.setText(rate + "★");
                    // TODO: 8/11/2017 distance
                    llRoot.setVisibility(View.VISIBLE);
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    @OnClick({R.id.img_back, R.id.img_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_call:
                CommonUtils.intentToCall("02462931011", this);
                break;
        }
    }


}
