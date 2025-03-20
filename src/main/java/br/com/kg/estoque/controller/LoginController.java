package br.com.kg.estoque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class LoginController {
    
    @GetMapping("/login")
    public ModelAndView login(HttpSession session){
        session.invalidate();
        return new ModelAndView("login/form-login");
    }
}
