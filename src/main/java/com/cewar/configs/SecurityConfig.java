package com.cewar.configs;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cewar.enums.Authority;
import com.cewar.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userDetailsService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/account/**").hasRole(Authority.USER.toString())
                .requestMatchers("/write").hasRole(Authority.WRITE.toString())
                .requestMatchers("/api/card/update").hasRole(Authority.ADMIN.toString())
                .requestMatchers("/api/users/addCard/**").hasRole(Authority.WRITE.toString())
                // .requestMatchers("/api/users/**").hasRole(Authority.ADMIN.toString())
                .requestMatchers("/edit").hasRole(Authority.ADMIN.toString())
                .requestMatchers("/pack").hasRole(Authority.USER.toString())
                .requestMatchers("/test").hasRole(Authority.USER.toString())
                .anyRequest().permitAll()) // Permit requests to all other URIs not listed
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .failureUrl("/login?error")
                .permitAll())
            .httpBasic(withDefaults());
		return http.build();
	}

    // Hierarichal Roles Configuration
    @Bean
    static RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role("ADMIN").implies("WRITE")
        .role("WRITE").implies("USER")
        .build();
    }

    // and, if using pre-post method security also add (idk what this does lol)
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}