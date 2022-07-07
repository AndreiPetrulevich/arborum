package ru.arborumapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "arborum.api")
public class ArborumApiProperties {

    private Connection connection;
    private Endpoint endpoint;

    @Getter
    @Setter
    public static class Endpoint{
        private String manufacturerUrl;
        private String categoryUrl;
        private String productUrl;
    }

    @Getter
    @Setter
    public static class Connection {
        private long period;
        private long maxPeriod;
        private int maxAttempts;

        private long connectTimeout;
//        private TimeUnit connectTimeoutUnit;
        private long readTimeout;
//        private TimeUnit readTimeoutUnit;
        private boolean followRedirects;
    }
}
