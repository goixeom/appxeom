package goixeom.com.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import me.zhanghai.android.materialprogressbar.BuildConfig;

public class FileUtils {
    public static String getFolder(Context context) {
        String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + context.getString(com.wang.avi.R.string.app_name) + File.separator + "file" + File.separator;
        File folder = new File(filePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return filePath;
    }

    public void openFolder() {
    }

    public static void openFolder(Context context) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory().getPath() + getFolder(context)), "text/csv");
        context.startActivity(Intent.createChooser(intent, "Open folder"));
    }



    public static String readFile(Context context, String nameFile, String namePath) {
        return BuildConfig.FLAVOR;
    }
}
