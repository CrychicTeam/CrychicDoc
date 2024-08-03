package dev.xkmc.l2library.util.nbt;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class NBTObj {

    public static final String BASE = "_base";

    public static final int TYPE_STRING = 8;

    public final CompoundTag tag;

    public NBTObj() {
        this.tag = new CompoundTag();
    }

    public NBTObj(ItemStack is, String key) {
        this.tag = is.getOrCreateTagElement(key);
    }

    public NBTObj(CompoundTag data) {
        this.tag = data;
    }

    private NBTObj(NBTObj parent, String key) {
        boolean old = parent.tag.contains(key);
        this.tag = parent.tag.getCompound(key);
        if (!old) {
            parent.tag.put(key, this.tag);
        }
    }

    public void fromBlockPos(BlockPos pos) {
        this.tag.putInt("x", pos.m_123341_());
        this.tag.putInt("y", pos.m_123342_());
        this.tag.putInt("z", pos.m_123343_());
    }

    public <T> NBTList<T> getList(String key) {
        return new NBTList<>(this, key);
    }

    public ResourceLocation getRL(String key) {
        return new ResourceLocation(this.tag.getString(key));
    }

    public NBTObj getSub(String key) {
        return new NBTObj(this, key);
    }

    public BlockPos toBlockPos() {
        int x = this.tag.getInt("x");
        int y = this.tag.getInt("y");
        int z = this.tag.getInt("z");
        return new BlockPos(x, y, z);
    }
}