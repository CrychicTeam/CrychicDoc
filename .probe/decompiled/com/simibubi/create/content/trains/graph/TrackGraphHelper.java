package com.simibubi.create.content.trains.graph;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.BezierTrackPointLocation;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackBlockEntity;
import com.simibubi.create.foundation.utility.Couple;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TrackGraphHelper {

    @Nullable
    public static TrackGraphLocation getGraphLocationAt(Level level, BlockPos pos, Direction.AxisDirection targetDirection, Vec3 targetAxis) {
        BlockState trackBlockState = level.getBlockState(pos);
        if (!(trackBlockState.m_60734_() instanceof ITrackBlock track)) {
            return null;
        } else {
            Vec3 axis = targetAxis.scale((double) targetDirection.getStep());
            double length = axis.length();
            TrackGraph graph = null;
            TrackNodeLocation location = new TrackNodeLocation(Vec3.atBottomCenterOf(pos).add(0.0, track.getElevationAtCenter(level, pos, trackBlockState), 0.0)).in(level);
            graph = Create.RAILWAYS.sided(level).getGraph(level, location);
            if (graph != null) {
                TrackNode node = graph.locateNode(location);
                if (node != null) {
                    Map<TrackNode, TrackEdge> connectionsFrom = graph.getConnectionsFrom(node);
                    for (Entry<TrackNode, TrackEdge> entry : connectionsFrom.entrySet()) {
                        TrackNode backNode = (TrackNode) entry.getKey();
                        Vec3 direction = ((TrackEdge) entry.getValue()).getDirection(true);
                        if (!(direction.scale(length).distanceToSqr(axis.scale(-1.0)) > 2.4414062E-4F)) {
                            TrackGraphLocation graphLocation = new TrackGraphLocation();
                            graphLocation.edge = Couple.create(node.getLocation(), backNode.getLocation());
                            graphLocation.position = 0.0;
                            graphLocation.graph = graph;
                            return graphLocation;
                        }
                    }
                }
            }
            Collection<TrackNodeLocation.DiscoveredLocation> ends = track.getConnected(level, pos, trackBlockState, true, null);
            Vec3 start = Vec3.atBottomCenterOf(pos).add(0.0, track.getElevationAtCenter(level, pos, trackBlockState), 0.0);
            TrackNode frontNode = null;
            TrackNode backNode = null;
            double position = 0.0;
            boolean singleTrackPiece = true;
            for (TrackNodeLocation.DiscoveredLocation current : ends) {
                Vec3 offset = current.getLocation().subtract(start).normalize().scale(length);
                Vec3 compareOffset = offset.multiply(1.0, 0.0, 1.0).normalize();
                boolean forward = compareOffset.distanceToSqr(axis.multiply(-1.0, 0.0, -1.0).normalize()) < 2.4414062E-4F;
                boolean backwards = compareOffset.distanceToSqr(axis.multiply(1.0, 0.0, 1.0).normalize()) < 2.4414062E-4F;
                if (forward || backwards) {
                    TrackNodeLocation.DiscoveredLocation previous = null;
                    double distance = 0.0;
                    for (int i = 0; i < 100 && distance < 32.0; i++) {
                        TrackNodeLocation.DiscoveredLocation loc = current;
                        if (graph == null) {
                            graph = Create.RAILWAYS.sided(level).getGraph(level, current);
                        }
                        if (graph != null && graph.locateNode(current) != null) {
                            TrackNode node = graph.locateNode(current);
                            if (forward) {
                                frontNode = node;
                            }
                            if (backwards) {
                                backNode = node;
                                position = distance + axis.length() / 2.0;
                            }
                            break;
                        }
                        singleTrackPiece = false;
                        for (TrackNodeLocation.DiscoveredLocation discoveredLocation : ITrackBlock.walkConnectedTracks(level, current, true)) {
                            if (discoveredLocation != previous) {
                                Vec3 diff = discoveredLocation.getLocation().subtract(loc.getLocation());
                                if (!((forward ? axis.scale(-1.0) : axis).distanceToSqr(diff.normalize().scale(length)) > 2.4414062E-4F)) {
                                    previous = current;
                                    current = discoveredLocation;
                                    distance += diff.length();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (frontNode != null && backNode != null) {
                if (singleTrackPiece) {
                    position = frontNode.getLocation().getLocation().distanceTo(backNode.getLocation().getLocation()) / 2.0;
                }
                TrackGraphLocation graphLocation = new TrackGraphLocation();
                graphLocation.edge = Couple.create(backNode.getLocation(), frontNode.getLocation());
                graphLocation.position = position;
                graphLocation.graph = graph;
                return graphLocation;
            } else {
                return null;
            }
        }
    }

    @Nullable
    public static TrackGraphLocation getBezierGraphLocationAt(Level level, BlockPos pos, Direction.AxisDirection targetDirection, BezierTrackPointLocation targetBezier) {
        BlockState state = level.getBlockState(pos);
        if (state.m_60734_() instanceof ITrackBlock track) {
            if (level.getBlockEntity(pos) instanceof TrackBlockEntity trackBE) {
                BezierConnection bc = (BezierConnection) trackBE.getConnections().get(targetBezier.curveTarget());
                if (bc != null && bc.isPrimary()) {
                    TrackNodeLocation targetLoc = new TrackNodeLocation(bc.starts.getSecond()).in(level);
                    if (bc.smoothing != null) {
                        targetLoc.yOffsetPixels = bc.smoothing.getSecond();
                    }
                    for (TrackNodeLocation.DiscoveredLocation location : track.getConnected(level, pos, state, true, null)) {
                        TrackGraph graph = Create.RAILWAYS.sided(level).getGraph(level, location);
                        if (graph != null) {
                            TrackNode targetNode = graph.locateNode(targetLoc);
                            if (targetNode != null) {
                                TrackNode node = graph.locateNode(location);
                                TrackEdge edge = (TrackEdge) graph.getConnectionsFrom(node).get(targetNode);
                                if (edge != null) {
                                    TrackGraphLocation graphLocation = new TrackGraphLocation();
                                    graphLocation.graph = graph;
                                    graphLocation.edge = Couple.create(location, targetLoc);
                                    graphLocation.position = (double) ((float) (targetBezier.segment() + 1) / 2.0F);
                                    if (targetDirection == Direction.AxisDirection.POSITIVE) {
                                        graphLocation.edge = graphLocation.edge.swap();
                                        graphLocation.position = edge.getLength() - graphLocation.position;
                                    }
                                    return graphLocation;
                                }
                            }
                        }
                    }
                    return null;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}