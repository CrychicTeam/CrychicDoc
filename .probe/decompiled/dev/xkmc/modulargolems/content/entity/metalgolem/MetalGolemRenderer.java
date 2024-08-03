package dev.xkmc.modulargolems.content.entity.metalgolem;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.modulargolems.content.client.armor.GolemEquipmentModels;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemRenderer;
import dev.xkmc.modulargolems.content.entity.common.GolemBannerLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class MetalGolemRenderer extends AbstractGolemRenderer<MetalGolemEntity, MetalGolemPartType, MetalGolemModel> {

    protected static void transform(PoseStack stack, ItemDisplayContext transform, @Nullable MetalGolemPartType part) {
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
                    float size = 0.45F;
                    stack.scale(size, -size, size);
                    stack.translate(0.0, -0.15, 0.0);
                    return;
                }
        }
        stack.mulPose(Axis.ZP.rotationDegrees(135.0F));
        stack.mulPose(Axis.YP.rotationDegrees(-155.0F));
        if (part == null) {
            float size = 0.375F;
            stack.scale(size, size, size);
            stack.translate(0.0, -2.2, 0.0);
        } else if (part == MetalGolemPartType.BODY) {
            float size = 0.525F;
            stack.scale(size, size, size);
            stack.translate(0.0F, -1.0F, 0.0F);
        } else if (part == MetalGolemPartType.LEG) {
            float size = 0.6F;
            stack.scale(size, size, size);
            stack.translate(0.0, -2.2, 0.0);
        } else if (part == MetalGolemPartType.LEFT) {
            float size = 0.55F;
            stack.scale(size, size, size);
            stack.translate(-0.7, -1.7, 0.0);
        }
    }

    public MetalGolemRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new MetalGolemModel(ctx.bakeLayer(GolemEquipmentModels.METALGOLEM)), 0.7F, MetalGolemPartType::values);
        this.m_115326_(new MetalGolemCrackinessLayer(this));
        this.m_115326_(new GolemEquipmentRenderer(this, ctx));
        this.m_115326_(new GolemBannerLayer<>(this, ctx.getItemInHandRenderer()));
    }

    protected void setupRotations(MetalGolemEntity entity, PoseStack stack, float v1, float v2, float v3) {
        super.m_7523_(entity, stack, v1, v2, v3);
        if (!((double) entity.f_267362_.speed() < 0.01)) {
            float f = 13.0F;
            float f1 = entity.f_267362_.position() - entity.f_267362_.speed() * (1.0F - v3) + 6.0F;
            float f2 = (Math.abs(f1 % f - 6.5F) - 3.25F) / 3.25F;
            stack.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
        }
    }
}