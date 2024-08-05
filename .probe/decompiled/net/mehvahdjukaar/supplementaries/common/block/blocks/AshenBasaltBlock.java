package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.List;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

public class AshenBasaltBlock extends Block {

    public AshenBasaltBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        return direction == Direction.UP && !neighborState.m_60713_((Block) ModRegistry.ASH_BLOCK.get()) ? Blocks.BASALT.defaultBlockState() : super.m_7417_(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return Blocks.BASALT.defaultBlockState().m_287290_(builder);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return Blocks.BASALT.getCloneItemStack(level, pos, Blocks.BASALT.defaultBlockState());
    }
}