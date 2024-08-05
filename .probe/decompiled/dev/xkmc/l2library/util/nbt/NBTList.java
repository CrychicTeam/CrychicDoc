package dev.xkmc.l2library.util.nbt;

import net.minecraft.nbt.ListTag;

public class NBTList<T> {

    private final ListTag tag;

    NBTList(NBTObj parent, String key) {
        boolean old = parent.tag.contains(key);
        this.tag = parent.tag.getList(key, 10);
        if (!old) {
            parent.tag.put(key, this.tag);
        }
    }

    public NBTObj add() {
        NBTObj ans = new NBTObj();
        this.tag.add(ans.tag);
        return ans;
    }

    public NBTObj get(int i) {
        return new NBTObj(this.tag.getCompound(i));
    }

    public int size() {
        return this.tag.size();
    }
}