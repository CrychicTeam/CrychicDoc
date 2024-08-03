package me.jellysquid.mods.sodium.client.model.quad.properties;

import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;

public class ModelQuadFlags {

    public static final int IS_PARTIAL = 1;

    public static final int IS_PARALLEL = 2;

    public static final int IS_ALIGNED = 4;

    public static boolean contains(int flags, int mask) {
        return (flags & mask) != 0;
    }

    public static int getQuadFlags(ModelQuadView quad, Direction face) {
        float minX = 32.0F;
        float minY = 32.0F;
        float minZ = 32.0F;
        float maxX = -32.0F;
        float maxY = -32.0F;
        float maxZ = -32.0F;
        int numVertices = 4;
        if (quad instanceof BakedQuad bakedQuad) {
            numVertices = Math.min(numVertices, bakedQuad.getVertices().length / 8);
        }
        float lX = Float.NaN;
        float lY = Float.NaN;
        float lZ = Float.NaN;
        boolean degenerate = false;
        for (int i = 0; i < numVertices; i++) {
            float x = quad.getX(i);
            float y = quad.getY(i);
            float z = quad.getZ(i);
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            minZ = Math.min(minZ, z);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
            maxZ = Math.max(maxZ, z);
            if (x == lX && y == lY && z == lZ) {
                degenerate = true;
            } else {
                lX = x;
                lY = y;
                lZ = z;
            }
        }
        boolean var10000;
        label153: {
            label125: if (!degenerate) {
                switch(face.getAxis()) {
                    case X:
                        if (minY >= 1.0E-4F || minZ >= 1.0E-4F || maxY <= 0.9999F || maxZ <= 0.9999F) {
                            break label125;
                        }
                        break;
                    case Y:
                        if (minX >= 1.0E-4F || minZ >= 1.0E-4F || maxX <= 0.9999F || maxZ <= 0.9999F) {
                            break label125;
                        }
                        break;
                    case Z:
                        if (minX >= 1.0E-4F || minY >= 1.0E-4F || maxX <= 0.9999F || maxY <= 0.9999F) {
                            break label125;
                        }
                        break;
                    default:
                        throw new IncompatibleClassChangeError();
                }
                var10000 = false;
                break label153;
            }
            var10000 = true;
        }
        boolean partial = var10000;
        boolean parallel;
        label93: {
            label92: {
                parallel = switch(face.getAxis()) {
                    case X ->
                        minX == maxX;
                    case Y ->
                        minY == maxY;
                    case Z ->
                        minZ == maxZ;
                };
                if (parallel) {
                    switch(face) {
                        case DOWN:
                            if (minY < 1.0E-4F) {
                                break label92;
                            }
                            break;
                        case UP:
                            if (maxY > 0.9999F) {
                                break label92;
                            }
                            break;
                        case NORTH:
                            if (minZ < 1.0E-4F) {
                                break label92;
                            }
                            break;
                        case SOUTH:
                            if (maxZ > 0.9999F) {
                                break label92;
                            }
                            break;
                        case WEST:
                            if (minX < 1.0E-4F) {
                                break label92;
                            }
                            break;
                        case EAST:
                            if (maxX > 0.9999F) {
                                break label92;
                            }
                            break;
                        default:
                            throw new IncompatibleClassChangeError();
                    }
                }
                var10000 = false;
                break label93;
            }
            var10000 = true;
        }
        boolean aligned = var10000;
        int flags = 0;
        if (partial) {
            flags |= 1;
        }
        if (parallel) {
            flags |= 2;
        }
        if (aligned) {
            flags |= 4;
        }
        return flags;
    }
}