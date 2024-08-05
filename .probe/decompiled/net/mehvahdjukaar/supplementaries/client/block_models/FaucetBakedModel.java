package net.mehvahdjukaar.supplementaries.client.block_models;

import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.client.model.BakedQuadBuilder;
import net.mehvahdjukaar.moonlight.api.client.model.CustomBakedModel;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
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

public class FaucetBakedModel implements CustomBakedModel {

    private static final boolean SINGLE_PASS = PlatHelper.getPlatform().isFabric();

    private final BakedModel goblet;

    private final BakedModel liquid;

    public FaucetBakedModel(BakedModel goblet, BakedModel liquid, ModelState rotation) {
        this.goblet = goblet;
        this.liquid = liquid;
    }

    @Override
    public List<BakedQuad> getBlockQuads(BlockState state, Direction side, RandomSource rand, RenderType renderType, ExtraModelData data) {
        List<BakedQuad> quads = new ArrayList();
        if (SINGLE_PASS || renderType == RenderType.translucent()) {
            SoftFluid fluid = data.get(ModBlockProperties.FLUID);
            if (fluid != null && !fluid.isEmptyFluid()) {
                List<BakedQuad> l = this.liquid.getQuads(state, side, rand);
                if (!l.isEmpty()) {
                    int color = ColorUtils.swapFormat(data.get(ModBlockProperties.FLUID_COLOR)) | 0xFF000000;
                    int col2 = color & 16777215 | 671088640;
                    TextureAtlasSprite sprite = ModMaterials.get(fluid.getFlowingTexture()).sprite();
                    BakedQuadBuilder b = BakedQuadBuilder.create(sprite);
                    for (BakedQuad q : l) {
                        q = VertexUtil.swapSprite(q, sprite);
                        VertexUtil.recolorVertices(q.getVertices(), i -> i != 1 && i != 2 ? color : col2);
                        b.fromVanilla(q);
                        b.setDirection(q.getDirection());
                        b.lightEmission(fluid.getLuminosity());
                        quads.add(b.build());
                    }
                }
            }
            if (!SINGLE_PASS) {
                return quads;
            }
        }
        quads.addAll(this.goblet.getQuads(state, side, rand));
        return quads;
    }

    @Override
    public TextureAtlasSprite getBlockParticle(ExtraModelData extraModelData) {
        return this.goblet.getParticleIcon();
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