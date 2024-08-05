package com.simibubi.create.foundation.config.ui;

import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.widget.BoxWidget;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

public class ConfigModListScreen extends ConfigScreen {

    ConfigScreenList list;

    HintableTextFieldWidget search;

    BoxWidget goBack;

    List<ConfigModListScreen.ModEntry> allEntries;

    public ConfigModListScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected void init() {
        super.m_7856_();
        int listWidth = Math.min(this.f_96543_ - 80, 300);
        this.list = new ConfigScreenList(this.f_96541_, listWidth, this.f_96544_ - 60, 15, this.f_96544_ - 45, 40);
        this.list.m_93507_(this.f_96543_ / 2 - this.list.getWidth() / 2);
        this.m_142416_(this.list);
        this.allEntries = new ArrayList();
        ModList.get().getMods().stream().map(IModInfo::getModId).forEach(id -> this.allEntries.add(new ConfigModListScreen.ModEntry(id, this)));
        this.allEntries.sort((e1, e2) -> {
            int empty = (e2.button.f_93623_ ? 1 : 0) - (e1.button.f_93623_ ? 1 : 0);
            return empty != 0 ? empty : e1.id.compareToIgnoreCase(e2.id);
        });
        this.list.m_6702_().clear();
        this.list.m_6702_().addAll(this.allEntries);
        this.goBack = new BoxWidget(this.f_96543_ / 2 - listWidth / 2 - 30, this.f_96544_ / 2 + 65, 20, 20).<ElementWidget>withPadding(2.0F, 2.0F).withCallback(() -> ScreenOpener.open(this.parent));
        this.goBack.showingElement(AllIcons.I_CONFIG_BACK.asStencil().withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.goBack)));
        this.goBack.getToolTip().add(Components.literal("Go Back"));
        this.m_142416_(this.goBack);
        this.search = new HintableTextFieldWidget(this.f_96547_, this.f_96543_ / 2 - listWidth / 2, this.f_96544_ - 35, listWidth, 20);
        this.search.m_94151_(this::updateFilter);
        this.search.setHint("Search...");
        this.search.m_94198_();
        this.m_142416_(this.search);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.m_7933_(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            if (keyCode == 259) {
                ScreenOpener.open(this.parent);
            }
            return false;
        }
    }

    private void updateFilter(String search) {
        this.list.m_6702_().clear();
        this.allEntries.stream().filter(modEntry -> modEntry.id.contains(search.toLowerCase(Locale.ROOT))).forEach(this.list.m_6702_()::add);
        this.list.m_93410_(this.list.m_93517_());
        if (this.list.m_6702_().size() > 0) {
            this.search.m_94202_(Theme.i(Theme.Key.TEXT));
        } else {
            this.search.m_94202_(Theme.i(Theme.Key.BUTTON_FAIL));
        }
    }

    public static class ModEntry extends ConfigScreenList.LabeledEntry {

        protected BoxWidget button;

        protected String id;

        public ModEntry(String id, Screen parent) {
            super(ConfigScreen.toHumanReadable(id));
            this.id = id;
            this.button = new BoxWidget(0, 0, 35, 16).showingElement(AllIcons.I_CONFIG_OPEN.asStencil().at(10.0F, 0.0F));
            this.button.modifyElement(e -> ((DelegatedStencilElement) e).withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.button)));
            if (ConfigHelper.hasAnyForgeConfig(id)) {
                this.button.withCallback(() -> ScreenOpener.open(new BaseConfigScreen(parent, id)));
            } else {
                this.button.f_93623_ = false;
                this.button.updateColorsFromState();
                this.button.modifyElement(e -> ((DelegatedStencilElement) e).withElementRenderer(BaseConfigScreen.DISABLED_RENDERER));
                this.labelTooltip.add(Components.literal(ConfigScreen.toHumanReadable(id)));
                this.labelTooltip.addAll(TooltipHelper.cutStringTextComponent("This Mod does not have any configs registered or is not using Forge's config system", TooltipHelper.Palette.ALL_GRAY));
            }
            this.listeners.add(this.button);
        }

        public String getId() {
            return this.id;
        }

        @Override
        public void tick() {
            super.tick();
            this.button.tick();
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean p_230432_9_, float partialTicks) {
            super.render(graphics, index, y, x, width, height, mouseX, mouseY, p_230432_9_, partialTicks);
            this.button.m_252865_(x + width - 108);
            this.button.m_253211_(y + 10);
            this.button.setHeight(height - 20);
            this.button.m_88315_(graphics, mouseX, mouseY, partialTicks);
        }

        @Override
        protected int getLabelWidth(int totalWidth) {
            return (int) ((float) totalWidth * 0.4F) + 30;
        }
    }
}