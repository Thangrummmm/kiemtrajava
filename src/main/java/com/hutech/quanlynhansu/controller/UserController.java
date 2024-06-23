package com.hutech.quanlynhansu.controller;

import ch.qos.logback.classic.Logger;
import com.hutech.quanlynhansu.entity.Employee;
import com.hutech.quanlynhansu.entity.User;
import com.hutech.quanlynhansu.security.UserPrincipal;
import com.hutech.quanlynhansu.service.EmployeeService;
import com.hutech.quanlynhansu.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final EmployeeService employeeService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;



    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers(); // Sử dụng phương thức mới trong UserService
        model.addAttribute("listUsers", users);
        return "user/list";
    }

    @GetMapping("/new")
    public String showNewUserForm(@RequestParam(value = "employeeId", required = false) Long employeeId, Model model) {
        User user = new User();
        if (employeeId != null) {
            try {
                Employee employee = employeeService.getEmployeeById(employeeId);
                user.setEmployee(employee);
                user.setRole("EMPLOYEE"); // Thiết lập role mặc định là EMPLOYEE
            } catch (EntityNotFoundException e) {
                model.addAttribute("error", e.getMessage());
                return "error"; // Trả về trang lỗi nếu không tìm thấy nhân viên
            }
        }
        model.addAttribute("user", user);
        return "user/new";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult result,
                           @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors() || !user.getPassword().equals(confirmPassword)) {
                if (!user.getPassword().equals(confirmPassword)) {
                    redirectAttributes.addFlashAttribute("passwordError", "Mật khẩu không khớp");
                }
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/users/new?employeeId=" + user.getEmployee().getEmployeeId();
            }

            // Kiểm tra xem username đã tồn tại chưa
            if (userService.getUserByUsername(user.getUsername()) != null) {
                redirectAttributes.addFlashAttribute("usernameError", "Tên đăng nhập đã tồn tại");
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/users/new?employeeId=" + user.getEmployee().getEmployeeId();
            }

            // Mã hóa mật khẩu
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Thiết lập role mặc định nếu không có employeeId
            if (user.getEmployee() == null) {
                user.setRole("EMPLOYEE");
            }

            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("message", "Thêm người dùng thành công!");
            return "redirect:/users"; // Chuyển hướng về trang danh sách người dùng
        } catch (Exception e) {
            // Ghi log lỗi
            log.error("Error saving user", e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm người dùng.");
            return "redirect:/users/new"; // Chuyển hướng về form thêm người dùng với thông báo lỗi
        }
    }
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            // Xử lý trường hợp không tìm thấy người dùng
            model.addAttribute("error", "Không tìm thấy người dùng.");
            return "error";
        }
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") @Valid User user,
                             BindingResult result,
                             @RequestParam("confirmPassword") String confirmPassword,
                             RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra xem người dùng tồn tại
            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng.");
                return "redirect:/users";
            }

            // Kiểm tra validation của dữ liệu nhập vào
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/users/edit/" + id;
            }

            // Kiểm tra mật khẩu (nếu có thay đổi)
            if (!user.getPassword().isEmpty()) {
                if (!user.getPassword().equals(confirmPassword)) {
                    redirectAttributes.addFlashAttribute("passwordError", "Mật khẩu không khớp.");
                    redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
                    redirectAttributes.addFlashAttribute("user", user);
                    return "redirect:/users/edit/" + id;
                }
                // Mã hóa mật khẩu mới
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            // Cập nhật thông tin người dùng
            existingUser.setUsername(user.getUsername());
            existingUser.setRole(user.getRole());
            // Cập nhật các trường khác của User (nếu cần)

            // Kiểm tra xem username đã tồn tại chưa (trừ trường hợp username không thay đổi)
            if (!existingUser.getUsername().equals(user.getUsername()) && userService.getUserByUsername(user.getUsername()) != null) {
                redirectAttributes.addFlashAttribute("usernameError", "Tên đăng nhập đã tồn tại.");
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/users/edit/" + id;
            }

            userService.updateUser(existingUser); // Lưu lại user đã cập nhật

            redirectAttributes.addFlashAttribute("message", "Cập nhật người dùng thành công!");
            return "redirect:/users";
        } catch (Exception e) {
            log.error("Error updating user with id " + id, e); // Ghi log lỗi
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật người dùng.");
            return "redirect:/users/edit/" + id;
        }
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
