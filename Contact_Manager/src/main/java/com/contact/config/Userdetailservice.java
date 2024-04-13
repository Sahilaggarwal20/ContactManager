package com.contact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contact.entity.User;

public class Userdetailservice implements UserDetailsService {

	@Autowired
	private com.contact.Repository.Crepo Crepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = Crepo.getUserByUserName(username);

		if (user == null) {
			throw new UsernameNotFoundException("Could not found user");
		}
		CUserDetail cuserdetail = new CUserDetail(user);
		return cuserdetail;
	}

}
