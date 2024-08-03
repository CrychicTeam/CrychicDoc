package team.lodestar.lodestone.systems.particle.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3d;
import org.joml.Vector3f;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.helpers.BlockHelper;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.LodestoneBehaviorComponent;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

public class WorldParticleBuilder extends AbstractParticleBuilder<WorldParticleOptions> {

    private static final Random RANDOM = new Random();

    final WorldParticleOptions options;

    boolean forceSpawn = false;

    double zMotion = 0.0;

    double maxZSpeed = 0.0;

    double maxZOffset = 0.0;

    public static WorldParticleBuilder create(LodestoneWorldParticleType particle) {
        return create(particle, null);
    }

    public static WorldParticleBuilder create(LodestoneWorldParticleType particle, LodestoneBehaviorComponent behavior) {
        return create(new WorldParticleOptions(particle).setBehavior(behavior));
    }

    public static WorldParticleBuilder create(RegistryObject<? extends LodestoneWorldParticleType> particle) {
        return create(particle, null);
    }

    public static WorldParticleBuilder create(RegistryObject<? extends LodestoneWorldParticleType> particle, LodestoneBehaviorComponent behavior) {
        return create(new WorldParticleOptions((ParticleType<?>) particle.get()).setBehavior(behavior));
    }

    public static WorldParticleBuilder create(WorldParticleOptions options) {
        return new WorldParticleBuilder(options);
    }

    protected WorldParticleBuilder(WorldParticleOptions options) {
        this.options = options;
    }

    public WorldParticleOptions getParticleOptions() {
        return this.options;
    }

    public WorldParticleBuilder setBehavior(LodestoneBehaviorComponent behaviorComponent) {
        this.getParticleOptions().setBehavior(behaviorComponent);
        return this;
    }

    public <T extends LodestoneBehaviorComponent> Optional<T> getBehaviorComponent(Class<T> targetClass) {
        return targetClass.isInstance(this.getParticleOptions().behaviorComponent) ? Optional.of(this.getParticleOptions().behaviorComponent) : Optional.empty();
    }

    public <T extends LodestoneBehaviorComponent> Optional<GenericParticleData> getBehaviorData(Class<T> targetClass, Function<T, GenericParticleData> dataFunction) {
        return targetClass.isInstance(this.options.behaviorComponent) ? Optional.of((GenericParticleData) dataFunction.apply(this.options.behaviorComponent)) : Optional.empty();
    }

    public <T extends LodestoneBehaviorComponent> Optional<LodestoneBehaviorComponent> getBehaviorComponent(Class<T> targetClass, Function<WorldParticleOptions, T> componentSupplier) {
        return targetClass.isInstance(this.options.behaviorComponent) ? Optional.of((LodestoneBehaviorComponent) componentSupplier.apply(this.getParticleOptions())) : Optional.empty();
    }

    public <T extends LodestoneBehaviorComponent> WorldParticleBuilder replaceExistingBehavior(Class<T> targetClass, Function<T, LodestoneBehaviorComponent> behaviorFunction) {
        if (targetClass.isInstance(this.options.behaviorComponent)) {
            this.getParticleOptions().setBehavior((LodestoneBehaviorComponent) behaviorFunction.apply(this.options.behaviorComponent));
        }
        return this;
    }

    public <T extends LodestoneBehaviorComponent> WorldParticleBuilder modifyBehaviorData(Class<T> targetClass, Function<T, GenericParticleData> dataGetter, Consumer<GenericParticleData> dataConsumer) {
        if (targetClass.isInstance(this.options.behaviorComponent)) {
            dataConsumer.accept((GenericParticleData) dataGetter.apply(this.options.behaviorComponent));
        }
        return this;
    }

    public <T extends LodestoneBehaviorComponent> WorldParticleBuilder modifyBehavior(Class<T> targetClass, Consumer<T> behaviorConsumer) {
        if (targetClass.isInstance(this.options.behaviorComponent)) {
            behaviorConsumer.accept(this.options.behaviorComponent);
        }
        return this;
    }

    public WorldParticleBuilder modifyData(GenericParticleData dataType, Consumer<GenericParticleData> dataConsumer) {
        dataConsumer.accept(dataType);
        return this;
    }

    public WorldParticleBuilder modifyData(Supplier<GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        return this.modifyData((GenericParticleData) dataType.get(), dataConsumer);
    }

    public WorldParticleBuilder modifyData(Function<WorldParticleBuilder, GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        this.modifyData((GenericParticleData) dataType.apply(this), dataConsumer);
        return this;
    }

    public WorldParticleBuilder modifyOptionalData(Optional<GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        dataType.ifPresent(dataConsumer);
        return this;
    }

    public WorldParticleBuilder modifyOptionalData(Function<WorldParticleBuilder, Optional<GenericParticleData>> dataType, Consumer<GenericParticleData> dataConsumer) {
        this.modifyOptionalData((Optional<GenericParticleData>) dataType.apply(this), dataConsumer);
        return this;
    }

    public final WorldParticleBuilder modifyData(Collection<Supplier<GenericParticleData>> dataTypes, Consumer<GenericParticleData> dataConsumer) {
        for (Supplier<GenericParticleData> dataFunction : dataTypes) {
            dataConsumer.accept((GenericParticleData) dataFunction.get());
        }
        return this;
    }

    public WorldParticleBuilder enableNoClip() {
        return this.setNoClip(true);
    }

    public WorldParticleBuilder disableNoClip() {
        return this.setNoClip(false);
    }

    public WorldParticleBuilder setNoClip(boolean noClip) {
        this.getParticleOptions().noClip = noClip;
        return this;
    }

    public WorldParticleBuilder setRenderType(ParticleRenderType renderType) {
        this.getParticleOptions().renderType = renderType;
        return this;
    }

    public WorldParticleBuilder setRenderTarget(RenderHandler.LodestoneRenderLayer renderLayer) {
        this.getParticleOptions().renderLayer = renderLayer;
        return this;
    }

    public WorldParticleBuilder enableForcedSpawn() {
        return this.setForceSpawn(true);
    }

    public WorldParticleBuilder disableForcedSpawn() {
        return this.setForceSpawn(false);
    }

    public WorldParticleBuilder setForceSpawn(boolean forceSpawn) {
        this.forceSpawn = forceSpawn;
        return this;
    }

    public WorldParticleBuilder enableCull() {
        return this.setShouldCull(true);
    }

    public WorldParticleBuilder disableCull() {
        return this.setShouldCull(false);
    }

    public WorldParticleBuilder setShouldCull(boolean shouldCull) {
        this.getParticleOptions().shouldCull = shouldCull;
        return this;
    }

    public WorldParticleBuilder setRandomMotion(double maxSpeed) {
        return this.setRandomMotion(maxSpeed, maxSpeed, maxSpeed);
    }

    public WorldParticleBuilder setRandomMotion(double maxHSpeed, double maxVSpeed) {
        return this.setRandomMotion(maxHSpeed, maxVSpeed, maxHSpeed);
    }

    public WorldParticleBuilder setRandomMotion(double maxXSpeed, double maxYSpeed, double maxZSpeed) {
        this.maxXSpeed = maxXSpeed;
        this.maxYSpeed = maxYSpeed;
        this.maxZSpeed = maxZSpeed;
        return this;
    }

    public WorldParticleBuilder addMotion(Vector3f motion) {
        return this.addMotion((double) motion.x(), (double) motion.y(), (double) motion.z());
    }

    public WorldParticleBuilder addMotion(Vec3 motion) {
        return this.addMotion(motion.x, motion.y, motion.z);
    }

    public WorldParticleBuilder addMotion(double vx, double vy, double vz) {
        this.xMotion += vx;
        this.yMotion += vy;
        this.zMotion += vz;
        return this;
    }

    public WorldParticleBuilder setMotion(Vector3f motion) {
        return this.setMotion((double) motion.x(), (double) motion.y(), (double) motion.z());
    }

    public WorldParticleBuilder setMotion(Vec3 motion) {
        return this.setMotion(motion.x, motion.y, motion.z);
    }

    public WorldParticleBuilder setMotion(double vx, double vy, double vz) {
        this.xMotion = vx;
        this.yMotion = vy;
        this.zMotion = vz;
        return this;
    }

    public WorldParticleBuilder setRandomOffset(double maxDistance) {
        return this.setRandomOffset(maxDistance, maxDistance, maxDistance);
    }

    public WorldParticleBuilder setRandomOffset(double maxHDist, double maxVDist) {
        return this.setRandomOffset(maxHDist, maxVDist, maxHDist);
    }

    public WorldParticleBuilder setRandomOffset(double maxXDist, double maxYDist, double maxZDist) {
        this.maxXOffset = maxXDist;
        this.maxYOffset = maxYDist;
        this.maxZOffset = maxZDist;
        return this;
    }

    public WorldParticleBuilder act(Consumer<WorldParticleBuilder> particleBuilderConsumer) {
        particleBuilderConsumer.accept(this);
        return this;
    }

    public WorldParticleBuilder addTickActor(Consumer<LodestoneWorldParticle> particleActor) {
        this.getParticleOptions().tickActors.add(particleActor);
        return this;
    }

    public WorldParticleBuilder addSpawnActor(Consumer<LodestoneWorldParticle> particleActor) {
        this.getParticleOptions().spawnActors.add(particleActor);
        return this;
    }

    public WorldParticleBuilder addRenderActor(Consumer<LodestoneWorldParticle> particleActor) {
        this.getParticleOptions().renderActors.add(particleActor);
        return this;
    }

    public WorldParticleBuilder clearActors() {
        return this.clearTickActor().clearSpawnActors().clearRenderActors();
    }

    public WorldParticleBuilder clearTickActor() {
        this.getParticleOptions().tickActors.clear();
        return this;
    }

    public WorldParticleBuilder clearSpawnActors() {
        this.getParticleOptions().spawnActors.clear();
        return this;
    }

    public WorldParticleBuilder clearRenderActors() {
        this.getParticleOptions().renderActors.clear();
        return this;
    }

    public WorldParticleBuilder spawn(Level level, double x, double y, double z) {
        double yaw = (double) RANDOM.nextFloat() * Math.PI * 2.0;
        double pitch = (double) RANDOM.nextFloat() * Math.PI - (Math.PI / 2);
        double xSpeed = (double) RANDOM.nextFloat() * this.maxXSpeed;
        double ySpeed = (double) RANDOM.nextFloat() * this.maxYSpeed;
        double zSpeed = (double) RANDOM.nextFloat() * this.maxZSpeed;
        this.xMotion = this.xMotion + Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion = this.yMotion + Math.sin(pitch) * ySpeed;
        this.zMotion = this.zMotion + Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        double yaw2 = (double) RANDOM.nextFloat() * Math.PI * 2.0;
        double pitch2 = (double) RANDOM.nextFloat() * Math.PI - (Math.PI / 2);
        double xDist = (double) RANDOM.nextFloat() * this.maxXOffset;
        double yDist = (double) RANDOM.nextFloat() * this.maxYOffset;
        double zDist = (double) RANDOM.nextFloat() * this.maxZOffset;
        double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
        double yPos = Math.sin(pitch2) * yDist;
        double zPos = Math.cos(yaw2) * Math.cos(pitch2) * zDist;
        level.addParticle(this.getParticleOptions(), this.forceSpawn, x + xPos, y + yPos, z + zPos, this.xMotion, this.yMotion, this.zMotion);
        return this;
    }

    public WorldParticleBuilder repeat(Level level, double x, double y, double z, int n) {
        for (int i = 0; i < n; i++) {
            this.spawn(level, x, y, z);
        }
        return this;
    }

    public WorldParticleBuilder surroundBlock(Level level, BlockPos pos, Direction... directions) {
        if (directions.length == 0) {
            directions = Direction.values();
        }
        for (Direction direction : directions) {
            double yaw = (double) RANDOM.nextFloat() * Math.PI * 2.0;
            double pitch = (double) RANDOM.nextFloat() * Math.PI - (Math.PI / 2);
            double xSpeed = (double) RANDOM.nextFloat() * this.maxXSpeed;
            double ySpeed = (double) RANDOM.nextFloat() * this.maxYSpeed;
            double zSpeed = (double) RANDOM.nextFloat() * this.maxZSpeed;
            this.xMotion = this.xMotion + Math.sin(yaw) * Math.cos(pitch) * xSpeed;
            this.yMotion = this.yMotion + Math.sin(pitch) * ySpeed;
            this.zMotion = this.zMotion + Math.cos(yaw) * Math.cos(pitch) * zSpeed;
            Direction.Axis direction$axis = direction.getAxis();
            double d0 = 0.5625;
            double xPos = direction$axis == Direction.Axis.X ? 0.5 + d0 * (double) direction.getStepX() : RANDOM.nextDouble();
            double yPos = direction$axis == Direction.Axis.Y ? 0.5 + d0 * (double) direction.getStepY() : RANDOM.nextDouble();
            double zPos = direction$axis == Direction.Axis.Z ? 0.5 + d0 * (double) direction.getStepZ() : RANDOM.nextDouble();
            level.addParticle(this.getParticleOptions(), this.forceSpawn, (double) pos.m_123341_() + xPos, (double) pos.m_123342_() + yPos, (double) pos.m_123343_() + zPos, this.xMotion, this.yMotion, this.zMotion);
        }
        return this;
    }

    public WorldParticleBuilder repeatSurroundBlock(Level level, BlockPos pos, int n) {
        for (int i = 0; i < n; i++) {
            this.surroundBlock(level, pos);
        }
        return this;
    }

    public WorldParticleBuilder repeatSurroundBlock(Level level, BlockPos pos, int n, Direction... directions) {
        for (int i = 0; i < n; i++) {
            this.surroundBlock(level, pos, directions);
        }
        return this;
    }

    public WorldParticleBuilder surroundVoxelShape(Level level, BlockPos pos, VoxelShape voxelShape, int max) {
        int[] c = new int[1];
        int perBoxMax = max / voxelShape.toAabbs().size();
        Supplier<Boolean> r = () -> {
            c[0]++;
            if (c[0] >= perBoxMax) {
                c[0] = 0;
                return true;
            } else {
                return false;
            }
        };
        Vec3 v = BlockHelper.fromBlockPos(pos);
        voxelShape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            Vec3 b = v.add(x1, y1, z1);
            Vec3 e = v.add(x2, y2, z2);
            List<Runnable> runs = new ArrayList();
            runs.add((Runnable) () -> this.spawnLine(level, b, v.add(x2, y1, z1)));
            runs.add((Runnable) () -> this.spawnLine(level, b, v.add(x1, y2, z1)));
            runs.add((Runnable) () -> this.spawnLine(level, b, v.add(x1, y1, z2)));
            runs.add((Runnable) () -> this.spawnLine(level, v.add(x1, y2, z1), v.add(x2, y2, z1)));
            runs.add((Runnable) () -> this.spawnLine(level, v.add(x1, y2, z1), v.add(x1, y2, z2)));
            runs.add((Runnable) () -> this.spawnLine(level, e, v.add(x2, y2, z1)));
            runs.add((Runnable) () -> this.spawnLine(level, e, v.add(x1, y2, z2)));
            runs.add((Runnable) () -> this.spawnLine(level, e, v.add(x2, y1, z2)));
            runs.add((Runnable) () -> this.spawnLine(level, v.add(x2, y1, z1), v.add(x2, y1, z2)));
            runs.add((Runnable) () -> this.spawnLine(level, v.add(x1, y1, z2), v.add(x2, y1, z2)));
            runs.add((Runnable) () -> this.spawnLine(level, v.add(x2, y1, z1), v.add(x2, y2, z1)));
            runs.add((Runnable) () -> this.spawnLine(level, v.add(x1, y1, z2), v.add(x1, y2, z2)));
            Collections.shuffle(runs);
            for (Runnable runnable : runs) {
                runnable.run();
                if ((Boolean) r.get()) {
                    break;
                }
            }
        });
        return this;
    }

    public WorldParticleBuilder surroundVoxelShape(Level level, BlockPos pos, BlockState state, int max) {
        VoxelShape voxelShape = state.m_60808_(level, pos);
        if (voxelShape.isEmpty()) {
            voxelShape = Shapes.block();
        }
        return this.surroundVoxelShape(level, pos, voxelShape, max);
    }

    public WorldParticleBuilder spawnAtRandomFace(Level level, BlockPos pos) {
        Direction direction = Direction.values()[RANDOM.nextInt(Direction.values().length)];
        double yaw = (double) RANDOM.nextFloat() * Math.PI * 2.0;
        double pitch = (double) RANDOM.nextFloat() * Math.PI - (Math.PI / 2);
        double xSpeed = (double) RANDOM.nextFloat() * this.maxXSpeed;
        double ySpeed = (double) RANDOM.nextFloat() * this.maxYSpeed;
        double zSpeed = (double) RANDOM.nextFloat() * this.maxZSpeed;
        this.xMotion = this.xMotion + Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion = this.yMotion + Math.sin(pitch) * ySpeed;
        this.zMotion = this.zMotion + Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        Direction.Axis direction$axis = direction.getAxis();
        double d0 = 0.5625;
        double xPos = direction$axis == Direction.Axis.X ? 0.5 + d0 * (double) direction.getStepX() : RANDOM.nextDouble();
        double yPos = direction$axis == Direction.Axis.Y ? 0.5 + d0 * (double) direction.getStepY() : RANDOM.nextDouble();
        double zPos = direction$axis == Direction.Axis.Z ? 0.5 + d0 * (double) direction.getStepZ() : RANDOM.nextDouble();
        level.addParticle(this.getParticleOptions(), this.forceSpawn, (double) pos.m_123341_() + xPos, (double) pos.m_123342_() + yPos, (double) pos.m_123343_() + zPos, this.xMotion, this.yMotion, this.zMotion);
        return this;
    }

    public WorldParticleBuilder repeatRandomFace(Level level, BlockPos pos, int n) {
        for (int i = 0; i < n; i++) {
            this.spawnAtRandomFace(level, pos);
        }
        return this;
    }

    public WorldParticleBuilder createCircle(Level level, double x, double y, double z, double distance, double currentCount, double totalCount) {
        double xSpeed = (double) RANDOM.nextFloat() * this.maxXSpeed;
        double ySpeed = (double) RANDOM.nextFloat() * this.maxYSpeed;
        double zSpeed = (double) RANDOM.nextFloat() * this.maxZSpeed;
        double theta = (Math.PI * 2) / totalCount;
        double finalAngle = currentCount / totalCount + theta * currentCount;
        double dx2 = distance * Math.cos(finalAngle);
        double dz2 = distance * Math.sin(finalAngle);
        Vector3d vector2f = new Vector3d(dx2, 0.0, dz2);
        this.xMotion = vector2f.x * xSpeed;
        this.zMotion = vector2f.z * zSpeed;
        double yaw2 = (double) RANDOM.nextFloat() * Math.PI * 2.0;
        double pitch2 = (double) RANDOM.nextFloat() * Math.PI - (Math.PI / 2);
        double xDist = (double) RANDOM.nextFloat() * this.maxXOffset;
        double yDist = (double) RANDOM.nextFloat() * this.maxYOffset;
        double zDist = (double) RANDOM.nextFloat() * this.maxZOffset;
        double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
        double yPos = Math.sin(pitch2) * yDist;
        double zPos = Math.cos(yaw2) * Math.cos(pitch2) * zDist;
        level.addParticle(this.getParticleOptions(), this.forceSpawn, x + xPos + dx2, y + yPos, z + zPos + dz2, this.xMotion, ySpeed, this.zMotion);
        return this;
    }

    public WorldParticleBuilder repeatCircle(Level level, double x, double y, double z, double distance, int times) {
        for (int i = 0; i < times; i++) {
            this.createCircle(level, x, y, z, distance, (double) i, (double) times);
        }
        return this;
    }

    public WorldParticleBuilder createBlockOutline(Level level, BlockPos pos, BlockState state) {
        VoxelShape voxelShape = state.m_60808_(level, pos);
        double d = 0.25;
        voxelShape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            Vec3 v = BlockHelper.fromBlockPos(pos);
            Vec3 b = BlockHelper.fromBlockPos(pos).add(x1, y1, z1);
            Vec3 e = BlockHelper.fromBlockPos(pos).add(x2, y2, z2);
            this.spawnLine(level, b, v.add(x2, y1, z1));
            this.spawnLine(level, b, v.add(x1, y2, z1));
            this.spawnLine(level, b, v.add(x1, y1, z2));
            this.spawnLine(level, v.add(x1, y2, z1), v.add(x2, y2, z1));
            this.spawnLine(level, v.add(x1, y2, z1), v.add(x1, y2, z2));
            this.spawnLine(level, e, v.add(x2, y2, z1));
            this.spawnLine(level, e, v.add(x1, y2, z2));
            this.spawnLine(level, e, v.add(x2, y1, z2));
            this.spawnLine(level, v.add(x2, y1, z1), v.add(x2, y1, z2));
            this.spawnLine(level, v.add(x1, y1, z2), v.add(x2, y1, z2));
            this.spawnLine(level, v.add(x2, y1, z1), v.add(x2, y2, z1));
            this.spawnLine(level, v.add(x1, y1, z2), v.add(x1, y2, z2));
        });
        return this;
    }

    public WorldParticleBuilder spawnLine(Level level, Vec3 one, Vec3 two) {
        double yaw = (double) RANDOM.nextFloat() * Math.PI * 2.0;
        double pitch = (double) RANDOM.nextFloat() * Math.PI - (Math.PI / 2);
        double xSpeed = (double) RANDOM.nextFloat() * this.maxXSpeed;
        double ySpeed = (double) RANDOM.nextFloat() * this.maxYSpeed;
        double zSpeed = (double) RANDOM.nextFloat() * this.maxZSpeed;
        this.xMotion = this.xMotion + Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion = this.yMotion + Math.sin(pitch) * ySpeed;
        this.zMotion = this.zMotion + Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        Vec3 pos = one.lerp(two, RANDOM.nextDouble());
        level.addParticle(this.getParticleOptions(), this.forceSpawn, pos.x, pos.y, pos.z, this.xMotion, this.yMotion, this.zMotion);
        return this;
    }

    public WorldParticleBuilder modifyColorData(Consumer<ColorParticleData> dataConsumer) {
        return (WorldParticleBuilder) super.modifyColorData(dataConsumer);
    }

    public WorldParticleBuilder setColorData(ColorParticleData colorData) {
        return (WorldParticleBuilder) super.setColorData(colorData);
    }

    public WorldParticleBuilder setScaleData(GenericParticleData scaleData) {
        return (WorldParticleBuilder) super.setScaleData(scaleData);
    }

    public WorldParticleBuilder setTransparencyData(GenericParticleData transparencyData) {
        return (WorldParticleBuilder) super.setTransparencyData(transparencyData);
    }

    public WorldParticleBuilder setSpinData(SpinParticleData spinData) {
        return (WorldParticleBuilder) super.setSpinData(spinData);
    }

    public WorldParticleBuilder multiplyGravity(float gravityMultiplier) {
        return (WorldParticleBuilder) super.multiplyGravity(gravityMultiplier);
    }

    public WorldParticleBuilder modifyGravity(Function<Float, Supplier<Float>> gravityReplacement) {
        return (WorldParticleBuilder) super.modifyGravity(gravityReplacement);
    }

    public WorldParticleBuilder setGravityStrength(float gravity) {
        return (WorldParticleBuilder) super.setGravityStrength(gravity);
    }

    public WorldParticleBuilder setGravityStrength(Supplier<Float> gravityStrengthSupplier) {
        return (WorldParticleBuilder) super.setGravityStrength(gravityStrengthSupplier);
    }

    public WorldParticleBuilder multiplyFriction(float GravityMultiplier) {
        return (WorldParticleBuilder) super.multiplyFriction(GravityMultiplier);
    }

    public WorldParticleBuilder modifyFriction(Function<Float, Supplier<Float>> GravityReplacement) {
        return (WorldParticleBuilder) super.modifyFriction(GravityReplacement);
    }

    public WorldParticleBuilder setFrictionStrength(float Gravity) {
        return (WorldParticleBuilder) super.setFrictionStrength(Gravity);
    }

    public WorldParticleBuilder setFrictionStrength(Supplier<Float> GravityStrengthSupplier) {
        return (WorldParticleBuilder) super.setFrictionStrength(GravityStrengthSupplier);
    }

    public WorldParticleBuilder multiplyLifetime(float lifetimeMultiplier) {
        return (WorldParticleBuilder) super.multiplyLifetime(lifetimeMultiplier);
    }

    public WorldParticleBuilder modifyLifetime(Function<Integer, Supplier<Integer>> lifetimeReplacement) {
        return (WorldParticleBuilder) super.modifyLifetime(lifetimeReplacement);
    }

    public WorldParticleBuilder setLifetime(int lifetime) {
        return (WorldParticleBuilder) super.setLifetime(lifetime);
    }

    public WorldParticleBuilder setLifetime(Supplier<Integer> lifetimeSupplier) {
        return (WorldParticleBuilder) super.setLifetime(lifetimeSupplier);
    }

    public WorldParticleBuilder multiplyLifeDelay(float lifeDelayMultiplier) {
        return (WorldParticleBuilder) super.multiplyLifeDelay(lifeDelayMultiplier);
    }

    public WorldParticleBuilder modifyLifeDelay(Function<Integer, Supplier<Integer>> lifeDelayReplacement) {
        return (WorldParticleBuilder) super.modifyLifeDelay(lifeDelayReplacement);
    }

    public WorldParticleBuilder setLifeDelay(int lifeDelay) {
        return (WorldParticleBuilder) super.setLifeDelay(lifeDelay);
    }

    public WorldParticleBuilder setLifeDelay(Supplier<Integer> lifeDelaySupplier) {
        return (WorldParticleBuilder) super.setLifeDelay(lifeDelaySupplier);
    }

    public WorldParticleBuilder setSpritePicker(SimpleParticleOptions.ParticleSpritePicker spritePicker) {
        return (WorldParticleBuilder) super.setSpritePicker(spritePicker);
    }

    public WorldParticleBuilder setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType) {
        return (WorldParticleBuilder) super.setDiscardFunction(discardFunctionType);
    }
}