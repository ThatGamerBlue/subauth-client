package com.thatgamerblue.subauth.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SubAuthPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		saveDefaultConfig();

		SubAuthEventHandler eventHandler = new SubAuthEventHandler(this);
		Bukkit.getPluginManager().registerEvents(eventHandler, this);
	}
}
