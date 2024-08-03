package me.shedaniel.clothconfig2.gui.widget;

import com.google.common.collect.Iterators;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigScreen;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class SearchFieldEntry extends AbstractConfigListEntry<Object> {

    private final EditBox editBox = new EditBox(Minecraft.getInstance().font, 0, 0, 100, 18, Component.empty());

    private String[] lowerCases = this.editBox.getValue().isEmpty() ? new String[0] : this.editBox.getValue().toLowerCase(Locale.ROOT).split(" ");

    public SearchFieldEntry(ConfigScreen screen, ClothConfigScreen.ListWidget<AbstractConfigEntry<AbstractConfigEntry<?>>> listWidget) {
        super(Component.empty(), false);
        this.editBox.setResponder(s -> this.lowerCases = s.isEmpty() ? new String[0] : s.toLowerCase(Locale.ROOT).split(" "));
        listWidget.entriesTransformer = entries -> new AbstractList<AbstractConfigEntry<AbstractConfigEntry<?>>>() {

            public Iterator<AbstractConfigEntry<AbstractConfigEntry<?>>> iterator() {
                return (Iterator<AbstractConfigEntry<AbstractConfigEntry<?>>>) (SearchFieldEntry.this.editBox.getValue().isEmpty() ? entries.iterator() : Iterators.filter(entries.iterator(), entry -> entry.isDisplayed() && screen.matchesSearch(entry.getSearchTags())));
            }

            public AbstractConfigEntry<AbstractConfigEntry<?>> get(int index) {
                return (AbstractConfigEntry<AbstractConfigEntry<?>>) Iterators.get(this.iterator(), index);
            }

            public void add(int index, AbstractConfigEntry<AbstractConfigEntry<?>> element) {
                entries.add(index, element);
            }

            public AbstractConfigEntry<AbstractConfigEntry<?>> remove(int index) {
                AbstractConfigEntry<AbstractConfigEntry<?>> entry = this.get(index);
                return entries.remove(entry) ? entry : null;
            }

            public boolean remove(Object o) {
                return entries.remove(o);
            }

            public void clear() {
                entries.clear();
            }

            public int size() {
                return Iterators.size(this.iterator());
            }
        };
    }

    public boolean matchesSearch(Iterator<String> tags) {
        if (this.lowerCases.length == 0) {
            return true;
        } else if (!tags.hasNext()) {
            return true;
        } else {
            for (String lowerCase : this.lowerCases) {
                boolean found = false;
                for (String tag : () -> tags) {
                    if (tag.toLowerCase(Locale.ROOT).contains(lowerCase)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        this.editBox.m_93674_(Mth.clamp(entryWidth - 10, 0, 500));
        this.editBox.m_252865_(x + entryWidth / 2 - this.editBox.m_5711_() / 2);
        this.editBox.m_253211_(y + entryHeight / 2 - 9);
        this.editBox.m_88315_(graphics, mouseX, mouseY, delta);
        if (this.editBox.getValue().isEmpty()) {
            this.editBox.setSuggestion("Search...");
        } else {
            this.editBox.setSuggestion(null);
        }
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public Optional<Object> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return Collections.singletonList(this.editBox);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.singletonList(this.editBox);
    }
}