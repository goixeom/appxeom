package goixeom.com.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 8/28/2017.
 */

public class AppConfig {

    @SerializedName("direction")
    private String direction;
    @SerializedName("bike")
    private String bike;
    @SerializedName("car")
    private String car;
    @SerializedName("time_update")
    private long timeUpdate;
    @SerializedName("time_wait")
    private long timeWait;

    public long getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(long timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    public long getTimeWait() {
        return timeWait;
    }

    public void setTimeWait(long timeWait) {
        this.timeWait = timeWait;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getBike() {
        return bike;
    }

    public void setBike(String bike) {
        this.bike = bike;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }
}
