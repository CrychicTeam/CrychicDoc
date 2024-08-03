package net.minecraft.data.loot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public abstract class BlockLootSubProvider implements LootTableSubProvider {

    protected static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

    protected static final LootItemCondition.Builder HAS_NO_SILK_TOUCH = HAS_SILK_TOUCH.invert();

    protected static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));

    private static final LootItemCondition.Builder HAS_SHEARS_OR_SILK_TOUCH = HAS_SHEARS.or(HAS_SILK_TOUCH);

    private static final LootItemCondition.Builder HAS_NO_SHEARS_OR_SILK_TOUCH = HAS_SHEARS_OR_SILK_TOUCH.invert();

    protected final Set<Item> explosionResistant;

    protected final FeatureFlagSet enabledFeatures;

    protected final Map<ResourceLocation, LootTable.Builder> map;

    protected static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[] { 0.05F, 0.0625F, 0.083333336F, 0.1F };

    private static final float[] NORMAL_LEAVES_STICK_CHANCES = new float[] { 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F };

    protected BlockLootSubProvider(Set<Item> setItem0, FeatureFlagSet featureFlagSet1) {
        this(setItem0, featureFlagSet1, new HashMap());
    }

    protected BlockLootSubProvider(Set<Item> setItem0, FeatureFlagSet featureFlagSet1, Map<ResourceLocation, LootTable.Builder> mapResourceLocationLootTableBuilder2) {
        this.explosionResistant = setItem0;
        this.enabledFeatures = featureFlagSet1;
        this.map = mapResourceLocationLootTableBuilder2;
    }

    protected <T extends FunctionUserBuilder<T>> T applyExplosionDecay(ItemLike itemLike0, FunctionUserBuilder<T> functionUserBuilderT1) {
        return !this.explosionResistant.contains(itemLike0.asItem()) ? functionUserBuilderT1.apply(ApplyExplosionDecay.explosionDecay()) : functionUserBuilderT1.unwrap();
    }

    protected <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike itemLike0, ConditionUserBuilder<T> conditionUserBuilderT1) {
        return !this.explosionResistant.contains(itemLike0.asItem()) ? conditionUserBuilderT1.when(ExplosionCondition.survivesExplosion()) : conditionUserBuilderT1.unwrap();
    }

    public LootTable.Builder createSingleItemTable(ItemLike itemLike0) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(itemLike0, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(itemLike0))));
    }

    private static LootTable.Builder createSelfDropDispatchTable(Block block0, LootItemCondition.Builder lootItemConditionBuilder1, LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder2) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(((LootPoolSingletonContainer.Builder) LootItem.lootTableItem(block0).m_79080_(lootItemConditionBuilder1)).m_7170_(lootPoolEntryContainerBuilder2)));
    }

    protected static LootTable.Builder createSilkTouchDispatchTable(Block block0, LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder1) {
        return createSelfDropDispatchTable(block0, HAS_SILK_TOUCH, lootPoolEntryContainerBuilder1);
    }

    protected static LootTable.Builder createShearsDispatchTable(Block block0, LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder1) {
        return createSelfDropDispatchTable(block0, HAS_SHEARS, lootPoolEntryContainerBuilder1);
    }

    protected static LootTable.Builder createSilkTouchOrShearsDispatchTable(Block block0, LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder1) {
        return createSelfDropDispatchTable(block0, HAS_SHEARS_OR_SILK_TOUCH, lootPoolEntryContainerBuilder1);
    }

    protected LootTable.Builder createSingleItemTableWithSilkTouch(Block block0, ItemLike itemLike1) {
        return createSilkTouchDispatchTable(block0, (LootPoolEntryContainer.Builder<?>) this.applyExplosionCondition(block0, LootItem.lootTableItem(itemLike1)));
    }

    protected LootTable.Builder createSingleItemTable(ItemLike itemLike0, NumberProvider numberProvider1) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add((LootPoolEntryContainer.Builder<?>) this.applyExplosionDecay(itemLike0, LootItem.lootTableItem(itemLike0).apply(SetItemCountFunction.setCount(numberProvider1)))));
    }

    protected LootTable.Builder createSingleItemTableWithSilkTouch(Block block0, ItemLike itemLike1, NumberProvider numberProvider2) {
        return createSilkTouchDispatchTable(block0, (LootPoolEntryContainer.Builder<?>) this.applyExplosionDecay(block0, LootItem.lootTableItem(itemLike1).apply(SetItemCountFunction.setCount(numberProvider2))));
    }

    private static LootTable.Builder createSilkTouchOnlyTable(ItemLike itemLike0) {
        return LootTable.lootTable().withPool(LootPool.lootPool().when(HAS_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(itemLike0)));
    }

    private LootTable.Builder createPotFlowerItemTable(ItemLike itemLike0) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(Blocks.FLOWER_POT, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Blocks.FLOWER_POT)))).withPool(this.applyExplosionCondition(itemLike0, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(itemLike0))));
    }

    protected LootTable.Builder createSlabItemTable(Block block0) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add((LootPoolEntryContainer.Builder<?>) this.applyExplosionDecay(block0, LootItem.lootTableItem(block0).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))))));
    }

    protected <T extends Comparable<T> & StringRepresentable> LootTable.Builder createSinglePropConditionTable(Block block0, Property<T> propertyT1, T t2) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(block0, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block0).m_79080_(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(propertyT1, t2))))));
    }

    protected LootTable.Builder createNameableBlockEntityTable(Block block0) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(block0, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block0).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)))));
    }

    protected LootTable.Builder createShulkerBoxDrop(Block block0) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(block0, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block0).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Lock", "BlockEntityTag.Lock").copy("LootTable", "BlockEntityTag.LootTable").copy("LootTableSeed", "BlockEntityTag.LootTableSeed")).apply(SetContainerContents.setContents(BlockEntityType.SHULKER_BOX).withEntry(DynamicLoot.dynamicEntry(ShulkerBoxBlock.CONTENTS))))));
    }

    protected LootTable.Builder createCopperOreDrops(Block block0) {
        return createSilkTouchDispatchTable(block0, (LootPoolEntryContainer.Builder<?>) this.applyExplosionDecay(block0, LootItem.lootTableItem(Items.RAW_COPPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createLapisOreDrops(Block block0) {
        return createSilkTouchDispatchTable(block0, (LootPoolEntryContainer.Builder<?>) this.applyExplosionDecay(block0, LootItem.lootTableItem(Items.LAPIS_LAZULI).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createRedstoneOreDrops(Block block0) {
        return createSilkTouchDispatchTable(block0, (LootPoolEntryContainer.Builder<?>) this.applyExplosionDecay(block0, LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createBannerDrop(Block block0) {
        return LootTable.lootTable().withPool(this.applyExplosionCondition(block0, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block0).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Patterns", "BlockEntityTag.Patterns")))));
    }

    protected static LootTable.Builder createBeeNestDrop(Block block0) {
        return LootTable.lootTable().withPool(LootPool.lootPool().when(HAS_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block0).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Bees", "BlockEntityTag.Bees")).apply(CopyBlockState.copyState(block0).copy(BeehiveBlock.HONEY_LEVEL))));
    }

    protected static LootTable.Builder createBeeHiveDrop(Block block0) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(((LootPoolSingletonContainer.Builder) LootItem.lootTableItem(block0).m_79080_(HAS_SILK_TOUCH)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Bees", "BlockEntityTag.Bees")).apply(CopyBlockState.copyState(block0).copy(BeehiveBlock.HONEY_LEVEL)).m_7170_(LootItem.lootTableItem(block0))));
    }

    protected static LootTable.Builder createCaveVinesDrop(Block block0) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.GLOW_BERRIES)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CaveVines.BERRIES, true))));
    }

    protected LootTable.Builder createOreDrop(Block block0, Item item1) {
        return createSilkTouchDispatchTable(block0, (LootPoolEntryContainer.Builder<?>) this.applyExplosionDecay(block0, LootItem.lootTableItem(item1).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createMushroomBlockDrop(Block block0, ItemLike itemLike1) {
        return createSilkTouchDispatchTable(block0, (LootPoolEntryContainer.Builder<?>) this.applyExplosionDecay(block0, LootItem.lootTableItem(itemLike1).apply(SetItemCountFunction.setCount(UniformGenerator.between(-6.0F, 2.0F))).apply(LimitCount.limitCount(IntRange.lowerBound(0)))));
    }

    protected LootTable.Builder createGrassDrops(Block block0) {
        return createShearsDispatchTable(block0, this.applyExplosionDecay(block0, ((LootPoolSingletonContainer.Builder) LootItem.lootTableItem(Items.WHEAT_SEEDS).m_79080_(LootItemRandomChanceCondition.randomChance(0.125F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2))));
    }

    public LootTable.Builder createStemDrops(Block block0, Item item1) {
        return LootTable.lootTable().withPool(this.applyExplosionDecay(block0, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add((LootPoolEntryContainer.Builder<?>) LootItem.lootTableItem(item1).m_230984_(StemBlock.AGE.getPossibleValues(), p_249795_ -> SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, (float) (p_249795_ + 1) / 15.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StemBlock.AGE, p_249795_)))))));
    }

    public LootTable.Builder createAttachedStemDrops(Block block0, Item item1) {
        return LootTable.lootTable().withPool(this.applyExplosionDecay(block0, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(item1).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.53333336F))))));
    }

    protected static LootTable.Builder createShearsOnlyDrop(ItemLike itemLike0) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_SHEARS).add(LootItem.lootTableItem(itemLike0)));
    }

    protected LootTable.Builder createMultifaceBlockDrops(Block block0, LootItemCondition.Builder lootItemConditionBuilder1) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(this.applyExplosionDecay(block0, ((LootPoolSingletonContainer.Builder) ((LootPoolSingletonContainer.Builder) LootItem.lootTableItem(block0).m_79080_(lootItemConditionBuilder1)).m_230987_(Direction.values(), p_251536_ -> SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MultifaceBlock.getFaceProperty(p_251536_), true))))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))));
    }

    protected LootTable.Builder createLeavesDrops(Block block0, Block block1, float... float2) {
        return createSilkTouchOrShearsDispatchTable(block0, ((LootPoolSingletonContainer.Builder) this.applyExplosionCondition(block0, LootItem.lootTableItem(block1))).m_79080_(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, float2))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_NO_SHEARS_OR_SILK_TOUCH).add(((LootPoolSingletonContainer.Builder) this.applyExplosionDecay(block0, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))).m_79080_(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, NORMAL_LEAVES_STICK_CHANCES))));
    }

    protected LootTable.Builder createOakLeavesDrops(Block block0, Block block1, float... float2) {
        return this.createLeavesDrops(block0, block1, float2).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_NO_SHEARS_OR_SILK_TOUCH).add(((LootPoolSingletonContainer.Builder) this.applyExplosionCondition(block0, LootItem.lootTableItem(Items.APPLE))).m_79080_(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F))));
    }

    protected LootTable.Builder createMangroveLeavesDrops(Block block0) {
        return createSilkTouchOrShearsDispatchTable(block0, ((LootPoolSingletonContainer.Builder) this.applyExplosionDecay(Blocks.MANGROVE_LEAVES, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))).m_79080_(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, NORMAL_LEAVES_STICK_CHANCES)));
    }

    protected LootTable.Builder createCropDrops(Block block0, Item item1, Item item2, LootItemCondition.Builder lootItemConditionBuilder3) {
        return this.applyExplosionDecay(block0, LootTable.lootTable().withPool(LootPool.lootPool().add(((LootPoolSingletonContainer.Builder) LootItem.lootTableItem(item1).m_79080_(lootItemConditionBuilder3)).m_7170_(LootItem.lootTableItem(item2)))).withPool(LootPool.lootPool().when(lootItemConditionBuilder3).add(LootItem.lootTableItem(item2).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))));
    }

    protected static LootTable.Builder createDoublePlantShearsDrop(Block block0) {
        return LootTable.lootTable().withPool(LootPool.lootPool().when(HAS_SHEARS).add(LootItem.lootTableItem(block0).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))));
    }

    protected LootTable.Builder createDoublePlantWithSeedDrops(Block block0, Block block1) {
        LootPoolEntryContainer.Builder<?> $$2 = ((LootPoolSingletonContainer.Builder) LootItem.lootTableItem(block1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))).m_79080_(HAS_SHEARS)).m_7170_(((LootPoolSingletonContainer.Builder) this.applyExplosionCondition(block0, LootItem.lootTableItem(Items.WHEAT_SEEDS))).m_79080_(LootItemRandomChanceCondition.randomChance(0.125F)));
        return LootTable.lootTable().withPool(LootPool.lootPool().add($$2).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(block0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).build()).build()), new BlockPos(0, 1, 0)))).withPool(LootPool.lootPool().add($$2).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(block0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).build()).build()), new BlockPos(0, -1, 0))));
    }

    protected LootTable.Builder createCandleDrops(Block block0) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(block0, LootItem.lootTableItem(block0).m_230984_(List.of(2, 3, 4), p_249985_ -> SetItemCountFunction.setCount(ConstantValue.exactly((float) p_249985_.intValue())).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CandleBlock.CANDLES, p_249985_)))))));
    }

    protected LootTable.Builder createPetalsDrops(Block block0) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(block0, LootItem.lootTableItem(block0).m_230984_(IntStream.rangeClosed(1, 4).boxed().toList(), p_272348_ -> SetItemCountFunction.setCount(ConstantValue.exactly((float) p_272348_.intValue())).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block0).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PinkPetalsBlock.AMOUNT, p_272348_)))))));
    }

    protected static LootTable.Builder createCandleCakeDrops(Block block0) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block0)));
    }

    public static LootTable.Builder noDrop() {
        return LootTable.lootTable();
    }

    protected abstract void generate();

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumerResourceLocationLootTableBuilder0) {
        this.generate();
        Set<ResourceLocation> $$1 = new HashSet();
        for (Block $$2 : BuiltInRegistries.BLOCK) {
            if ($$2.m_245993_(this.enabledFeatures)) {
                ResourceLocation $$3 = $$2.m_60589_();
                if ($$3 != BuiltInLootTables.EMPTY && $$1.add($$3)) {
                    LootTable.Builder $$4 = (LootTable.Builder) this.map.remove($$3);
                    if ($$4 == null) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", $$3, BuiltInRegistries.BLOCK.getKey($$2)));
                    }
                    biConsumerResourceLocationLootTableBuilder0.accept($$3, $$4);
                }
            }
        }
        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
        }
    }

    protected void addNetherVinesDropTable(Block block0, Block block1) {
        LootTable.Builder $$2 = createSilkTouchOrShearsDispatchTable(block0, LootItem.lootTableItem(block0).m_79080_(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.33F, 0.55F, 0.77F, 1.0F)));
        this.add(block0, $$2);
        this.add(block1, $$2);
    }

    protected LootTable.Builder createDoorTable(Block block0) {
        return this.createSinglePropConditionTable(block0, DoorBlock.HALF, DoubleBlockHalf.LOWER);
    }

    protected void dropPottedContents(Block block0) {
        this.add(block0, p_250193_ -> this.createPotFlowerItemTable(((FlowerPotBlock) p_250193_).getContent()));
    }

    protected void otherWhenSilkTouch(Block block0, Block block1) {
        this.add(block0, createSilkTouchOnlyTable(block1));
    }

    protected void dropOther(Block block0, ItemLike itemLike1) {
        this.add(block0, this.createSingleItemTable(itemLike1));
    }

    protected void dropWhenSilkTouch(Block block0) {
        this.otherWhenSilkTouch(block0, block0);
    }

    protected void dropSelf(Block block0) {
        this.dropOther(block0, block0);
    }

    protected void add(Block block0, Function<Block, LootTable.Builder> functionBlockLootTableBuilder1) {
        this.add(block0, (LootTable.Builder) functionBlockLootTableBuilder1.apply(block0));
    }

    protected void add(Block block0, LootTable.Builder lootTableBuilder1) {
        this.map.put(block0.m_60589_(), lootTableBuilder1);
    }
}