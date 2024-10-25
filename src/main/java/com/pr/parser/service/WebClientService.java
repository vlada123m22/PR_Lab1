package com.pr.parser.service;

import com.pr.parser.rest.ScrappingProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

@Service
@RequiredArgsConstructor
public class WebClientService {

    private final ScrappingProperties scrappingProperties;

    public Mono<String> fetchHtmlContent(String path) {

        return Mono.fromCallable(() -> {
                    String host = extractHost(scrappingProperties.getBaseUrl());

                    SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

                    try (SSLSocket socket = (SSLSocket) factory.createSocket(host, 443);
                         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                         BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                        writer.write("GET " + path + " HTTP/1.1\r\n");
                        writer.write("Host: " + host + "\r\n");
                        writer.write("Connection: close\r\n\r\n");
                        writer.flush();

                        StringBuilder response = new StringBuilder();
                        String line;
                        boolean isBody = false;

                        while ((line = reader.readLine()) != null) {
                            if (isBody) {
                                response.append(line).append("\n");
                            }
                            if (line.isEmpty()) {
                                isBody = true;
                            }
                        }

                        return response.toString();
                    } catch (IOException e) {
                        throw new RuntimeException("Error fetching HTML content over HTTPS", e);
                    }
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    private String extractHost(String url) {
        try {
            java.net.URL urlObj = new java.net.URL(url);
            return urlObj.getHost();
        } catch (Exception e) {
            throw new RuntimeException("Invalid URL format: " + url, e);
        }
    }
}
