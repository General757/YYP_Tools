//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyp.tools.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Yan on 2019/10/11.
 */
public class FilenameUtils {
    public static final char EXTENSION_SEPARATOR = '.';
    public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char SYSTEM_SEPARATOR;
    private static final char OTHER_SEPARATOR;

    public FilenameUtils() {
    }

    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == '\\';
    }

    private static boolean isSeparator(char ch) {
        return ch == '/' || ch == '\\';
    }

    public static String normalize(String filename) {
        return doNormalize(filename, SYSTEM_SEPARATOR, true);
    }

    public static String normalize(String filename, boolean unixSeparator) {
        int var2 = unixSeparator ? 47 : 92;
        return doNormalize(filename, (char) var2, true);
    }

    public static String normalizeNoEndSeparator(String filename) {
        return doNormalize(filename, SYSTEM_SEPARATOR, false);
    }

    public static String normalizeNoEndSeparator(String filename, boolean unixSeparator) {
        int var2 = unixSeparator ? 47 : 92;
        return doNormalize(filename, (char) var2, false);
    }

    private static String doNormalize(String filename, char separator, boolean keepSeparator) {
        if (filename == null) {
            return null;
        } else {
            int var3 = filename.length();
            if (var3 == 0) {
                return filename;
            } else {
                int var4 = getPrefixLength(filename);
                if (var4 < 0) {
                    return null;
                } else {
                    char[] var5 = new char[var3 + 2];
                    filename.getChars(0, filename.length(), var5, 0);
                    char var6 = separator == SYSTEM_SEPARATOR ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;

                    for (int var7 = 0; var7 < var5.length; ++var7) {
                        if (var5[var7] == var6) {
                            var5[var7] = separator;
                        }
                    }

                    boolean var10 = true;
                    if (var5[var3 - 1] != separator) {
                        var5[var3++] = separator;
                        var10 = false;
                    }

                    int var8;
                    for (var8 = var4 + 1; var8 < var3; ++var8) {
                        if (var5[var8] == separator && var5[var8 - 1] == separator) {
                            System.arraycopy(var5, var8, var5, var8 - 1, var3 - var8);
                            --var3;
                            --var8;
                        }
                    }

                    for (var8 = var4 + 1; var8 < var3; ++var8) {
                        if (var5[var8] == separator && var5[var8 - 1] == '.' && (var8 == var4 + 1 || var5[var8 - 2] == separator)) {
                            if (var8 == var3 - 1) {
                                var10 = true;
                            }

                            System.arraycopy(var5, var8 + 1, var5, var8 - 1, var3 - var8);
                            var3 -= 2;
                            --var8;
                        }
                    }

                    label109:
                    for (var8 = var4 + 2; var8 < var3; ++var8) {
                        if (var5[var8] == separator && var5[var8 - 1] == '.' && var5[var8 - 2] == '.' && (var8 == var4 + 2 || var5[var8 - 3] == separator)) {
                            if (var8 == var4 + 2) {
                                return null;
                            }

                            if (var8 == var3 - 1) {
                                var10 = true;
                            }

                            for (int var9 = var8 - 4; var9 >= var4; --var9) {
                                if (var5[var9] == separator) {
                                    System.arraycopy(var5, var8 + 1, var5, var9 + 1, var3 - var8);
                                    var3 -= var8 - var9;
                                    var8 = var9 + 1;
                                    continue label109;
                                }
                            }

                            System.arraycopy(var5, var8 + 1, var5, var4, var3 - var8);
                            var3 -= var8 + 1 - var4;
                            var8 = var4 + 1;
                        }
                    }

                    if (var3 <= 0) {
                        return "";
                    } else if (var3 <= var4) {
                        return new String(var5, 0, var3);
                    } else if (var10 && keepSeparator) {
                        return new String(var5, 0, var3);
                    } else {
                        return new String(var5, 0, var3 - 1);
                    }
                }
            }
        }
    }

    public static String concat(String basePath, String fullFilenameToAdd) {
        int var2 = getPrefixLength(fullFilenameToAdd);
        if (var2 < 0) {
            return null;
        } else if (var2 > 0) {
            return normalize(fullFilenameToAdd);
        } else if (basePath == null) {
            return null;
        } else {
            int var3 = basePath.length();
            if (var3 == 0) {
                return normalize(fullFilenameToAdd);
            } else {
                char var4 = basePath.charAt(var3 - 1);
                return isSeparator(var4) ? normalize(basePath + fullFilenameToAdd) : normalize(basePath + '/' + fullFilenameToAdd);
            }
        }
    }

    public static String separatorsToUnix(String path) {
        return path != null && path.indexOf(92) != -1 ? path.replace('\\', '/') : path;
    }

    public static String separatorsToWindows(String path) {
        return path != null && path.indexOf(47) != -1 ? path.replace('/', '\\') : path;
    }

    public static String separatorsToSystem(String path) {
        if (path == null) {
            return null;
        } else {
            return isSystemWindows() ? separatorsToWindows(path) : separatorsToUnix(path);
        }
    }

    public static int getPrefixLength(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int var1 = filename.length();
            if (var1 == 0) {
                return 0;
            } else {
                char var2 = filename.charAt(0);
                if (var2 == ':') {
                    return -1;
                } else if (var1 == 1) {
                    if (var2 == '~') {
                        return 2;
                    } else {
                        return isSeparator(var2) ? 1 : 0;
                    }
                } else {
                    int var4;
                    if (var2 == '~') {
                        int var6 = filename.indexOf(47, 1);
                        var4 = filename.indexOf(92, 1);
                        if (var6 == -1 && var4 == -1) {
                            return var1 + 1;
                        } else {
                            var6 = var6 == -1 ? var4 : var6;
                            var4 = var4 == -1 ? var6 : var4;
                            return Math.min(var6, var4) + 1;
                        }
                    } else {
                        char var3 = filename.charAt(1);
                        if (var3 == ':') {
                            var2 = Character.toUpperCase(var2);
                            if (var2 >= 'A' && var2 <= 'Z') {
                                return var1 != 2 && isSeparator(filename.charAt(2)) ? 3 : 2;
                            } else {
                                return -1;
                            }
                        } else if (isSeparator(var2) && isSeparator(var3)) {
                            var4 = filename.indexOf(47, 2);
                            int var5 = filename.indexOf(92, 2);
                            if ((var4 != -1 || var5 != -1) && var4 != 2 && var5 != 2) {
                                var4 = var4 == -1 ? var5 : var4;
                                var5 = var5 == -1 ? var4 : var5;
                                return Math.min(var4, var5) + 1;
                            } else {
                                return -1;
                            }
                        } else {
                            return isSeparator(var2) ? 1 : 0;
                        }
                    }
                }
            }
        }
    }

    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int var1 = filename.lastIndexOf(47);
            int var2 = filename.lastIndexOf(92);
            return Math.max(var1, var2);
        }
    }

    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        } else {
            int var1 = filename.lastIndexOf(46);
            int var2 = indexOfLastSeparator(filename);
            return var2 > var1 ? -1 : var1;
        }
    }

    public static String getPrefix(String filename) {
        if (filename == null) {
            return null;
        } else {
            int var1 = getPrefixLength(filename);
            if (var1 < 0) {
                return null;
            } else {
                return var1 > filename.length() ? filename + '/' : filename.substring(0, var1);
            }
        }
    }

    public static String getPath(String filename) {
        return doGetPath(filename, 1);
    }

    public static String getPathNoEndSeparator(String filename) {
        return doGetPath(filename, 0);
    }

    private static String doGetPath(String filename, int separatorAdd) {
        if (filename == null) {
            return null;
        } else {
            int var2 = getPrefixLength(filename);
            if (var2 < 0) {
                return null;
            } else {
                int var3 = indexOfLastSeparator(filename);
                int var4 = var3 + separatorAdd;
                return var2 < filename.length() && var3 >= 0 && var2 < var4 ? filename.substring(var2, var4) : "";
            }
        }
    }

    public static String getFullPath(String filename) {
        return doGetFullPath(filename, true);
    }

    public static String getFullPathNoEndSeparator(String filename) {
        return doGetFullPath(filename, false);
    }

    private static String doGetFullPath(String filename, boolean includeSeparator) {
        if (filename == null) {
            return null;
        } else {
            int var2 = getPrefixLength(filename);
            if (var2 < 0) {
                return null;
            } else if (var2 >= filename.length()) {
                return includeSeparator ? getPrefix(filename) : filename;
            } else {
                int var3 = indexOfLastSeparator(filename);
                if (var3 < 0) {
                    return filename.substring(0, var2);
                } else {
                    int var4 = var3 + (includeSeparator ? 1 : 0);
                    if (var4 == 0) {
                        ++var4;
                    }

                    return filename.substring(0, var4);
                }
            }
        }
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        } else {
            int var1 = indexOfLastSeparator(filename);
            return filename.substring(var1 + 1);
        }
    }

    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        } else {
            int var1 = indexOfExtension(filename);
            return var1 == -1 ? "" : filename.substring(var1 + 1);
        }
    }

    public static boolean isExtension(String filename, String extension) {
        if (filename == null) {
            return false;
        } else if (extension != null && extension.length() != 0) {
            String var2 = getExtension(filename);
            return var2.equals(extension);
        } else {
            return indexOfExtension(filename) == -1;
        }
    }

    public static boolean isExtension(String filename, String[] extensions) {
        if (filename == null) {
            return false;
        } else if (extensions != null && extensions.length != 0) {
            String var2 = getExtension(filename);
            String[] var3 = extensions;
            int var4 = extensions.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String var6 = var3[var5];
                if (var2.equals(var6)) {
                    return true;
                }
            }

            return false;
        } else {
            return indexOfExtension(filename) == -1;
        }
    }

    public static boolean isExtension(String filename, Collection<String> extensions) {
        if (filename == null) {
            return false;
        } else if (extensions != null && !extensions.isEmpty()) {
            String var2 = getExtension(filename);
            Iterator var3 = extensions.iterator();

            String var4;
            do {
                if (!var3.hasNext()) {
                    return false;
                }

                var4 = (String) var3.next();
            } while (!var2.equals(var4));

            return true;
        } else {
            return indexOfExtension(filename) == -1;
        }
    }

    static String[] splitOnTokens(String text) {
        if (text.indexOf(63) == -1 && text.indexOf(42) == -1) {
            return new String[]{text};
        } else {
            char[] var1 = text.toCharArray();
            ArrayList var2 = new ArrayList();
            StringBuilder var3 = new StringBuilder();

            for (int var4 = 0; var4 < var1.length; ++var4) {
                if (var1[var4] != '?' && var1[var4] != '*') {
                    var3.append(var1[var4]);
                } else {
                    if (var3.length() != 0) {
                        var2.add(var3.toString());
                        var3.setLength(0);
                    }

                    if (var1[var4] == '?') {
                        var2.add("?");
                    } else if (var2.isEmpty() || var4 > 0 && !((String) var2.get(var2.size() - 1)).equals("*")) {
                        var2.add("*");
                    }
                }
            }

            if (var3.length() != 0) {
                var2.add(var3.toString());
            }

            return (String[]) var2.toArray(new String[var2.size()]);
        }
    }

    static {
        SYSTEM_SEPARATOR = File.separatorChar;
        if (isSystemWindows()) {
            OTHER_SEPARATOR = '/';
        } else {
            OTHER_SEPARATOR = '\\';
        }

    }
}

