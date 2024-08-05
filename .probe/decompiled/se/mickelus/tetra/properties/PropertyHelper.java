package se.mickelus.tetra.properties;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.mutil.util.InventoryStream;
import se.mickelus.tetra.blocks.workbench.AbstractWorkbenchBlock;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltHelper;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.QuickslotInventory;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.StorageInventory;
import se.mickelus.tetra.module.ItemUpgradeRegistry;

@ParametersAreNonnullByDefault
public class PropertyHelper {

    public static int getItemToolLevel(ItemStack itemStack, ToolAction tool) {
        return (Integer) Optional.of(itemStack).filter(stack -> !stack.isEmpty()).map(PropertyHelper::getReplacement).filter(stack -> stack.getItem() instanceof IToolProvider).map(stack -> ((IToolProvider) stack.getItem()).getToolLevel(stack, tool)).orElse(0);
    }

    public static Set<ToolAction> getItemTools(ItemStack itemStack) {
        return (Set<ToolAction>) Optional.of(itemStack).filter(stack -> !stack.isEmpty()).map(PropertyHelper::getReplacement).filter(stack -> stack.getItem() instanceof IToolProvider).map(stack -> ((IToolProvider) stack.getItem()).getTools(stack)).orElse(Collections.emptySet());
    }

    public static int getPlayerEffectLevel(Player player, ItemEffect effect) {
        return (Integer) Stream.concat(player.getInventory().offhand.stream(), player.getInventory().items.stream()).filter(itemStack -> !itemStack.isEmpty()).map(PropertyHelper::getReplacement).filter(itemStack -> itemStack.getItem() instanceof IModularItem).map(itemStack -> ((IModularItem) itemStack.getItem()).getEffectLevel(itemStack, effect)).max(Integer::compare).orElse(0);
    }

    public static float getPlayerEffectEfficiency(Player player, ItemEffect effect) {
        return (Float) Stream.concat(player.getInventory().offhand.stream(), player.getInventory().items.stream()).filter(itemStack -> !itemStack.isEmpty()).map(PropertyHelper::getReplacement).filter(itemStack -> itemStack.getItem() instanceof IModularItem).max(Comparator.comparingInt(itemStack -> ((IModularItem) itemStack.getItem()).getEffectLevel(itemStack, effect))).map(itemStack -> ((IModularItem) itemStack.getItem()).getEffectEfficiency(itemStack, effect)).orElse(0.0F);
    }

    public static int getPlayerToolLevel(Player player, ToolAction tool) {
        return (Integer) Stream.concat(player.getInventory().offhand.stream(), player.getInventory().items.stream()).filter(itemStack -> !itemStack.isEmpty()).map(PropertyHelper::getReplacement).filter(itemStack -> itemStack.getItem() instanceof IToolProvider).map(itemStack -> ((IToolProvider) itemStack.getItem()).getToolLevel(itemStack, tool)).max(Integer::compare).orElse(0);
    }

    public static Set<ToolAction> getPlayerTools(Player player) {
        return (Set<ToolAction>) Stream.concat(player.getInventory().offhand.stream(), player.getInventory().items.stream()).filter(itemStack -> !itemStack.isEmpty()).map(PropertyHelper::getReplacement).filter(itemStack -> itemStack.getItem() instanceof IToolProvider).flatMap(itemStack -> ((IToolProvider) itemStack.getItem()).getTools(itemStack).stream()).collect(Collectors.toSet());
    }

    public static Map<ToolAction, Integer> getPlayerToolLevels(Player player) {
        return (Map<ToolAction, Integer>) Stream.concat(player.getInventory().offhand.stream(), player.getInventory().items.stream()).filter(itemStack -> !itemStack.isEmpty()).map(PropertyHelper::getReplacement).filter(itemStack -> itemStack.getItem() instanceof IToolProvider).map(itemStack -> ((IToolProvider) itemStack.getItem()).getToolLevels(itemStack)).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Math::max));
    }

    public static int getInventoryToolLevel(Container inventory, ToolAction tool) {
        int result = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            int comparisonLevel = result;
            result = (Integer) Optional.of(inventory.getItem(i)).filter(itemStack -> !itemStack.isEmpty()).map(PropertyHelper::getReplacement).filter(itemStack -> itemStack.getItem() instanceof IToolProvider).map(itemStack -> ((IToolProvider) itemStack.getItem()).getToolLevel(itemStack, tool)).filter(level -> level > comparisonLevel).orElse(comparisonLevel);
        }
        return result;
    }

    public static Set<ToolAction> getInventoryTools(Container inventory) {
        Set<ToolAction> result = new HashSet();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ((Stream) Optional.of(inventory.getItem(i)).filter(itemStack -> !itemStack.isEmpty()).map(PropertyHelper::getReplacement).filter(itemStack -> itemStack.getItem() instanceof IToolProvider).map(itemStack -> ((IToolProvider) itemStack.getItem()).getTools(itemStack).stream()).orElseGet(Stream::empty)).forEach(result::add);
        }
        return result;
    }

    public static Map<ToolAction, Integer> getInventoryToolLevels(Container inventory) {
        return (Map<ToolAction, Integer>) InventoryStream.of(inventory).filter(itemStack -> !itemStack.isEmpty()).map(PropertyHelper::getReplacement).filter(itemStack -> itemStack.getItem() instanceof IToolProvider).map(itemStack -> ((IToolProvider) itemStack.getItem()).getToolLevels(itemStack)).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Math::max));
    }

    public static ItemStack getInventoryProvidingItemStack(Container inventory, ToolAction tool, int level) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack result = (ItemStack) Optional.of(inventory.getItem(i)).filter(itemStack -> !itemStack.isEmpty()).map(PropertyHelper::getReplacement).filter(itemStack -> itemStack.getItem() instanceof IToolProvider).filter(itemStack -> ((IToolProvider) itemStack.getItem()).getToolLevel(itemStack, tool) >= level).orElse(ItemStack.EMPTY);
            if (!result.isEmpty()) {
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getPlayerProvidingItemStack(ToolAction tool, int level, Entity entity) {
        return (ItemStack) ((Stream) CastOptional.cast(entity, Player.class).map(player -> Stream.concat(Stream.of(player.m_21205_(), player.m_21206_()), player.getInventory().items.stream())).orElse(Stream.empty())).filter(itemStack -> !itemStack.isEmpty()).map(PropertyHelper::getReplacement).filter(itemStack -> itemStack.getItem() instanceof IToolProvider).filter(itemStack -> ((IToolProvider) itemStack.getItem()).getToolLevel(itemStack, tool) >= level).findAny().orElse(ItemStack.EMPTY);
    }

    public static ItemStack consumeCraftToolInventory(Container inventory, Player player, ItemStack targetStack, ToolAction tool, int level, boolean consumeResources) {
        ItemStack itemStack = getInventoryProvidingItemStack(inventory, tool, level);
        return itemStack.getItem() instanceof IToolProvider ? ((IToolProvider) itemStack.getItem()).onCraftConsume(itemStack, targetStack, player, tool, level, consumeResources) : null;
    }

    public static ItemStack consumeActionToolInventory(Container inventory, Player player, ItemStack targetStack, ToolAction tool, int level, boolean consumeResources) {
        ItemStack itemStack = getInventoryProvidingItemStack(inventory, tool, level);
        return itemStack.getItem() instanceof IToolProvider ? ((IToolProvider) itemStack.getItem()).onActionConsume(itemStack, targetStack, player, tool, level, consumeResources) : null;
    }

    private static ItemStack getReplacement(ItemStack itemStack) {
        ItemStack replacement = ItemUpgradeRegistry.instance.getReplacement(itemStack);
        return !replacement.isEmpty() ? replacement : itemStack;
    }

    public static int getBlockToolLevel(Level world, BlockPos pos, BlockState blockStateIn, ToolAction tool) {
        return (Integer) Optional.of(blockStateIn).map(BlockBehaviour.BlockStateBase::m_60734_).flatMap(block -> CastOptional.cast(block, AbstractWorkbenchBlock.class)).map(block -> block.getToolLevel(world, pos, blockStateIn, tool)).orElse(0);
    }

    public static Collection<ToolAction> getBlockTools(Level world, BlockPos pos, BlockState blockStateIn) {
        return (Collection<ToolAction>) Optional.of(blockStateIn).map(BlockBehaviour.BlockStateBase::m_60734_).flatMap(block -> CastOptional.cast(block, AbstractWorkbenchBlock.class)).map(block -> block.getTools(world, pos, blockStateIn)).orElse(Collections.emptyList());
    }

    public static Map<ToolAction, Integer> getBlockToolLevels(Level world, BlockPos pos, BlockState blockStateIn) {
        return (Map<ToolAction, Integer>) Optional.of(blockStateIn).map(BlockBehaviour.BlockStateBase::m_60734_).flatMap(block -> CastOptional.cast(block, AbstractWorkbenchBlock.class)).map(block -> block.getToolLevels(world, pos, blockStateIn)).orElse(Collections.emptyMap());
    }

    public static int getToolbeltToolLevel(Player player, ToolAction tool) {
        return (Integer) Optional.of(ToolbeltHelper.findToolbelt(player)).filter(toolbeltStack -> !toolbeltStack.isEmpty()).map(toolbeltStack -> Math.max(getInventoryToolLevel(new QuickslotInventory(toolbeltStack), tool), getInventoryToolLevel(new StorageInventory(toolbeltStack), tool))).orElse(0);
    }

    public static Set<ToolAction> getToolbeltTools(Player player) {
        return (Set<ToolAction>) Optional.of(ToolbeltHelper.findToolbelt(player)).filter(toolbeltStack -> !toolbeltStack.isEmpty()).map(toolbeltStack -> Sets.union(getInventoryTools(new QuickslotInventory(toolbeltStack)), getInventoryTools(new StorageInventory(toolbeltStack)))).orElse(Collections.emptySet());
    }

    public static Map<ToolAction, Integer> getToolbeltToolLevels(Player player) {
        return (Map<ToolAction, Integer>) ((Stream) Optional.of(ToolbeltHelper.findToolbelt(player)).filter(toolbeltStack -> !toolbeltStack.isEmpty()).map(toolbeltStack -> Stream.of(getInventoryToolLevels(new QuickslotInventory(toolbeltStack)), getInventoryToolLevels(new StorageInventory(toolbeltStack)))).orElseGet(Stream::empty)).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Math::max));
    }

    @Nullable
    public static ItemStack consumeCraftToolToolbelt(Player player, ItemStack targetStack, ToolAction tool, int level, boolean consumeResources) {
        return (ItemStack) Optional.of(ToolbeltHelper.findToolbelt(player)).filter(toolbeltStack -> !toolbeltStack.isEmpty()).map(toolbeltStack -> {
            QuickslotInventory quickslotInventory = new QuickslotInventory(toolbeltStack);
            ItemStack result = consumeCraftToolInventory(quickslotInventory, player, targetStack, tool, level, consumeResources);
            if (result != null) {
                quickslotInventory.setChanged();
                return result;
            } else {
                StorageInventory storageInventory = new StorageInventory(toolbeltStack);
                result = consumeCraftToolInventory(quickslotInventory, player, targetStack, tool, level, consumeResources);
                if (result != null) {
                    storageInventory.m_6596_();
                    return result;
                } else {
                    return null;
                }
            }
        }).orElse(null);
    }

    public static ItemStack consumeActionToolToolbelt(Player player, ItemStack targetStack, ToolAction tool, int level, boolean consumeResources) {
        return (ItemStack) Optional.of(ToolbeltHelper.findToolbelt(player)).filter(toolbeltStack -> !toolbeltStack.isEmpty()).map(toolbeltStack -> {
            QuickslotInventory quickslotInventory = new QuickslotInventory(toolbeltStack);
            ItemStack result = consumeActionToolInventory(quickslotInventory, player, targetStack, tool, level, consumeResources);
            if (result != null) {
                quickslotInventory.setChanged();
                return result;
            } else {
                StorageInventory storageInventory = new StorageInventory(toolbeltStack);
                result = consumeActionToolInventory(quickslotInventory, player, targetStack, tool, level, consumeResources);
                if (result != null) {
                    storageInventory.m_6596_();
                    return result;
                } else {
                    return null;
                }
            }
        }).orElse(null);
    }

    public static ItemStack getToolbeltProvidingItemStack(ToolAction tool, int level, Player player) {
        return (ItemStack) Optional.of(ToolbeltHelper.findToolbelt(player)).filter(itemStack -> !itemStack.isEmpty()).map(toolbeltStack -> {
            ItemStack itemStack = getInventoryProvidingItemStack(new QuickslotInventory(toolbeltStack), tool, level);
            return !itemStack.isEmpty() ? itemStack : getInventoryProvidingItemStack(new StorageInventory(toolbeltStack), tool, level);
        }).orElse(ItemStack.EMPTY);
    }

    public static int getCombinedToolLevel(Player player, Level world, BlockPos pos, BlockState blockStateIn, ToolAction tool) {
        return IntStream.of(new int[] { getPlayerToolLevel(player, tool), getToolbeltToolLevel(player, tool), getBlockToolLevel(world, pos, blockStateIn, tool) }).max().orElse(0);
    }

    public static Map<ToolAction, Integer> getCombinedToolLevels(Player player, Level world, BlockPos pos, BlockState blockStateIn) {
        return (Map<ToolAction, Integer>) Stream.of(getInventoryToolLevels(player.getInventory()), getToolbeltToolLevels(player), getBlockToolLevels(world, pos, blockStateIn)).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Math::max));
    }
}