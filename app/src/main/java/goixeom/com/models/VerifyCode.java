package goixeom.com.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 6/19/2017.
 */

public class VerifyCode {
    @SerializedName("code")
    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
