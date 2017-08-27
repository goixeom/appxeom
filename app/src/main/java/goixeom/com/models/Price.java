package goixeom.com.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 8/22/2017.
 */

public class Price {

    @SerializedName("bike")
    private int bike;
    @SerializedName("car")
    private int car;

    public int getBike() {
        return bike;
    }

    public void setBike(int bike) {
        this.bike = bike;
    }

    public int getCar() {
        return car;
    }

    public void setCar(int car) {
        this.car = car;
    }
}
