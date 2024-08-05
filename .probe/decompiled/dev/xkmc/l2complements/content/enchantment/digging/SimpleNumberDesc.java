package dev.xkmc.l2complements.content.enchantment.digging;

import dev.xkmc.l2complements.init.data.LangData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public interface SimpleNumberDesc extends BlockBreaker {

    @Override
    default List<Component> descFull(int lv, String key, boolean alt, boolean book) {
        return List.of(Component.translatable(key, this.range(Math.min(this.getMaxLevel(), lv)) + "").withStyle(ChatFormatting.GRAY), LangData.diggerRotate().withStyle(ChatFormatting.DARK_GRAY));
    }

    int range(int var1);
}