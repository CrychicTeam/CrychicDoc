package dev.xkmc.modulargolems.compat.materials.common;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class AddSlotModifier extends GolemModifier {

    public AddSlotModifier() {
        super(StatFilterType.MASS, 5);
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", v).withStyle(ChatFormatting.GREEN));
    }

    @Override
    public int addSlot(List<UpgradeItem> list, int lv) {
        return lv;
    }
}