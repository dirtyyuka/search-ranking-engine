package com.engine.searchranking.core;

import java.util.*;

import org.springframework.stereotype.Component;

import com.engine.searchranking.core.document.Document;
import com.engine.searchranking.core.index.InvertedIndex;
import com.engine.searchranking.core.rank.Ranker;
import com.engine.searchranking.core.tokenize.Tokenizer;
import com.engine.searchranking.dto.SearchResult;

@Component
public class SearchEngine {

    private final Tokenizer tokenizer = new Tokenizer();
    private final InvertedIndex index = new InvertedIndex();
    private final Ranker ranker = new Ranker();
    private final Map<String, Document> documents = new HashMap<>();
    private final Map<String, Integer> docLengths = new HashMap<>();

    // doc counter
    private int docCount = 0;
    private double avgDocLength = 0;

    public void index(Document doc) {
        documents.put(doc.id(), doc);
        List<String> tokens = tokenizer.tokenize(doc.body());
        
        // add to index and store doc length
        index.add(doc, tokens);
        docLengths.put(doc.id(), tokens.size());

        // increment doc count
        docCount++;
        avgDocLength = (avgDocLength * (docCount - 1) + tokens.size()) / docCount; // update avg doc length
    }

    // return ranked docs
    public List<SearchResult> search(String query, int page, int pageSize) {
        var tokens = tokenizer.tokenize(query);
        var scores = ranker.rank(tokens, index, docCount, docLengths, avgDocLength);

        int topK = (page + 1) * pageSize;
        PriorityQueue<SearchResult> pq = new PriorityQueue<>(Comparator.comparingDouble(SearchResult::score));
        for (var entry : scores.entrySet()) {
            if (pq.size() < topK) {
                pq.offer(new SearchResult(documents.get(entry.getKey()), entry.getValue()));
            } else if (entry.getValue() > pq.peek().score()) {
                pq.poll();
                pq.offer(new SearchResult(documents.get(entry.getKey()), entry.getValue()));
            }
        }
        // sort and paginate results
        List<SearchResult> results = new ArrayList<>(pq);
        results.sort(Comparator.comparingDouble(SearchResult::score).reversed());
        return results.subList(page * pageSize, Math.min(results.size(), (page + 1) * pageSize));
    }

    public List<SearchResult> getAllDocs() {
        List<SearchResult> results = new ArrayList<>();
        for (Document doc : documents.values()) {
            results.add(new SearchResult(doc, 0)); // score is 0 for all docs
        }
        return results;
    }
}
