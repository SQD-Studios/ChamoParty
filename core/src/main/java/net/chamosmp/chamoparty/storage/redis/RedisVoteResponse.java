package net.chamosmp.chamoparty.storage.redis;

import java.util.UUID;

public record RedisVoteResponse(
        String username,
        String serviceName,
        int responseCount,
        UUID userId
) {
    public RedisVoteResponse addResponse(String userId) {
        int newResponseCount = responseCount + 1;
        UUID newUserId = this.userId;

        if (this.userId == null && userId != null && userId.length() == 36) {
            newUserId = UUID.fromString(userId);
        }
        return new RedisVoteResponse(
                username,
                serviceName,
                newResponseCount,
                newUserId
        );
    }
}