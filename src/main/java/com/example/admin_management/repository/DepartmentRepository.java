package com.example.admin_management.repository;

import com.example.admin_management.model.Department;
import com.example.admin_management.model.PageResponse;
import com.example.admin_management.model.Role;
import com.example.admin_management.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.admin_management.constants.Constant.PAGE_SIZE;

@Repository
public class DepartmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Department> getAllDepartment() {
        String sql = "SELECT * FROM department ORDER BY Id";

        List<Department> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Department u = new Department();
            u.setId(rs.getInt("Id"));
            u.setName(rs.getString("Name"));
            Integer parentId = rs.getInt("ParentId");
            if (rs.wasNull()) {
                u.setParentId(null);
            } else {
                u.setParentId(parentId);
            }
            u.setMemo(rs.getString("Memo"));
            u.setCreateTime(rs.getTimestamp("CreateTime"));
            return u;
        });
        return users;
    }

    public Department findDepartmentByName(String departmentName) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM department where Name = ?",
                    new Object[]{departmentName},
                    (rs, rowNum) -> {
                        Department u = new Department();
                        u.setId(rs.getInt("Id"));
                        u.setName(rs.getString("Name"));
                        u.setMemo(rs.getString("Memo"));
                        return u;
                    });
        }catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public int add(Department department) {
        String maxIdSql = "SELECT MAX(Id) FROM department";
        Integer maxId = jdbcTemplate.queryForObject(maxIdSql, Integer.class);
        int nextId = (maxId != null ? maxId : 0) + 1;
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return jdbcTemplate.update("INSERT INTO department (Id, Name, ParentId, Memo, CreateTime) VALUES (?, ?, ?, ?, ?)",
                nextId, department.getName(), department.getParentId(), department.getMemo(), dateTimeNow);
    }

    public int delete(int departmentId) {
        return jdbcTemplate.update("DELETE FROM department WHERE Id = ?", departmentId);
    }

    public int update(Department department) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM department WHERE Id = ?",
                    new Object[]{department.getId()},
                    Integer.class
            );
            if(count != null && count > 0) {
                return jdbcTemplate.update("UPDATE department set Name =?, Memo =?, ParentId =? where Id=?", department.getName(), department.getMemo(), department.getParentId(), department.getId());
            }else {
                return -1;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isDepartmentUsed(Integer departmentId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM user WHERE DepartmentId = ?",
                new Object[]{departmentId},
                Integer.class
        );
        return count != null && count > 0;
    }

}
