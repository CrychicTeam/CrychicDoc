package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class PumpkinBlock extends StemGrownBlock {

    protected PumpkinBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        ItemStack $$6 = player3.m_21120_(interactionHand4);
        if ($$6.is(Items.SHEARS)) {
            if (!level1.isClientSide) {
                Direction $$7 = blockHitResult5.getDirection();
                Direction $$8 = $$7.getAxis() == Direction.Axis.Y ? player3.m_6350_().getOpposite() : $$7;
                level1.playSound(null, blockPos2, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level1.setBlock(blockPos2, (BlockState) Blocks.CARVED_PUMPKIN.defaultBlockState().m_61124_(CarvedPumpkinBlock.FACING, $$8), 11);
                ItemEntity $$9 = new ItemEntity(level1, (double) blockPos2.m_123341_() + 0.5 + (double) $$8.getStepX() * 0.65, (double) blockPos2.m_123342_() + 0.1, (double) blockPos2.m_123343_() + 0.5 + (double) $$8.getStepZ() * 0.65, new ItemStack(Items.PUMPKIN_SEEDS, 4));
                $$9.m_20334_(0.05 * (double) $$8.getStepX() + level1.random.nextDouble() * 0.02, 0.05, 0.05 * (double) $$8.getStepZ() + level1.random.nextDouble() * 0.02);
                level1.m_7967_($$9);
                $$6.hurtAndBreak(1, player3, p_55287_ -> p_55287_.m_21190_(interactionHand4));
                level1.m_142346_(player3, GameEvent.SHEAR, blockPos2);
                player3.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
            }
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            return super.m_6227_(blockState0, level1, blockPos2, player3, interactionHand4, blockHitResult5);
        }
    }

    @Override
    public StemBlock getStem() {
        return (StemBlock) Blocks.PUMPKIN_STEM;
    }

    @Override
    public AttachedStemBlock getAttachedStem() {
        return (AttachedStemBlock) Blocks.ATTACHED_PUMPKIN_STEM;
    }
}