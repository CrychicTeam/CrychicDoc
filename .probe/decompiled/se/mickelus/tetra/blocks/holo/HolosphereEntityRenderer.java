package se.mickelus.tetra.blocks.holo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import se.mickelus.mutil.util.RotationHelper;
import se.mickelus.tetra.util.Lherper;

public class HolosphereEntityRenderer implements BlockEntityRenderer<HolosphereBlockEntity> {

    public static final Material material = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("tetra", "block/holosphere_hud"));

    private final BlockEntityRenderDispatcher dispatcher;

    private final Font font;

    public HolosphereEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = context.getBlockEntityRenderDispatcher();
        this.font = context.getFont();
    }

    public void render(HolosphereBlockEntity entity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        long timestamp = entity.getScanModeTimestamp();
        Level level = entity.m_58904_();
        if (timestamp != 0L && (timestamp >= 0L || level.getGameTime() + timestamp <= 20L)) {
            BlockPos pos = entity.m_58899_();
            ChunkPos chunkPos = new ChunkPos(pos);
            matrixStack.pushPose();
            matrixStack.translate(0.5, 1.0, 0.5);
            matrixStack.popPose();
            int light = this.getLightColor(entity.m_58904_(), entity.m_58899_());
            double angle = RotationHelper.getHorizontalAngle(Minecraft.getInstance().getCameraEntity().getEyePosition(partialTicks), Vec3.atCenterOf(entity.m_58899_()));
            Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
            rotation.mul(Axis.YP.rotationDegrees((float) (angle / Math.PI * 180.0)));
            VertexConsumer vertexBuilder = material.buffer(buffer, RenderType::m_110473_);
            this.renderBackdrop(vertexBuilder, matrixStack, rotation, light, (float) level.getGameTime() + partialTicks, timestamp);
            entity.getScanResults().stream().filter(scan -> scan.timestamp() <= level.getGameTime()).forEach(scan -> {
                int x = scan.chunkX() - chunkPos.x;
                int z = scan.chunkZ() - chunkPos.z;
                long openTimestamp = timestamp + (long) (Math.abs(x) + Math.abs(z));
                this.renderMarker(vertexBuilder, matrixStack, material.sprite(), rotation, (float) level.getGameTime() + partialTicks, openTimestamp, light, 0.5F + (float) (x * 1) / 16.0F, 0.0F, 0.5F + (float) (z * 1) / 16.0F, scan);
            });
        }
    }

    public void renderBackdrop(VertexConsumer consumer, PoseStack poseStack, Quaternionf rotation, int light, float time, long openTimestamp) {
        float animFast = openTimestamp > 0L ? Lherper.easeOut(Mth.clampedMap(time - (float) openTimestamp, 0.0F, 5.0F, 0.0F, 1.0F)) : Lherper.easeOut(Lherper.easeOut(Mth.clampedMap(time + (float) openTimestamp - 2.0F, 0.0F, 5.0F, 1.0F, 0.0F)));
        float animSlow = openTimestamp > 0L ? Lherper.easeOut(Lherper.easeOut(Mth.clampedMap(time - (float) openTimestamp - 5.0F, 0.0F, 5.0F, 0.0F, 1.0F))) : Lherper.easeOut(Mth.clampedMap(time + (float) openTimestamp, 0.0F, 5.0F, 1.0F, 0.0F));
        float animSlow2 = openTimestamp > 0L ? Lherper.easeOut(Lherper.easeOut(Mth.clampedMap(time - (float) openTimestamp - 10.0F, 0.0F, 5.0F, 0.0F, 1.0F))) : Lherper.easeOut(Mth.clampedMap(time + (float) openTimestamp, 0.0F, 5.0F, 1.0F, 0.0F));
        if (animFast > 0.0F) {
            this.drawQuad(consumer, poseStack, rotation, material.sprite(), light, 1.0F, animFast * 16.0F, 6, 0, 0.5F, 0.96875F, 0.5F, 16777215, animFast, -0.0025F, 1.0F);
        }
        if (animSlow > 0.0F) {
            this.drawQuad(consumer, poseStack, rotation, material.sprite(), light, 1.0F, 1.0F, 5, 0, 0.5F, (6.5F - animSlow * 0.5F) / 16.0F, 0.5F, 16777215, animSlow, -0.0025F, 1.0F);
            this.drawQuad(consumer, poseStack, rotation, material.sprite(), light, 1.0F, 1.0F, 5, 0, 0.5F, (24.5F + animSlow * 0.5F) / 16.0F, 0.5F, 16777215, animSlow, -0.0025F, 1.0F);
        }
        if (animSlow2 > 0.0F) {
            float offset = (20.0F + animSlow2 * 1.0F) / 16.0F;
            Quaternionf up = Axis.YN.rotationDegrees(45.0F);
            up.mul(Axis.XN.rotationDegrees(90.0F));
            this.drawQuad(consumer, poseStack, up, material.sprite(), light, 3.0F, 3.0F, 2, 3, offset + 0.5F, (4.5F + animSlow2 * 1.5F) / 16.0F, 0.5F, 16777215, animSlow2, 0.0F, 1.0F);
            this.drawQuad(consumer, poseStack, up, material.sprite(), light, 3.0F, 3.0F, 2, 9, 0.5F - offset, (4.5F + animSlow2 * 1.5F) / 16.0F, 0.5F, 16777215, animSlow2, 0.0F, 1.0F);
            this.drawQuad(consumer, poseStack, up, material.sprite(), light, 3.0F, 3.0F, 2, 6, 0.5F, (4.5F + animSlow2 * 1.5F) / 16.0F, offset + 0.5F, 16777215, animSlow2, 0.0F, 1.0F);
            this.drawQuad(consumer, poseStack, up, material.sprite(), light, 3.0F, 3.0F, 2, 0, 0.5F, (4.5F + animSlow2 * 1.5F) / 16.0F, 0.5F - offset, 16777215, animSlow2, 0.0F, 1.0F);
        }
    }

    public void renderMarker(VertexConsumer consumer, PoseStack poseStack, TextureAtlasSprite sprite, Quaternionf rotation, float time, long openTimestamp, int light, float x, float y, float z, HolosphereBlockEntity.ScanResult scan) {
        float anim = this.calculateMarkerAnimation(time, openTimestamp, scan.timestamp());
        if (openTimestamp != 0L && anim != 0.0F) {
            double height = (double) scan.height();
            float ry = -0.1F + anim * (0.1F + (float) (0.005F * height));
            int color = scan.structures().isEmpty() ? Lherper.lerpColors(Mth.clampedMap(scan.temperature() / 3.0F, 0.0F, 1.0F, 0.0F, 1.0F), 12961023, 12779476, 16515017, 16763837) : 15882800;
            color = Lherper.lerpColors((float) Mth.clampedMap(height, 40.0, 140.0, 0.5, 1.0), 0, color);
            float opacity = 0.9F * anim;
            this.drawQuad(consumer, poseStack, rotation, sprite, light, 1.0F, 4.0F, 7, 0, x, ry - 0.01F, z, 0, opacity * 0.3F, -0.001F, 1.125F);
            this.drawQuad(consumer, poseStack, rotation, sprite, light, 1.0F, 4.0F, 7, 0, x, ry, z, color, opacity);
            if (!scan.structures().isEmpty()) {
                float cut = Mth.clamp(ry - 0.28125F, 0.0F, 2.0F) * 16.0F;
                float sh = 16.0F - cut;
                if (sh > 0.0F) {
                    this.drawQuad(consumer, poseStack, rotation, material.sprite(), light, 1.0F, sh, 6, 0, x, 1.0F + ((16.0F - sh) / 2.0F - 0.5F) / 16.0F, z, 15882800, anim, -0.002F, 1.0F);
                }
                if (cut < 18.0F) {
                    this.drawQuad(consumer, poseStack, rotation, material.sprite(), light, 1.0F, 1.0F, 5, 0, x, 1.5625F, z, 0, anim, -0.003F, 1.125F);
                    this.drawQuad(consumer, poseStack, rotation, material.sprite(), light, 1.0F, 1.0F, 5, 0, x, 1.5625F, z, 15882800, anim, -0.002F, 1.0F);
                }
            }
        }
    }

    private float calculateMarkerAnimation(float time, long openTimestamp, long scanTimestamp) {
        if (time - (float) scanTimestamp < 5.0F) {
            return Lherper.easeOut(Mth.clampedMap(time - (float) scanTimestamp, 0.0F, 5.0F, 0.0F, 1.0F));
        } else {
            return openTimestamp > 0L ? Lherper.easeOut(Mth.clampedMap(time - (float) openTimestamp, 0.0F, 5.0F, 0.0F, 1.0F)) : Lherper.easeOut(Lherper.easeOut(Mth.clampedMap(time + (float) openTimestamp - 20.0F, 0.0F, 5.0F, 1.0F, 0.0F)));
        }
    }

    private void drawQuad(VertexConsumer consumer, PoseStack poseStack, Quaternionf rotation, TextureAtlasSprite sprite, int light, float width, float height, int u, int v, float x, float y, float z, int color, float a) {
        this.drawQuad(consumer, poseStack, rotation, sprite, light, width, height, u, v, x, y, z, color, a, 0.0F, 1.0F);
    }

    private void drawQuad(VertexConsumer consumer, PoseStack poseStack, Quaternionf rotation, TextureAtlasSprite sprite, int light, float width, float height, int u, int v, float x, float y, float z, int color, float a, float zIndex, float scale) {
        float spriteWidth = (float) sprite.contents().width();
        float spriteHeight = (float) sprite.contents().height();
        float voxelSize = 1.0F / Math.max(spriteWidth, spriteHeight) * scale;
        float r = (float) FastColor.ARGB32.red(color) / 255.0F;
        float g = (float) FastColor.ARGB32.green(color) / 255.0F;
        float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
        PoseStack.Pose pose = poseStack.last();
        Matrix3f normal = pose.normal();
        Matrix4f matrix4f = pose.pose();
        Vector3f[] matrix = new Vector3f[] { new Vector3f(-0.5F * width, -0.5F * height, zIndex), new Vector3f(-0.5F * width, 0.5F * height, zIndex), new Vector3f(0.5F * width, 0.5F * height, zIndex), new Vector3f(0.5F * width, -0.5F * height, zIndex) };
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = matrix[i];
            rotation.transform(vector3f);
            vector3f.mul(voxelSize);
            vector3f.add(x, y, z);
        }
        consumer.vertex(matrix4f, matrix[0].x(), matrix[0].y(), matrix[0].z()).color(r, g, b, a).uv((float) u / spriteWidth, ((float) v + height) / spriteHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, matrix[1].x(), matrix[1].y(), matrix[1].z()).color(r, g, b, a).uv((float) u / spriteWidth, (float) v / spriteHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, matrix[2].x(), matrix[2].y(), matrix[2].z()).color(r, g, b, a).uv(((float) u + width) / spriteWidth, (float) v / spriteHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, matrix[3].x(), matrix[3].y(), matrix[3].z()).color(r, g, b, a).uv(((float) u + width) / spriteWidth, ((float) v + height) / spriteHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private int getLightColor(Level level, BlockPos pos) {
        return 15728880;
    }

    private void drawLabel(String label, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.scale(-0.0125F, -0.0125F, 0.0125F);
        Matrix4f matrix4f = matrixStack.last().pose();
        float x = (float) (-this.font.width(label)) / 2.0F;
        this.font.drawInBatch(label, x + 1.0F, 0.0F, 0, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight, false);
        this.font.drawInBatch(label, x - 1.0F, 0.0F, 0, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight, false);
        this.font.drawInBatch(label, x, -1.0F, 0, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight, false);
        this.font.drawInBatch(label, x, 1.0F, 0, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight, false);
        matrixStack.translate(0.0F, 0.0F, -0.0125F);
        this.font.drawInBatch(label, x, 0.0F, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight, false);
    }
}