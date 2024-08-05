package dev.xkmc.modulargolems.content.item.golem;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class NBTAnalytic {

    public static void analyze(ItemStack stack, List<Component> list) {
        CompoundTag root = stack.getTag();
        if (root != null) {
            list.add(log(root, "NBT"));
            if (root.contains("golem_entity")) {
                CompoundTag entity = root.getCompound("golem_entity");
                list.add(log(entity, "entity NBT"));
                list.add(log(entity, "equipment", "ArmorItems", "HandItems"));
                list.add(log(entity, "golem data", "auto-serial"));
                list.add(log(entity, "Capability", "ForgeCaps"));
                list.add(log(entity, "Attribute", "Attributes"));
            }
        }
    }

    private static Component log(CompoundTag root, String name, String... key) {
        int ans = 0;
        if (key.length == 0) {
            ans = root.sizeInBytes();
        } else {
            for (String str : key) {
                Tag tag = root.get(str);
                if (tag != null) {
                    ans += tag.sizeInBytes();
                }
            }
        }
        Component size = ans >= 1024 ? Component.literal((ans >> 10) + "").withStyle(ChatFormatting.AQUA).append(" kiB") : Component.literal(ans + "").withStyle(ChatFormatting.AQUA).append(" bytes");
        return Component.literal(name + " size: ").append(size).withStyle(ChatFormatting.GRAY);
    }
}