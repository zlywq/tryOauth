package g1.oauthlogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import g1.ClassInTopPackage;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

// mvn clean package -Dmaven.test.skip=true
@SpringBootApplication(scanBasePackageClasses = {ClassInTopPackage.class},exclude = {MongoAutoConfiguration.class})
//@SpringBootConfiguration
//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
//@ComponentScan(basePackageClasses=ClassInTopPackage.class)
public class LoginApplication 
implements ServletContextInitializer
{
    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.getSessionCookieConfig().setName("SESSIONID");
    }
}
