package com.example.Sparta.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
public class GoodsViewController {

    /* 강의 및 강사 페이지 */
    @GetMapping("/goods")
    public String mainPage(@RequestParam(value="page", defaultValue="0") int page,
                            @RequestParam(value="option", defaultValue="") String option,
                           @RequestParam(value="desc", defaultValue= "true") boolean desc,
                           Model model) {
        model.addAttribute("page",page);
        model.addAttribute("option",option);
        model.addAttribute("desc",desc);
        return "goods/goods";
    }

    /* 강의 페이지 */
    @GetMapping("/product/{id}")
    public String lecturePage(Model model,@PathVariable("id") int id) {
        return "goods/product";
    }

    /* 강사 페이지 */
    @GetMapping("/cart")
    public String teacherPage() {
        return "goods/cart";
    }
}
