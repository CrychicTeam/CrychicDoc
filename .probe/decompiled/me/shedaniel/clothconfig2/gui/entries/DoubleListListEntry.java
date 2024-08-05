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
public class DoubleListListEntry extends AbstractTextFieldListListEntry<Double, DoubleListListEntry.DoubleListCell, DoubleListListEntry> {

    private double minimum = Double.NEGATIVE_INFINITY;

    private double maximum = Double.POSITIVE_INFINITY;

    @Deprecated
    @Internal
    public DoubleListListEntry(Component fieldName, List<Double> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Double>> saveConsumer, Supplier<List<Double>> defaultValue, Component resetButtonKey) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, false);
    }

    @Deprecated
    @Internal
    public DoubleListListEntry(Component fieldName, List<Double> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Double>> saveConsumer, Supplier<List<Double>> defaultValue, Component resetButtonKey, boolean requiresRestart) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, true, true);
    }

    @Deprecated
    @Internal
    public DoubleListListEntry(Component fieldName, List<Double> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Double>> saveConsumer, Supplier<List<Double>> defaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront, DoubleListListEntry.DoubleListCell::new);
    }

    public DoubleListListEntry setMaximum(Double maximum) {
        this.maximum = maximum;
        return this;
    }

    public DoubleListListEntry setMinimum(Double minimum) {
        this.minimum = minimum;
        return this;
    }

    public DoubleListListEntry self() {
        return this;
    }

    public static class DoubleListCell extends AbstractTextFieldListListEntry.AbstractTextFieldListCell<Double, DoubleListListEntry.DoubleListCell, DoubleListListEntry> {

        public DoubleListCell(Double value, DoubleListListEntry listListEntry) {
            super(value, listListEntry);
        }

        @Nullable
        protected Double substituteDefault(@Nullable Double value) {
            return value == null ? 0.0 : value;
        }

        @Override
        protected boolean isValidText(@NotNull String text) {
            return text.chars().allMatch(c -> Character.isDigit(c) || c == 45 || c == 46);
        }

        public Double getValue() {
            try {
                return Double.valueOf(this.widget.getValue());
            } catch (NumberFormatException var2) {
                return 0.0;
            }
        }

        @Override
        public Optional<Component> getError() {
            try {
                double i = Double.parseDouble(this.widget.getValue());
                if (i > this.listListEntry.maximum) {
                    return Optional.of(Component.translatable("text.cloth-config.error.too_large", this.listListEntry.maximum));
                }
                if (i < this.listListEntry.minimum) {
                    return Optional.of(Component.translatable("text.cloth-config.error.too_small", this.listListEntry.minimum));
                }
            } catch (NumberFormatException var3) {
                return Optional.of(Component.translatable("text.cloth-config.error.not_valid_number_double"));
            }
            return Optional.empty();
        }
    }
}