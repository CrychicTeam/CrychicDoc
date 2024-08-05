package net.mehvahdjukaar.supplementaries.common.block.tiles;

import net.mehvahdjukaar.supplementaries.common.block.blocks.WindVaneBlock;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.integration.BreezyCompat;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WindVaneBlockTile extends BlockEntity {

    private float yaw = 0.0F;

    private float prevYaw = 0.0F;

    private float offset = 0.0F;

    public WindVaneBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.WIND_VANE_TILE.get(), pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        float tp = (float) (Math.PI * 2);
        this.offset = 400.0F * (Mth.sin(0.005F * (float) this.f_58858_.m_123341_() % tp) + Mth.sin(0.005F * (float) this.f_58858_.m_123343_() % tp) + Mth.sin(0.005F * (float) this.f_58858_.m_123342_() % tp));
    }

    public float getYaw(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevYaw, this.yaw);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, WindVaneBlockTile tile) {
        float currentYaw = tile.yaw;
        tile.prevYaw = currentYaw;
        if (!pLevel.isClientSide()) {
            if (pLevel.getGameTime() % 20L == 0L) {
                Block block = pState.m_60734_();
                if (block instanceof WindVaneBlock) {
                    WindVaneBlock.updatePower(pState, pLevel, pPos);
                }
            }
        } else {
            int power = (Integer) pState.m_61143_(WindVaneBlock.WIND_STRENGTH);
            float tp = (float) (Math.PI * 2);
            float t = (float) (pLevel.getGameTime() % 24000L) + tile.offset;
            float b = (float) Math.max(1.0, (double) power * (Double) ClientConfigs.Blocks.WIND_VANE_POWER_SCALING.get());
            double maxAngle1 = (Double) ClientConfigs.Blocks.WIND_VANE_ANGLE_1.get();
            double maxAngle2 = (Double) ClientConfigs.Blocks.WIND_VANE_ANGLE_2.get();
            double period1 = (Double) ClientConfigs.Blocks.WIND_VANE_PERIOD_1.get();
            double period2 = (Double) ClientConfigs.Blocks.WIND_VANE_PERIOD_2.get();
            float newYaw = (float) (maxAngle1 * (double) Mth.sin((float) ((double) tp * ((double) (t * b) / period1 % 360.0))) + maxAngle2 * (double) Mth.sin((float) ((double) tp * ((double) (t * b) / period2 % 360.0))));
            newYaw += CompatHandler.BREEZY ? BreezyCompat.getWindDirection(pPos, pLevel) : 90.0F;
            tile.yaw = Mth.clamp(newYaw, currentYaw - 8.0F, currentYaw + 8.0F);
        }
    }
}