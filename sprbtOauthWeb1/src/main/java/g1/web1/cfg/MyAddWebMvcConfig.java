package g1.web1.cfg;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import g1.libweb.controller.MyXmlView;

import java.util.Arrays;



/*
参考WebMvcAutoConfiguration中的WebMvcAutoConfigurationAdapter的源代码，关于viewResolver(BeanFactory beanFactory)的，
自行实现了ContentNegotiatingViewResolver的bean。目前看来Spring Boot MVC自动配置的东西还是有，暂且没发现意外情况。

 */

@Configuration
public class MyAddWebMvcConfig //extends WebMvcConfigurerAdapter 
{
	//WebMvcAutoConfiguration WebMvcAutoConfiguration1;


	
	@Bean
	public ContentNegotiatingViewResolver viewResolver(BeanFactory beanFactory)  {
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		
		ContentNegotiationManager ContentNegotiationManager1 = beanFactory.getBean(ContentNegotiationManager.class) ;
//		ContentNegotiationManager1.addMediaType("json", MediaType.APPLICATION_JSON);
		resolver.setContentNegotiationManager(ContentNegotiationManager1);
		
//	    FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
//	    viewResolver.setPrefix("/templates/");
//	    viewResolver.setSuffix(".ftl");
	    //contentViewResolver.setViewResolvers(Arrays.<ViewResolver> asList(viewResolver));
	    
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		MyXmlView xmlView = new MyXmlView();
	    //defaultView.setExtractValueFromSingleKeyModel(true);
	    resolver.setDefaultViews(Arrays.<View> asList(jsonView,xmlView));
	    
	    // ContentNegotiatingViewResolver uses all the other view resolvers to locate a view so it should have a high precedence
	 	resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
	 	
	    return resolver;
	}
}
