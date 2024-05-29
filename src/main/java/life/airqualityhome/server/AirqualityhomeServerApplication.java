package life.airqualityhome.server;


import life.airqualityhome.server.config.ApplicationProperties;
import life.airqualityhome.server.repositories.helper.RefreshableCRUDRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableJpaRepositories(repositoryBaseClass = RefreshableCRUDRepositoryImpl.class)
public class AirqualityhomeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirqualityhomeServerApplication.class, args);
    }

}
