package com.engine.searchranking.dto;

import com.engine.searchranking.core.document.Document;

public record SearchResult(Document document, double score) {
}
