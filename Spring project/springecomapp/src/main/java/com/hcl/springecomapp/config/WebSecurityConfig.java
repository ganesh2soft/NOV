package com.hcl.springecomapp.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.hcl.springecomapp.entity.AppRole;
import com.hcl.springecomapp.entity.Users;
import com.hcl.springecomapp.repository.UsersRepository;
import com.hcl.springecomapp.jwt.AuthEntryPointJwt;
import com.hcl.springecomapp.jwt.AuthTokenFilter;
import com.hcl.springecomapp.security.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		.cors(cors -> {
		}).exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> 
				auth.requestMatchers("/api/auth/signup", "/api/auth/signin","/api/auth/user",
						"/api/auth/signout","/api/auth/username").permitAll()
						.requestMatchers("/v3/api-docs/**").permitAll()
						.requestMatchers("/h2-console/**").permitAll()
						.requestMatchers("/api/admin/**").hasRole("ADMIN") // This checks "ROLE_ADMIN" internally
						.requestMatchers("/api/public/**").permitAll()
						.requestMatchers("/api/public/products/**").permitAll()
						.requestMatchers("/swagger-ui/**").permitAll()
						//.requestMatchers("/api/**").permitAll()
						.requestMatchers("/error").permitAll()
						
						.anyRequest().authenticated());

		http.authenticationProvider(authenticationProvider());
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

		return http.build();
	}

	@Bean
	public CorsFilter corsFilter() {
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.setAllowedOriginPatterns(List.of("http://localhost:3000"));
	    //config.addAllowedOrigin("http://localhost:3000");
	    config.setAllowedHeaders(List.of("*"));
	    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
	    config.setExposedHeaders(List.of("Set-Cookie", "Authorization"));
	    config.setAllowCredentials(true);
	    source.registerCorsConfiguration("/**", config);
	    
	    return new CorsFilter(source);
	}
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers("/v2/api-docs", 
				"/configuration/ui", "/swagger-resources/**",
				"/configuration/security", "/swagger-ui.html", "/webjars/**");
	}
	
	
	
	@Bean
	public CommandLineRunner initDefaultAdmin(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
	    return args -> {
	        if (!usersRepository.existsByUserName("admin")) {
	            Users admin = new Users(
	                "admin",
	                "admin@example.com",
	                passwordEncoder.encode("adminPass")
	            );
	            
	            // Assign ADMIN role
	            Set<AppRole> roles = new HashSet<>();
	            roles.add(AppRole.ADMIN);
	            admin.setRoles(roles);

	            usersRepository.save(admin);
	            System.out.println("Default ADMIN user created: admin / adminPass");
	        } else {
	            System.out.println("Admin user already exists. Skipping creation.");
	        }
	    };
	}

}

/*
 * @Configuration
 * 
 * @EnableWebSecurity //@EnableMethodSecurity public class SecurityConfig {
 * 
 * @Autowired //private JWTFilter jwtFilter; private AuthTokenFilter
 * authTokenFilter;
 * 
 * @Autowired private UserDetailsServiceImpl userDetailsServiceImpl;
 * 
 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
 * Exception { http .csrf() .disable() .authorizeHttpRequests()
 * .requestMatchers(AppConstants.PUBLIC_URLS).permitAll()
 * .requestMatchers(AppConstants.USER_URLS).hasAnyAuthority("USER", "ADMIN")
 * .requestMatchers(AppConstants.ADMIN_URLS).hasAuthority("ADMIN") .anyRequest()
 * .authenticated() .and() .exceptionHandling().authenticationEntryPoint(
 * (request, response, authException) ->
 * response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
 * .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.
 * STATELESS);
 * 
 * http.addFilterBefore(authTokenFilter,
 * UsernamePasswordAuthenticationFilter.class);
 * 
 * http.authenticationProvider(daoAuthenticationProvider());
 * 
 * DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();
 * 
 * return defaultSecurityFilterChain; }
 * 
 * @Bean public DaoAuthenticationProvider daoAuthenticationProvider() {
 * DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
 * 
 * provider.setUserDetailsService(userDetailsServiceImpl);
 * provider.setPasswordEncoder(passwordEncoder());
 * 
 * return provider; }
 * 
 * @Bean public PasswordEncoder passwordEncoder() { return new
 * BCryptPasswordEncoder(); }
 * 
 * @Bean public AuthenticationManager
 * authenticationManager(AuthenticationConfiguration configuration) throws
 * Exception { return configuration.getAuthenticationManager(); } }
 * 
 */