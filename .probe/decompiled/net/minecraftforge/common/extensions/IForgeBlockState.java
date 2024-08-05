package net.minecraftforge.common.extensions;

import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IForgeBlockState {

    private BlockState self() {
        return (BlockState) this;
    }

    default float getFriction(LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return this.self().m_60734_().getFriction(this.self(), level, pos, entity);
    }

    default int getLightEmission(BlockGetter level, BlockPos pos) {
        return this.self().m_60734_().getLightEmission(this.self(), level, pos);
    }

    default boolean isLadder(LevelReader level, BlockPos pos, LivingEntity entity) {
        return this.self().m_60734_().isLadder(this.self(), level, pos, entity);
    }

    default boolean canHarvestBlock(BlockGetter level, BlockPos pos, Player player) {
        return this.self().m_60734_().canHarvestBlock(this.self(), level, pos, player);
    }

    default boolean onDestroyedByPlayer(Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return this.self().m_60734_().onDestroyedByPlayer(this.self(), level, pos, player, willHarvest, fluid);
    }

    default boolean isBed(BlockGetter level, BlockPos pos, @Nullable LivingEntity sleeper) {
        return this.self().m_60734_().isBed(this.self(), level, pos, sleeper);
    }

    default boolean isValidSpawn(LevelReader level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
        return this.self().m_60734_().isValidSpawn(this.self(), level, pos, type, entityType);
    }

    default Optional<Vec3> getRespawnPosition(EntityType<?> type, LevelReader level, BlockPos pos, float orientation, @Nullable LivingEntity entity) {
        return this.self().m_60734_().getRespawnPosition(this.self(), type, level, pos, orientation, entity);
    }

    default void setBedOccupied(Level level, BlockPos pos, LivingEntity sleeper, boolean occupied) {
        this.self().m_60734_().setBedOccupied(this.self(), level, pos, sleeper, occupied);
    }

    default Direction getBedDirection(LevelReader level, BlockPos pos) {
        return this.self().m_60734_().getBedDirection(this.self(), level, pos);
    }

    default float getExplosionResistance(BlockGetter level, BlockPos pos, Explosion explosion) {
        return this.self().m_60734_().getExplosionResistance(this.self(), level, pos, explosion);
    }

    default ItemStack getCloneItemStack(HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return this.self().m_60734_().getCloneItemStack(this.self(), target, level, pos, player);
    }

    default boolean addLandingEffects(ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        return this.self().m_60734_().addLandingEffects(this.self(), level, pos, state2, entity, numberOfParticles);
    }

    default boolean addRunningEffects(Level level, BlockPos pos, Entity entity) {
        return this.self().m_60734_().addRunningEffects(this.self(), level, pos, entity);
    }

    default boolean canSustainPlant(BlockGetter level, BlockPos pos, Direction facing, IPlantable plantable) {
        return this.self().m_60734_().canSustainPlant(this.self(), level, pos, facing, plantable);
    }

    default boolean onTreeGrow(LevelReader level, BiConsumer<BlockPos, BlockState> placeFunction, RandomSource randomSource, BlockPos pos, TreeConfiguration config) {
        return this.self().m_60734_().onTreeGrow(this.self(), level, placeFunction, randomSource, pos, config);
    }

    default boolean isFertile(BlockGetter level, BlockPos pos) {
        return this.self().m_60734_().isFertile(this.self(), level, pos);
    }

    default boolean isConduitFrame(LevelReader level, BlockPos pos, BlockPos conduit) {
        return this.self().m_60734_().isConduitFrame(this.self(), level, pos, conduit);
    }

    default boolean isPortalFrame(BlockGetter level, BlockPos pos) {
        return this.self().m_60734_().isPortalFrame(this.self(), level, pos);
    }

    default int getExpDrop(LevelReader level, RandomSource randomSource, BlockPos pos, int fortuneLevel, int silkTouchLevel) {
        return this.self().m_60734_().getExpDrop(this.self(), level, randomSource, pos, fortuneLevel, silkTouchLevel);
    }

    default BlockState rotate(LevelAccessor level, BlockPos pos, Rotation direction) {
        return this.self().m_60734_().rotate(this.self(), level, pos, direction);
    }

    default float getEnchantPowerBonus(LevelReader level, BlockPos pos) {
        return this.self().m_60734_().getEnchantPowerBonus(this.self(), level, pos);
    }

    default void onNeighborChange(LevelReader level, BlockPos pos, BlockPos neighbor) {
        this.self().m_60734_().onNeighborChange(this.self(), level, pos, neighbor);
    }

    default boolean shouldCheckWeakPower(SignalGetter level, BlockPos pos, Direction side) {
        return this.self().m_60734_().shouldCheckWeakPower(this.self(), level, pos, side);
    }

    default boolean getWeakChanges(LevelReader level, BlockPos pos) {
        return this.self().m_60734_().getWeakChanges(this.self(), level, pos);
    }

    default SoundType getSoundType(LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return this.self().m_60734_().getSoundType(this.self(), level, pos, entity);
    }

    @Nullable
    default float[] getBeaconColorMultiplier(LevelReader level, BlockPos pos, BlockPos beacon) {
        return this.self().m_60734_().getBeaconColorMultiplier(this.self(), level, pos, beacon);
    }

    default BlockState getStateAtViewpoint(BlockGetter level, BlockPos pos, Vec3 viewpoint) {
        return this.self().m_60734_().getStateAtViewpoint(this.self(), level, pos, viewpoint);
    }

    default boolean isSlimeBlock() {
        return this.self().m_60734_().isSlimeBlock(this.self());
    }

    default boolean isStickyBlock() {
        return this.self().m_60734_().isStickyBlock(this.self());
    }

    default boolean canStickTo(@NotNull BlockState other) {
        return this.self().m_60734_().canStickTo(this.self(), other);
    }

    default int getFlammability(BlockGetter level, BlockPos pos, Direction face) {
        return this.self().m_60734_().getFlammability(this.self(), level, pos, face);
    }

    default boolean isFlammable(BlockGetter level, BlockPos pos, Direction face) {
        return this.self().m_60734_().isFlammable(this.self(), level, pos, face);
    }

    default void onCaughtFire(Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        this.self().m_60734_().onCaughtFire(this.self(), level, pos, face, igniter);
    }

    default int getFireSpreadSpeed(BlockGetter level, BlockPos pos, Direction face) {
        return this.self().m_60734_().getFireSpreadSpeed(this.self(), level, pos, face);
    }

    default boolean isFireSource(LevelReader level, BlockPos pos, Direction side) {
        return this.self().m_60734_().isFireSource(this.self(), level, pos, side);
    }

    default boolean canEntityDestroy(BlockGetter level, BlockPos pos, Entity entity) {
        return this.self().m_60734_().canEntityDestroy(this.self(), level, pos, entity);
    }

    default boolean isBurning(BlockGetter level, BlockPos pos) {
        return this.self().m_60734_().isBurning(this.self(), level, pos);
    }

    @Nullable
    default BlockPathTypes getBlockPathType(BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return this.self().m_60734_().getBlockPathType(this.self(), level, pos, mob);
    }

    @Nullable
    default BlockPathTypes getAdjacentBlockPathType(BlockGetter level, BlockPos pos, @Nullable Mob mob, BlockPathTypes originalType) {
        return this.self().m_60734_().getAdjacentBlockPathType(this.self(), level, pos, mob, originalType);
    }

    default boolean canDropFromExplosion(BlockGetter level, BlockPos pos, Explosion explosion) {
        return this.self().m_60734_().canDropFromExplosion(this.self(), level, pos, explosion);
    }

    default void onBlockExploded(Level level, BlockPos pos, Explosion explosion) {
        this.self().m_60734_().onBlockExploded(this.self(), level, pos, explosion);
    }

    default boolean collisionExtendsVertically(BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return this.self().m_60734_().collisionExtendsVertically(this.self(), level, pos, collidingEntity);
    }

    default boolean shouldDisplayFluidOverlay(BlockAndTintGetter level, BlockPos pos, FluidState fluidState) {
        return this.self().m_60734_().shouldDisplayFluidOverlay(this.self(), level, pos, fluidState);
    }

    @Nullable
    default BlockState getToolModifiedState(UseOnContext context, ToolAction toolAction, boolean simulate) {
        BlockState eventState = ForgeEventFactory.onToolUse(this.self(), context, toolAction, simulate);
        return eventState != this.self() ? eventState : this.self().m_60734_().getToolModifiedState(this.self(), context, toolAction, simulate);
    }

    default boolean isScaffolding(LivingEntity entity) {
        return this.self().m_60734_().isScaffolding(this.self(), entity.m_9236_(), entity.m_20183_(), entity);
    }

    default boolean canRedstoneConnectTo(BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return this.self().m_60734_().canConnectRedstone(this.self(), level, pos, direction);
    }

    default boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState neighborState, Direction dir) {
        return this.self().m_60734_().hidesNeighborFace(level, pos, this.self(), neighborState, dir);
    }

    default boolean supportsExternalFaceHiding() {
        return this.self().m_60734_().supportsExternalFaceHiding(this.self());
    }

    default void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState) {
        this.self().m_60734_().onBlockStateChange(level, pos, oldState, this.self());
    }

    default boolean canBeHydrated(BlockGetter getter, BlockPos pos, FluidState fluid, BlockPos fluidPos) {
        return this.self().m_60734_().canBeHydrated(this.self(), getter, pos, fluid, fluidPos);
    }

    default BlockState getAppearance(BlockAndTintGetter level, BlockPos pos, Direction side, @Nullable BlockState queryState, @Nullable BlockPos queryPos) {
        return this.self().m_60734_().getAppearance(this.self(), level, pos, side, queryState, queryPos);
    }
}