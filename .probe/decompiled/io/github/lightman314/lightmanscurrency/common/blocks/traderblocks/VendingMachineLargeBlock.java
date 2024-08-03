package io.github.lightman314.lightmanscurrency.common.blocks.traderblocks;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.traders.blocks.TraderBlockTallWideRotatable;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.ItemTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.interfaces.IItemTraderBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.NonNullSupplier;

public class VendingMachineLargeBlock extends TraderBlockTallWideRotatable implements IItemTraderBlock {

    public static final int TRADECOUNT = 12;

    public VendingMachineLargeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isBlockOpaque(@Nonnull BlockState state) {
        return !(Boolean) state.m_61143_(ISBOTTOM);
    }

    @Override
    public BlockEntity makeTrader(BlockPos pos, BlockState state) {
        return new ItemTraderBlockEntity(pos, state, 12);
    }

    @Override
    public BlockEntityType<?> traderType() {
        return ModBlockEntities.ITEM_TRADER.get();
    }

    @Override
    protected NonNullSupplier<List<Component>> getItemTooltips() {
        return LCText.TOOLTIP_ITEM_TRADER.asTooltip(12);
    }
}