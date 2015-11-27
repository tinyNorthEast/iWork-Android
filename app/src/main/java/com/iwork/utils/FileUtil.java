package com.iwork.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;


import com.iwork.Base.BaseApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * IO工具类
 *
 * @author Wood
 * @date 2013-04-17
 */
@SuppressLint("DefaultLocale")
public class FileUtil {
    // private static final String TAG = IOUtil.class.getSimpleName();

    private static ZipInputStream zipInputStream;


    public static String cutTheTailOfTheFile(String path) {
        File file = new File(path);
        if (file != null && file.exists() && file.isFile() && path.endsWith(".tmp")) {
            String str = path.substring(0, path.lastIndexOf(".tmp"));
            file.renameTo(new File(str));
            return str;
        } else {
            return path;
        }
    }

    /**
     * 创建文件
     *
     * @param fileName
     *            文件名(包含路径)
     * @param isOver
     *            是否覆盖存在的文件
     * @return boolean
     * */
    public static boolean create(String fileName, boolean isOver) {
        if (TextUtil.isEmpty(fileName)) {
            return false;
        }
        boolean b = false;
        File file = new File(fileName);
        try {
            if (isOver) {
                b = file.createNewFile();
            } else {
                if (file.exists()) {

                } else {
                    b = file.createNewFile();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    /** 判断文件是否存在 */
    public static boolean isExists(String fileName) {
        if (TextUtil.isEmpty(fileName)) {
            return false;
        }
        File file = new File(fileName);
        return file.exists();
    }

    private static boolean isNull(String s) {
        return TextUtil.isEmpty(s);
    }

    /**
     * 删除文件
     *
     * @param fileName
     *            文件名(包含路径)
     * */
    public static boolean delete(String fileName) {
        if (isNull(fileName)) {
            return false;
        }
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * 创建目录
     *
     * @param name
     *            目录名，可以是多级目录
     * */
    public static boolean mkDir(String name) {
        if (isNull(name)) {
            return false;
        }

        File file = new File(name);
        if (file.isDirectory()) {
            return true;
        }
        // mkdirs可以创建多级目录, mkdir只能创建一级目录
        return file.mkdirs();
    }

    /**
     * 将字符串写入文件
     *
     * @param fileName
     *            文件名(包含路径)
     * @param str
     *            字符串
     * */
    public static void writeString(String fileName, String str) {
        if (isNull(fileName) || isNull(str)) {
            return;
        }
        File file = new File(fileName);
        try {
            OutputStream outStream = new FileOutputStream(file);
            byte[] b = str.getBytes();
            outStream.write(b);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向现有文件追加字符
     *
     * @param fileName 文件名
     *            (包含路径)
     * @param str
     *            字符串
     * */
    public static void appString(String fileName, String str) {
        if (isNull(fileName) || isNull(str)) {
            return;
        }
        File file = new File(fileName);
        try {
            OutputStream outStream = new FileOutputStream(file, true);
            byte[] b = str.getBytes();
            outStream.write(b);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件内容
     *
     * @param fileName
     *            文件名(包含路径)
     * @return 返回字符串
     */
    public static String readFile(String fileName) {
        if (TextUtil.isEmpty(fileName)) {
            return null;
        }
        File file = new File(fileName);
        InputStream is;
        try {
            is = new FileInputStream(file);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            is.close();
            outStream.flush();
            outStream.close();
            return outStream.toString("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readFileToByte(String fileName) {
        if (TextUtil.isEmpty(fileName)) {
            return null;
        }
        File file = new File(fileName);
        InputStream is;
        try {
            is = new FileInputStream(file);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            is.close();
            outStream.flush();
            outStream.close();
            return outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取流文件内容，并将其转为字符串
     * */
    public static String readStream(InputStream inStream) {
        if (inStream == null) {
            return null;
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            inStream.close();
            outStream.flush();
            outStream.close();
            return outStream.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveFile(String fileName, InputStream is) {
        saveFile(fileName, is, false);
    }

    /**
     * 将数据流保存在指定文件里
     *
     * @param fileName
     *            文件名(包含路径)
     * @param is
     *            数据流
     * */
    public static void saveFile(String fileName, InputStream is, boolean append) {
        if (TextUtil.isEmpty(fileName) || is == null) {
            return;
        }
        try {
            OutputStream outStream = new FileOutputStream(fileName, append);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            is.close();
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(String fileName, String content, boolean append) {
        if (TextUtil.isEmpty(content)) {
            return;
        }
        saveFile(fileName, new ByteArrayInputStream(content.getBytes()), append);
    }

    /**
     * 合并文件
     *
     * @param file1
     *            需要合并的文件
     * @param file2
     *            需要合并的文件
     * @param file3
     *            很并后的文件
     * */
    public static void sequenceFile(String file1, String file2, String file3) {
        if (TextUtil.isEmpty(file1) || TextUtil.isEmpty(file2) || TextUtil.isEmpty(file2)) {
            return;
        }
        File f1 = new File(file1);
        File f2 = new File(file2);
        File f3 = new File(file3);

        try {
            InputStream in1 = new FileInputStream(f1);
            InputStream in2 = new FileInputStream(f2);
            OutputStream outStream = new FileOutputStream(f3);
            SequenceInputStream sis = new SequenceInputStream(in1, in2);
            int temp = 0;
            while ((temp = sis.read()) != -1) {
                outStream.write(temp);
            }
            in1.close();
            in2.close();
            outStream.close();
            sis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩多个文件
     *
     * @param srcfile s
     *            需要压缩的文件
     * @param zipFile
     *            压缩后的文件
     * */
    public static String zipFile(File[] srcfile, String zipFile) {
        if (srcfile == null || isNull(zipFile)) {
            return null;
        }

        byte[] buf = new byte[1024];
        try {
            File file = new File(zipFile);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipFile;
    }

    /**
     * 压缩单个文件
     *
     * @param fileName
     *            需要压缩的文件
     * @param zipFile
     *            压缩后的文件
     * */
    public static String zipFile(String fileName, String zipFile) {
        if (isNull(fileName) || isNull(zipFile)) {
            return null;
        }
        File file = new File(fileName);
        File zf = new File(zipFile);
        try {
            InputStream inputStream = new FileInputStream(file);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zf));
            out.putNextEntry(new ZipEntry(file.getName()));
            out.setComment("Zip File");
            int tmp = 0;
            while ((tmp = inputStream.read()) != -1) {
                out.write(tmp);
            }
            inputStream.close();
            out.finish();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zipFile;
    }

    /**
     * 解压缩文件
     *
     * @param zipFile
     *            压缩文件
     * @param fileName
     *            解压后的文件
     * */
    public static void unZipFile(String zipFile, String fileName) {
        if (isNull(fileName) || isNull(zipFile)) {
            return;
        }
        File file = new File(zipFile);
        File outFile = new File(fileName);

        try {
            ZipFile zpFile = new ZipFile(file);
            zipInputStream = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = zipInputStream.getNextEntry();
            InputStream input = zpFile.getInputStream(entry);
            OutputStream output = new FileOutputStream(outFile);
            int temp = 0;
            while ((temp = input.read()) != -1) {
                output.write(temp);
            }
            input.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 返回文件大小单位KB */
    public static long getLogFileSize(String fileName) {
        if (TextUtil.isEmpty(fileName)) {
            return 0;
        }

        File file = new File(fileName);
        if (file.isFile()) {
            return file.length() / 1024;
        } else {
            return 0;
        }
    }

    /** 返回文件大小单位Byte */
    public static long getFileSize(String fileName) {
        if (TextUtil.isEmpty(fileName)) {
            return 0;
        }

        File file = new File(fileName);
        try {
            return new FileInputStream(file).available();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取单个文件的MD5值
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }

        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 输入输出流拷贝
     *
     * @param is
     * @param os
     */
    public static void copy(InputStream is, OutputStream os) throws Exception {
        if (is == null || os == null)
            return;
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        byte[] buf = new byte[getAvailableSize(bis)];
        int i = 0;
        while ((i = bis.read(buf)) != -1) {
            bos.write(buf, 0, i);
        }
        bis.close();
        bos.close();
    }

    private static int getAvailableSize(InputStream is) throws IOException {
        if (is == null)
            return 0;
        int available = is.available();
        return available <= 0 ? 1024 : available;
    }

    /**
     * 保存数据到uri指定位置
     *
     * @param uri
     * @param data
     */
    public static void save(Uri uri, byte[] data) {
        String path = uri.getPath();
        try {
            BufferedOutputStream baos = new BufferedOutputStream(new FileOutputStream(path));
            baos.write(data);
            baos.flush();
            baos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeInputStream(InputStream is) {
        if (is == null)
            return;
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeOutStream(OutputStream os) {
        if (os == null)
            return;
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将二进制转换为音频文件
     *
     * @param b
     *            待转换的二进制
     * @param fileName
     *            转换后的文件名
     * @return fileName 成功后返回文件名，失败返回null
     * */
    public static String byte2AudioFile(byte[] b, String fileName) {
        FileOutputStream outStream;
        try {
            outStream = BaseApplication.getAppContext().openFileOutput(fileName + ".mp3", Context.MODE_PRIVATE);
            outStream.write(b);
            outStream.flush();
            outStream.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取输入流
     *
     * @param uri
     * @return
     */
    public static InputStream getInputStream(Uri uri) {
        try {
            if (uri.getScheme().equals("file")) {
                return new FileInputStream(uri.getPath());
            } else {
                return BaseApplication.getAppContext().getContentResolver().openInputStream(uri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * 根据Uri返回文件路径
     *
     * @param uri
     * @return
     */
    public static String getFilePath(Uri uri) {
        String path = "";
        if (uri.getScheme().equals("file")) {
            path = uri.getPath();
        } else {
            Cursor cursor = BaseApplication.getAppContext().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            path = cursor.getString(1);
            FileUtil.closeCursor(cursor);
        }
        return path;
    }

    /**
     * 关闭游标cursor
     *
     * @param cursor
     */
    public static void closeCursor(Cursor cursor) {
        try {
            /* 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15) */
            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * delete file the file can be a directory
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file == null) {
            return;
        }
        if (file.exists()) {
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                if (files == null)
                    return;
                for (File f : files) {
                    f.delete();
                }
            }
            // file.delete();
        }
    }

    public static void deleteDir(File dir) {
        deleteFile(dir);
    }

    /**
     * 获取指定扩展名的文件
     *
     * @param path
     *            路径
     * @param suffix
     *            文件扩展名
     * */
    public static File[] getFiles4Filter(final String path, final String suffix) {
        if (TextUtil.isEmpty(path) || TextUtil.isEmpty(suffix)) {
            return null;
        }
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(suffix);
            }
        };

        return new File(path).listFiles(filter);
    }




    public static void save(File file, Object obj) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeOutStream(oos);
        }
    }

    public static void deleteDir(String dir) {
        delete(dir);
    }

    public static boolean delete(Context context, String filename) {
        String path = context.getFilesDir() + File.separator + filename;
        File file = new File(path);
        return !file.exists() || file.delete();
    }

    /** 按行读取，以数组形式返回 */
    public static String[] getUrlFromTxt(String filePath) {
        File file = null;
        BufferedReader br = null;
        String strXmlLine = "";
        String strXmlInfo = "";
        String[] str = null;
        try {
            file = new File(filePath);
            br = new BufferedReader(new FileReader(file));
            while ((strXmlLine = br.readLine()) != null) {
                strXmlInfo += strXmlLine + ",";
            }
            str = strXmlInfo.split(",");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    // uri返回的是file:///...，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other
     * file-based ContentProviders.
     *
     * @param oldPath
     *            String 原文件路径 如：c:/fqf.txt
     * @param newPath
     *            String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other
     * file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
