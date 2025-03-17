package com.example.external_api.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;


@Configuration
public class WebClientConfig {
    /*@Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("https://172.17.231.170:9191/session-management")
                .build();
    }*/

    @Bean
    public WebClient webClient() throws SSLException {
        // Create an insecure HttpClient that ignores SSL certificate errors
        HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> {
            try {
                SslContext sslContext = SslContextBuilder.forClient()
                        .trustManager(new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                            @Override
                            public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                            @Override
                            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                        }).build();

                sslContextSpec.sslContext(sslContext);
            } catch (SSLException e) {
                throw new RuntimeException(e);
            }
        });

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("https://172.17.231.170:9191/session-management")
                .build();
    }
}
