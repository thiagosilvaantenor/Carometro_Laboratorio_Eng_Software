package br.com.carometro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class InicioController {

	
	@GetMapping("/")
    public ModelAndView paginaInicio() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("inicio/inicio");
        return modelAndView;
    }
}
