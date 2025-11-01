package com.thatgamerblue.subauth.plugin.impl;

import com.thatgamerblue.subauth.core.api.ILogConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogWrapper implements ILogConsumer {
	private static final Logger logger = Logger.getLogger("SubAuth");

	@Override
	public void info(String message, Object... fmt) {
		logger.log(Level.INFO, message, fmt);
	}

	@Override
	public void severe(String message, Object... fmt) {
		logger.log(Level.SEVERE, message, fmt);
	}
}
