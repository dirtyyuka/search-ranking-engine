package com.engine.searchranking.core.index;
import java.util.*;

import com.engine.searchranking.core.document.Document;

public class InvertedIndex {
    
    private final Map<String, Map<String, Integer>> index = new HashMap<>();

    // count token frequencies
    public void add(Document doc, List<String> tokens) {
        for (String token: tokens) {
            index
                .computeIfAbsent(token, k -> new HashMap<>())
                .merge(doc.id(), 1, (oldVal, newVal) -> Integer.sum(oldVal, newVal));
            
        }
    }

    // return token frequencies
    public Map<String, Integer> getPostings(String token) {
        return index.getOrDefault(token, Map.of());
    }
}
