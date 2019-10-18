package com.nec.lib.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
}
