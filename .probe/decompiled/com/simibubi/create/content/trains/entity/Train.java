package com.simibubi.create.content.trains.entity;

import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.DiscoveredPath;
import com.simibubi.create.content.trains.graph.EdgeData;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackGraphLocation;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.observer.TrackObserver;
import com.simibubi.create.content.trains.schedule.ScheduleRuntime;
import com.simibubi.create.content.trains.signal.SignalBlock;
import com.simibubi.create.content.trains.signal.SignalBoundary;
import com.simibubi.create.content.trains.signal.SignalEdgeGroup;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;

public class Train {

    public double speed = 0.0;

    public double targetSpeed = 0.0;

    public Double speedBeforeStall = null;

    public int carriageWaitingForChunks = -1;

    public double throttle = 1.0;

    public boolean honk = false;

    public UUID id;

    @Nullable
    public UUID owner;

    public TrackGraph graph;

    public Navigation navigation;

    public ScheduleRuntime runtime;

    public TrainIconType icon;

    public Component name;

    public TrainStatus status;

    public boolean invalid;

    public TravellingPoint.SteerDirection manualSteer;

    public boolean manualTick;

    public UUID currentStation;

    public boolean currentlyBackwards;

    public boolean doubleEnded;

    public List<Carriage> carriages;

    public List<Integer> carriageSpacing;

    public boolean updateSignalBlocks;

    public Map<UUID, UUID> occupiedSignalBlocks;

    public Set<UUID> reservedSignalBlocks;

    public Set<UUID> occupiedObservers;

    public Map<UUID, Pair<Integer, Boolean>> cachedObserverFiltering;

    List<TrainMigration> migratingPoints;

    public int migrationCooldown;

    public boolean derailed;

    public int fuelTicks;

    public int honkTicks;

    public Boolean lowHonk;

    public int honkPitch;

    public float accumulatedSteamRelease;

    int tickOffset;

    double[] stress;

    public Player backwardsDriver;

    public Train(UUID id, UUID owner, TrackGraph graph, List<Carriage> carriages, List<Integer> carriageSpacing, boolean doubleEnded) {
        this.id = id;
        this.owner = owner;
        this.graph = graph;
        this.carriages = carriages;
        this.carriageSpacing = carriageSpacing;
        this.icon = TrainIconType.getDefault();
        this.stress = new double[carriageSpacing.size()];
        this.name = Lang.translateDirect("train.unnamed");
        this.status = new TrainStatus(this);
        this.doubleEnded = doubleEnded;
        carriages.forEach(c -> c.setTrain(this));
        this.navigation = new Navigation(this);
        this.runtime = new ScheduleRuntime(this);
        this.migratingPoints = new ArrayList();
        this.currentStation = null;
        this.manualSteer = TravellingPoint.SteerDirection.NONE;
        this.occupiedSignalBlocks = new HashMap();
        this.reservedSignalBlocks = new HashSet();
        this.occupiedObservers = new HashSet();
        this.cachedObserverFiltering = new HashMap();
        this.tickOffset = Create.RANDOM.nextInt(100);
    }

    public void earlyTick(Level level) {
        this.status.tick(level);
        if (this.graph == null && !this.migratingPoints.isEmpty()) {
            this.reattachToTracks(level);
        }
        if (this.graph == null) {
            this.addToSignalGroups(this.occupiedSignalBlocks.keySet());
        } else {
            if (this.updateSignalBlocks) {
                this.updateSignalBlocks = false;
                this.collectInitiallyOccupiedSignalBlocks();
            }
            this.addToSignalGroups(this.occupiedSignalBlocks.keySet());
            this.addToSignalGroups(this.reservedSignalBlocks);
            if (!this.occupiedObservers.isEmpty()) {
                this.tickOccupiedObservers(level);
            }
        }
    }

    private void tickOccupiedObservers(Level level) {
        int storageVersion = 0;
        for (Carriage carriage : this.carriages) {
            storageVersion += carriage.storage.getVersion();
        }
        for (UUID uuid : this.occupiedObservers) {
            TrackObserver observer = this.graph.getPoint(EdgePointType.OBSERVER, uuid);
            if (observer != null) {
                FilterItemStack filter = observer.getFilter();
                if (filter.isEmpty()) {
                    observer.keepAlive(this);
                } else {
                    Pair<Integer, Boolean> cachedMatch = (Pair<Integer, Boolean>) this.cachedObserverFiltering.computeIfAbsent(uuid, $ -> Pair.of(-1, false));
                    boolean shouldActivate = cachedMatch.getSecond();
                    if (cachedMatch.getFirst() == storageVersion) {
                        if (shouldActivate) {
                            observer.keepAlive(this);
                        }
                    } else {
                        shouldActivate = false;
                        Iterator var9 = this.carriages.iterator();
                        label81: while (true) {
                            if (var9.hasNext()) {
                                Carriage carriage = (Carriage) var9.next();
                                if (!shouldActivate) {
                                    IItemHandlerModifiable inv = carriage.storage.getItems();
                                    if (inv != null) {
                                        for (int slot = 0; slot < inv.getSlots() && !shouldActivate; slot++) {
                                            ItemStack extractItem = inv.extractItem(slot, 1, true);
                                            if (!extractItem.isEmpty()) {
                                                shouldActivate |= filter.test(level, extractItem);
                                            }
                                        }
                                    }
                                    IFluidHandler tank = carriage.storage.getFluids();
                                    if (tank == null) {
                                        continue;
                                    }
                                    int slotx = 0;
                                    while (true) {
                                        if (slotx >= tank.getTanks() || shouldActivate) {
                                            continue label81;
                                        }
                                        FluidStack drain = tank.drain(1, IFluidHandler.FluidAction.SIMULATE);
                                        if (!drain.isEmpty()) {
                                            shouldActivate |= filter.test(level, drain);
                                        }
                                        slotx++;
                                    }
                                }
                            }
                            this.cachedObserverFiltering.put(uuid, Pair.of(storageVersion, shouldActivate));
                            if (shouldActivate) {
                                observer.keepAlive(this);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private void addToSignalGroups(Collection<UUID> groups) {
        Map<UUID, SignalEdgeGroup> groupMap = Create.RAILWAYS.signalEdgeGroups;
        Iterator<UUID> iterator = groups.iterator();
        while (iterator.hasNext()) {
            SignalEdgeGroup signalEdgeGroup = (SignalEdgeGroup) groupMap.get(iterator.next());
            if (signalEdgeGroup == null) {
                iterator.remove();
            } else {
                signalEdgeGroup.trains.add(this);
            }
        }
    }

    public void tick(Level level) {
        Create.RAILWAYS.markTracksDirty();
        if (this.graph == null) {
            this.carriages.forEach(c -> c.manageEntities(level));
            this.updateConductors();
        } else {
            this.updateConductors();
            this.runtime.tick(level);
            this.navigation.tick(level);
            this.tickPassiveSlowdown();
            if (this.derailed) {
                this.tickDerailedSlowdown();
            }
            double distance = this.speed;
            Carriage previousCarriage = null;
            int carriageCount = this.carriages.size();
            boolean stalled = false;
            double maxStress = 0.0;
            if (this.carriageWaitingForChunks != -1) {
                distance = 0.0;
            }
            for (int i = 0; i < carriageCount; i++) {
                Carriage carriage = (Carriage) this.carriages.get(i);
                if (previousCarriage != null) {
                    int target = (Integer) this.carriageSpacing.get(i - 1);
                    double actual = (double) target;
                    TravellingPoint leadingPoint = carriage.getLeadingPoint();
                    TravellingPoint trailingPoint = previousCarriage.getTrailingPoint();
                    int entries = 0;
                    double total = 0.0;
                    if (leadingPoint.node1 != null && trailingPoint.node1 != null) {
                        ResourceKey<Level> d1 = leadingPoint.node1.getLocation().dimension;
                        ResourceKey<Level> d2 = trailingPoint.node1.getLocation().dimension;
                        for (boolean b : Iterate.trueAndFalse) {
                            ResourceKey<Level> d = b ? d1 : d2;
                            if ((b || !d1.equals(d2)) && d1.equals(d2)) {
                                Carriage.DimensionalCarriageEntity dimensional = carriage.getDimensionalIfPresent(d);
                                Carriage.DimensionalCarriageEntity dimensional2 = previousCarriage.getDimensionalIfPresent(d);
                                if (dimensional != null && dimensional2 != null) {
                                    Vec3 leadingAnchor = dimensional.leadingAnchor();
                                    Vec3 trailingAnchor = dimensional2.trailingAnchor();
                                    if (leadingAnchor != null && trailingAnchor != null) {
                                        double distanceTo = leadingAnchor.distanceToSqr(trailingAnchor);
                                        if (carriage.leadingBogey().isUpsideDown() != previousCarriage.trailingBogey().isUpsideDown()) {
                                            distanceTo = Math.sqrt(distanceTo - 4.0);
                                        } else {
                                            distanceTo = Math.sqrt(distanceTo);
                                        }
                                        total += distanceTo;
                                        entries++;
                                    }
                                }
                            }
                        }
                    }
                    if (entries > 0) {
                        actual = total / (double) entries;
                    }
                    this.stress[i - 1] = (double) target - actual;
                    maxStress = Math.max(maxStress, Math.abs((double) target - actual));
                }
                previousCarriage = carriage;
                if (carriage.stalled) {
                    if (this.speedBeforeStall == null) {
                        this.speedBeforeStall = this.speed;
                    }
                    distance = 0.0;
                    this.speed = 0.0;
                    stalled = true;
                }
            }
            if (!stalled && this.speedBeforeStall != null) {
                this.speed = Mth.clamp(this.speedBeforeStall, -1.0, 1.0);
                this.speedBeforeStall = null;
            }
            boolean approachingStation = this.navigation.distanceToDestination < 5.0;
            double leadingModifier = approachingStation ? 0.75 : 0.5;
            double trailingModifier = approachingStation ? 0.0 : 0.125;
            boolean blocked = false;
            boolean iterateFromBack = this.speed < 0.0;
            for (int index = 0; index < carriageCount; index++) {
                int i = iterateFromBack ? carriageCount - 1 - index : index;
                double leadingStress = i == 0 ? 0.0 : this.stress[i - 1] * -(iterateFromBack ? trailingModifier : leadingModifier);
                double trailingStress = i == this.stress.length ? 0.0 : this.stress[i] * (iterateFromBack ? leadingModifier : trailingModifier);
                Carriage carriagex = (Carriage) this.carriages.get(i);
                TravellingPoint toFollowForward = i == 0 ? null : ((Carriage) this.carriages.get(i - 1)).getTrailingPoint();
                TravellingPoint toFollowBackward = i == carriageCount - 1 ? null : ((Carriage) this.carriages.get(i + 1)).getLeadingPoint();
                double totalStress = this.derailed ? 0.0 : leadingStress + trailingStress;
                boolean first = i == 0;
                boolean last = i == carriageCount - 1;
                int carriageType = first ? (last ? 3 : 0) : (last ? 2 : 1);
                double actualDistance = carriagex.travel(level, this.graph, distance + totalStress, toFollowForward, toFollowBackward, carriageType);
                blocked |= carriagex.blocked || carriagex.isOnIncompatibleTrack();
                boolean onTwoBogeys = carriagex.isOnTwoBogeys();
                maxStress = Math.max(maxStress, onTwoBogeys ? (double) carriagex.bogeySpacing - carriagex.getAnchorDiff() : 0.0);
                maxStress = Math.max(maxStress, carriagex.leadingBogey().getStress());
                if (onTwoBogeys) {
                    maxStress = Math.max(maxStress, carriagex.trailingBogey().getStress());
                }
                if (index == 0) {
                    distance = actualDistance;
                    this.collideWithOtherTrains(level, carriagex);
                    this.backwardsDriver = null;
                    if (this.graph == null) {
                        return;
                    }
                }
            }
            if (blocked) {
                this.speed = 0.0;
                this.navigation.cancelNavigation();
                this.runtime.tick(level);
                this.status.endOfTrack();
            } else if (maxStress > 4.0) {
                this.speed = 0.0;
                this.navigation.cancelNavigation();
                this.runtime.tick(level);
                this.derailed = true;
                this.status.highStress();
            } else if (this.speed != 0.0) {
                this.status.trackOK();
            }
            this.updateNavigationTarget(distance);
        }
    }

    public TravellingPoint.IEdgePointListener frontSignalListener() {
        return (distance, couple) -> {
            if (couple.getFirst() instanceof GlobalStation station) {
                if (station.canApproachFrom((TrackNode) ((Couple) couple.getSecond()).getSecond()) && this.navigation.destination == station) {
                    this.speed = 0.0;
                    this.navigation.distanceToDestination = 0.0;
                    this.navigation.currentPath.clear();
                    this.arriveAt(this.navigation.destination);
                    this.navigation.destination = null;
                    return true;
                } else {
                    return false;
                }
            } else if (couple.getFirst() instanceof TrackObserver observer) {
                this.occupiedObservers.add(observer.getId());
                return false;
            } else if (!(couple.getFirst() instanceof SignalBoundary signal)) {
                return false;
            } else if (this.navigation.waitingForSignal != null && this.navigation.waitingForSignal.getFirst().equals(signal.getId())) {
                this.speed = 0.0;
                this.navigation.distanceToSignal = 0.0;
                return true;
            } else {
                UUID groupId = signal.getGroup((TrackNode) ((Couple) couple.getSecond()).getSecond());
                SignalEdgeGroup signalEdgeGroup = (SignalEdgeGroup) Create.RAILWAYS.signalEdgeGroups.get(groupId);
                if (signalEdgeGroup == null) {
                    return false;
                } else {
                    if ((this.runtime.getSchedule() == null || this.runtime.paused) && signalEdgeGroup.isOccupiedUnless(this)) {
                        this.carriages.forEach(c -> c.forEachPresentEntity(cce -> cce.getControllingPlayer().ifPresent(uuid -> AllAdvancements.RED_SIGNAL.awardTo(cce.m_9236_().m_46003_(uuid)))));
                    }
                    signalEdgeGroup.reserved = signal;
                    this.occupy(groupId, signal.id);
                    return false;
                }
            }
        };
    }

    public void cancelStall() {
        this.speedBeforeStall = null;
        this.carriages.forEach(c -> {
            c.stalled = false;
            c.forEachPresentEntity(cce -> cce.getContraption().getActors().forEach(pair -> {
                MovementBehaviour behaviour = AllMovementBehaviours.getBehaviour(((StructureTemplate.StructureBlockInfo) pair.getKey()).state());
                if (behaviour != null) {
                    behaviour.cancelStall((MovementContext) pair.getValue());
                }
            }));
        });
    }

    private boolean occupy(UUID groupId, @Nullable UUID boundaryId) {
        this.reservedSignalBlocks.remove(groupId);
        return boundaryId != null && this.occupiedSignalBlocks.containsKey(groupId) && boundaryId.equals(this.occupiedSignalBlocks.get(groupId)) ? false : this.occupiedSignalBlocks.put(groupId, boundaryId) == null;
    }

    public TravellingPoint.IEdgePointListener backSignalListener() {
        return (distance, couple) -> {
            if (couple.getFirst() instanceof TrackObserver observer) {
                this.occupiedObservers.remove(observer.getId());
                this.cachedObserverFiltering.remove(observer.getId());
                return false;
            } else if (couple.getFirst() instanceof SignalBoundary signal) {
                UUID var7 = signal.getGroup((TrackNode) ((Couple) couple.getSecond()).getFirst());
                this.occupiedSignalBlocks.remove(var7);
                return false;
            } else {
                return false;
            }
        };
    }

    private void updateNavigationTarget(double distance) {
        if (this.navigation.destination != null) {
            Pair<UUID, Boolean> blockingSignal = this.navigation.waitingForSignal;
            boolean fullRefresh = this.navigation.distanceToDestination > 100.0 && this.navigation.distanceToDestination % 100.0 > 20.0;
            boolean signalRefresh = blockingSignal != null && this.navigation.distanceToSignal % 50.0 > 5.0;
            boolean partialRefresh = this.navigation.distanceToDestination < 100.0 && this.navigation.distanceToDestination % 50.0 > 5.0;
            double toSubstract = this.navigation.destinationBehindTrain ? -distance : distance;
            boolean navigatingManually = this.runtime.paused;
            this.navigation.distanceToDestination -= toSubstract;
            if (blockingSignal != null) {
                this.navigation.distanceToSignal -= toSubstract;
                signalRefresh &= this.navigation.distanceToSignal % 50.0 < 5.0;
            }
            fullRefresh &= this.navigation.distanceToDestination % 100.0 <= 20.0;
            partialRefresh &= this.navigation.distanceToDestination % 50.0 <= 5.0;
            if (blockingSignal != null && this.navigation.ticksWaitingForSignal % 100 == 50) {
                SignalBoundary signal = this.graph.getPoint(EdgePointType.SIGNAL, blockingSignal.getFirst());
                fullRefresh |= signal != null && signal.types.get(blockingSignal.getSecond()) == SignalBlock.SignalType.CROSS_SIGNAL;
            }
            if (signalRefresh) {
                this.navigation.waitingForSignal = null;
            }
            if (fullRefresh || partialRefresh) {
                if (this.reservedSignalBlocks.isEmpty()) {
                    if (!navigatingManually && fullRefresh) {
                        DiscoveredPath preferredPath = this.runtime.startCurrentInstruction();
                        if (preferredPath != null) {
                            this.navigation.startNavigation(preferredPath);
                        }
                    }
                }
            }
        }
    }

    private void tickDerailedSlowdown() {
        this.speed /= 3.0;
        if (Mth.equal(this.speed, 0.0)) {
            this.speed = 0.0;
        }
    }

    private void tickPassiveSlowdown() {
        if (!this.manualTick && this.navigation.destination == null && this.speed != 0.0) {
            double acceleration = (double) this.acceleration();
            if (this.speed > 0.0) {
                this.speed = Math.max(this.speed - acceleration, 0.0);
            } else {
                this.speed = Math.min(this.speed + acceleration, 0.0);
            }
        }
        this.manualTick = false;
    }

    private void updateConductors() {
        for (Carriage carriage : this.carriages) {
            carriage.updateConductors();
        }
    }

    public boolean hasForwardConductor() {
        for (Carriage carriage : this.carriages) {
            if (carriage.presentConductors.getFirst()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasBackwardConductor() {
        for (Carriage carriage : this.carriages) {
            if (carriage.presentConductors.getSecond()) {
                return true;
            }
        }
        return false;
    }

    private void collideWithOtherTrains(Level level, Carriage carriage) {
        if (!this.derailed) {
            TravellingPoint trailingPoint = carriage.getTrailingPoint();
            TravellingPoint leadingPoint = carriage.getLeadingPoint();
            if (leadingPoint.node1 != null && trailingPoint.node1 != null) {
                ResourceKey<Level> dimension = leadingPoint.node1.getLocation().dimension;
                if (dimension.equals(trailingPoint.node1.getLocation().dimension)) {
                    Vec3 start = (this.speed < 0.0 ? trailingPoint : leadingPoint).getPosition(this.graph);
                    Vec3 end = (this.speed < 0.0 ? leadingPoint : trailingPoint).getPosition(this.graph);
                    Pair<Train, Vec3> collision = this.findCollidingTrain(level, start, end, dimension);
                    if (collision != null) {
                        Train train = collision.getFirst();
                        double combinedSpeed = Math.abs(this.speed) + Math.abs(train.speed);
                        if (combinedSpeed > 0.2F) {
                            Vec3 v = collision.getSecond();
                            level.explode(null, v.x, v.y, v.z, (float) Math.min(3.0 * combinedSpeed, 5.0), Level.ExplosionInteraction.NONE);
                        }
                        this.crash();
                        train.crash();
                    }
                }
            }
        }
    }

    public Pair<Train, Vec3> findCollidingTrain(Level level, Vec3 start, Vec3 end, ResourceKey<Level> dimension) {
        Vec3 diff = end.subtract(start);
        double maxDistanceSqr = Math.pow((double) AllConfigs.server().trains.maxAssemblyLength.get().intValue(), 2.0);
        label109: for (Train train : Create.RAILWAYS.sided(level).trains.values()) {
            if (train != this && (train.graph == null || train.graph == this.graph)) {
                Vec3 lastPoint = null;
                for (Carriage otherCarriage : train.carriages) {
                    for (boolean betweenBits : Iterate.trueAndFalse) {
                        if (!betweenBits || lastPoint != null) {
                            TravellingPoint otherLeading = otherCarriage.getLeadingPoint();
                            TravellingPoint otherTrailing = otherCarriage.getTrailingPoint();
                            if (otherLeading.edge != null && otherTrailing.edge != null) {
                                ResourceKey<Level> otherDimension = otherLeading.node1.getLocation().dimension;
                                if (otherDimension.equals(otherTrailing.node1.getLocation().dimension) && otherDimension.equals(dimension)) {
                                    Vec3 start2 = otherLeading.getPosition(train.graph);
                                    Vec3 end2 = otherTrailing.getPosition(train.graph);
                                    if (Math.min(start2.distanceToSqr(start), end2.distanceToSqr(start)) > maxDistanceSqr) {
                                        continue label109;
                                    }
                                    if (betweenBits) {
                                        end2 = start2;
                                        start2 = lastPoint;
                                    }
                                    lastPoint = end2;
                                    if (!(end.y < end2.y - 3.0) && !(end2.y < end.y - 3.0) || !(start.y < start2.y - 3.0) && !(start2.y < start.y - 3.0)) {
                                        Vec3 diff2 = end2.subtract(start2);
                                        Vec3 normedDiff = diff.normalize();
                                        Vec3 normedDiff2 = diff2.normalize();
                                        double[] intersect = VecHelper.intersect(start, start2, normedDiff, normedDiff2, Direction.Axis.Y);
                                        if (intersect == null) {
                                            Vec3 intersectSphere = VecHelper.intersectSphere(start2, normedDiff2, start, 0.125);
                                            if (intersectSphere == null || !Mth.equal(normedDiff2.dot(intersectSphere.subtract(start2).normalize()), 1.0)) {
                                                continue;
                                            }
                                            intersect = new double[] { intersectSphere.distanceTo(start) - 0.125, intersectSphere.distanceTo(start2) - 0.125 };
                                        }
                                        if (!(intersect[0] > diff.length()) && !(intersect[1] > diff2.length()) && !(intersect[0] < 0.0) && !(intersect[1] < 0.0)) {
                                            return Pair.of(train, start.add(normedDiff.scale(intersect[0])));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void crash() {
        this.navigation.cancelNavigation();
        if (!this.derailed) {
            this.speed = -Mth.clamp(this.speed, -0.5, 0.5);
            this.derailed = true;
            this.graph = null;
            this.status.crash();
            for (Carriage carriage : this.carriages) {
                carriage.forEachPresentEntity(e -> e.m_146897_().forEach(entity -> {
                    if (entity instanceof Player p) {
                        Optional<UUID> controllingPlayer = e.getControllingPlayer();
                        if (!controllingPlayer.isPresent() || !((UUID) controllingPlayer.get()).equals(p.m_20148_())) {
                            AllAdvancements.TRAIN_CRASH.awardTo(p);
                        }
                    }
                }));
            }
            if (this.backwardsDriver != null) {
                AllAdvancements.TRAIN_CRASH_BACKWARDS.awardTo(this.backwardsDriver);
            }
        }
    }

    public boolean disassemble(Direction assemblyDirection, BlockPos pos) {
        if (!this.canDisassemble()) {
            return false;
        } else {
            int offset = 1;
            boolean backwards = this.currentlyBackwards;
            Level level = null;
            for (int i = 0; i < this.carriages.size(); i++) {
                Carriage carriage = (Carriage) this.carriages.get(backwards ? this.carriages.size() - i - 1 : i);
                CarriageContraptionEntity entity = carriage.anyAvailableEntity();
                if (entity == null) {
                    return false;
                }
                level = entity.m_9236_();
                if (entity.getContraption() instanceof CarriageContraption cc) {
                    cc.returnStorageForDisassembly(carriage.storage);
                }
                entity.m_146884_(Vec3.atLowerCornerOf(pos.relative(assemblyDirection, backwards ? offset + carriage.bogeySpacing : offset).below(carriage.leadingBogey().isUpsideDown() ? 2 : 0)));
                entity.disassemble();
                for (CarriageBogey bogey : carriage.bogeys) {
                    if (bogey != null) {
                        Vec3 bogeyPosition = bogey.getAnchorPosition();
                        if (bogeyPosition != null && level.getBlockEntity(BlockPos.containing(bogeyPosition)) instanceof AbstractBogeyBlockEntity sbbe) {
                            sbbe.setBogeyData(bogey.bogeyData);
                        }
                    }
                }
                offset += carriage.bogeySpacing;
                if (i < this.carriageSpacing.size()) {
                    offset += this.carriageSpacing.get(backwards ? this.carriageSpacing.size() - i - 1 : i);
                }
            }
            GlobalStation currentStation = this.getCurrentStation();
            if (currentStation != null) {
                currentStation.cancelReservation(this);
                BlockPos blockEntityPos = currentStation.getBlockEntityPos();
                if (level.getBlockEntity(blockEntityPos) instanceof StationBlockEntity sbe) {
                    sbe.lastDisassembledTrainName = this.name.copy();
                }
            }
            Create.RAILWAYS.removeTrain(this.id);
            AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new TrainPacket(this, false));
            return true;
        }
    }

    public boolean canDisassemble() {
        for (Carriage carriage : this.carriages) {
            if (carriage.presentInMultipleDimensions()) {
                return false;
            }
            CarriageContraptionEntity entity = carriage.anyAvailableEntity();
            if (entity == null) {
                return false;
            }
            if (!Mth.equal(entity.pitch, 0.0F)) {
                return false;
            }
            if (!Mth.equal((entity.yaw % 90.0F + 360.0F) % 90.0F, 0.0F)) {
                return false;
            }
        }
        return true;
    }

    public boolean isTravellingOn(TrackNode node) {
        MutableBoolean affected = new MutableBoolean(false);
        this.forEachTravellingPoint(tp -> {
            if (tp.node1 == node || tp.node2 == node) {
                affected.setTrue();
            }
        });
        return affected.booleanValue();
    }

    public void detachFromTracks() {
        this.migratingPoints.clear();
        this.navigation.cancelNavigation();
        this.forEachTravellingPoint(tp -> this.migratingPoints.add(new TrainMigration(tp)));
        this.graph = null;
    }

    public void forEachTravellingPoint(Consumer<TravellingPoint> callback) {
        for (Carriage c : this.carriages) {
            c.leadingBogey().points.forEach(callback::accept);
            if (c.isOnTwoBogeys()) {
                c.trailingBogey().points.forEach(callback::accept);
            }
        }
    }

    public void forEachTravellingPointBackwards(BiConsumer<TravellingPoint, Double> callback) {
        double lastWheelOffset = 0.0;
        for (int i = 0; i < this.carriages.size(); i++) {
            int index = this.carriages.size() - i - 1;
            Carriage carriage = (Carriage) this.carriages.get(index);
            CarriageBogey trailingBogey = carriage.trailingBogey();
            double trailSpacing = trailingBogey.type.getWheelPointSpacing();
            callback.accept(trailingBogey.trailing(), i == 0 ? 0.0 : (double) ((Integer) this.carriageSpacing.get(index)).intValue() - lastWheelOffset - trailSpacing / 2.0);
            callback.accept(trailingBogey.leading(), trailSpacing);
            lastWheelOffset = trailSpacing / 2.0;
            if (carriage.isOnTwoBogeys()) {
                CarriageBogey leadingBogey = carriage.leadingBogey();
                double leadSpacing = carriage.leadingBogey().type.getWheelPointSpacing();
                callback.accept(leadingBogey.trailing(), (double) carriage.bogeySpacing - lastWheelOffset - leadSpacing / 2.0);
                callback.accept(trailingBogey.leading(), leadSpacing);
                lastWheelOffset = leadSpacing / 2.0;
            }
        }
    }

    public void reattachToTracks(Level level) {
        if (this.migrationCooldown > 0) {
            this.migrationCooldown--;
        } else {
            Set<Entry<UUID, TrackGraph>> entrySet = new HashSet(Create.RAILWAYS.trackNetworks.entrySet());
            Map<UUID, List<TrackGraphLocation>> successfulMigrations = new HashMap();
            for (TrainMigration md : this.migratingPoints) {
                Iterator<Entry<UUID, TrackGraph>> iterator = entrySet.iterator();
                while (iterator.hasNext()) {
                    Entry<UUID, TrackGraph> entry = (Entry<UUID, TrackGraph>) iterator.next();
                    TrackGraphLocation gl = md.tryMigratingTo((TrackGraph) entry.getValue());
                    if (gl == null) {
                        iterator.remove();
                    } else {
                        ((List) successfulMigrations.computeIfAbsent((UUID) entry.getKey(), uuid -> new ArrayList())).add(gl);
                    }
                }
            }
            if (entrySet.isEmpty()) {
                this.migrationCooldown = 40;
                this.status.failedMigration();
                this.derailed = true;
            } else {
                Iterator var9 = entrySet.iterator();
                if (var9.hasNext()) {
                    Entry<UUID, TrackGraph> entry = (Entry<UUID, TrackGraph>) var9.next();
                    this.graph = (TrackGraph) entry.getValue();
                    List<TrackGraphLocation> locations = (List<TrackGraphLocation>) successfulMigrations.get(entry.getKey());
                    this.forEachTravellingPoint(tp -> tp.migrateTo(locations));
                    this.migratingPoints.clear();
                    if (this.derailed) {
                        this.status.successfulMigration();
                    }
                    this.derailed = false;
                    if (this.runtime.getSchedule() != null && this.runtime.state == ScheduleRuntime.State.IN_TRANSIT) {
                        this.runtime.state = ScheduleRuntime.State.PRE_TRANSIT;
                    }
                    GlobalStation currentStation = this.getCurrentStation();
                    if (currentStation != null) {
                        currentStation.reserveFor(this);
                    }
                    this.updateSignalBlocks = true;
                    this.migrationCooldown = 0;
                }
            }
        }
    }

    public int getTotalLength() {
        int length = 0;
        for (int i = 0; i < this.carriages.size(); i++) {
            Carriage carriage = (Carriage) this.carriages.get(i);
            if (i == 0) {
                length = (int) ((double) length + carriage.leadingBogey().type.getWheelPointSpacing() / 2.0);
            }
            if (i == this.carriages.size() - 1) {
                length = (int) ((double) length + carriage.trailingBogey().type.getWheelPointSpacing() / 2.0);
            }
            length += carriage.bogeySpacing;
            if (i < this.carriageSpacing.size()) {
                length += this.carriageSpacing.get(i);
            }
        }
        return length;
    }

    public void leaveStation() {
        GlobalStation currentStation = this.getCurrentStation();
        if (currentStation != null) {
            currentStation.trainDeparted(this);
        }
        this.currentStation = null;
    }

    public void arriveAt(GlobalStation station) {
        this.setCurrentStation(station);
        this.reservedSignalBlocks.clear();
        this.runtime.destinationReached();
    }

    public void setCurrentStation(GlobalStation station) {
        this.currentStation = station.id;
    }

    public GlobalStation getCurrentStation() {
        if (this.currentStation == null) {
            return null;
        } else {
            return this.graph == null ? null : this.graph.getPoint(EdgePointType.STATION, this.currentStation);
        }
    }

    @Nullable
    public LivingEntity getOwner(Level level) {
        try {
            UUID uuid = this.owner;
            return uuid == null ? null : level.getServer().getPlayerList().getPlayer(uuid);
        } catch (IllegalArgumentException var3) {
            return null;
        }
    }

    public void approachTargetSpeed(float accelerationMod) {
        double actualTarget = this.targetSpeed;
        if (!Mth.equal(actualTarget, this.speed)) {
            if (this.manualTick) {
                this.leaveStation();
            }
            double acceleration = (double) this.acceleration();
            if (this.speed < actualTarget) {
                this.speed = Math.min(this.speed + acceleration * (double) accelerationMod, actualTarget);
            } else if (this.speed > actualTarget) {
                this.speed = Math.max(this.speed - acceleration * (double) accelerationMod, actualTarget);
            }
        }
    }

    public void collectInitiallyOccupiedSignalBlocks() {
        TravellingPoint trailingPoint = ((Carriage) this.carriages.get(this.carriages.size() - 1)).getTrailingPoint();
        TrackNode node1 = trailingPoint.node1;
        TrackNode node2 = trailingPoint.node2;
        TrackEdge edge = trailingPoint.edge;
        double position = trailingPoint.position;
        EdgeData signalData = edge.getEdgeData();
        this.occupiedSignalBlocks.clear();
        this.reservedSignalBlocks.clear();
        this.occupiedObservers.clear();
        this.cachedObserverFiltering.clear();
        TravellingPoint signalScout = new TravellingPoint(node1, node2, edge, position, false);
        Map<UUID, SignalEdgeGroup> allGroups = Create.RAILWAYS.signalEdgeGroups;
        MutableObject<UUID> prevGroup = new MutableObject(null);
        if (signalData.hasSignalBoundaries()) {
            SignalBoundary nextBoundary = signalData.next(EdgePointType.SIGNAL, position);
            if (nextBoundary == null) {
                double d = 0.0;
                SignalBoundary prev = null;
                SignalBoundary current = signalData.next(EdgePointType.SIGNAL, 0.0);
                while (current != null) {
                    prev = current;
                    d = current.getLocationOn(edge);
                    current = signalData.next(EdgePointType.SIGNAL, d);
                }
                if (prev != null) {
                    UUID group = prev.getGroup(node2);
                    if (Create.RAILWAYS.signalEdgeGroups.containsKey(group)) {
                        this.occupy(group, null);
                        prevGroup.setValue(group);
                    }
                }
            } else {
                UUID group = nextBoundary.getGroup(node1);
                if (Create.RAILWAYS.signalEdgeGroups.containsKey(group)) {
                    this.occupy(group, null);
                    prevGroup.setValue(group);
                }
            }
        } else {
            UUID groupId = signalData.getEffectiveEdgeGroupId(this.graph);
            if (allGroups.containsKey(groupId)) {
                this.occupy(groupId, null);
                prevGroup.setValue(groupId);
            }
        }
        this.forEachTravellingPointBackwards((tp, d) -> signalScout.travel(this.graph, d, signalScout.follow(tp), (distance, couple) -> {
            if (couple.getFirst() instanceof TrackObserver observer) {
                this.occupiedObservers.add(observer.getId());
                return false;
            } else if (couple.getFirst() instanceof SignalBoundary signal) {
                ((Couple) couple.getSecond()).map(signal::getGroup).forEach(id -> {
                    if (Create.RAILWAYS.signalEdgeGroups.containsKey(id)) {
                        if (!id.equals(prevGroup.getValue())) {
                            this.occupy(id, null);
                            prevGroup.setValue(id);
                        }
                    }
                });
                return false;
            } else {
                return false;
            }
        }, signalScout.ignoreTurns()));
    }

    public boolean shouldCarriageSyncThisTick(long gameTicks, int updateInterval) {
        return (gameTicks + (long) this.tickOffset) % (long) updateInterval == 0L;
    }

    public Couple<Couple<TrackNode>> getEndpointEdges() {
        return Couple.create(((Carriage) this.carriages.get(0)).getLeadingPoint(), ((Carriage) this.carriages.get(this.carriages.size() - 1)).getTrailingPoint()).map(tp -> Couple.create(tp.node1, tp.node2));
    }

    public int getNavigationPenalty() {
        if (this.manualTick) {
            return 200;
        } else if (this.runtime.getSchedule() == null || this.runtime.paused) {
            return 700;
        } else if (this.navigation.waitingForSignal != null && this.navigation.ticksWaitingForSignal > 0) {
            return 50 + Math.min(this.navigation.ticksWaitingForSignal / 20, 1000);
        } else {
            return (this.navigation.destination == null || !(this.navigation.distanceToDestination < 50.0)) && !(this.navigation.distanceToSignal < 20.0) ? 25 : 50;
        }
    }

    public void burnFuel() {
        if (this.fuelTicks > 0) {
            this.fuelTicks--;
        } else {
            boolean iterateFromBack = this.speed < 0.0;
            int carriageCount = this.carriages.size();
            for (int index = 0; index < carriageCount; index++) {
                int i = iterateFromBack ? carriageCount - 1 - index : index;
                Carriage carriage = (Carriage) this.carriages.get(i);
                IItemHandlerModifiable fuelItems = carriage.storage.getFuelItems();
                if (fuelItems != null) {
                    for (int slot = 0; slot < fuelItems.getSlots(); slot++) {
                        ItemStack stack = fuelItems.extractItem(slot, 1, true);
                        int burnTime = ForgeHooks.getBurnTime(stack, null);
                        if (burnTime > 0) {
                            stack = fuelItems.extractItem(slot, 1, false);
                            this.fuelTicks = this.fuelTicks + burnTime * stack.getCount();
                            ItemStack containerItem = stack.getCraftingRemainingItem();
                            if (!containerItem.isEmpty()) {
                                ItemHandlerHelper.insertItemStacked(fuelItems, containerItem, false);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public float maxSpeed() {
        return (this.fuelTicks > 0 ? AllConfigs.server().trains.poweredTrainTopSpeed.getF() : AllConfigs.server().trains.trainTopSpeed.getF()) / 20.0F;
    }

    public float maxTurnSpeed() {
        return (this.fuelTicks > 0 ? AllConfigs.server().trains.poweredTrainTurningTopSpeed.getF() : AllConfigs.server().trains.trainTurningTopSpeed.getF()) / 20.0F;
    }

    public float acceleration() {
        return (this.fuelTicks > 0 ? AllConfigs.server().trains.poweredTrainAcceleration.getF() : AllConfigs.server().trains.trainAcceleration.getF()) / 400.0F;
    }

    public CompoundTag write(DimensionPalette dimensions) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", this.id);
        if (this.owner != null) {
            tag.putUUID("Owner", this.owner);
        }
        if (this.graph != null) {
            tag.putUUID("Graph", this.graph.id);
        }
        tag.put("Carriages", NBTHelper.writeCompoundList(this.carriages, c -> c.write(dimensions)));
        tag.putIntArray("CarriageSpacing", this.carriageSpacing);
        tag.putBoolean("DoubleEnded", this.doubleEnded);
        tag.putDouble("Speed", this.speed);
        tag.putDouble("Throttle", this.throttle);
        if (this.speedBeforeStall != null) {
            tag.putDouble("SpeedBeforeStall", this.speedBeforeStall);
        }
        tag.putInt("Fuel", this.fuelTicks);
        tag.putDouble("TargetSpeed", this.targetSpeed);
        tag.putString("IconType", this.icon.id.toString());
        tag.putString("Name", Component.Serializer.toJson(this.name));
        if (this.currentStation != null) {
            tag.putUUID("Station", this.currentStation);
        }
        tag.putBoolean("Backwards", this.currentlyBackwards);
        tag.putBoolean("Derailed", this.derailed);
        tag.putBoolean("UpdateSignals", this.updateSignalBlocks);
        tag.put("SignalBlocks", NBTHelper.writeCompoundList(this.occupiedSignalBlocks.entrySet(), e -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putUUID("Id", (UUID) e.getKey());
            if (e.getValue() != null) {
                compoundTag.putUUID("Boundary", (UUID) e.getValue());
            }
            return compoundTag;
        }));
        tag.put("ReservedSignalBlocks", NBTHelper.writeCompoundList(this.reservedSignalBlocks, uid -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putUUID("Id", uid);
            return compoundTag;
        }));
        tag.put("OccupiedObservers", NBTHelper.writeCompoundList(this.occupiedObservers, uid -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putUUID("Id", uid);
            return compoundTag;
        }));
        tag.put("MigratingPoints", NBTHelper.writeCompoundList(this.migratingPoints, tm -> tm.write(dimensions)));
        tag.put("Runtime", this.runtime.write());
        tag.put("Navigation", this.navigation.write(dimensions));
        return tag;
    }

    public static Train read(CompoundTag tag, Map<UUID, TrackGraph> trackNetworks, DimensionPalette dimensions) {
        UUID id = tag.getUUID("Id");
        UUID owner = tag.contains("Owner") ? tag.getUUID("Owner") : null;
        UUID graphId = tag.contains("Graph") ? tag.getUUID("Graph") : null;
        TrackGraph graph = graphId == null ? null : (TrackGraph) trackNetworks.get(graphId);
        List<Carriage> carriages = new ArrayList();
        NBTHelper.iterateCompoundList(tag.getList("Carriages", 10), c -> carriages.add(Carriage.read(c, graph, dimensions)));
        List<Integer> carriageSpacing = new ArrayList();
        for (int i : tag.getIntArray("CarriageSpacing")) {
            carriageSpacing.add(i);
        }
        boolean doubleEnded = tag.getBoolean("DoubleEnded");
        Train train = new Train(id, owner, graph, carriages, carriageSpacing, doubleEnded);
        train.speed = tag.getDouble("Speed");
        train.throttle = tag.getDouble("Throttle");
        if (tag.contains("SpeedBeforeStall")) {
            train.speedBeforeStall = tag.getDouble("SpeedBeforeStall");
        }
        train.targetSpeed = tag.getDouble("TargetSpeed");
        train.icon = TrainIconType.byId(new ResourceLocation(tag.getString("IconType")));
        train.name = Component.Serializer.fromJson(tag.getString("Name"));
        train.currentStation = tag.contains("Station") ? tag.getUUID("Station") : null;
        train.currentlyBackwards = tag.getBoolean("Backwards");
        train.derailed = tag.getBoolean("Derailed");
        train.updateSignalBlocks = tag.getBoolean("UpdateSignals");
        train.fuelTicks = tag.getInt("Fuel");
        NBTHelper.iterateCompoundList(tag.getList("SignalBlocks", 10), c -> train.occupiedSignalBlocks.put(c.getUUID("Id"), c.contains("Boundary") ? c.getUUID("Boundary") : null));
        NBTHelper.iterateCompoundList(tag.getList("ReservedSignalBlocks", 10), c -> train.reservedSignalBlocks.add(c.getUUID("Id")));
        NBTHelper.iterateCompoundList(tag.getList("OccupiedObservers", 10), c -> train.occupiedObservers.add(c.getUUID("Id")));
        NBTHelper.iterateCompoundList(tag.getList("MigratingPoints", 10), c -> train.migratingPoints.add(TrainMigration.read(c, dimensions)));
        train.runtime.read(tag.getCompound("Runtime"));
        train.navigation.read(tag.getCompound("Navigation"), graph, dimensions);
        if (train.getCurrentStation() != null) {
            train.getCurrentStation().reserveFor(train);
        }
        return train;
    }

    public int countPlayerPassengers() {
        AtomicInteger count = new AtomicInteger();
        for (Carriage carriage : this.carriages) {
            carriage.forEachPresentEntity(e -> e.m_146897_().forEach(p -> {
                if (p instanceof Player) {
                    count.incrementAndGet();
                }
            }));
        }
        return count.intValue();
    }

    public void determineHonk(Level level) {
        if (this.lowHonk == null) {
            for (int index = 0; index < this.carriages.size(); index++) {
                Carriage carriage = (Carriage) this.carriages.get(index);
                Carriage.DimensionalCarriageEntity dimensional = carriage.getDimensionalIfPresent(level.dimension());
                if (dimensional == null) {
                    return;
                }
                CarriageContraptionEntity entity = (CarriageContraptionEntity) dimensional.entity.get();
                if (entity == null || !(entity.getContraption() instanceof CarriageContraption otherCC)) {
                    break;
                }
                Pair<Boolean, Integer> first = otherCC.soundQueue.getFirstWhistle(entity);
                if (first != null) {
                    this.lowHonk = first.getFirst();
                    this.honkPitch = first.getSecond();
                }
            }
        }
    }

    public float distanceToLocationSqr(Level level, Vec3 location) {
        float distance = Float.MAX_VALUE;
        for (Carriage carriage : this.carriages) {
            Carriage.DimensionalCarriageEntity dce = carriage.getDimensionalIfPresent(level.dimension());
            if (dce != null && dce.positionAnchor != null) {
                distance = Math.min(distance, (float) dce.positionAnchor.distanceToSqr(location));
            }
        }
        return distance;
    }

    public static class Penalties {

        static final int STATION = 50;

        static final int STATION_WITH_TRAIN = 300;

        static final int MANUAL_TRAIN = 200;

        static final int IDLE_TRAIN = 700;

        static final int ARRIVING_TRAIN = 50;

        static final int WAITING_TRAIN = 50;

        static final int ANY_TRAIN = 25;

        static final int RED_SIGNAL = 25;

        static final int REDSTONE_RED_SIGNAL = 400;
    }
}