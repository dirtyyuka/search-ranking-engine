package com.engine.searchranking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.engine.searchranking.dto.SearchResult;
import com.engine.searchranking.service.SearchService;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/docs")
    public ResponseEntity<List<SearchResult>> getAllDocs() {
        // This endpoint can be implemented to return all indexed documents if needed
        return ResponseEntity.ok(searchService.getAllDocs());
    }
    
    // Endpoint: /search?q=example&page=0&pageSize=10
    @GetMapping("/search")
    public ResponseEntity<List<SearchResult>> search(
        @RequestParam String query,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        List<SearchResult> results = searchService.search(query, page, pageSize);
        return ResponseEntity.ok(results);
    }

    // Endpoint: /index?docId=1&body=example
    @PostMapping("/index")
    public ResponseEntity<String> index(
        @RequestParam String docId,
        @RequestParam String title,
        @RequestParam String body
    ) {
        searchService.index(docId, title, body);
        return ResponseEntity.ok("Document indexed successfully");
    }
}
