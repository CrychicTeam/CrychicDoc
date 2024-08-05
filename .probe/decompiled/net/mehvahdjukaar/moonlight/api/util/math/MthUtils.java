package net.mehvahdjukaar.moonlight.api.util.math;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import net.mehvahdjukaar.moonlight.api.util.math.colors.BaseColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MthUtils {

    public static final float PHI = (float) (1.0 + (Math.sqrt(5.0) - 1.0) / 2.0);

    public static float[] polarToCartesian(float a, float r) {
        float x = r * Mth.cos(a);
        float y = r * Mth.sin(a);
        return new float[] { x, y };
    }

    public static float signedAngleDiff(double to, double from) {
        float x1 = Mth.cos((float) to);
        float y1 = Mth.sin((float) to);
        float x2 = Mth.cos((float) from);
        float y2 = Mth.sin((float) from);
        return (float) Mth.atan2((double) (x1 * y1 - y1 * x2), (double) (x1 * x2 + y1 * y2));
    }

    public static Vec3 changeBasisN(Vec3 newBasisYVector, Vec3 rot) {
        Vec3 y = newBasisYVector.normalize();
        Vec3 x = new Vec3(y.y, y.z, y.x).normalize();
        Vec3 z = y.cross(x).normalize();
        return changeBasis(x, y, z, rot);
    }

    public static Vec3 changeBasis(Vec3 newX, Vec3 newY, Vec3 newZ, Vec3 rot) {
        return newX.scale(rot.x).add(newY.scale(rot.y)).add(newZ.scale(rot.z));
    }

    public static Vec3 getNormalFrom3DData(int direction) {
        return V3itoV3(Direction.from3DDataValue(direction).getNormal());
    }

    public static Vec3 V3itoV3(Vec3i v) {
        return new Vec3((double) v.getX(), (double) v.getY(), (double) v.getZ());
    }

    private static double isClockWise(UnaryOperator<Vec3> rot, Direction dir) {
        Vec3 v = V3itoV3(dir.getNormal());
        Vec3 v2 = (Vec3) rot.apply(v);
        return v2.dot(new Vec3(0.0, 1.0, 0.0));
    }

    public static Vec3 rotateVec3(Vec3 vec, Direction dir) {
        double cos = 1.0;
        double sin = 0.0;
        switch(dir) {
            case SOUTH:
                cos = -1.0;
                sin = 0.0;
                break;
            case WEST:
                cos = 0.0;
                sin = 1.0;
                break;
            case EAST:
                cos = 0.0;
                sin = -1.0;
                break;
            case UP:
                return new Vec3(vec.x, -vec.z, vec.y);
            case DOWN:
                return new Vec3(vec.x, vec.z, vec.y);
        }
        double dx = vec.x * cos + vec.z * sin;
        double dy = vec.y;
        double dz = vec.z * cos - vec.x * sin;
        return new Vec3(dx, dy, dz);
    }

    public static float averageAngles(Float... angles) {
        float x = 0.0F;
        float y = 0.0F;
        Float[] var3 = angles;
        int var4 = angles.length;
        for (int var5 = 0; var5 < var4; var5++) {
            float a = var3[var5];
            x += Mth.cos((float) ((double) a * Math.PI * 2.0));
            y += Mth.sin((float) ((double) a * Math.PI * 2.0));
        }
        return (float) (Mth.atan2((double) y, (double) x) / (Math.PI * 2));
    }

    public static double getPitch(Vec3 vec3) {
        return -Math.toDegrees(Math.asin(vec3.y));
    }

    public static double getYaw(Vec3 vec3) {
        return Math.toDegrees(Math.atan2(-vec3.x, vec3.z));
    }

    public static double getRoll(Vec3 vec3) {
        return Math.toDegrees(Math.atan2(vec3.y, vec3.x));
    }

    public static double wrapRad(double pValue) {
        double p = Math.PI * 2;
        double d0 = pValue % p;
        if (d0 >= Math.PI) {
            d0 -= p;
        }
        if (d0 < -Math.PI) {
            d0 += p;
        }
        return d0;
    }

    public static float wrapRad(float pValue) {
        float p = (float) (Math.PI * 2);
        float d0 = pValue % p;
        if ((double) d0 >= Math.PI) {
            d0 -= p;
        }
        if ((double) d0 < -Math.PI) {
            d0 += p;
        }
        return d0;
    }

    public static float nextWeighted(RandomSource rand, float max, float bias) {
        float r = rand.nextFloat();
        if (bias <= 0.0F) {
            if (bias == 0.0F) {
                return r * max;
            }
            bias = -bias / (bias - 1.0F);
        }
        return max * (1.0F - r) / (bias * max * r + 1.0F);
    }

    public static float nextWeighted(RandomSource rand, float max, float bias, float min) {
        return nextWeighted(rand, max - min, bias) + min;
    }

    public static float nextWeighted(RandomSource rand, float max) {
        return nextWeighted(rand, max, 1.0F);
    }

    public static <T extends BaseColor<T>> T lerpColorScale(List<T> palette, float phase) {
        if (phase >= 1.0F) {
            phase %= 1.0F;
        }
        int n = palette.size();
        float g = (float) n * phase;
        int ind = (int) Math.floor((double) g);
        float delta = g % 1.0F;
        T start = (T) palette.get(ind);
        T end = (T) palette.get((ind + 1) % n);
        return start.mixWith(end, delta);
    }

    public static boolean isWithinRectangle(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public static VoxelShape rotateVoxelShape(VoxelShape source, Direction direction) {
        if (direction == Direction.NORTH) {
            return source;
        } else {
            AtomicReference<VoxelShape> newShape = new AtomicReference(Shapes.empty());
            source.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                Vec3 min = new Vec3(minX - 0.5, minY - 0.5, minZ - 0.5);
                Vec3 max = new Vec3(maxX - 0.5, maxY - 0.5, maxZ - 0.5);
                Vec3 v1 = rotateVec3(min, direction);
                Vec3 v2 = rotateVec3(max, direction);
                VoxelShape s = Shapes.create(0.5 + Math.min(v1.x, v2.x), 0.5 + Math.min(v1.y, v2.y), 0.5 + Math.min(v1.z, v2.z), 0.5 + Math.max(v1.x, v2.x), 0.5 + Math.max(v1.y, v2.y), 0.5 + Math.max(v1.z, v2.z));
                newShape.set(Shapes.or((VoxelShape) newShape.get(), s));
            });
            return (VoxelShape) newShape.get();
        }
    }

    public static VoxelShape moveVoxelShape(VoxelShape source, Vec3 v) {
        AtomicReference<VoxelShape> newShape = new AtomicReference(Shapes.empty());
        source.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            VoxelShape s = Shapes.create(minX + v.x, minY + v.y, minZ + v.z, maxX + v.x, maxY + v.y, maxZ + v.z);
            newShape.set(Shapes.or((VoxelShape) newShape.get(), s));
        });
        return (VoxelShape) newShape.get();
    }

    public static double lambertW0(double x) {
        double maxError = 1.0E-6;
        if (x == -0.36787944117144233) {
            return -1.0;
        } else if (!(x >= -0.36787944117144233)) {
            throw new IllegalArgumentException("Not in valid range for lambertW function. x has to be greater than or equal to -1/e.");
        } else {
            double nLog = Math.log(x);
            for (double nLog0 = 1.0; Math.abs(nLog0 - nLog) > maxError; nLog = x * Math.exp(-nLog0) / (1.0 + nLog0)) {
                nLog0 = x * Math.exp(-nLog) / (1.0 + nLog);
            }
            return (double) (Math.round(1000000.0 * nLog) / 1000000L);
        }
    }

    public static double lambertW1(double x) {
        double maxError = 1.0E-6;
        if (x == -0.36787944117144233) {
            return -1.0;
        } else if (x < 0.0 && x > -0.36787944117144233) {
            double nLog = Math.log(-x);
            for (double nLog0 = 1.0; Math.abs(nLog0 - nLog) > maxError; nLog = (nLog0 * nLog0 + x / Math.exp(nLog0)) / (nLog0 + 1.0)) {
                nLog0 = (nLog * nLog + x / Math.exp(nLog)) / (nLog + 1.0);
            }
            return (double) (Math.round(1000000.0 * nLog) / 1000000L);
        } else if (x == 0.0) {
            return 0.0;
        } else {
            throw new IllegalArgumentException("Not in valid range for lambertW function. x has to be in [-1/e,0]");
        }
    }

    private static float exp01(float t, float base) {
        return (float) ((double) base * Math.pow((double) (1.0F / base + 1.0F), (double) t) - (double) base);
    }

    public static float normalizedExponent(float t, float curve) {
        if (curve == 0.0F) {
            return t;
        } else {
            float base;
            if (curve > 0.0F) {
                base = (float) (-Math.log((double) curve));
            } else {
                base = (float) (Math.log((double) (-curve)) - 1.0);
            }
            return exp01(t, base);
        }
    }

    public static BlockHitResult collideWithSweptAABB(Entity entity, Vec3 movement, double maxStep) {
        AABB aabb = entity.getBoundingBox();
        return collideWithSweptAABB(entity.position(), aabb, movement, entity.level(), maxStep);
    }

    public static BlockHitResult collideWithSweptAABB(Vec3 myPos, AABB myBox, Vec3 movement, Level level, double maxStep) {
        double len = movement.length();
        if (maxStep >= len) {
            return collideWithSweptAABB(myPos, myBox, movement, level);
        } else {
            double step = 0.0;
            while (step < len) {
                Vec3 stepMovement = movement.scale(step / len);
                BlockHitResult result = collideWithSweptAABB(myPos, myBox, stepMovement, level);
                if (result.getType() != HitResult.Type.MISS) {
                    return result;
                }
                step += maxStep;
                step = Math.min(step, len);
            }
            Vec3 missPos = myPos.add(movement);
            return BlockHitResult.miss(missPos, Direction.UP, BlockPos.containing(missPos));
        }
    }

    public static BlockHitResult collideWithSweptAABB(Vec3 myPos, AABB myBox, Vec3 movement, Level level) {
        AABB encompassing = myBox.expandTowards(movement);
        Set<BlockPos> positions = (Set<BlockPos>) BlockPos.betweenClosedStream(encompassing).map(BlockPos::m_7949_).collect(Collectors.toSet());
        MthUtils.CollisionResult earliestCollision = null;
        BlockPos hitPos = null;
        for (BlockPos pos : positions) {
            BlockState state = level.getBlockState(pos);
            if (!state.m_60795_()) {
                for (AABB box : state.m_60812_(level, pos).toAabbs()) {
                    box = box.move(pos);
                    MthUtils.CollisionResult result = sweptAABB(myBox, box, movement);
                    if (result != null && !(result.entryTime < 0.0)) {
                        if (earliestCollision == null) {
                            earliestCollision = result;
                            hitPos = pos;
                        } else if (result.entryTime == earliestCollision.entryTime) {
                            Vec3 collidedPos = myPos.add(movement.scale(result.entryTime));
                            if (pos.m_203193_(collidedPos) < hitPos.m_203193_(collidedPos)) {
                                earliestCollision = result;
                                hitPos = pos;
                            }
                        } else if (result.entryTime < earliestCollision.entryTime) {
                            earliestCollision = result;
                            hitPos = pos;
                        }
                    }
                }
            }
        }
        if (earliestCollision != null && earliestCollision.entryTime < 1.0) {
            movement = movement.scale(earliestCollision.entryTime);
            Vec3 finalPos = myPos.add(movement);
            return new BlockHitResult(finalPos, earliestCollision.direction, hitPos, false);
        } else {
            Vec3 missPos = myPos.add(movement);
            return BlockHitResult.miss(missPos, Direction.UP, BlockPos.containing(missPos));
        }
    }

    private static MthUtils.CollisionResult sweptAABB(AABB movingBox, AABB staticBox, Vec3 movement) {
        double entryX;
        double exitX;
        if (movement.x > 0.0) {
            entryX = (staticBox.minX - movingBox.maxX) / movement.x;
            exitX = (staticBox.maxX - movingBox.minX) / movement.x;
        } else if (movement.x < 0.0) {
            entryX = (staticBox.maxX - movingBox.minX) / movement.x;
            exitX = (staticBox.minX - movingBox.maxX) / movement.x;
        } else {
            entryX = Double.NEGATIVE_INFINITY;
            exitX = Double.POSITIVE_INFINITY;
        }
        double entryY;
        double exitY;
        if (movement.y > 0.0) {
            entryY = (staticBox.minY - movingBox.maxY) / movement.y;
            exitY = (staticBox.maxY - movingBox.minY) / movement.y;
        } else if (movement.y < 0.0) {
            entryY = (staticBox.maxY - movingBox.minY) / movement.y;
            exitY = (staticBox.minY - movingBox.maxY) / movement.y;
        } else {
            entryY = Double.NEGATIVE_INFINITY;
            exitY = Double.POSITIVE_INFINITY;
        }
        double entryZ;
        double exitZ;
        if (movement.z > 0.0) {
            entryZ = (staticBox.minZ - movingBox.maxZ) / movement.z;
            exitZ = (staticBox.maxZ - movingBox.minZ) / movement.z;
        } else if (movement.z < 0.0) {
            entryZ = (staticBox.maxZ - movingBox.minZ) / movement.z;
            exitZ = (staticBox.minZ - movingBox.maxZ) / movement.z;
        } else {
            entryZ = Double.NEGATIVE_INFINITY;
            exitZ = Double.POSITIVE_INFINITY;
        }
        double entryTime = Math.max(Math.max(entryX, entryY), entryZ);
        double exitTime = Math.min(Math.min(exitX, exitY), exitZ);
        if (!(entryTime > exitTime) && (!(entryX < 0.0) || !(entryY < 0.0) || !(entryZ < 0.0)) && !(entryX > 1.0) && !(entryY > 1.0) && !(entryZ > 1.0)) {
            Direction collisionDirection;
            if (entryX > entryY && entryX > entryZ) {
                if (movement.x > 0.0) {
                    collisionDirection = Direction.EAST;
                } else {
                    collisionDirection = Direction.WEST;
                }
            } else if (entryY > entryZ) {
                if (movement.y > 0.0) {
                    collisionDirection = Direction.UP;
                } else {
                    collisionDirection = Direction.DOWN;
                }
            } else if (movement.z > 0.0) {
                collisionDirection = Direction.SOUTH;
            } else {
                collisionDirection = Direction.NORTH;
            }
            return new MthUtils.CollisionResult(entryTime, collisionDirection);
        } else {
            return null;
        }
    }

    private static record CollisionResult(double entryTime, Direction direction) {
    }
}