package dev.ftb.mods.ftbxmodcompat.ftbquests.rei;

import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedLootCrateCache;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import me.shedaniel.rei.api.client.registry.display.DynamicDisplayGenerator;
import me.shedaniel.rei.api.client.view.ViewSearchBuilder;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.world.item.ItemStack;

public enum LootCrateDisplayGenerator implements DynamicDisplayGenerator<LootCrateDisplay> {

    INSTANCE;

    private final WrappedLootCrateCache cache = new WrappedLootCrateCache(crates -> {
    }, crates -> FTBQuestsREIIntegration.onLootCratesChanged());

    public Optional<List<LootCrateDisplay>> getRecipeFor(EntryStack<?> entry) {
        if (entry.getType() == VanillaEntryTypes.ITEM) {
            EntryStack<ItemStack> itemEntry = entry.cast();
            return Optional.of(this.cache.findCratesWithOutput((ItemStack) itemEntry.getValue()).stream().map(LootCrateDisplay::new).toList());
        } else {
            return Optional.empty();
        }
    }

    public Optional<List<LootCrateDisplay>> getUsageFor(EntryStack<?> entry) {
        if (entry.getType() == VanillaEntryTypes.ITEM) {
            EntryStack<ItemStack> itemEntry = entry.cast();
            return Optional.of(this.cache.findCratesWithInput((ItemStack) itemEntry.getValue()).stream().map(LootCrateDisplay::new).toList());
        } else {
            return Optional.empty();
        }
    }

    public Optional<List<LootCrateDisplay>> generate(ViewSearchBuilder builder) {
        return super.generate(builder);
    }

    public Collection<EntryStack<?>> getCrateEntryStacks() {
        return (Collection<EntryStack<?>>) this.cache.knownCrateStacks().stream().map(itemStack -> EntryStack.of(VanillaEntryTypes.ITEM, itemStack)).collect(Collectors.toCollection(ArrayList::new));
    }

    public void refresh() {
        this.cache.refresh();
    }
}