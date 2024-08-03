package net.mehvahdjukaar.moonlight.api.block;

import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.IExtraModelDataProvider;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.moonlight.api.client.util.LOD;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class DynamicRenderedItemDisplayTile extends ItemDisplayTile implements IExtraModelDataProvider {

    public static final ModelDataKey<Boolean> IS_FANCY = DynamicRenderedBlockTile.IS_FANCY;

    private boolean isFancy = false;

    private int extraFancyTicks = 0;

    protected DynamicRenderedItemDisplayTile(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state, int capacity) {
        super(tileEntityTypeIn, pos, state, capacity);
    }

    protected DynamicRenderedItemDisplayTile(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        builder.with(IS_FANCY, this.isFancy);
    }

    public abstract boolean isNeverFancy();

    public void onFancyChanged(boolean fancy) {
    }

    public boolean rendersFancy() {
        return this.isFancy;
    }

    public boolean shouldRenderFancy(Vec3 cameraPos) {
        if (this.isNeverFancy()) {
            return false;
        } else {
            boolean newFancyStatus = this.getFancyDistance(cameraPos);
            boolean oldStatus = this.isFancy;
            if (oldStatus != newFancyStatus) {
                this.isFancy = newFancyStatus;
                this.onFancyChanged(this.isFancy);
                if (this.f_58857_ == Minecraft.getInstance().level) {
                    this.requestModelReload();
                    this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 8);
                }
                if (!this.isFancy) {
                    this.extraFancyTicks = 4;
                }
            }
            if (this.extraFancyTicks > 0) {
                this.extraFancyTicks--;
                return true;
            } else {
                return this.isFancy;
            }
        }
    }

    protected boolean getFancyDistance(Vec3 cameraPos) {
        LOD lod = new LOD(cameraPos, this.m_58899_());
        return lod.isNear();
    }
}