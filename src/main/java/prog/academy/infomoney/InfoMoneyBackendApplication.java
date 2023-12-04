package prog.academy.infomoney;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import prog.academy.infomoney.dto.request.RegisterRequest;
import prog.academy.infomoney.service.AuthenticationService;

@SpringBootApplication
@Log4j2
public class InfoMoneyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfoMoneyBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService service) {
        return args -> {
            if (service.isAdminCreated()) {
                return;
            }
            var admin = new RegisterRequest("admin@admin.com", "admin");
            log.info("Admin token: {}", service.register(admin, true).accessToken());
        };
    }

}
