package sk.tuke.kpi.kp.rollingCube;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.kpi.kp.rollingCube.game.consoleui.ConsoleUi;
import sk.tuke.kpi.kp.rollingCube.game.core.MazeField;
import sk.tuke.kpi.kp.rollingCube.service.*;
import sk.tuke.kpi.kp.rollingCube.service.restClient.CommentServiceRestClient;
import sk.tuke.kpi.kp.rollingCube.service.restClient.RatingServiceRestClient;
import sk.tuke.kpi.kp.rollingCube.service.restClient.ScoreServiceRestClient;
import sk.tuke.kpi.kp.rollingCube.service.restClient.UserServiceRestClient;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
            pattern = "sk.tuke.kpi.kp.rollingCube.server.*"))
public class SpringClient {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUi ui) {
        return args -> ui.play();
    }

    @Bean
    public ConsoleUi consoleUI(MazeField field) {
        return new ConsoleUi(field);
    }

    @Bean
    public MazeField field() {
        return new MazeField();
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }

    @Bean
    public UserService userService() {
        return new UserServiceRestClient();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
