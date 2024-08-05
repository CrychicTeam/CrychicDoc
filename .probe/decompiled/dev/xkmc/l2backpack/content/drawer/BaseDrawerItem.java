package dev.xkmc.l2backpack.content.drawer;

import dev.xkmc.l2backpack.content.capability.PickupBagItem;
import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.insert.OverlayInsertItem;
import dev.xkmc.l2backpack.init.advancement.BackpackTriggers;
import dev.xkmc.l2backpack.network.DrawerInteractToServer;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public interface BaseDrawerItem extends PickupBagItem, OverlayInsertItem {

    String KEY = "drawerItem";

    String STACKING = "StackingFactor";

    int MAX_FACTOR = 8;

    static boolean canAccept(ItemStack drawer, ItemStack stack) {
        return !stack.hasTag() && !stack.isEmpty() && stack.getItem() == getItem(drawer);
    }

    static Item getItem(ItemStack drawer) {
        return (Item) Optional.ofNullable(drawer.getTag()).map(e -> e.contains("drawerItem") ? e.getString("drawerItem") : null).map(e -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(e))).orElse(Items.AIR);
    }

    static int loadFromInventory(int max, int count, Item item, Player player) {
        int ext = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack inv_stack = player.getInventory().items.get(i);
            if (inv_stack.getItem() == item && !inv_stack.hasTag()) {
                int take = Math.min(max - count, inv_stack.getCount());
                count += take;
                ext += take;
                inv_stack.shrink(take);
                if (count == max) {
                    break;
                }
            }
        }
        return ext;
    }

    static int getStacking(ItemStack drawer) {
        return getStacking(PickupConfig.getConfig(drawer));
    }

    static int getStacking(@Nullable CompoundTag tag) {
        if (tag == null) {
            return getStacking();
        } else {
            int factor = tag.getInt("StackingFactor");
            if (factor < 1) {
                factor = 1;
            }
            return getStacking() * factor;
        }
    }

    static int getStackingFactor(ItemStack drawer) {
        int factor = PickupConfig.getConfig(drawer).getInt("StackingFactor");
        if (factor < 1) {
            factor = 1;
        }
        return factor;
    }

    static ItemStack setStackingFactor(ItemStack drawer, int factor) {
        PickupConfig.getConfig(drawer).putInt("StackingFactor", factor);
        return drawer;
    }

    static int getStacking() {
        return 64;
    }

    void insert(ItemStack var1, ItemStack var2, Player var3);

    default void setItem(ItemStack drawer, Item item, Player player) {
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
        ???;
    }

    @Override
    default ItemStack takeItem(ItemStack drawer, ServerPlayer player) {
        ItemStack stack = this.takeItem(drawer, Integer.MAX_VALUE, player, false);
        if (!stack.isEmpty()) {
            BackpackTriggers.DRAWER.trigger(player, DrawerInteractToServer.Type.TAKE);
        }
        return stack;
    }

    ItemStack takeItem(ItemStack var1, int var2, Player var3, boolean var4);

    boolean canSetNewItem(ItemStack var1);

    @Override
    default boolean clientInsert(ItemStack storage, ItemStack carried, int cid, Slot slot, boolean perform, int button, DrawerInteractToServer.Callback suppress, int limit) {
        if (carried.isEmpty()) {
            return false;
        } else if (carried.hasTag()) {
            return true;
        } else if (this.canSetNewItem(storage)) {
            if (perform) {
                this.sendInsertPacket(cid, carried, slot, suppress, limit);
            }
            return true;
        } else if (canAccept(storage, carried)) {
            if (perform) {
                this.sendInsertPacket(cid, carried, slot, suppress, limit);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    default boolean mayClientTake() {
        return true;
    }

    @Override
    default void attemptInsert(ItemStack storage, ItemStack carried, ServerPlayer player) {
        if (!carried.isEmpty() && !carried.hasTag()) {
            if (this.canSetNewItem(storage)) {
                this.setItem(storage, carried.getItem(), player);
            }
            if (canAccept(storage, carried)) {
                this.insert(storage, carried, player);
                BackpackTriggers.DRAWER.trigger(player, DrawerInteractToServer.Type.INSERT);
            }
        }
    }

    ResourceLocation backgroundLoc();

    static {
        ???;
    }
}