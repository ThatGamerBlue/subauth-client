package com.thatgamerblue.subauth.core.ws.messages;

import lombok.Value;
import org.json.simple.JSONObject;

@Value
public class AuthenticationMessage extends WSMessage {
	String token;

	@Override
	public void serialize(JSONObject object) {
		super.serialize(object);
		object.put("token", token);
	}
}
