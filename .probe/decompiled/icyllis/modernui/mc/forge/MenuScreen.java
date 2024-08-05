package icyllis.modernui.mc.forge;

import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.mc.MuiScreen;
import icyllis.modernui.mc.UIManager;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

final class MenuScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements MuiScreen {

    private final UIManager mHost;

    private final Fragment mFragment;

    @Nullable
    private final icyllis.modernui.mc.ScreenCallback mCallback;

    MenuScreen(UIManager host, Fragment fragment, @Nullable icyllis.modernui.mc.ScreenCallback callback, T menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.mHost = host;
        this.mFragment = (Fragment) Objects.requireNonNull(fragment);
        this.mCallback = callback != null ? callback : (fragment instanceof icyllis.modernui.mc.ScreenCallback cbk ? cbk : null);
    }

    @Override
    protected void init() {
        super.init();
        this.mHost.initScreen(this);
    }

    @Override
    public void resize(@Nonnull Minecraft minecraft, int width, int height) {
        super.m_6574_(minecraft, width, height);
    }

    @Override
    public void render(@Nonnull GuiGraphics gr, int mouseX, int mouseY, float deltaTick) {
        icyllis.modernui.mc.ScreenCallback callback = this.getCallback();
        if (callback == null || callback.hasDefaultBackground()) {
            this.m_280273_(gr);
        }
        this.mHost.render();
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics gr, float deltaTick, int x, int y) {
    }

    @Override
    public void removed() {
        super.removed();
        this.mHost.removed();
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
        return null;
    }

    @Override
    public boolean isMenuScreen() {
        return true;
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