package dev.xkmc.modulargolems.content.item.wand;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2itemselector.select.item.CustomDisplaySelectItem;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BaseWandItem extends Item implements CustomDisplaySelectItem {

    @Nullable
    private final ItemEntry<? extends BaseWandItem> base;

    @Nullable
    private final MGLangData right;

    @Nullable
    private final MGLangData shift;

    public BaseWandItem(Item.Properties properties, @Nullable MGLangData right, @Nullable MGLangData shift, @Nullable ItemEntry<? extends BaseWandItem> base) {
        super(properties);
        this.base = base;
        this.right = right;
        this.shift = shift;
    }

    public ItemStack getDisplay(ResourceLocation id, ItemStack stack) {
        return this.base == null ? stack : this.base.asStack();
    }

    @Override
    public final void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        if (this.base != null) {
            list.add(MGLangData.WAND_MODE.get(this.base.asStack().getHoverName()));
            list.add(MGLangData.WAND_SWITCH.get());
        }
        if (this.right != null) {
            list.add(MGLangData.WAND_RIGHT.get());
            list.add(this.right.get());
        }
        if (this.shift != null) {
            list.add(MGLangData.WAND_SHIFT.get());
            list.add(this.shift.get());
        }
    }
}