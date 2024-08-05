package icyllis.modernui.mc;

import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.fragment.Fragment;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.Screen;

public interface MuiScreen {

    @Nonnull
    Screen self();

    @Nonnull
    Fragment getFragment();

    @Nullable
    ScreenCallback getCallback();

    @Nullable
    Screen getPreviousScreen();

    boolean isMenuScreen();

    @UiThread
    void onBackPressed();
}