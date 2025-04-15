package com.example.admin_management.service;

import com.example.admin_management.model.PageResponse;
import com.example.admin_management.model.Role;
import com.example.admin_management.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public PageResponse<Role> getRoleListByPage(int pageIndex) {
        return roleRepository.getRoleListByPage(pageIndex);
    }

    public int add(Role role) {
        return roleRepository.add(role);
    }

    public int deleteById(Integer roleId) {
        return roleRepository.deleteById(roleId);
    }

    public int update(Role role) {
        return roleRepository.update(role);
    }

}
