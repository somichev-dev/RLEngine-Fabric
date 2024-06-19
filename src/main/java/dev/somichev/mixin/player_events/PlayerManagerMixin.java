package dev.somichev.mixin.player_events;

import dev.somichev.events.PlayerEvents;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Inject(at = @At(value = "TAIL"), method = "onPlayerConnect")
	private void onPlayerJoin(
		ClientConnection connection,
		ServerPlayerEntity player,
		ConnectedClientData clientData,
		CallbackInfo ci
	) {
		PlayerEvents.JOIN_EVENT.invoker().onPlayerJoin(player, player.getServer());
	}
}