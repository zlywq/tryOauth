package g1.oauthres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import g1.ClassInTopPackage;


/*
关于使用 security.oauth2.resource.user-info-uri 而不使用其他的security.oauth2.resource.* 试验OK，在http环境下。
但是，使用user-info-uri看来有性能问题，每次访问这里的url，都会导致本服务器去访问一次user-info-uri。


 */


@SpringBootApplication(exclude = {MongoAutoConfiguration.class},scanBasePackageClasses = {ClassInTopPackage.class})
public class ResourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class, args);
    }
}
