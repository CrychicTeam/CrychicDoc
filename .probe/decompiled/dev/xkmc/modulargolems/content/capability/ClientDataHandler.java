package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.multiplayer.ClientLevel;

public class ClientDataHandler {

    public static void handleUpdate(GolemConfigEntry init) {
        ClientLevel level = Proxy.getClientWorld();
        if (level != null) {
            GolemConfigStorage storage = GolemConfigStorage.get(level);
            storage.replaceStorage(init);
        }
    }
}