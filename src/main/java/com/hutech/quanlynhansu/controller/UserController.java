package com.hutech.quanlynhansu.controller;

import com.hutech.quanlynhansu.entity.User;
import com.hutech.quanlynhansu.security.UserPrincipal;
import com.hutech.quanlynhansu.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers(); // Sử dụng phương thức mới trong UserService
        model.addAttribute("listUsers", users);
        return "user/list";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user/new_user";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "user/new_user";
        }

        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id); // Sử dụng phương thức mới trong UserService
        model.addAttribute("user", user);
        return "user/edit_user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            user.setUserId(id);
            return "user/edit_user";
        }
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            Model model) {
        try {
            // Tạo đối tượng Authentication
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

            // Xác thực thông tin đăng nhập
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Lưu thông tin xác thực vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/"; // Chuyển hướng đến trang chủ sau khi đăng nhập thành công

        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password");
            return "user/login";
        }
    }

    @GetMapping("/logout")
    public String logoutUser(Authentication authentication) {
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/users/login";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            User user = principal.getUser();
            model.addAttribute("user", user);
            return "user/profile";
        }
        return "redirect:/users/login";
    }
}
