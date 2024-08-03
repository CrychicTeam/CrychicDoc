package io.github.lightman314.lightmanscurrency.common.blocks.traderblocks;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.traders.blocks.TraderBlockTallRotatable;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.ArmorDisplayTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.interfaces.IItemTraderBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;

public class ArmorDisplayBlock extends TraderBlockTallRotatable implements IItemTraderBlock {

    public ArmorDisplayBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isBlockOpaque() {
        return false;
    }

    @Override
    public BlockEntity makeTrader(BlockPos pos, BlockState state) {
        ArmorDisplayTraderBlockEntity trader = new ArmorDisplayTraderBlockEntity(pos, state);
        trader.flagAsLoaded();
        return trader;
    }

    @Override
    public BlockEntityType<?> traderType() {
        return ModBlockEntities.ARMOR_TRADER.get();
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ArmorDisplayTraderBlockEntity) {
            ((ArmorDisplayTraderBlockEntity) blockEntity).destroyArmorStand();
        }
        super.m_6810_(state, level, pos, newState, isMoving);
    }

    @Override
    protected NonNullSupplier<List<Component>> getItemTooltips() {
        return LCText.TOOLTIP_ITEM_TRADER_ARMOR.asTooltip(4);
    }
}