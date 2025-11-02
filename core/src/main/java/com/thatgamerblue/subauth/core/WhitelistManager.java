package com.thatgamerblue.subauth.core;

import com.thatgamerblue.subauth.core.api.IConfiguration;
import com.thatgamerblue.subauth.core.api.IWrappedPlayer;
import com.thatgamerblue.subauth.core.api.ILogConsumer;
import com.thatgamerblue.subauth.core.util.Strings;
import com.thatgamerblue.subauth.core.ws.WSClient;
import com.thatgamerblue.subauth.core.ws.messages.subscriptions.Subscription;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WhitelistManager {
	private final Map<Subscription, List<UUID>> whitelistedPlayers = new ConcurrentHashMap<>();

	private Set<UUID> fastCheckWhitelist = new HashSet<>();
	private WSClient wsClient;
	private IConfiguration config;
	private boolean notConfigured;
	private final ILogConsumer logger;

	public WhitelistManager(IConfiguration config, ILogConsumer logger) {
		this.config = config;
		this.notConfigured = config.getTokens().contains("token1");
		this.logger = logger;
		printLicense();
	}

	public void checkConfiguredConnect() {
		if (notConfigured) {
			logger.severe("=====================================================");
			logger.severe("SubAuth needs to be configured before you can use it!");
			logger.severe("");
			logger.severe("By default it will disallow all users not opped or manually whitelisted");
			logger.severe("via the Minecraft whitelist system.");
			logger.severe("");
			logger.severe("Please get a token from the SubAuth server and put it in your config file,");
			logger.severe("removing the lines that say token1, token2, etc, then restart your server.");
			logger.severe("=====================================================");
		} else {
			wsConnect();
		}
	}

	public boolean isAllowedToJoin(IWrappedPlayer player) {
		final UUID playerUuid = player.getUUID();
		boolean isOp = player.isOp();
		boolean whitelist = player.isWhitelisted();
		boolean hasPermission = player.hasPermission("subauth.bypass");
		boolean subWhitelist = fastCheckWhitelist.contains(playerUuid);
		if (!isOp && !whitelist && !subWhitelist && !hasPermission) {
			return false;
		}

		List<Subscription> matchingSubscriptions = new ArrayList<>();
		for (Map.Entry<Subscription, List<UUID>> entry : whitelistedPlayers.entrySet()) {
			if (entry.getValue().contains(playerUuid)) {
				matchingSubscriptions.add(entry.getKey());
			}
		}
		String s = "[" + Strings.join(matchingSubscriptions, ", ") + "]";
		logger.info("Allowing player " + player.getName() + " to join: op? " + isOp + " mc whitelist? " + whitelist + " permission? " + hasPermission);
		logger.info(player.getName() + ": subWhitelist is " + s);
		return true;
	}

	public boolean shouldSendNotConfiguredMessage(IWrappedPlayer player) {
		return notConfigured && (player.isOp() || player.hasPermission("subauth.admin"));
	}

	public void updateWhitelist(Subscription subscription, List<UUID> whitelist) {
		logger.info("Received whitelist update for subscription: " + subscription.toString());
		whitelistedPlayers.put(subscription, whitelist);
		rebuildFastWhitelist();
	}

	public void clearWhitelist() {
		whitelistedPlayers.clear();
		rebuildFastWhitelist();
	}

	public void wsConnect() {
		if (this.wsClient == null) {
			this.wsClient = new WSClient(this, config, logger);
			wsClient.connect();
		} else if (wsClient.isShouldReconnect()) {
			this.wsClient.reconnect();
		}
	}

	public void shutdown() {
		if (this.wsClient != null) {
			this.wsClient.shutdown();
			this.wsClient = null;
		}
	}

	public void reloadConfiguration(IConfiguration config) {
		this.config = config;
		this.notConfigured = config.getTokens().contains("token1");

		checkConfiguredConnect();
	}

	private void rebuildFastWhitelist() {
		Set<UUID> newSet = new HashSet<>();
		for (List<UUID> list : whitelistedPlayers.values()) {
			newSet.addAll(list);
		}
		fastCheckWhitelist = newSet;
	}

	private void printLicense() {
		logger.info("SubAuth is software licensed under AGPLv3. Contributions are welcome!");
		logger.info("The source code for SubAuth is at https://github.com/ThatGamerBlue/subauth");
	}
}
