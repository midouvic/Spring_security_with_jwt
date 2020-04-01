package org.sid.service;

import java.util.ArrayList;
import java.util.Collection;

import org.sid.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
   @Autowired
   private AccountService accountService;
   
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		AppUser user=accountService.findUserByUsername(userName);
		if(user==null) throw new UsernameNotFoundException(userName);
		
		Collection<GrantedAuthority> authorities=new ArrayList<>();
		
		user.getRoles().forEach(r->authorities.add(new SimpleGrantedAuthority(r.getRoleName())));
		
		return new User(user.getUserName(),user.getPassword(),authorities);
	}

}
