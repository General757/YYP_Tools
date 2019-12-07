//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.yyp.tools.Charsets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Yan on 2019/10/11.
 */
public class FileUtils {

    public static final double KB = 1024.0D;
    public static final double MB = 1048576.0D;
    public static final double GB = 1.073741824E9D;
    public static final long ONE_KB = 1024L;
    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024L);
    public static final long ONE_MB = 1048576L;
    public static final BigInteger ONE_MB_BI;
    private static final long FILE_COPY_BUFFER_SIZE = 31457280L;
    public static final long ONE_GB = 1073741824L;
    public static final BigInteger ONE_GB_BI;
    public static final long ONE_TB = 1099511627776L;
    public static final BigInteger ONE_TB_BI;
    public static final long ONE_PB = 1125899906842624L;
    public static final BigInteger ONE_PB_BI;
    public static final long ONE_EB = 1152921504606846976L;
    public static final BigInteger ONE_EB_BI;
    public static final BigInteger ONE_ZB;
    public static final BigInteger ONE_YB;
    public static final File[] EMPTY_FILE_ARRAY;
    private static final Charset UTF8;
    public static final int BUFFER_SIZE = 8192;
    public static final int ICON_TYPE_ROOT = 1;
    public static final int ICON_TYPE_FOLDER = 2;
    public static final int ICON_TYPE_MP3 = 3;
    public static final int ICON_TYPE_MTV = 4;
    public static final int ICON_TYPE_JPG = 5;
    public static final int ICON_TYPE_FILE = 6;
    public static final String MTV_REG = "^.*\\.(mp4|3gp)$";
    public static final String MP3_REG = "^.*\\.(mp3|wav)$";
    public static final String JPG_REG = "^.*\\.(gif|jpg|png)$";
    private static final String FILENAME_REGIX = "^[^\\/?\"*:<>\\\u0005]{1,255}$";
    public static final String FILE_EXTENSION_SEPARATOR = ".";

    private FileUtils() {
        throw new Error("Do not need instantiate!");
    }


    public static File getFile(File directory, String... names) {
        if (directory == null) {
            throw new NullPointerException("directorydirectory must not be null");
        } else if (names == null) {
            throw new NullPointerException("names must not be null");
        } else {
            File var2 = directory;
            String[] var3 = names;
            int var4 = names.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String var6 = var3[var5];
                var2 = new File(var2, var6);
            }

            return var2;
        }
    }

    public static File getFile(String... names) {
        if (names == null) {
            throw new NullPointerException("names must not be null");
        } else {
            File var1 = null;
            String[] var2 = names;
            int var3 = names.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String var5 = var2[var4];
                if (var1 == null) {
                    var1 = new File(var5);
                } else {
                    var1 = new File(var1, var5);
                }
            }

            return var1;
        }
    }

    public static boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        } else {
            File var1 = new File(path);
            return var1.exists();
        }
    }

    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }

    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            } else if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            } else {
                return new FileInputStream(file);
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }

            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File var2 = file.getParentFile();
            if (var2 != null && !var2.mkdirs() && !var2.isDirectory()) {
                throw new IOException("Directory '" + var2 + "' could not be created");
            }
        }

        return new FileOutputStream(file, append);
    }

    public static String byteCountToDisplaySize(BigInteger size) {
        String var1;
        if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
            var1 = size.divide(ONE_EB_BI) + " EB";
        } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
            var1 = size.divide(ONE_PB_BI) + " PB";
        } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
            var1 = size.divide(ONE_TB_BI) + " TB";
        } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            var1 = size.divide(ONE_GB_BI) + " GB";
        } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
            var1 = size.divide(ONE_MB_BI) + " MB";
        } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
            var1 = size.divide(ONE_KB_BI) + " KB";
        } else {
            var1 = size + " bytes";
        }

        return var1;
    }

    public static String byteCountToDisplaySize(long size) {
        return byteCountToDisplaySize(BigInteger.valueOf(size));
    }

    public static void touch(File file) throws IOException {
        if (!file.exists()) {
            FileOutputStream var1 = openOutputStream(file);
            IOUtils.closeQuietly(var1);
        }

        boolean var2 = file.setLastModified(System.currentTimeMillis());
        if (!var2) {
            throw new IOException("Unable to set the last modification time for " + file);
        }
    }

    public static File[] convertFileCollectionToFileArray(Collection<File> files) {
        return (File[]) files.toArray(new File[files.size()]);
    }

    private static String[] toSuffixes(String[] extensions) {
        String[] var1 = new String[extensions.length];

        for (int var2 = 0; var2 < extensions.length; ++var2) {
            var1[var2] = "." + extensions[var2];
        }

        return var1;
    }

    public static boolean contentEquals(File file1, File file2) throws IOException {
        boolean var2 = file1.exists();
        if (var2 != file2.exists()) {
            return false;
        } else if (!var2) {
            return true;
        } else if (!file1.isDirectory() && !file2.isDirectory()) {
            if (file1.length() != file2.length()) {
                return false;
            } else if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
                return true;
            } else {
                FileInputStream var3 = null;
                FileInputStream var4 = null;

                boolean var5;
                try {
                    var3 = new FileInputStream(file1);
                    var4 = new FileInputStream(file2);
                    var5 = IOUtils.contentEquals(var3, var4);
                } finally {
                    IOUtils.closeQuietly(var3);
                    IOUtils.closeQuietly(var4);
                }

                return var5;
            }
        } else {
            throw new IOException("Can't compare directories, only files");
        }
    }

    public static boolean contentEqualsIgnoreEOL(File file1, File file2, String charsetName) throws IOException {
        boolean var3 = file1.exists();
        if (var3 != file2.exists()) {
            return false;
        } else if (!var3) {
            return true;
        } else if (!file1.isDirectory() && !file2.isDirectory()) {
            if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
                return true;
            } else {
                InputStreamReader var4 = null;
                InputStreamReader var5 = null;

                boolean var6;
                try {
                    if (charsetName == null) {
                        var4 = new InputStreamReader(new FileInputStream(file1));
                        var5 = new InputStreamReader(new FileInputStream(file2));
                    } else {
                        var4 = new InputStreamReader(new FileInputStream(file1), charsetName);
                        var5 = new InputStreamReader(new FileInputStream(file2), charsetName);
                    }

                    var6 = IOUtils.contentEqualsIgnoreEOL(var4, var5);
                } finally {
                    IOUtils.closeQuietly(var4);
                    IOUtils.closeQuietly(var5);
                }

                return var6;
            }
        } else {
            throw new IOException("Can't compare directories, only files");
        }
    }

    public static File toFile(URL url) {
        if (url != null && "file".equalsIgnoreCase(url.getProtocol())) {
            String var1 = url.getFile().replace('/', File.separatorChar);
            var1 = decodeUrl(var1);
            return new File(var1);
        } else {
            return null;
        }
    }

    static String decodeUrl(String url) {
        String var1 = url;
        if (url != null && url.indexOf(37) >= 0) {
            int var2 = url.length();
            StringBuffer var3 = new StringBuffer();
            ByteBuffer var4 = ByteBuffer.allocate(var2);
            int var5 = 0;

            while (true) {
                while (true) {
                    if (var5 >= var2) {
                        var1 = var3.toString();
                        return var1;
                    }

                    if (url.charAt(var5) != '%') {
                        break;
                    }

                    try {
                        while (true) {
                            byte var6 = (byte) Integer.parseInt(url.substring(var5 + 1, var5 + 3), 16);
                            var4.put(var6);
                            var5 += 3;
                            if (var5 >= var2 || url.charAt(var5) != '%') {
                                break;
                            }
                        }
                    } catch (RuntimeException var10) {
                        break;
                    } finally {
                        if (var4.position() > 0) {
                            var4.flip();
                            var3.append(UTF8.decode(var4).toString());
                            var4.clear();
                        }

                    }
                }

                var3.append(url.charAt(var5++));
            }
        } else {
            return var1;
        }
    }

    public static File[] toFiles(URL[] urls) {
        if (urls != null && urls.length != 0) {
            File[] var1 = new File[urls.length];

            for (int var2 = 0; var2 < urls.length; ++var2) {
                URL var3 = urls[var2];
                if (var3 != null) {
                    if (!var3.getProtocol().equals("file")) {
                        throw new IllegalArgumentException("URL could not be converted to a File: " + var3);
                    }

                    var1[var2] = toFile(var3);
                }
            }

            return var1;
        } else {
            return EMPTY_FILE_ARRAY;
        }
    }

    public static URL[] toURLs(File[] files) throws IOException {
        URL[] var1 = new URL[files.length];

        for (int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = files[var2].toURI().toURL();
        }

        return var1;
    }

    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }

    public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (destDir.exists() && !destDir.isDirectory()) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        } else {
            File var3 = new File(destDir, srcFile.getName());
            copyFile(srcFile, var3, preserveFileDate);
        }
    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
        copyFile(srcFile, destFile, true);
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        } else if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        } else if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        } else {
            File var3 = destFile.getParentFile();
            if (var3 != null && !var3.mkdirs() && !var3.isDirectory()) {
                throw new IOException("Destination '" + var3 + "' directory cannot be created");
            } else if (destFile.exists() && !destFile.canWrite()) {
                throw new IOException("Destination '" + destFile + "' exists but is read-only");
            } else {
                doCopyFile(srcFile, destFile, preserveFileDate);
            }
        }
    }

    public static long copyFile(File input, OutputStream output) throws IOException {
        FileInputStream var2 = new FileInputStream(input);

        long var3;
        try {
            var3 = IOUtils.copyLarge(var2, output);
        } finally {
            var2.close();
        }

        return var3;
    }

    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        } else {
            FileInputStream var3 = null;
            FileOutputStream var4 = null;
            FileChannel var5 = null;
            FileChannel var6 = null;

            try {
                var3 = new FileInputStream(srcFile);
                var4 = new FileOutputStream(destFile);
                var5 = var3.getChannel();
                var6 = var4.getChannel();
                long var7 = var5.size();
                long var9 = 0L;

                for (long var11 = 0L; var9 < var7; var9 += var6.transferFrom(var5, var9, var11)) {
                    var11 = var7 - var9 > 31457280L ? 31457280L : var7 - var9;
                }
            } finally {
                IOUtils.closeQuietly(var6);
                IOUtils.closeQuietly(var4);
                IOUtils.closeQuietly(var5);
                IOUtils.closeQuietly(var3);
            }

            if (srcFile.length() != destFile.length()) {
                throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
            } else {
                if (preserveFileDate) {
                    destFile.setLastModified(srcFile.lastModified());
                }

            }
        }
    }

    public static void copyDirectoryToDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        } else if (srcDir.exists() && !srcDir.isDirectory()) {
            throw new IllegalArgumentException("Source '" + destDir + "' is not a directory");
        } else if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (destDir.exists() && !destDir.isDirectory()) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        } else {
            copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
        }
    }

    public static void copyDirectory(File srcDir, File destDir) throws IOException {
        copyDirectory(srcDir, destDir, true);
    }

    public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, (FileFilter) null, preserveFileDate);
    }

    public static void copyDirectory(File srcDir, File destDir, FileFilter filter) throws IOException {
        copyDirectory(srcDir, destDir, filter, true);
    }

    public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!srcDir.exists()) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
        } else if (!srcDir.isDirectory()) {
            throw new IOException("Source '" + srcDir + "' exists but is not a directory");
        } else if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
        } else {
            ArrayList var4 = null;
            if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
                File[] var5 = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
                if (var5 != null && var5.length > 0) {
                    var4 = new ArrayList(var5.length);
                    File[] var6 = var5;
                    int var7 = var5.length;

                    for (int var8 = 0; var8 < var7; ++var8) {
                        File var9 = var6[var8];
                        File var10 = new File(destDir, var9.getName());
                        var4.add(var10.getCanonicalPath());
                    }
                }
            }

            doCopyDirectory(srcDir, destDir, filter, preserveFileDate, var4);
        }
    }

    private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate, List<String> exclusionList) throws IOException {
        File[] var5 = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
        if (var5 == null) {
            throw new IOException("Failed to list contents of " + srcDir);
        } else {
            if (destDir.exists()) {
                if (!destDir.isDirectory()) {
                    throw new IOException("Destination '" + destDir + "' exists but is not a directory");
                }
            } else if (!destDir.mkdirs() && !destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' directory cannot be created");
            }

            if (!destDir.canWrite()) {
                throw new IOException("Destination '" + destDir + "' cannot be written to");
            } else {
                File[] var6 = var5;
                int var7 = var5.length;

                for (int var8 = 0; var8 < var7; ++var8) {
                    File var9 = var6[var8];
                    File var10 = new File(destDir, var9.getName());
                    if (exclusionList == null || !exclusionList.contains(var9.getCanonicalPath())) {
                        if (var9.isDirectory()) {
                            doCopyDirectory(var9, var10, filter, preserveFileDate, exclusionList);
                        } else {
                            doCopyFile(var9, var10, preserveFileDate);
                        }
                    }
                }

                if (preserveFileDate) {
                    destDir.setLastModified(srcDir.lastModified());
                }

            }
        }
    }

    public static void copyURLToFile(URL source, File destination) throws IOException {
        InputStream var2 = source.openStream();
        copyInputStreamToFile(var2, destination);
    }

    public static void copyURLToFile(URL source, File destination, int connectionTimeout, int readTimeout) throws IOException {
        URLConnection var4 = source.openConnection();
        var4.setConnectTimeout(connectionTimeout);
        var4.setReadTimeout(readTimeout);
        InputStream var5 = var4.getInputStream();
        copyInputStreamToFile(var5, destination);
    }

    public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        try {
            FileOutputStream var2 = openOutputStream(destination);

            try {
                IOUtils.copy(source, var2);
                var2.close();
            } finally {
                IOUtils.closeQuietly(var2);
            }
        } finally {
            IOUtils.closeQuietly(source);
        }

    }

    public static boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        } else {
            try {
                if (file.isDirectory()) {
                    cleanDirectory(file);
                }
            } catch (Exception var3) {
                ;
            }

            try {
                return file.delete();
            } catch (Exception var2) {
                return false;
            }
        }
    }

    public static File getApplicationSdcardPath(Context context) {
        File var1 = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        if (var1 == null) {
            var1 = context.getFilesDir();
        }

        return var1;
    }

    public static void cleanDirectory(File directory) throws IOException {
        String var9;
        if (!directory.exists()) {
            var9 = directory + " does not exist";
            throw new IllegalArgumentException(var9);
        } else if (!directory.isDirectory()) {
            var9 = directory + " is not a directory";
            throw new IllegalArgumentException(var9);
        } else {
            File[] var1 = directory.listFiles();
            if (var1 == null) {
                throw new IOException("Failed to list contents of " + directory);
            } else {
                IOException var2 = null;
                File[] var3 = var1;
                int var4 = var1.length;

                for (int var5 = 0; var5 < var4; ++var5) {
                    File var6 = var3[var5];

                    try {
                        forceDelete(var6);
                    } catch (IOException var8) {
                        var2 = var8;
                    }
                }

                if (null != var2) {
                    throw var2;
                }
            }
        }
    }

    public static boolean waitFor(File file, int seconds) {
        int var2 = 0;
        int var3 = 0;

        while (!file.exists()) {
            if (var3++ >= 10) {
                var3 = 0;
                if (var2++ > seconds) {
                    return false;
                }
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException var5) {
                ;
            } catch (Exception var6) {
                break;
            }
        }

        return true;
    }

    public static String readFileToString(File file, Charset encoding) throws IOException {
        FileInputStream var2 = null;

        String var3;
        try {
            var2 = openInputStream(file);
            var3 = IOUtils.toString(var2, Charsets.toCharset(encoding));
        } finally {
            IOUtils.closeQuietly(var2);
        }

        return var3;
    }

    public static String readFileToString(File file, String encoding) throws IOException {
        return readFileToString(file, Charsets.toCharset(encoding));
    }

    public static String readFileToString(File file) throws IOException {
        return readFileToString(file, Charset.defaultCharset());
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        FileInputStream var1 = null;

        byte[] var2;
        try {
            var1 = openInputStream(file);
            var2 = IOUtils.toByteArray(var1, file.length());
        } finally {
            IOUtils.closeQuietly(var1);
        }

        return var2;
    }

    public static List<String> readLines(File file, Charset encoding) throws IOException {
        FileInputStream var2 = null;

        List var3;
        try {
            var2 = openInputStream(file);
            var3 = IOUtils.readLines(var2, Charsets.toCharset(encoding));
        } finally {
            IOUtils.closeQuietly(var2);
        }

        return var3;
    }

    public static List<String> readLines(File file, String encoding) throws IOException {
        return readLines(file, Charsets.toCharset(encoding));
    }

    public static List<String> readLines(File file) throws IOException {
        return readLines(file, Charset.defaultCharset());
    }

    public static void writeStringToFile(File file, String data, Charset encoding) throws IOException {
        writeStringToFile(file, data, encoding, false);
    }

    public static void writeStringToFile(File file, String data, String encoding) throws IOException {
        writeStringToFile(file, data, encoding, false);
    }

    public static void writeStringToFile(File file, String data, Charset encoding, boolean append) throws IOException {
        FileOutputStream var4 = null;

        try {
            var4 = openOutputStream(file, append);
            IOUtils.write(data, var4, encoding);
            var4.close();
        } finally {
            IOUtils.closeQuietly(var4);
        }

    }

    public static void writeStringToFile(File file, String data, String encoding, boolean append) throws IOException {
        writeStringToFile(file, data, Charsets.toCharset(encoding), append);
    }

    public static void writeStringToFile(File file, String data) throws IOException {
        writeStringToFile(file, data, Charset.defaultCharset(), false);
    }

    public static void writeStringToFile(File file, String data, boolean append) throws IOException {
        writeStringToFile(file, data, Charset.defaultCharset(), append);
    }

    public static void write(File file, CharSequence data) throws IOException {
        write(file, data, Charset.defaultCharset(), false);
    }

    public static void write(File file, CharSequence data, boolean append) throws IOException {
        write(file, data, Charset.defaultCharset(), append);
    }

    public static void write(File file, CharSequence data, Charset encoding) throws IOException {
        write(file, data, encoding, false);
    }

    public static void write(File file, CharSequence data, String encoding) throws IOException {
        write(file, data, encoding, false);
    }

    public static void write(File file, CharSequence data, Charset encoding, boolean append) throws IOException {
        String var4 = data == null ? null : data.toString();
        writeStringToFile(file, var4, encoding, append);
    }

    public static void write(File file, CharSequence data, String encoding, boolean append) throws IOException {
        write(file, data, Charsets.toCharset(encoding), append);
    }

    public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
        writeByteArrayToFile(file, data, false);
    }

    public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
        FileOutputStream var3 = null;

        try {
            var3 = openOutputStream(file, append);
            var3.write(data);
            var3.close();
        } finally {
            IOUtils.closeQuietly(var3);
        }

    }

    public static void writeLines(File file, String encoding, Collection<?> lines) throws IOException {
        writeLines(file, encoding, lines, (String) null, false);
    }

    public static void writeLines(File file, String encoding, Collection<?> lines, boolean append) throws IOException {
        writeLines(file, encoding, lines, (String) null, append);
    }

    public static void writeLines(File file, Collection<?> lines) throws IOException {
        writeLines(file, (String) null, lines, (String) null, false);
    }

    public static void writeLines(File file, Collection<?> lines, boolean append) throws IOException {
        writeLines(file, (String) null, lines, (String) null, append);
    }

    public static void writeLines(File file, String encoding, Collection<?> lines, String lineEnding) throws IOException {
        writeLines(file, encoding, lines, lineEnding, false);
    }

    public static void writeLines(File file, String encoding, Collection<?> lines, String lineEnding, boolean append) throws IOException {
        FileOutputStream var5 = null;

        try {
            var5 = openOutputStream(file, append);
            BufferedOutputStream var6 = new BufferedOutputStream(var5);
            IOUtils.writeLines(lines, lineEnding, var6, encoding);
            var6.flush();
            var5.close();
        } finally {
            IOUtils.closeQuietly(var5);
        }

    }

    public static void writeLines(File file, Collection<?> lines, String lineEnding) throws IOException {
        writeLines(file, (String) null, lines, lineEnding, false);
    }

    public static void writeLines(File file, Collection<?> lines, String lineEnding, boolean append) throws IOException {
        writeLines(file, (String) null, lines, lineEnding, append);
    }

    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean var1 = file.exists();
            if (!file.delete()) {
                if (!var1) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }

                String var2 = "Unable to delete file: " + file;
                throw new IOException(var2);
            }
        }

    }

    public static void forceDeleteOnExit(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }

    }

    private static void deleteDirectoryOnExit(File directory) throws IOException {
        if (directory.exists()) {
            directory.deleteOnExit();
            if (!isSymlink(directory)) {
                cleanDirectoryOnExit(directory);
            }

        }
    }

    private static void cleanDirectoryOnExit(File directory) throws IOException {
        String var9;
        if (!directory.exists()) {
            var9 = directory + " does not exist";
            throw new IllegalArgumentException(var9);
        } else if (!directory.isDirectory()) {
            var9 = directory + " is not a directory";
            throw new IllegalArgumentException(var9);
        } else {
            File[] var1 = directory.listFiles();
            if (var1 == null) {
                throw new IOException("Failed to list contents of " + directory);
            } else {
                IOException var2 = null;
                File[] var3 = var1;
                int var4 = var1.length;

                for (int var5 = 0; var5 < var4; ++var5) {
                    File var6 = var3[var5];

                    try {
                        forceDeleteOnExit(var6);
                    } catch (IOException var8) {
                        var2 = var8;
                    }
                }

                if (null != var2) {
                    throw var2;
                }
            }
        }
    }

    public static void forceMkdir(File directory) throws IOException {
        String var1;
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                var1 = "File " + directory + " exists and is " + "not a directory. Unable to create directory.";
                throw new IOException(var1);
            }
        } else if (!directory.mkdirs() && !directory.isDirectory()) {
            var1 = "Unable to create directory " + directory;
            throw new IOException(var1);
        }

    }

    public static boolean mkdirs(File directory) {
        try {
            forceMkdir(directory);
            return true;
        } catch (IOException var2) {
            return false;
        }
    }

    public static long sizeOf(File file) {
        if (!file.exists()) {
            String var1 = file + " does not exist";
            throw new IllegalArgumentException(var1);
        } else {
            return file.isDirectory() ? sizeOfDirectory(file) : file.length();
        }
    }

    public static BigInteger sizeOfAsBigInteger(File file) {
        if (!file.exists()) {
            String var1 = file + " does not exist";
            throw new IllegalArgumentException(var1);
        } else {
            return file.isDirectory() ? sizeOfDirectoryAsBigInteger(file) : BigInteger.valueOf(file.length());
        }
    }

    public static long sizeOfDirectory(File directory) {
        checkDirectory(directory);
        File[] var1 = directory.listFiles();
        if (var1 == null) {
            return 0L;
        } else {
            long var2 = 0L;
            File[] var4 = var1;
            int var5 = var1.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                File var7 = var4[var6];

                try {
                    if (!isSymlink(var7)) {
                        var2 += sizeOf(var7);
                        if (var2 < 0L) {
                            break;
                        }
                    }
                } catch (IOException var9) {
                    ;
                }
            }

            return var2;
        }
    }

    public static BigInteger sizeOfDirectoryAsBigInteger(File directory) {
        checkDirectory(directory);
        File[] var1 = directory.listFiles();
        if (var1 == null) {
            return BigInteger.ZERO;
        } else {
            BigInteger var2 = BigInteger.ZERO;
            File[] var3 = var1;
            int var4 = var1.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                File var6 = var3[var5];

                try {
                    if (!isSymlink(var6)) {
                        var2 = var2.add(BigInteger.valueOf(sizeOf(var6)));
                    }
                } catch (IOException var8) {
                    ;
                }
            }

            return var2;
        }
    }

    private static void checkDirectory(File directory) {
        if (!directory.exists()) {
            throw new IllegalArgumentException(directory + " does not exist");
        } else if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " is not a directory");
        }
    }

    public static boolean isFileNewer(File file, File reference) {
        if (reference == null) {
            throw new IllegalArgumentException("No specified reference file");
        } else if (!reference.exists()) {
            throw new IllegalArgumentException("The reference file '" + reference + "' doesn't exist");
        } else {
            return isFileNewer(file, reference.lastModified());
        }
    }

    public static boolean isFileNewer(File file, Date date) {
        if (date == null) {
            throw new IllegalArgumentException("No specified date");
        } else {
            return isFileNewer(file, date.getTime());
        }
    }

    public static boolean isFileNewer(File file, long timeMillis) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        } else if (!file.exists()) {
            return false;
        } else {
            return file.lastModified() > timeMillis;
        }
    }

    public static boolean isFileOlder(File file, File reference) {
        if (reference == null) {
            throw new IllegalArgumentException("No specified reference file");
        } else if (!reference.exists()) {
            throw new IllegalArgumentException("The reference file '" + reference + "' doesn't exist");
        } else {
            return isFileOlder(file, reference.lastModified());
        }
    }

    public static boolean isFileOlder(File file, Date date) {
        if (date == null) {
            throw new IllegalArgumentException("No specified date");
        } else {
            return isFileOlder(file, date.getTime());
        }
    }

    public static boolean isFileOlder(File file, long timeMillis) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        } else if (!file.exists()) {
            return false;
        } else {
            return file.lastModified() < timeMillis;
        }
    }

    public static void moveDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!srcDir.exists()) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
        } else if (!srcDir.isDirectory()) {
            throw new IOException("Source '" + srcDir + "' is not a directory");
        } else if (destDir.exists()) {
            throw new FileExistsException("Destination '" + destDir + "' already exists");
        } else {
            boolean var2 = srcDir.renameTo(destDir);
            if (!var2) {
                if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
                    throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
                }

                copyDirectory(srcDir, destDir);
                deleteDirectory(srcDir);
                if (srcDir.exists()) {
                    throw new IOException("Failed to delete original directory '" + srcDir + "' after copy to '" + destDir + "'");
                }
            }

        }
    }

    public static void moveDirectoryToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
        if (src == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destDir == null) {
            throw new NullPointerException("Destination directory must not be null");
        } else {
            if (!destDir.exists() && createDestDir) {
                destDir.mkdirs();
            }

            if (!destDir.exists()) {
                throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "]");
            } else if (!destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' is not a directory");
            } else {
                moveDirectory(src, new File(destDir, src.getName()));
            }
        }
    }

    public static void moveFile(File srcFile, File destFile) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        } else if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        } else if (destFile.exists()) {
            throw new FileExistsException("Destination '" + destFile + "' already exists");
        } else if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        } else {
            boolean var2 = srcFile.renameTo(destFile);
            if (!var2) {
                copyFile(srcFile, destFile);
                if (!srcFile.delete()) {
                    deleteQuietly(destFile);
                    throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
                }
            }

        }
    }

    public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destDir == null) {
            throw new NullPointerException("Destination directory must not be null");
        } else {
            if (!destDir.exists() && createDestDir) {
                destDir.mkdirs();
            }

            if (!destDir.exists()) {
                throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + createDestDir + "]");
            } else if (!destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' is not a directory");
            } else {
                moveFile(srcFile, new File(destDir, srcFile.getName()));
            }
        }
    }

    public static void moveToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
        if (src == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!src.exists()) {
            throw new FileNotFoundException("Source '" + src + "' does not exist");
        } else {
            if (src.isDirectory()) {
                moveDirectoryToDirectory(src, destDir, createDestDir);
            } else {
                moveFileToDirectory(src, destDir, createDestDir);
            }

        }
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        } else if (FilenameUtils.isSystemWindows()) {
            return false;
        } else {
            File var1 = null;
            if (file.getParent() == null) {
                var1 = file;
            } else {
                File var2 = file.getParentFile().getCanonicalFile();
                var1 = new File(var2, file.getName());
            }

            return !var1.getCanonicalFile().equals(var1.getAbsoluteFile());
        }
    }

    public static boolean deleteFD(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        } else {
            File var1 = new File(path);
            File var2 = new File(var1.getAbsolutePath() + System.currentTimeMillis());
            var1.renameTo(var2);
            return deleteFD(var2);
        }
    }

    public static boolean deleteFD(File fd) {
        if (!fd.exists()) {
            return false;
        } else {
            return fd.isDirectory() ? deleteDirectory(fd) : fd.delete();
        }
    }

    public static boolean isDeleteFile(String fileName) {
        File var1 = new File(fileName);
        if (var1.exists()) {
            var1.delete();
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDeleteFile(File file) {
        if (file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    public static boolean deleteDirectory(File dir) {
        clearDirectory(dir);
        return dir.delete();
    }

    public static void clearDirectory(File dir) {
        File[] var1 = dir.listFiles();
        if (var1 != null) {
            File[] var2 = var1;
            int var3 = var1.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                File var5 = var2[var4];
                if (var5.isDirectory()) {
                    deleteDirectory(var5);
                } else {
                    var5.delete();
                }
            }

        }
    }

    public static File openOrCreateFile(String path) throws IOException {
        File var1 = new File(path);
        if (var1.exists() && var1.isFile()) {
            return var1;
        } else {
            boolean var2 = false;
            File var3 = var1.getParentFile();
            if (!var3.exists()) {
                var2 = var3.mkdirs();
                if (!var2) {
                    return null;
                }
            }

            var2 = var1.createNewFile();
            return !var2 ? null : var1;
        }
    }

    public static void mergeFiles(String outFile, String[] files) {
        FileChannel var2 = null;

        try {
            var2 = (new FileOutputStream(outFile)).getChannel();
            String[] var3 = files;
            int var4 = files.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String var6 = var3[var5];
                FileChannel var7 = (new FileInputStream(var6)).getChannel();
                ByteBuffer var8 = ByteBuffer.allocate(8192);

                while (var7.read(var8) != -1) {
                    var8.flip();
                    var2.write(var8);
                    var8.clear();
                }

                var7.close();
            }
        } catch (IOException var17) {
            var17.printStackTrace();
        } finally {
            try {
                if (var2 != null) {
                    var2.close();
                }
            } catch (IOException var16) {
                ;
            }

        }

    }

    public static final String getMimeType(String filePath) {
        String var1 = null;
        if (VERSION.SDK_INT >= 26) {
            File var2 = new File(filePath);

            try {
                var1 = Files.probeContentType(var2.toPath());
            } catch (IOException var17) {
                Log.e("AliYunLog", "Get mime type failed!", var17);
            }
        }

        if (var1 == null) {
            String var19 = MimeTypeMap.getFileExtensionFromUrl(filePath);
            var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var19);
        }

        if (var1 == null) {
            BufferedInputStream var20 = null;

            try {
                var20 = new BufferedInputStream(new FileInputStream(filePath));
                var1 = URLConnection.guessContentTypeFromStream(var20);
            } catch (FileNotFoundException var15) {
                Log.e("AliYunLog", "Get mime type failed!", var15);
            } catch (IOException var16) {
                Log.e("AliYunLog", "Get mime type failed!", var16);
            } finally {
                if (var20 != null) {
                    try {
                        var20.close();
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }
                }

            }
        }

        if (var1 == null) {
            Options var21 = new Options();
            var21.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, var21);
            var1 = var21.outMimeType;
        }

        if (var1 == null) {
            var1 = "";
        }

        return var1;
    }

    static {
        ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
        ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
        ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
        ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
        ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
        ONE_ZB = BigInteger.valueOf(1024L).multiply(BigInteger.valueOf(1152921504606846976L));
        ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
        EMPTY_FILE_ARRAY = new File[0];
        UTF8 = Charset.forName("UTF-8");
    }

    public static class FileExistsException extends IOException {
        private static final long serialVersionUID = 1L;

        public FileExistsException() {
        }

        public FileExistsException(String message) {
            super(message);
        }

        public FileExistsException(File file) {
            super("File " + file + " exists");
        }
    }

    public static void DeleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                if (file.isDirectory()) {
                    File[] childFile = file.listFiles();
                    if (childFile == null || childFile.length == 0) {
                        file.delete();
                        return;
                    }

                    File[] var2 = childFile;
                    int var3 = childFile.length;

                    for (int var4 = 0; var4 < var3; ++var4) {
                        File f = var2[var4];
                        DeleteFile(f);
                    }

                    file.delete();
                }

            }
        }
    }

    public static boolean renameFile(File file, String newFileName) {
        if (newFileName.matches("^[^\\/?\"*:<>\\\u0005]{1,255}$")) {
            File newFile = null;
            if (file.isDirectory()) {
                newFile = new File(file.getParentFile(), newFileName);
            } else {
                String temp = newFileName + file.getName().substring(file.getName().lastIndexOf(46));
                newFile = new File(file.getParentFile(), temp);
            }

            if (file.renameTo(newFile)) {
                return true;
            }
        }

        return false;
    }

    public static String getFileSize(File file) {
        FileInputStream fis = null;

        String var3;
        try {
            fis = new FileInputStream(file);
            int length = fis.available();
            if ((long) length < 1073741824L) {
                if ((long) length >= 1048576L) {
                    var3 = String.format("%.2f MB", (double) length * 1.0D / 1048576.0D);
                    return var3;
                }

                var3 = String.format("%.2f KB", (double) length * 1.0D / 1024.0D);
                return var3;
            }

            var3 = String.format("%.2f GB", (double) length * 1.0D / 1.073741824E9D);
        } catch (Exception var15) {
            var15.printStackTrace();
            return "未知";
        } finally {
            try {
                fis.close();
            } catch (IOException var14) {
                var14.printStackTrace();
            }

        }

        return var3;
    }

    public static void openFile(Activity activity, File file) throws Exception {
        Intent intent = new Intent();
        intent.setFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), getMimeType(file, activity));
        activity.startActivity(intent);
    }

    public static String getMimeType(File file, Activity activity) throws Exception {
        String name = file.getName().substring(file.getName().lastIndexOf(46) + 1).toLowerCase();
        int id = activity.getResources().getIdentifier(activity.getPackageName() + ":string/" + name, (String) null, (String) null);
        if ("class".equals(name)) {
            return "application/octet-stream";
        } else if ("3gp".equals(name)) {
            return "video/3gpp";
        } else if ("nokia-op-logo".equals(name)) {
            return "image/vnd.nok-oplogo-color";
        } else if (id == 0) {
            throw new Exception("未找到分享该格式的应用");
        } else {
            return activity.getString(id);
        }
    }

    public static List<HashMap<String, Object>> recursionFolder(File folder, FileFilter filter) {
        List<HashMap<String, Object>> list = new ArrayList();
        File[] files = folder.listFiles();
        if (filter != null) {
            files = folder.listFiles(filter);
        }

        if (files != null) {
            for (int m = 0; m < files.length; ++m) {
                File file = files[m];
                if (file.isDirectory()) {
                    list.addAll(recursionFolder(file, filter));
                } else {
                    HashMap<String, Object> map = new HashMap();
                    map.put("file", file);
                    if (file.getAbsolutePath().toLowerCase().matches("^.*\\.(mp3|wav)$")) {
                        map.put("iconType", 3);
                    } else if (file.getAbsolutePath().toLowerCase().matches("^.*\\.(mp4|3gp)$")) {
                        map.put("iconType", 4);
                    } else if (file.getAbsolutePath().toLowerCase().matches("^.*\\.(gif|jpg|png)$")) {
                        map.put("iconType", 5);
                    } else {
                        map.put("iconType", 6);
                    }

                    list.add(map);
                }
            }
        }

        return list;
    }

    public static List<HashMap<String, Object>> unrecursionFolder(File folder, FileFilter filter) {
        List<HashMap<String, Object>> list = new ArrayList();
        if (!folder.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            HashMap<String, Object> map = new HashMap();
            map.put("file", folder.getParentFile());
            map.put("iconType", 1);
            list.add(map);
        }

        File[] files = folder.listFiles();
        if (filter != null) {
            files = folder.listFiles(filter);
        }

        if (files != null && files.length > 0) {
            File[] var4 = files;
            int var5 = files.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                File p = var4[var6];
                HashMap<String, Object> map = new HashMap();
                map.put("file", p);
                if (p.isDirectory()) {
                    map.put("iconType", 2);
                } else if (p.getAbsolutePath().toLowerCase().matches("^.*\\.(mp3|wav)$")) {
                    map.put("iconType", 3);
                } else if (p.getAbsolutePath().toLowerCase().matches("^.*\\.(mp4|3gp)$")) {
                    map.put("iconType", 4);
                } else if (p.getAbsolutePath().toLowerCase().matches("^.*\\.(gif|jpg|png)$")) {
                    map.put("iconType", 5);
                } else {
                    map.put("iconType", 6);
                }

                list.add(map);
            }
        }

        return list;
    }

    public static FileFilter getFileFilter(final String reg, boolean isdir) {
        return isdir ? new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().toLowerCase().matches(reg) || pathname.isDirectory();
            }
        } : new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().toLowerCase().matches(reg) && pathname.isFile();
            }
        };
    }

    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (!file.isFile()) {
            return null;
        } else {
            BufferedReader reader = null;

            StringBuilder var7;
            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);

                String line;
                for (reader = new BufferedReader(is); (line = reader.readLine()) != null; fileContent.append(line)) {
                    if (!fileContent.toString().equals("")) {
                        fileContent.append("\r\n");
                    }
                }

                reader.close();
                var7 = fileContent;
            } catch (IOException var16) {
                throw new RuntimeException("IOException occurred. ", var16);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var15) {
                        throw new RuntimeException("IOException occurred. ", var15);
                    }
                }

            }

            return var7;
        }
    }

    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        } else {
            FileWriter fileWriter = null;

            boolean var4;
            try {
                makeDirs(filePath);
                fileWriter = new FileWriter(filePath, append);
                fileWriter.write(content);
                fileWriter.close();
                var4 = true;
            } catch (IOException var13) {
                throw new RuntimeException("IOException occurred. ", var13);
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException var12) {
                        throw new RuntimeException("IOException occurred. ", var12);
                    }
                }

            }

            return var4;
        }
    }

    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (contentList != null && contentList.size() >= 1) {
            FileWriter fileWriter = null;

            boolean var16;
            try {
                makeDirs(filePath);
                fileWriter = new FileWriter(filePath, append);
                int i = 0;

                String line;
                for (Iterator var5 = contentList.iterator(); var5.hasNext(); fileWriter.write(line)) {
                    line = (String) var5.next();
                    if (i++ > 0) {
                        fileWriter.write("\r\n");
                    }
                }

                fileWriter.close();
                var16 = true;
            } catch (IOException var14) {
                throw new RuntimeException("IOException occurred. ", var14);
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException var13) {
                        throw new RuntimeException("IOException occurred. ", var13);
                    }
                }

            }

            return var16;
        } else {
            return false;
        }
    }

    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    public static boolean writeFile(File file, InputStream stream, boolean append) {
        FileOutputStream o = null;

        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte[] data = new byte[1024];
            boolean var5 = true;

            int length;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }

            o.flush();
            boolean var6 = true;
            return var6;
        } catch (FileNotFoundException var16) {
            throw new RuntimeException("FileNotFoundException occurred. ", var16);
        } catch (IOException var17) {
            throw new RuntimeException("IOException occurred. ", var17);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException var15) {
                    throw new RuntimeException("IOException occurred. ", var15);
                }
            }

        }
    }

    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException var4) {
            throw new RuntimeException("FileNotFoundException occurred. ", var4);
        }

        return writeFile((String) destFilePath, (InputStream) inputStream);
    }

    public static final byte[] input2byte(InputStream inStream) {
        if (inStream == null) {
            return null;
        } else {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            boolean var3 = false;

            int rc;
            try {
                while ((rc = inStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
            } catch (IOException var5) {
                var5.printStackTrace();
            }

            return swapStream.toByteArray();
        }
    }

    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList();
        if (!file.isFile()) {
            return null;
        } else {
            BufferedReader reader = null;

            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
                reader = new BufferedReader(is);
                String line = null;

                while ((line = reader.readLine()) != null) {
                    fileContent.add(line);
                }

                reader.close();
                return fileContent;
            } catch (IOException var16) {
                throw new RuntimeException("IOException occurred. ", var16);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var15) {
                        throw new RuntimeException("IOException occurred. ", var15);
                    }
                }

            }
        }
    }

    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int extenPosi = filePath.lastIndexOf(".");
            int filePosi = filePath.lastIndexOf(File.separator);
            if (filePosi == -1) {
                return extenPosi == -1 ? filePath : filePath.substring(0, extenPosi);
            } else if (extenPosi == -1) {
                return filePath.substring(filePosi + 1);
            } else {
                return filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1);
            }
        }
    }

    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int filePosi = filePath.lastIndexOf(File.separator);
            return filePosi == -1 ? filePath : filePath.substring(filePosi + 1);
        }
    }

    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int filePosi = filePath.lastIndexOf(File.separator);
            return filePosi == -1 ? "" : filePath.substring(0, filePosi);
        }
    }

    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        } else {
            int extenPosi = filePath.lastIndexOf(".");
            int filePosi = filePath.lastIndexOf(File.separator);
            if (extenPosi == -1) {
                return "";
            } else {
                return filePosi >= extenPosi ? "" : filePath.substring(extenPosi + 1);
            }
        }
    }

    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        } else {
            File folder = new File(folderName);
            return folder.exists() && folder.isDirectory() || folder.mkdirs();
        }
    }

    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        } else {
            File file = new File(filePath);
            return file.exists() && file.isFile();
        }
    }

    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        } else {
            File dire = new File(directoryPath);
            return dire.exists() && dire.isDirectory();
        }
    }

    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        } else {
            File file = new File(path);
            if (!file.exists()) {
                return true;
            } else if (file.isFile()) {
                return file.delete();
            } else if (!file.isDirectory()) {
                return false;
            } else {
                File[] var2 = file.listFiles();
                int var3 = var2.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    File f = var2[var4];
                    if (f.isFile()) {
                        f.delete();
                    } else if (f.isDirectory()) {
                        deleteFile(f.getAbsolutePath());
                    }
                }

                return file.delete();
            }
        }
    }

    public static long getFileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1L;
        } else {
            File file = new File(path);
            return file.exists() && file.isFile() ? file.length() : -1L;
        }
    }

    public static File uri2File(Activity activity, Uri uri) {
        String[] projection;
        if (VERSION.SDK_INT < 11) {
            projection = new String[]{"_data"};
            Cursor actualimagecursor = activity.managedQuery(uri, projection, (String) null, (String[]) null, (String) null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow("_data");
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            return new File(img_path);
        } else {
            projection = new String[]{"_data"};
            CursorLoader loader = new CursorLoader(activity, uri, projection, (String) null, (String[]) null, (String) null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        }
    }
}

