package com.example.admin_management.controller;

import com.example.admin_management.model.PageResponse;
import com.example.admin_management.model.Role;
import com.example.admin_management.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        Role existRole = roleService.findRoleByName(role.getName());
        if(existRole != null) {
            return ResponseEntity.badRequest().body("角色名已存在");
        }
        int result = roleService.add(role);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "根据角色Id删除角色")
    public int deleteRole(@PathVariable Integer id) {
        return roleService.deleteById(id);
    }

    @PutMapping
    @Operation(summary = "更新角色", description = "更新角色信息")
    public ResponseEntity<?> updateRole(@RequestBody Role role) {
        Role existRole = roleService.findRoleByName(role.getName());
        if(existRole != null && !Objects.equals(existRole.getId(), role.getId())) {
            return ResponseEntity.badRequest().body("角色名已存在");
        }
        int result = roleService.update(role);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

}
