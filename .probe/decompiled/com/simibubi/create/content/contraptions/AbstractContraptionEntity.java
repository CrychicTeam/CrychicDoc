package com.simibubi.create.content.contraptions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceMovement;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsStopControllingPacket;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.elevator.ElevatorContraption;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.contraptions.mounted.MountedContraption;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.contraptions.sync.ContraptionSeatMappingPacket;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.collision.Matrix3d;
import com.simibubi.create.foundation.mixin.accessor.ServerLevelAccessor;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.MutablePair;

public abstract class AbstractContraptionEntity extends Entity implements IEntityAdditionalSpawnData {

    private static final EntityDataAccessor<Boolean> STALLED = SynchedEntityData.defineId(AbstractContraptionEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<UUID>> CONTROLLED_BY = SynchedEntityData.defineId(AbstractContraptionEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    public final Map<Entity, MutableInt> collidingEntities;

    protected Contraption contraption;

    protected boolean initialized;

    protected boolean prevPosInvalid;

    private boolean skipActorStop;

    public int staleTicks = 3;

    public AbstractContraptionEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.prevPosInvalid = true;
        this.collidingEntities = new IdentityHashMap();
    }

    protected void setContraption(Contraption contraption) {
        this.contraption = contraption;
        if (contraption != null) {
            if (!this.m_9236_().isClientSide) {
                contraption.onEntityCreated(this);
            }
        }
    }

    @Override
    public void move(MoverType pType, Vec3 pPos) {
        if (pType != MoverType.SHULKER) {
            if (pType != MoverType.SHULKER_BOX) {
                if (pType != MoverType.PISTON) {
                    super.move(pType, pPos);
                }
            }
        }
    }

    public boolean supportsTerrainCollision() {
        return this.contraption instanceof TranslatingContraption && !(this.contraption instanceof ElevatorContraption);
    }

    protected void contraptionInitialize() {
        this.contraption.onEntityInitialize(this.m_9236_(), this);
        this.initialized = true;
    }

    public boolean collisionEnabled() {
        return true;
    }

    public void registerColliding(Entity collidingEntity) {
        this.collidingEntities.put(collidingEntity, new MutableInt());
    }

    public void addSittingPassenger(Entity passenger, int seatIndex) {
        for (Entity entity : this.m_20197_()) {
            BlockPos seatOf = this.contraption.getSeatOf(entity.getUUID());
            if (seatOf != null && seatOf.equals(this.contraption.getSeats().get(seatIndex))) {
                if (entity instanceof Player) {
                    return;
                }
                if (!(passenger instanceof Player)) {
                    return;
                }
                entity.stopRiding();
            }
        }
        passenger.startRiding(this, true);
        if (passenger instanceof TamableAnimal ta) {
            ta.setInSittingPose(true);
        }
        if (!this.m_9236_().isClientSide) {
            this.contraption.getSeatMapping().put(passenger.getUUID(), seatIndex);
            AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new ContraptionSeatMappingPacket(this.m_19879_(), this.contraption.getSeatMapping()));
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        Vec3 transformedVector = this.getPassengerPosition(passenger, 1.0F);
        super.removePassenger(passenger);
        if (passenger instanceof TamableAnimal ta) {
            ta.setInSittingPose(false);
        }
        if (!this.m_9236_().isClientSide) {
            if (transformedVector != null) {
                passenger.getPersistentData().put("ContraptionDismountLocation", VecHelper.writeNBT(transformedVector));
            }
            this.contraption.getSeatMapping().remove(passenger.getUUID());
            AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new ContraptionSeatMappingPacket(this.m_19879_(), this.contraption.getSeatMapping(), passenger.getId()));
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity entityLiving) {
        Vec3 position = super.getDismountLocationForPassenger(entityLiving);
        CompoundTag data = entityLiving.getPersistentData();
        if (!data.contains("ContraptionDismountLocation")) {
            return position;
        } else {
            position = VecHelper.readNBT(data.getList("ContraptionDismountLocation", 6));
            data.remove("ContraptionDismountLocation");
            entityLiving.m_6853_(false);
            if (!data.contains("ContraptionMountLocation")) {
                return position;
            } else {
                Vec3 prevPosition = VecHelper.readNBT(data.getList("ContraptionMountLocation", 6));
                data.remove("ContraptionMountLocation");
                if (entityLiving instanceof Player player && !prevPosition.closerThan(position, 5000.0)) {
                    AllAdvancements.LONG_TRAVEL.awardTo(player);
                }
                return position;
            }
        }
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction callback) {
        if (this.m_20363_(passenger)) {
            Vec3 transformedVector = this.getPassengerPosition(passenger, 1.0F);
            if (transformedVector != null) {
                callback.accept(passenger, transformedVector.x, transformedVector.y + SeatEntity.getCustomEntitySeatOffset(passenger) - 0.125, transformedVector.z);
            }
        }
    }

    public Vec3 getPassengerPosition(Entity passenger, float partialTicks) {
        if (this.contraption == null) {
            return null;
        } else {
            UUID id = passenger.getUUID();
            if (passenger instanceof OrientedContraptionEntity) {
                BlockPos localPos = this.contraption.getBearingPosOf(id);
                if (localPos != null) {
                    return this.toGlobalVector(VecHelper.getCenterOf(localPos), partialTicks).add(VecHelper.getCenterOf(BlockPos.ZERO)).subtract(0.5, 1.0, 0.5);
                }
            }
            AABB bb = passenger.getBoundingBox();
            double ySize = bb.getYsize();
            BlockPos seat = this.contraption.getSeatOf(id);
            return seat == null ? null : this.toGlobalVector(Vec3.atLowerCornerOf(seat).add(0.5, passenger.getMyRidingOffset() + ySize - 0.15F, 0.5), partialTicks).add(VecHelper.getCenterOf(BlockPos.ZERO)).subtract(0.5, ySize, 0.5);
        }
    }

    @Override
    protected boolean canAddPassenger(Entity p_184219_1_) {
        return p_184219_1_ instanceof OrientedContraptionEntity ? true : this.contraption.getSeatMapping().size() < this.contraption.getSeats().size();
    }

    public Component getContraptionName() {
        return this.m_7755_();
    }

    public Optional<UUID> getControllingPlayer() {
        return this.f_19804_.get(CONTROLLED_BY);
    }

    public void setControllingPlayer(@Nullable UUID playerId) {
        this.f_19804_.set(CONTROLLED_BY, Optional.ofNullable(playerId));
    }

    public boolean startControlling(BlockPos controlsLocalPos, Player player) {
        return false;
    }

    public boolean control(BlockPos controlsLocalPos, Collection<Integer> heldControls, Player player) {
        return true;
    }

    public void stopControlling(BlockPos controlsLocalPos) {
        this.getControllingPlayer().map(this.m_9236_()::m_46003_).map(p -> p instanceof ServerPlayer ? (ServerPlayer) p : null).ifPresent(p -> AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> p), new ControlsStopControllingPacket()));
        this.setControllingPlayer(null);
    }

    public boolean handlePlayerInteraction(Player player, BlockPos localPos, Direction side, InteractionHand interactionHand) {
        int indexOfSeat = this.contraption.getSeats().indexOf(localPos);
        if (indexOfSeat != -1 && !AllItems.WRENCH.isIn(player.m_21120_(interactionHand))) {
            if (player.m_20159_()) {
                return false;
            } else {
                Entity toDismount = null;
                for (Entry<UUID, Integer> entry : this.contraption.getSeatMapping().entrySet()) {
                    if ((Integer) entry.getValue() == indexOfSeat) {
                        for (Entity entity : this.m_20197_()) {
                            if (((UUID) entry.getKey()).equals(entity.getUUID())) {
                                if (entity instanceof Player) {
                                    return false;
                                }
                                toDismount = entity;
                            }
                        }
                    }
                }
                if (toDismount != null && !this.m_9236_().isClientSide) {
                    Vec3 transformedVector = this.getPassengerPosition(toDismount, 1.0F);
                    toDismount.stopRiding();
                    if (transformedVector != null) {
                        toDismount.teleportTo(transformedVector.x, transformedVector.y, transformedVector.z);
                    }
                }
                if (this.m_9236_().isClientSide) {
                    return true;
                } else {
                    this.addSittingPassenger((Entity) SeatBlock.getLeashed(this.m_9236_(), player).or(player), indexOfSeat);
                    return true;
                }
            }
        } else {
            return this.contraption.interactors.containsKey(localPos) ? ((MovingInteractionBehaviour) this.contraption.interactors.get(localPos)).handlePlayerInteraction(player, interactionHand, localPos, this) : this.contraption.storage.handlePlayerStorageInteraction(this.contraption, player, localPos);
        }
    }

    public Vec3 toGlobalVector(Vec3 localVec, float partialTicks) {
        return this.toGlobalVector(localVec, partialTicks, false);
    }

    public Vec3 toGlobalVector(Vec3 localVec, float partialTicks, boolean prevAnchor) {
        Vec3 anchor = prevAnchor ? this.getPrevAnchorVec() : this.getAnchorVec();
        Vec3 rotationOffset = VecHelper.getCenterOf(BlockPos.ZERO);
        localVec = localVec.subtract(rotationOffset);
        localVec = this.applyRotation(localVec, partialTicks);
        return localVec.add(rotationOffset).add(anchor);
    }

    public Vec3 toLocalVector(Vec3 localVec, float partialTicks) {
        return this.toLocalVector(localVec, partialTicks, false);
    }

    public Vec3 toLocalVector(Vec3 globalVec, float partialTicks, boolean prevAnchor) {
        Vec3 anchor = prevAnchor ? this.getPrevAnchorVec() : this.getAnchorVec();
        Vec3 rotationOffset = VecHelper.getCenterOf(BlockPos.ZERO);
        globalVec = globalVec.subtract(anchor).subtract(rotationOffset);
        globalVec = this.reverseRotation(globalVec, partialTicks);
        return globalVec.add(rotationOffset);
    }

    @Override
    public void tick() {
        if (this.contraption == null) {
            this.m_146870_();
        } else {
            this.collidingEntities.entrySet().removeIf(e -> ((MutableInt) e.getValue()).incrementAndGet() > 3);
            this.f_19854_ = this.m_20185_();
            this.f_19855_ = this.m_20186_();
            this.f_19856_ = this.m_20189_();
            this.prevPosInvalid = false;
            if (!this.initialized) {
                this.contraptionInitialize();
            }
            this.contraption.tickStorage(this);
            this.tickContraption();
            super.tick();
            if (this.m_9236_().isClientSide()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    if (this.contraption.deferInvalidate) {
                        this.contraption.deferInvalidate = false;
                        ContraptionRenderDispatcher.invalidate(this.contraption);
                    }
                });
            }
            if (this.m_9236_() instanceof ServerLevelAccessor sl) {
                for (Entity entity : this.m_20197_()) {
                    if (!(entity instanceof Player) && !entity.isAlwaysTicking() && !sl.create$getEntityTickList().contains(entity)) {
                        this.m_7332_(entity);
                    }
                }
            }
        }
    }

    public void alignPassenger(Entity passenger) {
        Vec3 motion = this.getContactPointMotion(passenger.getEyePosition());
        if (!Mth.equal(motion.length(), 0.0)) {
            if (!(passenger instanceof ArmorStand)) {
                if (passenger instanceof LivingEntity living) {
                    float prevAngle = living.m_146908_();
                    float angle = AngleHelper.deg(-Mth.atan2(motion.x, motion.z));
                    angle = AngleHelper.angleLerp(0.4F, (double) prevAngle, (double) angle);
                    if (this.m_9236_().isClientSide) {
                        living.lerpTo(0.0, 0.0, 0.0, 0.0F, 0.0F, 0, false);
                        living.lerpHeadTo(0.0F, 0);
                        living.m_146922_(angle);
                        living.m_146926_(0.0F);
                        living.yBodyRot = angle;
                        living.yHeadRot = angle;
                    } else {
                        living.m_146922_(angle);
                    }
                }
            }
        }
    }

    public void setBlock(BlockPos localPos, StructureTemplate.StructureBlockInfo newInfo) {
        this.contraption.blocks.put(localPos, newInfo);
        AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new ContraptionBlockChangedPacket(this.m_19879_(), localPos, newInfo.state()));
    }

    protected abstract void tickContraption();

    public abstract Vec3 applyRotation(Vec3 var1, float var2);

    public abstract Vec3 reverseRotation(Vec3 var1, float var2);

    public void tickActors() {
        boolean stalledPreviously = this.contraption.stalled;
        if (!this.m_9236_().isClientSide) {
            this.contraption.stalled = false;
        }
        this.skipActorStop = true;
        for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> pair : this.contraption.getActors()) {
            MovementContext context = (MovementContext) pair.right;
            StructureTemplate.StructureBlockInfo blockInfo = (StructureTemplate.StructureBlockInfo) pair.left;
            MovementBehaviour actor = AllMovementBehaviours.getBehaviour(blockInfo.state());
            if (actor != null) {
                Vec3 oldMotion = context.motion;
                Vec3 actorPosition = this.toGlobalVector(VecHelper.getCenterOf(blockInfo.pos()).add(actor.getActiveAreaOffset(context)), 1.0F);
                BlockPos gridPosition = BlockPos.containing(actorPosition);
                boolean newPosVisited = !context.stall && this.shouldActorTrigger(context, blockInfo, actor, actorPosition, gridPosition);
                context.rotation = v -> this.applyRotation(v, 1.0F);
                context.position = actorPosition;
                if (this.isActorActive(context, actor) || actor.mustTickWhileDisabled()) {
                    if (newPosVisited && !context.stall) {
                        actor.visitNewPosition(context, gridPosition);
                        if (!this.m_6084_()) {
                            break;
                        }
                        context.firstMovement = false;
                    }
                    if (!oldMotion.equals(context.motion)) {
                        actor.onSpeedChanged(context, oldMotion, context.motion);
                        if (!this.m_6084_()) {
                            break;
                        }
                    }
                    actor.tick(context);
                    if (!this.m_6084_()) {
                        break;
                    }
                    this.contraption.stalled = this.contraption.stalled | context.stall;
                }
            }
        }
        if (!this.m_6084_()) {
            this.contraption.stop(this.m_9236_());
        } else {
            this.skipActorStop = false;
            for (Entity entity : this.m_20197_()) {
                if (entity instanceof OrientedContraptionEntity && this.contraption.stabilizedSubContraptions.containsKey(entity.getUUID())) {
                    OrientedContraptionEntity orientedCE = (OrientedContraptionEntity) entity;
                    if (orientedCE.contraption != null && orientedCE.contraption.stalled) {
                        this.contraption.stalled = true;
                        break;
                    }
                }
            }
            if (!this.m_9236_().isClientSide) {
                if (!stalledPreviously && this.contraption.stalled) {
                    this.onContraptionStalled();
                }
                this.f_19804_.set(STALLED, this.contraption.stalled);
            } else {
                this.contraption.stalled = this.isStalled();
            }
        }
    }

    public void refreshPSIs() {
        for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> pair : this.contraption.getActors()) {
            MovementContext context = (MovementContext) pair.right;
            StructureTemplate.StructureBlockInfo blockInfo = (StructureTemplate.StructureBlockInfo) pair.left;
            MovementBehaviour actor = AllMovementBehaviours.getBehaviour(blockInfo.state());
            if (actor instanceof PortableStorageInterfaceMovement && this.isActorActive(context, actor) && context.position != null) {
                actor.visitNewPosition(context, BlockPos.containing(context.position));
            }
        }
    }

    protected boolean isActorActive(MovementContext context, MovementBehaviour actor) {
        return actor.isActive(context);
    }

    protected void onContraptionStalled() {
        AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new ContraptionStallPacket(this.m_19879_(), this.m_20185_(), this.m_20186_(), this.m_20189_(), this.getStalledAngle()));
    }

    protected boolean shouldActorTrigger(MovementContext context, StructureTemplate.StructureBlockInfo blockInfo, MovementBehaviour actor, Vec3 actorPosition, BlockPos gridPosition) {
        Vec3 previousPosition = context.position;
        if (previousPosition == null) {
            return false;
        } else {
            context.motion = actorPosition.subtract(previousPosition);
            if (!this.m_9236_().isClientSide() && context.contraption.entity instanceof CarriageContraptionEntity cce && cce.getCarriage() != null) {
                Train train = cce.getCarriage().train;
                double actualSpeed = train.speedBeforeStall != null ? train.speedBeforeStall : train.speed;
                context.motion = context.motion.normalize().scale(Math.abs(actualSpeed));
            }
            Vec3 relativeMotion = context.motion;
            relativeMotion = this.reverseRotation(relativeMotion, 1.0F);
            context.relativeMotion = relativeMotion;
            return !BlockPos.containing(previousPosition).equals(gridPosition) || (context.relativeMotion.length() > 0.0 || context.contraption instanceof CarriageContraption) && context.firstMovement;
        }
    }

    public void move(double x, double y, double z) {
        this.setPos(this.m_20185_() + x, this.m_20186_() + y, this.m_20189_() + z);
    }

    public Vec3 getAnchorVec() {
        return this.m_20182_();
    }

    public Vec3 getPrevAnchorVec() {
        return this.getPrevPositionVec();
    }

    public float getYawOffset() {
        return 0.0F;
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(x, y, z);
        if (this.contraption != null) {
            AABB cbox = this.contraption.bounds;
            if (cbox != null) {
                Vec3 actualVec = this.getAnchorVec();
                this.m_20011_(cbox.move(actualVec));
            }
        }
    }

    public static float yawFromVector(Vec3 vec) {
        return (float) (((Math.PI * 3.0 / 2.0) + Math.atan2(vec.z, vec.x)) / Math.PI * 180.0);
    }

    public static float pitchFromVector(Vec3 vec) {
        return (float) (Math.acos(vec.y) / Math.PI * 180.0);
    }

    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        return builder.sized(1.0F, 1.0F);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(STALLED, false);
        this.f_19804_.define(CONTROLLED_BY, Optional.empty());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        CompoundTag compound = new CompoundTag();
        this.writeAdditional(compound, true);
        if (ContraptionData.isTooLargeForSync(compound)) {
            String info = this.getContraption().getType().id + " @" + this.m_20182_() + " (" + this.m_20149_() + ")";
            Create.LOGGER.warn("Could not send Contraption Spawn Data (Packet too big): " + info);
            compound = null;
        }
        buffer.writeNbt(compound);
    }

    @Override
    protected final void addAdditionalSaveData(CompoundTag compound) {
        this.writeAdditional(compound, false);
    }

    protected void writeAdditional(CompoundTag compound, boolean spawnPacket) {
        if (this.contraption != null) {
            compound.put("Contraption", this.contraption.writeNBT(spawnPacket));
        }
        compound.putBoolean("Stalled", this.isStalled());
        compound.putBoolean("Initialized", this.initialized);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        CompoundTag nbt = additionalData.readAnySizeNbt();
        if (nbt != null) {
            this.readAdditional(nbt, true);
        }
    }

    @Override
    protected final void readAdditionalSaveData(CompoundTag compound) {
        this.readAdditional(compound, false);
    }

    protected void readAdditional(CompoundTag compound, boolean spawnData) {
        if (!compound.isEmpty()) {
            this.initialized = compound.getBoolean("Initialized");
            this.contraption = Contraption.fromNBT(this.m_9236_(), compound.getCompound("Contraption"), spawnData);
            this.contraption.entity = this;
            this.f_19804_.set(STALLED, compound.getBoolean("Stalled"));
        }
    }

    public void disassemble() {
        if (this.m_6084_()) {
            if (this.contraption != null) {
                StructureTransform transform = this.makeStructureTransform();
                this.contraption.stop(this.m_9236_());
                AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new ContraptionDisassemblyPacket(this.m_19879_(), transform));
                this.contraption.addBlocksToWorld(this.m_9236_(), transform);
                this.contraption.addPassengersToWorld(this.m_9236_(), transform, this.m_20197_());
                for (Entity entity : this.m_20197_()) {
                    if (entity instanceof OrientedContraptionEntity) {
                        UUID id = entity.getUUID();
                        if (this.contraption.stabilizedSubContraptions.containsKey(id)) {
                            BlockPos transformed = transform.apply(((BlockFace) this.contraption.stabilizedSubContraptions.get(id)).getConnectedPos());
                            entity.setPos((double) transformed.m_123341_(), (double) transformed.m_123342_(), (double) transformed.m_123343_());
                            ((AbstractContraptionEntity) entity).disassemble();
                        }
                    }
                }
                this.skipActorStop = true;
                this.m_146870_();
                this.m_20153_();
                this.moveCollidedEntitiesOnDisassembly(transform);
                AllSoundEvents.CONTRAPTION_DISASSEMBLE.playOnServer(this.m_9236_(), this.m_20183_());
            }
        }
    }

    private void moveCollidedEntitiesOnDisassembly(StructureTransform transform) {
        for (Entity entity : this.collidingEntities.keySet()) {
            Vec3 localVec = this.toLocalVector(entity.position(), 0.0F);
            Vec3 transformed = transform.apply(localVec);
            if (this.m_9236_().isClientSide) {
                entity.setPos(transformed.x, transformed.y + 0.0625, transformed.z);
            } else {
                entity.teleportTo(transformed.x, transformed.y + 0.0625, transformed.z);
            }
        }
    }

    @Override
    public void remove(Entity.RemovalReason entityRemovalReason0) {
        if (!this.m_9236_().isClientSide && !this.m_213877_() && this.contraption != null && !this.skipActorStop) {
            this.contraption.stop(this.m_9236_());
        }
        if (this.contraption != null) {
            this.contraption.onEntityRemoved(this);
        }
        super.remove(entityRemovalReason0);
    }

    protected abstract StructureTransform makeStructureTransform();

    @Override
    public void kill() {
        this.m_20153_();
        super.kill();
    }

    @Override
    protected void onBelowWorld() {
        this.m_20153_();
        super.onBelowWorld();
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
    }

    @Override
    protected void doWaterSplashEffect() {
    }

    public Contraption getContraption() {
        return this.contraption;
    }

    public boolean isStalled() {
        return this.f_19804_.get(STALLED);
    }

    @OnlyIn(Dist.CLIENT)
    static void handleStallPacket(ContraptionStallPacket packet) {
        if (Minecraft.getInstance().level.getEntity(packet.entityID) instanceof AbstractContraptionEntity ce) {
            ce.handleStallInformation(packet.x, packet.y, packet.z, packet.angle);
        }
    }

    @OnlyIn(Dist.CLIENT)
    static void handleBlockChangedPacket(ContraptionBlockChangedPacket packet) {
        if (Minecraft.getInstance().level.getEntity(packet.entityID) instanceof AbstractContraptionEntity ce) {
            ce.handleBlockChange(packet.localPos, packet.newState);
        }
    }

    @OnlyIn(Dist.CLIENT)
    static void handleDisassemblyPacket(ContraptionDisassemblyPacket packet) {
        if (Minecraft.getInstance().level.getEntity(packet.entityID) instanceof AbstractContraptionEntity ce) {
            ce.moveCollidedEntitiesOnDisassembly(packet.transform);
        }
    }

    protected abstract float getStalledAngle();

    protected abstract void handleStallInformation(double var1, double var3, double var5, float var7);

    @OnlyIn(Dist.CLIENT)
    protected void handleBlockChange(BlockPos localPos, BlockState newState) {
        if (this.contraption != null && this.contraption.blocks.containsKey(localPos)) {
            StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) this.contraption.blocks.get(localPos);
            this.contraption.blocks.put(localPos, new StructureTemplate.StructureBlockInfo(info.pos(), newState, info.nbt()));
            if (info.state() != newState && !(newState.m_60734_() instanceof SlidingDoorBlock)) {
                this.contraption.deferInvalidate = true;
            }
            this.contraption.invalidateColliders();
        }
    }

    @Override
    public CompoundTag saveWithoutId(CompoundTag nbt) {
        Vec3 vec = this.m_20182_();
        for (Entity entity : this.m_20197_()) {
            entity.removalReason = Entity.RemovalReason.UNLOADED_TO_CHUNK;
            Vec3 prevVec = entity.position();
            entity.setPosRaw(vec.x, prevVec.y, vec.z);
            entity.removalReason = null;
        }
        CompoundTag tag = super.saveWithoutId(nbt);
        return tag;
    }

    @Override
    public void setDeltaMovement(Vec3 motionIn) {
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public void setContraptionMotion(Vec3 vec) {
        super.setDeltaMovement(vec);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    public Vec3 getPrevPositionVec() {
        return this.prevPosInvalid ? this.m_20182_() : new Vec3(this.f_19854_, this.f_19855_, this.f_19856_);
    }

    public abstract AbstractContraptionEntity.ContraptionRotationState getRotationState();

    public Vec3 getContactPointMotion(Vec3 globalContactPoint) {
        if (this.prevPosInvalid) {
            return Vec3.ZERO;
        } else {
            Vec3 contactPoint = this.toGlobalVector(this.toLocalVector(globalContactPoint, 0.0F, true), 1.0F, true);
            Vec3 contraptionLocalMovement = contactPoint.subtract(globalContactPoint);
            Vec3 contraptionAnchorMovement = this.m_20182_().subtract(this.getPrevPositionVec());
            return contraptionLocalMovement.add(contraptionAnchorMovement);
        }
    }

    @Override
    public boolean canCollideWith(Entity e) {
        if (e instanceof Player && e.isSpectator()) {
            return false;
        } else if (e.noPhysics) {
            return false;
        } else if (e instanceof HangingEntity) {
            return false;
        } else if (e instanceof AbstractMinecart) {
            return !(this.contraption instanceof MountedContraption);
        } else if (e instanceof SuperGlueEntity) {
            return false;
        } else if (e instanceof SeatEntity) {
            return false;
        } else if (e instanceof Projectile) {
            return false;
        } else if (e.getVehicle() != null) {
            return false;
        } else {
            for (Entity riding = this.m_20202_(); riding != null; riding = riding.getVehicle()) {
                if (riding == e) {
                    return false;
                }
            }
            return e.getPistonPushReaction() == PushReaction.NORMAL;
        }
    }

    @Override
    public boolean hasExactlyOnePlayerPassenger() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void applyLocalTransforms(PoseStack var1, float var2);

    @Override
    protected boolean updateInWaterStateAndDoFluidPushing() {
        return false;
    }

    @Override
    public void setSecondsOnFire(int p_70015_1_) {
    }

    public boolean isReadyForRender() {
        return this.initialized;
    }

    public boolean isAliveOrStale() {
        return !this.m_6084_() && !this.m_9236_().isClientSide() ? false : this.staleTicks > 0;
    }

    public static class ContraptionRotationState {

        public static final AbstractContraptionEntity.ContraptionRotationState NONE = new AbstractContraptionEntity.ContraptionRotationState();

        float xRotation = 0.0F;

        float yRotation = 0.0F;

        float zRotation = 0.0F;

        float secondYRotation = 0.0F;

        Matrix3d matrix;

        public Matrix3d asMatrix() {
            if (this.matrix != null) {
                return this.matrix;
            } else {
                this.matrix = new Matrix3d().asIdentity();
                if (this.xRotation != 0.0F) {
                    this.matrix.multiply(new Matrix3d().asXRotation(AngleHelper.rad((double) (-this.xRotation))));
                }
                if (this.yRotation != 0.0F) {
                    this.matrix.multiply(new Matrix3d().asYRotation(AngleHelper.rad((double) (-this.yRotation))));
                }
                if (this.zRotation != 0.0F) {
                    this.matrix.multiply(new Matrix3d().asZRotation(AngleHelper.rad((double) (-this.zRotation))));
                }
                return this.matrix;
            }
        }

        public boolean hasVerticalRotation() {
            return this.xRotation != 0.0F || this.zRotation != 0.0F;
        }

        public float getYawOffset() {
            return this.secondYRotation;
        }
    }
}