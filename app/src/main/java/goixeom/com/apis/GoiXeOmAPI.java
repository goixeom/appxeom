package goixeom.com.apis;


import java.util.List;

import goixeom.com.models.BookingTrip;
import goixeom.com.models.Discount;
import goixeom.com.models.History;
import goixeom.com.models.MyPlace;
import goixeom.com.models.News;
import goixeom.com.models.NotificationData;
import goixeom.com.models.PlaceNearby;
import goixeom.com.models.Price;
import goixeom.com.models.Routes;
import goixeom.com.models.StringResponse;
import goixeom.com.models.TripInforModel;
import goixeom.com.models.User;
import goixeom.com.models.VerifyCode;
import goixeom.com.socket.LocationBearing;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by MyPC on 9/12/2016.
 */
public interface GoiXeOmAPI {
    @GET(ApiConstants.API_CHECK_PHONE_EXIST)
    Call<ApiResponse<StringResponse>> checkPhoneExist(@Query("key") String key, @Query("phone") String phone);

    @GET(ApiConstants.API_INFOR_APP)
    Call<ApiResponse<String>> getInfoApp(@Query("key") String key);
    @GET(ApiConstants.API_NOTIFICATION)
    Call<ApiResponse<List<NotificationData>>> getListNotification(@Query("key") String key, @Query("id") String id);
    @FormUrlEncoded
    @POST(ApiConstants.API_UPDATE_PROFILE)
    Call<ApiResponse<String>> updateInformation(@Field("key") String key, @Field("phone") String phone
            , @Field("id") String id
            , @Field("name") String nameField
            , @Field("email") String email);

    @GET(ApiConstants.API_SMS)
    Call<ApiResponse<VerifyCode>> sendSMS(@Query("key") String key, @Query("phone") String phone);

    @FormUrlEncoded
    @POST(ApiConstants.API_REGISTER)
    Call<ApiResponse<String>> register(@Field("key") String key, @Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST(ApiConstants.API_FEEDBACK)
    Call<ApiResponse> feedback(@Field("key") String key, @Field("id") String id, @Field("content") String content);

    @FormUrlEncoded
    @POST(ApiConstants.API_CHANGE_PASS)
    Call<ApiResponse> changePass(@Field("key") String key, @Field("id") String id, @Field("password") String content);

    @GET(ApiConstants.API_LOGIN)
    Call<ApiResponse<User>> login(@Query("key") String key, @Query("phone") String phone, @Query("password") String password, @Query("imei") String imei);

    @FormUrlEncoded
    @POST(ApiConstants.API_FORGOT_PASSWORD)
    Call<ApiResponse<String>> forgotPassword(@Field("key") String key, @Field("phone") String phone
            , @Field("email") String email);

    @POST(ApiConstants.API_SIGNIN_FB)
    Call<ApiResponse<User>> signInFaccebook(@Query("key") String key
            , @Query("fbid") String id
            , @Query("name") String nameField
            , @Query("email") String email
            , @Query("avatar") String avt, @Query("imei") String imei);

    @POST(ApiConstants.API_SIGNIN_GG)
    Call<ApiResponse<User>> signInGoogle(@Query("key") String key
            , @Query("ggid") String id
            , @Query("name") String nameField
            , @Query("email") String email
            , @Query("avatar") String avt, @Query("imei") String imei);

    @GET(ApiConstants.API_GET_INFO)
    Call<ApiResponse<User>> getInfor(@Query("key") String key, @Query("id") String id);

    @GET(ApiConstants.API_GET_PROMOTIONS_TYPE)
    Call<ApiResponse<List<Discount>>> getPromotions(@Query("key") String key, @Query("id") String id,@Query("type") int type);
    @GET(ApiConstants.API_GET_PROMOTIONS)
    Call<ApiResponse<List<Discount>>> getPromotions(@Query("key") String key, @Query("id") String id);

    @GET(ApiConstants.API_GET_HISTORY)
    Call<ApiResponse<List<History>>> getHistories(@Query("key") String key, @Query("id") String id, @Query("page") int page);

    @GET(ApiConstants.API_GET_DISTANCECOST)
    Call<ApiResponse<Price>> getDistanceCost(@Query("key") String key, @Query("distance") double distance);

    @GET(ApiConstants.API_GET_DRIVER)
    Call<ApiResponse<List<LocationBearing>>> getDriver(@Query("key") String key, @Query("lat") double lat, @Query("lng") double lng,@Query("type") int type);

    @FormUrlEncoded
    @POST(ApiConstants.API_CREATE_BOOKING)
    Call<ApiResponse<BookingTrip>> createBooking(@Field("key") String key
            , @Field("id") String id
            , @Field("start") String start
            , @Field("end") String end
            , @Field("price") int price
            , @Field("id_code") int id_code
            , @Field("distance") double distance
                , @Field("duration") String duration
            , @Field("lat_from") double latFrom
            , @Field("lng_from") double lngFrom
            , @Field("lat_to") double latTo
            , @Field("lng_to") double lngTo
            , @Field("type_address") String typeAddress
            , @Field("type") int typeService
            , @Field("name") String name
    );

    @GET(ApiConstants.API_GET_DRIVER_BOOKING)
    Call<ApiResponse<BookingTrip>> getListDriverBooking(@Query("key") String key, @Query("t_id") int idBooking);

    @FormUrlEncoded
    @POST(ApiConstants.API_CANCLE_BOOKING)
    Call<ApiResponse> cancleBooking(@Field("key") String key
            , @Field("t_id") int idTrip, @Field("status") int status);

    @FormUrlEncoded
    @POST(ApiConstants.API_RATE_DRIVER)
    Call<ApiResponse> rateDriver(@Field("key") String key
            , @Field("t_id") int idTrip, @Field("rate") int rate);
    @FormUrlEncoded
    @POST(ApiConstants.API_VOTE_DRIVER)
    Call<ApiResponse> voteDriver(@Field("key") String key
            , @Field("t_id") int idTrip, @Field("vote") int vote,@Field("rate") int rate);

    @FormUrlEncoded
    @POST(ApiConstants.API_ADD_TO_FAVOURITE)
    Call<ApiResponse> addAddressToFavourite(@Field("key") String key
            , @Field("id") String idUser, @Field("name") String name,@Field("address") String address,@Field("type") String type,@Field("lat") double lat,@Field("lng") double lng);
    @FormUrlEncoded
    @POST(ApiConstants.API_CLEAR_TO_FAVOURITE)
    Call<ApiResponse> clearToFavourite(@Field("key") String key
            , @Field("id") String id);

    @GET(ApiConstants.API_PLACE_SUGGESST)
    Call<ApiResponse<List<MyPlace>>> getAddress(@Query("input") String input
            , @Query("types") String type
            , @Query("strictbounds") boolean strictbounds
            , @Query("location") String location
            , @Query("radius") int radius
            , @Query("key") String key);

    @GET(ApiConstants.API_NEAR_BY)
    Call<ApiResponse<List<PlaceNearby>>> getNearby(
            @Query("location") String location
            , @Query("radius") int radius
            , @Query("type") String  type
            , @Query("limit") int limit
            , @Query("key") String key);

    @GET(ApiConstants.API_DIRECTION)
    Call<ApiResponse<List<Routes>>> getDirection(
            @Query("origin") String orgin
            , @Query("destination") String destination
            , @Query("key") String key);

    @GET("geocode/json")
    Call<ApiResponse<List<PlaceNearby>>> getLocation(
            @Query("address") String location
            , @Query("sensor") boolean sensor
            , @Query("key") String key
    );

    @GET(ApiConstants.API_DETAIL_BOOKING)
    Call<ApiResponse<TripInforModel>> getDetailBooking(@Query("key") String key
            , @Query("t_id") int idTrip
            , @Query("d_id") int idDriver);
    @GET(ApiConstants.API_GET_DETAIL_BOOKING)
    Call<ApiResponse<TripInforModel>> getDetailBooking(@Query("key") String key
            , @Query("t_id") int idTrip
           );
    @GET(ApiConstants.API_CONFIG)
    Call<ApiResponse<Integer>> getConfig(@Query("key") String key
    );
    @GET(ApiConstants.API_RECENTLY)
    Call<ApiResponse<List<MyPlace>>> getListRecently(@Query("key") String key,@Query("id") String idUser
    );
    @GET(ApiConstants.API_GET_FAV_ADDRESS)
    Call<ApiResponse<List<MyPlace>>> getListFavourite(@Query("key") String key,@Query("id") String idUser
    );
    @GET(ApiConstants.API_GET_NEWS)
    Call<ApiResponse<List<News>>> getNews(@Query("key") String key
    );
}
