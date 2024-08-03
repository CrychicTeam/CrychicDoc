package dev.kosmx.playerAnim.impl.forge;

import java.io.IOException;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.spongepowered.asm.service.MixinService;

@Internal
public class HelperImpl {

    public static boolean isBendyLibPresent() {
        return hasClass("io.github.kosmx.bendylib.impl.ICuboid");
    }

    private static boolean hasClass(String name) {
        try {
            MixinService.getService().getBytecodeProvider().getClassNode(name);
            return true;
        } catch (IOException | ClassNotFoundException var2) {
            return false;
        }
    }
}