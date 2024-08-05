package io.github.lightman314.lightmanscurrency.common.blocks.traderblocks;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.LazyShapes;
import io.github.lightman314.lightmanscurrency.api.traders.blocks.TraderBlockRotatable;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.ItemTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.interfaces.IItemTraderBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.NonNullSupplier;

public class ShelfBlock extends TraderBlockRotatable implements IItemTraderBlock {

    protected final int tradeCount;

    private static final VoxelShape SHAPE_NORTH = m_49796_(0.0, 0.0, 0.0, 16.0, 16.0, 5.0);

    private static final VoxelShape SHAPE_SOUTH = m_49796_(0.0, 0.0, 11.0, 16.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_EAST = m_49796_(11.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_WEST = m_49796_(0.0, 0.0, 0.0, 5.0, 16.0, 16.0);

    public ShelfBlock(BlockBehaviour.Properties properties) {
        this(properties, 1);
    }

    public ShelfBlock(BlockBehaviour.Properties properties, int tradeCount) {
        super(properties, LazyShapes.lazyDirectionalShape(SHAPE_NORTH, SHAPE_EAST, SHAPE_SOUTH, SHAPE_WEST));
        this.tradeCount = tradeCount;
    }

    @Override
    public BlockEntity makeTrader(BlockPos pos, BlockState state) {
        return new ItemTraderBlockEntity(pos, state, this.tradeCount);
    }

    @Override
    public BlockEntityType<?> traderType() {
        return ModBlockEntities.ITEM_TRADER.get();
    }

    @Override
    protected NonNullSupplier<List<Component>> getItemTooltips() {
        return LCText.TOOLTIP_ITEM_TRADER.asTooltip(this.tradeCount);
    }
}