package dev.somichev.mixin.player_events;

import dev.somichev.events.PlayerEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("ALL")
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
	@Shadow
	@Final
	public MinecraftServer server;

	@Inject(method = "onSpawn", at = @At("HEAD"))
	private void onPlayerSpawn(CallbackInfo ci) {
		PlayerEvents.SPAWN_EVENT.invoker().onPlayerSpawn((ServerPlayerEntity) (Object) this, this.server);
	}

	@Inject(method = "onDeath", at = @At("HEAD"))
	private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
		PlayerEvents.DEATH_EVENT.invoker().onPlayerDeath((ServerPlayerEntity) (Object) this, this.server, damageSource);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void tickStart(CallbackInfo ci) {
		PlayerEvents.TICK_START_EVENT.invoker().onPlayerTickStart((ServerPlayerEntity) (Object) this, this.server);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tickEnd(CallbackInfo ci) {
		PlayerEvents.TICK_END_EVENT.invoker().onPlayerTickEnd((ServerPlayerEntity) (Object) this, this.server);
	}
}
