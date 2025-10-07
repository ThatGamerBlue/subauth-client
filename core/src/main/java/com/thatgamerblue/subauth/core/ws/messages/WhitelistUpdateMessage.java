package com.thatgamerblue.subauth.core.ws.messages;

import java.util.ArrayList;
import java.util.List;
import com.thatgamerblue.subauth.core.ws.messages.subscriptions.Subscription;
import lombok.Value;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Value
public class WhitelistUpdateMessage extends WSMessage {
	Subscription cause;
	List<String> whitelist;

	public static WhitelistUpdateMessage deserialize(JSONObject object) {
		Subscription cause = Subscription.deserialize((JSONObject) object.get("cause"));
		JSONArray whitelistJson = (JSONArray) object.get("whitelist");
		ArrayList<String> whitelist = new ArrayList<>();
		for (Object o : whitelistJson) {
			whitelist.add((String) o);
		}
		return new WhitelistUpdateMessage(cause, whitelist);
	}

	@Override
	public void serialize(JSONObject object) {
		throw new IllegalStateException("this is a received message");
	}
}
