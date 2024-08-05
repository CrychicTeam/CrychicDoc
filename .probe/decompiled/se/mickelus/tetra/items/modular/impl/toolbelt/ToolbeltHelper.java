package se.mickelus.tetra.items.modular.impl.toolbelt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;
import se.mickelus.tetra.compat.curios.CuriosCompat;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.PotionsInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuickslotInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuiverInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.StorageInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.ToolbeltInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.ToolbeltSlotType;
import se.mickelus.tetra.properties.IToolProvider;
import top.theillusivec4.curios.api.CuriosApi;

@ParametersAreNonnullByDefault
public class ToolbeltHelper {

    public static void equipItemFromToolbelt(Player player, ToolbeltSlotType slotType, int index, InteractionHand hand) {
        ToolbeltInventory inventory = null;
        ItemStack toolbeltStack = findToolbelt(player);
        if (toolbeltStack.getItem() instanceof ModularToolbeltItem) {
            switch(slotType) {
                case quickslot:
                    inventory = new QuickslotInventory(toolbeltStack);
                    break;
                case potion:
                    inventory = new PotionsInventory(toolbeltStack);
                    break;
                case quiver:
                    inventory = new QuiverInventory(toolbeltStack);
                    break;
                case storage:
                    inventory = new StorageInventory(toolbeltStack);
            }
            if (inventory.getContainerSize() > index && !inventory.getItem(index).isEmpty()) {
                ItemStack heldItemStack = player.m_21120_(hand);
                player.m_21008_(hand, inventory.takeItemStack(index));
                if (!heldItemStack.isEmpty() && !storeItemInToolbelt(toolbeltStack, heldItemStack) && !player.getInventory().add(heldItemStack)) {
                    inventory.storeItemInInventory(player.m_21120_(hand));
                    player.m_21008_(hand, heldItemStack);
                    player.displayClientMessage(Component.translatable("tetra.toolbelt.blocked"), true);
                }
            }
        }
    }

    public static boolean storeItemInToolbelt(Player player) {
        ItemStack toolbeltStack = findToolbelt(player);
        ItemStack itemStack = player.m_21120_(InteractionHand.OFF_HAND);
        InteractionHand sourceHand = InteractionHand.OFF_HAND;
        if (itemStack.isEmpty()) {
            itemStack = player.m_21120_(InteractionHand.MAIN_HAND);
            sourceHand = InteractionHand.MAIN_HAND;
        }
        if (toolbeltStack.isEmpty() || itemStack.isEmpty() || itemStack.getItem() == ModularToolbeltItem.instance.get()) {
            return true;
        } else if (storeItemInToolbelt(toolbeltStack, itemStack)) {
            player.m_21008_(sourceHand, ItemStack.EMPTY);
            return true;
        } else {
            return false;
        }
    }

    public static boolean storeItemInToolbelt(ItemStack toolbeltStack, ItemStack itemStack) {
        if (new PotionsInventory(toolbeltStack).storeItemInInventory(itemStack)) {
            return true;
        } else if (new QuiverInventory(toolbeltStack).storeItemInInventory(itemStack)) {
            return true;
        } else {
            return new QuickslotInventory(toolbeltStack).storeItemInInventory(itemStack) ? true : new StorageInventory(toolbeltStack).storeItemInInventory(itemStack);
        }
    }

    public static ItemStack findToolbelt(Player player) {
        if (CuriosCompat.isLoaded) {
            Optional<ImmutableTriple<String, Integer, ItemStack>> maybeToolbelt = CuriosApi.getCuriosHelper().findEquippedCurio(ModularToolbeltItem.instance.get(), player);
            if (maybeToolbelt.isPresent()) {
                return (ItemStack) ((ImmutableTriple) maybeToolbelt.get()).right;
            }
            if (ConfigHandler.toolbeltCurioOnly.get()) {
                return ItemStack.EMPTY;
            }
        }
        Inventory inventoryPlayer = player.getInventory();
        for (int i = 0; i < inventoryPlayer.items.size(); i++) {
            ItemStack itemStack = inventoryPlayer.getItem(i);
            if (ModularToolbeltItem.instance.get().equals(itemStack.getItem())) {
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static List<ItemStack> getToolbeltItems(Player player) {
        return (List<ItemStack>) Optional.of(findToolbelt(player)).filter(toolbeltStack -> !toolbeltStack.isEmpty()).map(toolbeltStack -> {
            QuickslotInventory quickslots = new QuickslotInventory(toolbeltStack);
            StorageInventory storage = new StorageInventory(toolbeltStack);
            List<ItemStack> result = new ArrayList(quickslots.m_6643_() + storage.m_6643_());
            for (int i = 0; i < quickslots.m_6643_(); i++) {
                result.add(i, quickslots.m_8020_(i));
            }
            for (int i = 0; i < storage.m_6643_(); i++) {
                result.add(quickslots.m_6643_() + i, storage.m_8020_(i));
            }
            return result;
        }).orElse(Collections.emptyList());
    }

    public static void emptyOverflowSlots(ItemStack itemStack, Player player) {
        new QuickslotInventory(itemStack).emptyOverflowSlots(player);
        new PotionsInventory(itemStack).emptyOverflowSlots(player);
        new StorageInventory(itemStack).emptyOverflowSlots(player);
        new QuiverInventory(itemStack).emptyOverflowSlots(player);
    }

    public static int getQuickAccessSlotIndex(Player player, HitResult traceResult, BlockState blockState) {
        ItemStack toolbeltStack = findToolbelt(player);
        if (toolbeltStack.isEmpty()) {
            return -1;
        } else {
            QuickslotInventory inventory = new QuickslotInventory(toolbeltStack);
            List<Collection<ItemEffect>> effects = inventory.getSlotEffects();
            if (traceResult instanceof BlockHitResult trace) {
                Vec3 hitVector = trace.m_82450_();
                BlockPos blockPos = trace.getBlockPos();
                BlockInteraction blockInteraction = (BlockInteraction) CastOptional.cast(blockState.m_60734_(), IInteractiveBlock.class).map(block -> BlockInteraction.getInteractionAtPoint(player, blockState, blockPos, trace.getDirection(), (double) ((float) hitVector.x - (float) blockPos.m_123341_()), (double) ((float) hitVector.y - (float) blockPos.m_123342_()), (double) ((float) hitVector.z - (float) blockPos.m_123343_()))).orElse(null);
                for (int i = 0; i < inventory.m_6643_(); i++) {
                    ItemStack itemStack = inventory.m_8020_(i);
                    if (((Collection) effects.get(i)).contains(ItemEffect.quickAccess) && !itemStack.isEmpty()) {
                        if (itemStack.isCorrectToolForDrops(blockState)) {
                            return i;
                        }
                        if (ItemModularHandheld.canDenail(blockState)) {
                            boolean itemCanDenail = (Boolean) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getEffectLevel(itemStack, ItemEffect.denailing) > 0).orElse(false);
                            if (itemCanDenail) {
                                return i;
                            }
                        }
                        if (blockInteraction != null && itemStack.getItem() instanceof IToolProvider) {
                            IToolProvider providerItem = (IToolProvider) itemStack.getItem();
                            if (providerItem.getToolLevel(itemStack, blockInteraction.requiredTool) >= blockInteraction.requiredLevel) {
                                return i;
                            }
                        }
                    }
                }
            }
            return -1;
        }
    }
}