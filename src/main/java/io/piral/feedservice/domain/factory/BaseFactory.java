package io.piral.feedservice.domain.factory;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public abstract class BaseFactory {
    protected String generateEntityUid() {
        return UUID.randomUUID().toString();
    }
}
