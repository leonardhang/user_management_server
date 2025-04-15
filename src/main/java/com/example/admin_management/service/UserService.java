package com.example.admin_management.service;

import com.example.admin_management.model.PageResponse;
import com.example.admin_management.model.User;
import com.example.admin_management.model.UserFilterOption;
import com.example.admin_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(String id) {
        return userRepository.findById(id);
    }

    public PageResponse<User> getUserListByPage(int pageIndex) {
        return userRepository.getUserListByPage(pageIndex);
    }

    public User findUserByName(String name) {
        return userRepository.findByUserName(name);
    }

    public int createUser(User user) {
        return userRepository.add(user);
    }

    public int deleteUser(String id) {
        return userRepository.deleteById(id);
    }

    public boolean isExitUserByName(String name) {
        return userRepository.findByUserName(name) != null;
    }

    public int updateUser(User user) {
        return userRepository.update(user);
    }

    public PageResponse<User> getFilterUserList(UserFilterOption filterOption) {
        return userRepository.getFilterUserList(filterOption);
    }

}

