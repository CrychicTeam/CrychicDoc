package com.mojang.realmsclient.util;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class UploadTokenCache {

    private static final Long2ObjectMap<String> TOKEN_CACHE = new Long2ObjectOpenHashMap();

    public static String get(long long0) {
        return (String) TOKEN_CACHE.get(long0);
    }

    public static void invalidate(long long0) {
        TOKEN_CACHE.remove(long0);
    }

    public static void put(long long0, String string1) {
        TOKEN_CACHE.put(long0, string1);
    }
}