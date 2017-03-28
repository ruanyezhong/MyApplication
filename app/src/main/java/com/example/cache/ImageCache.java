package com.example.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.administrator.myapplication.R;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片缓存类
 * Created by Administrator on 2017/3/21.
 */

public class ImageCache {

    private static ImageCache imageInstance;  //单例

    private ImageCache() {
    }

    public static ImageCache getImageInstance() {
        if (imageInstance == null) {
            synchronized (ImageCache.class) {
                if (imageInstance == null)
                    imageInstance = new ImageCache();
            }
        }
        return imageInstance;
    }

    int maxMemory = (int) Runtime.getRuntime().maxMemory();
    int cacheSize = maxMemory / 8;
    LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {  //缓存图片
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount();
        }
    };

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null)
            mMemoryCache.put(key, bitmap);
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    public void loadBitmap(int resId, ImageView imageView) {
        String imageKey = String.valueOf(resId);
        Bitmap bitmap = getBitmapFromMemoryCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.ic_image_add);
            BitmapWorkerTask workerTask = new BitmapWorkerTask();
            workerTask.execute(imageKey);

        }

    }

    class BitmapWorkerTask extends AsyncTask<String, Integer, Bitmap> {
        private String imageUrl;

        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            Bitmap bitmap = downLoadBitmap(params[0]);
            if (bitmap != null) {
                addBitmapToMemoryCache(params[0], bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //更新UI(显示图片)

        }
    }

    //建立HTTP请求，并获取Bitmap对象
    private Bitmap downLoadBitmap(String imageUrl) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(imageUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(10 * 1000);
           // conn.setDoInput(true);
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bitmap;
    }

}
