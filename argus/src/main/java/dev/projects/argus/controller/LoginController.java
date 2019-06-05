package dev.projects.argus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String pagelogin() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(Model model, String error, String logout) {
		if(error != null) {
			model.addAttribute("msg", "Email ou Senha inválidos");
		}
		if(logout != null) {
			model.addAttribute("msg", "Desconectado");
		}
		
		return "/login";
	}

}
