package com.contact.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "USER")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@NotBlank(message = "Required!!")
	private String Name;

	@Column(unique = true)
	@NotBlank(message = "Required!!")
	private String Email;

	@NotBlank(message = "Required!!")
	private String Password;

	@NotBlank(message = "Required!!")
	private String Role;

	private boolean enabled;

	@NotBlank(message = "Required!!")
	private String About;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Contact> contacts = new ArrayList<>();

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}

	public String getAbout() {
		return About;
	}

	public void setAbout(String about) {
		About = about;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", Name=" + Name + ", Email=" + Email + ", Password=" + Password + ", Role=" + Role
				+ ", enabled=" + enabled + ", About=" + About + ", contacts=" + contacts + "]";
	}

}
