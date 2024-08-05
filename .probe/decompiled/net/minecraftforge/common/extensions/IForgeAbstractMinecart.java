package net.minecraftforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public interface IForgeAbstractMinecart {

    float DEFAULT_MAX_SPEED_AIR_LATERAL = 0.4F;

    float DEFAULT_MAX_SPEED_AIR_VERTICAL = -1.0F;

    double DEFAULT_AIR_DRAG = 0.95F;

    private AbstractMinecart self() {
        return (AbstractMinecart) this;
    }

    default BlockPos getCurrentRailPosition() {
        int x = Mth.floor(this.self().m_20185_());
        int y = Mth.floor(this.self().m_20186_());
        int z = Mth.floor(this.self().m_20189_());
        BlockPos pos = new BlockPos(x, y, z);
        if (this.self().m_9236_().getBlockState(pos.below()).m_204336_(BlockTags.RAILS)) {
            pos = pos.below();
        }
        return pos;
    }

    double getMaxSpeedWithRail();

    void moveMinecartOnRail(BlockPos var1);

    boolean canUseRail();

    void setCanUseRail(boolean var1);

    default boolean shouldDoRailFunctions() {
        return true;
    }

    default boolean isPoweredCart() {
        return this.self().getMinecartType() == AbstractMinecart.Type.FURNACE;
    }

    default boolean canBeRidden() {
        return this.self().getMinecartType() == AbstractMinecart.Type.RIDEABLE;
    }

    default float getMaxCartSpeedOnRail() {
        return 1.2F;
    }

    float getCurrentCartSpeedCapOnRail();

    void setCurrentCartSpeedCapOnRail(float var1);

    float getMaxSpeedAirLateral();

    void setMaxSpeedAirLateral(float var1);

    float getMaxSpeedAirVertical();

    void setMaxSpeedAirVertical(float var1);

    double getDragAir();

    void setDragAir(double var1);

    default double getSlopeAdjustment() {
        return 0.0078125;
    }

    default int getComparatorLevel() {
        return -1;
    }
}