package io.redspace.ironsspellbooks.datafix.fixers;

import io.redspace.ironsspellbooks.datafix.DataFixerElement;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fml.ModList;

public class FixApoth extends DataFixerElement {

    private final String key = "affix_data";

    @Override
    public List<String> preScanValuesToMatch() {
        return List.of("affix_data");
    }

    @Override
    public boolean runFixer(CompoundTag tag) {
        if (!ModList.get().isLoaded("apotheosis")) {
            return false;
        } else {
            if (tag != null && tag.get("affix_data") instanceof CompoundTag affixTag) {
                Tag var4 = affixTag.get("gems");
                if (var4 instanceof ListTag) {
                    for (Tag gem : (ListTag) var4) {
                        CompoundTag itemTag = ((CompoundTag) gem).getCompound("tag");
                        if (itemTag.getString("gem").equals("irons_spellbooks:poison")) {
                            itemTag.putString("gem", "irons_spellbooks:nature");
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}