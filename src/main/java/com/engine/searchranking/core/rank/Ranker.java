package com.engine.searchranking.core.rank;

import java.util.*;

import com.engine.searchranking.core.index.InvertedIndex;

// Logic behind BM25 ranking:
// - (k1 + 1) numerator defense
// - when tfCount is very large, 
// - denominator to be assumed tf then tf * (k1 + 1) / tf = k1 + 1, 
// - which is the upper bound of tf component, 
// - limiting the influence of term frequency

public class Ranker {

    public Map<String, Double> rank(List<String> queryTokens, InvertedIndex index, int totalDocs, Map<String, Integer> docLengths, double avgDocLength) {
        Map<String, Double> scores = new HashMap<>();

        for (String token : queryTokens) {
            Map<String, Integer> postings = index.getPostings(token);
            if (postings.isEmpty())
                continue; // empty case

            int df = postings.size(); // document frequency
            double idf = Math.log((totalDocs - df + 0.5) / (df + 0.5) + 1); // idf calculation
            double k1 = 2.0;
            double b = 0.75;

            for (Map.Entry<String, Integer> entry : postings.entrySet()) {
                int tfCount = entry.getValue();
                double docLen = docLengths.get(entry.getKey());
                double norm = 1 - b + b * (docLen / avgDocLength); // normalization factor
                double tf = (tfCount * (k1 + 1)) / (tfCount + k1 * norm); // tf calculation
                System.out.println(
                "token=" + token +
                " doc=" + entry.getKey() +
                " count=" + tfCount +
                " idf=" + idf +
                " tf=" + tf +
                " size=" + df + 
                " totalDocs=" + totalDocs
                );
                scores.merge(entry.getKey(), idf * tf, (x, y) -> x + y); // accumulate scores
            }
        }

        return scores;
    }
}
