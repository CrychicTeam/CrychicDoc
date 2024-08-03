package snownee.loquat.placement.tree;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

public class TreeNode {

    public final List<String> tags = Lists.newArrayList();

    private final ResourceLocation pool;

    @Nullable
    private final String parentEdge;

    private final List<TreeNode> children = ObjectArrayList.of();

    private final List<ResourceLocation> lowPriorityProcessors = ObjectArrayList.of();

    private final List<ResourceLocation> highPriorityProcessors = ObjectArrayList.of();

    private String uniqueGroup;

    private int minEdgeDistance;

    private Function<String, TreeNode> fallbackNodeProvider = jointName -> null;

    private CompoundTag data;

    private boolean offsetTowardsJigsawFront = true;

    private boolean checkForCollisions = true;

    public TreeNode(ResourceLocation pool) {
        this(pool, null);
    }

    public TreeNode(ResourceLocation pool, @Nullable String parentEdge) {
        this.pool = pool;
        this.parentEdge = parentEdge == null ? null : new ResourceLocation(parentEdge).toString();
    }

    public TreeNode addChild(String edgeName, String childPool) {
        TreeNode child = new TreeNode(new ResourceLocation(childPool), edgeName);
        this.children.add(child);
        return child;
    }

    public void walk(Consumer<TreeNode> consumer) {
        consumer.accept(this);
        for (TreeNode child : this.children) {
            child.walk(consumer);
        }
    }

    public ResourceLocation getPool() {
        return this.pool;
    }

    @Nullable
    public String getParentEdge() {
        return this.parentEdge;
    }

    public List<TreeNode> getChildren() {
        return this.children;
    }

    public List<ResourceLocation> getLowPriorityProcessors() {
        return this.lowPriorityProcessors;
    }

    public List<ResourceLocation> getHighPriorityProcessors() {
        return this.highPriorityProcessors;
    }

    public String getUniqueGroup() {
        return this.uniqueGroup;
    }

    public void setUniqueGroup(String uniqueGroup) {
        this.uniqueGroup = uniqueGroup;
    }

    public int getMinEdgeDistance() {
        return this.minEdgeDistance;
    }

    public void setMinEdgeDistance(int minEdgeDistance) {
        this.minEdgeDistance = minEdgeDistance;
    }

    public Function<String, TreeNode> getFallbackNodeProvider() {
        return this.fallbackNodeProvider;
    }

    public void setFallbackNodeProvider(Function<String, TreeNode> fallbackNodeProvider) {
        this.fallbackNodeProvider = fallbackNodeProvider;
    }

    public CompoundTag getData() {
        return this.data;
    }

    public void setData(CompoundTag data) {
        this.data = data;
    }

    public boolean isOffsetTowardsJigsawFront() {
        return this.offsetTowardsJigsawFront;
    }

    public void setOffsetTowardsJigsawFront(boolean offsetTowardsJigsawFront) {
        this.offsetTowardsJigsawFront = offsetTowardsJigsawFront;
    }

    public boolean isCheckForCollisions() {
        return this.checkForCollisions;
    }

    public void setCheckForCollisions(boolean checkForCollisions) {
        this.checkForCollisions = checkForCollisions;
    }
}