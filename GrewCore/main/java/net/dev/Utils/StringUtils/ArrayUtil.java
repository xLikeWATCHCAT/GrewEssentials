package net.dev.Utils.StringUtils;

import com.google.common.collect.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

public class ArrayUtil {
    public static <T> boolean contains(T[] array, T obj) {
        return indexOf(array, obj) != -1;
    }

    public static <T> int indexOf(T[] array, T obj) {
        return array == null || array.length == 0 ? -1 : IntStream.range(0, array.length).filter(i -> array[i] != null && array[i].equals(obj)).findFirst().orElse(-1);
    }

    @SafeVarargs
    public static <T> T[] asArray(T... args) {
        return args;
    }

    public static <T> T[] toArray(List<T> list) {
        T[] a = (T[]) Array.newInstance(list.getClass().getComponentType(), list.size());
        Arrays.setAll(a, list::get);
        return a;
    }

    @SafeVarargs
    public static <T> List<T> asList(T... args) {
        List<T> list = Lists.newArrayList();
        Collections.addAll(list, args);
        return list;
    }

    public static <T> T skipEmpty(T obj) {
        return skipEmpty(obj, null);
    }

    public static <T> T[] skipEmpty(T[] obj) {
        return skipEmpty(obj, null);
    }

    public static <T> T skipEmpty(T obj, T def) {
        return Strings.isEmpty(String.valueOf(obj)) ? def : obj;
    }

    public static <T> T[] skipEmpty(T[] obj, T[] def) {
        return obj.length == 0 ? def : skipEmpty(obj[0]) == null ? def : obj;
    }

    public static String arrayJoin(String[] args, int start) {
        return IntStream.range(start, args.length).mapToObj(i -> args[i] + " ").collect(Collectors.joining()).trim();
    }

    public static <T> T[] arrayAppend(T[] array, T obj) {
        T[] arrayNew = arrayExpand(array, 1);
        arrayNew[array.length] = obj;
        return arrayNew;
    }

    public static <T> T[] arrayAddFirst(T[] array, T obj) {
        T[] arrayNew = arrayExpandAtFirst(array, 1);
        arrayNew[0] = obj;
        return arrayNew;
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    public static <T> T arrayExpand(T oldArray, int expand) {
        int length = Array.getLength(oldArray);
        Object newArray = Array.newInstance(oldArray.getClass().getComponentType(), length + expand);
        System.arraycopy(oldArray, 0, newArray, 0, length);
        return (T) newArray;
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    public static <T> T arrayExpandAtFirst(T oldArray, int expand) {
        int length = Array.getLength(oldArray);
        Object newArray = Array.newInstance(oldArray.getClass().getComponentType(), length + expand);
        System.arraycopy(oldArray, 0, newArray, expand, length);
        return (T) newArray;
    }
}
