package dev.xkmc.modulargolems.content.menu.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

public class GolemTabManager<G extends GolemTabGroup<G>> {

    protected final List<GolemTabBase<G, ?>> list = new ArrayList();

    public final ITabScreen screen;

    public final G token;

    public int tabPage;

    public GolemTabToken<G, ?> selected;

    public GolemTabManager(ITabScreen screen, G token) {
        this.screen = screen;
        this.token = token;
    }

    public void init(Consumer<AbstractWidget> adder, GolemTabToken<G, ?> selected) {
        List<GolemTabToken<G, ?>> token_list = this.token.getList();
        this.list.clear();
        this.selected = selected;
        int guiLeft = this.screen.getGuiLeft();
        int guiTop = this.screen.getGuiTop();
        int imgWidth = this.screen.getXSize();
        for (int i = 0; i < token_list.size(); i++) {
            GolemTabToken<G, ?> token = (GolemTabToken<G, ?>) token_list.get(i);
            GolemTabBase<G, ?> tab = token.create(i, this);
            tab.m_252865_(guiLeft + imgWidth + GolemTabType.RIGHT.getX(tab.index));
            tab.m_253211_(guiTop + GolemTabType.RIGHT.getY(tab.index));
            adder.accept(tab);
            this.list.add(tab);
        }
        this.updateVisibility();
    }

    private void updateVisibility() {
        for (GolemTabBase<G, ?> tab : this.list) {
            tab.f_93624_ = tab.index >= this.tabPage * 8 && tab.index < (this.tabPage + 1) * 8;
            tab.f_93623_ = tab.f_93624_;
        }
    }

    public Screen getScreen() {
        return this.screen.asScreen();
    }

    public void onToolTipRender(GuiGraphics stack, int mouseX, int mouseY) {
        for (GolemTabBase<G, ?> tab : this.list) {
            if (tab.f_93624_ && tab.m_198029_()) {
                tab.onTooltip(stack, mouseX, mouseY);
            }
        }
    }
}