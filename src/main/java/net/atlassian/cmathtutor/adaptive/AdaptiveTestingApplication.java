package net.atlassian.cmathtutor.adaptive;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javafx.application.Application;
import net.atlassian.cmathtutor.adaptive.repository.CrudRepository;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "net.atlassian.cmathtutor.adaptive.repository",
	basePackageClasses = { CrudRepository.class })
public class AdaptiveTestingApplication {

    public static void main(String[] args) {
	Application.launch(JavaFxApplication.class, args);
//	SpringApplication.run(AdaptiveTestingApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
	return builder.build();
    }

}
