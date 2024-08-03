package com.mna.blocks.sorcery;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.blocks.tileentities.SimpleSpectralTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class SpectralWebBlock extends WebBlock implements IDontCreateBlockItem, EntityBlock, ITranslucentBlock {

    public SpectralWebBlock(BlockBehaviour.Properties blockProps) {
        super(blockProps);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(SpectralCraftingTableBlock.PERMANENT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(SpectralCraftingTableBlock.PERMANENT);
    }

    public boolean isFireSource(BlockState state, LevelReader level, BlockPos pos, Direction direction) {
        return true;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 10000;
    }

    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 10000;
    }

    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SimpleSpectralTile(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if ((Boolean) state.m_61143_(SpectralCraftingTableBlock.PERMANENT)) {
            return null;
        } else {
            return type == TileEntityInit.SIMPLE_SPECTRAL_TILE.get() && !level.isClientSide() ? (lvl, pos, state1, be) -> SimpleSpectralTile.ServerTick(lvl, pos, state1, (SimpleSpectralTile) be) : null;
        }
    }
}