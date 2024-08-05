package dev.xkmc.modulargolems.content.entity.metalgolem;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.modulargolems.content.client.armor.GolemEquipmentModels;
import dev.xkmc.modulargolems.content.client.armor.GolemModelPath;
import dev.xkmc.modulargolems.content.item.equipments.GolemModelItem;
import dev.xkmc.modulargolems.content.item.equipments.MetalGolemBeaconItem;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class GolemEquipmentRenderer extends RenderLayer<MetalGolemEntity, MetalGolemModel> {

    public HashMap<ModelLayerLocation, MetalGolemModel> map = new HashMap();

    final ResourceLocation BEACON_LOCATION = new ResourceLocation("modulargolems", "textures/equipments/beacon.png");

    final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");

    public GolemEquipmentRenderer(RenderLayerParent<MetalGolemEntity, MetalGolemModel> r, EntityRendererProvider.Context e) {
        super(r);
        for (ModelLayerLocation l : GolemEquipmentModels.LIST) {
            this.map.put(l, new MetalGolemModel(e.bakeLayer(l)));
        }
    }

    public void render(@NotNull PoseStack pose, MultiBufferSource source, int i, @NotNull MetalGolemEntity entity, float f1, float f2, float f3, float f4, float f5, float f6) {
        for (EquipmentSlot e : EquipmentSlot.values()) {
            ItemStack stack = entity.m_6844_(e);
            Item color = stack.getItem();
            if (!(color instanceof GolemModelItem)) {
                if (stack.getItem() instanceof MetalGolemBeaconItem beacon) {
                    if (!entity.isAddedToWorld()) {
                        return;
                    }
                    float[] colorx = new float[] { 1.0F, 1.0F, 1.0F };
                    this.renderBeacon(pose, source, (float) i, entity.m_9236_().getGameTime(), entity.m_20206_());
                    this.renderBeam(pose, source, (float) i, 1.0F, entity.m_9236_().getGameTime(), entity.m_20206_(), colorx);
                } else {
                    this.renderArmWithItem(entity, stack, e, pose, source, i);
                }
            } else {
                GolemModelItem mgaitem = (GolemModelItem) color;
                GolemModelPath gmpath = GolemModelPath.get(mgaitem.getModelPath());
                for (List<String> ls : gmpath.paths()) {
                    MetalGolemModel model = (MetalGolemModel) this.map.get(gmpath.models());
                    model.copyFrom((MetalGolemModel) this.m_117386_());
                    ModelPart gemr = model.root();
                    pose.pushPose();
                    for (String s : ls) {
                        gemr.translateAndRotate(pose);
                        gemr = gemr.getChild(s);
                    }
                    gemr.render(pose, source.getBuffer(RenderType.armorCutoutNoCull(mgaitem.getModelTexture())), i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                    pose.popPose();
                }
            }
        }
    }

    protected void renderArmWithItem(MetalGolemEntity entity, ItemStack stack, EquipmentSlot slot, PoseStack pose, MultiBufferSource source, int light) {
        if (!stack.isEmpty()) {
            ItemDisplayContext ctx = null;
            if (slot == EquipmentSlot.MAINHAND) {
                ctx = ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
            } else if (slot == EquipmentSlot.OFFHAND) {
                ctx = ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
            }
            if (ctx != null) {
                pose.pushPose();
                ((MetalGolemModel) this.m_117386_()).transformToHand(slot, pose);
                boolean offhand = slot == EquipmentSlot.OFFHAND;
                pose.translate((float) (offhand ? 1 : -1) * 0.7F, 0.8F, -0.25F);
                pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
                pose.mulPose(Axis.YP.rotationDegrees(180.0F));
                Minecraft.getInstance().getItemRenderer().renderStatic(entity, stack, ctx, offhand, pose, source, entity.m_9236_(), light, OverlayTexture.NO_OVERLAY, entity.m_19879_() + slot.ordinal());
                pose.popPose();
            }
        }
    }

    protected void renderBeacon(PoseStack pose, MultiBufferSource source, float pTick, long gameTick, float height) {
        float width = 3.0F;
        pose.pushPose();
        pose.scale(1.0F, -1.0F, 1.0F);
        pose.translate(0.0, (double) (-height / 2.0F), 0.0);
        float accurateTick = (float) Math.floorMod(gameTick, 90) + pTick;
        pose.mulPose(Axis.YP.rotationDegrees(accurateTick - 45.0F));
        VertexConsumer buffer = source.getBuffer(RenderType.armorCutoutNoCull(this.BEACON_LOCATION));
        PoseStack.Pose posestack$pose = pose.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        addVertex(matrix4f, matrix3f, buffer, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0.0F, width, 0.0F, 0.0F);
        addVertex(matrix4f, matrix3f, buffer, 1.0F, 1.0F, 1.0F, 1.0F, 0, width, 0.0F, 0.0F, 1.0F);
        addVertex(matrix4f, matrix3f, buffer, 1.0F, 1.0F, 1.0F, 1.0F, 0, 0.0F, -width, 1.0F, 1.0F);
        addVertex(matrix4f, matrix3f, buffer, 1.0F, 1.0F, 1.0F, 1.0F, 0, -width, 0.0F, 1.0F, 0.0F);
        pose.popPose();
    }

    protected void renderBeam(PoseStack pose, MultiBufferSource source, float pTick, float scale, long gameTick, float height, float[] color) {
        float width1 = 0.2F;
        float width2 = 0.25F;
        int length = 1024;
        pose.pushPose();
        pose.scale(1.0F, -1.0F, 1.0F);
        pose.translate(0.0, (double) (height / 2.0F), 0.0);
        float accurateTick = (float) Math.floorMod(gameTick, 40) + pTick;
        float f2 = Mth.frac(accurateTick * 0.2F - (float) Mth.floor(accurateTick * 0.1F));
        float colorR = color[0];
        float colorG = color[1];
        float colorB = color[2];
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(accurateTick * 2.25F - 45.0F));
        float v1 = -1.0F + f2;
        float v2 = (float) length * scale * (0.5F / width1) + v1;
        renderPart(pose, source.getBuffer(RenderType.beaconBeam(this.BEAM_LOCATION, false)), colorR, colorG, colorB, 1.0F, 0, length, 0.0F, width1, width1, 0.0F, -width1, 0.0F, 0.0F, -width1, 0.0F, 1.0F, v2, v1);
        pose.popPose();
        v1 = -1.0F + f2;
        v2 = (float) length * scale + v1;
        renderPart(pose, source.getBuffer(RenderType.beaconBeam(this.BEAM_LOCATION, true)), colorR, colorG, colorB, 0.125F, 0, length, -width2, -width2, width2, -width2, -width2, width2, width2, width2, 0.0F, 1.0F, v2, v1);
        pose.popPose();
    }

    private static void renderPart(PoseStack pose, VertexConsumer buffer, float r, float g, float b, float a, int start, int end, float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float u1, float u2, float v1, float v2) {
        PoseStack.Pose posestack$pose = pose.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        renderQuad(matrix4f, matrix3f, buffer, r, g, b, a, start, end, float0, float1, float2, float3, u1, u2, v1, v2);
        renderQuad(matrix4f, matrix3f, buffer, r, g, b, a, start, end, float6, float7, float4, float5, u1, u2, v1, v2);
        renderQuad(matrix4f, matrix3f, buffer, r, g, b, a, start, end, float2, float3, float6, float7, u1, u2, v1, v2);
        renderQuad(matrix4f, matrix3f, buffer, r, g, b, a, start, end, float4, float5, float0, float1, u1, u2, v1, v2);
    }

    private static void renderQuad(Matrix4f pose, Matrix3f normal, VertexConsumer buffer, float r, float g, float b, float a, int y1, int y2, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        addVertex(pose, normal, buffer, r, g, b, a, y2, x1, z1, u2, v1);
        addVertex(pose, normal, buffer, r, g, b, a, y1, x1, z1, u2, v2);
        addVertex(pose, normal, buffer, r, g, b, a, y1, x2, z2, u1, v2);
        addVertex(pose, normal, buffer, r, g, b, a, y2, x2, z2, u1, v1);
    }

    private static void addVertex(Matrix4f pose, Matrix3f normal, VertexConsumer buffer, float r, float g, float b, float a, int y, float x, float z, float u, float v) {
        buffer.vertex(pose, x, (float) y, z).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
    }
}