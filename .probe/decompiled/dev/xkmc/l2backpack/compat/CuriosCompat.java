package dev.xkmc.l2backpack.compat;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2screentracker.compat.CuriosTrackCompatImpl;
import dev.xkmc.l2screentracker.screen.source.ItemSource;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import dev.xkmc.l2screentracker.screen.source.SimpleSlotData;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class CuriosCompat {

    public static Optional<Pair<ItemStack, PlayerSlot<?>>> getSlot(LivingEntity player, Predicate<ItemStack> pred) {
        return ModList.get().isLoaded("curios") ? getSlotImpl(player, pred) : Optional.empty();
    }

    public static Optional<ItemStack> getRenderingSlot(LivingEntity player, Predicate<ItemStack> pred) {
        return ModList.get().isLoaded("curios") ? getRenderingSlotImpl(player, pred) : Optional.empty();
    }

    private static Optional<Pair<ItemStack, PlayerSlot<?>>> getSlotImpl(LivingEntity player, Predicate<ItemStack> pred) {
        LazyOptional<ICuriosItemHandler> curio = CuriosApi.getCuriosInventory(player);
        if (curio.isPresent() && curio.resolve().isPresent()) {
            IItemHandlerModifiable e = ((ICuriosItemHandler) curio.resolve().get()).getEquippedCurios();
            for (int i = 0; i < e.getSlots(); i++) {
                ItemStack stack = e.getStackInSlot(i);
                if (pred.test(stack)) {
                    return Optional.of(Pair.of(stack, new PlayerSlot((ItemSource) CuriosTrackCompatImpl.get().IS_CURIOS.get(), new SimpleSlotData(i))));
                }
            }
        }
        return Optional.empty();
    }

    private static Optional<ItemStack> getRenderingSlotImpl(LivingEntity player, Predicate<ItemStack> pred) {
        LazyOptional<ICuriosItemHandler> curio = CuriosApi.getCuriosInventory(player);
        if (curio.isPresent() && curio.resolve().isPresent()) {
            Map<String, ICurioStacksHandler> e = ((ICuriosItemHandler) curio.resolve().get()).getCurios();
            for (ICurioStacksHandler ent : e.values()) {
                if (ent.isVisible()) {
                    for (int i = 0; i < ent.getCosmeticStacks().getSlots(); i++) {
                        ItemStack stack = ent.getCosmeticStacks().getStackInSlot(i);
                        if (pred.test(stack)) {
                            return Optional.of(stack);
                        }
                    }
                    for (int ix = 0; ix < ent.getStacks().getSlots(); ix++) {
                        if (ent.getRenders().size() <= ix || ent.getRenders().get(ix)) {
                            ItemStack stack = ent.getStacks().getStackInSlot(ix);
                            if (pred.test(stack)) {
                                return Optional.of(stack);
                            }
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
}