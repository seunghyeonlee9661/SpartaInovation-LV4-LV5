package com.example.Sparta.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
public class LectureViewController {

    /* 로그인 페이지 */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /* 강의 및 강사 페이지 */
    @GetMapping("/lectures")
    public String mainPage(@RequestParam(value="page", defaultValue="0") int page,
                           @RequestParam(value="category", defaultValue="") String category,
                           @RequestParam(value="option", defaultValue="") String option,
                           @RequestParam(value="desc", defaultValue= "true") boolean desc,
                           Model model) {
        model.addAttribute("page",page);
        model.addAttribute("category",category);
        model.addAttribute("option",option);
        model.addAttribute("desc",desc);
        return "lectures/lectures";
    }

    /* 강의 페이지 */
    @GetMapping("/lecture/{id}")
    public String lecturePage(Model model,@PathVariable("id") int id) {
        return "lectures/lecture";
    }

    /* 강사 페이지 */
    @GetMapping("/teacher/{id}")
    public String teacherPage(Model model,@PathVariable("id") int id) {
        return "lectures/teacher";
    }
}
