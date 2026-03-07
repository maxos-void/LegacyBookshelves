package com.github.maxos.legacyBookshelves.config.data;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.HashMap;

public class MessagesData {

    @Getter
    private final String prefix;
    private final HashMap<String, String> messages;

    public MessagesData(HashMap<String, String> messages, String prefix) {
        this.messages = messages;
        this.prefix = prefix;
    }

    public String getMsg(String msgId) {
        String foundMsg = messages.getOrDefault(msgId, "Мы пытались чота написать вам, но поймали null...");
        return foundMsg;
    }

}
