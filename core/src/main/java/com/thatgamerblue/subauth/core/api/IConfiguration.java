package com.thatgamerblue.subauth.core.api;

import java.util.List;

public interface IConfiguration {
	String getWsHost();

	int getWsPort();

	String getDisallowMessage();

	List<String> getTokens();
}
