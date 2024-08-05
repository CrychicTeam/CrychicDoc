package com.simibubi.create.content.trains.bogey;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

public final class BogeyInstance {

    private final BogeySizes.BogeySize size;

    private final BogeyStyle style;

    public final CarriageBogey bogey;

    public final BogeyRenderer renderer;

    public final Optional<BogeyRenderer.CommonRenderer> commonRenderer;

    public BogeyInstance(CarriageBogey bogey, BogeyStyle style, BogeySizes.BogeySize size, MaterialManager materialManager) {
        this.bogey = bogey;
        this.size = size;
        this.style = style;
        this.renderer = this.style.createRendererInstance(this.size);
        this.commonRenderer = this.style.getNewCommonRenderInstance();
        this.commonRenderer.ifPresent(bogeyRenderer -> bogeyRenderer.initialiseContraptionModelData(materialManager, bogey));
        this.renderer.initialiseContraptionModelData(materialManager, bogey);
    }

    public void beginFrame(float wheelAngle, PoseStack ms) {
        if (ms == null) {
            this.renderer.emptyTransforms();
        } else {
            this.commonRenderer.ifPresent(bogeyRenderer -> bogeyRenderer.render(this.bogey.bogeyData, wheelAngle, ms));
            this.renderer.render(this.bogey.bogeyData, wheelAngle, ms);
        }
    }

    public void updateLight(BlockAndTintGetter world, CarriageContraptionEntity entity) {
        BlockPos lightPos = BlockPos.containing(this.getLightPos(entity));
        this.commonRenderer.ifPresent(bogeyRenderer -> bogeyRenderer.updateLight(world.getBrightness(LightLayer.BLOCK, lightPos), world.getBrightness(LightLayer.SKY, lightPos)));
        this.renderer.updateLight(world.getBrightness(LightLayer.BLOCK, lightPos), world.getBrightness(LightLayer.SKY, lightPos));
    }

    private Vec3 getLightPos(CarriageContraptionEntity entity) {
        return this.bogey.getAnchorPosition() != null ? this.bogey.getAnchorPosition() : entity.m_7371_(AnimationTickHolder.getPartialTicks());
    }

    @FunctionalInterface
    interface BogeyInstanceFactory {

        BogeyInstance create(CarriageBogey var1, BogeySizes.BogeySize var2, MaterialManager var3);
    }
}