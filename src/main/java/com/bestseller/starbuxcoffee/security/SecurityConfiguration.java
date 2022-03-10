package com.bestseller.starbuxcoffee.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bestseller.starbuxcoffee.security.filter.CartClientIdFilter;
import com.bestseller.starbuxcoffee.security.filter.TokenAuthenticationFilter;
import com.bestseller.starbuxcoffee.security.repository.UserRepository;
import com.bestseller.starbuxcoffee.security.service.AuthenticationService;
import com.bestseller.starbuxcoffee.security.token.TokenService;
import com.bestseller.starbuxcoffee.service.CartService;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserRepository repository;

	@Autowired
	private CartService cartService;

	private List<String> bypassAllFilters;

	private List<String> bypassAllTokenAuthFilter;

	private List<String> shouldFilterCartFilter;

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.authenticationService).passwordEncoder(new BCryptPasswordEncoder(10));
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		this.bypassAllFilters = new ArrayList<>();
		this.bypassAllTokenAuthFilter = new ArrayList<>();
		this.shouldFilterCartFilter = new ArrayList<>();

		this.bypassAllFilters.add("/auth/**");
		this.bypassAllFilters.add("/auth/test/**");
		this.bypassAllFilters.add("/cart/**");
		this.bypassAllFilters.add("/h2-console/**");
		this.bypassAllFilters.add("/h2/**");
		this.bypassAllFilters.add("/actuator/**");
		this.bypassAllFilters.add("/products/**");
		this.bypassAllFilters.add("/swagger-ui/**");
		this.bypassAllFilters.add("/swagger-ui.html");
		this.bypassAllFilters.add("/v3/api-docs/**");

		this.shouldFilterCartFilter.add("/cart/**");

		this.bypassAllTokenAuthFilter.addAll(this.bypassAllFilters);

		http.authorizeRequests()//
				.antMatchers(HttpMethod.POST, "/auth/**").permitAll()//
				.antMatchers("/auth/test/**").permitAll()//
				.antMatchers("/cart/**").permitAll()//
				.antMatchers("/products/**").permitAll()//
				.antMatchers("/h2/**").permitAll() //
				.antMatchers("/swagger-ui/**").permitAll() //
				.antMatchers("/swagger-ui.html").permitAll() //
				.antMatchers("/v3/api-docs/**").permitAll() //
				.antMatchers("/actuator/**").permitAll()//
				.antMatchers("/admin/**").authenticated()//
				.anyRequest().authenticated()//
				.and()//
				.csrf().disable()//
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
				.and()//
				.addFilterBefore(//
						new TokenAuthenticationFilter(this.tokenService, this.repository,
								this.bypassAllTokenAuthFilter),
						UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(new CartClientIdFilter(this.shouldFilterCartFilter, this.cartService),
						TokenAuthenticationFilter.class);
		http.headers().frameOptions().disable();
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		super.configure(web);
	}

}
