package com.contact.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contact.Repository.ContactRepo;
import com.contact.Repository.Crepo;
import com.contact.entity.Contact;
import com.contact.entity.User;
import com.contact.helper.Message;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserControl {

	private BCryptPasswordEncoder bcryptpasswordencoder;
	@Autowired
	private Crepo crepo;
	private Object userRepository;

	@Autowired
	private ContactRepo contactrepo;

	@Autowired
	public UserControl(Crepo crepo, BCryptPasswordEncoder bcryptpasswordencoder) {
		this.crepo = crepo;
		this.bcryptpasswordencoder = bcryptpasswordencoder;
	}

	// adding data to common response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {

		String username = principal.getName();
		System.out.println("username: " + username);

		User userByUserName = crepo.getUserByUserName(username);
		System.out.println(userByUserName);

		model.addAttribute("user", userByUserName);
	}

// dashboard	
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		return "Normal/user_dash";
	}

	// open add form controller
	@GetMapping("/add_contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact";
	}

//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		try {
			String name = principal.getName();
			User user = crepo.getUserByUserName(name); // Use the autowired repository directly
			// processing file
			if (file.isEmpty()) {
				// file is empty
				System.out.println("empty");
				contact.setImageurl("default.png");

			} else {
				// upload file
				contact.setImageurl(file.getOriginalFilename());
				File savefile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("uploaded");
			}
			contact.setUser(user);

			user.getContacts().add(contact);
			crepo.save(user); // Save the user using the repository
			System.out.println("Data" + contact);
			System.out.println("Added");

			// successfull message
			session.setAttribute("message", new Message("Your contact is added successfully!!", "success"));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error" + e.getMessage());
			e.printStackTrace();

			// error message
			session.setAttribute("message", new Message("Something Went Wrong!!", "danger"));
		}
		return "normal/add_contact";
	}

	// show contact handler

	@GetMapping("/showContact/{page}")
	public String showContact(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title", "Show User Contacts");

		// bringing the contacts from the database with reference to the user
		String userName = principal.getName();
		User user = crepo.getUserByUserName(userName);

		PageRequest pageable = PageRequest.of(page, 5);
		Page<Contact> contacts = contactrepo.findContactsByUser(user.getId(), pageable);

		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());
		return "Normal/showContact";

	}

	// displaying specific details
	@RequestMapping("/{cid}/contact")
	public String IndividualContact(@PathVariable("cid") Long cid, Model model, Principal principal,
			HttpSession session) {

		Optional<Contact> contactOptional = this.contactrepo.findById(cid);
		Contact contact = contactOptional.get();
		String userName = principal.getName();
		User user = crepo.getUserByUserName(userName);

		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}

		return "normal/contact_detail";

	}

	// delete contact
	@GetMapping("/delete/{cid}")
	public String deletecontact(@PathVariable("cid") Long cid, Model model, Principal principal, HttpSession session) {
		Optional<Contact> contactOptional = this.contactrepo.findById(cid);
		Contact contact = contactOptional.get();
		String userName = principal.getName();
		User user = crepo.getUserByUserName(userName);
		if (user.getId() == contact.getUser().getId()) {

			String imagePath = "static/image/" + contact.getImageurl();
			File imageFile = new File(imagePath);
			if (imageFile.exists()) {
				imageFile.delete();
			}

			user.getContacts().remove(contact);

			crepo.save(user);

			// Delete the contact
			this.contactrepo.delete(contact);
			session.setAttribute("message", new Message("Contact deleted successfully", "success"));

		}

		return "redirect:/user/showContact/0";
	}

	// update
	@PostMapping("/updatecontact/{cid}")
	public String updatecontact(@PathVariable("cid") Long cid, Model m, Principal principal, HttpSession session) {
		Optional<Contact> contactOptional = this.contactrepo.findById(cid);
		Contact contact = contactOptional.get();

		m.addAttribute("contact", contact);
		m.addAttribute("title", "Update Contact");
		/*
		 * String name = principal.getName(); User user = crepo.getUserByUserName(name);
		 * contact.setUser(user); user.getContacts().add(contact); crepo.save(user);
		 * session.setAttribute("message", new
		 * Message("Your contact is updated successfully!!", "success"));
		 */
		return "normal/updatecontact";
	}

	// update contact handler

	@PostMapping("/process-update")
	public String updatehandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Model m, HttpSession session, Principal principal) {

		try {
			Optional<Contact> oldContactOptional = this.contactrepo.findById(contact.getCid());
		    Contact oldContact = oldContactOptional.get();

			if (!file.isEmpty()) {
				
				// delete old ohoto updating old photo
				File deletefile = new ClassPathResource("static/image").getFile();
				File file1 = new File(deletefile,oldContact.getImageurl());
				file1.delete();
				
				// rewrite file updating
				File savefile = new ClassPathResource("static/image").getFile();
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImageurl(file.getOriginalFilename());
				
				
			}
			else{
		        contact.setImageurl(oldContact.getImageurl());
			}
			
			String name = principal.getName();
			User user = crepo.getUserByUserName(name);
			contact.setUser(user);
			this.contactrepo.save(contact);
			session.setAttribute("message", new Message("Your contact is updated successfully!!", "success"));

		} catch (Exception e) {

		}
		// return "redirect:/user/showContact/0";
		return "redirect:/user/"+contact.getCid()+"/contact";
	}
	// your profile
	@GetMapping("/profile")
	public String yourprofile(Model model) {
		model.addAttribute("title", "User Profile");
		return "normal/profile";
	}

	// open handler
	@GetMapping("/settings")
	public String opensetting() {

		return "normal/settings";
	}

	// change password
	@PostMapping("/changepassword")
	public String changepass(@RequestParam("oldpass") String oldpass, @RequestParam("newpass") String newpass,
			Principal principal, HttpSession session) {

		String userName = principal.getName();
		User currentUser = crepo.getUserByUserName(userName);
		if (this.bcryptpasswordencoder.matches(oldpass, currentUser.getPassword())) {

			// change password
			String encodedNewPassword = bcryptpasswordencoder.encode(newpass);
			currentUser.setPassword(encodedNewPassword);
			crepo.save(currentUser);

			session.setAttribute("message", new Message("Your Password Successfully Changed", "success"));

		} else {
			// error
			session.setAttribute("message", new Message("Enter correct old password", "danger"));
			return "redirect:/user/settings";

		}

		return "redirect:/user/index";
	}

}
