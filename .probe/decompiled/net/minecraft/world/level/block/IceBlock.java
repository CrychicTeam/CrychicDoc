package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class IceBlock extends HalfTransparentBlock {

    public IceBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    public static BlockState meltsInto() {
        return Blocks.WATER.defaultBlockState();
    }

    @Override
    public void playerDestroy(Level level0, Player player1, BlockPos blockPos2, BlockState blockState3, @Nullable BlockEntity blockEntity4, ItemStack itemStack5) {
        super.m_6240_(level0, player1, blockPos2, blockState3, blockEntity4, itemStack5);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack5) == 0) {
            if (level0.dimensionType().ultraWarm()) {
                level0.removeBlock(blockPos2, false);
                return;
            }
            BlockState $$6 = level0.getBlockState(blockPos2.below());
            if ($$6.m_280555_() || $$6.m_278721_()) {
                level0.setBlockAndUpdate(blockPos2, meltsInto());
            }
        }
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (serverLevel1.m_45517_(LightLayer.BLOCK, blockPos2) > 11 - blockState0.m_60739_(serverLevel1, blockPos2)) {
            this.melt(blockState0, serverLevel1, blockPos2);
        }
    }

    protected void melt(BlockState blockState0, Level level1, BlockPos blockPos2) {
        if (level1.dimensionType().ultraWarm()) {
            level1.removeBlock(blockPos2, false);
        } else {
            level1.setBlockAndUpdate(blockPos2, meltsInto());
            level1.neighborChanged(blockPos2, meltsInto().m_60734_(), blockPos2);
        }
    }
}