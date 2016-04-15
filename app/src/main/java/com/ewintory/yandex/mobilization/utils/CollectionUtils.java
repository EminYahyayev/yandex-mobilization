package com.ewintory.yandex.mobilization.utils;

import java.util.Collection;

public final class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    private CollectionUtils() {
        throw new AssertionError("No instances.");
    }
}
