package com.example.Shop.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {
	@GetMapping("/swagger-ui")
	public String swaggerUI() {
		return "redirect:/swagger-ui/index.html";
	}
}