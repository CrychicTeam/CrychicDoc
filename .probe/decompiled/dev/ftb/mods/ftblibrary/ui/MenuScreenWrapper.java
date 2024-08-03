package dev.ftb.mods.ftblibrary.ui;

import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.KeyModifiers;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MenuScreenWrapper<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IScreenWrapper {

    private final BaseScreen wrappedGui;

    private boolean drawSlots = true;

    private final TooltipList tooltipList = new TooltipList();

    public MenuScreenWrapper(BaseScreen g, T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.wrappedGui = g;
    }

    public MenuScreenWrapper<T> disableSlotDrawing() {
        this.drawSlots = false;
        return this;
    }

    @Override
    public void init() {
        super.init();
        this.wrappedGui.initGui();
        this.f_97735_ = this.wrappedGui.getX();
        this.f_97736_ = this.wrappedGui.getY();
        this.f_97726_ = this.wrappedGui.width;
        this.f_97727_ = this.wrappedGui.height;
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
            return this.wrappedGui.mousePressed(MouseButton.get(button)) || super.mouseClicked(x, y, button);
        }
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        this.wrappedGui.updateMouseOver((int) x, (int) y);
        this.wrappedGui.mouseReleased(MouseButton.get(button));
        return super.mouseReleased(x, y, button);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        return this.wrappedGui.mouseScrolled(scroll) || super.m_6050_(x, y, scroll);
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
                this.wrappedGui.closeGui(false);
            }
            return true;
        } else {
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

    @Override
    protected void renderBg(GuiGraphics graphics, float f, int mx, int my) {
        Theme theme = this.wrappedGui.getTheme();
        GuiHelper.setupDrawing();
        this.renderBackground(graphics);
        GuiHelper.setupDrawing();
        this.wrappedGui.draw(graphics, theme, this.f_97735_, this.f_97736_, this.f_97726_, this.f_97727_);
        if (this.drawSlots) {
            GuiHelper.setupDrawing();
            for (Slot slot : this.f_97732_.slots) {
                theme.drawContainerSlot(graphics, this.f_97735_ + slot.x, this.f_97736_ + slot.y, 16, 16);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.pose().pushPose();
        graphics.pose().translate((float) (-this.f_97735_), (float) (-this.f_97736_), 0.0F);
        GuiHelper.setupDrawing();
        Theme theme = this.wrappedGui.getTheme();
        this.wrappedGui.drawForeground(graphics, theme, this.f_97735_, this.f_97736_, this.f_97726_, this.f_97727_);
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
            List<FormattedCharSequence> lines = Tooltip.splitTooltip(this.f_96541_, (Component) this.tooltipList.getLines().stream().reduce((c1, c2) -> c1.copy().append("\n").append(c2)).orElse(Component.empty()));
            graphics.pose().translate(0.0F, 0.0F, 600.0F);
            graphics.setColor(1.0F, 1.0F, 1.0F, 0.8F);
            graphics.renderTooltip(theme.getFont(), lines, DefaultTooltipPositioner.INSTANCE, mouseX, Math.max(mouseY, 18));
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.pose().translate(0.0F, 0.0F, -600.0F);
        }
        this.tooltipList.reset();
        graphics.pose().popPose();
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        if (this.wrappedGui.drawDefaultBackground(graphics)) {
            super.m_280273_(graphics);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        this.wrappedGui.updateGui(mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void containerTick() {
        super.containerTick();
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