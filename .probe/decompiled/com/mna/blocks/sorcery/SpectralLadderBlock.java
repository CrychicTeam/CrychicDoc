package com.mna.blocks.sorcery;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.blocks.tileentities.SimpleSpectralTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class SpectralLadderBlock extends LadderBlock implements EntityBlock, ITranslucentBlock, IDontCreateBlockItem {

    public SpectralLadderBlock(BlockBehaviour.Properties blockProps) {
        super(blockProps);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(SpectralCraftingTableBlock.PERMANENT, false));
    }

    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SpectralCraftingTableBlock.PERMANENT);
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