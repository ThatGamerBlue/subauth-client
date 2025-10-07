package com.thatgamerblue.subauth.core.ws.messages;

import org.json.simple.JSONObject;

public abstract class WSMessage {
	public static WSMessage deserialize(JSONObject object) {
		String type = (String) object.get("type");
		switch (type) {
			case "ErrorMessage":
				return ErrorMessage.deserialize(object);
			case "WhitelistUpdateMessage":
				return WhitelistUpdateMessage.deserialize(object);
		}
		throw new IllegalStateException("Unknown message type: " + type);
	}

	public void serialize(JSONObject object) {
		object.put("type", getClass().getSimpleName());
	}
}
