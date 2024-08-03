package net.mehvahdjukaar.amendments.common.block;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import net.mehvahdjukaar.amendments.common.recipe.RecipeUtils;
import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.integration.AlexCavesCompat;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.reg.ModBlockProperties;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.amendments.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.PotionNBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class LiquidCauldronBlock extends ModCauldronBlock {

    public static final int MAX_LEVEL = PlatHelper.getPlatform().isFabric() ? 3 : 4;

    public static final IntegerProperty LEVEL = PlatHelper.getPlatform().isFabric() ? BlockStateProperties.LEVEL_CAULDRON : ModBlockProperties.LEVEL_1_4;

    public static final IntegerProperty LIGHT_LEVEL = ModBlockProperties.LIGHT_LEVEL;

    public static final BooleanProperty BOILING = ModBlockProperties.BOILING;

    public LiquidCauldronBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(value -> (Integer) value.m_61143_(LIGHT_LEVEL)));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49965_().any()).m_61124_(LEVEL, 1)).m_61124_(LIGHT_LEVEL, 0)).m_61124_(BOILING, false));
    }

    @Override
    public IntegerProperty getLevelProperty() {
        return LEVEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(LEVEL, LIGHT_LEVEL, BOILING);
    }

    @Override
    protected boolean canReceiveStalactiteDrip(Fluid fluid) {
        return fluid != Fluids.WATER && fluid != Fluids.LAVA;
    }

    @Override
    public void receiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid) {
        if (!this.isFull(state) && level.getBlockEntity(pos) instanceof LiquidCauldronBlockTile te) {
            SoftFluidStack sf = SoftFluidStack.fromFluid(fluid, 1, null);
            if (!sf.isEmpty() && te.getSoftFluidTank().addFluid(sf, false) != 0) {
                level.m_220407_(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
                level.m_46796_(1047, pos, 0);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof LiquidCauldronBlockTile te) {
            if (te.handleInteraction(player, hand)) {
                this.maybeSendPotionMixMessage(te.getSoftFluidTank(), player);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (!(Boolean) CommonConfigs.CAULDRON_CRAFTING.get()) {
                return InteractionResult.PASS;
            }
            SoftFluidTank tank = te.getSoftFluidTank();
            SoftFluidStack fluid = tank.getFluid();
            ItemStack stack = player.m_21120_(hand);
            Pair<ItemStack, Float> crafted = RecipeUtils.craftWithFluid(level, tank.getFluid(), stack, true);
            if (crafted != null) {
                int mult = fluid.is(BuiltInSoftFluids.POTION.get()) ? (Integer) CommonConfigs.POTION_RECIPES_PER_LAYER.get() : 1;
                if (this.doCraftItem(level, pos, player, hand, fluid, stack, (ItemStack) crafted.getFirst(), (Float) crafted.getSecond(), mult)) {
                    te.setChanged();
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isFull(BlockState state) {
        return (Integer) state.m_61143_(LEVEL) == MAX_LEVEL;
    }

    @Override
    protected double getContentHeight(BlockState state) {
        return 0.4375 + 0.125 * (double) ((Integer) state.m_61143_(LEVEL)).intValue();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        BlockState s = super.m_7417_(state, direction, neighborState, level, currentPos, neighborPos);
        if (direction == Direction.DOWN && level.m_7702_(currentPos) instanceof LiquidCauldronBlockTile te) {
            boolean isFire = shouldBoil(neighborState, te.getSoftFluidTank().getFluid());
            s = (BlockState) s.m_61124_(BOILING, isFire);
        }
        return s;
    }

    public static boolean shouldBoil(BlockState belowState, SoftFluidStack fluid) {
        return belowState.m_204336_(ModTags.HEAT_SOURCES) && fluid.is(ModTags.CAN_BOIL);
    }

    @Override
    protected void handleEntityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if ((Boolean) state.m_61143_(BOILING) && entity instanceof LivingEntity) {
            entity.hurt(new DamageSource(ModRegistry.BOILING_DAMAGE.getHolder()), 1.0F);
        }
        if (entity instanceof LivingEntity living && entity.mayInteract(level, pos) && level.getBlockEntity(pos) instanceof LiquidCauldronBlockTile tile) {
            SoftFluidStack fluid = tile.getSoftFluidTank().getFluid();
            PotionNBTHelper.Type potType = this.getPotType(fluid);
            if (potType != null && potType != PotionNBTHelper.Type.REGULAR && this.applyPotionFluidEffects(level, pos, living, fluid)) {
                tile.consumeOneLayer();
            }
            if (CompatHandler.ALEX_CAVES) {
                AlexCavesCompat.acidDamage(fluid, level, pos, state, entity);
            }
        }
    }

    private boolean applyPotionFluidEffects(Level level, BlockPos pos, LivingEntity living, SoftFluidStack stack) {
        List<MobEffectInstance> effects = PotionUtils.getAllEffects(stack.getTag());
        boolean success = false;
        for (MobEffectInstance effect : effects) {
            MobEffect ef = effect.getEffect();
            if (!living.hasEffect(ef)) {
                if (ef.isInstantenous()) {
                    ef.applyInstantenousEffect(null, null, living, effect.getAmplifier(), 1.0);
                } else {
                    living.addEffect(new MobEffectInstance(effect));
                }
                success = true;
            }
        }
        if (success) {
            level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return success;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if (level.getBlockEntity(pos) instanceof LiquidCauldronBlockTile te) {
            SoftFluidTank tank = te.getSoftFluidTank();
            if ((Boolean) state.m_61143_(BOILING)) {
                int color = tank.getCachedParticleColor(level, pos);
                int light = tank.getFluidValue().getEmissivity();
                playBubblingAnimation(level, pos, this.getContentHeight(state), rand, color, light);
            }
            if (level.random.nextInt(4) == 0) {
                SoftFluidStack fluid = tank.getFluid();
                PotionNBTHelper.Type type = this.getPotType(fluid);
                double height = this.getContentHeight(state);
                if (type != null) {
                    if (this.getPotionEffects(fluid).size() >= (Integer) CommonConfigs.POTION_MIXING_LIMIT.get()) {
                        addSurfaceParticles(ParticleTypes.SMOKE, level, pos, 2, height, rand, 0.0F, 0.0F, 0.0F);
                    }
                    if (type != PotionNBTHelper.Type.REGULAR) {
                        ParticleOptions particle = type == PotionNBTHelper.Type.SPLASH ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT;
                        int color = tank.getCachedParticleColor(level, pos);
                        this.addPotionParticles(particle, level, pos, 1, height, rand, color);
                    }
                }
                if (CompatHandler.ALEX_CAVES) {
                    AlexCavesCompat.acidParticles(fluid, level, pos, rand, height);
                }
                BlockPos blockPos = pos.above();
                if (fluid.is(BuiltInSoftFluids.LAVA.get()) && level.getBlockState(blockPos).m_60795_() && !level.getBlockState(blockPos).m_60804_(level, blockPos)) {
                    Vec3 c = pos.getCenter();
                    if (rand.nextInt(20) == 0) {
                        addSurfaceParticles(ParticleTypes.LAVA, level, pos, 1, height, rand, 0.0F, 0.0F, 0.0F);
                        level.playLocalSound(c.x, height, c.z, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
                    }
                    if (rand.nextInt(40) == 0) {
                        level.playLocalSound(c.x, height, c.z, SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
                    }
                }
            }
        }
    }

    @Nullable
    private PotionNBTHelper.Type getPotType(SoftFluidStack stack) {
        return stack.is(BuiltInSoftFluids.POTION.get()) && stack.hasTag() ? PotionNBTHelper.getPotionType(stack.getTag()) : null;
    }

    private List<MobEffectInstance> getPotionEffects(SoftFluidStack stack) {
        return PotionUtils.getAllEffects(stack.getTag());
    }

    public static void playBubblingAnimation(Level level, BlockPos pos, double surface, RandomSource rand, int color, int light) {
        SimpleParticleType type = ModRegistry.BOILING_PARTICLE.get();
        int count = 2;
        addSurfaceParticles(type, level, pos, count, surface, rand, (float) color, (float) pos.m_123342_() + 0.3125F, (float) light);
        if (level.random.nextInt(4) == 0) {
            level.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.4F + level.random.nextFloat() * 0.2F, 0.35F + level.random.nextFloat() * 0.2F, false);
        }
    }

    private void addPotionParticles(ParticleOptions type, Level level, BlockPos pos, int count, double surface, RandomSource rand, int color) {
        float r = (float) FastColor.ARGB32.red(color) / 255.0F;
        float g = (float) FastColor.ARGB32.green(color) / 255.0F;
        float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
        addSurfaceParticles(type, level, pos, count, surface, rand, r, g, b);
    }

    @Override
    public BlockState updateStateOnFluidChange(BlockState state, Level level, BlockPos pos, SoftFluidStack fluid) {
        BlockState exploded = this.maybeExplode(state, level, pos, fluid);
        if (exploded != null) {
            return exploded;
        } else {
            int light = fluid.fluid().getLuminosity();
            if (light != (Integer) state.m_61143_(ModBlockProperties.LIGHT_LEVEL)) {
                state = (BlockState) state.m_61124_(ModBlockProperties.LIGHT_LEVEL, light);
            }
            int height = fluid.getCount();
            if (fluid.isEmpty()) {
                state = Blocks.CAULDRON.defaultBlockState();
            } else {
                state = (BlockState) state.m_61124_(LEVEL, height);
            }
            return state;
        }
    }

    public void maybeSendPotionMixMessage(SoftFluidTank fluidTank, Player player) {
        if (fluidTank.getFluid().is(BuiltInSoftFluids.POTION.get())) {
            List<MobEffectInstance> potionEffects = this.getPotionEffects(fluidTank.getFluid());
            int potionEffectAmount = potionEffects.size();
            if (potionEffectAmount == (Integer) CommonConfigs.POTION_MIXING_LIMIT.get()) {
                player.displayClientMessage(Component.translatable("message.amendments.cauldron"), true);
            }
        }
    }

    @Nullable
    private BlockState maybeExplode(BlockState state, Level level, BlockPos pos, SoftFluidStack fluid) {
        List<MobEffectInstance> potionEffects = this.getPotionEffects(fluid);
        int potionEffectAmount = potionEffects.size();
        if (potionEffectAmount >= (Integer) CommonConfigs.POTION_MIXING_LIMIT.get()) {
            if (potionEffectAmount > (Integer) CommonConfigs.POTION_MIXING_LIMIT.get()) {
                level.m_46961_(pos, true);
                Vec3 vec3 = pos.getCenter();
                level.explode(null, level.damageSources().badRespawnPointExplosion(vec3), null, vec3.x, vec3.y, vec3.z, 1.4F, false, Level.ExplosionInteraction.NONE);
                return state;
            } else {
                if (level.isClientSide) {
                    addSurfaceParticles(ParticleTypes.SMOKE, level, pos, 12, this.getContentHeight(state), level.random, 0.0F, 0.0F, 0.0F);
                }
                level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                return null;
            }
        } else {
            Map<MobEffect, MobEffect> inverse = (Map<MobEffect, MobEffect>) CommonConfigs.INVERSE_POTIONS.get();
            List<MobEffect> effects = potionEffects.stream().map(MobEffectInstance::m_19544_).toList();
            for (MobEffect effect : effects) {
                MobEffect inv = (MobEffect) inverse.get(effect);
                if (inv != null && effects.contains(inv)) {
                    if (level.isClientSide) {
                        addSurfaceParticles(ParticleTypes.POOF, level, pos, 8, this.getContentHeight(state), level.random, 0.0F, 0.01F + level.random.nextFloat() * 0.1F, 0.0F);
                    }
                    level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return Blocks.CAULDRON.defaultBlockState();
                }
            }
            return null;
        }
    }
}