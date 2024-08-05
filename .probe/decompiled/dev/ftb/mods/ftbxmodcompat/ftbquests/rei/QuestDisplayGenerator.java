package dev.ftb.mods.ftbxmodcompat.ftbquests.rei;

import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedQuestCache;
import java.util.List;
import java.util.Optional;
import me.shedaniel.rei.api.client.registry.display.DynamicDisplayGenerator;
import me.shedaniel.rei.api.client.view.ViewSearchBuilder;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.world.item.ItemStack;

public enum QuestDisplayGenerator implements DynamicDisplayGenerator<QuestDisplay> {

    INSTANCE;

    final WrappedQuestCache cache = new WrappedQuestCache();

    public void refresh() {
        this.cache.clear();
    }

    public Optional<List<QuestDisplay>> getRecipeFor(EntryStack<?> entry) {
        if (entry.getType() == VanillaEntryTypes.ITEM) {
            EntryStack<ItemStack> itemEntry = entry.cast();
            return Optional.of(this.cache.findQuestsWithOutput((ItemStack) itemEntry.getValue()).stream().map(QuestDisplay::new).toList());
        } else {
            return Optional.empty();
        }
    }

    public Optional<List<QuestDisplay>> getUsageFor(EntryStack<?> entry) {
        if (entry.getType() == VanillaEntryTypes.ITEM) {
            EntryStack<ItemStack> itemEntry = entry.cast();
            return Optional.of(this.cache.findQuestsWithInput((ItemStack) itemEntry.getValue()).stream().map(QuestDisplay::new).toList());
        } else {
            return Optional.empty();
        }
    }

    public Optional<List<QuestDisplay>> generate(ViewSearchBuilder builder) {
        return Optional.empty();
    }
}