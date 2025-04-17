package com.example.admin_management.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Department {

    private Integer id;
    private Integer parentId;
    private String name;
    private String memo;
    private LocalDateTime createTime;
    private List<Department> children;

    public Department() {
        this.children = new java.util.ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        if(createTime != null) {
            this.createTime = createTime.toLocalDateTime();
        }
    }

    public List<Department> getChildren() {
        return children;
    }

    public void setChildren(List<Department> children) {
        this.children = children;
    }
}
