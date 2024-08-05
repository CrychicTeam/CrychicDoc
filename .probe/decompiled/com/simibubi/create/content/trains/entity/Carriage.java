package com.simibubi.create.content.trains.entity;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.minecart.TrainCargoManager;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackNodeLocation;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import org.apache.commons.lang3.mutable.MutableDouble;

public class Carriage {

    public static final AtomicInteger netIdGenerator = new AtomicInteger();

    public Train train;

    public int id;

    public boolean blocked;

    public boolean stalled;

    public Couple<Boolean> presentConductors;

    public int bogeySpacing;

    public Couple<CarriageBogey> bogeys;

    public TrainCargoManager storage;

    CompoundTag serialisedEntity;

    Map<Integer, CompoundTag> serialisedPassengers;

    private Map<ResourceKey<Level>, Carriage.DimensionalCarriageEntity> entities;

    static final int FIRST = 0;

    static final int MIDDLE = 1;

    static final int LAST = 2;

    static final int BOTH = 3;

    private Set<ResourceKey<Level>> currentlyTraversedDimensions = new HashSet();

    private TravellingPoint portalScout = new TravellingPoint();

    public Carriage(CarriageBogey bogey1, @Nullable CarriageBogey bogey2, int bogeySpacing) {
        this.bogeySpacing = bogeySpacing;
        this.bogeys = Couple.create(bogey1, bogey2);
        this.id = netIdGenerator.incrementAndGet();
        this.serialisedEntity = new CompoundTag();
        this.presentConductors = Couple.create(false, false);
        this.serialisedPassengers = new HashMap();
        this.entities = new HashMap();
        this.storage = new TrainCargoManager();
        bogey1.setLeading();
        bogey1.carriage = this;
        if (bogey2 != null) {
            bogey2.carriage = this;
        }
    }

    public boolean isOnIncompatibleTrack() {
        return this.leadingBogey().type.isOnIncompatibleTrack(this, true) || this.trailingBogey().type.isOnIncompatibleTrack(this, false);
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public boolean presentInMultipleDimensions() {
        return this.entities.size() > 1;
    }

    public void setContraption(Level level, CarriageContraption contraption) {
        this.storage = null;
        CarriageContraptionEntity entity = CarriageContraptionEntity.create(level, contraption);
        entity.setCarriage(this);
        contraption.startMoving(level);
        contraption.onEntityInitialize(level, entity);
        this.updateContraptionAnchors();
        Carriage.DimensionalCarriageEntity dimensional = this.getDimensional(level);
        dimensional.alignEntity(entity);
        dimensional.removeAndSaveEntity(entity, true);
    }

    public Carriage.DimensionalCarriageEntity getDimensional(Level level) {
        return this.getDimensional(level.dimension());
    }

    public Carriage.DimensionalCarriageEntity getDimensional(ResourceKey<Level> dimension) {
        return (Carriage.DimensionalCarriageEntity) this.entities.computeIfAbsent(dimension, $ -> new Carriage.DimensionalCarriageEntity());
    }

    @Nullable
    public Carriage.DimensionalCarriageEntity getDimensionalIfPresent(ResourceKey<Level> dimension) {
        return (Carriage.DimensionalCarriageEntity) this.entities.get(dimension);
    }

    public double travel(Level level, TrackGraph graph, double distance, TravellingPoint toFollowForward, TravellingPoint toFollowBackward, int type) {
        Function<TravellingPoint, TravellingPoint.ITrackSelector> forwardControl = toFollowForward == null ? this.train.navigation::control : mp -> mp.follow(toFollowForward);
        Function<TravellingPoint, TravellingPoint.ITrackSelector> backwardControl = toFollowBackward == null ? this.train.navigation::control : mp -> mp.follow(toFollowBackward);
        boolean onTwoBogeys = this.isOnTwoBogeys();
        double stress = this.train.derailed ? 0.0 : (onTwoBogeys ? (double) this.bogeySpacing - this.getAnchorDiff() : 0.0);
        this.blocked = false;
        MutableDouble distanceMoved = new MutableDouble(distance);
        boolean iterateFromBack = distance < 0.0;
        for (boolean firstBogey : Iterate.trueAndFalse) {
            if (firstBogey || onTwoBogeys) {
                boolean actuallyFirstBogey = !onTwoBogeys || firstBogey ^ iterateFromBack;
                CarriageBogey bogey = this.bogeys.get(actuallyFirstBogey);
                double bogeyCorrection = stress * (actuallyFirstBogey ? 0.5 : -0.5);
                double bogeyStress = bogey.getStress();
                for (boolean firstWheel : Iterate.trueAndFalse) {
                    boolean actuallyFirstWheel = firstWheel ^ iterateFromBack;
                    TravellingPoint point = bogey.points.get(actuallyFirstWheel);
                    TravellingPoint prevPoint = !actuallyFirstWheel ? bogey.points.getFirst() : (!actuallyFirstBogey && onTwoBogeys ? this.bogeys.getFirst().points.getSecond() : null);
                    TravellingPoint nextPoint = actuallyFirstWheel ? bogey.points.getSecond() : (actuallyFirstBogey && onTwoBogeys ? this.bogeys.getSecond().points.getFirst() : null);
                    double correction = bogeyStress * (actuallyFirstWheel ? 0.5 : -0.5);
                    double toMove = distanceMoved.getValue();
                    TravellingPoint.ITrackSelector frontTrackSelector = prevPoint == null ? (TravellingPoint.ITrackSelector) forwardControl.apply(point) : point.follow(prevPoint);
                    TravellingPoint.ITrackSelector backTrackSelector = nextPoint == null ? (TravellingPoint.ITrackSelector) backwardControl.apply(point) : point.follow(nextPoint);
                    boolean atFront = (type == 0 || type == 3) && actuallyFirstWheel && actuallyFirstBogey;
                    boolean atBack = (type == 2 || type == 3) && !actuallyFirstWheel && (!actuallyFirstBogey || !onTwoBogeys);
                    TravellingPoint.IEdgePointListener frontListener = this.train.frontSignalListener();
                    TravellingPoint.IEdgePointListener backListener = this.train.backSignalListener();
                    TravellingPoint.IEdgePointListener passiveListener = point.ignoreEdgePoints();
                    toMove += correction + bogeyCorrection;
                    TravellingPoint.ITrackSelector trackSelector = toMove > 0.0 ? frontTrackSelector : backTrackSelector;
                    TravellingPoint.IEdgePointListener signalListener = toMove > 0.0 ? (atFront ? frontListener : (atBack ? backListener : passiveListener)) : (atFront ? backListener : (atBack ? frontListener : passiveListener));
                    double moved = point.travel(graph, toMove, trackSelector, signalListener, point.ignoreTurns(), c -> {
                        for (Carriage.DimensionalCarriageEntity dce : this.entities.values()) {
                            if (c.either(tnl -> tnl.equalsIgnoreDim(dce.pivot))) {
                                return false;
                            }
                        }
                        if (this.entities.size() > 1) {
                            this.train.status.doublePortal();
                            return true;
                        } else {
                            return false;
                        }
                    });
                    this.blocked = this.blocked | point.blocked;
                    distanceMoved.setValue(moved);
                }
            }
        }
        this.updateContraptionAnchors();
        this.manageEntities(level);
        return distanceMoved.getValue();
    }

    public double getAnchorDiff() {
        double diff = 0.0;
        int entries = 0;
        TravellingPoint leadingPoint = this.getLeadingPoint();
        TravellingPoint trailingPoint = this.getTrailingPoint();
        if (leadingPoint.node1 != null && trailingPoint.node1 != null && !leadingPoint.node1.getLocation().dimension.equals(trailingPoint.node1.getLocation().dimension)) {
            return (double) this.bogeySpacing;
        } else {
            for (Carriage.DimensionalCarriageEntity dce : this.entities.values()) {
                if (dce.leadingAnchor() != null && dce.trailingAnchor() != null) {
                    entries++;
                    diff += dce.leadingAnchor().distanceTo(dce.trailingAnchor());
                }
            }
            return entries == 0 ? (double) this.bogeySpacing : diff / (double) entries;
        }
    }

    public void updateConductors() {
        if (this.anyAvailableEntity() != null && this.entities.size() <= 1 && this.serialisedPassengers.size() <= 0) {
            this.presentConductors.replace($ -> false);
            for (Carriage.DimensionalCarriageEntity dimensionalCarriageEntity : this.entities.values()) {
                CarriageContraptionEntity entity = (CarriageContraptionEntity) dimensionalCarriageEntity.entity.get();
                if (entity != null && entity.m_6084_()) {
                    this.presentConductors.replaceWithParams((current, checked) -> current || checked, entity.checkConductors());
                }
            }
        }
    }

    public void manageEntities(Level level) {
        this.currentlyTraversedDimensions.clear();
        this.bogeys.forEach(cb -> {
            if (cb != null) {
                cb.points.forEach(tp -> {
                    if (tp.node1 != null) {
                        this.currentlyTraversedDimensions.add(tp.node1.getLocation().dimension);
                    }
                });
            }
        });
        Iterator<Entry<ResourceKey<Level>, Carriage.DimensionalCarriageEntity>> iterator = this.entities.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<ResourceKey<Level>, Carriage.DimensionalCarriageEntity> entry = (Entry<ResourceKey<Level>, Carriage.DimensionalCarriageEntity>) iterator.next();
            boolean discard = !this.currentlyTraversedDimensions.isEmpty() && !this.currentlyTraversedDimensions.contains(entry.getKey());
            MinecraftServer server = level.getServer();
            if (server != null) {
                ServerLevel currentLevel = server.getLevel((ResourceKey<Level>) entry.getKey());
                if (currentLevel != null) {
                    Carriage.DimensionalCarriageEntity dimensionalCarriageEntity = (Carriage.DimensionalCarriageEntity) entry.getValue();
                    CarriageContraptionEntity entity = (CarriageContraptionEntity) dimensionalCarriageEntity.entity.get();
                    if (entity == null) {
                        if (discard) {
                            iterator.remove();
                        } else if (dimensionalCarriageEntity.positionAnchor != null && CarriageEntityHandler.isActiveChunk(currentLevel, BlockPos.containing(dimensionalCarriageEntity.positionAnchor))) {
                            dimensionalCarriageEntity.createEntity(currentLevel, this.anyAvailableEntity() == null);
                        }
                    } else {
                        if (discard) {
                            discard = dimensionalCarriageEntity.discardTicks > 3;
                            dimensionalCarriageEntity.discardTicks++;
                        } else {
                            dimensionalCarriageEntity.discardTicks = 0;
                        }
                        CarriageEntityHandler.validateCarriageEntity(entity);
                        if (!entity.m_6084_() || entity.leftTickingChunks || discard) {
                            dimensionalCarriageEntity.removeAndSaveEntity(entity, discard);
                            if (discard) {
                                iterator.remove();
                            }
                            continue;
                        }
                    }
                    entity = (CarriageContraptionEntity) dimensionalCarriageEntity.entity.get();
                    if (entity != null && dimensionalCarriageEntity.positionAnchor != null) {
                        dimensionalCarriageEntity.alignEntity(entity);
                        entity.syncCarriage();
                    }
                }
            }
        }
    }

    public void updateContraptionAnchors() {
        CarriageBogey leadingBogey = this.leadingBogey();
        if (!leadingBogey.points.either(t -> t.edge == null)) {
            CarriageBogey trailingBogey = this.trailingBogey();
            if (!trailingBogey.points.either(t -> t.edge == null)) {
                ResourceKey<Level> leadingBogeyDim = leadingBogey.getDimension();
                ResourceKey<Level> trailingBogeyDim = trailingBogey.getDimension();
                double leadingWheelSpacing = leadingBogey.type.getWheelPointSpacing();
                double trailingWheelSpacing = trailingBogey.type.getWheelPointSpacing();
                boolean leadingUpsideDown = leadingBogey.isUpsideDown();
                boolean trailingUpsideDown = trailingBogey.isUpsideDown();
                for (boolean leading : Iterate.trueAndFalse) {
                    TravellingPoint point = leading ? this.getLeadingPoint() : this.getTrailingPoint();
                    TravellingPoint otherPoint = !leading ? this.getLeadingPoint() : this.getTrailingPoint();
                    ResourceKey<Level> dimension = point.node1.getLocation().dimension;
                    ResourceKey<Level> otherDimension = otherPoint.node1.getLocation().dimension;
                    if (dimension.equals(otherDimension) && leading) {
                        this.getDimensional(dimension).discardPivot();
                    } else {
                        Carriage.DimensionalCarriageEntity dce = this.getDimensional(dimension);
                        dce.positionAnchor = dimension.equals(leadingBogeyDim) ? leadingBogey.getAnchorPosition() : this.pivoted(dce, dimension, point, leading ? leadingWheelSpacing / 2.0 : (double) this.bogeySpacing + trailingWheelSpacing / 2.0, leadingUpsideDown, trailingUpsideDown);
                        boolean backAnchorFlip = trailingBogey.isUpsideDown() ^ leadingBogey.isUpsideDown();
                        if (this.isOnTwoBogeys()) {
                            dce.rotationAnchors.setFirst(dimension.equals(leadingBogeyDim) ? leadingBogey.getAnchorPosition() : this.pivoted(dce, dimension, point, leading ? leadingWheelSpacing / 2.0 : (double) this.bogeySpacing + trailingWheelSpacing / 2.0, leadingUpsideDown, trailingUpsideDown));
                            dce.rotationAnchors.setSecond(dimension.equals(trailingBogeyDim) ? trailingBogey.getAnchorPosition(backAnchorFlip) : this.pivoted(dce, dimension, point, leading ? leadingWheelSpacing / 2.0 + (double) this.bogeySpacing : trailingWheelSpacing / 2.0, leadingUpsideDown, trailingUpsideDown));
                        } else if (dimension.equals(otherDimension)) {
                            dce.rotationAnchors = leadingBogey.points.map(tp -> tp.getPosition(this.train.graph));
                        } else {
                            dce.rotationAnchors.setFirst(leadingBogey.points.getFirst() == point ? point.getPosition(this.train.graph) : this.pivoted(dce, dimension, point, leadingWheelSpacing, leadingUpsideDown, trailingUpsideDown));
                            dce.rotationAnchors.setSecond(leadingBogey.points.getSecond() == point ? point.getPosition(this.train.graph) : this.pivoted(dce, dimension, point, leadingWheelSpacing, leadingUpsideDown, trailingUpsideDown));
                        }
                        int prevmin = dce.minAllowedLocalCoord();
                        int prevmax = dce.maxAllowedLocalCoord();
                        dce.updateCutoff(leading);
                        if (prevmin != dce.minAllowedLocalCoord() || prevmax != dce.maxAllowedLocalCoord()) {
                            dce.updateRenderedCutoff();
                            dce.updatePassengerLoadout();
                        }
                    }
                }
            }
        }
    }

    private Vec3 pivoted(Carriage.DimensionalCarriageEntity dce, ResourceKey<Level> dimension, TravellingPoint start, double offset, boolean leadingUpsideDown, boolean trailingUpsideDown) {
        if (this.train.graph == null) {
            return dce.pivot == null ? null : dce.pivot.getLocation();
        } else {
            TrackNodeLocation pivot = dce.findPivot(dimension, start == this.getLeadingPoint());
            if (pivot == null) {
                return null;
            } else {
                boolean flipped = start != this.getLeadingPoint() && leadingUpsideDown != trailingUpsideDown;
                Vec3 startVec = start.getPosition(this.train.graph, flipped);
                Vec3 portalVec = pivot.getLocation().add(0.0, leadingUpsideDown ? -1.0 : 1.0, 0.0);
                return VecHelper.lerp((float) (offset / startVec.distanceTo(portalVec)), startVec, portalVec);
            }
        }
    }

    public void alignEntity(Level level) {
        Carriage.DimensionalCarriageEntity dimensionalCarriageEntity = (Carriage.DimensionalCarriageEntity) this.entities.get(level.dimension());
        if (dimensionalCarriageEntity != null) {
            CarriageContraptionEntity entity = (CarriageContraptionEntity) dimensionalCarriageEntity.entity.get();
            if (entity != null) {
                dimensionalCarriageEntity.alignEntity(entity);
            }
        }
    }

    public TravellingPoint getLeadingPoint() {
        return this.leadingBogey().leading();
    }

    public TravellingPoint getTrailingPoint() {
        return this.trailingBogey().trailing();
    }

    public CarriageBogey leadingBogey() {
        return this.bogeys.getFirst();
    }

    public CarriageBogey trailingBogey() {
        return this.isOnTwoBogeys() ? this.bogeys.getSecond() : this.leadingBogey();
    }

    public boolean isOnTwoBogeys() {
        return this.bogeys.getSecond() != null;
    }

    public CarriageContraptionEntity anyAvailableEntity() {
        for (Carriage.DimensionalCarriageEntity dimensionalCarriageEntity : this.entities.values()) {
            CarriageContraptionEntity entity = (CarriageContraptionEntity) dimensionalCarriageEntity.entity.get();
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

    public void forEachPresentEntity(Consumer<CarriageContraptionEntity> callback) {
        for (Carriage.DimensionalCarriageEntity dimensionalCarriageEntity : this.entities.values()) {
            CarriageContraptionEntity entity = (CarriageContraptionEntity) dimensionalCarriageEntity.entity.get();
            if (entity != null) {
                callback.accept(entity);
            }
        }
    }

    public CompoundTag write(DimensionPalette dimensions) {
        CompoundTag tag = new CompoundTag();
        tag.put("FirstBogey", this.bogeys.getFirst().write(dimensions));
        if (this.isOnTwoBogeys()) {
            tag.put("SecondBogey", this.bogeys.getSecond().write(dimensions));
        }
        tag.putInt("Spacing", this.bogeySpacing);
        tag.putBoolean("FrontConductor", this.presentConductors.getFirst());
        tag.putBoolean("BackConductor", this.presentConductors.getSecond());
        tag.putBoolean("Stalled", this.stalled);
        Map<Integer, CompoundTag> passengerMap = new HashMap();
        for (Carriage.DimensionalCarriageEntity dimensionalCarriageEntity : this.entities.values()) {
            CarriageContraptionEntity entity = (CarriageContraptionEntity) dimensionalCarriageEntity.entity.get();
            if (entity != null) {
                this.serialize(entity);
                Contraption contraption = entity.getContraption();
                if (contraption != null) {
                    Map<UUID, Integer> mapping = contraption.getSeatMapping();
                    for (Entity passenger : entity.m_20197_()) {
                        if (mapping.containsKey(passenger.getUUID())) {
                            passengerMap.put((Integer) mapping.get(passenger.getUUID()), passenger.serializeNBT());
                        }
                    }
                }
            }
        }
        tag.put("Entity", this.serialisedEntity.copy());
        CompoundTag passengerTag = new CompoundTag();
        passengerMap.putAll(this.serialisedPassengers);
        passengerMap.forEach((seat, nbt) -> passengerTag.put("Seat" + seat, nbt.copy()));
        tag.put("Passengers", passengerTag);
        tag.put("EntityPositioning", NBTHelper.writeCompoundList(this.entities.entrySet(), e -> {
            CompoundTag c = ((Carriage.DimensionalCarriageEntity) e.getValue()).write();
            c.putInt("Dim", dimensions.encode((ResourceKey<Level>) e.getKey()));
            return c;
        }));
        return tag;
    }

    private void serialize(Entity entity) {
        this.serialisedEntity = entity.serializeNBT();
        this.serialisedEntity.remove("Passengers");
        this.serialisedEntity.getCompound("Contraption").remove("Passengers");
    }

    public static Carriage read(CompoundTag tag, TrackGraph graph, DimensionPalette dimensions) {
        CarriageBogey bogey1 = CarriageBogey.read(tag.getCompound("FirstBogey"), graph, dimensions);
        CarriageBogey bogey2 = tag.contains("SecondBogey") ? CarriageBogey.read(tag.getCompound("SecondBogey"), graph, dimensions) : null;
        Carriage carriage = new Carriage(bogey1, bogey2, tag.getInt("Spacing"));
        carriage.stalled = tag.getBoolean("Stalled");
        carriage.presentConductors = Couple.create(tag.getBoolean("FrontConductor"), tag.getBoolean("BackConductor"));
        carriage.serialisedEntity = tag.getCompound("Entity").copy();
        NBTHelper.iterateCompoundList(tag.getList("EntityPositioning", 10), c -> carriage.getDimensional(dimensions.decode(c.getInt("Dim"))).read(c));
        CompoundTag passengersTag = tag.getCompound("Passengers");
        passengersTag.getAllKeys().forEach(key -> carriage.serialisedPassengers.put(Integer.valueOf(key.substring(4)), passengersTag.getCompound(key)));
        return carriage;
    }

    public class DimensionalCarriageEntity {

        public Vec3 positionAnchor;

        public Couple<Vec3> rotationAnchors;

        public WeakReference<CarriageContraptionEntity> entity = new WeakReference(null);

        public TrackNodeLocation pivot;

        int discardTicks;

        public float cutoff;

        public boolean pointsInitialised;

        public DimensionalCarriageEntity() {
            this.rotationAnchors = Couple.create(null, null);
            this.pointsInitialised = false;
        }

        public void discardPivot() {
            int prevmin = this.minAllowedLocalCoord();
            int prevmax = this.maxAllowedLocalCoord();
            this.cutoff = 0.0F;
            this.pivot = null;
            if (!Carriage.this.serialisedPassengers.isEmpty() && this.entity.get() != null || prevmin != this.minAllowedLocalCoord() || prevmax != this.maxAllowedLocalCoord()) {
                this.updatePassengerLoadout();
                this.updateRenderedCutoff();
            }
        }

        public void updateCutoff(boolean leadingIsCurrent) {
            Vec3 leadingAnchor = this.rotationAnchors.getFirst();
            Vec3 trailingAnchor = this.rotationAnchors.getSecond();
            if (leadingAnchor != null && trailingAnchor != null) {
                if (this.pivot == null) {
                    this.cutoff = 0.0F;
                } else {
                    Vec3 pivotLoc = this.pivot.getLocation().add(0.0, 1.0, 0.0);
                    double leadingSpacing = Carriage.this.leadingBogey().type.getWheelPointSpacing() / 2.0;
                    double trailingSpacing = Carriage.this.trailingBogey().type.getWheelPointSpacing() / 2.0;
                    double anchorSpacing = leadingSpacing + (double) Carriage.this.bogeySpacing + trailingSpacing;
                    if (Carriage.this.isOnTwoBogeys()) {
                        Vec3 diff = trailingAnchor.subtract(leadingAnchor).normalize();
                        trailingAnchor = trailingAnchor.add(diff.scale(trailingSpacing));
                        leadingAnchor = leadingAnchor.add(diff.scale(-leadingSpacing));
                    }
                    double leadingDiff = leadingAnchor.distanceTo(pivotLoc);
                    double trailingDiff = trailingAnchor.distanceTo(pivotLoc);
                    leadingDiff /= anchorSpacing;
                    trailingDiff /= anchorSpacing;
                    if (leadingIsCurrent && leadingDiff > trailingDiff && leadingDiff > 1.0) {
                        this.cutoff = 0.0F;
                    } else if (leadingIsCurrent && leadingDiff < trailingDiff && trailingDiff > 1.0) {
                        this.cutoff = 1.0F;
                    } else if (!leadingIsCurrent && leadingDiff > trailingDiff && leadingDiff > 1.0) {
                        this.cutoff = -1.0F;
                    } else if (!leadingIsCurrent && leadingDiff < trailingDiff && trailingDiff > 1.0) {
                        this.cutoff = 0.0F;
                    } else {
                        this.cutoff = (float) Mth.clamp(1.0 - (leadingIsCurrent ? leadingDiff : trailingDiff), 0.0, 1.0) * (float) (leadingIsCurrent ? 1 : -1);
                    }
                }
            }
        }

        public TrackNodeLocation findPivot(ResourceKey<Level> dimension, boolean leading) {
            if (this.pivot != null) {
                return this.pivot;
            } else {
                TravellingPoint start = leading ? Carriage.this.getLeadingPoint() : Carriage.this.getTrailingPoint();
                TravellingPoint end = !leading ? Carriage.this.getLeadingPoint() : Carriage.this.getTrailingPoint();
                Carriage.this.portalScout.node1 = start.node1;
                Carriage.this.portalScout.node2 = start.node2;
                Carriage.this.portalScout.edge = start.edge;
                Carriage.this.portalScout.position = start.position;
                TravellingPoint.ITrackSelector trackSelector = Carriage.this.portalScout.follow(end);
                int distance = Carriage.this.bogeySpacing + 10;
                int direction = leading ? -1 : 1;
                Carriage.this.portalScout.travel(Carriage.this.train.graph, (double) (direction * distance), trackSelector, Carriage.this.portalScout.ignoreEdgePoints(), Carriage.this.portalScout.ignoreTurns(), nodes -> {
                    for (boolean b : Iterate.trueAndFalse) {
                        if (((TrackNodeLocation) nodes.get(b)).dimension.equals(dimension)) {
                            this.pivot = (TrackNodeLocation) nodes.get(b);
                        }
                    }
                    return true;
                });
                return this.pivot;
            }
        }

        public CompoundTag write() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat("Cutoff", this.cutoff);
            tag.putInt("DiscardTicks", this.discardTicks);
            Carriage.this.storage.write(tag, false);
            if (this.pivot != null) {
                tag.put("Pivot", this.pivot.write(null));
            }
            if (this.positionAnchor != null) {
                tag.put("PositionAnchor", VecHelper.writeNBT(this.positionAnchor));
            }
            if (this.rotationAnchors.both(Objects::nonNull)) {
                tag.put("RotationAnchors", this.rotationAnchors.serializeEach(VecHelper::writeNBTCompound));
            }
            return tag;
        }

        public void read(CompoundTag tag) {
            this.cutoff = tag.getFloat("Cutoff");
            this.discardTicks = tag.getInt("DiscardTicks");
            Carriage.this.storage.read(tag, null, false);
            if (tag.contains("Pivot")) {
                this.pivot = TrackNodeLocation.read(tag.getCompound("Pivot"), null);
            }
            if (this.positionAnchor == null) {
                if (tag.contains("PositionAnchor")) {
                    this.positionAnchor = VecHelper.readNBT(tag.getList("PositionAnchor", 6));
                }
                if (tag.contains("RotationAnchors")) {
                    this.rotationAnchors = Couple.deserializeEach(tag.getList("RotationAnchors", 10), VecHelper::readNBTCompound);
                }
            }
        }

        public Vec3 leadingAnchor() {
            return Carriage.this.isOnTwoBogeys() ? this.rotationAnchors.getFirst() : this.positionAnchor;
        }

        public Vec3 trailingAnchor() {
            return Carriage.this.isOnTwoBogeys() ? this.rotationAnchors.getSecond() : this.positionAnchor;
        }

        public int minAllowedLocalCoord() {
            if (this.cutoff <= 0.0F) {
                return Integer.MIN_VALUE;
            } else {
                return this.cutoff >= 1.0F ? Integer.MAX_VALUE : Mth.floor((float) (-Carriage.this.bogeySpacing + -1) + (float) (2 + Carriage.this.bogeySpacing) * this.cutoff);
            }
        }

        public int maxAllowedLocalCoord() {
            if (this.cutoff >= 0.0F) {
                return Integer.MAX_VALUE;
            } else {
                return this.cutoff <= -1.0F ? Integer.MIN_VALUE : Mth.ceil((float) (-Carriage.this.bogeySpacing + -1) + (float) (2 + Carriage.this.bogeySpacing) * (this.cutoff + 1.0F));
            }
        }

        public void updatePassengerLoadout() {
            Entity entity = (Entity) this.entity.get();
            if (entity instanceof CarriageContraptionEntity cce) {
                if (entity.level() instanceof ServerLevel sLevel) {
                    HashSet var16 = new HashSet();
                    int min = this.minAllowedLocalCoord();
                    int max = this.maxAllowedLocalCoord();
                    for (Entry<Integer, CompoundTag> entry : Carriage.this.serialisedPassengers.entrySet()) {
                        Integer seatId = (Integer) entry.getKey();
                        List<BlockPos> seats = cce.getContraption().getSeats();
                        if (seatId < seats.size()) {
                            BlockPos localPos = (BlockPos) seats.get(seatId);
                            if (cce.isLocalCoordWithin(localPos, min, max)) {
                                CompoundTag tag = (CompoundTag) entry.getValue();
                                Entity passenger = null;
                                if (tag.contains("PlayerPassenger")) {
                                    passenger = sLevel.getServer().getPlayerList().getPlayer(tag.getUUID("PlayerPassenger"));
                                } else {
                                    passenger = EntityType.loadEntityRecursive(tag, entity.level(), e -> {
                                        e.moveTo(this.positionAnchor);
                                        return e;
                                    });
                                    if (passenger != null) {
                                        sLevel.tryAddFreshEntityWithPassengers(passenger);
                                    }
                                }
                                if (passenger != null) {
                                    ResourceKey<Level> passengerDimension = passenger.level().dimension();
                                    if (!passengerDimension.equals(sLevel.m_46472_()) && passenger instanceof ServerPlayer sp) {
                                        continue;
                                    }
                                    cce.addSittingPassenger(passenger, seatId);
                                }
                                var16.add(seatId);
                            }
                        }
                    }
                    var16.forEach(Carriage.this.serialisedPassengers::remove);
                    Map<UUID, Integer> mapping = cce.getContraption().getSeatMapping();
                    for (Entity passengerx : entity.getPassengers()) {
                        BlockPos localPos = cce.getContraption().getSeatOf(passengerx.getUUID());
                        if (!cce.isLocalCoordWithin(localPos, min, max) && mapping.containsKey(passengerx.getUUID())) {
                            Integer seat = (Integer) mapping.get(passengerx.getUUID());
                            if (passengerx instanceof ServerPlayer sp) {
                                this.dismountPlayer(sLevel, sp, seat, true);
                            } else {
                                Carriage.this.serialisedPassengers.put(seat, passengerx.serializeNBT());
                                passengerx.discard();
                            }
                        }
                    }
                }
            }
        }

        private void dismountPlayer(ServerLevel sLevel, ServerPlayer sp, Integer seat, boolean capture) {
            if (!capture) {
                sp.stopRiding();
            } else {
                CompoundTag tag = new CompoundTag();
                tag.putUUID("PlayerPassenger", sp.m_20148_());
                Carriage.this.serialisedPassengers.put(seat, tag);
                sp.stopRiding();
                sp.getPersistentData().remove("ContraptionDismountLocation");
                for (Entry<ResourceKey<Level>, Carriage.DimensionalCarriageEntity> other : Carriage.this.entities.entrySet()) {
                    Carriage.DimensionalCarriageEntity otherDce = (Carriage.DimensionalCarriageEntity) other.getValue();
                    if (otherDce != this && !sp.m_9236_().dimension().equals(other.getKey())) {
                        Vec3 loc = otherDce.pivot == null ? otherDce.positionAnchor : otherDce.pivot.getLocation();
                        if (loc != null) {
                            ServerLevel level = sLevel.getServer().getLevel((ResourceKey<Level>) other.getKey());
                            sp.teleportTo(level, loc.x, loc.y, loc.z, sp.m_146908_(), sp.m_146909_());
                            sp.m_20091_();
                            AllAdvancements.TRAIN_PORTAL.awardTo(sp);
                        }
                    }
                }
            }
        }

        public void updateRenderedCutoff() {
            Entity entity = (Entity) this.entity.get();
            if (entity instanceof CarriageContraptionEntity cce) {
                if (cce.getContraption() instanceof CarriageContraption cc) {
                    cc.portalCutoffMin = this.minAllowedLocalCoord();
                    cc.portalCutoffMax = this.maxAllowedLocalCoord();
                    if (entity.level().isClientSide()) {
                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.invalidate(cce));
                    }
                }
            }
        }

        @OnlyIn(Dist.CLIENT)
        private void invalidate(CarriageContraptionEntity entity) {
            entity.getContraption().deferInvalidate = true;
            entity.updateRenderedPortalCutoff();
        }

        private void createEntity(Level level, boolean loadPassengers) {
            Entity entity = (Entity) EntityType.create(Carriage.this.serialisedEntity, level).orElse(null);
            if (entity instanceof CarriageContraptionEntity cce) {
                entity.moveTo(this.positionAnchor);
                this.entity = new WeakReference(cce);
                cce.setCarriage(Carriage.this);
                cce.syncCarriage();
                if (level instanceof ServerLevel sl) {
                    sl.addFreshEntity(entity);
                }
                this.updatePassengerLoadout();
            } else {
                Carriage.this.train.invalid = true;
            }
        }

        private void removeAndSaveEntity(CarriageContraptionEntity entity, boolean portal) {
            Contraption contraption = entity.getContraption();
            if (contraption != null) {
                Map<UUID, Integer> mapping = contraption.getSeatMapping();
                for (Entity passenger : entity.m_20197_()) {
                    if (mapping.containsKey(passenger.getUUID())) {
                        Integer seat = (Integer) mapping.get(passenger.getUUID());
                        if (passenger instanceof ServerPlayer sp) {
                            this.dismountPlayer(sp.serverLevel(), sp, seat, portal);
                        } else {
                            Carriage.this.serialisedPassengers.put(seat, passenger.serializeNBT());
                        }
                    }
                }
            }
            for (Entity passengerx : entity.m_20197_()) {
                if (!(passengerx instanceof Player)) {
                    passengerx.discard();
                }
            }
            Carriage.this.serialize(entity);
            entity.m_146870_();
            this.entity.clear();
        }

        public void alignEntity(CarriageContraptionEntity entity) {
            if (!this.rotationAnchors.either(Objects::isNull)) {
                Vec3 positionVec = this.rotationAnchors.getFirst();
                Vec3 coupledVec = this.rotationAnchors.getSecond();
                double diffX = positionVec.x - coupledVec.x;
                double diffY = positionVec.y - coupledVec.y;
                double diffZ = positionVec.z - coupledVec.z;
                entity.prevYaw = entity.yaw;
                entity.prevPitch = entity.pitch;
                if (!entity.m_9236_().isClientSide()) {
                    Vec3 lookahead = this.positionAnchor.add(this.positionAnchor.subtract(entity.m_20182_()).normalize().scale(16.0));
                    for (Entity e : entity.m_20197_()) {
                        if (e instanceof Player && !(e.distanceToSqr(entity) > 1024.0)) {
                            if (!CarriageEntityHandler.isActiveChunk(entity.m_9236_(), BlockPos.containing(lookahead))) {
                                Carriage.this.train.carriageWaitingForChunks = Carriage.this.id;
                                return;
                            }
                            break;
                        }
                    }
                    if (entity.m_20197_().stream().anyMatch(p -> p instanceof Player)) {
                    }
                    if (Carriage.this.train.carriageWaitingForChunks == Carriage.this.id) {
                        Carriage.this.train.carriageWaitingForChunks = -1;
                    }
                    entity.setServerSidePrevPosition();
                }
                entity.m_146884_(this.positionAnchor);
                entity.yaw = (float) (Mth.atan2(diffZ, diffX) * 180.0 / Math.PI) + 180.0F;
                entity.pitch = (float) (Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ)) * 180.0 / Math.PI) * -1.0F;
                if (entity.firstPositionUpdate) {
                    entity.f_19854_ = entity.m_20185_();
                    entity.f_19855_ = entity.m_20186_();
                    entity.f_19856_ = entity.m_20189_();
                    entity.prevYaw = entity.yaw;
                    entity.prevPitch = entity.pitch;
                }
            }
        }
    }
}