package com.rekindled.embers.item;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.block.GlimmerBlock;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class GlimmerCrystalItem extends Item {

    public GlimmerCrystalItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide() && world.getGameTime() % 10L == 0L && world.m_45517_(LightLayer.SKY, entity.blockPosition()) - world.getSkyDarken() > 12) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.isSameItem(oldStack, newStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        InteractionResult interactionresult = this.place(new BlockPlaceContext(pContext));
        if (!interactionresult.consumesAction() && this.m_41472_()) {
            InteractionResult interactionresult1 = this.m_7203_(pContext.getLevel(), pContext.getPlayer(), pContext.getHand()).getResult();
            return interactionresult1 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : interactionresult1;
        } else {
            return interactionresult;
        }
    }

    public InteractionResult place(BlockPlaceContext context) {
        if (!context.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockState blockstate = this.getPlacementState(context);
            if (blockstate == null) {
                return InteractionResult.FAIL;
            } else if (!this.placeBlock(context, blockstate)) {
                return InteractionResult.FAIL;
            } else {
                BlockPos blockpos = context.getClickedPos();
                Level level = context.m_43725_();
                Player player = context.m_43723_();
                ItemStack itemstack = context.m_43722_();
                BlockState blockstate1 = level.getBlockState(blockpos);
                if (blockstate1.m_60713_(blockstate.m_60734_())) {
                    blockstate1.m_60734_().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
                    }
                }
                SoundType soundtype = blockstate1.getSoundType(level, blockpos, context.m_43723_());
                level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, player), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                level.m_220407_(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
                if (player == null || !player.getAbilities().instabuild) {
                    itemstack.hurtAndBreak(1, player, e -> {
                    });
                }
                level.addParticle(GlimmerBlock.GLIMMER, (double) ((float) blockpos.m_123341_() + 0.5F), (double) ((float) blockpos.m_123342_() + 0.5F), (double) ((float) blockpos.m_123343_() + 0.5F), 0.0, 0.0, 0.0);
                level.addParticle(GlimmerBlock.EMBER, (double) ((float) blockpos.m_123341_() + 0.5F), (double) ((float) blockpos.m_123342_() + 0.5F), (double) ((float) blockpos.m_123343_() + 0.5F), 0.0, 0.001, 0.0);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
    }

    protected SoundEvent getPlaceSound(BlockState state, Level world, BlockPos pos, Player entity) {
        return state.getSoundType(world, pos, entity).getPlaceSound();
    }

    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
        return pContext.m_43725_().setBlock(pContext.getClickedPos(), pState, 11);
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext pContext) {
        return RegistryManager.GLIMMER.get().getStateForPlacement(pContext);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return 16777215;
    }
}