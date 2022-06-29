package ru.arborumapi.config;

import feign.*;
import feign.codec.ErrorDecoder;
import feign.optionals.OptionalDecoder;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.support.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.arborumapi.manufacturer.api.ManufacturerGateway;

import java.util.concurrent.TimeUnit;

import static feign.FeignException.errorStatus;

@Configuration
@EnableConfigurationProperties(ArborumApiProperties.class)
@RequiredArgsConstructor
public class FeignConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;
    private final ObjectProvider<HttpMessageConverterCustomizer> customizers;
    private final ArborumApiProperties arborumApiProperties;

    @Bean
    public ManufacturerGateway manufacturerGateway() {
        return Feign.builder()
                .encoder(new SpringEncoder(messageConverters))
                .decoder(new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters, this.customizers))))
                .retryer(new Retryer.Default(
                        arborumApiProperties.getConnection().getPeriod(),
                        arborumApiProperties.getConnection().getMaxPeriod(),
                        arborumApiProperties.getConnection().getMaxAttempts()
                ))
                .errorDecoder(errorDecoder())
                .options(new Request.Options(
                        arborumApiProperties.getConnection().getConnectTimeout(),
                            TimeUnit.MILLISECONDS,
//                        arborumApiProperties.getConnection().getConnectTimeoutUnit(),
                        arborumApiProperties.getConnection().getReadTimeout(),
//                        arborumApiProperties.getConnection().getReadTimeoutUnit(),
                            TimeUnit.MILLISECONDS,
                        arborumApiProperties.getConnection().isFollowRedirects()
                ))
//                .client()
                .contract(new SpringMvcContract())
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger(ManufacturerGateway.class))
                .target(ManufacturerGateway.class, arborumApiProperties.getEndpoint().getManufacturerUrl());
    }

    private ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            FeignException exception = errorStatus(methodKey, response);
            if (exception.status() == 500 || exception.status() == 503) {
                return new RetryableException(
                        response.status(),
                        exception.getMessage(),
                        response.request().httpMethod(),
                        exception,
                        null,
                        response.request());
            }
            return exception;
        };
    }
}
