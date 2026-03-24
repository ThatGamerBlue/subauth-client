package com.thatgamerblue.subauth.fabric.impl;

import com.thatgamerblue.subauth.core.api.IWrappedPlayer;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerWrapper implements IWrappedPlayer {
	private final PlayerList playerList;
	private final NameAndId player;

	@Override
	public UUID getUUID() {
		return player.id();
	}

	@Override
	public String getName() {
		return player.name();
	}

	@Override
	public boolean isOp() {
		return playerList.getOps().get(player) != null;
	}

	@Override
	public boolean isWhitelisted() {
		return playerList.getWhiteList().isWhiteListed(player);
	}

	@Override
	public boolean hasPermission(String permission) {
		return false; // fabric has no permissions api
	}
}
