package com.simibubi.create.content.trains.entity;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.entity.EntityInstance;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.bogey.BogeyInstance;
import com.simibubi.create.content.trains.bogey.BogeyRenderer;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import org.joml.Vector3f;

public class CarriageContraptionInstance extends EntityInstance<CarriageContraptionEntity> implements DynamicInstance {

    private final PoseStack ms = new PoseStack();

    private Carriage carriage;

    private Couple<BogeyInstance> bogeys;

    private Couple<Boolean> bogeyHidden = Couple.create(() -> false);

    public CarriageContraptionInstance(MaterialManager materialManager, CarriageContraptionEntity entity) {
        super(materialManager, entity);
        entity.bindInstance(this);
    }

    public void init() {
        this.carriage = ((CarriageContraptionEntity) this.entity).getCarriage();
        if (this.carriage != null) {
            this.bogeys = this.carriage.bogeys.mapNotNullWithParam((bogey, manager) -> bogey.getStyle().createInstance(bogey, bogey.type.getSize(), manager), this.materialManager);
            this.updateLight();
        }
    }

    public void setBogeyVisibility(boolean first, boolean visible) {
        this.bogeyHidden.set(first, !visible);
    }

    public void beginFrame() {
        if (this.bogeys == null) {
            if (((CarriageContraptionEntity) this.entity).isReadyForRender()) {
                this.init();
            }
        } else {
            float partialTicks = AnimationTickHolder.getPartialTicks();
            float viewYRot = ((CarriageContraptionEntity) this.entity).m_5675_(partialTicks);
            float viewXRot = ((CarriageContraptionEntity) this.entity).m_5686_(partialTicks);
            int bogeySpacing = this.carriage.bogeySpacing;
            this.ms.pushPose();
            Vector3f instancePosition = this.getInstancePosition(partialTicks);
            TransformStack.cast(this.ms).translate(instancePosition);
            for (boolean current : Iterate.trueAndFalse) {
                BogeyInstance instance = this.bogeys.get(current);
                if (instance != null) {
                    if (this.bogeyHidden.get(current)) {
                        instance.beginFrame(0.0F, null);
                    } else {
                        this.ms.pushPose();
                        CarriageBogey bogey = instance.bogey;
                        CarriageContraptionEntityRenderer.translateBogey(this.ms, bogey, bogeySpacing, viewYRot, viewXRot, partialTicks);
                        this.ms.translate(0.0, -1.5078125, 0.0);
                        instance.beginFrame(bogey.wheelAngle.getValue(partialTicks), this.ms);
                        this.ms.popPose();
                    }
                }
            }
            this.ms.popPose();
        }
    }

    public void updateLight() {
        if (this.bogeys != null) {
            this.bogeys.forEach(instance -> {
                if (instance != null) {
                    instance.updateLight(this.world, (CarriageContraptionEntity) this.entity);
                }
            });
        }
    }

    public void remove() {
        if (this.bogeys != null) {
            this.bogeys.forEach(instance -> {
                if (instance != null) {
                    instance.commonRenderer.ifPresent(BogeyRenderer::remove);
                    instance.renderer.remove();
                }
            });
        }
    }

    public boolean decreaseFramerateWithDistance() {
        return false;
    }
}