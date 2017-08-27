package goixeom.com;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class FragmentEmail extends BaseFragment {
    @BindView(R.id.edt_passwork_login)
    EditText edtPassworkLogin;
    Unbinder unbinder;
    private ImageView img_send;
//    private ViewPager viewpager;

    public FragmentEmail() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_email, container, false);
        img_send = (ImageView) view.findViewById(R.id.img_send);
//        viewpager = (ViewPager) MainActivity.getInstance().findViewById(R.id.viewpager);
        click();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void click() {
        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPassworkLogin.getText().toString().isEmpty()){
                    showDialogErrorContent(getString(R.string.please_enter_email_2));
                    return;
                }
                if (!isValidEmail(edtPassworkLogin.getText().toString())){
                    showDialogErrorContent(getString(R.string.please_enter_email));
                    return;
                }
                String phone = getmSetting().getString(Constants.PHONE);
                if(phone.isEmpty()) return;
                getDialogProgress().showDialog();
                Call<ApiResponse<String>> forgotPassword = getmApi().forgotPassword(ApiConstants.API_KEY,phone,edtPassworkLogin.getText().toString());
                forgotPassword.enqueue(new CallBackCustom<ApiResponse<String>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<String>>() {
                    @Override
                    public void onResponse(ApiResponse<String> object) {
                        if(object.getStatus() == ApiConstants.CODE_SUCESS){
                            getmSetting().put(Constants.EMAIL,edtPassworkLogin.getText().toString());
//                            viewpager.setCurrentItem(Constants.POS_FORGET);
                            ((MainActivity)getActivity()).trantoTab(new FragmentForget());

                        }else{
                                showDialogErrorContent(getString(R.string.emaill_exist));
                        }
                    }
                }));

            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
