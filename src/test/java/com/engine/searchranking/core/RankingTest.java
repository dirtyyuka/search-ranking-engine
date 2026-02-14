package com.engine.searchranking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.engine.searchranking.core.document.Document;
import com.engine.searchranking.core.index.InvertedIndex;
import com.engine.searchranking.core.rank.Ranker;
import com.engine.searchranking.core.tokenize.Tokenizer;

public class RankingTest {

    // intialize objects
    Ranker ranker = new Ranker();
    InvertedIndex index = new InvertedIndex();
    Tokenizer tokenizer = new Tokenizer();

    // docs
    Document doc1 = new Document("1", "Test1", "This is a valid rank test. This test is very valid.");
    Document doc2 = new Document("2", "Test2", "Rank aggregation in search engines");
    // add tokens
    List<String> tokens1 = tokenizer.tokenize(doc1.body());
    List<String> tokens2 = tokenizer.tokenize(doc2.body());

    Map<String, Integer> docLengths = Map.of(
            "1", tokens1.size(),
            "2", tokens2.size()
    );
    double avgDocLength = (tokens1.size() + tokens2.size()) / 2.0; // average doc length

    @Test
    void shouldCheckForValidRanking() {
        index.add(doc1, tokens1);
        index.add(doc2, tokens2);
        Map<String, Double> scores = ranker.rank(List.of("valid"), index, 2, docLengths, avgDocLength);

        // check if rank 
        System.out.println(scores);
        System.out.println(index.getPostings("valid"));
        System.out.println(avgDocLength);
        assertEquals(scores.get("1"), 0.0);
    }

    @Test
    void shouldCheckForMissingToken() {
        index.add(doc1, tokens1);
        index.add(doc2, tokens2);
        Map<String, Double> scores = ranker.rank(List.of("missing"), index, 2, docLengths, avgDocLength);

        // check if missing token is handled
        assertEquals(Map.of(), scores);
    }

    @Test
    void shouldCheckForValidOrder() {
        index.add(doc1, tokens1);
        index.add(doc2, tokens2);
        Map<String, Double> scores = ranker.rank(List.of("rank"), index, 2, docLengths, avgDocLength);

        // check if order is valid
        assertEquals(List.of("1", "2"), scores.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry<String, Double>::getValue).thenComparing(Map.Entry::getKey))
                .map(Map.Entry::getKey)
                .toList());
    }
}
