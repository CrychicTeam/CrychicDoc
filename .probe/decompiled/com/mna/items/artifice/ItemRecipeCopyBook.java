package com.mna.items.artifice;

import com.mna.Registries;
import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.items.TieredItem;
import com.mna.blocks.tileentities.EldrinAltarTile;
import com.mna.blocks.tileentities.ManaweavingAltarTile;
import com.mna.items.base.IRadialInventorySelect;
import com.mna.items.renderers.books.RecipeCopyBookRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

public class ItemRecipeCopyBook extends TieredItem implements DyeableLeatherItem, IRadialInventorySelect {

    private static final String NBT_RECIPE_OUTPUT = "recipe_output";

    private static final String NBT_RECIPE_COST = "recipe_cost";

    private static final String NBT_RECIPE_INPUTS = "recipe_inputs";

    private static final String NBT_RECIPE_FACTION = "faction";

    private static final String NBT_RECIPE_TIER = "tier";

    private static final String NBT_RECIPE_PATTERN_COUNT = "pattern_count";

    private static final String NBT_RECIPE_TYPE = "recipe_type";

    private static final String NBT_RECIPE_CONTAINER = "recipes";

    private static final String NBT_MODE = "mode";

    public static final int RECIPE_SLOTS = 16;

    private static final byte RECIPE_TYPE_MANAWEAVING = 0;

    private static final byte RECIPE_TYPE_ELDRIN = 1;

    public ItemRecipeCopyBook() {
        super(new Item.Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        BlockEntity tile = pContext.getLevel().getBlockEntity(pos);
        ItemStack stack = pContext.getItemInHand();
        ItemRecipeCopyBook.Modes mode = getMode(stack);
        if (pContext.getPlayer().m_6047_()) {
            toggleMode(stack);
        } else if (tile != null) {
            if (tile instanceof ManaweavingAltarTile mwTile) {
                if (mode == ItemRecipeCopyBook.Modes.COPY) {
                    if (!copyRememberedRecipe(stack, mwTile)) {
                        return InteractionResult.FAIL;
                    }
                } else if (!pasteRememberedRecipe(stack, mwTile, pContext.getPlayer())) {
                    return InteractionResult.FAIL;
                }
            } else if (tile instanceof EldrinAltarTile eTile) {
                if (mode == ItemRecipeCopyBook.Modes.COPY) {
                    if (!copyRememberedRecipe(stack, eTile)) {
                        return InteractionResult.FAIL;
                    }
                } else if (!pasteRememberedRecipe(stack, eTile, pContext.getPlayer())) {
                    return InteractionResult.FAIL;
                }
            }
            return InteractionResult.sidedSuccess(pContext.getLevel().isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        ItemRecipeCopyBook.Modes mode = getMode(pStack);
        if (mode == ItemRecipeCopyBook.Modes.COPY) {
            pTooltipComponents.add(Component.translatable("item.mna.recipe_copy_book.copy").withStyle(ChatFormatting.AQUA));
        } else {
            int slot = getSlot(pStack);
            ItemStack output = getOutputItem(pStack, slot);
            Component tileEntityName = Component.translatable(getTileType(pStack, slot) == 0 ? "block.mna.manaweaving_altar" : "block.mna.eldrin_altar").withStyle(ChatFormatting.AQUA);
            pTooltipComponents.add(Component.translatable("item.mna.recipe_copy_book.paste").withStyle(ChatFormatting.AQUA).append(Component.translatable("item.mna.recipe_copy_book.paste_2", Component.translatable(output.getDescriptionId()).withStyle(ChatFormatting.AQUA), tileEntityName).withStyle(ChatFormatting.WHITE)));
        }
        IRadialInventorySelect.super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return getMode(pStack) == ItemRecipeCopyBook.Modes.PASTE;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new RecipeCopyBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    public static boolean copyRememberedRecipe(ItemStack stack, ManaweavingAltarTile tile) {
        ItemStack output = tile.getReCraftOutput();
        List<ItemStack> inputs = tile.getReCraftInput();
        IFaction faction = tile.getLastCraftFaction();
        int tier = tile.getLastCraftTier();
        int patternCount = tile.getLastCraftPatternCount();
        if (!output.isEmpty() && inputs.size() != 0) {
            CompoundTag outputTag = new CompoundTag();
            output.save(outputTag);
            ListTag inputsTag = new ListTag();
            inputs.forEach(is -> {
                CompoundTag item = new CompoundTag();
                is.save(item);
                inputsTag.add(item);
            });
            CompoundTag data = new CompoundTag();
            data.put("recipe_output", outputTag);
            data.put("recipe_inputs", inputsTag);
            data.putInt("tier", tier);
            data.putInt("pattern_count", patternCount);
            if (faction != null) {
                data.putString("faction", ((IForgeRegistry) Registries.Factions.get()).getKey(faction).toString());
            }
            data.putByte("recipe_type", (byte) 0);
            int slot = getSlot(stack);
            setSlotData(stack, slot, data);
            return true;
        } else {
            return false;
        }
    }

    public static boolean copyRememberedRecipe(ItemStack stack, EldrinAltarTile tile) {
        ItemStack output = tile.getReCraftOutput();
        List<ItemStack> inputs = tile.getReCraftInput();
        IFaction faction = tile.getLastCraftFaction();
        int tier = tile.getLastCraftTier();
        if (!output.isEmpty() && inputs.size() != 0) {
            CompoundTag outputTag = new CompoundTag();
            output.save(outputTag);
            ListTag inputsTag = new ListTag();
            inputs.forEach(is -> {
                CompoundTag item = new CompoundTag();
                is.save(item);
                inputsTag.add(item);
            });
            CompoundTag affTag = new CompoundTag();
            tile.getReCraftEldrin().entrySet().forEach(e -> affTag.putFloat(((Affinity) e.getKey()).name(), (Float) e.getValue()));
            CompoundTag data = new CompoundTag();
            data.put("recipe_output", outputTag);
            data.put("recipe_inputs", inputsTag);
            data.put("recipe_cost", affTag);
            data.putInt("tier", tier);
            if (faction != null) {
                data.putString("faction", ((IForgeRegistry) Registries.Factions.get()).getKey(faction).toString());
            }
            data.putByte("recipe_type", (byte) 1);
            int slot = getSlot(stack);
            setSlotData(stack, slot, data);
            return true;
        } else {
            return false;
        }
    }

    public static boolean pasteRememberedRecipe(ItemStack stack, ManaweavingAltarTile tile, @Nullable Player player) {
        int slot = getSlot(stack);
        CompoundTag data = getSlotData(stack, slot);
        if (data == null) {
            return false;
        } else if (data.contains("recipe_type") && data.getByte("recipe_type") == 0 && data.contains("tier", 3) && data.contains("pattern_count", 3) && data.contains("recipe_inputs", 9) && data.contains("recipe_output", 10)) {
            CompoundTag outputTag = data.getCompound("recipe_output");
            ListTag inputTag = data.getList("recipe_inputs", 10);
            int tier = data.getInt("tier");
            int patternCount = data.getInt("pattern_count");
            String factionID = data.getString("faction");
            ItemStack output = ItemStack.of(outputTag);
            ArrayList<ItemStack> inputs = new ArrayList();
            inputTag.forEach(tag -> inputs.add(ItemStack.of((CompoundTag) tag)));
            IFaction faction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(new ResourceLocation(factionID));
            tile.setReCraftRecipe(output, inputs, faction, tier, patternCount);
            if (player != null) {
                tile.reCraft(player);
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean pasteRememberedRecipe(ItemStack stack, EldrinAltarTile tile, @Nullable Player player) {
        int slot = getSlot(stack);
        CompoundTag data = getSlotData(stack, slot);
        if (data.contains("recipe_type") && data.getByte("recipe_type") == 1 && data.contains("tier", 3) && data.contains("recipe_inputs", 9) && data.contains("recipe_output", 10) && data.contains("recipe_cost", 10)) {
            CompoundTag outputTag = data.getCompound("recipe_output");
            CompoundTag outputCost = data.getCompound("recipe_cost");
            ListTag inputTag = data.getList("recipe_inputs", 10);
            int tier = data.getInt("tier");
            String factionID = data.getString("faction");
            ItemStack output = ItemStack.of(outputTag);
            ArrayList<ItemStack> inputs = new ArrayList();
            inputTag.forEach(tag -> inputs.add(ItemStack.of((CompoundTag) tag)));
            IFaction faction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(new ResourceLocation(factionID));
            HashMap<Affinity, Float> powerRequirements = new HashMap();
            for (Affinity aff : Affinity.values()) {
                if (outputCost.contains(aff.name())) {
                    powerRequirements.put(aff, outputCost.getFloat(aff.name()));
                }
            }
            tile.setReCraftRecipe(output, inputs, faction, tier, powerRequirements);
            if (player != null) {
                tile.reCraft(player);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int capacity() {
        return 16;
    }

    @Override
    public int getIndex(ItemStack stack) {
        return getSlot(stack);
    }

    @Override
    public void setIndex(ItemStack stack, int index) {
        setSlot(stack, index);
    }

    @Override
    public IItemHandlerModifiable getInventory(final ItemStack stackEquipped, @Nullable Player player) {
        return new IItemHandlerModifiable() {

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return false;
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return ItemStack.EMPTY;
            }

            @NotNull
            @Override
            public ItemStack getStackInSlot(int slot) {
                return ItemRecipeCopyBook.getOutputItem(stackEquipped, slot);
            }

            @Override
            public int getSlots() {
                return ItemRecipeCopyBook.this.capacity();
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }

            @NotNull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return ItemStack.EMPTY;
            }

            @Override
            public void setStackInSlot(int slot, @NotNull ItemStack stack) {
            }
        };
    }

    public static ItemRecipeCopyBook.Modes getMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? ItemRecipeCopyBook.Modes.COPY : ItemRecipeCopyBook.Modes.values()[tag.getInt("mode") % ItemRecipeCopyBook.Modes.values().length];
    }

    public static void toggleMode(ItemStack stack) {
        int idx = (getMode(stack).ordinal() + 1) % ItemRecipeCopyBook.Modes.values().length;
        stack.getOrCreateTag().putInt("mode", idx);
    }

    public static int getSlot(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? 0 : tag.getInt("index");
    }

    public static void setSlot(ItemStack stack, int slot) {
        stack.getOrCreateTag().putInt("index", slot);
        ItemStack output = getOutputItem(stack, slot);
        ItemRecipeCopyBook.Modes mode = getMode(stack);
        if (output.isEmpty() && mode == ItemRecipeCopyBook.Modes.PASTE) {
            toggleMode(stack);
        } else if (!output.isEmpty() && mode == ItemRecipeCopyBook.Modes.COPY) {
            toggleMode(stack);
        }
    }

    public static CompoundTag getSlotData(ItemStack stack, int slot) {
        return stack.getTag() == null ? null : stack.getTag().getCompound("recipes_" + slot);
    }

    public static void setSlotData(ItemStack stack, int slot, CompoundTag data) {
        stack.getOrCreateTag().put("recipes_" + slot, data);
        if (getSlot(stack) == slot && getMode(stack) == ItemRecipeCopyBook.Modes.COPY) {
            toggleMode(stack);
        }
    }

    public static ItemStack getOutputItem(ItemStack stack, int slot) {
        CompoundTag tag = getSlotData(stack, slot);
        return tag != null && tag.contains("recipe_output", 10) ? ItemStack.of(tag.getCompound("recipe_output")) : ItemStack.EMPTY;
    }

    public static int getTileType(ItemStack stack, int slot) {
        CompoundTag tag = getSlotData(stack, slot);
        return tag != null && tag.contains("recipe_type", 1) ? tag.getInt("recipe_type") : 0;
    }

    public static int getTier(ItemStack stack, int slot) {
        CompoundTag tag = getSlotData(stack, slot);
        return tag != null && tag.contains("tier", 3) ? tag.getInt("tier") : 0;
    }

    public static IFaction getFaction(ItemStack stack, int slot) {
        CompoundTag tag = getSlotData(stack, slot);
        return tag != null && tag.contains("faction", 8) ? (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(new ResourceLocation(tag.getString("faction"))) : null;
    }

    public static enum Modes {

        COPY, PASTE
    }
}