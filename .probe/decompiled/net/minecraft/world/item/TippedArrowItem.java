package net.minecraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

public class TippedArrowItem extends ArrowItem {

    public TippedArrowItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.m_7968_(), Potions.POISON);
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        PotionUtils.addPotionTooltip(itemStack0, listComponent2, 0.125F);
    }

    @Override
    public String getDescriptionId(ItemStack itemStack0) {
        return PotionUtils.getPotion(itemStack0).getName(this.m_5524_() + ".effect.");
    }
}