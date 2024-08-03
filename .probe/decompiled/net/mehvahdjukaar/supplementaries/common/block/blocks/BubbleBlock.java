package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BubbleBlockTile;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BubbleBlock extends Block implements EntityBlock {

    public BubbleBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext placeContext) {
        return !placeContext.m_7078_();
    }

    @Override
    public boolean isPossibleToRespawnInThis(BlockState blockState) {
        return true;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        if (collisionContext.isAbove(Shapes.block(), pos, false) && collisionContext instanceof EntityCollisionContext ec && ec.getEntity() instanceof LivingEntity) {
            return Shapes.block();
        }
        return Shapes.empty();
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if ((Boolean) CommonConfigs.Tools.BUBBLE_BREAK.get() && level instanceof ServerLevel serverLevel) {
            this.breakBubble(serverLevel, pos, state);
        }
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        this.makeParticle(pos, level);
        this.playBreakSound(state, level, pos, player);
    }

    private void playBreakSound(BlockState state, Level level, BlockPos pos, Player player) {
        SoundType soundtype = state.m_60827_();
        level.playSound(player, pos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
    }

    public boolean addLandingEffects(BlockState state1, ServerLevel worldserver, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        return true;
    }

    public void makeParticle(BlockPos pos, Level level) {
        level.addParticle((ParticleOptions) ModParticles.BUBBLE_BLOCK_PARTICLE.get(), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, 0.0, 0.0, 0.0);
    }

    public void sendParticles(BlockPos pos, ServerLevel level) {
        level.sendParticles((SimpleParticleType) ModParticles.BUBBLE_BLOCK_PARTICLE.get(), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, 1, 0.0, 0.0, 0.0, 0.0);
    }

    public void breakBubble(ServerLevel level, BlockPos pos, BlockState state) {
        level.m_7471_(pos, false);
        this.sendParticles(pos, level);
        this.playBreakSound(state, level, pos, null);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if ((Boolean) CommonConfigs.Tools.BUBBLE_BREAK.get()) {
            level.m_186460_(pos, this, 5);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float v) {
        super.fallOn(level, state, pos, entity, v);
        if (!level.isClientSide && (Boolean) CommonConfigs.Tools.BUBBLE_BREAK.get()) {
            if (entity instanceof LivingEntity le && (Boolean) CommonConfigs.Tools.BUBBLE_FEATHER_FALLING.get() && EnchantmentHelper.getEnchantmentLevel(Enchantments.FALL_PROTECTION, le) != 0) {
                return;
            }
            if (v > 3.0F) {
                this.breakBubble((ServerLevel) level, pos, state);
            } else {
                level.m_186460_(pos, this, (int) Mth.clamp(7.0F - v / 2.0F, 1.0F, 5.0F));
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        this.breakBubble(serverLevel, pos, state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BubbleBlockTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> tBlockEntityType) {
        return Utils.getTicker(tBlockEntityType, (BlockEntityType) ModRegistry.BUBBLE_BLOCK_TILE.get(), BubbleBlockTile::tick);
    }
}