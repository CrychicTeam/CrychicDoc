package com.mna.entities;

import com.mna.api.entities.FactionRaidRegistry;
import com.mna.entities.boss.CouncilWarden;
import com.mna.entities.boss.DemonLord;
import com.mna.entities.boss.FaerieQueen;
import com.mna.entities.boss.Odin;
import com.mna.entities.boss.PigDragon;
import com.mna.entities.boss.PumpkinKing;
import com.mna.entities.boss.WitherLich;
import com.mna.entities.boss.attacks.IllusionCreeper;
import com.mna.entities.boss.attacks.OrangeChickenProjectile;
import com.mna.entities.boss.attacks.PumpkinKingEntangle;
import com.mna.entities.boss.attacks.PumpkinKingIncinerate;
import com.mna.entities.boss.attacks.ThrownAllfatherAxe;
import com.mna.entities.boss.attacks.ThrownWeapon;
import com.mna.entities.boss.effects.Bifrost;
import com.mna.entities.boss.effects.DemonReturnPortal;
import com.mna.entities.constructs.BubbleBoat;
import com.mna.entities.constructs.ConstructAssemblyStand;
import com.mna.entities.constructs.MagicBroom;
import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.faction.Barkling;
import com.mna.entities.faction.Broker;
import com.mna.entities.faction.Chatterer;
import com.mna.entities.faction.Deathcap;
import com.mna.entities.faction.DemonImp;
import com.mna.entities.faction.HulkingZombie;
import com.mna.entities.faction.LanternWraith;
import com.mna.entities.faction.LivingWard;
import com.mna.entities.faction.Pixie;
import com.mna.entities.faction.SkeletonAssassin;
import com.mna.entities.faction.Spellbreaker;
import com.mna.entities.faction.WitchHunter;
import com.mna.entities.faction.util.FactionRaid;
import com.mna.entities.faction.util.FactionWar;
import com.mna.entities.faction.util.WitchHunterArrow;
import com.mna.entities.manaweaving.Manaweave;
import com.mna.entities.manaweaving.ThrownManaweaveBottle;
import com.mna.entities.passive.Magmoo;
import com.mna.entities.projectile.SentryProjectile;
import com.mna.entities.projectile.SkeletonAssassinBolo;
import com.mna.entities.projectile.SkeletonAssassinShuriken;
import com.mna.entities.projectile.WitchhunterTrickshot;
import com.mna.entities.rituals.AncientCouncil;
import com.mna.entities.rituals.DemonStone;
import com.mna.entities.rituals.FeyLight;
import com.mna.entities.rituals.FlatLands;
import com.mna.entities.rituals.FlatLandsProjectile;
import com.mna.entities.rituals.Portal;
import com.mna.entities.rituals.Ritual;
import com.mna.entities.rituals.TimeChangeBall;
import com.mna.entities.sorcery.AffinityIcon;
import com.mna.entities.sorcery.EntityDecoy;
import com.mna.entities.sorcery.Rift;
import com.mna.entities.sorcery.targeting.BoundBowProjectile;
import com.mna.entities.sorcery.targeting.Smite;
import com.mna.entities.sorcery.targeting.SpellBeam;
import com.mna.entities.sorcery.targeting.SpellCone;
import com.mna.entities.sorcery.targeting.SpellEmanation;
import com.mna.entities.sorcery.targeting.SpellEmber;
import com.mna.entities.sorcery.targeting.SpellFissure;
import com.mna.entities.sorcery.targeting.SpellFocus;
import com.mna.entities.sorcery.targeting.SpellProjectile;
import com.mna.entities.sorcery.targeting.SpellSigil;
import com.mna.entities.sorcery.targeting.SpellWall;
import com.mna.entities.sorcery.targeting.SpellWave;
import com.mna.entities.sorcery.targeting.SpellWrath;
import com.mna.entities.summon.AnimusBlock;
import com.mna.entities.summon.GreaterAnimus;
import com.mna.entities.summon.InsectSwarm;
import com.mna.entities.summon.SummonedSpectralHorse;
import com.mna.entities.utility.DisplayDamage;
import com.mna.entities.utility.DisplayReagents;
import com.mna.entities.utility.EldrinFlight;
import com.mna.entities.utility.FillHole;
import com.mna.entities.utility.ImpulseProjectile;
import com.mna.entities.utility.PresentItem;
import com.mna.entities.utility.RisingBlock;
import com.mna.entities.utility.SpellFX;
import com.mna.entities.utility.StationaryBlock;
import com.mna.entities.utility.ThrownRunescribingPattern;
import com.mna.entities.utility.WanderingWizard;
import com.mna.factions.Factions;
import com.mna.items.spawn_eggs.ModSpawnEggItem;
import java.util.HashMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "mna");

    public static final RegistryObject<EntityType<Ritual>> RITUAL_ENTITY = ENTITY_TYPES.register("ritual_entity", () -> EntityType.Builder.of(Ritual::new, MobCategory.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).fireImmune().noSummon().build("mna:ritual_entity"));

    public static final RegistryObject<EntityType<Portal>> PORTAL_ENTITY = ENTITY_TYPES.register("portal_entity", () -> EntityType.Builder.of(Portal::new, MobCategory.MISC).sized(1.0F, 2.0F).setShouldReceiveVelocityUpdates(false).fireImmune().noSummon().build("mna:portal_entity"));

    public static final RegistryObject<EntityType<DisplayReagents>> REAGENT_ENTITY = ENTITY_TYPES.register("reagent_entity", () -> EntityType.Builder.of(DisplayReagents::new, MobCategory.MISC).sized(1.0F, 1.0F).setShouldReceiveVelocityUpdates(false).fireImmune().noSummon().build("mna:reagent_entity"));

    public static final RegistryObject<EntityType<TimeChangeBall>> STARBALL_ENTITY = ENTITY_TYPES.register("starball_entity", () -> EntityType.Builder.of(TimeChangeBall::new, MobCategory.MISC).sized(1.0F, 1.0F).setShouldReceiveVelocityUpdates(false).fireImmune().noSummon().build("mna:starball_entity"));

    public static final RegistryObject<EntityType<Manaweave>> MANAWEAVE_ENTITY = ENTITY_TYPES.register("manaweave_entity", () -> EntityType.Builder.of(Manaweave::new, MobCategory.MISC).sized(1.0F, 1.0F).setShouldReceiveVelocityUpdates(false).fireImmune().noSummon().build("mna:manaweave_entity"));

    public static final RegistryObject<EntityType<SpellProjectile>> SPELL_PROJECTILE = ENTITY_TYPES.register("spell_projectile", () -> EntityType.Builder.of(SpellProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).setShouldReceiveVelocityUpdates(true).fireImmune().noSummon().build("mna:spell_projectile"));

    public static final RegistryObject<EntityType<OrangeChickenProjectile>> ORANGE_CHICKEN = ENTITY_TYPES.register("orange_chicken", () -> EntityType.Builder.of(OrangeChickenProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).setShouldReceiveVelocityUpdates(true).fireImmune().noSummon().build("mna:orange_chicken"));

    public static final RegistryObject<EntityType<IllusionCreeper>> ILLUSION_CREEPER = ENTITY_TYPES.register("illusion_creeper", () -> EntityType.Builder.of(IllusionCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8).setShouldReceiveVelocityUpdates(true).build("mna:illusion_creeper"));

    public static final RegistryObject<EntityType<ThrownAllfatherAxe>> ALLFATHER_AXE = ENTITY_TYPES.register("allfather_axe_thrown", () -> EntityType.Builder.of(ThrownAllfatherAxe::new, MobCategory.MISC).sized(1.75F, 0.25F).setShouldReceiveVelocityUpdates(false).fireImmune().noSummon().build("mna:allfather_axe_thrown"));

    public static final RegistryObject<EntityType<Smite>> SMITE_PROJECTILE = ENTITY_TYPES.register("smite_projectile", () -> EntityType.Builder.of(Smite::new, MobCategory.MISC).sized(0.25F, 0.25F).setShouldReceiveVelocityUpdates(false).fireImmune().noSummon().build("mna:smite_projectile"));

    public static final RegistryObject<EntityType<SentryProjectile>> SENTRY_PROJECTILE = ENTITY_TYPES.register("sentry_projectile", () -> EntityType.Builder.of(SentryProjectile::new, MobCategory.MISC).sized(0.05F, 0.05F).setShouldReceiveVelocityUpdates(false).fireImmune().noSummon().build("mna:sentry_projectile"));

    public static final RegistryObject<EntityType<SpellFX>> SPELL_FX = ENTITY_TYPES.register("spell_fx", () -> EntityType.Builder.of(SpellFX::new, MobCategory.MISC).sized(0.0F, 0.0F).noSave().noSummon().setShouldReceiveVelocityUpdates(false).fireImmune().build("mna:spell_fx"));

    public static final RegistryObject<EntityType<DisplayDamage>> DAMAGE_NUMBERS = ENTITY_TYPES.register("damage_numbers", () -> EntityType.Builder.of(DisplayDamage::new, MobCategory.MISC).sized(0.25F, 0.25F).noSave().noSummon().setShouldReceiveVelocityUpdates(false).fireImmune().build("mna:damage_numbers"));

    public static final RegistryObject<EntityType<Rift>> RIFT = ENTITY_TYPES.register("rift", () -> EntityType.Builder.of(Rift::new, MobCategory.MISC).sized(1.0F, 1.0F).setShouldReceiveVelocityUpdates(false).fireImmune().build("mna:rift"));

    public static final RegistryObject<EntityType<MagicBroom>> MAGIC_BROOM = ENTITY_TYPES.register("magic_broom", () -> EntityType.Builder.of(MagicBroom::new, MobCategory.MISC).sized(0.7F, 0.7F).noSummon().build("mna:magic_broom"));

    public static final RegistryObject<EntityType<BubbleBoat>> BUBBLE_BOAT = ENTITY_TYPES.register("bubble_boat", () -> EntityType.Builder.of(BubbleBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).build("mna:bubble_boat"));

    public static final RegistryObject<EntityType<FlatLandsProjectile>> FLAT_LANDS_PROJECTILE = ENTITY_TYPES.register("flat_lands_projectile", () -> EntityType.Builder.of(FlatLandsProjectile::new, MobCategory.MISC).sized(0.2F, 0.2F).noSummon().build("mna:flat_lands_projectile"));

    public static final RegistryObject<EntityType<FlatLands>> FLAT_LANDS = ENTITY_TYPES.register("flat_lands", () -> EntityType.Builder.of(FlatLands::new, MobCategory.MISC).sized(0.2F, 0.2F).noSummon().build("mna:flat_lands"));

    public static final RegistryObject<EntityType<Construct>> ANIMATED_CONSTRUCT = ENTITY_TYPES.register("animated_construct", () -> EntityType.Builder.of(Construct::new, MobCategory.MISC).sized(0.6F, 1.75F).noSummon().build("mna:animated_construct"));

    public static final RegistryObject<EntityType<ConstructAssemblyStand>> CONSTRUCT_ASSEMBLY_STAND = ENTITY_TYPES.register("construct_assembly_stand", () -> EntityType.Builder.of(ConstructAssemblyStand::new, MobCategory.MISC).sized(0.5F, 1.975F).clientTrackingRange(10).noSummon().build("mna:construct_assembly_stand"));

    public static final RegistryObject<EntityType<DemonStone>> DEMON_STONE = ENTITY_TYPES.register("demon_stone", () -> EntityType.Builder.of(DemonStone::new, MobCategory.MISC).sized(5.0F, 5.0F).build("mna:demon_stone"));

    public static final RegistryObject<EntityType<DemonLord>> DEMON_LORD = ENTITY_TYPES.register("demon_lord", () -> EntityType.Builder.of(DemonLord::new, MobCategory.MONSTER).sized(3.0F, 5.0F).build("mna:demon_lord"));

    public static final RegistryObject<EntityType<FeyLight>> FEY_LIGHT = ENTITY_TYPES.register("faerie_light", () -> EntityType.Builder.of(FeyLight::new, MobCategory.MISC).sized(0.5F, 0.5F).build("mna:faerie_light"));

    public static final RegistryObject<EntityType<FaerieQueen>> FAERIE_QUEEN = ENTITY_TYPES.register("faerie_queen", () -> EntityType.Builder.of(FaerieQueen::new, MobCategory.MISC).sized(1.0F, 2.0F).build("mna:faerie_queen"));

    public static final RegistryObject<EntityType<AncientCouncil>> ANCIENT_COUNCIL = ENTITY_TYPES.register("ancient_council", () -> EntityType.Builder.of(AncientCouncil::new, MobCategory.MISC).sized(1.0F, 2.0F).build("mna:ancient_council"));

    public static final RegistryObject<EntityType<CouncilWarden>> COUNCIL_WARDEN = ENTITY_TYPES.register("council_warden", () -> EntityType.Builder.of(CouncilWarden::new, MobCategory.MONSTER).sized(1.0F, 2.0F).build("mna:council_warden"));

    public static final RegistryObject<EntityType<SpellWall>> SPELL_WALL = ENTITY_TYPES.register("spell_entity_wall", () -> EntityType.Builder.of(SpellWall::new, MobCategory.MISC).sized(1.0F, 1.0F).noSummon().build("mna:spell_entity_wall"));

    public static final RegistryObject<EntityType<SpellWave>> SPELL_WAVE = ENTITY_TYPES.register("spell_entity_wave", () -> EntityType.Builder.of(SpellWave::new, MobCategory.MISC).sized(1.0F, 1.0F).noSummon().build("mna:spell_entity_wave"));

    public static final RegistryObject<EntityType<SpellFissure>> SPELL_FISSURE = ENTITY_TYPES.register("spell_entity_fissure", () -> EntityType.Builder.of(SpellFissure::new, MobCategory.MISC).sized(1.0F, 1.0F).noSummon().build("mna:spell_entity_fissure"));

    public static final RegistryObject<EntityType<SpellEmber>> SPELL_EMBER = ENTITY_TYPES.register("spell_entity_ember", () -> EntityType.Builder.of(SpellEmber::new, MobCategory.MISC).sized(0.25F, 0.25F).noSummon().build("mna:spell_entity_ember"));

    public static final RegistryObject<EntityType<SpellFocus>> SPELL_FOCUS = ENTITY_TYPES.register("spell_entity_focus", () -> EntityType.Builder.of(SpellFocus::new, MobCategory.MISC).sized(0.25F, 0.25F).noSummon().build("mna:spell_entity_focus"));

    public static final RegistryObject<EntityType<SpellEmanation>> SPELL_EMANATION = ENTITY_TYPES.register("spell_entity_emanation", () -> EntityType.Builder.of(SpellEmanation::new, MobCategory.MISC).sized(0.25F, 0.25F).noSummon().build("mna:spell_entity_emanation"));

    public static final RegistryObject<EntityType<SpellCone>> SPELL_CONE = ENTITY_TYPES.register("spell_entity_cone", () -> EntityType.Builder.of(SpellCone::new, MobCategory.MISC).sized(0.25F, 0.25F).noSummon().build("mna:spell_entity_cone"));

    public static final RegistryObject<EntityType<SpellWrath>> SPELL_WRATH = ENTITY_TYPES.register("spell_entity_rain", () -> EntityType.Builder.of(SpellWrath::new, MobCategory.MISC).sized(0.25F, 0.25F).noSummon().build("mna:spell_entity_rain"));

    public static final RegistryObject<EntityType<SpellBeam>> SPELL_BEAM = ENTITY_TYPES.register("spell_entity_beam", () -> EntityType.Builder.of(SpellBeam::new, MobCategory.MISC).sized(0.25F, 0.25F).noSummon().build("mna:spell_entity_beam"));

    public static final RegistryObject<EntityType<WandClone>> WAND_CLONE = ENTITY_TYPES.register("wand_clone", () -> EntityType.Builder.of(WandClone::new, MobCategory.MISC).sized(0.2F, 0.2F).noSummon().build("mna:wand_clone"));

    public static final RegistryObject<EntityType<AffinityIcon>> AFFINITY_ICON = ENTITY_TYPES.register("affinity_icon_entity", () -> EntityType.Builder.of(AffinityIcon::new, MobCategory.MISC).sized(1.0F, 1.0F).noSummon().build("mna:affinity_icon_entity"));

    public static final RegistryObject<EntityType<Deathcap>> MUSHROOM_SOLDIER = ENTITY_TYPES.register("mushroom_soldier", () -> EntityType.Builder.of(Deathcap::new, MobCategory.MONSTER).sized(1.0F, 1.25F).build("mna:mushroom_soldier"));

    public static final RegistryObject<EntityType<Pixie>> PIXIE = ENTITY_TYPES.register("pixie", () -> EntityType.Builder.of(Pixie::new, MobCategory.MONSTER).sized(0.4F, 0.5F).build("mna:pixie"));

    public static final RegistryObject<EntityType<EldrinFlight>> ELDRIN_FLIGHT = ENTITY_TYPES.register("eldrin_flight", () -> EntityType.Builder.of(EldrinFlight::new, MobCategory.MISC).sized(0.25F, 0.25F).noSummon().build("mna:eldrin_flight"));

    public static final RegistryObject<EntityType<DemonImp>> DEMON_IMP = ENTITY_TYPES.register("demon_imp", () -> EntityType.Builder.of(DemonImp::new, MobCategory.MONSTER).sized(0.75F, 1.5F).build("mna:demon_imp"));

    public static final RegistryObject<EntityType<Barkling>> BARKLING = ENTITY_TYPES.register("barkling", () -> EntityType.Builder.of(Barkling::new, MobCategory.MONSTER).sized(0.75F, 2.0F).build("mna:barkling"));

    public static final RegistryObject<EntityType<LanternWraith>> LANTERN_WRAITH = ENTITY_TYPES.register("lantern_wraith", () -> EntityType.Builder.of(LanternWraith::new, MobCategory.MONSTER).sized(1.0F, 1.5F).build("mna:lantern_wraith"));

    public static final RegistryObject<EntityType<Spellbreaker>> SPELLBREAKER = ENTITY_TYPES.register("spell_breaker", () -> EntityType.Builder.of(Spellbreaker::new, MobCategory.MONSTER).sized(1.0F, 2.0F).build("mna:spell_breaker"));

    public static final RegistryObject<EntityType<LivingWard>> LIVING_WARD = ENTITY_TYPES.register("living_ward", () -> EntityType.Builder.of(LivingWard::new, MobCategory.MONSTER).sized(1.0F, 1.0F).build("mna:living_ward"));

    public static final RegistryObject<EntityType<WitchHunter>> WITCH_HUNTER = ENTITY_TYPES.register("witch_hunter", () -> EntityType.Builder.of(WitchHunter::new, MobCategory.MONSTER).sized(1.0F, 2.0F).build("mna:witch_hunter"));

    public static final RegistryObject<EntityType<HulkingZombie>> HULKING_ZOMBIE = ENTITY_TYPES.register("hulking_zombie", () -> EntityType.Builder.of(HulkingZombie::new, MobCategory.MONSTER).sized(1.0F, 2.0F).build("mna:hulking_zombie"));

    public static final RegistryObject<EntityType<SkeletonAssassin>> SKELETON_ASSASSIN = ENTITY_TYPES.register("skeleton_assassin", () -> EntityType.Builder.of(SkeletonAssassin::new, MobCategory.MONSTER).sized(1.0F, 2.0F).build("mna:skeleton_assassin"));

    public static final RegistryObject<EntityType<Chatterer>> CHATTERER = ENTITY_TYPES.register("chatterer", () -> EntityType.Builder.of(Chatterer::new, MobCategory.MONSTER).sized(0.5F, 0.5F).build("mna:chatterer"));

    public static final RegistryObject<EntityType<SpellSigil>> SPELL_RUNE = ENTITY_TYPES.register("spell_rune_entity", () -> EntityType.Builder.of(SpellSigil::new, MobCategory.MISC).sized(1.0F, 0.1F).noSummon().build("mna:spell_rune_entity"));

    public static final RegistryObject<EntityType<WitchhunterTrickshot>> WITCH_HUNTER_TRICKSHOT = ENTITY_TYPES.register("witch_hunter_trickshot", () -> EntityType.Builder.of(WitchhunterTrickshot::new, MobCategory.MISC).sized(1.0F, 1.0F).noSummon().noSave().build("mna:witch_hunter_trickshot"));

    public static final RegistryObject<EntityType<WitchHunterArrow>> WITCH_HUNTER_ARROW = ENTITY_TYPES.register("witch_hunter_arrow", () -> EntityType.Builder.of(WitchHunterArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).noSave().noSummon().clientTrackingRange(4).updateInterval(20).build("mna:witch_hunter_arrow"));

    public static final RegistryObject<EntityType<FactionRaid>> FACTION_RAID_ENTITY = ENTITY_TYPES.register("faction_raid", () -> EntityType.Builder.of(FactionRaid::new, MobCategory.MISC).sized(1.0F, 1.0F).noSummon().build("mna:faction_raid"));

    public static final RegistryObject<EntityType<FactionWar>> FACTION_WAR = ENTITY_TYPES.register("faction_war", () -> EntityType.Builder.of(FactionWar::new, MobCategory.MONSTER).sized(1.0F, 1.0F).build("mna:faction_war"));

    public static final RegistryObject<EntityType<PresentItem>> PRESENTATION_ENTITY = ENTITY_TYPES.register("presentation_entity", () -> EntityType.Builder.of(PresentItem::new, MobCategory.MISC).sized(1.0F, 1.0F).noSummon().build("mna:presentation_entity"));

    public static final RegistryObject<EntityType<EntityDecoy>> DECOY_ENTITY = ENTITY_TYPES.register("decoy_entity", () -> EntityType.Builder.of(EntityDecoy::new, MobCategory.MISC).sized(1.0F, 2.0F).noSummon().build("mna:decoy_entity"));

    public static final RegistryObject<EntityType<AnimusBlock>> ANIMUS_BLOCK = ENTITY_TYPES.register("animus_block", () -> EntityType.Builder.of(AnimusBlock::new, MobCategory.MISC).sized(1.0F, 1.0F).noSummon().build("mna:animus_block"));

    public static final RegistryObject<EntityType<GreaterAnimus>> GREATER_ANIMUS = ENTITY_TYPES.register("greater_animus", () -> EntityType.Builder.of(GreaterAnimus::new, MobCategory.MISC).sized(1.0F, 2.0F).noSummon().build("mna:greater_animus"));

    public static final RegistryObject<EntityType<WanderingWizard>> WANDERING_WIZARD = ENTITY_TYPES.register("wandering_wizard", () -> EntityType.Builder.of(WanderingWizard::new, MobCategory.MISC).sized(1.0F, 2.0F).build("mna:wandering_wizard"));

    public static final RegistryObject<EntityType<Broker>> BROKER = ENTITY_TYPES.register("broker", () -> EntityType.Builder.of(Broker::new, MobCategory.MISC).sized(1.0F, 2.0F).build("mna:broker"));

    public static final RegistryObject<EntityType<ThrownManaweaveBottle>> MANAWEAVE_BOTTLE_THROWN = ENTITY_TYPES.register("thrown_manaweave_potion", () -> EntityType.Builder.of(ThrownManaweaveBottle::new, MobCategory.MISC).sized(0.2F, 0.2F).noSummon().build("mna:thrown_manaweave_potion"));

    public static final RegistryObject<EntityType<RisingBlock>> RISING_BLOCK_ENTITY = ENTITY_TYPES.register("rising_block_entity", () -> EntityType.Builder.of(RisingBlock::new, MobCategory.MISC).sized(1.0F, 1.0F).noSummon().build("mna:rising_block_entity"));

    public static final RegistryObject<EntityType<StationaryBlock>> STATIONARY_BLOCK_ENTITY = ENTITY_TYPES.register("stationary_block_entity", () -> EntityType.Builder.of(StationaryBlock::new, MobCategory.MISC).sized(1.0F, 1.0F).noSummon().build("mna:stationary_block_entity"));

    public static final RegistryObject<EntityType<SummonedSpectralHorse>> SPECTRAL_HORSE = ENTITY_TYPES.register("spectral_horse", () -> EntityType.Builder.of(SummonedSpectralHorse::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).build("mna:spectral_horse"));

    public static final RegistryObject<EntityType<ThrownRunescribingPattern>> THROWN_RUNESCRIBE_PATTERN = ENTITY_TYPES.register("thrown_runescribing_pattern", () -> EntityType.Builder.of(ThrownRunescribingPattern::new, MobCategory.MISC).sized(0.25F, 0.25F).noSummon().build("mna:thrown_runescribing_pattern"));

    public static final RegistryObject<EntityType<ImpulseProjectile>> IMPULSE_PROJECTILE = ENTITY_TYPES.register("impulse_projectile", () -> EntityType.Builder.of(ImpulseProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).noSummon().build("mna:impulse_projectile"));

    public static final RegistryObject<EntityType<BoundBowProjectile>> BOUND_BOW_PROJECTILE = ENTITY_TYPES.register("bound_bow_projectile", () -> EntityType.Builder.of(BoundBowProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).noSave().noSummon().clientTrackingRange(4).updateInterval(20).build("mna:bound_bow_projectile"));

    public static final RegistryObject<EntityType<SkeletonAssassinShuriken>> SKELETON_ASSASSIN_SHURIKEN = ENTITY_TYPES.register("skeleton_assassin_shuriken", () -> EntityType.Builder.of(SkeletonAssassinShuriken::new, MobCategory.MISC).sized(0.5F, 0.5F).noSave().noSummon().clientTrackingRange(4).updateInterval(20).build("mna:skeleton_assassin_shuriken"));

    public static final RegistryObject<EntityType<SkeletonAssassinBolo>> SKELETON_ASSASSIN_BOLO = ENTITY_TYPES.register("skeleton_assassin_bolo", () -> EntityType.Builder.of(SkeletonAssassinBolo::new, MobCategory.MISC).sized(0.5F, 0.5F).noSave().noSummon().clientTrackingRange(4).updateInterval(20).build("mna:skeleton_assassin_bolo"));

    public static final RegistryObject<EntityType<WitherLich>> LICH = ENTITY_TYPES.register("wither_lich", () -> EntityType.Builder.of(WitherLich::new, MobCategory.MONSTER).sized(2.0F, 3.0F).build("mna:wither_lich"));

    public static final RegistryObject<EntityType<PigDragon>> PIG_DRAGON = ENTITY_TYPES.register("tfpd", () -> EntityType.Builder.of(PigDragon::new, MobCategory.MONSTER).sized(2.0F, 3.0F).build("mna:tfpd"));

    public static final RegistryObject<EntityType<PumpkinKing>> PUMPKIN_KING = ENTITY_TYPES.register("pumpkin_king", () -> EntityType.Builder.of(PumpkinKing::new, MobCategory.MONSTER).sized(1.0F, 6.0F).build("mna:pumpkin_king"));

    public static final RegistryObject<EntityType<Odin>> ODIN = ENTITY_TYPES.register("odin", () -> EntityType.Builder.of(Odin::new, MobCategory.MONSTER).sized(1.0F, 2.0F).build("mna:odin"));

    public static final RegistryObject<EntityType<Bifrost>> BIFROST = ENTITY_TYPES.register("bifrost", () -> EntityType.Builder.of(Bifrost::new, MobCategory.MISC).sized(0.25F, 0.25F).setShouldReceiveVelocityUpdates(false).fireImmune().build("mna:bifrost"));

    public static final RegistryObject<EntityType<PumpkinKingIncinerate>> PUMPKIN_KING_INCINERATE = ENTITY_TYPES.register("pumpkin_king_incinerate", () -> EntityType.Builder.of(PumpkinKingIncinerate::new, MobCategory.MISC).sized(0.25F, 0.25F).noSave().noSummon().fireImmune().build("mna:pumpkin_king_incinerate"));

    public static final RegistryObject<EntityType<PumpkinKingEntangle>> PUMPKIN_KING_ENTANGLE = ENTITY_TYPES.register("pumpkin_king_entangle", () -> EntityType.Builder.of(PumpkinKingEntangle::new, MobCategory.MISC).sized(0.25F, 0.25F).noSave().noSummon().fireImmune().build("mna:pumpkin_king_incinerate"));

    public static final RegistryObject<EntityType<FillHole>> FILL_HOLE = ENTITY_TYPES.register("fill_hole", () -> EntityType.Builder.of(FillHole::new, MobCategory.MISC).sized(0.25F, 0.25F).noSave().noSummon().fireImmune().build("mna:fill_hole"));

    public static final RegistryObject<EntityType<InsectSwarm>> INSECT_SWARM = ENTITY_TYPES.register("insect_swarm", () -> EntityType.Builder.of(InsectSwarm::new, MobCategory.MONSTER).sized(1.25F, 1.25F).noSummon().build("mna:insect_swarm"));

    public static final RegistryObject<EntityType<DemonReturnPortal>> DEMON_RETURN_PORTAL = ENTITY_TYPES.register("demon_return_portal", () -> EntityType.Builder.of(DemonReturnPortal::new, MobCategory.MISC).sized(3.0F, 0.25F).noSave().noSummon().build("mna:demon_return_portal"));

    public static final RegistryObject<EntityType<ThrownWeapon>> THROWN_WEAPON = ENTITY_TYPES.register("thrown_weapon", () -> EntityType.Builder.of(ThrownWeapon::new, MobCategory.MISC).sized(1.75F, 0.25F).setShouldReceiveVelocityUpdates(false).fireImmune().noSummon().build("mna:thrown_weapon"));

    public static final RegistryObject<EntityType<Magmoo>> MAGMOO = ENTITY_TYPES.register("magmoo", () -> EntityType.Builder.of(Magmoo::new, MobCategory.MISC).sized(0.9F, 1.4F).clientTrackingRange(10).fireImmune().build("mna:magmoo"));

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterEntities(RegisterEvent event) {
        ModSpawnEggItem.initUnaddedEggs();
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(COUNCIL_WARDEN.get(), CouncilWarden.getGlobalAttributes().build());
        event.put(DEMON_LORD.get(), DemonLord.getGlobalAttributes().build());
        event.put(FAERIE_QUEEN.get(), FaerieQueen.getGlobalAttributes().build());
        event.put(ANCIENT_COUNCIL.get(), LivingUtilityEntity.getGlobalAttributes().build());
        event.put(MAGIC_BROOM.get(), MagicBroom.getGlobalAttributes().build());
        event.put(ANIMATED_CONSTRUCT.get(), Construct.getGlobalAttributes().build());
        event.put(MUSHROOM_SOLDIER.get(), Deathcap.getGlobalAttributes().build());
        event.put(PIXIE.get(), Pixie.getGlobalAttributes().build());
        event.put(BARKLING.get(), Barkling.getGlobalAttributes().build());
        event.put(DEMON_IMP.get(), DemonImp.getGlobalAttributes().build());
        event.put(HULKING_ZOMBIE.get(), HulkingZombie.getGlobalAttributes().build());
        event.put(SKELETON_ASSASSIN.get(), SkeletonAssassin.getGlobalAttributes().build());
        event.put(LANTERN_WRAITH.get(), LanternWraith.getGlobalAttributes().build());
        event.put(SPELLBREAKER.get(), Spellbreaker.getGlobalAttributes().build());
        event.put(WITCH_HUNTER.get(), WitchHunter.getGlobalAttributes().build());
        event.put(DECOY_ENTITY.get(), EntityDecoy.getGlobalAttributes().build());
        event.put(ANIMUS_BLOCK.get(), AnimusBlock.getGlobalAttributes().build());
        event.put(WANDERING_WIZARD.get(), WanderingWizard.getGlobalAttributes().build());
        event.put(BROKER.get(), Broker.getGlobalAttributes().build());
        event.put(SPECTRAL_HORSE.get(), SummonedSpectralHorse.getGlobalAttributes().build());
        event.put(LICH.get(), WitherLich.getGlobalAttributes().build());
        event.put(PIG_DRAGON.get(), PigDragon.getGlobalAttributes().build());
        event.put(PUMPKIN_KING.get(), PumpkinKing.getGlobalAttributes().build());
        event.put(ODIN.get(), Odin.getGlobalAttributes().build());
        event.put(CONSTRUCT_ASSEMBLY_STAND.get(), LivingEntity.createLivingAttributes().build());
        event.put(GREATER_ANIMUS.get(), GreaterAnimus.createAttributes().build());
        event.put(INSECT_SWARM.get(), InsectSwarm.createAttributes().build());
        event.put(LIVING_WARD.get(), LivingWard.getGlobalAttributes().build());
        event.put(ILLUSION_CREEPER.get(), Creeper.createAttributes().build());
        event.put(FACTION_WAR.get(), FactionWar.getGlobalAttributes().build());
        event.put(CHATTERER.get(), Chatterer.getGlobalAttributes().build());
        event.put(MAGMOO.get(), Cow.createAttributes().build());
    }

    @SubscribeEvent
    public static void handleSpawnPlacementRegistration(SpawnPlacementRegisterEvent event) {
        event.register(DEMON_IMP.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, DemonImp::canSpawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(LANTERN_WRAITH.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LanternWraith::canSpawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(SPELLBREAKER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(WITCH_HUNTER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(LIVING_WARD.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LivingWard::canSpawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(PIXIE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Pixie::canSpawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(MUSHROOM_SOLDIER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(BARKLING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(HULKING_ZOMBIE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(SKELETON_ASSASSIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(CHATTERER.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Chatterer::canSpawnPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(FACTION_WAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FactionWar::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
    }

    @SubscribeEvent
    public static void loadCompleteEventHandler(FMLLoadCompleteEvent event) {
        FactionRaidRegistry.registerSoldier(Factions.COUNCIL, SPELLBREAKER.get(), new HashMap<Integer, Integer>() {

            {
                this.put(30, 0);
                this.put(65, 1);
                this.put(100, 2);
            }
        });
        FactionRaidRegistry.registerSoldier(Factions.COUNCIL, WITCH_HUNTER.get(), new HashMap<Integer, Integer>() {

            {
                this.put(15, 0);
                this.put(45, 1);
                this.put(75, 2);
            }
        });
        FactionRaidRegistry.registerSoldier(Factions.COUNCIL, LIVING_WARD.get(), new HashMap<Integer, Integer>() {

            {
                this.put(25, 0);
                this.put(35, 1);
                this.put(45, 2);
            }
        });
        FactionRaidRegistry.registerSoldier(Factions.FEY, BARKLING.get(), new HashMap<Integer, Integer>() {

            {
                this.put(10, 0);
                this.put(25, 1);
                this.put(40, 2);
            }
        });
        FactionRaidRegistry.registerSoldier(Factions.FEY, MUSHROOM_SOLDIER.get(), new HashMap<Integer, Integer>() {

            {
                this.put(10, 0);
                this.put(45, 1);
                this.put(80, 2);
            }
        });
        FactionRaidRegistry.registerSoldier(Factions.FEY, PIXIE.get(), new HashMap<Integer, Integer>() {

            {
                this.put(10, 0);
                this.put(35, 1);
                this.put(60, 2);
            }
        });
        FactionRaidRegistry.registerSoldier(Factions.DEMONS, DEMON_IMP.get(), new HashMap<Integer, Integer>() {

            {
                this.put(20, 0);
                this.put(50, 1);
                this.put(80, 2);
            }
        });
        FactionRaidRegistry.registerSoldier(Factions.DEMONS, LANTERN_WRAITH.get(), new HashMap<Integer, Integer>() {

            {
                this.put(10, 0);
                this.put(35, 1);
                this.put(60, 2);
            }
        });
        FactionRaidRegistry.registerSoldier(Factions.UNDEAD, HULKING_ZOMBIE.get(), new HashMap<Integer, Integer>() {

            {
                this.put(30, 0);
                this.put(65, 1);
                this.put(100, 2);
            }
        });
        FactionRaidRegistry.registerSoldier(Factions.UNDEAD, SKELETON_ASSASSIN.get(), new HashMap<Integer, Integer>() {

            {
                this.put(15, 0);
                this.put(30, 1);
                this.put(45, 2);
            }
        });
        FactionRaidRegistry.registerSoldier(Factions.UNDEAD, CHATTERER.get(), new HashMap<Integer, Integer>() {

            {
                this.put(15, 0);
                this.put(30, 1);
                this.put(45, 2);
            }
        });
    }
}