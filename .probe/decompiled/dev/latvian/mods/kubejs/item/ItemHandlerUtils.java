package dev.latvian.mods.kubejs.item;

import dev.latvian.mods.kubejs.core.InventoryKJS;
import dev.latvian.mods.kubejs.platform.LevelPlatformHelper;
import java.util.Objects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemHandlerUtils {

    public static void giveItemToPlayer(Player player, @NotNull ItemStack stack, int preferredSlot) {
        if (!stack.isEmpty()) {
            InventoryKJS inventory = new PlayerMainInvWrapper(player.getInventory());
            Level level = player.m_9236_();
            ItemStack remainder = stack;
            if (preferredSlot >= 0 && preferredSlot < inventory.kjs$getSlots()) {
                remainder = inventory.kjs$insertItem(preferredSlot, stack, false);
            }
            if (!remainder.isEmpty()) {
                remainder = insertItemStacked(inventory, remainder, false);
            }
            if (remainder.isEmpty() || remainder.getCount() != stack.getCount()) {
                level.playSound(null, player.m_20185_(), player.m_20186_() + 0.5, player.m_20189_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
            if (!remainder.isEmpty() && !level.isClientSide) {
                ItemEntity itemEntity = new ItemEntity(level, player.m_20185_(), player.m_20186_() + 0.5, player.m_20189_(), remainder);
                itemEntity.setPickUpDelay(40);
                itemEntity.m_20256_(itemEntity.m_20184_().multiply(0.0, 1.0, 0.0));
                level.m_7967_(itemEntity);
            }
        }
    }

    @NotNull
    public static ItemStack insertItemStacked(InventoryKJS inventory, @NotNull ItemStack stack, boolean simulate) {
        if (inventory == null || stack.isEmpty()) {
            return stack;
        } else if (!stack.isStackable()) {
            return insertItem(inventory, stack, simulate);
        } else {
            int sizeInventory = inventory.kjs$getSlots();
            for (int i = 0; i < sizeInventory; i++) {
                ItemStack slot = inventory.kjs$getStackInSlot(i);
                if (canItemStacksStackRelaxed(slot, stack)) {
                    stack = inventory.kjs$insertItem(i, stack, simulate);
                    if (stack.isEmpty()) {
                        break;
                    }
                }
            }
            if (!stack.isEmpty()) {
                for (int ix = 0; ix < sizeInventory; ix++) {
                    if (inventory.kjs$getStackInSlot(ix).isEmpty()) {
                        stack = inventory.kjs$insertItem(ix, stack, simulate);
                        if (stack.isEmpty()) {
                            break;
                        }
                    }
                }
            }
            return stack;
        }
    }

    @NotNull
    public static ItemStack insertItem(InventoryKJS dest, @NotNull ItemStack stack, boolean simulate) {
        if (dest != null && !stack.isEmpty()) {
            for (int i = 0; i < dest.kjs$getSlots(); i++) {
                stack = dest.kjs$insertItem(i, stack, simulate);
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
            return stack;
        } else {
            return stack;
        }
    }

    public static boolean canItemStacksStackRelaxed(@NotNull ItemStack a, @NotNull ItemStack b) {
        if (a.isEmpty() || b.isEmpty() || a.getItem() != b.getItem()) {
            return false;
        } else if (!a.isStackable()) {
            return false;
        } else if (a.hasTag() != b.hasTag()) {
            return false;
        } else {
            return a.hasTag() && !Objects.equals(a.getTag(), b.getTag()) ? false : LevelPlatformHelper.get().areCapsCompatible(a, b);
        }
    }

    public static boolean canItemStacksStack(@NotNull ItemStack a, @NotNull ItemStack b) {
        if (a.isEmpty() || !ItemStack.isSameItem(a, b) || a.hasTag() != b.hasTag()) {
            return false;
        } else {
            return a.hasTag() && !Objects.equals(a.getTag(), b.getTag()) ? false : LevelPlatformHelper.get().areCapsCompatible(a, b);
        }
    }
}