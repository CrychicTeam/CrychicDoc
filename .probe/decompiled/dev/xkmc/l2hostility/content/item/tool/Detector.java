package dev.xkmc.l2hostility.content.item.tool;

import dev.xkmc.l2hostility.init.data.LangData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Detector extends Item {

    public Detector(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.ITEM_DETECTOR.get().withStyle(ChatFormatting.GRAY));
        list.add(LangData.sectionRender().withStyle(ChatFormatting.DARK_GREEN));
    }
}