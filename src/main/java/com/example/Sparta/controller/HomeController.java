package com.example.Sparta.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
public class HomeController {

    /* 로그인 페이지 */
    @GetMapping("/")
    public String login() {
        return "main";
    }
}
