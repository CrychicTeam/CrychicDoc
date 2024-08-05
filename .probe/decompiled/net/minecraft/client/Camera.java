package net.minecraft.client;

import java.util.Arrays;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera {

    private boolean initialized;

    private BlockGetter level;

    private Entity entity;

    private Vec3 position = Vec3.ZERO;

    private final BlockPos.MutableBlockPos blockPosition = new BlockPos.MutableBlockPos();

    private final Vector3f forwards = new Vector3f(0.0F, 0.0F, 1.0F);

    private final Vector3f up = new Vector3f(0.0F, 1.0F, 0.0F);

    private final Vector3f left = new Vector3f(1.0F, 0.0F, 0.0F);

    private float xRot;

    private float yRot;

    private final Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);

    private boolean detached;

    private float eyeHeight;

    private float eyeHeightOld;

    public static final float FOG_DISTANCE_SCALE = 0.083333336F;

    public void setup(BlockGetter blockGetter0, Entity entity1, boolean boolean2, boolean boolean3, float float4) {
        this.initialized = true;
        this.level = blockGetter0;
        this.entity = entity1;
        this.detached = boolean2;
        this.setRotation(entity1.getViewYRot(float4), entity1.getViewXRot(float4));
        this.setPosition(Mth.lerp((double) float4, entity1.xo, entity1.getX()), Mth.lerp((double) float4, entity1.yo, entity1.getY()) + (double) Mth.lerp(float4, this.eyeHeightOld, this.eyeHeight), Mth.lerp((double) float4, entity1.zo, entity1.getZ()));
        if (boolean2) {
            if (boolean3) {
                this.setRotation(this.yRot + 180.0F, -this.xRot);
            }
            this.move(-this.getMaxZoom(4.0), 0.0, 0.0);
        } else if (entity1 instanceof LivingEntity && ((LivingEntity) entity1).isSleeping()) {
            Direction $$5 = ((LivingEntity) entity1).getBedOrientation();
            this.setRotation($$5 != null ? $$5.toYRot() - 180.0F : 0.0F, 0.0F);
            this.move(0.0, 0.3, 0.0);
        }
    }

    public void tick() {
        if (this.entity != null) {
            this.eyeHeightOld = this.eyeHeight;
            this.eyeHeight = this.eyeHeight + (this.entity.getEyeHeight() - this.eyeHeight) * 0.5F;
        }
    }

    private double getMaxZoom(double double0) {
        for (int $$1 = 0; $$1 < 8; $$1++) {
            float $$2 = (float) (($$1 & 1) * 2 - 1);
            float $$3 = (float) (($$1 >> 1 & 1) * 2 - 1);
            float $$4 = (float) (($$1 >> 2 & 1) * 2 - 1);
            $$2 *= 0.1F;
            $$3 *= 0.1F;
            $$4 *= 0.1F;
            Vec3 $$5 = this.position.add((double) $$2, (double) $$3, (double) $$4);
            Vec3 $$6 = new Vec3(this.position.x - (double) this.forwards.x() * double0 + (double) $$2, this.position.y - (double) this.forwards.y() * double0 + (double) $$3, this.position.z - (double) this.forwards.z() * double0 + (double) $$4);
            HitResult $$7 = this.level.clip(new ClipContext($$5, $$6, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, this.entity));
            if ($$7.getType() != HitResult.Type.MISS) {
                double $$8 = $$7.getLocation().distanceTo(this.position);
                if ($$8 < double0) {
                    double0 = $$8;
                }
            }
        }
        return double0;
    }

    protected void move(double double0, double double1, double double2) {
        double $$3 = (double) this.forwards.x() * double0 + (double) this.up.x() * double1 + (double) this.left.x() * double2;
        double $$4 = (double) this.forwards.y() * double0 + (double) this.up.y() * double1 + (double) this.left.y() * double2;
        double $$5 = (double) this.forwards.z() * double0 + (double) this.up.z() * double1 + (double) this.left.z() * double2;
        this.setPosition(new Vec3(this.position.x + $$3, this.position.y + $$4, this.position.z + $$5));
    }

    protected void setRotation(float float0, float float1) {
        this.xRot = float1;
        this.yRot = float0;
        this.rotation.rotationYXZ(-float0 * (float) (Math.PI / 180.0), float1 * (float) (Math.PI / 180.0), 0.0F);
        this.forwards.set(0.0F, 0.0F, 1.0F).rotate(this.rotation);
        this.up.set(0.0F, 1.0F, 0.0F).rotate(this.rotation);
        this.left.set(1.0F, 0.0F, 0.0F).rotate(this.rotation);
    }

    protected void setPosition(double double0, double double1, double double2) {
        this.setPosition(new Vec3(double0, double1, double2));
    }

    protected void setPosition(Vec3 vec0) {
        this.position = vec0;
        this.blockPosition.set(vec0.x, vec0.y, vec0.z);
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public BlockPos getBlockPosition() {
        return this.blockPosition;
    }

    public float getXRot() {
        return this.xRot;
    }

    public float getYRot() {
        return this.yRot;
    }

    public Quaternionf rotation() {
        return this.rotation;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public boolean isDetached() {
        return this.detached;
    }

    public Camera.NearPlane getNearPlane() {
        Minecraft $$0 = Minecraft.getInstance();
        double $$1 = (double) $$0.getWindow().getWidth() / (double) $$0.getWindow().getHeight();
        double $$2 = Math.tan((double) ((float) $$0.options.fov().get().intValue() * (float) (Math.PI / 180.0)) / 2.0) * 0.05F;
        double $$3 = $$2 * $$1;
        Vec3 $$4 = new Vec3(this.forwards).scale(0.05F);
        Vec3 $$5 = new Vec3(this.left).scale($$3);
        Vec3 $$6 = new Vec3(this.up).scale($$2);
        return new Camera.NearPlane($$4, $$5, $$6);
    }

    public FogType getFluidInCamera() {
        if (!this.initialized) {
            return FogType.NONE;
        } else {
            FluidState $$0 = this.level.getFluidState(this.blockPosition);
            if ($$0.is(FluidTags.WATER) && this.position.y < (double) ((float) this.blockPosition.m_123342_() + $$0.getHeight(this.level, this.blockPosition))) {
                return FogType.WATER;
            } else {
                Camera.NearPlane $$1 = this.getNearPlane();
                for (Vec3 $$3 : Arrays.asList($$1.forward, $$1.getTopLeft(), $$1.getTopRight(), $$1.getBottomLeft(), $$1.getBottomRight())) {
                    Vec3 $$4 = this.position.add($$3);
                    BlockPos $$5 = BlockPos.containing($$4);
                    FluidState $$6 = this.level.getFluidState($$5);
                    if ($$6.is(FluidTags.LAVA)) {
                        if ($$4.y <= (double) ($$6.getHeight(this.level, $$5) + (float) $$5.m_123342_())) {
                            return FogType.LAVA;
                        }
                    } else {
                        BlockState $$7 = this.level.getBlockState($$5);
                        if ($$7.m_60713_(Blocks.POWDER_SNOW)) {
                            return FogType.POWDER_SNOW;
                        }
                    }
                }
                return FogType.NONE;
            }
        }
    }

    public final Vector3f getLookVector() {
        return this.forwards;
    }

    public final Vector3f getUpVector() {
        return this.up;
    }

    public final Vector3f getLeftVector() {
        return this.left;
    }

    public void reset() {
        this.level = null;
        this.entity = null;
        this.initialized = false;
    }

    public static class NearPlane {

        final Vec3 forward;

        private final Vec3 left;

        private final Vec3 up;

        NearPlane(Vec3 vec0, Vec3 vec1, Vec3 vec2) {
            this.forward = vec0;
            this.left = vec1;
            this.up = vec2;
        }

        public Vec3 getTopLeft() {
            return this.forward.add(this.up).add(this.left);
        }

        public Vec3 getTopRight() {
            return this.forward.add(this.up).subtract(this.left);
        }

        public Vec3 getBottomLeft() {
            return this.forward.subtract(this.up).add(this.left);
        }

        public Vec3 getBottomRight() {
            return this.forward.subtract(this.up).subtract(this.left);
        }

        public Vec3 getPointOnPlane(float float0, float float1) {
            return this.forward.add(this.up.scale((double) float1)).subtract(this.left.scale((double) float0));
        }
    }
}