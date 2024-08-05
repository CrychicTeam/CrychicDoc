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
public class IntegerListListEntry extends AbstractTextFieldListListEntry<Integer, IntegerListListEntry.IntegerListCell, IntegerListListEntry> {

    private int minimum = Integer.MIN_VALUE;

    private int maximum = Integer.MAX_VALUE;

    @Deprecated
    @Internal
    public IntegerListListEntry(Component fieldName, List<Integer> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Integer>> saveConsumer, Supplier<List<Integer>> defaultValue, Component resetButtonKey) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, false);
    }

    @Deprecated
    @Internal
    public IntegerListListEntry(Component fieldName, List<Integer> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Integer>> saveConsumer, Supplier<List<Integer>> defaultValue, Component resetButtonKey, boolean requiresRestart) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, true, true);
    }

    @Deprecated
    @Internal
    public IntegerListListEntry(Component fieldName, List<Integer> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Integer>> saveConsumer, Supplier<List<Integer>> defaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront, IntegerListListEntry.IntegerListCell::new);
    }

    public IntegerListListEntry setMaximum(int maximum) {
        this.maximum = maximum;
        return this;
    }

    public IntegerListListEntry setMinimum(int minimum) {
        this.minimum = minimum;
        return this;
    }

    public IntegerListListEntry self() {
        return this;
    }

    public static class IntegerListCell extends AbstractTextFieldListListEntry.AbstractTextFieldListCell<Integer, IntegerListListEntry.IntegerListCell, IntegerListListEntry> {

        public IntegerListCell(Integer value, IntegerListListEntry listListEntry) {
            super(value, listListEntry);
        }

        @Nullable
        protected Integer substituteDefault(@Nullable Integer value) {
            return value == null ? 0 : value;
        }

        @Override
        protected boolean isValidText(@NotNull String text) {
            return text.chars().allMatch(c -> Character.isDigit(c) || c == 45);
        }

        public Integer getValue() {
            try {
                return Integer.valueOf(this.widget.getValue());
            } catch (NumberFormatException var2) {
                return 0;
            }
        }

        @Override
        public Optional<Component> getError() {
            try {
                int i = Integer.parseInt(this.widget.getValue());
                if (i > this.listListEntry.maximum) {
                    return Optional.of(Component.translatable("text.cloth-config.error.too_large", this.listListEntry.maximum));
                }
                if (i < this.listListEntry.minimum) {
                    return Optional.of(Component.translatable("text.cloth-config.error.too_small", this.listListEntry.minimum));
                }
            } catch (NumberFormatException var2) {
                return Optional.of(Component.translatable("text.cloth-config.error.not_valid_number_int"));
            }
            return Optional.empty();
        }
    }
}