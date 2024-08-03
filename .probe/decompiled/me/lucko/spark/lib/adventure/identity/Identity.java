package me.lucko.spark.lib.adventure.identity;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.key.Key;
import me.lucko.spark.lib.adventure.pointer.Pointer;
import me.lucko.spark.lib.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface Identity extends Examinable {

    Pointer<String> NAME = Pointer.pointer(String.class, Key.key("adventure", "name"));

    Pointer<UUID> UUID = Pointer.pointer(UUID.class, Key.key("adventure", "uuid"));

    Pointer<Component> DISPLAY_NAME = Pointer.pointer(Component.class, Key.key("adventure", "display_name"));

    Pointer<Locale> LOCALE = Pointer.pointer(Locale.class, Key.key("adventure", "locale"));

    @NotNull
    static Identity nil() {
        return NilIdentity.INSTANCE;
    }

    @NotNull
    static Identity identity(@NotNull final UUID uuid) {
        return (Identity) (uuid.equals(NilIdentity.NIL_UUID) ? NilIdentity.INSTANCE : new IdentityImpl(uuid));
    }

    @NotNull
    UUID uuid();

    @NotNull
    @Override
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("uuid", this.uuid()));
    }
}