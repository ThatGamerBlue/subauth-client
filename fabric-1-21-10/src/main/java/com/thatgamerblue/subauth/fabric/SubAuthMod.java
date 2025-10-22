package com.thatgamerblue.subauth.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SubAuthMod implements DedicatedServerModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("SubAuth");

	@Override
	public void onInitializeServer() {
		LOGGER.info("SubAuth mod loaded");

		ServerPlayConnectionEvents.JOIN.register(new PlayerJoinEventListener());
	}
}
