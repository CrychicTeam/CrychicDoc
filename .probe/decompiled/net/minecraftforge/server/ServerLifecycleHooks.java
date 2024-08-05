package net.minecraftforge.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.StructureModifier;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.gametest.ForgeGameTestHooks;
import net.minecraftforge.network.ConnectionType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.resource.PathPackResources;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ServerLifecycleHooks {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Marker SERVERHOOKS = MarkerManager.getMarker("SERVERHOOKS");

    private static final LevelResource SERVERCONFIG = new LevelResource("serverconfig");

    private static volatile CountDownLatch exitLatch = null;

    private static MinecraftServer currentServer;

    private static AtomicBoolean allowLogins = new AtomicBoolean(false);

    private static Path getServerConfigPath(MinecraftServer server) {
        Path serverConfig = server.getWorldPath(SERVERCONFIG);
        if (!Files.isDirectory(serverConfig, new LinkOption[0])) {
            try {
                Files.createDirectories(serverConfig);
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        }
        return serverConfig;
    }

    public static boolean handleServerAboutToStart(MinecraftServer server) {
        currentServer = server;
        LogicalSidedProvider.setServer(() -> server);
        ConfigTracker.INSTANCE.loadConfigs(Type.SERVER, getServerConfigPath(server));
        runModifiers(server);
        return !MinecraftForge.EVENT_BUS.post(new ServerAboutToStartEvent(server));
    }

    public static boolean handleServerStarting(MinecraftServer server) {
        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
            LanguageHook.loadLanguagesOnServer(server);
            if (!(server instanceof GameTestServer)) {
                ForgeGameTestHooks.registerGametests();
            }
        });
        PermissionAPI.initializePermissionAPI();
        return !MinecraftForge.EVENT_BUS.post(new ServerStartingEvent(server));
    }

    public static void handleServerStarted(MinecraftServer server) {
        MinecraftForge.EVENT_BUS.post(new ServerStartedEvent(server));
        allowLogins.set(true);
    }

    public static void handleServerStopping(MinecraftServer server) {
        allowLogins.set(false);
        MinecraftForge.EVENT_BUS.post(new ServerStoppingEvent(server));
    }

    public static void expectServerStopped() {
        exitLatch = new CountDownLatch(1);
    }

    public static void handleServerStopped(MinecraftServer server) {
        if (!server.isDedicatedServer()) {
            GameData.revertToFrozen();
        }
        MinecraftForge.EVENT_BUS.post(new ServerStoppedEvent(server));
        currentServer = null;
        LogicalSidedProvider.setServer(null);
        CountDownLatch latch = exitLatch;
        if (latch != null) {
            latch.countDown();
            exitLatch = null;
        }
        ConfigTracker.INSTANCE.unloadConfigs(Type.SERVER, getServerConfigPath(server));
    }

    public static MinecraftServer getCurrentServer() {
        return currentServer;
    }

    public static boolean handleServerLogin(ClientIntentionPacket packet, Connection manager) {
        if (!allowLogins.get()) {
            MutableComponent text = Component.literal("Server is still starting! Please wait before reconnecting.");
            LOGGER.info(SERVERHOOKS, "Disconnecting Player (server is still starting): {}", text.getContents());
            manager.send(new ClientboundLoginDisconnectPacket(text));
            manager.disconnect(text);
            return false;
        } else {
            if (packet.getIntention() == ConnectionProtocol.LOGIN) {
                ConnectionType connectionType = ConnectionType.forVersionFlag(packet.getFMLVersion());
                int versionNumber = connectionType.getFMLVersionNumber(packet.getFMLVersion());
                if (connectionType == ConnectionType.MODDED && versionNumber != 3) {
                    rejectConnection(manager, connectionType, "This modded server is not impl compatible with your modded client. Please verify your Forge version closely matches the server. Got net version " + versionNumber + " this server is net version 3");
                    return false;
                }
                if (connectionType == ConnectionType.VANILLA && !NetworkRegistry.acceptsVanillaClientConnections()) {
                    rejectConnection(manager, connectionType, "This server has mods that require Forge to be installed on the client. Contact your server admin for more details.");
                    return false;
                }
            }
            if (packet.getIntention() == ConnectionProtocol.STATUS) {
                return true;
            } else {
                NetworkHooks.registerServerLoginChannel(manager, packet);
                return true;
            }
        }
    }

    private static void rejectConnection(Connection manager, ConnectionType type, String message) {
        manager.setProtocol(ConnectionProtocol.LOGIN);
        String ip = "local";
        if (manager.getRemoteAddress() != null) {
            ip = manager.getRemoteAddress().toString();
        }
        LOGGER.info(SERVERHOOKS, "[{}] Disconnecting {} connection attempt: {}", ip, type, message);
        MutableComponent text = Component.literal(message);
        manager.send(new ClientboundLoginDisconnectPacket(text));
        manager.disconnect(text);
    }

    public static void handleExit(int retVal) {
        System.exit(retVal);
    }

    @Internal
    public static RepositorySource buildPackFinder(Map<IModFile, ? extends PathPackResources> modResourcePacks) {
        return packAcceptor -> serverPackFinder(modResourcePacks, packAcceptor);
    }

    private static void serverPackFinder(Map<IModFile, ? extends PathPackResources> modResourcePacks, Consumer<Pack> packAcceptor) {
        for (Entry<IModFile, ? extends PathPackResources> e : modResourcePacks.entrySet()) {
            IModInfo mod = (IModInfo) ((IModFile) e.getKey()).getModInfos().get(0);
            if (!Objects.equals(mod.getModId(), "minecraft")) {
                String name = "mod:" + mod.getModId();
                Pack modPack = Pack.readMetaAndCreate(name, Component.literal(((PathPackResources) e.getValue()).m_5542_()), false, id -> (PackResources) e.getValue(), PackType.SERVER_DATA, Pack.Position.BOTTOM, PackSource.DEFAULT);
                if (modPack == null) {
                    ModLoader.get().addWarning(new ModLoadingWarning(mod, ModLoadingStage.ERROR, "fml.modloading.brokenresources", new Object[] { e.getKey() }));
                } else {
                    LOGGER.debug(Logging.CORE, "Generating PackInfo named {} for mod file {}", name, ((IModFile) e.getKey()).getFilePath());
                    packAcceptor.accept(modPack);
                }
            }
        }
    }

    private static void runModifiers(MinecraftServer server) {
        RegistryAccess registries = server.registryAccess();
        List<BiomeModifier> biomeModifiers = registries.registryOrThrow(ForgeRegistries.Keys.BIOME_MODIFIERS).holders().map(Holder::m_203334_).toList();
        List<StructureModifier> structureModifiers = registries.registryOrThrow(ForgeRegistries.Keys.STRUCTURE_MODIFIERS).holders().map(Holder::m_203334_).toList();
        registries.registryOrThrow(Registries.BIOME).holders().forEach(biomeHolder -> ((Biome) biomeHolder.value()).modifiableBiomeInfo().applyBiomeModifiers(biomeHolder, biomeModifiers));
        registries.registryOrThrow(Registries.STRUCTURE).holders().forEach(structureHolder -> ((Structure) structureHolder.value()).modifiableStructureInfo().applyStructureModifiers(structureHolder, structureModifiers));
    }
}