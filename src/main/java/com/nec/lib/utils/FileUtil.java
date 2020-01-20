package com.nec.lib.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileUtil {

    /** 读取文件，输出字节流 */
    public static byte[] getFileStream(String filePath, String fileName) {
        if(!filePath.endsWith("/") && !filePath.endsWith("\\"))
            filePath = filePath + "/";
        File file = new File(filePath + fileName);
        if (file.exists()) {
            byte[] buffer = null;
            byte[] buffer1 = new byte[1024];
            FileInputStream fis = null;
            ByteArrayOutputStream bos = null;
            try {
                fis = new FileInputStream(file);
                bos = new ByteArrayOutputStream();
                int n;
                while ((n = fis.read(buffer1)) != -1) {
                    bos.write(buffer1, 0, n);
                }
                buffer = bos.toByteArray();
            } catch (Exception e) {
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {}
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {}
                }
            }
            return buffer;
        } else {
            return null;
        }
    }

    public static String getFileExtension(byte[] fileData) {
        if(fileData.length < 10)
            return "";
        String ext = "";
        byte b0 = fileData[0];
        byte b1 = fileData[1];
        byte b2 = fileData[2];
        byte b3 = fileData[3];
        byte b6 = fileData[6];
        byte b7 = fileData[7];
        byte b8 = fileData[8];
        byte b9 = fileData[9];
        // GIF
        if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F')
            ext = ".GIF";
        // PNG
        else if (b1 == (byte) 'P' && b2 == (byte) 'N' && b3 == (byte) 'G')
            ext = ".PNG";
        // JPG
        else if (b6 == (byte) 'J' && b7 == (byte) 'F' && b8 == (byte) 'I' && b9 == (byte) 'F')
            ext = ".JPG";
        else
            ext = "";
        return ext;
    }

    public static List<String> ls(String path, boolean includeFoler, boolean includeFile) {
        return ls(path, includeFoler, includeFile, false, null, null);
    }

    public static List<String> ls(String path, boolean includeFoler, boolean includeFile, boolean includeLastModified) {
        return ls(path, includeFoler, includeFile, includeLastModified, includeLastModified?"yyyyMMddHHmm":null, includeLastModified?"|":null);
    }

    public static List<String> ls(String path, boolean includeFoler, boolean includeFile, boolean includeLastModified, String dateFormat, String partSeparator) {
        List<String> folderList = new ArrayList<>();
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                return folderList;
            } else {
                Arrays.sort(files, new Comparator<File>() { //按文件名排序

                    @Override
                    public int compare(File o1, File o2) {
                        if (o1.isDirectory() && o2.isFile())
                            return -1;
                        if (o1.isFile() && o2.isDirectory())
                            return 1;
                        return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
                    }
                });
                SimpleDateFormat formatter = null;
                try {
                    if (includeLastModified) {
                        formatter = new SimpleDateFormat(dateFormat != null && !dateFormat.isEmpty() ? dateFormat : "yyyyMMddHHmm");
                    }
                } catch (Exception e) {
                    formatter = new SimpleDateFormat("yyyyMMddHHmm");
                }
                for (File file2 : files) {
                    try {
                        String timeStr = "";
                        if (includeLastModified) {
                            Calendar cal = Calendar.getInstance();
                            long time = file2.lastModified();
                            cal.setTimeInMillis(time);
                            timeStr = partSeparator + formatter.format(cal.getTime());
                        }
                        if (file2.isDirectory()) {
                            if(includeFoler)
                                folderList.add(file2.getCanonicalPath()+timeStr);
                        } else {
                            if(includeFile)
                                folderList.add(pathLast(file2.getCanonicalPath())+timeStr);
                        }
                    } catch (IOException e) {
                    }
                }
            }
        }
        return folderList;
    }

    public static String pathLast(String path) {
        int lastIndex = path.lastIndexOf("\\");
        if(lastIndex == -1)
            lastIndex = path.lastIndexOf("/");
        if(lastIndex >= 0 && lastIndex+1 < path.length()) {
            return path.substring(lastIndex+1);
        } else
            return "";
    }
}
