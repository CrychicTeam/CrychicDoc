package com.simibubi.create.content.logistics.depot;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.util.transform.Rotate;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.jozufozu.flywheel.util.transform.Translate;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class EjectorRenderer extends ShaftRenderer<EjectorBlockEntity> {

    static final Vec3 pivot = VecHelper.voxelSpace(0.0, 11.25, 0.75);

    public EjectorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public boolean shouldRenderOffScreen(EjectorBlockEntity p_188185_1_) {
        return true;
    }

    protected void renderSafe(EjectorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.solid());
        float lidProgress = be.getLidProgress(partialTicks);
        float angle = lidProgress * 70.0F;
        if (!Backend.canUseInstancing(be.m_58904_())) {
            SuperByteBuffer model = CachedBufferer.partial(AllPartialModels.EJECTOR_TOP, be.m_58900_());
            applyLidAngle(be, angle, model);
            model.light(light).renderInto(ms, vertexBuilder);
        }
        TransformStack msr = TransformStack.cast(ms);
        float maxTime = (float) (be.earlyTarget != null ? (double) be.earlyTargetTime : be.launcher.getTotalFlyingTicks());
        for (IntAttached<ItemStack> intAttached : be.launchedItems) {
            float time = (float) intAttached.getFirst().intValue() + partialTicks;
            if (!(time > maxTime)) {
                ms.pushPose();
                Vec3 launchedItemLocation = be.getLaunchedItemLocation(time);
                msr.translate(launchedItemLocation.subtract(Vec3.atLowerCornerOf(be.m_58899_())));
                Vec3 itemRotOffset = VecHelper.voxelSpace(0.0, 3.0, 0.0);
                msr.translate(itemRotOffset);
                msr.rotateY((double) AngleHelper.horizontalAngle(be.getFacing()));
                msr.rotateX((double) (time * 40.0F));
                msr.translateBack(itemRotOffset);
                Minecraft.getInstance().getItemRenderer().renderStatic(intAttached.getValue(), ItemDisplayContext.GROUND, light, overlay, ms, buffer, be.m_58904_(), 0);
                ms.popPose();
            }
        }
        DepotBehaviour behaviour = be.getBehaviour(DepotBehaviour.TYPE);
        if (behaviour != null && !behaviour.isEmpty()) {
            ms.pushPose();
            applyLidAngle(be, angle, msr);
            ((TransformStack) ((TransformStack) msr.centre()).rotateY((double) (-180.0F - AngleHelper.horizontalAngle((Direction) be.m_58900_().m_61143_(EjectorBlock.HORIZONTAL_FACING))))).unCentre();
            DepotRenderer.renderItemsOf(be, partialTicks, ms, buffer, light, overlay, behaviour);
            ms.popPose();
        }
    }

    static <T extends Translate<T> & Rotate<T>> void applyLidAngle(KineticBlockEntity be, float angle, T tr) {
        applyLidAngle(be, pivot, angle, tr);
    }

    static <T extends Translate<T> & Rotate<T>> void applyLidAngle(KineticBlockEntity be, Vec3 rotationOffset, float angle, T tr) {
        ((Translate) ((Rotate) ((Translate) ((Translate) ((Translate) ((Rotate) ((Translate) tr.centre())).rotateY((double) (180.0F + AngleHelper.horizontalAngle((Direction) be.m_58900_().m_61143_(EjectorBlock.HORIZONTAL_FACING))))).unCentre()).translate(rotationOffset))).rotateX((double) (-angle))).translateBack(rotationOffset);
    }
}