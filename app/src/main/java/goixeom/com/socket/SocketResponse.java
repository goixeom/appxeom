package goixeom.com.socket;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DuongKK on 6/20/2017.
 */

public class SocketResponse {
    @SerializedName("status")
    int status;
    @SerializedName("t_id")
    int idTrip;
    @SerializedName("d_id")
    int idDriver;
    @SerializedName("u_id")
    int idUser;
    @SerializedName("lat")
    double lat;
    @SerializedName("lng")
    double lng;

    @SerializedName("imei")
    String imei;


    @SerializedName("title")
    String title;
    @SerializedName("content")
    String content;
    @SerializedName("category")
    int category;
    @SerializedName("type")
    int type;
    @SerializedName("type_detail")
    int typeDetail;
    @SerializedName("0")
    LocationBearing locationBearing0;
    @SerializedName("1")
    LocationBearing locationBearing1;
    @SerializedName("2")
    LocationBearing locationBearing2;
    @SerializedName("3")
    LocationBearing locationBearing3;
    @SerializedName("4")
    LocationBearing locationBearing4;
    @SerializedName("5")
    LocationBearing locationBearing5;
    @SerializedName("6")
    LocationBearing locationBearing6;
    @SerializedName("7")
    LocationBearing locationBearing7;


    public List<LocationBearing> getListLocation() {
        List<LocationBearing> list = new ArrayList<>();
        if (locationBearing0 != null) {
            list.add(locationBearing0);
        }
        if (locationBearing1 != null) {
            list.add(locationBearing1);
        }
        if (locationBearing2 != null) {
            list.add(locationBearing2);
        }
        if (locationBearing3 != null) {
            list.add(locationBearing3);
        }
        if (locationBearing4 != null) {
            list.add(locationBearing4);
        }
        if (locationBearing5 != null) {
            list.add(locationBearing5);
        }
        if (locationBearing6 != null) {
            list.add(locationBearing6);
        }
        if (locationBearing7 != null) {
            list.add(locationBearing7);
        }
        return list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTypeDetail() {
        return typeDetail;
    }

    public void setTypeDetail(int typeDetail) {
        this.typeDetail = typeDetail;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getStatus() {
        return status;
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

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(int idTrip) {
        this.idTrip = idTrip;
    }

    public int getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(int idDriver) {
        this.idDriver = idDriver;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
