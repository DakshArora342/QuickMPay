package com.quickmpay.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.quickmpay.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	 UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	 PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

		DefaultSecurityFilterChain build = security.csrf(a -> a.disable())
				.authorizeHttpRequests(b -> b
						.requestMatchers("/", "/signup","/payment","/resetPass","/forgetPass","/contact","/changePass","/changePassword", "/about","/recharge","/optionsHandler","/userhome","/getRate", "/verifyPage", "/css/**","/js/**", "/image/**","kycForm").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/register", "/verify","/riskyUserAccountDetails","/addRiskyAccount","/safeUserAccountDetails","/digitalUserAccountDetails","/placeOrder","/cancelOrder","/submitOrder","/getTips","/kycUpload","/rechargeLog","/orderRecord").permitAll()
						.requestMatchers("/handlePage","/addrate","/addWallet","/viewKyc","/adminhome","/addPercentage","/addTips","/users","/deleteUser","/viewOrders","/approveOrder","/rejectOrder","/allUsers","/transferAmount","changePassAdmin","/searchUser").permitAll()
						.requestMatchers("/hathi/admin").hasRole("ADMIN"))
						.formLogin(k -> k.loginPage("/signin").usernameParameter("email").permitAll()
//						.loginProcessingUrl("/signin")
//						.defaultSuccessUrl("/userDashboard")
						.failureUrl("/signin")
								).logout(
				                        logout -> logout
		                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		                                .permitAll()
		                                .deleteCookies("JSESSIONID").invalidateHttpSession(true).permitAll()
		           )
				.build();
		return build;
	}

	@Bean
	AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(userDetailsService());
		return authenticationProvider;
	}

	@Bean
	 AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
		try {
			return authenticationConfiguration.getAuthenticationManager();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
