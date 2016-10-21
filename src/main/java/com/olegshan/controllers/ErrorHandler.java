package com.olegshan.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by olegshan on 20.10.2016.
 */
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public String exception(Exception e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "exception";
    }
}
