package dev.xkmc.l2hostility.content.item.curio.core;

import dev.xkmc.l2complements.content.item.curios.CurioItem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class MultiSlotItem extends CurioItem implements ICurioItem {

    public MultiSlotItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        tooltips = ICurioItem.super.getAttributesTooltip(tooltips, stack);
        List<List<Component>> ans = new ArrayList();
        List<Component> cur = null;
        for (Component e : tooltips) {
            if (cur == null || e.getContents() == ComponentContents.EMPTY) {
                cur = new ArrayList();
                ans.add(cur);
            }
            cur.add(e);
        }
        if (ans.isEmpty()) {
            return tooltips;
        } else if (ans.size() == 1) {
            return (List<Component>) ans.get(0);
        } else {
            tooltips = (List<Component>) ans.get(0);
            for (int i = 0; i < tooltips.size(); i++) {
                if (((Component) tooltips.get(i)).getContents() instanceof TranslatableContents tr && tr.getKey().startsWith("curios.modifiers")) {
                    tooltips.set(i, Component.translatable("curios.modifiers.curio").withStyle(ChatFormatting.GOLD));
                    break;
                }
            }
            return tooltips;
        }
    }
}