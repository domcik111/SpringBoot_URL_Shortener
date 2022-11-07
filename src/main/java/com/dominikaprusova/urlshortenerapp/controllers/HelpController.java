package com.dominikaprusova.urlshortenerapp.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HelpController {


    //method for returning the help page
    @GetMapping("/help")
    public ModelAndView index() {
        System.out.println("Showing help page");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }
}
