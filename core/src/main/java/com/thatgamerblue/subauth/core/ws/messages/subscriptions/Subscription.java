package com.thatgamerblue.subauth.core.ws.messages.subscriptions;

import org.json.simple.JSONObject;

public abstract class Subscription {
	abstract String getId();

	public static Subscription deserialize(JSONObject object) {
		String type = (String) object.get("type");
		switch (type) {
			case "TwitchSubscription":
				return TwitchSubscription.deserialize(object);
		}
		throw new IllegalStateException("Unknown subscription type: " + type);
	}

	@Override
	public String toString() {
		return "Type: " + getClass().getSimpleName() + " ID: " + getId();
	}
}
