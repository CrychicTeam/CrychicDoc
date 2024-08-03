package net.mehvahdjukaar.modelfix;

import java.util.function.Supplier;
import net.mehvahdjukaar.modelfix.moonlight_configs.ConfigBuilder;
import net.mehvahdjukaar.modelfix.moonlight_configs.ConfigSpec;
import net.mehvahdjukaar.modelfix.moonlight_configs.ConfigType;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelFix {

    public static final String MOD_ID = "modelfix";

    public static final Logger LOGGER = LogManager.getLogger("Model Fix");

    private static final boolean MAC_OS = Util.getPlatform() == Util.OS.OSX;

    public static Supplier<Double> expansion;

    public static Supplier<Double> indent;

    public static Supplier<Double> shrinkMult = () -> 0.0;

    public static ConfigSpec config;

    public static void init(boolean fabric) {
        ConfigBuilder builder = ConfigBuilder.create(new ResourceLocation("modelfix", "client"), ConfigType.CLIENT);
        builder.push("default");
        Supplier<Double> e = builder.comment("quad expansion increment. enlarges each quad. Use to hide gaps. Keep both as close to 0 as possible. Note that increasing it to non 0 will slightly increase the amount of quads per item model. Could be an issue with HD texture packs.Try these values: 0.002 or 0.008").define("item_quads_expansion", fabric ? 0.0 : 0.0, -0.1, 0.1);
        Supplier<Double> i = builder.comment("quad x/y offset. simply put moves the quad toward the center of the item. Use to hide gaps").define("item_quads_indent", fabric ? 1.0E-4 : 0.007, -0.1, 0.1);
        builder.pop();
        builder.push("mac_os").comment("It has been reported that some mac os systems are affected by atlas bleeding so the mod cant apply its main fix by removing atlas shrinking. Instead it can reduce it as much as possible by multiplying it by shrink_value_multiplier");
        Supplier<Double> me = builder.comment("quad expansion increment. enlarges each quad. Use to hide gaps. Keep both as close to 0 as possible. Note that increasing it to non 0 will slightly increase the amount of quads per item model. Could be an issue with HD texture packs").define("item_quads_expansion", 0.0, -0.1, 0.1);
        Supplier<Double> mi = builder.comment("quad x/y offset. simply put moves the quad toward the center of the item. Use to hide gaps").define("item_quads_indent", 0.0099, -0.1, 0.1);
        Supplier<Double> sm = builder.comment("set to 0 for non macos behavior. 1 keeps vanilla behavior to prevent atlas bleeding").define("shrink_ratio_multiplier", 1.0, 0.0, 1.0);
        builder.pop();
        expansion = MAC_OS ? me : e;
        indent = MAC_OS ? mi : i;
        if (MAC_OS) {
            shrinkMult = sm;
        }
        builder.onChange(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && mc.getResourceManager() != null) {
                mc.reloadResourcePacks();
            }
        });
        config = builder.buildAndRegister();
    }

    public static Screen makeScreen(Screen screen) {
        return config.makeScreen(screen);
    }
}