package snownee.kiwi.util;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NBTHelper {

    @Nullable
    private final ItemStack stack;

    @Nullable
    private CompoundTag tag;

    private NBTHelper(@Nullable CompoundTag tag, @Nullable ItemStack stack) {
        this.stack = stack;
        this.tag = tag;
    }

    @Nullable
    public CompoundTag getTag(String key) {
        return this.getTag(key, false);
    }

    public CompoundTag getTag(String key, boolean createIfNull) {
        return this.getTagInternal(key, createIfNull, false);
    }

    private CompoundTag getTagInternal(String key, boolean createIfNull, boolean ignoreLastNode) {
        if (this.tag == null) {
            if (!createIfNull) {
                return null;
            }
            this.tag = new CompoundTag();
            if (this.stack != null) {
                this.stack.setTag(this.tag);
            }
        }
        if (key.isEmpty()) {
            return this.tag;
        } else {
            CompoundTag subTag = this.tag;
            String[] parts = key.split("\\.");
            int length = parts.length;
            if (ignoreLastNode) {
                length--;
            }
            for (int i = 0; i < length; i++) {
                if (!subTag.contains(parts[i], 10)) {
                    if (!createIfNull) {
                        return null;
                    }
                    subTag.put(parts[i], new CompoundTag());
                }
                subTag = (CompoundTag) subTag.get(parts[i]);
            }
            return subTag;
        }
    }

    private CompoundTag getTagInternal(String key) {
        return this.getTagInternal(key, true, true);
    }

    private String getLastNode(String key) {
        int index = key.lastIndexOf(".");
        return index < 0 ? key : key.substring(index + 1);
    }

    public NBTHelper setTag(String key, Tag value) {
        this.getTagInternal(key).put(this.getLastNode(key), value);
        return this;
    }

    public NBTHelper setInt(String key, int value) {
        this.getTagInternal(key).putInt(this.getLastNode(key), value);
        return this;
    }

    public int getInt(String key) {
        return this.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 3)) {
                return subTag.getInt(actualKey);
            }
        }
        return defaultValue;
    }

    public NBTHelper setLong(String key, long value) {
        this.getTagInternal(key).putLong(this.getLastNode(key), value);
        return this;
    }

    public long getLong(String key) {
        return this.getLong(key, 0L);
    }

    public long getLong(String key, long defaultValue) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 4)) {
                return subTag.getLong(actualKey);
            }
        }
        return defaultValue;
    }

    public NBTHelper setShort(String key, short value) {
        this.getTagInternal(key).putShort(this.getLastNode(key), value);
        return this;
    }

    public short getShort(String key) {
        return this.getShort(key, (short) 0);
    }

    public short getShort(String key, short defaultValue) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 2)) {
                return subTag.getShort(actualKey);
            }
        }
        return defaultValue;
    }

    public NBTHelper setDouble(String key, double value) {
        this.getTagInternal(key).putDouble(this.getLastNode(key), value);
        return this;
    }

    public double getDouble(String key) {
        return this.getDouble(key, 0.0);
    }

    public double getDouble(String key, double defaultValue) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 6)) {
                return subTag.getDouble(actualKey);
            }
        }
        return defaultValue;
    }

    public NBTHelper setFloat(String key, float value) {
        this.getTagInternal(key).putFloat(this.getLastNode(key), value);
        return this;
    }

    public float getFloat(String key) {
        return this.getFloat(key, 0.0F);
    }

    public float getFloat(String key, float defaultValue) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 5)) {
                return subTag.getFloat(actualKey);
            }
        }
        return defaultValue;
    }

    public NBTHelper setByte(String key, byte value) {
        this.getTagInternal(key).putFloat(this.getLastNode(key), (float) value);
        return this;
    }

    public byte getByte(String key) {
        return this.getByte(key, (byte) 0);
    }

    public byte getByte(String key, byte defaultValue) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 1)) {
                return subTag.getByte(actualKey);
            }
        }
        return defaultValue;
    }

    public NBTHelper setBoolean(String key, boolean value) {
        this.getTagInternal(key).putBoolean(this.getLastNode(key), value);
        return this;
    }

    public boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 1)) {
                return subTag.getBoolean(actualKey);
            }
        }
        return defaultValue;
    }

    public NBTHelper setPos(String key, BlockPos value) {
        this.getTagInternal(key).put(this.getLastNode(key), NbtUtils.writeBlockPos(value));
        return this;
    }

    @Nullable
    public BlockPos getPos(String key) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 10)) {
                return NbtUtils.readBlockPos(this.getTag(actualKey));
            }
        }
        return null;
    }

    public NBTHelper setBlockState(String key, BlockState value) {
        return this.setTag(key, NbtUtils.writeBlockState(value));
    }

    public BlockState getBlockState(String key) {
        CompoundTag subTag = this.getTagInternal(key, false, false);
        return subTag != null ? NbtUtils.readBlockState(BuiltInRegistries.BLOCK.m_255303_(), subTag) : Blocks.AIR.defaultBlockState();
    }

    public NBTHelper setGameProfile(String key, GameProfile value) {
        NbtUtils.writeGameProfile(this.getTag(key, true), value);
        return this;
    }

    @Nullable
    public GameProfile getGameProfile(String key) {
        CompoundTag subTag = this.getTagInternal(key, false, false);
        return subTag != null ? NbtUtils.readGameProfile(subTag) : null;
    }

    public NBTHelper setString(String key, String value) {
        this.getTagInternal(key).putString(this.getLastNode(key), value);
        return this;
    }

    @Nullable
    public String getString(String key) {
        return this.getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 8)) {
                return subTag.getString(actualKey);
            }
        }
        return defaultValue;
    }

    public NBTHelper setIntArray(String key, int[] value) {
        this.getTagInternal(key).putIntArray(this.getLastNode(key), value);
        return this;
    }

    public int[] getIntArray(String key) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 11)) {
                return subTag.getIntArray(actualKey);
            }
        }
        return new int[0];
    }

    public NBTHelper setByteArray(String key, byte[] value) {
        this.getTagInternal(key).putByteArray(this.getLastNode(key), value);
        return this;
    }

    public byte[] getByteArray(String key) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 7)) {
                return subTag.getByteArray(actualKey);
            }
        }
        return new byte[0];
    }

    public NBTHelper setUUID(String key, UUID value) {
        this.getTagInternal(key).putUUID(this.getLastNode(key), value);
        return this;
    }

    @Nullable
    public UUID getUUID(String key) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (!subTag.contains(actualKey + "Most", 4) || !subTag.contains(actualKey + "Least", 4)) {
                return subTag.getUUID(actualKey);
            }
        }
        return null;
    }

    @Nullable
    public ListTag getTagList(String key, int type) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            if (subTag.contains(actualKey, 9)) {
                return subTag.getList(actualKey, type);
            }
        }
        return null;
    }

    public boolean hasTag(String key, int type) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            if (key.isEmpty()) {
                return true;
            } else {
                String actualKey = this.getLastNode(key);
                return subTag.contains(actualKey, type);
            }
        } else {
            return false;
        }
    }

    public Set<String> keySet(String key) {
        return this.hasTag(key, 10) ? this.getTag(key).getAllKeys() : Collections.EMPTY_SET;
    }

    public NBTHelper remove(String key) {
        CompoundTag subTag = this.getTagInternal(key, false, true);
        if (subTag != null) {
            String actualKey = this.getLastNode(key);
            subTag.remove(actualKey);
        }
        return this;
    }

    @Nullable
    public CompoundTag get() {
        return this.tag;
    }

    public ItemStack getItem() {
        return this.stack == null ? ItemStack.EMPTY : this.stack;
    }

    public static NBTHelper of(ItemStack stack) {
        return new NBTHelper(stack.getTag(), stack);
    }

    public static NBTHelper of(CompoundTag tag) {
        return new NBTHelper(tag, null);
    }

    public static NBTHelper create() {
        return of(new CompoundTag());
    }
}