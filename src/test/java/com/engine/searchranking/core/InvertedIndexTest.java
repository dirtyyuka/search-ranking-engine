package com.engine.searchranking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import org.junit.jupiter.api.Test;

import com.engine.searchranking.core.document.Document;
import com.engine.searchranking.core.index.InvertedIndex;
import com.engine.searchranking.core.tokenize.Tokenizer;

public class InvertedIndexTest {

    // intialize objects
    InvertedIndex index = new InvertedIndex();
    Tokenizer tokenizer = new Tokenizer();
    Document doc = new Document("1", "Test1", "This is a test for proper token counting");

    // add tokens
    List<String> tokens = tokenizer.tokenize(doc.body());

    @Test
    void shouldReturnPostingForExistingToken() {
        index.add(doc, tokens);

        // check validity
        Map<String, Integer> postings = index.getPostings("test");
        assertEquals(Map.of("1", 1), postings);
    }

    @Test
    void shouldReturnEmptyMapForMissingToken() {
        index.add(doc, tokens);

        Map<String, Integer> postings = index.getPostings("anyhow");
        assertTrue(postings.isEmpty());
    }
}
