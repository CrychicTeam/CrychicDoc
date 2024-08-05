package net.mehvahdjukaar.amendments.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.amendments.common.block.WaterloggedLilyBlock;
import net.mehvahdjukaar.amendments.common.tile.WaterloggedLilyBlockTile;
import net.mehvahdjukaar.amendments.configs.ClientConfigs;
import net.mehvahdjukaar.moonlight.api.client.model.BakedQuadsTransformer;
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
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WaterloggedLilyModel implements CustomBakedModel {

    private final BlockModelShaper blockModelShaper = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();

    @Override
    public List<BakedQuad> getBlockQuads(BlockState state, Direction side, RandomSource rand, RenderType renderType, ExtraModelData data) {
        BlockState mimic = data.get(WaterloggedLilyBlockTile.MIMIC_KEY);
        List<BakedQuad> quads = new ArrayList();
        if (mimic != null && !mimic.m_60795_()) {
            BakedModel model = this.blockModelShaper.getBlockModel(mimic);
            List<BakedQuad> mimicQuads = model.getQuads(mimic, side, rand);
            PoseStack pose = new PoseStack();
            double v = 1.0 + (state.m_61143_(WaterloggedLilyBlock.EXTENDED) ? 0.0 : (Double) ClientConfigs.LILY_OFFSET.get());
            pose.translate(0.0F, (float) v, 0.0F);
            BakedQuadsTransformer transformer = BakedQuadsTransformer.create().applyingTransform(pose.last().pose());
            quads.addAll(transformer.transformAll(mimicQuads));
            return quads;
        } else {
            return quads;
        }
    }

    @Override
    public TextureAtlasSprite getBlockParticle(ExtraModelData data) {
        BlockState mimic = data.get(WaterloggedLilyBlockTile.MIMIC_KEY);
        if (mimic != null && !mimic.m_60795_()) {
            BakedModel model = this.blockModelShaper.getBlockModel(mimic);
            try {
                return model.getParticleIcon();
            } catch (Exception var5) {
            }
        }
        return this.blockModelShaper.getBlockModel(Blocks.LILY_PAD.defaultBlockState()).getParticleIcon();
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
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }
}