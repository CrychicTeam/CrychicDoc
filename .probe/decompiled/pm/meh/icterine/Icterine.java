package pm.meh.icterine;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import pm.meh.icterine.forge.ReloadListenerHandler;

@Mod("icterine")
public class Icterine {

    public Icterine() {
        Common.init();
        MinecraftForge.EVENT_BUS.register(ReloadListenerHandler.class);
    }
}