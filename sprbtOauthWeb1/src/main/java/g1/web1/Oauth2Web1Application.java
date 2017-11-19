package g1.web1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import g1.ClassInTopPackage;

/*
关于使用 security.oauth2.resource.user-info-uri 而不使用其他的security.oauth2.resource.* 试验OK，在http环境下。
但是，使用user-info-uri看来有性能问题，每次访问resource server的url，都会导致resource server去访问一次user-info-uri。


 */
@SpringBootApplication(scanBasePackageClasses={ClassInTopPackage.class},exclude = {MongoAutoConfiguration.class})
//@EnableAutoConfiguration
//@Configuration
//@ComponentScan(basePackageClasses=ClassInTopPackage.class)
public class Oauth2Web1Application {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2Web1Application.class, args);
	}
}
