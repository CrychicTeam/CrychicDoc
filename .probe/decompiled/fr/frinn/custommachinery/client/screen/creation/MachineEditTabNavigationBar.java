package fr.frinn.custommachinery.client.screen.creation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableList.Builder;
import fr.frinn.custommachinery.client.screen.creation.tabs.EditTabButton;
import fr.frinn.custommachinery.client.screen.creation.tabs.MachineEditTab;
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
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class MachineEditTabNavigationBar extends AbstractContainerEventHandler implements Renderable, GuiEventListener, NarratableEntry {

    private static final int NO_TAB = -1;

    private static final int MAX_WIDTH = 400;

    private static final int HEIGHT = 24;

    private static final int MARGIN = 14;

    private static final Component USAGE_NARRATION = Component.translatable("narration.tab_navigation.usage");

    private final GridLayout layout;

    private int x;

    private int y;

    private int width;

    private int height;

    private final TabManager tabManager;

    private final ImmutableList<MachineEditTab> tabs;

    private final ImmutableList<TabButton> tabButtons;

    public MachineEditTabNavigationBar(int width, TabManager tabManager, Iterable<MachineEditTab> tabs) {
        this.width = width;
        this.tabManager = tabManager;
        this.tabs = ImmutableList.copyOf(tabs);
        this.layout = new GridLayout(0, 0);
        this.layout.defaultCellSetting().alignHorizontallyCenter();
        Builder<TabButton> builder = ImmutableList.builder();
        int i = 0;
        for (MachineEditTab tab : tabs) {
            builder.add(this.layout.addChild(new EditTabButton(tabManager, tab, 0, 24), 0, i++));
        }
        this.tabButtons = builder.build();
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        super.setFocused(focused);
        if (focused instanceof TabButton tabButton) {
            this.tabManager.setCurrentTab(tabButton.tab(), true);
        }
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent event) {
        if (!this.m_93696_()) {
            TabButton tabButton = this.currentTabButton();
            if (tabButton != null) {
                return ComponentPath.path(this, ComponentPath.leaf(tabButton));
            }
        }
        return event instanceof FocusNavigationEvent.TabNavigation ? null : super.m_264064_(event);
    }

    @Override
    public List<TabButton> children() {
        return this.tabButtons;
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return (NarratableEntry.NarrationPriority) this.tabButtons.stream().map(AbstractWidget::m_142684_).max(Comparator.naturalOrder()).orElse(NarratableEntry.NarrationPriority.NONE);
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        Optional<TabButton> optional = this.tabButtons.stream().filter(AbstractWidget::m_274382_).findFirst().or(() -> Optional.ofNullable(this.currentTabButton()));
        optional.ifPresent(tabButton -> {
            this.narrateListElementPosition(narrationElementOutput.nest(), tabButton);
            tabButton.m_142291_(narrationElementOutput);
        });
        if (this.m_93696_()) {
            narrationElementOutput.add(NarratedElementType.USAGE, USAGE_NARRATION);
        }
    }

    protected void narrateListElementPosition(NarrationElementOutput narrationElementOutput, TabButton tabButton) {
        if (this.tabs.size() > 1) {
            int i = this.tabButtons.indexOf(tabButton);
            if (i != -1) {
                narrationElementOutput.add(NarratedElementType.POSITION, Component.translatable("narrator.position.tab", i + 1, this.tabs.size()));
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        UnmodifiableIterator var5 = this.tabButtons.iterator();
        while (var5.hasNext()) {
            TabButton tabButton = (TabButton) var5.next();
            tabButton.m_88315_(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    public void bounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public ScreenRectangle getRectangle() {
        return this.layout.m_264198_();
    }

    public void arrangeElements() {
        int i = Math.min(400, this.width);
        int j = Mth.roundToward(i / this.tabs.size(), 2);
        UnmodifiableIterator var3 = this.tabButtons.iterator();
        while (var3.hasNext()) {
            TabButton tabButton = (TabButton) var3.next();
            tabButton.m_93674_(j);
        }
        this.layout.arrangeElements();
        this.layout.m_252865_(this.x);
        this.layout.m_253211_(this.y);
    }

    public void selectTab(int index, boolean playClickSound) {
        if (this.m_93696_()) {
            this.setFocused((GuiEventListener) this.tabButtons.get(index));
        } else {
            this.tabManager.setCurrentTab((Tab) this.tabs.get(index), playClickSound);
        }
    }

    private int getNextTabIndex(int keycode) {
        if (keycode >= 49 && keycode <= 57) {
            return keycode - 49;
        } else {
            if (keycode == 258) {
                int i = this.currentTabIndex();
                if (i != -1) {
                    int j = Screen.hasShiftDown() ? i - 1 : i + 1;
                    return Math.floorMod(j, this.tabs.size());
                }
            }
            return -1;
        }
    }

    private int currentTabIndex() {
        Tab tab = this.tabManager.getCurrentTab();
        int i = this.tabs.indexOf(tab);
        return i != -1 ? i : -1;
    }

    @Nullable
    private TabButton currentTabButton() {
        int i = this.currentTabIndex();
        return i != -1 ? (TabButton) this.tabButtons.get(i) : null;
    }
}