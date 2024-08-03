package io.github.lightman314.lightmanscurrency.api.network;

import com.google.common.collect.ImmutableMap;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class LazyPacketData {

    public static final byte TYPE_NULL = 0;

    public static final byte TYPE_BOOLEAN = 1;

    public static final byte TYPE_INT = 2;

    public static final byte TYPE_LONG = 3;

    public static final byte TYPE_FLOAT = 4;

    public static final byte TYPE_DOUBLE = 5;

    public static final byte TYPE_STRING = 6;

    public static final byte TYPE_TEXT = 64;

    public static final byte TYPE_NBT = 65;

    private final ImmutableMap<String, LazyPacketData.Data> dataMap;

    private LazyPacketData(Map<String, LazyPacketData.Data> data) {
        this.dataMap = ImmutableMap.copyOf(data);
    }

    @Nonnull
    private LazyPacketData.Data getData(String key) {
        return (LazyPacketData.Data) this.dataMap.getOrDefault(key, LazyPacketData.Data.NULL);
    }

    public boolean contains(String key) {
        return this.dataMap.containsKey(key);
    }

    public boolean contains(String key, byte type) {
        return this.contains(key) && this.getData(key).type == type;
    }

    public boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        LazyPacketData.Data d = this.getData(key);
        return d.type == 1 ? (Boolean) d.value : defaultValue;
    }

    public int getInt(String key) {
        return this.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        LazyPacketData.Data d = this.getData(key);
        return d.type == 2 ? (Integer) d.value : defaultValue;
    }

    public long getLong(String key) {
        return this.getLong(key, 0L);
    }

    public long getLong(String key, long defaultValue) {
        LazyPacketData.Data d = this.getData(key);
        return d.type == 3 ? (Long) d.value : defaultValue;
    }

    public float getFloat(String key) {
        return this.getFloat(key, 0.0F);
    }

    public float getFloat(String key, float defaultValue) {
        LazyPacketData.Data d = this.getData(key);
        return d.type == 4 ? (Float) d.value : defaultValue;
    }

    public double getDouble(String key) {
        return this.getDouble(key, 0.0);
    }

    public double getDouble(String key, double defaultValue) {
        LazyPacketData.Data d = this.getData(key);
        return d.type == 5 ? (Double) d.value : defaultValue;
    }

    public String getString(String key) {
        return this.getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        LazyPacketData.Data d = this.getData(key);
        return d.type == 6 ? (String) d.value : defaultValue;
    }

    public ResourceLocation getResourceLocation(String key) {
        return this.getResourceLocation(key, null);
    }

    public ResourceLocation getResourceLocation(String key, ResourceLocation defaultValue) {
        LazyPacketData.Data d = this.getData(key);
        return d.type == 6 ? new ResourceLocation((String) d.value) : defaultValue;
    }

    public Component getText(String key) {
        return this.getText(key, EasyText.empty());
    }

    public Component getText(String key, Component defaultValue) {
        LazyPacketData.Data d = this.getData(key);
        return d.type == 64 ? (Component) d.value : defaultValue;
    }

    public CompoundTag getNBT(String key) {
        return this.getNBT(key, new CompoundTag());
    }

    public CompoundTag getNBT(String key, CompoundTag defaultValue) {
        LazyPacketData.Data d = this.getData(key);
        return d.type == 65 ? (CompoundTag) d.value : defaultValue;
    }

    public MoneyValue getMoneyValue(String key) {
        return this.getMoneyValue(key, MoneyValue.empty());
    }

    public MoneyValue getMoneyValue(String key, MoneyValue defaultValue) {
        LazyPacketData.Data d = this.getData(key);
        return d.type == 65 ? MoneyValue.load((CompoundTag) d.value) : defaultValue;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.dataMap.entrySet().size());
        this.dataMap.forEach((key, data) -> {
            buffer.writeUtf(key, 32);
            buffer.writeByte(data.type);
            data.encode(buffer);
        });
    }

    public static LazyPacketData decode(FriendlyByteBuf buffer) {
        int count = buffer.readInt();
        HashMap<String, LazyPacketData.Data> dataMap = new HashMap();
        for (int i = 0; i < count; i++) {
            String key = buffer.readUtf(32);
            LazyPacketData.Data data = LazyPacketData.Data.decode(buffer);
            dataMap.put(key, data);
        }
        return new LazyPacketData(dataMap);
    }

    public LazyPacketData.Builder copyToBuilder() {
        LazyPacketData.Builder b = new LazyPacketData.Builder();
        this.dataMap.forEach(b::addData);
        return b;
    }

    public static LazyPacketData.Builder builder() {
        return new LazyPacketData.Builder();
    }

    public static LazyPacketData.Builder simpleFlag(String key) {
        return simpleBoolean(key, true);
    }

    public static LazyPacketData.Builder simpleBoolean(String key, boolean value) {
        return builder().setBoolean(key, value);
    }

    public static LazyPacketData.Builder simpleInt(String key, int value) {
        return builder().setInt(key, value);
    }

    public static LazyPacketData.Builder simpleLong(String key, long value) {
        return builder().setLong(key, value);
    }

    public static LazyPacketData.Builder simpleFloat(String key, float value) {
        return builder().setFloat(key, value);
    }

    public static LazyPacketData.Builder simpleDouble(String key, double value) {
        return builder().setDouble(key, value);
    }

    public static LazyPacketData.Builder simpleString(String key, String value) {
        return builder().setString(key, value);
    }

    public static LazyPacketData.Builder simpleText(String key, Component value) {
        return builder().setText(key, value);
    }

    public static LazyPacketData.Builder simpleTag(String key, CompoundTag value) {
        return builder().setCompound(key, value);
    }

    public static LazyPacketData.Builder simpleMoneyValue(String key, MoneyValue value) {
        return builder().setMoneyValue(key, value);
    }

    public static final class Builder {

        Map<String, LazyPacketData.Data> data = new HashMap();

        private Builder() {
        }

        private void addData(@Nonnull String key, @Nonnull LazyPacketData.Data data) {
            this.data.put(key, data);
        }

        public LazyPacketData.Builder setBoolean(@Nonnull String key, boolean value) {
            this.data.put(key, LazyPacketData.Data.ofBoolean(value));
            return this;
        }

        public LazyPacketData.Builder setInt(@Nonnull String key, int value) {
            this.data.put(key, LazyPacketData.Data.ofInt(value));
            return this;
        }

        public LazyPacketData.Builder setLong(@Nonnull String key, long value) {
            this.data.put(key, LazyPacketData.Data.ofLong(value));
            return this;
        }

        public LazyPacketData.Builder setFloat(@Nonnull String key, float value) {
            this.data.put(key, LazyPacketData.Data.ofFloat(value));
            return this;
        }

        public LazyPacketData.Builder setDouble(@Nonnull String key, double value) {
            this.data.put(key, LazyPacketData.Data.ofDouble(value));
            return this;
        }

        public LazyPacketData.Builder setString(@Nonnull String key, String value) {
            this.data.put(key, LazyPacketData.Data.ofString(value));
            return this;
        }

        public LazyPacketData.Builder setResourceLocation(String key, ResourceLocation value) {
            this.data.put(key, LazyPacketData.Data.ofString(value.toString()));
            return this;
        }

        public LazyPacketData.Builder setText(String key, Component value) {
            this.data.put(key, LazyPacketData.Data.ofText(value));
            return this;
        }

        public LazyPacketData.Builder setCompound(String key, CompoundTag value) {
            this.data.put(key, LazyPacketData.Data.ofNBT(value));
            return this;
        }

        public LazyPacketData.Builder setMoneyValue(String key, MoneyValue value) {
            this.data.put(key, LazyPacketData.Data.ofMoneyValue(value));
            return this;
        }

        public LazyPacketData build() {
            return new LazyPacketData(this.data);
        }
    }

    private static record Data(byte type, Object value) {

        static final LazyPacketData.Data NULL = new LazyPacketData.Data((byte) 0, null);

        static LazyPacketData.Data ofNull() {
            return NULL;
        }

        static LazyPacketData.Data ofBoolean(boolean value) {
            return new LazyPacketData.Data((byte) 1, value);
        }

        static LazyPacketData.Data ofInt(int value) {
            return new LazyPacketData.Data((byte) 2, value);
        }

        static LazyPacketData.Data ofLong(long value) {
            return new LazyPacketData.Data((byte) 3, value);
        }

        static LazyPacketData.Data ofFloat(float value) {
            return new LazyPacketData.Data((byte) 4, value);
        }

        static LazyPacketData.Data ofDouble(double value) {
            return new LazyPacketData.Data((byte) 5, value);
        }

        static LazyPacketData.Data ofString(@Nullable String value) {
            return value == null ? NULL : new LazyPacketData.Data((byte) 6, value);
        }

        static LazyPacketData.Data ofResourceLocation(@Nullable ResourceLocation value) {
            return value == null ? NULL : new LazyPacketData.Data((byte) 6, value.toString());
        }

        static LazyPacketData.Data ofText(@Nullable Component value) {
            return value == null ? NULL : new LazyPacketData.Data((byte) 64, value);
        }

        static LazyPacketData.Data ofNBT(@Nullable CompoundTag value) {
            return value == null ? NULL : new LazyPacketData.Data((byte) 65, value);
        }

        static LazyPacketData.Data ofMoneyValue(@Nullable MoneyValue value) {
            if (value == null) {
                return NULL;
            } else {
                CompoundTag tag = value.save();
                LightmansCurrency.LogDebug("Saving Money Value to tag!\n" + tag.m_7916_());
                return ofNBT(tag);
            }
        }

        void encode(FriendlyByteBuf buffer) {
            if (this.type == 1) {
                buffer.writeBoolean((Boolean) this.value);
            }
            if (this.type == 2) {
                buffer.writeInt((Integer) this.value);
            }
            if (this.type == 3) {
                buffer.writeLong((Long) this.value);
            }
            if (this.type == 4) {
                buffer.writeFloat((Float) this.value);
            }
            if (this.type == 5) {
                buffer.writeDouble((Double) this.value);
            }
            if (this.type == 6) {
                int length = ((String) this.value).length();
                buffer.writeInt(length);
                buffer.writeUtf((String) this.value, length);
            }
            if (this.type == 64) {
                buffer.writeUtf(Component.Serializer.toJson((Component) this.value));
            }
            if (this.type == 65) {
                buffer.writeNbt((CompoundTag) this.value);
            }
        }

        static LazyPacketData.Data decode(FriendlyByteBuf buffer) {
            byte type = buffer.readByte();
            if (type == 0) {
                return ofNull();
            } else if (type == 1) {
                return ofBoolean(buffer.readBoolean());
            } else if (type == 2) {
                return ofInt(buffer.readInt());
            } else if (type == 3) {
                return ofLong(buffer.readLong());
            } else if (type == 4) {
                return ofFloat(buffer.readFloat());
            } else if (type == 5) {
                return ofDouble(buffer.readDouble());
            } else if (type == 6) {
                int length = buffer.readInt();
                return ofString(buffer.readUtf(length));
            } else if (type == 64) {
                return ofText(Component.Serializer.fromJson(buffer.readUtf()));
            } else if (type == 65) {
                return ofNBT(buffer.readAnySizeNbt());
            } else {
                throw new RuntimeException("Could not decode entry of type " + type + "as it is not a valid data entry type!");
            }
        }
    }
}