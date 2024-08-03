package net.minecraft.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix4f;

public class MapRenderer implements AutoCloseable {

    private static final ResourceLocation MAP_ICONS_LOCATION = new ResourceLocation("textures/map/map_icons.png");

    static final RenderType MAP_ICONS = RenderType.text(MAP_ICONS_LOCATION);

    private static final int WIDTH = 128;

    private static final int HEIGHT = 128;

    final TextureManager textureManager;

    private final Int2ObjectMap<MapRenderer.MapInstance> maps = new Int2ObjectOpenHashMap();

    public MapRenderer(TextureManager textureManager0) {
        this.textureManager = textureManager0;
    }

    public void update(int int0, MapItemSavedData mapItemSavedData1) {
        this.getOrCreateMapInstance(int0, mapItemSavedData1).forceUpload();
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, MapItemSavedData mapItemSavedData3, boolean boolean4, int int5) {
        this.getOrCreateMapInstance(int2, mapItemSavedData3).draw(poseStack0, multiBufferSource1, boolean4, int5);
    }

    private MapRenderer.MapInstance getOrCreateMapInstance(int int0, MapItemSavedData mapItemSavedData1) {
        return (MapRenderer.MapInstance) this.maps.compute(int0, (p_182563_, p_182564_) -> {
            if (p_182564_ == null) {
                return new MapRenderer.MapInstance(p_182563_, mapItemSavedData1);
            } else {
                p_182564_.replaceMapData(mapItemSavedData1);
                return p_182564_;
            }
        });
    }

    public void resetData() {
        ObjectIterator var1 = this.maps.values().iterator();
        while (var1.hasNext()) {
            MapRenderer.MapInstance $$0 = (MapRenderer.MapInstance) var1.next();
            $$0.close();
        }
        this.maps.clear();
    }

    public void close() {
        this.resetData();
    }

    class MapInstance implements AutoCloseable {

        private MapItemSavedData data;

        private final DynamicTexture texture;

        private final RenderType renderType;

        private boolean requiresUpload = true;

        MapInstance(int int0, MapItemSavedData mapItemSavedData1) {
            this.data = mapItemSavedData1;
            this.texture = new DynamicTexture(128, 128, true);
            ResourceLocation $$2 = MapRenderer.this.textureManager.register("map/" + int0, this.texture);
            this.renderType = RenderType.text($$2);
        }

        void replaceMapData(MapItemSavedData mapItemSavedData0) {
            boolean $$1 = this.data != mapItemSavedData0;
            this.data = mapItemSavedData0;
            this.requiresUpload |= $$1;
        }

        public void forceUpload() {
            this.requiresUpload = true;
        }

        private void updateTexture() {
            for (int $$0 = 0; $$0 < 128; $$0++) {
                for (int $$1 = 0; $$1 < 128; $$1++) {
                    int $$2 = $$1 + $$0 * 128;
                    this.texture.getPixels().setPixelRGBA($$1, $$0, MapColor.getColorFromPackedId(this.data.colors[$$2]));
                }
            }
            this.texture.upload();
        }

        void draw(PoseStack poseStack0, MultiBufferSource multiBufferSource1, boolean boolean2, int int3) {
            if (this.requiresUpload) {
                this.updateTexture();
                this.requiresUpload = false;
            }
            int $$4 = 0;
            int $$5 = 0;
            float $$6 = 0.0F;
            Matrix4f $$7 = poseStack0.last().pose();
            VertexConsumer $$8 = multiBufferSource1.getBuffer(this.renderType);
            $$8.vertex($$7, 0.0F, 128.0F, -0.01F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(int3).endVertex();
            $$8.vertex($$7, 128.0F, 128.0F, -0.01F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(int3).endVertex();
            $$8.vertex($$7, 128.0F, 0.0F, -0.01F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(int3).endVertex();
            $$8.vertex($$7, 0.0F, 0.0F, -0.01F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(int3).endVertex();
            int $$9 = 0;
            for (MapDecoration $$10 : this.data.getDecorations()) {
                if (!boolean2 || $$10.renderOnFrame()) {
                    poseStack0.pushPose();
                    poseStack0.translate(0.0F + (float) $$10.getX() / 2.0F + 64.0F, 0.0F + (float) $$10.getY() / 2.0F + 64.0F, -0.02F);
                    poseStack0.mulPose(Axis.ZP.rotationDegrees((float) ($$10.getRot() * 360) / 16.0F));
                    poseStack0.scale(4.0F, 4.0F, 3.0F);
                    poseStack0.translate(-0.125F, 0.125F, 0.0F);
                    byte $$11 = $$10.getImage();
                    float $$12 = (float) ($$11 % 16 + 0) / 16.0F;
                    float $$13 = (float) ($$11 / 16 + 0) / 16.0F;
                    float $$14 = (float) ($$11 % 16 + 1) / 16.0F;
                    float $$15 = (float) ($$11 / 16 + 1) / 16.0F;
                    Matrix4f $$16 = poseStack0.last().pose();
                    float $$17 = -0.001F;
                    VertexConsumer $$18 = multiBufferSource1.getBuffer(MapRenderer.MAP_ICONS);
                    $$18.vertex($$16, -1.0F, 1.0F, (float) $$9 * -0.001F).color(255, 255, 255, 255).uv($$12, $$13).uv2(int3).endVertex();
                    $$18.vertex($$16, 1.0F, 1.0F, (float) $$9 * -0.001F).color(255, 255, 255, 255).uv($$14, $$13).uv2(int3).endVertex();
                    $$18.vertex($$16, 1.0F, -1.0F, (float) $$9 * -0.001F).color(255, 255, 255, 255).uv($$14, $$15).uv2(int3).endVertex();
                    $$18.vertex($$16, -1.0F, -1.0F, (float) $$9 * -0.001F).color(255, 255, 255, 255).uv($$12, $$15).uv2(int3).endVertex();
                    poseStack0.popPose();
                    if ($$10.getName() != null) {
                        Font $$19 = Minecraft.getInstance().font;
                        Component $$20 = $$10.getName();
                        float $$21 = (float) $$19.width($$20);
                        float $$22 = Mth.clamp(25.0F / $$21, 0.0F, 6.0F / 9.0F);
                        poseStack0.pushPose();
                        poseStack0.translate(0.0F + (float) $$10.getX() / 2.0F + 64.0F - $$21 * $$22 / 2.0F, 0.0F + (float) $$10.getY() / 2.0F + 64.0F + 4.0F, -0.025F);
                        poseStack0.scale($$22, $$22, 1.0F);
                        poseStack0.translate(0.0F, 0.0F, -0.1F);
                        $$19.drawInBatch($$20, 0.0F, 0.0F, -1, false, poseStack0.last().pose(), multiBufferSource1, Font.DisplayMode.NORMAL, Integer.MIN_VALUE, int3);
                        poseStack0.popPose();
                    }
                    $$9++;
                }
            }
        }

        public void close() {
            this.texture.close();
        }
    }
}