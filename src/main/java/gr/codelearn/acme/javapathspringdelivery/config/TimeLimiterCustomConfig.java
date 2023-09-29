package gr.codelearn.acme.javapathspringdelivery.config;


import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class TimeLimiterCustomConfig {
    @Bean
    public TimeLimiter timeLimiter() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofMillis(200)) // Set the timeout duration (e.g., 200 ms)
                .cancelRunningFuture(true) // Cancel the running future on timeout
                .build();

        return TimeLimiter.of("basicTimeout", config);
    }
}
