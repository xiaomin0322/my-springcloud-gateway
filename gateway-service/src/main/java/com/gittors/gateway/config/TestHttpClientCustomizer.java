package com.gittors.gateway.config;

import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.stereotype.Component;
import reactor.netty.http.client.HttpClient;

@Component
public class TestHttpClientCustomizer implements HttpClientCustomizer {

    @Override
    public HttpClient customize(HttpClient httpClient) {
        return httpClient.metrics(true, new TestHttpClientMetricsRecorder());
    }
}
