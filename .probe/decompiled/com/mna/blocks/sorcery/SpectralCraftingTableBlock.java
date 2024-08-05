package com.mna.blocks.sorcery;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.blocks.tileentities.SpectralTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.gui.containers.block.ContainerSpectralCraftingTable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class SpectralCraftingTableBlock extends CraftingTableBlock implements IDontCreateBlockItem, ITranslucentBlock, EntityBlock {

    public static final Component CONTAINER_NAME = Component.translatable("container.crafting");

    public static final BooleanProperty PERMANENT = BooleanProperty.create("permanent");

    public SpectralCraftingTableBlock(BlockBehaviour.Properties properties) {
        super(properties.noOcclusion().noLootTable());
        this.m_49959_((BlockState) this.m_49966_().m_61124_(PERMANENT, false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpectralTile(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(PERMANENT);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if ((Boolean) state.m_61143_(PERMANENT)) {
            return null;
        } else {
            return type == TileEntityInit.SPECTRAL_TILE.get() && !level.isClientSide() ? (lvl, pos, state1, be) -> SpectralTile.ServerTick(lvl, pos, state1, (SpectralTile) be) : null;
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return new SimpleMenuProvider((id, inventory, player) -> new ContainerSpectralCraftingTable(id, inventory, ContainerLevelAccess.create(worldIn, pos)), CONTAINER_NAME);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}