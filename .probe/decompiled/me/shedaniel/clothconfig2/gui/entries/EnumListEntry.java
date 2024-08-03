package me.shedaniel.clothconfig2.gui.entries;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public class EnumListEntry<T extends Enum<?>> extends SelectionListEntry<T> {

    public static final Function<Enum, Component> DEFAULT_NAME_PROVIDER = t -> Component.translatable(t instanceof SelectionListEntry.Translatable ? ((SelectionListEntry.Translatable) t).getKey() : t.toString());

    @Deprecated
    @Internal
    public EnumListEntry(Component fieldName, Class<T> clazz, T value, Component resetButtonKey, Supplier<T> defaultValue, Consumer<T> saveConsumer) {
        super(fieldName, (T[]) clazz.getEnumConstants(), value, resetButtonKey, defaultValue, saveConsumer, DEFAULT_NAME_PROVIDER::apply);
    }

    @Deprecated
    @Internal
    public EnumListEntry(Component fieldName, Class<T> clazz, T value, Component resetButtonKey, Supplier<T> defaultValue, Consumer<T> saveConsumer, Function<Enum, Component> enumNameProvider) {
        super(fieldName, (T[]) clazz.getEnumConstants(), value, resetButtonKey, defaultValue, saveConsumer, enumNameProvider::apply, null);
    }

    @Deprecated
    @Internal
    public EnumListEntry(Component fieldName, Class<T> clazz, T value, Component resetButtonKey, Supplier<T> defaultValue, Consumer<T> saveConsumer, Function<Enum, Component> enumNameProvider, Supplier<Optional<Component[]>> tooltipSupplier) {
        super(fieldName, (T[]) clazz.getEnumConstants(), value, resetButtonKey, defaultValue, saveConsumer, enumNameProvider::apply, tooltipSupplier, false);
    }

    @Deprecated
    @Internal
    public EnumListEntry(Component fieldName, Class<T> clazz, T value, Component resetButtonKey, Supplier<T> defaultValue, Consumer<T> saveConsumer, Function<Enum, Component> enumNameProvider, Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, (T[]) clazz.getEnumConstants(), value, resetButtonKey, defaultValue, saveConsumer, enumNameProvider::apply, tooltipSupplier, requiresRestart);
    }
}