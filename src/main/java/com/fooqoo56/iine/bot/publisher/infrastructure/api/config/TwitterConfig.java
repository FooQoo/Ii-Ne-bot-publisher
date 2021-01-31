package com.fooqoo56.iine.bot.publisher.infrastructure.api.config;

import com.fooqoo56.iine.bot.publisher.infrastructure.api.filter.Oauth2Filter;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.filter.RestRequestFilter;
import io.netty.channel.ChannelOption;
import io.netty.resolver.DefaultAddressResolverGroup;
import java.time.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@ConstructorBinding
@ConfigurationProperties(prefix = "extension.api.twitter")
@RequiredArgsConstructor
@Getter
public class TwitterConfig {

    private final String baseUrl;

    private final String path;

    private final Duration connectTimeout;

    private final Duration readTimeout;

    private final String apikey;

    private final String apiSecret;

    private final String accessToken;

    private final String accessTokenSecret;

    private final Integer maxInMemorySize;

    /**
     * 検索APIクライアント.
     *
     * @param oauth2Filter      Oauth2Filter
     * @param restRequestFilter ログフィルタ
     * @return WebClient
     */
    @Bean
    public WebClient twitterSearchClient(final Oauth2Filter oauth2Filter,
                                         final RestRequestFilter restRequestFilter) {
        final HttpClient httpClient = HttpClient.create()
                .baseUrl(baseUrl)
                .responseTimeout(readTimeout)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout.toMillisPart())
                .resolver(DefaultAddressResolverGroup.INSTANCE);
        ;

        final ReactorClientHttpConnector connector =
                new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(maxInMemorySize))
                        .build())
                .clientConnector(connector)
                .filter(oauth2Filter)
                .filter(restRequestFilter)
                .build();
    }

    /**
     * いいねAPIクライアント.
     *
     * @param restRequestFilter ログフィルタ
     * @return WebClient
     */
    @Bean
    public WebClient twitterFavoriteClient(final RestRequestFilter restRequestFilter) {
        final HttpClient httpClient = HttpClient.create()
                .baseUrl(baseUrl)
                .responseTimeout(readTimeout)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout.toMillisPart())
                .resolver(DefaultAddressResolverGroup.INSTANCE);
        ;

        final ReactorClientHttpConnector connector =
                new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .clientConnector(connector)
                .filter(restRequestFilter)
                .build();
    }
}
