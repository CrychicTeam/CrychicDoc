package moe.wolfgirl.probejs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("probejs")
public class ProbeJS {

    public static final String MOD_ID = "probejs";

    public static final Logger LOGGER = LogManager.getLogger("probejs");

    public static final Gson GSON = new GsonBuilder().serializeSpecialFloatingPointValues().setLenient().disableHtmlEscaping().create();

    public static final Gson GSON_WRITER = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public ProbeJS() {
        EventBuses.registerModEventBus("probejs", FMLJavaModLoadingContext.get().getModEventBus());
    }
}