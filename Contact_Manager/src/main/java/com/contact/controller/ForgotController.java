package com.contact.controller;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotController {
	Random random = new Random(1000);

	//email from open handler
	@RequestMapping("/forgot")
	public String openemail() {
		
		return "forget_email_form";
	}
	
	@PostMapping("/sendotp")
	public String sendotp(@RequestParam("email")String email) {
		
		System.out.println("EMAIL "+email);
		
		//generating random otp
		int otp = random.nextInt(9999);
		System.out.println("OTP "+otp);
		
		//send otp to email
		
		
		return "verifyotp";
	}
	
}
