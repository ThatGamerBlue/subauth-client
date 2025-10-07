package com.thatgamerblue.subauth.plugin.impl;

import com.thatgamerblue.subauth.core.api.IWrappedPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerWrapper implements IWrappedPlayer {
	private final Player player;

	@Override
	public UUID getUUID() {
		return player.getUniqueId();
	}

	@Override
	public String getName() {
		return player.getName();
	}

	@Override
	public boolean isOp() {
		return player.isOp();
	}

	@Override
	public boolean isWhitelisted() {
		return player.isWhitelisted();
	}

	@Override
	public boolean hasPermission(String permission) {
		return player.hasPermission(permission);
	}
}
