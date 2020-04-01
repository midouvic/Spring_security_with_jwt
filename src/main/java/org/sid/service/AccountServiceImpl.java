package org.sid.service;

import org.sid.dao.RoleRepository;
import org.sid.dao.UserRepository;
import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class AccountServiceImpl implements AccountService{
    
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcpe;
	
	@Override
	public AppUser saveUser(AppUser user) {
		user.setPassword(bcpe.encode(user.getPassword()));
		return userRepository.save(user);
		 
	}

	@Override
	public AppRole saveRole(AppRole role) {
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String userName, String roleName) {
		AppUser user =userRepository.findByUserName(userName);
		AppRole role=roleRepository.findByRoleName(roleName);
		user.getRoles().add(role);
		
	}

	@Override
	public AppUser findUserByUsername(String userName) {
		
		return userRepository.findByUserName(userName);
	}

}
