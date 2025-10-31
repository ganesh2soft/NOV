package com.hcl.springecomapp.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hcl.springecomapp.entity.Users;
import com.hcl.springecomapp.repository.UsersRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	public UserDetailsService userDetailsService;
	@Autowired
	UsersRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
		// Handle preflight request
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			return;
		}
		try {
			String jwt = parseJwt(request);
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);
				//String username = jwtUtils.getEmailFromJwtToken(jwt);
				System.out.println("Username  from JWWWWWWTTTTT" + username);
				Users user = userRepository.findByUserName(username)
						.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
				UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
				//UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				System.out.println("UUUUUUUUUUUUUUUUUUSSSSSSSSSSSSS userdetails" + userDetails.toString());
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		}
		filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
		// Try to get JWT from cookie first
		String jwt = jwtUtils.getJwtFromCookies(request);

		// Fallback to Authorization header (if you also send Bearer tokens)
		if (jwt == null) {
			jwt = jwtUtils.getJwtFromHeader(request);
		}

		logger.debug("Parsed JWT: {}", jwt);
		return jwt;
	}

}
