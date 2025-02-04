 package com.contact.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.Repository.Crepo;
import com.contact.entity.User;
import com.contact.helper.Message;

@Controller
public class ContactController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private Crepo Crepo;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Contact Page");
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Contact Page");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Signup - Contact Page");
		model.addAttribute("user", new User());
		return "signup";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(@javax.validation.Valid @ModelAttribute("user") User user, BindingResult result1,

			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {
		System.out.println(agreement);

		try {
			if (!agreement) {
				System.out.println("not agreed terms and conditions");
				throw new Exception("not agreed terms and conditions");
			}
			if (result1.hasErrors()) {
				System.out.println("ERROR " + result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}
		user.setRole("ROLE_USER");
//			user.setAbout("About_User");

			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			System.out.println("Agreement " + agreement);
			System.out.println(user);

			User result = this.Crepo.save(user);

			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Success", "alert-success"));
			return "signup";

		} catch (Exception e) {

			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went Wrong, " + e.getMessage(), "alert-danger"));
			return "signup";
		}
	}
	@RequestMapping("/index")
	public String dashboard() {
	    return "user_dash";
	}

	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title", "Login Page");
		return "login";
	}
}