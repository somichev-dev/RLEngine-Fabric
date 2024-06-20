package dev.somichev.entity

import com.mojang.authlib.GameProfile
import com.mojang.serialization.Dynamic
import dev.somichev.RadioLampEngine
import dev.somichev.climate.calculation.PlayerWarmness
import dev.somichev.entity.attribute.RadioLampEngineEntityAttributes
import dev.somichev.gui.PlayerInventoryMenu
import dev.somichev.mixin.EntityAccessor
import dev.somichev.world.PositionState
import dev.somichev.world.RadioLampPersistentState
import eu.pb4.polymer.core.api.entity.PolymerEntity
import eu.pb4.polymer.resourcepack.api.PolymerModelData
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import eu.pb4.polymer.virtualentity.api.ElementHolder
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment
import eu.pb4.polymer.virtualentity.api.elements.ItemDisplayElement
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.CustomModelDataComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityStatuses
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.network.packet.c2s.common.SyncedClientOptions
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.stat.Stats
import net.minecraft.text.Text
import net.minecraft.text.TextCodecs
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.GameMode
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.event.GameEvent
import org.joml.Vector3f
import java.util.*


class CorpseEntity(type: EntityType<CorpseEntity>, world: World) : Entity(type, world), PolymerEntity,
    NamedScreenHandlerFactory {
    companion object {
        val id = RadioLampEngine.id("corpse")
        val model: PolymerModelData = PolymerResourcePackUtils.requestModel(Items.PAPER, id.withPrefixedPath("item/"))
    }

    private val elementHolder = ElementHolder()

    init {
        elementHolder.addElement(ItemDisplayElement(model.item().defaultStack.also {
            it[DataComponentTypes.CUSTOM_MODEL_DATA] = CustomModelDataComponent(model.value())
        }).also {
            it.yaw = yaw
            it.translation = Vector3f(0.25f, 0.5f, 0.25f)
        })
        elementHolder.attachment = EntityAttachment.of(elementHolder, this)
        isInvisible = true
    }

    var playerUUID: UUID? = null
    var playerDeathText: Text? = null
    private var ticks = 0

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        if (nbt.contains("player_uuid")) playerUUID = nbt.getUuid("player_uuid")
        if (nbt.contains("player_death_text")) {
            playerDeathText = TextCodecs.STRINGIFIED_CODEC.parse(
                server!!.registryManager.getOps(NbtOps.INSTANCE), nbt.get("player_death_text")
            ).orThrow
        }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        if (playerUUID != null) nbt.putUuid("player_uuid", playerUUID)
        if (playerDeathText != null) {
            nbt.put(
                "player_death_text", TextCodecs.STRINGIFIED_CODEC.encodeStart(
                    server!!.registryManager.getOps(NbtOps.INSTANCE), playerDeathText
                ).orThrow
            )
        }
    }


    override fun initDataTracker(builder: DataTracker.Builder?) {}
    override fun isInvulnerable() = true

    override fun tick() {
        if (ticks++ % 20 == 0 && playerUUID != null) {
            val player = server?.playerManager?.getPlayer(playerUUID) ?: return
            if ((player.isAlive && !player.isSpectator) || RadioLampPersistentState.getPlayerState(player).gravePos?.equals(
                    PositionState(blockPos, world.dimensionEntry.key.get())
                ) != true
            ) {
                remove(RemovalReason.DISCARDED)
            }
        }
    }

    override fun interact(player: PlayerEntity, hand: Hand): ActionResult {
        val server = server ?: return ActionResult.FAIL
        val corpsePair = forceGetPlayer()
        if (corpsePair?.first == null) {
            player.sendMessage(Text.literal("Этот трупик он ничей"))
            return ActionResult.FAIL
        }
        val corpse = corpsePair.first!!
        val isDisconnected = corpsePair.second
        val resurrectorStack = player.getStackInHand(hand)
        if (resurrectorStack.isOf(Items.TOTEM_OF_UNDYING)) {
            if (isDisconnected || player.isDead) {
                player.sendMessage(server.messageDecorator.decorate(corpse, Text.literal("До связи...")))
                return ActionResult.FAIL
            }

            corpse.attributes.getCustomInstance(RadioLampEngineEntityAttributes.temperature)?.let {
                it.baseValue = 0.0
            }

            corpse.teleport(world as ServerWorld, x, y, z, yaw, pitch)
            corpse.changeGameMode(GameMode.SURVIVAL)
            player.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING))
            Criteria.USED_TOTEM.trigger(corpse, resurrectorStack)
            corpse.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH)
            corpse.health = 1.0f
            corpse.clearStatusEffects()
            corpse.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 900, 1))
            corpse.addStatusEffect(StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1))
            corpse.addStatusEffect(StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0))
            world.sendEntityStatus(corpse, EntityStatuses.USE_TOTEM_OF_UNDYING)
            remove(RemovalReason.DISCARDED)
            if (!player.isCreative) player.setStackInHand(hand, ItemStack.EMPTY)
            return ActionResult.CONSUME
        }
        player.openHandledScreen(this)
        return ActionResult.SUCCESS
    }

    private fun forceGetPlayer(): Pair<ServerPlayerEntity?, Boolean>? {
        val server = server ?: return null
        var requestedPlayer = server.playerManager.getPlayer(playerUUID)
        val isDisconnected = requestedPlayer == null

        if (requestedPlayer == null && playerUUID != null) {
            requestedPlayer =
                server.playerManager.createPlayer(GameProfile(playerUUID, ""), SyncedClientOptions.createDefault())
            val compoundOpt = server.playerManager.loadPlayerData(requestedPlayer)
            if (compoundOpt.isPresent) {
                val compound = compoundOpt.get()
                if (compound.contains("Dimension")) {
                    val world = server.getWorld(
                        DimensionType.worldFromDimensionNbt(Dynamic(NbtOps.INSTANCE, compound["Dimension"])).result()
                            .get()
                    )

                    if (world != null) {
                        (requestedPlayer as EntityAccessor).setWorld(world)
                    }
                }
            }
        }
        return requestedPlayer to isDisconnected
    }

    override fun getDisplayName(): Text? {
        val corpsePair = forceGetPlayer()
        if (corpsePair?.first == null) return super.getDisplayName()
        val corpse = corpsePair.first!!
        return playerDeathText ?: corpse.name
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? {
        val corpsePair = forceGetPlayer()
        if (corpsePair?.first == null) return null
        val corpse = corpsePair.first!!
        return PlayerInventoryMenu(ScreenHandlerType.GENERIC_9X6, syncId, corpse, playerInventory)
    }

    override fun getPolymerEntityType(player: ServerPlayerEntity): EntityType<*> {
        return EntityType.WANDERING_TRADER
    }
}