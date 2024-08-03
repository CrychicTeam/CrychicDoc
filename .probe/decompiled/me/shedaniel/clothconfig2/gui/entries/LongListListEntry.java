package me.shedaniel.clothconfig2.gui.entries;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public class LongListListEntry extends AbstractTextFieldListListEntry<Long, LongListListEntry.LongListCell, LongListListEntry> {

    private long minimum = Long.MIN_VALUE;

    private long maximum = Long.MAX_VALUE;

    @Deprecated
    @Internal
    public LongListListEntry(Component fieldName, List<Long> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Long>> saveConsumer, Supplier<List<Long>> defaultValue, Component resetButtonKey) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, false);
    }

    @Deprecated
    @Internal
    public LongListListEntry(Component fieldName, List<Long> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Long>> saveConsumer, Supplier<List<Long>> defaultValue, Component resetButtonKey, boolean requiresRestart) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, true, true);
    }

    @Deprecated
    @Internal
    public LongListListEntry(Component fieldName, List<Long> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Long>> saveConsumer, Supplier<List<Long>> defaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront, LongListListEntry.LongListCell::new);
    }

    public LongListListEntry setMaximum(long maximum) {
        this.maximum = maximum;
        return this;
    }

    public LongListListEntry setMinimum(long minimum) {
        this.minimum = minimum;
        return this;
    }

    public LongListListEntry self() {
        return this;
    }

    public static class LongListCell extends AbstractTextFieldListListEntry.AbstractTextFieldListCell<Long, LongListListEntry.LongListCell, LongListListEntry> {

        public LongListCell(Long value, LongListListEntry listListEntry) {
            super(value, listListEntry);
        }

        @Nullable
        protected Long substituteDefault(@Nullable Long value) {
            return value == null ? 0L : value;
        }

        @Override
        protected boolean isValidText(@NotNull String text) {
            return text.chars().allMatch(c -> Character.isDigit(c) || c == 45);
        }

        public Long getValue() {
            try {
                return Long.valueOf(this.widget.getValue());
            } catch (NumberFormatException var2) {
                return 0L;
            }
        }

        @Override
        public Optional<Component> getError() {
            try {
                long l = Long.parseLong(this.widget.getValue());
                if (l > this.listListEntry.maximum) {
                    return Optional.of(Component.translatable("text.cloth-config.error.too_large", this.listListEntry.maximum));
                }
                if (l < this.listListEntry.minimum) {
                    return Optional.of(Component.translatable("text.cloth-config.error.too_small", this.listListEntry.minimum));
                }
            } catch (NumberFormatException var3) {
                return Optional.of(Component.translatable("text.cloth-config.error.not_valid_number_long"));
            }
            return Optional.empty();
        }
    }
}