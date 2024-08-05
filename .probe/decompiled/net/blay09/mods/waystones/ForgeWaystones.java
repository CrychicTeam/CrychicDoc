package net.blay09.mods.waystones;

import java.lang.reflect.InvocationTargetException;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.waystones.client.WaystonesClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("waystones")
public class ForgeWaystones {

    private static final Logger logger = LoggerFactory.getLogger(ForgeWaystones.class);

    public ForgeWaystones() {
        Balm.initialize("waystones", Waystones::initialize);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> BalmClient.initialize("waystones", WaystonesClient::initialize));
        Balm.initializeIfLoaded("theoneprobe", "net.blay09.mods.waystones.compat.TheOneProbeIntegration");
        if (Balm.isModLoaded("repurposed_structures")) {
            try {
                Class.forName("net.blay09.mods.waystones.compat.RepurposedStructuresIntegration").getConstructor().newInstance();
            } catch (IllegalAccessException | NoSuchMethodException | ClassNotFoundException | InvocationTargetException | InstantiationException var2) {
                logger.error("Failed to load Repurposed Structures integration", var2);
            }
        }
    }
}