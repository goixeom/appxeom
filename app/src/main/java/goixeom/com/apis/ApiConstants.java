package goixeom.com.apis;

/**
 * Created by MyPC on 9/12/2016.
 */
public class ApiConstants {

    public static final String API_ROOT="https://goixeom.com/webservice1/";
    public static final String API_CHECK_PHONE_EXIST="check-phone-customer.php";
    public static final String API_UPDATE_PROFILE="update-profile.php";
    public static final String API_SMS="verify-code-user.php";
    public static final String API_REGISTER="sign-up-customer.php";
    public static final String API_LOGIN="login-customer.php";
    public static final String API_FORGOT_PASSWORD="forgot-password-user.php";
    public static final String API_SIGNIN_FB="login-facebook.php";
    public static final String API_SIGNIN_GG="login-google.php";
    public static final String API_GET_INFO="get-user-info.php";
    public static final String API_PLACE_SUGGESST="place/autocomplete/json";
    public static final String API_GET_DISTANCECOST="price.php";
    public static final String API_GET_PROMOTIONS="promotion-code.php";
    public static final String API_GET_PROMOTIONS_TYPE="promotion-code-type.php";
    public static final String API_GET_HISTORY="trip-history.php";
    public static final String API_CREATE_BOOKING="book-trip.php";
    public static final String API_CANCLE_BOOKING="user-cancel.php";
    public static final String API_RATE_DRIVER="rate-driver.php";
    public static final String API_DETAIL_BOOKING="get-info-trip.php";
    public static final String API_GET_DETAIL_BOOKING="info-trip.php";
    public static final String API_NOTIFICATION="notification-customer.php";
    public static final String API_INFOR_APP="get-info.php";
    public static final String API_FEEDBACK="feedback.php";
    public static final String API_CHANGE_PASS="change-password-user.php";
    public static final String API_NEAR_BY="place/nearbysearch/json";
    public static final String API_DIRECTION="directions/json";
    public static final String API_GET_LOCATION_FROM_ADDRESS="geocode/json";
    public static final String API_CONFIG="get-config.php";
    public static final String API_CONFIG_APP="config.php";
    public static final String API_KEY="9a887d75fed2ffb7b641dbf85b3bffd9";
    public static final String API_GET_DRIVER="get-driver.php";
    public static final String API_GET_DRIVER_BOOKING="get-driver-booking.php";
    public static final String API_VOTE_DRIVER="vote-driver.php";
    public static final String API_ADD_TO_FAVOURITE="favorite.php";
    public static final String API_CLEAR_TO_FAVOURITE="delete-favorite.php";
    public static final String API_RECENTLY="list-address.php";
    public static final String API_GET_FAV_ADDRESS="address-favorite.php";
    public static final String API_GET_NEWS="news.php";


    //CODE
    public static final int CODE_SUCESS = 200;
    public static final int CODE_ERROR_PARAM = 404;
    public static final int CODE_ERROR_SERVER = 500;
    public static final int CODE_ERROR_ACTIVED = 400;
    public static final int CODE_ERROR_LOGIN_PARAM = 300;
    public static final int CODE_ERROR_NO_EXIST_ACC = 404;
    public static final int CODE_ERROR_ALREADY_ACTIVE = 100;
    public static final int CODE_ERROR_LOCKED_ACC = 101;
    public static final int CODE_DEVICE_ANDROID = 1;
    public static final int CODE_CUSTOMER = 2;
    public static final int CODE_FINDING = 0;
    public static final int CODE_BIDED = 1;
    public static final int CODE_FINISH = 2;
    public static final int CODE_DECLINE = -1;
    public static final int CODE_TIME_OUT = -2;


    //LOGIN
    public static final String KEY_PHONE_NUMBER="phone_number";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_DEVICE_TOKEN="device_token";
    public static final String KEY_TYPE_DEVICE="type_device";
    public static final String KEY_TYPE_USER="type_user";


    //ACCESSTOKEN

    public static final String KEY_USERNAME="UserName";
    public static final String KEY_PASSWORD_ACCESSTOKEN="Password";
    public static final String KEY_LANGUAGE="Language";
    public static final String KEY_SITE="Site";

    //STORE
    public static final String KEY_ACCESSTOKEN="AccessToken";
    public static final String KEY_PAGESIZE ="PageSize";
    public static final String KEY_PAGEINDEX="PageIndex";
    public static final String KEY_QUERY="Query";
    //PRODUCT
    public static final String KEY_CODE="Code";


    public static final String KEY_COLLECTION = "Collection";
    public static final String KEY_COLOR = "Color";
    public static final String KEY_STYLE = "Style";
    public static final String KEY_SIZE = "Size";

    public static final String KEY_IMAGE_SIZE = "ImageWidth";

    public static final String KEY_PRODUCT_CODE="ProductCode";


    public static final String KEY_MODE = "Mode";
    public static final String KEY_AUTHTOKEN="AuthenticatedAccessToken";

}
