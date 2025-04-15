package com.example.admin_management.repository;

import com.example.admin_management.model.PageResponse;
import com.example.admin_management.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.example.admin_management.constants.Constant.PAGE_SIZE;

@Repository
public class RoleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PageResponse<Role> getRoleListByPage(int pageIndex) {
        String sql = "SELECT * FROM role ORDER BY Id LIMIT ? OFFSET ?";
        String countSql = "SELECT COUNT(Id) FROM role";

        int offset = (pageIndex - 1) * PAGE_SIZE;
        List<Role> roles = jdbcTemplate.query(sql, new Object[]{PAGE_SIZE, offset}, (rs, rowNum) -> {
            Role u = new Role();
            u.setId(rs.getInt("Id"));
            u.setName(rs.getString("Name"));
            u.setMemo(rs.getString("Memo"));
            return u;
        });
        Integer totals = jdbcTemplate.queryForObject(countSql, Integer.class);
        int total = totals != null ? totals : 0;
        return new PageResponse<Role>(roles, total);
    }

    public int add(Role role) {
        String maxIdSql = "SELECT MAX(Id) FROM role";
        Integer maxId = jdbcTemplate.queryForObject(maxIdSql, Integer.class);
        int nextId = (maxId != null ? maxId : 0) + 1;
        return jdbcTemplate.update("INSERT INTO role (Id, Name, Memo) VALUES (?, ?, ?)",
                nextId, role.getName(), role.getMemo());
    }

    public int deleteById(Integer id) {
        return jdbcTemplate.update("DELETE FROM role WHERE Id = ?", id);
    }

    public int update(Role role) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM role WHERE Id = ?",
                    new Object[]{role.getId()},
                    Integer.class
            );
            if(count != null && count > 0) {
                return jdbcTemplate.update("UPDATE role set Name =?, Memo =? where Id=?", role.getName(), role.getMemo(), role.getId());
            }else {
                return -1;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
