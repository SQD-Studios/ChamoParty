package net.chamosmp.chamoparty.storage.redis;

import java.util.UUID;

public class RedisVoteResponse {

    private final String username;
    private final String serviceName;
    private int responseCount;
    private UUID userId;

    public RedisVoteResponse(String username, String serviceName, int responseCount, UUID userId) {
        super();
        this.username = username;
        this.serviceName = serviceName;
        this.responseCount = responseCount;
        this.userId = userId;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @return the responseCount
     */
    public int getResponseCount() {
        return responseCount;
    }

    /**
     * @return the userId
     */
    public UUID getUserId() {
        return userId;
    }

    public void addResponse(String userId) {
        this.responseCount += 1;
        if (this.userId == null && userId != null && userId.length() == 36) {
            this.userId = UUID.fromString(userId);
        }
    }

}
