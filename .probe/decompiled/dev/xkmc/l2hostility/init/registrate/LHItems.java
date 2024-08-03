package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2hostility.content.entity.ChargeType;
import dev.xkmc.l2hostility.content.item.consumable.BookCopy;
import dev.xkmc.l2hostility.content.item.consumable.BookEverything;
import dev.xkmc.l2hostility.content.item.consumable.BottleOfCurse;
import dev.xkmc.l2hostility.content.item.consumable.BottleOfSanity;
import dev.xkmc.l2hostility.content.item.consumable.EffectBoosterBottle;
import dev.xkmc.l2hostility.content.item.consumable.HostilityChargeItem;
import dev.xkmc.l2hostility.content.item.consumable.HostilityOrb;
import dev.xkmc.l2hostility.content.item.curio.curse.CurseOfEnvy;
import dev.xkmc.l2hostility.content.item.curio.curse.CurseOfGluttony;
import dev.xkmc.l2hostility.content.item.curio.curse.CurseOfGreed;
import dev.xkmc.l2hostility.content.item.curio.curse.CurseOfLust;
import dev.xkmc.l2hostility.content.item.curio.curse.CurseOfPride;
import dev.xkmc.l2hostility.content.item.curio.curse.CurseOfSloth;
import dev.xkmc.l2hostility.content.item.curio.curse.CurseOfWrath;
import dev.xkmc.l2hostility.content.item.curio.misc.Abrahadabra;
import dev.xkmc.l2hostility.content.item.curio.misc.AbyssalThorn;
import dev.xkmc.l2hostility.content.item.curio.misc.DivinityCross;
import dev.xkmc.l2hostility.content.item.curio.misc.DivinityLight;
import dev.xkmc.l2hostility.content.item.curio.misc.FlamingThorn;
import dev.xkmc.l2hostility.content.item.curio.misc.GreedOfNidhoggur;
import dev.xkmc.l2hostility.content.item.curio.misc.ImagineBreaker;
import dev.xkmc.l2hostility.content.item.curio.misc.InfinityGlove;
import dev.xkmc.l2hostility.content.item.curio.misc.LootingCharm;
import dev.xkmc.l2hostility.content.item.curio.misc.OddeyesGlasses;
import dev.xkmc.l2hostility.content.item.curio.misc.PlatinumStar;
import dev.xkmc.l2hostility.content.item.curio.misc.PocketOfRestoration;
import dev.xkmc.l2hostility.content.item.curio.misc.TripleStripCape;
import dev.xkmc.l2hostility.content.item.curio.ring.RingOfCorrosion;
import dev.xkmc.l2hostility.content.item.curio.ring.RingOfDivinity;
import dev.xkmc.l2hostility.content.item.curio.ring.RingOfHealing;
import dev.xkmc.l2hostility.content.item.curio.ring.RingOfIncarceration;
import dev.xkmc.l2hostility.content.item.curio.ring.RingOfLife;
import dev.xkmc.l2hostility.content.item.curio.ring.RingOfOcean;
import dev.xkmc.l2hostility.content.item.curio.ring.RingOfReflection;
import dev.xkmc.l2hostility.content.item.tool.Detector;
import dev.xkmc.l2hostility.content.item.tool.DetectorGlasses;
import dev.xkmc.l2hostility.content.item.tool.WitchWand;
import dev.xkmc.l2hostility.content.item.traits.SealedItem;
import dev.xkmc.l2hostility.content.item.wand.AiConfigWand;
import dev.xkmc.l2hostility.content.item.wand.EquipmentWand;
import dev.xkmc.l2hostility.content.item.wand.TargetSelectWand;
import dev.xkmc.l2hostility.content.item.wand.TraitAdderWand;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

@MethodsReturnNonnullByDefault
public class LHItems {

    public static final ItemEntry<HostilityOrb> HOSTILITY_ORB = L2Hostility.REGISTRATE.item("hostility_orb", p -> new HostilityOrb(p.stacksTo(64).rarity(Rarity.EPIC))).register();

    public static final ItemEntry<BottleOfCurse> BOTTLE_CURSE = L2Hostility.REGISTRATE.item("bottle_of_curse", p -> new BottleOfCurse(p.stacksTo(64).rarity(Rarity.RARE).craftRemainder(Items.GLASS_BOTTLE))).register();

    public static final ItemEntry<BottleOfSanity> BOTTLE_SANITY = L2Hostility.REGISTRATE.item("bottle_of_sanity", p -> new BottleOfSanity(p.stacksTo(64).rarity(Rarity.RARE).craftRemainder(Items.GLASS_BOTTLE))).register();

    public static final ItemEntry<Item> WITCH_DROPLET = L2Hostility.REGISTRATE.item("witch_droplet", Item::new).register();

    public static final ItemEntry<EffectBoosterBottle> BOOSTER_POTION = L2Hostility.REGISTRATE.item("booster_potion", p -> new EffectBoosterBottle(p.stacksTo(16).rarity(Rarity.RARE).craftRemainder(Items.GLASS_BOTTLE))).register();

    public static final ItemEntry<HostilityChargeItem> WITCH_CHARGE = L2Hostility.REGISTRATE.item("witch_charge", p -> new HostilityChargeItem(p, ChargeType.BOOST, () -> LangData.TOOLTIP_WITCH_CHARGE.get(LHConfig.COMMON.witchChargeMinDuration.get() / 20, Math.round(100.0 * LHConfig.COMMON.drainDuration.get()), LHConfig.COMMON.drainDurationMax.get() / 20).withStyle(ChatFormatting.GRAY))).register();

    public static final ItemEntry<HostilityChargeItem> ETERNAL_WITCH_CHARGE = L2Hostility.REGISTRATE.item("eternal_witch_charge", p -> new HostilityChargeItem(p, ChargeType.ETERNAL, () -> LangData.TOOLTIP_WITCH_ETERNAL.get(LHConfig.COMMON.witchChargeMinDuration.get() / 20).withStyle(ChatFormatting.GRAY))).register();

    public static final ItemEntry<Detector> DETECTOR = L2Hostility.REGISTRATE.item("hostility_detector", p -> new Detector(p.stacksTo(1))).register();

    public static final ItemEntry<DetectorGlasses> DETECTOR_GLASSES;

    public static final ItemEntry<WitchWand> WITCH_WAND;

    public static final ItemEntry<BookCopy> BOOK_COPY = L2Hostility.REGISTRATE.item("book_of_reprint", BookCopy::new).register();

    public static final ItemEntry<BookEverything> BOOK_OMNISCIENCE = L2Hostility.REGISTRATE.item("book_of_omniscience", BookEverything::new).register();

    public static final ItemEntry<Item> CHAOS_INGOT = L2Hostility.REGISTRATE.item("chaos_ingot", p -> new Item(p.rarity(Rarity.EPIC).fireResistant())).tag(new TagKey[] { LHTagGen.BEACON_PAYMENT }).register();

    public static final ItemEntry<SimpleFoiledItem> HOSTILITY_ESSENCE = L2Hostility.REGISTRATE.item("hostility_essence", p -> new SimpleFoiledItem(p.rarity(Rarity.EPIC).fireResistant())).register();

    public static final ItemEntry<SimpleFoiledItem> MIRACLE_POWDER = L2Hostility.REGISTRATE.item("miracle_powder", p -> new SimpleFoiledItem(p.rarity(Rarity.EPIC).fireResistant())).register();

    public static final ItemEntry<SimpleFoiledItem> MIRACLE_INGOT = L2Hostility.REGISTRATE.item("miracle_ingot", p -> new SimpleFoiledItem(p.rarity(Rarity.EPIC).fireResistant())).tag(new TagKey[] { LHTagGen.BEACON_PAYMENT }).register();

    public static final ItemEntry<CurseOfEnvy> CURSE_ENVY;

    public static final ItemEntry<CurseOfGluttony> CURSE_GLUTTONY;

    public static final ItemEntry<CurseOfGreed> CURSE_GREED;

    public static final ItemEntry<CurseOfLust> CURSE_LUST;

    public static final ItemEntry<CurseOfPride> CURSE_PRIDE;

    public static final ItemEntry<CurseOfSloth> CURSE_SLOTH;

    public static final ItemEntry<CurseOfWrath> CURSE_WRATH;

    public static final ItemEntry<RingOfOcean> RING_OCEAN;

    public static final ItemEntry<RingOfLife> RING_LIFE;

    public static final ItemEntry<RingOfDivinity> RING_DIVINITY;

    public static final ItemEntry<RingOfReflection> RING_REFLECTION;

    public static final ItemEntry<RingOfIncarceration> RING_INCARCERATION;

    public static final ItemEntry<RingOfCorrosion> RING_CORROSION;

    public static final ItemEntry<RingOfHealing> RING_HEALING;

    public static final ItemEntry<FlamingThorn> FLAMING_THORN;

    public static final ItemEntry<ImagineBreaker> IMAGINE_BREAKER;

    public static final ItemEntry<PlatinumStar> PLATINUM_STAR;

    public static final ItemEntry<InfinityGlove> INFINITY_GLOVE;

    public static final ItemEntry<OddeyesGlasses> ODDEYES_GLASSES;

    public static final ItemEntry<TripleStripCape> TRIPLE_STRIP_CAPE;

    public static final ItemEntry<Abrahadabra> ABRAHADABRA;

    public static final ItemEntry<GreedOfNidhoggur> NIDHOGGUR;

    public static final ItemEntry<PocketOfRestoration> RESTORATION;

    public static final ItemEntry<AbyssalThorn> ABYSSAL_THORN;

    public static final ItemEntry<DivinityCross> DIVINITY_CROSS;

    public static final ItemEntry<DivinityLight> DIVINITY_LIGHT;

    public static final ItemEntry<LootingCharm> LOOT_1;

    public static final ItemEntry<LootingCharm> LOOT_2;

    public static final ItemEntry<LootingCharm> LOOT_3;

    public static final ItemEntry<LootingCharm> LOOT_4;

    public static final ItemEntry<TraitAdderWand> ADDER = L2Hostility.REGISTRATE.item("trait_adder_wand", p -> new TraitAdderWand(p.stacksTo(1))).model((ctx, pvd) -> pvd.handheld(ctx)).register();

    public static final ItemEntry<TargetSelectWand> TARGET = L2Hostility.REGISTRATE.item("target_select_wand", p -> new TargetSelectWand(p.stacksTo(1))).model((ctx, pvd) -> pvd.handheld(ctx)).register();

    public static final ItemEntry<AiConfigWand> AI = L2Hostility.REGISTRATE.item("ai_config_wand", p -> new AiConfigWand(p.stacksTo(1))).model((ctx, pvd) -> pvd.handheld(ctx)).register();

    public static final ItemEntry<EquipmentWand> EQUIPMENT = L2Hostility.REGISTRATE.item("equipment_wand", p -> new EquipmentWand(p.stacksTo(1))).model((ctx, pvd) -> pvd.handheld(ctx)).register();

    public static final ItemEntry<SealedItem> SEAL = L2Hostility.REGISTRATE.item("sealed_item", p -> new SealedItem(p.stacksTo(1).fireResistant())).removeTab(LHBlocks.TAB.getKey()).tag(new TagKey[] { LHTagGen.NO_SEAL }).register();

    private static <T extends Item> ItemBuilder<T, ?> curio(String str, NonNullFunction<Item.Properties, T> factory) {
        return L2Hostility.REGISTRATE.item(str, factory).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/curio/" + ctx.getName()) }));
    }

    public static void register() {
    }

    static {
        TagKey<Item> head = ItemTags.create(new ResourceLocation("curios", "head"));
        DETECTOR_GLASSES = L2Hostility.REGISTRATE.item("detector_glasses", p -> new DetectorGlasses(p.stacksTo(1))).tag(new TagKey[] { head }).register();
        TagKey<Item> chaos = LHTagGen.CHAOS_CURIO;
        WITCH_WAND = L2Hostility.REGISTRATE.item("witch_wand", p -> new WitchWand(p.durability(300).rarity(Rarity.EPIC).fireResistant())).model((ctx, pvd) -> pvd.handheld(ctx)).tag(new TagKey[] { chaos }).register();
        TagKey<Item> charm = ItemTags.create(new ResourceLocation("curios", "charm"));
        TagKey<Item> curse = LHTagGen.CURSE_SLOT;
        LOOT_1 = curio("charm_of_looting_1", LootingCharm::new).tag(new TagKey[] { charm, curse, LHTagGen.NO_SEAL }).lang("Unpolished Looting Charm").register();
        LOOT_2 = curio("charm_of_looting_2", LootingCharm::new).tag(new TagKey[] { charm, curse, LHTagGen.NO_SEAL }).lang("Magical Looting Charm").register();
        LOOT_3 = curio("charm_of_looting_3", LootingCharm::new).tag(new TagKey[] { charm, curse, LHTagGen.NO_SEAL }).lang("Chaotic Looting Charm").register();
        LOOT_4 = curio("charm_of_looting_4", LootingCharm::new).tag(new TagKey[] { charm, curse, LHTagGen.NO_SEAL }).lang("Miraculous Looting Charm").register();
        CURSE_ENVY = curio("curse_of_envy", CurseOfEnvy::new).tag(new TagKey[] { chaos, charm, curse, LHTagGen.NO_SEAL }).register();
        CURSE_GLUTTONY = curio("curse_of_gluttony", CurseOfGluttony::new).tag(new TagKey[] { chaos, charm, curse, LHTagGen.NO_SEAL }).register();
        CURSE_GREED = curio("curse_of_greed", CurseOfGreed::new).tag(new TagKey[] { chaos, charm, curse, LHTagGen.NO_SEAL }).register();
        CURSE_LUST = curio("curse_of_lust", CurseOfLust::new).tag(new TagKey[] { chaos, charm, curse, LHTagGen.NO_SEAL }).register();
        CURSE_PRIDE = curio("curse_of_pride", CurseOfPride::new).tag(new TagKey[] { chaos, charm, curse }).register();
        CURSE_SLOTH = curio("curse_of_sloth", CurseOfSloth::new).tag(new TagKey[] { chaos, charm, curse, LHTagGen.NO_SEAL }).register();
        CURSE_WRATH = curio("curse_of_wrath", CurseOfWrath::new).tag(new TagKey[] { chaos, charm, curse }).register();
        TagKey<Item> ring = ItemTags.create(new ResourceLocation("curios", "ring"));
        RING_OCEAN = curio("ring_of_ocean", RingOfOcean::new).tag(new TagKey[] { chaos, ring }).register();
        RING_LIFE = curio("ring_of_life", RingOfLife::new).tag(new TagKey[] { chaos, ring }).register();
        RING_DIVINITY = curio("ring_of_divinity", RingOfDivinity::new).tag(new TagKey[] { chaos, ring }).register();
        RING_REFLECTION = curio("ring_of_reflection", RingOfReflection::new).tag(new TagKey[] { chaos, ring }).register();
        RING_INCARCERATION = curio("ring_of_incarceration", RingOfIncarceration::new).tag(new TagKey[] { chaos, ring }).register();
        RING_CORROSION = curio("ring_of_corrosion", RingOfCorrosion::new).tag(new TagKey[] { chaos, ring }).register();
        RING_HEALING = curio("ring_of_healing", RingOfHealing::new).tag(new TagKey[] { chaos, ring }).register();
        TagKey<Item> hand = ItemTags.create(new ResourceLocation("curios", "hands"));
        FLAMING_THORN = curio("flaming_thorn", FlamingThorn::new).tag(new TagKey[] { chaos, hand }).register();
        IMAGINE_BREAKER = curio("imagine_breaker", ImagineBreaker::new).tag(new TagKey[] { chaos, hand, LHTagGen.NO_SEAL }).register();
        PLATINUM_STAR = curio("platinum_star", PlatinumStar::new).tag(new TagKey[] { chaos, hand, charm }).register();
        INFINITY_GLOVE = curio("infinity_glove", InfinityGlove::new).tag(new TagKey[] { chaos, hand }).register();
        ABYSSAL_THORN = curio("abyssal_thorn", AbyssalThorn::new).tag(new TagKey[] { chaos, curse }).register();
        DIVINITY_CROSS = curio("divinity_cross", DivinityCross::new).tag(new TagKey[] { chaos, charm, curse }).register();
        DIVINITY_LIGHT = curio("divinity_light", DivinityLight::new).tag(new TagKey[] { chaos, charm, curse }).register();
        TagKey<Item> back = ItemTags.create(new ResourceLocation("curios", "back"));
        ODDEYES_GLASSES = curio("oddeyes_glasses", OddeyesGlasses::new).tag(new TagKey[] { chaos, head }).register();
        TRIPLE_STRIP_CAPE = curio("triple_strip_cape", TripleStripCape::new).tag(new TagKey[] { chaos, back }).register();
        ABRAHADABRA = curio("abrahadabra", Abrahadabra::new).tag(new TagKey[] { chaos, curse, LHTagGen.NO_SEAL }).register();
        NIDHOGGUR = curio("greed_of_nidhoggur", GreedOfNidhoggur::new).tag(new TagKey[] { chaos, curse, LHTagGen.NO_SEAL }).register();
        RESTORATION = L2Hostility.REGISTRATE.item("pocket_of_restoration", p -> new PocketOfRestoration(p, 128)).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[] { pvd.modLoc("item/curio/" + ctx.getName()) }).override().predicate(new ResourceLocation("l2hostility", "filled"), 0.5F).model(((ItemModelBuilder) pvd.getBuilder(ctx.getName() + "_full")).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/curio/" + ctx.getName() + "_full"))).tag(new TagKey[] { charm, LHTagGen.NO_SEAL }).register();
    }
}