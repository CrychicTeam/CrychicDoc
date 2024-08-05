package dev.xkmc.modulargolems.compat.materials.blazegear;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class DuplicateBlazeArmsLayer<T extends AbstractGolemEntity<?, ?>, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public static final ResourceLocation TEXTURE_BLAZE_ARMS = new ResourceLocation("textures/entity/blaze.png");

    private final DuplicatedBlazeArmsModel<T> model = new DuplicatedBlazeArmsModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BGClientEvents.BLAZE_ARMS_LAYER));

    public static void registerLayer() {
        AbstractGolemRenderer.LIST.add(DuplicateBlazeArmsLayer::new);
    }

    public DuplicateBlazeArmsLayer(LivingEntityRenderer<T, M> owner) {
        super(owner);
    }

    public void render(PoseStack matrixStack, MultiBufferSource buffer, int lightness, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if ((Integer) entity.getModifiers().getOrDefault(BGCompatRegistry.BLAZING.get(), 0) != 0) {
            matrixStack.pushPose();
            m_117376_(this.model, TEXTURE_BLAZE_ARMS, matrixStack, buffer, 15728880, entity, 1.0F, 1.0F, 1.0F);
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, Minecraft.getInstance().getPartialTick() + (float) entity.f_19797_, netHeadYaw, headPitch);
            matrixStack.popPose();
        }
    }
}