package com.example.chenchenggui.mykotlintestcode;

import android.app.ActivityManager;
import android.os.Build;

import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.cache.MemoryCacheParams;

@SuppressWarnings("ALL")
public class MyBitmapMemoryCacheParamsSupplier implements Supplier<MemoryCacheParams> {
    private static final int MAX_CACHE_ENTRIES = Integer.MAX_VALUE;
    private static final int MAX_CACHE_ASHM_ENTRIES = 128;
    private static final int MAX_CACHE_EVICTION_SIZE = 25;
    private static final int MAX_CACHE_EVICTION_ENTRIES = 125;
    private final ActivityManager mActivityManager;

    public MyBitmapMemoryCacheParamsSupplier(ActivityManager activityManager) {
        mActivityManager = activityManager;
    }

    @Override
    public MemoryCacheParams get() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new MemoryCacheParams(getMaxCacheSize(), MAX_CACHE_ENTRIES, MAX_CACHE_EVICTION_SIZE * ByteConstants.MB, MAX_CACHE_EVICTION_ENTRIES, 1 * ByteConstants.MB);
        } else {
            return new MemoryCacheParams(
                    getMaxCacheSize(),
                    MAX_CACHE_ASHM_ENTRIES,
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE);
        }
    }

    private int getMaxCacheSize() {
        final int maxMemory =
                Math.min(mActivityManager.getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
        int mb32 = 32;
        int mb64 = 64;
        if (maxMemory < mb32 * ByteConstants.MB) {
            return 30 * ByteConstants.MB;
        } else if (maxMemory < mb64 * ByteConstants.MB) {
            return 60 * ByteConstants.MB;
        } else {
            return maxMemory - 100;
        }
    }
}
