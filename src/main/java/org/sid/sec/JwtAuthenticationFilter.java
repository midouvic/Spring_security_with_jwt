package org.sid.sec;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sid.entities.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}
//Authentification de l'utilisateur	
@Override
public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
	AppUser user=null;
	try {
		user=new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
	} catch (Exception e) {
		new RuntimeException(e);
	} 
	System.out.println("*******************");
	System.out.println("userName= "+user.getUserName());
	System.out.println("password= "+user.getPassword());
	return authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
}
//Création du JWT 
@Override
protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
	User user=(User) authResult.getPrincipal();
	String jwt=Jwts.builder()
			.setSubject(user.getUsername())
			.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
			.setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
			.claim("roles",user.getAuthorities())
			.compact();
	
	response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+jwt);
	//super.successfulAuthentication(request, response, chain, authResult);
}
}
