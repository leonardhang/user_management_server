package com.example.admin_management.controller;

import com.example.admin_management.model.Department;
import com.example.admin_management.model.Role;
import com.example.admin_management.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    @Operation(summary = "获取所有部门", description = "获取所有部门")
    public ResponseEntity<?> getDepartments() {
        List<Department> result = departmentService.getAllDepartment();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "创建部门", description = "创建部门")
    public ResponseEntity<?> createDepartments(@RequestBody Department department) {
        Department existDepartment = departmentService.findDepartmentByName(department.getName());
        if(existDepartment != null) {
            return ResponseEntity.badRequest().body("部门已存在");
        }
        int result = departmentService.add(department);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门", description = "根据部门Id删除部门")
    public ResponseEntity<?> deleteDepartment(@PathVariable Integer id) {
        boolean isUsed = departmentService.isDepartmentUsed(id);
        if(isUsed) {
            return ResponseEntity.badRequest().body("部门已被使用，无法删除");
        }
        int result = departmentService.deleteById(id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "更新部门", description = "更新部门信息")
    public ResponseEntity<?> updateDepartment(@RequestBody Department department) {
        Department existDepartment = departmentService.findDepartmentByName(department.getName());
        if(existDepartment != null && !Objects.equals(existDepartment.getId(), department.getId())) {
            return ResponseEntity.badRequest().body("部门名已存在");
        }
        int result = departmentService.update(department);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

}
