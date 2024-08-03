package harmonised.pmmo.util;

import java.util.Arrays;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TagBuilder {

    private CompoundTag nbt = new CompoundTag();

    private TagBuilder() {
    }

    public static TagBuilder start() {
        return new TagBuilder();
    }

    public CompoundTag build() {
        return this.nbt;
    }

    public TagBuilder withString(@NonNull String key, @NonNull String value) {
        this.nbt.putString(key, value);
        return this;
    }

    public TagBuilder withBool(@NonNull String key, @NonNull boolean value) {
        this.nbt.putBoolean(key, value);
        return this;
    }

    public TagBuilder withFloat(@NonNull String key, @NonNull float value) {
        this.nbt.putFloat(key, value);
        return this;
    }

    public TagBuilder withList(@NonNull String key, @NonNull ListTag list) {
        this.nbt.put(key, list);
        return this;
    }

    public TagBuilder withList(@NonNull String key, Tag... tags) {
        ListTag list = new ListTag();
        list.addAll(Arrays.stream(tags).toList());
        this.nbt.put(key, list);
        return this;
    }

    public TagBuilder withInt(@NonNull String key, @NonNull int value) {
        this.nbt.putInt(key, value);
        return this;
    }

    public TagBuilder withDouble(@NonNull String key, @NonNull double value) {
        this.nbt.putDouble(key, value);
        return this;
    }

    public TagBuilder withLong(@NonNull String key, @NonNull long value) {
        this.nbt.putLong(key, value);
        return this;
    }
}