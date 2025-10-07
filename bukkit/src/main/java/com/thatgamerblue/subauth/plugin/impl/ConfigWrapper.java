package com.thatgamerblue.subauth.plugin.impl;

import com.thatgamerblue.subauth.core.ConfigDefaults;
import com.thatgamerblue.subauth.core.api.IConfiguration;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.Configuration;
import java.util.List;

@RequiredArgsConstructor
public class ConfigWrapper implements IConfiguration {
	private final Configuration config;

	@Override
	public String getWsHost() {
		return config.getString("subauth_host", ConfigDefaults.SUBAUTH_HOST);
	}

	@Override
	public int getWsPort() {
		return config.getInt("subauth_port", ConfigDefaults.SUBAUTH_PORT);
	}

	@Override
	public String getDisallowMessage() {
		return config.getString("disallow_message", ConfigDefaults.DISALLOW_MESSAGE);
	}

	@Override
	public List<String> getTokens() {
		List<String> val = config.getStringList("tokens");
		return val.isEmpty() ? ConfigDefaults.TOKENS : val;
	}
}
