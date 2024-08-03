package com.simibubi.create.compat.computercraft;

import com.simibubi.create.compat.Mods;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Lang;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

public class ComputerScreen extends AbstractSimiScreen {

    private final AllGuiTextures background = AllGuiTextures.COMPUTER;

    private final Supplier<Component> displayTitle;

    private final ComputerScreen.RenderWindowFunction additional;

    private final Screen previousScreen;

    private final Supplier<Boolean> hasAttachedComputer;

    private AbstractSimiWidget computerWidget;

    private IconButton confirmButton;

    public ComputerScreen(Component title, @Nullable ComputerScreen.RenderWindowFunction additional, Screen previousScreen, Supplier<Boolean> hasAttachedComputer) {
        this(title, () -> title, additional, previousScreen, hasAttachedComputer);
    }

    public ComputerScreen(Component title, Supplier<Component> displayTitle, @Nullable ComputerScreen.RenderWindowFunction additional, Screen previousScreen, Supplier<Boolean> hasAttachedComputer) {
        super(title);
        this.displayTitle = displayTitle;
        this.additional = additional;
        this.previousScreen = previousScreen;
        this.hasAttachedComputer = hasAttachedComputer;
    }

    @Override
    public void tick() {
        if (!(Boolean) this.hasAttachedComputer.get()) {
            this.f_96541_.setScreen(this.previousScreen);
        }
        super.tick();
    }

    @Override
    protected void init() {
        this.setWindowSize(this.background.width, this.background.height);
        super.init();
        int x = this.guiLeft;
        int y = this.guiTop;
        Mods.COMPUTERCRAFT.executeIfInstalled(() -> () -> {
            this.computerWidget = new ElementWidget(x + 33, y + 38).showingElement(GuiGameElement.of(Mods.COMPUTERCRAFT.getBlock("computer_advanced")));
            this.computerWidget.getToolTip().add(Lang.translate("gui.attached_computer.hint").component());
            this.m_142416_(this.computerWidget);
        });
        this.confirmButton = new IconButton(x + this.background.width - 33, y + this.background.height - 24, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(this::m_7379_);
        this.m_142416_(this.confirmButton);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.guiLeft;
        int y = this.guiTop;
        this.background.render(graphics, x, y);
        graphics.drawString(this.f_96547_, (Component) this.displayTitle.get(), Math.round((float) x + (float) this.background.width / 2.0F - (float) this.f_96547_.width((FormattedText) this.displayTitle.get()) / 2.0F), y + 4, 4464640, false);
        graphics.drawWordWrap(this.f_96547_, Lang.translate("gui.attached_computer.controlled").component(), x + 55, y + 32, 111, 8026746);
        if (this.additional != null) {
            this.additional.render(graphics, mouseX, mouseY, partialTicks, x, y, this.background);
        }
    }

    @FunctionalInterface
    public interface RenderWindowFunction {

        void render(GuiGraphics var1, int var2, int var3, float var4, int var5, int var6, AllGuiTextures var7);
    }
}