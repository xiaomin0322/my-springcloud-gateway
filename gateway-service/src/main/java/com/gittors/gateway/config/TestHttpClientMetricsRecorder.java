package com.gittors.gateway.config;

import reactor.netty.http.client.HttpClientMetricsRecorder;
import reactor.util.annotation.NonNull;

import java.net.SocketAddress;
import java.time.Duration;

public class TestHttpClientMetricsRecorder implements HttpClientMetricsRecorder {
    @Override
    public void recordDataReceivedTime(@NonNull SocketAddress remoteAddress, @NonNull String uri, @NonNull String method, @NonNull String status, @NonNull Duration time) {
    }

    @Override
    public void recordDataSentTime(@NonNull SocketAddress remoteAddress, @NonNull String uri, @NonNull String method, @NonNull Duration time) {
    }

    @Override
    public void recordResponseTime(@NonNull SocketAddress remoteAddress, @NonNull String uri, @NonNull String method, @NonNull String status, @NonNull Duration time) {
    }

    @Override
    public void recordDataReceived(@NonNull SocketAddress remoteAddress, @NonNull String uri, long bytes) {
    }

    @Override
    public void recordDataSent(@NonNull SocketAddress remoteAddress, @NonNull String uri, long bytes) {
    }

    @Override
    public void incrementErrorsCount(@NonNull SocketAddress remoteAddress, @NonNull String uri) {
    }

    @Override
    public void recordDataReceived(@NonNull SocketAddress remoteAddress, long bytes) {
    }

    @Override
    public void recordDataSent(@NonNull SocketAddress remoteAddress, long bytes) {
    }

    @Override
    public void incrementErrorsCount(@NonNull SocketAddress remoteAddress) {
    }

    @Override
    public void recordTlsHandshakeTime(@NonNull SocketAddress remoteAddress, @NonNull Duration time, @NonNull String status) {
    }

    @Override
    public void recordConnectTime(@NonNull SocketAddress remoteAddress, @NonNull Duration time, @NonNull String status) {
    }

    @Override
    public void recordResolveAddressTime(@NonNull SocketAddress remoteAddress, @NonNull Duration time, @NonNull String status) {
    }
}
