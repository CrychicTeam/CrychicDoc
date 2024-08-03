package net.minecraftforge.server.permission.nodes;

import com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PermissionNode<T> {

    private final String nodeName;

    private final PermissionType<T> type;

    private final PermissionNode.PermissionResolver<T> defaultResolver;

    private final PermissionDynamicContextKey<?>[] dynamics;

    @Nullable
    private Component readableName;

    @Nullable
    private Component description;

    public PermissionNode(ResourceLocation nodeName, PermissionType<T> type, PermissionNode.PermissionResolver<T> defaultResolver, PermissionDynamicContextKey... dynamics) {
        this(nodeName.getNamespace(), nodeName.getPath(), type, defaultResolver, dynamics);
    }

    public PermissionNode(String modID, String nodeName, PermissionType<T> type, PermissionNode.PermissionResolver<T> defaultResolver, PermissionDynamicContextKey... dynamics) {
        this(modID + "." + nodeName, type, defaultResolver, dynamics);
    }

    private PermissionNode(String nodeName, PermissionType<T> type, PermissionNode.PermissionResolver<T> defaultResolver, PermissionDynamicContextKey... dynamics) {
        this.nodeName = nodeName;
        this.type = type;
        this.dynamics = dynamics;
        this.defaultResolver = defaultResolver;
    }

    public PermissionNode setInformation(@NotNull Component readableName, @NotNull Component description) {
        Preconditions.checkNotNull(readableName, "Readable name for PermissionNodes must not be null %s", this.nodeName);
        Preconditions.checkNotNull(description, "Description for PermissionNodes must not be null %s", this.nodeName);
        this.readableName = readableName;
        this.description = description;
        return this;
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public PermissionType<T> getType() {
        return this.type;
    }

    public PermissionDynamicContextKey<?>[] getDynamics() {
        return this.dynamics;
    }

    public PermissionNode.PermissionResolver<T> getDefaultResolver() {
        return this.defaultResolver;
    }

    @Nullable
    public Component getReadableName() {
        return this.readableName;
    }

    @Nullable
    public Component getDescription() {
        return this.description;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof PermissionNode otherNode) ? false : this.nodeName.equals(otherNode.nodeName) && this.type.equals(otherNode.type);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.nodeName, this.type });
    }

    @FunctionalInterface
    public interface PermissionResolver<T> {

        T resolve(@Nullable ServerPlayer var1, UUID var2, PermissionDynamicContext<?>... var3);
    }
}