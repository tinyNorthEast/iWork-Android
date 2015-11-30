package com.iwork.utils;

import android.os.Bundle;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 集合对象工具类
 * 
 *
 */
public class CollectionUtil {

    /**
     * 获取集合大小
     * 
     * @param collection
     *            集合
     * @return
     */
    public static int size(Collection<?> collection) {
        return isEmpty(collection) ? 0 : collection.size();
    }

    /**
     * 检查集合元素是否存在
     * 
     * @param collection
     *            集合
     * @return
     */
    public static boolean isAvailable(Collection<?> collection, int index) {
        if (isEmpty(collection))
            return false;
        return index >= 0 && index < collection.size();

    }

    /**
     * 检查集合元素是否为空
     * 
     * @param collection
     *            集合
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();

    }

    /**
     * 检查数组元素是否为空
     * 
     * @param array
     *            数组
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;

    }

    /**
     * 检查Map元素是否为空
     * 
     * @param map
     *            Map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();

    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean inCollection(Object object, Set<? extends Object> collection) {
        return collection.contains(object);
    }

    public static boolean isEmpty(Bundle bundle) {
        return bundle == null || bundle.isEmpty();
    }

    public static int size(Object[] array) {
        return array == null ? 0 : array.length;
    }
}
