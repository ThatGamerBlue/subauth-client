package com.thatgamerblue.subauth.fabric.impl;

import com.thatgamerblue.subauth.core.api.ILogConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogWrapper implements ILogConsumer {
	public static final Logger LOGGER = LogManager.getLogger("SubAuth");

	@Override
	public void info(String message) {
		LOGGER.info(message);
	}

	@Override
	public void severe(String message) {
		LOGGER.error(message);
	}
}
