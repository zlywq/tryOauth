package g1.oauthlogin.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import g1.oauthlogin.sprsec.*;

import javax.sql.DataSource;


@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired //@Qualifier("dataSource")
    private DataSource dataSource;
    
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    
    @Bean
	MyAuthenticationProvider myAuthenticationProvider(){
		return new MyAuthenticationProvider(); 
	}
    @Bean
    AuthenticationEntryPoint entryPoint() {
		MyAuthenticationEntryPoint MyAuthenticationEntryPoint1 = new MyAuthenticationEntryPoint("/logreg/login");
		return MyAuthenticationEntryPoint1;	
	}
    @Bean
	MyAuthenticationSuccessHandler authSuccessHandler(){
		return new MyAuthenticationSuccessHandler(); 
	}
	@Bean
	MyAuthenticationFailureHandler authFailureHandler(){
		MyAuthenticationFailureHandler bean = new MyAuthenticationFailureHandler(); 
		bean.setDefaultFailureUrl("/logreg/login?error=true");
		return bean;
	}
	@Bean
	MyLogoutSuccessHandler myLogoutSuccessHandler(){
		MyLogoutSuccessHandler bean = new MyLogoutSuccessHandler(); 
		bean.setDefaultTargetUrl("/logreg/logout2");//("/logreg/login");
		return bean;
	}

//  @Bean
//  public BCryptPasswordEncoder passwordEncoder() {
//      return new BCryptPasswordEncoder();
//  }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
        auth.userDetailsService(myUserDetailsService); 
		auth.authenticationProvider(myAuthenticationProvider());
        //remember me
        //...auth.eraseCredentials(false);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// @formatter:off
//        http.formLogin().loginPage("/login").permitAll().successHandler(loginSuccessHandler())
//        .and().authorizeRequests()
//        .antMatchers("/images/**", "/checkcode", "/scripts/**", "/styles/**").permitAll()
//        .anyRequest().authenticated()
//        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
//        .and().exceptionHandling().accessDeniedPage("/deny")
//        .and().rememberMe().tokenValiditySeconds(86400).tokenRepository(tokenRepository());
        
//		http.antMatcher("/**").authorizeRequests().antMatchers("/logout","/login**", "/webjars/**").permitAll()
//		.anyRequest().authenticated()
//		.and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
//		.and().logout().logoutSuccessUrl("/logout2").permitAll()
//		.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//		.and().formLogin().loginPage("/login")
//			;
        

		//原本在xml配置上的http上有个entryPoint，看来是对应这里。如<http use-expressions="false" entry-point-ref="AuthenticationEntryPoint1">
		http.exceptionHandling().authenticationEntryPoint(entryPoint())
			.accessDeniedPage("/logreg/deny");

		http.formLogin().loginPage("/logreg/login").failureUrl("/logreg/login?error=true")
			.failureHandler(authFailureHandler()).successHandler(authSuccessHandler())
			.permitAll();
		http.logout().logoutUrl("/logreg/logout")
			.logoutSuccessHandler(myLogoutSuccessHandler())
			.permitAll()
			.invalidateHttpSession(true);
		http.authorizeRequests()
		.antMatchers("/images/**", "/scripts/**", "/styles/**", "/webjars/**",
				"/nologin/**","/tmptest/**","/logreg/login*","/logreg/reg*")
		.permitAll();
		http.authorizeRequests().anyRequest().authenticated(); //anyRequest() 似乎等于 antMatchers("/**")
		
		//<remember-me data-source-ref="dataSource" token-validity-seconds="1209600" /> 其中的data-source-ref目前发现用不着了，或者可能被下面的代码替换了
		http.rememberMe().tokenValiditySeconds(1209600) //在网页上试验要注意勾选对应的checkbox。
			.tokenRepository(tokenRepository()) //TODO 检查应该需要，应该是对应 persistent_logins 表的
			//.userDetailsService(myUserDetailsService());//...
			;

		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//		http.csrf().disable();//TODO 待研究调.json时如何支持

		// @formatter:on
    }



    @Bean
    public JdbcTokenRepositoryImpl tokenRepository(){
        JdbcTokenRepositoryImpl jtr = new JdbcTokenRepositoryImpl();
        jtr.setDataSource(dataSource);
        return jtr;
    }

    
}
