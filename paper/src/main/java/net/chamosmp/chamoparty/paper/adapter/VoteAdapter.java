package net.chamosmp.chamoparty.paper.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.chamosmp.chamoparty.paper.core.Plugin;
import net.chamosmp.chamoparty.paper.implementations.ChamoReward;
import net.chamosmp.chamoparty.paper.implementations.ChamoVote;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteAdapter extends TypeAdapter<ChamoVote> {

    private final Plugin plugin;

    private final Type seriType = new TypeToken<Map<String, Object>>() {
    }.getType();

    private final String SERVICENAME = "servicename";
    private final String REWARD = "reward";
    private final String CREATED_AT = "created_at";
    private final String IS_GIVE = "is_give";

    /**
     * @param plugin
     */
    public VoteAdapter(Plugin plugin) {
        super();
        this.plugin = plugin;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ChamoVote read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        String raw = reader.nextString();

        Map<String, Object> keys = this.plugin.getGson().fromJson(raw, this.seriType);

        Number createdAt = (Number) keys.get(this.CREATED_AT);

        Map<String, Object> rewardMap = (Map<String, Object>) keys.get(this.REWARD);
        List<String> commands = (List<String>) rewardMap.get("commands");
        List<String> messages = (List<String>) rewardMap.get("messages");
        Number percent = (Number) rewardMap.get("percent");

        ChamoReward chamoReward = new ChamoReward(percent.doubleValue(), commands, false, messages);

        String serviceName = (String) keys.get(this.SERVICENAME);
        boolean isGive = (boolean) keys.get(this.IS_GIVE);

        return new ChamoVote(serviceName, createdAt.longValue(), chamoReward, isGive);
    }

    @Override
    public void write(JsonWriter writer, ChamoVote chamoVote) throws IOException {

        if (chamoVote == null) {
            writer.nullValue();
            return;
        }

        Map<String, Object> serial = new HashMap<String, Object>();

        serial.put(this.SERVICENAME, chamoVote.getServiceName());
        serial.put(this.REWARD, chamoVote.getReward());
        serial.put(this.IS_GIVE, chamoVote.rewardIsGive());
        serial.put(this.CREATED_AT, chamoVote.getCreatedAt());

        writer.value(this.plugin.getGson().toJson(serial));
    }

}
