package goixeom.com.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goixeom.com.R;
import goixeom.com.adapters.ViewpagerAdapter;
import goixeom.com.fragments.DiscountFragment;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.Discount;
import goixeom.com.utils.Constants;

public class DetailDiscountActivity extends AppCompatActivity implements OnResponse<Boolean> {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private ViewpagerAdapter adapter;
    List<Discount> listDiscounts;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_discount);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            Bundle b = getIntent().getBundleExtra(Constants.BUNDLE);
            listDiscounts = b.getParcelableArrayList(Constants.MSG);
            position = b.getInt(Constants.POSITION);
        }
        adapter = new ViewpagerAdapter(getSupportFragmentManager());
        for (Discount item : listDiscounts) {
            DiscountFragment fm = new DiscountFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.MSG, item);
            fm.setArguments(bundle);
            fm.setCallBackNextPage(this);
            adapter.addFragment(fm);
        }
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(position);
    }

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onResponse(Boolean object) {
        int index = viewpager.getCurrentItem();
        if (object.booleanValue()) {
            //next to page
            if (index == adapter.getCount() - 1) return;
            viewpager.setCurrentItem(++index);
        } else {
            if (index == 0) return;
            viewpager.setCurrentItem(--index);
        }
    }
}
