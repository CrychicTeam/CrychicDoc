package icyllis.modernui.mc;

import com.mojang.blaze3d.platform.InputConstants;
import icyllis.modernui.annotation.MainThread;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.view.KeyEvent;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;

public interface ScreenCallback {

    @UiThread
    default boolean isBackKey(int keyCode, @Nonnull KeyEvent event) {
        if (keyCode == 256) {
            return true;
        } else {
            InputConstants.Key key = InputConstants.getKey(keyCode, event.getScanCode());
            return MuiModApi.get().isKeyBindingMatches(Minecraft.getInstance().options.keyInventory, key);
        }
    }

    @MainThread
    default boolean shouldClose() {
        return true;
    }

    @MainThread
    default boolean isPauseScreen() {
        return false;
    }

    @RenderThread
    default boolean hasDefaultBackground() {
        return true;
    }

    @RenderThread
    default boolean shouldBlurBackground() {
        return true;
    }
}