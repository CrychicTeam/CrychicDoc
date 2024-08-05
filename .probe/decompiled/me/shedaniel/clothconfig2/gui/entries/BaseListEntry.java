package me.shedaniel.clothconfig2.gui.entries;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import me.shedaniel.clothconfig2.api.Expandable;
import me.shedaniel.clothconfig2.api.ReferenceProvider;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public abstract class BaseListEntry<T, C extends BaseListCell, SELF extends BaseListEntry<T, C, SELF>> extends TooltipListEntry<List<T>> implements Expandable {

    protected static final ResourceLocation CONFIG_TEX = new ResourceLocation("cloth-config2", "textures/gui/cloth_config.png");

    @NotNull
    protected final List<C> cells;

    @NotNull
    protected final List<GuiEventListener> widgets;

    @NotNull
    protected final List<NarratableEntry> narratables;

    protected boolean expanded;

    protected boolean insertButtonEnabled = true;

    protected boolean deleteButtonEnabled;

    protected boolean insertInFront;

    protected BaseListEntry<T, C, SELF>.ListLabelWidget labelWidget;

    protected AbstractWidget resetWidget;

    @NotNull
    protected Function<SELF, C> createNewInstance;

    @NotNull
    protected Supplier<List<T>> defaultValue;

    @Nullable
    protected Component addTooltip = Component.translatable("text.cloth-config.list.add");

    @Nullable
    protected Component removeTooltip = Component.translatable("text.cloth-config.list.remove");

    @Internal
    public BaseListEntry(@NotNull Component fieldName, @Nullable Supplier<Optional<Component[]>> tooltipSupplier, @Nullable Supplier<List<T>> defaultValue, @NotNull Function<SELF, C> createNewInstance, @Nullable Consumer<List<T>> saveConsumer, Component resetButtonKey) {
        this(fieldName, tooltipSupplier, defaultValue, createNewInstance, saveConsumer, resetButtonKey, false);
    }

    @Internal
    public BaseListEntry(@NotNull Component fieldName, @Nullable Supplier<Optional<Component[]>> tooltipSupplier, @Nullable Supplier<List<T>> defaultValue, @NotNull Function<SELF, C> createNewInstance, @Nullable Consumer<List<T>> saveConsumer, Component resetButtonKey, boolean requiresRestart) {
        this(fieldName, tooltipSupplier, defaultValue, createNewInstance, saveConsumer, resetButtonKey, requiresRestart, true, true);
    }

    @Internal
    public BaseListEntry(@NotNull Component fieldName, @Nullable Supplier<Optional<Component[]>> tooltipSupplier, @Nullable Supplier<List<T>> defaultValue, @NotNull Function<SELF, C> createNewInstance, @Nullable Consumer<List<T>> saveConsumer, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(fieldName, tooltipSupplier, requiresRestart);
        this.deleteButtonEnabled = deleteButtonEnabled;
        this.insertInFront = insertInFront;
        this.cells = Lists.newArrayList();
        this.labelWidget = new BaseListEntry.ListLabelWidget();
        this.widgets = Lists.newArrayList(new GuiEventListener[] { this.labelWidget });
        this.narratables = Lists.newArrayList();
        this.resetWidget = Button.builder(resetButtonKey, widget -> {
            this.widgets.removeAll(this.cells);
            this.narratables.removeAll(this.cells);
            for (C cell : this.cells) {
                cell.onDelete();
            }
            this.cells.clear();
            ((List) defaultValue.get()).stream().map(this::getFromValue).forEach(this.cells::add);
            for (C cell : this.cells) {
                cell.onAdd();
            }
            this.widgets.addAll(this.cells);
            this.narratables.addAll(this.cells);
        }).bounds(0, 0, Minecraft.getInstance().font.width(resetButtonKey) + 6, 20).build();
        this.widgets.add(this.resetWidget);
        this.narratables.add(this.resetWidget);
        this.saveCallback = saveConsumer;
        this.createNewInstance = createNewInstance;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean isExpanded() {
        return this.expanded && this.isEnabled();
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public boolean isEdited() {
        return super.isEdited() ? true : this.cells.stream().anyMatch(BaseListCell::isEdited);
    }

    public boolean isMatchDefault() {
        Optional<List<T>> defaultValueOptional = this.getDefaultValue();
        if (defaultValueOptional.isPresent()) {
            List<T> value = this.getValue();
            List<T> defaultValue = (List<T>) defaultValueOptional.get();
            if (value.size() != defaultValue.size()) {
                return false;
            } else {
                for (int i = 0; i < value.size(); i++) {
                    if (!Objects.equals(value.get(i), defaultValue.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean isRequiresRestart() {
        return this.cells.stream().anyMatch(BaseListCell::isRequiresRestart);
    }

    @Override
    public void setRequiresRestart(boolean requiresRestart) {
    }

    public abstract SELF self();

    public boolean isDeleteButtonEnabled() {
        return this.deleteButtonEnabled && this.isEnabled();
    }

    public boolean isInsertButtonEnabled() {
        return this.insertButtonEnabled && this.isEnabled();
    }

    public void setDeleteButtonEnabled(boolean deleteButtonEnabled) {
        this.deleteButtonEnabled = deleteButtonEnabled;
    }

    public void setInsertButtonEnabled(boolean insertButtonEnabled) {
        this.insertButtonEnabled = insertButtonEnabled;
    }

    protected abstract C getFromValue(T var1);

    @NotNull
    public Function<SELF, C> getCreateNewInstance() {
        return this.createNewInstance;
    }

    public void setCreateNewInstance(@NotNull Function<SELF, C> createNewInstance) {
        this.createNewInstance = createNewInstance;
    }

    @Nullable
    public Component getAddTooltip() {
        return this.addTooltip;
    }

    public void setAddTooltip(@Nullable Component addTooltip) {
        this.addTooltip = addTooltip;
    }

    @Nullable
    public Component getRemoveTooltip() {
        return this.removeTooltip;
    }

    public void setRemoveTooltip(@Nullable Component removeTooltip) {
        this.removeTooltip = removeTooltip;
    }

    @Override
    public Optional<List<T>> getDefaultValue() {
        return this.defaultValue == null ? Optional.empty() : Optional.ofNullable((List) this.defaultValue.get());
    }

    @Override
    public int getItemHeight() {
        if (!this.isExpanded()) {
            return 24;
        } else {
            int i = 24;
            for (BaseListCell entry : this.cells) {
                i += entry.getCellHeight();
            }
            return i;
        }
    }

    @Override
    public List<? extends GuiEventListener> children() {
        if (!this.isExpanded()) {
            List<GuiEventListener> elements = new ArrayList(this.widgets);
            elements.removeAll(this.cells);
            return elements;
        } else {
            return this.widgets;
        }
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return this.narratables;
    }

    @Override
    public Optional<Component> getError() {
        List<Component> errors = (List<Component>) this.cells.stream().map(BaseListCell::getConfigError).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        return errors.size() > 1 ? Optional.of(Component.translatable("text.cloth-config.multi_error")) : errors.stream().findFirst();
    }

    @Override
    public void save() {
        for (C cell : this.cells) {
            if (cell instanceof ReferenceProvider) {
                ((ReferenceProvider) cell).provideReferenceEntry().save();
            }
        }
        super.save();
    }

    @Override
    public Rectangle getEntryArea(int x, int y, int entryWidth, int entryHeight) {
        this.labelWidget.rectangle.x = x - 15;
        this.labelWidget.rectangle.y = y;
        this.labelWidget.rectangle.width = entryWidth + 15;
        this.labelWidget.rectangle.height = 24;
        return new Rectangle(this.getParent().left, y, this.getParent().right - this.getParent().left, 20);
    }

    protected boolean isInsideCreateNew(double mouseX, double mouseY) {
        return this.isInsertButtonEnabled() && mouseX >= (double) (this.labelWidget.rectangle.x + 12) && mouseY >= (double) (this.labelWidget.rectangle.y + 3) && mouseX <= (double) (this.labelWidget.rectangle.x + 12 + 11) && mouseY <= (double) (this.labelWidget.rectangle.y + 3 + 11);
    }

    protected boolean isInsideDelete(double mouseX, double mouseY) {
        return this.isDeleteButtonEnabled() && mouseX >= (double) (this.labelWidget.rectangle.x + (this.isInsertButtonEnabled() ? 25 : 12)) && mouseY >= (double) (this.labelWidget.rectangle.y + 3) && mouseX <= (double) (this.labelWidget.rectangle.x + (this.isInsertButtonEnabled() ? 25 : 12) + 11) && mouseY <= (double) (this.labelWidget.rectangle.y + 3 + 11);
    }

    @Override
    public Optional<Component[]> getTooltip(int mouseX, int mouseY) {
        if (this.addTooltip != null && this.isInsideCreateNew((double) mouseX, (double) mouseY)) {
            return Optional.of(new Component[] { this.addTooltip });
        } else {
            return this.removeTooltip != null && this.isInsideDelete((double) mouseX, (double) mouseY) ? Optional.of(new Component[] { this.removeTooltip }) : super.getTooltip(mouseX, mouseY);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        RenderSystem.setShaderTexture(0, CONFIG_TEX);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BaseListCell focused = this.isExpanded() && this.m_7222_() != null && this.m_7222_() instanceof BaseListCell ? (BaseListCell) this.m_7222_() : null;
        boolean insideLabel = this.labelWidget.rectangle.contains(mouseX, mouseY);
        boolean insideCreateNew = this.isInsideCreateNew((double) mouseX, (double) mouseY);
        boolean insideDelete = this.isInsideDelete((double) mouseX, (double) mouseY);
        graphics.blit(CONFIG_TEX, x - 15, y + 5, 33, (this.isEnabled() ? (insideLabel && !insideCreateNew && !insideDelete ? 18 : 0) : 36) + (this.isExpanded() ? 9 : 0), 9, 9);
        if (this.isInsertButtonEnabled()) {
            graphics.blit(CONFIG_TEX, x - 15 + 13, y + 5, 42, insideCreateNew ? 9 : 0, 9, 9);
        }
        if (this.isDeleteButtonEnabled()) {
            graphics.blit(CONFIG_TEX, x - 15 + (this.isInsertButtonEnabled() ? 26 : 13), y + 5, 51, focused == null ? 0 : (insideDelete ? 18 : 9), 9, 9);
        }
        this.resetWidget.setX(x + entryWidth - this.resetWidget.getWidth());
        this.resetWidget.setY(y);
        this.resetWidget.active = this.isEditable() && this.getDefaultValue().isPresent() && !this.isMatchDefault();
        this.resetWidget.render(graphics, mouseX, mouseY, delta);
        int offset = (!this.isInsertButtonEnabled() && !this.isDeleteButtonEnabled() ? 0 : 6) + (this.isInsertButtonEnabled() ? 9 : 0) + (this.isDeleteButtonEnabled() ? 9 : 0);
        graphics.drawString(Minecraft.getInstance().font, this.getDisplayedFieldName().getVisualOrderText(), x + offset, y + 6, insideLabel && !this.resetWidget.isMouseOver((double) mouseX, (double) mouseY) && !insideDelete && !insideCreateNew ? -1638890 : this.getPreferredTextColor());
        if (this.isExpanded()) {
            int yy = y + 24;
            for (BaseListCell cell : this.cells) {
                cell.render(graphics, -1, yy, x + 14, entryWidth - 14, cell.getCellHeight(), mouseX, mouseY, this.getParent().getFocused() != null && this.getParent().getFocused().equals(this) && this.m_7222_() != null && this.m_7222_().equals(cell), delta);
                yy += cell.getCellHeight();
            }
        }
    }

    @Override
    public void updateSelected(boolean isSelected) {
        for (C cell : this.cells) {
            cell.updateSelected(isSelected && this.m_7222_() == cell && this.isExpanded());
        }
    }

    @Override
    public int getInitialReferenceOffset() {
        return 24;
    }

    public boolean insertInFront() {
        return this.insertInFront;
    }

    public class ListLabelWidget implements GuiEventListener {

        protected Rectangle rectangle = new Rectangle();

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int int_1) {
            if (!BaseListEntry.this.isEnabled()) {
                return false;
            } else if (BaseListEntry.this.resetWidget.isMouseOver(mouseX, mouseY)) {
                return false;
            } else if (BaseListEntry.this.isInsideCreateNew(mouseX, mouseY)) {
                BaseListEntry.this.setExpanded(true);
                C cell;
                if (BaseListEntry.this.insertInFront()) {
                    BaseListEntry.this.cells.add(0, cell = (C) BaseListEntry.this.createNewInstance.apply(BaseListEntry.this.self()));
                    BaseListEntry.this.widgets.add(0, cell);
                } else {
                    BaseListEntry.this.cells.add(cell = (C) BaseListEntry.this.createNewInstance.apply(BaseListEntry.this.self()));
                    BaseListEntry.this.widgets.add(cell);
                }
                cell.onAdd();
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            } else if (BaseListEntry.this.isDeleteButtonEnabled() && BaseListEntry.this.isInsideDelete(mouseX, mouseY)) {
                GuiEventListener focused = BaseListEntry.this.m_7222_();
                if (BaseListEntry.this.isExpanded() && focused instanceof BaseListCell) {
                    ((BaseListCell) focused).onDelete();
                    BaseListEntry.this.cells.remove(focused);
                    BaseListEntry.this.widgets.remove(focused);
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
                return true;
            } else if (this.rectangle.contains(mouseX, mouseY)) {
                BaseListEntry.this.setExpanded(!BaseListEntry.this.expanded);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void setFocused(boolean bl) {
        }

        @Override
        public boolean isFocused() {
            return false;
        }
    }
}