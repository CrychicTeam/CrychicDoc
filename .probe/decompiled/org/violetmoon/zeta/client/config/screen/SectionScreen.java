package org.violetmoon.zeta.client.config.screen;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.client.config.definition.ClientDefinitionExt;
import org.violetmoon.zeta.client.config.widget.DefaultDiscardDone;
import org.violetmoon.zeta.client.config.widget.ScrollableWidgetList;
import org.violetmoon.zeta.config.ChangeSet;
import org.violetmoon.zeta.config.Definition;
import org.violetmoon.zeta.config.SectionDefinition;
import org.violetmoon.zeta.config.ValueDefinition;

public class SectionScreen extends ZetaScreen {

    protected final SectionDefinition section;

    protected final ChangeSet changes;

    protected final String breadcrumbs;

    protected DefaultDiscardDone defaultDiscardDone;

    protected ScrollableWidgetList<SectionScreen, SectionScreen.Entry> list;

    public SectionScreen(ZetaClient zc, Screen parent, ChangeSet changes, SectionDefinition section) {
        super(zc, parent);
        this.section = section;
        this.changes = changes;
        this.breadcrumbs = "> " + String.join(" > ", section.path);
    }

    @Override
    protected void init() {
        super.m_7856_();
        double previousScrollAmount = this.list == null ? 0.0 : this.list.m_93517_();
        this.defaultDiscardDone = new DefaultDiscardDone(this, this.changes, this.section);
        this.defaultDiscardDone.addWidgets(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        });
        this.list = new ScrollableWidgetList<>(this);
        for (ValueDefinition<?> value : this.section.getValues()) {
            this.list.addEntry(new SectionScreen.ValueDefinitionEntry<>(this.changes, value));
        }
        Collection<SectionDefinition> subsections = this.section.getSubsections();
        if (!subsections.isEmpty()) {
            this.list.addEntry(new SectionScreen.Divider());
            for (SectionDefinition subsection : this.section.getSubsections()) {
                this.list.addEntry(new SectionScreen.SectionDefinitionEntry(this.changes, subsection));
            }
        }
        this.m_7787_(this.list);
        this.list.addChildWidgets(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_7787_(x$0);
        });
        this.list.m_93410_(previousScrollAmount);
        this.defaultDiscardDone.discard.f_93623_ = this.changes.isDirty(this.section);
    }

    @Override
    public void tick() {
        this.defaultDiscardDone.discard.f_93623_ = this.changes.isDirty(this.section);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        this.list.render(guiGraphics, mouseX, mouseY, partialTicks);
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        this.list.reenableVisibleWidgets();
        int left = 20;
        String modName = WordUtils.capitalizeFully(this.z.modid);
        guiGraphics.drawString(this.f_96547_, ChatFormatting.BOLD + I18n.get("quark.gui.config.header", modName), left, 10, 4775356);
        guiGraphics.drawString(this.f_96547_, this.breadcrumbs, left, 20, 16777215);
    }

    public class DefinitionEntry<T extends Definition> extends SectionScreen.Entry {

        private final ChangeSet changes;

        private final T def;

        private final ClientDefinitionExt<T> ext;

        public DefinitionEntry(ChangeSet changes, T def) {
            this.changes = changes;
            this.def = def;
            this.ext = SectionScreen.this.zc.clientConfigManager.getExt(def);
            this.ext.addWidgets(SectionScreen.this.zc, SectionScreen.this, changes, def, this::addScrollingWidget);
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int index, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            assert SectionScreen.this.f_96541_ != null;
            int left = rowLeft + 10;
            int top = rowTop + 4;
            int effIndex = index + 1;
            if (this.def instanceof SectionDefinition) {
                effIndex--;
            }
            this.drawBackground(guiGraphics, effIndex, rowTop, rowLeft, rowWidth, rowHeight, mouseX, mouseY, hovered);
            super.m_6311_(guiGraphics, index, rowTop, rowLeft, rowWidth, rowHeight, mouseX, mouseY, hovered, partialTicks);
            String name = this.def.getTranslatedDisplayName(x$0 -> I18n.get(x$0));
            if (this.changes.isDirty(this.def)) {
                name = name + ChatFormatting.GOLD + "*";
            }
            int len = SectionScreen.this.f_96541_.font.width(name);
            int maxLen = rowWidth - 85;
            String originalName = null;
            if (len > maxLen) {
                originalName = name;
                do {
                    name = name.substring(0, name.length() - 1);
                    len = SectionScreen.this.f_96541_.font.width(name);
                } while (len > maxLen);
                name = name + "...";
            }
            List<Component> tooltip = (List<Component>) this.def.getTranslatedComment(x$0 -> I18n.get(x$0)).stream().map(Component::m_237113_).collect(Collectors.toList());
            if (originalName != null) {
                if (tooltip.isEmpty()) {
                    tooltip.add(Component.literal(originalName));
                } else {
                    tooltip.add(0, Component.empty());
                    tooltip.add(0, Component.literal(originalName));
                }
            }
            if (!tooltip.isEmpty()) {
                int hoverLeft = left + SectionScreen.this.f_96541_.font.width(name + " ");
                int hoverRight = hoverLeft + SectionScreen.this.f_96541_.font.width("(?)");
                name = name + ChatFormatting.AQUA + " (?)";
                if (mouseX >= hoverLeft && mouseX < hoverRight && mouseY >= top && mouseY < top + 10) {
                    guiGraphics.renderComponentTooltip(SectionScreen.this.f_96547_, tooltip, mouseX, mouseY);
                }
            }
            guiGraphics.drawString(SectionScreen.this.f_96541_.font, name, left, top, 16777215, true);
            if (this.ext != null) {
                guiGraphics.drawString(SectionScreen.this.f_96541_.font, this.ext.getSubtitle(this.changes, this.def), left, top + 10, 10066329, true);
            }
        }

        @NotNull
        @Override
        public Component getNarration() {
            return Component.literal(this.def.getTranslatedDisplayName(x$0 -> I18n.get(x$0)));
        }
    }

    public class Divider extends SectionScreen.Entry {

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int index, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            assert SectionScreen.this.f_96541_ != null;
            String s = I18n.get("quark.gui.config.subcategories");
            guiGraphics.drawString(SectionScreen.this.f_96541_.font, s, (float) rowLeft + (float) (rowWidth / 2 - SectionScreen.this.f_96541_.font.width(s) / 2), (float) (rowTop + 7), 6711039, true);
        }

        @NotNull
        @Override
        public Component getNarration() {
            return Component.literal("");
        }
    }

    public abstract static class Entry extends ScrollableWidgetList.Entry<SectionScreen.Entry> {
    }

    public class SectionDefinitionEntry extends SectionScreen.DefinitionEntry<SectionDefinition> {

        public SectionDefinitionEntry(ChangeSet changes, SectionDefinition def) {
            super(changes, def);
        }
    }

    public class ValueDefinitionEntry<X> extends SectionScreen.DefinitionEntry<ValueDefinition<X>> {

        public ValueDefinitionEntry(ChangeSet changes, ValueDefinition<X> def) {
            super(changes, def);
        }
    }
}