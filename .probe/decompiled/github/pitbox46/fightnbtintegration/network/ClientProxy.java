package github.pitbox46.fightnbtintegration.network;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}