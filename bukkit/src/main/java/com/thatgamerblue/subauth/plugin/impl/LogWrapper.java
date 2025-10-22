package com.thatgamerblue.subauth.plugin.impl;

import com.thatgamerblue.subauth.core.api.ILogConsumer;
import java.util.logging.Logger;

public class LogWrapper implements ILogConsumer {
	private static final Logger logger = Logger.getLogger("SubAuth");

	@Override
	public void info(String message) {
		logger.info(message);
	}

	@Override
	public void severe(String message) {
		logger.severe(message);
	}
}
