package goixeom.com.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goixeom.com.R;
import goixeom.com.utils.Constants;

public class BasePrivacy extends BaseFragment {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.web)
    WebView web;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_privacy, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        boolean isTerm = getArguments().getBoolean(Constants.MSG, false);
        if (isTerm) {
            tvTitle.setText(Html.fromHtml(getString(R.string.txt_term)));
            web.loadUrl("file:///android_asset/term.html");
        } else {
            tvTitle.setText(Html.fromHtml(getString(R.string.txt_privacy)));
            web.loadUrl("file:///android_asset/privacy.html");
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        getFragmentManager().popBackStack();
    }
}
