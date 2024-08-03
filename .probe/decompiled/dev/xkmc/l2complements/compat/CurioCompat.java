package dev.xkmc.l2complements.compat;

import dev.xkmc.l2complements.content.item.curios.EffectValidItem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CurioCompat {

    public static List<ItemStack> getAllSlots(LivingEntity le) {
        List<ItemStack> list = new ArrayList();
        for (EquipmentSlot e : EquipmentSlot.values()) {
            list.add(le.getItemBySlot(e));
        }
        if (ModList.get().isLoaded("curios")) {
            fillSlots(le, list);
        }
        return list;
    }

    private static void fillSlots(LivingEntity le, List<ItemStack> list) {
        LazyOptional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(le);
        if (opt.resolve().isPresent()) {
            ICuriosItemHandler curio = (ICuriosItemHandler) opt.resolve().get();
            for (ICurioStacksHandler handler : curio.getCurios().values()) {
                IDynamicStackHandler stacks = handler.getStacks();
                int n = stacks.getSlots();
                for (int i = 0; i < n; i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        list.add(stack);
                    }
                }
            }
        }
    }

    public static boolean testEffect(MobEffectInstance ins, LivingEntity entity) {
        return ModList.get().isLoaded("curios") ? testEffectImpl(ins, entity) : false;
    }

    private static boolean testEffectImpl(MobEffectInstance ins, LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity).resolve().flatMap(cap -> cap.findFirstCurio(e -> {
            if (e.getItem() instanceof EffectValidItem item && item.isEffectValid(ins, e, entity)) {
                return true;
            }
            return false;
        })).isPresent();
    }
}