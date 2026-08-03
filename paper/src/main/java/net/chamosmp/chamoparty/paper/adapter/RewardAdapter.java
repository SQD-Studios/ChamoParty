package net.chamosmp.chamoparty.paper.adapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import net.chamosmp.chamoparty.paper.implementations.ChamoReward;
import net.chamosmp.chamoparty.paper.core.Plugin;

public class RewardAdapter extends TypeAdapter<ChamoReward> {

	private final Plugin plugin;

	private final Type seriType = new TypeToken<Map<String, Object>>() {
	}.getType();

	private final String PERCENT = "percent";
	private final String COMMANDS = "commands";
	private final String MESSAGES = "messages";

	/**
	 * @param plugin
	 */
	public RewardAdapter(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ChamoReward read(JsonReader reader) throws IOException {
		if (reader.peek() == JsonToken.NULL) {
			reader.nextNull();
			return null;
		}

		String raw = reader.nextString();
		
		Map<String, Object> keys = this.plugin.getGson().fromJson(raw, this.seriType);

		Number percent = (Number) keys.get(this.PERCENT);
		List<String> commands = (List<String>) keys.get(this.COMMANDS);
		List<String> messages = (List<String>) keys.get(this.MESSAGES);

		return new ChamoReward(percent.doubleValue(), commands, false, messages);
	}

	@Override
	public void write(JsonWriter writer, ChamoReward chamoReward) throws IOException {
		
		if (chamoReward == null) {
			writer.nullValue();
			return;
		}
		
		Map<String, Object> serial = new HashMap<String, Object>();
		
		serial.put(this.PERCENT, chamoReward.getPercent());
		serial.put(this.COMMANDS, chamoReward.getCommands());
		serial.put(this.MESSAGES, chamoReward.getMessages());
		
		writer.value(this.plugin.getGson().toJson(serial));
	}

}
