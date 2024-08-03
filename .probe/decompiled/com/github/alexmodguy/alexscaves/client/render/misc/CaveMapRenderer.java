package com.github.alexmodguy.alexscaves.client.render.misc;

import com.github.alexmodguy.alexscaves.server.item.CaveMapItem;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import org.joml.Matrix4f;

public class CaveMapRenderer {

    private static final Map<ItemStack, CaveMapRenderer> CAVE_MAPS_ITEM_FRAME = new HashMap();

    private static final Map<ItemStack, CaveMapRenderer> CAVE_MAPS_HAND = new HashMap();

    public static final ResourceLocation MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");

    public static final RenderType CAVE_MAP_PLAYER_TEXTURE = RenderType.text(new ResourceLocation("alexscaves", "textures/misc/map/cave_map_player.png"));

    public static final RenderType CAVE_MAP_PLAYER_DIRECTION_TEXTURE = RenderType.text(new ResourceLocation("alexscaves", "textures/misc/map/cave_map_player_direction.png"));

    private final RenderType renderType;

    public BlockPos target;

    private final DynamicTexture texture;

    public List<CaveMapRenderer.BiomeLabel> labels = new ArrayList();

    public final int[] mapBiomes;

    private final boolean transparent;

    private final Random random = new Random(42L);

    public CaveMapRenderer(BlockPos target, int[] mapBiomes, long seed, boolean transparent, int index) {
        this.transparent = transparent;
        this.target = target;
        this.mapBiomes = mapBiomes;
        this.texture = new DynamicTexture(128, 128, true);
        ResourceLocation resourcelocation = Minecraft.getInstance().textureManager.register("cave_map/" + index + (transparent ? "" : "_frame"), this.texture);
        this.renderType = RenderType.text(resourcelocation);
        this.updateTexture();
        this.updateLabels();
    }

    public static CaveMapRenderer getMapFor(ItemStack item, boolean transparent) {
        if (transparent) {
            if (CAVE_MAPS_HAND.containsKey(item)) {
                return (CaveMapRenderer) CAVE_MAPS_HAND.get(item);
            } else {
                CaveMapRenderer mapRenderer = new CaveMapRenderer(CaveMapItem.getBiomeBlockPos(item), CaveMapItem.createBiomeArray(item), CaveMapItem.getSeed(item), transparent, CAVE_MAPS_HAND.size());
                CAVE_MAPS_HAND.put(item, mapRenderer);
                return mapRenderer;
            }
        } else if (CAVE_MAPS_ITEM_FRAME.containsKey(item)) {
            return (CaveMapRenderer) CAVE_MAPS_ITEM_FRAME.get(item);
        } else {
            CaveMapRenderer mapRenderer = new CaveMapRenderer(CaveMapItem.getBiomeBlockPos(item), CaveMapItem.createBiomeArray(item), CaveMapItem.getSeed(item), transparent, CAVE_MAPS_ITEM_FRAME.size());
            CAVE_MAPS_ITEM_FRAME.put(item, mapRenderer);
            return mapRenderer;
        }
    }

    private void updateTexture() {
        Registry<Biome> registry = (Registry<Biome>) Minecraft.getInstance().level.m_9598_().registry(Registries.BIOME).orElse(null);
        if (registry != null && this.mapBiomes.length >= 16384) {
            for (int i = 0; i < 128; i++) {
                for (int j = 0; j < 128; j++) {
                    int k = j + i * 128;
                    int biomeId = this.mapBiomes[k];
                    Holder<Biome> biome = registry.asHolderIdMap().byId(biomeId);
                    int biomeColor = biome == null ? DefaultMapBackgrounds.DEFAULT.getMapColor(i, j) : (biome.is(ACTagRegistry.CAVE_MAP_BORDER_ON) && this.isBorderBiome(registry, biome, i, j) ? DefaultMapBackgrounds.BORDER.getMapColor(i, j) : this.getBiomeColor(biome, j, i));
                    int r = FastColor.ABGR32.red(biomeColor);
                    int g = FastColor.ABGR32.green(biomeColor);
                    int b = FastColor.ABGR32.blue(biomeColor);
                    double edge = Math.sqrt((double) ((i - 64) * (i - 64) + (j - 64) * (j - 64))) / 128.0;
                    int alpha = this.transparent ? Math.max(255 - (int) (255.0 * edge * edge), 10) : 255;
                    this.texture.getPixels().setPixelRGBA(j, i, FastColor.ABGR32.color(alpha, b, g, r));
                }
            }
        }
        this.texture.upload();
    }

    private boolean isBorderBiome(Registry<Biome> registry, Holder<Biome> biome, int i, int j) {
        int k = j + i * 128;
        int left = k - 1;
        int right = k + 1;
        int up = j + (i - 1) * 128;
        int down = j + (i + 1) * 128;
        if (left >= 0 && left < this.mapBiomes.length && left % 128 != 127 && left % 128 != 0 && registry.asHolderIdMap().byId(this.mapBiomes[left]) != biome) {
            return true;
        } else if (right >= 0 && right < this.mapBiomes.length && right % 128 != 127 && right % 128 != 0 && registry.asHolderIdMap().byId(this.mapBiomes[right]) != biome) {
            return true;
        } else {
            return up >= 0 && up < this.mapBiomes.length && up % 128 != 127 && up % 128 != 0 && registry.asHolderIdMap().byId(this.mapBiomes[up]) != biome ? true : down >= 0 && down < this.mapBiomes.length && down % 128 != 127 && down % 128 != 0 && registry.asHolderIdMap().byId(this.mapBiomes[down]) != biome;
        }
    }

    private void updateLabels() {
        this.labels.clear();
        int extraBiomes = this.random.nextInt(3) + 3;
        Registry<Biome> registry = (Registry<Biome>) Minecraft.getInstance().level.m_9598_().registry(Registries.BIOME).orElse(null);
        if (registry != null && this.mapBiomes.length >= 16384) {
            Pair<Integer, Integer> targetBiomeLoc = this.centerBiomeCoordinates(64, 64);
            CaveMapRenderer.BiomeLabel centerLabel = this.buildLabelFrom(targetBiomeLoc, registry, ((Integer) targetBiomeLoc.getFirst() - 64) / 2, true);
            this.labels.add(centerLabel);
            for (int i = 0; i < extraBiomes; i++) {
                Vec3 randomOffsetFromCenterVec = new Vec3(0.0, 0.0, (double) (this.random.nextInt(20) + 40)).yRot((float) Math.toRadians((double) (360.0F / (float) extraBiomes * (float) i + this.random.nextFloat() * 40.0F)));
                int offsetX = Mth.clamp(centerLabel.x() + (int) randomOffsetFromCenterVec.x, 10, 118);
                int offsetY = Mth.clamp(centerLabel.y() + (int) randomOffsetFromCenterVec.z, 10, 118);
                Pair<Integer, Integer> extraBiomeLoc = this.centerBiomeCoordinates(offsetX, offsetY);
                CaveMapRenderer.BiomeLabel builtLabel = this.buildLabelFrom(extraBiomeLoc, registry, ((Integer) extraBiomeLoc.getFirst() - 64) / 2, false);
                if (!this.labels.stream().anyMatch(biomeLabel -> biomeLabel.conflictsWith(builtLabel))) {
                    this.labels.add(builtLabel);
                }
            }
        }
    }

    private CaveMapRenderer.BiomeLabel buildLabelFrom(Pair<Integer, Integer> targetBiomeLoc, Registry<Biome> registry, int rotation, boolean center) {
        int k = (Integer) targetBiomeLoc.getFirst() + (Integer) targetBiomeLoc.getSecond() * 128;
        int biomeId = this.mapBiomes[k];
        Holder<Biome> holder = registry.asHolderIdMap().byId(biomeId);
        ResourceKey<Biome> biomeResourceKey = holder == null ? Biomes.PLAINS : (ResourceKey) holder.unwrapKey().orElse(Biomes.PLAINS);
        return new CaveMapRenderer.BiomeLabel(biomeResourceKey, (Integer) targetBiomeLoc.getFirst(), (Integer) targetBiomeLoc.getSecond(), rotation);
    }

    private Pair<Integer, Integer> centerBiomeCoordinates(int xIn, int yIn) {
        int colorFor = this.mapBiomes[xIn + yIn * 128];
        int farLeftX = xIn;
        int farRightX = xIn;
        int farUpY = yIn;
        int farDownY = yIn;
        while (farLeftX > 0 && this.mapBiomes[farLeftX + yIn * 128] == colorFor) {
            farLeftX--;
        }
        while (farRightX < 128 && this.mapBiomes[farRightX + yIn * 128] == colorFor) {
            farRightX++;
        }
        while (farDownY > 0 && this.mapBiomes[xIn + farDownY * 128] == colorFor) {
            farDownY--;
        }
        while (farUpY < 128 && this.mapBiomes[xIn + farUpY * 128] == colorFor) {
            farUpY++;
        }
        return new Pair(xIn, yIn);
    }

    private int getBiomeColor(Holder<Biome> biome, int u, int v) {
        if (biome.is(ACBiomeRegistry.MAGNETIC_CAVES)) {
            return DefaultMapBackgrounds.MAGNETIC_CAVES.getMapColor(u, v);
        } else if (biome.is(ACBiomeRegistry.PRIMORDIAL_CAVES)) {
            return DefaultMapBackgrounds.PRIMORDIAL_CAVES.getMapColor(u, v);
        } else if (biome.is(ACBiomeRegistry.TOXIC_CAVES)) {
            return DefaultMapBackgrounds.TOXIC_CAVES.getMapColor(u, v);
        } else if (biome.is(ACBiomeRegistry.ABYSSAL_CHASM)) {
            return DefaultMapBackgrounds.ABYSSAL_CHASM.getMapColor(u, v);
        } else if (biome.is(ACBiomeRegistry.FORLORN_HOLLOWS)) {
            return DefaultMapBackgrounds.FORLORN_HOLLOWS.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_SNOWY) && biome.is(BiomeTags.IS_OCEAN)) {
            return DefaultMapBackgrounds.FROZEN_OCEAN.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_WATER)) {
            return DefaultMapBackgrounds.WATER.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_DESERT)) {
            return DefaultMapBackgrounds.DESERT.getMapColor(u, v);
        } else if (biome.is(BiomeTags.IS_JUNGLE)) {
            return DefaultMapBackgrounds.JUNGLE.getMapColor(u, v);
        } else if (biome.is(BiomeTags.IS_BADLANDS)) {
            return DefaultMapBackgrounds.BADLANDS.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_MOUNTAIN) && biome.is(Tags.Biomes.IS_SNOWY)) {
            return DefaultMapBackgrounds.SNOWY_MOUNTAIN.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_CONIFEROUS) && biome.is(Tags.Biomes.IS_SNOWY)) {
            return DefaultMapBackgrounds.SNOWY_TAIGA.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_MOUNTAIN)) {
            return DefaultMapBackgrounds.MOUNTAIN.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_SWAMP)) {
            return DefaultMapBackgrounds.SWAMP.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_SNOWY) && biome.is(Tags.Biomes.IS_RARE)) {
            return DefaultMapBackgrounds.ICE_SPIKES.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_SNOWY)) {
            return DefaultMapBackgrounds.SNOWY.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_CONIFEROUS)) {
            return DefaultMapBackgrounds.TAIGA.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_MUSHROOM)) {
            return DefaultMapBackgrounds.MUSHROOM.getMapColor(u, v);
        } else if (biome.is(BiomeTags.IS_FOREST) && biome.is(Tags.Biomes.IS_SPOOKY)) {
            return DefaultMapBackgrounds.ROOFED_FOREST.getMapColor(u, v);
        } else if (biome.is(BiomeTags.IS_FOREST)) {
            return DefaultMapBackgrounds.FOREST.getMapColor(u, v);
        } else if (biome.is(BiomeTags.IS_SAVANNA)) {
            return DefaultMapBackgrounds.SAVANNA.getMapColor(u, v);
        } else if (biome.is(Tags.Biomes.IS_PLAINS)) {
            return DefaultMapBackgrounds.PLAINS.getMapColor(u, v);
        } else if (biome.is(Biomes.STONY_SHORE)) {
            return DefaultMapBackgrounds.STONY_SHORE.getMapColor(u, v);
        } else if (biome.is(BiomeTags.IS_BEACH)) {
            return DefaultMapBackgrounds.BEACH.getMapColor(u, v);
        } else if (biome.is(Biomes.DRIPSTONE_CAVES)) {
            return DefaultMapBackgrounds.DRIPSTONE_CAVES.getMapColor(u, v);
        } else if (biome.is(Biomes.LUSH_CAVES)) {
            return DefaultMapBackgrounds.LUSH_CAVES.getMapColor(u, v);
        } else {
            return biome.is(Biomes.DEEP_DARK) ? DefaultMapBackgrounds.DEEP_DARK.getMapColor(u, v) : DefaultMapBackgrounds.DEFAULT.getMapColor(u, v);
        }
    }

    public void renderLabels(PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        for (CaveMapRenderer.BiomeLabel label : this.labels) {
            poseStack.pushPose();
            Font font = Minecraft.getInstance().font;
            Component component = Component.translatable(this.getBiomeString(label.biome.location().toString()));
            int textWidth = font.width(component.getVisualOrderText());
            float distFromCenter = (float) Math.sqrt((double) ((label.x() - 64) * (label.x() - 64) + (label.y() - 64) * (label.y() - 64)));
            float textScale = Mth.clamp((128.0F - distFromCenter) / 128.0F, 0.5F, 1.0F) * Math.min(100.0F / (float) textWidth, 1.0F);
            if ((float) label.x() - (float) textWidth * 0.5F < 0.0F) {
                poseStack.translate((float) textWidth * 0.25F, 0.0F, 0.0F);
            }
            poseStack.translate(0.0F + (float) label.x() - (float) textWidth * textScale / 2.0F, (float) label.y(), -0.025F);
            poseStack.scale(textScale, textScale, -1.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) label.rotation()));
            poseStack.pushPose();
            poseStack.scale(1.1F, 1.1F, 1.1F);
            float f1 = (float) (-textWidth / 4);
            int color1 = 8874331;
            int color2 = 15457474;
            font.drawInBatch8xOutline(component.getVisualOrderText(), f1, 0.0F, color1, color2, poseStack.last().pose(), multiBufferSource, light);
            poseStack.popPose();
            poseStack.popPose();
        }
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, ItemStack map, boolean fullFrame, int light) {
        if (fullFrame) {
            poseStack.translate(-64.0F, -64.0F, 55.0F);
        }
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(this.renderType);
        vertexconsumer.vertex(matrix4f, 0.0F, 128.0F, -0.01F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
        vertexconsumer.vertex(matrix4f, 128.0F, 128.0F, -0.01F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(light).endVertex();
        vertexconsumer.vertex(matrix4f, 128.0F, 0.0F, -0.01F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
        vertexconsumer.vertex(matrix4f, 0.0F, 0.0F, -0.01F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
        this.renderLabels(poseStack, multiBufferSource, light);
        if (!fullFrame) {
            poseStack.pushPose();
            double dimensionScale = Minecraft.getInstance().player.m_9236_().dimensionType().coordinateScale();
            double playerX = (Minecraft.getInstance().player.m_20185_() * dimensionScale - (double) this.target.m_123341_()) / 7.0;
            double playerZ = (Minecraft.getInstance().player.m_20189_() * dimensionScale - (double) this.target.m_123343_()) / 7.0;
            double renderPlayerX = Mth.clamp(playerX + 64.0, 0.0, 128.0);
            double renderPlayerZ = Mth.clamp(playerZ + 64.0, 0.0, 128.0);
            poseStack.translate(renderPlayerX, renderPlayerZ, -0.05F);
            poseStack.pushPose();
            poseStack.mulPose(Axis.ZP.rotationDegrees(Minecraft.getInstance().player.m_146908_() + 180.0F));
            this.renderDetail(multiBufferSource.getBuffer(CAVE_MAP_PLAYER_TEXTURE), poseStack, 3, light, 4.0F);
            poseStack.popPose();
            poseStack.popPose();
        }
    }

    private String getBiomeString(String id) {
        return "biome." + id.replace(":", ".");
    }

    private void renderDetail(VertexConsumer vertexconsumer1, PoseStack poseStack, int yOffset, int light, float scale) {
        Matrix4f matrix4f1 = poseStack.last().pose();
        vertexconsumer1.vertex(matrix4f1, -1.0F * scale, 1.0F * scale, (float) yOffset * -0.001F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
        vertexconsumer1.vertex(matrix4f1, 1.0F * scale, 1.0F * scale, (float) yOffset * -0.001F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(light).endVertex();
        vertexconsumer1.vertex(matrix4f1, 1.0F * scale, -1.0F * scale, (float) yOffset * -0.001F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
        vertexconsumer1.vertex(matrix4f1, -1.0F * scale, -1.0F * scale, (float) yOffset * -0.001F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
    }

    private static record BiomeLabel(ResourceKey<Biome> biome, int x, int y, int rotation) {

        public boolean conflictsWith(CaveMapRenderer.BiomeLabel other) {
            int xD = this.x - other.x;
            int yD = this.y - other.y;
            return Math.sqrt((double) (xD * xD + yD * yD)) <= 40.0 || this.biome.equals(other.biome);
        }
    }
}