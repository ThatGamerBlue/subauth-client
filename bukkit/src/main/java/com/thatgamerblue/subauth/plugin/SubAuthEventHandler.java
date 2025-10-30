package com.thatgamerblue.subauth.plugin;

import com.thatgamerblue.subauth.core.WhitelistManager;
import com.thatgamerblue.subauth.core.api.IConfiguration;
import com.thatgamerblue.subauth.plugin.impl.ConfigWrapper;
import com.thatgamerblue.subauth.plugin.impl.LogWrapper;
import com.thatgamerblue.subauth.plugin.impl.PlayerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class SubAuthEventHandler implements Listener {
	private final IConfiguration config;
	private final WhitelistManager whitelistManager;
	private List<UUID> suppressQuitMessage = new ArrayList<>();

	public SubAuthEventHandler(SubAuthPlugin plugin) {
		this.config = new ConfigWrapper(plugin.getConfig());
		this.whitelistManager = new WhitelistManager(config, new LogWrapper());

		whitelistManager.checkConfiguredConnect();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		PlayerWrapper player = new PlayerWrapper(event.getPlayer());
		if (!whitelistManager.isAllowedToJoin(player)) {
			event.setJoinMessage(null); // no message
			suppressQuitMessage.add(player.getUUID());
			event.getPlayer().kickPlayer(config.getDisallowMessage());
			return;
		}
		if (whitelistManager.shouldSendNotConfiguredMessage(player)) {
			event.getPlayer().sendMessage(ChatColor.DARK_RED + "[SubAuth] SubAuth has not been properly configured, and will not allow users to join! Please check the config file.");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (suppressQuitMessage.contains(event.getPlayer().getUniqueId())) {
			event.setQuitMessage(null);
			suppressQuitMessage.remove(event.getPlayer().getUniqueId());
		}
	}

	public void shutdown() {
		whitelistManager.shutdown();
	}
}
