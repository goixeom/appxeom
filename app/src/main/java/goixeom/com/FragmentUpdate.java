package goixeom.com;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import goixeom.com.activities.ActivityPrivacy;
import goixeom.com.activities.MainActivity;
import goixeom.com.activities.MapsActivity;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.fragments.BaseFragment;
import goixeom.com.fragments.BasePrivacy;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.StringResponse;
import goixeom.com.models.User;
import goixeom.com.models.VerifyCode;
import goixeom.com.utils.Constants;
import goixeom.com.views.ClickPreventableTextView;
import retrofit2.Call;

/**
 * Created by Huy on 6/9/2017.
 */

public class FragmentUpdate extends BaseFragment {
    @BindView(R.id.edt_first_name)
    EditText edtFirstName;
    @BindView(R.id.edt_last_name)
    EditText edtLastName;
    static EditText edtPhone;
     static EditText edtEmail;
    @BindView(R.id.img_send1)
    ImageView imgSend;
    Unbinder unbinder;
    private ImageView img_send1;
    private ClickPreventableTextView txt;
    //    private ViewPager viewpager;
    private boolean isNeedToVerifyPhone;

    public FragmentUpdate() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    boolean isPrivacyClicked = false;
    boolean isTermClicked = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_update, container, false);
        img_send1 = (ImageView) view.findViewById(R.id.img_send1);
        edtEmail = (EditText) view.findViewById(R.id.edt_email);
        edtPhone = (EditText) view.findViewById(R.id.edt_phone);
        txt = (ClickPreventableTextView) view.findViewById(R.id.txt1);
//        viewpager = (ViewPager) MainActivity.getInstance().findViewById(R.id.viewpager);
        click();
        Spannable span = Spannable.Factory.getInstance().newSpannable("Bằng cách tiếp tục, tôi xác nhận rằng tôi đã đọc và đồng ý với điều khoản và chính sách quyền riêng tư");
        int length = span.length();
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                if (isTermClicked) {
                    isTermClicked = false;
                    return;
                }
                Intent i = new Intent(getActivity(), ActivityPrivacy.class);
                i.putExtra(Constants.MSG, true);

                Bundle b = new Bundle();
                b.putBoolean(Constants.MSG, true);
                BasePrivacy basePrivacy = new BasePrivacy();
                basePrivacy.setArguments(b);
                ((MainActivity) getActivity()).tranToTabRoot(basePrivacy);
//                startActivity(i);
                isTermClicked = true;


            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, length - 39, length - 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                if (isPrivacyClicked) {
                    isPrivacyClicked = false;
                    return;
                }
                Intent i = new Intent(getActivity(), ActivityPrivacy.class);
                i.putExtra(Constants.MSG, false);
                Bundle b = new Bundle();
                b.putBoolean(Constants.MSG, false);
                BasePrivacy basePrivacy = new BasePrivacy();
                basePrivacy.setArguments(b);
                ((MainActivity) getActivity()).tranToTabRoot(basePrivacy);
//                startActivity(i);
                isPrivacyClicked = true;


            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, length - 25, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        StyleSpan boldSpan1 = new StyleSpan(Typeface.BOLD);
        span.setSpan(new ForegroundColorSpan(Color.rgb(45, 178, 93)), length - 39, length - 29, 0);
//        span.setSpan(boldSpan1, length - 39, length - 29, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.rgb(255, 106, 106)), length - 25, length, 0);
//        span.setSpan(boldSpan, length - 25, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt.setText(span, TextView.BufferType.SPANNABLE);
        txt.setMovementMethod(LinkMovementMethod.getInstance());
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
//        edtEmail.setEnabled(false);
    }
    User user;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       user  = MainApplication.getInstance().getmUser();
        if (user != null) {
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                edtPhone.setText(user.getPhone());
                edtPhone.setEnabled(false);
                edtPhone.setFocusable(false);

            }
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                edtEmail.setText(user.getEmail());
                edtEmail.setEnabled(false);
                edtEmail.setFocusable(false);
            }
            edtLastName.setText(user.getName());
        }
        if (getArguments() != null) {
            isNeedToVerifyPhone = getArguments().getBoolean(Constants.ISFROMSOCIAL);
        }
//        edtEmail.setEnabled(MainApplication.getInstance().isEnableEmail());
//        edtPhone.setEnabled(MainApplication.getInstance().isEnablePhone());
//        edtLastName.setFilters(new InputFilter[] {
//                new InputFilter() {
//                    @Override
//                    public CharSequence filter(CharSequence cs, int start,
//                                               int end, Spanned spanned, int dStart, int dEnd) {
//                        // TODO Auto-generated method stub
//                        if(cs.equals(" ")){ // for backspace
//                            return cs;
//                        }
//                        if(cs.toString().matches("[a-zA-Z ]+")){
//                            return cs;
//                        }
//                        return cs.subSequence(start,dEnd);
//                    }
//                }
//        });

//       view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(this);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean(Constants.ENABLE_PHONE, MainApplication.getInstance().isEnablePhone());
//        outState.putBoolean(Constants.ENABLE_EMAIL, MainApplication.getInstance().isEnableEmail());
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
//        edtPhone.setEnabled(MainApplication.getInstance().isEnablePhone());

    }

    public void click() {
        img_send1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getText().toString();
                String firstName = edtFirstName.getText().toString();
                String lastName = edtLastName.getText().toString();
                String email = edtEmail.getText().toString();
                if (!verifyPhoneSucess(phone)) {
                    return;
                }
//                if (!verifyFirstName(firstName)) {
//                    return;
//                }
                if (!verifyLastName(lastName)) {
                    return;
                }
                if (!isValidEmailAddress(email)) {
                    return;
                }
                if (isNeedToVerifyPhone) {
                    checkPhoneThenVerify(phone, email, firstName, lastName);

                } else {
                    updateInformation(phone, firstName, lastName, email);
                }
            }
        });
    }

    private void checkPhoneThenVerify(final String phone, final String email, final String firstName, final String lastName) {
        getDialogProgress().showDialog();
        Call<ApiResponse<StringResponse>> checkPhoneExist = getmApi().checkPhoneExist(ApiConstants.API_KEY, phone);
        checkPhoneExist.enqueue(new CallBackCustom<ApiResponse<StringResponse>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<StringResponse>>() {
            @Override
            public void onResponse(ApiResponse<StringResponse> object) {
                getmSetting().put(Constants.PHONE, phone);
                if (object.getData() != null) {
                    showDialogErrorContent(getString(R.string.phone_exist));
                } else {
                    if (object.getStatus() == ApiConstants.CODE_ERROR_NO_EXIST_ACC) {
                        sendSms(phone, email, firstName, lastName);
                    }
                }
            }
        }));
    }

    private void sendSms(final String phone, final String email, final String firstName, final String lastName) {
        if (MainApplication.getInstance().getCountDownSendCode() != -1
                && MainApplication.getInstance().getBundleSendCode() != null
                && MainApplication.getInstance().getBundleSendCode().getParcelable(Constants.USER) != null) {
            User user = MainApplication.getInstance().getBundleSendCode().getParcelable(Constants.USER);
            if (user.getPhone() != null && user.getPhone().equals(phone)) {
                FragmentCode fgCode = new FragmentCode();
                fgCode.setArguments(MainApplication.getInstance().getBundleSendCode());
                ((MainActivity) getActivity()).trantoTab(fgCode);
                return;
            }
        }
        MainApplication.getInstance().setCountDownSendCode(-1);
        Call<ApiResponse<VerifyCode>> sms = getmApi().sendSMS(ApiConstants.API_KEY, phone);
        getDialogProgress().showDialog();
        sms.enqueue(new CallBackCustom<ApiResponse<VerifyCode>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<VerifyCode>>() {
            @Override
            public void onResponse(ApiResponse<VerifyCode> object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    LogUtils.d("Sent code to device");
                    FragmentCode fgCode = new FragmentCode();
                    Bundle bundle = new Bundle();
                    User user = new User();
                    user.setEmail(email);
                    user.setId(getmSetting().getString(Constants.ID));
                    user.setName(firstName + " " + lastName);
                    user.setPhone(phone);
                    bundle.putParcelable(Constants.USER, user);
                    bundle.putString(Constants.CODE_VERIFY, object.getData().getCode());
                    fgCode.setArguments(bundle);
                    MainApplication.getInstance().setBundleSendCode(bundle);
                    ((MainActivity) getActivity()).trantoTab(fgCode);
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }

    private void updateInformation(String phone, String firstName, String lastName, String email) {
        getDialogProgress().showDialog();
        Call<ApiResponse<String>> updateProfile = getmApi().updateInformation(ApiConstants.API_KEY, phone, MainApplication.getInstance().getmIdAuth(), firstName + " " + lastName, email);
        updateProfile.enqueue(new CallBackCustom<ApiResponse<String>>(getContext(), getDialogProgress(), new OnResponse<ApiResponse<String>>() {
            @Override
            public void onResponse(ApiResponse<String> object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    getmSetting().put(Constants.ID, MainApplication.getInstance().getmIdAuth());
                    MainApplication.getInstance().setmIdAuth("");
                    Intent intent = new Intent(getContext(), MapsActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    getActivity().finish();
                } else {
                    showDialogErrorContent(object.getMessage());
                }
            }
        }));
    }


    public boolean isValidEmailAddress(String email) {
        if (email.isEmpty()) {
            showDialogErrorContent(getString(R.string.please_enter_email_2));
            return false;
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty() && !email.equals(user.getEmail())) {
            showDialogErrorContent("Email không trùng khớp với tài khoản liên kết");
            return false;
        }
        if (!RegexUtils.isEmail(email) || !isUTF8MisInterpreted(email)) {
            new MaterialDialog.Builder(getContext()).title(R.string.error)
                    .content(getString(R.string.please_enter_email))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        return true;
    }

    private boolean verifyPhoneSucess(String phone) {

        if (phone.isEmpty()) {
            new MaterialDialog.Builder(getContext()).title(R.string.error)
                    .content(getString(R.string.empty_phone))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        if (user.getPhone() != null && !user.getPhone().isEmpty() && !phone.equals(user.getPhone())) {
            showDialogErrorContent("Số điện thoại không trùng khớp với tài khoản");
            return false;
        }
        if ((phone.length() < 10) || phone.length() > 11) {
            new MaterialDialog.Builder(getContext()).title(R.string.error)
                    .content(getString(R.string.please_enter_phone))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        if (phone.length() == 10) {
            if (!phone.substring(0, 2).equals("09")) {
                new MaterialDialog.Builder(getContext()).title(R.string.error)
                        .content(getString(R.string.please_enter_phone))
                        .positiveText(R.string.dismis)
                        .positiveColor(Color.GRAY)
                        .show();
                return false;
            }
        } else {
            if (!phone.substring(0, 2).equals("01")) {
                new MaterialDialog.Builder(getContext()).title(R.string.error)
                        .content(getString(R.string.please_enter_phone))
                        .positiveText(R.string.dismis)
                        .positiveColor(Color.GRAY)
                        .show();
                return false;
            }
        }


        return true;
    }

    public static boolean isUTF8MisInterpreted(String input) {
        //convenience overload for the most common UTF-8 misinterpretation
        //which is also the case in your question
        return isUTF8MisInterpreted(input, "Windows-1252");
    }

    public static boolean isUTF8MisInterpreted(String input, String encoding) {

        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        CharsetEncoder encoder = Charset.forName(encoding).newEncoder();
        ByteBuffer tmp;
        try {
            tmp = encoder.encode(CharBuffer.wrap(input));
        } catch (CharacterCodingException e) {
            return false;
        }

        try {
            decoder.decode(tmp);
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
    }


    private boolean verifyLastName(String name) {
        if (name.isEmpty()) {
            new MaterialDialog.Builder(getContext()).title(R.string.error)
                    .content(getString(R.string.please_enter_lname))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        if (name.length() < 4) {
            new MaterialDialog.Builder(getContext()).title(R.string.error)
                    .content("Họ và tên ít nhất 4 ký tự")
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        Pattern regex = Pattern.compile("[$/\\(){}~!%^*&+,'\"<>_\\_`.:;=?@#|]");
        Matcher matcher = regex.matcher(name);
        if (name.matches(".*\\d.*") || matcher.find()) {
            new MaterialDialog.Builder(getContext()).title(R.string.error)
                    .content(getString(R.string.incorrect_name))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        return true;
    }

    private boolean verifyFirstName(String name) {
        if (name.isEmpty()) {
            new MaterialDialog.Builder(getContext()).title(R.string.error)
                    .content(getString(R.string.please_enter_fname))
                    .positiveText(R.string.dismis)
                    .positiveColor(Color.GRAY)
                    .show();
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainApplication.getInstance().setCountDownSendCode(-1);
        MainApplication.getInstance().setBundleSendCode(null);
    }
}
