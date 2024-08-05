package me.shedaniel.clothconfig2.api;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;
import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractConfigEntry<T> extends DynamicElementListWidget.ElementEntry<AbstractConfigEntry<T>> implements ReferenceProvider<T>, ValueHolder<T> {

    private AbstractConfigScreen screen;

    private Supplier<Optional<Component>> errorSupplier;

    @Nullable
    private List<ReferenceProvider<?>> referencableEntries = null;

    @Nullable
    protected Consumer<T> saveCallback;

    private int cacheFieldNameHash = -1;

    private List<String> cachedTags = null;

    private Iterable<String> additionalSearchTags = null;

    public final void setReferenceProviderEntries(@Nullable List<ReferenceProvider<?>> referencableEntries) {
        this.referencableEntries = referencableEntries;
    }

    public void requestReferenceRebuilding() {
        AbstractConfigScreen configScreen = this.getConfigScreen();
        if (configScreen instanceof ReferenceBuildingConfigScreen) {
            ((ReferenceBuildingConfigScreen) configScreen).requestReferenceRebuilding();
        }
    }

    @NotNull
    @Override
    public AbstractConfigEntry<T> provideReferenceEntry() {
        return this;
    }

    @Nullable
    @Internal
    public final List<ReferenceProvider<?>> getReferenceProviderEntries() {
        return this.referencableEntries;
    }

    public abstract boolean isRequiresRestart();

    public abstract void setRequiresRestart(boolean var1);

    public abstract Component getFieldName();

    public Component getDisplayedFieldName() {
        MutableComponent text = this.getFieldName().copy();
        boolean hasError = this.getConfigError().isPresent();
        boolean isEdited = this.isEdited();
        if (hasError) {
            text = text.withStyle(ChatFormatting.RED);
        }
        if (isEdited) {
            text = text.withStyle(ChatFormatting.ITALIC);
        }
        if (!hasError && !isEdited) {
            text = text.withStyle(ChatFormatting.GRAY);
        }
        if (!this.isEnabled()) {
            text = text.withStyle(ChatFormatting.DARK_GRAY);
        }
        return text;
    }

    public Iterator<String> getSearchTags() {
        String s = this.getFieldName().getString();
        if (s.isEmpty()) {
            this.cacheFieldNameHash = -1;
            this.cachedTags = null;
            return ((Iterable) MoreObjects.firstNonNull(this.additionalSearchTags, Collections.emptyList())).iterator();
        } else {
            if (s.hashCode() != this.cacheFieldNameHash) {
                this.cacheFieldNameHash = s.hashCode();
                this.cachedTags = Lists.newArrayList(s.split(" "));
            }
            return Iterators.concat(this.cachedTags.iterator(), ((Iterable) MoreObjects.firstNonNull(this.additionalSearchTags, Collections.emptyList())).iterator());
        }
    }

    public void appendSearchTags(Iterable<String> tags) {
        if (this.additionalSearchTags == null) {
            this.additionalSearchTags = tags;
        } else {
            this.additionalSearchTags = Iterables.concat(this.additionalSearchTags, tags);
        }
    }

    public final Optional<Component> getConfigError() {
        return this.errorSupplier != null && ((Optional) this.errorSupplier.get()).isPresent() ? (Optional) this.errorSupplier.get() : this.getError();
    }

    public void lateRender(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
    }

    public void setErrorSupplier(Supplier<Optional<Component>> errorSupplier) {
        this.errorSupplier = errorSupplier;
    }

    public Optional<Component> getError() {
        return Optional.empty();
    }

    public abstract Optional<T> getDefaultValue();

    @Nullable
    public final AbstractConfigScreen getConfigScreen() {
        return this.screen;
    }

    public final void addTooltip(@NotNull Tooltip tooltip) {
        this.screen.addTooltip(tooltip);
    }

    protected FormattedCharSequence[] wrapLinesToScreen(Component[] lines) {
        return this.wrapLines(lines, this.screen.f_96543_);
    }

    protected FormattedCharSequence[] wrapLines(Component[] lines, int width) {
        Font font = Minecraft.getInstance().font;
        return (FormattedCharSequence[]) Arrays.stream(lines).map(line -> font.split(line, width)).flatMap(Collection::stream).toArray(FormattedCharSequence[]::new);
    }

    public void updateSelected(boolean isSelected) {
    }

    @Internal
    public final void setScreen(AbstractConfigScreen screen) {
        this.screen = screen;
    }

    public void save() {
        if (this.saveCallback != null) {
            this.saveCallback.accept(this.getValue());
        }
    }

    public boolean isEdited() {
        return this.getConfigError().isPresent();
    }

    @Override
    public int getItemHeight() {
        return 24;
    }

    public int getInitialReferenceOffset() {
        return 0;
    }
}