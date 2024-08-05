package portb.biggerstacks;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import portb.biggerstacks.config.ClientConfig;
import portb.biggerstacks.config.ServerConfig;
import portb.biggerstacks.event.ClientEvents;
import portb.biggerstacks.event.CommonForgeEvents;
import portb.biggerstacks.event.CommonModEvents;
import portb.biggerstacks.event.ServerEvents;
import portb.configlib.ConfigLib;
import portb.configlib.IMCAPI;
import portb.slw.MyLoggerFactory;

@Mod("biggerstacks")
public class BiggerStacks {

    public static final Logger LOGGER = LogUtils.getLogger();

    public BiggerStacks() {
        if (FMLEnvironment.dist.isClient()) {
            MinecraftForge.EVENT_BUS.register(ClientEvents.class);
        }
        MinecraftForge.EVENT_BUS.register(ServerEvents.class);
        MinecraftForge.EVENT_BUS.register(CommonModEvents.class);
        MinecraftForge.EVENT_BUS.register(CommonForgeEvents.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        ConfigLib.LOGGER = MyLoggerFactory.createMyLogger(LoggerFactory.getLogger(ConfigLib.class));
        registerConfigs();
    }

    private static void registerConfigs() {
        ModLoadingContext context = ModLoadingContext.get();
        if (FMLEnvironment.dist.isClient()) {
            context.registerConfig(Type.CLIENT, ClientConfig.SPEC, "biggerstacks-client.toml");
            context.registerConfig(Type.CLIENT, ServerConfig.LOCAL_INSTANCE.SPEC, "biggerstacks-local.toml");
        }
        context.registerConfig(Type.SERVER, ServerConfig.SERVER_INSTANCE.SPEC, "biggerstacks-server.toml");
    }

    void processIMC(InterModProcessEvent event) {
        event.getIMCStream().forEach(imcMessage -> IMCAPI.addIMCRuleSupplier(imcMessage.senderModId(), imcMessage.messageSupplier()));
    }
}