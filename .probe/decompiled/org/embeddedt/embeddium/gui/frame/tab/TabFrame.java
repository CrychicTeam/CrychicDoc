package org.embeddedt.embeddium.gui.frame.tab;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import java.util.Collection;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;
import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.gui.widgets.FlatButtonWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.Validate;
import org.embeddedt.embeddium.gui.frame.AbstractFrame;
import org.embeddedt.embeddium.gui.frame.ScrollableFrame;

public class TabFrame extends AbstractFrame {

    private static final int TAB_OPTION_INDENT = 5;

    private Dim2i tabSection;

    private final Dim2i frameSection;

    private final Multimap<String, Tab<?>> tabs;

    private final Runnable onSetTab;

    private final AtomicReference<Component> tabSectionSelectedTab;

    private final AtomicReference<Integer> tabSectionScrollBarOffset;

    private Tab<?> selectedTab;

    private AbstractFrame selectedFrame;

    private Dim2i tabSectionInner;

    private ScrollableFrame sidebarFrame;

    public TabFrame(Dim2i dim, boolean renderOutline, Multimap<String, Tab<?>> tabs, Runnable onSetTab, AtomicReference<Component> tabSectionSelectedTab, AtomicReference<Integer> tabSectionScrollBarOffset) {
        super(dim, renderOutline);
        this.tabs = ImmutableListMultimap.copyOf(tabs);
        int tabSectionY = (this.tabs.size() + this.tabs.keySet().size()) * 18;
        Optional<Integer> result = Stream.concat(tabs.keys().stream().map(id -> this.getStringWidth(TabHeaderWidget.getLabel(id)) + 10), tabs.values().stream().map(tab -> this.getStringWidth(tab.title()) + 5)).max(Integer::compareTo);
        this.tabSection = new Dim2i(this.dim.x(), this.dim.y(), (Integer) result.map(integer -> integer + 24).orElseGet(() -> (int) ((double) this.dim.width() * 0.35)), this.dim.height());
        this.tabSectionInner = tabSectionY > this.dim.height() ? this.tabSection.withHeight(tabSectionY) : this.tabSection;
        this.frameSection = new Dim2i(this.tabSection.getLimitX(), this.dim.y(), this.dim.width() - this.tabSection.width(), this.dim.height());
        this.onSetTab = onSetTab;
        this.tabSectionSelectedTab = tabSectionSelectedTab;
        this.tabSectionScrollBarOffset = tabSectionScrollBarOffset;
        if (this.tabSectionSelectedTab.get() != null) {
            this.selectedTab = (Tab<?>) this.tabs.values().stream().filter(tab -> tab.title().getString().equals(((Component) this.tabSectionSelectedTab.get()).getString())).findAny().orElse(null);
        }
        this.buildFrame();
        this.tabs.values().stream().filter(tab -> this.selectedTab != tab).forEach(tab -> tab.getFrameFunction().apply(this.frameSection));
    }

    public static TabFrame.Builder createBuilder() {
        return new TabFrame.Builder();
    }

    public void setTab(Tab<?> tab) {
        this.selectedTab = tab;
        this.tabSectionSelectedTab.set(this.selectedTab.title());
        if (this.onSetTab != null) {
            this.onSetTab.run();
        }
        this.buildFrame();
    }

    @Override
    public void buildFrame() {
        this.children.clear();
        this.drawable.clear();
        this.controlElements.clear();
        if (this.selectedTab == null && !this.tabs.isEmpty()) {
            this.selectedTab = (Tab<?>) this.tabs.values().iterator().next();
        }
        this.sidebarFrame = ScrollableFrame.createBuilder().setDimension(this.tabSection).setFrame(new TabFrame.TabSidebarFrame(this.tabSectionInner)).setVerticalScrollBarOffset(this.tabSectionScrollBarOffset).build();
        this.children.add(this.sidebarFrame);
        this.rebuildTabFrame();
        super.buildFrame();
    }

    private void rebuildTabFrame() {
        if (this.selectedTab != null) {
            AbstractFrame frame = (AbstractFrame) this.selectedTab.getFrameFunction().apply(this.frameSection);
            if (frame != null) {
                this.selectedFrame = frame;
                frame.buildFrame();
                this.children.add(frame);
            }
        }
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        for (AbstractWidget widget : this.children) {
            if (widget != this.selectedFrame) {
                widget.m_88315_(drawContext, mouseX, mouseY, delta);
            }
        }
        if (this.selectedFrame != null) {
            this.selectedFrame.render(drawContext, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.dim.containsCursor(mouseX, mouseY) && super.m_6375_(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.m_7979_(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.m_6348_(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        return super.m_6050_(mouseX, mouseY, verticalAmount);
    }

    public static class Builder {

        private final Multimap<String, Tab<?>> functions = MultimapBuilder.linkedHashKeys().arrayListValues().build();

        private Dim2i dim;

        private boolean renderOutline;

        private Runnable onSetTab;

        private AtomicReference<Component> tabSectionSelectedTab = new AtomicReference(null);

        private AtomicReference<Integer> tabSectionScrollBarOffset = new AtomicReference(0);

        public TabFrame.Builder setDimension(Dim2i dim) {
            this.dim = dim;
            return this;
        }

        public TabFrame.Builder shouldRenderOutline(boolean renderOutline) {
            this.renderOutline = renderOutline;
            return this;
        }

        public TabFrame.Builder addTabs(Consumer<Multimap<String, Tab<?>>> tabs) {
            tabs.accept(this.functions);
            return this;
        }

        public TabFrame.Builder onSetTab(Runnable onSetTab) {
            this.onSetTab = onSetTab;
            return this;
        }

        public TabFrame.Builder setTabSectionSelectedTab(AtomicReference<Component> tabSectionSelectedTab) {
            this.tabSectionSelectedTab = tabSectionSelectedTab;
            return this;
        }

        public TabFrame.Builder setTabSectionScrollBarOffset(AtomicReference<Integer> tabSectionScrollBarOffset) {
            this.tabSectionScrollBarOffset = tabSectionScrollBarOffset;
            return this;
        }

        public TabFrame build() {
            Validate.notNull(this.dim, "Dimension must be specified", new Object[0]);
            return new TabFrame(this.dim, this.renderOutline, this.functions, this.onSetTab, this.tabSectionSelectedTab, this.tabSectionScrollBarOffset);
        }
    }

    class TabSidebarFrame extends AbstractFrame {

        TabSidebarFrame(Dim2i dim) {
            super(dim, false);
        }

        @Override
        public void buildFrame() {
            this.children.clear();
            this.drawable.clear();
            this.controlElements.clear();
            this.rebuildTabs();
            super.buildFrame();
        }

        private void rebuildTabs() {
            int offsetY = 0;
            int width = TabFrame.this.tabSection.width() - 4;
            int height = 18;
            for (Entry<String, Collection<Tab<?>>> modEntry : TabFrame.this.tabs.asMap().entrySet()) {
                Dim2i modHeaderDim = new Dim2i(0, offsetY, width, height).withParentOffset(TabFrame.this.tabSection);
                offsetY += height;
                TabHeaderWidget headerButton = new TabHeaderWidget(modHeaderDim, (String) modEntry.getKey());
                headerButton.setLeftAligned(true);
                this.children.add(headerButton);
                for (Tab<?> tab : (Collection) modEntry.getValue()) {
                    Dim2i tabDim = new Dim2i(0, offsetY, width, height).withParentOffset(TabFrame.this.tabSection);
                    FlatButtonWidget button = new FlatButtonWidget(tabDim, tab.title(), () -> {
                        if ((Boolean) tab.onSelectFunction().get()) {
                            TabFrame.this.setTab(tab);
                        }
                    }) {

                        @Override
                        protected int getLeftAlignedTextOffset() {
                            return 5 + super.getLeftAlignedTextOffset();
                        }
                    };
                    button.setSelected(TabFrame.this.selectedTab == tab);
                    button.setLeftAligned(true);
                    this.children.add(button);
                    offsetY += height;
                }
            }
        }
    }
}