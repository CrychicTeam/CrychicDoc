package net.mehvahdjukaar.amendments.client.model;

import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.block.MimicBlockTile;
import net.mehvahdjukaar.moonlight.api.client.model.CustomBakedModel;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class HangingPotBakedModel implements CustomBakedModel {

    private final BakedModel rope;

    private final BlockModelShaper blockModelShaper;

    public HangingPotBakedModel(BakedModel rope, ModelState state) {
        this.rope = rope;
        this.blockModelShaper = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();
    }

    @Override
    public List<BakedQuad> getBlockQuads(BlockState state, Direction side, RandomSource rand, RenderType renderType, ExtraModelData data) {
        List<BakedQuad> quads = new ArrayList();
        if (state != null) {
            try {
                BlockState mimic = data.get(MimicBlockTile.MIMIC_KEY);
                if (mimic != null) {
                    BakedModel model = this.blockModelShaper.getBlockModel(mimic);
                    quads.addAll(model.getQuads(mimic, side, rand));
                }
            } catch (Exception var10) {
            }
            try {
                quads.addAll(this.rope.getQuads(state, side, rand));
            } catch (Exception var9) {
            }
        }
        return quads;
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
    public TextureAtlasSprite getBlockParticle(ExtraModelData data) {
        BlockState mimic = data.get(MimicBlockTile.MIMIC_KEY);
        if (mimic != null && !mimic.m_60795_()) {
            BakedModel model = this.blockModelShaper.getBlockModel(mimic);
            try {
                return model.getParticleIcon();
            } catch (Exception var5) {
            }
        }
        return this.rope.getParticleIcon();
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