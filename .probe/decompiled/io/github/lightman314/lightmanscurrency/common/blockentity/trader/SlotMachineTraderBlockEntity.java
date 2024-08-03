package io.github.lightman314.lightmanscurrency.common.blockentity.trader;

import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.blockentity.TraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineTraderData;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SlotMachineTraderBlockEntity extends TraderBlockEntity<SlotMachineTraderData> {

    public SlotMachineTraderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SLOT_MACHINE_TRADER.get(), pos, state);
    }

    @Nullable
    protected SlotMachineTraderData castOrNullify(@Nonnull TraderData trader) {
        return trader instanceof SlotMachineTraderData ? (SlotMachineTraderData) trader : null;
    }

    @Nonnull
    protected SlotMachineTraderData buildNewTrader() {
        return new SlotMachineTraderData(this.f_58857_, this.f_58858_);
    }
}