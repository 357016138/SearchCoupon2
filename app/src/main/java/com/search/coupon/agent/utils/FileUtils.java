package com.search.coupon.agent.utils;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件操作工具类
 * Created by fan on 2017/11/9.
 */
public class FileUtils {

    public static String SDCardPathRoot = Environment.getExternalStorageDirectory() + File.separator;

    public static String ROOT = SDCardPathRoot + "wechatsearch" + File.separator;
    public static String TEMP = ROOT + "temp" + File.separator;
    public static String CAMERA = TEMP + "camera" + File.separator;
    public static String CACHE = ROOT + "cache" + File.separator;
    public static String APK_PATH = ROOT + "apk" + File.separator;

    public static String saveJPGFile(byte[] data) {
        if (data == null)
            return null;

        File mediaStorageDir = new File(FileUtils.TEMP);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            String jpgFileName = System.currentTimeMillis() + ".jpg";
            fos = new FileOutputStream(FileUtils.TEMP + jpgFileName);
            bos = new BufferedOutputStream(fos);
            bos.write(data);
            return FileUtils.TEMP + jpgFileName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 保存下载文件
     *
     * @param filePath
     * @param data
     * @return
     */
    public static String saveDonwloadFile(String filePath, byte[] data) {
        if (data == null)
            return null;

        File mediaStorageDir = new File(FileUtils.CACHE);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(FileUtils.CACHE + filePath);
            bos = new BufferedOutputStream(fos);
            bos.write(data);
            return FileUtils.CACHE + filePath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 删除sd卡某个目录下的所有文件
     * @param root
     */
    public static void deleteAllFiles(File root) {
        if (root.isDirectory()) {
            File files[] = root.listFiles();
            if (files != null) {
                for (File f : files) {
                    // 判断是否为文件夹
                    if (f.isDirectory()) {
                        deleteAllFiles(f);
                    } else {
                        // 判断文件是否存在
                        if (f.exists()) {
                            try {
                                f.delete();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            root.delete();
        } else if (root.exists()) {
            root.delete();
        }
    }
}
