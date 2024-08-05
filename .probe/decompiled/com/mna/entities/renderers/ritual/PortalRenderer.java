package com.mna.entities.renderers.ritual;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.tools.RLoc;
import com.mna.entities.rituals.Portal;
import com.mna.factions.Factions;
import com.mna.tools.math.MathUtils;
import com.mna.tools.render.MARenderTypes;
import com.mna.tools.render.ModelUtils;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class PortalRenderer extends EntityRenderer<Portal> {

    public static final ResourceLocation model_mushroom = RLoc.create("fx/mushroom");

    public static final ResourceLocation model_pink_flower = RLoc.create("fx/flower_pink");

    public static final ResourceLocation model_yellow_flower = RLoc.create("fx/flower_yellow");

    public static final ResourceLocation model_purple_flower = RLoc.create("fx/flower_purple");

    public static final ResourceLocation model_council_wedge = RLoc.create("fx/wedge");

    public static final ResourceLocation model_undead_fissure = RLoc.create("fx/fissure");

    public static final ResourceLocation model_demon_portal = RLoc.create("fx/demon_portal");

    public static final ResourceLocation model_demon_portal_ring = RLoc.create("fx/demon_portal_ring");

    public static final ResourceLocation[] model_demon_portal_stones = new ResourceLocation[] { RLoc.create("fx/demon_portal_stone1"), RLoc.create("fx/demon_portal_stone2"), RLoc.create("fx/demon_portal_stone3"), RLoc.create("fx/demon_portal_stone4"), RLoc.create("fx/demon_portal_stone5"), RLoc.create("fx/demon_portal_stone6"), RLoc.create("fx/demon_portal_stone7") };

    public static final ResourceLocation[] model_demon_portal_runes = new ResourceLocation[] { RLoc.create("fx/demon_portal_runes1"), RLoc.create("fx/demon_portal_runes2"), RLoc.create("fx/demon_portal_runes3"), RLoc.create("fx/demon_portal_runes4"), RLoc.create("fx/demon_portal_runes5"), RLoc.create("fx/demon_portal_runes6"), RLoc.create("fx/demon_portal_runes7") };

    private static final int[] defaultColor = new int[] { 19, 184, 74 };

    public PortalRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(Portal entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        DyeColor dc = entityIn.getDyeColor();
        int[] portalDye = dc != null ? new int[] { (int) (dc.getTextureDiffuseColors()[0] * 255.0F), (int) (dc.getTextureDiffuseColors()[1] * 255.0F), (int) (dc.getTextureDiffuseColors()[2] * 255.0F) } : null;
        if (entityIn.isRTP()) {
            this.renderDefaultPortal(entityIn, matrixStackIn, bufferIn, packedLightIn, partialTicks, new int[] { 255, 255, 255 });
        } else {
            Pair<IFaction, Byte> renderData = entityIn.getRenderData();
            if (renderData == null) {
                this.renderDefaultPortal(entityIn, matrixStackIn, bufferIn, packedLightIn, partialTicks, portalDye != null ? portalDye : defaultColor);
            } else if (renderData.getFirst() == Factions.COUNCIL) {
                this.renderCouncilPortal(entityIn, matrixStackIn, bufferIn, packedLightIn, partialTicks, portalDye != null ? portalDye : Affinity.ARCANE.getSecondaryColor());
            } else if (renderData.getFirst() == Factions.UNDEAD) {
                this.renderUndeadPortal(entityIn, matrixStackIn, bufferIn, packedLightIn, partialTicks, portalDye != null ? portalDye : new int[] { 113, 221, 151 });
            } else if (renderData.getFirst() == Factions.DEMONS) {
                this.renderDemonPortal(entityIn, matrixStackIn, bufferIn, packedLightIn, partialTicks, portalDye != null ? portalDye : new int[] { 255, 0, 0 });
            } else if (renderData.getFirst() == Factions.FEY) {
                if ((Byte) renderData.getSecond() == 0) {
                    this.renderSummerFeyPortal(entityIn, matrixStackIn, bufferIn, packedLightIn, partialTicks, portalDye != null ? portalDye : new int[] { 238, 220, 165 });
                } else {
                    this.renderWinterFeyPortal(entityIn, matrixStackIn, bufferIn, packedLightIn, partialTicks, portalDye != null ? portalDye : Affinity.ICE.getColor());
                }
            } else {
                this.renderDefaultPortal(entityIn, matrixStackIn, bufferIn, packedLightIn, partialTicks, portalDye != null ? portalDye : defaultColor);
            }
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private float getSpawnPct(Portal portal, int spawnFXDuration, float partialTick) {
        return Math.min((float) portal.getAge() + partialTick, (float) spawnFXDuration) / (float) spawnFXDuration;
    }

    private void renderDefaultPortal(Portal portal, PoseStack pose, MultiBufferSource bufferSource, int packedLight, float partialTick, int[] color) {
        float scaleFactor = 3.0F * this.getSpawnPct(portal, 20, partialTick);
        float portalSpinDegrees = (float) (portal.getAge() * 3 % 360);
        float verticalOffset = 1.25F;
        VertexConsumer vertexBuilder = bufferSource.getBuffer(portal.isRTP() ? MARenderTypes.RTP_PORTAL_RENDER : MARenderTypes.PORTAL_RENDER);
        this.renderPortalTexture(pose, vertexBuilder, packedLight, color, 230, verticalOffset, scaleFactor, portalSpinDegrees, 0.0F, true);
        pose.translate(0.0F, 0.0F, 0.05F);
        this.renderPortalTexture(pose, vertexBuilder, packedLight, color, 230, verticalOffset, scaleFactor, -portalSpinDegrees, 0.0F, true);
    }

    private void renderWinterFeyPortal(Portal portal, PoseStack pose, MultiBufferSource bufferSource, int packedLight, float partialTick, int[] color) {
        float spawnPct = this.getSpawnPct(portal, 80, partialTick);
        VertexConsumer builder = bufferSource.getBuffer(RenderType.solid());
        int numMushrooms = (int) Math.min(spawnPct * 20.0F, 10.0F);
        for (int i = 0; i < numMushrooms; i++) {
            pose.pushPose();
            pose.mulPose(Axis.YP.rotationDegrees((float) (36 * i)));
            pose.translate(1.5F, 0.0F, 0.0F);
            pose.mulPose(Axis.YP.rotationDegrees((float) (15 * i)));
            if (i == numMushrooms - 1 && spawnPct < 0.55F) {
                float mushroomScale = spawnPct % 0.05F * 20.0F;
                pose.scale(mushroomScale, mushroomScale, mushroomScale);
            }
            ModelUtils.renderEntityModel(builder, portal.m_9236_(), model_mushroom, pose, packedLight, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 0.5F);
            pose.popPose();
        }
        if (!(spawnPct < 0.6F)) {
            float raySpawnPct = Math.min(spawnPct - 0.6F, 0.3F) / 0.3F;
            int rayAngleStep = 2;
            int numRays = 15;
            pose.pushPose();
            Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
            Quaternionf portalRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
            pose.mulPose(portalRotation);
            pose.translate(-1.0F, 2.0F, 0.0F);
            Random rnd = new Random(13L);
            for (int i = 0; i < numRays; i++) {
                int offset = rnd.nextInt(500);
                double alphaSin = Math.sin((double) (((float) portal.getAge() + partialTick) / 5.0F + (float) (offset * i)));
                int alpha = (int) ((25.0 + 20.0 * alphaSin) * (double) raySpawnPct);
                WorldRenderUtils.renderLightRay(0.0F, pose, bufferSource, color, Affinity.ICE.getSecondaryColor(), alpha, 10.0F, (float) (170 + rayAngleStep * i));
            }
            pose.popPose();
            if (!(spawnPct < 0.9F)) {
                float ringSpawnPct = (spawnPct - 0.9F) * 10.0F;
                pose.pushPose();
                int time = (int) (portal.m_9236_().getGameTime() % 24000L) / 600;
                int spin = Math.abs(20 - time);
                this.renderPortalTexture(pose, bufferSource.getBuffer(MARenderTypes.BRIGHT_LIGHT_RENDER), packedLight, color, (int) (230.0F * ringSpawnPct), 0.1F, 4.0F, (float) spin, 90.0F, false);
                pose.popPose();
            }
        }
    }

    private void renderSummerFeyPortal(Portal portal, PoseStack pose, MultiBufferSource bufferSource, int packedLight, float partialTick, int[] color) {
        float spawnPct = this.getSpawnPct(portal, 80, partialTick);
        VertexConsumer builder = bufferSource.getBuffer(RenderType.solid());
        ResourceLocation[] flowers = new ResourceLocation[] { model_pink_flower, model_yellow_flower, model_purple_flower };
        int numFlowers = (int) Math.min(spawnPct * 20.0F, 9.0F);
        for (int i = 0; i < numFlowers; i++) {
            pose.pushPose();
            pose.mulPose(Axis.YP.rotationDegrees(-40.56F * (float) i));
            pose.translate(1.5F, 0.0F, 0.0F);
            pose.mulPose(Axis.YP.rotationDegrees((float) (15 * i)));
            if (i == numFlowers - 1 && spawnPct < 0.5F) {
                float flowerScale = Math.min(spawnPct % 0.05F * 20.0F, 1.0F);
                pose.scale(flowerScale, flowerScale, flowerScale);
            }
            ModelUtils.renderEntityModel(builder, portal.m_9236_(), flowers[i % 3], pose, packedLight, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 0.5F);
            pose.popPose();
        }
        if (!(spawnPct < 0.6F)) {
            float ringSpawnPct = Math.min((spawnPct - 0.6F) * 10.0F, 1.0F);
            int spin = 60;
            this.renderPortalTexture(pose, bufferSource.getBuffer(MARenderTypes.BRIGHT_LIGHT_RENDER), packedLight, color, (int) (230.0F * ringSpawnPct), 0.05F, 4.0F, (float) spin, 90.0F, false);
            if (!(spawnPct < 0.7F)) {
                float raySpawnPct = Math.min(spawnPct - 0.7F, 0.3F) / 0.3F;
                int rayAngleStep = 15;
                int numRays = 10;
                pose.pushPose();
                Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
                Quaternionf portalRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
                pose.mulPose(portalRotation);
                Random rnd = new Random(13L);
                for (int i = 0; i < numRays; i++) {
                    int offset = rnd.nextInt(500);
                    double alphaSin = Math.sin((double) (((float) portal.getAge() + partialTick) / 5.0F + (float) (offset * i)));
                    int alpha = (int) (25.0 + 20.0 * alphaSin);
                    WorldRenderUtils.renderLightRay(0.0F, pose, bufferSource, new int[] { 242, 169, 78 }, new int[] { 255, 77, 0 }, (int) ((float) alpha * raySpawnPct), 10.0F, (float) (-90 + rayAngleStep * i));
                }
                pose.popPose();
            }
        }
    }

    private void renderCouncilPortal(Portal portal, PoseStack pose, MultiBufferSource bufferSource, int packedLight, float partialTick, int[] color) {
        float spawnPct = this.getSpawnPct(portal, 80, partialTick);
        Minecraft mc = Minecraft.getInstance();
        pose.pushPose();
        Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
        Quaternionf portalRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
        pose.mulPose(portalRotation);
        pose.mulPose(Axis.YP.rotationDegrees(180.0F));
        pose.translate(0.0, 1.5, 0.0);
        if (spawnPct > 0.9F) {
            PoseStack.Pose matrixstack$entry = pose.last();
            Matrix4f renderMatrix = matrixstack$entry.pose();
            VertexConsumer vertexBuilder = bufferSource.getBuffer(RenderType.lightning());
            int hexagonAlpha = (int) ((double) Math.min(200 + (int) (32.0 * Math.sin((double) ((float) mc.level.m_46467_() + partialTick) * 0.05)), 230) * ((double) spawnPct - 0.9) * 10.0);
            vertexBuilder.vertex(renderMatrix, -0.5F, -1.0F, 0.0F).color(color[0], color[1], color[2], hexagonAlpha).endVertex();
            vertexBuilder.vertex(renderMatrix, 0.5F, -1.0F, 0.0F).color(color[0], color[1], color[2], hexagonAlpha).endVertex();
            vertexBuilder.vertex(renderMatrix, 1.0F, 0.0F, 0.0F).color(color[0], color[1], color[2], hexagonAlpha).endVertex();
            vertexBuilder.vertex(renderMatrix, -1.0F, 0.0F, 0.0F).color(color[0], color[1], color[2], hexagonAlpha).endVertex();
            vertexBuilder.vertex(renderMatrix, -1.0F, 0.0F, 0.0F).color(color[0], color[1], color[2], hexagonAlpha).endVertex();
            vertexBuilder.vertex(renderMatrix, 1.0F, 0.0F, 0.0F).color(color[0], color[1], color[2], hexagonAlpha).endVertex();
            vertexBuilder.vertex(renderMatrix, 0.5F, 1.0F, 0.0F).color(color[0], color[1], color[2], hexagonAlpha).endVertex();
            vertexBuilder.vertex(renderMatrix, -0.5F, 1.0F, 0.0F).color(color[0], color[1], color[2], hexagonAlpha).endVertex();
        }
        float jitterScale = 0.025F;
        float jitter = ((float) mc.level.m_46467_() + partialTick) * 0.01F;
        float radiantAnimation = ((float) mc.level.m_46467_() + partialTick) * 0.2F;
        float oneSixth = 0.16666667F;
        if (spawnPct > 0.4F) {
            float radiantSpawnFactor = spawnPct - 0.4F;
            float radiantSpawnPct = Math.min(radiantSpawnFactor / 0.2F, 1.0F);
            float[] curRadiantAlphas = new float[] { MathUtils.clamp01(radiantSpawnPct / oneSixth), MathUtils.clamp01(Math.max(radiantSpawnPct - oneSixth, 0.0F) / oneSixth), MathUtils.clamp01(Math.max(radiantSpawnPct - oneSixth * 2.0F, 0.0F) / oneSixth), MathUtils.clamp01(Math.max(radiantSpawnPct - oneSixth * 3.0F, 0.0F) / oneSixth), MathUtils.clamp01(Math.max(radiantSpawnPct - oneSixth * 4.0F, 0.0F) / oneSixth), MathUtils.clamp01(Math.max(radiantSpawnPct - oneSixth * 5.0F, 0.0F) / oneSixth) };
            pose.pushPose();
            pose.translate(-0.5, -1.0, 0.0);
            pose.pushPose();
            pose.translate(Math.sin((double) (jitter + 10.0F)) * (double) jitterScale, Math.cos((double) (jitter + 20.0F)) * (double) jitterScale, 0.0);
            WorldRenderUtils.renderRadiant(radiantAnimation, pose, bufferSource, color, color, (int) (128.0F * curRadiantAlphas[0]), 1.0F, false);
            pose.popPose();
            if (radiantSpawnPct > oneSixth) {
                pose.translate(1.0F, 0.0F, 0.0F);
                pose.pushPose();
                pose.translate(Math.sin((double) (jitter + 10.0F)) * (double) jitterScale, Math.cos((double) (jitter + 20.0F)) * (double) jitterScale, 0.0);
                WorldRenderUtils.renderRadiant(radiantAnimation + 10.0F, pose, bufferSource, color, color, (int) (128.0F * curRadiantAlphas[1]), 1.0F, false);
                pose.popPose();
            }
            if (radiantSpawnPct > oneSixth * 2.0F) {
                pose.translate(0.5, 1.0, 0.0);
                pose.pushPose();
                pose.translate(Math.sin((double) (jitter + 10.0F)) * (double) jitterScale, Math.cos((double) (jitter + 20.0F)) * (double) jitterScale, 0.0);
                WorldRenderUtils.renderRadiant(radiantAnimation + 20.0F, pose, bufferSource, color, color, (int) (128.0F * curRadiantAlphas[2]), 1.0F, false);
                pose.popPose();
            }
            if (radiantSpawnPct > oneSixth * 3.0F) {
                pose.translate(-0.5, 1.0, 0.0);
                pose.pushPose();
                pose.translate(Math.sin((double) (jitter + 10.0F)) * (double) jitterScale, Math.cos((double) (jitter + 20.0F)) * (double) jitterScale, 0.0);
                WorldRenderUtils.renderRadiant(radiantAnimation + 30.0F, pose, bufferSource, color, color, (int) (128.0F * curRadiantAlphas[3]), 1.0F, false);
                pose.popPose();
            }
            if (radiantSpawnPct > oneSixth * 4.0F) {
                pose.translate(-1.0F, 0.0F, 0.0F);
                pose.pushPose();
                pose.translate(Math.sin((double) (jitter + 10.0F)) * (double) jitterScale, Math.cos((double) (jitter + 20.0F)) * (double) jitterScale, 0.0);
                WorldRenderUtils.renderRadiant(radiantAnimation + 40.0F, pose, bufferSource, color, color, (int) (128.0F * curRadiantAlphas[4]), 1.0F, false);
                pose.popPose();
            }
            if (radiantSpawnPct > oneSixth * 5.0F) {
                pose.translate(-0.5, -1.0, 0.0);
                pose.pushPose();
                pose.translate(Math.sin((double) (jitter + 10.0F)) * (double) jitterScale, Math.cos((double) (jitter + 20.0F)) * (double) jitterScale, 0.0);
                WorldRenderUtils.renderRadiant(radiantAnimation + 50.0F, pose, bufferSource, color, color, (int) (128.0F * curRadiantAlphas[5]), 1.0F, false);
                pose.popPose();
            }
            pose.popPose();
        }
        if (spawnPct > 0.62F) {
            float beamSpawnFactor = spawnPct - 0.62F;
            float beamSpawnPct = Math.min(beamSpawnFactor / 0.2F, 1.0F);
            float[] curBeamLengths = new float[] { MathUtils.clamp01(beamSpawnPct / oneSixth), MathUtils.clamp01(Math.max(beamSpawnPct - oneSixth, 0.0F) / oneSixth), MathUtils.clamp01(Math.max(beamSpawnPct - oneSixth * 2.0F, 0.0F) / oneSixth), MathUtils.clamp01(Math.max(beamSpawnPct - oneSixth * 3.0F, 0.0F) / oneSixth), MathUtils.clamp01(Math.max(beamSpawnPct - oneSixth * 4.0F, 0.0F) / oneSixth), MathUtils.clamp01(Math.max(beamSpawnPct - oneSixth * 5.0F, 0.0F) / oneSixth) };
            float beamWidth = 0.05F;
            pose.pushPose();
            pose.translate(-0.5, -1.0, 0.001);
            WorldRenderUtils.renderBeam(mc.level, partialTick, pose, bufferSource, packedLight, new Vec3(0.0, 0.0, 0.0), new Vec3(1.0, 0.0, 0.0), curBeamLengths[0], new int[] { 255, 255, 255 }, 176, beamWidth, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
            if (beamSpawnPct > oneSixth) {
                pose.translate(1.0F, 0.0F, 0.0F);
                WorldRenderUtils.renderBeam(mc.level, partialTick, pose, bufferSource, packedLight, new Vec3(0.0, 0.0, 0.0), new Vec3(0.5, 1.0, 0.0), curBeamLengths[1], new int[] { 255, 255, 255 }, 176, beamWidth, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
            }
            if (beamSpawnPct > oneSixth * 2.0F) {
                pose.translate(0.5, 1.0, 0.0);
                WorldRenderUtils.renderBeam(mc.level, partialTick, pose, bufferSource, packedLight, new Vec3(0.0, 0.0, 0.0), new Vec3(-0.5, 1.0, 0.0), curBeamLengths[2], new int[] { 255, 255, 255 }, 176, beamWidth, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
            }
            if (beamSpawnPct > oneSixth * 3.0F) {
                pose.translate(-0.5, 1.0, 0.0);
                WorldRenderUtils.renderBeam(mc.level, partialTick, pose, bufferSource, packedLight, new Vec3(0.0, 0.0, 0.0), new Vec3(-1.0, 0.0, 0.0), curBeamLengths[3], new int[] { 255, 255, 255 }, 176, beamWidth, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
            }
            if (beamSpawnPct > oneSixth * 4.0F) {
                pose.translate(-1.0F, 0.0F, 0.0F);
                WorldRenderUtils.renderBeam(mc.level, partialTick, pose, bufferSource, packedLight, new Vec3(0.0, 0.0, 0.0), new Vec3(-0.5, -1.0, 0.0), curBeamLengths[4], new int[] { 255, 255, 255 }, 176, beamWidth, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
            }
            if (beamSpawnPct > oneSixth * 5.0F) {
                pose.translate(-0.5, -1.0, 0.0);
                WorldRenderUtils.renderBeam(mc.level, partialTick, pose, bufferSource, packedLight, new Vec3(0.0, 0.0, 0.0), new Vec3(0.5, -1.0, 0.0), curBeamLengths[5], new int[] { 255, 255, 255 }, 176, beamWidth, MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
            }
            pose.popPose();
        }
        float wedgeSpawn = Math.min(spawnPct, 0.3F) / 0.4F + 0.25F;
        float wedgeInset = 0.15F;
        float wedgeScale = 0.75F;
        float wedgeTallFactor = 0.05F;
        VertexConsumer solid = bufferSource.getBuffer(RenderType.solid());
        pose.pushPose();
        pose.translate(-0.5 * (double) wedgeSpawn, (double) (-1.0F * wedgeSpawn), 0.0);
        pose.pushPose();
        pose.translate(wedgeInset - wedgeTallFactor, wedgeInset, 0.0F);
        pose.mulPose(Axis.ZP.rotationDegrees(60.0F));
        pose.translate(Math.sin((double) (jitter + 10.0F)) * (double) jitterScale, Math.cos((double) (jitter + 20.0F)) * (double) jitterScale, 0.0);
        pose.scale(wedgeScale, wedgeScale, wedgeScale);
        ModelUtils.renderEntityModel(solid, portal.m_9236_(), model_council_wedge, pose, packedLight, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 0.5F);
        pose.popPose();
        pose.translate(1.0F * wedgeSpawn, 0.0F, 0.0F);
        pose.pushPose();
        pose.translate(-wedgeInset + wedgeTallFactor, wedgeInset, 0.0F);
        pose.mulPose(Axis.ZP.rotationDegrees(120.0F));
        pose.translate(Math.sin((double) (jitter + 15.0F)) * (double) jitterScale, Math.cos((double) (jitter + 30.0F)) * (double) jitterScale, 0.0);
        pose.scale(wedgeScale, wedgeScale, wedgeScale);
        ModelUtils.renderEntityModel(solid, portal.m_9236_(), model_council_wedge, pose, packedLight, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 0.5F);
        pose.popPose();
        pose.translate(0.5 * (double) wedgeSpawn, (double) (1.0F * wedgeSpawn), 0.0);
        pose.pushPose();
        pose.translate(-wedgeInset, 0.0F, 0.0F);
        pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
        pose.translate(Math.sin((double) (jitter + 20.0F)) * (double) jitterScale, Math.cos((double) (jitter + 20.0F)) * (double) jitterScale, 0.0);
        pose.scale(wedgeScale, wedgeScale, wedgeScale);
        ModelUtils.renderEntityModel(solid, portal.m_9236_(), model_council_wedge, pose, packedLight, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 0.5F);
        pose.popPose();
        pose.translate(-2.0F * wedgeSpawn, 0.0F, 0.0F);
        pose.pushPose();
        pose.translate(wedgeInset, 0.0F, 0.0F);
        pose.translate(Math.sin((double) (jitter + 15.0F)) * (double) jitterScale, Math.cos((double) (jitter + 30.0F)) * (double) jitterScale, 0.0);
        pose.scale(wedgeScale, wedgeScale, wedgeScale);
        ModelUtils.renderEntityModel(solid, portal.m_9236_(), model_council_wedge, pose, packedLight, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 0.5F);
        pose.popPose();
        pose.translate(0.5 * (double) wedgeSpawn, (double) (1.0F * wedgeSpawn), 0.0);
        pose.pushPose();
        pose.translate(wedgeInset - wedgeTallFactor, -wedgeInset, 0.0F);
        pose.mulPose(Axis.ZP.rotationDegrees(-60.0F));
        pose.translate(Math.sin((double) (jitter + 10.0F)) * (double) jitterScale, Math.cos((double) (jitter + 20.0F)) * (double) jitterScale, 0.0);
        pose.scale(wedgeScale, wedgeScale, wedgeScale);
        ModelUtils.renderEntityModel(solid, portal.m_9236_(), model_council_wedge, pose, packedLight, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 0.5F);
        pose.popPose();
        pose.translate(1.0F * wedgeSpawn, 0.0F, 0.0F);
        pose.pushPose();
        pose.translate(-wedgeInset + wedgeTallFactor, -wedgeInset, 0.0F);
        pose.mulPose(Axis.ZP.rotationDegrees(-120.0F));
        pose.translate(Math.sin((double) (jitter + 25.0F)) * (double) jitterScale, Math.cos((double) (jitter + 50.0F)) * (double) jitterScale, 0.0);
        pose.scale(wedgeScale, wedgeScale, wedgeScale);
        ModelUtils.renderEntityModel(solid, portal.m_9236_(), model_council_wedge, pose, packedLight, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 0.5F);
        pose.popPose();
        pose.popPose();
        pose.popPose();
    }

    private void renderUndeadPortal(Portal portal, PoseStack pose, MultiBufferSource bufferSource, int packedLight, float partialTick, int[] color) {
        float spawnPct = this.getSpawnPct(portal, 60, partialTick);
        VertexConsumer builder = bufferSource.getBuffer(RenderType.solid());
        pose.pushPose();
        float translatePct = 1.0F - Math.min(spawnPct * 2.0F, 1.0F);
        float hJitterS = spawnPct > 0.5F ? 0.0F : (float) Math.sin((double) (portal.getAge() * 3)) * 0.025F;
        float hJitterC = spawnPct > 0.5F ? 0.0F : (float) Math.cos((double) (portal.getAge() * 3)) * 0.025F;
        pose.translate((double) hJitterS, 0.05 - 0.5 * (double) translatePct, (double) (-hJitterC));
        ModelUtils.renderEntityModel(builder, portal.m_9236_(), model_undead_fissure, pose, packedLight, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 0.5F);
        pose.popPose();
        if (spawnPct > 0.5F) {
            float[] rayScales = new float[] { MathUtils.clamp01((spawnPct - 0.5F) * 2.0F), MathUtils.clamp01((spawnPct - 0.6F) / 0.4F), MathUtils.clamp01((spawnPct - 0.7F) / 0.3F), MathUtils.clamp01((spawnPct - 0.8F) / 0.2F), MathUtils.clamp01((spawnPct - 0.9F) / 0.1F) };
            Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
            Quaternionf portalRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
            int rayAlpha = (int) (150.0 + 50.0 * Math.sin((double) (((float) portal.getAge() + partialTick) / 25.0F)));
            float beamSeparation = 0.5F;
            float beamWidth = 0.05F;
            float beamFlare = 0.5F;
            pose.pushPose();
            pose.mulPose(portalRotation);
            pose.translate(-beamSeparation, 0.0F, 0.0F);
            WorldRenderUtils.renderLightBeam(0.0F, pose, bufferSource, color, color, rayAlpha, 10.0F * rayScales[0], beamWidth, beamFlare, 15.0F);
            pose.translate(beamSeparation, 0.0F, 0.0F);
            WorldRenderUtils.renderLightBeam(0.0F, pose, bufferSource, color, color, rayAlpha, 10.0F * rayScales[1], beamWidth, beamFlare, 0.0F);
            pose.translate(beamSeparation, 0.0F, 0.0F);
            WorldRenderUtils.renderLightBeam(0.0F, pose, bufferSource, color, color, rayAlpha, 10.0F * rayScales[2], beamWidth, beamFlare, -15.0F);
            pose.translate(-beamSeparation / 2.0F, 0.0F, 0.0F);
            WorldRenderUtils.renderLightBeam(0.0F, pose, bufferSource, color, color, rayAlpha, 5.0F * rayScales[3], beamWidth, beamFlare, -15.0F);
            pose.translate(-beamSeparation, 0.0F, 0.0F);
            WorldRenderUtils.renderLightBeam(0.0F, pose, bufferSource, color, color, rayAlpha, 5.0F * rayScales[4], beamWidth, beamFlare, 15.0F);
            pose.popPose();
        }
    }

    private void renderDemonPortal(Portal portal, PoseStack pose, MultiBufferSource bufferSource, int packedLight, float partialTick, int[] color) {
        float yFloatA = (float) Math.sin((double) (((float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTick) / 40.0F)) * 0.05F;
        float yFloatB = (float) Math.sin((double) (((float) (ManaAndArtifice.instance.proxy.getGameTicks() + 40L) + partialTick) / 40.0F)) * 0.05F;
        float yFloatC = (float) Math.sin((double) (((float) (ManaAndArtifice.instance.proxy.getGameTicks() + 80L) + partialTick) / 40.0F)) * 0.05F;
        float[] offsets = new float[] { yFloatA, yFloatB, yFloatC };
        float[] colors = new float[] { (float) color[0] / 255.0F, (float) color[1] / 255.0F, (float) color[2] / 255.0F };
        VertexConsumer builder = bufferSource.getBuffer(RenderType.translucent());
        float spawnPct = this.getSpawnPct(portal, 40, partialTick);
        float oneSeventh = 0.14285715F;
        float[] ringOffsets = new float[] { -1.0F, -1.65F, -2.5F, -3.0F, -2.5F, -1.85F, -1.0F };
        float[] ringHOffsets = new float[] { 1.0F, 1.0F, 1.0F, 0.0F, -1.0F, -1.0F, -1.0F };
        float stoneSpawnPct = Math.min(spawnPct, 0.6F) / 0.6F;
        float[] stoneSpawnPcts = new float[] { 1.0F - MathUtils.clamp01(stoneSpawnPct / oneSeventh), 1.0F - MathUtils.clamp01(Math.max(stoneSpawnPct - oneSeventh, 0.0F) / oneSeventh), 1.0F - MathUtils.clamp01(Math.max(stoneSpawnPct - oneSeventh * 2.0F, 0.0F) / oneSeventh), 1.0F - MathUtils.clamp01(Math.max(stoneSpawnPct - oneSeventh * 3.0F, 0.0F) / oneSeventh), 1.0F - MathUtils.clamp01(Math.max(stoneSpawnPct - oneSeventh * 4.0F, 0.0F) / oneSeventh), 1.0F - MathUtils.clamp01(Math.max(stoneSpawnPct - oneSeventh * 5.0F, 0.0F) / oneSeventh), 1.0F - MathUtils.clamp01(Math.max(stoneSpawnPct - oneSeventh * 6.0F, 0.0F) / oneSeventh) };
        Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
        Quaternionf portalRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
        pose.mulPose(portalRotation);
        pose.mulPose(Axis.YP.rotationDegrees(90.0F));
        pose.translate(0.0F, 1.0F, 0.0F);
        if (spawnPct > 0.6F) {
            float time = (float) portal.m_9236_().getGameTime() + partialTick;
            float ringScale = (spawnPct - 0.6F) / 0.4F;
            pose.pushPose();
            pose.translate(-0.3, 0.25, 0.0);
            pose.mulPose(Axis.XP.rotationDegrees(time));
            pose.scale(ringScale, ringScale, ringScale);
            pose.translate(0.0, -0.25, 0.0);
            pose.translate(0.0, -0.95, 0.0);
            ModelUtils.renderEntityModel(builder, portal.m_9236_(), model_demon_portal, pose, 15728880, OverlayTexture.NO_OVERLAY, colors, 1.0F);
            pose.popPose();
            pose.pushPose();
            pose.translate(-0.25, 0.25, 0.0);
            pose.mulPose(Axis.XP.rotationDegrees(-5.0F * time));
            pose.scale(ringScale, ringScale, ringScale);
            pose.translate(0.0, -0.25, 0.0);
            pose.translate(0.0, -0.95, 0.0);
            ModelUtils.renderEntityModel(builder, portal.m_9236_(), model_demon_portal_ring, pose, 15728880, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 1.0F);
            pose.popPose();
        }
        pose.pushPose();
        pose.translate(0.0, -1.1, 0.0);
        int count = 0;
        builder = bufferSource.getBuffer(RenderType.solid());
        for (ResourceLocation model : model_demon_portal_stones) {
            float stonePct = stoneSpawnPcts[count];
            pose.pushPose();
            pose.translate(0.0F, offsets[count % offsets.length] + ringOffsets[count] * stonePct, ringHOffsets[count] * stonePct);
            ModelUtils.renderEntityModel(builder, portal.m_9236_(), model, pose, packedLight, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 1.0F);
            pose.popPose();
            count++;
        }
        count = 0;
        for (ResourceLocation model : model_demon_portal_runes) {
            float stonePct = stoneSpawnPcts[count];
            pose.pushPose();
            pose.translate(0.0F, offsets[count % offsets.length] + ringOffsets[count] * stonePct, ringHOffsets[count] * stonePct);
            ModelUtils.renderEntityModel(builder, portal.m_9236_(), model, pose, 15728880, OverlayTexture.NO_OVERLAY, colors, 1.0F);
            pose.popPose();
            count++;
        }
        pose.popPose();
    }

    private void renderPortalTexture(PoseStack pose, VertexConsumer vertexBuilder, int packedLight, int[] color, int alpha, float verticalOffset, float scaleFactor, float spin, float tilt, boolean faceCamera) {
        pose.pushPose();
        if (faceCamera) {
            Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
            Quaternionf portalRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
            pose.mulPose(portalRotation);
        }
        pose.translate(0.0F, verticalOffset, 0.0F);
        pose.scale(scaleFactor, scaleFactor, scaleFactor);
        pose.mulPose(Axis.XP.rotationDegrees(tilt));
        pose.mulPose(Axis.YP.rotationDegrees(180.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(spin));
        pose.translate(0.0F, -0.25F, 0.0F);
        PoseStack.Pose matrixstack$entry = pose.last();
        Matrix4f renderMatrix = matrixstack$entry.pose();
        Matrix3f normalMatrix = matrixstack$entry.normal();
        float nrmV = (float) Math.cos((double) spin * Math.PI / 180.0);
        float nrmH = (float) Math.cos((double) (spin - 90.0F) * Math.PI / 180.0);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLight, 0.0F, 0.0F, 0.0F, 1.0F, nrmH, nrmV, color, alpha);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLight, 1.0F, 0.0F, 1.0F, 1.0F, nrmH, nrmV, color, alpha);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLight, 1.0F, 1.0F, 1.0F, 0.0F, nrmH, nrmV, color, alpha);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLight, 0.0F, 1.0F, 0.0F, 0.0F, nrmH, nrmV, color, alpha);
        pose.popPose();
    }

    private static void addVertex(VertexConsumer vertexBuilder_, Matrix4f renderMatrix, Matrix3f normalMatrix, int packedLight, float x, float y, float u, float v, float nrmH, float nrmV, int[] rgb, int a) {
        vertexBuilder_.vertex(renderMatrix, x - 0.5F, y - 0.25F, 0.0F).color(rgb[0], rgb[1], rgb[2], a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, nrmH, nrmV, nrmH).endVertex();
    }

    public ResourceLocation getTextureLocation(Portal entity) {
        return entity.isRTP() ? MARenderTypes.RTP_PORTAL_TEXTURE : MARenderTypes.PORTAL_TEXTURE;
    }
}