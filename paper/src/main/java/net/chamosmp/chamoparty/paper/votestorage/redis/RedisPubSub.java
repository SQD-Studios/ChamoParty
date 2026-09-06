package net.chamosmp.chamoparty.paper.votestorage.redis;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import net.chamosmp.chamoparty.api.storage.RedisSubChannel;
import net.chamosmp.chamoparty.paper.ChamoPartyPlugin;
import net.chamosmp.chamoparty.paper.core.logger.Logger;
import net.chamosmp.chamoparty.paper.core.logger.Logger.LogType;
import net.chamosmp.chamoparty.paper.core.sched.SchedulerUtil;
import net.chamosmp.chamoparty.paper.save.LegacyJsonConfig;
import net.chamosmp.chamoparty.paper.votestorage.storages.RedisStorage;
import net.chamosmp.chamoparty.storage.redis.RedisVoteResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class RedisPubSub implements RedisPubSubListener<String, String> {

    private final String SEPARATOR = ";;";

    private final ChamoPartyPlugin plugin;
    private final RedisStorage storage;

    private final ChamoRedisClient chamoRedisClient;
    private final RedisPubSubAsyncCommands<String, String> asyncCommands;
    private final StatefulRedisPubSubConnection<String, String> connection;

    private final List<UUID> sendingUUID = new ArrayList<>();

    private final Map<UUID, RedisVoteResponse> voteResponses = new HashMap<>();

    /**
     * @param plugin
     * @param storage
     * @param client
     */
    public RedisPubSub(ChamoPartyPlugin plugin, RedisStorage storage, ChamoRedisClient chamoRedisClient) {
        this.plugin = plugin;
        this.storage = storage;

        this.chamoRedisClient = chamoRedisClient;

        connection = chamoRedisClient.getConnection();
        asyncCommands = chamoRedisClient.getAsyncCommands();

        try {
            connection.addListener(this);
            RedisFuture<Void> future =
                    asyncCommands.subscribe(LegacyJsonConfig.redisChannel);

            future.thenAccept(_ -> {
                Logger.log("Subscribed to the redis channel");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows you to send a message
     *
     * @param subChannel
     * @param message
     */
    private UUID sendMessage(RedisSubChannel channel, String message) {
        final UUID uuid = UUID.randomUUID();
        SchedulerUtil.runAsync(plugin, () -> {
            try {
                String jMessage = channel.name() + SEPARATOR + uuid;
                if (message != null) {
                    jMessage += this.SEPARATOR + message;
                }
                asyncCommands.publish(LegacyJsonConfig.redisChannel, jMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return uuid;
    }

    /**
     * Allows to stop the thread
     */
    public void stop() {
        RedisFuture<Void> unsubscribe =
                asyncCommands.unsubscribe(LegacyJsonConfig.redisChannel);
        unsubscribe.thenAccept(_ -> {
           chamoRedisClient.close();
        });
    }

    /**
     * Allows you to send a message
     *
     * @param subChannel
     */
    private UUID sendMessage(RedisSubChannel channel) {
        return this.sendMessage(channel, null);
    }

    /**
     * Allows you to add a vote to the voteparty
     */
    public void sendAddVoteCount() {
        this.sendMessage(RedisSubChannel.ADD_VOTEPARTY);
    }

    /**
     * Allows you to send the information to start the voting party
     */
    public void sendHandleVoteParty() {
        this.sendMessage(RedisSubChannel.HANDLE_VOTEPARTY);
    }

    /**
     * Allows you to send the voting action
     *
     * @param username
     * @param serviceName
     * @param uuid
     */
    public void sendVoteAction(String username, String serviceName, UUID uuid) {

        String message = username + ";;" + serviceName;
        UUID messageId = this.sendMessage(RedisSubChannel.ADD_VOTE, message);

        // Allows to give the reward if the player is not connected
        RedisVoteResponse redisVoteResponse = new RedisVoteResponse(messageId, username, serviceName, 1, uuid);
        this.voteResponses.put(messageId, redisVoteResponse);

    }

    /**
     * Allows you to reply to the server that sent the voting request to say
     * that the player is not allowed to vote
     *
     * @param uuid
     * @param username
     * @param serviceName
     */
    private void handleVoteResponseError(UUID uuid, String username, String serviceName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
        this.handleVoteResponse(uuid, false, offlinePlayer.getUniqueId().toString());
    }

    /**
     * We will return the answer with all the information
     *
     * @param uuid
     * @param isSuccess
     * @param message
     */
    private void handleVoteResponse(UUID uuid, boolean isSuccess, String message) {
        String jMessage = uuid.toString() + this.SEPARATOR + isSuccess + this.SEPARATOR + message;
        this.sendMessage(RedisSubChannel.VOTE_RESPONSE, jMessage);
    }

    /**
     * Allows you to perform an action when receiving the voting confirmation
     *
     * @param messageId Identifier of the message that sent the request to vote
     * @param isSuccess Allows to know if the vote is successful
     * @param userId    Player's UUID
     */
    private void processResponse(UUID messageId, boolean isSuccess, String userId) {

        RedisVoteResponse redisVoteResponse = this.voteResponses.getOrDefault(messageId, null);

        // If the redis vote response is null, then the messageID is incorrect
        // or the value is delete
        if (redisVoteResponse == null) {
            return;
        }

        // If the answer is a success, then we delete the value
        if (isSuccess) {
            this.voteResponses.remove(messageId);
        } else {

            // Otherwise we check that the number of responses corresponds to
            // the number of servers indicated in the configuration file
            // We also add the UUID of the player if it is present

            redisVoteResponse.addResponse(userId);

            if (redisVoteResponse.getResponseCount() >= LegacyJsonConfig.redisServerAmount) {

                // We will check if the UUID of the player exists, if yes then
                // we will give a reward so that the player can recover it when
                // he will connect

                // If the player cannot be found, then nothing can be done and
                // the vote will be lost

                if (redisVoteResponse.getUserId() != null) {

                    this.plugin.getManager().voteOffline(redisVoteResponse.getUserId(),
                            redisVoteResponse.getServiceName());

                } else {

                    Logger.log("Impossible to find the player " + redisVoteResponse.getUsername(), LogType.WARNING);

                }

            }

        }

    }

    @Override
    public void message(String channel, String message) {
        if (!this.plugin.isEnabled()) {
            return;
        }

        try {
            if (channel.equals(LegacyJsonConfig.redisChannel)) {

                String[] values = message.split(this.SEPARATOR);

                RedisSubChannel subChannel = RedisSubChannel.byName(values[0]);
                String uuidAsString = values[1];
                UUID uuid = UUID.fromString(uuidAsString);

                // Allows to verify that the server sending the information does
                // not receive it.

                if (this.sendingUUID.contains(uuid)) {
                    this.sendingUUID.remove(uuid);
                    return;
                }

                switch (subChannel) {
                    case ADD_VOTEPARTY:
                        this.storage.addSecretVoteCount(1);
                        break;
                    case HANDLE_VOTEPARTY:
                        this.storage.setSecretVoteCount(0);
                        this.plugin.getManager().secretStart();
                        break;
                    case ADD_VOTE:
                        this.handleVoteResponse(uuid, true, null);
                        break;
                    case VOTE_RESPONSE:
                        UUID messageId = UUID.fromString(values[2]);
                        boolean isSuccess = Boolean.valueOf(values[3]);
                        String userId = values[4];
                        this.processResponse(messageId, isSuccess, userId);
                        break;
                    default:
                        break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    Useless redis listener stuff
     */

    @Override
    public void message(String pattern, String channel, String message) {

    }

    @Override
    public void subscribed(String channel, long count) {

    }

    @Override
    public void psubscribed(String pattern, long count) {

    }

    @Override
    public void unsubscribed(String channel, long count) {

    }

    @Override
    public void punsubscribed(String pattern, long count) {

    }
}
