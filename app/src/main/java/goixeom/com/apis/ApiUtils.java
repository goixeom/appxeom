package goixeom.com.apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MyPC on 9/12/2016.
 */
public class ApiUtils {
    public static Retrofit getRootApi() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiConstants.API_ROOT).client(okHttpClient).addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit;
    }

    public static Retrofit getAPIPLACE() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder().disableHtmlEscaping().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit;
    }
//
//    public static String getTokenAccess(Activity activity) {
//        String token = SharedPref.getInstance(activity).getString(Constant.KEY_ACCESSTOKEN, "");
//        RLog.e(token);
//        if (token.isEmpty()) {
//            Intent intent = new Intent(activity, SplashActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            activity.startActivity(intent);
//            activity.finish();
//            return "";
//        } else {
//            return token;
//        }
//    }
//
//    public static User getUser(Activity activity) {
//        User user = AppController.getInstance().getUser();
//        if (user != null) {
//            return user;
//        } else {
//            Intent intent = new Intent(activity, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            activity.startActivity(intent);
//
//            return null;
////            activity.finish();
//        }
//    }

    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri


        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }


}
