package fr.lucreeper74.createmetallurgy.content.belt_grinder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import fr.lucreeper74.createmetallurgy.registries.CMPartialModels;
import fr.lucreeper74.createmetallurgy.registries.CMSpriteShifts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BeltGrinderRenderer extends SafeBlockEntityRenderer<BeltGrinderBlockEntity> {

    public BeltGrinderRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(BeltGrinderBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        this.renderShaft(be, ms, buffer, light, overlay);
        this.renderBelt(be, ms, buffer, light);
        this.renderItems(be, partialTicks, ms, buffer, light, overlay);
        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, buffer, light, overlay);
    }

    protected void renderBelt(BeltGrinderBlockEntity be, PoseStack ms, MultiBufferSource buffer, int light) {
        BlockState blockState = be.m_58900_();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        SpriteShiftEntry beltShift = CMSpriteShifts.SAND_PAPER_BELT;
        float speed = be.getSpeed() * 2.0F;
        float renderTick = AnimationTickHolder.getRenderTime(be.m_58904_());
        float time = renderTick * (float) Direction.AxisDirection.POSITIVE.getStep();
        float spriteSize = beltShift.getTarget().getV1() - beltShift.getTarget().getV0();
        double scroll = (double) (speed * time) / 504.0;
        scroll -= Math.floor(scroll);
        scroll = scroll * (double) spriteSize * 0.5;
        SuperByteBuffer rotatedCoil = CachedBufferer.partialFacing(CMPartialModels.GRINDER_BELT, blockState, (Direction) blockState.m_61143_(BlockStateProperties.HORIZONTAL_FACING));
        rotatedCoil.light(light).renderInto(ms, vb);
        rotatedCoil.shiftUVScrolling(beltShift, (float) scroll).light(light).renderInto(ms, vb);
    }

    protected void renderItems(BeltGrinderBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!be.inv.isEmpty()) {
            boolean alongZ = ((Direction) be.m_58900_().m_61143_(BeltGrinderBlock.HORIZONTAL_FACING)).getAxis() == Direction.Axis.Z;
            ms.pushPose();
            float offset = getOffset(be, partialTicks, alongZ);
            for (int i = 0; i < be.inv.getSlots(); i++) {
                ItemStack stack = be.inv.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    Minecraft mc = Minecraft.getInstance();
                    ItemRenderer itemRenderer = mc.getItemRenderer();
                    BakedModel modelWithOverrides = itemRenderer.getModel(stack, be.m_58904_(), null, 0);
                    boolean blockItem = modelWithOverrides.isGui3d();
                    ms.translate(alongZ ? (double) offset : 0.5, blockItem ? 0.925F : 0.8125, alongZ ? 0.5 : (double) offset);
                    ms.scale(0.5F, 0.5F, 0.5F);
                    if (alongZ) {
                        ms.mulPose(Axis.YP.rotationDegrees(90.0F));
                    }
                    ms.mulPose(Axis.XP.rotationDegrees(90.0F));
                    itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, ms, buffer, mc.level, 0);
                    break;
                }
            }
            ms.popPose();
        }
    }

    private void renderShaft(BeltGrinderBlockEntity be, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        KineticBlockEntityRenderer.renderRotatingBuffer(be, this.getRotatedModel(be), ms, buffer.getBuffer(RenderType.solid()), light);
    }

    private SuperByteBuffer getRotatedModel(BeltGrinderBlockEntity be) {
        return CachedBufferer.block(KineticBlockEntityRenderer.KINETIC_BLOCK, this.getRenderedBlockState(be));
    }

    protected BlockState getRenderedBlockState(KineticBlockEntity be) {
        return KineticBlockEntityRenderer.shaft(KineticBlockEntityRenderer.getRotationAxisOf(be));
    }

    private static float getOffset(BeltGrinderBlockEntity be, float partialTicks, boolean alongZ) {
        boolean moving = be.inv.recipeDuration != 0.0F;
        float offset = moving ? be.inv.remainingTime / be.inv.recipeDuration : 0.0F;
        float processingSpeed = Mth.clamp(Math.abs(be.getSpeed()) / 32.0F, 1.0F, 128.0F);
        if (moving) {
            offset = Mth.clamp(offset + (-partialTicks + 0.5F) * processingSpeed / be.inv.recipeDuration, 0.125F, 1.0F);
            if (!be.inv.appliedRecipe) {
                offset++;
            }
            offset /= 2.0F;
        }
        if (be.getSpeed() == 0.0F) {
            offset = 0.5F;
        }
        if (be.getSpeed() < 0.0F ^ alongZ) {
            offset = 1.0F - offset;
        }
        return offset;
    }
}