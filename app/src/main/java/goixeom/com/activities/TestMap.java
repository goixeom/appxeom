package goixeom.com.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import goixeom.com.R;
import goixeom.com.maputils.MapUtils;
import goixeom.com.utils.CommonUtils;

public class TestMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    Marker marker;
    Marker marker2;
    Marker marker3;
    int i = 0;
    final LatLng minhkhaiLatlng = new LatLng(20.996841, 105.865764);
    final LatLng minhkhaiLatlngPhai = new LatLng(20.996841, 105.865906);
    final LatLng minhkhaiLatlngDuoi = new LatLng(20.996713, 105.865801);
    final LatLng minhkhaiLatlngTrai = new LatLng(20.996831, 105.865549);
    final LatLng minhkhaiLatlngTren = new LatLng(20.997006, 105.865683);
    MarkerHandler markerHandler = new MarkerHandler();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        CommonUtils.focusCurrentLocation(minhkhaiLatlng, 20, mMap);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                        @Override
                                        public void onMapLoaded() {
                                            marker = CommonUtils.addMarker(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_motorcycle), mMap, minhkhaiLatlngDuoi, 0);
                                            marker2 = CommonUtils.addMarker(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_motorcycle), mMap, minhkhaiLatlngTrai, 90);
                                            marker3 = CommonUtils.addMarker(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_motorcycle), mMap, minhkhaiLatlngTren, 180);
//                                            MapUtils.animateMarkerNew(minhkhaiLatlngTrai, marker, -90);
//                                            MapUtils.rotateMarker(marker,-120);
                                        }
                                    }
        );

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                new Handler().post(markerHandler);

            }


        });

    }

    class MarkerHandler implements Runnable {

        public Handler mPostHandler = new Handler();


        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (i == 0) {
//
//                    float bearing = Math.round(MapUtils.getBearing(minhkhaiLatlngDuoi, minhkhaiLatlng));
//                    LogUtils.e("bearing = " + bearing);
//                    bearing = getBestBearing(marker, bearing);
//                    LogUtils.e("best bearing =  " + bearing);
////                    MapUtils.rotateMarker(marker, bearing);
                        MapUtils.animateMarkerNew(minhkhaiLatlng, marker, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlngPhai, marker2, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlngDuoi, marker3, 0);
                    } else if (i == 1) {
//                    float bearing =  Math.round(MapUtils.getBearing(minhkhaiLatlng, minhkhaiLatlngTrai));
//                    LogUtils.e("minh khai duoi " + bearing);
//                    bearing = getBestBearing(marker, bearing);
//                    LogUtils.e("best bearing =  " + bearing);
//
////                    MapUtils.rotateMarker(marker, bearing);
                        MapUtils.animateMarkerNew(minhkhaiLatlngPhai, marker, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlngTrai, marker2, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlng, marker3, 0);
                    } else if (i == 2) {
//                    float bearing =  Math.round(MapUtils.getBearing(minhkhaiLatlngTrai, minhkhaiLatlng));
//                    LogUtils.e("minh khai duoi " + bearing);
//                    bearing = getBestBearing(marker, bearing);
//                    LogUtils.e("best bearing =  " + bearing);
//
////                    MapUtils.rotateMarker(marker, bearing);
                        MapUtils.animateMarkerNew(minhkhaiLatlng, marker, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlngDuoi, marker2, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlngTren, marker3, 0);
                    } else if (i == 3) {
//                    float bearing =  Math.round(MapUtils.getBearing(minhkhaiLatlng, minhkhaiLatlngTren));
//                    LogUtils.e("minh khai duoi " + bearing);
//                    bearing = getBestBearing(marker, bearing);
//                    LogUtils.e("best bearing =  " + bearing);
//
////                    MapUtils.rotateMarker(marker, bearing);
                        MapUtils.animateMarkerNew(minhkhaiLatlngDuoi, marker, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlngTrai, marker2, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlng, marker3, 0);
                    } else if (i == 4) {
//                    float bearing =  Math.round(MapUtils.getBearing(minhkhaiLatlngTren, minhkhaiLatlng));
//                    LogUtils.e("minh khai duoi " + bearing);
//                    bearing = getBestBearing(marker, bearing);
//                    LogUtils.e("best bearing =  " + bearing);
//
////                    MapUtils.rotateMarker(marker, bearing);
                        MapUtils.animateMarkerNew(minhkhaiLatlngTren, marker, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlng, marker2, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlngDuoi, marker3, 0);
                    } else if (i == 5) {
//                    float bearing =  Math.round(MapUtils.getBearing(minhkhaiLatlng, minhkhaiLatlngPhai));
//                    LogUtils.e("minh khai duoi " + bearing);
//                    bearing = getBestBearing(marker, bearing);
//                    LogUtils.e("best bearing =  " + bearing);
//
////                    MapUtils.rotateMarker(marker, bearing);
                        MapUtils.animateMarkerNew(minhkhaiLatlngDuoi, marker, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlngTrai, marker2, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlngTren, marker3, 0);
                    } else if (i == 6) {
//                    float bearing =  Math.round(MapUtils.getBearing(minhkhaiLatlngPhai, minhkhaiLatlng));
//                    LogUtils.e("minh khai duoi " + bearing);
//                    bearing = getBestBearing(marker, bearing);
//                    LogUtils.e("best bearing =  " + bearing);
//
////                    MapUtils.rotateMarker(marker, bearing);
                        MapUtils.animateMarkerNew(minhkhaiLatlngDuoi, marker, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlngPhai, marker2, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlngTrai, marker3, 0);
                    } else {
                        float bearing = Math.round(MapUtils.getBearing(minhkhaiLatlng, minhkhaiLatlngDuoi));
//                    LogUtils.e("minh khai duoi " + bearing);
//                    bearing = getBestBearing(marker, bearing);
//                    LogUtils.e("best bearing =  " + bearing);
//
////                    MapUtils.rotateMarker(marker, bearing);
                        MapUtils.animateMarkerNew(minhkhaiLatlng, marker, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlng, marker2, 0);
                        MapUtils.animateMarkerNew(minhkhaiLatlng, marker3, 0);
                        i = -1;
                    }

                    i++;

                }
            });
        }

        // locationBearing.notifyAnimation(MapsActivity.this, mMap, (int) sec);


    }

    private float getBestBearing(Marker marker, float bearing) {
//        if(Math.abs(bearing) == Math.abs(marker.getRotation())) return 0;
        if (bearing == marker.getRotation()) return -90;
        LogUtils.e("minh khai : " + bearing);
        bearing = bearing > 180 ? bearing - 360 : bearing;

//        bearing = bearing*marker.getRotation() <0 ? Math.abs(marker.getRotation()) + Math.abs(bearing) : bearing;
        if (Math.abs(bearing) + Math.abs(marker.getRotation()) > 180) {
            bearing = Math.abs(bearing) + Math.abs(marker.getRotation()) - 360;
        }
        return bearing;
    }
}
