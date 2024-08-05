package net.minecraft.client.gui.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

public class OptionsList extends ContainerObjectSelectionList<OptionsList.Entry> {

    public OptionsList(Minecraft minecraft0, int int1, int int2, int int3, int int4, int int5) {
        super(minecraft0, int1, int2, int3, int4, int5);
        this.f_93394_ = false;
    }

    public int addBig(OptionInstance<?> optionInstance0) {
        return this.m_7085_(OptionsList.Entry.big(this.f_93386_.options, this.f_93388_, optionInstance0));
    }

    public void addSmall(OptionInstance<?> optionInstance0, @Nullable OptionInstance<?> optionInstance1) {
        this.m_7085_(OptionsList.Entry.small(this.f_93386_.options, this.f_93388_, optionInstance0, optionInstance1));
    }

    public void addSmall(OptionInstance<?>[] optionInstance0) {
        for (int $$1 = 0; $$1 < optionInstance0.length; $$1 += 2) {
            this.addSmall(optionInstance0[$$1], $$1 < optionInstance0.length - 1 ? optionInstance0[$$1 + 1] : null);
        }
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int getScrollbarPosition() {
        return super.m_5756_() + 32;
    }

    @Nullable
    public AbstractWidget findOption(OptionInstance<?> optionInstance0) {
        for (OptionsList.Entry $$1 : this.m_6702_()) {
            AbstractWidget $$2 = (AbstractWidget) $$1.options.get(optionInstance0);
            if ($$2 != null) {
                return $$2;
            }
        }
        return null;
    }

    public Optional<AbstractWidget> getMouseOver(double double0, double double1) {
        for (OptionsList.Entry $$2 : this.m_6702_()) {
            for (AbstractWidget $$3 : $$2.children) {
                if ($$3.isMouseOver(double0, double1)) {
                    return Optional.of($$3);
                }
            }
        }
        return Optional.empty();
    }

    protected static class Entry extends ContainerObjectSelectionList.Entry<OptionsList.Entry> {

        final Map<OptionInstance<?>, AbstractWidget> options;

        final List<AbstractWidget> children;

        private Entry(Map<OptionInstance<?>, AbstractWidget> mapOptionInstanceAbstractWidget0) {
            this.options = mapOptionInstanceAbstractWidget0;
            this.children = ImmutableList.copyOf(mapOptionInstanceAbstractWidget0.values());
        }

        public static OptionsList.Entry big(Options options0, int int1, OptionInstance<?> optionInstance2) {
            return new OptionsList.Entry(ImmutableMap.of(optionInstance2, optionInstance2.createButton(options0, int1 / 2 - 155, 0, 310)));
        }

        public static OptionsList.Entry small(Options options0, int int1, OptionInstance<?> optionInstance2, @Nullable OptionInstance<?> optionInstance3) {
            AbstractWidget $$4 = optionInstance2.createButton(options0, int1 / 2 - 155, 0, 150);
            return optionInstance3 == null ? new OptionsList.Entry(ImmutableMap.of(optionInstance2, $$4)) : new OptionsList.Entry(ImmutableMap.of(optionInstance2, $$4, optionInstance3, optionInstance3.createButton(options0, int1 / 2 - 155 + 160, 0, 150)));
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            this.children.forEach(p_280776_ -> {
                p_280776_.setY(int2);
                p_280776_.render(guiGraphics0, int6, int7, float9);
            });
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.children;
        }
    }
}