package me.jellysquid.mods.sodium.client.model.light.smooth;

import net.minecraft.core.Direction;

enum AoNeighborInfo {

    DOWN(new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH }, 0.5F) {

        @Override
        public void calculateCornerWeights(float x, float y, float z, float[] out) {
            float v = 1.0F - x;
            out[0] = v * z;
            out[1] = v * (1.0F - z);
            out[2] = (1.0F - v) * (1.0F - z);
            out[3] = (1.0F - v) * z;
        }

        @Override
        public void mapCorners(int[] lm0, float[] ao0, int[] lm1, float[] ao1) {
            lm1[0] = lm0[0];
            lm1[1] = lm0[1];
            lm1[2] = lm0[2];
            lm1[3] = lm0[3];
            ao1[0] = ao0[0];
            ao1[1] = ao0[1];
            ao1[2] = ao0[2];
            ao1[3] = ao0[3];
        }

        @Override
        public float getDepth(float x, float y, float z) {
            return y;
        }
    }
    ,
    UP(new Direction[] { Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH }, 1.0F) {

        @Override
        public void calculateCornerWeights(float x, float y, float z, float[] out) {
            out[0] = x * z;
            out[1] = x * (1.0F - z);
            out[2] = (1.0F - x) * (1.0F - z);
            out[3] = (1.0F - x) * z;
        }

        @Override
        public void mapCorners(int[] lm0, float[] ao0, int[] lm1, float[] ao1) {
            lm1[2] = lm0[0];
            lm1[3] = lm0[1];
            lm1[0] = lm0[2];
            lm1[1] = lm0[3];
            ao1[2] = ao0[0];
            ao1[3] = ao0[1];
            ao1[0] = ao0[2];
            ao1[1] = ao0[3];
        }

        @Override
        public float getDepth(float x, float y, float z) {
            return 1.0F - y;
        }
    }
    ,
    NORTH(new Direction[] { Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST }, 0.8F) {

        @Override
        public void calculateCornerWeights(float x, float y, float z, float[] out) {
            float u = 1.0F - x;
            out[0] = y * u;
            out[1] = y * (1.0F - u);
            out[2] = (1.0F - y) * (1.0F - u);
            out[3] = (1.0F - y) * u;
        }

        @Override
        public void mapCorners(int[] lm0, float[] ao0, int[] lm1, float[] ao1) {
            lm1[3] = lm0[0];
            lm1[0] = lm0[1];
            lm1[1] = lm0[2];
            lm1[2] = lm0[3];
            ao1[3] = ao0[0];
            ao1[0] = ao0[1];
            ao1[1] = ao0[2];
            ao1[2] = ao0[3];
        }

        @Override
        public float getDepth(float x, float y, float z) {
            return z;
        }
    }
    ,
    SOUTH(new Direction[] { Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP }, 0.8F) {

        @Override
        public void calculateCornerWeights(float x, float y, float z, float[] out) {
            float v = 1.0F - x;
            out[0] = y * v;
            out[1] = (1.0F - y) * v;
            out[2] = (1.0F - y) * (1.0F - v);
            out[3] = y * (1.0F - v);
        }

        @Override
        public void mapCorners(int[] lm0, float[] ao0, int[] lm1, float[] ao1) {
            lm1[0] = lm0[0];
            lm1[1] = lm0[1];
            lm1[2] = lm0[2];
            lm1[3] = lm0[3];
            ao1[0] = ao0[0];
            ao1[1] = ao0[1];
            ao1[2] = ao0[2];
            ao1[3] = ao0[3];
        }

        @Override
        public float getDepth(float x, float y, float z) {
            return 1.0F - z;
        }
    }
    ,
    WEST(new Direction[] { Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH }, 0.6F) {

        @Override
        public void calculateCornerWeights(float x, float y, float z, float[] out) {
            out[0] = y * z;
            out[1] = y * (1.0F - z);
            out[2] = (1.0F - y) * (1.0F - z);
            out[3] = (1.0F - y) * z;
        }

        @Override
        public void mapCorners(int[] lm0, float[] ao0, int[] lm1, float[] ao1) {
            lm1[3] = lm0[0];
            lm1[0] = lm0[1];
            lm1[1] = lm0[2];
            lm1[2] = lm0[3];
            ao1[3] = ao0[0];
            ao1[0] = ao0[1];
            ao1[1] = ao0[2];
            ao1[2] = ao0[3];
        }

        @Override
        public float getDepth(float x, float y, float z) {
            return x;
        }
    }
    ,
    EAST(new Direction[] { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH }, 0.6F) {

        @Override
        public void calculateCornerWeights(float x, float y, float z, float[] out) {
            float v = 1.0F - y;
            out[0] = v * z;
            out[1] = v * (1.0F - z);
            out[2] = (1.0F - v) * (1.0F - z);
            out[3] = (1.0F - v) * z;
        }

        @Override
        public void mapCorners(int[] lm0, float[] ao0, int[] lm1, float[] ao1) {
            lm1[1] = lm0[0];
            lm1[2] = lm0[1];
            lm1[3] = lm0[2];
            lm1[0] = lm0[3];
            ao1[1] = ao0[0];
            ao1[2] = ao0[1];
            ao1[3] = ao0[2];
            ao1[0] = ao0[3];
        }

        @Override
        public float getDepth(float x, float y, float z) {
            return 1.0F - x;
        }
    }
    ;

    public final Direction[] faces;

    public final float strength;

    private static final AoNeighborInfo[] VALUES = values();

    private AoNeighborInfo(Direction[] directions, float strength) {
        this.faces = directions;
        this.strength = strength;
    }

    public abstract void calculateCornerWeights(float var1, float var2, float var3, float[] var4);

    public abstract void mapCorners(int[] var1, float[] var2, int[] var3, float[] var4);

    public abstract float getDepth(float var1, float var2, float var3);

    public static AoNeighborInfo get(Direction direction) {
        return VALUES[direction.get3DDataValue()];
    }
}