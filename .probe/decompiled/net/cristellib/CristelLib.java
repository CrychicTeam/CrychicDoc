package net.cristellib;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Set;
import net.cristellib.builtinpacks.BuiltInDataPacks;
import net.cristellib.builtinpacks.RuntimePack;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CristelLib {

    public static final String MOD_ID = "cristellib";

    public static final Logger LOGGER = LogManager.getLogger("Cristel Lib");

    public static final RuntimePack DATA_PACK = new RuntimePack("Runtime Pack", SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA), CristelLibExpectPlatform.getResourceDirectory("cristellib", "pack.png"));

    public static final String minTerraBlenderVersion = "3.0.0.170";

    private static final CristelLibRegistry REGISTRY = new CristelLibRegistry();

    public static void init() {
    }

    public static void preInit() {
        BuiltInDataPacks.registerPack(DATA_PACK, Component.literal("Cristel Lib Config Pack"), () -> true);
        CristelLibRegistry.configs = ImmutableMap.copyOf(CristelLibExpectPlatform.getConfigs(REGISTRY));
        UnmodifiableIterator var0 = CristelLibRegistry.getConfigs().values().iterator();
        while (var0.hasNext()) {
            Set<StructureConfig> pack = (Set<StructureConfig>) var0.next();
            for (StructureConfig config : pack) {
                config.writeConfig();
                config.addSetsToRuntimePack();
            }
        }
    }
}