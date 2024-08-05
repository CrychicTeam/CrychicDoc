package vectorwing.farmersdelight.data.loot;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import vectorwing.farmersdelight.common.loot.function.CopyMealFunction;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class FDBlockLoot extends BlockLootSubProvider {

    private final Set<Block> generatedLootTables = new HashSet();

    public FDBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.m_245724_(ModBlocks.STOVE.get());
        this.dropNamedContainer(ModBlocks.BASKET.get());
        this.m_246481_(ModBlocks.COOKING_POT.get(), block -> LootTable.lootTable().withPool((LootPool.Builder) this.m_247733_(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyMealFunction.builder())))));
        this.m_245724_(ModBlocks.CUTTING_BOARD.get());
        this.m_245724_(ModBlocks.CARROT_CRATE.get());
        this.m_245724_(ModBlocks.POTATO_CRATE.get());
        this.m_245724_(ModBlocks.BEETROOT_CRATE.get());
        this.m_245724_(ModBlocks.CABBAGE_CRATE.get());
        this.m_245724_(ModBlocks.TOMATO_CRATE.get());
        this.m_245724_(ModBlocks.ONION_CRATE.get());
        this.m_245724_(ModBlocks.RICE_BALE.get());
        this.m_245724_(ModBlocks.RICE_BAG.get());
        this.m_245724_(ModBlocks.STRAW_BALE.get());
        this.m_245724_(ModBlocks.ROPE.get());
        this.m_245724_(ModBlocks.SAFETY_NET.get());
        this.m_245724_(ModBlocks.HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.WHITE_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.ORANGE_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.MAGENTA_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.LIGHT_BLUE_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.YELLOW_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.LIME_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.PINK_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.GRAY_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.LIGHT_GRAY_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.CYAN_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.PURPLE_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.BLUE_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.BROWN_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.GREEN_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.RED_HANGING_CANVAS_SIGN.get());
        this.m_245724_(ModBlocks.BLACK_HANGING_CANVAS_SIGN.get());
        this.dropNamedContainer(ModBlocks.OAK_CABINET.get());
        this.dropNamedContainer(ModBlocks.SPRUCE_CABINET.get());
        this.dropNamedContainer(ModBlocks.BIRCH_CABINET.get());
        this.dropNamedContainer(ModBlocks.JUNGLE_CABINET.get());
        this.dropNamedContainer(ModBlocks.ACACIA_CABINET.get());
        this.dropNamedContainer(ModBlocks.DARK_OAK_CABINET.get());
        this.dropNamedContainer(ModBlocks.MANGROVE_CABINET.get());
        this.dropNamedContainer(ModBlocks.BAMBOO_CABINET.get());
        this.dropNamedContainer(ModBlocks.CHERRY_CABINET.get());
        this.dropNamedContainer(ModBlocks.CRIMSON_CABINET.get());
        this.dropNamedContainer(ModBlocks.WARPED_CABINET.get());
        this.m_245724_(ModBlocks.CANVAS_RUG.get());
        this.m_245724_(ModBlocks.TATAMI.get());
        this.m_245724_(ModBlocks.HALF_TATAMI_MAT.get());
        this.m_245724_(ModBlocks.ORGANIC_COMPOST.get());
        this.m_245724_(ModBlocks.RICH_SOIL.get());
        this.m_246125_(ModBlocks.RICH_SOIL_FARMLAND.get(), ModBlocks.RICH_SOIL.get());
    }

    protected void dropNamedContainer(Block block) {
        this.m_246481_(block, x$0 -> this.m_246180_(x$0));
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        this.generatedLootTables.add(block);
        this.f_244441_.put(block.m_60589_(), builder);
    }

    protected Iterable<Block> getKnownBlocks() {
        return this.generatedLootTables;
    }
}