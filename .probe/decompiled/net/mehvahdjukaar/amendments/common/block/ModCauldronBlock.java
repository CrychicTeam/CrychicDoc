package net.mehvahdjukaar.amendments.common.block;

import java.util.Map;
import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class ModCauldronBlock extends AbstractCauldronBlock implements EntityBlock {

    public ModCauldronBlock(BlockBehaviour.Properties properties) {
        super(properties, Map.of());
    }

    @Override
    public Item asItem() {
        return Items.CAULDRON;
    }

    public abstract IntegerProperty getLevelProperty();

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return (Integer) state.m_61143_(this.getLevelProperty());
    }

    @Override
    public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LiquidCauldronBlockTile(pos, state);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (this.m_151979_(state, pos, entity)) {
            if (level.isClientSide && level.getBlockEntity(pos) instanceof LiquidCauldronBlockTile tile) {
                int color = tile.getSoftFluidTank().getCachedParticleColor(level, pos);
                int light = tile.getSoftFluidTank().getFluidValue().getEmissivity();
                playSplashAnimation(level, pos, entity, this.m_142446_(state), color, light);
            }
            super.m_142072_(level, state, pos, entity, 0.0F);
        } else {
            super.m_142072_(level, state, pos, entity, fallDistance);
        }
    }

    public static void playSplashAnimation(Level level, BlockPos pos, Entity e, double waterLevel, int color, int light) {
        Entity feetEntity = (Entity) (e.isVehicle() && e.getControllingPassenger() != null ? e.getControllingPassenger() : e);
        float offset = feetEntity == e ? 0.2F : 0.9F;
        Vec3 movement = feetEntity.getDeltaMovement();
        RandomSource rand = level.random;
        float speed = Math.min(1.0F, (float) Math.sqrt(movement.x * movement.x * 0.2 + movement.y * movement.y + movement.z * movement.z * 0.2) * offset);
        if (speed < 0.25F) {
            level.playLocalSound(e.getX(), e.getY(), e.getZ(), e.getSwimSplashSound(), e.getSoundSource(), speed, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F, false);
        } else {
            level.playLocalSound(e.getX(), e.getY(), e.getZ(), e.getSwimHighSpeedSplashSound(), e.getSoundSource(), speed, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F, false);
        }
        double surface = (double) pos.m_123342_() + waterLevel;
        float radius = 1.5F;
        float width = e.getBbWidth();
        spawnSplashParticles(level, e, pos, rand, surface, color, light, ModRegistry.BOILING_PARTICLE.get(), radius, width);
        spawnSplashParticles(level, e, pos, rand, surface, color, light, ModRegistry.SPLASH_PARTICLE.get(), radius, width);
        e.gameEvent(GameEvent.SPLASH);
    }

    private static void spawnSplashParticles(Level level, Entity e, BlockPos pos, RandomSource rand, double surface, int color, int light, ParticleOptions particleOptions, float radius, float width) {
        float mx = (float) pos.m_123341_() + 0.125F;
        float Mx = (float) (pos.m_123341_() + 1) - 0.125F;
        float mz = (float) pos.m_123343_() + 0.125F;
        float Mz = (float) (pos.m_123343_() + 1) - 0.125F;
        for (int i = 0; (float) i < 1.0F + width * 20.0F; i++) {
            double x = e.getX() + (rand.nextDouble() - 0.5) * (double) width * (double) radius;
            double z = e.getZ() + (rand.nextDouble() - 0.5) * (double) width * (double) radius;
            if (x >= (double) mx && x <= (double) Mx && z >= (double) mz && z <= (double) Mz) {
                level.addParticle(particleOptions, x, surface, z, (double) color, surface, (double) light);
            }
        }
    }

    public static void playExtinguishSound(Level level, BlockPos pos, Entity entity) {
        level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, entity.getSoundSource(), 0.7F, 1.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (this.m_151979_(state, pos, entity)) {
            entity.wasTouchingWater = true;
            if (level.isClientSide) {
                return;
            }
            if (entity.isOnFire()) {
                entity.clearFire();
                playExtinguishSound(level, pos, entity);
                if (entity.mayInteract(level, pos) && level.getBlockEntity(pos) instanceof LiquidCauldronBlockTile te) {
                    te.consumeOneLayer();
                }
            }
            this.handleEntityInside(state, level, pos, entity);
        }
    }

    protected abstract void handleEntityInside(BlockState var1, Level var2, BlockPos var3, Entity var4);

    public boolean doCraftItem(Level level, BlockPos pos, Player player, InteractionHand hand, SoftFluidStack fluid, ItemStack itemStack, ItemStack crafted, float layerPerItem, int itemCountMultiplier) {
        int maxRecolorable = (int) ((float) (crafted.getCount() * itemCountMultiplier * fluid.getCount()) / layerPerItem);
        int amountToRecolor = Math.min(maxRecolorable, itemStack.getCount());
        if (amountToRecolor <= 0) {
            return false;
        } else {
            crafted.setCount(amountToRecolor);
            level.playSound(player, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.3F);
            if (player instanceof ServerPlayer serverPlayer) {
                player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemStack);
                if (!player.isCreative()) {
                    itemStack.shrink(amountToRecolor);
                    fluid.shrink(Mth.ceil(layerPerItem * (float) amountToRecolor / (float) itemCountMultiplier));
                }
                if (itemStack.isEmpty()) {
                    player.m_21008_(hand, crafted);
                } else if (!player.getInventory().add(crafted)) {
                    player.drop(crafted, false);
                }
                return true;
            } else {
                return true;
            }
        }
    }

    public abstract BlockState updateStateOnFluidChange(BlockState var1, Level var2, BlockPos var3, SoftFluidStack var4);

    public static void addSurfaceParticles(ParticleOptions type, Level level, BlockPos pos, int count, double surface, RandomSource rand, float r, float g, float b) {
        for (int i = 0; i < count; i++) {
            double x = (double) pos.m_123341_() + 0.1875 + (double) rand.nextFloat() * 0.625;
            double y = (double) pos.m_123342_() + surface;
            double z = (double) pos.m_123343_() + 0.1875 + (double) rand.nextFloat() * 0.625;
            level.addParticle(type, x, y, z, (double) r, (double) g, (double) b);
        }
    }
}