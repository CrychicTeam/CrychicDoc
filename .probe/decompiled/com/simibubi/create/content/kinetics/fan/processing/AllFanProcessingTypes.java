package com.simibubi.create.content.kinetics.fan.processing;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.LitBlazeBurnerBlock;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.VecHelper;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class AllFanProcessingTypes {

    public static final AllFanProcessingTypes.NoneType NONE = register("none", new AllFanProcessingTypes.NoneType());

    public static final AllFanProcessingTypes.BlastingType BLASTING = register("blasting", new AllFanProcessingTypes.BlastingType());

    public static final AllFanProcessingTypes.HauntingType HAUNTING = register("haunting", new AllFanProcessingTypes.HauntingType());

    public static final AllFanProcessingTypes.SmokingType SMOKING = register("smoking", new AllFanProcessingTypes.SmokingType());

    public static final AllFanProcessingTypes.SplashingType SPLASHING = register("splashing", new AllFanProcessingTypes.SplashingType());

    private static final Map<String, FanProcessingType> LEGACY_NAME_MAP;

    private static <T extends FanProcessingType> T register(String id, T type) {
        FanProcessingTypeRegistry.register(Create.asResource(id), type);
        return type;
    }

    @Nullable
    public static FanProcessingType ofLegacyName(String name) {
        return (FanProcessingType) LEGACY_NAME_MAP.get(name);
    }

    public static void register() {
    }

    public static FanProcessingType parseLegacy(String str) {
        FanProcessingType type = ofLegacyName(str);
        return type != null ? type : FanProcessingType.parse(str);
    }

    static {
        Object2ReferenceOpenHashMap<String, FanProcessingType> map = new Object2ReferenceOpenHashMap();
        map.put("NONE", NONE);
        map.put("BLASTING", BLASTING);
        map.put("HAUNTING", HAUNTING);
        map.put("SMOKING", SMOKING);
        map.put("SPLASHING", SPLASHING);
        map.trim();
        LEGACY_NAME_MAP = map;
    }

    public static class BlastingType implements FanProcessingType {

        private static final RecipeWrapper RECIPE_WRAPPER = new RecipeWrapper(new ItemStackHandler(1));

        @Override
        public boolean isValidAt(Level level, BlockPos pos) {
            FluidState fluidState = level.getFluidState(pos);
            if (AllTags.AllFluidTags.FAN_PROCESSING_CATALYSTS_BLASTING.matches(fluidState)) {
                return true;
            } else {
                BlockState blockState = level.getBlockState(pos);
                return AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_BLASTING.matches(blockState) ? !blockState.m_61138_(BlazeBurnerBlock.HEAT_LEVEL) || ((BlazeBurnerBlock.HeatLevel) blockState.m_61143_(BlazeBurnerBlock.HEAT_LEVEL)).isAtLeast(BlazeBurnerBlock.HeatLevel.FADING) : false;
            }
        }

        @Override
        public int getPriority() {
            return 100;
        }

        @Override
        public boolean canProcess(ItemStack stack, Level level) {
            RECIPE_WRAPPER.setItem(0, stack);
            Optional<SmeltingRecipe> smeltingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, RECIPE_WRAPPER, level);
            if (smeltingRecipe.isPresent()) {
                return true;
            } else {
                RECIPE_WRAPPER.setItem(0, stack);
                Optional<BlastingRecipe> blastingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.BLASTING, RECIPE_WRAPPER, level);
                return blastingRecipe.isPresent() ? true : !stack.getItem().isFireResistant();
            }
        }

        @Nullable
        @Override
        public List<ItemStack> process(ItemStack stack, Level level) {
            RECIPE_WRAPPER.setItem(0, stack);
            Optional<SmokingRecipe> smokingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMOKING, RECIPE_WRAPPER, level);
            RECIPE_WRAPPER.setItem(0, stack);
            Optional<? extends AbstractCookingRecipe> smeltingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, RECIPE_WRAPPER, level);
            if (!smeltingRecipe.isPresent()) {
                RECIPE_WRAPPER.setItem(0, stack);
                smeltingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.BLASTING, RECIPE_WRAPPER, level);
            }
            if (smeltingRecipe.isPresent()) {
                RegistryAccess registryAccess = level.registryAccess();
                if (!smokingRecipe.isPresent() || !ItemStack.isSameItem(((SmokingRecipe) smokingRecipe.get()).m_8043_(registryAccess), ((AbstractCookingRecipe) smeltingRecipe.get()).getResultItem(registryAccess))) {
                    return RecipeApplier.applyRecipeOn(level, stack, (Recipe<?>) smeltingRecipe.get());
                }
            }
            return Collections.emptyList();
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {
            if (level.random.nextInt(8) == 0) {
                level.addParticle(ParticleTypes.LARGE_SMOKE, pos.x, pos.y + 0.25, pos.z, 0.0, 0.0625, 0.0);
            }
        }

        @Override
        public void morphAirFlow(FanProcessingType.AirFlowParticleAccess particleAccess, RandomSource random) {
            particleAccess.setColor(Color.mixColors(16729088, 16746581, random.nextFloat()));
            particleAccess.setAlpha(0.5F);
            if (random.nextFloat() < 0.03125F) {
                particleAccess.spawnExtraParticle(ParticleTypes.FLAME, 0.25F);
            }
            if (random.nextFloat() < 0.0625F) {
                particleAccess.spawnExtraParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.LAVA.defaultBlockState()), 0.25F);
            }
        }

        @Override
        public void affectEntity(Entity entity, Level level) {
            if (!level.isClientSide) {
                if (!entity.fireImmune()) {
                    entity.setSecondsOnFire(10);
                    entity.hurt(CreateDamageSources.fanLava(level), 4.0F);
                }
            }
        }
    }

    public static class HauntingType implements FanProcessingType {

        private static final HauntingRecipe.HauntingWrapper HAUNTING_WRAPPER = new HauntingRecipe.HauntingWrapper();

        @Override
        public boolean isValidAt(Level level, BlockPos pos) {
            FluidState fluidState = level.getFluidState(pos);
            if (AllTags.AllFluidTags.FAN_PROCESSING_CATALYSTS_HAUNTING.matches(fluidState)) {
                return true;
            } else {
                BlockState blockState = level.getBlockState(pos);
                if (AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_HAUNTING.matches(blockState)) {
                    return blockState.m_204336_(BlockTags.CAMPFIRES) && blockState.m_61138_(CampfireBlock.LIT) && !blockState.m_61143_(CampfireBlock.LIT) ? false : !blockState.m_61138_(LitBlazeBurnerBlock.FLAME_TYPE) || blockState.m_61143_(LitBlazeBurnerBlock.FLAME_TYPE) == LitBlazeBurnerBlock.FlameType.SOUL;
                } else {
                    return false;
                }
            }
        }

        @Override
        public int getPriority() {
            return 300;
        }

        @Override
        public boolean canProcess(ItemStack stack, Level level) {
            HAUNTING_WRAPPER.m_6836_(0, stack);
            Optional<HauntingRecipe> recipe = AllRecipeTypes.HAUNTING.find(HAUNTING_WRAPPER, level);
            return recipe.isPresent();
        }

        @Nullable
        @Override
        public List<ItemStack> process(ItemStack stack, Level level) {
            HAUNTING_WRAPPER.m_6836_(0, stack);
            Optional<HauntingRecipe> recipe = AllRecipeTypes.HAUNTING.find(HAUNTING_WRAPPER, level);
            return recipe.isPresent() ? RecipeApplier.applyRecipeOn(level, stack, (Recipe<?>) recipe.get()) : null;
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {
            if (level.random.nextInt(8) == 0) {
                pos = pos.add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1.0F).multiply(1.0, 0.05F, 1.0).normalize().scale(0.15F));
                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y + 0.45F, pos.z, 0.0, 0.0, 0.0);
                if (level.random.nextInt(2) == 0) {
                    level.addParticle(ParticleTypes.SMOKE, pos.x, pos.y + 0.25, pos.z, 0.0, 0.0, 0.0);
                }
            }
        }

        @Override
        public void morphAirFlow(FanProcessingType.AirFlowParticleAccess particleAccess, RandomSource random) {
            particleAccess.setColor(Color.mixColors(0, 1205608, random.nextFloat()));
            particleAccess.setAlpha(1.0F);
            if (random.nextFloat() < 0.0078125F) {
                particleAccess.spawnExtraParticle(ParticleTypes.SOUL_FIRE_FLAME, 0.125F);
            }
            if (random.nextFloat() < 0.03125F) {
                particleAccess.spawnExtraParticle(ParticleTypes.SMOKE, 0.125F);
            }
        }

        @Override
        public void affectEntity(Entity entity, Level level) {
            if (level.isClientSide) {
                if (entity instanceof Horse) {
                    Vec3 p = entity.getPosition(0.0F);
                    Vec3 v = p.add(0.0, 0.5, 0.0).add(VecHelper.offsetRandomly(Vec3.ZERO, level.random, 1.0F).multiply(1.0, 0.2F, 1.0).normalize().scale(1.0));
                    level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, v.x, v.y, v.z, 0.0, 0.1F, 0.0);
                    if (level.random.nextInt(3) == 0) {
                        level.addParticle(ParticleTypes.LARGE_SMOKE, p.x, p.y + 0.5, p.z, (double) ((level.random.nextFloat() - 0.5F) * 0.5F), 0.1F, (double) ((level.random.nextFloat() - 0.5F) * 0.5F));
                    }
                }
            } else {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30, 0, false, false));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1, false, false));
                }
                if (entity instanceof Horse horse) {
                    int progress = horse.getPersistentData().getInt("CreateHaunting");
                    if (progress < 100) {
                        if (progress % 10 == 0) {
                            level.playSound(null, entity.blockPosition(), SoundEvents.SOUL_ESCAPE, SoundSource.NEUTRAL, 1.0F, 1.5F * (float) progress / 100.0F);
                        }
                        horse.getPersistentData().putInt("CreateHaunting", progress + 1);
                        return;
                    }
                    level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.NEUTRAL, 1.25F, 0.65F);
                    SkeletonHorse skeletonHorse = EntityType.SKELETON_HORSE.create(level);
                    CompoundTag serializeNBT = horse.m_20240_(new CompoundTag());
                    serializeNBT.remove("UUID");
                    if (!horse.getArmor().isEmpty()) {
                        horse.m_19983_(horse.getArmor());
                    }
                    skeletonHorse.deserializeNBT(serializeNBT);
                    skeletonHorse.m_146884_(horse.m_20318_(0.0F));
                    level.m_7967_(skeletonHorse);
                    horse.m_146870_();
                }
            }
        }
    }

    public static class NoneType implements FanProcessingType {

        @Override
        public boolean isValidAt(Level level, BlockPos pos) {
            return true;
        }

        @Override
        public int getPriority() {
            return -1000000;
        }

        @Override
        public boolean canProcess(ItemStack stack, Level level) {
            return false;
        }

        @Nullable
        @Override
        public List<ItemStack> process(ItemStack stack, Level level) {
            return null;
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {
        }

        @Override
        public void morphAirFlow(FanProcessingType.AirFlowParticleAccess particleAccess, RandomSource random) {
        }

        @Override
        public void affectEntity(Entity entity, Level level) {
        }
    }

    public static class SmokingType implements FanProcessingType {

        private static final RecipeWrapper RECIPE_WRAPPER = new RecipeWrapper(new ItemStackHandler(1));

        @Override
        public boolean isValidAt(Level level, BlockPos pos) {
            FluidState fluidState = level.getFluidState(pos);
            if (AllTags.AllFluidTags.FAN_PROCESSING_CATALYSTS_SMOKING.matches(fluidState)) {
                return true;
            } else {
                BlockState blockState = level.getBlockState(pos);
                if (AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SMOKING.matches(blockState)) {
                    if (blockState.m_204336_(BlockTags.CAMPFIRES) && blockState.m_61138_(CampfireBlock.LIT) && !(Boolean) blockState.m_61143_(CampfireBlock.LIT)) {
                        return false;
                    } else {
                        return blockState.m_61138_(LitBlazeBurnerBlock.FLAME_TYPE) && blockState.m_61143_(LitBlazeBurnerBlock.FLAME_TYPE) != LitBlazeBurnerBlock.FlameType.REGULAR ? false : !blockState.m_61138_(BlazeBurnerBlock.HEAT_LEVEL) || blockState.m_61143_(BlazeBurnerBlock.HEAT_LEVEL) == BlazeBurnerBlock.HeatLevel.SMOULDERING;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override
        public int getPriority() {
            return 200;
        }

        @Override
        public boolean canProcess(ItemStack stack, Level level) {
            RECIPE_WRAPPER.setItem(0, stack);
            Optional<SmokingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMOKING, RECIPE_WRAPPER, level);
            return recipe.isPresent();
        }

        @Nullable
        @Override
        public List<ItemStack> process(ItemStack stack, Level level) {
            RECIPE_WRAPPER.setItem(0, stack);
            Optional<SmokingRecipe> smokingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMOKING, RECIPE_WRAPPER, level);
            return smokingRecipe.isPresent() ? RecipeApplier.applyRecipeOn(level, stack, (Recipe<?>) smokingRecipe.get()) : null;
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {
            if (level.random.nextInt(8) == 0) {
                level.addParticle(ParticleTypes.POOF, pos.x, pos.y + 0.25, pos.z, 0.0, 0.0625, 0.0);
            }
        }

        @Override
        public void morphAirFlow(FanProcessingType.AirFlowParticleAccess particleAccess, RandomSource random) {
            particleAccess.setColor(Color.mixColors(0, 5592405, random.nextFloat()));
            particleAccess.setAlpha(1.0F);
            if (random.nextFloat() < 0.03125F) {
                particleAccess.spawnExtraParticle(ParticleTypes.SMOKE, 0.125F);
            }
            if (random.nextFloat() < 0.03125F) {
                particleAccess.spawnExtraParticle(ParticleTypes.LARGE_SMOKE, 0.125F);
            }
        }

        @Override
        public void affectEntity(Entity entity, Level level) {
            if (!level.isClientSide) {
                if (!entity.fireImmune()) {
                    entity.setSecondsOnFire(2);
                    entity.hurt(CreateDamageSources.fanFire(level), 2.0F);
                }
            }
        }
    }

    public static class SplashingType implements FanProcessingType {

        private static final SplashingRecipe.SplashingWrapper SPLASHING_WRAPPER = new SplashingRecipe.SplashingWrapper();

        @Override
        public boolean isValidAt(Level level, BlockPos pos) {
            FluidState fluidState = level.getFluidState(pos);
            if (AllTags.AllFluidTags.FAN_PROCESSING_CATALYSTS_SPLASHING.matches(fluidState)) {
                return true;
            } else {
                BlockState blockState = level.getBlockState(pos);
                return AllTags.AllBlockTags.FAN_PROCESSING_CATALYSTS_SPLASHING.matches(blockState);
            }
        }

        @Override
        public int getPriority() {
            return 400;
        }

        @Override
        public boolean canProcess(ItemStack stack, Level level) {
            SPLASHING_WRAPPER.m_6836_(0, stack);
            Optional<SplashingRecipe> recipe = AllRecipeTypes.SPLASHING.find(SPLASHING_WRAPPER, level);
            return recipe.isPresent();
        }

        @Nullable
        @Override
        public List<ItemStack> process(ItemStack stack, Level level) {
            SPLASHING_WRAPPER.m_6836_(0, stack);
            Optional<SplashingRecipe> recipe = AllRecipeTypes.SPLASHING.find(SPLASHING_WRAPPER, level);
            return recipe.isPresent() ? RecipeApplier.applyRecipeOn(level, stack, (Recipe<?>) recipe.get()) : null;
        }

        @Override
        public void spawnProcessingParticles(Level level, Vec3 pos) {
            if (level.random.nextInt(8) == 0) {
                Vector3f color = new Color(22015).asVectorF();
                level.addParticle(new DustParticleOptions(color, 1.0F), pos.x + (double) ((level.random.nextFloat() - 0.5F) * 0.5F), pos.y + 0.5, pos.z + (double) ((level.random.nextFloat() - 0.5F) * 0.5F), 0.0, 0.125, 0.0);
                level.addParticle(ParticleTypes.SPIT, pos.x + (double) ((level.random.nextFloat() - 0.5F) * 0.5F), pos.y + 0.5, pos.z + (double) ((level.random.nextFloat() - 0.5F) * 0.5F), 0.0, 0.125, 0.0);
            }
        }

        @Override
        public void morphAirFlow(FanProcessingType.AirFlowParticleAccess particleAccess, RandomSource random) {
            particleAccess.setColor(Color.mixColors(4495871, 2258943, random.nextFloat()));
            particleAccess.setAlpha(1.0F);
            if (random.nextFloat() < 0.03125F) {
                particleAccess.spawnExtraParticle(ParticleTypes.BUBBLE, 0.125F);
            }
            if (random.nextFloat() < 0.03125F) {
                particleAccess.spawnExtraParticle(ParticleTypes.BUBBLE_POP, 0.125F);
            }
        }

        @Override
        public void affectEntity(Entity entity, Level level) {
            if (!level.isClientSide) {
                if (entity instanceof EnderMan || entity.getType() == EntityType.SNOW_GOLEM || entity.getType() == EntityType.BLAZE) {
                    entity.hurt(entity.damageSources().drown(), 2.0F);
                }
                if (entity.isOnFire()) {
                    entity.clearFire();
                    level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.NEUTRAL, 0.7F, 1.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
                }
            }
        }
    }
}