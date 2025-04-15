package com.example.admin_management.controller;

import com.example.admin_management.model.PageResponse;
import com.example.admin_management.model.Role;
import com.example.admin_management.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/page/{pageIndex}")
    @Operation(summary = "分页查询角色列表", description = "页码：pageIndex,分页查询")
    public PageResponse<Role> getRoleListByPage(@PathVariable int pageIndex) {
        return roleService.getRoleListByPage(pageIndex);
    }

    @PostMapping
    @Operation(summary = "创建角色", description = "创建角色")
    public int createRole(@RequestBody Role role) {
        return roleService.add(role);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "根据角色Id删除角色")
    public int deleteRole(@PathVariable Integer id) {
        return roleService.deleteById(id);
    }

    @PutMapping
    @Operation(summary = "更新角色", description = "更新角色信息")
    public int updateRole(@RequestBody Role role) {
        return roleService.update(role);
    }

}
