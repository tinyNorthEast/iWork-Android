package com.iwork.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.iwork.helper.ResourcesHelper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;


/**
 * 图片操作工具(加载、创建、缩放、剪裁、圆角、复制...)
 * 
 */
@SuppressLint("NewApi")
public class ImageUtil {
    private static final String TAG = "ImageUtil";

    private static final int MAX_SIZE = 1280;// 最大尺寸400

    /**
     * Convert drawable to Bitmap
     * 
     * @param drawable
     * @return bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null)
            return null;

        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError e) {
        }
        if (bitmap == null) {
            return bitmap;
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Resize bitmap in original scale
     *
     * @param bitmap
     * @param aMaxWidth
     * @param aMaxHeight
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int aMaxWidth, int aMaxHeight) {

        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        // no need to resize
        if (originWidth < aMaxWidth && originHeight < aMaxHeight) {
            return bitmap;
        }

        int newWidth = originWidth;
        int newHeight = originHeight;

        if (originWidth > aMaxWidth) {
            newWidth = aMaxWidth;

            double i = originWidth * 1.0 / aMaxWidth;
            newHeight = (int) Math.floor(originHeight / i);

            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        }

        if (newHeight > aMaxHeight) {
            newHeight = aMaxHeight;

            int startPointY = (int) ((originHeight - aMaxHeight) / 2.0);
            bitmap = Bitmap.createBitmap(bitmap, 0, startPointY, newWidth, newHeight);
        }

        return bitmap;
    }

    /**
     * 增加圆角
     *
     * @param bitmap
     * @param width
     * @param height
     * @param radius
     * @return
     */
    public static Bitmap round(Bitmap bitmap, int width, int height, int radius, boolean recycleSource) {
        if (width == 0 || height == 0 || radius <= 0 || bitmap == null)
            return bitmap;

        Bitmap ret = null;
        try {
            ret = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            // OutOfMemoryHandler.handle();
        }
        if (ret == null)
            return null;

        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if (recycleSource)
            ImageUtil.clear(bitmap);
        return ret;
    }

    /**
     * 增加圆角
     *
     * @param bitmap
     * @param radius
     * @return
     */
    public static Bitmap round(Bitmap bitmap, int radius, boolean recycleSource) {
        if (radius <= 0 || bitmap == null)
            return bitmap;
        return round(bitmap, bitmap.getWidth(), bitmap.getHeight(), radius, recycleSource);
    }

    /**
     * Get rounded bitmap image
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap bitmap, int width, int height) {
        if (width == 0 || height == 0) {
            return null;
        }
        Bitmap resizedBitmap = null;
        try {
            resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        } catch (OutOfMemoryError e) {
            // OutOfMemoryHandler.handle();
        }
        if (resizedBitmap == null)
            return null;
        else {
            ImageUtil.clear(bitmap);
            return resizedBitmap;
        }
    }

    /**
     * 裁剪图片，默认先缩放
     *
     * @param bitmapPath
     *            原图片路径
     * @param width
     * @param height
     * @return
     */
    public static Bitmap crop(String bitmapPath, int width, int height) {
        Bitmap bitmap = createBitmap(bitmapPath);
        bitmap = scale(bitmap, width, height, ScaleType.CENTER_CROP, true);
        return crop(bitmap, width, height, true);
    }

    /**
     * 裁剪图片，默认先缩放
     *
     * @param bitmap
     *            原图
     * @param width
     * @param height
     * @param recycleSource
     *            是否回收原图
     * @return
     */
    public static Bitmap scaleAndCrop(Bitmap bitmap, int width, int height, boolean recycleSource) {
        bitmap = scale(bitmap, width, height, ScaleType.CENTER_CROP, recycleSource);
        return crop(bitmap, width, height, true);
    }

    /**
     * 剪裁图片 思路：取原图与目标大小的交叉部分
     *
     * @param sourceBitmap
     *            原图
     * @param targetWidth
     *            剪裁到的宽度
     * @param targetHeight
     *            剪裁到的高度
     * @param recycleSource
     *            是否回收原图
     * @return
     */
    private static Bitmap crop(Bitmap sourceBitmap, int targetWidth, int targetHeight, boolean recycleSource) {
        if (sourceBitmap == null)
            return null;

        Bitmap croppedBitmap = null;

        // 获取原图缩放之后与目标图的交叉区域
        int xDiff = Math.max(0, sourceBitmap.getWidth() - targetWidth);
        int yDiff = Math.max(0, sourceBitmap.getHeight() - targetHeight);
        int x = xDiff / 2;
        int y = yDiff / 2;

        try {
            // 根据交叉区域进行剪裁
            croppedBitmap = Bitmap.createBitmap(sourceBitmap, x, y, targetWidth, targetHeight);
        } catch (OutOfMemoryError e) {
            // OutOfMemoryHandler.handle();
        }

        if (recycleSource && sourceBitmap != croppedBitmap)
            clear(sourceBitmap);
        return croppedBitmap;
    }

    /**
     * 等比缩放图片
     *
     * @param sourceBitmap
     *            原图
     * @param targetWidth
     *            目标宽度
     * @param targetHeight
     *            目标高度
     * @param scaleType
     *            缩放类型同ImageView.ScaleType，但只用到CENTER_CROP和 CENTER_INSIDE
     * @param recycleSource
     *            是否回收原图
     *
     * @return
     */
    public static Bitmap scale(Bitmap sourceBitmap, float targetWidth, float targetHeight, ScaleType scaleType, boolean recycleSource) {
        if (sourceBitmap == null || sourceBitmap.isRecycled())
            return null;

        Bitmap scaledBitmap = null;

        float scale;

        float sourceWidth = sourceBitmap.getWidth();
        float sourceHeight = sourceBitmap.getHeight();

        float sourceRatio = sourceWidth / sourceHeight;
        float targetRatio = targetWidth / targetHeight;

        // 计算缩放比例，比较(原图宽/高比)和(目标图的宽/高比)，若前者大用高度比例，否则用宽度比例
        if (scaleType == ScaleType.CENTER_CROP)
            scale = sourceRatio > targetRatio ? targetHeight / sourceHeight : targetWidth / sourceWidth;
        else
            scale = sourceRatio < targetRatio ? targetHeight / sourceHeight : targetWidth / sourceWidth;

        // 不需要缩放，直接返回
        if (scale == 1.0F)
            return sourceBitmap;

        // 创建缩放矩阵
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);

        // XjLog.d(TAG, "sourceWidth : " + sourceWidth + " , sourceHeight : "
        // + sourceHeight);
        try {
            // 将原图缩放
            scaledBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);

            // XjLog.d(TAG, "scaled: width : " + scaledBitmap.getWidth());
            // XjLog.d(TAG, "scaled: height : " + scaledBitmap.getHeight());
        } catch (IllegalArgumentException e) {
        } catch (OutOfMemoryError e) {
            // OutOfMemoryHandler.handle();
        }

        if (recycleSource && sourceBitmap != scaledBitmap)
            clear(sourceBitmap);
        return scaledBitmap;
    }

    public static Options getBitmapOptions(String path, int maxSize) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = (options.outWidth > options.outHeight ? options.outWidth : options.outHeight)
                / (maxSize < 1 ? MAX_SIZE : maxSize);
        options.inJustDecodeBounds = false;
        return options;
    }

    public static Options getBitmapOptions(InputStream is, int maxSize) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize = (options.outWidth > options.outHeight ? options.outWidth : options.outHeight) / maxSize;
        options.inJustDecodeBounds = false;
        return options;
    }

    public static Options getBitmapOptions(Resources res, int resId, int maxSize) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = (options.outWidth > options.outHeight ? options.outWidth : options.outHeight) / maxSize;
        options.inJustDecodeBounds = false;
        return options;
    }

    public static Options getBitmapOptions(Context context, Uri uri, int maxSize) {
        if (context == null || uri == null)
            return null;
        Options options = new Options();

        FileDescriptor fd = getFileDescriptor(context, uri);

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);

        options.inSampleSize = (options.outWidth > options.outHeight ? options.outWidth : options.outHeight) / maxSize;

        options.inJustDecodeBounds = false;

        return options;
    }

    public static FileDescriptor getFileDescriptor(Context context, Uri uri) {
        if (context == null || uri == null)
            return null;
        ContentResolver res = context.getContentResolver();

        ParcelFileDescriptor parcelFileDescriptor = null;
        FileDescriptor fd = null;
        try {
            parcelFileDescriptor = res.openFileDescriptor(uri, "r");
            fd = parcelFileDescriptor.getFileDescriptor();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (parcelFileDescriptor != null)
                    parcelFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fd;
    }

    public static Bitmap createBitmap(String path) {
        return createBitmap(path, MAX_SIZE);
    }

    public static Bitmap createBitmap(String path, int maxSize) {
        if (Utils.isTextEmpty(path))
            return null;
        File file = new File(path);
        if (!file.exists() || !file.isFile())
            return null;
        Bitmap bitmap = null;
        Options options = getBitmapOptions(path, maxSize);
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis, null, options);
            // bitmap = BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError e) {
            // OutOfMemoryHandler.handle();
            System.gc();
            // return createBitmap(path, maxSize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return bitmap;
    }

    public static Bitmap createBitmap(File file) {
        return createBitmap(file.getAbsolutePath());
    }

    public static Bitmap createBitmap(int width, int height, Config config) {
        if (width <= 0 || height <= 0)
            return null;
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            // OutOfMemoryHandler.handle();
        }
        return bitmap;
    }

    public static Bitmap createBitmap(InputStream is) {
        if (is == null)
            return null;
        Bitmap bitmap = null;
        try {
            Options options = getBitmapOptions(is, MAX_SIZE);
            bitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (OutOfMemoryError e) {
        }
        return bitmap;
    }

    public static Bitmap createBitmap(InputStream is, Rect outPadding, Options opts) {
        if (is == null)
            return null;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(is, outPadding, opts);
        } catch (OutOfMemoryError e) {
            // OutOfMemoryHandler.handle();
        }
        return bitmap;
    }

    public static Bitmap createBitmap(Context context, int resId) {
        if (context == null)
            return null;
        Bitmap bitmap = null;
        try {
            Options options = getBitmapOptions(context.getResources(), resId, MAX_SIZE);
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        } catch (OutOfMemoryError e) {
            // OutOfMemoryHandler.handle();
        }
        return bitmap;
    }

    public static Drawable createDrawable(int resId) {
        return ResourcesHelper.getDrawable(resId);
    }

    /**
     * 从Uri中获取一张bitmap
     *
     * @param context
     * @param uri
     * @param maxSize
     * @return
     */
    public static Bitmap createBitmap(Context context, Uri uri, int maxSize) {
        if (context == null || uri == null)
            return null;
        FileDescriptor fd = getFileDescriptor(context, uri);
        Options options = getBitmapOptions(context, uri, maxSize);
        if (fd == null || options == null)
            return null;
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
        if (bitmap != null && (bitmap.getWidth() > options.outWidth || bitmap.getHeight() > options.outHeight)) {
            Bitmap tmp = Bitmap.createScaledBitmap(bitmap, options.outWidth, options.outHeight, true);
            if (tmp != null && tmp != bitmap)
                ImageUtil.clear(bitmap);
            if (tmp != null)
                bitmap = tmp;
        }
        return bitmap;
    }

    /**
     * 根据参数修整图片
     *
     * @param bitmap
     * @param width
     * @param height
     * @param radius
     * @param needCrop
     * @param needScale
     * @return
     */
    public static Bitmap revise(Bitmap bitmap, int width, int height, int radius, boolean needCrop, boolean needScale) {
        return revise(bitmap, width, height, radius, needCrop, needScale, true);
    }

    /**
     * 异步剪裁图片
     *
     * @param bitmap
     * @param width
     * @param height
     * @param radius
     * @param needCrop
     * @param needScale
     * @param listener
     */
    public static void revise(
            final Bitmap bitmap, final int width, final int height, final int radius, final boolean needCrop, final boolean needScale,
            final ImageListener listener) {
        Thread job = new Thread() {
            Bitmap mBitmap;

            @Override
            public void run() {
                mBitmap = revise(bitmap, width, height, radius, needCrop, needScale, true);
                UiThreadHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (listener == null)
                            return;
                        listener.onRevise(mBitmap);
                    }
                });
            }

        };
        job.start();
    }

    /**
     * 根据参数修整图片
     *
     * @param bitmap
     * @param width
     * @param height
     * @param radius
     * @param needCrop
     * @param needScale
     * @param recycleSource
     *            是否回收原图
     * @return
     */
    public static Bitmap revise(Bitmap bitmap, int width, int height, int radius, boolean needCrop, boolean needScale, boolean recycleSource) {
        if (bitmap == null)
            return null;
        if (needCrop && needScale || (radius > 0 && (width > 0 || height > 0))) {
            bitmap = scaleAndCrop(bitmap, width, height, recycleSource);
        } else if (needScale) {
            bitmap = scale(bitmap, width, height, ImageView.ScaleType.CENTER_INSIDE, recycleSource);
        } else if (needCrop)
            bitmap = crop(bitmap, width, height, recycleSource);
        if (radius > 0)
            bitmap = ImageUtil.round(bitmap, width, height, radius, recycleSource);
        return bitmap;
    }

    /**
     * 根据参数修整图片
     * 
     * @param filename
     * @param context
     * @param width
     * @param height
     * @param radius
     * @param needCrop
     * @param needScale
     * @return
     */
    public static Bitmap revise(String filename, Context context, int width, int height, int radius, boolean needCrop, boolean needScale) {
        Bitmap bitmap = ImageUtil.createBitmap(context, filename, Math.max(width, height));
        return revise(bitmap, width, height, radius, needCrop, needScale);
    }

    /**
     * 根据参数修整图片
     * 
     * @param filepath
     * @param width
     * @param height
     * @param radius
     * @param needCrop
     * @param needScale
     * @return
     */
    public static Bitmap revise(String filepath, int width, int height, int radius, boolean needCrop, boolean needScale) {
        Bitmap bitmap = ImageUtil.createBitmap(filepath, Math.max(width, height));
        if (bitmap == null)
            return null;
        return revise(bitmap, width, height, radius, needCrop, needScale);
    }

    /**
     * 将bitmap写入文件
     * 
     * @param bitmap
     * @param path
     * @param quality
     * @param recycleSource
     * @return
     * {@link #writeToFile(Bitmap, String, int, boolean)}
     */
    public static String writeToFile(Bitmap bitmap, String path, int quality, boolean recycleSource) {
        if (bitmap == null)
            return null;
        if (bitmap.isRecycled()) {
            return null;
        }

        FileOutputStream fos = null;
        try {
            File f = new File(path);
            if (!f.exists()) {
                File parentFile = f.getAbsoluteFile().getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
            } else {
                f.delete();
            }

            if (f.createNewFile()) {
                // 因为是异步操作，UI 线程可能回收 Bitmap ，因此再多做一次处理。
                if (bitmap.isRecycled()) {
                    return null;
                }
                fos = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, fos);
                fos.flush();
                return path;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable e) {
        }
        finally {
            FileUtil.closeOutStream(fos);
        }
        if (recycleSource)
            clear(bitmap);

        return null;
    }

    /**
     * 释放bitmap
     * 
     * @param bitmap
     */
    public static void clear(Bitmap bitmap) {
        if (bitmap != null && Build.VERSION.SDK_INT < 14)
            bitmap.recycle();
    }

    /**
     * 刷新ImageView上的图片，并将原来的图片回收
     * 
     * @param iv
     * @param imgTagId
     * @param bitmap
     * @param defaultBitmap
     */
    public static void refresh(ImageView iv, int imgTagId, Bitmap bitmap, Bitmap defaultBitmap) {
        if (iv == null)
            return;
        Bitmap oldBitmap = (Bitmap) iv.getTag(imgTagId);
        if (oldBitmap != bitmap)
            clear(oldBitmap);
        iv.setImageBitmap(bitmap == null ? defaultBitmap : bitmap);
        iv.setTag(imgTagId, bitmap);
    }

    public static int refresh(ImageView iv, int imgTagId, Bitmap bitmap, int resId) {
        if (iv == null)
            return 0;
        int hashCode = 0;
        Bitmap oldBitmap = (Bitmap) iv.getTag(imgTagId);
        if (oldBitmap != null && oldBitmap != bitmap) {
            hashCode = oldBitmap.hashCode();
            clear(oldBitmap);
        }
        if (bitmap == null)
            iv.setImageResource(resId);
        else
            iv.setImageBitmap(bitmap);
        iv.setTag(imgTagId, bitmap);
        return hashCode;
    }

    public static Bitmap createBitmap(Context context, String filename) {
        return createBitmap(context, filename, MAX_SIZE);
    }

    public static Bitmap createBitmap(Context context, String filename, int maxSize) {
        if (context == null || Utils.isTextEmpty(filename))
            return null;
        filename = context.getFilesDir() + File.separator + filename;
        return createBitmap(filename, maxSize);
    }

    public static Bitmap createBitmapFromAsset(Context context, String filename) {
        if (context == null || Utils.isTextEmpty(filename))
            return null;
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = context.getAssets().open(filename);
            bitmap = ImageUtil.createBitmap(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Bitmap createBitmapFromFile(Context context, String filename) {
        if (context == null || Utils.isTextEmpty(filename))
            return null;
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = context.openFileInput(filename);
            bitmap = ImageUtil.createBitmap(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 对Bitmap.createBitmap的原始封装，只是加了内存溢出判断
     * 
     * @param src
     * @param x
     * @param y
     * @param width
     * @param height
     * @param m
     * @param filter
     * @return
     */
    public static Bitmap createBitmap(Bitmap src, int x, int y, int width, int height, Matrix m, boolean filter) {
        if (src == null)
            return null;
        Bitmap bitmap = src;
        try {
            // 将原图缩放
            bitmap = Bitmap.createBitmap(src, x, y, width, height, m, filter);
        } catch (IllegalArgumentException e) {
        } catch (OutOfMemoryError e) {
        }
        return bitmap;
    }

    /**
     * 将Bitmap转化成int数组
     * 
     * @param bitmap
     * @return
     */
    public static int[] getIntArray(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        int pix[] = null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        try {
            pix = new int[w * h];
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pix;
    }

    /**
     * 在Bitmap上做装饰
     * 
     * @param bitmap
     *            原始图片
     * @param dots
     *            装饰用的点
     * @return 装饰后的图片对象
     */
    public static Bitmap decorate(Bitmap bitmap, int[] dots) {
        if (bitmap == null || dots == null)
            return null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        try {
            if (dots.length != w * h)
                return null;
            if (dots.length > 0) {
                bitmap.setPixels(dots, 0, w, 0, 0, w, h);
                return bitmap;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将图片更新到MediaStore
     * 
     * @param resolver
     * @param title
     * @param location
     * @param orientation
     * @param jpeg
     *            图片二进制数据
     * @param path
     *            图片路径
     * @return
     */
    public static Uri saveToMediaStore(ContentResolver resolver, String title, Location location, int orientation, byte[] jpeg, String path) {
        ContentValues values = new ContentValues(9);
        values.put(ImageColumns.TITLE, title);
        values.put(ImageColumns.DISPLAY_NAME, title + ".jpg");
        Date d = new Date();
        values.put(ImageColumns.DATE_TAKEN, d.getDate());
        values.put(ImageColumns.MIME_TYPE, "image/jpeg");
        values.put(ImageColumns.ORIENTATION, orientation);
        values.put(ImageColumns.DATA, path);
        values.put(ImageColumns.SIZE, jpeg.length);

        if (location != null) {
            values.put(ImageColumns.LATITUDE, location.getLatitude());
            values.put(ImageColumns.LONGITUDE, location.getLongitude());
        }

        Uri uri = resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri == null) {
            return null;
        }
        return uri;
    }

    public static byte[] toBytes(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 65, baos);
        byte[] bytes = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static String getImageNameByTime() {
        Calendar calendar = Calendar.getInstance();
        return "IMG_"
                + String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH))
                + String.valueOf(calendar.get(Calendar.DATE)) + "_" + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))
                + String.valueOf(calendar.get(Calendar.MINUTE)) + String.valueOf(calendar.get(Calendar.SECOND)) + ".jpg";
    }

    /**
     * 从url获取该图片的文件名
     * 
     * @param url
     * @param postfix
     * @return
     */
    public static String getImageNameFromUrl(String url, String postfix) {
        return getImageNameFromUrl(url, postfix, null);
    }

    /**
     * 从url获取当前图片的文件名，如果url以ignoreTag开头则直接返回该url；如果ignoreTag为空，则不会判断ignoreTag
     * 
     * @param url
     * @param postfix
     * @param ignoreTag
     * @return
     */
    public static String getImageNameFromUrl(String url, String postfix, String ignoreTag) {
        if (Utils.isTextEmpty(url) || ((!Utils.isTextEmpty(ignoreTag)) && url.startsWith(ignoreTag)))
            return url;
        int lastIndex = url.lastIndexOf(postfix);
        if (lastIndex < 0)
            lastIndex = url.length() - 1;
        int beginIndex = url.lastIndexOf("/") + 1;
        int slashIndex = url.lastIndexOf("%2F") + 3;
        int finalSlashIndex = url.lastIndexOf("%252F") + 5;
        beginIndex = Math.max(Math.max(beginIndex, slashIndex), finalSlashIndex);

        if (beginIndex >= lastIndex) {
            return null;
        }
        return url.substring(beginIndex, lastIndex);
    }

    public static Bitmap copy(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled())
            return null;
        return bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
    }

    public static BitmapDrawable decodeWithOOMHandling(Resources res, String imagePath) {
        BitmapDrawable result = null;

        if (Utils.isTextEmpty(imagePath))
            return result;

        try {
            result = new BitmapDrawable(res, imagePath);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e.getMessage(), e);
            System.gc();
            // TODO: handle exception
            // Wait some time while GC is working
            SystemClock.sleep(1000);
            System.gc();
        }
        return result;
    }

    public static Bitmap createReflectedBitmap(Bitmap b, float reflectionFraction, int dropShadowRadius, int sideShadowColor) {
        if (0 == reflectionFraction && 0 == dropShadowRadius) {
            return b;
        }

        Bitmap result;
        int padding = dropShadowRadius;

        // Create the result bitmap, in which we'll print the
        // original bitmap and its reflection
        result = Bitmap.createBitmap(
                b.getWidth() + padding * 2, 2 * padding + (int) (b.getHeight() * (1 + reflectionFraction)), Config.ARGB_8888);

        // We'll work in a canvas
        Canvas canvas = new Canvas(result);

        // Add a drop shadow
        Paint dropShadow = new Paint();
        dropShadow.setShadowLayer(padding, 0, 0, 0xFF000000);
        canvas.drawRect(padding, padding, b.getWidth() + padding, result.getHeight() - padding, dropShadow);

        // draw the original image
        canvas.drawBitmap(b, padding, padding, null);

        // draw the reflection
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        canvas.setMatrix(matrix);
        int reflectionHeight = Math.round(reflectionFraction * b.getHeight());
        canvas.drawBitmap(b, new Rect(0, b.getHeight() - reflectionHeight, b.getWidth(), b.getHeight()), new Rect(
                padding, -reflectionHeight - padding - b.getHeight(), padding + b.getWidth(), -padding - b.getHeight()), null);
        canvas.setMatrix(new Matrix());

        // draw the gradient
        LinearGradient shader = new LinearGradient(0, b.getHeight(), 0, result.getHeight(), 0x40000000, sideShadowColor, TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DARKEN));
        canvas
                .drawRect(
                        padding, padding + b.getHeight(), padding + b.getWidth(), padding + b.getHeight() * (1 + reflectionFraction), paint);
        return result;
    }

    /**
     * 图片缩放
     * 
     * @param bitmap
     *            缩放的图片
     * @param newWidth
     *            缩放后的宽度
     * @param newHeight
     *            缩放后的高度
     * @return bitmap
     * @author ChenHuajiang
     * */
    public static Bitmap zoomBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null) {
            return null;
        }
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();

        float scaleWidth = ((float) newWidth) / bmpWidth;
        float scaleHeight = ((float) newHeight) / bmpHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
        return resizeBmp;
    }

    /**
     * 图片操作回调
     * 
     * @author wangzengyang 2013-4-11
     * 
     */
    public interface ImageListener {
        /**
         * 剪裁回调(UI线程中)
         * 
         * @param bitmap
         */
        void onRevise(Bitmap bitmap);
    }

    public static Drawable createDrawable(File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (is == null)
            return null;
        return BitmapDrawable.createFromStream(is, null);
    }

    public static Bitmap download(String urlString, ImageloadCallback callback) {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            inputStream = conn.getInputStream();

            return BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null)
                conn.disconnect();
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    public interface ImageloadCallback {
        public void onSuccess(Bitmap bitmap);
    }
//    public static Bitmap fetchBitmap(String url) {
//        return download(url, null);
//    }

//    /**
//     * 保存图片
//     *
//     * @param ad
//     */
//    public static void save(final String url, final String fileName, final Business business) {
//        new Job() {
//
//            @Override
//            protected void run() {
//                Bitmap bitmap = ImageUtil.fetchBitmap(url);
//                saveBitmap(bitmap, fileName, business);
//            }
//        }.start();
//    }
//
//    public static void saveBitmap(Bitmap bitmap, String filename) {
//        if (TextUtil.isEmpty(filename))
//            return;
//        File file = FileUtil.getImageFile(filename, business);
//        ImageUtil.writeToFile(bitmap, file.getAbsolutePath(), 65, false);
//    }
}
