package dev.ftb.mods.ftblibrary.ui;

import dev.architectury.platform.Platform;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.KeyModifiers;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;

public class ScreenWrapper extends Screen implements IScreenWrapper {

    private final BaseScreen wrappedGui;

    private final TooltipList tooltipList = new TooltipList();

    public ScreenWrapper(BaseScreen g) {
        super(g.getTitle());
        this.wrappedGui = g;
    }

    @Override
    public void init() {
        super.init();
        this.wrappedGui.initGui();
    }

    @Override
    public boolean isPauseScreen() {
        return this.wrappedGui.doesGuiPauseGame();
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        this.wrappedGui.updateMouseOver((int) x, (int) y);
        if (button == MouseButton.BACK.id) {
            this.wrappedGui.onBack();
            return true;
        } else {
            return this.wrappedGui.mousePressed(MouseButton.get(button)) || super.m_6375_(x, y, button);
        }
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        this.wrappedGui.updateMouseOver((int) x, (int) y);
        this.wrappedGui.mouseReleased(MouseButton.get(button));
        return super.m_6348_(x, y, button);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        return this.wrappedGui.mouseScrolled(scroll) || super.m_6050_(x, y, scroll);
    }

    @Override
    public boolean mouseDragged(double x, double y, int button, double dragX, double dragY) {
        return this.wrappedGui.mouseDragged(button, dragX, dragY) || super.m_7979_(x, y, button, dragX, dragY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Key key = new Key(keyCode, scanCode, modifiers);
        if (this.wrappedGui.keyPressed(key)) {
            return true;
        } else if (key.backspace()) {
            this.wrappedGui.onBack();
            return true;
        } else if (this.wrappedGui.onClosedByKey(key)) {
            if (this.shouldCloseOnEsc()) {
                this.wrappedGui.closeGui(true);
            }
            return true;
        } else {
            if (Platform.isModLoaded("jei")) {
                this.wrappedGui.getIngredientUnderMouse().ifPresent(underMouse -> this.handleIngredientKey(key, underMouse.ingredient()));
            }
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        Key key = new Key(keyCode, scanCode, modifiers);
        this.wrappedGui.keyReleased(key);
        return super.m_7920_(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char keyChar, int modifiers) {
        return this.wrappedGui.charTyped(keyChar, new KeyModifiers(modifiers)) ? true : super.m_5534_(keyChar, keyChar);
    }

    private void handleIngredientKey(Key key, Object object) {
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.wrappedGui.updateGui(mouseX, mouseY, partialTicks);
        this.renderBackground(graphics);
        GuiHelper.setupDrawing();
        int x = this.wrappedGui.getX();
        int y = this.wrappedGui.getY();
        int w = this.wrappedGui.width;
        int h = this.wrappedGui.height;
        Theme theme = this.wrappedGui.getTheme();
        this.wrappedGui.draw(graphics, theme, x, y, w, h);
        this.wrappedGui.drawForeground(graphics, theme, x, y, w, h);
        this.wrappedGui.addMouseOverText(this.tooltipList);
        if (!this.tooltipList.shouldRender()) {
            this.wrappedGui.getIngredientUnderMouse().ifPresent(underMouse -> {
                if (underMouse.tooltip()) {
                    Object ingredient = underMouse.ingredient();
                    if (ingredient instanceof ItemStack stack && !stack.isEmpty()) {
                        graphics.pose().pushPose();
                        graphics.pose().translate(0.0F, 0.0F, (float) this.tooltipList.zOffsetItemTooltip);
                        graphics.renderTooltip(theme.getFont(), (ItemStack) ingredient, mouseX, mouseY);
                        graphics.pose().popPose();
                    }
                }
            });
        } else {
            graphics.pose().translate(0.0F, 0.0F, 600.0F);
            graphics.setColor(1.0F, 1.0F, 1.0F, 0.8F);
            graphics.renderTooltip(theme.getFont(), this.tooltipList.getLines(), Optional.empty(), mouseX, Math.max(mouseY, 18));
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.pose().translate(0.0F, 0.0F, -600.0F);
        }
        this.tooltipList.reset();
    }

    @Override
    public void renderBackground(GuiGraphics matrixStack) {
        if (this.wrappedGui.drawDefaultBackground(matrixStack)) {
            super.renderBackground(matrixStack);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.wrappedGui.tick();
    }

    @Override
    public BaseScreen getGui() {
        return this.wrappedGui;
    }

    @Override
    public void removed() {
        this.wrappedGui.onClosed();
        super.removed();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return this.getGui().shouldCloseOnEsc();
    }
}