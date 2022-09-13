package com.focus617.webbackendspringboot.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ThymeleafController {

    @GetMapping("/index")
    fun index(model: Model): String {
        model.addAttribute("welcome", "Hello, this is test page for Thymeleaf.")
        return "index"
    }

}