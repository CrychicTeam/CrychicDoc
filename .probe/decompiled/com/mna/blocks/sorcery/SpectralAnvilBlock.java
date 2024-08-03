package com.mna.blocks.sorcery;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.blocks.tileentities.SpectralTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.gui.containers.block.ContainerSpectralAnvil;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SpectralAnvilBlock extends AnvilBlock implements IDontCreateBlockItem, EntityBlock, ITranslucentBlock {

    public static final Component CONTAINER_TITLE = Component.translatable("container.repair");

    public SpectralAnvilBlock(BlockBehaviour.Properties p_i48450_1_) {
        super(p_i48450_1_);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(SpectralCraftingTableBlock.PERMANENT, false));
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState p_220052_1_, Level p_220052_2_, BlockPos p_220052_3_) {
        return new SimpleMenuProvider((p_220272_2_, p_220272_3_, p_220272_4_) -> new ContainerSpectralAnvil(p_220272_2_, p_220272_3_, ContainerLevelAccess.create(p_220052_2_, p_220052_3_)), CONTAINER_TITLE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SpectralCraftingTableBlock.PERMANENT);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpectralTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if ((Boolean) state.m_61143_(SpectralCraftingTableBlock.PERMANENT)) {
            return null;
        } else {
            return type == TileEntityInit.SPECTRAL_TILE.get() && !level.isClientSide() ? (lvl, pos, state1, be) -> SpectralTile.ServerTick(lvl, pos, state1, (SpectralTile) be) : null;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return Shapes.block();
    }
}