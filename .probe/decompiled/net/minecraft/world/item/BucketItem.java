package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class BucketItem extends Item implements DispensibleContainerItem {

    private final Fluid content;

    public BucketItem(Fluid fluid0, Item.Properties itemProperties1) {
        super(itemProperties1);
        this.content = fluid0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        BlockHitResult $$4 = m_41435_(level0, player1, this.content == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        if ($$4.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass($$3);
        } else if ($$4.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass($$3);
        } else {
            BlockPos $$5 = $$4.getBlockPos();
            Direction $$6 = $$4.getDirection();
            BlockPos $$7 = $$5.relative($$6);
            if (!level0.mayInteract(player1, $$5) || !player1.mayUseItemAt($$7, $$6, $$3)) {
                return InteractionResultHolder.fail($$3);
            } else if (this.content == Fluids.EMPTY) {
                BlockState $$8 = level0.getBlockState($$5);
                if ($$8.m_60734_() instanceof BucketPickup) {
                    BucketPickup $$9 = (BucketPickup) $$8.m_60734_();
                    ItemStack $$10 = $$9.pickupBlock(level0, $$5, $$8);
                    if (!$$10.isEmpty()) {
                        player1.awardStat(Stats.ITEM_USED.get(this));
                        $$9.getPickupSound().ifPresent(p_150709_ -> player1.playSound(p_150709_, 1.0F, 1.0F));
                        level0.m_142346_(player1, GameEvent.FLUID_PICKUP, $$5);
                        ItemStack $$11 = ItemUtils.createFilledResult($$3, player1, $$10);
                        if (!level0.isClientSide) {
                            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player1, $$10);
                        }
                        return InteractionResultHolder.sidedSuccess($$11, level0.isClientSide());
                    }
                }
                return InteractionResultHolder.fail($$3);
            } else {
                BlockState $$12 = level0.getBlockState($$5);
                BlockPos $$13 = $$12.m_60734_() instanceof LiquidBlockContainer && this.content == Fluids.WATER ? $$5 : $$7;
                if (this.emptyContents(player1, level0, $$13, $$4)) {
                    this.checkExtraContent(player1, level0, $$3, $$13);
                    if (player1 instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player1, $$13, $$3);
                    }
                    player1.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(getEmptySuccessItem($$3, player1), level0.isClientSide());
                } else {
                    return InteractionResultHolder.fail($$3);
                }
            }
        }
    }

    public static ItemStack getEmptySuccessItem(ItemStack itemStack0, Player player1) {
        return !player1.getAbilities().instabuild ? new ItemStack(Items.BUCKET) : itemStack0;
    }

    @Override
    public void checkExtraContent(@Nullable Player player0, Level level1, ItemStack itemStack2, BlockPos blockPos3) {
    }

    @Override
    public boolean emptyContents(@Nullable Player player0, Level level1, BlockPos blockPos2, @Nullable BlockHitResult blockHitResult3) {
        if (!(this.content instanceof FlowingFluid)) {
            return false;
        } else {
            BlockState $$4 = level1.getBlockState(blockPos2);
            Block $$5 = $$4.m_60734_();
            boolean $$6 = $$4.m_60722_(this.content);
            boolean $$7 = $$4.m_60795_() || $$6 || $$5 instanceof LiquidBlockContainer && ((LiquidBlockContainer) $$5).canPlaceLiquid(level1, blockPos2, $$4, this.content);
            if (!$$7) {
                return blockHitResult3 != null && this.emptyContents(player0, level1, blockHitResult3.getBlockPos().relative(blockHitResult3.getDirection()), null);
            } else if (level1.dimensionType().ultraWarm() && this.content.is(FluidTags.WATER)) {
                int $$8 = blockPos2.m_123341_();
                int $$9 = blockPos2.m_123342_();
                int $$10 = blockPos2.m_123343_();
                level1.playSound(player0, blockPos2, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level1.random.nextFloat() - level1.random.nextFloat()) * 0.8F);
                for (int $$11 = 0; $$11 < 8; $$11++) {
                    level1.addParticle(ParticleTypes.LARGE_SMOKE, (double) $$8 + Math.random(), (double) $$9 + Math.random(), (double) $$10 + Math.random(), 0.0, 0.0, 0.0);
                }
                return true;
            } else if ($$5 instanceof LiquidBlockContainer && this.content == Fluids.WATER) {
                ((LiquidBlockContainer) $$5).placeLiquid(level1, blockPos2, $$4, ((FlowingFluid) this.content).getSource(false));
                this.playEmptySound(player0, level1, blockPos2);
                return true;
            } else {
                if (!level1.isClientSide && $$6 && !$$4.m_278721_()) {
                    level1.m_46961_(blockPos2, true);
                }
                if (!level1.setBlock(blockPos2, this.content.defaultFluidState().createLegacyBlock(), 11) && !$$4.m_60819_().isSource()) {
                    return false;
                } else {
                    this.playEmptySound(player0, level1, blockPos2);
                    return true;
                }
            }
        }
    }

    protected void playEmptySound(@Nullable Player player0, LevelAccessor levelAccessor1, BlockPos blockPos2) {
        SoundEvent $$3 = this.content.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        levelAccessor1.playSound(player0, blockPos2, $$3, SoundSource.BLOCKS, 1.0F, 1.0F);
        levelAccessor1.gameEvent(player0, GameEvent.FLUID_PLACE, blockPos2);
    }
}