//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import android.os.Build.VERSION;

import com.yyp.tools.Charsets;
import com.yyp.tools.stream.ByteArrayOutputStream;
import com.yyp.tools.stream.StringBuilderWriter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Yan on 2019/10/11.
 */
public class IOUtils {
    private static final int EOF = -1;
    public static final char DIR_SEPARATOR_UNIX = '/';
    public static final char DIR_SEPARATOR_WINDOWS = '\\';
    public static final char DIR_SEPARATOR;
    public static final String LINE_SEPARATOR_UNIX = "\n";
    public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
    public static final String LINE_SEPARATOR;
    private static final int SKIP_BUFFER_SIZE = 2048;
    private static char[] SKIP_CHAR_BUFFER;
    private static byte[] SKIP_BYTE_BUFFER;
    public static final int DEFAULT_BUFFER_SIZE = 32768;
    public static final int CONTINUE_LOADING_PERCENTAGE = 75;

    public IOUtils() {
    }

    public static void close(URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }

    }

    public static void closeQuietly(Reader input) {
        closeQuietly((Closeable) input);
    }

    public static void closeQuietly(Writer output) {
        closeQuietly((Closeable) output);
    }

    public static void closeQuietly(InputStream input) {
        closeQuietly((Closeable) input);
    }

    public static void closeQuietly(OutputStream output) {
        closeQuietly((Closeable) output);
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
            ;
        }

    }

    public static void closeQuietly(Socket sock) {
        if (sock != null) {
            try {
                sock.close();
            } catch (IOException var2) {
                ;
            }
        }

    }

    public static void closeQuietly(Selector selector) {
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException var2) {
                ;
            }
        }

    }

    public static void closeQuietly(ServerSocket sock) {
        if (sock != null) {
            try {
                sock.close();
            } catch (IOException var2) {
                ;
            }
        }

    }

    public static InputStream toBufferedInputStream(InputStream input) throws IOException {
        return ByteArrayOutputStream.toBufferedInputStream(input);
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream var1 = new ByteArrayOutputStream();
        copy((InputStream) input, (OutputStream) var1);
        return var1.toByteArray();
    }

    public static byte[] toByteArray(InputStream input, long size) throws IOException {
        if (size > 2147483647L) {
            throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
        } else {
            return toByteArray(input, (int) size);
        }
    }

    public static byte[] toByteArray(InputStream input, int size) throws IOException {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        } else if (size == 0) {
            return new byte[0];
        } else {
            byte[] var2 = new byte[size];

            int var3;
            int var4;
            for (var3 = 0; var3 < size && (var4 = input.read(var2, var3, size - var3)) != -1; var3 += var4) {
                ;
            }

            if (var3 != size) {
                throw new IOException("Unexpected readed size. current: " + var3 + ", excepted: " + size);
            } else {
                return var2;
            }
        }
    }

    public static byte[] toByteArray(Reader input) throws IOException {
        return toByteArray(input, Charset.defaultCharset());
    }

    public static byte[] toByteArray(Reader input, Charset encoding) throws IOException {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        copy((Reader) input, (OutputStream) var2, (Charset) encoding);
        return var2.toByteArray();
    }

    public static byte[] toByteArray(Reader input, String encoding) throws IOException {
        return toByteArray(input, Charsets.toCharset(encoding));
    }

    @Deprecated
    public static byte[] toByteArray(String input) throws IOException {
        return input.getBytes();
    }

    public static byte[] toByteArray(URI uri) throws IOException {
        return toByteArray(uri.toURL());
    }

    public static byte[] toByteArray(URL url) throws IOException {
        URLConnection var1 = url.openConnection();

        byte[] var2;
        try {
            var2 = toByteArray(var1);
        } finally {
            close(var1);
        }

        return var2;
    }

    public static byte[] toByteArray(URLConnection urlConn) throws IOException {
        InputStream var1 = urlConn.getInputStream();

        byte[] var2;
        try {
            var2 = toByteArray(var1);
        } finally {
            var1.close();
        }

        return var2;
    }

    public static char[] toCharArray(InputStream is) throws IOException {
        return toCharArray(is, Charset.defaultCharset());
    }

    public static char[] toCharArray(InputStream is, Charset encoding) throws IOException {
        CharArrayWriter var2 = new CharArrayWriter();
        copy((InputStream) is, (Writer) var2, (Charset) encoding);
        return var2.toCharArray();
    }

    public static char[] toCharArray(InputStream is, String encoding) throws IOException {
        return toCharArray(is, Charsets.toCharset(encoding));
    }

    public static char[] toCharArray(Reader input) throws IOException {
        CharArrayWriter var1 = new CharArrayWriter();
        copy((Reader) input, (Writer) var1);
        return var1.toCharArray();
    }

    public static String toString(InputStream input) throws IOException {
        return toString(input, Charset.defaultCharset());
    }

    public static String toString(InputStream input, Charset encoding) throws IOException {
        StringBuilderWriter var2 = new StringBuilderWriter();
        copy((InputStream) input, (Writer) var2, (Charset) encoding);
        return var2.toString();
    }

    public static String toString(InputStream input, String encoding) throws IOException {
        return toString(input, Charsets.toCharset(encoding));
    }

    public static String toString(Reader input) throws IOException {
        StringBuilderWriter var1 = new StringBuilderWriter();
        copy((Reader) input, (Writer) var1);
        return var1.toString();
    }

    public static String toString(URI uri) throws IOException {
        return toString(uri, Charset.defaultCharset());
    }

    public static String toString(URI uri, Charset encoding) throws IOException {
        return toString(uri.toURL(), Charsets.toCharset(encoding));
    }

    public static String toString(URI uri, String encoding) throws IOException {
        return toString(uri, Charsets.toCharset(encoding));
    }

    public static String toString(URL url) throws IOException {
        return toString(url, Charset.defaultCharset());
    }

    public static String toString(URL url, Charset encoding) throws IOException {
        InputStream var2 = url.openStream();

        String var3;
        try {
            var3 = toString(var2, encoding);
        } finally {
            var2.close();
        }

        return var3;
    }

    public static String toString(URL url, String encoding) throws IOException {
        return toString(url, Charsets.toCharset(encoding));
    }

    @Deprecated
    public static String toString(byte[] input) throws IOException {
        return new String(input);
    }

    public static String toString(byte[] input, String encoding) throws IOException {
        return new String(input, encoding);
    }

    public static List<String> readLines(InputStream input) throws IOException {
        return readLines(input, Charset.defaultCharset());
    }

    public static List<String> readLines(InputStream input, Charset encoding) throws IOException {
        InputStreamReader var2 = new InputStreamReader(input, Charsets.toCharset(encoding));
        return readLines((Reader) var2);
    }

    public static List<String> readLines(InputStream input, String encoding) throws IOException {
        return readLines(input, Charsets.toCharset(encoding));
    }

    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader var1 = toBufferedReader(input);
        ArrayList var2 = new ArrayList();

        for (String var3 = var1.readLine(); var3 != null; var3 = var1.readLine()) {
            var2.add(var3);
        }

        return var2;
    }

    public static InputStream toInputStream(CharSequence input) {
        return toInputStream(input, Charset.defaultCharset());
    }

    public static InputStream toInputStream(CharSequence input, Charset encoding) {
        return toInputStream(input.toString(), encoding);
    }

    public static InputStream toInputStream(CharSequence input, String encoding) throws IOException {
        return toInputStream(input, Charsets.toCharset(encoding));
    }

    public static InputStream toInputStream(String input) {
        return toInputStream(input, Charset.defaultCharset());
    }

    public static InputStream toInputStream(String input, Charset encoding) {
        return new ByteArrayInputStream(StringCodingUtils.getBytes(input, Charsets.toCharset(encoding)));
    }

    public static InputStream toInputStream(String input, String encoding) throws IOException {
        byte[] var2 = StringCodingUtils.getBytes(input, Charsets.toCharset(encoding));
        return new ByteArrayInputStream(var2);
    }

    public static void write(byte[] data, OutputStream output) throws IOException {
        if (data != null) {
            output.write(data);
        }

    }

    public static void write(byte[] data, Writer output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(byte[] data, Writer output, Charset encoding) throws IOException {
        if (data != null) {
            if (VERSION.SDK_INT < 9) {
                output.write(new String(data, Charsets.toCharset(encoding).name()));
            } else {
                output.write(new String(data, Charsets.toCharset(encoding)));
            }
        }

    }

    public static void write(byte[] data, Writer output, String encoding) throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    public static void write(char[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }

    }

    public static void write(char[] data, OutputStream output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(char[] data, OutputStream output, Charset encoding) throws IOException {
        if (data != null) {
            output.write(StringCodingUtils.getBytes(new String(data), Charsets.toCharset(encoding)));
        }

    }

    public static void write(char[] data, OutputStream output, String encoding) throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    public static void write(CharSequence data, Writer output) throws IOException {
        if (data != null) {
            write(data.toString(), output);
        }

    }

    public static void write(CharSequence data, OutputStream output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(CharSequence data, OutputStream output, Charset encoding) throws IOException {
        if (data != null) {
            write(data.toString(), output, encoding);
        }

    }

    public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    public static void write(String data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }

    }

    public static void write(String data, OutputStream output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(String data, OutputStream output, Charset encoding) throws IOException {
        if (data != null) {
            output.write(StringCodingUtils.getBytes(data, Charsets.toCharset(encoding)));
        }

    }

    public static void write(String data, OutputStream output, String encoding) throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    @Deprecated
    public static void write(StringBuffer data, Writer output) throws IOException {
        if (data != null) {
            output.write(data.toString());
        }

    }

    @Deprecated
    public static void write(StringBuffer data, OutputStream output) throws IOException {
        write(data, output, (String) null);
    }

    @Deprecated
    public static void write(StringBuffer data, OutputStream output, String encoding) throws IOException {
        if (data != null) {
            output.write(StringCodingUtils.getBytes(data.toString(), Charsets.toCharset(encoding)));
        }

    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output) throws IOException {
        writeLines(lines, lineEnding, output, Charset.defaultCharset());
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, Charset encoding) throws IOException {
        if (lines != null) {
            if (lineEnding == null) {
                lineEnding = LINE_SEPARATOR;
            }

            Charset var4 = Charsets.toCharset(encoding);

            for (Iterator var5 = lines.iterator(); var5.hasNext(); output.write(StringCodingUtils.getBytes(lineEnding, var4))) {
                Object var6 = var5.next();
                if (var6 != null) {
                    output.write(StringCodingUtils.getBytes(var6.toString(), var4));
                }
            }

        }
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String encoding) throws IOException {
        writeLines(lines, lineEnding, output, Charsets.toCharset(encoding));
    }

    public static void writeLines(Collection<?> lines, String lineEnding, Writer writer) throws IOException {
        if (lines != null) {
            if (lineEnding == null) {
                lineEnding = LINE_SEPARATOR;
            }

            for (Iterator var3 = lines.iterator(); var3.hasNext(); writer.write(lineEnding)) {
                Object var4 = var3.next();
                if (var4 != null) {
                    writer.write(var4.toString());
                }
            }

        }
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long var2 = copyLarge(input, output);
        return var2 > 2147483647L ? -1 : (int) var2;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copyLarge(input, output, new byte['耀']);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long var3 = 0L;

        int var6;
        for (boolean var5 = false; -1 != (var6 = input.read(buffer)); var3 += (long) var6) {
            output.write(buffer, 0, var6);
        }

        return var3;
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new byte['耀']);
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
        if (inputOffset > 0L) {
            skipFully(input, inputOffset);
        }

        if (length == 0L) {
            return 0L;
        } else {
            int var7 = buffer.length;
            int var8 = var7;
            if (length > 0L && length < (long) var7) {
                var8 = (int) length;
            }

            long var10 = 0L;

            int var9;
            while (var8 > 0 && -1 != (var9 = input.read(buffer, 0, var8))) {
                output.write(buffer, 0, var9);
                var10 += (long) var9;
                if (length > 0L) {
                    var8 = (int) Math.min(length - var10, (long) var7);
                }
            }

            return var10;
        }
    }

    public static void copy(InputStream input, Writer output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(InputStream input, Writer output, Charset encoding) throws IOException {
        InputStreamReader var3 = new InputStreamReader(input, Charsets.toCharset(encoding));
        copy((Reader) var3, (Writer) output);
    }

    public static void copy(InputStream input, Writer output, String encoding) throws IOException {
        copy(input, output, Charsets.toCharset(encoding));
    }

    public static int copy(Reader input, Writer output) throws IOException {
        long var2 = copyLarge(input, output);
        return var2 > 2147483647L ? -1 : (int) var2;
    }

    public static long copyLarge(Reader input, Writer output) throws IOException {
        return copyLarge(input, output, new char['耀']);
    }

    public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
        long var3 = 0L;

        int var6;
        for (boolean var5 = false; -1 != (var6 = input.read(buffer)); var3 += (long) var6) {
            output.write(buffer, 0, var6);
        }

        return var3;
    }

    public static long copyLarge(Reader input, Writer output, long inputOffset, long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new char['耀']);
    }

    public static long copyLarge(Reader input, Writer output, long inputOffset, long length, char[] buffer) throws IOException {
        if (inputOffset > 0L) {
            skipFully(input, inputOffset);
        }

        if (length == 0L) {
            return 0L;
        } else {
            int var7 = buffer.length;
            if (length > 0L && length < (long) buffer.length) {
                var7 = (int) length;
            }

            long var9 = 0L;

            int var8;
            while (var7 > 0 && -1 != (var8 = input.read(buffer, 0, var7))) {
                output.write(buffer, 0, var8);
                var9 += (long) var8;
                if (length > 0L) {
                    var7 = (int) Math.min(length - var9, (long) buffer.length);
                }
            }

            return var9;
        }
    }

    public static void copy(Reader input, OutputStream output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(Reader input, OutputStream output, Charset encoding) throws IOException {
        OutputStreamWriter var3 = new OutputStreamWriter(output, Charsets.toCharset(encoding));
        copy((Reader) input, (Writer) var3);
        var3.flush();
    }

    public static void copy(Reader input, OutputStream output, String encoding) throws IOException {
        copy(input, output, Charsets.toCharset(encoding));
    }

    public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream((InputStream) input1);
        }

        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream((InputStream) input2);
        }

        int var3;
        for (int var2 = ((InputStream) input1).read(); -1 != var2; var2 = ((InputStream) input1).read()) {
            var3 = ((InputStream) input2).read();
            if (var2 != var3) {
                return false;
            }
        }

        var3 = ((InputStream) input2).read();
        return var3 == -1;
    }

    public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
        BufferedReader input3 = toBufferedReader(input1);
        BufferedReader input4 = toBufferedReader(input2);

        int var3;
        for (int var2 = input3.read(); -1 != var2; var2 = input3.read()) {
            var3 = input4.read();
            if (var2 != var3) {
                return false;
            }
        }

        var3 = input4.read();
        return var3 == -1;
    }

    public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IOException {
        BufferedReader var2 = toBufferedReader(input1);
        BufferedReader var3 = toBufferedReader(input2);
        String var4 = var2.readLine();

        String var5;
        for (var5 = var3.readLine(); var4 != null && var5 != null && var4.equals(var5); var5 = var3.readLine()) {
            var4 = var2.readLine();
        }

        return var4 == null ? var5 == null : var4.equals(var5);
    }

    public static long skip(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        } else {
            if (SKIP_BYTE_BUFFER == null) {
                SKIP_BYTE_BUFFER = new byte[2048];
            }

            long var3;
            long var5;
            for (var3 = toSkip; var3 > 0L; var3 -= var5) {
                var5 = (long) input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(var3, 2048L));
                if (var5 < 0L) {
                    break;
                }
            }

            return toSkip - var3;
        }
    }

    public static long skip(Reader input, long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        } else {
            if (SKIP_CHAR_BUFFER == null) {
                SKIP_CHAR_BUFFER = new char[2048];
            }

            long var3;
            long var5;
            for (var3 = toSkip; var3 > 0L; var3 -= var5) {
                var5 = (long) input.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(var3, 2048L));
                if (var5 < 0L) {
                    break;
                }
            }

            return toSkip - var3;
        }
    }

    public static void skipFully(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        } else {
            long var3 = skip(input, toSkip);
            if (var3 != toSkip) {
                throw new EOFException("Bytes to skip: " + toSkip + " actual: " + var3);
            }
        }
    }

    public static void skipFully(Reader input, long toSkip) throws IOException {
        long var3 = skip(input, toSkip);
        if (var3 != toSkip) {
            throw new EOFException("Chars to skip: " + toSkip + " actual: " + var3);
        }
    }

    public static int read(Reader input, char[] buffer, int offset, int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        } else {
            int var4;
            int var6;
            for (var4 = length; var4 > 0; var4 -= var6) {
                int var5 = length - var4;
                var6 = input.read(buffer, offset + var5, var4);
                if (-1 == var6) {
                    break;
                }
            }

            return length - var4;
        }
    }

    public static int read(Reader input, char[] buffer) throws IOException {
        return read((Reader) input, (char[]) buffer, 0, buffer.length);
    }

    public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        } else {
            int var4;
            int var6;
            for (var4 = length; var4 > 0; var4 -= var6) {
                int var5 = length - var4;
                var6 = input.read(buffer, offset + var5, var4);
                if (-1 == var6) {
                    break;
                }
            }

            return length - var4;
        }
    }

    public static int read(InputStream input, byte[] buffer) throws IOException {
        return read((InputStream) input, (byte[]) buffer, 0, buffer.length);
    }

    public static void readFully(Reader input, char[] buffer, int offset, int length) throws IOException {
        int var4 = read(input, buffer, offset, length);
        if (var4 != length) {
            throw new EOFException("Length to read: " + length + " actual: " + var4);
        }
    }

    public static void readFully(Reader input, char[] buffer) throws IOException {
        readFully((Reader) input, (char[]) buffer, 0, buffer.length);
    }

    public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        int var4 = read(input, buffer, offset, length);
        if (var4 != length) {
            throw new EOFException("Length to read: " + length + " actual: " + var4);
        }
    }

    public static void readFully(InputStream input, byte[] buffer) throws IOException {
        readFully((InputStream) input, (byte[]) buffer, 0, buffer.length);
    }

    public static boolean copyStream(InputStream is, RandomAccessFile os, CopyListener listener, int alreadyRead) throws IOException {
        return copyStream(is, os, listener, alreadyRead, 32768);
    }

    public static boolean copyStream(InputStream is, RandomAccessFile os, CopyListener listener, int alreadyRead, int bufferSize) throws IOException {
        int var5 = alreadyRead;
        int var6 = is.available();
        byte[] var7 = new byte[bufferSize];
        if (shouldStopLoading(listener, alreadyRead, var6)) {
            return false;
        } else {
            do {
                int var8;
                if ((var8 = is.read(var7, 0, bufferSize)) == -1) {
                    return true;
                }

                os.write(var7, 0, var8);
                var5 += var8;
            } while (!shouldStopLoading(listener, var5, var6));

            return false;
        }
    }

    private static boolean shouldStopLoading(CopyListener listener, int current, int total) {
        if (listener != null) {
            boolean var3 = listener.onBytesCopied(current, total);
            if (!var3 && 100 * current / total < 75) {
                return true;
            }
        }

        return false;
    }

    public static void closeSilently(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception var2) {
            ;
        }

    }

    static {
        DIR_SEPARATOR = File.separatorChar;
        StringBuilderWriter var0 = new StringBuilderWriter(4);
        PrintWriter var1 = new PrintWriter(var0);
        var1.println();
        LINE_SEPARATOR = var0.toString();
        var1.close();
    }

    public interface CopyListener {
        boolean onBytesCopied(int var1, int var2);
    }
}

