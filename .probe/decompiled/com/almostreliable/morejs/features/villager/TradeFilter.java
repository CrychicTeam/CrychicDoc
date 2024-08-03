package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.util.TriConsumer;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class TradeFilter {

    private final Ingredient firstMatcher;

    private final Ingredient secondMatcher;

    private final Ingredient outputMatcher;

    private IntRange merchantLevelMatch = IntRange.all();

    private IntRange firstCountMatcher = IntRange.all();

    private IntRange secondCountMatcher = IntRange.all();

    private IntRange outputCountMatcher = IntRange.all();

    private TriConsumer<ItemStack, ItemStack, ItemStack> onMatch = ($1, $2, $3) -> {
    };

    @Nullable
    private Set<TradeTypes> tradeTypes;

    @Nullable
    private Set<VillagerProfession> professions;

    public TradeFilter(Ingredient firstMatcher, Ingredient secondMatcher, Ingredient outputMatcher) {
        this.firstMatcher = firstMatcher;
        this.secondMatcher = secondMatcher;
        this.outputMatcher = outputMatcher;
    }

    public void onMatch(TriConsumer<ItemStack, ItemStack, ItemStack> onMatch) {
        this.onMatch = onMatch;
    }

    public void setMerchantLevelMatcher(IntRange merchantLevelMatch) {
        this.merchantLevelMatch = merchantLevelMatch;
    }

    public void setFirstCountMatcher(IntRange firstCountMatcher) {
        this.firstCountMatcher = firstCountMatcher;
    }

    public void setSecondCountMatcher(IntRange secondCountMatcher) {
        this.secondCountMatcher = secondCountMatcher;
    }

    public void setOutputCountMatcher(IntRange outputCountMatcher) {
        this.outputCountMatcher = outputCountMatcher;
    }

    public void setTradeTypes(Set<TradeTypes> tradeTypes) {
        this.tradeTypes = tradeTypes;
    }

    public void setProfessions(@Nullable Set<VillagerProfession> professions) {
        this.professions = professions;
    }

    public boolean matchMerchantLevel(int level) {
        return this.merchantLevelMatch.test(level);
    }

    public boolean matchProfession(VillagerProfession profession) {
        return this.professions == null || this.professions.contains(profession);
    }

    public boolean matchType(TradeTypes type) {
        return this.tradeTypes == null || this.tradeTypes.contains(type);
    }

    public boolean match(ItemStack first, ItemStack second, ItemStack output, TradeTypes type) {
        boolean firstMatch = this.firstMatcher.test(first) && this.firstCountMatcher.test(first.getCount());
        boolean secondMatch = this.secondMatcher.test(second) && this.secondCountMatcher.test(second.getCount());
        boolean outputMatch = this.outputMatcher.test(output) && this.outputCountMatcher.test(output.getCount());
        boolean matched = this.matchType(type) && firstMatch && secondMatch && outputMatch;
        if (matched) {
            this.onMatch.accept(first, second, output);
        }
        return matched;
    }

    public boolean match(ItemStack first, ItemStack output, TradeTypes type) {
        return this.match(first, ItemStack.EMPTY, output, type);
    }

    public interface Filterable {

        default boolean matchesTradeFilter(TradeFilter filter) {
            return false;
        }
    }
}