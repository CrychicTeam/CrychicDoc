package net.minecraft.world.level.pathfinder;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class Node {

    public final int x;

    public final int y;

    public final int z;

    private final int hash;

    public int heapIdx = -1;

    public float g;

    public float h;

    public float f;

    @Nullable
    public Node cameFrom;

    public boolean closed;

    public float walkedDistance;

    public float costMalus;

    public BlockPathTypes type = BlockPathTypes.BLOCKED;

    public Node(int int0, int int1, int int2) {
        this.x = int0;
        this.y = int1;
        this.z = int2;
        this.hash = createHash(int0, int1, int2);
    }

    public Node cloneAndMove(int int0, int int1, int int2) {
        Node $$3 = new Node(int0, int1, int2);
        $$3.heapIdx = this.heapIdx;
        $$3.g = this.g;
        $$3.h = this.h;
        $$3.f = this.f;
        $$3.cameFrom = this.cameFrom;
        $$3.closed = this.closed;
        $$3.walkedDistance = this.walkedDistance;
        $$3.costMalus = this.costMalus;
        $$3.type = this.type;
        return $$3;
    }

    public static int createHash(int int0, int int1, int int2) {
        return int1 & 0xFF | (int0 & 32767) << 8 | (int2 & 32767) << 24 | (int0 < 0 ? Integer.MIN_VALUE : 0) | (int2 < 0 ? 32768 : 0);
    }

    public float distanceTo(Node node0) {
        float $$1 = (float) (node0.x - this.x);
        float $$2 = (float) (node0.y - this.y);
        float $$3 = (float) (node0.z - this.z);
        return Mth.sqrt($$1 * $$1 + $$2 * $$2 + $$3 * $$3);
    }

    public float distanceToXZ(Node node0) {
        float $$1 = (float) (node0.x - this.x);
        float $$2 = (float) (node0.z - this.z);
        return Mth.sqrt($$1 * $$1 + $$2 * $$2);
    }

    public float distanceTo(BlockPos blockPos0) {
        float $$1 = (float) (blockPos0.m_123341_() - this.x);
        float $$2 = (float) (blockPos0.m_123342_() - this.y);
        float $$3 = (float) (blockPos0.m_123343_() - this.z);
        return Mth.sqrt($$1 * $$1 + $$2 * $$2 + $$3 * $$3);
    }

    public float distanceToSqr(Node node0) {
        float $$1 = (float) (node0.x - this.x);
        float $$2 = (float) (node0.y - this.y);
        float $$3 = (float) (node0.z - this.z);
        return $$1 * $$1 + $$2 * $$2 + $$3 * $$3;
    }

    public float distanceToSqr(BlockPos blockPos0) {
        float $$1 = (float) (blockPos0.m_123341_() - this.x);
        float $$2 = (float) (blockPos0.m_123342_() - this.y);
        float $$3 = (float) (blockPos0.m_123343_() - this.z);
        return $$1 * $$1 + $$2 * $$2 + $$3 * $$3;
    }

    public float distanceManhattan(Node node0) {
        float $$1 = (float) Math.abs(node0.x - this.x);
        float $$2 = (float) Math.abs(node0.y - this.y);
        float $$3 = (float) Math.abs(node0.z - this.z);
        return $$1 + $$2 + $$3;
    }

    public float distanceManhattan(BlockPos blockPos0) {
        float $$1 = (float) Math.abs(blockPos0.m_123341_() - this.x);
        float $$2 = (float) Math.abs(blockPos0.m_123342_() - this.y);
        float $$3 = (float) Math.abs(blockPos0.m_123343_() - this.z);
        return $$1 + $$2 + $$3;
    }

    public BlockPos asBlockPos() {
        return new BlockPos(this.x, this.y, this.z);
    }

    public Vec3 asVec3() {
        return new Vec3((double) this.x, (double) this.y, (double) this.z);
    }

    public boolean equals(Object object0) {
        return !(object0 instanceof Node $$1) ? false : this.hash == $$1.hash && this.x == $$1.x && this.y == $$1.y && this.z == $$1.z;
    }

    public int hashCode() {
        return this.hash;
    }

    public boolean inOpenSet() {
        return this.heapIdx >= 0;
    }

    public String toString() {
        return "Node{x=" + this.x + ", y=" + this.y + ", z=" + this.z + "}";
    }

    public void writeToStream(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeInt(this.x);
        friendlyByteBuf0.writeInt(this.y);
        friendlyByteBuf0.writeInt(this.z);
        friendlyByteBuf0.writeFloat(this.walkedDistance);
        friendlyByteBuf0.writeFloat(this.costMalus);
        friendlyByteBuf0.writeBoolean(this.closed);
        friendlyByteBuf0.writeEnum(this.type);
        friendlyByteBuf0.writeFloat(this.f);
    }

    public static Node createFromStream(FriendlyByteBuf friendlyByteBuf0) {
        Node $$1 = new Node(friendlyByteBuf0.readInt(), friendlyByteBuf0.readInt(), friendlyByteBuf0.readInt());
        readContents(friendlyByteBuf0, $$1);
        return $$1;
    }

    protected static void readContents(FriendlyByteBuf friendlyByteBuf0, Node node1) {
        node1.walkedDistance = friendlyByteBuf0.readFloat();
        node1.costMalus = friendlyByteBuf0.readFloat();
        node1.closed = friendlyByteBuf0.readBoolean();
        node1.type = friendlyByteBuf0.readEnum(BlockPathTypes.class);
        node1.f = friendlyByteBuf0.readFloat();
    }
}