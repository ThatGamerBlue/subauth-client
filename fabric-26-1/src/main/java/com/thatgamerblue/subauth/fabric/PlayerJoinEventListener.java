package com.thatgamerblue.subauth.fabric;

import com.google.gson.Gson;
import com.thatgamerblue.subauth.core.WhitelistManager;
import com.thatgamerblue.subauth.fabric.impl.ConfigWrapper;
import com.thatgamerblue.subauth.fabric.impl.LogWrapper;
import com.thatgamerblue.subauth.fabric.impl.PlayerWrapper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PlayerJoinEventListener implements ServerPlayConnectionEvents.Join, ServerLifecycleEvents.ServerStopped {
	private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("subauth.json");
	private static final Gson GSON = new Gson();
	private final WhitelistManager whitelistManager;
	private ConfigWrapper config;

	public PlayerJoinEventListener() {
		loadConfig();
		this.whitelistManager = new WhitelistManager(config, new LogWrapper());

		whitelistManager.checkConfiguredConnect();
	}

	public Component checkWhitelistHook(PlayerList list, NameAndId player) {
		PlayerWrapper playerWrapper = new PlayerWrapper(list, player);
		if (!whitelistManager.isAllowedToJoin(playerWrapper)) {
			return Component.literal(config.getDisallowMessage());
		}
		return null;
	}

	@Override
	public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
		PlayerWrapper playerWrapper = new PlayerWrapper(server.getPlayerList(), handler.player.nameAndId());
		if (whitelistManager.shouldSendNotConfiguredMessage(playerWrapper)) {
			Component message = Component.literal("[SubAuth] SubAuth has not been properly configured, and will not allow users to join! Please check the config file.").withStyle(ChatFormatting.DARK_RED);
			handler.player.sendSystemMessage(message, false);
		}
	}

	@Override
	public void onServerStopped(MinecraftServer minecraftServer) {
		whitelistManager.shutdown();
	}

	private void loadConfig() {
		if (!Files.exists(CONFIG_FILE)) {
			try (InputStream is = getClass().getResourceAsStream("/subauth.json")) {
				Files.copy(is, CONFIG_FILE, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				throw new RuntimeException("Failed to load default config file!", ex);
			}
		}

		String configContents;
		try {
			configContents = Files.readString(CONFIG_FILE);
		} catch (IOException ex) {
			throw new RuntimeException("Failed to read config file from disk!", ex);
		}
		this.config = GSON.fromJson(configContents, ConfigWrapper.class);
	}
}
