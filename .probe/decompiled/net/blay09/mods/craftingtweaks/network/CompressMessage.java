package net.blay09.mods.craftingtweaks.network;

import java.util.Objects;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.craftingtweaks.CompressType;
import net.blay09.mods.craftingtweaks.CraftingTweaksProviderManager;
import net.blay09.mods.craftingtweaks.InventoryCraftingCompress;
import net.blay09.mods.craftingtweaks.InventoryCraftingDecompress;
import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.config.CraftingTweaksConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CompressMessage {

    private final int slotNumber;

    private final CompressType type;

    public CompressMessage(int slotNumber, CompressType type) {
        this.slotNumber = slotNumber;
        this.type = type;
    }

    public static CompressMessage decode(FriendlyByteBuf buf) {
        int slotNumber = buf.readInt();
        CompressType type = CompressType.values()[buf.readByte()];
        return new CompressMessage(slotNumber, type);
    }

    public static void encode(CompressMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.slotNumber);
        buf.writeByte(message.type.ordinal());
    }

    public static void handle(ServerPlayer player, CompressMessage message) {
        if (player != null) {
            AbstractContainerMenu menu = player.f_36096_;
            if (menu != null) {
                CompressType compressType = message.type;
                Slot mouseSlot = menu.slots.get(message.slotNumber);
                if (mouseSlot.container instanceof Inventory) {
                    ItemStack mouseStack = mouseSlot.getItem();
                    if (!mouseStack.isEmpty()) {
                        CraftingGrid grid = (CraftingGrid) CraftingTweaksProviderManager.getDefaultCraftingGrid(menu).orElse(null);
                        boolean compressRequiresCraftingGrid = CraftingTweaksConfig.getActive().common.compressRequiresCraftingGrid;
                        if (!compressRequiresCraftingGrid || grid != null) {
                            if (compressType != CompressType.DECOMPRESS_ALL && compressType != CompressType.DECOMPRESS_STACK && compressType != CompressType.DECOMPRESS_ONE) {
                                switch(compressType) {
                                    case COMPRESS_ONE:
                                        compressMouseSlot(player, menu, mouseSlot, grid, compressRequiresCraftingGrid, false);
                                        break;
                                    case COMPRESS_STACK:
                                        compressMouseSlot(player, menu, mouseSlot, grid, compressRequiresCraftingGrid, true);
                                        break;
                                    case COMPRESS_ALL:
                                        compressAll(player, menu, mouseSlot, grid, compressRequiresCraftingGrid);
                                }
                            } else {
                                boolean decompressAll = compressType != CompressType.DECOMPRESS_ONE;
                                for (Slot slot : menu.slots) {
                                    if (compressType == CompressType.DECOMPRESS_ALL || slot == mouseSlot) {
                                        ItemStack slotStack = slot.getItem();
                                        if (slot.container instanceof Inventory && ItemStack.isSameItemSameTags(slot.getItem(), mouseSlot.getItem())) {
                                            ItemStack result = findMatchingResult(new InventoryCraftingDecompress(menu, slotStack), player);
                                            if (!result.isEmpty() && !isBlacklisted(result) && !slotStack.isEmpty() && slotStack.getCount() >= 1) {
                                                while (player.m_150109_().add(result.copy())) {
                                                    giveLeftoverItems(player, slotStack, 1);
                                                    slot.remove(1);
                                                    if (!decompressAll || !slot.hasItem() || slotStack.getCount() < 1 || slotStack.getItem() == result.getItem()) {
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            menu.broadcastChanges();
                        }
                    }
                }
            }
        }
    }

    private static void compressMouseSlot(ServerPlayer player, AbstractContainerMenu menu, Slot mouseSlot, CraftingGrid grid, boolean compressRequiresCraftingGrid, boolean wholeStack) {
        int maxGridSize = grid != null && compressRequiresCraftingGrid ? grid.getGridSize(player, menu) : 9;
        ItemStack mouseStack = mouseSlot.getItem();
        CompressMessage.CompressionRecipe recipe = findRecipe(menu, player, mouseStack, maxGridSize);
        int recipeSize = recipe.size();
        if (recipeSize > 0) {
            int maxStackSize = recipe.result().getMaxStackSize();
            int craftsPossible = Math.min(mouseStack.getCount() / recipeSize, wholeStack ? maxStackSize : 1);
            if (craftsPossible == 0) {
                return;
            }
            int itemsToRemove = craftsPossible * recipeSize;
            giveLeftoverItems(player, mouseStack, itemsToRemove);
            mouseStack.shrink(itemsToRemove);
            addCraftedItemsToInventory(player, recipe.result(), craftsPossible);
        }
    }

    private static void compressAll(ServerPlayer player, AbstractContainerMenu menu, Slot mouseSlot, CraftingGrid grid, boolean compressRequiresCraftingGrid) {
        int maxGridSize = grid != null && compressRequiresCraftingGrid ? grid.getGridSize(player, menu) : 9;
        ItemStack mouseStack = mouseSlot.getItem().copy();
        int totalItemCount = countTotalItems(menu, mouseStack);
        CompressMessage.CompressionRecipe recipe = findRecipe(menu, player, mouseStack, maxGridSize);
        int recipeSize = recipe.size();
        if (recipeSize > 0) {
            int craftsPossible = totalItemCount / recipeSize;
            int itemsToRemove = craftsPossible * recipeSize;
            removeSourceItems(player, menu, mouseStack, itemsToRemove);
            addCraftedItemsToInventory(player, recipe.result(), craftsPossible);
        }
    }

    private static void giveLeftoverItems(ServerPlayer player, ItemStack slotStack, int count) {
        for (int i = 0; i < count; i++) {
            ItemStack containerItem = Balm.getHooks().getCraftingRemainingItem(slotStack);
            if (!player.m_36356_(containerItem)) {
                ItemEntity itemEntity = player.m_36176_(containerItem, false);
                if (itemEntity != null) {
                    itemEntity.setNoPickUpDelay();
                    itemEntity.setTarget(player.m_20148_());
                }
            }
        }
    }

    private static <T extends CraftingContainer & RecipeHolder> ItemStack findMatchingResult(T craftingInventory, ServerPlayer player) {
        RecipeManager recipeManager = ((MinecraftServer) Objects.requireNonNull(player.m_20194_())).getRecipeManager();
        Level level = player.m_9236_();
        CraftingRecipe recipe = (CraftingRecipe) recipeManager.getRecipeFor(RecipeType.CRAFTING, craftingInventory, level).orElse(null);
        return recipe != null && craftingInventory.setRecipeUsed(level, player, recipe) ? recipe.m_5874_(craftingInventory, level.registryAccess()) : ItemStack.EMPTY;
    }

    private static boolean isBlacklisted(ItemStack result) {
        ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(result.getItem());
        return CraftingTweaksConfig.getActive().common.compressDenylist.contains(registryName.toString());
    }

    private static int countTotalItems(AbstractContainerMenu menu, ItemStack sourceItem) {
        int totalItemCount = 0;
        for (Slot slot : menu.slots) {
            ItemStack slotStack = slot.getItem();
            if (slot.container instanceof Inventory && ItemStack.isSameItemSameTags(slot.getItem(), sourceItem)) {
                totalItemCount += slotStack.getCount();
            }
        }
        return totalItemCount;
    }

    private static CompressMessage.CompressionRecipe findRecipe(AbstractContainerMenu menu, ServerPlayer player, ItemStack exampleStack, int maxGridSize) {
        int recipeSize = 0;
        ItemStack result = ItemStack.EMPTY;
        if (maxGridSize >= 9) {
            InventoryCraftingCompress exampleInventory = new InventoryCraftingCompress(menu, 3, exampleStack);
            result = findMatchingResult(exampleInventory, player);
            if (!result.isEmpty() && !isBlacklisted(result)) {
                recipeSize = 9;
            }
        }
        if (recipeSize == 0 && maxGridSize >= 4) {
            InventoryCraftingCompress exampleInventory = new InventoryCraftingCompress(menu, 2, exampleStack);
            result = findMatchingResult(exampleInventory, player);
            if (!result.isEmpty() && !isBlacklisted(result)) {
                recipeSize = 4;
            }
        }
        return new CompressMessage.CompressionRecipe(recipeSize, result);
    }

    private static void removeSourceItems(ServerPlayer player, AbstractContainerMenu menu, ItemStack sourceItem, int itemsToRemove) {
        for (Slot slot : menu.slots) {
            ItemStack slotStack = slot.getItem();
            if (slot.container instanceof Inventory && ItemStack.isSameItemSameTags(slot.getItem(), sourceItem)) {
                int removedFromSlot = Math.min(slotStack.getCount(), itemsToRemove);
                giveLeftoverItems(player, slotStack, removedFromSlot);
                slot.remove(removedFromSlot);
                itemsToRemove -= removedFromSlot;
                if (itemsToRemove == 0) {
                    break;
                }
            }
        }
    }

    private static void addCraftedItemsToInventory(ServerPlayer player, ItemStack result, int timesCrafted) {
        int itemsCrafted = timesCrafted * result.getCount();
        while (itemsCrafted > 0) {
            ItemStack craftedStack = result.copy();
            craftedStack.setCount(Math.min(itemsCrafted, result.getMaxStackSize()));
            itemsCrafted -= craftedStack.getCount();
            if (!player.m_150109_().add(craftedStack)) {
                player.m_36176_(craftedStack, true);
            }
        }
    }

    static record CompressionRecipe(int size, ItemStack result) {
    }
}