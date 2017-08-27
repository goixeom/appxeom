package goixeom.com;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.PhoneUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import goixeom.com.activities.MainActivity;
import goixeom.com.activities.MapsActivity;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.fragments.BaseFragment;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.User;
import goixeom.com.utils.Constants;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class FragmentPass extends BaseFragment {
    @BindView(R.id.edt_passwork_login)
    EditText edtPassworkLogin;
    Unbinder unbinder;
    @BindView(R.id.txt_forget_passowrk)
    CustomTextView txtForgetPassowrk;
    private ImageView img_next1;
//    private ViewPager view/Pager;

    public FragmentPass() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_passwork, container, false);
        unbinder = ButterKnife.bind(this, view);
//        viewPager = (ViewPager) MainActivity.getInstance().findViewById(R.id.viewpager);
        img_next1 = (ImageView) view.findViewById(R.id.img_next1);
        img_next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyPass(edtPassworkLogin.getText().toString())) {
                    String phone = MainApplication.getInstance().getPhoneNumber();
                    if (phone == null || phone.isEmpty()) return;
                    getDialogProgress().showDialog();
                    String imei = PhoneUtils.getIMEI();
                    Call<ApiResponse<User>> login = getmApi().login(ApiConstants.API_KEY, phone, edtPassworkLogin.getText().toString(),imei);
                    login.enqueue(new CallBackCustom<ApiResponse<User>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<User>>() {
                        @Override
                        public void onResponse(ApiResponse<User> object) {
                            if (object.getData() != null) {
                                MainApplication.getInstance().setmUser(object.getData());

                                if(object.getData().getEmail() ==null || object.getData().getEmail().isEmpty()
                                        || object.getData().getName()==null ||  object.getData().getName().isEmpty()
                                        || object.getData().getPhone()==null ||  object.getData().getPhone().isEmpty()){
                                    MainApplication.getInstance().setmIdAuth(object.getData().getId());
//                                    getmSetting().put(Constants.ID,object.getData().getId());
                                    ((MainActivity)getActivity()).trantoTab(new FragmentUpdate());
                                    return;
                                }
                                getmSetting().put(Constants.ID,object.getData().getId());
                                MainApplication.getInstance().setmIdAuth("");
                                Intent intent = new Intent(getContext(), MapsActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                getActivity().finish();
                            } else {
                                new MaterialDialog.Builder(getContext()).title(R.string.error)
                                        .content(object.getMessage())
                                        .positiveText(R.string.dismis)
                                        .positiveColor(Color.GRAY)
                                        .show();
                            }
                        }
                    }));
                }
//                viewPager.setCurrentItem(3);

            }
        });
        txtForgetPassowrk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        ((TextView)view).setTextColor(ContextCompat.getColor(getContext(),R.color.green)); //white
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        ((TextView)view).setTextColor(ContextCompat.getColor(getContext(),R.color.gray)); //black
                        break;
                }
                return false;
            }
        });
        return view;
    }

    private boolean verifyPass(String name) {
        if (name.isEmpty()) {
            new MaterialDialog.Builder(getContext()).title(R.string.error)
                    .content(getString(R.string.enter_pass))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
//        if (name.length()<6) {
//            new MaterialDialog.Builder(getContext()).title(R.string.error)
//                    .content("Mật khẩu phải từ 6 ký tự trở lên")
//                    .positiveText(R.string.dismis)
//                    .positiveColor(Color.GRAY)
//                    .show();
//            return false;
//        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    @OnClick(R.id.txt_forget_passowrk)
    public void onViewClicked() {
//        viewPager.setCurrentItem(Constants.POS_EMAIL);
        ((MainActivity)getActivity()).trantoTab(new FragmentEmail());

    }
}
