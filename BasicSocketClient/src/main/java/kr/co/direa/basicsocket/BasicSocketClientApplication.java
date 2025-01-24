package kr.co.direa.basicsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan(basePackages = "kr.co.direa.basicsocket.properties")
@SpringBootApplication
public class BasicSocketClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicSocketClientApplication.class, args);
	}

}
