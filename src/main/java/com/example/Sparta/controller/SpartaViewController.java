package com.example.Sparta.controller;

import com.example.Sparta.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
public class SpartaViewController {

    /* 로그인 페이지 */
    @GetMapping("/login")
    public String index(Model model) {
        model.addAttribute("menu","backoffice");
        return "login";
    }
//
//    /* 로그인 후 페이지 */
//    @GetMapping("/main")
//    public String board(Model model, HttpServletRequest req,@RequestParam(value="page", defaultValue="0") int page,@RequestParam(value="category", defaultValue="") String category) {
//        model.addAttribute("menu","backoffice");
//        model.addAttribute("user",(User) req.getAttribute("user"));
//        model.addAttribute("page",page);
//        model.addAttribute("category",category);
//        return "main";
//    }
//    /* 강의 페이지 */
//    @GetMapping("/lecture/{id}")
//    public String lecture(Model model,@PathVariable("id") int id) {
//        model.addAttribute("menu","backoffice");
//        return "lecture";
//    }
//
//    /* 강사 페이지 */
//    @GetMapping("/teacher/{id}")
//    public String teacher(Model model,@PathVariable("id") int id) {
//        model.addAttribute("menu","backoffice");
//        return "teacher";
//    }
}
