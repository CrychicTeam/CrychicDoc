package com.simibubi.create.content.kinetics.fan;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessing;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import org.apache.commons.lang3.tuple.Pair;

public class AirCurrent {

    public final IAirCurrentSource source;

    public AABB bounds = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

    public List<AirCurrent.AirCurrentSegment> segments = new ArrayList();

    public Direction direction;

    public boolean pushing;

    public float maxDistance;

    protected List<Pair<TransportedItemStackHandlerBehaviour, FanProcessingType>> affectedItemHandlers = new ArrayList();

    protected List<Entity> caughtEntities = new ArrayList();

    private static final double[][] DEPTH_TEST_COORDINATES = new double[][] { { 0.25, 0.25 }, { 0.25, 0.75 }, { 0.5, 0.5 }, { 0.75, 0.25 }, { 0.75, 0.75 } };

    private static boolean isClientPlayerInAirCurrent;

    @OnlyIn(Dist.CLIENT)
    private static AirCurrentSound flyingSound;

    public AirCurrent(IAirCurrentSource source) {
        this.source = source;
    }

    public void tick() {
        if (this.direction == null) {
            this.rebuild();
        }
        Level world = this.source.getAirCurrentWorld();
        if (world != null && world.isClientSide) {
            float offset = this.pushing ? 0.5F : this.maxDistance + 0.5F;
            Vec3 pos = VecHelper.getCenterOf(this.source.getAirCurrentPos()).add(Vec3.atLowerCornerOf(this.direction.getNormal()).scale((double) offset));
            if ((double) world.random.nextFloat() < AllConfigs.client().fanParticleDensity.get()) {
                world.addParticle(new AirFlowParticleData(this.source.getAirCurrentPos()), pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
            }
        }
        this.tickAffectedEntities(world);
        this.tickAffectedHandlers();
    }

    protected void tickAffectedEntities(Level world) {
        Iterator<Entity> iterator = this.caughtEntities.iterator();
        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            if (entity.isAlive() && entity.getBoundingBox().intersects(this.bounds) && !isPlayerCreativeFlying(entity)) {
                Vec3i flow = (this.pushing ? this.direction : this.direction.getOpposite()).getNormal();
                float speed = Math.abs(this.source.getSpeed());
                float sneakModifier = entity.isShiftKeyDown() ? 4096.0F : 512.0F;
                double entityDistance = VecHelper.alignedDistanceToFace(entity.position(), this.source.getAirCurrentPos(), this.direction);
                double entityDistanceOld = entity.position().distanceTo(VecHelper.getCenterOf(this.source.getAirCurrentPos()));
                float acceleration = (float) ((double) (speed / sneakModifier) / (entityDistanceOld / (double) this.maxDistance));
                Vec3 previousMotion = entity.getDeltaMovement();
                float maxAcceleration = 5.0F;
                double xIn = Mth.clamp((double) ((float) flow.getX() * acceleration) - previousMotion.x, (double) (-maxAcceleration), (double) maxAcceleration);
                double yIn = Mth.clamp((double) ((float) flow.getY() * acceleration) - previousMotion.y, (double) (-maxAcceleration), (double) maxAcceleration);
                double zIn = Mth.clamp((double) ((float) flow.getZ() * acceleration) - previousMotion.z, (double) (-maxAcceleration), (double) maxAcceleration);
                entity.setDeltaMovement(previousMotion.add(new Vec3(xIn, yIn, zIn).scale(0.125)));
                entity.fallDistance = 0.0F;
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> enableClientPlayerSound(entity, Mth.clamp(speed / 128.0F * 0.4F, 0.01F, 0.4F)));
                if (entity instanceof ServerPlayer) {
                    ((ServerPlayer) entity).connection.aboveGroundTickCount = 0;
                }
                FanProcessingType processingType = this.getTypeAt((float) entityDistance);
                if (processingType != AllFanProcessingTypes.NONE) {
                    if (entity instanceof ItemEntity) {
                        ItemEntity itemEntity = (ItemEntity) entity;
                        if (world != null && world.isClientSide) {
                            processingType.spawnProcessingParticles(world, entity.position());
                        } else if (FanProcessing.canProcess(itemEntity, processingType) && FanProcessing.applyProcessing(itemEntity, processingType) && this.source instanceof EncasedFanBlockEntity fan) {
                            fan.award(AllAdvancements.FAN_PROCESSING);
                        }
                    } else if (world != null) {
                        processingType.affectEntity(entity, world);
                    }
                }
            } else {
                iterator.remove();
            }
        }
    }

    public static boolean isPlayerCreativeFlying(Entity entity) {
        return !(entity instanceof Player player) ? false : player.isCreative() && player.getAbilities().flying;
    }

    public void tickAffectedHandlers() {
        for (Pair<TransportedItemStackHandlerBehaviour, FanProcessingType> pair : this.affectedItemHandlers) {
            TransportedItemStackHandlerBehaviour handler = (TransportedItemStackHandlerBehaviour) pair.getKey();
            Level world = handler.getWorld();
            FanProcessingType processingType = (FanProcessingType) pair.getRight();
            handler.handleProcessingOnAllItems(transported -> {
                if (world.isClientSide) {
                    processingType.spawnProcessingParticles(world, handler.getWorldPositionOf(transported));
                    return TransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
                } else {
                    TransportedItemStackHandlerBehaviour.TransportedResult applyProcessing = FanProcessing.applyProcessing(transported, world, processingType);
                    if (!applyProcessing.doesNothing() && this.source instanceof EncasedFanBlockEntity fan) {
                        fan.award(AllAdvancements.FAN_PROCESSING);
                    }
                    return applyProcessing;
                }
            });
        }
    }

    public void rebuild() {
        if (this.source.getSpeed() == 0.0F) {
            this.maxDistance = 0.0F;
            this.segments.clear();
            this.bounds = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        } else {
            this.direction = this.source.getAirflowOriginSide();
            this.pushing = this.source.getAirFlowDirection() == this.direction;
            this.maxDistance = this.source.getMaxDistance();
            Level world = this.source.getAirCurrentWorld();
            BlockPos start = this.source.getAirCurrentPos();
            float max = this.maxDistance;
            Direction facing = this.direction;
            Vec3 directionVec = Vec3.atLowerCornerOf(facing.getNormal());
            this.maxDistance = getFlowLimit(world, start, max, facing);
            this.segments.clear();
            AirCurrent.AirCurrentSegment currentSegment = null;
            FanProcessingType type = AllFanProcessingTypes.NONE;
            int limit = this.getLimit();
            int searchStart = this.pushing ? 1 : limit;
            int searchEnd = this.pushing ? limit : 1;
            int searchStep = this.pushing ? 1 : -1;
            int toOffset = this.pushing ? -1 : 0;
            for (int i = searchStart; i * searchStep <= searchEnd * searchStep; i += searchStep) {
                BlockPos currentPos = start.relative(this.direction, i);
                FanProcessingType newType = FanProcessingType.getAt(world, currentPos);
                if (newType != AllFanProcessingTypes.NONE) {
                    type = newType;
                }
                if (currentSegment == null) {
                    currentSegment = new AirCurrent.AirCurrentSegment();
                    currentSegment.startOffset = i + toOffset;
                    currentSegment.type = type;
                } else if (currentSegment.type != type) {
                    currentSegment.endOffset = i + toOffset;
                    this.segments.add(currentSegment);
                    currentSegment = new AirCurrent.AirCurrentSegment();
                    currentSegment.startOffset = i + toOffset;
                    currentSegment.type = type;
                }
            }
            if (currentSegment != null) {
                currentSegment.endOffset = searchEnd + searchStep + toOffset;
                this.segments.add(currentSegment);
            }
            if (this.maxDistance < 0.25F) {
                this.bounds = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            } else {
                float factor = this.maxDistance - 1.0F;
                Vec3 scale = directionVec.scale((double) factor);
                if (factor > 0.0F) {
                    this.bounds = new AABB(start.relative(this.direction)).expandTowards(scale);
                } else {
                    this.bounds = new AABB(start.relative(this.direction)).contract(scale.x, scale.y, scale.z).move(scale);
                }
            }
            this.findAffectedHandlers();
        }
    }

    public static float getFlowLimit(Level world, BlockPos start, float max, Direction facing) {
        for (int i = 0; (float) i < max; i++) {
            BlockPos currentPos = start.relative(facing, i + 1);
            if (!world.isLoaded(currentPos)) {
                return (float) i;
            }
            BlockState state = world.getBlockState(currentPos);
            BlockState copycatState = CopycatBlock.getMaterial(world, currentPos);
            if (!shouldAlwaysPass(copycatState.m_60795_() ? state : copycatState)) {
                VoxelShape shape = state.m_60812_(world, currentPos);
                if (!shape.isEmpty()) {
                    if (shape == Shapes.block()) {
                        return (float) i;
                    }
                    double shapeDepth = findMaxDepth(shape, facing);
                    if (shapeDepth != Double.POSITIVE_INFINITY) {
                        return Math.min((float) ((double) i + shapeDepth + 0.03125), max);
                    }
                }
            }
        }
        return max;
    }

    private static double findMaxDepth(VoxelShape shape, Direction direction) {
        Direction.Axis axis = direction.getAxis();
        Direction.AxisDirection axisDirection = direction.getAxisDirection();
        double maxDepth = 0.0;
        for (double[] coordinates : DEPTH_TEST_COORDINATES) {
            double depth;
            if (axisDirection == Direction.AxisDirection.POSITIVE) {
                double min = shape.min(axis, coordinates[0], coordinates[1]);
                if (min == Double.POSITIVE_INFINITY) {
                    return Double.POSITIVE_INFINITY;
                }
                depth = min;
            } else {
                double max = shape.max(axis, coordinates[0], coordinates[1]);
                if (max == Double.NEGATIVE_INFINITY) {
                    return Double.POSITIVE_INFINITY;
                }
                depth = 1.0 - max;
            }
            if (depth > maxDepth) {
                maxDepth = depth;
            }
        }
        return maxDepth;
    }

    private static boolean shouldAlwaysPass(BlockState state) {
        return AllTags.AllBlockTags.FAN_TRANSPARENT.matches(state);
    }

    private int getLimit() {
        return (float) ((int) this.maxDistance) == this.maxDistance ? (int) this.maxDistance : (int) this.maxDistance + 1;
    }

    public void findAffectedHandlers() {
        Level world = this.source.getAirCurrentWorld();
        BlockPos start = this.source.getAirCurrentPos();
        this.affectedItemHandlers.clear();
        int limit = this.getLimit();
        for (int i = 1; i <= limit; i++) {
            FanProcessingType segmentType = this.getTypeAt((float) (i - 1));
            for (int offset : Iterate.zeroAndOne) {
                BlockPos pos = start.relative(this.direction, i).below(offset);
                TransportedItemStackHandlerBehaviour behaviour = BlockEntityBehaviour.get(world, pos, TransportedItemStackHandlerBehaviour.TYPE);
                if (behaviour != null) {
                    FanProcessingType type = FanProcessingType.getAt(world, pos);
                    if (type == AllFanProcessingTypes.NONE) {
                        type = segmentType;
                    }
                    this.affectedItemHandlers.add(Pair.of(behaviour, type));
                }
                if (this.direction.getAxis().isVertical()) {
                    break;
                }
            }
        }
    }

    public void findEntities() {
        this.caughtEntities.clear();
        this.caughtEntities = this.source.getAirCurrentWorld().m_45933_(null, this.bounds);
    }

    public FanProcessingType getTypeAt(float offset) {
        if (offset >= 0.0F && offset <= this.maxDistance) {
            if (this.pushing) {
                for (AirCurrent.AirCurrentSegment airCurrentSegment : this.segments) {
                    if (offset <= (float) airCurrentSegment.endOffset) {
                        return airCurrentSegment.type;
                    }
                }
            } else {
                for (AirCurrent.AirCurrentSegment airCurrentSegmentx : this.segments) {
                    if (offset >= (float) airCurrentSegmentx.endOffset) {
                        return airCurrentSegmentx.type;
                    }
                }
            }
        }
        return AllFanProcessingTypes.NONE;
    }

    @OnlyIn(Dist.CLIENT)
    private static void enableClientPlayerSound(Entity e, float maxVolume) {
        if (e == Minecraft.getInstance().getCameraEntity()) {
            isClientPlayerInAirCurrent = true;
            float pitch = (float) Mth.clamp(e.getDeltaMovement().length() * 0.5, 0.5, 2.0);
            if (flyingSound == null || flyingSound.m_7801_()) {
                flyingSound = new AirCurrentSound(SoundEvents.ELYTRA_FLYING, pitch);
                Minecraft.getInstance().getSoundManager().play(flyingSound);
            }
            flyingSound.setPitch(pitch);
            flyingSound.fadeIn(maxVolume);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void tickClientPlayerSounds() {
        if (!isClientPlayerInAirCurrent && flyingSound != null) {
            if (flyingSound.isFaded()) {
                flyingSound.stopSound();
            } else {
                flyingSound.fadeOut();
            }
        }
        isClientPlayerInAirCurrent = false;
    }

    private static class AirCurrentSegment {

        private FanProcessingType type;

        private int startOffset;

        private int endOffset;
    }
}