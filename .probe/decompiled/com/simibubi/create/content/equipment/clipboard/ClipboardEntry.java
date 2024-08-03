package com.simibubi.create.content.equipment.clipboard;

import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class ClipboardEntry {

    public boolean checked;

    public MutableComponent text;

    public ItemStack icon;

    public ClipboardEntry(boolean checked, MutableComponent text) {
        this.checked = checked;
        this.text = text;
        this.icon = ItemStack.EMPTY;
    }

    public ClipboardEntry displayItem(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public static List<List<ClipboardEntry>> readAll(ItemStack clipboardItem) {
        CompoundTag tag = clipboardItem.getTag();
        return (List<List<ClipboardEntry>>) (tag == null ? new ArrayList() : NBTHelper.readCompoundList(tag.getList("Pages", 10), pageTag -> NBTHelper.readCompoundList(pageTag.getList("Entries", 10), ClipboardEntry::readNBT)));
    }

    public static List<ClipboardEntry> getLastViewedEntries(ItemStack heldItem) {
        List<List<ClipboardEntry>> pages = readAll(heldItem);
        if (pages.isEmpty()) {
            return new ArrayList();
        } else {
            int page = heldItem.getTag() == null ? 0 : Math.min(heldItem.getTag().getInt("PreviouslyOpenedPage"), pages.size() - 1);
            return (List<ClipboardEntry>) pages.get(page);
        }
    }

    public static void saveAll(List<List<ClipboardEntry>> entries, ItemStack clipboardItem) {
        CompoundTag tag = clipboardItem.getOrCreateTag();
        tag.put("Pages", NBTHelper.writeCompoundList(entries, list -> {
            CompoundTag pageTag = new CompoundTag();
            pageTag.put("Entries", NBTHelper.writeCompoundList(list, ClipboardEntry::writeNBT));
            return pageTag;
        }));
    }

    public CompoundTag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("Checked", this.checked);
        nbt.putString("Text", Component.Serializer.toJson(this.text));
        if (this.icon.isEmpty()) {
            return nbt;
        } else {
            nbt.put("Icon", this.icon.serializeNBT());
            return nbt;
        }
    }

    public static ClipboardEntry readNBT(CompoundTag tag) {
        ClipboardEntry clipboardEntry = new ClipboardEntry(tag.getBoolean("Checked"), Component.Serializer.fromJson(tag.getString("Text")));
        if (tag.contains("Icon")) {
            clipboardEntry.displayItem(ItemStack.of(tag.getCompound("Icon")));
        }
        return clipboardEntry;
    }
}