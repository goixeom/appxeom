package goixeom.com.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 6/23/2017.
 */

public class History {

    @SerializedName("date")
    public String date;
    @SerializedName("start")
    public String start;
    @SerializedName("end")
    public String end;
    @SerializedName("id")
    public int id;
    @SerializedName("distance")
    float distance;
    @SerializedName("vote")
    int vote;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
