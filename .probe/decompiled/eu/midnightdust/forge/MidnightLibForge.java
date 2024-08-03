package eu.midnightdust.forge;

import eu.midnightdust.core.MidnightLibClient;
import eu.midnightdust.core.MidnightLibServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;

@Mod("midnightlib")
public class MidnightLibForge {

    public MidnightLibForge() {
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (remote, server) -> true));
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> MidnightLibClient::onInitializeClient);
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> MidnightLibServer::onInitializeServer);
    }
}