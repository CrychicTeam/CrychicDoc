package com.simibubi.create.content.kinetics.crafter;

import com.google.common.base.Predicates;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pointing;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.FireworkRocketRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

public class RecipeGridHandler {

    public static List<MechanicalCrafterBlockEntity> getAllCraftersOfChain(MechanicalCrafterBlockEntity root) {
        return getAllCraftersOfChainIf(root, Predicates.alwaysTrue());
    }

    public static List<MechanicalCrafterBlockEntity> getAllCraftersOfChainIf(MechanicalCrafterBlockEntity root, Predicate<MechanicalCrafterBlockEntity> test) {
        return getAllCraftersOfChainIf(root, test, false);
    }

    public static List<MechanicalCrafterBlockEntity> getAllCraftersOfChainIf(MechanicalCrafterBlockEntity root, Predicate<MechanicalCrafterBlockEntity> test, boolean poweredStart) {
        List<MechanicalCrafterBlockEntity> crafters = new ArrayList();
        List<Pair<MechanicalCrafterBlockEntity, MechanicalCrafterBlockEntity>> frontier = new ArrayList();
        Set<MechanicalCrafterBlockEntity> visited = new HashSet();
        frontier.add(Pair.of(root, null));
        boolean powered = false;
        boolean empty = false;
        boolean allEmpty = true;
        while (!frontier.isEmpty()) {
            Pair<MechanicalCrafterBlockEntity, MechanicalCrafterBlockEntity> pair = (Pair<MechanicalCrafterBlockEntity, MechanicalCrafterBlockEntity>) frontier.remove(0);
            MechanicalCrafterBlockEntity current = (MechanicalCrafterBlockEntity) pair.getKey();
            MechanicalCrafterBlockEntity last = (MechanicalCrafterBlockEntity) pair.getValue();
            if (visited.contains(current)) {
                return null;
            }
            if (!test.test(current)) {
                empty = true;
            } else {
                allEmpty = false;
            }
            if (poweredStart && current.m_58904_().m_276867_(current.m_58899_())) {
                powered = true;
            }
            crafters.add(current);
            visited.add(current);
            MechanicalCrafterBlockEntity target = getTargetingCrafter(current);
            if (target != last && target != null) {
                frontier.add(Pair.of(target, current));
            }
            for (MechanicalCrafterBlockEntity preceding : getPrecedingCrafters(current)) {
                if (preceding != last) {
                    frontier.add(Pair.of(preceding, current));
                }
            }
        }
        return (!empty || powered) && !allEmpty ? crafters : null;
    }

    public static MechanicalCrafterBlockEntity getTargetingCrafter(MechanicalCrafterBlockEntity crafter) {
        BlockState state = crafter.m_58900_();
        if (!isCrafter(state)) {
            return null;
        } else {
            BlockPos targetPos = crafter.m_58899_().relative(MechanicalCrafterBlock.getTargetDirection(state));
            MechanicalCrafterBlockEntity targetBE = CrafterHelper.getCrafter(crafter.m_58904_(), targetPos);
            if (targetBE == null) {
                return null;
            } else {
                BlockState targetState = targetBE.m_58900_();
                if (!isCrafter(targetState)) {
                    return null;
                } else {
                    return state.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING) != targetState.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING) ? null : targetBE;
                }
            }
        }
    }

    public static List<MechanicalCrafterBlockEntity> getPrecedingCrafters(MechanicalCrafterBlockEntity crafter) {
        BlockPos pos = crafter.m_58899_();
        Level world = crafter.m_58904_();
        List<MechanicalCrafterBlockEntity> crafters = new ArrayList();
        BlockState blockState = crafter.m_58900_();
        if (!isCrafter(blockState)) {
            return crafters;
        } else {
            Direction blockFacing = (Direction) blockState.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING);
            Direction blockPointing = MechanicalCrafterBlock.getTargetDirection(blockState);
            for (Direction facing : Iterate.directions) {
                if (blockFacing.getAxis() != facing.getAxis() && blockPointing != facing) {
                    BlockPos neighbourPos = pos.relative(facing);
                    BlockState neighbourState = world.getBlockState(neighbourPos);
                    if (isCrafter(neighbourState) && MechanicalCrafterBlock.getTargetDirection(neighbourState) == facing.getOpposite() && blockFacing == neighbourState.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING)) {
                        MechanicalCrafterBlockEntity be = CrafterHelper.getCrafter(world, neighbourPos);
                        if (be != null) {
                            crafters.add(be);
                        }
                    }
                }
            }
            return crafters;
        }
    }

    private static boolean isCrafter(BlockState state) {
        return AllBlocks.MECHANICAL_CRAFTER.has(state);
    }

    public static ItemStack tryToApplyRecipe(Level world, RecipeGridHandler.GroupedItems items) {
        items.calcStats();
        CraftingContainer craftinginventory = new MechanicalCraftingInventory(items);
        ItemStack result = null;
        RegistryAccess registryAccess = world.registryAccess();
        if (AllConfigs.server().recipes.allowRegularCraftingInCrafter.get()) {
            result = (ItemStack) world.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftinginventory, world).filter(r -> isRecipeAllowed(r, craftinginventory)).map(r -> r.m_5874_(craftinginventory, registryAccess)).orElse(null);
        }
        if (result == null) {
            result = (ItemStack) AllRecipeTypes.MECHANICAL_CRAFTING.find(craftinginventory, world).map(r -> r.assemble(craftinginventory, registryAccess)).orElse(null);
        }
        return result;
    }

    public static boolean isRecipeAllowed(CraftingRecipe recipe, CraftingContainer inventory) {
        if (recipe instanceof FireworkRocketRecipe) {
            int numItems = 0;
            for (int i = 0; i < inventory.m_6643_(); i++) {
                if (!inventory.m_8020_(i).isEmpty()) {
                    numItems++;
                }
            }
            if (numItems > AllConfigs.server().recipes.maxFireworkIngredientsInCrafter.get()) {
                return false;
            }
        }
        return !AllRecipeTypes.shouldIgnoreInAutomation(recipe);
    }

    public static class GroupedItems {

        Map<Pair<Integer, Integer>, ItemStack> grid = new HashMap();

        int minX;

        int minY;

        int maxX;

        int maxY;

        int width;

        int height;

        boolean statsReady;

        public GroupedItems() {
        }

        public GroupedItems(ItemStack stack) {
            this.grid.put(Pair.of(0, 0), stack);
        }

        public void mergeOnto(RecipeGridHandler.GroupedItems other, Pointing pointing) {
            int xOffset = pointing == Pointing.LEFT ? 1 : (pointing == Pointing.RIGHT ? -1 : 0);
            int yOffset = pointing == Pointing.DOWN ? 1 : (pointing == Pointing.UP ? -1 : 0);
            this.grid.forEach((pair, stack) -> other.grid.put(Pair.of((Integer) pair.getKey() + xOffset, (Integer) pair.getValue() + yOffset), stack));
            other.statsReady = false;
        }

        public void write(CompoundTag nbt) {
            ListTag gridNBT = new ListTag();
            this.grid.forEach((pair, stack) -> {
                CompoundTag entry = new CompoundTag();
                entry.putInt("x", (Integer) pair.getKey());
                entry.putInt("y", (Integer) pair.getValue());
                entry.put("item", stack.serializeNBT());
                gridNBT.add(entry);
            });
            nbt.put("Grid", gridNBT);
        }

        public static RecipeGridHandler.GroupedItems read(CompoundTag nbt) {
            RecipeGridHandler.GroupedItems items = new RecipeGridHandler.GroupedItems();
            ListTag gridNBT = nbt.getList("Grid", 10);
            gridNBT.forEach(inbt -> {
                CompoundTag entry = (CompoundTag) inbt;
                int x = entry.getInt("x");
                int y = entry.getInt("y");
                ItemStack stack = ItemStack.of(entry.getCompound("item"));
                items.grid.put(Pair.of(x, y), stack);
            });
            return items;
        }

        public void calcStats() {
            if (!this.statsReady) {
                this.statsReady = true;
                this.minX = 0;
                this.minY = 0;
                this.maxX = 0;
                this.maxY = 0;
                for (Pair<Integer, Integer> pair : this.grid.keySet()) {
                    int x = (Integer) pair.getKey();
                    int y = (Integer) pair.getValue();
                    this.minX = Math.min(this.minX, x);
                    this.minY = Math.min(this.minY, y);
                    this.maxX = Math.max(this.maxX, x);
                    this.maxY = Math.max(this.maxY, y);
                }
                this.width = this.maxX - this.minX + 1;
                this.height = this.maxY - this.minY + 1;
            }
        }
    }
}