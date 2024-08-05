package net.mehvahdjukaar.supplementaries.client.block_models;

import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.client.model.CustomBakedModel;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class JarBakedModel implements CustomBakedModel {

    private static final boolean SINGLE_PASS = true;

    private static final Vector3f LAST_KNOWN_DIMENSIONS = new Vector3f(0.5F, 0.75F, 0.0625F);

    private final BakedModel jar;

    private final float width;

    private final float height;

    private final float yOffset;

    public JarBakedModel(BakedModel goblet, float width, float height, float yOffset, ModelState rotation) {
        this.jar = goblet;
        this.width = width;
        this.height = height;
        this.yOffset = yOffset;
        LAST_KNOWN_DIMENSIONS.set(width, height, yOffset);
    }

    public static Vector3f getJarLiquidDimensions() {
        return LAST_KNOWN_DIMENSIONS;
    }

    @Override
    public List<BakedQuad> getBlockQuads(BlockState state, Direction side, RandomSource rand, RenderType renderType, ExtraModelData data) {
        List<BakedQuad> quads = new ArrayList();
        if (renderType == RenderType.cutout()) {
            quads.addAll(this.jar.getQuads(state, side, rand));
        }
        return quads;
    }

    @Override
    public TextureAtlasSprite getBlockParticle(ExtraModelData extraModelData) {
        return this.jar.getParticleIcon();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
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
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }
}