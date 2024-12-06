package com.pedrodovale.experimentingwith.rocketchat;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = BookclubChatProperties.PREFIX)
@Getter
@Setter
public class BookclubChatProperties {
    public static final String PREFIX = "bookclub-chat";

    private URI uri = URI.create("http://localhost:3000");
    @NotBlank
    private String personalAccessToken;
    @NotBlank
    private String userId;
}
