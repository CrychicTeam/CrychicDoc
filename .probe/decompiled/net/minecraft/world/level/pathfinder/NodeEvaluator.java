package net.minecraft.world.level.pathfinder;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;

public abstract class NodeEvaluator {

    protected PathNavigationRegion level;

    protected Mob mob;

    protected final Int2ObjectMap<Node> nodes = new Int2ObjectOpenHashMap();

    protected int entityWidth;

    protected int entityHeight;

    protected int entityDepth;

    protected boolean canPassDoors;

    protected boolean canOpenDoors;

    protected boolean canFloat;

    protected boolean canWalkOverFences;

    public void prepare(PathNavigationRegion pathNavigationRegion0, Mob mob1) {
        this.level = pathNavigationRegion0;
        this.mob = mob1;
        this.nodes.clear();
        this.entityWidth = Mth.floor(mob1.m_20205_() + 1.0F);
        this.entityHeight = Mth.floor(mob1.m_20206_() + 1.0F);
        this.entityDepth = Mth.floor(mob1.m_20205_() + 1.0F);
    }

    public void done() {
        this.level = null;
        this.mob = null;
    }

    protected Node getNode(BlockPos blockPos0) {
        return this.getNode(blockPos0.m_123341_(), blockPos0.m_123342_(), blockPos0.m_123343_());
    }

    protected Node getNode(int int0, int int1, int int2) {
        return (Node) this.nodes.computeIfAbsent(Node.createHash(int0, int1, int2), p_77332_ -> new Node(int0, int1, int2));
    }

    public abstract Node getStart();

    public abstract Target getGoal(double var1, double var3, double var5);

    protected Target getTargetFromNode(Node node0) {
        return new Target(node0);
    }

    public abstract int getNeighbors(Node[] var1, Node var2);

    public abstract BlockPathTypes getBlockPathType(BlockGetter var1, int var2, int var3, int var4, Mob var5);

    public abstract BlockPathTypes getBlockPathType(BlockGetter var1, int var2, int var3, int var4);

    public void setCanPassDoors(boolean boolean0) {
        this.canPassDoors = boolean0;
    }

    public void setCanOpenDoors(boolean boolean0) {
        this.canOpenDoors = boolean0;
    }

    public void setCanFloat(boolean boolean0) {
        this.canFloat = boolean0;
    }

    public void setCanWalkOverFences(boolean boolean0) {
        this.canWalkOverFences = boolean0;
    }

    public boolean canPassDoors() {
        return this.canPassDoors;
    }

    public boolean canOpenDoors() {
        return this.canOpenDoors;
    }

    public boolean canFloat() {
        return this.canFloat;
    }

    public boolean canWalkOverFences() {
        return this.canWalkOverFences;
    }
}