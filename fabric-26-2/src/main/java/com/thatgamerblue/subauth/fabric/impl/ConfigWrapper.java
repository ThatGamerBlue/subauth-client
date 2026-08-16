package com.thatgamerblue.subauth.fabric.impl;

import com.google.gson.annotations.SerializedName;
import com.thatgamerblue.subauth.core.api.IConfiguration;
import lombok.Value;
import java.util.List;

@Value
public class ConfigWrapper implements IConfiguration {
	@SerializedName("subauth_host")
	String wsHost;
	@SerializedName("subauth_port")
	int wsPort;
	@SerializedName("disallow_message")
	String disallowMessage;
	@SerializedName("tokens")
	List<String> tokens;
}
