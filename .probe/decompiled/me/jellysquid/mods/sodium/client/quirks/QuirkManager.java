package me.jellysquid.mods.sodium.client.quirks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraftforge.fml.ModList;

public class QuirkManager {

    public static final boolean REBIND_LIGHTMAP_TEXTURE = Stream.of("quartz", "lodestone").allMatch(QuirkManager::isLoaded);

    private static boolean isLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    static {
        try {
            List<String> enabledQuirks = new ArrayList();
            for (Field f : QuirkManager.class.getDeclaredFields()) {
                if (f.getType() == boolean.class && Modifier.isStatic(f.getModifiers()) && f.getBoolean(null)) {
                    enabledQuirks.add(f.getName());
                }
            }
            if (enabledQuirks.size() > 0) {
                SodiumClientMod.logger().warn("Enabled the following quirks in QuirkManager: [{}]", String.join(", ", enabledQuirks));
            }
        } catch (ReflectiveOperationException var5) {
            var5.printStackTrace();
        }
    }
}