package dev.xkmc.l2complements.content.item.equipments;

import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SculkiumTool extends ExtraToolConfig {

    public static float cachedHardness;

    public float getDestroySpeed(ItemStack stack, BlockState state, float old) {
        return cachedHardness > 1.0F ? old * cachedHardness : old;
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        list.add(LangData.IDS.SCULKIUM_TOOL.get().withStyle(ChatFormatting.GRAY));
    }
}