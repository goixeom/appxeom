package goixeom.com.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 8/25/2017.
 */

public class News implements Parcelable {
    public static final int VIEW_NORMAL = 0;
    public static final int VIEW_PHOTO = 1;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("date")
    private String date;
    @SerializedName("image")
    private String image;
    public int getType(){
        if(getImage()==null || getImage().isEmpty()){
            return VIEW_NORMAL;
        }
        return VIEW_PHOTO;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.date);
        dest.writeString(this.image);
    }

    public News() {
    }

    protected News(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.date = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
