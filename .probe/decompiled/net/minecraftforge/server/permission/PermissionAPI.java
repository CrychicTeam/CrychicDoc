package net.minecraftforge.server.permission;

import java.lang.StackWalker.Option;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.exceptions.UnregisteredPermissionException;
import net.minecraftforge.server.permission.handler.DefaultPermissionHandler;
import net.minecraftforge.server.permission.handler.IPermissionHandler;
import net.minecraftforge.server.permission.handler.IPermissionHandlerFactory;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContext;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public final class PermissionAPI {

    private static final Logger LOGGER = LogManager.getLogger();

    private static IPermissionHandler activeHandler = null;

    public static Collection<PermissionNode<?>> getRegisteredNodes() {
        return activeHandler == null ? Collections.emptySet() : activeHandler.getRegisteredNodes();
    }

    private PermissionAPI() {
    }

    @Nullable
    public static ResourceLocation getActivePermissionHandler() {
        return activeHandler == null ? null : activeHandler.getIdentifier();
    }

    public static <T> T getPermission(ServerPlayer player, PermissionNode<T> node, PermissionDynamicContext<?>... context) {
        if (!activeHandler.getRegisteredNodes().contains(node)) {
            throw new UnregisteredPermissionException(node);
        } else {
            return activeHandler.getPermission(player, node, context);
        }
    }

    public static <T> T getOfflinePermission(UUID player, PermissionNode<T> node, PermissionDynamicContext<?>... context) {
        if (!activeHandler.getRegisteredNodes().contains(node)) {
            throw new UnregisteredPermissionException(node);
        } else {
            return activeHandler.getOfflinePermission(player, node, context);
        }
    }

    public static void initializePermissionAPI() {
        Class<?> callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        if (callerClass != ServerLifecycleHooks.class) {
            LOGGER.warn("{} tried to initialize the PermissionAPI, this call will be ignored.", callerClass.getName());
        } else {
            activeHandler = null;
            PermissionGatherEvent.Handler handlerEvent = new PermissionGatherEvent.Handler();
            MinecraftForge.EVENT_BUS.post(handlerEvent);
            Map<ResourceLocation, IPermissionHandlerFactory> availableHandlers = handlerEvent.getAvailablePermissionHandlerFactories();
            try {
                ResourceLocation selectedPermissionHandler = new ResourceLocation(ForgeConfig.SERVER.permissionHandler.get());
                if (!availableHandlers.containsKey(selectedPermissionHandler)) {
                    LOGGER.error("Unable to find configured permission handler {}, will use {}", selectedPermissionHandler, DefaultPermissionHandler.IDENTIFIER);
                    selectedPermissionHandler = DefaultPermissionHandler.IDENTIFIER;
                }
                IPermissionHandlerFactory factory = (IPermissionHandlerFactory) availableHandlers.get(selectedPermissionHandler);
                PermissionGatherEvent.Nodes nodesEvent = new PermissionGatherEvent.Nodes();
                MinecraftForge.EVENT_BUS.post(nodesEvent);
                activeHandler = factory.create(nodesEvent.getNodes());
                if (!selectedPermissionHandler.equals(activeHandler.getIdentifier())) {
                    LOGGER.warn("Identifier for permission handler {} does not match registered one {}", activeHandler.getIdentifier(), selectedPermissionHandler);
                }
                LOGGER.info("Successfully initialized permission handler {}", activeHandler.getIdentifier());
            } catch (ResourceLocationException var6) {
                LOGGER.error("Error parsing config value 'permissionHandler'", var6);
            }
        }
    }
}