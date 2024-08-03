package net.minecraft.world.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class ShearsItem extends Item {

    public ShearsItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public boolean mineBlock(ItemStack itemStack0, Level level1, BlockState blockState2, BlockPos blockPos3, LivingEntity livingEntity4) {
        if (!level1.isClientSide && !blockState2.m_204336_(BlockTags.FIRE)) {
            itemStack0.hurtAndBreak(1, livingEntity4, p_43076_ -> p_43076_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return !blockState2.m_204336_(BlockTags.LEAVES) && !blockState2.m_60713_(Blocks.COBWEB) && !blockState2.m_60713_(Blocks.GRASS) && !blockState2.m_60713_(Blocks.FERN) && !blockState2.m_60713_(Blocks.DEAD_BUSH) && !blockState2.m_60713_(Blocks.HANGING_ROOTS) && !blockState2.m_60713_(Blocks.VINE) && !blockState2.m_60713_(Blocks.TRIPWIRE) && !blockState2.m_204336_(BlockTags.WOOL) ? super.mineBlock(itemStack0, level1, blockState2, blockPos3, livingEntity4) : true;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState0) {
        return blockState0.m_60713_(Blocks.COBWEB) || blockState0.m_60713_(Blocks.REDSTONE_WIRE) || blockState0.m_60713_(Blocks.TRIPWIRE);
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack0, BlockState blockState1) {
        if (blockState1.m_60713_(Blocks.COBWEB) || blockState1.m_204336_(BlockTags.LEAVES)) {
            return 15.0F;
        } else if (blockState1.m_204336_(BlockTags.WOOL)) {
            return 5.0F;
        } else {
            return !blockState1.m_60713_(Blocks.VINE) && !blockState1.m_60713_(Blocks.GLOW_LICHEN) ? super.getDestroySpeed(itemStack0, blockState1) : 2.0F;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        if ($$3.m_60734_() instanceof GrowingPlantHeadBlock $$5 && !$$5.isMaxAge($$3)) {
            Player $$6 = useOnContext0.getPlayer();
            ItemStack $$7 = useOnContext0.getItemInHand();
            if ($$6 instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) $$6, $$2, $$7);
            }
            $$1.playSound($$6, $$2, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
            BlockState $$8 = $$5.getMaxAgeState($$3);
            $$1.setBlockAndUpdate($$2, $$8);
            $$1.m_220407_(GameEvent.BLOCK_CHANGE, $$2, GameEvent.Context.of(useOnContext0.getPlayer(), $$8));
            if ($$6 != null) {
                $$7.hurtAndBreak(1, $$6, p_186374_ -> p_186374_.m_21190_(useOnContext0.getHand()));
            }
            return InteractionResult.sidedSuccess($$1.isClientSide);
        }
        return super.useOn(useOnContext0);
    }
}