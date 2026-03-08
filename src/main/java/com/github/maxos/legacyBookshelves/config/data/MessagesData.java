package com.github.maxos.legacyBookshelves.config.data;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class MessagesData {

    @Getter
    private final String prefix;
    private final Map<String, String> messages;

    public MessagesData(Map<String, String> messages, String prefix) {
        this.messages = messages;
        this.prefix = prefix;
    }

    public String getMsg(String msgId) {
        return messages.getOrDefault(msgId, "Мы пытались чота написать вам, но поймали null...");
    }

}
