package net.minecraft.world.level.pathfinder;

import net.minecraft.network.FriendlyByteBuf;

public class Target extends Node {

    private float bestHeuristic = Float.MAX_VALUE;

    private Node bestNode;

    private boolean reached;

    public Target(Node node0) {
        super(node0.x, node0.y, node0.z);
    }

    public Target(int int0, int int1, int int2) {
        super(int0, int1, int2);
    }

    public void updateBest(float float0, Node node1) {
        if (float0 < this.bestHeuristic) {
            this.bestHeuristic = float0;
            this.bestNode = node1;
        }
    }

    public Node getBestNode() {
        return this.bestNode;
    }

    public void setReached() {
        this.reached = true;
    }

    public boolean isReached() {
        return this.reached;
    }

    public static Target createFromStream(FriendlyByteBuf friendlyByteBuf0) {
        Target $$1 = new Target(friendlyByteBuf0.readInt(), friendlyByteBuf0.readInt(), friendlyByteBuf0.readInt());
        m_262841_(friendlyByteBuf0, $$1);
        return $$1;
    }
}