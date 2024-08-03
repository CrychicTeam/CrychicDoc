package net.minecraft.world.level.pathfinder;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.profiling.metrics.MetricCategory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;

public class PathFinder {

    private static final float FUDGING = 1.5F;

    private final Node[] neighbors = new Node[32];

    private final int maxVisitedNodes;

    private final NodeEvaluator nodeEvaluator;

    private static final boolean DEBUG = false;

    private final BinaryHeap openSet = new BinaryHeap();

    public PathFinder(NodeEvaluator nodeEvaluator0, int int1) {
        this.nodeEvaluator = nodeEvaluator0;
        this.maxVisitedNodes = int1;
    }

    @Nullable
    public Path findPath(PathNavigationRegion pathNavigationRegion0, Mob mob1, Set<BlockPos> setBlockPos2, float float3, int int4, float float5) {
        this.openSet.clear();
        this.nodeEvaluator.prepare(pathNavigationRegion0, mob1);
        Node $$6 = this.nodeEvaluator.getStart();
        if ($$6 == null) {
            return null;
        } else {
            Map<Target, BlockPos> $$7 = (Map<Target, BlockPos>) setBlockPos2.stream().collect(Collectors.toMap(p_77448_ -> this.nodeEvaluator.getGoal((double) p_77448_.m_123341_(), (double) p_77448_.m_123342_(), (double) p_77448_.m_123343_()), Function.identity()));
            Path $$8 = this.findPath(pathNavigationRegion0.getProfiler(), $$6, $$7, float3, int4, float5);
            this.nodeEvaluator.done();
            return $$8;
        }
    }

    @Nullable
    private Path findPath(ProfilerFiller profilerFiller0, Node node1, Map<Target, BlockPos> mapTargetBlockPos2, float float3, int int4, float float5) {
        profilerFiller0.push("find_path");
        profilerFiller0.markForCharting(MetricCategory.PATH_FINDING);
        Set<Target> $$6 = mapTargetBlockPos2.keySet();
        node1.g = 0.0F;
        node1.h = this.getBestH(node1, $$6);
        node1.f = node1.h;
        this.openSet.clear();
        this.openSet.insert(node1);
        Set<Node> $$7 = ImmutableSet.of();
        int $$8 = 0;
        Set<Target> $$9 = Sets.newHashSetWithExpectedSize($$6.size());
        int $$10 = (int) ((float) this.maxVisitedNodes * float5);
        while (!this.openSet.isEmpty()) {
            if (++$$8 >= $$10) {
                break;
            }
            Node $$11 = this.openSet.pop();
            $$11.closed = true;
            for (Target $$12 : $$6) {
                if ($$11.distanceManhattan($$12) <= (float) int4) {
                    $$12.setReached();
                    $$9.add($$12);
                }
            }
            if (!$$9.isEmpty()) {
                break;
            }
            if (!($$11.distanceTo(node1) >= float3)) {
                int $$13 = this.nodeEvaluator.getNeighbors(this.neighbors, $$11);
                for (int $$14 = 0; $$14 < $$13; $$14++) {
                    Node $$15 = this.neighbors[$$14];
                    float $$16 = this.distance($$11, $$15);
                    $$15.walkedDistance = $$11.walkedDistance + $$16;
                    float $$17 = $$11.g + $$16 + $$15.costMalus;
                    if ($$15.walkedDistance < float3 && (!$$15.inOpenSet() || $$17 < $$15.g)) {
                        $$15.cameFrom = $$11;
                        $$15.g = $$17;
                        $$15.h = this.getBestH($$15, $$6) * 1.5F;
                        if ($$15.inOpenSet()) {
                            this.openSet.changeCost($$15, $$15.g + $$15.h);
                        } else {
                            $$15.f = $$15.g + $$15.h;
                            this.openSet.insert($$15);
                        }
                    }
                }
            }
        }
        Optional<Path> $$18 = !$$9.isEmpty() ? $$9.stream().map(p_77454_ -> this.reconstructPath(p_77454_.getBestNode(), (BlockPos) mapTargetBlockPos2.get(p_77454_), true)).min(Comparator.comparingInt(Path::m_77398_)) : $$6.stream().map(p_77451_ -> this.reconstructPath(p_77451_.getBestNode(), (BlockPos) mapTargetBlockPos2.get(p_77451_), false)).min(Comparator.comparingDouble(Path::m_77407_).thenComparingInt(Path::m_77398_));
        profilerFiller0.pop();
        return !$$18.isPresent() ? null : (Path) $$18.get();
    }

    protected float distance(Node node0, Node node1) {
        return node0.distanceTo(node1);
    }

    private float getBestH(Node node0, Set<Target> setTarget1) {
        float $$2 = Float.MAX_VALUE;
        for (Target $$3 : setTarget1) {
            float $$4 = node0.distanceTo($$3);
            $$3.updateBest($$4, node0);
            $$2 = Math.min($$4, $$2);
        }
        return $$2;
    }

    private Path reconstructPath(Node node0, BlockPos blockPos1, boolean boolean2) {
        List<Node> $$3 = Lists.newArrayList();
        Node $$4 = node0;
        $$3.add(0, node0);
        while ($$4.cameFrom != null) {
            $$4 = $$4.cameFrom;
            $$3.add(0, $$4);
        }
        return new Path($$3, blockPos1, boolean2);
    }
}