package net.mehvahdjukaar.supplementaries.client.block_models;

import com.mojang.math.Transformation;
import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.client.model.BakedQuadBuilder;
import net.mehvahdjukaar.moonlight.api.client.model.CustomBakedModel;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.supplementaries.client.BlackboardManager;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BlackboardBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlackboardBlockTile;
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

public class BlackboardBakedModel implements CustomBakedModel {

    private final ModelState modelTransform;

    private final BakedModel back;

    public BlackboardBakedModel(BakedModel back, ModelState modelTransform) {
        this.back = back;
        this.modelTransform = modelTransform;
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
        return this.back.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public List<BakedQuad> getBlockQuads(BlockState state, Direction side, RandomSource rand, RenderType renderType, ExtraModelData data) {
        List<BakedQuad> quads = new ArrayList(this.back.getQuads(state, side, rand));
        if (data != ExtraModelData.EMPTY && state != null && side == null) {
            Direction dir = (Direction) state.m_61143_(BlackboardBlock.FACING);
            BlackboardManager.Key key = data.get(BlackboardBlockTile.BLACKBOARD_KEY);
            if (key != null) {
                BlackboardManager.Blackboard blackboard = BlackboardManager.getInstance(key);
                quads.addAll(blackboard.getOrCreateModel(dir, this::generateQuads));
            }
        }
        return quads;
    }

    private List<BakedQuad> generateQuads(BlackboardManager.Blackboard blackboard, Direction dir) {
        byte[][] pixels = blackboard.getPixels();
        boolean emissive = blackboard.isGlow();
        TextureAtlasSprite black = ModMaterials.BLACKBOARD_BLACK.sprite();
        TextureAtlasSprite white = ModMaterials.BLACKBOARD_WHITE.sprite();
        List<BakedQuad> quads = new ArrayList();
        Transformation rotation = this.modelTransform.getRotation();
        for (int x = 0; x < pixels.length; x++) {
            int length = 0;
            int startY = 0;
            byte prevColor = pixels[0][x];
            for (int y = 0; y <= pixels[x].length; y++) {
                Byte current = null;
                if (y < pixels[x].length) {
                    byte b = pixels[x][y];
                    if (prevColor == b) {
                        length++;
                        continue;
                    }
                    current = b;
                }
                int tint = 0xFF000000 | BlackboardBlock.colorFromByte(prevColor);
                TextureAtlasSprite sprite = prevColor == 0 ? black : white;
                quads.add(createPixelQuad((float) (15 - x) / 16.0F, (float) (16 - length - startY) / 16.0F, 0.0625F, (float) length / 16.0F, sprite, tint, rotation, prevColor != 0 && emissive));
                startY = y;
                if (current != null) {
                    prevColor = current;
                }
                length = 1;
            }
        }
        return quads;
    }

    public static BakedQuad createPixelQuad(float x, float y, float width, float height, TextureAtlasSprite sprite, int color, Transformation transform, boolean emissive) {
        float u0 = 1.0F - x;
        float v0 = 1.0F - y;
        float u1 = 1.0F - (x + width);
        float v1 = 1.0F - (y + height);
        BakedQuadBuilder builder = BakedQuadBuilder.create(sprite, transform);
        builder.setAutoDirection();
        putVertex(builder, x + width, y + height, u1, v1, color);
        putVertex(builder, x + width, y, u1, v0, color);
        putVertex(builder, x, y, u0, v0, color);
        putVertex(builder, x, y + height, u0, v1, color);
        if (emissive) {
            builder.lightEmission(15);
        }
        return builder.build();
    }

    private static void putVertex(BakedQuadBuilder builder, float x, float y, float u, float v, int color) {
        Vector3f posV = new Vector3f(x, y, 0.6875F);
        posV.set((float) Math.round(posV.x() * 16.0F) / 16.0F, (float) Math.round(posV.y() * 16.0F) / 16.0F, (float) Math.round(posV.z() * 16.0F) / 16.0F);
        builder.m_5483_((double) posV.x, (double) posV.y, (double) posV.z);
        builder.m_193479_(color);
        builder.m_7421_(u, v);
        builder.m_5601_(0.0F, 0.0F, -1.0F);
        builder.m_5752_();
    }
}