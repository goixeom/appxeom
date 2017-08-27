package goixeom.com.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 6/19/2017.
 */

public class Discount implements Parcelable {

    @SerializedName("pr_id")
    private String pr_id;
    @SerializedName("title")
    private String title;
    @SerializedName("pr_code")
    private String pr_code;
    @SerializedName("type")
    private String type;
    @SerializedName("value")
    private String value;
    @SerializedName("start_date")
    private String start_date;
    @SerializedName("end_date")
    private String end_date;
    @SerializedName("start_time")
    private String start_time;
    @SerializedName("end_time")
    private String end_time;
    @SerializedName("date_created")
    private String date_created;
    @SerializedName("active")
    private String active;
    @SerializedName("type_pr")
    private int typePr;
    public String getPr_id() {
        return pr_id;
    }

    public void setPr_id(String pr_id) {
        this.pr_id = pr_id;
    }

    public int getTypePr() {
        return typePr;
    }

    public void setTypePr(int typePr) {
        this.typePr = typePr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPr_code() {
        return pr_code;
    }

    public void setPr_code(String pr_code) {
        this.pr_code = pr_code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Discount() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pr_id);
        dest.writeString(this.title);
        dest.writeString(this.pr_code);
        dest.writeString(this.type);
        dest.writeString(this.value);
        dest.writeString(this.start_date);
        dest.writeString(this.end_date);
        dest.writeString(this.start_time);
        dest.writeString(this.end_time);
        dest.writeString(this.date_created);
        dest.writeString(this.active);
        dest.writeInt(this.typePr);
    }

    protected Discount(Parcel in) {
        this.pr_id = in.readString();
        this.title = in.readString();
        this.pr_code = in.readString();
        this.type = in.readString();
        this.value = in.readString();
        this.start_date = in.readString();
        this.end_date = in.readString();
        this.start_time = in.readString();
        this.end_time = in.readString();
        this.date_created = in.readString();
        this.active = in.readString();
        this.typePr = in.readInt();
    }

    public static final Creator<Discount> CREATOR = new Creator<Discount>() {
        @Override
        public Discount createFromParcel(Parcel source) {
            return new Discount(source);
        }

        @Override
        public Discount[] newArray(int size) {
            return new Discount[size];
        }
    };
}
