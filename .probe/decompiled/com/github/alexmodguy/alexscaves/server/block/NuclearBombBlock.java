package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public class NuclearBombBlock extends Block {

    public NuclearBombBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(8.0F, 1001.0F).sound(ACSoundTypes.NUCLEAR_BOMB));
    }

    public void onCaughtFire(BlockState state, Level level, BlockPos blockPos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!level.isClientSide) {
            NuclearBombEntity bomb = ACEntityRegistry.NUCLEAR_BOMB.get().create(level);
            bomb.m_6034_((double) blockPos.m_123341_() + 0.5, (double) blockPos.m_123342_(), (double) blockPos.m_123343_() + 0.5);
            level.m_7967_(bomb);
            level.playSound((Player) null, bomb.m_20185_(), bomb.m_20186_(), bomb.m_20189_(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.m_142346_(igniter, GameEvent.PRIME_FUSE, blockPos);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos blockPos, BlockState blockState, boolean b) {
        if (!blockState.m_60713_(state.m_60734_()) && level.m_276867_(blockPos)) {
            this.onCaughtFire(state, level, blockPos, null, null);
            level.removeBlock(blockPos, false);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos blockPos, Block block, BlockPos blockPos1, boolean forced) {
        if (level.m_276867_(blockPos)) {
            this.onCaughtFire(state, level, blockPos, null, null);
            level.removeBlock(blockPos, false);
        }
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult blockHitResult, Projectile projectile) {
        if (!level.isClientSide) {
            BlockPos blockpos = blockHitResult.getBlockPos();
            Entity entity = projectile.getOwner();
            if (projectile.m_6060_() && projectile.mayInteract(level, blockpos)) {
                this.onCaughtFire(state, level, blockpos, null, entity instanceof LivingEntity ? (LivingEntity) entity : null);
                level.removeBlock(blockpos, false);
            }
        }
    }

    @Override
    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack itemstack = player.m_21120_(hand);
        if (!itemstack.is(Items.FLINT_AND_STEEL) && !itemstack.is(Items.FIRE_CHARGE)) {
            return super.m_6227_(state, level, blockPos, player, hand, result);
        } else {
            this.onCaughtFire(state, level, blockPos, result.getDirection(), player);
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
            Item item = itemstack.getItem();
            if (!player.isCreative()) {
                if (itemstack.is(Items.FLINT_AND_STEEL)) {
                    itemstack.hurtAndBreak(1, player, p_57425_ -> p_57425_.m_21190_(hand));
                } else {
                    itemstack.shrink(1);
                }
            }
            player.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
}