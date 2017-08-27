package goixeom.com.apis;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MyPC on 9/12/2016.
 */
public class ApiResponse<T>{
    @SerializedName("message")
    String message;
    @SerializedName("errorId")
    int status;
    @SerializedName("data")
    T data;
    @SerializedName("predictions")
    T place;
    @SerializedName("results")
    T placeNearBy;
    @SerializedName("routes")
    T routes;

    @SerializedName("Count")
    int count;

    public T getRoutes() {
        return routes;
    }

    public void setRoutes(T routes) {
        this.routes = routes;
    }

    public T getPlaceNearBy() {
        return placeNearBy;
    }

    public void setPlaceNearBy(T placeNearBy) {
        this.placeNearBy = placeNearBy;
    }

    public ApiResponse(){};
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public T getPlace() {
        return place;
    }

    public void setPlace(T place) {
        this.place = place;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
