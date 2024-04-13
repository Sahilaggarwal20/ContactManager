package com.contact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class Config extends WebSecurityConfigurerAdapter {

	@Bean
	public Userdetailservice getUserdetailservice() {
		return new Userdetailservice();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserdetailservice());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.authenticationProvider(authenticationProvider());
	}

	/* @Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("USER")
				.antMatchers("/**").permitAll().and().formLogin().and().csrf().disable();
	} */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.authorizeRequests()
	        .antMatchers("/admin/**").hasRole("ADMIN")
	        .antMatchers("/user/**").hasRole("USER")
	        .antMatchers("/**").permitAll()
	        .and()
	        .formLogin().loginPage("/signin")
	        .loginProcessingUrl("/dologin")
	        .defaultSuccessUrl("/user/index")
	        .and()
	        .csrf().disable();
	}

	}


