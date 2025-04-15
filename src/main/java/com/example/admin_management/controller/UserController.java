package com.example.admin_management.controller;

import com.example.admin_management.model.PageResponse;
import com.example.admin_management.model.User;
import com.example.admin_management.model.UserFilterOption;
import com.example.admin_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "获取所有用户", description = "获取所有用户列表")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户", description = "根据ID查询用户信息")
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @GetMapping("/page/{pageIndex}")
    @Operation(summary = "分页查询用户列表", description = "页码：pageIndex,分页查询")
    public PageResponse<User> getUserListByPage(@PathVariable int pageIndex) {
        return userService.getUserListByPage(pageIndex);
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "后台创建用户")
    public int createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户Id删除用户")
    public int deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

    @PutMapping
    @Operation(summary = "更新用户", description = "更新用户信息")
    public int updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PostMapping("/filter")
    @Operation(summary = "过滤查询用户列表", description = "参数：searchText, gender, pageIndex")
    public PageResponse<User> getFilterUserList(@RequestBody UserFilterOption filterOption) {
        return userService.getFilterUserList(filterOption);
    }

}

