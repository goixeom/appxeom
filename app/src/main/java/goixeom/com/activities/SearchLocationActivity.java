package goixeom.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import goixeom.com.R;
import goixeom.com.adapters.AdapterPlace;
import goixeom.com.adapters.OnLoadMoreListener;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.ApiUtils;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.apis.CallBackCustomNoDelay;
import goixeom.com.apis.GoiXeOmAPI;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.MyPlace;
import goixeom.com.models.PlaceNearby;
import goixeom.com.utils.Constants;
import retrofit2.Call;
import se.walkercrou.places.GooglePlaces;

public class SearchLocationActivity extends BaseActivity implements OnResponse<Integer> {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_add)
    ImageView btnAdd;
    @BindView(R.id.txt_add)
    ImageView txtAdd;

    @BindView(R.id.list_item)
    RecyclerView rcv;
    @BindView(R.id.edt_add_from)
    EditText edtAddFrom;
    @BindView(R.id.edt_add_des)
    EditText edtAddDes;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.tv_add_from)
    TextView tvAddFrom;
    @BindView(R.id.tv_add_des)
    TextView tvAddDes;
    @BindView(R.id.ll_root)
    RelativeLayout llRoot;
    @BindView(R.id.btn_clear)
    ImageView btnClear;

    @BindView(R.id.rad_group)
    RadioGroup radGroup;
    @BindView(R.id.rad_recently)
    RadioButton radRecently;
    private ImageView txt_add;
    private List<MyPlace> mListPlace = new ArrayList<>();
    private List<MyPlace> mListPlaceFrom = new ArrayList<>();
    private AdapterPlace mAdapterPlace;
    //    private AdapterPlace mAdapterPlaceFrom;
    private final int TYPE_FROM = 0;
    private final int TYPE_DES = 1;
    GooglePlaces client;
    double lat = 21.0278;
    double lng = 105.8342;
    String latlngStr = "20.993161,105.867588";
    private MyPlace mPlaceFrom;
    private MyPlace mPlaceDes;
    private int toolTipStep = 0;
    private Handler handlerRecyclerview = new Handler();

    @Override
    public void pingNotification(String title, String content) {

    }

    @Override
    protected void onSoketConnected() {

    }

    OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadmore() {
            mListPlace.add(null);
            mAdapterPlace.notifyItemInserted(mListPlace.size() - 1);
            if (listNearBy == null || listNearBy.size() == mListPlace.size()-1) {
                mListPlace.remove(mListPlace.size() - 1);
                mAdapterPlace.notifyItemRemoved(mListPlace.size());
                return;
            }
            handlerRecyclerview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //   remove progress item
                    mListPlace.remove(mListPlace.size() - 1);
                    mAdapterPlace.notifyItemRemoved(mListPlace.size());
                    //add items one by one
                    int start = mListPlace.size();
                    int end = start + 5;
                    if (listNearBy != null && !listNearBy.isEmpty() && !mListPlace.isEmpty()) {
                        for (int index = start; index < end; index++) {
                            if (index >= listNearBy.size()) break;
                            mListPlace.add(listNearBy.get(index));
                            start++;
                        }
                    }
                    mAdapterPlace.notifyDataSetChanged();
                    mAdapterPlace.setLoaded(false);
                }
            }, 2000);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        ButterKnife.bind(this);
        view = llRoot;
        txt_add = (ImageView) findViewById(R.id.txt_add);
        boolean isFromFocus = false;
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                String addressFrom = getIntent().getExtras().getString(Constants.FROM);
                lat = getIntent().getExtras().getDouble(Constants.LAT);
                lng = getIntent().getExtras().getDouble(Constants.LNG);
                edtAddFrom.setText(addressFrom);
                mPlaceFrom = new MyPlace();
                mPlaceFrom.setDescription(addressFrom);
                mPlaceFrom.setLat(lat);
                mPlaceFrom.setLng(lng);
//                edtAddDes.setText(getIntent().getExtras().getString(Constants.DES));
                isFromFocus = getIntent().getExtras().getBoolean(Constants.IS_FROM_FOCUS, false);

            }
        }
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setHasFixedSize(true);
        mAdapterPlace = new AdapterPlace(rcv, mListPlace, this, this, new OnResponse<Integer>() {
            @Override
            public void onResponse(Integer object) {
                getLocationToAddFav(mListPlace.get(object.intValue()));
            }
        }, new OnResponse<Integer>() {
            @Override
            public void onResponse(Integer object) {
                clearFavourite(mListPlace.get(object.intValue()));
            }
        });
        rcv.setAdapter(mAdapterPlace);
        mAdapterPlace.setOnLoadMoreListener(onLoadMoreListener);
        client = new GooglePlaces(getString(R.string.google_map_id));
        edtAddDes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getAddress(charSequence.toString(), TYPE_DES);
                //  new PlaceAsyn().execute(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtAddFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getAddress(charSequence.toString(), TYPE_FROM);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvAddFrom.setText(edtAddFrom.getText());
        tvAddFrom.setSelected(true);
        edtAddFrom.setVisibility(View.GONE);
        tvAddDes.setText(edtAddDes.getText());
        tvAddDes.setSelected(true);
        edtAddDes.setVisibility(View.GONE);
        edtAddDes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (!focus) {
                    tvAddDes.setVisibility(View.VISIBLE);
                    tvAddDes.setSelected(true);
                    edtAddDes.setVisibility(View.GONE);
                } else {
                    tvAddDes.setVisibility(View.GONE);
                    edtAddDes.setVisibility(View.VISIBLE);
                }
            }
        });
        edtAddFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (!focus) {
                    tvAddFrom.setVisibility(View.VISIBLE);
                    tvAddFrom.setSelected(true);
                    edtAddFrom.setVisibility(View.GONE);
                } else {
                    tvAddFrom.setVisibility(View.GONE);
                    edtAddFrom.setVisibility(View.VISIBLE);
                }
            }
        });
        tvAddFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAddFrom.setVisibility(View.GONE);
                edtAddFrom.setVisibility(View.VISIBLE);
                edtAddFrom.requestFocus();
                edtAddFrom.setText("");
                KeyboardUtils.showSoftInput(edtAddFrom);
                btnAdd.setVisibility(View.VISIBLE);
                btnClear.setVisibility(View.GONE);

            }
        });
        tvAddDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAddDes.setVisibility(View.GONE);
                edtAddDes.setVisibility(View.VISIBLE);
                edtAddDes.requestFocus();
                edtAddDes.setText("");
                KeyboardUtils.showSoftInput(edtAddDes);
                btnClear.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);

            }
        });

        if (!isFromFocus) {
            tvAddDes.performClick();
        } else {
            tvAddFrom.performClick();
        }

//        getmSetting().put(Constants.TOOLTIP, true);

        radRecently.setChecked(true);
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                rcv.setAdapter(null);
                mAdapterPlace.clearSparse();
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rad_bus: {
                        getNearby(getString(R.string.bus));
                        break;
                    }
                    case R.id.rad_shop: {
                        getNearby(getString(R.string.shop));
                        break;
                    }
                    case R.id.rad_res: {
                        getNearby(getString(R.string.food));
                        break;
                    }
                    case R.id.rad_fav: {
                        getListFavourite();
                        break;
                    }
                    case R.id.rad_recently: {
                        getListRecently();
                        break;
                    }
                    default: {
                        getListRecently();
                    }
                }
            }
        });
        getListRecently();
    }

    private void clearFavourite(MyPlace place) {
        Call<ApiResponse> addToFavourite = getmApi().clearToFavourite(ApiConstants.API_KEY, place.getId());
        addToFavourite.enqueue(new CallBackCustomNoDelay<ApiResponse>(this, new OnResponse<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    LogUtils.e("clear to add favourite");
                }
            }
        }));
    }

    private void addToFavourite(MyPlace place) {
        Call<ApiResponse> addToFavourite = getmApi().addAddressToFavourite(ApiConstants.API_KEY, getmSetting().getString(Constants.ID), place.getName(), place.getDescription(), place.getType() == null ? "" : place.getType(), place.getLat(), place.getLng());
        addToFavourite.enqueue(new CallBackCustomNoDelay<ApiResponse>(this, new OnResponse<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse object) {
                if (object.getStatus() == ApiConstants.CODE_SUCESS) {
                    LogUtils.e("sussecc to add favourite");
                }
            }
        }));
    }

    public void getListRecently() {
        Call<ApiResponse<List<MyPlace>>> getListRecent = getmApi().getListRecently(ApiConstants.API_KEY, getmSetting().getString(Constants.ID));
        getListRecent.enqueue(new CallBackCustomNoDelay<ApiResponse<List<MyPlace>>>(this, new OnResponse<ApiResponse<List<MyPlace>>>() {
            @Override
            public void onResponse(ApiResponse<List<MyPlace>> object) {
                if (object.getData() != null && object.getData().size() != 0) {
                    loading.setVisibility(View.GONE);
                    mListPlace.clear();
                    if (listNearBy != null)
                        listNearBy.clear();
                    mListPlaceFrom.clear();
                    for (MyPlace p : object.getData()) {
                        p.setDescription(p.getAddress());
                        listNearBy.add(p);
                        if(mListPlace.size()<=5)
                        mListPlace.add(p);
                    }
                    rcv.setAdapter(mAdapterPlace);
                    mAdapterPlace.notifyDataSetChanged();
                } else {
                    LogUtils.e(object.getMessage());
                    loading.setVisibility(View.GONE);
                }
            }
        }));
    }

    public void getListFavourite() {
        Call<ApiResponse<List<MyPlace>>> getListRecent = getmApi().getListFavourite(ApiConstants.API_KEY, getmSetting().getString(Constants.ID));
        getListRecent.enqueue(new CallBackCustomNoDelay<ApiResponse<List<MyPlace>>>(this, new OnResponse<ApiResponse<List<MyPlace>>>() {
            @Override
            public void onResponse(ApiResponse<List<MyPlace>> object) {
                if (object.getData() != null ) {
                    loading.setVisibility(View.GONE);
                    mListPlace.clear();
                    mListPlaceFrom.clear();
                    if (listNearBy != null)
                        listNearBy.clear();
                    for (MyPlace p : object.getData()) {
                        p.setDescription(p.getAddress());
                        listNearBy.add(p);
                        if(mListPlace.size()<=5)
                        mListPlace.add(p);
                    }

                    rcv.setAdapter(mAdapterPlace);
                    mAdapterPlace.notifyDataSetChanged();
                } else {
                    LogUtils.e(object.getMessage());
                    loading.setVisibility(View.GONE);
                }
            }
        }));
    }

    public void getAddress(String intput, final int type) {
        if (lat != 0 && lng != 0) {
            latlngStr = lat + "," + lng;
        }
        Call<ApiResponse<List<MyPlace>>> getAdress = ApiUtils.getAPIPLACE().create(GoiXeOmAPI.class).getAddress(intput, "geocode", true, latlngStr, 60000, getString(R.string.google_map_id));
        getAdress.enqueue(new CallBackCustom<ApiResponse<List<MyPlace>>>(this, new OnResponse<ApiResponse<List<MyPlace>>>() {
            @Override
            public void onResponse(ApiResponse<List<MyPlace>> object) {
                if (object.getPlace() != null && object.getPlace().size() != 0) {
                    loading.setVisibility(View.GONE);
                    mListPlace.clear();
                    mListPlaceFrom.clear();
                    listNearBy.clear();
                    mAdapterPlace.clearSparse();
                    mAdapterPlace.notifyDataSetChanged();

                    for (MyPlace p : object.getPlace()) {
                        if (p != null && p.getFormatting() != null && p.getFormatting().getMain_text() != null) {
                            p.setName(p.getFormatting().getMain_text());
                        }
                        mListPlace.add(p);
                    }
                    // mListPlace.addAll(object.getPlace());
                    mAdapterPlace.notifyDataSetChanged();
                    rcv.setAdapter(mAdapterPlace);
                } else {
                    LogUtils.e(object.getMessage());
                    loading.setVisibility(View.GONE);

                }
            }
        }));
    }

    List<MyPlace> listNearBy = new ArrayList<>();

    private void getNearby(final String type) {
        if (lat != 0 && lng != 0) {
            latlngStr = lat + "," + lng;
        }
        loading.setVisibility(View.VISIBLE);
        rcv.setVisibility(View.INVISIBLE);
        Call<ApiResponse<List<PlaceNearby>>> getNear = ApiUtils.getAPIPLACE().create(GoiXeOmAPI.class).getNearby(latlngStr, 3000, type, 5, getString(R.string.google_map_id));
        getNear.enqueue(new CallBackCustom<ApiResponse<List<PlaceNearby>>>(this, new OnResponse<ApiResponse<List<PlaceNearby>>>() {
            @Override
            public void onResponse(ApiResponse<List<PlaceNearby>> object) {
                if (object.getPlaceNearBy() != null && object.getPlaceNearBy().size() != 0) {
                    loading.setVisibility(View.GONE);
                    rcv.setVisibility(View.VISIBLE);
                    if (object.getPlaceNearBy().size() > 0) {
                        mListPlace.clear();
                        if (listNearBy != null) listNearBy.clear();
                        int index = 0;
                        for (PlaceNearby it : object.getPlaceNearBy()) {
                            index++;
                            addToListNearby(it, type);
                        }
                        mAdapterPlace.notifyDataSetChanged();
                        rcv.setAdapter(mAdapterPlace);
                    }
                } else {
                    LogUtils.e(object.getMessage());
                    loading.setVisibility(View.GONE);
                    rcv.setVisibility(View.VISIBLE);

                }
            }
        }));
    }

    private void addToListNearby(PlaceNearby it, String type) {
        MyPlace mItem = new MyPlace();
        mItem.setLat(it.getGeometry().getLocation().getLat());
        mItem.setLng(it.getGeometry().getLocation().getLng());
        mItem.setType(type);
        String[] vics = it.getVicinity().split(",");
        mItem.setName(it.getName());
        if (it.getName().matches("[0-9]+") && type.equals(getString(R.string.bus))) {
            mItem.setName("Bến xe " + it.getName());
        }
        mItem.setDescription(it.getVicinity());
        if (mListPlace.size() <= 5)
            mListPlace.add(mItem);
        listNearBy.add(mItem);
    }

    @OnClick({R.id.btn_back, R.id.btn_add, R.id.txt_add, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_add:
                Intent i = new Intent(SearchLocationActivity.this, SelectAddressActivity.class);
                if (mPlaceFrom != null) {
                    i.putExtra(Constants.LAT, mPlaceFrom.getLat());
                    i.putExtra(Constants.LNG, mPlaceFrom.getLng());
                }
                i.putExtra(Constants.MSG, true);
                startActivityForResult(i, PLACEFROM_PICKER_REQUEST);

                break;
            case R.id.txt_add:
                getNearby("bus_station");
                break;
            case R.id.btn_clear:
                Intent intent = new Intent(SearchLocationActivity.this, SelectAddressActivity.class);
                intent.putExtra(Constants.MSG, false);
                if (mPlaceFrom != null) {
                    intent.putExtra(Constants.LAT, mPlaceFrom.getLat());
                    intent.putExtra(Constants.LNG, mPlaceFrom.getLng());
                }
                startActivityForResult(intent, PLACEFDES_PICKER_REQUEST);
                break;
        }
    }

    int PLACEFROM_PICKER_REQUEST = 1;
    int PLACEFDES_PICKER_REQUEST = 2;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CODE_REQ_BOOKING && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (requestCode == PLACEFROM_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                MyPlace myplace = data.getBundleExtra(Constants.BUNDLE).getParcelable(Constants.MSG);
                setPlaceFrom(myplace);
            }
            if (mPlaceFrom != null && mPlaceDes != null) {
                Intent i = new Intent(SearchLocationActivity.this, BookingActivity.class);

                Bundle b = new Bundle();
                b.putParcelable(Constants.DES, mPlaceDes);
                b.putParcelable(Constants.FROM, mPlaceFrom);
                i.putExtra(Constants.BUNDLE, b);
                startActivityForResult(i, Constants.CODE_REQ_BOOKING);
            }
        } else if (requestCode == PLACEFDES_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                MyPlace myplace = data.getBundleExtra(Constants.BUNDLE).getParcelable(Constants.MSG);
                setPlaceDes(myplace);
            }
            if (mPlaceFrom != null && mPlaceDes != null) {
                Intent i = new Intent(SearchLocationActivity.this, BookingActivity.class);

                Bundle b = new Bundle();
                b.putParcelable(Constants.DES, mPlaceDes);
                b.putParcelable(Constants.FROM, mPlaceFrom);
                i.putExtra(Constants.BUNDLE, b);
                startActivityForResult(i, Constants.CODE_REQ_BOOKING);
            }
        }

    }

    private void setPlaceFrom(MyPlace myplace) {
        edtAddFrom.setText(myplace.getDescription());
        tvAddFrom.setText(myplace.getDescription());
        tvAddFrom.setVisibility(View.VISIBLE);
        edtAddFrom.setVisibility(View.GONE);
        tvAddFrom.setSelected(true);
        mPlaceFrom = myplace;
        tvAddDes.performClick();

    }

    @Override
    public void onResponse(Integer object) {
        MyPlace place = mListPlace.get(object);
        if (place.getLng() == 0 && place.getLat() == 0) {
            //LatLng latLng = getLocationFromAddress(mListPlace.get(object).getDescription());

//            new PlaceAsyn(place).execute();
            getLocationFromAddress(place.getDescription());
            return;
        }
        if (edtAddFrom.isFocused()) {
            setPlaceFrom(place);
            edtAddDes.requestFocus();
            tvAddDes.performClick();
        } else {
            setPlaceDes(place);
        }
        if (mPlaceFrom != null && mPlaceDes != null && mPlaceFrom.getDescription() != null && mPlaceDes.getDescription() != null && mPlaceDes.getLng() != 0 && mPlaceFrom.getLat() != 0) {
            Intent i = new Intent(SearchLocationActivity.this, BookingActivity.class);
            Bundle b = new Bundle();
            b.putParcelable(Constants.DES, mPlaceDes);
            b.putParcelable(Constants.FROM, mPlaceFrom);
            b.putString(Constants.TYPE_ADDRESS, place.getType());
            i.putExtra(Constants.BUNDLE, b);
//            setResult(RESULT_OK, i);
            startActivityForResult(i, Constants.CODE_REQ_BOOKING);

            finish();
        }


    }

    private void setPlaceDes(MyPlace place) {
        edtAddDes.setText(place.getDescription());
        tvAddDes.setText(place.getDescription());
        tvAddDes.setVisibility(View.VISIBLE);
        edtAddDes.setVisibility(View.GONE);
        tvAddDes.setSelected(true);
        mPlaceDes = place;
    }

    public void getLocationFromAddress(final String strAddress) {
        getDialogProgress().showDialog();
        Call<ApiResponse<List<PlaceNearby>>> getLocation = ApiUtils.getAPIPLACE().create(GoiXeOmAPI.class).getLocation(strAddress, false, getString(R.string.google_map_id));
        getLocation.enqueue(new CallBackCustomNoDelay<ApiResponse<List<PlaceNearby>>>(this, getDialogProgress(), new OnResponse<ApiResponse<List<PlaceNearby>>>() {
            @Override
            public void onResponse(ApiResponse<List<PlaceNearby>> object) {
                if (object.getPlaceNearBy() != null && object.getPlaceNearBy().size() != 0) {
                    PlaceNearby placeNearby = object.getPlaceNearBy().get(0);
                    MyPlace place = new MyPlace();
                    place.setDescription(strAddress);
                    place.setLng(placeNearby.getGeometry().getLocation().getLng());
                    place.setLat(placeNearby.getGeometry().getLocation().getLat());
                    if (edtAddFrom.isFocused()) {
                        setPlaceFrom(place);
                        tvAddDes.performClick();
                        edtAddDes.requestFocus();
                    } else {
                        setPlaceDes(place);
                    }
                    if (mPlaceFrom != null && mPlaceDes != null && mPlaceFrom.getDescription() != null && mPlaceDes.getDescription() != null && mPlaceDes.getLng() != 0 && mPlaceFrom.getLat() != 0) {
                        Intent i = new Intent(SearchLocationActivity.this, BookingActivity.class);
                        Bundle b = new Bundle();
                        b.putParcelable(Constants.DES, mPlaceDes);
                        b.putParcelable(Constants.FROM, mPlaceFrom);
                        i.putExtra(Constants.BUNDLE, b);
//            setResult(RESULT_OK, i);
                        startActivityForResult(i, Constants.CODE_REQ_BOOKING);
//            finish();
                    }
                }
            }
        }));
    }
    public void getLocationToAddFav(final MyPlace place) {
        getDialogProgress().showDialog();
        Call<ApiResponse<List<PlaceNearby>>> getLocation = ApiUtils.getAPIPLACE().create(GoiXeOmAPI.class).getLocation(place.getDescription(), false, getString(R.string.google_map_id));
        getLocation.enqueue(new CallBackCustomNoDelay<ApiResponse<List<PlaceNearby>>>(this, getDialogProgress(), new OnResponse<ApiResponse<List<PlaceNearby>>>() {
            @Override
            public void onResponse(ApiResponse<List<PlaceNearby>> object) {
                if (object.getPlaceNearBy() != null && object.getPlaceNearBy().size() != 0) {
                    PlaceNearby placeNearby = object.getPlaceNearBy().get(0);
                    place.setLng(placeNearby.getGeometry().getLocation().getLng());
                    place.setLat(placeNearby.getGeometry().getLocation().getLat());
                    addToFavourite(place);
                }
            }
        }));
    }
//    class PlaceAsyn extends AsyncTask<Void, Void, LatLng> {
//        MyPlace place;
//        LatLng myLatLng;
//
//        PlaceAsyn(MyPlace place) {
//            this.place = place;
//            myLatLng = new LatLng(21.0278, 105.8342);
//        }
//
//        @Override
//        protected LatLng doInBackground(Void... strings) {
//            Geocoder coder = new Geocoder(SearchLocationActivity.this, new Locale("vi"));
//            List<Address> address;
//            LatLng p1 = null;
//            try {
//                address = coder.getFromLocationName(place.getFormatting().getMain_text(), 6, myLatLng.latitude - 0.1, myLatLng.longitude - 0.1, myLatLng.latitude + 0.1, myLatLng.longitude + 0.1);
//                if (address == null || address.size() == 0) {
//                    address = coder.getFromLocationName(place.getDescription(), 6, myLatLng.latitude - 0.1, myLatLng.longitude - 0.1, myLatLng.latitude + 0.1, myLatLng.longitude + 0.1);
//
//                }
//                Address location = address.get(0);
//                location.getLatitude();
//                location.getLongitude();
//                p1 = new LatLng(location.getLatitude(), location.getLongitude());
//                return p1;
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            }
//            return p1;
//        }
//
//        @Override
//        protected void onPostExecute(LatLng places) {
//            super.onPostExecute(places);
//            place.setLng(places.longitude);
//            place.setLat(places.latitude);
//            if (edtAddFrom.isFocused()) {
//                setPlaceFrom(place);
//
//            } else {
//                setPlaceDes(place);
//            }
//            if (mPlaceFrom != null && mPlaceDes != null) {
//                Intent i = new Intent(SearchLocationActivity.this, BookingActivity.class);
//                Bundle b = new Bundle();
//                b.putParcelable(Constants.DES, mPlaceDes);
//                b.putParcelable(Constants.FROM, mPlaceFrom);
//                i.putExtra(Constants.BUNDLE, b);
////            setResult(RESULT_OK, i);
//                startActivityForResult(i, Constants.CODE_REQ_BOOKING);
////            finish();
//            }
//
//        }
//    }

}
