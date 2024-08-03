package icyllis.modernui.mc.forge;

import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.mc.BlurHandler;
import icyllis.modernui.mc.MuiScreen;
import icyllis.modernui.mc.UIManager;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;

final class SimpleScreen extends Screen implements MuiScreen {

    private final UIManager mHost;

    @Nullable
    private final Screen mPrevious;

    private final Fragment mFragment;

    @Nullable
    private final icyllis.modernui.mc.ScreenCallback mCallback;

    SimpleScreen(UIManager host, Fragment fragment, @Nullable icyllis.modernui.mc.ScreenCallback callback, @Nullable Screen previous, @Nullable CharSequence title) {
        super((Component) (title != null && !title.isEmpty() ? Component.literal(title.toString()) : CommonComponents.EMPTY));
        this.mHost = host;
        this.mPrevious = previous;
        this.mFragment = (Fragment) Objects.requireNonNull(fragment);
        this.mCallback = callback != null ? callback : (fragment instanceof icyllis.modernui.mc.ScreenCallback cbk ? cbk : null);
    }

    @Override
    protected void init() {
        super.init();
        this.mHost.initScreen(this);
        icyllis.modernui.mc.ScreenCallback callback = this.getCallback();
        if (callback == null || callback.shouldBlurBackground()) {
            BlurHandler.INSTANCE.forceBlur();
        }
    }

    @Override
    public void resize(@Nonnull Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
    }

    @Override
    public void render(@Nonnull GuiGraphics gr, int mouseX, int mouseY, float deltaTick) {
        icyllis.modernui.mc.ScreenCallback callback = this.getCallback();
        if (callback == null || callback.hasDefaultBackground()) {
            BlurHandler.INSTANCE.drawScreenBackground(gr, 0, 0, this.f_96543_, this.f_96544_);
            MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(this, gr));
        }
        this.mHost.render();
    }

    @Override
    public void removed() {
        super.removed();
        this.mHost.removed();
    }

    @Override
    public boolean isPauseScreen() {
        icyllis.modernui.mc.ScreenCallback callback = this.getCallback();
        return callback == null || callback.isPauseScreen();
    }

    @Nonnull
    @Override
    public Screen self() {
        return this;
    }

    @Nonnull
    @Override
    public Fragment getFragment() {
        return this.mFragment;
    }

    @Nullable
    @Override
    public icyllis.modernui.mc.ScreenCallback getCallback() {
        return this.mCallback;
    }

    @Nullable
    @Override
    public Screen getPreviousScreen() {
        return this.mPrevious;
    }

    @Override
    public boolean isMenuScreen() {
        return false;
    }

    @Override
    public void onBackPressed() {
        this.mHost.getOnBackPressedDispatcher().onBackPressed();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        this.mHost.onHoverMove(true);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean charTyped(char ch, int modifiers) {
        return this.mHost.onCharTyped(ch);
    }
}