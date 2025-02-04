package com.contact.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long cid;
	private Long Phone;
	private String Name;
	private String Description;
	private String Work;
	private String Email;
	private String Imageurl;

	@ManyToOne
	private User user;

	public Contact() {
		super();

	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Long getPhone() {
		return Phone;
	}

	public void setPhone(Long phone) {
		Phone = phone;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getWork() {
		return Work;
	}

	public void setWork(String work) {
		Work = work;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getImageurl() {
		return Imageurl;
	}

	public void setImageurl(String imageurl) {
		Imageurl = imageurl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	/*
	 * @Override public String toString() { return "Contact [cid=" + cid +
	 * ", Phone=" + Phone + ", Name=" + Name + ", Description=" + Description +
	 * ", Work=" + Work + ", Email=" + Email + ", Imageurl=" + Imageurl + ", user="
	 * + user + "]"; }
	 * 
	 * @Override public boolean equals(Object obj) { return
	 * this.cid==((Contact)obj).getCid(); }
	 */
}