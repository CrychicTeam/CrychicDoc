package net.minecraftforge.server.permission.handler;

import java.util.Set;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContext;
import net.minecraftforge.server.permission.nodes.PermissionNode;

public interface IPermissionHandler {

    ResourceLocation getIdentifier();

    Set<PermissionNode<?>> getRegisteredNodes();

    <T> T getPermission(ServerPlayer var1, PermissionNode<T> var2, PermissionDynamicContext<?>... var3);

    <T> T getOfflinePermission(UUID var1, PermissionNode<T> var2, PermissionDynamicContext<?>... var3);
}