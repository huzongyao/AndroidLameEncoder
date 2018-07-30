package com.hzy.lame.demo;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class CommonUtils {

    public static String getMp3FilePath(Context context) {
        String path = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = context.getExternalFilesDir("mp3") +
                    File.separator + generateMp3FileName();
        }
        if (path == null) {
            path = context.getFilesDir() + File.separator + "mp3"
                    + File.separator + generateMp3FileName();
        }
        return path;
    }

    public static String generateMp3FileName() {
        long backTime = new Date().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(backTime));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        return "rec_" + year + month + date + hour + minute + second + ".mp3";
    }
}
