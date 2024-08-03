package net.minecraftforge.common.data;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public final class ForgeBlockTagsProvider extends BlockTagsProvider {

    public ForgeBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "forge", existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider holderLookupProvider0) {
        this.m_206424_(Tags.Blocks.BARRELS).addTag(Tags.Blocks.BARRELS_WOODEN);
        this.m_206424_(Tags.Blocks.BARRELS_WOODEN).add(Blocks.BARREL);
        this.m_206424_(Tags.Blocks.BOOKSHELVES).add(Blocks.BOOKSHELF);
        this.m_206424_(Tags.Blocks.CHESTS).addTags(new TagKey[] { Tags.Blocks.CHESTS_ENDER, Tags.Blocks.CHESTS_TRAPPED, Tags.Blocks.CHESTS_WOODEN });
        this.m_206424_(Tags.Blocks.CHESTS_ENDER).add(Blocks.ENDER_CHEST);
        this.m_206424_(Tags.Blocks.CHESTS_TRAPPED).add(Blocks.TRAPPED_CHEST);
        this.m_206424_(Tags.Blocks.CHESTS_WOODEN).add(Blocks.CHEST, Blocks.TRAPPED_CHEST);
        this.m_206424_(Tags.Blocks.COBBLESTONE).addTags(new TagKey[] { Tags.Blocks.COBBLESTONE_NORMAL, Tags.Blocks.COBBLESTONE_INFESTED, Tags.Blocks.COBBLESTONE_MOSSY, Tags.Blocks.COBBLESTONE_DEEPSLATE });
        this.m_206424_(Tags.Blocks.COBBLESTONE_NORMAL).add(Blocks.COBBLESTONE);
        this.m_206424_(Tags.Blocks.COBBLESTONE_INFESTED).add(Blocks.INFESTED_COBBLESTONE);
        this.m_206424_(Tags.Blocks.COBBLESTONE_MOSSY).add(Blocks.MOSSY_COBBLESTONE);
        this.m_206424_(Tags.Blocks.COBBLESTONE_DEEPSLATE).add(Blocks.COBBLED_DEEPSLATE);
        this.m_206424_(Tags.Blocks.END_STONES).add(Blocks.END_STONE);
        this.m_206424_(Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST);
        this.m_206424_(Tags.Blocks.FENCE_GATES).addTags(new TagKey[] { Tags.Blocks.FENCE_GATES_WOODEN });
        this.m_206424_(Tags.Blocks.FENCE_GATES_WOODEN).add(Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE, Blocks.WARPED_FENCE_GATE, Blocks.MANGROVE_FENCE_GATE, Blocks.BAMBOO_FENCE_GATE, Blocks.CHERRY_FENCE_GATE);
        this.m_206424_(Tags.Blocks.FENCES).addTags(new TagKey[] { Tags.Blocks.FENCES_NETHER_BRICK, Tags.Blocks.FENCES_WOODEN });
        this.m_206424_(Tags.Blocks.FENCES_NETHER_BRICK).add(Blocks.NETHER_BRICK_FENCE);
        this.m_206424_(Tags.Blocks.FENCES_WOODEN).addTag(BlockTags.WOODEN_FENCES);
        this.m_206424_(Tags.Blocks.GLASS).addTags(new TagKey[] { Tags.Blocks.GLASS_COLORLESS, Tags.Blocks.STAINED_GLASS, Tags.Blocks.GLASS_TINTED });
        this.m_206424_(Tags.Blocks.GLASS_COLORLESS).add(Blocks.GLASS);
        this.m_206424_(Tags.Blocks.GLASS_SILICA).add(Blocks.GLASS, Blocks.BLACK_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.WHITE_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS);
        this.m_206424_(Tags.Blocks.GLASS_TINTED).add(Blocks.TINTED_GLASS);
        this.addColored(this.m_206424_(Tags.Blocks.STAINED_GLASS)::m_255245_, Tags.Blocks.GLASS, "{color}_stained_glass");
        this.m_206424_(Tags.Blocks.GLASS_PANES).addTags(new TagKey[] { Tags.Blocks.GLASS_PANES_COLORLESS, Tags.Blocks.STAINED_GLASS_PANES });
        this.m_206424_(Tags.Blocks.GLASS_PANES_COLORLESS).add(Blocks.GLASS_PANE);
        this.addColored(this.m_206424_(Tags.Blocks.STAINED_GLASS_PANES)::m_255245_, Tags.Blocks.GLASS_PANES, "{color}_stained_glass_pane");
        this.m_206424_(Tags.Blocks.GRAVEL).add(Blocks.GRAVEL);
        this.m_206424_(Tags.Blocks.NETHERRACK).add(Blocks.NETHERRACK);
        this.m_206424_(Tags.Blocks.OBSIDIAN).add(Blocks.OBSIDIAN);
        this.m_206424_(Tags.Blocks.ORE_BEARING_GROUND_DEEPSLATE).add(Blocks.DEEPSLATE);
        this.m_206424_(Tags.Blocks.ORE_BEARING_GROUND_NETHERRACK).add(Blocks.NETHERRACK);
        this.m_206424_(Tags.Blocks.ORE_BEARING_GROUND_STONE).add(Blocks.STONE);
        this.m_206424_(Tags.Blocks.ORE_RATES_DENSE).add(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE);
        this.m_206424_(Tags.Blocks.ORE_RATES_SINGULAR).add(Blocks.ANCIENT_DEBRIS, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.IRON_ORE, Blocks.NETHER_QUARTZ_ORE);
        this.m_206424_(Tags.Blocks.ORE_RATES_SPARSE).add(Blocks.NETHER_GOLD_ORE);
        this.m_206424_(Tags.Blocks.ORES).addTags(new TagKey[] { Tags.Blocks.ORES_COAL, Tags.Blocks.ORES_COPPER, Tags.Blocks.ORES_DIAMOND, Tags.Blocks.ORES_EMERALD, Tags.Blocks.ORES_GOLD, Tags.Blocks.ORES_IRON, Tags.Blocks.ORES_LAPIS, Tags.Blocks.ORES_REDSTONE, Tags.Blocks.ORES_QUARTZ, Tags.Blocks.ORES_NETHERITE_SCRAP });
        this.m_206424_(Tags.Blocks.ORES_COAL).addTag(BlockTags.COAL_ORES);
        this.m_206424_(Tags.Blocks.ORES_COPPER).addTag(BlockTags.COPPER_ORES);
        this.m_206424_(Tags.Blocks.ORES_DIAMOND).addTag(BlockTags.DIAMOND_ORES);
        this.m_206424_(Tags.Blocks.ORES_EMERALD).addTag(BlockTags.EMERALD_ORES);
        this.m_206424_(Tags.Blocks.ORES_GOLD).addTag(BlockTags.GOLD_ORES);
        this.m_206424_(Tags.Blocks.ORES_IRON).addTag(BlockTags.IRON_ORES);
        this.m_206424_(Tags.Blocks.ORES_LAPIS).addTag(BlockTags.LAPIS_ORES);
        this.m_206424_(Tags.Blocks.ORES_QUARTZ).add(Blocks.NETHER_QUARTZ_ORE);
        this.m_206424_(Tags.Blocks.ORES_REDSTONE).addTag(BlockTags.REDSTONE_ORES);
        this.m_206424_(Tags.Blocks.ORES_NETHERITE_SCRAP).add(Blocks.ANCIENT_DEBRIS);
        this.m_206424_(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE);
        this.m_206424_(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE);
        this.m_206424_(Tags.Blocks.ORES_IN_GROUND_STONE).add(Blocks.COAL_ORE, Blocks.COPPER_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE);
        this.m_206424_(Tags.Blocks.SAND).addTags(new TagKey[] { Tags.Blocks.SAND_COLORLESS, Tags.Blocks.SAND_RED });
        this.m_206424_(Tags.Blocks.SAND_COLORLESS).add(Blocks.SAND);
        this.m_206424_(Tags.Blocks.SAND_RED).add(Blocks.RED_SAND);
        this.m_206424_(Tags.Blocks.SANDSTONE).add(Blocks.SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE);
        this.m_206424_(Tags.Blocks.STONE).add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.INFESTED_STONE, Blocks.STONE, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_DIORITE, Blocks.POLISHED_GRANITE, Blocks.DEEPSLATE, Blocks.POLISHED_DEEPSLATE, Blocks.INFESTED_DEEPSLATE, Blocks.TUFF);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS).addTags(new TagKey[] { Tags.Blocks.STORAGE_BLOCKS_AMETHYST, Tags.Blocks.STORAGE_BLOCKS_COAL, Tags.Blocks.STORAGE_BLOCKS_COPPER, Tags.Blocks.STORAGE_BLOCKS_DIAMOND, Tags.Blocks.STORAGE_BLOCKS_EMERALD, Tags.Blocks.STORAGE_BLOCKS_GOLD, Tags.Blocks.STORAGE_BLOCKS_IRON, Tags.Blocks.STORAGE_BLOCKS_LAPIS, Tags.Blocks.STORAGE_BLOCKS_QUARTZ, Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER, Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD, Tags.Blocks.STORAGE_BLOCKS_RAW_IRON, Tags.Blocks.STORAGE_BLOCKS_REDSTONE, Tags.Blocks.STORAGE_BLOCKS_NETHERITE });
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_AMETHYST).add(Blocks.AMETHYST_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_COAL).add(Blocks.COAL_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_COPPER).add(Blocks.COPPER_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_DIAMOND).add(Blocks.DIAMOND_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_EMERALD).add(Blocks.EMERALD_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_GOLD).add(Blocks.GOLD_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_IRON).add(Blocks.IRON_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_LAPIS).add(Blocks.LAPIS_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_QUARTZ).add(Blocks.QUARTZ_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER).add(Blocks.RAW_COPPER_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD).add(Blocks.RAW_GOLD_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_RAW_IRON).add(Blocks.RAW_IRON_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_REDSTONE).add(Blocks.REDSTONE_BLOCK);
        this.m_206424_(Tags.Blocks.STORAGE_BLOCKS_NETHERITE).add(Blocks.NETHERITE_BLOCK);
    }

    private void addColored(Consumer<Block> consumer, TagKey<Block> group, String pattern) {
        String prefix = group.location().getPath().toUpperCase(Locale.ENGLISH) + "_";
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation key = new ResourceLocation("minecraft", pattern.replace("{color}", color.getName()));
            TagKey<Block> tag = this.getForgeTag(prefix + color.getName());
            Block block = ForgeRegistries.BLOCKS.getValue(key);
            if (block == null || block == Blocks.AIR) {
                throw new IllegalStateException("Unknown vanilla block: " + key.toString());
            }
            this.m_206424_(tag).add(block);
            consumer.accept(block);
        }
    }

    private TagKey<Block> getForgeTag(String name) {
        try {
            name = name.toUpperCase(Locale.ENGLISH);
            return (TagKey<Block>) Tags.Blocks.class.getDeclaredField(name).get(null);
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException var3) {
            throw new IllegalStateException(Tags.Blocks.class.getName() + " is missing tag name: " + name);
        }
    }

    @Override
    public String getName() {
        return "Forge Block Tags";
    }
}