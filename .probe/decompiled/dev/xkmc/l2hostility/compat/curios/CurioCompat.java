package dev.xkmc.l2hostility.compat.curios;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CurioCompat {

    public static boolean hasItemInCurioOrSlot(LivingEntity player, Item item) {
        for (EquipmentSlot e : EquipmentSlot.values()) {
            if (player.getItemBySlot(e).is(item)) {
                return true;
            }
        }
        return hasItemInCurio(player, item);
    }

    public static boolean hasItemInCurio(LivingEntity player, Item item) {
        return ModList.get().isLoaded("curios") ? hasItemImpl(player, item) : false;
    }

    public static List<ItemStack> getItems(LivingEntity player, Predicate<ItemStack> pred) {
        List<ItemStack> ans = new ArrayList();
        for (EquipmentSlot e : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(e);
            if (pred.test(stack)) {
                ans.add(stack);
            }
        }
        if (ModList.get().isLoaded("curios")) {
            getItemImpl(ans, player, pred);
        }
        return ans;
    }

    public static List<EntitySlotAccess> getItemAccess(LivingEntity player) {
        List<EntitySlotAccess> ans = new ArrayList();
        for (EquipmentSlot e : EquipmentSlot.values()) {
            ans.add(new EquipmentSlotAccess(player, e));
        }
        if (ModList.get().isLoaded("curios")) {
            getItemAccessImpl(ans, player);
        }
        return ans;
    }

    @Nullable
    public static EntitySlotAccess decode(String id, LivingEntity le) {
        try {
            String[] strs = id.split("/");
            if (strs[0].equals("equipment")) {
                return new EquipmentSlotAccess(le, EquipmentSlot.byName(strs[1]));
            }
            if (strs[0].equals("curios")) {
                Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(le).resolve();
                if (opt.isEmpty()) {
                    return null;
                }
                Optional<ICurioStacksHandler> handler = ((ICuriosItemHandler) opt.get()).getStacksHandler(strs[1]);
                if (handler.isEmpty()) {
                    return null;
                }
                int index = strs.length == 2 ? 0 : Integer.parseInt(strs[2]);
                return new CurioCompat.CurioSlotAccess(le, ((ICurioStacksHandler) handler.get()).getStacks(), index, strs[1]);
            }
        } catch (Exception var6) {
        }
        return null;
    }

    private static boolean hasItemImpl(LivingEntity player, Item item) {
        LazyOptional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(player);
        if (opt.resolve().isEmpty()) {
            return false;
        } else {
            ItemStack stack = new ItemStack(item);
            for (ICurioStacksHandler e : ((ICuriosItemHandler) opt.resolve().get()).getCurios().values()) {
                if (e.getStacks().getSlots() != 0 && (e.getIdentifier().equals("curio") || stack.is(ItemTags.create(new ResourceLocation("curios", e.getIdentifier()))))) {
                    for (int i = 0; i < e.getStacks().getSlots(); i++) {
                        if (e.getStacks().getStackInSlot(i).is(item)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    private static void getItemImpl(List<ItemStack> list, LivingEntity player, Predicate<ItemStack> pred) {
        LazyOptional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(player);
        if (!opt.resolve().isEmpty()) {
            for (ICurioStacksHandler e : ((ICuriosItemHandler) opt.resolve().get()).getCurios().values()) {
                for (int i = 0; i < e.getStacks().getSlots(); i++) {
                    ItemStack stack = e.getStacks().getStackInSlot(i);
                    if (pred.test(stack)) {
                        list.add(stack);
                    }
                }
            }
        }
    }

    private static void getItemAccessImpl(List<EntitySlotAccess> list, LivingEntity player) {
        LazyOptional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(player);
        if (!opt.resolve().isEmpty()) {
            for (ICurioStacksHandler e : ((ICuriosItemHandler) opt.resolve().get()).getCurios().values()) {
                for (int i = 0; i < e.getStacks().getSlots(); i++) {
                    list.add(new CurioCompat.CurioSlotAccess(player, e.getStacks(), i, e.getIdentifier()));
                }
            }
        }
    }

    public static boolean isSlotAdder(EntitySlotAccess access) {
        if (access instanceof CurioCompat.CurioSlotAccess slot) {
            ItemStack stack = access.get();
            Optional<ICurio> opt = CuriosApi.getCurio(stack).resolve();
            if (opt.isEmpty()) {
                return false;
            } else {
                Multimap<Attribute, AttributeModifier> multimap = CuriosApi.getAttributeModifiers(new SlotContext(slot.id, slot.player, 0, false, true), UUID.randomUUID(), stack);
                for (Attribute e : multimap.keySet()) {
                    if (e instanceof SlotAttribute) {
                        return true;
                    }
                }
                return false;
            }
        } else {
            return false;
        }
    }

    private static record CurioSlotAccess(LivingEntity player, IDynamicStackHandler handler, int slot, String id) implements EntitySlotAccess {

        @Override
        public ItemStack get() {
            return this.handler.getStackInSlot(this.slot);
        }

        @Override
        public void set(ItemStack stack) {
            this.handler.setStackInSlot(this.slot, stack);
        }

        @Override
        public String getID() {
            return "curios/" + this.id + "/" + this.slot;
        }
    }
}