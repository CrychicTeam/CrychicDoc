package dev.xkmc.l2weaponry.compat;

import com.doo.xenchantment.enchantment.IncDamage;
import dev.xkmc.l2weaponry.content.item.base.WeaponItem;
import java.util.function.Function;

public class XEnchCompat {

    public static void onInit() {
        IncDamage.DAMAGE_GETTER.add((Function) stack -> stack.getItem() instanceof WeaponItem weapon ? weapon.attackDamage : null);
    }
}