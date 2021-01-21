package com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.request;

import com.fooqoo56.iine.bot.publisher.presentation.dto.mq.TweetCondition;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Value;
import org.springframework.lang.NonNull;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Value
@Builder
public class TweetRequest implements Serializable {

    private static final long serialVersionUID = 4303034636519921068L;

    @NonNull
    String query;

    @NonNull
    Lang lang = Lang.JA;

    @NonNull
    ResultType resultType = ResultType.RECENT;

    @NonNull
    Integer count = 100;

    @NonNull
    Boolean includeEntitiesFlag = false;

    @NonNull
    LocalDate until = LocalDate.now();

    /**
     * payloadをAPIクエリへ変換.
     *
     * @param payload PayLoad
     * @return APIクエリ
     */
    public static TweetRequest convertPayloadToRequest(final TweetCondition payload) {
        return TweetRequest
                .builder()
                .query(payload.getQuery())
                .build();
    }

    /**
     * クエリをMap型に変換.
     *
     * @return Map
     */
    public MultiValueMap<String, String> getQueryMap() {
        final MultiValueMap<String, String> queries = new LinkedMultiValueMap<>();

        queries.add("q", query);
        queries.add("lang", lang.getCountry());
        queries.add("result_type", resultType.getName());
        queries.add("count", count.toString());
        queries.add("include_entities", includeEntitiesFlag.toString());
        queries.add("until", until.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        return queries;
    }
}
