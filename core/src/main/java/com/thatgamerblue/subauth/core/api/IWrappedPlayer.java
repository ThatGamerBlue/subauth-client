package com.thatgamerblue.subauth.core.api;

import java.util.UUID;

public interface IWrappedPlayer {
	UUID getUUID();
	String getName();
	boolean isOp();
	boolean isWhitelisted();
	boolean hasPermission(String permission);
}
