package com.mna.items;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.faction.FactionIDs;
import com.mna.api.items.MACreativeTabs;
import com.mna.api.items.TieredItem;
import com.mna.api.sound.Music;
import com.mna.api.tools.MATags;
import com.mna.blocks.BlockInit;
import com.mna.items.armor.BoneArmorItem;
import com.mna.items.armor.BrokenMageArmor;
import com.mna.items.armor.CouncilArmorItem;
import com.mna.items.armor.DemonArmorItem;
import com.mna.items.armor.DyeableMageArmor;
import com.mna.items.armor.FeyArmorItem;
import com.mna.items.armor.MAArmorMaterial;
import com.mna.items.artifact.AllfatherAxe;
import com.mna.items.artifact.AllfatherAxeControl;
import com.mna.items.artifice.FactionSpecificSpellModifierRing;
import com.mna.items.artifice.FilterItem;
import com.mna.items.artifice.FortuneRing;
import com.mna.items.artifice.HealingPoulticeItem;
import com.mna.items.artifice.ItemArcaneCrown;
import com.mna.items.artifice.ItemBubbleBoat;
import com.mna.items.artifice.ItemDowsingRod;
import com.mna.items.artifice.ItemEnderDisk;
import com.mna.items.artifice.ItemEnderguardAmulet;
import com.mna.items.artifice.ItemFactionHorn;
import com.mna.items.artifice.ItemFluidJug;
import com.mna.items.artifice.ItemHellfireStaff;
import com.mna.items.artifice.ItemHellfireTrident;
import com.mna.items.artifice.ItemInfiniteFluidJug;
import com.mna.items.artifice.ItemLodestarCopier;
import com.mna.items.artifice.ItemPilgrimStaff;
import com.mna.items.artifice.ItemRecipeCopyBook;
import com.mna.items.artifice.ItemSpectralElytra;
import com.mna.items.artifice.ItemThaumaturgicCompass;
import com.mna.items.artifice.ItemTransitoryTunnel;
import com.mna.items.artifice.ItemWardingAmulet;
import com.mna.items.artifice.ItemWitherguardAmulet;
import com.mna.items.artifice.SpellModifierRing;
import com.mna.items.artifice.charms.ItemCancelDrownCharm;
import com.mna.items.artifice.charms.ItemCancelFatalFallCharm;
import com.mna.items.artifice.charms.ItemCancelFireCharm;
import com.mna.items.artifice.charms.ItemContingencyCharm;
import com.mna.items.artifice.charms.ItemTeleportToBedCharm;
import com.mna.items.artifice.curio.ItemAntidoteBracelet;
import com.mna.items.artifice.curio.ItemCritImmunityAmulet;
import com.mna.items.artifice.curio.ItemEldrinBracelet;
import com.mna.items.artifice.curio.ItemEldritchOrb;
import com.mna.items.artifice.curio.ItemEmberglowBracelet;
import com.mna.items.artifice.curio.ItemProjectileAmulet;
import com.mna.items.artifice.curio.ItemTrickeryBracelet;
import com.mna.items.base.INoCreativeTab;
import com.mna.items.constructs.BellOfBidding;
import com.mna.items.constructs.ItemMagicBroom;
import com.mna.items.constructs.parts.arms.ConstructPartAxeArmLeft;
import com.mna.items.constructs.parts.arms.ConstructPartAxeArmRight;
import com.mna.items.constructs.parts.arms.ConstructPartBladeArmLeft;
import com.mna.items.constructs.parts.arms.ConstructPartBladeArmRight;
import com.mna.items.constructs.parts.arms.ConstructPartCasterArmLeft;
import com.mna.items.constructs.parts.arms.ConstructPartCasterArmRight;
import com.mna.items.constructs.parts.arms.ConstructPartFishingArmLeft;
import com.mna.items.constructs.parts.arms.ConstructPartFishingArmRight;
import com.mna.items.constructs.parts.arms.ConstructPartFluidNozzleLeft;
import com.mna.items.constructs.parts.arms.ConstructPartFluidNozzleRight;
import com.mna.items.constructs.parts.arms.ConstructPartGrabberArmLeft;
import com.mna.items.constructs.parts.arms.ConstructPartGrabberArmRight;
import com.mna.items.constructs.parts.arms.ConstructPartHammerArmLeft;
import com.mna.items.constructs.parts.arms.ConstructPartHammerArmRight;
import com.mna.items.constructs.parts.arms.ConstructPartManaCannonLeft;
import com.mna.items.constructs.parts.arms.ConstructPartManaCannonRight;
import com.mna.items.constructs.parts.arms.ConstructPartShieldArmLeft;
import com.mna.items.constructs.parts.arms.ConstructPartShieldArmRight;
import com.mna.items.constructs.parts.head.ConstructPartBasicHead;
import com.mna.items.constructs.parts.head.ConstructPartHornHead;
import com.mna.items.constructs.parts.head.ConstructPartSmartHead;
import com.mna.items.constructs.parts.legs.ConstructPartBasicLegs;
import com.mna.items.constructs.parts.legs.ConstructPartEnderLegs;
import com.mna.items.constructs.parts.legs.ConstructPartReinforcedLegs;
import com.mna.items.constructs.parts.legs.ConstructPartRocketLegs;
import com.mna.items.constructs.parts.torso.ConstructPartArmorTorso;
import com.mna.items.constructs.parts.torso.ConstructPartBasicTorso;
import com.mna.items.constructs.parts.torso.ConstructPartManaTorso;
import com.mna.items.constructs.parts.torso.ConstructPartStorageTorso;
import com.mna.items.constructs.parts.torso.ConstructPartTankTorso;
import com.mna.items.food.ItemClayMug;
import com.mna.items.food.ItemManaTea;
import com.mna.items.manaweaving.ItemManaweaveBottle;
import com.mna.items.manaweaving.ItemManaweaverWand;
import com.mna.items.manaweaving.ItemManaweavingPattern;
import com.mna.items.relic.ItemAstroBlade;
import com.mna.items.relic.ItemRunicMalus;
import com.mna.items.relic.MithionsMagnificentMbag;
import com.mna.items.relic.ScrollOfIcarianFlight;
import com.mna.items.ritual.FlatLandsBook;
import com.mna.items.ritual.ItemPlayerCharm;
import com.mna.items.ritual.ItemPractitionersPatch;
import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.items.ritual.ItemPurifiedVinteumDust;
import com.mna.items.ritual.ItemThaumaturgicLink;
import com.mna.items.ritual.ItemWizardChalk;
import com.mna.items.ritual.ItemWorldCharm;
import com.mna.items.ritual.MoteItem;
import com.mna.items.ritual.PractitionersPouchPatches;
import com.mna.items.runes.ItemMetalRitualRune;
import com.mna.items.runes.ItemRune;
import com.mna.items.runes.ItemRuneMarking;
import com.mna.items.runes.ItemRunePattern;
import com.mna.items.runes.ItemRunescribingRecipe;
import com.mna.items.runes.ItemStoneRune;
import com.mna.items.runes.MarkBookItem;
import com.mna.items.sorcery.ItemAnimusDust;
import com.mna.items.sorcery.ItemBangle;
import com.mna.items.sorcery.ItemBookOfRote;
import com.mna.items.sorcery.ItemCrystalOfMemories;
import com.mna.items.sorcery.ItemCrystalPhylactery;
import com.mna.items.sorcery.ItemEntityCrystal;
import com.mna.items.sorcery.ItemManaGem;
import com.mna.items.sorcery.ItemModifierBook;
import com.mna.items.sorcery.ItemSightUnguent;
import com.mna.items.sorcery.ItemSpell;
import com.mna.items.sorcery.ItemSpellBook;
import com.mna.items.sorcery.ItemSpellGrimoire;
import com.mna.items.sorcery.ItemSpellRecipe;
import com.mna.items.sorcery.ItemStaff;
import com.mna.items.sorcery.ItemTornJournalPage;
import com.mna.items.sorcery.PhylacteryStaffItem;
import com.mna.items.sorcery.VellumItem;
import com.mna.items.sorcery.bound.ItemBoundAxe;
import com.mna.items.sorcery.bound.ItemBoundBow;
import com.mna.items.sorcery.bound.ItemBoundShield;
import com.mna.items.sorcery.bound.ItemBoundSword;
import com.mna.items.villager_lootables.LootPouchItem;
import com.mna.items.worldgen.ItemCloudBottle;
import com.mna.items.worldgen.ItemWakebloom;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "mna");

    public static final RegistryObject<Item> __DEBUG = ITEMS.register("debug_wand", () -> new ItemDebugStick());

    public static final RegistryObject<ItemGuideBook> GUIDE_BOOK = ITEMS.register("guide_book", () -> new ItemGuideBook());

    public static final RegistryObject<Item> COUNCIL_HUD_BADGE = ITEMS.register("council_hud_badge_item", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FEY_SUMMER_HUD_BADGE = ITEMS.register("fey_summer_hud_badge_item", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FEY_WINTER_HUD_BADGE = ITEMS.register("fey_winter_hud_badge_item", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> UNDEAD_HUD_BADGE = ITEMS.register("undead_hud_badge_item", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WAKEBLOOM_BLOCK_ITEM = ITEMS.register("wakebloom", () -> new ItemWakebloom(BlockInit.WAKEBLOOM.get(), new Item.Properties()));

    public static final RegistryObject<ItemAnimusDust> ANIMUS_DUST = ITEMS.register("animus_dust", () -> new ItemAnimusDust());

    public static final RegistryObject<Item> ANIMATED_QUILL = ITEMS.register("animated_quill", () -> new Item(new Item.Properties()));

    public static final RegistryObject<TieredItem> ARCANE_ASH = ITEMS.register("arcane_ash", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> ARCANE_COMPOUND = ITEMS.register("arcane_compound", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> ARCANIST_INK = ITEMS.register("arcanist_ink", () -> new TieredItem(new Item.Properties().durability(128)));

    public static final RegistryObject<VellumItem> VELLUM = ITEMS.register("vellum", () -> new VellumItem());

    public static final RegistryObject<ItemSpellRecipe> ENCHANTED_VELLUM = ITEMS.register("enchanted_vellum", () -> new ItemSpellRecipe(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));

    public static final RegistryObject<ItemSpellGrimoire> GRIMOIRE = ITEMS.register("grimoire", () -> new ItemSpellGrimoire());

    public static final RegistryObject<ItemSpellGrimoire> GRIMOIRE_COUNCIL = ITEMS.register("grimoire_council", () -> new ItemSpellGrimoire(FactionIDs.COUNCIL, true));

    public static final RegistryObject<ItemSpellGrimoire> GRIMOIRE_FEY = ITEMS.register("grimoire_fey", () -> new ItemSpellGrimoire(FactionIDs.FEY, true));

    public static final RegistryObject<ItemSpellGrimoire> GRIMOIRE_UNDEAD = ITEMS.register("grimoire_undead", () -> new ItemSpellGrimoire(FactionIDs.UNDEAD, true));

    public static final RegistryObject<ItemSpellGrimoire> GRIMOIRE_DEMON = ITEMS.register("grimoire_demon", () -> new ItemSpellGrimoire(FactionIDs.DEMONS, true));

    public static final RegistryObject<MarkBookItem> BOOK_MARKS = ITEMS.register("book_marks", () -> new MarkBookItem());

    public static final RegistryObject<PhylacteryStaffItem> STAFF_PHYLACTERY = ITEMS.register("staff_phylactery", () -> new PhylacteryStaffItem());

    public static final RegistryObject<ItemSpellBook> SPELL_BOOK = ITEMS.register("spell_book", () -> new ItemSpellBook(false));

    public static final RegistryObject<ItemSpell> SPELL = ITEMS.register("spell", () -> new ItemSpell());

    public static final RegistryObject<ItemRecipeCopyBook> RECIPE_COPY_BOOK = ITEMS.register("recipe_copy_book", () -> new ItemRecipeCopyBook());

    public static final RegistryObject<ItemBangle> BANGLE = ITEMS.register("bangle", () -> new ItemBangle(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_AMETHYST = ITEMS.register("staff_amethyst", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_AUM = ITEMS.register("staff_aum", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_CERUBLOSSOM = ITEMS.register("staff_cerublossom", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_CHIMERITE = ITEMS.register("staff_chimerite", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_DESERTNOVA = ITEMS.register("staff_desertnova", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_EMERALD = ITEMS.register("staff_emerald", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_GLASS = ITEMS.register("staff_glass", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_GOLD = ITEMS.register("staff_gold", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_IRON = ITEMS.register("staff_iron", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_LAPIS = ITEMS.register("staff_lapis", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_NETHERQUARTZ = ITEMS.register("staff_netherquartz", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_PRISMARINECRYSTAL = ITEMS.register("staff_prismarinecrystal", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_PRISMARINESHARD = ITEMS.register("staff_prismarineshard", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_REDSTONE = ITEMS.register("staff_redstone", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_SKULL = ITEMS.register("staff_skull", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_SKULL_ALT = ITEMS.register("staff_skull_alt", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_TARMA = ITEMS.register("staff_tarma", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_VINTEUM = ITEMS.register("staff_vinteum", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemStaff> STAFF_WAKEBLOOM = ITEMS.register("staff_wakebloom", () -> new ItemStaff(4.0F));

    public static final RegistryObject<ItemCloudBottle> CLOUD_IN_A_BOTTLE = ITEMS.register("cloud_in_a_bottle", () -> new ItemCloudBottle());

    public static final RegistryObject<ItemProjectileAmulet> PROJECTILE_AMULET = ITEMS.register("projectile_amulet", () -> new ItemProjectileAmulet(250.0F));

    public static final RegistryObject<ItemCritImmunityAmulet> CRIT_IMMUNITY_AMULET = ITEMS.register("crit_immune", () -> new ItemCritImmunityAmulet(250.0F));

    public static final RegistryObject<ItemLodestarCopier> LODESTAR_COPIER = ITEMS.register("lodestar_copier", () -> new ItemLodestarCopier());

    public static final RegistryObject<TieredItem> BONE_ASH = ITEMS.register("bone_ash", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemBubbleBoat> BUBBLE_BOAT = ITEMS.register("bubble_boat", () -> new ItemBubbleBoat(0));

    public static final RegistryObject<ItemBubbleBoat> BRIMSTONE_BOAT = ITEMS.register("brimstone_boat", () -> new ItemBubbleBoat(1));

    public static final RegistryObject<Item> CHIMERITE_GEM = ITEMS.register("chimerite_gem", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TRANSMUTED_SILVER = ITEMS.register("transmuted_silver", () -> new Item(new Item.Properties()));

    public static final RegistryObject<ItemCrystalPhylactery> CRYSTAL_PHYLACTERY = ITEMS.register("crystal_phylactery", () -> new ItemCrystalPhylactery());

    public static final RegistryObject<ItemCrystalOfMemories> CRYSTAL_OF_MEMORIES = ITEMS.register("crystal_of_memories", () -> new ItemCrystalOfMemories());

    public static final RegistryObject<ItemEldritchOrb> ELDRITCH_ORB = ITEMS.register("eldritch_orb", () -> new ItemEldritchOrb());

    public static final RegistryObject<TieredItem> COWL_OF_CONSUMPTION = ITEMS.register("cowl_of_consumption", () -> new TieredItem(new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<ItemEnderDisk> ENDER_DISK = ITEMS.register("ender_disc", () -> new ItemEnderDisk());

    public static final RegistryObject<ItemEntityCrystal> ENTITY_ENTRAPMENT_CRYSTAL = ITEMS.register("entrapment_crystal", () -> new ItemEntityCrystal());

    public static final RegistryObject<Item> FLAT_LANDS_BOOK = ITEMS.register("flat_lands_book", () -> new FlatLandsBook());

    public static final RegistryObject<FilterItem> FILTER_ITEM = ITEMS.register("filter_item", () -> new FilterItem());

    public static final RegistryObject<ItemHellfireStaff> HELLFIRE_STAFF = ITEMS.register("hellfire_staff", () -> new ItemHellfireStaff());

    public static final RegistryObject<HealingPoulticeItem> HEALING_POULTICE = ITEMS.register("healing_poultice", () -> new HealingPoulticeItem());

    public static final RegistryObject<LootPouchItem> HERBALIST_POUCH = ITEMS.register("herbalist_pouch", () -> new LootPouchItem(MATags.Items.HERBALIST_POUCH_ITEMS));

    public static final RegistryObject<LootPouchItem> INSCRIPTIONIST_POUCH = ITEMS.register("inscriptionist_pouch", () -> new LootPouchItem(MATags.Items.INSCRIPTIONIST_POUCH_ITEMS));

    public static final RegistryObject<LootPouchItem> WEAVERS_POUCH = ITEMS.register("weaver_pouch", () -> new LootPouchItem(MATags.Items.WEAVER_POUCH_ITEMS, is -> {
        if (is.getItem() == ItemInit.MANAWEAVE_BOTTLE.get()) {
            ItemManaweaveBottle.setRandomPattern(is);
        }
    }));

    public static final RegistryObject<TieredItem> SACHET_EARTH = ITEMS.register("sachet_earth", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> SACHET_AIR = ITEMS.register("sachet_air", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> SACHET_WATER = ITEMS.register("sachet_water", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> SACHET_FIRE = ITEMS.register("sachet_fire", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> SACHET_ENDER = ITEMS.register("sachet_ender", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> SACHET_ARCANE = ITEMS.register("sachet_arcane", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemTornJournalPage> TORN_JOURNAL_PAGE = ITEMS.register("torn_journal_page", () -> new ItemTornJournalPage(false));

    public static final RegistryObject<ItemTornJournalPage> SPELL_PART_THESIS = ITEMS.register("spell_part_thesis", () -> new ItemTornJournalPage(true));

    public static final RegistryObject<TieredItem> MARK_OF_THE_FEY = ITEMS.register("mark_of_the_fey", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> MARK_OF_THE_COUNCIL = ITEMS.register("mark_of_the_council", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> MARK_OF_THE_NETHER = ITEMS.register("mark_of_the_nether", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> MARK_OF_THE_UNDEAD = ITEMS.register("mark_of_the_undead", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemFactionHorn> FACTION_HORN_COUNCIL = ITEMS.register("faction_horn_council", () -> new ItemFactionHorn(FactionIDs.COUNCIL));

    public static final RegistryObject<ItemFactionHorn> FACTION_HORN_DEMONS = ITEMS.register("faction_horn_demons", () -> new ItemFactionHorn(FactionIDs.DEMONS));

    public static final RegistryObject<ItemFactionHorn> FACTION_HORN_FEY = ITEMS.register("faction_horn_fey", () -> new ItemFactionHorn(FactionIDs.FEY));

    public static final RegistryObject<ItemFactionHorn> FACTION_HORN_UNDEAD = ITEMS.register("faction_horn_undead", () -> new ItemFactionHorn(FactionIDs.UNDEAD));

    public static final RegistryObject<ItemMagicBroom> MAGIC_BROOM = ITEMS.register("magic_broom", () -> new ItemMagicBroom(0));

    public static final RegistryObject<ItemMagicBroom> VORTEX_BROOM = ITEMS.register("vortex_broom", () -> new ItemMagicBroom(1));

    public static final RegistryObject<ItemManaGem> MANA_CRYSTAL_FRAGMENT = ITEMS.register("mana_crystal_fragment", () -> new ItemManaGem(new Item.Properties(), 250.0F));

    public static final RegistryObject<ItemManaGem> MINOR_MANA_GEM = ITEMS.register("minor_mana_gem", () -> new ItemManaGem(new Item.Properties(), 1500.0F));

    public static final RegistryObject<ItemManaGem> MAJOR_MANA_GEM = ITEMS.register("major_mana_gem", () -> new ItemManaGem(new Item.Properties(), 15000.0F));

    public static final RegistryObject<ItemManaweaverWand> MANAWEAVER_WAND = ITEMS.register("manaweaver_wand", () -> new ItemManaweaverWand(new Item.Properties().durability(25)));

    public static final RegistryObject<ItemManaweaverWand> MANAWEAVER_WAND_ADVANCED = ITEMS.register("manaweaver_wand_advanced", () -> new ItemManaweaverWand());

    public static final RegistryObject<ItemManaweaverWand> MANAWEAVER_WAND_IMPROVISED = ITEMS.register("improvised_manaweaver_wand", () -> new ItemManaweaverWand(new Item.Properties().durability(5)));

    public static final RegistryObject<ItemManaweaveBottle> MANAWEAVE_BOTTLE = ITEMS.register("manaweave_bottle", () -> new ItemManaweaveBottle());

    public static final RegistryObject<ItemModifierBook> MODIFIER_BOOK = ITEMS.register("modifier_book", () -> new ItemModifierBook());

    public static final RegistryObject<ItemRunescribingRecipe> RECIPE_SCRAP_RUNESCRIBING = ITEMS.register("runescribing_recipe_paper", () -> new ItemRunescribingRecipe());

    public static final RegistryObject<ItemManaweavingPattern> RECIPE_SCRAP_MANAWEAVING_PATTERN = ITEMS.register("manaweaving_pattern_recipe_paper", () -> new ItemManaweavingPattern());

    public static final RegistryObject<ItemThaumaturgicLink> THAUMATURGIC_LINK = ITEMS.register("thaumaturgic_link", () -> new ItemThaumaturgicLink());

    public static final RegistryObject<ItemThaumaturgicCompass> THAUMATURGIC_COMPASS = ITEMS.register("thaumaturgic_compass", () -> new ItemThaumaturgicCompass());

    public static final RegistryObject<ItemPlayerCharm> PLAYER_CHARM = ITEMS.register("player_charm", () -> new ItemPlayerCharm());

    public static final RegistryObject<TieredItem> RITUAL_FOCUS_MINOR = ITEMS.register("ritual_focus_minor", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> RITUAL_FOCUS_LESSER = ITEMS.register("ritual_focus_lesser", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> RITUAL_FOCUS_GREATER = ITEMS.register("ritual_focus_greater", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemPractitionersPouch> PRACTITIONERS_POUCH = ITEMS.register("practitioners_pouch", () -> new ItemPractitionersPouch());

    public static final RegistryObject<MithionsMagnificentMbag> MITHIONS_MAGNIFICENT_MBAG = ITEMS.register("mithions_magnificent_mbag", () -> new MithionsMagnificentMbag());

    public static final RegistryObject<ItemPractitionersPatch> PATCH_DEPTH = ITEMS.register("patch_depth", () -> new ItemPractitionersPatch(PractitionersPouchPatches.DEPTH, 1, true));

    public static final RegistryObject<ItemPractitionersPatch> PATCH_DEPTH_2 = ITEMS.register("patch_depth_2", () -> new ItemPractitionersPatch(PractitionersPouchPatches.DEPTH, 2, true));

    public static final RegistryObject<ItemPractitionersPatch> PATCH_SPEED = ITEMS.register("patch_speed", () -> new ItemPractitionersPatch(PractitionersPouchPatches.SPEED, 1, false));

    public static final RegistryObject<ItemPractitionersPatch> PATCH_SPEED_2 = ITEMS.register("patch_speed_2", () -> new ItemPractitionersPatch(PractitionersPouchPatches.SPEED, 2, false));

    public static final RegistryObject<ItemPractitionersPatch> PATCH_SPEED_3 = ITEMS.register("patch_speed_3", () -> new ItemPractitionersPatch(PractitionersPouchPatches.SPEED, 3, false));

    public static final RegistryObject<ItemPractitionersPatch> PATCH_GLYPH = ITEMS.register("patch_glyph", () -> new ItemPractitionersPatch(PractitionersPouchPatches.GLYPH, 1, true));

    public static final RegistryObject<ItemPractitionersPatch> PATCH_MOTE = ITEMS.register("patch_mote", () -> new ItemPractitionersPatch(PractitionersPouchPatches.MOTE, 1, true));

    public static final RegistryObject<ItemPractitionersPatch> PATCH_CONVEYANCE = ITEMS.register("patch_conveyance", () -> new ItemPractitionersPatch(PractitionersPouchPatches.CONVEYANCE, 1, true));

    public static final RegistryObject<ItemPractitionersPatch> PATCH_RIFT = ITEMS.register("patch_rift", () -> new ItemPractitionersPatch(PractitionersPouchPatches.RIFT, 1, true));

    public static final RegistryObject<ItemPractitionersPatch> PATCH_WEAVE = ITEMS.register("patch_weave", () -> new ItemPractitionersPatch(PractitionersPouchPatches.WEAVE, 1, true));

    public static final RegistryObject<ItemPractitionersPatch> PATCH_COLLECTION = ITEMS.register("patch_collection", () -> new ItemPractitionersPatch(PractitionersPouchPatches.COLLECTION, 1, true));

    public static final RegistryObject<ItemPractitionersPatch> PATCH_VOID = ITEMS.register("patch_void", () -> new ItemPractitionersPatch(PractitionersPouchPatches.VOID, 1, true));

    public static final RegistryObject<ItemBookOfRote> ROTE_BOOK = ITEMS.register("book_of_rote", () -> new ItemBookOfRote());

    public static final RegistryObject<TieredItem> CLAY_RUNE_PLATE = ITEMS.register("rune_clay_plate", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemRunePattern> RUNE_PATTERN = ITEMS.register("rune_pattern", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> RUNESMITH_HAMMER = ITEMS.register("runesmith_hammer", () -> new TieredItem(new Item.Properties().durability(512)).setSneakBypass());

    public static final RegistryObject<TieredItem> RUNESMITH_CHISEL = ITEMS.register("runesmith_chisel", () -> new TieredItem(new Item.Properties().durability(256)));

    public static final RegistryObject<TieredItem> RUNIC_SILK = ITEMS.register("runic_silk", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemMetalRitualRune> RUNE_RITUAL_METAL = ITEMS.register("rune_ritual_metal", () -> new ItemMetalRitualRune());

    public static final RegistryObject<ItemRunePattern> RUNE_PATTERN_RITUAL_METAL = ITEMS.register("rune_pattern_ritual_metal", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> RUNE_DEFENSE = ITEMS.register("rune_defense", () -> new ItemRune());

    public static final RegistryObject<ItemRunePattern> RUNE_PATTERN_DEFENSE = ITEMS.register("rune_pattern_defense", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> RUNE_AURA = ITEMS.register("rune_aura", () -> new ItemRune());

    public static final RegistryObject<ItemRunePattern> RUNE_PATTERN_AURA = ITEMS.register("rune_pattern_aura", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> RUNE_PROJECTION = ITEMS.register("rune_projection", () -> new ItemRune());

    public static final RegistryObject<ItemRunePattern> RUNE_PATTERN_PROJECTION = ITEMS.register("rune_pattern_projection", () -> new ItemRunePattern());

    public static final RegistryObject<ItemRuneMarking> RUNE_MARKING = ITEMS.register("rune_marking", () -> new ItemRuneMarking());

    public static final RegistryObject<ItemRunePattern> RUNE_PATTERN_MARKING = ITEMS.register("rune_pattern_marking", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> CHARM_PATTERN_BASE = ITEMS.register("charm_pattern_base", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> CHARM_PATTERN = ITEMS.register("charm_pattern", () -> new ItemRune());

    public static final RegistryObject<TieredItem> RUNE_PATTERN_AIR = ITEMS.register("rune_pattern_air", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> RUNE_AIR = ITEMS.register("rune_air", () -> new ItemRune());

    public static final RegistryObject<TieredItem> RUNE_PATTERN_EARTH = ITEMS.register("rune_pattern_earth", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> RUNE_EARTH = ITEMS.register("rune_earth", () -> new ItemRune());

    public static final RegistryObject<TieredItem> RUNE_PATTERN_WATER = ITEMS.register("rune_pattern_water", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> RUNE_WATER = ITEMS.register("rune_water", () -> new ItemRune());

    public static final RegistryObject<TieredItem> RUNE_PATTERN_FIRE = ITEMS.register("rune_pattern_fire", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> RUNE_FIRE = ITEMS.register("rune_fire", () -> new ItemRune());

    public static final RegistryObject<TieredItem> RUNE_PATTERN_ENDER = ITEMS.register("rune_pattern_ender", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> RUNE_ENDER = ITEMS.register("rune_ender", () -> new ItemRune());

    public static final RegistryObject<TieredItem> RUNE_PATTERN_ARCANE = ITEMS.register("rune_pattern_arcane", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> RUNE_ARCANE = ITEMS.register("rune_arcane", () -> new ItemRune());

    public static final RegistryObject<ItemSpectralElytra> SPECTRAL_ELYTRA = ITEMS.register("spectral_elytra", () -> new ItemSpectralElytra());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_BLACK = ITEMS.register("stone_rune_black", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_BLANK = ITEMS.register("stone_rune_blank", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_BLUE = ITEMS.register("stone_rune_blue", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_BROWN = ITEMS.register("stone_rune_brown", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_CYAN = ITEMS.register("stone_rune_cyan", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_GRAY = ITEMS.register("stone_rune_gray", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_GREEN = ITEMS.register("stone_rune_green", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_LIGHT_BLUE = ITEMS.register("stone_rune_light_blue", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_LIGHT_GRAY = ITEMS.register("stone_rune_light_gray", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_LIME = ITEMS.register("stone_rune_lime", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_MAGENTA = ITEMS.register("stone_rune_magenta", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_ORANGE = ITEMS.register("stone_rune_orange", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_PINK = ITEMS.register("stone_rune_pink", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_PURPLE = ITEMS.register("stone_rune_purple", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_RED = ITEMS.register("stone_rune_red", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_WHITE = ITEMS.register("stone_rune_white", () -> new ItemStoneRune());

    public static final RegistryObject<ItemStoneRune> STONE_RUNE_YELLOW = ITEMS.register("stone_rune_yellow", () -> new ItemStoneRune());

    public static final RegistryObject<ItemPilgrimStaff> STAFF_PILGRIM = ITEMS.register("pilgrim_staff", () -> new ItemPilgrimStaff());

    public static final RegistryObject<ItemSightUnguent> SIGHT_UNGUENT = ITEMS.register("eldrin_sight_unguent", () -> new ItemSightUnguent(new Item.Properties(), 1));

    public static final RegistryObject<ItemSightUnguent> WELLSPRING_SIGHT_UNGUENT = ITEMS.register("wellspring_sight_unguent", () -> new ItemSightUnguent(new Item.Properties(), 0));

    public static final RegistryObject<ItemDowsingRod> WELLSPRING_DOWSING_ROD = ITEMS.register("dowsing_rod", () -> new ItemDowsingRod());

    public static final RegistryObject<ItemTransitoryTunnel> TRANSITORY_TUNNEL = ITEMS.register("transitory_tunnel", () -> new ItemTransitoryTunnel());

    public static final RegistryObject<TieredItem> VINTEUM_DUST = ITEMS.register("vinteum_dust", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> VINTEUM_COATED_IRON = ITEMS.register("vinteum_coated_iron", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> VINTEUM_INGOT = ITEMS.register("vinteum_ingot", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> VINTEUM_INGOT_SUPERHEATED = ITEMS.register("superheated_vinteum_ingot", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> PURIFIED_VINTEUM_DUST = ITEMS.register("purified_vinteum_dust", () -> new ItemPurifiedVinteumDust(new Item.Properties()));

    public static final RegistryObject<Item> PURIFIED_VINTEUM_COATED_IRON = ITEMS.register("purified_vinteum_coated_iron", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> PURIFIED_VINTEUM_INGOT = ITEMS.register("purified_vinteum_ingot", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> VINTEUM_INGOT_PURIFIED_SUPERHEATED = ITEMS.register("superheated_purified_vinteum_ingot", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemStaff> WAND_AMETHYST = ITEMS.register("wand_amethyst", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_AUM = ITEMS.register("wand_aum", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_CERUBLOSSOM = ITEMS.register("wand_cerublossom", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_CHIMERITE = ITEMS.register("wand_chimerite", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_DESERTNOVA = ITEMS.register("wand_desertnova", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_EMERALD = ITEMS.register("wand_emerald", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_GLASS = ITEMS.register("wand_glass", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_GOLD = ITEMS.register("wand_gold", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_IRON = ITEMS.register("wand_iron", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_LAPIS = ITEMS.register("wand_lapis", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_NETHERQUARTZ = ITEMS.register("wand_netherquartz", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_PRISMARINECRYSTAL = ITEMS.register("wand_prismarinecrystal", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_PRISMARINESHARD = ITEMS.register("wand_prismarineshard", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_REDSTONE = ITEMS.register("wand_redstone", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_SKULL = ITEMS.register("wand_skull", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_SKULL_ALT = ITEMS.register("wand_skull_alt", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_TARMA = ITEMS.register("wand_tarma", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_VINTEUM = ITEMS.register("wand_vinteum", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemStaff> WAND_WAKEBLOOM = ITEMS.register("wand_wakebloom", () -> new ItemStaff(2.0F, -2.0F));

    public static final RegistryObject<ItemWizardChalk> WIZARD_CHALK = ITEMS.register("wizard_chalk", () -> new ItemWizardChalk());

    public static final RegistryObject<ItemWandOfCloning> WAND_CLONE = ITEMS.register("wand_clone", () -> new ItemWandOfCloning());

    public static final RegistryObject<ItemFluidJug> FLUID_JUG = ITEMS.register("fluid_jug", () -> new ItemFluidJug(BlockInit.FLUID_JUG.get()));

    public static final RegistryObject<ItemInfiniteFluidJug> FLUID_JUG_INFINITE_WATER = ITEMS.register("fluid_jug_infinite_water", () -> new ItemInfiniteFluidJug(BlockInit.FLUID_JUG_INFINITE_WATER.get(), Fluids.WATER, false));

    public static final RegistryObject<ItemInfiniteFluidJug> FLUID_JUG_INFINITE_LAVA = ITEMS.register("fluid_jug_infinite_lava", () -> new ItemInfiniteFluidJug(BlockInit.FLUID_JUG_INFINITE_LAVA.get(), Fluids.LAVA, true));

    public static final RegistryObject<ItemRunicMalus> RUNIC_MALUS = ITEMS.register("runic_malus", () -> new ItemRunicMalus());

    public static final RegistryObject<ItemAstroBlade> ASTRO_BLADE = ITEMS.register("astro_blade", () -> new ItemAstroBlade());

    public static final RegistryObject<ItemHellfireTrident> HELLFIRE_TRIDENT = ITEMS.register("hellfire_trident", () -> new ItemHellfireTrident());

    public static final RegistryObject<ScrollOfIcarianFlight> ICARIAN_FLIGHT = ITEMS.register("icarian_flight", () -> new ScrollOfIcarianFlight());

    public static final RegistryObject<TieredItem> WITHERBONE = ITEMS.register("witherbone", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> LIVING_FLAME = ITEMS.register("living_flame", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> IRONBARK = ITEMS.register("ironbark", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> ELDRIN_RIFT = ITEMS.register("eldrin_rift", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<Item> RESONATING_LUMP = ITEMS.register("resonating_lump", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RESONATING_DUST = ITEMS.register("resonating_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<ItemClayMug> CLAY_MUG = ITEMS.register("clay_mug", () -> new ItemClayMug());

    public static final RegistryObject<ItemManaTea> MANA_TEA = ITEMS.register("mana_coffee", () -> new ItemManaTea());

    public static final RegistryObject<MoteItem> MOTE_AIR = ITEMS.register("mote_air", () -> new MoteItem(Affinity.WIND, new Item.Properties()));

    public static final RegistryObject<MoteItem> MOTE_EARTH = ITEMS.register("mote_earth", () -> new MoteItem(Affinity.EARTH, new Item.Properties()));

    public static final RegistryObject<MoteItem> MOTE_FIRE = ITEMS.register("mote_fire", () -> new MoteItem(Affinity.FIRE, new Item.Properties()));

    public static final RegistryObject<MoteItem> MOTE_WATER = ITEMS.register("mote_water", () -> new MoteItem(Affinity.WATER, new Item.Properties()));

    public static final RegistryObject<MoteItem> MOTE_ARCANE = ITEMS.register("mote_arcane", () -> new MoteItem(Affinity.ARCANE, new Item.Properties()));

    public static final RegistryObject<MoteItem> MOTE_ENDER = ITEMS.register("mote_ender", () -> new MoteItem(Affinity.ENDER, new Item.Properties()));

    public static final RegistryObject<MoteItem> GREATER_MOTE_AIR = ITEMS.register("greater_mote_air", () -> new MoteItem(Affinity.WIND, new Item.Properties()));

    public static final RegistryObject<MoteItem> GREATER_MOTE_EARTH = ITEMS.register("greater_mote_earth", () -> new MoteItem(Affinity.EARTH, new Item.Properties()));

    public static final RegistryObject<MoteItem> GREATER_MOTE_FIRE = ITEMS.register("greater_mote_fire", () -> new MoteItem(Affinity.FIRE, new Item.Properties()));

    public static final RegistryObject<MoteItem> GREATER_MOTE_WATER = ITEMS.register("greater_mote_water", () -> new MoteItem(Affinity.WATER, new Item.Properties()));

    public static final RegistryObject<MoteItem> GREATER_MOTE_ARCANE = ITEMS.register("greater_mote_arcane", () -> new MoteItem(Affinity.ARCANE, new Item.Properties()));

    public static final RegistryObject<MoteItem> GREATER_MOTE_ENDER = ITEMS.register("greater_mote_ender", () -> new MoteItem(Affinity.ENDER, new Item.Properties()));

    public static final RegistryObject<MoteItem> GREATER_MOTE_LIGHTNING = ITEMS.register("greater_mote_lightning", () -> new MoteItem(Affinity.LIGHTNING, new Item.Properties()));

    public static final RegistryObject<MoteItem> GREATER_MOTE_ICE = ITEMS.register("greater_mote_ice", () -> new MoteItem(Affinity.ICE, new Item.Properties()));

    public static final RegistryObject<MoteItem> GREATER_MOTE_HELLFIRE = ITEMS.register("greater_mote_hellfire", () -> new MoteItem(Affinity.HELLFIRE, new Item.Properties()));

    public static final RegistryObject<ItemTeleportToBedCharm> BED_CHARM = ITEMS.register("bed_charm", () -> new ItemTeleportToBedCharm());

    public static final RegistryObject<ItemCancelFireCharm> BURN_CHARM = ITEMS.register("burn_charm", () -> new ItemCancelFireCharm());

    public static final RegistryObject<ItemCancelDrownCharm> DROWN_CHARM = ITEMS.register("drown_charm", () -> new ItemCancelDrownCharm());

    public static final RegistryObject<TieredItem> BRIMSTONE_CHARM = ITEMS.register("brimstone_charm", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemCancelFatalFallCharm> FALL_CHARM = ITEMS.register("fall_charm", () -> new ItemCancelFatalFallCharm());

    public static final RegistryObject<ItemWorldCharm> WORLD_CHARM = ITEMS.register("world_charm", () -> new ItemWorldCharm());

    public static final RegistryObject<ItemContingencyCharm> CC_DAMAGE_MINOR = ITEMS.register("damage_charm_minor", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.DAMAGE, false));

    public static final RegistryObject<ItemContingencyCharm> CC_DEATH_MINOR = ITEMS.register("death_charm_minor", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.DEATH, false));

    public static final RegistryObject<ItemContingencyCharm> CC_RAID_MINOR = ITEMS.register("raid_charm_minor", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.FACTION_RAID, false));

    public static final RegistryObject<ItemContingencyCharm> CC_FALL_MINOR = ITEMS.register("pre_fall_charm_minor", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.FALL, false));

    public static final RegistryObject<ItemContingencyCharm> CC_LOW_CR_MINOR = ITEMS.register("low_cr_charm_minor", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.LOW_CASTING_RESOURCE, false));

    public static final RegistryObject<ItemContingencyCharm> CC_LOW_HEALTH_MINOR = ITEMS.register("low_health_charm_minor", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.LOW_HEALTH, false));

    public static final RegistryObject<ItemContingencyCharm> CC_SUFFOCATE_MINOR = ITEMS.register("suffocation_charm_minor", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.SUFFOCATION, false));

    public static final RegistryObject<ItemContingencyCharm> CC_DAMAGE_MAJOR = ITEMS.register("damage_charm_major", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.DAMAGE, false, 20));

    public static final RegistryObject<ItemContingencyCharm> CC_DEATH_MAJOR = ITEMS.register("death_charm_major", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.DEATH, false));

    public static final RegistryObject<ItemContingencyCharm> CC_RAID_MAJOR = ITEMS.register("raid_charm_major", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.FACTION_RAID, false, 3));

    public static final RegistryObject<ItemContingencyCharm> CC_FALL_MAJOR = ITEMS.register("pre_fall_charm_major", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.FALL, false, 50));

    public static final RegistryObject<ItemContingencyCharm> CC_LOW_CR_MAJOR = ITEMS.register("low_cr_charm_major", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.LOW_CASTING_RESOURCE, false, 5));

    public static final RegistryObject<ItemContingencyCharm> CC_LOW_HEALTH_MAJOR = ITEMS.register("low_health_charm_major", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.LOW_HEALTH, false, 3));

    public static final RegistryObject<ItemContingencyCharm> CC_SUFFOCATE_MAJOR = ITEMS.register("suffocation_charm_major", () -> new ItemContingencyCharm(ItemContingencyCharm.ContingencyEvent.SUFFOCATION, false, 5));

    public static final RegistryObject<ItemArcaneCrown> ARCANE_CROWN = ITEMS.register("arcane_crown", () -> new ItemArcaneCrown());

    public static final RegistryObject<BrokenMageArmor> BROKEN_HOOD = ITEMS.register("broken_hood", () -> new BrokenMageArmor(ArmorItem.Type.HELMET));

    public static final RegistryObject<BrokenMageArmor> BROKEN_ROBES = ITEMS.register("broken_robes", () -> new BrokenMageArmor(ArmorItem.Type.CHESTPLATE));

    public static final RegistryObject<BrokenMageArmor> BROKEN_LEGGINGS = ITEMS.register("broken_leggings", () -> new BrokenMageArmor(ArmorItem.Type.LEGGINGS));

    public static final RegistryObject<BrokenMageArmor> BROKEN_BOOTS = ITEMS.register("broken_boots", () -> new BrokenMageArmor(ArmorItem.Type.BOOTS));

    public static final RegistryObject<TieredItem> INFUSED_SILK = ITEMS.register("infused_silk", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> INFUSED_THREAD = ITEMS.register("infused_thread", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<DyeableMageArmor> MAGE_HOOD = ITEMS.register("mage_hood", () -> new DyeableMageArmor(MAArmorMaterial.INFUSED_SILK, ArmorItem.Type.HELMET, new Item.Properties(), 0.25F, 0.01F));

    public static final RegistryObject<DyeableMageArmor> MAGE_ROBES = ITEMS.register("mage_robes", () -> new DyeableMageArmor(MAArmorMaterial.INFUSED_SILK, ArmorItem.Type.CHESTPLATE, new Item.Properties(), 0.25F, 0.01F));

    public static final RegistryObject<DyeableMageArmor> MAGE_LEGGINGS = ITEMS.register("mage_leggings", () -> new DyeableMageArmor(MAArmorMaterial.INFUSED_SILK, ArmorItem.Type.LEGGINGS, new Item.Properties(), 0.25F, 0.01F));

    public static final RegistryObject<DyeableMageArmor> MAGE_BOOTS = ITEMS.register("mage_boots", () -> new DyeableMageArmor(MAArmorMaterial.INFUSED_SILK, ArmorItem.Type.BOOTS, new Item.Properties(), 0.25F, 0.01F));

    public static final RegistryObject<TieredItem> PATTERN_VINTEUM_NEEDLE = ITEMS.register("pattern_vinteum_needle", () -> new TieredItem(new Item.Properties().durability(4)));

    public static final RegistryObject<TieredItem> SORCEROUS_SEWING_SET = ITEMS.register("sorcerous_sewing_set", () -> new TieredItem(new Item.Properties().durability(4)));

    public static final RegistryObject<TieredItem> VINTEUM_NEEDLE = ITEMS.register("vinteum_needle", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<BoneArmorItem> BONE_ARMOR_HEAD = ITEMS.register("bone_armor_head", () -> new BoneArmorItem(MAArmorMaterial.BONE, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<BoneArmorItem> BONE_ARMOR__CHEST = ITEMS.register("bone_armor_chest", () -> new BoneArmorItem(MAArmorMaterial.BONE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<BoneArmorItem> BONE_ARMOR__LEGGINGS = ITEMS.register("bone_armor_leggings", () -> new BoneArmorItem(MAArmorMaterial.BONE, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<BoneArmorItem> BONE_ARMOR__BOOTS = ITEMS.register("bone_armor_boots", () -> new BoneArmorItem(MAArmorMaterial.BONE, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<FeyArmorItem> FEY_ARMOR_HEAD = ITEMS.register("fey_armor_head", () -> new FeyArmorItem(MAArmorMaterial.FEY, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<FeyArmorItem> FEY_ARMOR_CHEST = ITEMS.register("fey_armor_chest", () -> new FeyArmorItem(MAArmorMaterial.FEY, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<FeyArmorItem> FEY_ARMOR_LEGGINGS = ITEMS.register("fey_armor_leggings", () -> new FeyArmorItem(MAArmorMaterial.FEY, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<FeyArmorItem> FEY_ARMOR_BOOTS = ITEMS.register("fey_armor_boots", () -> new FeyArmorItem(MAArmorMaterial.FEY, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<DemonArmorItem> DEMON_ARMOR_HEAD = ITEMS.register("demon_armor_head", () -> new DemonArmorItem(MAArmorMaterial.DEMON, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<DemonArmorItem> DEMON_ARMOR_CHEST = ITEMS.register("demon_armor_chest", () -> new DemonArmorItem(MAArmorMaterial.DEMON, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<DemonArmorItem> DEMON_ARMOR_LEGGINGS = ITEMS.register("demon_armor_leggings", () -> new DemonArmorItem(MAArmorMaterial.DEMON, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<DemonArmorItem> DEMON_ARMOR_BOOTS = ITEMS.register("demon_armor_boots", () -> new DemonArmorItem(MAArmorMaterial.DEMON, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<CouncilArmorItem> COUNCIL_ARMOR_HEAD = ITEMS.register("council_armor_head", () -> new CouncilArmorItem(MAArmorMaterial.COUNCIL, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<CouncilArmorItem> COUNCIL_ARMOR__CHEST = ITEMS.register("council_armor_chest", () -> new CouncilArmorItem(MAArmorMaterial.COUNCIL, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<CouncilArmorItem> COUNCIL_ARMOR__LEGGINGS = ITEMS.register("council_armor_leggings", () -> new CouncilArmorItem(MAArmorMaterial.COUNCIL, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<CouncilArmorItem> COUNCIL_ARMOR__BOOTS = ITEMS.register("council_armor_boots", () -> new CouncilArmorItem(MAArmorMaterial.COUNCIL, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<ItemBookOfRote> PUNKIN_STAFF = ITEMS.register("pumpkin_staff", () -> new ItemBookOfRote());

    public static final RegistryObject<AllfatherAxe> ALLFATHER_AXE = ITEMS.register("allfather_axe", () -> new AllfatherAxe());

    public static final RegistryObject<AllfatherAxeControl> ALLFATHER_AXE_CONTROL = ITEMS.register("allfather_axe_control", () -> new AllfatherAxeControl());

    public static final RegistryObject<Item> DEMON_LORD_AXE = ITEMS.register("demon_lord_axe", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> DEMON_LORD_SWORD = ITEMS.register("demon_lord_sword", () -> new Item(new Item.Properties().fireResistant()));

    public static final RegistryObject<ItemBoundSword> BOUND_SWORD = ITEMS.register("bound_sword", () -> new ItemBoundSword(2.0F, -1.0F));

    public static final RegistryObject<ItemBoundAxe> BOUND_AXE = ITEMS.register("bound_axe", () -> new ItemBoundAxe(3.0F, -3.0F));

    public static final RegistryObject<ItemBoundBow> BOUND_BOW = ITEMS.register("bound_bow", () -> new ItemBoundBow());

    public static final RegistryObject<ItemBoundShield> BOUND_SHIELD = ITEMS.register("bound_shield", () -> new ItemBoundShield());

    public static final RegistryObject<ItemShuriken> SKELETON_SHURIKEN = ITEMS.register("skeleton_assassin_shuriken", () -> new ItemShuriken(new Item.Properties()));

    public static final RegistryObject<Item> HAT_FEZ = ITEMS.register("hat_fez", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HAT_TALL_COWBOY = ITEMS.register("hat_tall_cowboy", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HAT_LARGE_WITCH = ITEMS.register("hat_large_witch", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HAT_STYLED_SKULL = ITEMS.register("hat_styled_skull", () -> new Item(new Item.Properties()));

    public static final RegistryObject<TieredItem> MUNDANE_BRACELET = ITEMS.register("mundane_bracelet", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemRunePattern> MUNDANE_BRACELET_PATTERN = ITEMS.register("mundane_bracelet_pattern", () -> new ItemRunePattern(new Item.Properties()));

    public static final RegistryObject<ItemAntidoteBracelet> ANTIDOTE_BRACELET = ITEMS.register("antidote_bracelet", () -> new ItemAntidoteBracelet());

    public static final RegistryObject<ItemEmberglowBracelet> EMBERGLOW_BRACELET = ITEMS.register("emberglow_bracelet", () -> new ItemEmberglowBracelet(new Item.Properties().fireResistant().setNoRepair().rarity(Rarity.UNCOMMON), 1000.0F));

    public static final RegistryObject<ItemEldrinBracelet> ELDRIN_BRACELET = ITEMS.register("eldrin_bracelet", () -> new ItemEldrinBracelet(new Item.Properties().setNoRepair().rarity(Rarity.UNCOMMON), 500.0F));

    public static final RegistryObject<ItemTrickeryBracelet> TRICKERY_BRACELET = ITEMS.register("trickery_bracelet", () -> new ItemTrickeryBracelet());

    public static final RegistryObject<TieredItem> MUNDANE_RING = ITEMS.register("mundane_ring", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> BREAK_RING_LESSER = ITEMS.register("break_ring_lesser", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> BREAK_RING_GREATER = ITEMS.register("break_ring_greater", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> COLLECTOR_RING_LESSER = ITEMS.register("collector_ring_lesser", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> COLLECTOR_RING_GREATER = ITEMS.register("collector_ring_greater", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemRunePattern> MUNDANE_RING_PATTERN = ITEMS.register("mundane_ring_pattern", () -> new ItemRunePattern(new Item.Properties()));

    public static final RegistryObject<FortuneRing> FORTUNE_RING_MINOR = ITEMS.register("fortune_ring_minor", () -> new FortuneRing(new Item.Properties().rarity(Rarity.UNCOMMON), 250.0F));

    public static final RegistryObject<FortuneRing> FORTUNE_RING = ITEMS.register("fortune_ring", () -> new FortuneRing(new Item.Properties().rarity(Rarity.RARE), 500.0F));

    public static final RegistryObject<FortuneRing> FORTUNE_RING_GREATER = ITEMS.register("fortune_ring_greater", () -> new FortuneRing(new Item.Properties().rarity(Rarity.EPIC), 750.0F));

    public static final RegistryObject<SpellModifierRing> SILK_TOUCH_RING = ITEMS.register("silk_touch_ring", () -> new SpellModifierRing(new Item.Properties().rarity(Rarity.RARE), 2500.0F));

    public static final RegistryObject<SpellModifierRing> BLINK_PRECISION_RING = ITEMS.register("blink_precision_ring", () -> new SpellModifierRing(new Item.Properties().rarity(Rarity.EPIC), 1000.0F));

    public static final RegistryObject<SpellModifierRing> AIR_CAST_RING = ITEMS.register("air_cast_ring", () -> new SpellModifierRing(new Item.Properties().rarity(Rarity.EPIC), 1500.0F));

    public static final RegistryObject<FactionSpecificSpellModifierRing> BONE_RING = ITEMS.register("bone_ring", () -> new FactionSpecificSpellModifierRing(new Item.Properties().rarity(Rarity.EPIC), 1500.0F, FactionIDs.UNDEAD));

    public static final RegistryObject<TieredItem> BELT_BUCKLE = ITEMS.register("belt_buckle", () -> new ItemRune());

    public static final RegistryObject<TieredItem> BELT_BUCKLE_PATTERN = ITEMS.register("pattern_belt_buckle", () -> new ItemRunePattern());

    public static final RegistryObject<TieredItem> BELT_AFFINITY_LOCK = ITEMS.register("affinity_lock_belt", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> BELT_SELFISHNESS = ITEMS.register("selfish_belt", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<TieredItem> MUNDANE_AMULET_PATTERN = ITEMS.register("mundane_amulet_pattern", () -> new ItemRunePattern(new Item.Properties()));

    public static final RegistryObject<TieredItem> MUNDANE_AMULET = ITEMS.register("mundane_amulet", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemWardingAmulet> WARDING_AMULET = ITEMS.register("warding_amulet", () -> new ItemWardingAmulet(new Item.Properties(), 1000.0F));

    public static final RegistryObject<TieredItem> BATTLEMAGE_AMULET = ITEMS.register("battlemage_amulet", () -> new TieredItem(new Item.Properties()));

    public static final RegistryObject<ItemEnderguardAmulet> ENDERGUARD_AMULET = ITEMS.register("enderguard_amulet", () -> new ItemEnderguardAmulet(new Item.Properties(), 1000.0F));

    public static final RegistryObject<ItemWitherguardAmulet> WITHERGUARD_AMULET = ITEMS.register("witherguard_amulet", () -> new ItemWitherguardAmulet(new Item.Properties(), 150.0F));

    public static final RegistryObject<RecordItem> DANCE_MIX = ITEMS.register("strange_record", () -> new RecordItem(13, () -> Music.Construct.DANCE_MIX, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 3293));

    public static final RegistryObject<BellOfBidding> BELL_OF_BIDDING = ITEMS.register("bell_of_bidding", () -> new BellOfBidding());

    public static final RegistryObject<ItemRune> CONSTRUCT_RUNE_ROD = ITEMS.register("constructs/rune_rod", () -> new ItemRune(new Item.Properties()));

    public static final RegistryObject<ItemRune> CONSTRUCT_RUNE_TORSO = ITEMS.register("constructs/rune_torso", () -> new ItemRune(new Item.Properties()));

    public static final RegistryObject<ItemRune> CONSTRUCT_RUNE_HIPS = ITEMS.register("constructs/rune_hips", () -> new ItemRune(new Item.Properties()));

    public static final RegistryObject<ItemRune> CONSTRUCT_RUNE_HAMMER = ITEMS.register("constructs/rune_hammer", () -> new ItemRune(new Item.Properties()));

    public static final RegistryObject<ItemRune> CONSTRUCT_RUNE_CLAW = ITEMS.register("constructs/rune_claw", () -> new ItemRune(new Item.Properties()));

    public static final RegistryObject<ItemRune> CONSTRUCT_RUNE_HEAD = ITEMS.register("constructs/rune_head", () -> new ItemRune(new Item.Properties()));

    public static final RegistryObject<ItemRune> CONSTRUCT_RUNE_AXE = ITEMS.register("constructs/rune_axe", () -> new ItemRune(new Item.Properties()));

    public static final RegistryObject<ItemRunePattern> CONSTRUCT_RUNE_PATTERN_ROD = ITEMS.register("constructs/rune_pattern_rod", () -> new ItemRunePattern(new Item.Properties()));

    public static final RegistryObject<ItemRunePattern> CONSTRUCT_RUNE_PATTERN_TORSO = ITEMS.register("constructs/rune_pattern_torso", () -> new ItemRunePattern(new Item.Properties()));

    public static final RegistryObject<ItemRunePattern> CONSTRUCT_RUNE_PATTERN_HIPS = ITEMS.register("constructs/rune_pattern_hips", () -> new ItemRunePattern(new Item.Properties()));

    public static final RegistryObject<ItemRunePattern> CONSTRUCT_RUNE_PATTERN_HAMMER = ITEMS.register("constructs/rune_pattern_hammer", () -> new ItemRunePattern(new Item.Properties()));

    public static final RegistryObject<ItemRunePattern> CONSTRUCT_RUNE_PATTERN_CLAW = ITEMS.register("constructs/rune_pattern_claw", () -> new ItemRunePattern(new Item.Properties()));

    public static final RegistryObject<ItemRunePattern> CONSTRUCT_RUNE_PATTERN_HEAD = ITEMS.register("constructs/rune_pattern_head", () -> new ItemRunePattern(new Item.Properties()));

    public static final RegistryObject<ItemRunePattern> CONSTRUCT_RUNE_PATTERN_AXE = ITEMS.register("constructs/rune_pattern_axe", () -> new ItemRunePattern(new Item.Properties()));

    public static final RegistryObject<ConstructPartBasicHead> CONSTRUCT_BASIC_HEAD_WICKERWOOD = ITEMS.register("constructs/construct_basic_head_wickerwood", () -> new ConstructPartBasicHead(ConstructMaterial.WICKERWOOD));

    public static final RegistryObject<ConstructPartBasicTorso> CONSTRUCT_BASIC_TORSO_WICKERWOOD = ITEMS.register("constructs/construct_basic_torso_wickerwood", () -> new ConstructPartBasicTorso(ConstructMaterial.WICKERWOOD));

    public static final RegistryObject<ConstructPartStorageTorso> CONSTRUCT_STORAGE_TORSO_WICKERWOOD = ITEMS.register("constructs/construct_storage_torso_wickerwood", () -> new ConstructPartStorageTorso(ConstructMaterial.WICKERWOOD));

    public static final RegistryObject<ConstructPartBasicLegs> CONSTRUCT_BASIC_LEGS_WICKERWOOD = ITEMS.register("constructs/construct_basic_legs_wickerwood", () -> new ConstructPartBasicLegs(ConstructMaterial.WICKERWOOD));

    public static final RegistryObject<ConstructPartFishingArmLeft> CONSTRUCT_FISHING_ARM_LEFT_WICKERWOOD = ITEMS.register("constructs/construct_fishing_rod_left_wickerwood", () -> new ConstructPartFishingArmLeft(ConstructMaterial.WICKERWOOD));

    public static final RegistryObject<ConstructPartFishingArmRight> CONSTRUCT_FISHING_ARM_RIGHT_WICKERWOOD = ITEMS.register("constructs/construct_fishing_rod_right_wickerwood", () -> new ConstructPartFishingArmRight(ConstructMaterial.WICKERWOOD));

    public static final RegistryObject<ConstructPartBladeArmLeft> CONSTRUCT_BLADE_ARM_LEFT_WICKERWOOD = ITEMS.register("constructs/construct_blade_arm_left_wickerwood", () -> new ConstructPartBladeArmLeft(ConstructMaterial.WICKERWOOD, 3, -2.4F));

    public static final RegistryObject<ConstructPartBladeArmRight> CONSTRUCT_BLADE_ARM_RIGHT_WICKERWOOD = ITEMS.register("constructs/construct_blade_arm_right_wickerwood", () -> new ConstructPartBladeArmRight(ConstructMaterial.WICKERWOOD, 3, -2.4F));

    public static final RegistryObject<ConstructPartHammerArmLeft> CONSTRUCT_HAMMER_ARM_LEFT_WICKERWOOD = ITEMS.register("constructs/construct_hammer_arm_left_wickerwood", () -> new ConstructPartHammerArmLeft(ConstructMaterial.WICKERWOOD, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartHammerArmRight> CONSTRUCT_HAMMER_ARM_RIGHT_WICKERWOOD = ITEMS.register("constructs/construct_hammer_arm_right_wickerwood", () -> new ConstructPartHammerArmRight(ConstructMaterial.WICKERWOOD, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartGrabberArmLeft> CONSTRUCT_GRABBER_ARM_LEFT_WICKERWOOD = ITEMS.register("constructs/construct_grabber_arm_left_wickerwood", () -> new ConstructPartGrabberArmLeft(ConstructMaterial.WICKERWOOD, 1));

    public static final RegistryObject<ConstructPartGrabberArmRight> CONSTRUCT_GRABBER_ARM_RIGHT_WICKERWOOD = ITEMS.register("constructs/construct_grabber_arm_right_wickerwood", () -> new ConstructPartGrabberArmRight(ConstructMaterial.WICKERWOOD, 1));

    public static final RegistryObject<ConstructPartAxeArmLeft> CONSTRUCT_AXE_ARM_LEFT_WICKERWOOD = ITEMS.register("constructs/construct_axe_arm_left_wickerwood", () -> new ConstructPartAxeArmLeft(ConstructMaterial.WICKERWOOD, 6.0F, -3.2F));

    public static final RegistryObject<ConstructPartAxeArmRight> CONSTRUCT_AXE_ARM_RIGHT_WICKERWOOD = ITEMS.register("constructs/construct_axe_arm_right_wickerwood", () -> new ConstructPartAxeArmRight(ConstructMaterial.WICKERWOOD, 6.0F, -3.2F));

    public static final RegistryObject<ConstructPartBasicHead> CONSTRUCT_BASIC_HEAD_WOOD = ITEMS.register("constructs/construct_basic_head_wood", () -> new ConstructPartBasicHead(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartSmartHead> CONSTRUCT_SMART_HEAD_WOOD = ITEMS.register("constructs/construct_smart_head_wood", () -> new ConstructPartSmartHead(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartHornHead> CONSTRUCT_HORN_HEAD_WOOD = ITEMS.register("constructs/construct_horn_head_wood", () -> new ConstructPartHornHead(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartBasicTorso> CONSTRUCT_BASIC_TORSO_WOOD = ITEMS.register("constructs/construct_basic_torso_wood", () -> new ConstructPartBasicTorso(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartBasicLegs> CONSTRUCT_BASIC_LEGS_WOOD = ITEMS.register("constructs/construct_basic_legs_wood", () -> new ConstructPartBasicLegs(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartReinforcedLegs> CONSTRUCT_REINFORCED_LEGS_WOOD = ITEMS.register("constructs/construct_reinforced_legs_wood", () -> new ConstructPartReinforcedLegs(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartEnderLegs> CONSTRUCT_ENDER_LEGS_WOOD = ITEMS.register("constructs/construct_ender_legs_wood", () -> new ConstructPartEnderLegs(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartRocketLegs> CONSTRUCT_ROCKET_LEGS_WOOD = ITEMS.register("constructs/construct_rocket_legs_wood", () -> new ConstructPartRocketLegs(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartFishingArmLeft> CONSTRUCT_FISHING_ARM_LEFT_WOOD = ITEMS.register("constructs/construct_fishing_rod_left_wood", () -> new ConstructPartFishingArmLeft(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartFishingArmRight> CONSTRUCT_FISHING_ARM_RIGHT_WOOD = ITEMS.register("constructs/construct_fishing_rod_right_wood", () -> new ConstructPartFishingArmRight(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartBladeArmLeft> CONSTRUCT_BLADE_ARM_LEFT_WOOD = ITEMS.register("constructs/construct_blade_arm_left_wood", () -> new ConstructPartBladeArmLeft(ConstructMaterial.WOOD, 3, -2.4F));

    public static final RegistryObject<ConstructPartBladeArmRight> CONSTRUCT_BLADE_ARM_RIGHT_WOOD = ITEMS.register("constructs/construct_blade_arm_right_wood", () -> new ConstructPartBladeArmRight(ConstructMaterial.WOOD, 3, -2.4F));

    public static final RegistryObject<ConstructPartHammerArmLeft> CONSTRUCT_HAMMER_ARM_LEFT_WOOD = ITEMS.register("constructs/construct_hammer_arm_left_wood", () -> new ConstructPartHammerArmLeft(ConstructMaterial.WOOD, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartHammerArmRight> CONSTRUCT_HAMMER_ARM_RIGHT_WOOD = ITEMS.register("constructs/construct_hammer_arm_right_wood", () -> new ConstructPartHammerArmRight(ConstructMaterial.WOOD, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartGrabberArmLeft> CONSTRUCT_GRABBER_ARM_LEFT_WOOD = ITEMS.register("constructs/construct_grabber_arm_left_wood", () -> new ConstructPartGrabberArmLeft(ConstructMaterial.WOOD, 1));

    public static final RegistryObject<ConstructPartGrabberArmRight> CONSTRUCT_GRABBER_ARM_RIGHT_WOOD = ITEMS.register("constructs/construct_grabber_arm_right_wood", () -> new ConstructPartGrabberArmRight(ConstructMaterial.WOOD, 1));

    public static final RegistryObject<ConstructPartAxeArmLeft> CONSTRUCT_AXE_ARM_LEFT_WOOD = ITEMS.register("constructs/construct_axe_arm_left_wood", () -> new ConstructPartAxeArmLeft(ConstructMaterial.WOOD, 6.0F, -3.2F));

    public static final RegistryObject<ConstructPartAxeArmRight> CONSTRUCT_AXE_ARM_RIGHT_WOOD = ITEMS.register("constructs/construct_axe_arm_right_wood", () -> new ConstructPartAxeArmRight(ConstructMaterial.WOOD, 6.0F, -3.2F));

    public static final RegistryObject<ConstructPartShieldArmLeft> CONSTRUCT_SHIELD_ARM_LEFT_WOOD = ITEMS.register("constructs/construct_shield_arm_left_wood", () -> new ConstructPartShieldArmLeft(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartShieldArmRight> CONSTRUCT_SHIELD_ARM_RIGHT_WOOD = ITEMS.register("constructs/construct_shield_arm_right_wood", () -> new ConstructPartShieldArmRight(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartManaCannonLeft> CONSTRUCT_MANA_CANNON_LEFT_WOOD = ITEMS.register("constructs/construct_mana_cannon_left_wood", () -> new ConstructPartManaCannonLeft(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartManaCannonRight> CONSTRUCT_MANA_CANNON_RIGHT_WOOD = ITEMS.register("constructs/construct_mana_cannon_right_wood", () -> new ConstructPartManaCannonRight(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartFluidNozzleLeft> CONSTRUCT_FLUID_NOZZLE_LEFT_WOOD = ITEMS.register("constructs/construct_fluid_nozzle_left_wood", () -> new ConstructPartFluidNozzleLeft(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartFluidNozzleRight> CONSTRUCT_FLUID_NOZZLE_RIGHT_WOOD = ITEMS.register("constructs/construct_fluid_nozzle_right_wood", () -> new ConstructPartFluidNozzleRight(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartCasterArmLeft> CONSTRUCT_CASTER_ARM_LEFT_WOOD = ITEMS.register("constructs/construct_caster_arm_left_wood", () -> new ConstructPartCasterArmLeft(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartCasterArmRight> CONSTRUCT_CASTER_ARM_RIGHT_WOOD = ITEMS.register("constructs/construct_caster_arm_right_wood", () -> new ConstructPartCasterArmRight(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartArmorTorso> CONSTRUCT_ARMOR_TORSO_WOOD = ITEMS.register("constructs/construct_armor_torso_wood", () -> new ConstructPartArmorTorso(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartManaTorso> CONSTRUCT_MANA_TORSO_WOOD = ITEMS.register("constructs/construct_mana_torso_wood", () -> new ConstructPartManaTorso(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartStorageTorso> CONSTRUCT_STORAGE_TORSO_WOOD = ITEMS.register("constructs/construct_storage_torso_wood", () -> new ConstructPartStorageTorso(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartTankTorso> CONSTRUCT_TANK_TORSO_WOOD = ITEMS.register("constructs/construct_tank_torso_wood", () -> new ConstructPartTankTorso(ConstructMaterial.WOOD));

    public static final RegistryObject<ConstructPartBasicHead> CONSTRUCT_BASIC_HEAD_STONE = ITEMS.register("constructs/construct_basic_head_stone", () -> new ConstructPartBasicHead(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartSmartHead> CONSTRUCT_SMART_HEAD_STONE = ITEMS.register("constructs/construct_smart_head_stone", () -> new ConstructPartSmartHead(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartHornHead> CONSTRUCT_HORN_HEAD_STONE = ITEMS.register("constructs/construct_horn_head_stone", () -> new ConstructPartHornHead(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartBasicTorso> CONSTRUCT_BASIC_TORSO_STONE = ITEMS.register("constructs/construct_basic_torso_stone", () -> new ConstructPartBasicTorso(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartBasicLegs> CONSTRUCT_BASIC_LEGS_STONE = ITEMS.register("constructs/construct_basic_legs_stone", () -> new ConstructPartBasicLegs(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartReinforcedLegs> CONSTRUCT_REINFORCED_LEGS_STONE = ITEMS.register("constructs/construct_reinforced_legs_stone", () -> new ConstructPartReinforcedLegs(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartEnderLegs> CONSTRUCT_ENDER_LEGS_STONE = ITEMS.register("constructs/construct_ender_legs_stone", () -> new ConstructPartEnderLegs(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartRocketLegs> CONSTRUCT_ROCKET_LEGS_STONE = ITEMS.register("constructs/construct_rocket_legs_stone", () -> new ConstructPartRocketLegs(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartFishingArmLeft> CONSTRUCT_FISHING_ARM_LEFT_STONE = ITEMS.register("constructs/construct_fishing_rod_left_stone", () -> new ConstructPartFishingArmLeft(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartFishingArmRight> CONSTRUCT_FISHING_ARM_RIGHT_STONE = ITEMS.register("constructs/construct_fishing_rod_right_stone", () -> new ConstructPartFishingArmRight(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartBladeArmLeft> CONSTRUCT_BLADE_ARM_LEFT_STONE = ITEMS.register("constructs/construct_blade_arm_left_stone", () -> new ConstructPartBladeArmLeft(ConstructMaterial.STONE, 3, -2.4F));

    public static final RegistryObject<ConstructPartBladeArmRight> CONSTRUCT_BLADE_ARM_RIGHT_STONE = ITEMS.register("constructs/construct_blade_arm_right_stone", () -> new ConstructPartBladeArmRight(ConstructMaterial.STONE, 3, -2.4F));

    public static final RegistryObject<ConstructPartHammerArmLeft> CONSTRUCT_HAMMER_ARM_LEFT_STONE = ITEMS.register("constructs/construct_hammer_arm_left_stone", () -> new ConstructPartHammerArmLeft(ConstructMaterial.STONE, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartHammerArmRight> CONSTRUCT_HAMMER_ARM_RIGHT_STONE = ITEMS.register("constructs/construct_hammer_arm_right_stone", () -> new ConstructPartHammerArmRight(ConstructMaterial.STONE, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartGrabberArmLeft> CONSTRUCT_GRABBER_ARM_LEFT_STONE = ITEMS.register("constructs/construct_grabber_arm_left_stone", () -> new ConstructPartGrabberArmLeft(ConstructMaterial.STONE, 2));

    public static final RegistryObject<ConstructPartGrabberArmRight> CONSTRUCT_GRABBER_ARM_RIGHT_STONE = ITEMS.register("constructs/construct_grabber_arm_right_stone", () -> new ConstructPartGrabberArmRight(ConstructMaterial.STONE, 2));

    public static final RegistryObject<ConstructPartAxeArmLeft> CONSTRUCT_AXE_ARM_LEFT_STONE = ITEMS.register("constructs/construct_axe_arm_left_stone", () -> new ConstructPartAxeArmLeft(ConstructMaterial.STONE, 7.0F, -3.2F));

    public static final RegistryObject<ConstructPartAxeArmRight> CONSTRUCT_AXE_ARM_RIGHT_STONE = ITEMS.register("constructs/construct_axe_arm_right_stone", () -> new ConstructPartAxeArmRight(ConstructMaterial.STONE, 7.0F, -3.2F));

    public static final RegistryObject<ConstructPartShieldArmLeft> CONSTRUCT_SHIELD_ARM_LEFT_STONE = ITEMS.register("constructs/construct_shield_arm_left_stone", () -> new ConstructPartShieldArmLeft(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartShieldArmRight> CONSTRUCT_SHIELD_ARM_RIGHT_STONE = ITEMS.register("constructs/construct_shield_arm_right_stone", () -> new ConstructPartShieldArmRight(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartManaCannonLeft> CONSTRUCT_MANA_CANNON_LEFT_STONE = ITEMS.register("constructs/construct_mana_cannon_left_stone", () -> new ConstructPartManaCannonLeft(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartManaCannonRight> CONSTRUCT_MANA_CANNON_RIGHT_STONE = ITEMS.register("constructs/construct_mana_cannon_right_stone", () -> new ConstructPartManaCannonRight(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartFluidNozzleLeft> CONSTRUCT_FLUID_NOZZLE_LEFT_STONE = ITEMS.register("constructs/construct_fluid_nozzle_left_stone", () -> new ConstructPartFluidNozzleLeft(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartFluidNozzleRight> CONSTRUCT_FLUID_NOZZLE_RIGHT_STONE = ITEMS.register("constructs/construct_fluid_nozzle_right_stone", () -> new ConstructPartFluidNozzleRight(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartCasterArmLeft> CONSTRUCT_CASTER_ARM_LEFT_STONE = ITEMS.register("constructs/construct_caster_arm_left_stone", () -> new ConstructPartCasterArmLeft(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartCasterArmRight> CONSTRUCT_CASTER_ARM_RIGHT_STONE = ITEMS.register("constructs/construct_caster_arm_right_stone", () -> new ConstructPartCasterArmRight(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartArmorTorso> CONSTRUCT_ARMOR_TORSO_STONE = ITEMS.register("constructs/construct_armor_torso_stone", () -> new ConstructPartArmorTorso(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartManaTorso> CONSTRUCT_MANA_TORSO_STONE = ITEMS.register("constructs/construct_mana_torso_stone", () -> new ConstructPartManaTorso(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartStorageTorso> CONSTRUCT_STORAGE_TORSO_STONE = ITEMS.register("constructs/construct_storage_torso_stone", () -> new ConstructPartStorageTorso(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartTankTorso> CONSTRUCT_TANK_TORSO_STONE = ITEMS.register("constructs/construct_tank_torso_stone", () -> new ConstructPartTankTorso(ConstructMaterial.STONE));

    public static final RegistryObject<ConstructPartBasicHead> CONSTRUCT_BASIC_HEAD_IRON = ITEMS.register("constructs/construct_basic_head_iron", () -> new ConstructPartBasicHead(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartSmartHead> CONSTRUCT_SMART_HEAD_IRON = ITEMS.register("constructs/construct_smart_head_iron", () -> new ConstructPartSmartHead(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartHornHead> CONSTRUCT_HORN_HEAD_IRON = ITEMS.register("constructs/construct_horn_head_iron", () -> new ConstructPartHornHead(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartBasicTorso> CONSTRUCT_BASIC_TORSO_IRON = ITEMS.register("constructs/construct_basic_torso_iron", () -> new ConstructPartBasicTorso(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartBasicLegs> CONSTRUCT_BASIC_LEGS_IRON = ITEMS.register("constructs/construct_basic_legs_iron", () -> new ConstructPartBasicLegs(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartReinforcedLegs> CONSTRUCT_REINFORCED_LEGS_IRON = ITEMS.register("constructs/construct_reinforced_legs_iron", () -> new ConstructPartReinforcedLegs(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartEnderLegs> CONSTRUCT_ENDER_LEGS_IRON = ITEMS.register("constructs/construct_ender_legs_iron", () -> new ConstructPartEnderLegs(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartRocketLegs> CONSTRUCT_ROCKET_LEGS_IRON = ITEMS.register("constructs/construct_rocket_legs_iron", () -> new ConstructPartRocketLegs(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartFishingArmLeft> CONSTRUCT_FISHING_ARM_LEFT_IRON = ITEMS.register("constructs/construct_fishing_rod_left_iron", () -> new ConstructPartFishingArmLeft(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartFishingArmRight> CONSTRUCT_FISHING_ARM_RIGHT_IRON = ITEMS.register("constructs/construct_fishing_rod_right_iron", () -> new ConstructPartFishingArmRight(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartBladeArmLeft> CONSTRUCT_BLADE_ARM_LEFT_IRON = ITEMS.register("constructs/construct_blade_arm_left_iron", () -> new ConstructPartBladeArmLeft(ConstructMaterial.IRON, 3, -2.4F));

    public static final RegistryObject<ConstructPartBladeArmRight> CONSTRUCT_BLADE_ARM_RIGHT_IRON = ITEMS.register("constructs/construct_blade_arm_right_iron", () -> new ConstructPartBladeArmRight(ConstructMaterial.IRON, 3, -2.4F));

    public static final RegistryObject<ConstructPartHammerArmLeft> CONSTRUCT_HAMMER_ARM_LEFT_IRON = ITEMS.register("constructs/construct_hammer_arm_left_iron", () -> new ConstructPartHammerArmLeft(ConstructMaterial.IRON, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartHammerArmRight> CONSTRUCT_HAMMER_ARM_RIGHT_IRON = ITEMS.register("constructs/construct_hammer_arm_right_iron", () -> new ConstructPartHammerArmRight(ConstructMaterial.IRON, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartGrabberArmLeft> CONSTRUCT_GRABBER_ARM_LEFT_IRON = ITEMS.register("constructs/construct_grabber_arm_left_iron", () -> new ConstructPartGrabberArmLeft(ConstructMaterial.IRON, 3));

    public static final RegistryObject<ConstructPartGrabberArmRight> CONSTRUCT_GRABBER_ARM_RIGHT_IRON = ITEMS.register("constructs/construct_grabber_arm_right_iron", () -> new ConstructPartGrabberArmRight(ConstructMaterial.IRON, 3));

    public static final RegistryObject<ConstructPartAxeArmLeft> CONSTRUCT_AXE_ARM_LEFT_IRON = ITEMS.register("constructs/construct_axe_arm_left_iron", () -> new ConstructPartAxeArmLeft(ConstructMaterial.IRON, 6.0F, -3.1F));

    public static final RegistryObject<ConstructPartAxeArmRight> CONSTRUCT_AXE_ARM_RIGHT_IRON = ITEMS.register("constructs/construct_axe_arm_right_iron", () -> new ConstructPartAxeArmRight(ConstructMaterial.IRON, 6.0F, -3.1F));

    public static final RegistryObject<ConstructPartShieldArmLeft> CONSTRUCT_SHIELD_ARM_LEFT_IRON = ITEMS.register("constructs/construct_shield_arm_left_iron", () -> new ConstructPartShieldArmLeft(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartShieldArmRight> CONSTRUCT_SHIELD_ARM_RIGHT_IRON = ITEMS.register("constructs/construct_shield_arm_right_iron", () -> new ConstructPartShieldArmRight(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartManaCannonLeft> CONSTRUCT_MANA_CANNON_LEFT_IRON = ITEMS.register("constructs/construct_mana_cannon_left_iron", () -> new ConstructPartManaCannonLeft(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartManaCannonRight> CONSTRUCT_MANA_CANNON_RIGHT_IRON = ITEMS.register("constructs/construct_mana_cannon_right_iron", () -> new ConstructPartManaCannonRight(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartFluidNozzleLeft> CONSTRUCT_FLUID_NOZZLE_LEFT_IRON = ITEMS.register("constructs/construct_fluid_nozzle_left_iron", () -> new ConstructPartFluidNozzleLeft(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartFluidNozzleRight> CONSTRUCT_FLUID_NOZZLE_RIGHT_IRON = ITEMS.register("constructs/construct_fluid_nozzle_right_iron", () -> new ConstructPartFluidNozzleRight(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartCasterArmLeft> CONSTRUCT_CASTER_ARM_LEFT_IRON = ITEMS.register("constructs/construct_caster_arm_left_iron", () -> new ConstructPartCasterArmLeft(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartCasterArmRight> CONSTRUCT_CASTER_ARM_RIGHT_IRON = ITEMS.register("constructs/construct_caster_arm_right_iron", () -> new ConstructPartCasterArmRight(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartArmorTorso> CONSTRUCT_ARMOR_TORSO_IRON = ITEMS.register("constructs/construct_armor_torso_iron", () -> new ConstructPartArmorTorso(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartManaTorso> CONSTRUCT_MANA_TORSO_IRON = ITEMS.register("constructs/construct_mana_torso_iron", () -> new ConstructPartManaTorso(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartStorageTorso> CONSTRUCT_STORAGE_TORSO_IRON = ITEMS.register("constructs/construct_storage_torso_iron", () -> new ConstructPartStorageTorso(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartTankTorso> CONSTRUCT_TANK_TORSO_IRON = ITEMS.register("constructs/construct_tank_torso_iron", () -> new ConstructPartTankTorso(ConstructMaterial.IRON));

    public static final RegistryObject<ConstructPartBasicHead> CONSTRUCT_BASIC_HEAD_GOLD = ITEMS.register("constructs/construct_basic_head_gold", () -> new ConstructPartBasicHead(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartSmartHead> CONSTRUCT_SMART_HEAD_GOLD = ITEMS.register("constructs/construct_smart_head_gold", () -> new ConstructPartSmartHead(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartHornHead> CONSTRUCT_HORN_HEAD_GOLD = ITEMS.register("constructs/construct_horn_head_gold", () -> new ConstructPartHornHead(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartBasicTorso> CONSTRUCT_BASIC_TORSO_GOLD = ITEMS.register("constructs/construct_basic_torso_gold", () -> new ConstructPartBasicTorso(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartBasicLegs> CONSTRUCT_BASIC_LEGS_GOLD = ITEMS.register("constructs/construct_basic_legs_gold", () -> new ConstructPartBasicLegs(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartReinforcedLegs> CONSTRUCT_REINFORCED_LEGS_GOLD = ITEMS.register("constructs/construct_reinforced_legs_gold", () -> new ConstructPartReinforcedLegs(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartEnderLegs> CONSTRUCT_ENDER_LEGS_GOLD = ITEMS.register("constructs/construct_ender_legs_gold", () -> new ConstructPartEnderLegs(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartRocketLegs> CONSTRUCT_ROCKET_LEGS_GOLD = ITEMS.register("constructs/construct_rocket_legs_gold", () -> new ConstructPartRocketLegs(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartFishingArmLeft> CONSTRUCT_FISHING_ARM_LEFT_GOLD = ITEMS.register("constructs/construct_fishing_rod_left_gold", () -> new ConstructPartFishingArmLeft(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartFishingArmRight> CONSTRUCT_FISHING_ARM_RIGHT_GOLD = ITEMS.register("constructs/construct_fishing_rod_right_gold", () -> new ConstructPartFishingArmRight(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartBladeArmLeft> CONSTRUCT_BLADE_ARM_LEFT_GOLD = ITEMS.register("constructs/construct_blade_arm_left_gold", () -> new ConstructPartBladeArmLeft(ConstructMaterial.GOLD, 3, -2.4F));

    public static final RegistryObject<ConstructPartBladeArmRight> CONSTRUCT_BLADE_ARM_RIGHT_GOLD = ITEMS.register("constructs/construct_blade_arm_right_gold", () -> new ConstructPartBladeArmRight(ConstructMaterial.GOLD, 3, -2.4F));

    public static final RegistryObject<ConstructPartHammerArmLeft> CONSTRUCT_HAMMER_ARM_LEFT_GOLD = ITEMS.register("constructs/construct_hammer_arm_left_gold", () -> new ConstructPartHammerArmLeft(ConstructMaterial.GOLD, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartHammerArmRight> CONSTRUCT_HAMMER_ARM_RIGHT_GOLD = ITEMS.register("constructs/construct_hammer_arm_right_gold", () -> new ConstructPartHammerArmRight(ConstructMaterial.GOLD, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartGrabberArmLeft> CONSTRUCT_GRABBER_ARM_LEFT_GOLD = ITEMS.register("constructs/construct_grabber_arm_left_gold", () -> new ConstructPartGrabberArmLeft(ConstructMaterial.GOLD, 3));

    public static final RegistryObject<ConstructPartGrabberArmRight> CONSTRUCT_GRABBER_ARM_RIGHT_GOLD = ITEMS.register("constructs/construct_grabber_arm_right_gold", () -> new ConstructPartGrabberArmRight(ConstructMaterial.GOLD, 3));

    public static final RegistryObject<ConstructPartAxeArmLeft> CONSTRUCT_AXE_ARM_LEFT_GOLD = ITEMS.register("constructs/construct_axe_arm_left_gold", () -> new ConstructPartAxeArmLeft(ConstructMaterial.GOLD, 6.0F, -3.0F));

    public static final RegistryObject<ConstructPartAxeArmRight> CONSTRUCT_AXE_ARM_RIGHT_GOLD = ITEMS.register("constructs/construct_axe_arm_right_gold", () -> new ConstructPartAxeArmRight(ConstructMaterial.GOLD, 6.0F, -3.0F));

    public static final RegistryObject<ConstructPartShieldArmLeft> CONSTRUCT_SHIELD_ARM_LEFT_GOLD = ITEMS.register("constructs/construct_shield_arm_left_gold", () -> new ConstructPartShieldArmLeft(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartShieldArmRight> CONSTRUCT_SHIELD_ARM_RIGHT_GOLD = ITEMS.register("constructs/construct_shield_arm_right_gold", () -> new ConstructPartShieldArmRight(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartManaCannonLeft> CONSTRUCT_MANA_CANNON_LEFT_GOLD = ITEMS.register("constructs/construct_mana_cannon_left_gold", () -> new ConstructPartManaCannonLeft(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartManaCannonRight> CONSTRUCT_MANA_CANNON_RIGHT_GOLD = ITEMS.register("constructs/construct_mana_cannon_right_gold", () -> new ConstructPartManaCannonRight(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartFluidNozzleLeft> CONSTRUCT_FLUID_NOZZLE_LEFT_GOLD = ITEMS.register("constructs/construct_fluid_nozzle_left_gold", () -> new ConstructPartFluidNozzleLeft(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartFluidNozzleRight> CONSTRUCT_FLUID_NOZZLE_RIGHT_GOLD = ITEMS.register("constructs/construct_fluid_nozzle_right_gold", () -> new ConstructPartFluidNozzleRight(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartCasterArmLeft> CONSTRUCT_CASTER_ARM_LEFT_GOLD = ITEMS.register("constructs/construct_caster_arm_left_gold", () -> new ConstructPartCasterArmLeft(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartCasterArmRight> CONSTRUCT_CASTER_ARM_RIGHT_GOLD = ITEMS.register("constructs/construct_caster_arm_right_gold", () -> new ConstructPartCasterArmRight(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartArmorTorso> CONSTRUCT_ARMOR_TORSO_GOLD = ITEMS.register("constructs/construct_armor_torso_gold", () -> new ConstructPartArmorTorso(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartManaTorso> CONSTRUCT_MANA_TORSO_GOLD = ITEMS.register("constructs/construct_mana_torso_gold", () -> new ConstructPartManaTorso(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartStorageTorso> CONSTRUCT_STORAGE_TORSO_GOLD = ITEMS.register("constructs/construct_storage_torso_gold", () -> new ConstructPartStorageTorso(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartTankTorso> CONSTRUCT_TANK_TORSO_GOLD = ITEMS.register("constructs/construct_tank_torso_gold", () -> new ConstructPartTankTorso(ConstructMaterial.GOLD));

    public static final RegistryObject<ConstructPartBasicHead> CONSTRUCT_BASIC_HEAD_DIAMOND = ITEMS.register("constructs/construct_basic_head_diamond", () -> new ConstructPartBasicHead(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartSmartHead> CONSTRUCT_SMART_HEAD_DIAMOND = ITEMS.register("constructs/construct_smart_head_diamond", () -> new ConstructPartSmartHead(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartHornHead> CONSTRUCT_HORN_HEAD_DIAMOND = ITEMS.register("constructs/construct_horn_head_diamond", () -> new ConstructPartHornHead(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartBasicTorso> CONSTRUCT_BASIC_TORSO_DIAMOND = ITEMS.register("constructs/construct_basic_torso_diamond", () -> new ConstructPartBasicTorso(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartBasicLegs> CONSTRUCT_BASIC_LEGS_DIAMOND = ITEMS.register("constructs/construct_basic_legs_diamond", () -> new ConstructPartBasicLegs(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartReinforcedLegs> CONSTRUCT_REINFORCED_LEGS_DIAMOND = ITEMS.register("constructs/construct_reinforced_legs_diamond", () -> new ConstructPartReinforcedLegs(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartEnderLegs> CONSTRUCT_ENDER_LEGS_DIAMOND = ITEMS.register("constructs/construct_ender_legs_diamond", () -> new ConstructPartEnderLegs(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartRocketLegs> CONSTRUCT_ROCKET_LEGS_DIAMOND = ITEMS.register("constructs/construct_rocket_legs_diamond", () -> new ConstructPartRocketLegs(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartFishingArmLeft> CONSTRUCT_FISHING_ARM_LEFT_DIAMOND = ITEMS.register("constructs/construct_fishing_rod_left_diamond", () -> new ConstructPartFishingArmLeft(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartFishingArmRight> CONSTRUCT_FISHING_ARM_RIGHT_DIAMOND = ITEMS.register("constructs/construct_fishing_rod_right_diamond", () -> new ConstructPartFishingArmRight(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartBladeArmLeft> CONSTRUCT_BLADE_ARM_LEFT_DIAMOND = ITEMS.register("constructs/construct_blade_arm_left_diamond", () -> new ConstructPartBladeArmLeft(ConstructMaterial.DIAMOND, 3, -2.4F));

    public static final RegistryObject<ConstructPartBladeArmRight> CONSTRUCT_BLADE_ARM_RIGHT_DIAMOND = ITEMS.register("constructs/construct_blade_arm_right_diamond", () -> new ConstructPartBladeArmRight(ConstructMaterial.DIAMOND, 3, -2.4F));

    public static final RegistryObject<ConstructPartHammerArmLeft> CONSTRUCT_HAMMER_ARM_LEFT_DIAMOND = ITEMS.register("constructs/construct_hammer_arm_left_diamond", () -> new ConstructPartHammerArmLeft(ConstructMaterial.DIAMOND, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartHammerArmRight> CONSTRUCT_HAMMER_ARM_RIGHT_DIAMOND = ITEMS.register("constructs/construct_hammer_arm_right_diamond", () -> new ConstructPartHammerArmRight(ConstructMaterial.DIAMOND, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartGrabberArmLeft> CONSTRUCT_GRABBER_ARM_LEFT_DIAMOND = ITEMS.register("constructs/construct_grabber_arm_left_diamond", () -> new ConstructPartGrabberArmLeft(ConstructMaterial.DIAMOND, 4));

    public static final RegistryObject<ConstructPartGrabberArmRight> CONSTRUCT_GRABBER_ARM_RIGHT_DIAMOND = ITEMS.register("constructs/construct_grabber_arm_right_diamond", () -> new ConstructPartGrabberArmRight(ConstructMaterial.DIAMOND, 4));

    public static final RegistryObject<ConstructPartAxeArmLeft> CONSTRUCT_AXE_ARM_LEFT_DIAMOND = ITEMS.register("constructs/construct_axe_arm_left_diamond", () -> new ConstructPartAxeArmLeft(ConstructMaterial.DIAMOND, 5.0F, -3.0F));

    public static final RegistryObject<ConstructPartAxeArmRight> CONSTRUCT_AXE_ARM_RIGHT_DIAMOND = ITEMS.register("constructs/construct_axe_arm_right_diamond", () -> new ConstructPartAxeArmRight(ConstructMaterial.DIAMOND, 5.0F, -3.0F));

    public static final RegistryObject<ConstructPartShieldArmLeft> CONSTRUCT_SHIELD_ARM_LEFT_DIAMOND = ITEMS.register("constructs/construct_shield_arm_left_diamond", () -> new ConstructPartShieldArmLeft(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartShieldArmRight> CONSTRUCT_SHIELD_ARM_RIGHT_DIAMOND = ITEMS.register("constructs/construct_shield_arm_right_diamond", () -> new ConstructPartShieldArmRight(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartManaCannonLeft> CONSTRUCT_MANA_CANNON_LEFT_DIAMOND = ITEMS.register("constructs/construct_mana_cannon_left_diamond", () -> new ConstructPartManaCannonLeft(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartManaCannonRight> CONSTRUCT_MANA_CANNON_RIGHT_DIAMOND = ITEMS.register("constructs/construct_mana_cannon_right_diamond", () -> new ConstructPartManaCannonRight(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartFluidNozzleLeft> CONSTRUCT_FLUID_NOZZLE_LEFT_DIAMOND = ITEMS.register("constructs/construct_fluid_nozzle_left_diamond", () -> new ConstructPartFluidNozzleLeft(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartFluidNozzleRight> CONSTRUCT_FLUID_NOZZLE_RIGHT_DIAMOND = ITEMS.register("constructs/construct_fluid_nozzle_right_diamond", () -> new ConstructPartFluidNozzleRight(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartCasterArmLeft> CONSTRUCT_CASTER_ARM_LEFT_DIAMOND = ITEMS.register("constructs/construct_caster_arm_left_diamond", () -> new ConstructPartCasterArmLeft(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartCasterArmRight> CONSTRUCT_CASTER_ARM_RIGHT_DIAMOND = ITEMS.register("constructs/construct_caster_arm_right_diamond", () -> new ConstructPartCasterArmRight(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartArmorTorso> CONSTRUCT_ARMOR_TORSO_DIAMOND = ITEMS.register("constructs/construct_armor_torso_diamond", () -> new ConstructPartArmorTorso(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartManaTorso> CONSTRUCT_MANA_TORSO_DIAMOND = ITEMS.register("constructs/construct_mana_torso_diamond", () -> new ConstructPartManaTorso(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartStorageTorso> CONSTRUCT_STORAGE_TORSO_DIAMOND = ITEMS.register("constructs/construct_storage_torso_diamond", () -> new ConstructPartStorageTorso(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartTankTorso> CONSTRUCT_TANK_TORSO_DIAMOND = ITEMS.register("constructs/construct_tank_torso_diamond", () -> new ConstructPartTankTorso(ConstructMaterial.DIAMOND));

    public static final RegistryObject<ConstructPartBasicHead> CONSTRUCT_BASIC_HEAD_OBSIDIAN = ITEMS.register("constructs/construct_basic_head_obsidian", () -> new ConstructPartBasicHead(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartSmartHead> CONSTRUCT_SMART_HEAD_OBSIDIAN = ITEMS.register("constructs/construct_smart_head_obsidian", () -> new ConstructPartSmartHead(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartHornHead> CONSTRUCT_HORN_HEAD_OBSIDIAN = ITEMS.register("constructs/construct_horn_head_obsidian", () -> new ConstructPartHornHead(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartBasicTorso> CONSTRUCT_BASIC_TORSO_OBSIDIAN = ITEMS.register("constructs/construct_basic_torso_obsidian", () -> new ConstructPartBasicTorso(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartBasicLegs> CONSTRUCT_BASIC_LEGS_OBSIDIAN = ITEMS.register("constructs/construct_basic_legs_obsidian", () -> new ConstructPartBasicLegs(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartReinforcedLegs> CONSTRUCT_REINFORCED_LEGS_OBSIDIAN = ITEMS.register("constructs/construct_reinforced_legs_obsidian", () -> new ConstructPartReinforcedLegs(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartEnderLegs> CONSTRUCT_ENDER_LEGS_OBSIDIAN = ITEMS.register("constructs/construct_ender_legs_obsidian", () -> new ConstructPartEnderLegs(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartRocketLegs> CONSTRUCT_ROCKET_LEGS_OBSIDIAN = ITEMS.register("constructs/construct_rocket_legs_obsidian", () -> new ConstructPartRocketLegs(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartFishingArmLeft> CONSTRUCT_FISHING_ARM_LEFT_OBSIDIAN = ITEMS.register("constructs/construct_fishing_rod_left_obsidian", () -> new ConstructPartFishingArmLeft(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartFishingArmRight> CONSTRUCT_FISHING_ARM_RIGHT_OBSIDIAN = ITEMS.register("constructs/construct_fishing_rod_right_obsidian", () -> new ConstructPartFishingArmRight(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartBladeArmLeft> CONSTRUCT_BLADE_ARM_LEFT_OBSIDIAN = ITEMS.register("constructs/construct_blade_arm_left_obsidian", () -> new ConstructPartBladeArmLeft(ConstructMaterial.OBSIDIAN, 3, -2.4F));

    public static final RegistryObject<ConstructPartBladeArmRight> CONSTRUCT_BLADE_ARM_RIGHT_OBSIDIAN = ITEMS.register("constructs/construct_blade_arm_right_obsidian", () -> new ConstructPartBladeArmRight(ConstructMaterial.OBSIDIAN, 3, -2.4F));

    public static final RegistryObject<ConstructPartHammerArmLeft> CONSTRUCT_HAMMER_ARM_LEFT_OBSIDIAN = ITEMS.register("constructs/construct_hammer_arm_left_obsidian", () -> new ConstructPartHammerArmLeft(ConstructMaterial.OBSIDIAN, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartHammerArmRight> CONSTRUCT_HAMMER_ARM_RIGHT_OBSIDIAN = ITEMS.register("constructs/construct_hammer_arm_right_obsidian", () -> new ConstructPartHammerArmRight(ConstructMaterial.OBSIDIAN, 1.0F, -2.8F));

    public static final RegistryObject<ConstructPartGrabberArmLeft> CONSTRUCT_GRABBER_ARM_LEFT_OBSIDIAN = ITEMS.register("constructs/construct_grabber_arm_left_obsidian", () -> new ConstructPartGrabberArmLeft(ConstructMaterial.OBSIDIAN, 4));

    public static final RegistryObject<ConstructPartGrabberArmRight> CONSTRUCT_GRABBER_ARM_RIGHT_OBSIDIAN = ITEMS.register("constructs/construct_grabber_arm_right_obsidian", () -> new ConstructPartGrabberArmRight(ConstructMaterial.OBSIDIAN, 4));

    public static final RegistryObject<ConstructPartAxeArmLeft> CONSTRUCT_AXE_ARM_LEFT_OBSIDIAN = ITEMS.register("constructs/construct_axe_arm_left_obsidian", () -> new ConstructPartAxeArmLeft(ConstructMaterial.OBSIDIAN, 5.0F, -3.0F));

    public static final RegistryObject<ConstructPartAxeArmRight> CONSTRUCT_AXE_ARM_RIGHT_OBSIDIAN = ITEMS.register("constructs/construct_axe_arm_right_obsidian", () -> new ConstructPartAxeArmRight(ConstructMaterial.OBSIDIAN, 5.0F, -3.0F));

    public static final RegistryObject<ConstructPartShieldArmLeft> CONSTRUCT_SHIELD_ARM_LEFT_OBSIDIAN = ITEMS.register("constructs/construct_shield_arm_left_obsidian", () -> new ConstructPartShieldArmLeft(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartShieldArmRight> CONSTRUCT_SHIELD_ARM_RIGHT_OBSIDIAN = ITEMS.register("constructs/construct_shield_arm_right_obsidian", () -> new ConstructPartShieldArmRight(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartManaCannonLeft> CONSTRUCT_MANA_CANNON_LEFT_OBSIDIAN = ITEMS.register("constructs/construct_mana_cannon_left_obsidian", () -> new ConstructPartManaCannonLeft(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartManaCannonRight> CONSTRUCT_MANA_CANNON_RIGHT_OBSIDIAN = ITEMS.register("constructs/construct_mana_cannon_right_obsidian", () -> new ConstructPartManaCannonRight(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartFluidNozzleLeft> CONSTRUCT_FLUID_NOZZLE_LEFT_OBSIDIAN = ITEMS.register("constructs/construct_fluid_nozzle_left_obsidian", () -> new ConstructPartFluidNozzleLeft(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartFluidNozzleRight> CONSTRUCT_FLUID_NOZZLE_RIGHT_OBSIDIAN = ITEMS.register("constructs/construct_fluid_nozzle_right_obsidian", () -> new ConstructPartFluidNozzleRight(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartCasterArmLeft> CONSTRUCT_CASTER_ARM_LEFT_OBSIDIAN = ITEMS.register("constructs/construct_caster_arm_left_obsidian", () -> new ConstructPartCasterArmLeft(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartCasterArmRight> CONSTRUCT_CASTER_ARM_RIGHT_OBSIDIAN = ITEMS.register("constructs/construct_caster_arm_right_obsidian", () -> new ConstructPartCasterArmRight(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartArmorTorso> CONSTRUCT_ARMOR_TORSO_OBSIDIAN = ITEMS.register("constructs/construct_armor_torso_obsidian", () -> new ConstructPartArmorTorso(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartManaTorso> CONSTRUCT_MANA_TORSO_OBSIDIAN = ITEMS.register("constructs/construct_mana_torso_obsidian", () -> new ConstructPartManaTorso(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartStorageTorso> CONSTRUCT_STORAGE_TORSO_OBSIDIAN = ITEMS.register("constructs/construct_storage_torso_obsidian", () -> new ConstructPartStorageTorso(ConstructMaterial.OBSIDIAN));

    public static final RegistryObject<ConstructPartTankTorso> CONSTRUCT_TANK_TORSO_OBSIDIAN = ITEMS.register("constructs/construct_tank_torso_obsidian", () -> new ConstructPartTankTorso(ConstructMaterial.OBSIDIAN));

    @SubscribeEvent
    public static void FillCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        List<Item> _excluded = Arrays.asList(GREATER_MOTE_HELLFIRE.get(), GREATER_MOTE_LIGHTNING.get(), GREATER_MOTE_ICE.get(), COUNCIL_HUD_BADGE.get(), FEY_SUMMER_HUD_BADGE.get(), FEY_WINTER_HUD_BADGE.get(), UNDEAD_HUD_BADGE.get());
        if (event.getTab() == MACreativeTabs.GENERAL) {
            ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> isInCreativeTab(item, _excluded)).forEach(item -> event.m_246326_(item));
        }
    }

    private static boolean isInCreativeTab(Item item, List<Item> excluded) {
        return !(item instanceof INoCreativeTab) && !excluded.contains(item);
    }
}