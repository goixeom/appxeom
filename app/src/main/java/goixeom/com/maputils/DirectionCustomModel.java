package goixeom.com.maputils;

import com.google.android.gms.maps.model.Polyline;

import java.util.HashMap;
import java.util.List;

/**
 * Created by DuongKK on 6/18/2017.
 */

public class DirectionCustomModel {
    List<List<HashMap<String,String>>> mapPolyline ;
    String duration;
    String distanceTxt;
    int distance;    //meter
    Polyline polyline;
    public String getDistanceTxt() {
        return distanceTxt;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public void setDistanceTxt(String distanceTxt) {
        this.distanceTxt = distanceTxt;
    }

    public List<List<HashMap<String, String>>> getMapPolyline() {
        return mapPolyline;
    }

    public void setMapPolyline(List<List<HashMap<String, String>>> mapPolyline) {
        this.mapPolyline = mapPolyline;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
