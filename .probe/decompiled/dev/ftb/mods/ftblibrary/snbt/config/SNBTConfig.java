package dev.ftb.mods.ftblibrary.snbt.config;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftblibrary.snbt.SNBTNet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class SNBTConfig extends BaseValue<List<BaseValue<?>>> {

    private int displayOrder = 0;

    public static SNBTConfig create(String name) {
        return new SNBTConfig(null, name, new ArrayList());
    }

    private SNBTConfig(SNBTConfig parent, String name, List<BaseValue<?>> defaultValue) {
        super(parent, name, defaultValue);
    }

    @Override
    public void write(SNBTCompoundTag tag) {
        if (this.parent == null) {
            tag.comment("", String.join("\n", this.comment));
            for (BaseValue<?> value : this.defaultValue.stream().sorted().toList()) {
                value.write(tag);
            }
        } else {
            tag.comment(this.key, String.join("\n", this.comment));
            SNBTCompoundTag newTag = new SNBTCompoundTag();
            for (BaseValue<?> value : this.defaultValue.stream().sorted().toList()) {
                value.write(newTag);
            }
            tag.m_128365_(this.key, newTag);
        }
    }

    @Override
    public void read(SNBTCompoundTag tag) {
        if (this.parent == null) {
            for (BaseValue<?> value : this.defaultValue) {
                if (tag.m_128441_(value.key)) {
                    value.read(tag);
                }
            }
        } else if (tag.m_128425_(this.key, 10)) {
            SNBTCompoundTag newTag = tag.getCompound(this.key);
            for (BaseValue<?> valuex : this.defaultValue) {
                if (newTag.m_128441_(valuex.key)) {
                    valuex.read(newTag);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void createClientConfig(ConfigGroup group) {
        List<BaseValue<?>> sorted = this.defaultValue.stream().filter(v -> !v.excluded).sorted(Comparator.comparingInt(o -> o.displayOrder)).toList();
        ConfigGroup g = this.parent == null ? group : group.getOrCreateSubgroup(this.key, this.displayOrder);
        sorted.forEach(value -> value.createClientConfig(g));
    }

    public void write(FriendlyByteBuf buf) {
        SNBTCompoundTag tag = new SNBTCompoundTag();
        this.write(tag);
        SNBTNet.write(buf, tag);
    }

    public void read(FriendlyByteBuf buf) {
        this.read(SNBTNet.readCompound(buf));
    }

    public void load(Path path) {
        SNBTCompoundTag tag = SNBT.read(path);
        if (tag != null) {
            this.read(tag);
        }
        this.save(path);
    }

    public void save(Path path) {
        this.saveNow(path);
    }

    public void saveNow(Path path) {
        if (this.parent != null) {
            this.parent.saveNow(path);
        } else {
            SNBTCompoundTag tag = new SNBTCompoundTag();
            this.write(tag);
            SNBT.write(path, tag);
        }
    }

    public void load(Path path, Path defaultPath, Supplier<String[]> comment) {
        if (Files.exists(defaultPath, new LinkOption[0])) {
            if (!Files.exists(path, new LinkOption[0])) {
                try {
                    Files.createDirectories(path.getParent());
                    Files.copy(defaultPath, path);
                } catch (IOException var5) {
                    var5.printStackTrace();
                }
            }
        } else {
            SNBTConfig defaultConfigFile = create(this.key);
            defaultConfigFile.comment((String[]) comment.get());
            defaultConfigFile.save(defaultPath);
        }
        this.load(path);
    }

    public <T extends BaseValue<?>> T add(T value) {
        this.defaultValue.add(value);
        return value;
    }

    public SNBTConfig addGroup(String key) {
        return this.addGroup(key, 0);
    }

    public SNBTConfig addGroup(String key, int displayOrder) {
        SNBTConfig value = new SNBTConfig(this, key, new ArrayList());
        value.displayOrder = displayOrder;
        return this.add(value);
    }

    public BooleanValue addBoolean(String key, boolean def) {
        return this.add(new BooleanValue(this, key, def));
    }

    public IntValue addInt(String key, int def) {
        return this.add(new IntValue(this, key, def));
    }

    public IntValue addInt(String key, int def, int min, int max) {
        return this.addInt(key, def).range(Integer.valueOf(min), Integer.valueOf(max));
    }

    public LongValue addLong(String key, long def) {
        return this.add(new LongValue(this, key, def));
    }

    public LongValue addLong(String key, long def, long min, long max) {
        return this.addLong(key, def).range(Long.valueOf(min), Long.valueOf(max));
    }

    public DoubleValue addDouble(String key, double def) {
        return this.add(new DoubleValue(this, key, def));
    }

    public DoubleValue addDouble(String key, double def, double min, double max) {
        return this.addDouble(key, def).range(Double.valueOf(min), Double.valueOf(max));
    }

    public StringValue addString(String key, String def) {
        return this.add(new StringValue(this, key, def));
    }

    public StringListValue addStringList(String key, List<String> def) {
        return this.add(new StringListValue(this, key, def));
    }

    public <T> EnumValue<T> addEnum(String key, NameMap<T> nameMap) {
        return this.add(new EnumValue<>(this, key, nameMap));
    }

    public <T> EnumValue<T> addEnum(String key, NameMap<T> nameMap, T def) {
        return this.add(new EnumValue<>(this, key, nameMap, def));
    }

    public IntArrayValue addIntArray(String key, int[] def) {
        return this.add(new IntArrayValue(this, key, def));
    }
}