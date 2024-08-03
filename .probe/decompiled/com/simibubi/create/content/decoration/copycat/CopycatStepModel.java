package com.simibubi.create.content.decoration.copycat;

import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;

public class CopycatStepModel extends CopycatModel {

    protected static final Vec3 VEC_Y_3 = new Vec3(0.0, 0.75, 0.0);

    protected static final Vec3 VEC_Y_2 = new Vec3(0.0, 0.5, 0.0);

    protected static final Vec3 VEC_Y_N2 = new Vec3(0.0, -0.5, 0.0);

    protected static final AABB CUBE_AABB = new AABB(BlockPos.ZERO);

    public CopycatStepModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material, ModelData wrappedData, RenderType renderType) {
        Direction facing = (Direction) state.m_61145_(CopycatStepBlock.FACING).orElse(Direction.SOUTH);
        boolean upperHalf = state.m_61145_(CopycatStepBlock.HALF).orElse(Half.BOTTOM) == Half.TOP;
        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData, renderType);
        int size = templateQuads.size();
        List<BakedQuad> quads = new ArrayList();
        Vec3 normal = Vec3.atLowerCornerOf(facing.getNormal());
        Vec3 normalScaled2 = normal.scale(0.5);
        Vec3 normalScaledN3 = normal.scale(-0.75);
        AABB bb = CUBE_AABB.contract(-normal.x * 0.75, 0.75, -normal.z * 0.75);
        for (boolean top : Iterate.trueAndFalse) {
            for (boolean front : Iterate.trueAndFalse) {
                AABB bb1 = bb;
                if (front) {
                    bb1 = bb.move(normalScaledN3);
                }
                if (top) {
                    bb1 = bb1.move(VEC_Y_3);
                }
                Vec3 offset = Vec3.ZERO;
                if (front) {
                    offset = offset.add(normalScaled2);
                }
                if (top != upperHalf) {
                    offset = offset.add(upperHalf ? VEC_Y_2 : VEC_Y_N2);
                }
                for (int i = 0; i < size; i++) {
                    BakedQuad quad = (BakedQuad) templateQuads.get(i);
                    Direction direction = quad.getDirection();
                    if ((!front || direction != facing) && (front || direction != facing.getOpposite()) && (top || direction != Direction.UP) && (!top || direction != Direction.DOWN)) {
                        quads.add(BakedQuadHelper.cloneWithCustomGeometry(quad, BakedModelHelper.cropAndMove(quad.getVertices(), quad.getSprite(), bb1, offset)));
                    }
                }
            }
        }
        return quads;
    }
}