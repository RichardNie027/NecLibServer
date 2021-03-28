package com.nec.lib.utils;

import jxl.common.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;

public class BeanUtil {

    /**
     * 利用反射实现对象之间属性复制
     * @param from
     * @param to
     */
    public static void copyProperties(Object from, Object to) {
        copyPropertiesExclude(from, to, null);
    }

    /**
     * 复制对象属性
     * @param from
     * @param to
     * @param excludsArray 排除属性列表
     * @throws
     */
    public static void copyPropertiesExclude(Object from, Object to, String[] excludsArray) {
        if (from == null) {
            return;
        }
        Field[] fromFields = from.getClass().getDeclaredFields();
        Field[] toFields = to.getClass().getDeclaredFields();
        Field fromField = null, toField = null;
        String key = null;
        OUTER:
        for (int i = 0; i < fromFields.length; i++) {
            fromField = fromFields[i];
            key = fromField.getName();
            if (key.equals("serialVersionUID") || key.startsWith("$")) {
                continue;
            }
            if(excludsArray != null && excludsArray.length > 0)
                for(String excludeKey: excludsArray)
                    if(key.equals(excludeKey))
                        continue OUTER;
            Object value = getFieldValue(from, key);
            for(int j=0; j < toFields.length; j++) {
                toField = toFields[j];
                if(toField.getName().equals(key)) {
                    try {
                        setFieldValue(to, key, value);
                        break;
                    } catch (Exception e) {}
                }
            }
        }
    }

    /**
     * 对象属性值复制，仅复制指定名称的属性值
     * @param from
     * @param to
     * @param includsArray
     * @throws
     */
    public static void copyPropertiesInclude(Object from, Object to, String[] includsArray) {
        if (from == null) {
            return;
        }
        Field[] fromFields = from.getClass().getDeclaredFields();
        Field[] toFields = to.getClass().getDeclaredFields();
        Field fromField = null, toField = null;
        String key = null;
        OUTER:
        for (int i = 0; i < fromFields.length; i++) {
            fromField = fromFields[i];
            key = fromField.getName();
            if (key.equals("serialVersionUID") || key.startsWith("$")) {
                continue;
            }
            boolean found = false;
            if(includsArray != null && includsArray.length > 0)
                for(String excludeKey: includsArray)
                    if(key.equals(excludeKey)) {
                        found = true;
                        break;
                    }
            if(!found)
                continue;
            Object value = getFieldValue(from, key);
            for(int j=0; j < toFields.length; j++) {
                toField = toFields[j];
                if(toField.getName().equals(key)) {
                    try {
                        setFieldValue(to, key, value);
                        break;
                    } catch (Exception e) {}
                }
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////

    public static Map<String, Object> objectToMap(Object object) {
        return objectToMap(object,null);
    }

    /**
     * Bean to Map， Date转String yyyyMMddHHmmss
     * @param object Bean
     * @param excludsArray 排除属性
     * @return
     */
    public static Map<String, Object> objectToMap(Object object, String[] excludsArray) {
        if (object == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = object.getClass().getDeclaredFields();
        OUTER:
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                String key = field.getName();
                if (key.equals("serialVersionUID") || key.startsWith("$")) {
                    continue;
                }
                if(excludsArray != null && excludsArray.length > 0)
                    for(String excludeKey: excludsArray)
                        if(key.equals(excludeKey))
                            continue OUTER;
                Object value = getFieldValue(object, key);
                if(value instanceof Collection)
                    continue;
                if (value == null) {
                    map.put(key, null);
                } else if (value instanceof Date) {
                    String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(((Date) value).getTime());
                    map.put(key, dateStr);
                } else if (value instanceof Number) {
                    double dbNum = ((Number) value).doubleValue();
                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        map.put(key, dbNum);
                    } else {
                        // 判断数字是否为整数值
                        long lngNum = (long) dbNum;
                        if (dbNum == lngNum) {
                            map.put(key, lngNum);
                        } else {
                            map.put(key, dbNum);
                        }
                    }
                } else if (value.getClass().isEnum()) {
                    Method method = value.getClass().getMethod("getCode", (Class<?>[]) null);
                    Object val = method.invoke(value, (Object[]) null);
                    map.put(key, String.valueOf(val));
                } else if (value.getClass().isPrimitive()) {
                    if (value.getClass().getName().equals("int")) {
                        value = (Integer)value;
                    } else if (value.getClass().getName().equals("long")) {
                        value = (Long)value;
                    } else if (value.getClass().getName().equals("double")) {
                        value = (Double)value;
                    } else if (value.getClass().getName().equals("short")) {
                        value = (Short)value;
                    } else if (value.getClass().getName().equals("float")) {
                        value = (Float)value;
                    } else if (value.getClass().getName().equals("boolean")) {
                        value = (Boolean)value;
                    } else if (value.getClass().getName().equals("byte")) {
                        value = (Byte)value;
                    }
                    map.put(key, value + "");
                } else {
                    map.put(key, value.toString());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return map;
    }

    public static boolean equalsInRange(Object obj1, Object obj2, String... names) {
        if(obj1==null && obj2==null)
            return true;
        if(obj1==null || obj2==null)
            return false;
        Method[] methods1 = obj1.getClass().getDeclaredMethods();
        Method[] methods2 = obj2.getClass().getDeclaredMethods();
        for(String name: names) {
            name = "get" + name.substring(0,1).toUpperCase() + name.substring(1);
            Method method1 = findMethodByName(methods1, name);
            Method method2 = findMethodByName(methods2, name);
            if(method1==null || method2==null)
                return false;
            try {
                Object value1 = method1.invoke(obj1, new Object[0]);
                Object value2 = method2.invoke(obj2, new Object[0]);
                if(value1 == null && value2 == null)
                    ;
                else if(value1 == null || value2 == null)
                    return false;
                else {
                    if(value1 instanceof Collection && value2 instanceof Collection) {
                        if(((Collection)value1).size() != ((Collection)value1).size())
                            return false;
                    } else
                    if(!value1.equals(value2))
                        return false;
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return true;
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * 从方法数组中获取指定名称的方法
     *
     * @param methods
     * @param name
     * @return
     */
    public static Method findMethodByName(Method[] methods, String name) {
        for (int j = 0; j < methods.length; j++) {
            if (methods[j].getName().equals(name))
                return methods[j];
        }
        return null;
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValue(final Object object, final String fieldName) throws IllegalArgumentException,RuntimeException {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("never happend exception!", e);
        }
        return result;
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) throws IllegalArgumentException,RuntimeException {
        Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("never happend exception!", e);
        }
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    protected static Field getDeclaredField(final Object object, final String fieldName) {
        Assert.verify(object!=null);
        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     * 循环向上转型,获取类的DeclaredField.
     */
    @SuppressWarnings("unchecked")
    protected static Field getDeclaredField(final Class clazz, final String fieldName) {
        Assert.verify(clazz!=null);
        Assert.verify(!fieldName.isEmpty());
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 强制转换fileld可访问.
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    public static Object getSimpleProperty(Object bean, String propName) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return bean.getClass().getMethod(getReadMethod(propName)).invoke(bean);
    }

    private static String getReadMethod(String name) {
        return "get" + name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * @param obj 操作的对象
     * @param att 操作的属性
     * @param value 设置的值
     * @param type 参数的类型
     * @return 存在setter方法
     * setter(obj, "name", "XX", String.class);setter(obj, "age", 18, int.class);
     */
    public static boolean setter(Object obj, String att, Object value, Class<?>type){
        try {
            Method method = obj.getClass().getMethod("set" + capitalFirst(att), type);
            method.invoke(obj, value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * @param obj 操作的对象
     * @param att 操作的属性
     * @return 属性值
     */
    public static Object getter(Object obj, String att){
        Object object = null;
        try {
            Method method = obj.getClass().getMethod("get" + capitalFirst(att));
            object = method.invoke(obj);
        }catch (Exception e){
        }
        return object;
    }
    /**
     * 首字母大写
     */
    protected static String capitalFirst(String old){	// 将单词的首字母大写
        String str = old.substring(0,1).toUpperCase() + old.substring(1) ;
        return str ;
    }

//    class Person {//Person class for test.
//        private String name;
//        private int age;
//        public String getName(){return this.name;}
//        public void setName(String name){this.name = name;}
//        public void setAge(int age){this.age = age;}
//        public int getAge(){return this.age;}
//    }
    public static void main(String []args){
        Class<?> c = null;
        Object obj = null;
        try{
            c = Class.forName("Person");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        try {
            obj = c.newInstance();
        }catch (InstantiationException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        setter(obj, "name", "XX", String.class);
        setter(obj, "age", 18, int.class);
        System.out.print("姓名：");
        System.out.print(getter(obj, "name"));
        System.out.print("年龄：");
        System.out.print(getter(obj, "age"));
    }

}
