package com.simibubi.create.content.logistics.tunnel;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.logistics.flwdata.FlapData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;

public class BeltTunnelInstance extends BlockEntityInstance<BeltTunnelBlockEntity> implements DynamicInstance {

    private final Map<Direction, ArrayList<FlapData>> tunnelFlaps = new EnumMap(Direction.class);

    public BeltTunnelInstance(MaterialManager materialManager, BeltTunnelBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        Instancer<FlapData> model = materialManager.defaultSolid().material(AllMaterialSpecs.FLAPS).getModel(AllPartialModels.BELT_TUNNEL_FLAP, this.blockState);
        int blockLight = this.world.m_45517_(LightLayer.BLOCK, this.pos);
        int skyLight = this.world.m_45517_(LightLayer.SKY, this.pos);
        blockEntity.flaps.forEach((direction, flapValue) -> {
            float flapness = flapValue.getValue(AnimationTickHolder.getPartialTicks());
            float horizontalAngle = direction.getOpposite().toYRot();
            float flapScale = direction.getAxis() == Direction.Axis.X ? 1.0F : -1.0F;
            ArrayList<FlapData> flaps = new ArrayList(4);
            for (int segment = 0; segment <= 3; segment++) {
                float intensity = segment == 3 ? 1.5F : (float) (segment + 1);
                float segmentOffset = -0.190625F * (float) segment + 0.0046875F;
                FlapData key = (FlapData) model.createInstance();
                key.setPosition(this.getInstancePosition()).setSegmentOffset(segmentOffset, 0.0F, 0.0F).setBlockLight(blockLight).setSkyLight(skyLight).setHorizontalAngle(horizontalAngle).setFlapness(flapness).setFlapScale(flapScale).setPivotVoxelSpace(0.0F, 10.0F, 1.0F).setIntensity(intensity);
                flaps.add(key);
            }
            this.tunnelFlaps.put(direction, flaps);
        });
    }

    public boolean shouldReset() {
        return super.shouldReset() || this.tunnelFlaps.size() != ((BeltTunnelBlockEntity) this.blockEntity).flaps.size();
    }

    public void beginFrame() {
        this.tunnelFlaps.forEach((direction, keys) -> {
            LerpedFloat lerpedFloat = (LerpedFloat) ((BeltTunnelBlockEntity) this.blockEntity).flaps.get(direction);
            if (lerpedFloat != null) {
                float flapness = lerpedFloat.getValue(AnimationTickHolder.getPartialTicks());
                for (FlapData flap : keys) {
                    flap.setFlapness(flapness);
                }
            }
        });
    }

    public void updateLight() {
        this.relight(this.pos, this.tunnelFlaps.values().stream().flatMap(Collection::stream));
    }

    public void remove() {
        this.tunnelFlaps.values().stream().flatMap(Collection::stream).forEach(InstanceData::delete);
    }
}