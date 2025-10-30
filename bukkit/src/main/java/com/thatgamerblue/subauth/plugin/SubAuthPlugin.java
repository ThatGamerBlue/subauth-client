package com.thatgamerblue.subauth.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SubAuthPlugin extends JavaPlugin {
	private SubAuthEventHandler eventHandler;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		eventHandler = new SubAuthEventHandler(this);
		Bukkit.getPluginManager().registerEvents(eventHandler, this);
	}

	@Override
	public void onDisable() {
		eventHandler.shutdown();
	}
}
