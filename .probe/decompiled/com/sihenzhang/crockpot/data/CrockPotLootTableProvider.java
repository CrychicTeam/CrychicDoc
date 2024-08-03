package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import com.sihenzhang.crockpot.block.AbstractCrockPotDoubleCropBlock;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.block.food.AbstractStackableFoodBlock;
import com.sihenzhang.crockpot.entity.CrockPotEntities;
import com.sihenzhang.crockpot.item.CrockPotItems;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.data.loot.packs.VanillaEntityLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

public class CrockPotLootTableProvider extends LootTableProvider {

    public CrockPotLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(CrockPotLootTableProvider.CrockPotBlockLoot::new, LootContextParamSets.BLOCK), new LootTableProvider.SubProviderEntry(CrockPotLootTableProvider.CrockPotEntityLoot::new, LootContextParamSets.ENTITY)));
    }

    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {
        map.forEach((name, table) -> table.validate(validationTracker));
    }

    public static class CrockPotBlockLoot extends VanillaBlockLoot {

        @Override
        protected void generate() {
            this.m_247577_(CrockPotBlocks.CROCK_POT.get(), this.m_245142_(CrockPotBlocks.CROCK_POT.get(), Blocks.STONE, ConstantValue.exactly(6.0F)));
            this.m_245724_(CrockPotBlocks.PORTABLE_CROCK_POT.get());
            this.m_247577_(CrockPotBlocks.BIRDCAGE.get(), this.m_247398_(CrockPotBlocks.BIRDCAGE.get()));
            this.m_245724_(CrockPotBlocks.UNKNOWN_CROPS.get());
            this.m_247577_(CrockPotBlocks.ASPARAGUS.get(), this.createCropDropsWithSeed(CrockPotBlocks.ASPARAGUS.get(), CrockPotItems.ASPARAGUS.get(), CrockPotItems.ASPARAGUS_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.ASPARAGUS.get(), AbstractCrockPotCropBlock.f_52244_, 7)));
            this.m_247577_(CrockPotBlocks.EGGPLANT.get(), this.createCropDropsWithSeed(CrockPotBlocks.EGGPLANT.get(), CrockPotItems.EGGPLANT.get(), CrockPotItems.EGGPLANT_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.EGGPLANT.get(), AbstractCrockPotCropBlock.f_52244_, 7)));
            this.m_247577_(CrockPotBlocks.GARLIC.get(), this.createCropDropsWithSeed(CrockPotBlocks.GARLIC.get(), CrockPotItems.GARLIC.get(), CrockPotItems.GARLIC_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.GARLIC.get(), AbstractCrockPotCropBlock.f_52244_, 7)));
            this.m_247577_(CrockPotBlocks.ONION.get(), this.createCropDropsWithSeed(CrockPotBlocks.ONION.get(), CrockPotItems.ONION.get(), CrockPotItems.ONION_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.ONION.get(), AbstractCrockPotCropBlock.f_52244_, 7)));
            this.m_247577_(CrockPotBlocks.PEPPER.get(), this.createCropDropsWithSeed(CrockPotBlocks.PEPPER.get(), CrockPotItems.PEPPER.get(), CrockPotItems.PEPPER_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.PEPPER.get(), AbstractCrockPotCropBlock.f_52244_, 7)));
            this.m_247577_(CrockPotBlocks.TOMATO.get(), this.createCropDropsWithSeed(CrockPotBlocks.TOMATO.get(), CrockPotItems.TOMATO.get(), CrockPotItems.TOMATO_SEEDS.get(), blockStatePropertyCondition(CrockPotBlocks.TOMATO.get(), AbstractCrockPotCropBlock.f_52244_, 7)));
            ((List) CrockPotBlocks.FOODS.get()).forEach(this::dropFood);
        }

        protected LootTable.Builder createCropDropsWithSeed(Block pCropBlock, Item pGrownCropItem, Item pSeedsItem, LootItemCondition.Builder pDropGrownCropCondition) {
            return LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(pSeedsItem))).withPool(LootPool.lootPool().when(pDropGrownCropCondition).add((LootPoolEntryContainer.Builder<?>) this.m_246108_(pGrownCropItem, LootItem.lootTableItem(pGrownCropItem).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))));
        }

        protected static LootItemCondition.Builder blockStatePropertyCondition(Block pBlock, Property<Integer> pProperty, int pValue) {
            return LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(pProperty, pValue));
        }

        protected void dropFood(Block block) {
            if (block instanceof AbstractStackableFoodBlock stackableFoodBlock) {
                LootTable.Builder lootTable = LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add((LootPoolEntryContainer.Builder<?>) this.m_246108_(stackableFoodBlock, LootItem.lootTableItem(stackableFoodBlock).m_230984_(List.of(1, 2, 3, 4, 5, 6), p_249985_ -> SetItemCountFunction.setCount(ConstantValue.exactly((float) p_249985_.intValue())).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(stackableFoodBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(stackableFoodBlock.getStacksProperty(), p_249985_)))))));
                this.m_247577_(block, lootTable);
            } else {
                this.m_245724_(block);
            }
        }

        protected Iterable<Block> getKnownBlocks() {
            return CrockPotBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> !(block instanceof AbstractCrockPotDoubleCropBlock)).toList();
        }
    }

    public static class CrockPotEntityLoot extends VanillaEntityLoot {

        @Override
        public void generate() {
            this.m_245309_(CrockPotEntities.VOLT_GOAT.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(CrockPotItems.VOLT_GOAT_HORN.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(-2.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)).setLimit(2)))));
        }

        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return Stream.of(CrockPotEntities.VOLT_GOAT.get());
        }
    }
}