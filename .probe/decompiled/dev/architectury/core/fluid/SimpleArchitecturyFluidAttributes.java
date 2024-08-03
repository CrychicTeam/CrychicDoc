package dev.architectury.core.fluid;

import com.google.common.base.Suppliers;
import dev.architectury.fluid.FluidStack;
import dev.architectury.registry.registries.RegistrySupplier;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class SimpleArchitecturyFluidAttributes implements ArchitecturyFluidAttributes {

    private final Supplier<? extends Fluid> flowingFluid;

    private final Supplier<? extends Fluid> sourceFluid;

    private boolean canConvertToSource = false;

    private int slopeFindDistance = 4;

    private int dropOff = 1;

    private Supplier<? extends Optional<Item>> bucketItem = Optional::empty;

    private int tickDelay = 5;

    private float explosionResistance = 100.0F;

    private Supplier<? extends Optional<? extends LiquidBlock>> block = Optional::empty;

    @Nullable
    private ResourceLocation sourceTexture;

    @Nullable
    private ResourceLocation flowingTexture;

    @Nullable
    private ResourceLocation overlayTexture;

    private int color = 16777215;

    private int luminosity = 0;

    private int density = 1000;

    private int temperature = 300;

    private int viscosity = 1000;

    private boolean lighterThanAir = false;

    private Rarity rarity = Rarity.COMMON;

    @Nullable
    private SoundEvent fillSound = SoundEvents.BUCKET_FILL;

    @Nullable
    private SoundEvent emptySound = SoundEvents.BUCKET_EMPTY;

    private final Supplier<String> defaultTranslationKey = Suppliers.memoize(() -> Util.makeDescriptionId("fluid", this.getSourceFluid().arch$registryName()));

    public static SimpleArchitecturyFluidAttributes ofSupplier(Supplier<? extends Supplier<? extends Fluid>> flowingFluid, Supplier<? extends Supplier<? extends Fluid>> sourceFluid) {
        return of(() -> (Fluid) ((Supplier) flowingFluid.get()).get(), () -> (Fluid) ((Supplier) sourceFluid.get()).get());
    }

    public static SimpleArchitecturyFluidAttributes of(Supplier<? extends Fluid> flowingFluid, Supplier<? extends Fluid> sourceFluid) {
        return new SimpleArchitecturyFluidAttributes(flowingFluid, sourceFluid);
    }

    protected SimpleArchitecturyFluidAttributes(Supplier<? extends Fluid> flowingFluid, Supplier<? extends Fluid> sourceFluid) {
        this.flowingFluid = flowingFluid;
        this.sourceFluid = sourceFluid;
    }

    public SimpleArchitecturyFluidAttributes convertToSource(boolean canConvertToSource) {
        this.canConvertToSource = canConvertToSource;
        return this;
    }

    public SimpleArchitecturyFluidAttributes slopeFindDistance(int slopeFindDistance) {
        this.slopeFindDistance = slopeFindDistance;
        return this;
    }

    public SimpleArchitecturyFluidAttributes dropOff(int dropOff) {
        this.dropOff = dropOff;
        return this;
    }

    public SimpleArchitecturyFluidAttributes bucketItemSupplier(Supplier<RegistrySupplier<Item>> bucketItem) {
        return this.bucketItem(() -> ((RegistrySupplier) bucketItem.get()).toOptional());
    }

    public SimpleArchitecturyFluidAttributes bucketItem(RegistrySupplier<Item> bucketItem) {
        return this.bucketItem(bucketItem::toOptional);
    }

    public SimpleArchitecturyFluidAttributes bucketItem(Supplier<? extends Optional<Item>> bucketItem) {
        this.bucketItem = (Supplier<? extends Optional<Item>>) Objects.requireNonNull(bucketItem);
        return this;
    }

    public SimpleArchitecturyFluidAttributes tickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
        return this;
    }

    public SimpleArchitecturyFluidAttributes explosionResistance(float explosionResistance) {
        this.explosionResistance = explosionResistance;
        return this;
    }

    public SimpleArchitecturyFluidAttributes blockSupplier(Supplier<RegistrySupplier<? extends LiquidBlock>> block) {
        return this.block(() -> ((RegistrySupplier) block.get()).toOptional());
    }

    public SimpleArchitecturyFluidAttributes block(RegistrySupplier<? extends LiquidBlock> block) {
        return this.block(block::toOptional);
    }

    public SimpleArchitecturyFluidAttributes block(Supplier<? extends Optional<? extends LiquidBlock>> block) {
        this.block = (Supplier<? extends Optional<? extends LiquidBlock>>) Objects.requireNonNull(block);
        return this;
    }

    public SimpleArchitecturyFluidAttributes sourceTexture(ResourceLocation sourceTexture) {
        this.sourceTexture = sourceTexture;
        return this;
    }

    public SimpleArchitecturyFluidAttributes flowingTexture(ResourceLocation flowingTexture) {
        this.flowingTexture = flowingTexture;
        return this;
    }

    public SimpleArchitecturyFluidAttributes overlayTexture(ResourceLocation overlayTexture) {
        this.overlayTexture = overlayTexture;
        return this;
    }

    public SimpleArchitecturyFluidAttributes color(int color) {
        this.color = color;
        return this;
    }

    public SimpleArchitecturyFluidAttributes luminosity(int luminosity) {
        this.luminosity = luminosity;
        return this;
    }

    public SimpleArchitecturyFluidAttributes density(int density) {
        this.density = density;
        return this;
    }

    public SimpleArchitecturyFluidAttributes temperature(int temperature) {
        this.temperature = temperature;
        return this;
    }

    public SimpleArchitecturyFluidAttributes viscosity(int viscosity) {
        this.viscosity = viscosity;
        return this;
    }

    public SimpleArchitecturyFluidAttributes lighterThanAir(boolean lighterThanAir) {
        this.lighterThanAir = lighterThanAir;
        return this;
    }

    public SimpleArchitecturyFluidAttributes rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public SimpleArchitecturyFluidAttributes fillSound(SoundEvent fillSound) {
        this.fillSound = fillSound;
        return this;
    }

    public SimpleArchitecturyFluidAttributes emptySound(SoundEvent emptySound) {
        this.emptySound = emptySound;
        return this;
    }

    @Nullable
    @Override
    public String getTranslationKey(@Nullable FluidStack stack) {
        return (String) this.defaultTranslationKey.get();
    }

    @Override
    public final Fluid getFlowingFluid() {
        return (Fluid) this.flowingFluid.get();
    }

    @Override
    public final Fluid getSourceFluid() {
        return (Fluid) this.sourceFluid.get();
    }

    @Override
    public boolean canConvertToSource() {
        return this.canConvertToSource;
    }

    @Override
    public int getSlopeFindDistance(@Nullable LevelReader level) {
        return this.slopeFindDistance;
    }

    @Override
    public int getDropOff(@Nullable LevelReader level) {
        return this.dropOff;
    }

    @Nullable
    @Override
    public Item getBucketItem() {
        return (Item) ((Optional) this.bucketItem.get()).orElse(null);
    }

    @Override
    public int getTickDelay(@Nullable LevelReader level) {
        return this.tickDelay;
    }

    @Override
    public float getExplosionResistance() {
        return this.explosionResistance;
    }

    @Nullable
    @Override
    public LiquidBlock getBlock() {
        return (LiquidBlock) ((Optional) this.block.get()).orElse(null);
    }

    @Override
    public ResourceLocation getSourceTexture(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.sourceTexture;
    }

    @Override
    public ResourceLocation getFlowingTexture(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.flowingTexture;
    }

    @Override
    public ResourceLocation getOverlayTexture(@Nullable FluidState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.overlayTexture;
    }

    @Override
    public int getColor(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.color;
    }

    @Override
    public int getLuminosity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.luminosity;
    }

    @Override
    public int getDensity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.density;
    }

    @Override
    public int getTemperature(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.temperature;
    }

    @Override
    public int getViscosity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.viscosity;
    }

    @Override
    public boolean isLighterThanAir(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.lighterThanAir;
    }

    @Override
    public Rarity getRarity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.rarity;
    }

    @Nullable
    @Override
    public SoundEvent getFillSound(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.fillSound;
    }

    @Nullable
    @Override
    public SoundEvent getEmptySound(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return this.emptySound;
    }
}