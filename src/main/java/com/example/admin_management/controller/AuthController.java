package com.example.admin_management.controller;

import com.example.admin_management.model.*;
import com.example.admin_management.service.UserService;
import com.example.admin_management.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户名，密码。登录成功，生成jwt token")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
            User user = userService.findUserByName(request.getUserName());
            if(user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到用户");
            }
            if(user.getStatus() != 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("用户已停用");
            }
            String token = JwtTokenUtil.generateToken(request.getUserName());
            LoginResponse result = new LoginResponse(user, token);
            return ResponseEntity.ok(result);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }
    }

    @PostMapping("/registry")
    @Operation(summary = "用户注册", description = "用户名，密码。 用户名不可重名，用户状态为正常")
    public ResponseEntity<?> registry(@RequestBody RegistryRequest request) {
        try {
            if(userService.isExitUserByName(request.getUserName())) {
                return ResponseEntity.badRequest().body("用户已存在");
            }
            User u = new User();
            u.setName(request.getUserName());
            u.setPhoneNumber(request.getPhoneNumber());
            u.setPassword(request.getPassword());
            userService.createUser(u);
            return new ResponseEntity<>(u, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("注册失败");
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "token 刷新", description = "token 1天有效期，自动刷新")
    public ResponseEntity<?> refreshToken(@RequestBody String token) {
        try {
            Claims claims = JwtTokenUtil.getClaimsFromToken(token);
            String userName = claims.getSubject();
            if(JwtTokenUtil.isTokenExpired(token)) {
                String refreshToken = JwtTokenUtil.generateToken(userName);
                return new ResponseEntity<>(new TokenResponse(refreshToken), HttpStatus.OK);
            }
            return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无效的token");
        }
    }

}
