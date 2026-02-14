package com.engine.searchranking.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class CachedQueryResult {
    private List<SearchResult> results;
    private int depth; // how many results are cached for this query

    public CachedQueryResult(List<SearchResult> results) {
        this.results = results;
        this.depth = results.size();
    }

    public void update(List<SearchResult> newResults) {
        this.results = newResults;
        this.depth = newResults.size();
    }
}
