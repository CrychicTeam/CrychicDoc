package net.minecraft.world.level.pathfinder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class Path {

    private final List<Node> nodes;

    private Node[] openSet = new Node[0];

    private Node[] closedSet = new Node[0];

    @Nullable
    private Set<Target> targetNodes;

    private int nextNodeIndex;

    private final BlockPos target;

    private final float distToTarget;

    private final boolean reached;

    public Path(List<Node> listNode0, BlockPos blockPos1, boolean boolean2) {
        this.nodes = listNode0;
        this.target = blockPos1;
        this.distToTarget = listNode0.isEmpty() ? Float.MAX_VALUE : ((Node) this.nodes.get(this.nodes.size() - 1)).distanceManhattan(this.target);
        this.reached = boolean2;
    }

    public void advance() {
        this.nextNodeIndex++;
    }

    public boolean notStarted() {
        return this.nextNodeIndex <= 0;
    }

    public boolean isDone() {
        return this.nextNodeIndex >= this.nodes.size();
    }

    @Nullable
    public Node getEndNode() {
        return !this.nodes.isEmpty() ? (Node) this.nodes.get(this.nodes.size() - 1) : null;
    }

    public Node getNode(int int0) {
        return (Node) this.nodes.get(int0);
    }

    public void truncateNodes(int int0) {
        if (this.nodes.size() > int0) {
            this.nodes.subList(int0, this.nodes.size()).clear();
        }
    }

    public void replaceNode(int int0, Node node1) {
        this.nodes.set(int0, node1);
    }

    public int getNodeCount() {
        return this.nodes.size();
    }

    public int getNextNodeIndex() {
        return this.nextNodeIndex;
    }

    public void setNextNodeIndex(int int0) {
        this.nextNodeIndex = int0;
    }

    public Vec3 getEntityPosAtNode(Entity entity0, int int1) {
        Node $$2 = (Node) this.nodes.get(int1);
        double $$3 = (double) $$2.x + (double) ((int) (entity0.getBbWidth() + 1.0F)) * 0.5;
        double $$4 = (double) $$2.y;
        double $$5 = (double) $$2.z + (double) ((int) (entity0.getBbWidth() + 1.0F)) * 0.5;
        return new Vec3($$3, $$4, $$5);
    }

    public BlockPos getNodePos(int int0) {
        return ((Node) this.nodes.get(int0)).asBlockPos();
    }

    public Vec3 getNextEntityPos(Entity entity0) {
        return this.getEntityPosAtNode(entity0, this.nextNodeIndex);
    }

    public BlockPos getNextNodePos() {
        return ((Node) this.nodes.get(this.nextNodeIndex)).asBlockPos();
    }

    public Node getNextNode() {
        return (Node) this.nodes.get(this.nextNodeIndex);
    }

    @Nullable
    public Node getPreviousNode() {
        return this.nextNodeIndex > 0 ? (Node) this.nodes.get(this.nextNodeIndex - 1) : null;
    }

    public boolean sameAs(@Nullable Path path0) {
        if (path0 == null) {
            return false;
        } else if (path0.nodes.size() != this.nodes.size()) {
            return false;
        } else {
            for (int $$1 = 0; $$1 < this.nodes.size(); $$1++) {
                Node $$2 = (Node) this.nodes.get($$1);
                Node $$3 = (Node) path0.nodes.get($$1);
                if ($$2.x != $$3.x || $$2.y != $$3.y || $$2.z != $$3.z) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean canReach() {
        return this.reached;
    }

    @VisibleForDebug
    void setDebug(Node[] node0, Node[] node1, Set<Target> setTarget2) {
        this.openSet = node0;
        this.closedSet = node1;
        this.targetNodes = setTarget2;
    }

    @VisibleForDebug
    public Node[] getOpenSet() {
        return this.openSet;
    }

    @VisibleForDebug
    public Node[] getClosedSet() {
        return this.closedSet;
    }

    public void writeToStream(FriendlyByteBuf friendlyByteBuf0) {
        if (this.targetNodes != null && !this.targetNodes.isEmpty()) {
            friendlyByteBuf0.writeBoolean(this.reached);
            friendlyByteBuf0.writeInt(this.nextNodeIndex);
            friendlyByteBuf0.writeInt(this.targetNodes.size());
            this.targetNodes.forEach(p_164708_ -> p_164708_.m_164699_(friendlyByteBuf0));
            friendlyByteBuf0.writeInt(this.target.m_123341_());
            friendlyByteBuf0.writeInt(this.target.m_123342_());
            friendlyByteBuf0.writeInt(this.target.m_123343_());
            friendlyByteBuf0.writeInt(this.nodes.size());
            for (Node $$1 : this.nodes) {
                $$1.writeToStream(friendlyByteBuf0);
            }
            friendlyByteBuf0.writeInt(this.openSet.length);
            for (Node $$2 : this.openSet) {
                $$2.writeToStream(friendlyByteBuf0);
            }
            friendlyByteBuf0.writeInt(this.closedSet.length);
            for (Node $$3 : this.closedSet) {
                $$3.writeToStream(friendlyByteBuf0);
            }
        }
    }

    public static Path createFromStream(FriendlyByteBuf friendlyByteBuf0) {
        boolean $$1 = friendlyByteBuf0.readBoolean();
        int $$2 = friendlyByteBuf0.readInt();
        int $$3 = friendlyByteBuf0.readInt();
        Set<Target> $$4 = Sets.newHashSet();
        for (int $$5 = 0; $$5 < $$3; $$5++) {
            $$4.add(Target.createFromStream(friendlyByteBuf0));
        }
        BlockPos $$6 = new BlockPos(friendlyByteBuf0.readInt(), friendlyByteBuf0.readInt(), friendlyByteBuf0.readInt());
        List<Node> $$7 = Lists.newArrayList();
        int $$8 = friendlyByteBuf0.readInt();
        for (int $$9 = 0; $$9 < $$8; $$9++) {
            $$7.add(Node.createFromStream(friendlyByteBuf0));
        }
        Node[] $$10 = new Node[friendlyByteBuf0.readInt()];
        for (int $$11 = 0; $$11 < $$10.length; $$11++) {
            $$10[$$11] = Node.createFromStream(friendlyByteBuf0);
        }
        Node[] $$12 = new Node[friendlyByteBuf0.readInt()];
        for (int $$13 = 0; $$13 < $$12.length; $$13++) {
            $$12[$$13] = Node.createFromStream(friendlyByteBuf0);
        }
        Path $$14 = new Path($$7, $$6, $$1);
        $$14.openSet = $$10;
        $$14.closedSet = $$12;
        $$14.targetNodes = $$4;
        $$14.nextNodeIndex = $$2;
        return $$14;
    }

    public String toString() {
        return "Path(length=" + this.nodes.size() + ")";
    }

    public BlockPos getTarget() {
        return this.target;
    }

    public float getDistToTarget() {
        return this.distToTarget;
    }
}