package journeymap.client;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.api.impl.OptionsDisplayFactory;
import journeymap.client.api.util.PluginHelper;
import journeymap.client.cartography.ChunkRenderController;
import journeymap.client.cartography.color.ColorPalette;
import journeymap.client.data.DataCache;
import journeymap.client.event.dispatchers.CustomEventDispatcher;
import journeymap.client.event.dispatchers.forge.ForgeEventDispatcher;
import journeymap.client.event.forge.ForgeEventHandlerManager;
import journeymap.client.event.forge.ForgeKeyEvents;
import journeymap.client.event.handlers.ChunkMonitorHandler;
import journeymap.client.io.FileHandler;
import journeymap.client.io.IconSetFileHandler;
import journeymap.client.io.ThemeLoader;
import journeymap.client.log.ChatLog;
import journeymap.client.log.JMLogger;
import journeymap.client.log.StatTimer;
import journeymap.client.model.RegionImageCache;
import journeymap.client.properties.CoreProperties;
import journeymap.client.properties.FullMapProperties;
import journeymap.client.properties.MiniMapProperties;
import journeymap.client.properties.TopoProperties;
import journeymap.client.properties.WaypointProperties;
import journeymap.client.properties.WebMapProperties;
import journeymap.client.render.map.TileDrawStepCache;
import journeymap.client.service.webmap.Webmap;
import journeymap.client.task.main.IMainThreadTask;
import journeymap.client.task.main.MainTaskController;
import journeymap.client.task.main.MappingMonitorTask;
import journeymap.client.task.multi.ITaskManager;
import journeymap.client.task.multi.TaskController;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.migrate.Migration;
import journeymap.common.nbt.RegionDataStorageHandler;
import journeymap.common.network.dispatch.ClientNetworkDispatcher;
import journeymap.common.network.handler.ClientPacketHandler;
import journeymap.common.version.VersionCheck;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = { Dist.CLIENT }, modid = "journeymap")
public class JourneymapClient {

    public static final String FULL_VERSION = Journeymap.MC_VERSION + "-" + Journeymap.JM_VERSION;

    public static final String MOD_NAME = Journeymap.SHORT_MOD_NAME + " " + FULL_VERSION;

    private volatile String currentWorldId = null;

    private volatile CoreProperties coreProperties;

    private volatile FullMapProperties fullMapProperties;

    private volatile MiniMapProperties miniMapProperties1;

    private volatile MiniMapProperties miniMapProperties2;

    private volatile TopoProperties topoProperties;

    private volatile WebMapProperties webMapProperties;

    private volatile WaypointProperties waypointProperties;

    private volatile OptionsDisplayFactory optionsDisplayFactory;

    private volatile Boolean initialized = false;

    private final InternalStateHandler stateHandler;

    private final ForgeKeyEvents keyEvents;

    private Logger logger = LogManager.getLogger("journeymap");

    private boolean threadLogging = false;

    private ClientNetworkDispatcher dispatcher;

    private ClientPacketHandler packetHandler;

    private final MainTaskController mainThreadTaskController = new MainTaskController();

    private TaskController multithreadTaskController;

    private ChunkRenderController chunkRenderController;

    private static JourneymapClient instance;

    public boolean hasOptifine = false;

    public JourneymapClient() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetupEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetupEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadCompleteEvent);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeKeyEvents::onKeyRegisterEvent);
        this.keyEvents = new ForgeKeyEvents();
        this.stateHandler = new InternalStateHandler();
        CustomEventDispatcher.init(new ForgeEventDispatcher());
    }

    public void commonSetupEvent(FMLCommonSetupEvent event) {
        try {
            PluginHelper.INSTANCE.preInitPlugins(getClientPluginScanResult());
        } catch (Throwable var3) {
            var3.printStackTrace();
        }
    }

    public void clientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Journeymap.getLogger().info("Journeymap Initializing");
            this.packetHandler = new ClientPacketHandler();
            this.dispatcher = new ClientNetworkDispatcher(Journeymap.getInstance().getNetworkHandler());
            this.init();
        });
    }

    public void loadCompleteEvent(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            Journeymap.getLogger().info("Journeymap PostInit");
            this.postInit();
        });
    }

    private void init() {
        StatTimer timer = null;
        try {
            timer = StatTimer.getDisposable("elapsed").start();
            boolean migrationOk = new Migration("journeymap.client.task.migrate").performTasks();
            this.logger = JMLogger.init();
            this.logger.info("initialize ENTER");
            if (this.initialized) {
                this.logger.warn("Already initialized, aborting");
            } else {
                PluginHelper.INSTANCE.initPlugins(ClientAPI.INSTANCE);
                this.logger.debug("Loading and Generating Journeymap configs");
                this.loadConfigProperties();
                JMLogger.logProperties();
                this.threadLogging = false;
                ForgeEventHandlerManager.registerHandlers();
                this.keyEvents.getHandler().registerActions();
                this.logger.info("initialize EXIT, " + (timer == null ? "" : timer.getLogReportString()));
            }
        } catch (Throwable var3) {
            if (this.logger == null) {
                this.logger = LogManager.getLogger("journeymap");
            }
            this.logger.error(LogFormatter.toString(var3));
            throw var3;
        }
    }

    private void postInit() {
        StatTimer timer = null;
        try {
            this.logger.debug("postInitialize ENTER");
            timer = StatTimer.getDisposable("elapsed").start();
            this.queueMainThreadTask(new MappingMonitorTask());
            IconSetFileHandler.initialize();
            ThemeLoader.initialize(true);
            if (this.webMapProperties.enabled.get()) {
                Webmap.INSTANCE.start();
            }
            ChatLog.announceMod(false);
            this.initialized = true;
            VersionCheck.getVersionAvailable();
            this.optifineCheck();
        } catch (Throwable var6) {
            if (this.logger == null) {
                this.logger = LogManager.getLogger("journeymap");
            }
            this.logger.error(LogFormatter.toString(var6));
        } finally {
            this.logger.info("postInitialize EXIT, " + (timer == null ? "" : timer.getLogReportString()));
        }
        JMLogger.setLevelFromProperties();
    }

    private void optifineCheck() {
        try {
            Class optifine = Class.forName("net.optifine.override.ChunkCacheOF");
            this.logger.info("OptiFine detected.");
            this.hasOptifine = optifine != null;
        } catch (ClassNotFoundException var2) {
            this.hasOptifine = false;
        }
    }

    public CoreProperties getCoreProperties() {
        return this.coreProperties;
    }

    public FullMapProperties getFullMapProperties() {
        return this.fullMapProperties;
    }

    public TopoProperties getTopoProperties() {
        return this.topoProperties;
    }

    public void disable() {
        this.initialized = false;
        ForgeEventHandlerManager.unregisterAll();
        this.stopMapping();
        ClientAPI.INSTANCE.purge();
        DataCache.INSTANCE.purge();
    }

    public void enable() {
        if (!this.initialized) {
        }
    }

    public MiniMapProperties getMiniMapProperties(int which) {
        switch(which) {
            case 2:
                this.miniMapProperties2.setActive(true);
                this.miniMapProperties1.setActive(false);
                return this.getMiniMapProperties2();
            default:
                this.miniMapProperties1.setActive(true);
                this.miniMapProperties2.setActive(false);
                return this.getMiniMapProperties1();
        }
    }

    public MiniMapProperties getActiveMiniMapProperties() {
        return this.miniMapProperties1.isActive() ? this.getMiniMapProperties1() : this.getMiniMapProperties2();
    }

    public int getActiveMinimapId() {
        return this.miniMapProperties1.isActive() ? 1 : 2;
    }

    public MiniMapProperties getMiniMapProperties1() {
        return this.miniMapProperties1;
    }

    public MiniMapProperties getMiniMapProperties2() {
        return this.miniMapProperties2;
    }

    public WebMapProperties getWebMapProperties() {
        return this.webMapProperties;
    }

    public WaypointProperties getWaypointProperties() {
        return this.waypointProperties;
    }

    public boolean isUpdateCheckEnabled() {
        return this.getCoreProperties().checkUpdates.get();
    }

    public Boolean isInitialized() {
        return this.initialized;
    }

    public Boolean isMapping() {
        return this.initialized && this.multithreadTaskController != null && this.multithreadTaskController.isActive();
    }

    public Boolean isThreadLogging() {
        return this.threadLogging;
    }

    public Webmap getJmServer() {
        return Webmap.INSTANCE;
    }

    public void queueOneOff(Runnable runnable) throws Exception {
        if (this.multithreadTaskController != null) {
            this.multithreadTaskController.queueOneOff(runnable);
        }
    }

    public void toggleTask(Class<? extends ITaskManager> managerClass, boolean enable, Object params) {
        if (this.multithreadTaskController != null) {
            this.multithreadTaskController.toggleTask(managerClass, enable, params);
        }
    }

    public boolean isTaskManagerEnabled(Class<? extends ITaskManager> managerClass) {
        return this.multithreadTaskController != null ? this.multithreadTaskController.isTaskManagerEnabled(managerClass) : false;
    }

    public boolean isMainThreadTaskActive() {
        return this.mainThreadTaskController != null ? this.mainThreadTaskController.isActive() : false;
    }

    public void startMapping() {
        synchronized (this) {
            Minecraft mc = Minecraft.getInstance();
            if (mc != null && mc.level != null && this.initialized && this.coreProperties.mappingEnabled.get()) {
                File worldDir = FileHandler.getJMWorldDir(mc, this.currentWorldId);
                if (worldDir != null) {
                    if (!worldDir.exists()) {
                        boolean created = worldDir.mkdirs();
                        if (!created) {
                            JMLogger.logOnce("CANNOT CREATE DATA DIRECTORY FOR WORLD: " + worldDir.getPath());
                            return;
                        }
                    }
                    this.reset();
                    this.multithreadTaskController = new TaskController();
                    this.multithreadTaskController.enableTasks();
                    long totalMB = Runtime.getRuntime().totalMemory() / 1024L / 1024L;
                    long freeMB = Runtime.getRuntime().freeMemory() / 1024L / 1024L;
                    String memory = String.format("Memory: %sMB total, %sMB free", totalMB, freeMB);
                    ResourceKey<Level> dimension = mc.level.m_46472_();
                    this.logger.info(String.format("Mapping started in %s%s%s. %s ", FileHandler.getJMWorldDir(mc, this.currentWorldId), File.separator, FileHandler.getDimNameForPath(FileHandler.getJMWorldDir(mc, this.currentWorldId), dimension), memory));
                    if (this.stateHandler.isJourneyMapServerConnection() || this.stateHandler.isModdedServerConnection() || Minecraft.getInstance().hasSingleplayerServer()) {
                        this.dispatcher.sendPermissionRequest();
                    }
                    ClientAPI.INSTANCE.getClientEventManager().fireMappingEvent(true, dimension);
                    UIManager.INSTANCE.getMiniMap().reset();
                }
            }
        }
    }

    private static List<String> getClientPluginScanResult() {
        return (List<String>) ModList.get().getAllScanData().stream().map(ModFileScanData::getAnnotations).flatMap(Collection::stream).filter(annotationData -> Objects.equals(annotationData.annotationType(), PluginHelper.PLUGIN_ANNOTATION_NAME)).map(AnnotationData::memberName).collect(Collectors.toList());
    }

    public void stopMapping() {
        synchronized (this) {
            ChunkMonitorHandler.getInstance().reset();
            Minecraft mc = Minecraft.getInstance();
            if (this.isMapping() && mc != null) {
                this.logger.info(String.format("Mapping halted in %s%s%s", FileHandler.getJMWorldDir(mc, this.currentWorldId), File.separator, mc.level.m_46472_().location()));
                RegionImageCache.INSTANCE.flushToDiskAsync(true);
                ColorPalette colorPalette = ColorPalette.getActiveColorPalette();
                if (colorPalette != null) {
                    colorPalette.writeToFile();
                }
            }
            if (this.multithreadTaskController != null) {
                this.multithreadTaskController.disableTasks();
                this.multithreadTaskController.clear();
                this.multithreadTaskController = null;
            }
            if (mc != null) {
                ResourceKey<Level> dimension = mc.level != null ? mc.level.m_46472_() : Level.OVERWORLD;
                ClientAPI.INSTANCE.getClientEventManager().fireMappingEvent(false, dimension);
            }
        }
    }

    private void reset() {
        this.stateHandler.reset();
        if (!Minecraft.getInstance().hasSingleplayerServer()) {
            this.dispatcher.sendPermissionRequest();
            try {
                this.dispatcher.sendWorldIdRequest();
            } catch (Exception var2) {
                JMLogger.throwLogOnce("WorldId Packet error, likely due to multiple map mods installed", var2);
            }
        }
        this.loadConfigProperties();
        DataCache.INSTANCE.purge();
        ChunkMonitorHandler.getInstance().reset();
        this.chunkRenderController = new ChunkRenderController();
        Fullscreen.state().requireRefresh();
        Fullscreen.state().follow.set(true);
        StatTimer.resetAll();
        TileDrawStepCache.clear();
        UIManager.INSTANCE.getMiniMap().reset();
        UIManager.INSTANCE.reset();
        WaypointStore.INSTANCE.reset();
        RegionDataStorageHandler.getInstance().flushDataCache();
    }

    public void queueMainThreadTask(IMainThreadTask task) {
        this.mainThreadTaskController.addTask(task);
    }

    public void performMainThreadTasks() {
        this.mainThreadTaskController.performTasks();
    }

    public void performMultithreadTasks() {
        try {
            synchronized (this) {
                if (this.isMapping()) {
                    this.multithreadTaskController.performTasks();
                }
            }
        } catch (Throwable var4) {
            String error = "Error in JourneyMap.performMultithreadTasks(): " + var4.getMessage();
            ChatLog.announceError(error);
            this.logger.error(LogFormatter.toString(var4));
        }
    }

    public ChunkRenderController getChunkRenderController() {
        return this.chunkRenderController;
    }

    public void saveConfigProperties() {
        if (this.coreProperties != null) {
            this.coreProperties.save();
        }
        if (this.fullMapProperties != null) {
            this.fullMapProperties.save();
        }
        if (this.miniMapProperties1 != null) {
            this.miniMapProperties1.save();
        }
        if (this.miniMapProperties2 != null) {
            this.miniMapProperties2.save();
        }
        if (this.topoProperties != null) {
            this.topoProperties.save();
        }
        if (this.webMapProperties != null) {
            this.webMapProperties.save();
        }
        if (this.waypointProperties != null) {
            this.waypointProperties.save();
        }
        if (this.optionsDisplayFactory != null) {
            this.optionsDisplayFactory.save();
        }
    }

    public void loadConfigProperties() {
        this.saveConfigProperties();
        this.optionsDisplayFactory = new OptionsDisplayFactory().buildAddonProperties().load();
        this.coreProperties = new CoreProperties().load();
        this.fullMapProperties = new FullMapProperties().load();
        this.miniMapProperties1 = new MiniMapProperties(1).load();
        this.miniMapProperties2 = new MiniMapProperties(2).load();
        this.topoProperties = new TopoProperties().load();
        this.webMapProperties = new WebMapProperties().load();
        this.waypointProperties = new WaypointProperties().load();
    }

    public static JourneymapClient getInstance() {
        return instance;
    }

    public String getCurrentWorldId() {
        return this.currentWorldId;
    }

    public void setCurrentWorldId(String worldId) {
        synchronized (this) {
            Minecraft mc = Minecraft.getInstance();
            if (!mc.hasSingleplayerServer()) {
                if ("".equals(worldId)) {
                    return;
                }
                File currentWorldDirectory = FileHandler.getJMWorldDirForWorldId(mc, this.currentWorldId);
                File newWorldDirectory = FileHandler.getJMWorldDir(mc, worldId);
                boolean worldIdUnchanged = Constants.safeEqual(worldId, this.currentWorldId);
                boolean directoryUnchanged = currentWorldDirectory != null && newWorldDirectory != null && currentWorldDirectory.getPath().equals(newWorldDirectory.getPath());
                if (worldIdUnchanged && directoryUnchanged) {
                    Journeymap.getLogger().debug("World UID hasn't changed: " + worldId);
                    return;
                }
                boolean wasMapping = this.isMapping();
                if (wasMapping) {
                    this.stopMapping();
                }
                this.currentWorldId = worldId;
                Journeymap.getLogger().info("World UID is set to: " + worldId);
            }
        }
    }

    public int getRenderDistance() {
        int gameRenderDistance = Math.max(1, Minecraft.getInstance().options.getEffectiveRenderDistance() - 1);
        if (getInstance().getStateHandler().isJourneyMapServerConnection()) {
            int serverDistance = getInstance().getStateHandler().getMaxRenderDistance();
            gameRenderDistance = serverDistance == 0 ? gameRenderDistance : Math.min(serverDistance, gameRenderDistance);
        }
        return gameRenderDistance;
    }

    public ForgeKeyEvents getKeyEvents() {
        return this.keyEvents;
    }

    public ClientNetworkDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public ClientPacketHandler getPacketHandler() {
        return this.packetHandler;
    }

    public InternalStateHandler getStateHandler() {
        return this.stateHandler;
    }
}