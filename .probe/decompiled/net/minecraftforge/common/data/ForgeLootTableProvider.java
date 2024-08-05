package net.minecraftforge.common.data;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.CompositeEntryBase;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.CompositeLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.loot.CanToolPerformAction;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public final class ForgeLootTableProvider extends LootTableProvider {

    public ForgeLootTableProvider(PackOutput packOutput) {
        super(packOutput, Set.of(), VanillaLootTableProvider.create(packOutput).getTables());
    }

    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationcontext) {
    }

    public List<LootTableProvider.SubProviderEntry> getTables() {
        return (List<LootTableProvider.SubProviderEntry>) super.getTables().stream().map(entry -> new LootTableProvider.SubProviderEntry(() -> this.replaceAndFilterChangesOnly((LootTableSubProvider) entry.provider().get()), entry.paramSet())).collect(Collectors.toList());
    }

    private LootTableSubProvider replaceAndFilterChangesOnly(LootTableSubProvider subProvider) {
        return newConsumer -> subProvider.generate((resourceLocation, builder) -> {
            if (this.findAndReplaceInLootTableBuilder(builder, Items.SHEARS, ToolActions.SHEARS_DIG)) {
                newConsumer.accept(resourceLocation, builder);
            }
        });
    }

    private boolean findAndReplaceInLootTableBuilder(LootTable.Builder builder, Item from, ToolAction toolAction) {
        List<LootPool> lootPools = (List<LootPool>) ObfuscationReflectionHelper.getPrivateValue(LootTable.Builder.class, builder, "f_79156_");
        boolean found = false;
        if (lootPools == null) {
            throw new IllegalStateException(LootTable.Builder.class.getName() + " is missing field f_79156_");
        } else {
            for (LootPool lootPool : lootPools) {
                if (this.findAndReplaceInLootPool(lootPool, from, toolAction)) {
                    found = true;
                }
            }
            return found;
        }
    }

    private boolean findAndReplaceInLootPool(LootPool lootPool, Item from, ToolAction toolAction) {
        LootPoolEntryContainer[] lootEntries = (LootPoolEntryContainer[]) ObfuscationReflectionHelper.getPrivateValue(LootPool.class, lootPool, "f_79023_");
        LootItemCondition[] lootConditions = (LootItemCondition[]) ObfuscationReflectionHelper.getPrivateValue(LootPool.class, lootPool, "f_79024_");
        boolean found = false;
        if (lootEntries == null) {
            throw new IllegalStateException(LootPool.class.getName() + " is missing field f_79023_");
        } else {
            for (LootPoolEntryContainer lootEntry : lootEntries) {
                if (this.findAndReplaceInLootEntry(lootEntry, from, toolAction)) {
                    found = true;
                }
                if (lootEntry instanceof CompositeEntryBase && this.findAndReplaceInParentedLootEntry((CompositeEntryBase) lootEntry, from, toolAction)) {
                    found = true;
                }
            }
            if (lootConditions == null) {
                throw new IllegalStateException(LootPool.class.getName() + " is missing field f_79024_");
            } else {
                for (int i = 0; i < lootConditions.length; i++) {
                    LootItemCondition lootCondition = lootConditions[i];
                    if (lootCondition instanceof MatchTool && this.checkMatchTool((MatchTool) lootCondition, from)) {
                        lootConditions[i] = CanToolPerformAction.canToolPerformAction(toolAction).build();
                        found = true;
                    } else if (lootCondition instanceof InvertedLootItemCondition) {
                        LootItemCondition invLootCondition = (LootItemCondition) ObfuscationReflectionHelper.getPrivateValue(InvertedLootItemCondition.class, (InvertedLootItemCondition) lootCondition, "f_81681_");
                        if (invLootCondition instanceof MatchTool && this.checkMatchTool((MatchTool) invLootCondition, from)) {
                            lootConditions[i] = InvertedLootItemCondition.invert(CanToolPerformAction.canToolPerformAction(toolAction)).build();
                            found = true;
                        } else if (invLootCondition instanceof CompositeLootItemCondition) {
                            CompositeLootItemCondition compositeLootItemCondition = (CompositeLootItemCondition) invLootCondition;
                            if (this.findAndReplaceInComposite(compositeLootItemCondition, from, toolAction)) {
                                found = true;
                            }
                        }
                    }
                }
                return found;
            }
        }
    }

    private boolean findAndReplaceInParentedLootEntry(CompositeEntryBase entry, Item from, ToolAction toolAction) {
        LootPoolEntryContainer[] lootEntries = (LootPoolEntryContainer[]) ObfuscationReflectionHelper.getPrivateValue(CompositeEntryBase.class, entry, "f_79428_");
        boolean found = false;
        if (lootEntries == null) {
            throw new IllegalStateException(CompositeEntryBase.class.getName() + " is missing field f_79428_");
        } else {
            for (LootPoolEntryContainer lootEntry : lootEntries) {
                if (this.findAndReplaceInLootEntry(lootEntry, from, toolAction)) {
                    found = true;
                }
            }
            return found;
        }
    }

    private boolean findAndReplaceInLootEntry(LootPoolEntryContainer entry, Item from, ToolAction toolAction) {
        LootItemCondition[] lootConditions = (LootItemCondition[]) ObfuscationReflectionHelper.getPrivateValue(LootPoolEntryContainer.class, entry, "f_79636_");
        boolean found = false;
        if (lootConditions == null) {
            throw new IllegalStateException(LootPoolEntryContainer.class.getName() + " is missing field f_79636_");
        } else {
            for (int i = 0; i < lootConditions.length; i++) {
                if (lootConditions[i] instanceof CompositeLootItemCondition composite && this.findAndReplaceInComposite(composite, from, toolAction)) {
                    found = true;
                    continue;
                }
                if (lootConditions[i] instanceof MatchTool && this.checkMatchTool((MatchTool) lootConditions[i], from)) {
                    lootConditions[i] = CanToolPerformAction.canToolPerformAction(toolAction).build();
                    found = true;
                }
            }
            return found;
        }
    }

    private boolean findAndReplaceInComposite(CompositeLootItemCondition alternative, Item from, ToolAction toolAction) {
        LootItemCondition[] lootConditions = (LootItemCondition[]) ObfuscationReflectionHelper.getPrivateValue(CompositeLootItemCondition.class, alternative, "f_285609_");
        boolean found = false;
        if (lootConditions == null) {
            throw new IllegalStateException(CompositeLootItemCondition.class.getName() + " is missing field f_285609_");
        } else {
            for (int i = 0; i < lootConditions.length; i++) {
                if (lootConditions[i] instanceof MatchTool && this.checkMatchTool((MatchTool) lootConditions[i], from)) {
                    lootConditions[i] = CanToolPerformAction.canToolPerformAction(toolAction).build();
                    found = true;
                }
            }
            return found;
        }
    }

    private boolean checkMatchTool(MatchTool lootCondition, Item expected) {
        ItemPredicate predicate = (ItemPredicate) ObfuscationReflectionHelper.getPrivateValue(MatchTool.class, lootCondition, "f_81993_");
        Set<Item> items = (Set<Item>) ObfuscationReflectionHelper.getPrivateValue(ItemPredicate.class, predicate, "f_151427_");
        return items != null && items.contains(expected);
    }
}