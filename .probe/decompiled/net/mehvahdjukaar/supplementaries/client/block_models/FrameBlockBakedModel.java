package net.mehvahdjukaar.supplementaries.client.block_models;

import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.block.MimicBlock;
import net.mehvahdjukaar.moonlight.api.client.model.CustomBakedModel;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FrameBlock;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class FrameBlockBakedModel implements CustomBakedModel {

    private final BakedModel overlay;

    private final BlockModelShaper blockModelShaper;

    public FrameBlockBakedModel(BakedModel overlay, ModelState state) {
        this.overlay = overlay;
        this.blockModelShaper = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();
    }

    @Override
    public List<BakedQuad> getBlockQuads(BlockState state, Direction side, RandomSource rand, RenderType renderType, ExtraModelData data) {
        List<BakedQuad> quads = new ArrayList();
        if (state != null) {
            try {
                BlockState mimic = data.get(ModBlockProperties.MIMIC);
                if (mimic == null || mimic.m_60795_()) {
                    BakedModel model = this.blockModelShaper.getBlockModel((BlockState) state.m_61124_(FrameBlock.HAS_BLOCK, false));
                    quads.addAll(model.getQuads(mimic, side, rand));
                    return quads;
                }
                if (!(mimic.m_60734_() instanceof MimicBlock)) {
                    BakedModel model = this.blockModelShaper.getBlockModel(mimic);
                    quads.addAll(model.getQuads(mimic, side, rand));
                }
            } catch (Exception var10) {
            }
            try {
                quads.addAll(this.overlay.getQuads(state, side, rand));
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
    public TextureAtlasSprite getBlockParticle(ExtraModelData extraModelData) {
        return (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ModTextures.TIMBER_CROSS_BRACE_TEXTURE);
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