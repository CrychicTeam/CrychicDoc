package com.mna.blocks.manaweaving;

import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.ManaResevoirTile;
import com.mna.blocks.tileentities.ManaweaveProjectorTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ManaResevoirBlock extends WaterloggableBlock implements ICutoutBlock, EntityBlock {

    public static final IntegerProperty FILL_LEVEL = IntegerProperty.create("fill_level", 0, 4);

    public static final int EMPTY = 0;

    public static final int QUARTER = 1;

    public static final int HALF = 2;

    public static final int THREE_QUARTERS = 3;

    public static final int FULL = 4;

    public ManaResevoirBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F).noOcclusion(), false);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(FILL_LEVEL, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Block.box(6.0, 0.0, 6.0, 10.0, 8.0, 10.0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FILL_LEVEL);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManaResevoirTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.MANA_RESEVOIR.get() ? (lvl, pos, state1, be) -> ManaResevoirTile.Tick(lvl, pos, state1, (ManaResevoirTile) be) : null;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te != null && te instanceof ManaweaveProjectorTile) {
            float pct = ((ManaweaveProjectorTile) te).getMana() / 100.0F;
            return (int) (pct * 15.0F);
        } else {
            return 0;
        }
    }
}