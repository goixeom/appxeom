package goixeom.com.socket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.annotations.SerializedName;

import java.util.Stack;

import goixeom.com.maputils.MapUtils;

/**
 * Created by DuongKK on 6/28/2017.
 */

public class LocationBearing {
    private static double lng;
    private static double lat;
    Context context;
    @SerializedName("id")
    private int d_id;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lng")
    private double longitude;
    @SerializedName("angle")
    private double angle;
    private Marker marker;
    private double oldLat;
    private double oldLng;
    private float oldBearing;
    Stack<LatLng> latLngStack = new Stack<>();

    public void addToStack(LatLng latLng) {
        latLngStack.push(latLng);
        notifyAnimation();
    }

    public float compute(int sec) {
        return ((float) sec) / (float) 1000;
    }

    public void notifyAnimation() {
        while (!latLngStack.isEmpty()) {
            animateMarkerNew(latLngStack.pop(), marker);
//
//            getDirectionToLatlng(marker.getPosition(),latLngStack.pop());
        }

    }

    public Stack<LatLng> getLatLngStack() {
        return latLngStack;
    }

    public void setLatLngStack(Stack<LatLng> latLngStack) {
        this.latLngStack = latLngStack;
    }

    public static void animateMarkerNew(final LatLng destination, final Marker marker) {
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = destination;
            float[] dis =new float[2];
             Location.distanceBetween(startPosition.latitude,startPosition.longitude,endPosition.latitude,endPosition.longitude,dis);
            if(startPosition.latitude == endPosition.latitude && startPosition.longitude == endPosition.longitude || dis[0]<5) return;

            final float startRotation = marker.getRotation();
            float bearing = Math.round(MapUtils.getBearing(startPosition, endPosition));
            LogUtils.e("minh khai duoi " + bearing);
            bearing = getBestBearing(marker, bearing);
            LogUtils.e("best bearing =  " + bearing);
            rotateMarker(marker, bearing);
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        lng = v * endPosition.longitude + (1 - v)
                                * startPosition.longitude;
                        lat = v * endPosition.latitude + (1 - v)
                                * startPosition.latitude;
                        LatLng newPos = new LatLng(lat, lng);
                        marker.setPosition(newPos);
//                        marker.setRotation(getBearing(startPosition, newPos));
//                        if (startPosition.latitude == newPosition.latitude && startPosition.longitude == newPosition.longitude)
//                            return;
//                        marker.setRotation(v*bearing);
//                        marker.setRotation(v*bearing);
                    } catch (Exception ex) {
                        //I don't care atm..
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

//                     if (marker != null) {
//                         marker.remove();
//                     }
//                     CommonUtils.addMarker(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_motorcycle, null), googleMap, destination);
//                    marker.setVisible(false);
                }
            });
            valueAnimator.start();
        }
    }

//    Handler handler;
//    int index, next;
//    private float v;
//    private double lat, lng;
//    LatLng startPosition, endPosition;
//    private void getDirectionToLatlng( final LatLng old, final LatLng newLatlng){
//        Call<ApiResponse<List<Routes>>> direction = ApiUtils.getAPIPLACE().create(GoiXeOmAPI.class)
//                .getDirection(old.latitude + "," + old.longitude
//                        , newLatlng.latitude + "," + newLatlng.longitude, context.getString(R.string.google_map_id));
//        direction.enqueue(new CallBackCustomNoDelay<ApiResponse<List<Routes>>>(context, new OnResponse<ApiResponse<List<Routes>>>() {
//            @Override
//            public void onResponse(ApiResponse<List<Routes>> object) {
//                if (object.getRoutes() != null && object.getRoutes().size() > 0) {
//
//                    PolylineOptions line = new PolylineOptions();
//                    int n = object.getRoutes().get(0).getLegs().get(0).getSteps().size();
//                    for (int i = 0; i < n; i++) {
//                        line.add(new LatLng(
//                                object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStart_location().getLat()
//                                , object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getStart_location().getLng()
//                        ));
//                        line.addAll(decodePoly(object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getPolyline().getPoints()));
//                        line.add(new LatLng(
//                                object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getEnd_location().getLat()
//                                , object.getRoutes().get(0).getLegs().get(0).getSteps().get(i).getEnd_location().getLng()
//                        ));
//                    }
//                    driverMarker(line.getPoints());
//                } else {
//                    LogUtils.e("direction null or no routes");
//                }
//            }
//        }));
//    }
//    private List<LatLng> decodePoly(String encoded) {
//
//        List<LatLng> poly = new ArrayList<LatLng>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng p = new LatLng((((double) lat / 1E5)),
//                    (((double) lng / 1E5)));
//            poly.add(p);
//        }
//        return poly;
//    }
//
//    //ref :com.developers.uberanimation
//    void driverMarker(final List<LatLng> polyLineList) {
//        handler = new Handler();
//        index = -1;
//        next = 1;
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//                if (index < polyLineList.size() - 1) {
//                    index++;
//                    next = index + 1;
//                }
//                if (index < polyLineList.size() - 1) {
//                    startPosition = polyLineList.get(index);
//                    endPosition = polyLineList.get(next);
//                }
//                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
//                valueAnimator.setDuration(3000);
//                valueAnimator.setInterpolator(new LinearInterpolator());
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                        v = valueAnimator.getAnimatedFraction();
//                        lng = v * endPosition.longitude + (1 - v)
//                                * startPosition.longitude;
//                        lat = v * endPosition.latitude + (1 - v)
//                                * startPosition.latitude;
//                        LatLng newPos = new LatLng(lat, lng);
//                        marker.setPosition(newPos);
//                        marker.setAnchor(0.5f, 0.5f);
//                        marker.setRotation(getBearing(startPosition, newPos));
////                        mMap.moveCamera(CameraUpdateFactory
////                                .newCameraPosition
////                                        (new CameraPosition.Builder()
////                                                .target(newPos)
////                                                .zoom(15.5f)
////                                                .build()));
////                        Log.d(TAG, "Coming: " + newPos);
//                    }
//                });
//                valueAnimator.start();
//                handler.postDelayed(this, 3000);
//            }
//        }, 3000);
//    }
//

    public static void rotateMarker(final Marker marker, final float toRotation) {
//        if (!isMarkerRotating) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = marker.getRotation();
        final long duration = 300;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
//                    isMarkerRotating = true;

                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;

                marker.setRotation(-rot > 180 ? rot / 2 : rot);

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
//                        isMarkerRotating = false;
                }
            }
        });
//        }
    }

    public static float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    public static interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        public static class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    private static float getBestBearing(Marker marker, float bearing) {
//        if(Math.abs(bearing) == Math.abs(marker.getRotation())) return 0;
        if (bearing == marker.getRotation()) return -90;
        LogUtils.e("minh khai : " + bearing);
        bearing = bearing > 180 ? bearing - 360 : bearing;

//        bearing = bearing*marker.getRotation() <0 ? Math.abs(marker.getRotation()) + Math.abs(bearing) : bearing;
//        if(Math.abs(bearing) + Math.abs(marker.getRotation())>180) {
//            bearing = Math.abs(bearing) + Math.abs(marker.getRotation()) -360;
//        }
        return bearing;
    }

    public int getD_id() {
        return d_id;
    }

    public void setD_id(int d_id) {
        this.d_id = d_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAngle() {
        return angle;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
