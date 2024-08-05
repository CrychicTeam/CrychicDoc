package noppes.npcs.api.wrapper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.INbt;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.util.NBTJsonUtil;

public class NBTWrapper implements INbt {

    private CompoundTag compound;

    public NBTWrapper(CompoundTag compound) {
        this.compound = compound;
    }

    @Override
    public void remove(String key) {
        this.compound.remove(key);
    }

    @Override
    public boolean has(String key) {
        return this.compound.contains(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return this.compound.getBoolean(key);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        this.compound.putBoolean(key, value);
    }

    @Override
    public short getShort(String key) {
        return this.compound.getShort(key);
    }

    @Override
    public void setShort(String key, short value) {
        this.compound.putShort(key, value);
    }

    @Override
    public int getInteger(String key) {
        return this.compound.getInt(key);
    }

    @Override
    public void setInteger(String key, int value) {
        this.compound.putInt(key, value);
    }

    @Override
    public byte getByte(String key) {
        return this.compound.getByte(key);
    }

    @Override
    public void setByte(String key, byte value) {
        this.compound.putByte(key, value);
    }

    @Override
    public long getLong(String key) {
        return this.compound.getLong(key);
    }

    @Override
    public void setLong(String key, long value) {
        this.compound.putLong(key, value);
    }

    @Override
    public double getDouble(String key) {
        return this.compound.getDouble(key);
    }

    @Override
    public void setDouble(String key, double value) {
        this.compound.putDouble(key, value);
    }

    @Override
    public float getFloat(String key) {
        return this.compound.getFloat(key);
    }

    @Override
    public void setFloat(String key, float value) {
        this.compound.putFloat(key, value);
    }

    @Override
    public String getString(String key) {
        return this.compound.getString(key);
    }

    @Override
    public void putString(String key, String value) {
        this.compound.putString(key, value);
    }

    @Override
    public byte[] getByteArray(String key) {
        return this.compound.getByteArray(key);
    }

    @Override
    public void setByteArray(String key, byte[] value) {
        this.compound.putByteArray(key, value);
    }

    @Override
    public int[] getIntegerArray(String key) {
        return this.compound.getIntArray(key);
    }

    @Override
    public void setIntegerArray(String key, int[] value) {
        this.compound.putIntArray(key, value);
    }

    @Override
    public Object[] getList(String key, int type) {
        ListTag list = this.compound.getList(key, type);
        Object[] nbts = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if (list.getElementType() == 10) {
                nbts[i] = NpcAPI.Instance().getINbt(list.getCompound(i));
            } else if (list.getElementType() == 8) {
                nbts[i] = list.getString(i);
            } else if (list.getElementType() == 6) {
                nbts[i] = list.getDouble(i);
            } else if (list.getElementType() == 5) {
                nbts[i] = list.getFloat(i);
            } else if (list.getElementType() == 3) {
                nbts[i] = list.getInt(i);
            } else if (list.getElementType() == 11) {
                nbts[i] = list.getIntArray(i);
            }
        }
        return nbts;
    }

    @Override
    public int getListType(String key) {
        Tag b = this.compound.get(key);
        if (b == null) {
            return 0;
        } else if (b.getId() != 9) {
            throw new CustomNPCsException("NBT tag " + key + " isn't a list");
        } else {
            return ((ListTag) b).getElementType();
        }
    }

    @Override
    public void setList(String key, Object[] value) {
        ListTag list = new ListTag();
        for (Object nbt : value) {
            if (nbt instanceof INbt) {
                list.add(((INbt) nbt).getMCNBT());
            } else if (nbt instanceof String) {
                list.add(StringTag.valueOf((String) nbt));
            } else if (nbt instanceof Double) {
                list.add(DoubleTag.valueOf((Double) nbt));
            } else if (nbt instanceof Float) {
                list.add(FloatTag.valueOf((Float) nbt));
            } else if (nbt instanceof Integer) {
                list.add(IntTag.valueOf((Integer) nbt));
            } else if (nbt instanceof int[]) {
                list.add(new IntArrayTag((int[]) nbt));
            }
        }
        this.compound.put(key, list);
    }

    @Override
    public INbt getCompound(String key) {
        return NpcAPI.Instance().getINbt(this.compound.getCompound(key));
    }

    @Override
    public void setCompound(String key, INbt value) {
        if (value == null) {
            throw new CustomNPCsException("Value cant be null");
        } else {
            this.compound.put(key, value.getMCNBT());
        }
    }

    @Override
    public String[] getKeys() {
        return (String[]) this.compound.getAllKeys().toArray(new String[this.compound.getAllKeys().size()]);
    }

    @Override
    public int getType(String key) {
        return this.compound.get(key).getId();
    }

    @Override
    public CompoundTag getMCNBT() {
        return this.compound;
    }

    @Override
    public String toJsonString() {
        return NBTJsonUtil.Convert(this.compound);
    }

    @Override
    public boolean isEqual(INbt nbt) {
        return nbt == null ? false : this.compound.equals(nbt.getMCNBT());
    }

    @Override
    public void clear() {
        for (String name : this.compound.getAllKeys()) {
            this.compound.remove(name);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.compound.isEmpty();
    }

    @Override
    public void merge(INbt nbt) {
        this.compound.merge(nbt.getMCNBT());
    }

    @Override
    public void mcSetTag(String key, Tag base) {
        this.compound.put(key, base);
    }

    @Override
    public Tag mcGetTag(String key) {
        return this.compound.get(key);
    }
}