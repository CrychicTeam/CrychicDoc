package com.simibubi.create.content.trains.entity;

import com.google.common.base.Strings;
import com.simibubi.create.AllEntityDataSerializers;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.ContraptionBlockChangedPacket;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsBlock;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.trains.CubeParticleData;
import com.simibubi.create.content.trains.TrainHUDUpdatePacket;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

public class CarriageContraptionEntity extends OrientedContraptionEntity {

    private static final EntityDataAccessor<CarriageSyncData> CARRIAGE_DATA = SynchedEntityData.defineId(CarriageContraptionEntity.class, AllEntityDataSerializers.CARRIAGE_DATA);

    private static final EntityDataAccessor<Optional<UUID>> TRACK_GRAPH = SynchedEntityData.defineId(CarriageContraptionEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Boolean> SCHEDULED = SynchedEntityData.defineId(CarriageContraptionEntity.class, EntityDataSerializers.BOOLEAN);

    public UUID trainId;

    public int carriageIndex;

    private Carriage carriage;

    public boolean validForRender;

    public boolean movingBackwards;

    public boolean leftTickingChunks;

    public boolean firstPositionUpdate;

    private boolean arrivalSoundPlaying;

    private boolean arrivalSoundReversed;

    private int arrivalSoundTicks;

    private Vec3 serverPrevPos;

    @OnlyIn(Dist.CLIENT)
    public CarriageSounds sounds;

    @OnlyIn(Dist.CLIENT)
    public CarriageParticles particles;

    Vec3 derailParticleOffset;

    private Set<BlockPos> particleSlice = new HashSet();

    private float particleAvgY = 0.0F;

    double navDistanceTotal = 0.0;

    int hudPacketCooldown = 0;

    boolean stationMessage = false;

    @OnlyIn(Dist.CLIENT)
    private WeakReference<CarriageContraptionInstance> instanceHolder;

    public CarriageContraptionEntity(EntityType<?> type, Level world) {
        super(type, world);
        this.validForRender = false;
        this.firstPositionUpdate = true;
        this.arrivalSoundTicks = Integer.MIN_VALUE;
        this.derailParticleOffset = VecHelper.offsetRandomly(Vec3.ZERO, world.random, 1.5F).multiply(1.0, 0.25, 1.0);
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(CARRIAGE_DATA, new CarriageSyncData());
        this.f_19804_.define(TRACK_GRAPH, Optional.empty());
        this.f_19804_.define(SCHEDULED, false);
    }

    public void syncCarriage() {
        CarriageSyncData carriageData = this.getCarriageData();
        if (carriageData != null) {
            if (this.carriage != null) {
                carriageData.update(this, this.carriage);
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (this.m_9236_().isClientSide) {
            this.bindCarriage();
            if (TRACK_GRAPH.equals(key)) {
                this.updateTrackGraph();
            }
            if (CARRIAGE_DATA.equals(key)) {
                CarriageSyncData carriageData = this.getCarriageData();
                if (carriageData == null) {
                    return;
                }
                if (this.carriage == null) {
                    return;
                }
                carriageData.apply(this, this.carriage);
            }
        }
    }

    public CarriageSyncData getCarriageData() {
        return this.f_19804_.get(CARRIAGE_DATA);
    }

    public boolean hasSchedule() {
        return this.f_19804_.get(SCHEDULED);
    }

    public void setServerSidePrevPosition() {
        this.serverPrevPos = this.m_20182_();
    }

    @Override
    public Vec3 getPrevPositionVec() {
        return !this.m_9236_().isClientSide() && this.serverPrevPos != null ? this.serverPrevPos : super.getPrevPositionVec();
    }

    public boolean isLocalCoordWithin(BlockPos localPos, int min, int max) {
        if (!(this.getContraption() instanceof CarriageContraption cc)) {
            return false;
        } else {
            Direction facing = cc.getAssemblyDirection();
            Direction.Axis axis = facing.getClockWise().getAxis();
            int coord = axis.choose(localPos.m_123343_(), localPos.m_123342_(), localPos.m_123341_()) * -facing.getAxisDirection().getStep();
            return coord >= min && coord <= max;
        }
    }

    public static CarriageContraptionEntity create(Level world, CarriageContraption contraption) {
        CarriageContraptionEntity entity = new CarriageContraptionEntity((EntityType<?>) AllEntityTypes.CARRIAGE_CONTRAPTION.get(), world);
        entity.setContraption(contraption);
        entity.setInitialOrientation(contraption.getAssemblyDirection().getClockWise());
        entity.startAtInitialYaw();
        return entity;
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.contraption instanceof CarriageContraption cc) {
            for (Entity entity : this.m_20197_()) {
                if (!(entity instanceof Player)) {
                    BlockPos seatOf = cc.getSeatOf(entity.getUUID());
                    if (seatOf != null && cc.conductorSeats.get(seatOf) != null) {
                        this.alignPassenger(entity);
                    }
                }
            }
        }
    }

    @Override
    public void setBlock(BlockPos localPos, StructureTemplate.StructureBlockInfo newInfo) {
        if (this.carriage != null) {
            this.carriage.forEachPresentEntity(cce -> {
                cce.contraption.getBlocks().put(localPos, newInfo);
                AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> cce), new ContraptionBlockChangedPacket(cce.m_19879_(), localPos, newInfo.state()));
            });
        }
    }

    @Override
    protected void tickContraption() {
        if (this.nonDamageTicks > 0) {
            this.nonDamageTicks--;
        }
        if (this.contraption instanceof CarriageContraption cc) {
            if (this.carriage == null) {
                if (this.m_9236_().isClientSide) {
                    this.bindCarriage();
                } else {
                    this.m_146870_();
                }
            } else if (!Create.RAILWAYS.sided(this.m_9236_()).trains.containsKey(this.carriage.train.id)) {
                this.m_146870_();
            } else {
                this.tickActors();
                boolean isStalled = this.isStalled();
                this.carriage.stalled = isStalled;
                CarriageSyncData carriageData = this.getCarriageData();
                if (!this.m_9236_().isClientSide) {
                    this.f_19804_.set(SCHEDULED, this.carriage.train.runtime.getSchedule() != null);
                    boolean shouldCarriageSyncThisTick = this.carriage.train.shouldCarriageSyncThisTick(this.m_9236_().getGameTime(), this.m_6095_().updateInterval());
                    if (shouldCarriageSyncThisTick && carriageData.isDirty()) {
                        this.f_19804_.set(CARRIAGE_DATA, null);
                        this.f_19804_.set(CARRIAGE_DATA, carriageData);
                        carriageData.setDirty(false);
                    }
                    Navigation navigation = this.carriage.train.navigation;
                    if (navigation.announceArrival && Math.abs(navigation.distanceToDestination) < 60.0 && this.carriageIndex == (this.carriage.train.speed < 0.0 ? this.carriage.train.carriages.size() - 1 : 0)) {
                        navigation.announceArrival = false;
                        this.arrivalSoundPlaying = true;
                        this.arrivalSoundReversed = this.carriage.train.speed < 0.0;
                        this.arrivalSoundTicks = Integer.MIN_VALUE;
                    }
                    if (this.arrivalSoundPlaying) {
                        this.tickArrivalSound(cc);
                    }
                    this.f_19804_.set(TRACK_GRAPH, Optional.ofNullable(this.carriage.train.graph).map(g -> g.id));
                } else {
                    Carriage.DimensionalCarriageEntity dce = this.carriage.getDimensional(this.m_9236_());
                    if (this.f_19797_ % 10 == 0) {
                        this.updateTrackGraph();
                    }
                    if (dce.pointsInitialised) {
                        carriageData.approach(this, this.carriage, 1.0F / (float) this.m_6095_().updateInterval());
                        if (!this.carriage.train.derailed) {
                            this.carriage.updateContraptionAnchors();
                        }
                        this.f_19854_ = this.m_20185_();
                        this.f_19855_ = this.m_20186_();
                        this.f_19856_ = this.m_20189_();
                        dce.alignEntity(this);
                        if (this.sounds == null) {
                            this.sounds = new CarriageSounds(this);
                        }
                        this.sounds.tick(dce);
                        if (this.particles == null) {
                            this.particles = new CarriageParticles(this);
                        }
                        this.particles.tick(dce);
                        double distanceTo = 0.0;
                        if (!this.firstPositionUpdate) {
                            Vec3 diff = this.m_20182_().subtract(this.f_19854_, this.f_19855_, this.f_19856_);
                            Vec3 relativeDiff = VecHelper.rotate(diff, (double) this.yaw, Direction.Axis.Y);
                            double signum = Math.signum(-relativeDiff.x);
                            distanceTo = diff.length() * signum;
                            this.movingBackwards = signum < 0.0;
                        }
                        this.carriage.bogeys.getFirst().updateAngles(this, distanceTo);
                        if (this.carriage.isOnTwoBogeys()) {
                            this.carriage.bogeys.getSecond().updateAngles(this, distanceTo);
                        }
                        if (this.carriage.train.derailed) {
                            this.spawnDerailParticles(this.carriage);
                        }
                        if (dce.pivot != null) {
                            this.spawnPortalParticles(dce);
                        }
                        this.firstPositionUpdate = false;
                        this.validForRender = true;
                    }
                }
            }
        }
    }

    private void bindCarriage() {
        if (this.carriage == null) {
            Train train = (Train) Create.RAILWAYS.sided(this.m_9236_()).trains.get(this.trainId);
            if (train != null && train.carriages.size() > this.carriageIndex) {
                this.carriage = (Carriage) train.carriages.get(this.carriageIndex);
                if (this.carriage != null) {
                    Carriage.DimensionalCarriageEntity dimensional = this.carriage.getDimensional(this.m_9236_());
                    dimensional.entity = new WeakReference(this);
                    dimensional.pivot = null;
                    this.carriage.updateContraptionAnchors();
                    dimensional.updateRenderedCutoff();
                }
                this.updateTrackGraph();
            }
        }
    }

    private void tickArrivalSound(CarriageContraption cc) {
        List<Carriage> carriages = this.carriage.train.carriages;
        if (this.arrivalSoundTicks == Integer.MIN_VALUE) {
            int carriageCount = carriages.size();
            Integer tick = null;
            for (int index = 0; index < carriageCount; index++) {
                int i = this.arrivalSoundReversed ? carriageCount - 1 - index : index;
                Carriage carriage = (Carriage) carriages.get(i);
                CarriageContraptionEntity entity = (CarriageContraptionEntity) carriage.getDimensional(this.m_9236_()).entity.get();
                if (entity == null || !(entity.contraption instanceof CarriageContraption otherCC)) {
                    break;
                }
                tick = this.arrivalSoundReversed ? otherCC.soundQueue.lastTick() : otherCC.soundQueue.firstTick();
                if (tick != null) {
                    break;
                }
            }
            if (tick == null) {
                this.arrivalSoundPlaying = false;
                return;
            }
            this.arrivalSoundTicks = tick;
        }
        if (this.f_19797_ % 2 != 0) {
            boolean keepTicking = false;
            for (Carriage c : carriages) {
                CarriageContraptionEntity entityx = (CarriageContraptionEntity) c.getDimensional(this.m_9236_()).entity.get();
                if (entityx != null && entityx.contraption instanceof CarriageContraption otherCC) {
                    keepTicking |= otherCC.soundQueue.tick(entityx, this.arrivalSoundTicks, this.arrivalSoundReversed);
                }
            }
            if (!keepTicking) {
                this.arrivalSoundPlaying = false;
            } else {
                this.arrivalSoundTicks = this.arrivalSoundTicks + (this.arrivalSoundReversed ? -1 : 1);
            }
        }
    }

    @Override
    public void tickActors() {
        super.tickActors();
    }

    @Override
    protected boolean isActorActive(MovementContext context, MovementBehaviour actor) {
        if (this.contraption instanceof CarriageContraption cc) {
            return !super.isActorActive(context, actor) ? false : cc.notInPortal() || this.m_9236_().isClientSide();
        } else {
            return false;
        }
    }

    @Override
    protected void handleStallInformation(double x, double y, double z, float angle) {
    }

    private void spawnDerailParticles(Carriage carriage) {
        if (this.f_19796_.nextFloat() < 0.05F) {
            Vec3 v = this.m_20182_().add(this.derailParticleOffset);
            this.m_9236_().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, v.x, v.y, v.z, 0.0, 0.04, 0.0);
        }
    }

    @Override
    protected void addPassenger(Entity pPassenger) {
        super.m_20348_(pPassenger);
        if (pPassenger instanceof Player player) {
            player.getPersistentData().put("ContraptionMountLocation", VecHelper.writeNBT(player.m_20182_()));
        }
    }

    private void spawnPortalParticles(Carriage.DimensionalCarriageEntity dce) {
        Vec3 pivot = dce.pivot.getLocation().add(0.0, 1.5, 0.0);
        if (!this.particleSlice.isEmpty()) {
            boolean alongX = Mth.equal(pivot.x, (double) Math.round(pivot.x));
            int extraFlip = Direction.fromYRot((double) this.yaw).getAxisDirection().getStep();
            Vec3 emitter = pivot.add(0.0, (double) this.particleAvgY, 0.0);
            double speed = this.m_20182_().distanceTo(this.getPrevPositionVec());
            int size = (int) ((double) this.particleSlice.size() * Mth.clamp(4.0 - speed * 4.0, 0.0, 4.0));
            for (BlockPos pos : this.particleSlice) {
                if (size == 0 || this.f_19796_.nextInt(size) == 0) {
                    if (alongX) {
                        pos = new BlockPos(0, pos.m_123342_(), pos.m_123341_());
                    }
                    Vec3 v = pivot.add((double) (pos.m_123341_() * extraFlip), (double) pos.m_123342_(), (double) (pos.m_123343_() * extraFlip));
                    CubeParticleData data = new CubeParticleData(0.25F, 0.0F, 0.5F, 0.65F + (this.f_19796_.nextFloat() - 0.5F) * 0.25F, 4, false);
                    Vec3 m = v.subtract(emitter).normalize().scale(0.325F);
                    m = VecHelper.rotate(m, (double) (this.f_19796_.nextFloat() * 360.0F), alongX ? Direction.Axis.X : Direction.Axis.Z);
                    m = m.add(VecHelper.offsetRandomly(Vec3.ZERO, this.f_19796_, 0.25F));
                    this.m_9236_().addParticle(data, v.x, v.y, v.z, m.x, m.y, m.z);
                }
            }
        }
    }

    @Override
    public void onClientRemoval() {
        super.m_142036_();
        this.f_19804_.set(CARRIAGE_DATA, new CarriageSyncData());
        if (this.carriage != null) {
            Carriage.DimensionalCarriageEntity dce = this.carriage.getDimensional(this.m_9236_());
            dce.pointsInitialised = false;
            this.carriage.leadingBogey().couplingAnchors = Couple.create(null, null);
            this.carriage.trailingBogey().couplingAnchors = Couple.create(null, null);
        }
        this.firstPositionUpdate = true;
        if (this.sounds != null) {
            this.sounds.stop();
        }
    }

    @Override
    protected void writeAdditional(CompoundTag compound, boolean spawnPacket) {
        super.writeAdditional(compound, spawnPacket);
        compound.putUUID("TrainId", this.trainId);
        compound.putInt("CarriageIndex", this.carriageIndex);
    }

    @Override
    protected void readAdditional(CompoundTag compound, boolean spawnPacket) {
        super.readAdditional(compound, spawnPacket);
        this.trainId = compound.getUUID("TrainId");
        this.carriageIndex = compound.getInt("CarriageIndex");
        if (spawnPacket) {
            this.f_19790_ = this.m_20185_();
            this.f_19791_ = this.m_20186_();
            this.f_19792_ = this.m_20189_();
        }
    }

    @Override
    public Component getContraptionName() {
        return this.carriage != null ? this.carriage.train.name : super.getContraptionName();
    }

    public Couple<Boolean> checkConductors() {
        Couple<Boolean> sides = Couple.create(false, false);
        if (!(this.contraption instanceof CarriageContraption cc)) {
            return sides;
        } else {
            sides.setFirst(cc.blazeBurnerConductors.getFirst());
            sides.setSecond(cc.blazeBurnerConductors.getSecond());
            for (Entity entity : this.m_20197_()) {
                if (!(entity instanceof Player)) {
                    BlockPos seatOf = cc.getSeatOf(entity.getUUID());
                    if (seatOf != null) {
                        Couple<Boolean> validSides = (Couple<Boolean>) cc.conductorSeats.get(seatOf);
                        if (validSides != null) {
                            sides.setFirst(Boolean.valueOf(sides.getFirst() || validSides.getFirst()));
                            sides.setSecond(Boolean.valueOf(sides.getSecond() || validSides.getSecond()));
                        }
                    }
                }
            }
            return sides;
        }
    }

    @Override
    public boolean startControlling(BlockPos controlsLocalPos, Player player) {
        if (player == null || player.isSpectator()) {
            return false;
        } else if (this.carriage == null) {
            return false;
        } else if (this.carriage.train.derailed) {
            return false;
        } else {
            Train train = this.carriage.train;
            if (train.runtime.getSchedule() != null && !train.runtime.paused) {
                train.status.manualControls();
            }
            train.navigation.cancelNavigation();
            train.runtime.paused = true;
            train.navigation.waitingForSignal = null;
            return true;
        }
    }

    @Override
    public Component getDisplayName() {
        return (Component) (this.carriage == null ? Lang.translateDirect("train") : this.carriage.train.name);
    }

    @Override
    public boolean control(BlockPos controlsLocalPos, Collection<Integer> heldControls, Player player) {
        if (this.carriage == null) {
            return false;
        } else if (this.carriage.train.derailed) {
            return false;
        } else if (this.m_9236_().isClientSide) {
            return true;
        } else if (player.isSpectator()) {
            return false;
        } else if (!this.toGlobalVector(VecHelper.getCenterOf(controlsLocalPos), 1.0F).closerThan(player.m_20182_(), 8.0)) {
            return false;
        } else if (heldControls.contains(5)) {
            return false;
        } else {
            StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) this.contraption.getBlocks().get(controlsLocalPos);
            Direction initialOrientation = this.getInitialOrientation().getCounterClockWise();
            boolean inverted = false;
            if (info != null && info.state().m_61138_(ControlsBlock.f_54117_)) {
                inverted = !((Direction) info.state().m_61143_(ControlsBlock.f_54117_)).equals(initialOrientation);
            }
            if (this.hudPacketCooldown-- <= 0 && player instanceof ServerPlayer sp) {
                AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> sp), new TrainHUDUpdatePacket(this.carriage.train));
                this.hudPacketCooldown = 5;
            }
            int targetSpeed = 0;
            if (heldControls.contains(0)) {
                targetSpeed++;
            }
            if (heldControls.contains(1)) {
                targetSpeed--;
            }
            int targetSteer = 0;
            if (heldControls.contains(2)) {
                targetSteer++;
            }
            if (heldControls.contains(3)) {
                targetSteer--;
            }
            if (inverted) {
                targetSpeed *= -1;
                targetSteer *= -1;
            }
            if (targetSpeed != 0) {
                this.carriage.train.burnFuel();
            }
            boolean slow = inverted ^ targetSpeed < 0;
            boolean spaceDown = heldControls.contains(4);
            GlobalStation currentStation = this.carriage.train.getCurrentStation();
            if (currentStation != null && spaceDown) {
                this.sendPrompt(player, Lang.translateDirect("train.arrived_at", Components.literal(currentStation.name).withStyle(s -> s.withColor(7358000))), false);
                return true;
            } else {
                if (this.carriage.train.speedBeforeStall != null && targetSpeed != 0 && Math.signum(this.carriage.train.speedBeforeStall) != (double) Math.signum((float) targetSpeed)) {
                    this.carriage.train.cancelStall();
                }
                if (currentStation != null && targetSpeed != 0) {
                    this.stationMessage = false;
                    this.sendPrompt(player, Lang.translateDirect("train.departing_from", Components.literal(currentStation.name).withStyle(s -> s.withColor(7358000))), false);
                }
                if (currentStation == null) {
                    Navigation nav = this.carriage.train.navigation;
                    if (nav.destination != null) {
                        if (!spaceDown) {
                            nav.cancelNavigation();
                        }
                        if (spaceDown) {
                            double f = nav.distanceToDestination / this.navDistanceTotal;
                            int progress = (int) (Mth.clamp(1.0 - (1.0 - f) * (1.0 - f), 0.0, 1.0) * 30.0);
                            boolean arrived = progress == 0;
                            MutableComponent whiteComponent = Components.literal(Strings.repeat("|", progress));
                            MutableComponent greenComponent = Components.literal(Strings.repeat("|", 30 - progress));
                            int fromColor = 16761412;
                            int toColor = 5413141;
                            int mixedColor = Color.mixColors(toColor, fromColor, (float) progress / 30.0F);
                            int targetColor = arrived ? toColor : 5524805;
                            MutableComponent component = greenComponent.withStyle(st -> st.withColor(mixedColor)).append(whiteComponent.withStyle(st -> st.withColor(targetColor)));
                            this.sendPrompt(player, component, true);
                            this.carriage.train.manualTick = true;
                            return true;
                        }
                    }
                    double directedSpeed = targetSpeed != 0 ? (double) targetSpeed : this.carriage.train.speed;
                    GlobalStation lookAhead = nav.findNearestApproachable(!this.carriage.train.doubleEnded || (directedSpeed != 0.0 ? directedSpeed > 0.0 : !inverted));
                    if (lookAhead != null) {
                        if (spaceDown) {
                            this.carriage.train.manualTick = true;
                            nav.startNavigation(nav.findPathTo(lookAhead, -1.0));
                            this.carriage.train.manualTick = false;
                            this.navDistanceTotal = nav.distanceToDestination;
                            return true;
                        }
                        this.displayApproachStationMessage(player, lookAhead);
                    } else {
                        this.cleanUpApproachStationMessage(player);
                    }
                }
                this.carriage.train.manualSteer = targetSteer < 0 ? TravellingPoint.SteerDirection.RIGHT : (targetSteer > 0 ? TravellingPoint.SteerDirection.LEFT : TravellingPoint.SteerDirection.NONE);
                double topSpeed = (double) (this.carriage.train.maxSpeed() * AllConfigs.server().trains.manualTrainSpeedModifier.getF());
                double cappedTopSpeed = topSpeed * this.carriage.train.throttle;
                if (this.carriage.getLeadingPoint().edge != null && this.carriage.getLeadingPoint().edge.isTurn() || this.carriage.getTrailingPoint().edge != null && this.carriage.getTrailingPoint().edge.isTurn()) {
                    topSpeed = (double) this.carriage.train.maxTurnSpeed();
                }
                if (slow) {
                    topSpeed /= 4.0;
                }
                this.carriage.train.targetSpeed = Math.min(topSpeed, cappedTopSpeed) * (double) targetSpeed;
                boolean counteringAcceleration = Math.abs((double) Math.signum((float) targetSpeed) - Math.signum(this.carriage.train.speed)) > 1.5;
                if (slow && !counteringAcceleration) {
                    this.carriage.train.backwardsDriver = player;
                }
                this.carriage.train.manualTick = true;
                this.carriage.train.approachTargetSpeed(counteringAcceleration ? 2.0F : 1.0F);
                return true;
            }
        }
    }

    private void sendPrompt(Player player, MutableComponent component, boolean shadow) {
        if (player instanceof ServerPlayer sp) {
            AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> sp), new TrainPromptPacket(component, shadow));
        }
    }

    private void displayApproachStationMessage(Player player, GlobalStation station) {
        this.sendPrompt(player, Lang.translateDirect("contraption.controls.approach_station", Components.keybind("key.jump"), station.name), false);
        this.stationMessage = true;
    }

    private void cleanUpApproachStationMessage(Player player) {
        if (this.stationMessage) {
            player.displayClientMessage(Components.immutableEmpty(), true);
            this.stationMessage = false;
        }
    }

    private void updateTrackGraph() {
        if (this.carriage != null) {
            Optional<UUID> optional = this.f_19804_.get(TRACK_GRAPH);
            if (optional.isEmpty()) {
                this.carriage.train.graph = null;
                this.carriage.train.derailed = true;
            } else {
                TrackGraph graph = (TrackGraph) CreateClient.RAILWAYS.sided(this.m_9236_()).trackNetworks.get(optional.get());
                if (graph != null) {
                    this.carriage.train.graph = graph;
                    this.carriage.train.derailed = false;
                }
            }
        }
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    public Carriage getCarriage() {
        return this.carriage;
    }

    public void setCarriage(Carriage carriage) {
        this.carriage = carriage;
        this.trainId = carriage.train.id;
        this.carriageIndex = carriage.train.carriages.indexOf(carriage);
        if (this.contraption instanceof CarriageContraption cc) {
            cc.swapStorageAfterAssembly(this);
        }
        if (carriage.train.graph != null) {
            this.f_19804_.set(TRACK_GRAPH, Optional.of(carriage.train.graph.id));
        }
        Carriage.DimensionalCarriageEntity dimensional = carriage.getDimensional(this.m_9236_());
        dimensional.pivot = null;
        carriage.updateContraptionAnchors();
        dimensional.updateRenderedCutoff();
    }

    @OnlyIn(Dist.CLIENT)
    public void bindInstance(CarriageContraptionInstance instance) {
        this.instanceHolder = new WeakReference(instance);
        this.updateRenderedPortalCutoff();
    }

    @OnlyIn(Dist.CLIENT)
    public void updateRenderedPortalCutoff() {
        if (this.carriage != null) {
            this.particleSlice.clear();
            this.particleAvgY = 0.0F;
            if (this.contraption instanceof CarriageContraption cc) {
                Direction forward = cc.getAssemblyDirection().getClockWise();
                Direction.Axis axis = forward.getAxis();
                boolean x = axis == Direction.Axis.X;
                boolean flip = true;
                for (BlockPos pos : this.contraption.getBlocks().keySet()) {
                    if (cc.atSeam(pos)) {
                        int pX = x ? pos.m_123341_() : pos.m_123343_();
                        pX *= forward.getAxisDirection().getStep() * (flip ? 1 : -1);
                        pos = new BlockPos(pX, pos.m_123342_(), 0);
                        this.particleSlice.add(pos);
                        this.particleAvgY = this.particleAvgY + (float) pos.m_123342_();
                    }
                }
            }
            if (this.particleSlice.size() > 0) {
                this.particleAvgY = this.particleAvgY / (float) this.particleSlice.size();
            }
            if (this.instanceHolder != null) {
                CarriageContraptionInstance instance = (CarriageContraptionInstance) this.instanceHolder.get();
                if (instance != null) {
                    int bogeySpacing = this.carriage.bogeySpacing;
                    this.carriage.bogeys.forEachWithContext((bogey, first) -> {
                        if (bogey != null) {
                            BlockPos bogeyPos = bogey.isLeading ? BlockPos.ZERO : BlockPos.ZERO.relative(this.getInitialOrientation().getCounterClockWise(), bogeySpacing);
                            instance.setBogeyVisibility(first, !this.contraption.isHiddenInPortal(bogeyPos));
                        }
                    });
                }
            }
        }
    }
}