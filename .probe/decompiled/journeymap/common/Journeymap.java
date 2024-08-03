package journeymap.common;

import com.mojang.authlib.GameProfile;
import journeymap.client.JourneymapClient;
import journeymap.common.config.AdminAccessConfig;
import journeymap.common.config.forge.ForgeConfig;
import journeymap.common.events.forge.ForgeServerEvents;
import journeymap.common.nbt.WorldIdData;
import journeymap.common.network.data.NetworkHandler;
import journeymap.common.network.dispatch.NetworkDispatcher;
import journeymap.common.network.forge.ForgeNetworkHandler;
import journeymap.common.network.handler.PacketHandler;
import journeymap.common.properties.PropertiesManager;
import journeymap.common.version.Version;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("journeymap")
@EventBusSubscriber(modid = "journeymap")
public class Journeymap {

    public static final Version MINIMUM_SERVER_ACCEPTABLE_VERSION = new Version(5, 8, 1);

    public static final Version MINIMUM_CLIENT_ACCEPTABLE_VERSION = new Version(5, 8, 1);

    public static final Version DEV_VERSION = new Version(5, 8, 1, "dev");

    public static final String MOD_ID = "journeymap";

    public static final String SHORT_MOD_NAME = JM.SHORT_MOD_NAME;

    public static boolean DEV_MODE = false;

    public static final Version JM_VERSION = Version.from(JM.VERSION_MAJOR, JM.VERSION_MINOR, JM.VERSION_MICRO, JM.VERSION_PATCH, new Version(5, 8, 0, "dev"));

    public static final String LOADER_VERSION = JM.LOADER_VERSION;

    public static final String LOADER_NAME = JM.LOADER_NAME;

    public static final String MC_VERSION = JM.MC_VERSION;

    public static final String WEBSITE_URL = JM.WEBSITE_URL;

    public static final String DOWNLOAD_URL = JM.DOWNLOAD_URL;

    public static final String VERSION_URL = JM.VERSION_URL;

    private static Journeymap instance;

    private NetworkDispatcher dispatcher;

    private PacketHandler handler;

    private MinecraftServer server;

    private NetworkHandler networkHandler;

    public Journeymap() {
        instance = this;
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.register(new JourneymapClient()));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetupEvent);
        MinecraftForge.EVENT_BUS.addListener(this::serverStartingEvent);
        ModLoadingContext.get().registerConfig(Type.SERVER, ForgeConfig.SERVER_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverConfig);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imc);
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (remote, isServer) -> true));
    }

    public static Journeymap getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return LogManager.getLogger("journeymap");
    }

    public static Logger getLogger(String name) {
        return LogManager.getLogger("journeymap/" + name);
    }

    private void imc(InterModEnqueueEvent event) {
        InterModComms.sendTo("darkmodeeverywhere", "dme-shaderblacklist", () -> "journeymap");
    }

    public void commonSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            getLogger().info("Initializing Packet Registries");
            this.networkHandler = new ForgeNetworkHandler();
            this.dispatcher = new NetworkDispatcher(this.networkHandler);
        });
        this.handler = new PacketHandler();
        MinecraftForge.EVENT_BUS.register(new ForgeServerEvents());
    }

    @SubscribeEvent
    public void serverConfig(ModConfigEvent event) {
        if (event.getConfig().getType() == Type.SERVER) {
            AdminAccessConfig.getInstance().load(new ForgeConfig());
        }
    }

    @SubscribeEvent
    public void serverStartingEvent(ServerStartingEvent event) {
        this.server = event.getServer();
        WorldIdData.getWorldId();
        PropertiesManager.getInstance();
    }

    public NetworkDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public PacketHandler getPacketHandler() {
        return this.handler;
    }

    public NetworkHandler getNetworkHandler() {
        return this.networkHandler;
    }

    public static boolean isOp(Player player) {
        if (LogicalSide.CLIENT.equals(EffectiveSide.get())) {
            boolean creative = player.getAbilities().instabuild;
            boolean cheatMode = LoaderHooks.getServer().getPlayerList().isOp(new GameProfile(player.m_20148_(), player.getName().getString()));
            return creative || cheatMode;
        } else {
            return LoaderHooks.getServer().getPlayerList().getOps().m_11388_(player.getGameProfile()) != null || CommonConstants.debugOverride(player);
        }
    }
}