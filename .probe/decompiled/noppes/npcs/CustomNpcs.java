package noppes.npcs;

import com.google.common.collect.Maps;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import nikedemos.markovnames.generators.MarkovGenerator;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.command.CmdNoppes;
import noppes.npcs.command.CmdSchematics;
import noppes.npcs.config.ConfigLoader;
import noppes.npcs.config.ConfigProp;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.ChunkController;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.GlobalDataController;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.VisibilityController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.ScoreBoardMixin;
import noppes.npcs.packets.Packets;
import noppes.npcs.shared.common.util.LogWriter;

@Mod("customnpcs")
public class CustomNpcs {

    public static final String MODID = "customnpcs";

    public static final String VERSION = "1.16";

    @ConfigProp(info = "Whether scripting is enabled or not")
    public static boolean EnableScripting = true;

    @ConfigProp(info = "Arguments given to the Nashorn scripting library")
    public static String NashorArguments = "-strict";

    @ConfigProp(info = "Disable Chat Bubbles")
    public static boolean EnableChatBubbles = true;

    @ConfigProp(info = "Navigation search range for NPCs. Not recommended to increase if you have a slow pc or on a server")
    public static int NpcNavRange = 32;

    @ConfigProp(info = "Limit too how many npcs can be in one chunk for natural spawning")
    public static int NpcNaturalSpawningChunkLimit = 4;

    @ConfigProp(info = "Set to true if you want the dialog command option to be able to use op commands like tp etc")
    public static boolean NpcUseOpCommands = false;

    @ConfigProp(info = "If set to true only opped people can use the /noppes command")
    public static boolean NoppesCommandOpOnly = false;

    @ConfigProp
    public static boolean InventoryGuiEnabled = true;

    public static boolean FixUpdateFromPre_1_12 = false;

    @ConfigProp(info = "If you are running sponge and you want to disable the permissions set this to true")
    public static boolean DisablePermissions = false;

    @ConfigProp
    public static boolean SceneButtonsEnabled = true;

    public static long ticks;

    public static CommonProxy proxy = (CommonProxy) DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    @ConfigProp(info = "Enables CustomNpcs startup update message")
    public static boolean EnableUpdateChecker = true;

    public static CustomNpcs instance;

    public static boolean FreezeNPCs = false;

    @ConfigProp(info = "Only ops can create and edit npcs")
    public static boolean OpsOnly = false;

    @ConfigProp(info = "Default interact line. Leave empty to not have one")
    public static String DefaultInteractLine = "Hello @p";

    @ConfigProp(info = "Number of chunk loading npcs that can be active at the same time")
    public static int ChuckLoaders = 20;

    public static File Dir;

    @ConfigProp(info = "Enables leaves decay")
    public static boolean LeavesDecayEnabled = true;

    @ConfigProp(info = "Enables Vine Growth")
    public static boolean VineGrowthEnabled = true;

    @ConfigProp(info = "Enables Ice Melting")
    public static boolean IceMeltsEnabled = true;

    @ConfigProp(info = "Normal players can use soulstone on animals")
    public static boolean SoulStoneAnimals = true;

    @ConfigProp(info = "Normal players can use soulstone on all npcs")
    public static boolean SoulStoneNPCs = false;

    @ConfigProp(info = "Type 0 = Normal, Type 1 = Solid")
    public static int HeadWearType = 1;

    @ConfigProp(info = "When set to Minecraft it will use minecrafts font, when Default it will use OpenSans. Can only use fonts installed on your PC")
    public static String FontType = "Default";

    @ConfigProp(info = "Font size for custom fonts (doesn't work with minecrafts font)")
    public static int FontSize = 18;

    @ConfigProp(info = "On some servers or with certain plugins, it doesnt work, so you can disable it here")
    public static boolean EnableInvisibleNpcs = true;

    @ConfigProp
    public static boolean NpcSpeachTriggersChatEvent = false;

    public static ConfigLoader Config;

    public static boolean VerboseDebug = false;

    public static MinecraftServer Server;

    public CustomNpcs() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        CustomTabs.CREATIVE_TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
        File dir = new File(FMLPaths.CONFIGDIR.get().toFile(), "..");
        Dir = new File(dir, "customnpcs");
        if (!Dir.exists()) {
            Dir.mkdir();
        }
        Config = new ConfigLoader(this.getClass(), new File(dir, "config"), "CustomNpcs");
        Config.loadConfig();
    }

    private void postLoad(FMLLoadCompleteEvent event) {
        proxy.postload();
        CustomItems.registerDispenser();
    }

    private void setup(FMLCommonSetupEvent event) {
        if (NpcNavRange < 16) {
            NpcNavRange = 16;
        }
        Packets.register();
        MinecraftForge.EVENT_BUS.register(new ServerEventsHandler());
        MinecraftForge.EVENT_BUS.register(new ServerTickHandler());
        MinecraftForge.EVENT_BUS.register(proxy);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new CustomNpcsPermissions());
        NpcAPI.Instance().events().register(new AbilityEventHandler());
        proxy.load();
        PixelmonHelper.load();
        ScriptController controller = new ScriptController();
        if (EnableScripting && controller.languages.size() > 0) {
            MinecraftForge.EVENT_BUS.register(controller);
            MinecraftForge.EVENT_BUS.register(new ScriptPlayerEventHandler().registerForgeEvents());
            MinecraftForge.EVENT_BUS.register(new ScriptItemEventHandler());
        }
        setPrivateValue(RangedAttribute.class, (RangedAttribute) Attributes.MAX_HEALTH, Double.MAX_VALUE, 1);
        new RecipeController();
    }

    @SubscribeEvent
    public void setAboutToStart(ServerAboutToStartEvent event) {
        Availability.scores.clear();
        Server = event.getServer();
        MarkovGenerator.load();
        ChunkController.instance.clear();
        FactionController.instance.load();
        new PlayerDataController();
        new TransportController();
        new GlobalDataController();
        new SpawnController();
        new LinkedNpcController();
        new MassBlockController();
        VisibilityController.instance = new VisibilityController();
        ScriptController.Instance.loadCategories();
        ScriptController.Instance.loadStoredData();
        ScriptController.Instance.loadPlayerScripts();
        ScriptController.Instance.loadForgeScripts();
        ScriptController.HasStart = false;
        WrapperNpcAPI.clearCache();
        CmdSchematics.names.clear();
        CmdSchematics.names.addAll(SchematicController.Instance.list());
    }

    @SubscribeEvent
    public void started(ServerStartedEvent event) {
        RecipeController.instance.load();
        new BankController();
        DialogController.instance.load();
        QuestController.instance.load();
        ScriptController.HasStart = true;
        ServerCloneController.Instance = new ServerCloneController();
    }

    @SubscribeEvent
    public void stopped(ServerStoppedEvent event) {
        ServerCloneController.Instance = null;
        Server = null;
    }

    @SubscribeEvent
    public void serverstart(ServerStartingEvent event) {
        EntityNPCInterface.ChatEventPlayer = new FakePlayer(event.getServer().getLevel(Level.OVERWORLD), EntityNPCInterface.ChatEventProfile);
        EntityNPCInterface.CommandPlayer = new FakePlayer(event.getServer().getLevel(Level.OVERWORLD), EntityNPCInterface.CommandProfile);
        EntityNPCInterface.GenericPlayer = new FakePlayer(event.getServer().getLevel(Level.OVERWORLD), EntityNPCInterface.GenericProfile);
        for (ServerLevel level : Server.getAllLevels()) {
            ServerScoreboard board = level.getScoreboard();
            board.addDirtyListener(() -> {
                for (String objective : Availability.scores) {
                    Objective so = board.m_83477_(objective);
                    if (so != null) {
                        for (ServerPlayer player : Server.getPlayerList().getPlayers()) {
                            if (!board.m_83461_(player.m_6302_(), so) && board.getObjectiveDisplaySlotCount(so) == 0) {
                                player.connection.send(new ClientboundSetObjectivePacket(so, 0));
                            }
                            ScoreBoardMixin mixin = (ScoreBoardMixin) board;
                            Map<Objective, Score> map = (Map<Objective, Score>) mixin.getScores().computeIfAbsent(player.m_6302_(), p_197898_0_ -> Maps.newHashMap());
                            Score sco = (Score) map.computeIfAbsent(so, ob -> new Score(board, ob, player.m_6302_()));
                            player.connection.send(new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, so.getName(), sco.getOwner(), sco.getScore()));
                        }
                    }
                }
            });
            board.addDirtyListener(() -> {
                for (ServerPlayer playerMP : Server.getPlayerList().getPlayers()) {
                    VisibilityController.instance.onUpdate(playerMP);
                }
            });
        }
    }

    @SubscribeEvent
    public void registerCommand(RegisterCommandsEvent e) {
        CmdNoppes.register(e.getDispatcher());
    }

    public static File getLevelSaveDirectory() {
        return getLevelSaveDirectory(null);
    }

    public static File getLevelSaveDirectory(String s) {
        try {
            File dir = new File(".");
            if (Server != null) {
                if (!Server.isDedicatedServer()) {
                    new File(Minecraft.getInstance().gameDirectory, "saves");
                }
                dir = Server.getWorldPath(new LevelResource("customnpcs")).toFile();
            }
            if (s != null) {
                dir = new File(dir, s);
            }
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir;
        } catch (Exception var2) {
            LogWriter.error("Error getting worldsave", var2);
            return null;
        }
    }

    public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, int fieldIndex) {
        try {
            Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            f.set(instance, value);
        } catch (IllegalAccessException var5) {
            LogWriter.error("setPrivateValue error", var5);
        }
    }
}