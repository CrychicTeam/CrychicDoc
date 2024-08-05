package net.mehvahdjukaar.amendments.client.model;

import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.amendments.common.tile.CarpetedBlockTile;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.client.model.BakedQuadsTransformer;
import net.mehvahdjukaar.moonlight.api.client.model.CustomBakedModel;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CarpetedBlockModel implements CustomBakedModel {

    private final BakedModel carpet;

    private final BlockModelShaper blockModelShaper;

    protected static boolean SINGLE_PASS = PlatHelper.getPlatform().isFabric();

    public CarpetedBlockModel(BakedModel carpet, ModelState state) {
        this.carpet = carpet;
        this.blockModelShaper = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();
    }

    @Override
    public List<BakedQuad> getBlockQuads(BlockState state, Direction side, RandomSource rand, @Nullable RenderType renderType, ExtraModelData data) {
        List<BakedQuad> quads = new ArrayList();
        if (state == null) {
            return quads;
        } else {
            try {
                BlockState mimic = data.get(CarpetedBlockTile.MIMIC_KEY);
                if (mimic != null) {
                    RenderType originalRenderType = ItemBlockRenderTypes.getChunkRenderType(mimic);
                    if (originalRenderType == renderType || renderType == null || SINGLE_PASS) {
                        BakedModel model = this.blockModelShaper.getBlockModel(mimic);
                        quads.addAll(model.getQuads(mimic, side, rand));
                    }
                }
            } catch (Exception var13) {
            }
            if (renderType == RenderType.solid() || renderType == null || SINGLE_PASS) {
                try {
                    BlockState carpetBlock = data.get(CarpetedBlockTile.CARPET_KEY);
                    List<BakedQuad> carpetQuads = this.carpet.getQuads(state, side, rand);
                    if (!carpetQuads.isEmpty()) {
                        if (carpetBlock != null) {
                            boolean occl = state.m_60713_((Block) ModRegistry.CARPET_SLAB.get());
                            TextureAtlasSprite sprite = this.getCarpetSprite(carpetBlock);
                            BakedQuadsTransformer transformer = BakedQuadsTransformer.create().applyingSprite(sprite).applyingAmbientOcclusion(occl);
                            carpetQuads = transformer.transformAll(carpetQuads);
                        }
                        quads.addAll(carpetQuads);
                    }
                } catch (Exception var12) {
                    boolean var15 = true;
                }
            }
            return quads;
        }
    }

    private TextureAtlasSprite getCarpetSprite(BlockState carpetBlock) {
        return this.blockModelShaper.getBlockModel(carpetBlock).getParticleIcon();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getBlockParticle(ExtraModelData data) {
        BlockState mimic = data.get(CarpetedBlockTile.MIMIC_KEY);
        if (mimic != null && !mimic.m_60795_()) {
            BakedModel model = this.blockModelShaper.getBlockModel(mimic);
            try {
                return model.getParticleIcon();
            } catch (Exception var5) {
            }
        }
        return this.carpet.getParticleIcon();
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