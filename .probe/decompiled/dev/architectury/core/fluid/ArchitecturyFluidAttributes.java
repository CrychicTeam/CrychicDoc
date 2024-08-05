package dev.architectury.core.fluid;

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public interface ArchitecturyFluidAttributes {

    @Nullable
    String getTranslationKey(@Nullable FluidStack var1);

    @Nullable
    default String getTranslationKey() {
        return this.getTranslationKey(null);
    }

    default Component getName(@Nullable FluidStack stack) {
        return Component.translatable(this.getTranslationKey(stack));
    }

    default Component getName() {
        return this.getName(null);
    }

    Fluid getFlowingFluid();

    Fluid getSourceFluid();

    boolean canConvertToSource();

    int getSlopeFindDistance(@Nullable LevelReader var1);

    default int getSlopeFindDistance() {
        return this.getSlopeFindDistance(null);
    }

    int getDropOff(@Nullable LevelReader var1);

    default int getDropOff() {
        return this.getDropOff(null);
    }

    @Nullable
    Item getBucketItem();

    int getTickDelay(@Nullable LevelReader var1);

    default int getTickDelay() {
        return this.getTickDelay(null);
    }

    float getExplosionResistance();

    @Nullable
    LiquidBlock getBlock();

    @Deprecated(forRemoval = true)
    ResourceLocation getSourceTexture(@Nullable FluidStack var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3);

    default ResourceLocation getSourceTexture(@Nullable FluidState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.getSourceTexture(state == null ? null : FluidStack.create(state.getType(), FluidStack.bucketAmount()), level, pos);
    }

    default ResourceLocation getSourceTexture(@Nullable FluidStack stack) {
        return this.getSourceTexture(stack, null, null);
    }

    default ResourceLocation getSourceTexture() {
        return this.getSourceTexture(null);
    }

    @Deprecated(forRemoval = true)
    ResourceLocation getFlowingTexture(@Nullable FluidStack var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3);

    default ResourceLocation getFlowingTexture(@Nullable FluidState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.getFlowingTexture(state == null ? null : FluidStack.create(state.getType(), FluidStack.bucketAmount()), level, pos);
    }

    default ResourceLocation getFlowingTexture(@Nullable FluidStack stack) {
        return this.getFlowingTexture(stack, null, null);
    }

    default ResourceLocation getFlowingTexture() {
        return this.getFlowingTexture(null);
    }

    @Nullable
    default ResourceLocation getOverlayTexture(@Nullable FluidState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return null;
    }

    @Nullable
    default ResourceLocation getOverlayTexture(@Nullable FluidStack stack) {
        return null;
    }

    @Nullable
    default ResourceLocation getOverlayTexture() {
        return this.getOverlayTexture(null);
    }

    @Deprecated(forRemoval = true)
    int getColor(@Nullable FluidStack var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3);

    default int getColor(@Nullable FluidState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.getColor(state == null ? null : FluidStack.create(state.getType(), FluidStack.bucketAmount()), level, pos);
    }

    default int getColor(@Nullable FluidStack stack) {
        return this.getColor(stack, null, null);
    }

    default int getColor() {
        return this.getColor(null);
    }

    int getLuminosity(@Nullable FluidStack var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3);

    default int getLuminosity(@Nullable FluidStack stack) {
        return this.getLuminosity(stack, null, null);
    }

    default int getLuminosity() {
        return this.getLuminosity(null);
    }

    int getDensity(@Nullable FluidStack var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3);

    default int getDensity(@Nullable FluidStack stack) {
        return this.getDensity(stack, null, null);
    }

    default int getDensity() {
        return this.getDensity(null);
    }

    int getTemperature(@Nullable FluidStack var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3);

    default int getTemperature(@Nullable FluidStack stack) {
        return this.getTemperature(stack, null, null);
    }

    default int getTemperature() {
        return this.getTemperature(null);
    }

    int getViscosity(@Nullable FluidStack var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3);

    default int getViscosity(@Nullable FluidStack stack) {
        return this.getViscosity(stack, null, null);
    }

    default int getViscosity() {
        return this.getViscosity(null);
    }

    boolean isLighterThanAir(@Nullable FluidStack var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3);

    default boolean isLighterThanAir(@Nullable FluidStack stack) {
        return this.isLighterThanAir(stack, null, null);
    }

    default boolean isLighterThanAir() {
        return this.isLighterThanAir(null);
    }

    Rarity getRarity(@Nullable FluidStack var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3);

    default Rarity getRarity(@Nullable FluidStack stack) {
        return this.getRarity(stack, null, null);
    }

    default Rarity getRarity() {
        return this.getRarity(null);
    }

    @Nullable
    SoundEvent getFillSound(@Nullable FluidStack var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3);

    @Nullable
    default SoundEvent getFillSound(@Nullable FluidStack stack) {
        return this.getFillSound(stack, null, null);
    }

    @Nullable
    default SoundEvent getFillSound() {
        return this.getFillSound(null);
    }

    @Nullable
    SoundEvent getEmptySound(@Nullable FluidStack var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3);

    @Nullable
    default SoundEvent getEmptySound(@Nullable FluidStack stack) {
        return this.getEmptySound(stack, null, null);
    }

    @Nullable
    default SoundEvent getEmptySound() {
        return this.getEmptySound(null);
    }
}