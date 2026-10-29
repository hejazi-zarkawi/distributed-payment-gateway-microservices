package com.hejazi.distributed_payment_gateway.common_lib.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.BytesMessageSender;
import zipkin2.reporter.urlconnection.URLConnectionSender;

@Configuration
public class ZipkinConfig {
    @Bean
    public BytesMessageSender zipkinSender(
            @Value("${management.zipkin.tracing.endpoint}") String endpoint) {
        return URLConnectionSender.create(endpoint);
    }
}
