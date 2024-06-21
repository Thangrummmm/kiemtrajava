package com.hutech.quanlynhansu.service;

import com.hutech.quanlynhansu.entity.User;
import com.hutech.quanlynhansu.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService; // Sử dụng UserService để lấy thông tin người dùng

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Lấy thông tin người dùng từ UserService
        User user = userService.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username); // Ném ngoại lệ nếu không tìm thấy
        }

        return new UserPrincipal(user); // Trả về UserPrincipal (chứa thông tin UserDetails)
    }
}
