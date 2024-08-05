package net.minecraftforge.fluids;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

public class FluidType {

    public static final int BUCKET_VOLUME = 1000;

    public static final Lazy<Integer> SIZE = Lazy.of(() -> ((IForgeRegistry) ForgeRegistries.FLUID_TYPES.get()).getKeys().size());

    private String descriptionId;

    private final double motionScale;

    private final boolean canPushEntity;

    private final boolean canSwim;

    private final boolean canDrown;

    private final float fallDistanceModifier;

    private final boolean canExtinguish;

    private final boolean canConvertToSource;

    private final boolean supportsBoating;

    @Nullable
    private final BlockPathTypes pathType;

    @Nullable
    private final BlockPathTypes adjacentPathType;

    private final boolean canHydrate;

    private final int lightLevel;

    private final int density;

    private final int temperature;

    private final int viscosity;

    private final Rarity rarity;

    protected final Map<SoundAction, SoundEvent> sounds;

    private Object renderProperties;

    public FluidType(FluidType.Properties properties) {
        this.descriptionId = properties.descriptionId;
        this.motionScale = properties.motionScale;
        this.canPushEntity = properties.canPushEntity;
        this.canSwim = properties.canSwim;
        this.canDrown = properties.canDrown;
        this.fallDistanceModifier = properties.fallDistanceModifier;
        this.canExtinguish = properties.canExtinguish;
        this.canConvertToSource = properties.canConvertToSource;
        this.supportsBoating = properties.supportsBoating;
        this.pathType = properties.pathType;
        this.adjacentPathType = properties.adjacentPathType;
        this.sounds = ImmutableMap.copyOf(properties.sounds);
        this.canHydrate = properties.canHydrate;
        this.lightLevel = properties.lightLevel;
        this.density = properties.density;
        this.temperature = properties.temperature;
        this.viscosity = properties.viscosity;
        this.rarity = properties.rarity;
        this.initClient();
    }

    public Component getDescription() {
        return Component.translatable(this.getDescriptionId());
    }

    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("fluid_type", ((IForgeRegistry) ForgeRegistries.FLUID_TYPES.get()).getKey(this));
        }
        return this.descriptionId;
    }

    public int getLightLevel() {
        return this.lightLevel;
    }

    public int getDensity() {
        return this.density;
    }

    public int getTemperature() {
        return this.temperature;
    }

    public int getViscosity() {
        return this.viscosity;
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    @Nullable
    public SoundEvent getSound(SoundAction action) {
        return (SoundEvent) this.sounds.get(action);
    }

    public double motionScale(Entity entity) {
        return this.motionScale;
    }

    public boolean canPushEntity(Entity entity) {
        return this.canPushEntity;
    }

    public boolean canSwim(Entity entity) {
        return this.canSwim;
    }

    public float getFallDistanceModifier(Entity entity) {
        return this.fallDistanceModifier;
    }

    public boolean canExtinguish(Entity entity) {
        return this.canExtinguish;
    }

    public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity) {
        return false;
    }

    public boolean canDrownIn(LivingEntity entity) {
        return this.canDrown;
    }

    public void setItemMovement(ItemEntity entity) {
        Vec3 vec3 = entity.m_20184_();
        entity.m_20334_(vec3.x * 0.99F, vec3.y + (double) (vec3.y < 0.06F ? 5.0E-4F : 0.0F), vec3.z * 0.99F);
    }

    public boolean supportsBoating(Boat boat) {
        return this.supportsBoating;
    }

    public boolean supportsBoating(FluidState state, Boat boat) {
        return this.supportsBoating(boat);
    }

    public boolean shouldUpdateWhileBoating(FluidState state, Boat boat, Entity rider) {
        return !this.supportsBoating(state, boat);
    }

    public boolean canRideVehicleUnder(Entity vehicle, Entity rider) {
        return this == ForgeMod.WATER_TYPE.get() ? !vehicle.dismountsUnderwater() : true;
    }

    public boolean canHydrate(Entity entity) {
        return this.canHydrate;
    }

    @Nullable
    public SoundEvent getSound(Entity entity, SoundAction action) {
        return this.getSound(action);
    }

    public boolean canExtinguish(FluidState state, BlockGetter getter, BlockPos pos) {
        return this.canExtinguish;
    }

    public boolean canConvertToSource(FluidState state, LevelReader reader, BlockPos pos) {
        return this.canConvertToSource;
    }

    @Nullable
    public BlockPathTypes getBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog) {
        return this.pathType;
    }

    @Nullable
    public BlockPathTypes getAdjacentBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, BlockPathTypes originalType) {
        return this.adjacentPathType;
    }

    @Nullable
    public SoundEvent getSound(@Nullable Player player, BlockGetter getter, BlockPos pos, SoundAction action) {
        return this.getSound(action);
    }

    public boolean canHydrate(FluidState state, BlockGetter getter, BlockPos pos, BlockState source, BlockPos sourcePos) {
        return this.canHydrate;
    }

    public int getLightLevel(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getLightLevel();
    }

    public int getDensity(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getDensity();
    }

    public int getTemperature(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getTemperature();
    }

    public int getViscosity(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getViscosity();
    }

    public boolean canConvertToSource(FluidStack stack) {
        return this.canConvertToSource;
    }

    @Nullable
    public SoundEvent getSound(FluidStack stack, SoundAction action) {
        return this.getSound(action);
    }

    public Component getDescription(FluidStack stack) {
        return Component.translatable(this.getDescriptionId(stack));
    }

    public String getDescriptionId(FluidStack stack) {
        return this.getDescriptionId();
    }

    public boolean canHydrate(FluidStack stack) {
        return this.canHydrate;
    }

    public int getLightLevel(FluidStack stack) {
        return this.getLightLevel();
    }

    public int getDensity(FluidStack stack) {
        return this.getDensity();
    }

    public int getTemperature(FluidStack stack) {
        return this.getTemperature();
    }

    public int getViscosity(FluidStack stack) {
        return this.getViscosity();
    }

    public Rarity getRarity(FluidStack stack) {
        return this.getRarity();
    }

    public final boolean isAir() {
        return this == ForgeMod.EMPTY_TYPE.get();
    }

    public final boolean isVanilla() {
        return this == ForgeMod.LAVA_TYPE.get() || this == ForgeMod.WATER_TYPE.get();
    }

    public ItemStack getBucket(FluidStack stack) {
        return new ItemStack(stack.getFluid().getBucket());
    }

    public BlockState getBlockForFluidState(BlockAndTintGetter getter, BlockPos pos, FluidState state) {
        return state.createLegacyBlock();
    }

    public FluidState getStateForPlacement(BlockAndTintGetter getter, BlockPos pos, FluidStack stack) {
        return stack.getFluid().defaultFluidState();
    }

    public final boolean canBePlacedInLevel(BlockAndTintGetter getter, BlockPos pos, FluidState state) {
        return !this.getBlockForFluidState(getter, pos, state).m_60795_();
    }

    public final boolean canBePlacedInLevel(BlockAndTintGetter getter, BlockPos pos, FluidStack stack) {
        return this.canBePlacedInLevel(getter, pos, this.getStateForPlacement(getter, pos, stack));
    }

    public final boolean isLighterThanAir() {
        return this.getDensity() <= 0;
    }

    public boolean isVaporizedOnPlacement(Level level, BlockPos pos, FluidStack stack) {
        return !level.dimensionType().ultraWarm() ? false : this == ForgeMod.WATER_TYPE.get() || this.getStateForPlacement(level, pos, stack).is(FluidTags.WATER);
    }

    public void onVaporize(@Nullable Player player, Level level, BlockPos pos, FluidStack stack) {
        SoundEvent sound = this.getSound(player, level, pos, SoundActions.FLUID_VAPORIZE);
        level.playSound(player, pos, sound != null ? sound : SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
        for (int l = 0; l < 8; l++) {
            level.addAlwaysVisibleParticle(ParticleTypes.LARGE_SMOKE, (double) pos.m_123341_() + Math.random(), (double) pos.m_123342_() + Math.random(), (double) pos.m_123343_() + Math.random(), 0.0, 0.0, 0.0);
        }
    }

    public String toString() {
        ResourceLocation name = ((IForgeRegistry) ForgeRegistries.FLUID_TYPES.get()).getKey(this);
        return name != null ? name.toString() : "Unregistered FluidType";
    }

    public Object getRenderPropertiesInternal() {
        return this.renderProperties;
    }

    private void initClient() {
        if (FMLEnvironment.dist == Dist.CLIENT && !FMLLoader.getLaunchHandler().isData()) {
            this.initializeClient(properties -> {
                if (properties == this) {
                    throw new IllegalStateException("Don't extend IFluidTypeRenderProperties in your fluid type, use an anonymous class instead.");
                } else {
                    this.renderProperties = properties;
                }
            });
        }
    }

    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
    }

    public static final class Properties {

        private String descriptionId;

        private double motionScale = 0.014;

        private boolean canPushEntity = true;

        private boolean canSwim = true;

        private boolean canDrown = true;

        private float fallDistanceModifier = 0.5F;

        private boolean canExtinguish = false;

        private boolean canConvertToSource = false;

        private boolean supportsBoating = false;

        @Nullable
        private BlockPathTypes pathType = BlockPathTypes.WATER;

        @Nullable
        private BlockPathTypes adjacentPathType = BlockPathTypes.WATER_BORDER;

        private final Map<SoundAction, SoundEvent> sounds = new HashMap();

        private boolean canHydrate = false;

        private int lightLevel = 0;

        private int density = 1000;

        private int temperature = 300;

        private int viscosity = 1000;

        private Rarity rarity = Rarity.COMMON;

        private Properties() {
        }

        public static FluidType.Properties create() {
            return new FluidType.Properties();
        }

        public FluidType.Properties descriptionId(String descriptionId) {
            this.descriptionId = descriptionId;
            return this;
        }

        public FluidType.Properties motionScale(double motionScale) {
            this.motionScale = motionScale;
            return this;
        }

        public FluidType.Properties canPushEntity(boolean canPushEntity) {
            this.canPushEntity = canPushEntity;
            return this;
        }

        public FluidType.Properties canSwim(boolean canSwim) {
            this.canSwim = canSwim;
            return this;
        }

        public FluidType.Properties canDrown(boolean canDrown) {
            this.canDrown = canDrown;
            return this;
        }

        public FluidType.Properties fallDistanceModifier(float fallDistanceModifier) {
            this.fallDistanceModifier = fallDistanceModifier;
            return this;
        }

        public FluidType.Properties canExtinguish(boolean canExtinguish) {
            this.canExtinguish = canExtinguish;
            return this;
        }

        public FluidType.Properties canConvertToSource(boolean canConvertToSource) {
            this.canConvertToSource = canConvertToSource;
            return this;
        }

        public FluidType.Properties supportsBoating(boolean supportsBoating) {
            this.supportsBoating = supportsBoating;
            return this;
        }

        public FluidType.Properties pathType(@Nullable BlockPathTypes pathType) {
            this.pathType = pathType;
            return this;
        }

        public FluidType.Properties adjacentPathType(@Nullable BlockPathTypes adjacentPathType) {
            this.adjacentPathType = adjacentPathType;
            return this;
        }

        public FluidType.Properties sound(SoundAction action, SoundEvent sound) {
            this.sounds.put(action, sound);
            return this;
        }

        public FluidType.Properties canHydrate(boolean canHydrate) {
            this.canHydrate = canHydrate;
            return this;
        }

        public FluidType.Properties lightLevel(int lightLevel) {
            if (lightLevel >= 0 && lightLevel <= 15) {
                this.lightLevel = lightLevel;
                return this;
            } else {
                throw new IllegalArgumentException("The light level should be between [0,15].");
            }
        }

        public FluidType.Properties density(int density) {
            this.density = density;
            return this;
        }

        public FluidType.Properties temperature(int temperature) {
            this.temperature = temperature;
            return this;
        }

        public FluidType.Properties viscosity(int viscosity) {
            if (viscosity < 0) {
                throw new IllegalArgumentException("The viscosity should never be negative.");
            } else {
                this.viscosity = viscosity;
                return this;
            }
        }

        public FluidType.Properties rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }
    }
}