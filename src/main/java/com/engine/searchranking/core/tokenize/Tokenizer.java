package com.engine.searchranking.core.tokenize;
import java.util.*;

public class Tokenizer {
    
    public List<String> tokenize(String text) { 
        return Arrays.stream(text.toLowerCase().split("\\W+")).filter(s -> !s.isBlank()).toList();
    }
}
