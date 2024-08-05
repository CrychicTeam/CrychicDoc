package harmonised.pmmo.setup.datagen;

import harmonised.pmmo.features.loot_modifiers.RareDropModifier;
import harmonised.pmmo.features.loot_modifiers.SkillLootConditionKill;
import harmonised.pmmo.features.loot_modifiers.SkillLootConditionPlayer;
import harmonised.pmmo.features.loot_modifiers.TreasureLootModifier;
import harmonised.pmmo.features.loot_modifiers.ValidBlockCondition;
import harmonised.pmmo.util.RegistryUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class GLMProvider extends GlobalLootModifierProvider {

    public GLMProvider(PackOutput gen) {
        super(gen, "pmmo");
    }

    @Override
    protected void start() {
        this.add("coal_ores", this.of(Tags.Blocks.ORES_COAL, Items.DIAMOND, 1, 0.01, "mining", 30));
        this.add("apple_from_leaves", this.of(BlockTags.LEAVES, Items.APPLE, 1, 0.025, "farming", 30));
        this.add("gapple_from_leaves", this.of(BlockTags.LEAVES, Items.GOLDEN_APPLE, 1, 0.01, "farming", 60));
        this.add("egapple_from_leaves", this.of(BlockTags.LEAVES, Items.ENCHANTED_GOLDEN_APPLE, 1, 0.01, "farming", 60));
        this.add("dirt_cake", this.of(BlockTags.DIRT, Items.CAKE, 1, 0.003251, "excavation", 0));
        this.add("dirt_bone", this.of(BlockTags.DIRT, Items.BONE, 1, 0.01, "excavation", 25));
        this.add("iron_deposits", this.of(Tags.Blocks.STONE, Items.IRON_NUGGET, 2, 0.001, "mining", 15));
        this.add("pig_step", this.of(Tags.Blocks.BOOKSHELVES, Items.MUSIC_DISC_PIGSTEP, 1, 0.005, "building", 10));
        this.add("grass_grass", this.of(Blocks.GRASS_BLOCK, Items.GRASS, 1, 0.01, "farming", 5));
        this.add("magma_to_cream", this.of(Blocks.MAGMA_BLOCK, Items.MAGMA_CREAM, 1, 0.01, "mining", 25));
        this.add("nether_gold", this.of(Tags.Blocks.NETHERRACK, Items.GOLD_NUGGET, 2, 0.01, "mining", 20));
        this.add("berry_surprise", this.of(Blocks.PODZOL, Items.SWEET_BERRIES, 2, 0.0, "farming", 15));
        this.add("tears_of_sand", this.of(Blocks.SOUL_SAND, Items.GHAST_TEAR, 1, 0.001, "excavation", 40));
        this.add("tears_of_soil", this.of(Blocks.SOUL_SOIL, Items.GHAST_TEAR, 1, 0.001, "excavation", 40));
        this.add("extra_logs", this.extra(BlockTags.LOGS, 1, 0.01, "woodcutting", 1));
        this.add("extra_coal", this.extra(Tags.Blocks.ORES_COAL, 1, 0.01, "mining", 1));
        this.add("extra_copper", this.extra(Tags.Blocks.ORES_COPPER, 1, 0.01, "mining", 5));
        this.add("extra_diamond", this.extra(Tags.Blocks.ORES_DIAMOND, 1, 0.0033, "mining", 30));
        this.add("extra_emerald", this.extra(Tags.Blocks.ORES_EMERALD, 1, 0.0075, "mining", 20));
        this.add("extra_debris", this.of(Blocks.ANCIENT_DEBRIS, Items.ANCIENT_DEBRIS, 1, 0.01, "mining", 60));
        this.add("extra_gold", this.extra(Tags.Blocks.ORES_GOLD, 1, 0.005, "mining", 25));
        this.add("extra_iron", this.extra(Tags.Blocks.ORES_IRON, 1, 0.05, "mining", 20));
        this.add("extra_lapis", this.extra(Tags.Blocks.ORES_LAPIS, 1, 0.0015, "mining", 30));
        this.add("extra_quartz", this.extra(Tags.Blocks.ORES_QUARTZ, 1, 0.005, "mining", 30));
        this.add("extra_redstone", this.extra(Tags.Blocks.ORES_REDSTONE, 3, 0.02, "mining", 20));
        this.add("extra_bamboo", this.of(Blocks.BAMBOO, Items.BAMBOO, 1, 0.0035, "farming", 20));
        this.add("extra_beets", this.of(Blocks.BEETROOTS, Items.BEETROOT, 1, 0.001, "farming", 1));
        this.add("extra_cactus", this.of(Blocks.CACTUS, Items.CACTUS, 1, 0.0045, "farming", 10));
        this.add("extra_carrot", this.of(Blocks.CARROTS, Items.CARROT, 1, 0.015, "farming", 1));
        this.add("extra_cocoa", this.of(Blocks.COCOA, Items.COCOA_BEANS, 1, 0.015, "farming", 15));
        this.add("extra_kelp", this.of(Blocks.KELP, Items.KELP, 1, 0.025, "farming", 10));
        this.add("extra_kelp_plant", this.of(Blocks.KELP_PLANT, Items.KELP, 1, 0.025, "farming", 10));
        this.add("extra_melon", this.of(Blocks.MELON, Items.MELON_SLICE, 1, 0.01, "farming", 20));
        this.add("extra_wart", this.of(Blocks.NETHER_WART, Items.NETHER_WART, 1, 0.075, "farming", 50));
        this.add("extra_potato", this.of(Blocks.POTATOES, Items.POTATO, 1, 0.015, "farming", 1));
        this.add("extra_pumpkin", this.of(Blocks.PUMPKIN, Items.PUMPKIN, 1, 0.006, "farming", 20));
        this.add("extra_pickle", this.of(Blocks.SEA_PICKLE, Items.SEA_PICKLE, 1, 0.015, "farming", 10));
        this.add("extra_sugar", this.of(Blocks.SUGAR_CANE, Items.SUGAR_CANE, 1, 0.033, "farming", 5));
        this.add("extra_wheat", this.of(Blocks.WHEAT, Items.WHEAT, 1, 0.05, "farming", 1));
        this.add("fish_bow", this.fish(Items.BOW, 1, 0.01, "fishing", 10));
        this.add("fish_rod", this.fish(Items.FISHING_ROD, 1, 0.05, "fishing", 1));
        this.add("fish_heart", this.fish(Items.HEART_OF_THE_SEA, 1, 0.001, "fishing", 30));
        this.add("fish_nautilus", this.fish(Items.NAUTILUS_SHELL, 1, 0.005, "fishing", 25));
        this.add("fish_star", this.fish(Items.NETHER_STAR, 1, 1.0E-4, "fishing", 60));
        this.add("fish_chain_boots", this.fish(Items.CHAINMAIL_BOOTS, 1, 0.005, "fishing", 20));
        this.add("fish_chain_plate", this.fish(Items.CHAINMAIL_CHESTPLATE, 1, 0.005, "fishing", 20));
        this.add("fish_chain_helm", this.fish(Items.CHAINMAIL_HELMET, 1, 0.005, "fishing", 20));
        this.add("fish_chain_pants", this.fish(Items.CHAINMAIL_LEGGINGS, 1, 0.005, "fishing", 20));
        this.add("fish_diamond_axe", this.fish(Items.DIAMOND_AXE, 1, 1.0E-4, "fishing", 50));
        this.add("fish_diamond_hoe", this.fish(Items.DIAMOND_HOE, 1, 1.0E-4, "fishing", 50));
        this.add("fish_diamond_pick", this.fish(Items.DIAMOND_PICKAXE, 1, 1.0E-4, "fishing", 50));
        this.add("fish_diamond_shovel", this.fish(Items.DIAMOND_SHOVEL, 1, 1.0E-4, "fishing", 50));
        this.add("fish_diamond_sword", this.fish(Items.DIAMOND_SWORD, 1, 1.0E-4, "fishing", 50));
        this.add("fish_diamond_boots", this.fish(Items.DIAMOND_BOOTS, 1, 1.0E-4, "fishing", 50));
        this.add("fish_diamond_plate", this.fish(Items.DIAMOND_CHESTPLATE, 1, 1.0E-4, "fishing", 50));
        this.add("fish_diamond_helm", this.fish(Items.DIAMOND_HELMET, 1, 1.0E-4, "fishing", 50));
        this.add("fish_diamond_pants", this.fish(Items.DIAMOND_LEGGINGS, 1, 1.0E-4, "fishing", 50));
        this.add("fish_gold_axe", this.fish(Items.GOLDEN_AXE, 1, 0.005, "fishing", 30));
        this.add("fish_gold_hoe", this.fish(Items.GOLDEN_HOE, 1, 0.005, "fishing", 30));
        this.add("fish_gold_pick", this.fish(Items.GOLDEN_PICKAXE, 1, 0.005, "fishing", 30));
        this.add("fish_gold_shovel", this.fish(Items.GOLDEN_SHOVEL, 1, 0.005, "fishing", 30));
        this.add("fish_gold_sword", this.fish(Items.GOLDEN_SWORD, 1, 0.005, "fishing", 30));
        this.add("fish_gold_boots", this.fish(Items.GOLDEN_BOOTS, 1, 0.005, "fishing", 30));
        this.add("fish_gold_plate", this.fish(Items.GOLDEN_CHESTPLATE, 1, 0.005, "fishing", 30));
        this.add("fish_gold_helm", this.fish(Items.GOLDEN_HELMET, 1, 0.005, "fishing", 30));
        this.add("fish_gold_pants", this.fish(Items.GOLDEN_LEGGINGS, 1, 0.005, "fishing", 30));
        this.add("fish_iron_axe", this.fish(Items.IRON_AXE, 1, 0.01, "fishing", 20));
        this.add("fish_iron_hoe", this.fish(Items.IRON_HOE, 1, 0.01, "fishing", 20));
        this.add("fish_iron_pick", this.fish(Items.IRON_PICKAXE, 1, 0.01, "fishing", 20));
        this.add("fish_iron_shovel", this.fish(Items.IRON_SHOVEL, 1, 0.01, "fishing", 20));
        this.add("fish_iron_sword", this.fish(Items.IRON_SWORD, 1, 0.01, "fishing", 20));
        this.add("fish_iron_boots", this.fish(Items.IRON_BOOTS, 1, 0.01, "fishing", 20));
        this.add("fish_iron_plate", this.fish(Items.IRON_CHESTPLATE, 1, 0.01, "fishing", 20));
        this.add("fish_iron_helm", this.fish(Items.IRON_HELMET, 1, 0.01, "fishing", 20));
        this.add("fish_iron_pants", this.fish(Items.IRON_LEGGINGS, 1, 0.01, "fishing", 20));
        this.add("fish_leather_boots", this.fish(Items.LEATHER_BOOTS, 1, 0.01, "fishing", 1));
        this.add("fish_leather_plate", this.fish(Items.LEATHER_CHESTPLATE, 1, 0.01, "fishing", 1));
        this.add("fish_leather_helm", this.fish(Items.LEATHER_HELMET, 1, 0.01, "fishing", 1));
        this.add("fish_leather_pants", this.fish(Items.LEATHER_LEGGINGS, 1, 0.01, "fishing", 1));
        this.add("fish_netherite_axe", this.fish(Items.NETHERITE_AXE, 1, 1.0E-5, "fishing", 70));
        this.add("fish_netherite_hoe", this.fish(Items.NETHERITE_HOE, 1, 1.0E-5, "fishing", 70));
        this.add("fish_netherite_pick", this.fish(Items.NETHERITE_PICKAXE, 1, 1.0E-5, "fishing", 70));
        this.add("fish_netherite_shovel", this.fish(Items.NETHERITE_SHOVEL, 1, 1.0E-5, "fishing", 70));
        this.add("fish_netherite_sword", this.fish(Items.NETHERITE_SWORD, 1, 1.0E-5, "fishing", 70));
        this.add("fish_netherite_boots", this.fish(Items.NETHERITE_BOOTS, 1, 1.0E-5, "fishing", 70));
        this.add("fish_netherite_plate", this.fish(Items.NETHERITE_CHESTPLATE, 1, 1.0E-5, "fishing", 70));
        this.add("fish_netherite_helm", this.fish(Items.NETHERITE_HELMET, 1, 1.0E-5, "fishing", 70));
        this.add("fish_netherite_pants", this.fish(Items.NETHERITE_LEGGINGS, 1, 1.0E-5, "fishing", 70));
        this.add("fish_wood_axe", this.fish(Items.WOODEN_AXE, 1, 0.001, "fishing", 0, 15));
        this.add("fish_wood_hoe", this.fish(Items.WOODEN_HOE, 1, 0.001, "fishing", 0, 15));
        this.add("fish_wood_pick", this.fish(Items.WOODEN_PICKAXE, 1, 0.001, "fishing", 0, 15));
        this.add("fish_wood_shovel", this.fish(Items.WOODEN_SHOVEL, 1, 0.001, "fishing", 0, 15));
        this.add("fish_wood_sword", this.fish(Items.WOODEN_SWORD, 1, 0.001, "fishing", 0, 15));
        this.add("mob_chicken", this.mob(EntityType.CHICKEN, Items.EGG, 1, 0.1, "breeding", 10));
        this.add("mob_dragon_head", this.mob(EntityType.ENDER_DRAGON, Items.DRAGON_HEAD, 1, 1.0, "slayer", 50));
        this.add("mob_dragon_egg", this.mob(EntityType.ENDER_DRAGON, Items.DRAGON_EGG, 1, 1.0, "slayer", 50));
        this.add("mob_sheep", this.mob(EntityType.SHEEP, Items.STRING, 1, 0.1, "breeding", 10));
        this.add("mob_slime", this.mob(EntityType.SLIME, Items.SLIME_BLOCK, 1, 1.0, "slayer", 30));
        this.add("mob_zombie", this.mob(EntityType.ZOMBIE, Items.BEETROOT, 1, 0.4, "combat", 20));
    }

    private TreasureLootModifier of(TagKey<Block> validBlocks, Item drop, int count, double chance, String skill, int minLevel) {
        return new TreasureLootModifier(new LootItemCondition[] { new SkillLootConditionPlayer(minLevel, Integer.MAX_VALUE, skill), new ValidBlockCondition(validBlocks) }, RegistryUtil.getId(drop), count, chance);
    }

    private TreasureLootModifier of(Block validBlocks, Item drop, int count, double chance, String skill, int minLevel) {
        return new TreasureLootModifier(new LootItemCondition[] { new SkillLootConditionPlayer(minLevel, Integer.MAX_VALUE, skill), new ValidBlockCondition(validBlocks) }, RegistryUtil.getId(drop), count, chance);
    }

    private TreasureLootModifier extra(TagKey<Block> validBlocks, int count, double chance, String skill, int minLevel) {
        return new TreasureLootModifier(new LootItemCondition[] { new SkillLootConditionPlayer(minLevel, Integer.MAX_VALUE, skill), new ValidBlockCondition(validBlocks) }, RegistryUtil.getId(Blocks.AIR), count, chance);
    }

    private RareDropModifier fish(Item drop, int count, double chance, String skill, int minLevel, int maxLevel) {
        return new RareDropModifier(new LootItemCondition[] { LootTableIdCondition.builder(BuiltInLootTables.FISHING).build(), new SkillLootConditionKill(minLevel, maxLevel, skill) }, RegistryUtil.getId(drop), count, chance);
    }

    private RareDropModifier fish(Item drop, int count, double chance, String skill, int minLevel) {
        return this.fish(drop, count, chance, skill, minLevel, Integer.MAX_VALUE);
    }

    private RareDropModifier mob(EntityType<?> mob, Item drop, int count, double chance, String skill, int minLevel, int maxLevel) {
        return new RareDropModifier(new LootItemCondition[] { LootItemKilledByPlayerCondition.killedByPlayer().build(), LootTableIdCondition.builder(mob.getDefaultLootTable()).build(), new SkillLootConditionKill(minLevel, maxLevel, skill) }, RegistryUtil.getId(drop), count, chance);
    }

    private RareDropModifier mob(EntityType<?> mob, Item drop, int count, double chance, String skill, int minLevel) {
        return this.mob(mob, drop, count, chance, skill, minLevel, Integer.MAX_VALUE);
    }
}