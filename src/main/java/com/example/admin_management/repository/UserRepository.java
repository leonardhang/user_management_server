package com.example.admin_management.repository;

import com.example.admin_management.model.PageResponse;
import com.example.admin_management.model.User;
import com.example.admin_management.model.UserFilterOption;
import com.example.admin_management.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.admin_management.constants.Constant.DEFAULT_NEW_USER_PASSWORD;
import static com.example.admin_management.constants.Constant.PAGE_SIZE;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM user", (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getString("Id"));
            u.setName(rs.getString("Name"));
            u.setNickName(rs.getString("NickName"));
            u.setEmail(rs.getString("Email"));
            return u;
        });
    }

    public PageResponse<User> getUserListByPage(int pageIndex) {
        String sql = "SELECT * FROM user u left join department p on u.DepartmentId=p.Id ORDER BY u.Name, u.CreateTime LIMIT ? OFFSET ?";
        String countSql = "SELECT COUNT(Id) FROM user";

        int offset = (pageIndex - 1) * PAGE_SIZE;
        List<User> users = jdbcTemplate.query(sql, new Object[]{PAGE_SIZE, offset}, (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getString("u.Id"));
            u.setName(rs.getString("u.Name"));
            u.setNickName(rs.getString("NickName"));
            u.setEmail(rs.getString("Email"));
            u.setPhoneNumber(rs.getString("PhoneNumber"));
            u.setStatus(rs.getInt("Status"));
            u.setGender(rs.getInt("Gender"));
            u.setBirthday(rs.getDate("Birthday"));
            u.setCreateTime(rs.getTimestamp("CreateTime"));
            int departmentId = rs.getInt("DepartmentId");
            if (rs.wasNull()) {
                u.setDepartmentId(null);
            } else {
                u.setDepartmentId(departmentId);
            }
            u.setDepartmentName(rs.getString("p.Name"));
            return u;
        });
        int total = jdbcTemplate.queryForObject(countSql, Integer.class);
        return new PageResponse<User>(users, total);
    }

    public User findById(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM user WHERE id = ?",
                new Object[]{id},
                (rs, rowNum) -> {
                    User u = new User();
                    u.setId(rs.getString("Id"));
                    u.setName(rs.getString("Name"));
                    u.setNickName(rs.getString("NickName"));
                    u.setEmail(rs.getString("Email"));
                    return u;
                });
    }

    public User findByUserName(String userName) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM user WHERE Name = ?",
                    new Object[]{userName},
                    (rs, rowNum) -> {
                        User u = new User();
                        u.setId(rs.getString("Id"));
                        u.setName(rs.getString("Name"));
                        u.setNickName(rs.getString("NickName"));
                        u.setEmail(rs.getString("Email"));
                        u.setPassword(rs.getString("Password"));
                        u.setStatus(rs.getInt("Status"));
                        return u;
                    });
        }catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }

    public int add(User user) {
        String password = user.getPassword();
        String encodePassword = PasswordUtil.encodePassword(password.isEmpty() ? DEFAULT_NEW_USER_PASSWORD : password);
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return jdbcTemplate.update("INSERT INTO user (Id, Name, NickName, PhoneNumber, Email, Password, Birthday, Gender, Status, DepartmentId, CreateTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                UUID.randomUUID().toString(), user.getName(), user.getNickName(), user.getPhoneNumber(), user.getEmail(), encodePassword, user.getBirthday(), user.getGender(), 1, user.getDepartmentId(), dateTimeNow);
    }

    public int deleteById(String id) {
        return jdbcTemplate.update("DELETE FROM user WHERE id = ?", id);
    }

    public int update(User user) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM user WHERE Id = ?",
                    new Object[]{user.getId()},
                    Integer.class
            );
            if(count > 0) {
                return jdbcTemplate.update("UPDATE user set Name =?, NickName =?, PhoneNumber=?, Gender=?, Birthday=?, Status=?, DepartmentId=? where Id=?", user.getName(), user.getNickName(),
                        user.getPhoneNumber(), user.getGender(), user.getBirthday(), user.getStatus(), user.getDepartmentId(), user.getId());
            }else {
                return -1;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PageResponse<User> getFilterUserList(UserFilterOption filterOption) {

        StringBuilder sql = new StringBuilder("SELECT * FROM user u left join department p on u.DepartmentId = p.Id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (filterOption.getSearchText() != null && !filterOption.getSearchText().isEmpty()) {
            sql.append(" AND (u.Name LIKE ? OR u.NickName LIKE ? OR u.PhoneNumber LIKE ?)");
            String likePattern = "%" + filterOption.getSearchText() + "%";
            params.add(likePattern);
            params.add(likePattern);
            params.add(likePattern);
        }
        if(filterOption.getGender() != -1) {
            sql.append(" AND u.Gender = ?");
            params.add(filterOption.getGender());
        }
        int offset = (filterOption.getPage() - 1) * PAGE_SIZE;
        sql.append(" ORDER BY u.Name, u.CreateTime LIMIT ? OFFSET ?");
        params.add(PAGE_SIZE);
        params.add(offset);

        List<User> users = jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getString("u.Id"));
            u.setName(rs.getString("u.Name"));
            u.setNickName(rs.getString("NickName"));
            u.setEmail(rs.getString("Email"));
            u.setPhoneNumber(rs.getString("PhoneNumber"));
            u.setStatus(rs.getInt("Status"));
            u.setGender(rs.getInt("Gender"));
            u.setBirthday(rs.getDate("Birthday"));
            u.setCreateTime(rs.getTimestamp("u.CreateTime"));
            int departmentId = rs.getInt("u.DepartmentId");
            if (rs.wasNull()) {
                u.setDepartmentId(null);
            } else {
                u.setDepartmentId(departmentId);
            }
            u.setDepartmentName(rs.getString("p.Name"));
            return u;
        });
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM user WHERE 1=1");
        List<Object> countParams = new ArrayList<>();
        if (filterOption.getSearchText() != null && !filterOption.getSearchText().isEmpty()) {
            countSql.append(" AND (Name LIKE ? OR NickName LIKE ? OR PhoneNumber LIKE ?)");
            String likePattern = "%" + filterOption.getSearchText() + "%";
            countParams.add(likePattern);
            countParams.add(likePattern);
            countParams.add(likePattern);
        }
        if(filterOption.getGender() != -1) {
            countSql.append(" AND Gender = ?");
            countParams.add(filterOption.getGender());
        }
        Integer total = jdbcTemplate.queryForObject(countSql.toString(), countParams.toArray(), Integer.class);

        return new PageResponse<User>(users, total);
    }

}
