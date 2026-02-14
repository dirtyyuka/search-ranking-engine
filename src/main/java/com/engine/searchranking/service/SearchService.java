package com.engine.searchranking.service;

import java.util.Map;
import java.util.List;
import java.util.Collections;
import org.springframework.stereotype.Service;

import com.engine.searchranking.core.SearchEngine;
import com.engine.searchranking.core.cache.QueryCache;
import com.engine.searchranking.dto.CachedQueryResult;
import com.engine.searchranking.dto.SearchResult;
import com.engine.searchranking.core.document.Document;

@Service
public class SearchService {

    private static final int CACHE_SIZE = 100; // cache size

    private final SearchEngine searchEngine;
    private final Map<String, CachedQueryResult> queryCache = Collections.synchronizedMap(new QueryCache(CACHE_SIZE));

    public SearchService(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    public List<SearchResult> getAllDocs() {
        return searchEngine.getAllDocs();
    }

    public List<SearchResult> search(String query, int page, int pageSize) {
        // check cache first
        CachedQueryResult cachedResult = queryCache.get(query);
        if (cachedResult != null && cachedResult.getDepth() >= (page + 1) * pageSize) {
            // return cached results if they cover the requested page
            return cachedResult.getResults().subList(page * pageSize,
                    Math.min(cachedResult.getResults().size(), (page + 1) * pageSize));
        }

        // if not in cache, perform search
        List<SearchResult> results = searchEngine.search(query, page, pageSize);
        // cache the results
        if (queryCache.containsKey(query)) {
            queryCache.get(query).update(results);
        } else {
            queryCache.put(query, new CachedQueryResult(results));
        }

        return results;
    }

    public void index(String docId, String title, String body) {
        searchEngine.index(new Document(docId, title, body));
    }
}
