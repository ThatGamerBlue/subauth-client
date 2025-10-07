package com.thatgamerblue.subauth.plugin;

import com.thatgamerblue.subauth.core.WhitelistManager;
import com.thatgamerblue.subauth.core.api.IConfiguration;
import com.thatgamerblue.subauth.plugin.impl.ConfigWrapper;
import com.thatgamerblue.subauth.plugin.impl.PlayerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SubAuthEventHandler implements Listener {
	private final IConfiguration config;
	private final WhitelistManager whitelistManager;

	public SubAuthEventHandler(SubAuthPlugin plugin) {
		this.config = new ConfigWrapper(plugin.getConfig());
		this.whitelistManager = new WhitelistManager(config);

		whitelistManager.checkConfiguredConnect();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		PlayerWrapper player = new PlayerWrapper(event.getPlayer());
		if (!whitelistManager.isAllowedToJoin(player)) {
			event.getPlayer().kickPlayer(config.getDisallowMessage());
			return;
		}
		if (whitelistManager.shouldSendNotConfiguredMessage(player)) {
			event.getPlayer().sendMessage(ChatColor.DARK_RED + "[SubAuth] SubAuth has not been properly configured, and will not allow users to join! Please check the config file.");
		}
	}
}
