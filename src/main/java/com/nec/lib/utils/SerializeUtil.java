package com.nec.lib.utils;

import java.io.*;

public class SerializeUtil {

    //序列化
    public static byte [] serialize(Object obj) {
        ObjectOutputStream obi = null;
        ByteArrayOutputStream bai = null;
        try {
            bai = new ByteArrayOutputStream();
            obi = new ObjectOutputStream(bai);
            obi.writeObject(obj);
            byte[] bytes = bai.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //反序列化
    public static Object unserizlize(byte[] bytes) {
        ObjectInputStream oii = null;
        ByteArrayInputStream bis = null;
        bis=new ByteArrayInputStream(bytes);
        try {
            oii = new ObjectInputStream(bis);
            Object obj = oii.readObject();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
