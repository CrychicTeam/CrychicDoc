package net.mehvahdjukaar.supplementaries.client.block_models;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.client.model.CustomBakedModel;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FlowerBoxBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FlowerBoxBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.FlowerPotHandler;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class FlowerBoxBakedModel implements CustomBakedModel {

    private final BakedModel box;

    private final BlockModelShaper blockModelShaper;

    private final ModelState rotation;

    public FlowerBoxBakedModel(BakedModel box, ModelState rotation) {
        this.box = box;
        this.blockModelShaper = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();
        this.rotation = rotation;
    }

    @Override
    public List<BakedQuad> getBlockQuads(BlockState state, Direction side, RandomSource rand, RenderType renderType, ExtraModelData data) {
        List<BakedQuad> quads = new ArrayList();
        try {
            quads.addAll(this.box.getQuads(state, side, rand));
        } catch (Exception var13) {
        }
        try {
            if (state != null) {
                BlockState[] flowers = new BlockState[] { data.get(FlowerBoxBlockTile.FLOWER_0), data.get(FlowerBoxBlockTile.FLOWER_1), data.get(FlowerBoxBlockTile.FLOWER_2) };
                PoseStack poseStack = new PoseStack();
                Matrix4f rot = this.rotation.getRotation().getMatrix();
                poseStack.mulPoseMatrix(rot);
                poseStack.translate(-0.3125, 0.0, 0.0);
                if ((Boolean) state.m_61143_(FlowerBoxBlock.FLOOR)) {
                    poseStack.translate(0.0, 0.0, -0.3125);
                }
                float scale = 0.625F;
                poseStack.scale(scale, scale, scale);
                poseStack.translate(0.5, 0.5, 1.0);
                for (int i = 0; i < 3; i++) {
                    BlockState flower = flowers[i];
                    if (flower != null && !flower.m_60795_()) {
                        poseStack.pushPose();
                        poseStack.translate(0.5 * (double) i, 0.0, 0.0);
                        if (flower.m_61138_(BlockStateProperties.FLOWER_AMOUNT)) {
                            poseStack.translate(scale / 4.0F, 0.0F, scale / 4.0F);
                        }
                        this.addBlockToModel(i, quads, flower, poseStack, side, rand);
                        if (flower.m_61138_(DoublePlantBlock.HALF)) {
                            poseStack.translate(0.0F, scale, 0.0F);
                            this.addBlockToModel(i, quads, (BlockState) flower.m_61124_(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), poseStack, side, rand);
                        }
                        poseStack.popPose();
                    }
                }
            }
        } catch (Exception var14) {
        }
        return quads;
    }

    private void addBlockToModel(int index, List<BakedQuad> quads, BlockState state, PoseStack poseStack, @Nullable Direction side, @NotNull RandomSource rand) {
        ResourceLocation res = FlowerPotHandler.getSpecialFlowerModel(state.m_60734_().asItem());
        BakedModel model;
        if (res != null) {
            if (state.m_61138_(DoublePlantBlock.HALF) && state.m_61143_(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                return;
            }
            model = ClientHelper.getModel(this.blockModelShaper.getModelManager(), res);
        } else {
            model = this.blockModelShaper.getBlockModel(state);
        }
        for (BakedQuad q : model.getQuads(state, side, rand)) {
            poseStack.pushPose();
            int[] v = Arrays.copyOf(q.getVertices(), q.getVertices().length);
            if (res == null) {
                poseStack.translate(-0.5F, -0.5F, -0.5F);
                poseStack.scale(0.6249F, 0.6249F, 0.6249F);
            } else {
                poseStack.translate(-0.5F, -0.3125F, -0.5F);
            }
            Matrix4f matrix = poseStack.last().pose();
            VertexUtil.transformVertices(v, matrix);
            poseStack.popPose();
            quads.add(new BakedQuad(v, q.getTintIndex() >= 0 ? index : q.getTintIndex(), Direction.rotate(matrix, q.getDirection()), q.getSprite(), q.isShade()));
        }
    }

    @Override
    public TextureAtlasSprite getBlockParticle(ExtraModelData extraModelData) {
        return this.box.getParticleIcon();
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