package com.nec.lib.utils;

import java.util.Arrays;
import java.util.List;

public class ArrayUtil {

    public static Integer[] convertStrArrayToInt(String[] strArray) {
        if (strArray != null && strArray.length > 0) {
            Integer array[] = new Integer[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                array[i] = Integer.parseInt(strArray[i]);
            }
            return array;
        } else {
            return null;
        }
    }

    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static String concatStrArrayToString(String[] strArray, String separator) {
        String result = "";
        for (String str : strArray) {
            if(!result.isEmpty())
                result = result + separator + str;
            else
                result = result + str;
        }
        return  result;
    }

    public static String concatStrListToString(List<String> strList, String separator) {
        String result = "";
        for (String str : strList) {
            if(result.isEmpty())
                result = result + str;
            else
                result = result + separator + str;
        }
        return  result;
    }
}
