package io.redspace.ironsspellbooks.entity.spells.magic_missile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.fireball.FireballRenderer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class MagicMissileRenderer extends EntityRenderer<MagicMissileProjectile> {

    private static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/magic_missile/magic_missile.png");

    private static final ResourceLocation[] FIRE_TEXTURES = new ResourceLocation[] { IronsSpellbooks.id("textures/entity/magic_missile/fire_1.png"), IronsSpellbooks.id("textures/entity/magic_missile/fire_2.png"), IronsSpellbooks.id("textures/entity/magic_missile/fire_3.png"), IronsSpellbooks.id("textures/entity/magic_missile/fire_4.png") };

    private final ModelPart body;

    protected final ModelPart outline;

    public MagicMissileRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(FireballRenderer.MODEL_LAYER_LOCATION);
        this.body = modelpart.getChild("body");
        this.outline = modelpart.getChild("outline");
    }

    public void render(MagicMissileProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        Vec3 motion = entity.m_20184_();
        float xRot = -((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * 180.0F / (float) Math.PI) - 90.0F);
        float yRot = -((float) (Mth.atan2(motion.z, motion.x) * 180.0F / (float) Math.PI) + 90.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        poseStack.scale(0.35F, 0.35F, 0.45F);
        VertexConsumer consumer = bufferSource.getBuffer(this.renderType(this.getTextureLocation(entity)));
        this.body.render(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, 0.8F, 0.8F, 0.8F, 1.0F);
        poseStack.scale(0.8F, 0.8F, 0.8F);
        poseStack.translate(0.0F, 0.0F, 0.4F);
        consumer = bufferSource.getBuffer(this.renderType(this.getFireTextureLocation(entity)));
        this.outline.render(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, 0.8F, 0.8F, 0.8F, 1.0F);
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public RenderType renderType(ResourceLocation TEXTURE) {
        return RenderType.energySwirl(TEXTURE, 0.0F, 0.0F);
    }

    public ResourceLocation getTextureLocation(MagicMissileProjectile entity) {
        return TEXTURE;
    }

    public ResourceLocation getFireTextureLocation(Projectile entity) {
        int frame = entity.f_19797_ % FIRE_TEXTURES.length;
        return FIRE_TEXTURES[frame];
    }
}