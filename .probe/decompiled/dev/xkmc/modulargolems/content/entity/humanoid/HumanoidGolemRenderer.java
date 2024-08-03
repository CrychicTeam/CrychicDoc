package dev.xkmc.modulargolems.content.entity.humanoid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemRenderer;
import dev.xkmc.modulargolems.content.entity.common.GolemBannerLayer;
import dev.xkmc.modulargolems.content.entity.humanoid.skin.ClientSkinDispatch;
import dev.xkmc.modulargolems.content.entity.humanoid.skin.SpecialRenderSkin;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

public class HumanoidGolemRenderer extends AbstractGolemRenderer<HumanoidGolemEntity, HumaniodGolemPartType, HumanoidGolemModel> {

    protected static void transform(PoseStack stack, ItemDisplayContext transform, @Nullable HumaniodGolemPartType part) {
        switch(transform) {
            case GUI:
            case FIRST_PERSON_LEFT_HAND:
            case FIRST_PERSON_RIGHT_HAND:
            default:
                break;
            case THIRD_PERSON_LEFT_HAND:
            case THIRD_PERSON_RIGHT_HAND:
                {
                    stack.translate(0.25, 0.4, 0.5);
                    float size = 0.625F;
                    stack.scale(size, size, size);
                    break;
                }
            case GROUND:
                {
                    stack.translate(0.25, 0.0, 0.5);
                    float size = 0.625F;
                    stack.scale(size, size, size);
                    break;
                }
            case NONE:
            case HEAD:
            case FIXED:
                {
                    stack.translate(0.5, 0.5, 0.5);
                    float size = 0.5F;
                    stack.scale(size, -size, size);
                    stack.translate(0.0, -0.5, 0.0);
                    return;
                }
        }
        stack.mulPose(Axis.ZP.rotationDegrees(135.0F));
        stack.mulPose(Axis.YP.rotationDegrees(-155.0F));
        if (part == null) {
            float size = 0.45F;
            stack.scale(size, size, size);
            stack.translate(0.0F, -2.0F, 0.0F);
        } else if (part == HumaniodGolemPartType.BODY) {
            float size = 0.65F;
            stack.scale(size, size, size);
            stack.translate(0.0, -1.2, 0.0);
        } else if (part == HumaniodGolemPartType.LEGS) {
            float size = 0.8F;
            stack.scale(size, size, size);
            stack.translate(0.0F, -2.0F, 0.0F);
        } else if (part == HumaniodGolemPartType.ARMS) {
            float size = 0.6F;
            stack.scale(size, size, size);
            stack.translate(0.0, -1.5, 0.0);
        }
    }

    public HumanoidGolemRenderer(EntityRendererProvider.Context ctx) {
        this(ctx, false);
    }

    public HumanoidGolemRenderer(EntityRendererProvider.Context ctx, boolean slim) {
        super(ctx, new HumanoidGolemModel(ctx.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F, HumaniodGolemPartType::values);
        this.m_115326_(new HumanoidArmorLayer<>(this, new HumanoidModel(ctx.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(ctx.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)), ctx.getModelManager()));
        this.m_115326_(new CustomHeadLayer<>(this, ctx.getModelSet(), 1.0F, 1.0F, 1.0F, ctx.getItemInHandRenderer()));
        this.m_115326_(new ElytraLayer<>(this, ctx.getModelSet()));
        this.m_115326_(new LayerWrapper<>(this, new ItemInHandLayer<>(this, ctx.getItemInHandRenderer())));
        this.m_115326_(new GolemBannerLayer<>(this, ctx.getItemInHandRenderer()));
    }

    public void render(HumanoidGolemEntity entity, float f1, float f2, PoseStack stack, MultiBufferSource source, int i) {
        Entity camera = Minecraft.getInstance().getCameraEntity();
        if (Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON || camera == null || camera.getVehicle() == null || entity.m_20202_() != camera.getVehicle()) {
            SpecialRenderSkin profile = ClientSkinDispatch.get(entity);
            if (profile != null) {
                profile.render(entity, f1, f2, stack, source, i);
            } else {
                this.renderImpl(entity, f1, f2, stack, source, i);
            }
        }
    }

    public void renderImpl(HumanoidGolemEntity entity, float f1, float f2, PoseStack stack, MultiBufferSource source, int i) {
        super.render(entity, f1, f2, stack, source, i);
    }
}