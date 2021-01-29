package com.fooqoo56.iine.bot.publisher.infrastructure.api.repositoryimpl;

import com.fooqoo56.iine.bot.publisher.domain.api.repository.TwitterRepository;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.config.TwitterConfig;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.request.TweetRequest;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetListResponse;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.response.TweetResponse;
import com.fooqoo56.iine.bot.publisher.infrastructure.api.util.OauthAuthorizationHeaderBuilder;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
@RequiredArgsConstructor
public class TwitterRepositoryImpl implements TwitterRepository {

    private final TwitterConfig config;
    private final WebClient twitterSearchClient;
    private final WebClient twitterFavoriteClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public TweetListResponse findTweet(final TweetRequest request) {
        final HttpHeaders headers = new HttpHeaders();

        final String url =
                UriComponentsBuilder.fromHttpUrl(config.getBaseUrl())
                        .path(getSearchPath())
                        .queryParams(request.getQueryMap())
                        .build()
                        .toString();

        return twitterSearchClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(TweetListResponse.class)
                .block();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TweetResponse favoriteTweet(final String id) {

        final OauthAuthorizationHeaderBuilder builder = OauthAuthorizationHeaderBuilder
                .builder()
                .method(HttpMethod.POST.name().toUpperCase())
                .url(config.getBaseUrl() + getFavoritePath())
                .consumerSecret(config.getApiSecret())
                .tokenSecret(config.getAccessTokenSecret())
                .accessToken(config.getAccessToken())
                .consumerKey(config.getApikey())
                .queryParameters(Map.of("id", id))
                .build();

        final String url =
                UriComponentsBuilder.fromHttpUrl(config.getBaseUrl())
                        .path(getFavoritePath())
                        .queryParam("id", id)
                        .build()
                        .toString();

        return twitterFavoriteClient
                .post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, builder.getOauthHeader())
                .retrieve()
                .bodyToMono(TweetResponse.class)
                .block();
    }

    /**
     * searchパス取得.
     *
     * @return パス
     */
    private String getSearchPath() {
        return config.getPath() + "search/tweets.json";
    }

    /**
     * いいねパス取得.
     *
     * @return パス
     */
    private String getFavoritePath() {
        return config.getPath() + "favorites/create.json";
    }
}
