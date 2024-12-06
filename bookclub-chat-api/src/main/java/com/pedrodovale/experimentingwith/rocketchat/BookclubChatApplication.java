package com.pedrodovale.experimentingwith.rocketchat;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@EnableConfigurationProperties(BookclubChatProperties.class)
@RequiredArgsConstructor
public class BookclubChatApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookclubChatApplication.class, args);
  }

  @Bean
  public RestClient restClient(BookclubChatProperties bookclubChatProperties) {
    return RestClient.builder()
        .requestFactory(new HttpComponentsClientHttpRequestFactory())
        .baseUrl(bookclubChatProperties.getUri().toString())
        .defaultHeader("x-Auth-Token", bookclubChatProperties.getPersonalAccessToken())
        .defaultHeader("x-User-Id", bookclubChatProperties.getUserId())
        .build();
  }
}
