package org.sid.web;

import java.util.List;

import org.sid.dao.UserRepository;
import org.sid.entities.AppUser;
import org.sid.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRestController {
	@Autowired 
	private AccountService accountService;
	@Autowired
	private UserRepository userRepository;
	
//Ajouter un utilisateur	
	
	@PostMapping("/register")
	public AppUser registe(@RequestBody RegisterForm userForm) {
		if(!userForm.getPassword().equals(userForm.getRepassword())) 
			throw new RuntimeException("you must confirm your password");
		AppUser us=accountService.findUserByUsername(userForm.getUsername());
		if (us!=null) 
			throw new RuntimeException("user already exist");
		AppUser user=new AppUser();
		user.setUserName(userForm.getUsername());
		user.setPassword(userForm.getPassword());
		
		 accountService.saveUser(user);
		accountService.addRoleToUser(user.getUserName(), "USER");
		return user;
	}
//Afficher tous les utilisateurs	
	
	@GetMapping("/register")
	public List<AppUser> allUser() {
		return userRepository.findAll();
	}

}
