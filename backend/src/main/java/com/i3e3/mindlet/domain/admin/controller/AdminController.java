package com.i3e3.mindlet.domain.admin.controller;

import com.i3e3.mindlet.domain.admin.controller.form.RegisterForm;
import com.i3e3.mindlet.domain.admin.controller.form.LoginForm;
import com.i3e3.mindlet.domain.admin.exception.DuplicateIdException;
import com.i3e3.mindlet.domain.admin.exception.InvalidKeyException;
import com.i3e3.mindlet.domain.admin.exception.PasswordContainIdException;
import com.i3e3.mindlet.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String main(HttpSession session) {
        if (true) {
            return "redirect:/admin/login";
        }

        return "admin/index";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "admin/registerForm";
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute(name = "registerForm") RegisterForm form, BindingResult bindingResult) {
        log.info("register info={}", form);

        /**
         * 1차 에러 확인
         */
        if (bindingResult.hasErrors()) {
            return "admin/registerForm";
        }

        try {
            adminService.register(form.toDto());
        } catch (PasswordContainIdException e) {
            bindingResult.addError(new FieldError("form", "password", e.getMessage()));
        } catch (DuplicateIdException e) {
            bindingResult.addError(new FieldError("form", "id", e.getMessage()));
        } catch (InvalidKeyException e) {
            bindingResult.addError(new FieldError("form", "key", e.getMessage()));
        } catch (Exception e) {
            log.error("서버 예외 발생");
            throw e;
        }
        if (bindingResult.hasErrors()) {
            return "admin/registerForm";
        }

        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "admin/loginForm";
    }
}
