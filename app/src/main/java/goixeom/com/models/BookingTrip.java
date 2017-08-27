package goixeom.com.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DuongKK on 6/29/2017.
 */

public class BookingTrip {

    @SerializedName("t_id")
    private int t_id;
    @SerializedName("list_driver")
    private List<List_driver> list_driver;
    @SerializedName("lat_from")
    private double lat_from;
    @SerializedName("lng_from")
    private double lng_from;
    @SerializedName("lat_to")
    private double lat_to;
    @SerializedName("lng_to")
    private double lng_to;

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public List<List_driver> getList_driver() {
        return list_driver;
    }

    public void setList_driver(List<List_driver> list_driver) {
        this.list_driver = list_driver;
    }

    public double getLat_from() {
        return lat_from;
    }

    public void setLat_from(double lat_from) {
        this.lat_from = lat_from;
    }

    public double getLng_from() {
        return lng_from;
    }

    public void setLng_from(double lng_from) {
        this.lng_from = lng_from;
    }

    public double getLat_to() {
        return lat_to;
    }

    public void setLat_to(double lat_to) {
        this.lat_to = lat_to;
    }

    public double getLng_to() {
        return lng_to;
    }

    public void setLng_to(double lng_to) {
        this.lng_to = lng_to;
    }

    public static class List_driver {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("lat")
        private double lat;
        @SerializedName("lng")
        private double lng;
        @SerializedName("distance")
        private double distance;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}
