package goixeom.com.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.List;
import java.util.Locale;

import goixeom.com.CustomTextView;
import goixeom.com.R;
import goixeom.com.activities.MainActivity;
import goixeom.com.activities.MapsActivity;

/**
 * Created by DuongKK on 6/16/2017.
 */

public class CommonUtils {
    public static void shareSimpleText(String msg, Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", R.string.app_name);
        intent.putExtra("android.intent.extra.TEXT", msg);
        context.startActivity(Intent.createChooser(intent, "Send via"));
    }

    public static String getGreateAddressStr(String add) {
        if(add==null) return "";
        return add.replace(", Hanoi", "").replace(", HaNoi", "").replace(", Hà Nội", "").replace(", Hà nội", "").replace(", Ha noi", "");
    }
    public static void makeCollapsingToolbarLayoutLooksGood(Context context,CollapsingToolbarLayout collapsingToolbarLayout) {
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
    }
    public static void setSpanCustomTextView(String text, int index, CustomTextView customCustomTextView) {
        String s = text;
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 0, index, 0); // set size
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, index, 0);// set color
        ss1.setSpan(boldSpan, 0, index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        customCustomTextView.setText(ss1);
    }
//    public static boolean isNavigationBarAvailable(){
//
//        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
//        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
//
//        return (!(hasBackKey && hasHomeKey));
//    }
    public static void disable(ViewGroup layout) {
        if (layout == null) return;

        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disable((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

    public static void enable(ViewGroup layout) {
        if (layout == null) return;
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                enable((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }

    public static void launchMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    public static String getCompleteAddressNomal(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        try {
            List<android.location.Address> addresses = new Geocoder(context, Locale.getDefault()).getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = (Address) addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                LogUtils.e("My Current loction Nomal address" + strReturnedAddress.toString());
                return strAdd;
            }
            LogUtils.e("My Current loction address NomalNo Address returned!");
            return strAdd;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("My Current loction address NomalCanont get Address!");
            return strAdd;
        }
    }

    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        try {
            List<com.doctoror.geocoder.Address> addresses = new com.doctoror.geocoder.Geocoder(context, Locale.getDefault()).getFromLocation(LATITUDE, LONGITUDE, 20, true);
            if (addresses != null) {
                com.doctoror.geocoder.Address returnedAddress = (com.doctoror.geocoder.Address) addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                LogUtils.e(returnedAddress.toString());
                strAdd = returnedAddress.getFormattedAddress();
                LogUtils.e("My Current loction address" + strAdd.toString());
                return strAdd;
            }
            LogUtils.e("My Current loction addressNo Address returned!");
            return getCompleteAddressNomal(context, LATITUDE, LONGITUDE);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("My Current loction addressCanont get Address!");
            return getCompleteAddressNomal(context, LATITUDE, LONGITUDE);
        }
    }

    public static void focusAllMarkers(List<LatLng> places, GoogleMap mMap, Context context) {
        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
        for (LatLng place : places) {
            latLngBounds.include(place);
        }
        LatLngBounds bounds = latLngBounds.build();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(),14));
//        focusCurrentLocation(bounds.getCenter(),13,mMap);
        LogUtils.e(width + "----" + height);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, (2 * height / 3) + 100, 20));
       int density = context.getResources().getDisplayMetrics().densityDpi;
        if(density>320){
            height = (2 * height / 3)-300;
        }else{
            height = (2 * height / 3);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 200));


//        try {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
//        } catch (Exception e) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 900, 900, 50));
//            e.fillInStackTrace();
//        }
//        LatLng latLng  = new LatLng(        bounds.getCenter().latitude-0.0001,bounds.getCenter().longitude);
//        focusCurrentLocation(latLng,10,mMap);
    }

    public static void focusAllMarkersPadding(List<LatLng> places, GoogleMap mMap, Context context) {
        LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
        for (LatLng place : places) {
            latLngBounds.include(place);
        }
        LatLngBounds bounds = latLngBounds.build();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(),14));
//        focusCurrentLocation(bounds.getCenter(),13,mMap);
        LogUtils.e(width + "----" + height);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, (2 * height / 3) + 100, 20));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 600, 600, 16));


//        try {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
//        } catch (Exception e) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 900, 900, 50));
//            e.fillInStackTrace();
//        }
//        LatLng latLng  = new LatLng(        bounds.getCenter().latitude-0.0001,bounds.getCenter().longitude);
//        focusCurrentLocation(latLng,10,mMap);
    }

    public static void intentToCall(String phone, Context context) {
        Intent i = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + phone));
        context.startActivity(i);
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static void focusCurrentLocation(LatLng latLng, float sizeZoom, GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latLng).zoom(sizeZoom).build()));
    }

    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

    public static Marker addMarker(Drawable resource, GoogleMap mMap, LatLng latLng) {
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(resource);

        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).anchor(0.5f, 0.5f).icon(markerIcon));
        return marker;
    }

    public static Marker addMarker(Drawable resource, GoogleMap mMap, LatLng latLng, float rotate) {
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(resource);

        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).anchor(0.5f, 0.5f).rotation(rotate).icon(markerIcon));
        return marker;
    }

    public static Marker addMarker(Drawable resource, GoogleMap mMap, LatLng latLng, float rotate, String title) {
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(resource);
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).anchor(0.5f, 0.5f).rotation(rotate).icon(markerIcon));
        return marker;
    }
    public static Marker addMarkerFromFile(File resource, GoogleMap mMap, LatLng latLng,float rotate,String tile) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).anchor(0.5f, 0.5f).rotation(rotate).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(resource.getPath()), 90, 90, false))));
        marker.showInfoWindow();
        return marker;
    }
    public static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

        public static Drawable resize(Drawable image,Context context) {
            Bitmap b = ((BitmapDrawable)image).getBitmap();
            Bitmap bitmapResized = Bitmap.createScaledBitmap(b, (int) context.getResources().getDimension(R.dimen.size_w_bike), (int) context.getResources().getDimension(R.dimen.size_h_bike), false);
            return new BitmapDrawable(context.getResources(), bitmapResized);
        }
        public static Drawable resizeCar(Drawable image,Context context) {
            Bitmap b = ((BitmapDrawable)image).getBitmap();
            Bitmap bitmapResized = Bitmap.createScaledBitmap(b,(int) context.getResources().getDimension(R.dimen.size_w_car), (int) context.getResources().getDimension(R.dimen.size_h_car), false);
            return new BitmapDrawable(context.getResources(), bitmapResized);
        }
    public static Notification createNotificationWithMsg(Context context, String title, String msg, int isNomalNoti) {
        android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setAutoCancel(true).setOngoing(false);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        Intent intentMain = new Intent(context, MapsActivity.class);
        intentMain.putExtra(Constants.NOTIFICATION_SOCKET, isNomalNoti);
//        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Vicostone/");
//        intent.setData(uri);
//        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
//        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Vicostone/"); // a directory
//        intent.setDataAndType(uri, "*/*");
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        stackBuilder.addNextIntent(intentMain);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        return builder.build();

    }

    public static boolean isNavigationBarAvailable(Activity c){
        boolean hasNavigationBar = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        {
            hasNavigationBar = !ViewConfiguration.get(c).hasPermanentMenuKey();
        }
        else
        {
            hasNavigationBar = false;
        }
        return hasNavigationBar;
    }
    public static int getSoftButtonsBarSizePort(Activity activity) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }
}
