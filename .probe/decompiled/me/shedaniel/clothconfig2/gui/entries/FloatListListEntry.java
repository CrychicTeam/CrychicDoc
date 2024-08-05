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
public class FloatListListEntry extends AbstractTextFieldListListEntry<Float, FloatListListEntry.FloatListCell, FloatListListEntry> {

    private float minimum = Float.NEGATIVE_INFINITY;

    private float maximum = Float.POSITIVE_INFINITY;

    @Deprecated
    @Internal
    public FloatListListEntry(Component fieldName, List<Float> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Float>> saveConsumer, Supplier<List<Float>> defaultValue, Component resetButtonKey) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, false);
    }

    @Deprecated
    @Internal
    public FloatListListEntry(Component fieldName, List<Float> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Float>> saveConsumer, Supplier<List<Float>> defaultValue, Component resetButtonKey, boolean requiresRestart) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, true, true);
    }

    @Deprecated
    @Internal
    public FloatListListEntry(Component fieldName, List<Float> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<Float>> saveConsumer, Supplier<List<Float>> defaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront, FloatListListEntry.FloatListCell::new);
    }

    public FloatListListEntry setMaximum(float maximum) {
        this.maximum = maximum;
        return this;
    }

    public FloatListListEntry setMinimum(float minimum) {
        this.minimum = minimum;
        return this;
    }

    public FloatListListEntry self() {
        return this;
    }

    public static class FloatListCell extends AbstractTextFieldListListEntry.AbstractTextFieldListCell<Float, FloatListListEntry.FloatListCell, FloatListListEntry> {

        public FloatListCell(Float value, FloatListListEntry listListEntry) {
            super(value, listListEntry);
        }

        @Nullable
        protected Float substituteDefault(@Nullable Float value) {
            return value == null ? 0.0F : value;
        }

        @Override
        protected boolean isValidText(@NotNull String text) {
            return text.chars().allMatch(c -> Character.isDigit(c) || c == 45 || c == 46);
        }

        public Float getValue() {
            try {
                return Float.valueOf(this.widget.getValue());
            } catch (NumberFormatException var2) {
                return 0.0F;
            }
        }

        @Override
        public Optional<Component> getError() {
            try {
                float i = Float.parseFloat(this.widget.getValue());
                if (i > this.listListEntry.maximum) {
                    return Optional.of(Component.translatable("text.cloth-config.error.too_large", this.listListEntry.maximum));
                }
                if (i < this.listListEntry.minimum) {
                    return Optional.of(Component.translatable("text.cloth-config.error.too_small", this.listListEntry.minimum));
                }
            } catch (NumberFormatException var2) {
                return Optional.of(Component.translatable("text.cloth-config.error.not_valid_number_float"));
            }
            return Optional.empty();
        }
    }
}