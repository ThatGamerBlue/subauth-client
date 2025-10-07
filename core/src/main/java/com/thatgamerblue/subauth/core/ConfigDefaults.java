package com.thatgamerblue.subauth.core;

import java.util.ArrayList;
import java.util.List;

public class ConfigDefaults {
	public static final String SUBAUTH_HOST = "subauth.thatgamerblue.com";
	public static final int SUBAUTH_PORT = 443;
	public static final String DISALLOW_MESSAGE = "This server is protected by SubAuth. You must be a subscriber to join this server.";
	public static final List<String> TOKENS = new ArrayList<String>() {{
		add("token1");
		add("token2");
		add("etc");
	}};
}
