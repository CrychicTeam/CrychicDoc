package me.shedaniel.clothconfig2.gui.entries;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.Expandable;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public class MultiElementListEntry<T> extends TooltipListEntry<T> implements Expandable {

    private static final ResourceLocation CONFIG_TEX = new ResourceLocation("cloth-config2", "textures/gui/cloth_config.png");

    private final T object;

    private final List<AbstractConfigListEntry<?>> entries;

    private final MultiElementListEntry<T>.CategoryLabelWidget widget;

    private final List<Object> children;

    private boolean expanded;

    @Internal
    public MultiElementListEntry(Component categoryName, T object, List<AbstractConfigListEntry<?>> entries, boolean defaultExpanded) {
        super(categoryName, null);
        this.object = object;
        this.entries = entries;
        this.expanded = defaultExpanded;
        this.widget = new MultiElementListEntry.CategoryLabelWidget();
        this.children = Lists.newArrayList(new Object[] { this.widget });
        this.children.addAll(entries);
        this.setReferenceProviderEntries(entries);
    }

    @Override
    public boolean isRequiresRestart() {
        for (AbstractConfigListEntry<?> entry : this.entries) {
            if (entry.isRequiresRestart()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEdited() {
        for (AbstractConfigListEntry<?> entry : this.entries) {
            if (entry.isEdited()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<String> getSearchTags() {
        return Iterators.concat(super.getSearchTags(), Iterators.concat(this.entries.stream().map(AbstractConfigEntry::getSearchTags).iterator()));
    }

    @Override
    public void setRequiresRestart(boolean requiresRestart) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.m_6375_(mouseX, mouseY, button);
    }

    public Component getCategoryName() {
        return this.getFieldName();
    }

    @Override
    public T getValue() {
        return this.object;
    }

    @Override
    public Optional<T> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        boolean insideWidget = this.widget.rectangle.contains(mouseX, mouseY);
        RenderSystem.setShaderTexture(0, CONFIG_TEX);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(CONFIG_TEX, x - 15, y + 5, 24, (this.isEnabled() ? (insideWidget ? 18 : 0) : 36) + (this.isExpanded() ? 9 : 0), 9, 9);
        graphics.drawString(Minecraft.getInstance().font, this.getDisplayedFieldName().getVisualOrderText(), x, y + 6, insideWidget ? -1638890 : -1);
        for (AbstractConfigListEntry entry : this.entries) {
            entry.setParent(this.getParent());
            entry.setScreen(this.getConfigScreen());
        }
        if (this.isExpanded()) {
            int yy = y + 24;
            for (AbstractConfigListEntry<?> entry : this.entries) {
                entry.render(graphics, -1, yy, x + 14, entryWidth - 14, entry.getItemHeight(), mouseX, mouseY, isHovered, delta);
                yy += entry.getItemHeight();
                yy += Math.max(0, entry.getMorePossibleHeight());
            }
        }
    }

    @Override
    public Rectangle getEntryArea(int x, int y, int entryWidth, int entryHeight) {
        this.widget.rectangle.x = x - 15;
        this.widget.rectangle.y = y;
        this.widget.rectangle.width = entryWidth + 15;
        this.widget.rectangle.height = 24;
        return new Rectangle(this.getParent().left, y, this.getParent().right - this.getParent().left, 20);
    }

    @Override
    public int getItemHeight() {
        if (!this.isExpanded()) {
            return 24;
        } else {
            int i = 24;
            for (AbstractConfigListEntry<?> entry : this.entries) {
                i += entry.getItemHeight();
            }
            return i;
        }
    }

    @Override
    public void updateSelected(boolean isSelected) {
        for (AbstractConfigListEntry<?> entry : this.entries) {
            entry.updateSelected(this.isExpanded() && isSelected && this.m_7222_() == entry);
        }
    }

    @Override
    public int getInitialReferenceOffset() {
        return 24;
    }

    @Override
    public void lateRender(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (this.isExpanded()) {
            for (AbstractConfigListEntry<?> entry : this.entries) {
                entry.lateRender(graphics, mouseX, mouseY, delta);
            }
        }
    }

    @Override
    public int getMorePossibleHeight() {
        if (!this.isExpanded()) {
            return -1;
        } else {
            List<Integer> list = new ArrayList();
            int i = 24;
            for (AbstractConfigListEntry<?> entry : this.entries) {
                i += entry.getItemHeight();
                if (entry.getMorePossibleHeight() >= 0) {
                    list.add(i + entry.getMorePossibleHeight());
                }
            }
            list.add(i);
            return (Integer) list.stream().max(Integer::compare).orElse(0) - this.getItemHeight();
        }
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return this.isExpanded() ? this.children : Collections.singletonList(this.widget);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return this.isExpanded() ? this.children : Collections.singletonList(this.widget);
    }

    @Override
    public void save() {
        this.entries.forEach(AbstractConfigEntry::save);
    }

    @Override
    public Optional<Component> getError() {
        List<Component> errors = (List<Component>) this.entries.stream().map(AbstractConfigEntry::getConfigError).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        return errors.size() > 1 ? Optional.of(Component.translatable("text.cloth-config.multi_error")) : errors.stream().findFirst();
    }

    @Override
    public boolean isExpanded() {
        return this.expanded && this.isEnabled();
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public class CategoryLabelWidget implements GuiEventListener, NarratableEntry {

        private final Rectangle rectangle = new Rectangle();

        private boolean isHovered;

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int int_1) {
            if (MultiElementListEntry.this.isEnabled() && this.rectangle.contains(mouseX, mouseY)) {
                MultiElementListEntry.this.setExpanded(!MultiElementListEntry.this.expanded);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return this.isHovered = true;
            } else {
                return this.isHovered = false;
            }
        }

        @Override
        public void setFocused(boolean bl) {
        }

        @Override
        public boolean isFocused() {
            return false;
        }

        @Override
        public NarratableEntry.NarrationPriority narrationPriority() {
            return this.isHovered ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {
            narrationElementOutput.add(NarratedElementType.TITLE, MultiElementListEntry.this.getFieldName());
        }
    }
}