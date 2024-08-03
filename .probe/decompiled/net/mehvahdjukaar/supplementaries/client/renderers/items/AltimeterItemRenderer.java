package net.mehvahdjukaar.supplementaries.client.renderers.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.mehvahdjukaar.moonlight.api.client.ItemStackRenderer;
import net.mehvahdjukaar.moonlight.api.client.model.BakedQuadBuilder;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AltimeterItemRenderer extends ItemStackRenderer {

    private static final Map<ResourceKey<Level>, Pair<TextureAtlasSprite, Int2ObjectMap<BakedModel>>> MODEL_CACHE = new HashMap();

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        ClientLevel level = Minecraft.getInstance().level;
        ResourceKey<Level> dimension = level == null ? Level.OVERWORLD : level.m_46472_();
        Pair<TextureAtlasSprite, Int2ObjectMap<BakedModel>> pair = (Pair<TextureAtlasSprite, Int2ObjectMap<BakedModel>>) MODEL_CACHE.getOrDefault(dimension, (Pair) MODEL_CACHE.get(Level.OVERWORLD));
        TextureAtlasSprite sprite = (TextureAtlasSprite) pair.getFirst();
        int textureH = sprite.contents().height();
        double stripDepth = calculateDepthIndex(level, textureH);
        int mult = (Integer) ClientConfigs.Items.DEPTH_METER_STEP_MULT.get();
        int index = (int) Math.round(stripDepth * (double) mult);
        BakedModel model = (BakedModel) ((Int2ObjectMap) pair.getSecond()).computeIfAbsent(index, i -> new AltimeterItemRenderer.AltimeterModel((float) index / (float) mult, textureH, sprite));
        Minecraft.getInstance().getItemRenderer().render(Items.DIAMOND.getDefaultInstance(), transformType, false, poseStack, buffer, packedLight, packedOverlay, model);
        poseStack.popPose();
    }

    private static double calculateDepthIndex(@Nullable ClientLevel level, int textureH) {
        int min = level == null ? -64 : level.m_141937_();
        int max = level == null ? 312 : level.m_151558_();
        LocalPlayer player = Minecraft.getInstance().player;
        double depth = player == null ? 64.0 : player.m_20182_().y;
        double normDepth = Mth.clamp((depth - (double) min) / (double) (max - min), 0.0, 1.0);
        return normDepth * (double) (textureH - 6);
    }

    public static void onReload() {
        MODEL_CACHE.clear();
        List<ResourceLocation> resourceLocations = new ArrayList((Collection) ClientConfigs.Items.DEPTH_METER_DIMENSIONS.get());
        resourceLocations.add(Level.OVERWORLD.location());
        for (ResourceLocation d : resourceLocations) {
            ResourceKey<Level> res = ResourceKey.create(Registries.DIMENSION, d);
            TextureAtlasSprite sprite = (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(Supplementaries.res("item/altimeter/" + d.toString().replace(":", "_")));
            if (sprite != null) {
                MODEL_CACHE.put(res, Pair.of(sprite, new Int2ObjectOpenHashMap()));
            }
        }
    }

    private static class AltimeterModel implements BakedModel {

        private final List<BakedQuad> quads = new ArrayList();

        private final ItemOverrides overrides;

        private final ItemTransforms transforms;

        AltimeterModel(float depth, int textureH, TextureAtlasSprite sprite) {
            int h = 5;
            float invDepth = (float) textureH - depth - (float) h;
            float shrink = sprite.uvShrinkRatio();
            BakedQuadBuilder builder = BakedQuadBuilder.create(sprite);
            builder.setAutoDirection();
            builder.setAutoBuild(this.quads::add);
            PoseStack ps = new PoseStack();
            float u0 = 0.0F;
            float u1 = 0.25F;
            float u3 = 0.25F;
            float u4 = 0.5F;
            for (int j = 0; j < 2; j++) {
                ps.translate(0.0F, 0.0F, 0.46875F);
                addScaledQuad(builder, ps, shrink, false, 0.375F, 0.375F, 0.625F, 0.6875F, u0, invDepth / (float) textureH, u1, (invDepth + (float) h) / (float) textureH);
                addScaledQuad(builder, ps, shrink, true, 0.375F, 0.6875F, 0.625F, 0.75F, u3, (invDepth - 1.0F) / (float) textureH, u4, invDepth / (float) textureH);
                ps.scale(-1.0F, 1.0F, -1.0F);
                ps.translate(-1.0F, 0.0F, -0.53125F);
                u0 = 0.25F;
                u1 = 0.0F;
                u3 = 0.5F;
                u4 = 0.25F;
            }
            BakedModel copy = ClientHelper.getModel(Minecraft.getInstance().getModelManager(), ClientRegistry.ALTIMETER_TEMPLATE);
            this.quads.addAll(copy.getQuads(null, null, RandomSource.create()));
            this.overrides = copy.getOverrides();
            this.transforms = copy.getTransforms();
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
            return direction == null ? this.quads : List.of();
        }

        @Override
        public boolean useAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean usesBlockLight() {
            return false;
        }

        @Override
        public boolean isCustomRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return null;
        }

        @Override
        public ItemTransforms getTransforms() {
            return this.transforms;
        }

        @Override
        public ItemOverrides getOverrides() {
            return this.overrides;
        }

        private static void addScaledQuad(BakedQuadBuilder builder, PoseStack ps, float shrink, boolean top, float x0, float y0, float x1, float y1, float u0, float v0, float u1, float v1) {
            float ix0 = shrink * (x0 - 0.5F) * 2.0F;
            float ix1 = shrink * (x1 - 0.5F) * 2.0F;
            float iy0 = top ? 0.0F : shrink * (y0 - 0.5F) * 2.0F;
            float iy1 = !top ? 0.0F : shrink * (y1 - 0.5F) * 2.0F;
            VertexUtil.addQuad(builder, ps, x0 + ix0, y0 + iy0, x1 + ix1, y1 + iy1, u0, v0, u1, v1, 255, 255, 255, 255, 0, 0);
        }
    }
}