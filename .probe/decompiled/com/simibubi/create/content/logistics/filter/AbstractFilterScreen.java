package com.simibubi.create.content.logistics.filter;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Indicator;
import com.simibubi.create.foundation.item.TooltipHelper;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractFilterScreen<F extends AbstractFilterMenu> extends AbstractSimiContainerScreen<F> {

    protected AllGuiTextures background;

    private List<Rect2i> extraAreas = Collections.emptyList();

    private IconButton resetButton;

    private IconButton confirmButton;

    protected AbstractFilterScreen(F menu, Inventory inv, Component title, AllGuiTextures background) {
        super(menu, inv, title);
        this.background = background;
    }

    @Override
    protected void init() {
        this.setWindowSize(Math.max(this.background.width, AllGuiTextures.PLAYER_INVENTORY.width), this.background.height + 4 + AllGuiTextures.PLAYER_INVENTORY.height);
        super.init();
        int x = this.f_97735_;
        int y = this.f_97736_;
        this.resetButton = new IconButton(x + this.background.width - 62, y + this.background.height - 24, AllIcons.I_TRASH);
        this.resetButton.withCallback(() -> {
            ((AbstractFilterMenu) this.f_97732_).clearContents();
            this.contentsCleared();
            ((AbstractFilterMenu) this.f_97732_).sendClearPacket();
        });
        this.confirmButton = new IconButton(x + this.background.width - 33, y + this.background.height - 24, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(() -> this.f_96541_.player.closeContainer());
        this.m_142416_(this.resetButton);
        this.m_142416_(this.confirmButton);
        this.extraAreas = ImmutableList.of(new Rect2i(x + this.background.width, y + this.background.height - 40, 80, 48));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int invX = this.getLeftOfCentered(AllGuiTextures.PLAYER_INVENTORY.width);
        int invY = this.f_97736_ + this.background.height + 4;
        this.renderPlayerInventory(graphics, invX, invY);
        int x = this.f_97735_;
        int y = this.f_97736_;
        this.background.render(graphics, x, y);
        graphics.drawString(this.f_96547_, this.f_96539_, x + (this.background.width - 8) / 2 - this.f_96547_.width(this.f_96539_) / 2, y + 4, AllItems.FILTER.isIn(((AbstractFilterMenu) this.f_97732_).contentHolder) ? 3158064 : 5841956, false);
        GuiGameElement.of(((AbstractFilterMenu) this.f_97732_).contentHolder).<GuiGameElement.GuiRenderBuilder>at((float) (x + this.background.width + 8), (float) (y + this.background.height - 52), -200.0F).scale(4.0).render(graphics);
    }

    @Override
    protected void containerTick() {
        if (!((AbstractFilterMenu) this.f_97732_).player.m_21205_().equals(((AbstractFilterMenu) this.f_97732_).contentHolder, false)) {
            ((AbstractFilterMenu) this.f_97732_).player.closeContainer();
        }
        super.containerTick();
        this.handleTooltips();
        this.handleIndicators();
    }

    protected void handleTooltips() {
        List<IconButton> tooltipButtons = this.getTooltipButtons();
        for (IconButton button : tooltipButtons) {
            if (!button.getToolTip().isEmpty()) {
                button.setToolTip((Component) button.getToolTip().get(0));
                button.getToolTip().add(TooltipHelper.holdShift(TooltipHelper.Palette.YELLOW, m_96638_()));
            }
        }
        if (m_96638_()) {
            List<MutableComponent> tooltipDescriptions = this.getTooltipDescriptions();
            for (int i = 0; i < tooltipButtons.size(); i++) {
                this.fillToolTip((IconButton) tooltipButtons.get(i), (Component) tooltipDescriptions.get(i));
            }
        }
    }

    public void handleIndicators() {
        for (IconButton button : this.getTooltipButtons()) {
            button.f_93623_ = this.isButtonEnabled(button);
        }
        for (Indicator indicator : this.getIndicators()) {
            indicator.state = this.isIndicatorOn(indicator) ? Indicator.State.ON : Indicator.State.OFF;
        }
    }

    protected abstract boolean isButtonEnabled(IconButton var1);

    protected abstract boolean isIndicatorOn(Indicator var1);

    protected List<IconButton> getTooltipButtons() {
        return Collections.emptyList();
    }

    protected List<MutableComponent> getTooltipDescriptions() {
        return Collections.emptyList();
    }

    protected List<Indicator> getIndicators() {
        return Collections.emptyList();
    }

    private void fillToolTip(IconButton button, Component tooltip) {
        if (button.m_198029_()) {
            List<Component> tip = button.getToolTip();
            tip.addAll(TooltipHelper.cutTextComponent(tooltip, TooltipHelper.Palette.ALL_GRAY));
        }
    }

    protected void contentsCleared() {
    }

    protected void sendOptionUpdate(FilterScreenPacket.Option option) {
        AllPackets.getChannel().sendToServer(new FilterScreenPacket(option));
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return this.extraAreas;
    }
}