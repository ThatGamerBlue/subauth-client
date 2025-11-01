package com.thatgamerblue.subauth.core.api;

public interface ILogConsumer {
	void info(String message, Object... fmt);
	void severe(String message, Object... fmt);
}
