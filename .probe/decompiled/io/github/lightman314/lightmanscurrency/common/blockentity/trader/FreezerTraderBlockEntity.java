package io.github.lightman314.lightmanscurrency.common.blockentity.trader;

import io.github.lightman314.lightmanscurrency.api.misc.IClientTicker;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class FreezerTraderBlockEntity extends ItemTraderBlockEntity implements IClientTicker {

    private float doorAngle;

    private float prevDoorAngle;

    public FreezerTraderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FREEZER_TRADER.get(), pos, state);
    }

    public FreezerTraderBlockEntity(BlockPos pos, BlockState state, int tradeCount) {
        super(ModBlockEntities.FREEZER_TRADER.get(), pos, state, tradeCount);
    }

    public float getDoorAngle(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevDoorAngle, this.doorAngle);
    }

    @Override
    public void clientTick() {
        TraderData trader = this.getTraderData();
        if (trader != null) {
            int userCount = trader.getUserCount();
            this.prevDoorAngle = this.doorAngle;
            if (userCount > 0 && this.doorAngle == 0.0F) {
                this.f_58857_.playLocalSound((double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.5, (double) this.f_58858_.m_123343_() + 0.5, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.9F, false);
            }
            if (userCount > 0 && this.doorAngle < 1.0F) {
                this.doorAngle += 0.1F;
            } else if (userCount <= 0 && this.doorAngle > 0.0F) {
                this.doorAngle -= 0.1F;
                if (this.doorAngle < 0.5F && this.prevDoorAngle >= 0.5F) {
                    this.f_58857_.playLocalSound((double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.5, (double) this.f_58858_.m_123343_() + 0.5, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.9F, false);
                }
            }
            if (this.doorAngle > 1.0F) {
                this.doorAngle = 1.0F;
            } else if (this.doorAngle < 0.0F) {
                this.doorAngle = 0.0F;
            }
        }
    }
}