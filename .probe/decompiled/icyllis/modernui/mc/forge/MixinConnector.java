package icyllis.modernui.mc.forge;

import icyllis.modernui.ModernUI;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

@Deprecated
public class MixinConnector implements IMixinConnector {

    public void connect() {
        Mixins.addConfiguration("mixins.modernui-forge.json");
        ModernUI.LOGGER.debug(ModernUI.MARKER, "Connected to mixin system");
    }
}