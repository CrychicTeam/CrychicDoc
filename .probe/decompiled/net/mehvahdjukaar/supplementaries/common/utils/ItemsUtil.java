package net.mehvahdjukaar.supplementaries.common.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.AdditionalItemPlacement;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.AdditionalItemPlacementsAPI;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.BlockPlacerItem;
import net.mehvahdjukaar.supplementaries.common.block.IKeyLockable;
import net.mehvahdjukaar.supplementaries.common.utils.forge.ItemsUtilImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ItemsUtil {

    @Nullable
    public static BlockState getPlacementState(BlockPlaceContext context, Block block) {
        return BlockPlacerItem.get().mimicGetPlacementState(context, block);
    }

    public static InteractionResult place(BlockPlaceContext context, Block blockToPlace) {
        return place(context, blockToPlace, null);
    }

    public static InteractionResult place(BlockPlaceContext context, Block blockToPlace, @Nullable SoundType placeSound) {
        return BlockPlacerItem.get().mimicPlace(context, blockToPlace, placeSound);
    }

    public static InteractionResult place(Item item, BlockPlaceContext pContext) {
        if (item instanceof BlockItem bi) {
            return bi.place(pContext);
        } else {
            AdditionalItemPlacement placement = AdditionalItemPlacementsAPI.getBehavior(item);
            return placement != null ? placement.overridePlace(pContext) : InteractionResult.PASS;
        }
    }

    public static void addStackToExisting(Player player, ItemStack stack, boolean avoidHands) {
        Inventory inv = player.getInventory();
        boolean added = false;
        for (int j = 0; j < inv.items.size(); j++) {
            if (inv.getItem(j).is(stack.getItem()) && inv.add(j, stack)) {
                added = true;
                break;
            }
        }
        if (avoidHands && !added) {
            for (int jx = 0; jx < inv.items.size(); jx++) {
                if (inv.getItem(jx).isEmpty() && jx != inv.selected && inv.add(jx, stack)) {
                    added = true;
                    break;
                }
            }
        }
        if (!added && inv.add(stack)) {
            player.drop(stack, false);
        }
    }

    public static boolean tryInteractingWithContainerItem(ItemStack containerStack, ItemStack incoming, Slot slot, ClickAction action, Player player, boolean inSlot) {
        if (action != ClickAction.PRIMARY) {
            if (incoming.isEmpty()) {
                if (!inSlot) {
                    return extractFromContainerItemIntoSlot(player, containerStack, slot);
                }
            } else if (addToContainerItem(player, containerStack, incoming, slot, true, inSlot)) {
                return addToContainerItem(player, containerStack, incoming, slot, false, inSlot);
            }
        }
        return false;
    }

    @ExpectPlatform
    @Transformed
    public static boolean addToContainerItem(Player player, ItemStack containerStack, ItemStack stack, Slot slot, boolean simulate, boolean inSlot) {
        return ItemsUtilImpl.addToContainerItem(player, containerStack, stack, slot, simulate, inSlot);
    }

    @ExpectPlatform
    @Transformed
    public static boolean extractFromContainerItemIntoSlot(Player player, ItemStack containerStack, Slot slot) {
        return ItemsUtilImpl.extractFromContainerItemIntoSlot(player, containerStack, slot);
    }

    @Nullable
    public static BlockEntity loadBlockEntityFromItem(CompoundTag tag, Item item) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof EntityBlock entityBlock) {
                BlockEntity te = entityBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                if (te != null) {
                    te.load(tag);
                }
                return te;
            }
        }
        return null;
    }

    @ExpectPlatform
    @Transformed
    public static float getEncumbermentFromInventory(ItemStack stack, ServerPlayer player) {
        return ItemsUtilImpl.getEncumbermentFromInventory(stack, player);
    }

    @ExpectPlatform
    @Transformed
    public static IKeyLockable.KeyStatus hasKeyInInventory(Player player, String key) {
        return ItemsUtilImpl.hasKeyInInventory(player, key);
    }

    @ExpectPlatform
    @Transformed
    public static ItemStack tryExtractingItem(Level level, @Nullable Direction dir, Object container) {
        return ItemsUtilImpl.tryExtractingItem(level, dir, container);
    }

    @ExpectPlatform
    @Transformed
    public static ItemStack tryAddingItem(ItemStack stack, Level level, @Nullable Direction direction, Object container) {
        return ItemsUtilImpl.tryAddingItem(stack, level, direction, container);
    }
}