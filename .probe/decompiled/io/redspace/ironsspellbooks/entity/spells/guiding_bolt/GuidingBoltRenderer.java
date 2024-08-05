package io.redspace.ironsspellbooks.entity.spells.guiding_bolt;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class GuidingBoltRenderer extends EntityRenderer<GuidingBoltProjectile> {

    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("irons_spellbooks", "guiding_bolt_model"), "main");

    private static final ResourceLocation BASE_TEXTURE = IronsSpellbooks.id("textures/entity/guiding_bolt/guiding_bolt.png");

    private static final ResourceLocation[] FIRE_TEXTURES = new ResourceLocation[] { IronsSpellbooks.id("textures/entity/guiding_bolt/fire_1.png"), IronsSpellbooks.id("textures/entity/guiding_bolt/fire_2.png"), IronsSpellbooks.id("textures/entity/guiding_bolt/fire_3.png"), IronsSpellbooks.id("textures/entity/guiding_bolt/fire_4.png"), IronsSpellbooks.id("textures/entity/guiding_bolt/fire_5.png"), IronsSpellbooks.id("textures/entity/guiding_bolt/fire_6.png"), IronsSpellbooks.id("textures/entity/guiding_bolt/fire_7.png") };

    protected final ModelPart body;

    protected final ModelPart outline;

    public GuidingBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(MODEL_LAYER_LOCATION);
        this.body = modelpart.getChild("body");
        this.outline = modelpart.getChild("outline");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -5.0F, 3.0F, 3.0F, 5.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("outline", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 16.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 48, 24);
    }

    public void render(GuidingBoltProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.translate(0.0, entity.m_20191_().getYsize() * 0.5, 0.0);
        Vec3 motion = entity.m_20184_();
        float xRot = -((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * 180.0F / (float) Math.PI) - 90.0F);
        float yRot = -((float) (Mth.atan2(motion.z, motion.x) * 180.0F / (float) Math.PI) + 90.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.energySwirl(this.getTextureLocation(entity), 0.0F, 0.0F));
        this.body.render(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY);
        consumer = bufferSource.getBuffer(RenderType.energySwirl(this.getFireTextureLocation(entity), 0.0F, 0.0F));
        poseStack.scale(0.4F, 0.4F, 0.4F);
        this.outline.render(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(GuidingBoltProjectile entity) {
        return BASE_TEXTURE;
    }

    public ResourceLocation getFireTextureLocation(Projectile entity) {
        int frame = entity.f_19797_ % FIRE_TEXTURES.length;
        return FIRE_TEXTURES[frame];
    }
}