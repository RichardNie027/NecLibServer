package com.nec.lib.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollectionUtil {

    public static <T> void removeItemFromList(List<T> list, T item) {
        Iterator<T> it = list.iterator();
        while(it.hasNext()) {
            T t = it.next();
            if(t.equals(item)) {
                it.remove();
                break;
            }
        }
    }

}