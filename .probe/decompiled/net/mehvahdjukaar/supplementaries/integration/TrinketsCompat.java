package net.mehvahdjukaar.supplementaries.integration;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.List;
import net.mehvahdjukaar.supplementaries.common.block.IKeyLockable;
import net.mehvahdjukaar.supplementaries.common.items.KeyItem;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TrinketsCompat {

    static IKeyLockable.KeyStatus getKey(Player player, String password) {
        TrinketComponent trinket = (TrinketComponent) TrinketsApi.getTrinketComponent(player).orElse(null);
        if (trinket != null) {
            List<Tuple<SlotReference, ItemStack>> found = trinket.getEquipped(i -> i.is(ModTags.KEYS) || i.getItem() instanceof KeyItem);
            if (found.isEmpty()) {
                return IKeyLockable.KeyStatus.NO_KEY;
            } else {
                for (Tuple<SlotReference, ItemStack> slot : found) {
                    ItemStack stack = slot.getB();
                    if (IKeyLockable.getKeyStatus(stack, password).isCorrect()) {
                        return IKeyLockable.KeyStatus.CORRECT_KEY;
                    }
                }
                return IKeyLockable.KeyStatus.INCORRECT_KEY;
            }
        } else {
            return IKeyLockable.KeyStatus.NO_KEY;
        }
    }

    static ItemStack getQuiver(Player player) {
        TrinketComponent trinket = (TrinketComponent) TrinketsApi.getTrinketComponent(player).orElse(null);
        if (trinket != null) {
            List<Tuple<SlotReference, ItemStack>> found = trinket.getEquipped((Item) ModRegistry.QUIVER_ITEM.get());
            if (!found.isEmpty()) {
                return (ItemStack) ((Tuple) found.get(0)).getB();
            }
        }
        return ItemStack.EMPTY;
    }
}