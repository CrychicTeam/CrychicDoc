package team.lodestar.lodestone.helpers;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CurioHelper {

    public static Optional<SlotResult> getEquippedCurio(LivingEntity entity, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, predicate);
    }

    public static Optional<SlotResult> getEquippedCurio(LivingEntity entity, Item curio) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, curio);
    }

    public static boolean hasCurioEquipped(LivingEntity entity, Item curio) {
        return getEquippedCurio(entity, curio).isPresent();
    }

    public static ArrayList<ItemStack> getEquippedCurios(LivingEntity entity) {
        Optional<IItemHandlerModifiable> optional = CuriosApi.getCuriosHelper().getEquippedCurios(entity).resolve();
        ArrayList<ItemStack> stacks = new ArrayList();
        if (optional.isPresent()) {
            IItemHandlerModifiable handler = (IItemHandlerModifiable) optional.get();
            for (int i = 0; i < handler.getSlots(); i++) {
                stacks.add(handler.getStackInSlot(i));
            }
        }
        return stacks;
    }

    public static ArrayList<ItemStack> getEquippedCurios(LivingEntity entity, Predicate<ItemStack> predicate) {
        Optional<IItemHandlerModifiable> optional = CuriosApi.getCuriosHelper().getEquippedCurios(entity).resolve();
        ArrayList<ItemStack> stacks = new ArrayList();
        if (optional.isPresent()) {
            IItemHandlerModifiable handler = (IItemHandlerModifiable) optional.get();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (predicate.test(stack)) {
                    stacks.add(stack);
                }
            }
        }
        return stacks;
    }

    public static Optional<ImmutableTriple<String, Integer, ItemStack>> findCosmeticCurio(Predicate<ItemStack> filter, LivingEntity livingEntity) {
        ImmutableTriple<String, Integer, ItemStack> result = (ImmutableTriple<String, Integer, ItemStack>) CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity).map(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            for (String id : curios.keySet()) {
                ICurioStacksHandler stacksHandler = (ICurioStacksHandler) curios.get(id);
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                IDynamicStackHandler cosmeticStackHelper = stacksHandler.getCosmeticStacks();
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    if (!stack.isEmpty() && filter.test(stack)) {
                        return new ImmutableTriple(id, i, stack);
                    }
                }
                for (int ix = 0; ix < cosmeticStackHelper.getSlots(); ix++) {
                    ItemStack stack = cosmeticStackHelper.getStackInSlot(ix);
                    if (!stack.isEmpty() && filter.test(stack)) {
                        return new ImmutableTriple(id, ix, stack);
                    }
                }
            }
            return new ImmutableTriple("", 0, ItemStack.EMPTY);
        }).orElse(new ImmutableTriple("", 0, ItemStack.EMPTY));
        return ((String) result.getLeft()).isEmpty() ? Optional.empty() : Optional.of(result);
    }
}