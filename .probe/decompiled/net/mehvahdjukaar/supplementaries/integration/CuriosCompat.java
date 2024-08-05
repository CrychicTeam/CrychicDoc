package net.mehvahdjukaar.supplementaries.integration;

import java.util.List;
import net.mehvahdjukaar.supplementaries.common.block.IKeyLockable;
import net.mehvahdjukaar.supplementaries.common.items.KeyItem;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class CuriosCompat {

    static IKeyLockable.KeyStatus getKey(Player player, String password) {
        List<SlotResult> found = CuriosApi.getCuriosHelper().findCurios(player, i -> i.is(ModTags.KEYS) || i.getItem() instanceof KeyItem);
        if (found.isEmpty()) {
            return IKeyLockable.KeyStatus.NO_KEY;
        } else {
            for (SlotResult slot : found) {
                ItemStack stack = slot.stack();
                if (IKeyLockable.getKeyStatus(stack, password).isCorrect()) {
                    return IKeyLockable.KeyStatus.CORRECT_KEY;
                }
            }
            return IKeyLockable.KeyStatus.INCORRECT_KEY;
        }
    }

    static ItemStack getQuiver(Player player) {
        List<SlotResult> found = CuriosApi.getCuriosHelper().findCurios(player, i -> i.is((Item) ModRegistry.QUIVER_ITEM.get()));
        return !found.isEmpty() ? ((SlotResult) found.get(0)).stack() : ItemStack.EMPTY;
    }
}