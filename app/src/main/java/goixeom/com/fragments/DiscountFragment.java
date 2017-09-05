package goixeom.com.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import carbon.widget.Button;
import goixeom.com.CustomTextView;
import goixeom.com.R;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.Discount;
import goixeom.com.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscountFragment extends Fragment {


    @BindView(R.id.frame)
    FrameLayout frame;
    @BindView(R.id.img)
    RoundedImageView img;
    @BindView(R.id.tv_name)
    CustomTextView tvName;
    @BindView(R.id.tv_content)
    CustomTextView tvContent;
    @BindView(R.id.code)
    Button code;
    @BindView(R.id.time_start)
    CustomTextView timeStart;
    @BindView(R.id.time_end)
    CustomTextView timeEnd;
    @BindView(R.id.fab_back)
    FloatingActionButton fabBack;
    Unbinder unbinder;
    OnResponse<Boolean> callBackNextPage;
    public DiscountFragment() {
        // Required empty public constructor
    }

    public OnResponse<Boolean> getCallBackNextPage() {
        return callBackNextPage;
    }

    public void setCallBackNextPage(OnResponse<Boolean> callBackNextPage) {
        this.callBackNextPage = callBackNextPage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discount, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Discount discount = getArguments().getParcelable(Constants.MSG);
            if (discount != null) {
                tvContent.setText(discount.getTitle());
                tvName.setText(discount.getPr_code());
                code.setText(discount.getPr_code());
                timeStart.setText(discount.getStart_time());
                timeEnd.setText(discount.getEnd_time());
                if(discount.getImg()!=null && !discount.getImg().isEmpty()){
                    Picasso.with(getContext()).load(discount.getImg()).into(img);
                }else{
                    Picasso.with(getContext()).load("https://goixeom.com/images/xeomer/duong-nguyen.jpg").into(img);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab_back)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }

    @OnClick({R.id.btn_previous, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_previous:
                    callBackNextPage.onResponse(false);
                break;
            case R.id.btn_next:
                callBackNextPage.onResponse(true);

                break;
        }
    }
}
