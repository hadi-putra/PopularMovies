package com.example.android.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msk-1196 on 6/19/17.
 */

public class ResponseApi<E> {
    private int page;
    private int totalResults;
    private int totalPages;
    private List<E> results;

    public ResponseApi() {
        results = new ArrayList<>();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<E> getResults() {
        return results;
    }

    public void setResults(List<E> results) {
        this.results = results;
    }
}
