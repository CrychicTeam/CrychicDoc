package net.minecraft.client.resources.model;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class BuiltInModel implements BakedModel {

    private final ItemTransforms itemTransforms;

    private final ItemOverrides overrides;

    private final TextureAtlasSprite particleTexture;

    private final boolean usesBlockLight;

    public BuiltInModel(ItemTransforms itemTransforms0, ItemOverrides itemOverrides1, TextureAtlasSprite textureAtlasSprite2, boolean boolean3) {
        this.itemTransforms = itemTransforms0;
        this.overrides = itemOverrides1;
        this.particleTexture = textureAtlasSprite2;
        this.usesBlockLight = boolean3;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState0, @Nullable Direction direction1, RandomSource randomSource2) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return this.usesBlockLight;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.particleTexture;
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.itemTransforms;
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.overrides;
    }
}