package com.engine.searchranking.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import org.junit.jupiter.api.Test;

import com.engine.searchranking.core.tokenize.Tokenizer;


public class TokenizerTest {
    
    @Test
    void shouldTokenizeSimpleSentences() {
        Tokenizer tokenizer = new Tokenizer();

        List<String> tokens = tokenizer.tokenize("Hello World");

        assertEquals(List.of("hello", "world"), tokens);
    }

    @Test
    void shouldRemovePunctuationsAndLowercase() {
        Tokenizer tokenizer = new Tokenizer();

        List<String> tokens = tokenizer.tokenize("Dijkstra's Algorithm");

        assertEquals(List.of("dijkstra", "s", "algorithm"), tokens);
    }
}
