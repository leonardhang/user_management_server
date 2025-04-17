package com.example.admin_management.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class TreeResponse<T> {

    private int id;
    private String name;
    private List<T> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
