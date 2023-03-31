package tacos.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import tacos.User;
import tacos.data.UserRepository;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepo) {
		
		return username -> {
			User user = userRepo.findByUsername(username);
			if (user != null)
				return user;
			throw new UsernameNotFoundException("User '" + username + "' not found");
		};
		
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
//				.csrf().ignoringRequestMatchers(PathRequest.toH2Console())
//				.and()
//	            .headers().frameOptions().sameOrigin()
//	            .and()
				
				.csrf().disable()
	            
				.authorizeHttpRequests()
				.requestMatchers("/design", "/orders")
				.hasRole("USER")
				.requestMatchers("/", "/**")
				.permitAll()
				
				.and()
				.formLogin()
				.loginPage("/login")
				.and()
				.logout()
				.logoutSuccessUrl("/")
				
				.and()
				.build();
	}
}
