package com.rekindled.embers.datagen;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.compat.curios.CuriosCompat;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class EmbersItemTags extends ItemTagsProvider {

    public static final TagKey<Item> PIPE_UNCLOGGER = ItemTags.create(new ResourceLocation("embers", "pipe_uncloggers"));

    public static final TagKey<Item> MATERIA_BLACKLIST = ItemTags.create(new ResourceLocation("embers", "materia_repair_blacklist"));

    public static final TagKey<Item> BREAKDOWN_BLACKLIST = ItemTags.create(new ResourceLocation("embers", "anvil_breakdown_blacklist"));

    public static final TagKey<Item> REPAIR_BLACKLIST = ItemTags.create(new ResourceLocation("embers", "anvil_repair_blacklist"));

    public static final TagKey<Item> TINKER_HAMMER = ItemTags.create(new ResourceLocation("embers", "tinker_hammer"));

    public static final TagKey<Item> INSCRIBABLE_PAPER = ItemTags.create(new ResourceLocation("embers", "inscribable_paper"));

    public static final TagKey<Item> TOOLS_HAMMERS = ItemTags.create(new ResourceLocation("forge", "tools/hammers"));

    public static final TagKey<Item> NORMAL_WALK_SPEED_TOOL = ItemTags.create(new ResourceLocation("embers", "normal_walk_speed_tool"));

    public static final TagKey<Item> AUGMENTABLE = ItemTags.create(new ResourceLocation("embers", "augmentables"));

    public static final TagKey<Item> AUGMENTABLE_TOOLS_AND_ARMORS = ItemTags.create(new ResourceLocation("embers", "augmentables/tools_armors"));

    public static final TagKey<Item> AUGMENTABLE_TOOLS = ItemTags.create(new ResourceLocation("embers", "augmentables/tools"));

    public static final TagKey<Item> AUGMENTABLE_PROJECTILE_WEAPONS = ItemTags.create(new ResourceLocation("embers", "augmentables/projectiles"));

    public static final TagKey<Item> AUGMENTABLE_ARMORS = ItemTags.create(new ResourceLocation("embers", "augmentables/armors"));

    public static final TagKey<Item> AUGMENTABLE_HELMETS = ItemTags.create(new ResourceLocation("embers", "augmentables/armors/helmets"));

    public static final TagKey<Item> AUGMENTABLE_CHESTPLATES = ItemTags.create(new ResourceLocation("embers", "augmentables/armors/chestplates"));

    public static final TagKey<Item> AUGMENTABLE_LEGGINGS = ItemTags.create(new ResourceLocation("embers", "augmentables/armors/leggings"));

    public static final TagKey<Item> AUGMENTABLE_BOOTS = ItemTags.create(new ResourceLocation("embers", "augmentables/armors/boots"));

    public static final TagKey<Item> TINKER_LENS_HELMETS = ItemTags.create(new ResourceLocation("embers", "tinker_lens_helmets"));

    public static final TagKey<Item> ASPECTUS = ItemTags.create(new ResourceLocation("embers", "aspectus"));

    public static final TagKey<Item> IRON_ASPECTUS = ItemTags.create(new ResourceLocation("embers", "aspectus/iron"));

    public static final TagKey<Item> COPPER_ASPECTUS = ItemTags.create(new ResourceLocation("embers", "aspectus/copper"));

    public static final TagKey<Item> LEAD_ASPECTUS = ItemTags.create(new ResourceLocation("embers", "aspectus/lead"));

    public static final TagKey<Item> SILVER_ASPECTUS = ItemTags.create(new ResourceLocation("embers", "aspectus/silver"));

    public static final TagKey<Item> DAWNSTONE_ASPECTUS = ItemTags.create(new ResourceLocation("embers", "aspectus/dawnstone"));

    public static final TagKey<Item> ASHEN_STONE = ItemTags.create(new ResourceLocation("embers", "ashen_stone"));

    public static final TagKey<Item> PLATES = ItemTags.create(new ResourceLocation("forge", "plates"));

    public static final TagKey<Item> IRON_PLATE = ItemTags.create(new ResourceLocation("forge", "plates/iron"));

    public static final TagKey<Item> COPPER_PLATE = ItemTags.create(new ResourceLocation("forge", "plates/copper"));

    public static final TagKey<Item> COPPER_NUGGET = ItemTags.create(new ResourceLocation("forge", "nuggets/copper"));

    public static final TagKey<Item> LEAD_ORE = ItemTags.create(new ResourceLocation("forge", "ores/lead"));

    public static final TagKey<Item> RAW_LEAD_BLOCK = ItemTags.create(new ResourceLocation("forge", "storage_blocks/raw_lead"));

    public static final TagKey<Item> LEAD_BLOCK = ItemTags.create(new ResourceLocation("forge", "storage_blocks/lead"));

    public static final TagKey<Item> RAW_LEAD = ItemTags.create(new ResourceLocation("forge", "raw_materials/lead"));

    public static final TagKey<Item> LEAD_INGOT = ItemTags.create(new ResourceLocation("forge", "ingots/lead"));

    public static final TagKey<Item> LEAD_NUGGET = ItemTags.create(new ResourceLocation("forge", "nuggets/lead"));

    public static final TagKey<Item> LEAD_PLATE = ItemTags.create(new ResourceLocation("forge", "plates/lead"));

    public static final TagKey<Item> SILVER_ORE = ItemTags.create(new ResourceLocation("forge", "ores/silver"));

    public static final TagKey<Item> RAW_SILVER_BLOCK = ItemTags.create(new ResourceLocation("forge", "storage_blocks/raw_silver"));

    public static final TagKey<Item> SILVER_BLOCK = ItemTags.create(new ResourceLocation("forge", "storage_blocks/silver"));

    public static final TagKey<Item> RAW_SILVER = ItemTags.create(new ResourceLocation("forge", "raw_materials/silver"));

    public static final TagKey<Item> SILVER_INGOT = ItemTags.create(new ResourceLocation("forge", "ingots/silver"));

    public static final TagKey<Item> SILVER_NUGGET = ItemTags.create(new ResourceLocation("forge", "nuggets/silver"));

    public static final TagKey<Item> SILVER_PLATE = ItemTags.create(new ResourceLocation("forge", "plates/silver"));

    public static final TagKey<Item> DAWNSTONE_BLOCK = ItemTags.create(new ResourceLocation("forge", "storage_blocks/dawnstone"));

    public static final TagKey<Item> DAWNSTONE_INGOT = ItemTags.create(new ResourceLocation("forge", "ingots/dawnstone"));

    public static final TagKey<Item> DAWNSTONE_NUGGET = ItemTags.create(new ResourceLocation("forge", "nuggets/dawnstone"));

    public static final TagKey<Item> DAWNSTONE_PLATE = ItemTags.create(new ResourceLocation("forge", "plates/dawnstone"));

    public static final TagKey<Item> NICKEL_INGOT = ItemTags.create(new ResourceLocation("forge", "ingots/nickel"));

    public static final TagKey<Item> TIN_INGOT = ItemTags.create(new ResourceLocation("forge", "ingots/tin"));

    public static final TagKey<Item> ALUMINUM_INGOT = ItemTags.create(new ResourceLocation("forge", "ingots/aluminum"));

    public static final TagKey<Item> ZINC_INGOT = ItemTags.create(new ResourceLocation("forge", "ingots/zinc"));

    public static final TagKey<Item> NICKEL_NUGGET = ItemTags.create(new ResourceLocation("forge", "nuggets/nickel"));

    public static final TagKey<Item> TIN_NUGGET = ItemTags.create(new ResourceLocation("forge", "nuggets/tin"));

    public static final TagKey<Item> ALUMINUM_NUGGET = ItemTags.create(new ResourceLocation("forge", "nuggets/aluminum"));

    public static final TagKey<Item> ZINC_NUGGET = ItemTags.create(new ResourceLocation("forge", "nuggets/zinc"));

    public static final TagKey<Item> CAMINITE_BRICK = ItemTags.create(new ResourceLocation("forge", "ingots/caminite_brick"));

    public static final TagKey<Item> ARCHAIC_BRICK = ItemTags.create(new ResourceLocation("forge", "ingots/archaic_brick"));

    public static final TagKey<Item> ASH_DUST = ItemTags.create(new ResourceLocation("forge", "dusts/ash"));

    public static final TagKey<Item> WORLD_BOTTOM = ItemTags.create(new ResourceLocation("embers", "world_bottom"));

    public static final TagKey<Item> SNOW = ItemTags.create(new ResourceLocation("embers", "snow"));

    public static final TagKey<Item> PRISTINE_COPPER = ItemTags.create(new ResourceLocation("embers", "pristine_copper"));

    public static final TagKey<Item> EXPOSED_COPPER = ItemTags.create(new ResourceLocation("embers", "exposed_copper"));

    public static final TagKey<Item> WEATHERED_COPPER = ItemTags.create(new ResourceLocation("embers", "weathered_copper"));

    public static final TagKey<Item> OXIDIZED_COPPER = ItemTags.create(new ResourceLocation("embers", "oxidized_copper"));

    public static final TagKey<Item> CRYSTAL_SEEDS = ItemTags.create(new ResourceLocation("embers", "crystal_seeds"));

    public static final TagKey<Item> COPPER_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/copper"));

    public static final TagKey<Item> IRON_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/iron"));

    public static final TagKey<Item> GOLD_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/gold"));

    public static final TagKey<Item> LEAD_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/lead"));

    public static final TagKey<Item> SILVER_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/silver"));

    public static final TagKey<Item> NICKEL_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/nickel"));

    public static final TagKey<Item> TIN_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/tin"));

    public static final TagKey<Item> ALUMINUM_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/aluminum"));

    public static final TagKey<Item> ZINC_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/zinc"));

    public static final TagKey<Item> PLATINUM_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/platinum"));

    public static final TagKey<Item> URANIUM_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/uranium"));

    public static final TagKey<Item> DAWNSTONE_SEED = ItemTags.create(new ResourceLocation("embers", "crystal_seeds/dawnstone"));

    public static final TagKey<Item> ANY_CURIO = ItemTags.create(new ResourceLocation("curios", "curio"));

    public static final TagKey<Item> RING_CURIO = ItemTags.create(new ResourceLocation("curios", "ring"));

    public static final TagKey<Item> BELT_CURIO = ItemTags.create(new ResourceLocation("curios", "belt"));

    public static final TagKey<Item> AMULET_CURIO = ItemTags.create(new ResourceLocation("curios", "necklace"));

    public static final TagKey<Item> BODY_CURIO = ItemTags.create(new ResourceLocation("curios", "body"));

    public static final TagKey<Item> CHARM_CURIO = ItemTags.create(new ResourceLocation("curios", "charm"));

    public EmbersItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, "embers", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.m_206424_(PIPE_UNCLOGGER).addTag(Tags.Items.RODS);
        this.m_206424_(TINKER_HAMMER).add(RegistryManager.TINKER_HAMMER.get());
        this.m_206424_(INSCRIBABLE_PAPER).add(Items.PAPER);
        this.m_206424_(NORMAL_WALK_SPEED_TOOL).add(RegistryManager.BLAZING_RAY.get()).add(RegistryManager.CINDER_STAFF.get());
        this.m_206424_(AUGMENTABLE).addTag(AUGMENTABLE_TOOLS).addTag(AUGMENTABLE_PROJECTILE_WEAPONS).addTag(AUGMENTABLE_ARMORS);
        this.m_206424_(AUGMENTABLE_TOOLS_AND_ARMORS).addTag(AUGMENTABLE_TOOLS).addTag(AUGMENTABLE_ARMORS);
        this.m_206424_(AUGMENTABLE_ARMORS).addTag(AUGMENTABLE_HELMETS).addTag(AUGMENTABLE_CHESTPLATES).addTag(AUGMENTABLE_LEGGINGS).addTag(AUGMENTABLE_BOOTS);
        this.m_206424_(AUGMENTABLE_TOOLS).addTag(Tags.Items.TOOLS).addTag(ItemTags.TOOLS);
        this.m_206424_(AUGMENTABLE_PROJECTILE_WEAPONS).add(RegistryManager.BLAZING_RAY.get()).add(RegistryManager.CINDER_STAFF.get());
        this.m_206424_(AUGMENTABLE_HELMETS).addTag(Tags.Items.ARMORS_HELMETS).add(Items.CARVED_PUMPKIN);
        this.m_206424_(AUGMENTABLE_CHESTPLATES).addTag(Tags.Items.ARMORS_CHESTPLATES);
        this.m_206424_(AUGMENTABLE_LEGGINGS).addTag(Tags.Items.ARMORS_LEGGINGS);
        this.m_206424_(AUGMENTABLE_BOOTS).addTag(Tags.Items.ARMORS_BOOTS);
        this.m_206424_(TINKER_LENS_HELMETS).add(RegistryManager.ASHEN_GOGGLES.get());
        this.m_206424_(ASPECTUS).addTags(new TagKey[] { IRON_ASPECTUS, COPPER_ASPECTUS, LEAD_ASPECTUS, SILVER_ASPECTUS, DAWNSTONE_ASPECTUS });
        this.m_206424_(IRON_ASPECTUS).add(RegistryManager.IRON_ASPECTUS.get());
        this.m_206424_(COPPER_ASPECTUS).add(RegistryManager.COPPER_ASPECTUS.get());
        this.m_206424_(LEAD_ASPECTUS).add(RegistryManager.LEAD_ASPECTUS.get());
        this.m_206424_(SILVER_ASPECTUS).add(RegistryManager.SILVER_ASPECTUS.get());
        this.m_206424_(DAWNSTONE_ASPECTUS).add(RegistryManager.DAWNSTONE_ASPECTUS.get());
        this.m_206424_(ASHEN_STONE).add(RegistryManager.ASHEN_STONE_ITEM.get(), RegistryManager.ASHEN_BRICK_ITEM.get(), RegistryManager.ASHEN_TILE_ITEM.get());
        this.toolTags(RegistryManager.LEAD_TOOLS);
        this.toolTags(RegistryManager.SILVER_TOOLS);
        this.toolTags(RegistryManager.DAWNSTONE_TOOLS);
        this.m_206424_(ItemTags.SWORDS).add(RegistryManager.TYRFING.get());
        this.m_206424_(Tags.Items.ORES).addTags(new TagKey[] { LEAD_ORE, SILVER_ORE });
        this.m_206424_(LEAD_ORE).add(RegistryManager.LEAD_ORE_ITEM.get()).add(RegistryManager.DEEPSLATE_LEAD_ORE_ITEM.get());
        this.m_206424_(SILVER_ORE).add(RegistryManager.SILVER_ORE_ITEM.get()).add(RegistryManager.DEEPSLATE_SILVER_ORE_ITEM.get());
        this.m_206424_(Tags.Items.ORES_IN_GROUND_STONE).add(RegistryManager.LEAD_ORE_ITEM.get());
        this.m_206424_(Tags.Items.ORES_IN_GROUND_DEEPSLATE).add(RegistryManager.DEEPSLATE_LEAD_ORE_ITEM.get());
        this.m_206424_(Tags.Items.ORES_IN_GROUND_STONE).add(RegistryManager.SILVER_ORE_ITEM.get());
        this.m_206424_(Tags.Items.ORES_IN_GROUND_DEEPSLATE).add(RegistryManager.DEEPSLATE_SILVER_ORE_ITEM.get());
        this.m_206424_(Tags.Items.STORAGE_BLOCKS).addTags(new TagKey[] { RAW_LEAD_BLOCK, RAW_SILVER_BLOCK });
        this.m_206424_(RAW_LEAD_BLOCK).add(RegistryManager.RAW_LEAD_BLOCK_ITEM.get());
        this.m_206424_(RAW_SILVER_BLOCK).add(RegistryManager.RAW_SILVER_BLOCK_ITEM.get());
        this.m_206424_(Tags.Items.STORAGE_BLOCKS).addTags(new TagKey[] { LEAD_BLOCK, SILVER_BLOCK, DAWNSTONE_BLOCK });
        this.m_206424_(LEAD_BLOCK).add(RegistryManager.LEAD_BLOCK_ITEM.get());
        this.m_206424_(SILVER_BLOCK).add(RegistryManager.SILVER_BLOCK_ITEM.get());
        this.m_206424_(DAWNSTONE_BLOCK).add(RegistryManager.DAWNSTONE_BLOCK_ITEM.get());
        this.m_206424_(Tags.Items.RAW_MATERIALS).addTags(new TagKey[] { RAW_LEAD, RAW_SILVER });
        this.m_206424_(RAW_LEAD).add(RegistryManager.RAW_LEAD.get());
        this.m_206424_(RAW_SILVER).add(RegistryManager.RAW_SILVER.get());
        this.m_206424_(Tags.Items.INGOTS).addTags(new TagKey[] { LEAD_INGOT, SILVER_INGOT, DAWNSTONE_INGOT });
        this.m_206424_(LEAD_INGOT).add(RegistryManager.LEAD_INGOT.get());
        this.m_206424_(SILVER_INGOT).add(RegistryManager.SILVER_INGOT.get());
        this.m_206424_(DAWNSTONE_INGOT).add(RegistryManager.DAWNSTONE_INGOT.get());
        this.m_206424_(Tags.Items.NUGGETS).addTags(new TagKey[] { COPPER_NUGGET, LEAD_NUGGET, SILVER_NUGGET, DAWNSTONE_NUGGET });
        this.m_206424_(COPPER_NUGGET).add(RegistryManager.COPPER_NUGGET.get());
        this.m_206424_(LEAD_NUGGET).add(RegistryManager.LEAD_NUGGET.get());
        this.m_206424_(SILVER_NUGGET).add(RegistryManager.SILVER_NUGGET.get());
        this.m_206424_(DAWNSTONE_NUGGET).add(RegistryManager.DAWNSTONE_NUGGET.get());
        this.m_206424_(PLATES).addTags(new TagKey[] { IRON_PLATE, COPPER_PLATE, LEAD_PLATE, SILVER_PLATE, DAWNSTONE_PLATE });
        this.m_206424_(IRON_PLATE).add(RegistryManager.IRON_PLATE.get());
        this.m_206424_(COPPER_PLATE).add(RegistryManager.COPPER_PLATE.get());
        this.m_206424_(LEAD_PLATE).add(RegistryManager.LEAD_PLATE.get());
        this.m_206424_(SILVER_PLATE).add(RegistryManager.SILVER_PLATE.get());
        this.m_206424_(DAWNSTONE_PLATE).add(RegistryManager.DAWNSTONE_PLATE.get());
        this.m_206424_(Tags.Items.INGOTS).addTags(new TagKey[] { CAMINITE_BRICK });
        this.m_206424_(CAMINITE_BRICK).add(RegistryManager.CAMINITE_BRICK.get());
        this.m_206424_(Tags.Items.INGOTS).addTags(new TagKey[] { ARCHAIC_BRICK });
        this.m_206424_(ARCHAIC_BRICK).add(RegistryManager.ARCHAIC_BRICK.get());
        this.m_206424_(Tags.Items.DUSTS).addTag(ASH_DUST);
        this.m_206424_(ASH_DUST).add(RegistryManager.ASH.get());
        this.m_206424_(ItemTags.PICKAXES).add(RegistryManager.CLOCKWORK_PICKAXE.get());
        this.m_206424_(ItemTags.AXES).add(RegistryManager.CLOCKWORK_AXE.get());
        this.m_206424_(Tags.Items.TOOLS).addTag(TOOLS_HAMMERS);
        this.m_206424_(TOOLS_HAMMERS).add(RegistryManager.TINKER_HAMMER.get(), RegistryManager.GRANDHAMMER.get());
        this.m_206424_(Tags.Items.ARMORS_HELMETS).add(RegistryManager.ASHEN_GOGGLES.get());
        this.m_206424_(Tags.Items.ARMORS_CHESTPLATES).add(RegistryManager.ASHEN_CLOAK.get());
        this.m_206424_(Tags.Items.ARMORS_LEGGINGS).add(RegistryManager.ASHEN_LEGGINGS.get());
        this.m_206424_(Tags.Items.ARMORS_BOOTS).add(RegistryManager.ASHEN_BOOTS.get());
        this.m_206424_(Tags.Items.SLIMEBALLS).add(RegistryManager.ADHESIVE.get());
        this.m_206424_(ItemTags.MUSIC_DISCS).add(RegistryManager.MUSIC_DISC_7F_PATTERNS.get());
        this.m_206421_(EmbersBlockTags.WORLD_BOTTOM, WORLD_BOTTOM);
        this.m_206421_(EmbersBlockTags.SNOW, SNOW);
        this.m_206421_(EmbersBlockTags.PRISTINE_COPPER, PRISTINE_COPPER);
        this.m_206421_(EmbersBlockTags.EXPOSED_COPPER, EXPOSED_COPPER);
        this.m_206421_(EmbersBlockTags.WEATHERED_COPPER, WEATHERED_COPPER);
        this.m_206421_(EmbersBlockTags.OXIDIZED_COPPER, OXIDIZED_COPPER);
        this.m_206421_(EmbersBlockTags.CRYSTAL_SEEDS, CRYSTAL_SEEDS);
        this.m_206421_(EmbersBlockTags.COPPER_SEED, COPPER_SEED);
        this.m_206421_(EmbersBlockTags.IRON_SEED, IRON_SEED);
        this.m_206421_(EmbersBlockTags.GOLD_SEED, GOLD_SEED);
        this.m_206421_(EmbersBlockTags.LEAD_SEED, LEAD_SEED);
        this.m_206421_(EmbersBlockTags.SILVER_SEED, SILVER_SEED);
        this.m_206421_(EmbersBlockTags.NICKEL_SEED, NICKEL_SEED);
        this.m_206421_(EmbersBlockTags.TIN_SEED, TIN_SEED);
        this.m_206421_(EmbersBlockTags.ALUMINUM_SEED, ALUMINUM_SEED);
        this.m_206421_(EmbersBlockTags.ZINC_SEED, ZINC_SEED);
        this.m_206421_(EmbersBlockTags.PLATINUM_SEED, PLATINUM_SEED);
        this.m_206421_(EmbersBlockTags.URANIUM_SEED, URANIUM_SEED);
        this.m_206421_(EmbersBlockTags.DAWNSTONE_SEED, DAWNSTONE_SEED);
        this.m_206424_(AUGMENTABLE_TOOLS).m_176839_(new ResourceLocation("weaponmaster", "wm_broadsword")).addOptional(new ResourceLocation("weaponmaster", "wm_rapier")).addOptional(new ResourceLocation("weaponmaster", "wm_broadswordlarge")).addOptional(new ResourceLocation("weaponmaster", "wm_rapierlarge"));
        this.m_206424_(AUGMENTABLE_TOOLS).m_176839_(new ResourceLocation("miapi", "modular_sword")).addOptional(new ResourceLocation("miapi", "modular_katana")).addOptional(new ResourceLocation("miapi", "modular_naginata")).addOptional(new ResourceLocation("miapi", "modular_greatsword")).addOptional(new ResourceLocation("miapi", "modular_dagger")).addOptional(new ResourceLocation("miapi", "modular_spear")).addOptional(new ResourceLocation("miapi", "modular_throwing_knife")).addOptional(new ResourceLocation("miapi", "modular_rapier")).addOptional(new ResourceLocation("miapi", "modular_longsword")).addOptional(new ResourceLocation("miapi", "modular_trident")).addOptional(new ResourceLocation("miapi", "modular_scythe")).addOptional(new ResourceLocation("miapi", "modular_sickle")).addOptional(new ResourceLocation("miapi", "modular_shovel")).addOptional(new ResourceLocation("miapi", "modular_pickaxe")).addOptional(new ResourceLocation("miapi", "modular_axe")).addOptional(new ResourceLocation("miapi", "modular_hoe")).addOptional(new ResourceLocation("miapi", "modular_mattock"));
        this.m_206424_(AUGMENTABLE_HELMETS).m_176839_(new ResourceLocation("miapi", "modular_helmet"));
        this.m_206424_(AUGMENTABLE_CHESTPLATES).m_176839_(new ResourceLocation("miapi", "modular_chestplate")).addOptional(new ResourceLocation("miapi", "modular_elytra"));
        this.m_206424_(AUGMENTABLE_LEGGINGS).m_176839_(new ResourceLocation("miapi", "modular_leggings"));
        this.m_206424_(AUGMENTABLE_BOOTS).m_176839_(new ResourceLocation("miapi", "modular_boots"));
        this.m_206424_(AUGMENTABLE_TOOLS).m_176839_(new ResourceLocation("tetra", "modular_single")).addOptional(new ResourceLocation("tetra", "modular_double")).addOptional(new ResourceLocation("tetra", "modular_sword"));
        this.m_206424_(ANY_CURIO).m_176839_(CuriosCompat.EMBER_BULB.getId());
        this.m_206424_(RING_CURIO).m_176839_(CuriosCompat.EMBER_RING.getId());
        this.m_206424_(BELT_CURIO).m_176839_(CuriosCompat.EMBER_BELT.getId());
        this.m_206424_(AMULET_CURIO).m_176839_(CuriosCompat.EMBER_AMULET.getId()).addOptional(CuriosCompat.ASHEN_AMULET.getId()).addOptional(CuriosCompat.NONBELEIVER_AMULET.getId());
        this.m_206424_(BODY_CURIO).m_176839_(CuriosCompat.DAWNSTONE_MAIL.getId());
        this.m_206424_(CHARM_CURIO).m_176839_(CuriosCompat.EXPLOSION_CHARM.getId());
    }

    public void toolTags(RegistryManager.ToolSet set) {
        this.m_206424_(ItemTags.SWORDS).add(set.SWORD.get());
        this.m_206424_(ItemTags.SHOVELS).add(set.SHOVEL.get());
        this.m_206424_(ItemTags.PICKAXES).add(set.PICKAXE.get());
        this.m_206424_(ItemTags.AXES).add(set.AXE.get());
        this.m_206424_(ItemTags.HOES).add(set.HOE.get());
        this.m_206424_(ItemTags.TOOLS).add(set.SWORD.get(), set.SHOVEL.get(), set.PICKAXE.get(), set.AXE.get(), set.HOE.get());
    }
}