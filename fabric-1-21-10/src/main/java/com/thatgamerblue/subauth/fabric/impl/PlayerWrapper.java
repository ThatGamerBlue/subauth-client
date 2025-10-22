package com.thatgamerblue.subauth.fabric.impl;

import com.thatgamerblue.subauth.core.api.IWrappedPlayer;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerWrapper implements IWrappedPlayer {
	private final MinecraftServer server;
	private final ServerPlayer player;

	@Override
	public UUID getUUID() {
		return player.getUUID();
	}

	@Override
	public String getName() {
		return player.getDisplayName().getString();
	}

	@Override
	public boolean isOp() {
		return player.getPermissionLevel() >= 2;
	}

	@Override
	public boolean isWhitelisted() {
		return server.getPlayerList().getWhiteList().isWhiteListed(player.nameAndId());
	}

	@Override
	public boolean hasPermission(String permission) {
		return false; // fabric has no permissions api
	}
}
