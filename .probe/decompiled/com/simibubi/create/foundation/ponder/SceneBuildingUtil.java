package com.simibubi.create.foundation.ponder;

import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

public class SceneBuildingUtil {

    public final SceneBuildingUtil.SelectionUtil select;

    public final SceneBuildingUtil.VectorUtil vector;

    public final SceneBuildingUtil.PositionUtil grid;

    private final BoundingBox sceneBounds;

    SceneBuildingUtil(BoundingBox sceneBounds) {
        this.sceneBounds = sceneBounds;
        this.select = new SceneBuildingUtil.SelectionUtil();
        this.vector = new SceneBuildingUtil.VectorUtil();
        this.grid = new SceneBuildingUtil.PositionUtil();
    }

    public class PositionUtil {

        public BlockPos at(int x, int y, int z) {
            return new BlockPos(x, y, z);
        }

        public BlockPos zero() {
            return this.at(0, 0, 0);
        }
    }

    public class SelectionUtil {

        public Selection everywhere() {
            return Selection.of(SceneBuildingUtil.this.sceneBounds);
        }

        public Selection position(int x, int y, int z) {
            return this.position(SceneBuildingUtil.this.grid.at(x, y, z));
        }

        public Selection position(BlockPos pos) {
            return this.cuboid(pos, BlockPos.ZERO);
        }

        public Selection fromTo(int x, int y, int z, int x2, int y2, int z2) {
            return this.fromTo(new BlockPos(x, y, z), new BlockPos(x2, y2, z2));
        }

        public Selection fromTo(BlockPos pos1, BlockPos pos2) {
            return this.cuboid(pos1, pos2.subtract(pos1));
        }

        public Selection column(int x, int z) {
            return this.cuboid(new BlockPos(x, 1, z), new Vec3i(0, SceneBuildingUtil.this.sceneBounds.getYSpan(), 0));
        }

        public Selection layer(int y) {
            return this.layers(y, 1);
        }

        public Selection layersFrom(int y) {
            return this.layers(y, SceneBuildingUtil.this.sceneBounds.getYSpan() - y);
        }

        public Selection layers(int y, int height) {
            return this.cuboid(new BlockPos(0, y, 0), new Vec3i(SceneBuildingUtil.this.sceneBounds.getXSpan() - 1, Math.min(SceneBuildingUtil.this.sceneBounds.getYSpan() - y, height) - 1, SceneBuildingUtil.this.sceneBounds.getZSpan() - 1));
        }

        public Selection cuboid(BlockPos origin, Vec3i size) {
            return Selection.of(BoundingBox.fromCorners(origin, origin.offset(size)));
        }
    }

    public class VectorUtil {

        public Vec3 centerOf(int x, int y, int z) {
            return this.centerOf(SceneBuildingUtil.this.grid.at(x, y, z));
        }

        public Vec3 centerOf(BlockPos pos) {
            return VecHelper.getCenterOf(pos);
        }

        public Vec3 topOf(int x, int y, int z) {
            return this.blockSurface(SceneBuildingUtil.this.grid.at(x, y, z), Direction.UP);
        }

        public Vec3 topOf(BlockPos pos) {
            return this.blockSurface(pos, Direction.UP);
        }

        public Vec3 blockSurface(BlockPos pos, Direction face) {
            return this.blockSurface(pos, face, 0.0F);
        }

        public Vec3 blockSurface(BlockPos pos, Direction face, float margin) {
            return this.centerOf(pos).add(Vec3.atLowerCornerOf(face.getNormal()).scale((double) (0.5F + margin)));
        }

        public Vec3 of(double x, double y, double z) {
            return new Vec3(x, y, z);
        }
    }
}