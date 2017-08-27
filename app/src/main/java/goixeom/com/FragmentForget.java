package goixeom.com;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import goixeom.com.activities.MainActivity;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.fragments.BaseFragment;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.utils.Constants;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class FragmentForget extends BaseFragment {
    @BindView(R.id.img_send)
    ImageView imgSend;
    Unbinder unbinder;
//    private ViewPager viewPager;
    private TextView txt3;
    private ImageView img_ok;
    private boolean isResent = false;
    CountDownTimer timer =new CountDownTimer(300000,1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
                isResent =false;
        }
    };
    public FragmentForget() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_forget_passwork, container, false);
//        viewPager = (ViewPager) MainActivity.getInstance().findViewById(R.id.viewpager);
        img_ok = (ImageView) view.findViewById(R.id.img_ok);
        img_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).trantoTab(new FragmentPass());

//                viewPager.setCurrentItem(Constants.POS_LOGIN);
            }
        });
        txt3 = (TextView) view.findViewById(R.id.txt3);
        txt3.setText(Html.fromHtml(getString(R.string.txt_take_pass)));
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        timer.cancel();
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.img_send)
    public void onViewClicked() {
        if(isResent){
            timer.start();
            showDialogErrorContent("Vui lòng đợi 5 phút tiếp theo để gửi lại mật khẩu");
            return;
        }
        String phone = getmSetting().getString(Constants.PHONE);
        String email = getmSetting().getString(Constants.EMAIL);
        if(phone.isEmpty() || email.isEmpty()) return;
        getDialogProgress().showDialog();
        Call<ApiResponse<String>> forgotPassword = getmApi().forgotPassword(ApiConstants.API_KEY,phone,email);
        forgotPassword.enqueue(new CallBackCustom<ApiResponse<String>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> object) {
                if(object.getStatus() == ApiConstants.CODE_SUCESS){
                    Toast.makeText(getContext(),getString(R.string.forgot_password_msg),Toast.LENGTH_SHORT).show();
                    isResent = true;
                }else{
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }
}
