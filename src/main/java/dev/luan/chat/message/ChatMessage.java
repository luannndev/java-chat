package dev.luan.chat.message;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public record ChatMessage(String sender, String message) {
    public static final Charset CHARSET = StandardCharsets.UTF_8;
}