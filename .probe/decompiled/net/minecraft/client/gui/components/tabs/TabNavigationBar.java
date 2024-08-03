package net.minecraft.client.gui.components.tabs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class TabNavigationBar extends AbstractContainerEventHandler implements Renderable, GuiEventListener, NarratableEntry {

    private static final int NO_TAB = -1;

    private static final int MAX_WIDTH = 400;

    private static final int HEIGHT = 24;

    private static final int MARGIN = 14;

    private static final Component USAGE_NARRATION = Component.translatable("narration.tab_navigation.usage");

    private final GridLayout layout;

    private int width;

    private final TabManager tabManager;

    private final ImmutableList<Tab> tabs;

    private final ImmutableList<TabButton> tabButtons;

    TabNavigationBar(int int0, TabManager tabManager1, Iterable<Tab> iterableTab2) {
        this.width = int0;
        this.tabManager = tabManager1;
        this.tabs = ImmutableList.copyOf(iterableTab2);
        this.layout = new GridLayout(0, 0);
        this.layout.defaultCellSetting().alignHorizontallyCenter();
        com.google.common.collect.ImmutableList.Builder<TabButton> $$3 = ImmutableList.builder();
        int $$4 = 0;
        for (Tab $$5 : iterableTab2) {
            $$3.add(this.layout.addChild(new TabButton(tabManager1, $$5, 0, 24), 0, $$4++));
        }
        this.tabButtons = $$3.build();
    }

    public static TabNavigationBar.Builder builder(TabManager tabManager0, int int1) {
        return new TabNavigationBar.Builder(tabManager0, int1);
    }

    public void setWidth(int int0) {
        this.width = int0;
    }

    @Override
    public void setFocused(boolean boolean0) {
        super.m_93692_(boolean0);
        if (this.m_7222_() != null) {
            this.m_7222_().setFocused(boolean0);
        }
    }

    @Override
    public void setFocused(@Nullable GuiEventListener guiEventListener0) {
        super.setFocused(guiEventListener0);
        if (guiEventListener0 instanceof TabButton $$1) {
            this.tabManager.setCurrentTab($$1.tab(), true);
        }
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent focusNavigationEvent0) {
        if (!this.m_93696_()) {
            TabButton $$1 = this.currentTabButton();
            if ($$1 != null) {
                return ComponentPath.path(this, ComponentPath.leaf($$1));
            }
        }
        return focusNavigationEvent0 instanceof FocusNavigationEvent.TabNavigation ? null : super.m_264064_(focusNavigationEvent0);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return this.tabButtons;
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return (NarratableEntry.NarrationPriority) this.tabButtons.stream().map(AbstractWidget::m_142684_).max(Comparator.naturalOrder()).orElse(NarratableEntry.NarrationPriority.NONE);
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput0) {
        Optional<TabButton> $$1 = this.tabButtons.stream().filter(AbstractWidget::m_274382_).findFirst().or(() -> Optional.ofNullable(this.currentTabButton()));
        $$1.ifPresent(p_274663_ -> {
            this.narrateListElementPosition(narrationElementOutput0.nest(), p_274663_);
            p_274663_.m_142291_(narrationElementOutput0);
        });
        if (this.m_93696_()) {
            narrationElementOutput0.add(NarratedElementType.USAGE, USAGE_NARRATION);
        }
    }

    protected void narrateListElementPosition(NarrationElementOutput narrationElementOutput0, TabButton tabButton1) {
        if (this.tabs.size() > 1) {
            int $$2 = this.tabButtons.indexOf(tabButton1);
            if ($$2 != -1) {
                narrationElementOutput0.add(NarratedElementType.POSITION, Component.translatable("narrator.position.tab", $$2 + 1, this.tabs.size()));
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        guiGraphics0.fill(0, 0, this.width, 24, -16777216);
        guiGraphics0.blit(CreateWorldScreen.HEADER_SEPERATOR, 0, this.layout.m_252907_() + this.layout.m_93694_() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
        UnmodifiableIterator var5 = this.tabButtons.iterator();
        while (var5.hasNext()) {
            TabButton $$4 = (TabButton) var5.next();
            $$4.m_88315_(guiGraphics0, int1, int2, float3);
        }
    }

    @Override
    public ScreenRectangle getRectangle() {
        return this.layout.m_264198_();
    }

    public void arrangeElements() {
        int $$0 = Math.min(400, this.width) - 28;
        int $$1 = Mth.roundToward($$0 / this.tabs.size(), 2);
        UnmodifiableIterator var3 = this.tabButtons.iterator();
        while (var3.hasNext()) {
            TabButton $$2 = (TabButton) var3.next();
            $$2.m_93674_($$1);
        }
        this.layout.arrangeElements();
        this.layout.m_252865_(Mth.roundToward((this.width - $$0) / 2, 2));
        this.layout.m_253211_(0);
    }

    public void selectTab(int int0, boolean boolean1) {
        if (this.m_93696_()) {
            this.setFocused((GuiEventListener) this.tabButtons.get(int0));
        } else {
            this.tabManager.setCurrentTab((Tab) this.tabs.get(int0), boolean1);
        }
    }

    public boolean keyPressed(int int0) {
        if (Screen.hasControlDown()) {
            int $$1 = this.getNextTabIndex(int0);
            if ($$1 != -1) {
                this.selectTab(Mth.clamp($$1, 0, this.tabs.size() - 1), true);
                return true;
            }
        }
        return false;
    }

    private int getNextTabIndex(int int0) {
        if (int0 >= 49 && int0 <= 57) {
            return int0 - 49;
        } else {
            if (int0 == 258) {
                int $$1 = this.currentTabIndex();
                if ($$1 != -1) {
                    int $$2 = Screen.hasShiftDown() ? $$1 - 1 : $$1 + 1;
                    return Math.floorMod($$2, this.tabs.size());
                }
            }
            return -1;
        }
    }

    private int currentTabIndex() {
        Tab $$0 = this.tabManager.getCurrentTab();
        int $$1 = this.tabs.indexOf($$0);
        return $$1 != -1 ? $$1 : -1;
    }

    @Nullable
    private TabButton currentTabButton() {
        int $$0 = this.currentTabIndex();
        return $$0 != -1 ? (TabButton) this.tabButtons.get($$0) : null;
    }

    public static class Builder {

        private final int width;

        private final TabManager tabManager;

        private final List<Tab> tabs = new ArrayList();

        Builder(TabManager tabManager0, int int1) {
            this.tabManager = tabManager0;
            this.width = int1;
        }

        public TabNavigationBar.Builder addTabs(Tab... tab0) {
            Collections.addAll(this.tabs, tab0);
            return this;
        }

        public TabNavigationBar build() {
            return new TabNavigationBar(this.width, this.tabManager, this.tabs);
        }
    }
}