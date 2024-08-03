package io.wispforest.lmft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.logging.LogUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public class LMFTCommon {

    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "lmft";

    public static boolean areTagsCooked = false;

    public static boolean disableIngameError = Boolean.getBoolean("lmft.disable_error_output");

    public static void init(Path configPath) {
        File configFile = new File(configPath + File.separator + "lmft.json");
        JsonObject configObject = null;
        try {
            if (configFile.createNewFile()) {
                LOGGER.info("[LMFT]: Unable to find needed config file, will attempt to create such.");
                configObject = new JsonObject();
                configObject.addProperty("disableIngameError", false);
                FileWriter writer = new FileWriter(configFile);
                try {
                    writer.write(GSON.toJson(configObject));
                } catch (Throwable var7) {
                    try {
                        writer.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                    throw var7;
                }
                writer.close();
            } else {
                configObject = (JsonObject) GSON.fromJson(Files.readString(configFile.toPath()), JsonObject.class);
                if (configObject == null) {
                    throw new IOException("[LMFT]: The config file was not found!");
                }
                LOGGER.info("[LMFT]: Loaded Config File!");
            }
        } catch (IOException var8) {
            LOGGER.error("[LMFT]: Unable to create the needed config file, using default values!", var8);
        } catch (JsonSyntaxException var9) {
            LOGGER.error("[LMFT]: Unable to read the needed config file, using default values!", var9);
        }
        if (configObject != null && configObject.has("disableIngameError")) {
            disableIngameError = disableIngameError || configObject.get("disableIngameError").getAsBoolean();
        }
    }

    public static void sendMessage(Player entity) {
        if (areTagsCooked && !disableIngameError) {
            entity.displayClientMessage(Component.literal("[Load My F***ing Tags]: It seems that some tags are a bit cooked. Look at the Logs for more details on broken functions. Click me for more info about this feature.").withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Dragon-Seeker/LoadMyFingTags/blob/3961e898550c4d996199bea0fa408a61e87e8dba/info.md"))).withStyle(ChatFormatting.RED, ChatFormatting.BOLD), false);
        }
    }
}