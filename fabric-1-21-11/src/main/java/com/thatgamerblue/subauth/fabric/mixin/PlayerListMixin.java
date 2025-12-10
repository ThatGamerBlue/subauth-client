package com.thatgamerblue.subauth.fabric.mixin;

import com.thatgamerblue.subauth.fabric.SubAuthMod;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.net.SocketAddress;

@Mixin(PlayerList.class)
public class PlayerListMixin {
	@Inject(method = "canPlayerLogin(Ljava/net/SocketAddress;Lnet/minecraft/server/players/NameAndId;)Lnet/minecraft/network/chat/Component;", at = {@At("RETURN")}, cancellable = true)
	public void canPlayerLoginMixin(SocketAddress socketAddress, NameAndId nameAndId, CallbackInfoReturnable<Component> cir) {
		if (cir.getReturnValue() == null) {
			cir.setReturnValue(SubAuthMod.eventHandler.checkWhitelistHook((PlayerList) (Object) this, nameAndId));
		}
	}
}
