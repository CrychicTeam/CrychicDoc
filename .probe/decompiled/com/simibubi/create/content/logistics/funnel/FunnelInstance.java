package com.simibubi.create.content.logistics.funnel;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.logistics.flwdata.FlapData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import java.util.ArrayList;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;

public class FunnelInstance extends BlockEntityInstance<FunnelBlockEntity> implements DynamicInstance {

    private final ArrayList<FlapData> flaps = new ArrayList(4);

    public FunnelInstance(MaterialManager materialManager, FunnelBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        if (blockEntity.hasFlap()) {
            PartialModel flapPartial = this.blockState.m_60734_() instanceof FunnelBlock ? AllPartialModels.FUNNEL_FLAP : AllPartialModels.BELT_FUNNEL_FLAP;
            Instancer<FlapData> model = materialManager.defaultSolid().material(AllMaterialSpecs.FLAPS).getModel(flapPartial, this.blockState);
            int blockLight = this.world.m_45517_(LightLayer.BLOCK, this.pos);
            int skyLight = this.world.m_45517_(LightLayer.SKY, this.pos);
            Direction direction = FunnelBlock.getFunnelFacing(this.blockState);
            float flapness = blockEntity.flap.getValue(AnimationTickHolder.getPartialTicks());
            float horizontalAngle = direction.getOpposite().toYRot();
            for (int segment = 0; segment <= 3; segment++) {
                float intensity = segment == 3 ? 1.5F : (float) (segment + 1);
                float segmentOffset = -0.190625F * (float) segment + 0.0046875F;
                FlapData key = (FlapData) model.createInstance();
                key.setPosition(this.getInstancePosition()).setSegmentOffset(segmentOffset, 0.0F, -blockEntity.getFlapOffset()).setBlockLight(blockLight).setSkyLight(skyLight).setHorizontalAngle(horizontalAngle).setFlapness(flapness).setFlapScale(-1.0F).setPivotVoxelSpace(0.0F, 10.0F, 9.5F).setIntensity(intensity);
                this.flaps.add(key);
            }
        }
    }

    public void beginFrame() {
        if (this.flaps != null) {
            float flapness = ((FunnelBlockEntity) this.blockEntity).flap.getValue(AnimationTickHolder.getPartialTicks());
            for (FlapData flap : this.flaps) {
                flap.setFlapness(flapness);
            }
        }
    }

    public void updateLight() {
        if (this.flaps != null) {
            this.relight(this.pos, this.flaps.stream());
        }
    }

    public void remove() {
        if (this.flaps != null) {
            this.flaps.forEach(InstanceData::delete);
        }
    }
}