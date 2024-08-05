package net.minecraft.world.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;

public class FlintAndSteelItem extends Item {

    public FlintAndSteelItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Player $$1 = useOnContext0.getPlayer();
        Level $$2 = useOnContext0.getLevel();
        BlockPos $$3 = useOnContext0.getClickedPos();
        BlockState $$4 = $$2.getBlockState($$3);
        if (!CampfireBlock.canLight($$4) && !CandleBlock.canLight($$4) && !CandleCakeBlock.canLight($$4)) {
            BlockPos $$5 = $$3.relative(useOnContext0.getClickedFace());
            if (BaseFireBlock.canBePlacedAt($$2, $$5, useOnContext0.getHorizontalDirection())) {
                $$2.playSound($$1, $$5, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, $$2.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState $$6 = BaseFireBlock.getState($$2, $$5);
                $$2.setBlock($$5, $$6, 11);
                $$2.m_142346_($$1, GameEvent.BLOCK_PLACE, $$3);
                ItemStack $$7 = useOnContext0.getItemInHand();
                if ($$1 instanceof ServerPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) $$1, $$5, $$7);
                    $$7.hurtAndBreak(1, $$1, p_41300_ -> p_41300_.m_21190_(useOnContext0.getHand()));
                }
                return InteractionResult.sidedSuccess($$2.isClientSide());
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            $$2.playSound($$1, $$3, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, $$2.getRandom().nextFloat() * 0.4F + 0.8F);
            $$2.setBlock($$3, (BlockState) $$4.m_61124_(BlockStateProperties.LIT, true), 11);
            $$2.m_142346_($$1, GameEvent.BLOCK_CHANGE, $$3);
            if ($$1 != null) {
                useOnContext0.getItemInHand().hurtAndBreak(1, $$1, p_41303_ -> p_41303_.m_21190_(useOnContext0.getHand()));
            }
            return InteractionResult.sidedSuccess($$2.isClientSide());
        }
    }
}