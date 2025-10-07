package com.thatgamerblue.subauth.core.ws.messages;

import lombok.Value;
import org.json.simple.JSONObject;

@Value
public class ErrorMessage extends WSMessage {
	ErrorType error;
	String extraData;

	public static ErrorMessage deserialize(JSONObject object) {
		ErrorType type = ErrorType.valueOf((String) object.get("error"));
		String extraData = (String) object.get("extraData");
		return new ErrorMessage(type, extraData);
	}

	@Override
	public void serialize(JSONObject object) {
		throw new IllegalStateException("this is a received message");
	}

	public enum ErrorType {
		INVALID_MESSAGE, INVALID_TOKEN, NO_HANDLER, ALREADY_SUBSCRIBED, UNKNOWN_USER;
	}
}
