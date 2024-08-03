package com.simibubi.create.content.trains.graph;

import com.simibubi.create.content.trains.track.BezierConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TrackGraphBounds {

    public AABB box;

    public List<BezierConnection> beziers = new ArrayList();

    public TrackGraphBounds(TrackGraph graph, ResourceKey<Level> dimension) {
        this.box = null;
        for (TrackNode node : graph.nodes.values()) {
            if (node.location.dimension == dimension) {
                this.include(node);
                Map<TrackNode, TrackEdge> connections = graph.getConnectionsFrom(node);
                for (TrackEdge edge : connections.values()) {
                    if (edge.turn != null && edge.turn.isPrimary()) {
                        this.beziers.add(edge.turn);
                    }
                }
            }
        }
        if (this.box != null) {
            this.box = this.box.inflate(2.0);
        }
    }

    private void include(TrackNode node) {
        Vec3 v = node.location.getLocation();
        AABB aabb = new AABB(v, v);
        this.box = this.box == null ? aabb : this.box.minmax(aabb);
    }
}