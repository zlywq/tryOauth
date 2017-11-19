package g1.oauthres;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;


/*
如果extends ResourceServerConfigurerAdapter，会导致一个运行时错误，见最下面。
难道oauth2 resource server不需要这个ResourceServerConfigurerAdapter......?
 */
@Configuration
public class MyOAuth2ResourceServerConfig
//        extends ResourceServerConfigurerAdapter
{
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//    }
//    public void configure(HttpSecurity http) throws Exception {
//    }



//    @Override  
//    public void configure(ResourceServerSecurityConfigurer config) {  
//        config.tokenServices(tokenServices());  
//    }  
//   
//    @Bean  
//    public TokenStore tokenStore() {  
//        return new JwtTokenStore(accessTokenConverter());  
//    }
//   
//    @Bean  
//    public JwtAccessTokenConverter accessTokenConverter() {  
////        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();  
////        converter.setSigningKey("123");  
////        return converter;  
//        
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("localhost.jks"), "123456".toCharArray())
//      		.getKeyPair("alias1", "123456".toCharArray());
//      
//        converter.setKeyPair(keyPair);
//        //converter.setSigningKey("123");
//
//        return converter;
//    }  
//   
//    @Bean  
//    @Primary  
//    public DefaultTokenServices tokenServices() {  
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();  
//        defaultTokenServices.setTokenStore(tokenStore());  
//        return defaultTokenServices;  
//    }















}
/*

开头注释提到的那个错误的具体信息如下
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'springSecurityFilterChain' defined in class path resource [org/springframework/security/config/annotation/web/configuration/WebSecurityConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [javax.servlet.Filter]: Factory method 'springSecurityFilterChain' threw exception; nested exception is java.lang.IllegalStateException: Cannot apply org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer@57435801 to already built object
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:599) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1173) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1067) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:513) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:483) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:306) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:230) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:197) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:296) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:197) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:761) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:866) ~[spring-context-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:542) ~[spring-context-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.boot.context.embedded.EmbeddedWebApplicationContext.refresh(EmbeddedWebApplicationContext.java:122) ~[spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:737) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:370) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:314) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1162) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1151) [spring-boot-1.5.2.RELEASE.jar:1.5.2.RELEASE]
	at g1.oauthres.ResourceApplication.main(ResourceApplication.java:15) [classes/:na]
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [javax.servlet.Filter]: Factory method 'springSecurityFilterChain' threw exception; nested exception is java.lang.IllegalStateException: Cannot apply org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer@57435801 to already built object
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:189) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:588) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	... 20 common frames omitted
Caused by: java.lang.IllegalStateException: Cannot apply org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer@57435801 to already built object
	at org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder.add(AbstractConfiguredSecurityBuilder.java:196) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder.apply(AbstractConfiguredSecurityBuilder.java:133) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.web.builders.HttpSecurity.getOrApply(HttpSecurity.java:1372) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.web.builders.HttpSecurity.authorizeRequests(HttpSecurity.java:651) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer.configure(ResourceServerSecurityConfigurer.java:199) ~[spring-security-oauth2-2.0.13.RELEASE.jar:na]
	at org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer.configure(ResourceServerSecurityConfigurer.java:55) ~[spring-security-oauth2-2.0.13.RELEASE.jar:na]
	at org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder.configure(AbstractConfiguredSecurityBuilder.java:384) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder.doBuild(AbstractConfiguredSecurityBuilder.java:330) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.AbstractSecurityBuilder.build(AbstractSecurityBuilder.java:41) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.web.builders.WebSecurity.performBuild(WebSecurity.java:290) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.web.builders.WebSecurity.performBuild(WebSecurity.java:77) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder.doBuild(AbstractConfiguredSecurityBuilder.java:334) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.AbstractSecurityBuilder.build(AbstractSecurityBuilder.java:41) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration.springSecurityFilterChain(WebSecurityConfiguration.java:104) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration$$EnhancerBySpringCGLIB$$1df787ff.CGLIB$springSecurityFilterChain$6(<generated>) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration$$EnhancerBySpringCGLIB$$1df787ff$$FastClassBySpringCGLIB$$be0304e.invoke(<generated>) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:228) ~[spring-core-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:358) ~[spring-context-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	at org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration$$EnhancerBySpringCGLIB$$1df787ff.springSecurityFilterChain(<generated>) ~[spring-security-config-4.2.2.RELEASE.jar:4.2.2.RELEASE]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_151]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_151]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_151]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_151]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:162) ~[spring-beans-4.3.7.RELEASE.jar:4.3.7.RELEASE]
	... 21 common frames omitted

 */
