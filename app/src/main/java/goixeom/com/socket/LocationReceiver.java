package goixeom.com.socket;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 7/3/2017.
 */

public class LocationReceiver {
    @SerializedName("id")
    int id;
    @SerializedName("result")
    SocketResponse result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SocketResponse getResult() {
        return result;
    }

    public void setResult(SocketResponse result) {
        this.result = result;
    }
}
