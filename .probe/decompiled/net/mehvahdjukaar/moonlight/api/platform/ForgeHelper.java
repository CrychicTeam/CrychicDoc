package net.mehvahdjukaar.moonlight.api.platform;

import com.mojang.serialization.DynamicOps;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.platform.forge.ForgeHelperImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class ForgeHelper {

    @Deprecated(forRemoval = true)
    @ExpectPlatform
    @Transformed
    public static FinishedRecipe addRecipeConditions(FinishedRecipe originalRecipe, List<Object> conditions) {
        return ForgeHelperImpl.addRecipeConditions(originalRecipe, conditions);
    }

    public static <T> DynamicOps<T> addConditionOps(DynamicOps<T> ops) {
        return ops;
    }

    @ExpectPlatform
    @Transformed
    public static boolean onProjectileImpact(Projectile improvedProjectileEntity, HitResult blockHitResult) {
        return ForgeHelperImpl.onProjectileImpact(improvedProjectileEntity, blockHitResult);
    }

    @ExpectPlatform
    @Transformed
    public static boolean isCurativeItem(ItemStack stack, MobEffectInstance effect) {
        return ForgeHelperImpl.isCurativeItem(stack, effect);
    }

    @ExpectPlatform
    @Transformed
    public static boolean canHarvestBlock(BlockState state, ServerLevel level, BlockPos pos, ServerPlayer player) {
        return ForgeHelperImpl.canHarvestBlock(state, level, pos, player);
    }

    @ExpectPlatform
    @Transformed
    public static float getFriction(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return ForgeHelperImpl.getFriction(state, level, pos, entity);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static boolean canEquipItem(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        return ForgeHelperImpl.canEquipItem(entity, stack, slot);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static boolean canEntityDestroy(Level level, BlockPos blockPos, Animal animal) {
        return ForgeHelperImpl.canEntityDestroy(level, blockPos, animal);
    }

    @ExpectPlatform
    @Transformed
    public static boolean onExplosionStart(Level level, Explosion explosion) {
        return ForgeHelperImpl.onExplosionStart(level, explosion);
    }

    @ExpectPlatform
    @Transformed
    public static void onLivingConvert(LivingEntity frFom, LivingEntity to) {
        ForgeHelperImpl.onLivingConvert(frFom, to);
    }

    @ExpectPlatform
    @Transformed
    public static boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, Consumer<Integer> timer) {
        return ForgeHelperImpl.canLivingConvert(entity, outcome, timer);
    }

    @ExpectPlatform
    @Transformed
    public static void onExplosionDetonate(Level level, Explosion explosion, List<Entity> entities, double diameter) {
        ForgeHelperImpl.onExplosionDetonate(level, explosion, entities, diameter);
    }

    @ExpectPlatform
    @Transformed
    public static double getReachDistance(LivingEntity entity) {
        return ForgeHelperImpl.getReachDistance(entity);
    }

    @ExpectPlatform
    @Transformed
    public static float getExplosionResistance(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        return ForgeHelperImpl.getExplosionResistance(state, level, pos, explosion);
    }

    @ExpectPlatform
    @Transformed
    public static void onBlockExploded(BlockState blockstate, Level level, BlockPos blockpos, Explosion explosion) {
        ForgeHelperImpl.onBlockExploded(blockstate, level, blockpos, explosion);
    }

    @ExpectPlatform
    @Transformed
    public static boolean areStacksEqual(ItemStack stack, ItemStack other, boolean sameNbt) {
        return ForgeHelperImpl.areStacksEqual(stack, other, sameNbt);
    }

    @ExpectPlatform
    @Transformed
    public static boolean isFireSource(BlockState blockState, Level level, BlockPos pos, Direction up) {
        return ForgeHelperImpl.isFireSource(blockState, level, pos, up);
    }

    @ExpectPlatform
    @Transformed
    public static boolean canDropFromExplosion(BlockState blockstate, Level level, BlockPos blockpos, Explosion explosion) {
        return ForgeHelperImpl.canDropFromExplosion(blockstate, level, blockpos, explosion);
    }

    @ExpectPlatform
    @Transformed
    public static boolean isDye(ItemStack itemstack) {
        return ForgeHelperImpl.isDye(itemstack);
    }

    @Nullable
    @ExpectPlatform
    @Transformed
    public static DyeColor getColor(ItemStack stack) {
        return ForgeHelperImpl.getColor(stack);
    }

    @ExpectPlatform
    @Transformed
    public static BlockState rotateBlock(BlockState state, Level world, BlockPos targetPos, Rotation rot) {
        return ForgeHelperImpl.rotateBlock(state, world, targetPos, rot);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static boolean isMultipartEntity(Entity e) {
        return ForgeHelperImpl.isMultipartEntity(e);
    }

    @ExpectPlatform
    @Transformed
    public static void setPoolName(LootPool.Builder pool, String name) {
        ForgeHelperImpl.setPoolName(pool, name);
    }

    @ExpectPlatform
    @Transformed
    public static RailShape getRailDirection(BaseRailBlock railBlock, BlockState blockstate, Level level, BlockPos blockpos, @Nullable AbstractMinecart o) {
        return ForgeHelperImpl.getRailDirection(railBlock, blockstate, level, blockpos, o);
    }

    @ExpectPlatform
    @Transformed
    public static Optional<ItemStack> getCraftingRemainingItem(ItemStack itemstack) {
        return ForgeHelperImpl.getCraftingRemainingItem(itemstack);
    }

    @ExpectPlatform
    @Transformed
    public static void reviveEntity(Entity entity) {
        ForgeHelperImpl.reviveEntity(entity);
    }

    @ExpectPlatform
    @Transformed
    public static boolean onCropsGrowPre(ServerLevel level, BlockPos pos, BlockState state, boolean b) {
        return ForgeHelperImpl.onCropsGrowPre(level, pos, state, b);
    }

    @ExpectPlatform
    @Transformed
    public static void onCropsGrowPost(ServerLevel level, BlockPos pos, BlockState state) {
        ForgeHelperImpl.onCropsGrowPost(level, pos, state);
    }

    @ExpectPlatform
    @Transformed
    public static void onEquipmentChange(LivingEntity entity, EquipmentSlot slot, ItemStack from, ItemStack to) {
        ForgeHelperImpl.onEquipmentChange(entity, slot, from, to);
    }

    @ExpectPlatform
    @Nullable
    @Transformed
    public static InteractionResult onRightClickBlock(Player player, InteractionHand hand, BlockPos below, BlockHitResult rayTraceResult) {
        return ForgeHelperImpl.onRightClickBlock(player, hand, below, rayTraceResult);
    }

    @ExpectPlatform
    @Transformed
    public static boolean canItemStack(ItemStack i, ItemStack i1) {
        return ForgeHelperImpl.canItemStack(i, i1);
    }

    @ExpectPlatform
    @Transformed
    public static int getLightEmission(BlockState state, Level level, BlockPos pos) {
        return ForgeHelperImpl.getLightEmission(state, level, pos);
    }

    @ExpectPlatform
    @Transformed
    public static Map<Block, Item> getBlockItemMap() {
        return ForgeHelperImpl.getBlockItemMap();
    }
}