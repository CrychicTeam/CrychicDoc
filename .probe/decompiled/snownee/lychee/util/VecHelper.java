package snownee.lychee.util;

import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import snownee.lychee.mixin.GameRendererAccess;

public class VecHelper {

    public static final Vec3 CENTER_OF_ORIGIN = new Vec3(0.5, 0.5, 0.5);

    public static Vec3 rotate(Vec3 vec, Vec3 rotationVec) {
        return rotate(vec, rotationVec.x, rotationVec.y, rotationVec.z);
    }

    public static Vec3 rotate(Vec3 vec, double xRot, double yRot, double zRot) {
        return rotate(rotate(rotate(vec, xRot, Direction.Axis.X), yRot, Direction.Axis.Y), zRot, Direction.Axis.Z);
    }

    public static Vec3 rotateCentered(Vec3 vec, double deg, Direction.Axis axis) {
        Vec3 shift = getCenterOf(BlockPos.ZERO);
        return rotate(vec.subtract(shift), deg, axis).add(shift);
    }

    public static Vec3 rotate(Vec3 vec, double deg, Direction.Axis axis) {
        if (deg == 0.0) {
            return vec;
        } else if (vec == Vec3.ZERO) {
            return vec;
        } else {
            float angle = (float) (deg / 180.0 * Math.PI);
            double sin = (double) Mth.sin(angle);
            double cos = (double) Mth.cos(angle);
            double x = vec.x;
            double y = vec.y;
            double z = vec.z;
            if (axis == Direction.Axis.X) {
                return new Vec3(x, y * cos - z * sin, z * cos + y * sin);
            } else if (axis == Direction.Axis.Y) {
                return new Vec3(x * cos + z * sin, y, z * cos - x * sin);
            } else {
                return axis == Direction.Axis.Z ? new Vec3(x * cos - y * sin, y * cos + x * sin, z) : vec;
            }
        }
    }

    public static Vec3 mirrorCentered(Vec3 vec, Mirror mirror) {
        Vec3 shift = getCenterOf(BlockPos.ZERO);
        return mirror(vec.subtract(shift), mirror).add(shift);
    }

    public static Vec3 mirror(Vec3 vec, Mirror mirror) {
        if (mirror == null || mirror == Mirror.NONE) {
            return vec;
        } else if (vec == Vec3.ZERO) {
            return vec;
        } else {
            double x = vec.x;
            double y = vec.y;
            double z = vec.z;
            if (mirror == Mirror.LEFT_RIGHT) {
                return new Vec3(x, y, -z);
            } else {
                return mirror == Mirror.FRONT_BACK ? new Vec3(-x, y, z) : vec;
            }
        }
    }

    public static Vec3 lookAt(Vec3 vec, Vec3 fwd) {
        fwd = fwd.normalize();
        Vec3 up = new Vec3(0.0, 1.0, 0.0);
        double dot = fwd.dot(up);
        if (Math.abs(dot) > 0.999) {
            up = new Vec3(0.0, 0.0, dot > 0.0 ? 1.0 : -1.0);
        }
        Vec3 right = fwd.cross(up).normalize();
        up = right.cross(fwd).normalize();
        double x = vec.x * right.x + vec.y * up.x + vec.z * fwd.x;
        double y = vec.x * right.y + vec.y * up.y + vec.z * fwd.y;
        double z = vec.x * right.z + vec.y * up.z + vec.z * fwd.z;
        return new Vec3(x, y, z);
    }

    public static boolean isVecPointingTowards(Vec3 vec, Direction direction) {
        return Vec3.atLowerCornerOf(direction.getNormal()).dot(vec.normalize()) > 0.125;
    }

    public static Vec3 getCenterOf(Vec3i pos) {
        return pos.equals(Vec3i.ZERO) ? CENTER_OF_ORIGIN : Vec3.atLowerCornerOf(pos).add(0.5, 0.5, 0.5);
    }

    public static Vec3 offsetRandomly(Vec3 vec, RandomSource r, float radius) {
        return new Vec3(vec.x + (double) ((r.nextFloat() - 0.5F) * 2.0F * radius), vec.y + (double) ((r.nextFloat() - 0.5F) * 2.0F * radius), vec.z + (double) ((r.nextFloat() - 0.5F) * 2.0F * radius));
    }

    public static Vec3 axisAlingedPlaneOf(Vec3 vec) {
        vec = vec.normalize();
        return new Vec3(1.0, 1.0, 1.0).subtract(Math.abs(vec.x), Math.abs(vec.y), Math.abs(vec.z));
    }

    public static Vec3 axisAlingedPlaneOf(Direction face) {
        return axisAlingedPlaneOf(Vec3.atLowerCornerOf(face.getNormal()));
    }

    public static ListTag writeNBT(Vec3 vec) {
        ListTag listnbt = new ListTag();
        listnbt.add(DoubleTag.valueOf(vec.x));
        listnbt.add(DoubleTag.valueOf(vec.y));
        listnbt.add(DoubleTag.valueOf(vec.z));
        return listnbt;
    }

    public static CompoundTag writeNBTCompound(Vec3 vec) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("V", writeNBT(vec));
        return compoundTag;
    }

    public static Vec3 readNBT(ListTag list) {
        return list.isEmpty() ? Vec3.ZERO : new Vec3(list.getDouble(0), list.getDouble(1), list.getDouble(2));
    }

    public static Vec3 readNBTCompound(CompoundTag nbt) {
        return readNBT(nbt.getList("V", 6));
    }

    public static void write(Vec3 vec, FriendlyByteBuf buffer) {
        buffer.writeDouble(vec.x);
        buffer.writeDouble(vec.y);
        buffer.writeDouble(vec.z);
    }

    public static Vec3 read(FriendlyByteBuf buffer) {
        return new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static Vec3 voxelSpace(double x, double y, double z) {
        return new Vec3(x, y, z).scale(0.0625);
    }

    public static int getCoordinate(Vec3i pos, Direction.Axis axis) {
        return axis.choose(pos.getX(), pos.getY(), pos.getZ());
    }

    public static float getCoordinate(Vec3 vec, Direction.Axis axis) {
        return (float) axis.choose(vec.x, vec.y, vec.z);
    }

    public static boolean onSameAxis(BlockPos pos1, BlockPos pos2, Direction.Axis axis) {
        if (pos1.equals(pos2)) {
            return true;
        } else {
            for (Direction.Axis otherAxis : Direction.Axis.values()) {
                if (axis != otherAxis && getCoordinate(pos1, otherAxis) != getCoordinate(pos2, otherAxis)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static Vec3 clamp(Vec3 vec, float maxLength) {
        return vec.lengthSqr() > (double) (maxLength * maxLength) ? vec.normalize().scale((double) maxLength) : vec;
    }

    public static Vec3 lerp(float p, Vec3 from, Vec3 to) {
        return from.add(to.subtract(from).scale((double) p));
    }

    public static Vec3 slerp(float p, Vec3 from, Vec3 to) {
        double theta = Math.acos(from.dot(to));
        return from.scale((double) Mth.sin(1.0F - p) * theta).add(to.scale((double) Mth.sin((float) (theta * (double) p)))).scale((double) (1.0F / Mth.sin((float) theta)));
    }

    public static Vec3 clampComponentWise(Vec3 vec, float maxLength) {
        return new Vec3(Mth.clamp(vec.x, (double) (-maxLength), (double) maxLength), Mth.clamp(vec.y, (double) (-maxLength), (double) maxLength), Mth.clamp(vec.z, (double) (-maxLength), (double) maxLength));
    }

    public static Vec3 componentMin(Vec3 vec1, Vec3 vec2) {
        return new Vec3(Math.min(vec1.x, vec2.x), Math.min(vec1.y, vec2.y), Math.min(vec1.z, vec2.z));
    }

    public static Vec3 componentMax(Vec3 vec1, Vec3 vec2) {
        return new Vec3(Math.max(vec1.x, vec2.x), Math.max(vec1.y, vec2.y), Math.max(vec1.z, vec2.z));
    }

    public static Vec3 project(Vec3 vec, Vec3 ontoVec) {
        return ontoVec.equals(Vec3.ZERO) ? Vec3.ZERO : ontoVec.scale(vec.dot(ontoVec) / ontoVec.lengthSqr());
    }

    @Nullable
    public static Vec3 intersectSphere(Vec3 origin, Vec3 lineDirection, Vec3 sphereCenter, double radius) {
        if (lineDirection.equals(Vec3.ZERO)) {
            return null;
        } else {
            if (lineDirection.lengthSqr() != 1.0) {
                lineDirection = lineDirection.normalize();
            }
            Vec3 diff = origin.subtract(sphereCenter);
            double lineDotDiff = lineDirection.dot(diff);
            double delta = lineDotDiff * lineDotDiff - (diff.lengthSqr() - radius * radius);
            if (delta < 0.0) {
                return null;
            } else {
                double t = -lineDotDiff + Math.sqrt(delta);
                return origin.add(lineDirection.scale(t));
            }
        }
    }

    public static Vec3 projectToPlayerView(Vec3 target, float partialTicks) {
        Camera ari = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 camera_pos = ari.getPosition();
        Quaternionf camera_rotation_conj = new Quaternionf(ari.rotation());
        camera_rotation_conj.conjugate();
        Vector3f result3f = new Vector3f((float) (camera_pos.x - target.x), (float) (camera_pos.y - target.y), (float) (camera_pos.z - target.z));
        result3f.rotate(camera_rotation_conj);
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.bobView().get() && mc.getCameraEntity() instanceof Player playerentity) {
            float distwalked_modified = playerentity.f_19787_;
            float f = distwalked_modified - playerentity.f_19867_;
            float f1 = -(distwalked_modified + f * partialTicks);
            float f2 = Mth.lerp(partialTicks, playerentity.oBob, playerentity.bob);
            Quaternionf q2 = Axis.XP.rotationDegrees(Math.abs(Mth.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F);
            q2.conjugate();
            result3f.rotate(q2);
            Quaternionf q1 = Axis.ZP.rotationDegrees(Mth.sin(f1 * (float) Math.PI) * f2 * 3.0F);
            q1.conjugate();
            result3f.rotate(q1);
            Vector3f bob_translation = new Vector3f(Mth.sin(f1 * (float) Math.PI) * f2 * 0.5F, -Math.abs(Mth.cos(f1 * (float) Math.PI) * f2), 0.0F);
            bob_translation.y = -bob_translation.y();
            result3f.add(bob_translation);
        }
        float fov = (float) ((GameRendererAccess) mc.gameRenderer).callGetFov(ari, partialTicks, true);
        float half_height = (float) mc.getWindow().getGuiScaledHeight() / 2.0F;
        float scale_factor = half_height / (result3f.z() * (float) Math.tan(Math.toRadians((double) (fov / 2.0F))));
        return new Vec3((double) (-result3f.x() * scale_factor), (double) (result3f.y() * scale_factor), (double) result3f.z());
    }

    public static Vec3 bezier(Vec3 p1, Vec3 p2, Vec3 q1, Vec3 q2, float t) {
        Vec3 v1 = lerp(t, p1, q1);
        Vec3 v2 = lerp(t, q1, q2);
        Vec3 v3 = lerp(t, q2, p2);
        Vec3 inner1 = lerp(t, v1, v2);
        Vec3 inner2 = lerp(t, v2, v3);
        return lerp(t, inner1, inner2);
    }

    public static Vec3 bezierDerivative(Vec3 p1, Vec3 p2, Vec3 q1, Vec3 q2, float t) {
        return p1.scale((double) (-3.0F * t * t + 6.0F * t - 3.0F)).add(q1.scale((double) (9.0F * t * t - 12.0F * t + 3.0F))).add(q2.scale((double) (-9.0F * t * t + 6.0F * t))).add(p2.scale((double) (3.0F * t * t)));
    }

    @Nullable
    public static double[] intersectRanged(Vec3 p1, Vec3 q1, Vec3 p2, Vec3 q2, Direction.Axis plane) {
        Vec3 pDiff = p2.subtract(p1);
        Vec3 qDiff = q2.subtract(q1);
        double[] intersect = intersect(p1, q1, pDiff.normalize(), qDiff.normalize(), plane);
        if (intersect == null) {
            return null;
        } else if (intersect[0] < 0.0 || intersect[1] < 0.0) {
            return null;
        } else {
            return !(intersect[0] * intersect[0] > pDiff.lengthSqr()) && !(intersect[1] * intersect[1] > qDiff.lengthSqr()) ? intersect : null;
        }
    }

    @Nullable
    public static double[] intersect(Vec3 p1, Vec3 p2, Vec3 r, Vec3 s, Direction.Axis plane) {
        if (plane == Direction.Axis.X) {
            p1 = new Vec3(p1.y, 0.0, p1.z);
            p2 = new Vec3(p2.y, 0.0, p2.z);
            r = new Vec3(r.y, 0.0, r.z);
            s = new Vec3(s.y, 0.0, s.z);
        }
        if (plane == Direction.Axis.Z) {
            p1 = new Vec3(p1.x, 0.0, p1.y);
            p2 = new Vec3(p2.x, 0.0, p2.y);
            r = new Vec3(r.x, 0.0, r.y);
            s = new Vec3(s.x, 0.0, s.y);
        }
        Vec3 qminusp = p2.subtract(p1);
        double rcs = r.x * s.z - r.z * s.x;
        if (Mth.equal(rcs, 0.0)) {
            return null;
        } else {
            Vec3 rdivrcs = r.scale(1.0 / rcs);
            Vec3 sdivrcs = s.scale(1.0 / rcs);
            double t = qminusp.x * sdivrcs.z - qminusp.z * sdivrcs.x;
            double u = qminusp.x * rdivrcs.z - qminusp.z * rdivrcs.x;
            return new double[] { t, u };
        }
    }
}