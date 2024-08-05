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
public class StringListListEntry extends AbstractTextFieldListListEntry<String, StringListListEntry.StringListCell, StringListListEntry> {

    @Deprecated
    @Internal
    public StringListListEntry(Component fieldName, List<String> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<String>> saveConsumer, Supplier<List<String>> defaultValue, Component resetButtonKey) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, false);
    }

    @Deprecated
    @Internal
    public StringListListEntry(Component fieldName, List<String> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<String>> saveConsumer, Supplier<List<String>> defaultValue, Component resetButtonKey, boolean requiresRestart) {
        this(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, true, true);
    }

    @Deprecated
    @Internal
    public StringListListEntry(Component fieldName, List<String> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<String>> saveConsumer, Supplier<List<String>> defaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront) {
        super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront, StringListListEntry.StringListCell::new);
    }

    public StringListListEntry self() {
        return this;
    }

    public static class StringListCell extends AbstractTextFieldListListEntry.AbstractTextFieldListCell<String, StringListListEntry.StringListCell, StringListListEntry> {

        public StringListCell(String value, StringListListEntry listListEntry) {
            super(value, listListEntry);
        }

        @Nullable
        protected String substituteDefault(@Nullable String value) {
            return value == null ? "" : value;
        }

        @Override
        protected boolean isValidText(@NotNull String text) {
            return true;
        }

        public String getValue() {
            return this.widget.getValue();
        }

        @Override
        public Optional<Component> getError() {
            return Optional.empty();
        }
    }
}