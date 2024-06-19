package dev.somichev.block

import dev.somichev.RadioLampEngine
import dev.somichev.block.custom_blocks.CustomBlock
import dev.somichev.block.custom_blocks.SimpleBlockModel
import eu.pb4.polymer.resourcepack.api.PolymerModelData
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import eu.pb4.polymer.virtualentity.api.ElementHolder
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particle.DustParticleEffect
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.Hand
import net.minecraft.util.ItemActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import org.joml.Vector3f

class KalykBlock: Block(settings), CustomBlock {
    data class KalykFuel(val item: Item, val effects: List<RegistryEntry<StatusEffect>>, val smokeColor: Vector3f)
    companion object{
        val id = RadioLampEngine.id("kalyk")
        val model = PolymerResourcePackUtils.requestModel(Items.BRICK_WALL, id.withPrefixedPath("block/"))
        val block = Blocks.BRICK_WALL
        val shape = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
        val settings = Settings.create()
            .strength(1.0f)
            .breakInstantly()
            .ticksRandomly()
            .sounds(BlockSoundGroup.COPPER)
            .pistonBehavior(PistonBehavior.DESTROY)
        val availableFuels = listOf(
            KalykFuel(Items.CARROT, listOf(
                StatusEffects.REGENERATION,
                StatusEffects.STRENGTH,
                StatusEffects.HUNGER
            ), Vector3f(50f/255f,206f/255f,70f/255f)),
            KalykFuel(Items.APPLE, listOf(
                StatusEffects.REGENERATION,
                StatusEffects.ABSORPTION,
                StatusEffects.MINING_FATIGUE
            ), Vector3f(253f/255f,166f/255f,34f/255f)),
            KalykFuel(Items.GLOW_BERRIES, listOf(
                StatusEffects.REGENERATION,
                StatusEffects.HASTE,
                StatusEffects.GLOWING
            ), Vector3f(255f/255f,237f/255f,13f/255f)),
            KalykFuel(Items.HONEYCOMB, listOf(
                StatusEffects.LUCK
            ), Vector3f(0f,0f,0f)),
        )
        val FUEL: IntProperty = IntProperty.of("fuel", 0, availableFuels.size)
    }
    init{
        defaultState = defaultState.with(FUEL, 0)
    }
    override fun onUseWithItem(
        stack: ItemStack,
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ItemActionResult {
        val selectedFuelIndex =
            availableFuels.indexOfFirst { stack.item == it.item } + 1
        if(selectedFuelIndex == 0) return ItemActionResult.FAIL
        world.setBlockState(pos, state.with(FUEL, selectedFuelIndex))
        world.playSound(
            null,
            pos,
            SoundEvents.ENTITY_BLAZE_SHOOT,
            SoundCategory.BLOCKS,
            1.0f,
            1.0f
        )
        return ItemActionResult.SUCCESS
    }

    override fun randomTick(
        state: BlockState,
        world: ServerWorld,
        pos: BlockPos,
        random: Random
    ) {
        val fuel = state.get(FUEL)
        spawnColoredSmoke(state, world, pos)
        if(fuel == 0) return
        val entities = world.getEntitiesByType(
            EntityType.PLAYER,
            Box.of(pos.toCenterPos(), 5.0, 5.0, 5.0),
            EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR
        )
        val effects = availableFuels[fuel].effects
            entities.forEach { e ->
                effects.forEach{ e.addStatusEffect(StatusEffectInstance(it, 20*60, 0)) }
        }
    }
    private fun spawnColoredSmoke(
        state: BlockState,
        world: ServerWorld,
        pos: BlockPos,
    ){
        // выбор цвета
        val fuel = state.get(FUEL)
        val color: Vector3f = if(fuel == 0) {
            Vector3f(0f)
        }
        else {
            availableFuels[fuel].smokeColor
        }
        val dustParticle = DustParticleEffect(color, 3f)
        world.spawnParticles(
            dustParticle,
            pos.x.toDouble() + 0.5,
            pos.y.toDouble() + 1.2,
            pos.z.toDouble() + 0.5,
            5,
            0.1,
            0.1,
            0.1,
            0.1
        )
    }
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FUEL)
    }
    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = shape

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = shape

    override fun getRaycastShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape  = shape
    override fun getSidesShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape = shape
    override fun getPolymerBlockState(state: BlockState?): BlockState {
        return Blocks.BARRIER.getStateWithProperties(state)
    }

    override fun getItemModel(state: BlockState): PolymerModelData = model
    override fun createElementHolder(
        world: ServerWorld?,
        pos: BlockPos?,
        initialBlockState: BlockState?
    ): ElementHolder = SimpleBlockModel(model)
}