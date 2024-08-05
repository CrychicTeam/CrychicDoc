package pie.ilikepiefoo.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import pie.ilikepiefoo.KubeJSAdditions;

@Mod("kubejsadditions")
public class KubeJSAdditionsForge {

    public KubeJSAdditionsForge() {
        EventBuses.registerModEventBus("kubejsadditions", FMLJavaModLoadingContext.get().getModEventBus());
        KubeJSAdditions.init();
    }
}