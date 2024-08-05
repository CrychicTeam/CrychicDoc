package dev.xkmc.modulargolems.content.menu.config;

import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.modulargolems.content.entity.mode.GolemMode;
import dev.xkmc.modulargolems.content.entity.mode.GolemModes;
import dev.xkmc.modulargolems.content.menu.registry.ConfigGroup;
import dev.xkmc.modulargolems.content.menu.registry.GolemTabRegistry;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabManager;
import dev.xkmc.modulargolems.content.menu.tabs.ITabScreen;
import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ToggleGolemConfigScreen extends BaseContainerScreen<ToggleGolemConfigMenu> implements ITabScreen {

    public ToggleGolemConfigScreen(ToggleGolemConfigMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }

    @Override
    protected void init() {
        super.m_7856_();
        new GolemTabManager<>(this, new ConfigGroup(((ToggleGolemConfigMenu) this.f_97732_).editor)).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, GolemTabRegistry.CONFIG_TOGGLE);
        int left = this.getGuiLeft();
        int top = this.getGuiTop();
        int width = this.getXSize();
        this.m_142416_(new CycleButton.Builder<GolemMode>(GolemMode::getName).withValues(GolemModes.LIST).withInitialValue(((ToggleGolemConfigMenu) this.f_97732_).editor.getDefaultMode()).create(left + 7, top + 40, width - 14, 20, MGLangData.CONFIG_MODE.get(), this::modeChange));
        boolean pos = ((ToggleGolemConfigMenu) this.f_97732_).editor.summonToPosition();
        CycleButton<Boolean> btn = CycleButton.booleanBuilder(MGLangData.CONFIG_TO_POSITION.get(), MGLangData.CONFIG_TO_TARGET.get()).withInitialValue(pos).create(left + 7, top + 70, width - 14, 20, MGLangData.CONFIG_POS.get(), this::positionChange);
        this.updatePositionTooltip(btn, pos);
        this.m_142416_(btn);
        this.m_142416_(CycleButton.onOffBuilder(((ToggleGolemConfigMenu) this.f_97732_).editor.locked()).create(left + 7, top + 100, width - 14, 20, MGLangData.CONFIG_LOCK.get(), this::lockChange));
    }

    @Override
    protected void renderBg(GuiGraphics g, float pTick, int mx, int my) {
        MenuLayoutConfig.ScreenRenderer sr = ((ToggleGolemConfigMenu) this.f_97732_).sprite.get().getRenderer(this);
        sr.start(g);
        if (((ToggleGolemConfigMenu) this.f_97732_).container.getItem(0).isEmpty()) {
            sr.draw(g, "hand", "altas_golem");
        }
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mx, int my) {
        super.m_280003_(g, mx, my);
        g.drawString(this.f_96547_, MGLangData.CONFIG_SET.get(), 32, 20, 4210752, false);
    }

    private void modeChange(CycleButton<GolemMode> button, GolemMode mode) {
        ((ToggleGolemConfigMenu) this.f_97732_).editor.setDefaultMode(mode);
    }

    private void positionChange(CycleButton<Boolean> button, boolean bool) {
        ((ToggleGolemConfigMenu) this.f_97732_).editor.setSummonToPosition(bool);
    }

    private void updatePositionTooltip(AbstractButton button, boolean bool) {
        button.m_257544_(Tooltip.create(bool ? MGLangData.CONFIG_TO_POSITION_TOOLTIP.get() : MGLangData.CONFIG_TO_TARGET_TOOLTIP.get()));
    }

    private void lockChange(CycleButton<Boolean> btn, Boolean lock) {
        ((ToggleGolemConfigMenu) this.f_97732_).editor.setLocked(lock);
    }

    @Override
    public int screenWidth() {
        return this.f_96543_;
    }

    @Override
    public int screenHeight() {
        return this.f_96544_;
    }
}