package com.example.admin_management.model;

public class Role {

    private Integer Id;
    private String Name;
    private String Memo;

    public Integer getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getMemo() {
        return Memo;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }
}
