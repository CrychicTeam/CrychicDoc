package dev.ftb.mods.ftblibrary.snbt.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftblibrary.snbt.SNBTUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public abstract class BaseValue<T> implements Comparable<BaseValue<T>> {

    public final SNBTConfig parent;

    public final String key;

    public final T defaultValue;

    private T value;

    boolean excluded;

    BooleanSupplier enabled = SNBTUtils.ALWAYS_TRUE;

    protected int displayOrder = 0;

    List<String> comment = new ArrayList(0);

    public BaseValue(@Nullable SNBTConfig c, String n, T def) {
        this.parent = c;
        this.key = n;
        this.defaultValue = def;
        this.value = this.defaultValue;
    }

    public String toString() {
        return this.parent == null ? this.key : this.parent + "/" + this.key;
    }

    public T get() {
        return this.value;
    }

    public void set(T v) {
        this.value = v;
    }

    public <E extends BaseValue<T>> E comment(String... s) {
        this.comment.addAll(Arrays.asList(s));
        return (E) this;
    }

    public <E extends BaseValue<T>> E excluded() {
        this.excluded = true;
        return (E) this;
    }

    public <E extends BaseValue<T>> E enabled(BooleanSupplier e) {
        this.enabled = e;
        return (E) this;
    }

    public abstract void write(SNBTCompoundTag var1);

    public abstract void read(SNBTCompoundTag var1);

    private int getOrder() {
        return this instanceof SNBTConfig ? 1 : 0;
    }

    public BaseValue<T> withDisplayOrder(int order) {
        this.displayOrder = order;
        return this;
    }

    public int compareTo(BaseValue<T> o) {
        int i = Integer.compare(this.getOrder(), o.getOrder());
        return i == 0 ? this.key.compareToIgnoreCase(o.key) : i;
    }

    @OnlyIn(Dist.CLIENT)
    public void createClientConfig(ConfigGroup group) {
    }
}