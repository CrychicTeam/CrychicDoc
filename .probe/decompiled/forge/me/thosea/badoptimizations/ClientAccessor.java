package forge.me.thosea.badoptimizations;

import net.minecraft.client.Minecraft;

public interface ClientAccessor {

    void badoptimizations$updateFpsString();

    static boolean shouldUpdateFpsString(Minecraft client) {
        return client.options.renderDebug && (!client.options.hideGui || client.screen != null);
    }
}