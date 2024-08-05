package io.redspace.ironsspellbooks.datafix.fixers;

import io.redspace.ironsspellbooks.datafix.DataFixerElement;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fml.ModList;

public class FixTetra extends DataFixerElement {

    private final String key = "sword/socket_material";

    @Override
    public List<String> preScanValuesToMatch() {
        return List.of("sword/socket_material");
    }

    @Override
    public boolean runFixer(CompoundTag tag) {
        if (!ModList.get().isLoaded("tetra")) {
            return false;
        } else {
            if (tag != null && tag.contains("sword/socket_material")) {
                Tag socketTag = tag.get("sword/socket_material");
                String poison = "sword_socket/irons_spellbooks_poison_rune_socket";
                String nature = "sword_socket/irons_spellbooks_nature_rune_socket";
                if (socketTag instanceof StringTag entry && entry.getAsString().equals(poison)) {
                    tag.remove("sword/socket_material");
                    tag.putString("sword/socket_material", nature);
                    return true;
                }
            }
            return false;
        }
    }
}