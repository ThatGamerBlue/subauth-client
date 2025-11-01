package com.thatgamerblue.subauth.core.ws;

import com.thatgamerblue.subauth.core.WhitelistManager;
import com.thatgamerblue.subauth.core.api.IConfiguration;
import com.thatgamerblue.subauth.core.api.ILogConsumer;
import com.thatgamerblue.subauth.core.ws.messages.AuthenticationMessage;
import com.thatgamerblue.subauth.core.ws.messages.ErrorMessage;
import com.thatgamerblue.subauth.core.ws.messages.WSMessage;
import com.thatgamerblue.subauth.core.ws.messages.WhitelistUpdateMessage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WSClient extends WebSocketClient {

	private final WhitelistManager whitelistManager;
	private final IConfiguration config;
	private final ILogConsumer logger;
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	@Getter
	private final List<String> invalidTokens;
	@Getter
	@Setter
	private boolean shouldReconnect = true;

	public WSClient(WhitelistManager whitelistManager, IConfiguration config, ILogConsumer logger) {
		super(URI.create("wss://" + config.getWsHost() + ":" + config.getWsPort() + "/ws"));
		setDaemon(true);
		this.whitelistManager = whitelistManager;
		this.config = config;
		this.logger = logger;
		this.invalidTokens = new ArrayList<>();
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		List<String> tokens = config.getTokens();
		for (String token : tokens) {
			if (!invalidTokens.contains(token)) {
				send(new AuthenticationMessage(token));
			}
		}
	}

	@Override
	public void onMessage(String s) {
		JSONParser parser = new JSONParser();
		WSMessage message;
		try {
			message = WSMessage.deserialize((JSONObject) parser.parse(s));
		} catch (Exception e) {
			logger.severe("Got invalid message from server: " + s);
			e.printStackTrace();
			return;
		}
		if (message instanceof ErrorMessage) {
			ErrorMessage error = (ErrorMessage) message;
			switch (error.getError()) {
				case NO_HANDLER:
					logger.severe("SubAuth backend encountered a critical error, wiping whitelist and disconnecting");
					whitelistManager.clearWhitelist();
					shouldReconnect = false;
					close();
					break;
				case INVALID_TOKEN:
					logger.severe("There is an invalid or outdated SubAuth token in your config file, please check it, and recreate any tokens if necessary");
					invalidTokens.add(error.getExtraData());
					break;
				case ALREADY_SUBSCRIBED:
					logger.severe("There is a duplicate SubAuth token in your config file, please check it, and remove any duplicates");
					break;
				case UNKNOWN_USER:
					logger.severe("One of your SubAuth tokens is owned by a user unknown to SubAuth. Did you unlink your account?");
					invalidTokens.add(error.getExtraData());
					break;
			}
		} else if (message instanceof WhitelistUpdateMessage) {
			WhitelistUpdateMessage whitelistUpdate = (WhitelistUpdateMessage) message;
			List<UUID> uuidList = new ArrayList<>();
			for (String uuidStr : whitelistUpdate.getWhitelist()) {
				uuidList.add(UUID.fromString(uuidStr));
			}
			whitelistManager.updateWhitelist(whitelistUpdate.getCause(), uuidList);
		}
	}

	@Override
	public void onClose(int i, String s, boolean b) {
		logger.info("SubAuth websocket closed");
		if (shouldReconnect) {
			logger.info("Scheduling reconnect");
			executor.schedule(new Runnable() {
				@Override
				public void run() {
					if (shouldReconnect) {
						logger.info("WebSocket reconnecting");
						reconnect();
					}
				}
			}, 5000, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void onError(Exception e) {
		logger.severe("SubAuth's websocket connection encountered an unrecoverable issue", e);
	}

	public void shutdown() {
		shouldReconnect = false;
		close();
		executor.shutdown();
	}

	private void send(WSMessage message) {
		JSONObject jsonObject = new JSONObject();
		message.serialize(jsonObject);
		send(jsonObject.toJSONString());
	}
}
