package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.math.MathUtils;
import dev.ftb.mods.ftblibrary.util.StringUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public final class NameMap<E> implements Iterable<E> {

    private final NameMap.Builder<E> builder;

    public final E defaultValue;

    public final Map<String, E> map;

    public final List<String> keys;

    public final List<E> values;

    public static <T> NameMap.Builder<T> of(T defaultValue, List<T> values) {
        return new NameMap.Builder<>(defaultValue, values);
    }

    public static <T> NameMap.Builder<T> of(T defaultValue, T[] values) {
        return of(defaultValue, Arrays.asList(values));
    }

    private NameMap(NameMap.Builder<E> b) {
        this.builder = b;
        this.values = Collections.unmodifiableList(b.values);
        Map<String, E> map0 = new LinkedHashMap(this.size());
        for (E value : this.values) {
            map0.put(this.getName(value), value);
        }
        this.map = Collections.unmodifiableMap(map0);
        this.keys = List.copyOf(this.map.keySet());
        this.defaultValue = this.get(this.getName(this.builder.defaultValue));
    }

    private NameMap(E def, NameMap<E> n) {
        this.builder = n.builder;
        this.map = n.map;
        this.keys = n.keys;
        this.values = n.values;
        this.defaultValue = this.get(this.getName(def));
    }

    public String getName(E value) {
        return (String) this.builder.idProvider.apply(value);
    }

    public Component getDisplayName(E value) {
        return (Component) this.builder.nameProvider.apply(value);
    }

    public Color4I getColor(E value) {
        return (Color4I) this.builder.colorProvider.apply(value);
    }

    public NameMap<E> withDefault(E def) {
        return def == this.defaultValue ? this : new NameMap<>(def, this);
    }

    public int size() {
        return this.values.size();
    }

    public E get(@Nullable String s) {
        E value = this.getNullable(s);
        return value == null ? this.defaultValue : value;
    }

    @Nullable
    public E getNullable(@Nullable String s) {
        return (E) (s != null && !s.isEmpty() && s.charAt(0) != '-' ? this.map.get(s) : null);
    }

    public E get(int index) {
        return (E) (index >= 0 && index < this.size() ? this.values.get(index) : this.defaultValue);
    }

    public E offset(E value, int index) {
        return this.get(MathUtils.mod(this.getIndex(value) + index, this.size()));
    }

    public E getNext(E value) {
        return this.offset(value, 1);
    }

    public E getPrevious(E value) {
        return this.offset(value, -1);
    }

    public int getIndex(E e) {
        return this.values.indexOf(e);
    }

    public int getStringIndex(String s) {
        return this.getIndex((E) this.map.get(s));
    }

    public E getRandom(Random rand) {
        return (E) this.values.get(rand.nextInt(this.size()));
    }

    public Iterator<E> iterator() {
        return this.values.iterator();
    }

    public void write(FriendlyByteBuf data, E object) {
        data.writeVarInt(this.getIndex(object));
    }

    public E read(FriendlyByteBuf data) {
        return this.get(data.readVarInt());
    }

    public Icon getIcon(E v) {
        return (Icon) this.builder.iconProvider.apply(v);
    }

    public static final class Builder<T> {

        private final T defaultValue;

        private final List<T> values;

        private Function<T, String> idProvider = t -> StringUtils.getID(t, 6);

        private Function<T, Component> nameProvider = t -> Component.literal((String) this.idProvider.apply(t));

        private Function<T, Color4I> colorProvider = t -> Icon.empty();

        private Function<T, Icon> iconProvider = t -> Icon.empty();

        private Builder(T def, List<T> v) {
            this.defaultValue = def;
            this.values = v;
        }

        public NameMap.Builder<T> id(Function<T, String> p) {
            this.idProvider = p;
            return this;
        }

        public NameMap.Builder<T> name(Function<T, Component> p) {
            this.nameProvider = p;
            return this;
        }

        public NameMap.Builder<T> nameKey(Function<T, String> p) {
            return this.name(v -> Component.translatable((String) p.apply(v)));
        }

        public NameMap.Builder<T> baseNameKey(String key) {
            return this.name(v -> Component.translatable(key + "." + (String) this.idProvider.apply(v)));
        }

        public NameMap.Builder<T> color(Function<T, Color4I> p) {
            this.colorProvider = p;
            return this;
        }

        public NameMap.Builder<T> icon(Function<T, Icon> p) {
            this.iconProvider = p;
            return this;
        }

        public NameMap<T> create() {
            return new NameMap<>(this);
        }
    }
}