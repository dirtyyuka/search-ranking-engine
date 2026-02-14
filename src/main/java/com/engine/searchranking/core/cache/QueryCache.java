package com.engine.searchranking.core.cache;

import java.util.LinkedHashMap;
import java.util.Map;

import com.engine.searchranking.dto.CachedQueryResult;

public class QueryCache extends LinkedHashMap<String, CachedQueryResult> {

    private final int maxEntries;

    public QueryCache(int maxEntries) {
        super(16, 0.75f, true); // accessOrder = true for LRU
        this.maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, CachedQueryResult> eldest) {
        return size() > maxEntries;
    }
}
