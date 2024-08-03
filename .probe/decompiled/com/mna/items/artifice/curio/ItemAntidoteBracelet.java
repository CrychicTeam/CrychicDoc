package com.mna.items.artifice.curio;

import com.mna.api.items.ChargeableItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;

public class ItemAntidoteBracelet extends ChargeableItem implements IPreEnchantedItem<ItemAntidoteBracelet> {

    public ItemAntidoteBracelet() {
        super(new Item.Properties().setNoRepair().rarity(Rarity.UNCOMMON), 60.0F);
    }

    @Override
    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        if (player.m_21124_(MobEffects.POISON) != null) {
            CuriosApi.getCuriosHelper().findFirstCurio(player, this).ifPresent(t -> {
                player.m_6234_(MobEffects.POISON);
                t.stack().hurtAndBreak(1, player, damager -> CuriosApi.getCuriosHelper().onBrokenCurio(t.slotContext()));
            });
            return true;
        } else {
            return false;
        }
    }
}