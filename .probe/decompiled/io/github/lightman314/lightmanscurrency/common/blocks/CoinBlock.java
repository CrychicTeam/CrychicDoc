package io.github.lightman314.lightmanscurrency.common.blocks;

import io.github.lightman314.lightmanscurrency.common.core.ModSounds;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoinBlock extends FallingBlock {

    private final Supplier<Item> coinItem;

    public CoinBlock(BlockBehaviour.Properties properties, Supplier<Item> coinItem) {
        super(properties);
        this.coinItem = coinItem;
    }

    protected boolean isFullBlock() {
        return true;
    }

    protected int getCoinCount() {
        return 36;
    }

    @Nonnull
    @Override
    public VoxelShape getOcclusionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return this.isFullBlock() ? super.m_7952_(state, level, pos) : Shapes.empty();
    }

    protected SoundEvent getBreakingSound() {
        return ModSounds.COINS_CLINKING.get();
    }

    @Override
    public void onLand(Level level, @Nonnull BlockPos pos, @Nonnull BlockState fallingState, @Nonnull BlockState hitState, @Nonnull FallingBlockEntity fallingBlock) {
        if (!level.isClientSide) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            for (int i = 0; i < this.getCoinCount(); i++) {
                Block.popResource(level, pos, new ItemStack((ItemLike) this.coinItem.get()));
            }
            level.playSound(null, pos, this.getBreakingSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }
}