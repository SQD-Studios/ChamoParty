package net.chamosmp.chamoparty.paper.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.chamosmp.chamoparty.paper.core.Plugin;
import net.chamosmp.chamoparty.paper.implementations.ChamoPlayerVote;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerAdapter extends TypeAdapter<net.chamosmp.chamoparty.paper.api.PlayerVote> {

    private final Plugin plugin;

    private final Type seriType = new TypeToken<Map<String, Object>>() {
    }.getType();

    private final String UNIQUEID = "uuid";
    private final String VOTES = "votes";

    /**
     * @param plugin
     */
    public PlayerAdapter(Plugin plugin) {
        super();
        this.plugin = plugin;
    }

    @SuppressWarnings("unchecked")
    @Override
    public net.chamosmp.chamoparty.paper.api.PlayerVote read(JsonReader reader) throws IOException {

        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        String raw = reader.nextString();

        Map<String, Object> keys = this.plugin.getGson().fromJson(raw, this.seriType);

        UUID uuid = UUID.fromString((String) keys.get(this.UNIQUEID));
        List<net.chamosmp.chamoparty.paper.api.Vote> votes = (List<net.chamosmp.chamoparty.paper.api.Vote>) keys.get(this.VOTES);

        return new ChamoPlayerVote(uuid, votes);
    }

    @Override
    public void write(JsonWriter writer, net.chamosmp.chamoparty.paper.api.PlayerVote playerVote) throws IOException {

        if (playerVote == null) {
            writer.nullValue();
            return;
        }

        Map<String, Object> serial = new HashMap<>();

        serial.put(this.UNIQUEID, playerVote.getUniqueId());
        serial.put(this.VOTES, playerVote.getVotes());

        writer.value(this.plugin.getGson().toJson(serial));
    }

}