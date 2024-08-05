package com.mna.tools;

import com.mna.items.ItemInit;
import java.util.Map;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.apache.commons.lang3.mutable.MutableInt;
import top.theillusivec4.curios.api.CuriosApi;

public class EnchantmentUtils {

    public static boolean getSilkTouch(Player player) {
        return ItemInit.SILK_TOUCH_RING.get().isEquippedAndHasMana(player, 1.0F, false);
    }

    public static int getFortuneLevel(Player player) {
        MutableInt fortune_level = new MutableInt(0);
        Item fortuneRing = null;
        if (ItemInit.FORTUNE_RING_GREATER.get().isEquippedAndHasMana(player, 1.0F, false)) {
            fortune_level.setValue(3);
            fortuneRing = ItemInit.FORTUNE_RING_GREATER.get();
        } else if (ItemInit.FORTUNE_RING.get().isEquippedAndHasMana(player, 1.0F, false)) {
            fortune_level.setValue(2);
        } else if (ItemInit.FORTUNE_RING_MINOR.get().isEquippedAndHasMana(player, 1.0F, false)) {
            fortune_level.setValue(1);
        }
        if (fortuneRing != null) {
            CuriosApi.getCuriosHelper().findFirstCurio(player, fortuneRing).ifPresent(t -> {
                Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(t.stack());
                fortune_level.add((Number) enchants.getOrDefault(Enchantments.BLOCK_FORTUNE, 0));
            });
        }
        return fortune_level.intValue();
    }
}