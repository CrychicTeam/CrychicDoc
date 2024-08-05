package io.redspace.ironsspellbooks.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.spells.blood.RayOfSiphoningSpell;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class SpellRenderingHelper {

    public static final ResourceLocation SOLID = IronsSpellbooks.id("textures/entity/ray/solid.png");

    public static final ResourceLocation BEACON = IronsSpellbooks.id("textures/entity/ray/beacon_beam.png");

    public static final ResourceLocation STRAIGHT_GLOW = IronsSpellbooks.id("textures/entity/ray/ribbon_glow.png");

    public static final ResourceLocation TWISTING_GLOW = IronsSpellbooks.id("textures/entity/ray/twisting_glow.png");

    public static void renderSpellHelper(SyncedSpellData spellData, LivingEntity castingMob, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {
        if (SpellRegistry.RAY_OF_SIPHONING_SPELL.get().getSpellId().equals(spellData.getCastingSpellId())) {
            renderRayOfSiphoning(castingMob, poseStack, bufferSource, partialTicks);
        }
    }

    public static void renderRayOfSiphoning(LivingEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {
        poseStack.pushPose();
        poseStack.translate(0.0F, entity.m_20192_() * 0.8F, 0.0F);
        PoseStack.Pose pose = poseStack.last();
        Vec3 start = Vec3.ZERO;
        Vec3 impact = Utils.raycastForEntity(entity.m_9236_(), entity, RayOfSiphoningSpell.getRange(0), true).getLocation();
        float distance = (float) entity.m_146892_().distanceTo(impact);
        float radius = 0.12F;
        int r = 178;
        int g = 0;
        int b = 0;
        int a = 255;
        float deltaTicks = (float) entity.f_19797_ + partialTicks;
        float deltaUV = -deltaTicks % 10.0F;
        float max = Mth.frac(deltaUV * 0.2F - (float) Mth.floor(deltaUV * 0.1F));
        float min = -1.0F + max;
        Vec3 dir = entity.m_20154_().normalize();
        float dx = (float) dir.x;
        float dz = (float) dir.z;
        float yRot = (float) Mth.atan2((double) dz, (double) dx) - 1.5707F;
        float dxz = Mth.sqrt(dx * dx + dz * dz);
        float dy = (float) dir.y;
        float xRot = (float) Mth.atan2((double) dy, (double) dxz);
        poseStack.mulPose(Axis.YP.rotation(-yRot));
        poseStack.mulPose(Axis.XP.rotation(-xRot));
        for (float j = 1.0F; j <= distance; j += 0.5F) {
            Vec3 wiggle = new Vec3((double) (Mth.sin(deltaTicks * 0.8F) * 0.02F), (double) (Mth.sin(deltaTicks * 0.8F + 100.0F) * 0.02F), (double) (Mth.cos(deltaTicks * 0.8F) * 0.02F));
            Vec3 end = new Vec3(0.0, 0.0, (double) Math.min(j, distance)).add(wiggle);
            VertexConsumer inner = bufferSource.getBuffer(RenderType.entityTranslucent(BEACON, true));
            drawHull(start, end, radius, radius, pose, inner, r, g, b, a, min, max);
            VertexConsumer outer = bufferSource.getBuffer(RenderType.entityTranslucent(TWISTING_GLOW));
            drawQuad(start, end, radius * 4.0F, 0.0F, pose, outer, r, g, b, a, min, max);
            drawQuad(start, end, 0.0F, radius * 4.0F, pose, outer, r, g, b, a, min, max);
            start = end;
        }
        poseStack.popPose();
    }

    private static void drawHull(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a, float uvMin, float uvMax) {
        drawQuad(from.subtract(0.0, (double) (height * 0.5F), 0.0), to.subtract(0.0, (double) (height * 0.5F), 0.0), width, 0.0F, pose, consumer, r, g, b, a, uvMin, uvMax);
        drawQuad(from.add(0.0, (double) (height * 0.5F), 0.0), to.add(0.0, (double) (height * 0.5F), 0.0), width, 0.0F, pose, consumer, r, g, b, a, uvMin, uvMax);
        drawQuad(from.subtract((double) (width * 0.5F), 0.0, 0.0), to.subtract((double) (width * 0.5F), 0.0, 0.0), 0.0F, height, pose, consumer, r, g, b, a, uvMin, uvMax);
        drawQuad(from.add((double) (width * 0.5F), 0.0, 0.0), to.add((double) (width * 0.5F), 0.0, 0.0), 0.0F, height, pose, consumer, r, g, b, a, uvMin, uvMax);
    }

    private static void drawQuad(Vec3 from, Vec3 to, float width, float height, PoseStack.Pose pose, VertexConsumer consumer, int r, int g, int b, int a, float uvMin, float uvMax) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        float halfWidth = width * 0.5F;
        float halfHeight = height * 0.5F;
        consumer.vertex(poseMatrix, (float) from.x - halfWidth, (float) from.y - halfHeight, (float) from.z).color(r, g, b, a).uv(0.0F, uvMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, (float) from.x + halfWidth, (float) from.y + halfHeight, (float) from.z).color(r, g, b, a).uv(1.0F, uvMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, (float) to.x + halfWidth, (float) to.y + halfHeight, (float) to.z).color(r, g, b, a).uv(1.0F, uvMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, (float) to.x - halfWidth, (float) to.y - halfHeight, (float) to.z).color(r, g, b, a).uv(0.0F, uvMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
    }
}