package goixeom.com.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 6/14/2017.
 */

public class User implements Parcelable {
    @SerializedName("u_id")
    public String u_id;
    @SerializedName("personal_code")
    public String personal_code;
    @SerializedName("rf_code")
    public String rf_code;
    @SerializedName("u_name")
    public String u_name;
    @SerializedName("u_phone")
    public String u_phone;
    @SerializedName("u_email")
    public String u_email;
    @SerializedName("u_password")
    public String u_password;
    @SerializedName("verify_code")
    public String verify_code;
    @SerializedName("u_address")
    public String u_address;
    @SerializedName("address1")
    public String address1;
    @SerializedName("street")
    public String street;
    @SerializedName("id_province")
    public String id_province;
    @SerializedName("id_district")
    public String id_district;
    @SerializedName("u_avatar")
    public String u_avatar;
    @SerializedName("avatar_fb")
    public String avatar_fb;
    @SerializedName("u_coin")
    public String u_coin;
    @SerializedName("u_account")
    public String u_account;
    @SerializedName("fbid")
    public String fbid;
    @SerializedName("ggid")
    public String ggid;
    @SerializedName("email_active")
    public String email_active;
    @SerializedName("phone_active")
    public String phone_active;
    @SerializedName("reg_date")
    public String reg_date;
    @SerializedName("active")
    public String active;
    @SerializedName("change_password")
    public int change_password;
    @SerializedName("time_change_password")
    public String time_change_password;
    @SerializedName("status")
    public String status;
    @SerializedName("login_session")
    public String login_session;
    @SerializedName("imei")
    public String imei;
    @SerializedName("type")
    public int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return u_id;
    }

    public void setId(String u_id) {
        this.u_id = u_id;
    }

    public String getPersonal_code() {
        return personal_code;
    }

    public void setPersonal_code(String personal_code) {
        this.personal_code = personal_code;
    }

    public String getRf_code() {
        return rf_code;
    }

    public void setRf_code(String rf_code) {
        this.rf_code = rf_code;
    }

    public String getName() {
        return u_name;
    }

    public void setName(String u_name) {
        this.u_name = u_name;
    }

    public String getPhone() {
        return u_phone;
    }

    public void setPhone(String u_phone) {
        this.u_phone = u_phone;
    }

    public String getEmail() {
        return u_email;
    }

    public void setEmail(String u_email) {
        this.u_email = u_email;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }

    public String getU_address() {
        return u_address;
    }

    public void setU_address(String u_address) {
        this.u_address = u_address;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getId_province() {
        return id_province;
    }

    public void setId_province(String id_province) {
        this.id_province = id_province;
    }

    public String getId_district() {
        return id_district;
    }

    public void setId_district(String id_district) {
        this.id_district = id_district;
    }

    public String getU_avatar() {
        return u_avatar;
    }

    public void setU_avatar(String u_avatar) {
        this.u_avatar = u_avatar;
    }

    public String getAvatarSocial() {
        return avatar_fb;
    }

    public void setAvatarSocial(String avatar_fb) {
        this.avatar_fb = avatar_fb;
    }

    public String getU_coin() {
        return u_coin;
    }

    public void setU_coin(String u_coin) {
        this.u_coin = u_coin;
    }

    public String getU_account() {
        return u_account;
    }

    public void setU_account(String u_account) {
        this.u_account = u_account;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getGgid() {
        return ggid;
    }

    public void setGgid(String ggid) {
        this.ggid = ggid;
    }

    public String getEmail_active() {
        return email_active;
    }

    public void setEmail_active(String email_active) {
        this.email_active = email_active;
    }

    public String getPhone_active() {
        return phone_active;
    }

    public void setPhone_active(String phone_active) {
        this.phone_active = phone_active;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getChange_password() {
        return change_password;
    }

    public void setChange_password(int change_password) {
        this.change_password = change_password;
    }

    public String getTime_change_password() {
        return time_change_password;
    }

    public void setTime_change_password(String time_change_password) {
        this.time_change_password = time_change_password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogin_session() {
        return login_session;
    }

    public void setLogin_session(String login_session) {
        this.login_session = login_session;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    //    public String getAvatarSocial() {
//        return avatarSocial;
//    }
//
//    public void setAvatarSocial(String avatarSocial) {
//        this.avatarSocial = avatarSocial;
//    }
//
//    @SerializedName("u_id")
//    private String id;
//    @SerializedName("avatar_fb")
//    private String avatarSocial;
//    @SerializedName("change_password")
//    private int changepassword;
//    @SerializedName("type")
//    private int typeUser;
//    @SerializedName("u_phone")
//    private String phone;
//    @SerializedName("u_email")
//    private String email;
//    @SerializedName("u_name")
//    private String name;
//    private String personal_code;
//    @SerializedName("rf_code")
//    private String u_email;
//    @SerializedName("password")
//    private int u_password;
//    @SerializedName("verify_code")
//    private String verify_code;
//    @SerializedName("u_address")
//    private String u_address;
//    @SerializedName("address1")
//    private String address1;
//    @SerializedName("street")
//    private String street;
//    @SerializedName("id_province")
//    private String id_province;
//    @SerializedName("id_district")
//    private String id_district;
//    @SerializedName("u_avatar")
//    private String u_avatar;
//    @SerializedName("u_coin")
//    private String u_coin;
//    @SerializedName("u_account")
//    private String u_account;
//    @SerializedName("fbid")
//    private String fbid;
//    @SerializedName("ggid")
//    private String ggid;
//    @SerializedName("email_active")
//    private String email_active;
//    @SerializedName("phone_active")
//    private String phone_active;
//    @SerializedName("reg_date")
//    private String reg_date;
//    @SerializedName("active")
//    private String active;
//
//    @SerializedName("time_change_password")
//    private String time_change_password;
//    @SerializedName("status")
//    private String status;
//    @SerializedName("login_session")
//    private String login_session;
//    @SerializedName("imei")
//    private String imei;
//
//    public int getChangepassword() {
//        return changepassword;
//    }
//
//    public void setChangepassword(int changepassword) {
//        this.changepassword = changepassword;
//    }
//
//    public User() {
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPersonal_code() {
//        return personal_code;
//    }
//
//    public void setPersonal_code(String personal_code) {
//        this.personal_code = personal_code;
//    }
//
//    public String getU_email() {
//        return u_email;
//    }
//
//    public void setU_email(String u_email) {
//        this.u_email = u_email;
//    }
//
//    public int getU_password() {
//        return u_password;
//    }
//
//    public void setU_password(int u_password) {
//        this.u_password = u_password;
//    }
//
//    public String getVerify_code() {
//        return verify_code;
//    }
//
//    public void setVerify_code(String verify_code) {
//        this.verify_code = verify_code;
//    }
//
//    public String getU_address() {
//        return u_address;
//    }
//
//    public void setU_address(String u_address) {
//        this.u_address = u_address;
//    }
//
//    public String getAddress1() {
//        return address1;
//    }
//
//    public void setAddress1(String address1) {
//        this.address1 = address1;
//    }
//
//    public String getStreet() {
//        return street;
//    }
//
//    public void setStreet(String street) {
//        this.street = street;
//    }
//
//    public String getId_province() {
//        return id_province;
//    }
//
//    public void setId_province(String id_province) {
//        this.id_province = id_province;
//    }
//
//    public String getId_district() {
//        return id_district;
//    }
//
//    public void setId_district(String id_district) {
//        this.id_district = id_district;
//    }
//
//    public String getU_avatar() {
//        return u_avatar;
//    }
//
//    public void setU_avatar(String u_avatar) {
//        this.u_avatar = u_avatar;
//    }
//
//    public String getU_coin() {
//        return u_coin;
//    }
//
//    public void setU_coin(String u_coin) {
//        this.u_coin = u_coin;
//    }
//
//    public String getU_account() {
//        return u_account;
//    }
//
//    public void setU_account(String u_account) {
//        this.u_account = u_account;
//    }
//
//    public String getFbid() {
//        return fbid;
//    }
//
//    public void setFbid(String fbid) {
//        this.fbid = fbid;
//    }
//
//    public String getGgid() {
//        return ggid;
//    }
//
//    public void setGgid(String ggid) {
//        this.ggid = ggid;
//    }
//
//    public String getEmail_active() {
//        return email_active;
//    }
//
//    public void setEmail_active(String email_active) {
//        this.email_active = email_active;
//    }
//
//    public String getPhone_active() {
//        return phone_active;
//    }
//
//    public void setPhone_active(String phone_active) {
//        this.phone_active = phone_active;
//    }
//
//    public String getReg_date() {
//        return reg_date;
//    }
//
//    public void setReg_date(String reg_date) {
//        this.reg_date = reg_date;
//    }
//
//    public String getActive() {
//        return active;
//    }
//
//    public void setActive(String active) {
//        this.active = active;
//    }
//
//
//
//    public String getTime_change_password() {
//        return time_change_password;
//    }
//
//    public void setTime_change_password(String time_change_password) {
//        this.time_change_password = time_change_password;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getLogin_session() {
//        return login_session;
//    }
//
//    public void setLogin_session(String login_session) {
//        this.login_session = login_session;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.id);
//        dest.writeString(this.avatarSocial);
//        dest.writeInt(this.changepassword);
//        dest.writeInt(this.typeUser);
//        dest.writeString(this.phone);
//        dest.writeString(this.email);
//        dest.writeString(this.name);
//        dest.writeString(this.personal_code);
//        dest.writeString(this.u_email);
//        dest.writeInt(this.u_password);
//        dest.writeString(this.verify_code);
//        dest.writeString(this.u_address);
//        dest.writeString(this.address1);
//        dest.writeString(this.street);
//        dest.writeString(this.id_province);
//        dest.writeString(this.id_district);
//        dest.writeString(this.u_avatar);
//        dest.writeString(this.u_coin);
//        dest.writeString(this.u_account);
//        dest.writeString(this.fbid);
//        dest.writeString(this.ggid);
//        dest.writeString(this.email_active);
//        dest.writeString(this.phone_active);
//        dest.writeString(this.reg_date);
//        dest.writeString(this.active);
//        dest.writeString(this.time_change_password);
//        dest.writeString(this.status);
//        dest.writeString(this.login_session);
//    }
//
//    protected User(Parcel in) {
//        this.id = in.readString();
//        this.avatarSocial = in.readString();
//        this.changepassword = in.readInt();
//        this.typeUser = in.readInt();
//        this.phone = in.readString();
//        this.email = in.readString();
//        this.name = in.readString();
//        this.personal_code = in.readString();
//        this.u_email = in.readString();
//        this.u_password = in.readInt();
//        this.verify_code = in.readString();
//        this.u_address = in.readString();
//        this.address1 = in.readString();
//        this.street = in.readString();
//        this.id_province = in.readString();
//        this.id_district = in.readString();
//        this.u_avatar = in.readString();
//        this.u_coin = in.readString();
//        this.u_account = in.readString();
//        this.fbid = in.readString();
//        this.ggid = in.readString();
//        this.email_active = in.readString();
//        this.phone_active = in.readString();
//        this.reg_date = in.readString();
//        this.active = in.readString();
//        this.time_change_password = in.readString();
//        this.status = in.readString();
//        this.login_session = in.readString();
//    }
//
//    public static final Creator<User> CREATOR = new Creator<User>() {
//        @Override
//        public User createFromParcel(Parcel source) {
//            return new User(source);
//        }
//
//        @Override
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.u_id);
        dest.writeString(this.personal_code);
        dest.writeString(this.rf_code);
        dest.writeString(this.u_name);
        dest.writeString(this.u_phone);
        dest.writeString(this.u_email);
        dest.writeString(this.u_password);
        dest.writeString(this.verify_code);
        dest.writeString(this.u_address);
        dest.writeString(this.address1);
        dest.writeString(this.street);
        dest.writeString(this.id_province);
        dest.writeString(this.id_district);
        dest.writeString(this.u_avatar);
        dest.writeString(this.avatar_fb);
        dest.writeString(this.u_coin);
        dest.writeString(this.u_account);
        dest.writeString(this.fbid);
        dest.writeString(this.ggid);
        dest.writeString(this.email_active);
        dest.writeString(this.phone_active);
        dest.writeString(this.reg_date);
        dest.writeString(this.active);
        dest.writeInt(this.change_password);
        dest.writeString(this.time_change_password);
        dest.writeString(this.status);
        dest.writeString(this.login_session);
        dest.writeString(this.imei);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.u_id = in.readString();
        this.personal_code = in.readString();
        this.rf_code = in.readString();
        this.u_name = in.readString();
        this.u_phone = in.readString();
        this.u_email = in.readString();
        this.u_password = in.readString();
        this.verify_code = in.readString();
        this.u_address = in.readString();
        this.address1 = in.readString();
        this.street = in.readString();
        this.id_province = in.readString();
        this.id_district = in.readString();
        this.u_avatar = in.readString();
        this.avatar_fb = in.readString();
        this.u_coin = in.readString();
        this.u_account = in.readString();
        this.fbid = in.readString();
        this.ggid = in.readString();
        this.email_active = in.readString();
        this.phone_active = in.readString();
        this.reg_date = in.readString();
        this.active = in.readString();
        this.change_password = in.readInt();
        this.time_change_password = in.readString();
        this.status = in.readString();
        this.login_session = in.readString();
        this.imei = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
