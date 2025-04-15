package com.example.admin_management.model;

import java.util.List;

public class PageResponse<T> {

    private final int totals;

    private final List<T> data;

    public PageResponse(List<T> data, int totals) {
        this.totals = totals;
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public int getTotals() {
        return totals;
    }
}
