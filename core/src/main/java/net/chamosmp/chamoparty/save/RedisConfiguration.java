package net.chamosmp.chamoparty.save;

import org.jetbrains.annotations.Nullable;

public record RedisConfiguration(
        String host,
        int port,
        @Nullable String password
) {
}