package com.simibubi.create.content.contraptions.render;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.jozufozu.flywheel.event.BeginFrameEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.util.Mth;

public class ContraptionRenderInfo {

    public final Contraption contraption;

    public final VirtualRenderWorld renderWorld;

    private final ContraptionMatrices matrices = new ContraptionMatrices();

    private boolean visible;

    public ContraptionRenderInfo(Contraption contraption, VirtualRenderWorld renderWorld) {
        this.contraption = contraption;
        this.renderWorld = renderWorld;
    }

    public int getEntityId() {
        return this.contraption.entity.m_19879_();
    }

    public boolean isDead() {
        return !this.contraption.entity.isAliveOrStale();
    }

    public void beginFrame(BeginFrameEvent event) {
        this.matrices.clear();
        AbstractContraptionEntity entity = this.contraption.entity;
        this.visible = event.getFrustum().isVisible(entity.m_6921_().inflate(2.0));
    }

    public boolean isVisible() {
        return this.visible && this.contraption.entity.isAliveOrStale() && this.contraption.entity.isReadyForRender();
    }

    public void setupMatrices(PoseStack viewProjection, double camX, double camY, double camZ) {
        if (!this.matrices.isReady()) {
            AbstractContraptionEntity entity = this.contraption.entity;
            viewProjection.pushPose();
            double x = Mth.lerp((double) AnimationTickHolder.getPartialTicks(), entity.f_19790_, entity.m_20185_()) - camX;
            double y = Mth.lerp((double) AnimationTickHolder.getPartialTicks(), entity.f_19791_, entity.m_20186_()) - camY;
            double z = Mth.lerp((double) AnimationTickHolder.getPartialTicks(), entity.f_19792_, entity.m_20189_()) - camZ;
            viewProjection.translate(x, y, z);
            this.matrices.setup(viewProjection, entity);
            viewProjection.popPose();
        }
    }

    public ContraptionMatrices getMatrices() {
        return this.matrices;
    }

    public void invalidate() {
    }
}