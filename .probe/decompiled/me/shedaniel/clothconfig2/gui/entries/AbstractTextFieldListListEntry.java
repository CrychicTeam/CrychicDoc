package me.shedaniel.clothconfig2.gui.entries;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractTextFieldListListEntry<T, C extends AbstractTextFieldListListEntry.AbstractTextFieldListCell<T, C, SELF>, SELF extends AbstractTextFieldListListEntry<T, C, SELF>> extends AbstractListListEntry<T, C, SELF> {

    @Internal
    public AbstractTextFieldListListEntry(Component fieldName, List<T> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<T>> saveConsumer, Supplier<List<T>> defaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront, BiFunction<T, SELF, C> createNewCell) {
        super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront, createNewCell);
    }

    @Internal
    public abstract static class AbstractTextFieldListCell<T, SELF extends AbstractTextFieldListListEntry.AbstractTextFieldListCell<T, SELF, OUTER_SELF>, OUTER_SELF extends AbstractTextFieldListListEntry<T, SELF, OUTER_SELF>> extends AbstractListListEntry.AbstractListCell<T, SELF, OUTER_SELF> {

        protected EditBox widget;

        private boolean isSelected;

        private boolean isHovered;

        public AbstractTextFieldListCell(@Nullable T value, OUTER_SELF listListEntry) {
            super(value, listListEntry);
            T finalValue = this.substituteDefault(value);
            this.widget = new EditBox(Minecraft.getInstance().font, 0, 0, 100, 18, Component.empty()) {

                @Override
                public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
                    this.m_93692_(AbstractTextFieldListCell.this.isSelected);
                    super.m_88315_(graphics, mouseX, mouseY, delta);
                }
            };
            this.widget.setFilter(this::isValidText);
            this.widget.setMaxLength(Integer.MAX_VALUE);
            this.widget.setBordered(false);
            this.widget.setValue(Objects.toString(finalValue));
            this.widget.moveCursorToStart();
            this.widget.setResponder(s -> this.widget.setTextColor(this.getPreferredTextColor()));
        }

        @Override
        public void updateSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        @Nullable
        protected abstract T substituteDefault(@Nullable T var1);

        protected abstract boolean isValidText(@NotNull String var1);

        @Override
        public int getCellHeight() {
            return 20;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            this.widget.m_93674_(entryWidth - 12);
            this.widget.m_252865_(x);
            this.widget.m_253211_(y + 1);
            this.widget.setEditable(this.listListEntry.isEditable());
            this.widget.m_88315_(graphics, mouseX, mouseY, delta);
            this.isHovered = this.widget.isMouseOver((double) mouseX, (double) mouseY);
            if (isSelected && this.listListEntry.isEditable()) {
                graphics.fill(x, y + 12, x + entryWidth - 12, y + 13, this.getConfigError().isPresent() ? -43691 : -2039584);
            }
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.widget);
        }

        @Override
        public NarratableEntry.NarrationPriority narrationPriority() {
            return this.isSelected ? NarratableEntry.NarrationPriority.FOCUSED : (this.isHovered ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE);
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {
            this.widget.m_142291_(narrationElementOutput);
        }
    }
}