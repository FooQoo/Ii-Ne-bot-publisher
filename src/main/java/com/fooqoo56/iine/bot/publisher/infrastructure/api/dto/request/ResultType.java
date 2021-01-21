package com.fooqoo56.iine.bot.publisher.infrastructure.api.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResultType {

    MIXED("mixed"),
    RECENT("recent"),
    POPULAR("popular");

    private final String name;

}
