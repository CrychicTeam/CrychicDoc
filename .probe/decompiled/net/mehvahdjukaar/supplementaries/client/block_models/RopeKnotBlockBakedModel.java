package net.mehvahdjukaar.supplementaries.client.block_models;

import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.block.MimicBlock;
import net.mehvahdjukaar.moonlight.api.client.model.CustomBakedModel;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.AbstractRopeKnotBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RopeBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
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

public class RopeKnotBlockBakedModel implements CustomBakedModel {

    private final BakedModel knot;

    private final BlockModelShaper blockModelShaper;

    public RopeKnotBlockBakedModel(BakedModel knot, ModelState state) {
        this.knot = knot;
        this.blockModelShaper = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();
    }

    @Override
    public List<BakedQuad> getBlockQuads(BlockState state, Direction side, RandomSource rand, RenderType renderType, ExtraModelData data) {
        List<BakedQuad> quads = new ArrayList();
        try {
            BlockState mimic = data.get(ModBlockProperties.MIMIC);
            if (mimic != null && !(mimic.m_60734_() instanceof MimicBlock) && !mimic.m_60795_()) {
                BakedModel model = this.blockModelShaper.getBlockModel(mimic);
                quads.addAll(model.getQuads(mimic, side, rand));
            }
        } catch (Exception var10) {
        }
        try {
            if (state != null && state.m_60734_() instanceof AbstractRopeKnotBlock) {
                BlockState rope = (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((RopeBlock) ModRegistry.ROPE.get()).m_49966_().m_61124_(AbstractRopeKnotBlock.UP, (Boolean) state.m_61143_(AbstractRopeKnotBlock.UP))).m_61124_(AbstractRopeKnotBlock.DOWN, (Boolean) state.m_61143_(AbstractRopeKnotBlock.DOWN))).m_61124_(AbstractRopeKnotBlock.NORTH, (Boolean) state.m_61143_(AbstractRopeKnotBlock.NORTH))).m_61124_(AbstractRopeKnotBlock.SOUTH, (Boolean) state.m_61143_(AbstractRopeKnotBlock.SOUTH))).m_61124_(AbstractRopeKnotBlock.EAST, (Boolean) state.m_61143_(AbstractRopeKnotBlock.EAST))).m_61124_(AbstractRopeKnotBlock.WEST, (Boolean) state.m_61143_(AbstractRopeKnotBlock.WEST));
                BakedModel model = this.blockModelShaper.getBlockModel(rope);
                quads.addAll(model.getQuads(rope, side, rand));
                quads.addAll(this.knot.getQuads(state, side, rand));
            }
        } catch (Exception var9) {
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
        BlockState mimic = data.get(ModBlockProperties.MIMIC);
        if (mimic != null && !mimic.m_60795_()) {
            BakedModel model = this.blockModelShaper.getBlockModel(mimic);
            try {
                return model.getParticleIcon();
            } catch (Exception var5) {
            }
        }
        return this.knot.getParticleIcon();
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