package br.com.joao.projeto.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@org.springframework.stereotype.Controller
public class Controller {

    @RequestMapping("/")
    @ResponseBody
    public String hello(){
        return "hello word";
    }
}
