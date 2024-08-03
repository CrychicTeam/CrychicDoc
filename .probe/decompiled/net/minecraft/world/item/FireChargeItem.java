package net.minecraft.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;

public class FireChargeItem extends Item {

    public FireChargeItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        boolean $$4 = false;
        if (!CampfireBlock.canLight($$3) && !CandleBlock.canLight($$3) && !CandleCakeBlock.canLight($$3)) {
            $$2 = $$2.relative(useOnContext0.getClickedFace());
            if (BaseFireBlock.canBePlacedAt($$1, $$2, useOnContext0.getHorizontalDirection())) {
                this.playSound($$1, $$2);
                $$1.setBlockAndUpdate($$2, BaseFireBlock.getState($$1, $$2));
                $$1.m_142346_(useOnContext0.getPlayer(), GameEvent.BLOCK_PLACE, $$2);
                $$4 = true;
            }
        } else {
            this.playSound($$1, $$2);
            $$1.setBlockAndUpdate($$2, (BlockState) $$3.m_61124_(BlockStateProperties.LIT, true));
            $$1.m_142346_(useOnContext0.getPlayer(), GameEvent.BLOCK_CHANGE, $$2);
            $$4 = true;
        }
        if ($$4) {
            useOnContext0.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess($$1.isClientSide);
        } else {
            return InteractionResult.FAIL;
        }
    }

    private void playSound(Level level0, BlockPos blockPos1) {
        RandomSource $$2 = level0.getRandom();
        level0.playSound(null, blockPos1, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, ($$2.nextFloat() - $$2.nextFloat()) * 0.2F + 1.0F);
    }
}