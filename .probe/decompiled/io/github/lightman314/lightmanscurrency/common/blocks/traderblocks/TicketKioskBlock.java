package io.github.lightman314.lightmanscurrency.common.blocks.traderblocks;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.LazyShapes;
import io.github.lightman314.lightmanscurrency.api.traders.blocks.TraderBlockTallRotatable;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.TicketTraderBlockEntity;
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

public class TicketKioskBlock extends TraderBlockTallRotatable implements IItemTraderBlock {

    public static final int TRADECOUNT = 4;

    private static final VoxelShape HORIZ_SHAPE = m_49796_(3.0, 0.0, 1.0, 13.0, 32.0, 15.0);

    private static final VoxelShape VERT_SHAPE = m_49796_(1.0, 0.0, 3.0, 15.0, 32.0, 13.0);

    public TicketKioskBlock(BlockBehaviour.Properties properties) {
        super(properties, LazyShapes.lazyTallDirectionalShape(VERT_SHAPE, HORIZ_SHAPE, VERT_SHAPE, HORIZ_SHAPE));
    }

    @Override
    protected boolean isBlockOpaque() {
        return false;
    }

    @Override
    public BlockEntity makeTrader(BlockPos pos, BlockState state) {
        return new TicketTraderBlockEntity(pos, state, 4);
    }

    @Override
    public BlockEntityType<?> traderType() {
        return ModBlockEntities.TICKET_TRADER.get();
    }

    @Override
    protected NonNullSupplier<List<Component>> getItemTooltips() {
        return LCText.TOOLTIP_ITEM_TRADER_TICKET.asTooltip(4);
    }
}