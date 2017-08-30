package goixeom.com.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import goixeom.com.R;
import goixeom.com.apis.ApiConstants;
import goixeom.com.apis.ApiResponse;
import goixeom.com.apis.CallBackCustom;
import goixeom.com.interfaces.OnResponse;
import goixeom.com.models.AppConfig;
import goixeom.com.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashScreenActivity extends BaseAuthActivity {
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    10000);
        } else {
            getAppConfig();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10000 ){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED){
                showDialogPermission();
            }else{
                getAppConfig();
            }
        }
    }
    private void showDialogPermission() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(this).title(getString(R.string.error))
                .content("Bạn cần cấp quyền truy cập ứng dụng để tiếp tục sử dụng dịch vụ")
                .positiveColor(Color.GRAY)
                .positiveText("Đồng ý")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ActivityCompat.requestPermissions(SplashScreenActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                10000);
                    }
                }).show();
    }
    private void getAppConfig() {
        Call<ApiResponse<AppConfig>> getApp = getmApi().getAppConfig(ApiConstants.API_KEY);
        getApp.enqueue(new CallBackCustom<ApiResponse<AppConfig>>(this, new OnResponse<ApiResponse<AppConfig>>() {
            @Override
            public void onResponse(ApiResponse<AppConfig> object) {
                getmSetting().put(Constants.TIME_UPDATE, object.getData().getTimeUpdate());
                downloadImage(object.getData());
            }
        }));
    }
    private void downloadImage(final AppConfig app) {
        Call<ResponseBody> download = getmApi().downloadFileWithDynamicUrlAsync(app.getBike());
        download.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            File writtenToDisk = writeResponseBodyToDisk(response.body(), getString(R.string.bike));

                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            downloadImageCar(app.getCar());
                        }
                    }.execute();
                } else {

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }
        });
    }

    private void downloadImageCar(String Url) {
        Call<ResponseBody> download = getmApi().downloadFileWithDynamicUrlAsync(Url);
        download.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            File writtenToDisk = writeResponseBodyToDisk(response.body(),  getString(R.string.car));
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent;
                            intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                    }.execute();
                } else {
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.e(t.getMessage());
            }
        });
    }
    private File writeResponseBodyToDisk(ResponseBody body, String name) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(goixeom.com.utils.FileUtils.getFolder(this)  + name);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    LogUtils.e("file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return futureStudioIconFile;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
