package net.blay09.mods.balm.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public interface BalmHooks {

    boolean blockGrowFeature(Level var1, RandomSource var2, BlockPos var3, @Nullable Holder<ConfiguredFeature<?, ?>> var4);

    boolean growCrop(ItemStack var1, Level var2, BlockPos var3, @Nullable Player var4);

    default CompoundTag getPersistentData(Player player) {
        return this.getPersistentData((Entity) player);
    }

    CompoundTag getPersistentData(Entity var1);

    void curePotionEffects(LivingEntity var1, ItemStack var2);

    boolean isFakePlayer(Player var1);

    ItemStack getCraftingRemainingItem(ItemStack var1);

    DyeColor getColor(ItemStack var1);

    boolean canItemsStack(ItemStack var1, ItemStack var2);

    int getBurnTime(ItemStack var1);

    void setBurnTime(Item var1, int var2);

    void firePlayerCraftingEvent(Player var1, ItemStack var2, Container var3);

    boolean useFluidTank(BlockState var1, Level var2, BlockPos var3, Player var4, InteractionHand var5, BlockHitResult var6);

    boolean isShield(ItemStack var1);

    boolean isRepairable(ItemStack var1);

    void setForcedPose(Player var1, Pose var2);

    MinecraftServer getServer();

    double getBlockReachDistance(Player var1);
}