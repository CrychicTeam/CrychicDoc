package com.simibubi.create.content.trains.entity;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.DiscoveredPath;
import com.simibubi.create.content.trains.graph.EdgeData;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.graph.TrackNodeLocation;
import com.simibubi.create.content.trains.signal.SignalBlock;
import com.simibubi.create.content.trains.signal.SignalBoundary;
import com.simibubi.create.content.trains.signal.SignalEdgeGroup;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import com.simibubi.create.content.trains.station.GlobalStation;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.TrackMaterial;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableObject;

public class Navigation {

    public Train train;

    public GlobalStation destination;

    public double distanceToDestination;

    public double distanceStartedAt;

    public boolean destinationBehindTrain;

    public boolean announceArrival;

    List<Couple<TrackNode>> currentPath;

    private TravellingPoint signalScout;

    public Pair<UUID, Boolean> waitingForSignal;

    private Map<UUID, Pair<SignalBoundary, Boolean>> waitingForChainedGroups;

    public double distanceToSignal;

    public int ticksWaitingForSignal;

    public Navigation(Train train) {
        this.train = train;
        this.currentPath = new ArrayList();
        this.signalScout = new TravellingPoint();
        this.waitingForChainedGroups = new HashMap();
    }

    public void tick(Level level) {
        if (this.destination != null) {
            if (!this.train.runtime.paused) {
                boolean frontDriver = this.train.hasForwardConductor();
                boolean backDriver = this.train.hasBackwardConductor();
                if (this.destinationBehindTrain && !backDriver) {
                    if (frontDriver) {
                        this.train.status.missingCorrectConductor();
                    } else {
                        this.train.status.missingConductor();
                    }
                    this.cancelNavigation();
                    return;
                }
                if (!this.destinationBehindTrain && !frontDriver) {
                    this.train.status.missingConductor();
                    this.cancelNavigation();
                    return;
                }
                this.train.status.foundConductor();
            }
            this.destination.reserveFor(this.train);
            double acceleration = (double) this.train.acceleration();
            double brakingDistance = this.train.speed * this.train.speed / (2.0 * acceleration);
            double speedMod = this.destinationBehindTrain ? -1.0 : 1.0;
            double preDepartureLookAhead = this.train.getCurrentStation() != null ? 4.5 : 0.0;
            double distanceToNextCurve = -1.0;
            if (this.train.graph != null) {
                if (this.waitingForSignal != null && this.currentSignalResolved()) {
                    UUID signalId = this.waitingForSignal.getFirst();
                    SignalBoundary signal = this.train.graph.getPoint(EdgePointType.SIGNAL, signalId);
                    if (signal != null && signal.types.get(this.waitingForSignal.getSecond()) == SignalBlock.SignalType.CROSS_SIGNAL) {
                        this.waitingForChainedGroups.clear();
                    }
                    this.waitingForSignal = null;
                }
                TravellingPoint leadingPoint = !this.destinationBehindTrain ? ((Carriage) this.train.carriages.get(0)).getLeadingPoint() : ((Carriage) this.train.carriages.get(this.train.carriages.size() - 1)).getTrailingPoint();
                if (this.waitingForSignal == null) {
                    this.distanceToSignal = Double.MAX_VALUE;
                    this.ticksWaitingForSignal = 0;
                }
                if (this.distanceToSignal > 0.0625) {
                    MutableDouble curveDistanceTracker = new MutableDouble(-1.0);
                    this.signalScout.node1 = leadingPoint.node1;
                    this.signalScout.node2 = leadingPoint.node2;
                    this.signalScout.edge = leadingPoint.edge;
                    this.signalScout.position = leadingPoint.position;
                    double brakingDistanceNoFlicker = brakingDistance + 3.0 - brakingDistance % 3.0;
                    double scanDistance = Mth.clamp(brakingDistanceNoFlicker, preDepartureLookAhead, this.distanceToDestination);
                    MutableDouble crossSignalDistanceTracker = new MutableDouble(-1.0);
                    MutableObject<Pair<UUID, Boolean>> trackingCrossSignal = new MutableObject(null);
                    this.waitingForChainedGroups.clear();
                    this.signalScout.travel(this.train.graph, (this.distanceToDestination + 50.0) * speedMod, this.controlSignalScout(), (distance, couple) -> {
                        boolean crossSignalTracked = trackingCrossSignal.getValue() != null;
                        if (!crossSignalTracked && distance > scanDistance) {
                            return true;
                        } else {
                            Couple<TrackNode> nodes = (Couple<TrackNode>) couple.getSecond();
                            TrackEdgePoint boundary = (TrackEdgePoint) couple.getFirst();
                            if (boundary == this.destination && ((GlobalStation) boundary).canApproachFrom(nodes.getSecond())) {
                                return true;
                            } else if (!(boundary instanceof SignalBoundary signal)) {
                                return false;
                            } else {
                                UUID entering = signal.getGroup(nodes.getSecond());
                                SignalEdgeGroup signalEdgeGroup = (SignalEdgeGroup) Create.RAILWAYS.signalEdgeGroups.get(entering);
                                if (signalEdgeGroup == null) {
                                    return false;
                                } else {
                                    boolean primary = entering.equals(signal.groups.getFirst());
                                    boolean crossSignal = signal.types.get(primary) == SignalBlock.SignalType.CROSS_SIGNAL;
                                    boolean occupied = !this.train.manualTick && (signal.isForcedRed(nodes.getSecond()) || signalEdgeGroup.isOccupiedUnless(this.train));
                                    if (!crossSignalTracked) {
                                        if (crossSignal) {
                                            trackingCrossSignal.setValue(Pair.of(boundary.id, primary));
                                            crossSignalDistanceTracker.setValue(distance);
                                            this.waitingForChainedGroups.put(entering, Pair.of(signal, primary));
                                        }
                                        if (occupied) {
                                            this.waitingForSignal = Pair.of(boundary.id, primary);
                                            this.distanceToSignal = distance;
                                            if (!crossSignal) {
                                                return true;
                                            }
                                        }
                                        if (!occupied && !crossSignal && distance < this.distanceToSignal + 0.25 && distance < brakingDistanceNoFlicker) {
                                            signalEdgeGroup.reserved = signal;
                                        }
                                        return false;
                                    } else {
                                        if (crossSignalTracked) {
                                            this.waitingForChainedGroups.put(entering, Pair.of(signal, primary));
                                            if (occupied) {
                                                this.waitingForSignal = (Pair<UUID, Boolean>) trackingCrossSignal.getValue();
                                                this.distanceToSignal = crossSignalDistanceTracker.doubleValue();
                                                if (!crossSignal) {
                                                    return true;
                                                }
                                            }
                                            if (!crossSignal) {
                                                if (distance < this.distanceToSignal + 0.25) {
                                                    trackingCrossSignal.setValue(null);
                                                    this.reserveChain();
                                                    return false;
                                                }
                                                return true;
                                            }
                                        }
                                        return false;
                                    }
                                }
                            }
                        }
                    }, (distance, edge) -> {
                        BezierConnection turn = edge.getTurn();
                        double vDistance = Math.abs(turn.starts.getFirst().y - turn.starts.getSecond().y);
                        if (turn == null || !(vDistance > 0.0625) || !(turn.axes.getFirst().multiply(1.0, 0.0, 1.0).distanceTo(turn.axes.getSecond().multiply(1.0, 0.0, 1.0).scale(-1.0)) < 0.015625) || !(vDistance / turn.getLength() < 0.225F)) {
                            float current = curveDistanceTracker.floatValue();
                            if (current == -1.0F || distance < (double) current) {
                                curveDistanceTracker.setValue(distance);
                            }
                        }
                    });
                    if (trackingCrossSignal.getValue() != null && this.waitingForSignal == null) {
                        this.reserveChain();
                    }
                    distanceToNextCurve = (double) curveDistanceTracker.floatValue();
                } else {
                    this.ticksWaitingForSignal++;
                }
            }
            double targetDistance = this.waitingForSignal != null ? this.distanceToSignal : this.distanceToDestination;
            targetDistance += 0.25;
            if (targetDistance > 0.03125 && this.train.getCurrentStation() != null) {
                if (this.waitingForSignal != null && this.distanceToSignal < preDepartureLookAhead) {
                    this.ticksWaitingForSignal++;
                    return;
                }
                this.train.leaveStation();
            }
            this.train.currentlyBackwards = this.destinationBehindTrain;
            if (targetDistance < -10.0) {
                this.cancelNavigation();
            } else if (targetDistance - Math.abs(this.train.speed) < 0.03125) {
                this.train.speed = Math.max(targetDistance, 0.03125) * speedMod;
            } else {
                this.train.burnFuel();
                double topSpeed = (double) this.train.maxSpeed();
                if (targetDistance < 10.0) {
                    double target = topSpeed * (targetDistance / 10.0);
                    if (target < Math.abs(this.train.speed)) {
                        this.train.speed = this.train.speed + (target - Math.abs(this.train.speed)) * 0.5 * speedMod;
                        return;
                    }
                }
                topSpeed *= this.train.throttle;
                double turnTopSpeed = Math.min(topSpeed, (double) this.train.maxTurnSpeed());
                double targetSpeed = targetDistance > brakingDistance ? topSpeed * speedMod : 0.0;
                if (distanceToNextCurve != -1.0) {
                    double slowingDistance = brakingDistance - turnTopSpeed * turnTopSpeed / (2.0 * acceleration);
                    double targetTurnSpeed = distanceToNextCurve > slowingDistance ? topSpeed * speedMod : turnTopSpeed * speedMod;
                    if (Math.abs(targetTurnSpeed) < Math.abs(targetSpeed)) {
                        targetSpeed = targetTurnSpeed;
                    }
                }
                this.train.targetSpeed = targetSpeed;
                this.train.approachTargetSpeed(1.0F);
            }
        }
    }

    private void reserveChain() {
        this.train.reservedSignalBlocks.addAll(this.waitingForChainedGroups.keySet());
        this.waitingForChainedGroups.forEach((groupId, boundary) -> {
            SignalEdgeGroup signalEdgeGroup = (SignalEdgeGroup) Create.RAILWAYS.signalEdgeGroups.get(groupId);
            if (signalEdgeGroup != null) {
                signalEdgeGroup.reserved = (SignalBoundary) boundary.getFirst();
            }
        });
        this.waitingForChainedGroups.clear();
    }

    private boolean currentSignalResolved() {
        if (this.train.manualTick) {
            return true;
        } else if (this.distanceToDestination < 0.5) {
            return true;
        } else {
            SignalBoundary signal = this.train.graph.getPoint(EdgePointType.SIGNAL, this.waitingForSignal.getFirst());
            if (signal == null) {
                return true;
            } else if (signal.types.get(this.waitingForSignal.getSecond()) == SignalBlock.SignalType.CROSS_SIGNAL) {
                for (Entry<UUID, Pair<SignalBoundary, Boolean>> entry : this.waitingForChainedGroups.entrySet()) {
                    Pair<SignalBoundary, Boolean> boundary = (Pair<SignalBoundary, Boolean>) entry.getValue();
                    SignalEdgeGroup signalEdgeGroup = (SignalEdgeGroup) Create.RAILWAYS.signalEdgeGroups.get(entry.getKey());
                    if (signalEdgeGroup == null) {
                        this.waitingForSignal.setFirst(null);
                        return true;
                    }
                    if (boundary.getFirst().isForcedRed(boundary.getSecond())) {
                        this.train.reservedSignalBlocks.clear();
                        return false;
                    }
                    if (signalEdgeGroup.isOccupiedUnless(this.train)) {
                        return false;
                    }
                }
                return true;
            } else {
                UUID groupId = signal.groups.get(this.waitingForSignal.getSecond());
                if (groupId == null) {
                    return true;
                } else {
                    SignalEdgeGroup signalEdgeGroupx = (SignalEdgeGroup) Create.RAILWAYS.signalEdgeGroups.get(groupId);
                    return signalEdgeGroupx == null ? true : !signalEdgeGroupx.isOccupiedUnless(this.train);
                }
            }
        }
    }

    public boolean isActive() {
        return this.destination != null;
    }

    public TravellingPoint.ITrackSelector control(TravellingPoint mp) {
        return this.destination == null ? mp.steer(this.train.manualSteer, new Vec3(0.0, 1.0, 0.0)) : (graph, pair) -> this.navigateOptions(this.currentPath, graph, (List<Entry<TrackNode, TrackEdge>>) pair.getSecond());
    }

    public TravellingPoint.ITrackSelector controlSignalScout() {
        if (this.destination == null) {
            return this.signalScout.steer(this.train.manualSteer, new Vec3(0.0, 1.0, 0.0));
        } else {
            List<Couple<TrackNode>> pathCopy = new ArrayList(this.currentPath);
            return (graph, pair) -> this.navigateOptions(pathCopy, graph, (List<Entry<TrackNode, TrackEdge>>) pair.getSecond());
        }
    }

    private Entry<TrackNode, TrackEdge> navigateOptions(List<Couple<TrackNode>> path, TrackGraph graph, List<Entry<TrackNode, TrackEdge>> options) {
        if (path.isEmpty()) {
            return (Entry<TrackNode, TrackEdge>) options.get(0);
        } else {
            Couple<TrackNode> nodes = (Couple<TrackNode>) path.get(0);
            TrackEdge targetEdge = graph.getConnection(nodes);
            for (Entry<TrackNode, TrackEdge> entry : options) {
                if (entry.getValue() == targetEdge) {
                    path.remove(0);
                    return entry;
                }
            }
            return (Entry<TrackNode, TrackEdge>) options.get(0);
        }
    }

    public void cancelNavigation() {
        this.distanceToDestination = 0.0;
        this.currentPath.clear();
        if (this.destination != null) {
            this.destination.cancelReservation(this.train);
            this.destination = null;
            this.train.runtime.transitInterrupted();
            this.train.reservedSignalBlocks.clear();
        }
    }

    public double startNavigation(DiscoveredPath pathTo) {
        boolean noneFound = pathTo == null;
        double distance = noneFound ? -1.0 : Math.abs(pathTo.distance);
        double cost = noneFound ? -1.0 : pathTo.cost;
        this.distanceToDestination = distance;
        if (noneFound) {
            this.distanceToDestination = this.distanceStartedAt = 0.0;
            this.currentPath = new ArrayList();
            if (this.destination != null) {
                this.cancelNavigation();
            }
            return -1.0;
        } else {
            if (Math.abs(this.distanceToDestination) > 100.0) {
                this.announceArrival = true;
            }
            this.currentPath = pathTo.path;
            this.destinationBehindTrain = pathTo.distance < 0.0;
            this.train.reservedSignalBlocks.clear();
            this.train.navigation.waitingForSignal = null;
            if (this.destination == null) {
                this.distanceStartedAt = distance;
            }
            if (this.destination == pathTo.destination) {
                return 0.0;
            } else {
                if (!this.train.runtime.paused) {
                    boolean frontDriver = this.train.hasForwardConductor();
                    boolean backDriver = this.train.hasBackwardConductor();
                    if (this.destinationBehindTrain && !backDriver) {
                        if (frontDriver) {
                            this.train.status.missingCorrectConductor();
                        } else {
                            this.train.status.missingConductor();
                        }
                        return -1.0;
                    }
                    if (!this.destinationBehindTrain && !frontDriver) {
                        if (backDriver) {
                            this.train.status.missingCorrectConductor();
                        } else {
                            this.train.status.missingConductor();
                        }
                        return -1.0;
                    }
                    this.train.status.foundConductor();
                }
                this.destination = pathTo.destination;
                return cost;
            }
        }
    }

    @Nullable
    public DiscoveredPath findPathTo(GlobalStation destination, double maxCost) {
        ArrayList<GlobalStation> destinations = new ArrayList();
        destinations.add(destination);
        return this.findPathTo(destinations, maxCost);
    }

    @Nullable
    public DiscoveredPath findPathTo(ArrayList<GlobalStation> destinations, double maxCost) {
        TrackGraph graph = this.train.graph;
        if (graph == null) {
            return null;
        } else {
            Couple<DiscoveredPath> results = Couple.create(null, null);
            for (boolean forward : Iterate.trueAndFalse) {
                if (this.destination == null || this.destinationBehindTrain != forward) {
                    TravellingPoint initialPoint = forward ? ((Carriage) this.train.carriages.get(0)).getLeadingPoint() : ((Carriage) this.train.carriages.get(this.train.carriages.size() - 1)).getTrailingPoint();
                    TrackEdge initialEdge = forward ? initialPoint.edge : (TrackEdge) graph.getConnectionsFrom(initialPoint.node2).get(initialPoint.node1);
                    this.search(Double.MAX_VALUE, maxCost, forward, destinations, (distance, cost, reachedVia, currentEntry, globalStation) -> {
                        for (GlobalStation destination : destinations) {
                            if (globalStation == destination) {
                                TrackEdge edge = currentEntry.getSecond();
                                TrackNode node1 = currentEntry.getFirst().getFirst();
                                TrackNode node2 = currentEntry.getFirst().getSecond();
                                List<Couple<TrackNode>> currentPath = new ArrayList();
                                Pair<Boolean, Couple<TrackNode>> backTrack = (Pair<Boolean, Couple<TrackNode>>) reachedVia.get(edge);
                                Couple<TrackNode> toReach = Couple.create(node1, node2);
                                for (TrackEdge edgeReached = edge; backTrack != null && edgeReached != initialEdge; backTrack = (Pair<Boolean, Couple<TrackNode>>) reachedVia.get(edgeReached)) {
                                    if (backTrack.getFirst()) {
                                        currentPath.add(0, toReach);
                                    }
                                    toReach = backTrack.getSecond();
                                    edgeReached = graph.getConnection(toReach);
                                }
                                double position = edge.getLength() - destination.getLocationOn(edge);
                                double distanceToDestination = distance - position;
                                results.set(forward, new DiscoveredPath((double) (forward ? 1 : -1) * distanceToDestination, cost, currentPath, destination));
                                return true;
                            }
                        }
                        return false;
                    });
                }
            }
            DiscoveredPath front = results.getFirst();
            DiscoveredPath back = results.getSecond();
            boolean frontEmpty = front == null;
            boolean backEmpty = back == null;
            boolean canDriveForward = this.train.hasForwardConductor() || this.train.runtime.paused;
            boolean canDriveBackward = this.train.doubleEnded && this.train.hasBackwardConductor() || this.train.runtime.paused;
            if (backEmpty || !canDriveBackward) {
                return canDriveForward ? front : null;
            } else if (!frontEmpty && canDriveForward) {
                boolean frontBetter = maxCost == -1.0 ? -back.distance > front.distance : back.cost > front.cost;
                return frontBetter ? front : back;
            } else {
                return canDriveBackward ? back : null;
            }
        }
    }

    public GlobalStation findNearestApproachable(boolean forward) {
        TrackGraph graph = this.train.graph;
        if (graph == null) {
            return null;
        } else {
            MutableObject<GlobalStation> result = new MutableObject(null);
            double acceleration = (double) this.train.acceleration();
            double minDistance = 0.75 * this.train.speed * this.train.speed / (2.0 * acceleration);
            double maxDistance = Math.max(32.0, 1.5 * this.train.speed * this.train.speed / (2.0 * acceleration));
            this.search(maxDistance, forward, null, (distance, cost, reachedVia, currentEntry, globalStation) -> {
                if (distance < minDistance) {
                    return false;
                } else {
                    TrackEdge edge = currentEntry.getSecond();
                    double position = edge.getLength() - globalStation.getLocationOn(edge);
                    if (distance - position < minDistance) {
                        return false;
                    } else {
                        Train presentTrain = globalStation.getPresentTrain();
                        if (presentTrain != null && presentTrain != this.train) {
                            return false;
                        } else {
                            result.setValue(globalStation);
                            return true;
                        }
                    }
                }
            });
            return (GlobalStation) result.getValue();
        }
    }

    public void search(double maxDistance, boolean forward, ArrayList<GlobalStation> destinations, Navigation.StationTest stationTest) {
        this.search(maxDistance, -1.0, forward, destinations, stationTest);
    }

    public void search(double maxDistance, double maxCost, boolean forward, ArrayList<GlobalStation> destinations, Navigation.StationTest stationTest) {
        TrackGraph graph = this.train.graph;
        if (graph != null) {
            Set<TrackMaterial.TrackType> validTypes = new HashSet();
            for (int i = 0; i < this.train.carriages.size(); i++) {
                Carriage carriage = (Carriage) this.train.carriages.get(i);
                if (i == 0) {
                    validTypes.addAll(carriage.leadingBogey().type.getValidPathfindingTypes(carriage.leadingBogey().getStyle()));
                } else {
                    validTypes.retainAll(carriage.leadingBogey().type.getValidPathfindingTypes(carriage.leadingBogey().getStyle()));
                }
                if (carriage.isOnTwoBogeys()) {
                    validTypes.retainAll(carriage.trailingBogey().type.getValidPathfindingTypes(carriage.trailingBogey().getStyle()));
                }
            }
            if (!validTypes.isEmpty()) {
                Map<TrackEdge, Integer> penalties = new IdentityHashMap();
                boolean costRelevant = maxCost >= 0.0;
                if (costRelevant) {
                    for (Train otherTrain : Create.RAILWAYS.trains.values()) {
                        if (otherTrain.graph == graph && otherTrain != this.train) {
                            int navigationPenalty = otherTrain.getNavigationPenalty();
                            otherTrain.getEndpointEdges().forEach(nodes -> {
                                if (!nodes.either(Objects::isNull)) {
                                    for (boolean flip : Iterate.trueAndFalse) {
                                        TrackEdge e = graph.getConnection(flip ? nodes.swap() : nodes);
                                        if (e != null) {
                                            int existing = (Integer) penalties.getOrDefault(e, 0);
                                            penalties.put(e, existing + navigationPenalty / 2);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
                TravellingPoint startingPoint = forward ? ((Carriage) this.train.carriages.get(0)).getLeadingPoint() : ((Carriage) this.train.carriages.get(this.train.carriages.size() - 1)).getTrailingPoint();
                Set<TrackEdge> visited = new HashSet();
                Map<TrackEdge, Pair<Boolean, Couple<TrackNode>>> reachedVia = new IdentityHashMap();
                PriorityQueue<Navigation.FrontierEntry> frontier = new PriorityQueue();
                TrackNode initialNode1 = forward ? startingPoint.node1 : startingPoint.node2;
                TrackNode initialNode2 = forward ? startingPoint.node2 : startingPoint.node1;
                TrackEdge initialEdge = (TrackEdge) graph.getConnectionsFrom(initialNode1).get(initialNode2);
                if (initialEdge != null) {
                    double distanceToNode2 = forward ? initialEdge.getLength() - startingPoint.position : startingPoint.position;
                    int signalWeight = Mth.clamp(this.ticksWaitingForSignal * 2, 25, 200);
                    int initialPenalty = 0;
                    if (costRelevant) {
                        initialPenalty += penalties.getOrDefault(initialEdge, 0);
                    }
                    EdgeData initialSignalData = initialEdge.getEdgeData();
                    if (initialSignalData.hasPoints()) {
                        for (TrackEdgePoint point : initialSignalData.getPoints()) {
                            if (!(point.getLocationOn(initialEdge) < initialEdge.getLength() - distanceToNode2)) {
                                if (costRelevant && distanceToNode2 + (double) initialPenalty > maxCost) {
                                    return;
                                }
                                if (!point.canNavigateVia(initialNode2)) {
                                    return;
                                }
                                if (point instanceof SignalBoundary) {
                                    SignalBoundary signal = (SignalBoundary) point;
                                    if (signal.isForcedRed(initialNode2)) {
                                        initialPenalty += 400;
                                        continue;
                                    }
                                    UUID group = signal.getGroup(initialNode2);
                                    if (group == null) {
                                        continue;
                                    }
                                    SignalEdgeGroup signalEdgeGroup = (SignalEdgeGroup) Create.RAILWAYS.signalEdgeGroups.get(group);
                                    if (signalEdgeGroup == null) {
                                        continue;
                                    }
                                    if (signalEdgeGroup.isOccupiedUnless(signal)) {
                                        initialPenalty += signalWeight;
                                        signalWeight /= 2;
                                    }
                                }
                                if (point instanceof GlobalStation) {
                                    GlobalStation station = (GlobalStation) point;
                                    Train presentTrain = station.getPresentTrain();
                                    boolean isOwnStation = presentTrain == this.train;
                                    if (presentTrain != null && !isOwnStation) {
                                        initialPenalty += 300;
                                    }
                                    if (station.canApproachFrom(initialNode2) && stationTest.test(distanceToNode2, distanceToNode2 + (double) initialPenalty, reachedVia, Pair.of(Couple.create(initialNode1, initialNode2), initialEdge), station)) {
                                        return;
                                    }
                                    if (!isOwnStation) {
                                        initialPenalty += 50;
                                    }
                                }
                            }
                        }
                    }
                    if (!costRelevant || !(distanceToNode2 + (double) initialPenalty > maxCost)) {
                        frontier.add(new Navigation.FrontierEntry(distanceToNode2, initialPenalty, initialNode1, initialNode2, initialEdge));
                        while (!frontier.isEmpty()) {
                            Navigation.FrontierEntry entry = (Navigation.FrontierEntry) frontier.poll();
                            if (visited.add(entry.edge)) {
                                double distance = entry.distance;
                                int penalty = entry.penalty;
                                if (!(distance > maxDistance)) {
                                    TrackEdge edge = entry.edge;
                                    TrackNode node1 = entry.node1;
                                    TrackNode node2 = entry.node2;
                                    if (entry.hasDestination) {
                                        EdgeData signalData = edge.getEdgeData();
                                        if (signalData.hasPoints()) {
                                            for (TrackEdgePoint pointx : signalData.getPoints()) {
                                                if (pointx instanceof GlobalStation stationx && stationx.canApproachFrom(node2) && stationTest.test(distance, (double) penalty, reachedVia, Pair.of(Couple.create(node1, node2), edge), stationx)) {
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                    List<Entry<TrackNode, TrackEdge>> validTargets = new ArrayList();
                                    Map<TrackNode, TrackEdge> connectionsFrom = graph.getConnectionsFrom(node2);
                                    for (Entry<TrackNode, TrackEdge> connection : connectionsFrom.entrySet()) {
                                        TrackNode newNode = (TrackNode) connection.getKey();
                                        if (newNode != node1 && edge.canTravelTo((TrackEdge) connection.getValue())) {
                                            validTargets.add(connection);
                                        }
                                    }
                                    if (!validTargets.isEmpty()) {
                                        label304: for (Entry<TrackNode, TrackEdge> target : validTargets) {
                                            if (validTypes.contains(((TrackEdge) target.getValue()).getTrackMaterial().trackType)) {
                                                TrackNode newNode = (TrackNode) target.getKey();
                                                TrackEdge newEdge = (TrackEdge) target.getValue();
                                                int newPenalty = penalty;
                                                double edgeLength = newEdge.getLength();
                                                double newDistance = distance + edgeLength;
                                                if (costRelevant) {
                                                    newPenalty = penalty + (Integer) penalties.getOrDefault(newEdge, 0);
                                                }
                                                boolean hasDestination = false;
                                                EdgeData signalData = newEdge.getEdgeData();
                                                if (signalData.hasPoints()) {
                                                    for (TrackEdgePoint pointxx : signalData.getPoints()) {
                                                        if (node2 != initialNode1 || !(pointxx.getLocationOn(newEdge) < edgeLength - distanceToNode2)) {
                                                            if (costRelevant && newDistance + (double) newPenalty > maxCost || !pointxx.canNavigateVia(newNode)) {
                                                                continue label304;
                                                            }
                                                            if (pointxx instanceof SignalBoundary) {
                                                                SignalBoundary signalx = (SignalBoundary) pointxx;
                                                                if (signalx.isForcedRed(newNode)) {
                                                                    newPenalty += 400;
                                                                    continue;
                                                                }
                                                                UUID groupx = signalx.getGroup(newNode);
                                                                if (groupx == null) {
                                                                    continue;
                                                                }
                                                                SignalEdgeGroup signalEdgeGroupx = (SignalEdgeGroup) Create.RAILWAYS.signalEdgeGroups.get(groupx);
                                                                if (signalEdgeGroupx == null) {
                                                                    continue;
                                                                }
                                                                if (signalEdgeGroupx.isOccupiedUnless(signalx)) {
                                                                    newPenalty += signalWeight;
                                                                    signalWeight /= 2;
                                                                }
                                                            }
                                                            if (pointxx instanceof GlobalStation) {
                                                                GlobalStation stationx = (GlobalStation) pointxx;
                                                                Train presentTrainx = stationx.getPresentTrain();
                                                                boolean isOwnStationx = presentTrainx == this.train;
                                                                if (presentTrainx != null && !isOwnStationx) {
                                                                    newPenalty += 300;
                                                                }
                                                                if (stationx.canApproachFrom(newNode) && stationTest.test(newDistance, newDistance + (double) newPenalty, reachedVia, Pair.of(Couple.create(node2, newNode), newEdge), stationx)) {
                                                                    hasDestination = true;
                                                                } else if (!isOwnStationx) {
                                                                    newPenalty += 50;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if (!costRelevant || !(newDistance + (double) newPenalty > maxCost)) {
                                                    double remainingDist = 0.0;
                                                    if (destinations != null && !destinations.isEmpty()) {
                                                        remainingDist = Double.MAX_VALUE;
                                                        Vec3 newNodePosition = newNode.getLocation().getLocation();
                                                        for (GlobalStation destination : destinations) {
                                                            TrackNodeLocation destinationNode = destination.edgeLocation.getFirst();
                                                            double dMin = Math.abs(newNodePosition.x - destinationNode.getLocation().x);
                                                            double dMid = Math.abs(newNodePosition.y - destinationNode.getLocation().y);
                                                            double dMax = Math.abs(newNodePosition.z - destinationNode.getLocation().z);
                                                            if (dMin > dMid) {
                                                                double temp = dMid;
                                                                dMid = dMin;
                                                                dMin = temp;
                                                            }
                                                            if (dMin > dMax) {
                                                                double temp = dMax;
                                                                dMax = dMin;
                                                                dMin = temp;
                                                            }
                                                            if (dMid > dMax) {
                                                                double temp = dMax;
                                                                dMax = dMid;
                                                                dMid = temp;
                                                            }
                                                            double currentRemaining = 0.317837245195782 * dMin + 0.414213562373095 * dMid + dMax + destination.position;
                                                            if (node2.getLocation().equals(destinationNode)) {
                                                                currentRemaining -= newEdge.getLength() * 2.0;
                                                            }
                                                            remainingDist = Math.min(remainingDist, currentRemaining);
                                                        }
                                                    }
                                                    reachedVia.putIfAbsent(newEdge, Pair.of(validTargets.size() > 1, Couple.create(node1, node2)));
                                                    frontier.add(new Navigation.FrontierEntry(newDistance, newPenalty, remainingDist, hasDestination, node2, newNode, newEdge));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public CompoundTag write(DimensionPalette dimensions) {
        CompoundTag tag = new CompoundTag();
        if (this.destination == null) {
            return tag;
        } else {
            this.removeBrokenPathEntries();
            tag.putUUID("Destination", this.destination.id);
            tag.putDouble("DistanceToDestination", this.distanceToDestination);
            tag.putDouble("DistanceStartedAt", this.distanceStartedAt);
            tag.putBoolean("BehindTrain", this.destinationBehindTrain);
            tag.putBoolean("AnnounceArrival", this.announceArrival);
            tag.put("Path", NBTHelper.writeCompoundList(this.currentPath, c -> {
                CompoundTag nbt = new CompoundTag();
                nbt.put("Nodes", c.map(TrackNode::getLocation).serializeEach(loc -> loc.write(dimensions)));
                return nbt;
            }));
            if (this.waitingForSignal == null) {
                return tag;
            } else {
                tag.putUUID("BlockingSignal", this.waitingForSignal.getFirst());
                tag.putBoolean("BlockingSignalSide", this.waitingForSignal.getSecond());
                tag.putDouble("DistanceToSignal", this.distanceToSignal);
                tag.putInt("TicksWaitingForSignal", this.ticksWaitingForSignal);
                return tag;
            }
        }
    }

    public void read(CompoundTag tag, TrackGraph graph, DimensionPalette dimensions) {
        this.destination = graph != null && tag.contains("Destination") ? graph.getPoint(EdgePointType.STATION, tag.getUUID("Destination")) : null;
        if (this.destination != null) {
            this.distanceToDestination = tag.getDouble("DistanceToDestination");
            this.distanceStartedAt = tag.getDouble("DistanceStartedAt");
            this.destinationBehindTrain = tag.getBoolean("BehindTrain");
            this.announceArrival = tag.getBoolean("AnnounceArrival");
            this.currentPath.clear();
            NBTHelper.iterateCompoundList(tag.getList("Path", 10), c -> this.currentPath.add(Couple.deserializeEach(c.getList("Nodes", 10), c2 -> TrackNodeLocation.read(c2, dimensions)).map(graph::locateNode)));
            this.removeBrokenPathEntries();
            this.waitingForSignal = tag.contains("BlockingSignal") ? Pair.of(tag.getUUID("BlockingSignal"), tag.getBoolean("BlockingSignalSide")) : null;
            if (this.waitingForSignal != null) {
                this.distanceToSignal = tag.getDouble("DistanceToSignal");
                this.ticksWaitingForSignal = tag.getInt("TicksWaitingForSignal");
            }
        }
    }

    private void removeBrokenPathEntries() {
        boolean nullEntriesPresent = false;
        Iterator<Couple<TrackNode>> iterator = this.currentPath.iterator();
        while (iterator.hasNext()) {
            Couple<TrackNode> couple = (Couple<TrackNode>) iterator.next();
            if (couple == null || couple.getFirst() == null || couple.getSecond() == null) {
                iterator.remove();
                nullEntriesPresent = true;
            }
        }
        if (nullEntriesPresent) {
            Create.LOGGER.error("Found null values in path of train with name: " + this.train.name.getString() + ", id: " + this.train.id.toString());
        }
    }

    private class FrontierEntry implements Comparable<Navigation.FrontierEntry> {

        double distance;

        int penalty;

        double remaining;

        boolean hasDestination;

        TrackNode node1;

        TrackNode node2;

        TrackEdge edge;

        public FrontierEntry(double distance, int penalty, TrackNode node1, TrackNode node2, TrackEdge edge) {
            this.distance = distance;
            this.penalty = penalty;
            this.remaining = 0.0;
            this.hasDestination = false;
            this.node1 = node1;
            this.node2 = node2;
            this.edge = edge;
        }

        public FrontierEntry(double distance, int penalty, double remaining, boolean hasDestination, TrackNode node1, TrackNode node2, TrackEdge edge) {
            this.distance = distance;
            this.penalty = penalty;
            this.remaining = remaining;
            this.hasDestination = hasDestination;
            this.node1 = node1;
            this.node2 = node2;
            this.edge = edge;
        }

        public int compareTo(Navigation.FrontierEntry o) {
            return Double.compare(this.distance + (double) this.penalty + this.remaining, o.distance + (double) o.penalty + o.remaining);
        }
    }

    @FunctionalInterface
    public interface StationTest {

        boolean test(double var1, double var3, Map<TrackEdge, Pair<Boolean, Couple<TrackNode>>> var5, Pair<Couple<TrackNode>, TrackEdge> var6, GlobalStation var7);
    }
}