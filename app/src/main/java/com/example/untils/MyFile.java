package com.example.untils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 文件类 （IO）
 * Created by Administrator on 2017/3/15.
 */

public class MyFile {

    private String readFile(Context context, String path) {

        try {
            FileInputStream fis = context.openFileInput(path);
            byte[] bytes = new byte[1024];
            int len;
            StringBuffer sb = new StringBuffer();
            while ((len = fis.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            fis.close();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean writeFile(Context context, String path, String msg) {
        if (msg == null)
            return false;
        try {
            FileOutputStream fos = context.openFileOutput(path, Context.MODE_PRIVATE);
            fos.write(msg.getBytes());
            fos.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
