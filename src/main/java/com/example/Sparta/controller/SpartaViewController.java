package com.example.Sparta.controller;

import com.example.Sparta.entity.User;
import com.example.Sparta.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
public class SpartaViewController {

    /* 로그인 페이지 */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /* 메인 */
    @GetMapping("/")
    public String main(@RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="category", defaultValue="") String category, Model model) {
        model.addAttribute("page",page);
        model.addAttribute("category",category);
        return "main";
    }

    /* 강의 페이지 */
    @GetMapping("/lecture/{id}")
    public String lecture(Model model,@PathVariable("id") int id) {
        return "lecture";
    }

    /* 강사 페이지 */
    @GetMapping("/teacher/{id}")
    public String teacher(Model model,@PathVariable("id") int id) {
        return "teacher";
    }
}
