//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by generalYan on 2019/10/21.
 */

public class ZipUtils {
    static byte[] bt1 = new byte[]{68, 79, 77, 84, 79, 80, 50, 48, 49, 50};
    static byte[] bt2 = new byte[]{80, 75, 3, 4, 20, 0, 8, 0, 8, 0};

    public ZipUtils() {
    }

    public static List<File> GetFileList(String zipFileString, boolean bContainFolder, boolean bContainFile) throws Exception {
        LogUtils.v("XZip", "GetFileList(String)");
        List<File> fileList = new ArrayList();
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        String szName = "";
        System.out.println("zipFileString=========" + zipFileString);

        ZipEntry zipEntry;
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            File folder;
            if (zipEntry.isDirectory()) {
                szName = szName.substring(0, szName.length() - 1);
                folder = new File(szName);
                if (bContainFolder) {
                    fileList.add(folder);
                }
            } else {
                folder = new File(szName);
                if (bContainFile) {
                    fileList.add(folder);
                }
            }
        }

        inZip.close();
        return fileList;
    }

    public static InputStream UpZip(String zipFileString, String fileString) throws Exception {
        Log.v("XZip", "UpZip(String, String)");
        ZipFile zipFile = new ZipFile(zipFileString);
        ZipEntry zipEntry = zipFile.getEntry(fileString);
        return zipFile.getInputStream(zipEntry);
    }

    public static List<File> UnZipFolder(String zipFileString, String outPathString) throws Exception {
        List<File> list = new ArrayList();
        File zipFile = new File(zipFileString);
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFile));
        String szName = "";

        while (true) {
            ZipEntry zipEntry;
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                File file;
                if (zipEntry.isDirectory()) {
                    szName = szName.substring(0, szName.length() - 1);
                    file = new File(outPathString + File.separator + szName);
                    file.mkdirs();
                } else {
                    file = new File(outPathString + File.separator + szName);
                    file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];

                    int len;
                    while ((len = inZip.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                        out.flush();
                    }

                    out.close();
                    out = null;
                    list.add(file);
                }
            }

            zipFile.delete();
            zipFile = null;
            inZip = null;
            return list;
        }
    }

    public static void ZipFolder(String srcFileString, String zipFileString) throws Exception {
        Log.v("XZip", "ZipFolder(String, String)");
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        File file = new File(srcFileString);
        ZipFiles(file.getParent() + File.separator, file.getName(), outZip);
        outZip.finish();
        outZip.close();
    }

    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        Log.v("XZip", "ZipFiles(String, String, ZipOutputStream)");
        if (zipOutputSteam != null) {
            File file = new File(folderString + fileString);
            int len;
            if (file.isFile()) {
                ZipEntry zipEntry = new ZipEntry(fileString);
                FileInputStream inputStream = new FileInputStream(file);
                zipOutputSteam.putNextEntry(zipEntry);
                byte[] buffer = new byte[4096];

                while ((len = inputStream.read(buffer)) != -1) {
                    zipOutputSteam.write(buffer, 0, len);
                }

                zipOutputSteam.closeEntry();
            } else {
                String[] fileList = file.list();
                if (fileList.length <= 0) {
                    ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                    zipOutputSteam.putNextEntry(zipEntry);
                    zipOutputSteam.closeEntry();
                }

                String[] var11 = fileList;
                len = fileList.length;

                for (int var12 = 0; var12 < len; ++var12) {
                    String aFileList = var11[var12];
                    ZipFiles(folderString, fileString + File.separator + aFileList, zipOutputSteam);
                }
            }

        }
    }

    public void finalize() throws Throwable {
    }

    public static boolean isExistZip(String folderString) {
        Log.v("XZip", "isExistZip(String, String)");
        Log.v("XZip", folderString);
        boolean flag = false;
        File[] files = (new File(folderString)).listFiles();
        if (files == null) {
            Log.v("XZip", "文件夹中无任何文件！");
            return false;
        } else {
            File[] var4 = files;
            int var5 = files.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                File file = var4[var6];
                if (!file.canRead()) {
                    Log.v("XZip", "文件夹中无任何文件！");
                }

                if (file.isFile() && file.getName().contains("zip")) {
                    Log.v("XZip", file.getPath());
                    flag = true;
                }
            }

            return flag;
        }
    }

    public static List<File> isExitUnZipFile(String zipUrl, List<String> databaseList) {
        Log.v("XZip", "isExitUnZipFile(String)");
        List<File> zipList = new ArrayList();
        File[] files = (new File(zipUrl)).listFiles();
        if (files == null) {
            Log.v("XZip", "文件夹中无任何文件！");
        }

        File[] var6 = files;
        int var7 = files.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            File file = var6[var8];
            File f = file;
            if (!file.canRead()) {
                Log.v("XZip", "文件夹中无任何文件！");
            }

            if (file.isFile() && file.getName().contains("zip")) {
                Log.v("XZip", file.getPath());
                File dbFile = null;

                try {
                    List<File> zipFiles = GetFileList(unLockZip(f.getPath()).getPath(), false, true);
                    Iterator var12 = zipFiles.iterator();

                    while (var12.hasNext()) {
                        File childFile = (File) var12.next();
                        if (childFile.getName().contains("db")) {
                            dbFile = childFile;
                        }
                    }

                    lockZip(f.getPath());
                } catch (Exception var14) {
                    var14.printStackTrace();
                }

                boolean isExit = false;
                if (databaseList != null && databaseList.size() > 0) {
                    for (int j = 0; j < databaseList.size(); ++j) {
                        if (dbFile.getName().equals(databaseList.get(j))) {
                            isExit = true;
                            break;
                        }

                        isExit = false;
                    }
                } else {
                    isExit = false;
                }

                if (!isExit) {
                    System.out.println("执行了");
                    zipList.add(file);
                }
            }
        }

        return zipList;
    }

    public static void lockZip(String url) {
        File zipFile = new File(url);

        try {
            RandomAccessFile raf = new RandomAccessFile(zipFile, "rw");
            raf.seek(0L);
            byte[] b = new byte[10];
            raf.read(b);
            if (!(new String(b)).equals(new String(bt1))) {
                raf.seek(0L);
                raf.write(bt1);
            }

            raf.close();
            raf = null;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static File unLockZip(String url) {
        Log.v("XZip", "解密压缩文件" + url);
        File zipFile = new File(url);

        try {
            RandomAccessFile raf = new RandomAccessFile(zipFile, "rw");
            raf.seek(0L);
            byte[] b = new byte[10];
            raf.read(b);
            if ((new String(b)).equals(new String(bt1))) {
                raf.seek(0L);
                raf.write(bt2);
            }

            raf.close();
            raf = null;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return zipFile;
    }

    public static String checkZipFileIsExist(File f, List<String> fileList) {
        Log.v("XZip", "isExitUnZipFile(String)");
        String retStr = "";
        String flag = "1";
        String tch = "*";
        if (f.isFile() && f.getName().contains("zip")) {
            Log.v("XZip", f.getPath());
            File dbFile = null;

            try {
                List<File> zipFiles = GetFileList(unLockZip(f.getPath()).getPath(), false, true);

                File childFile;
                for (Iterator var8 = zipFiles.iterator(); var8.hasNext(); childFile = null) {
                    childFile = (File) var8.next();
                    if (childFile.getName().contains("db")) {
                        dbFile = childFile;
                    } else if (childFile.getName().contains("txt")) {
                        tch = childFile.getName().substring(childFile.getName().indexOf("_") + 1, childFile.getName().lastIndexOf("_"));
                    }
                }

                zipFiles = null;
                lockZip(f.getPath());
            } catch (Exception var10) {
                var10.printStackTrace();
            }

            boolean isExit = false;
            retStr = dbFile.getName();
            if (fileList != null && fileList.size() > 0) {
                for (int j = 0; j < fileList.size(); ++j) {
                    if (dbFile.getName().equals(fileList.get(j))) {
                        isExit = true;
                        break;
                    }

                    isExit = false;
                }
            } else {
                isExit = false;
            }

            if (!isExit) {
                flag = "0";
            }

            dbFile = null;
        }

        return retStr + "," + flag + "," + tch;
    }
}

