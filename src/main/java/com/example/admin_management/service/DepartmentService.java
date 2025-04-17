package com.example.admin_management.service;

import com.example.admin_management.model.Department;
import com.example.admin_management.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartment() {
        List<Department> deparments = departmentRepository.getAllDepartment();
        return buildDepartmentTree(deparments);
    }

    public List<Department> buildDepartmentTree(List<Department> departments) {
        Map<Integer, Department> map = new HashMap<>();
        List<Department> roots = new ArrayList<>();

        for (Department dep : departments) {
            map.put(dep.getId(), dep);
        }

        for (Department dep : departments) {
            if (dep.getParentId() == null) {
                roots.add(dep);
            } else {
                Department parent = map.get(dep.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dep);
                }
            }
        }

        return roots;
    }

    public Department findDepartmentByName(String departmentName) {
        return departmentRepository.findDepartmentByName(departmentName.trim());
    }

    public int add(Department department) {
        return departmentRepository.add(department);
    }

    public int deleteById(int departmentId) {
        return departmentRepository.delete(departmentId);
    }

    public int update(Department department) {
        return departmentRepository.update(department);
    }

    public boolean isDepartmentUsed(int departmentId) {
        return departmentRepository.isDepartmentUsed(departmentId);
    }

}
