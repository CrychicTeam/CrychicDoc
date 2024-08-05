package net.minecraftforge.server.permission.handler;

import java.util.Collection;
import net.minecraftforge.server.permission.nodes.PermissionNode;

@FunctionalInterface
public interface IPermissionHandlerFactory {

    IPermissionHandler create(Collection<PermissionNode<?>> var1);
}