package goixeom.com.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 6/14/2017.
 */

public class StringResponse {
    @SerializedName("phone")
    String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
