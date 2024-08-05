package dev.ftb.mods.ftblibrary.ui;

import com.mojang.blaze3d.platform.Window;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.KeyModifiers;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class Widget implements IScreenWrapper, Comparable<Widget> {

    protected final Panel parent;

    public int posX;

    public int posY;

    public int width;

    public int height;

    protected boolean isMouseOver;

    private Widget.DrawLayer drawLayer = Widget.DrawLayer.FOREGROUND;

    public Widget(Panel p) {
        this.parent = p;
    }

    public Panel getParent() {
        return this.parent;
    }

    @Override
    public BaseScreen getGui() {
        return this.parent.getGui();
    }

    public void setX(int v) {
        this.posX = v;
    }

    public void setY(int v) {
        this.posY = v;
    }

    public void setWidth(int v) {
        this.width = Math.max(v, 0);
    }

    public void setHeight(int v) {
        this.height = Math.max(v, 0);
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public final void setPos(int x, int y) {
        this.setX(x);
        this.setY(y);
    }

    public final void setSize(int w, int h) {
        this.setWidth(w);
        this.setHeight(h);
    }

    public final Widget setPosAndSize(int x, int y, int w, int h) {
        this.setX(x);
        this.setY(y);
        this.setWidth(w);
        this.setHeight(h);
        return this;
    }

    public int getX() {
        return this.parent.getX() + this.posX;
    }

    public int getY() {
        return this.parent.getY() + this.posY;
    }

    public boolean collidesWith(int x, int y, int w, int h) {
        int ay = this.getY();
        if (ay < y + h && ay + this.height > y) {
            int ax = this.getX();
            return ax < x + w && ax + this.width > x;
        } else {
            return false;
        }
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean shouldDraw() {
        return true;
    }

    public Component getTitle() {
        return Component.empty();
    }

    public WidgetType getWidgetType() {
        return WidgetType.mouseOver(this.isMouseOver());
    }

    public void addMouseOverText(TooltipList list) {
        Component title = this.getTitle();
        if (title.getContents() != ComponentContents.EMPTY) {
            list.add(title);
        }
    }

    public final boolean isMouseOver() {
        return this.isMouseOver;
    }

    public boolean checkMouseOver(int mouseX, int mouseY) {
        if (this.parent == null) {
            return true;
        } else if (!this.parent.isMouseOver()) {
            return false;
        } else {
            int ax = this.getX();
            int ay = this.getY();
            return mouseX >= ax && mouseY >= ay && mouseX < ax + this.width && mouseY < ay + this.height;
        }
    }

    public void updateMouseOver(int mouseX, int mouseY) {
        this.isMouseOver = this.checkMouseOver(mouseX, mouseY);
    }

    public boolean shouldAddMouseOverText() {
        return this.isEnabled() && this.isMouseOver();
    }

    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
    }

    public boolean mousePressed(MouseButton button) {
        return false;
    }

    public boolean mouseDoubleClicked(MouseButton button) {
        return false;
    }

    public void mouseReleased(MouseButton button) {
    }

    public boolean mouseScrolled(double scroll) {
        return false;
    }

    public boolean mouseDragged(int button, double dragX, double dragY) {
        return false;
    }

    public boolean keyPressed(Key key) {
        return false;
    }

    public void keyReleased(Key key) {
    }

    public boolean charTyped(char c, KeyModifiers modifiers) {
        return false;
    }

    public Window getScreen() {
        return this.parent.getScreen();
    }

    public int getMouseX() {
        return this.parent.getMouseX();
    }

    public int getMouseY() {
        return this.parent.getMouseY();
    }

    public Widget.DrawLayer getDrawLayer() {
        return this.drawLayer;
    }

    public void setDrawLayer(Widget.DrawLayer drawLayer) {
        this.drawLayer = drawLayer;
    }

    public float getPartialTicks() {
        return this.parent.getPartialTicks();
    }

    public boolean handleClick(String scheme, String path) {
        return this.parent.handleClick(scheme, path);
    }

    public final boolean handleClick(String click) {
        int index = click.indexOf(58);
        return index == -1 ? this.handleClick("", click) : this.handleClick(click.substring(0, index), click.substring(index + 1));
    }

    final boolean shouldRenderInLayer(Widget.DrawLayer layer, int x, int y, int w, int h) {
        return this.drawLayer == layer && this.shouldDraw() && (!this.parent.getOnlyRenderWidgetsInside() || this.collidesWith(x, y, w, h));
    }

    public void onClosed() {
    }

    public Optional<PositionedIngredient> getIngredientUnderMouse() {
        return Optional.empty();
    }

    public boolean isGhostIngredientTarget(Object ingredient) {
        return false;
    }

    public void acceptGhostIngredient(Object ingredient) {
    }

    public static boolean isMouseButtonDown(MouseButton button) {
        return GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), button.id) == 1;
    }

    public static boolean isKeyDown(int key) {
        return GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), key) == 1;
    }

    public static String getClipboardString() {
        return Minecraft.getInstance().keyboardHandler.getClipboard();
    }

    public static void setClipboardString(String string) {
        Minecraft.getInstance().keyboardHandler.setClipboard(string);
    }

    public static boolean isShiftKeyDown() {
        return Screen.hasShiftDown();
    }

    public static boolean isCtrlKeyDown() {
        return Screen.hasControlDown();
    }

    public void tick() {
    }

    public String toString() {
        String s = this.getClass().getSimpleName();
        if (s.isEmpty()) {
            s = this.getClass().getSuperclass().getSimpleName();
        }
        return s;
    }

    public void playClickSound() {
        GuiHelper.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F);
    }

    @Nullable
    public CursorType getCursor() {
        return null;
    }

    public int compareTo(@NotNull Widget widget) {
        return 0;
    }

    public static enum DrawLayer {

        BACKGROUND, FOREGROUND
    }
}