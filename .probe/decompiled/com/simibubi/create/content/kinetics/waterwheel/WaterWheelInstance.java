package com.simibubi.create.content.kinetics.waterwheel;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.model.BlockModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.CutoutRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WaterWheelInstance<T extends WaterWheelBlockEntity> extends CutoutRotatingInstance<T> {

    protected final boolean large;

    protected final WaterWheelModelKey key;

    public WaterWheelInstance(MaterialManager materialManager, T blockEntity, boolean large) {
        super(materialManager, blockEntity);
        this.large = large;
        this.key = new WaterWheelModelKey(large, this.getRenderedBlockState(), blockEntity.material);
    }

    public static <T extends WaterWheelBlockEntity> WaterWheelInstance<T> standard(MaterialManager materialManager, T blockEntity) {
        return new WaterWheelInstance<>(materialManager, blockEntity, false);
    }

    public static <T extends WaterWheelBlockEntity> WaterWheelInstance<T> large(MaterialManager materialManager, T blockEntity) {
        return new WaterWheelInstance<>(materialManager, blockEntity, true);
    }

    public boolean shouldReset() {
        return super.shouldReset() || this.key.material() != ((WaterWheelBlockEntity) this.blockEntity).material;
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        return this.getRotatingMaterial().model(this.key, () -> {
            BakedModel model = WaterWheelRenderer.generateModel(this.key);
            BlockState state = this.key.state();
            Direction dir;
            if (this.key.large()) {
                dir = Direction.fromAxisAndDirection((Direction.Axis) state.m_61143_(LargeWaterWheelBlock.AXIS), Direction.AxisDirection.POSITIVE);
            } else {
                dir = (Direction) state.m_61143_(WaterWheelBlock.FACING);
            }
            PoseStack transform = (PoseStack) CachedBufferer.rotateToFaceVertical(dir).get();
            return BlockModel.of(model, Blocks.AIR.defaultBlockState(), transform);
        });
    }
}