package dev.somichev.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEvents {
	public static final Event<PlayerJoin> JOIN_EVENT =
		EventFactory.createArrayBacked(PlayerJoin.class, callbacks -> (player, server) -> {
			for (var event : callbacks) event.onPlayerJoin(player, server);
		});

	public static final Event<PlayerLeft> LEAVE_EVENT =
		EventFactory.createArrayBacked(PlayerLeft.class, callbacks -> (player, server) -> {
			for (var event : callbacks) event.onPlayerLeave(player, server);
		});

	public static final Event<PlayerSpawned> SPAWN_EVENT =
		EventFactory.createArrayBacked(PlayerSpawned.class, callbacks -> (player, server) -> {
			for (var event : callbacks) event.onPlayerSpawn(player, server);
		});

	public static final Event<PlayerDied> DEATH_EVENT =
		EventFactory.createArrayBacked(PlayerDied.class, callbacks -> (player, server, damageSource) -> {
			for (var event : callbacks) event.onPlayerDeath(player, server, damageSource);
		});

	public static final Event<PlayerTickStart> TICK_START_EVENT =
		EventFactory.createArrayBacked(PlayerTickStart.class, callbacks -> (player, server) -> {
			for (var event : callbacks) event.onPlayerTickStart(player, server);
		});

	public static final Event<PlayerTickEnd> TICK_END_EVENT =
		EventFactory.createArrayBacked(PlayerTickEnd.class, callbacks -> (player, server) -> {
			for (var event : callbacks) event.onPlayerTickEnd(player, server);
		});


	public interface PlayerJoin {
		void onPlayerJoin(ServerPlayerEntity player, MinecraftServer server);
	}

	public interface PlayerLeft {
		void onPlayerLeave(ServerPlayerEntity player, MinecraftServer server);
	}

	public interface PlayerSpawned {
		void onPlayerSpawn(ServerPlayerEntity player, MinecraftServer server);
	}

	public interface PlayerDied {
		void onPlayerDeath(ServerPlayerEntity player, MinecraftServer server, DamageSource damageSource);
	}

	public interface PlayerTickStart {
		void onPlayerTickStart(ServerPlayerEntity player, MinecraftServer server);
	}

	public interface PlayerTickEnd {
		void onPlayerTickEnd(ServerPlayerEntity player, MinecraftServer server);
	}
}