package net.minecraftforge.server.permission.handler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContext;
import net.minecraftforge.server.permission.nodes.PermissionNode;

public final class DefaultPermissionHandler implements IPermissionHandler {

    public static final ResourceLocation IDENTIFIER = new ResourceLocation("forge", "default_handler");

    private final Set<PermissionNode<?>> registeredNodes = new HashSet();

    private Set<PermissionNode<?>> immutableRegisteredNodes = Collections.unmodifiableSet(this.registeredNodes);

    public DefaultPermissionHandler(Collection<PermissionNode<?>> permissions) {
        this.registeredNodes.addAll(permissions);
    }

    @Override
    public ResourceLocation getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<PermissionNode<?>> getRegisteredNodes() {
        return this.immutableRegisteredNodes;
    }

    @Override
    public <T> T getPermission(ServerPlayer player, PermissionNode<T> node, PermissionDynamicContext<?>... context) {
        return node.getDefaultResolver().resolve(player, player.m_20148_(), context);
    }

    @Override
    public <T> T getOfflinePermission(UUID player, PermissionNode<T> node, PermissionDynamicContext<?>... context) {
        return node.getDefaultResolver().resolve(null, player, context);
    }
}