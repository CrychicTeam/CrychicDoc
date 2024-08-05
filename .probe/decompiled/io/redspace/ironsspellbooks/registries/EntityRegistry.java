package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.entity.VisualFallingBlockEntity;
import io.redspace.ironsspellbooks.entity.mobs.CatacombsZombie;
import io.redspace.ironsspellbooks.entity.mobs.MagehunterVindicator;
import io.redspace.ironsspellbooks.entity.mobs.SummonedHorse;
import io.redspace.ironsspellbooks.entity.mobs.SummonedPolarBear;
import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import io.redspace.ironsspellbooks.entity.mobs.SummonedVex;
import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingBoss;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingCorpseEntity;
import io.redspace.ironsspellbooks.entity.mobs.debug_wizard.DebugWizard;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.alchemist.ApothecaristEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.archevoker.ArchevokerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cryomancer.CryomancerEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.cultist.CultistEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.priest.PriestEntity;
import io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer.PyromancerEntity;
import io.redspace.ironsspellbooks.entity.spells.ArrowVolleyEntity;
import io.redspace.ironsspellbooks.entity.spells.ChainLightning;
import io.redspace.ironsspellbooks.entity.spells.EarthquakeAoe;
import io.redspace.ironsspellbooks.entity.spells.EchoingStrikeEntity;
import io.redspace.ironsspellbooks.entity.spells.ExtendedWitherSkull;
import io.redspace.ironsspellbooks.entity.spells.HealingAoe;
import io.redspace.ironsspellbooks.entity.spells.LightningStrike;
import io.redspace.ironsspellbooks.entity.spells.StompAoe;
import io.redspace.ironsspellbooks.entity.spells.acid_orb.AcidOrb;
import io.redspace.ironsspellbooks.entity.spells.ball_lightning.BallLightning;
import io.redspace.ironsspellbooks.entity.spells.black_hole.BlackHole;
import io.redspace.ironsspellbooks.entity.spells.blood_needle.BloodNeedle;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import io.redspace.ironsspellbooks.entity.spells.comet.Comet;
import io.redspace.ironsspellbooks.entity.spells.cone_of_cold.ConeOfColdProjectile;
import io.redspace.ironsspellbooks.entity.spells.creeper_head.CreeperHeadProjectile;
import io.redspace.ironsspellbooks.entity.spells.devour_jaw.DevourJaw;
import io.redspace.ironsspellbooks.entity.spells.dragon_breath.DragonBreathPool;
import io.redspace.ironsspellbooks.entity.spells.dragon_breath.DragonBreathProjectile;
import io.redspace.ironsspellbooks.entity.spells.eldritch_blast.EldritchBlastVisualEntity;
import io.redspace.ironsspellbooks.entity.spells.electrocute.ElectrocuteProjectile;
import io.redspace.ironsspellbooks.entity.spells.fire_breath.FireBreathProjectile;
import io.redspace.ironsspellbooks.entity.spells.fireball.MagicFireball;
import io.redspace.ironsspellbooks.entity.spells.fireball.SmallMagicFireball;
import io.redspace.ironsspellbooks.entity.spells.firebolt.FireboltProjectile;
import io.redspace.ironsspellbooks.entity.spells.firefly_swarm.FireflySwarmProjectile;
import io.redspace.ironsspellbooks.entity.spells.flame_strike.FlameStrike;
import io.redspace.ironsspellbooks.entity.spells.guiding_bolt.GuidingBoltProjectile;
import io.redspace.ironsspellbooks.entity.spells.gust.GustCollider;
import io.redspace.ironsspellbooks.entity.spells.ice_block.IceBlockProjectile;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleProjectile;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceProjectile;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowProjectile;
import io.redspace.ironsspellbooks.entity.spells.magic_missile.MagicMissileProjectile;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireBomb;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireField;
import io.redspace.ironsspellbooks.entity.spells.poison_arrow.PoisonArrow;
import io.redspace.ironsspellbooks.entity.spells.poison_breath.PoisonBreathProjectile;
import io.redspace.ironsspellbooks.entity.spells.poison_cloud.PoisonCloud;
import io.redspace.ironsspellbooks.entity.spells.poison_cloud.PoisonSplash;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalEntity;
import io.redspace.ironsspellbooks.entity.spells.ray_of_frost.RayOfFrostVisualEntity;
import io.redspace.ironsspellbooks.entity.spells.root.RootEntity;
import io.redspace.ironsspellbooks.entity.spells.shield.ShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.small_magic_arrow.SmallMagicArrow;
import io.redspace.ironsspellbooks.entity.spells.spectral_hammer.SpectralHammer;
import io.redspace.ironsspellbooks.entity.spells.sunbeam.Sunbeam;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.entity.spells.void_tentacle.VoidTentacle;
import io.redspace.ironsspellbooks.entity.spells.wall_of_fire.WallOfFireEntity;
import io.redspace.ironsspellbooks.entity.spells.wisp.WispEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {

    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "irons_spellbooks");

    public static final RegistryObject<EntityType<WispEntity>> WISP = ENTITIES.register("wisp", () -> EntityType.Builder.of(WispEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "wisp").toString()));

    public static final RegistryObject<EntityType<SpectralHammer>> SPECTRAL_HAMMER = ENTITIES.register("spectral_hammer", () -> EntityType.Builder.of(SpectralHammer::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "spectral_hammer").toString()));

    public static final RegistryObject<EntityType<MagicMissileProjectile>> MAGIC_MISSILE_PROJECTILE = ENTITIES.register("magic_missile", () -> EntityType.Builder.of(MagicMissileProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "magic_missile").toString()));

    public static final RegistryObject<EntityType<ConeOfColdProjectile>> CONE_OF_COLD_PROJECTILE = ENTITIES.register("cone_of_cold", () -> EntityType.Builder.of(ConeOfColdProjectile::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "cone_of_cold").toString()));

    public static final RegistryObject<EntityType<BloodSlashProjectile>> BLOOD_SLASH_PROJECTILE = ENTITIES.register("blood_slash", () -> EntityType.Builder.of(BloodSlashProjectile::new, MobCategory.MISC).sized(2.0F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "blood_slash").toString()));

    public static final RegistryObject<EntityType<ElectrocuteProjectile>> ELECTROCUTE_PROJECTILE = ENTITIES.register("electrocute", () -> EntityType.Builder.of(ElectrocuteProjectile::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "electrocute").toString()));

    public static final RegistryObject<EntityType<FireboltProjectile>> FIREBOLT_PROJECTILE = ENTITIES.register("firebolt", () -> EntityType.Builder.of(FireboltProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "firebolt").toString()));

    public static final RegistryObject<EntityType<IcicleProjectile>> ICICLE_PROJECTILE = ENTITIES.register("icicle", () -> EntityType.Builder.of(IcicleProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "icicle").toString()));

    public static final RegistryObject<EntityType<FireBreathProjectile>> FIRE_BREATH_PROJECTILE = ENTITIES.register("fire_breath", () -> EntityType.Builder.of(FireBreathProjectile::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "fire_breath").toString()));

    public static final RegistryObject<EntityType<DragonBreathProjectile>> DRAGON_BREATH_PROJECTILE = ENTITIES.register("dragon_breath", () -> EntityType.Builder.of(DragonBreathProjectile::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "dragon_breath").toString()));

    public static final RegistryObject<EntityType<DebugWizard>> DEBUG_WIZARD = ENTITIES.register("debug_wizard", () -> EntityType.Builder.of(DebugWizard::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "simple_wizard").toString()));

    public static final RegistryObject<EntityType<SummonedHorse>> SPECTRAL_STEED = ENTITIES.register("spectral_steed", () -> EntityType.Builder.of(SummonedHorse::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).clientTrackingRange(10).build(new ResourceLocation("irons_spellbooks", "spectral_steed").toString()));

    public static final RegistryObject<EntityType<ShieldEntity>> SHIELD_ENTITY = ENTITIES.register("shield", () -> EntityType.Builder.of(ShieldEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "shield").toString()));

    public static final RegistryObject<EntityType<WallOfFireEntity>> WALL_OF_FIRE_ENTITY = ENTITIES.register("wall_of_fire", () -> EntityType.Builder.of(WallOfFireEntity::new, MobCategory.MISC).sized(10.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "wall_of_fire").toString()));

    public static final RegistryObject<EntityType<SummonedVex>> SUMMONED_VEX = ENTITIES.register("summoned_vex", () -> EntityType.Builder.of(SummonedVex::new, MobCategory.CREATURE).sized(0.4F, 0.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "summoned_vex").toString()));

    public static final RegistryObject<EntityType<PyromancerEntity>> PYROMANCER = ENTITIES.register("pyromancer", () -> EntityType.Builder.of(PyromancerEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "pyromancer").toString()));

    public static final RegistryObject<EntityType<CryomancerEntity>> CRYOMANCER = ENTITIES.register("cryomancer", () -> EntityType.Builder.of(CryomancerEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "cryomancer").toString()));

    public static final RegistryObject<EntityType<LightningLanceProjectile>> LIGHTNING_LANCE_PROJECTILE = ENTITIES.register("lightning_lance", () -> EntityType.Builder.of(LightningLanceProjectile::new, MobCategory.MISC).sized(1.25F, 1.25F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "lightning_lance").toString()));

    public static final RegistryObject<EntityType<NecromancerEntity>> NECROMANCER = ENTITIES.register("necromancer", () -> EntityType.Builder.of(NecromancerEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "necromancer").toString()));

    public static final RegistryObject<EntityType<SummonedZombie>> SUMMONED_ZOMBIE = ENTITIES.register("summoned_zombie", () -> EntityType.Builder.of(SummonedZombie::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "summoned_zombie").toString()));

    public static final RegistryObject<EntityType<SummonedSkeleton>> SUMMONED_SKELETON = ENTITIES.register("summoned_skeleton", () -> EntityType.Builder.of(SummonedSkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "summoned_skeleton").toString()));

    public static final RegistryObject<EntityType<ExtendedWitherSkull>> WITHER_SKULL_PROJECTILE = ENTITIES.register("wither_skull", () -> EntityType.Builder.of(ExtendedWitherSkull::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "wither_skull").toString()));

    public static final RegistryObject<EntityType<MagicArrowProjectile>> MAGIC_ARROW_PROJECTILE = ENTITIES.register("magic_arrow", () -> EntityType.Builder.of(MagicArrowProjectile::new, MobCategory.MISC).sized(0.8F, 0.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "magic_arrow").toString()));

    public static final RegistryObject<EntityType<CreeperHeadProjectile>> CREEPER_HEAD_PROJECTILE = ENTITIES.register("creeper_head", () -> EntityType.Builder.of(CreeperHeadProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "creeper_head").toString()));

    public static final RegistryObject<EntityType<FrozenHumanoid>> FROZEN_HUMANOID = ENTITIES.register("frozen_humanoid", () -> EntityType.Builder.of(FrozenHumanoid::new, MobCategory.MISC).sized(0.6F, 1.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "frozen_humanoid").toString()));

    public static final RegistryObject<EntityType<SmallMagicFireball>> SMALL_FIREBALL_PROJECTILE = ENTITIES.register("small_fireball", () -> EntityType.Builder.of(SmallMagicFireball::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "small_fireball").toString()));

    public static final RegistryObject<EntityType<MagicFireball>> MAGIC_FIREBALL = ENTITIES.register("fireball", () -> EntityType.Builder.of(MagicFireball::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).build(new ResourceLocation("irons_spellbooks", "fireball").toString()));

    public static final RegistryObject<EntityType<SummonedPolarBear>> SUMMONED_POLAR_BEAR = ENTITIES.register("summoned_polar_bear", () -> EntityType.Builder.of(SummonedPolarBear::new, MobCategory.CREATURE).immuneTo(Blocks.POWDER_SNOW).sized(1.4F, 1.4F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "summoned_polar_bear").toString()));

    public static final RegistryObject<EntityType<DeadKingBoss>> DEAD_KING = ENTITIES.register("dead_king", () -> EntityType.Builder.of(DeadKingBoss::new, MobCategory.MONSTER).sized(0.9F, 3.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "dead_king").toString()));

    public static final RegistryObject<EntityType<DeadKingCorpseEntity>> DEAD_KING_CORPSE = ENTITIES.register("dead_king_corpse", () -> EntityType.Builder.of(DeadKingCorpseEntity::new, MobCategory.MISC).sized(1.5F, 0.95F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "dead_king_corpse").toString()));

    public static final RegistryObject<EntityType<CatacombsZombie>> CATACOMBS_ZOMBIE = ENTITIES.register("catacombs_zombie", () -> EntityType.Builder.of(CatacombsZombie::new, MobCategory.MONSTER).sized(1.5F, 0.95F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "catacombs_zombie").toString()));

    public static final RegistryObject<EntityType<ArchevokerEntity>> ARCHEVOKER = ENTITIES.register("archevoker", () -> EntityType.Builder.of(ArchevokerEntity::new, MobCategory.MONSTER).sized(0.6F, 2.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "archevoker").toString()));

    public static final RegistryObject<EntityType<MagehunterVindicator>> MAGEHUNTER_VINDICATOR = ENTITIES.register("magehunter_vindicator", () -> EntityType.Builder.of(MagehunterVindicator::new, MobCategory.MONSTER).sized(1.5F, 0.95F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "magehunter_vindicator").toString()));

    public static final RegistryObject<EntityType<KeeperEntity>> KEEPER = ENTITIES.register("citadel_keeper", () -> EntityType.Builder.of(KeeperEntity::new, MobCategory.MONSTER).sized(0.85F, 2.3F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "citadel_keeper").toString()));

    public static final RegistryObject<EntityType<VoidTentacle>> SCULK_TENTACLE = ENTITIES.register("sculk_tentacle", () -> EntityType.Builder.of(VoidTentacle::new, MobCategory.MISC).sized(2.5F, 5.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "sculk_tentacle").toString()));

    public static final RegistryObject<EntityType<IceBlockProjectile>> ICE_BLOCK_PROJECTILE = ENTITIES.register("ice_block_projectile", () -> EntityType.Builder.of(IceBlockProjectile::new, MobCategory.MISC).sized(1.25F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "ice_block_projectile").toString()));

    public static final RegistryObject<EntityType<PoisonCloud>> POISON_CLOUD = ENTITIES.register("poison_cloud", () -> EntityType.Builder.of(PoisonCloud::new, MobCategory.MISC).sized(4.0F, 0.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "poison_cloud").toString()));

    public static final RegistryObject<EntityType<Sunbeam>> SUNBEAM = ENTITIES.register("sunbeam", () -> EntityType.Builder.of(Sunbeam::new, MobCategory.MISC).sized(1.5F, 8.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "sunbeam").toString()));

    public static final RegistryObject<EntityType<DragonBreathPool>> DRAGON_BREATH_POOL = ENTITIES.register("dragon_breath_pool", () -> EntityType.Builder.of(DragonBreathPool::new, MobCategory.MISC).sized(4.0F, 0.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "dragon_breath_pool").toString()));

    public static final RegistryObject<EntityType<PoisonBreathProjectile>> POISON_BREATH_PROJECTILE = ENTITIES.register("poison_breath", () -> EntityType.Builder.of(PoisonBreathProjectile::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "poison_breath").toString()));

    public static final RegistryObject<EntityType<PoisonArrow>> POISON_ARROW = ENTITIES.register("poison_arrow", () -> EntityType.Builder.of(PoisonArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "poison_arrow").toString()));

    public static final RegistryObject<EntityType<SmallMagicArrow>> SMALL_MAGIC_ARROW = ENTITIES.register("small_magic_arrow", () -> EntityType.Builder.of(SmallMagicArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "small_magic_arrow").toString()));

    public static final RegistryObject<EntityType<PoisonSplash>> POISON_SPLASH = ENTITIES.register("poison_splash", () -> EntityType.Builder.of(PoisonSplash::new, MobCategory.MISC).sized(3.5F, 4.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "poison_splash").toString()));

    public static final RegistryObject<EntityType<AcidOrb>> ACID_ORB = ENTITIES.register("acid_orb", () -> EntityType.Builder.of(AcidOrb::new, MobCategory.MISC).sized(0.75F, 0.75F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "acid_orb").toString()));

    public static final RegistryObject<EntityType<RootEntity>> ROOT = ENTITIES.register("root", () -> EntityType.Builder.of(RootEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "root").toString()));

    public static final RegistryObject<EntityType<BlackHole>> BLACK_HOLE = ENTITIES.register("black_hole", () -> EntityType.Builder.of(BlackHole::new, MobCategory.MISC).sized(11.0F, 11.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "black_hole").toString()));

    public static final RegistryObject<EntityType<BloodNeedle>> BLOOD_NEEDLE = ENTITIES.register("blood_needle", () -> EntityType.Builder.of(BloodNeedle::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "blood_needle").toString()));

    public static final RegistryObject<EntityType<FireField>> FIRE_FIELD = ENTITIES.register("fire_field", () -> EntityType.Builder.of(FireField::new, MobCategory.MISC).sized(4.0F, 0.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "fire_field").toString()));

    public static final RegistryObject<EntityType<FireBomb>> FIRE_BOMB = ENTITIES.register("magma_ball", () -> EntityType.Builder.of(FireBomb::new, MobCategory.MISC).sized(0.75F, 0.75F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "magma_ball").toString()));

    public static final RegistryObject<EntityType<Comet>> COMET = ENTITIES.register("comet", () -> EntityType.Builder.of(Comet::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "comet").toString()));

    public static final RegistryObject<EntityType<TargetedAreaEntity>> TARGET_AREA_ENTITY = ENTITIES.register("target_area", () -> EntityType.Builder.of(TargetedAreaEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "target_area").toString()));

    public static final RegistryObject<EntityType<HealingAoe>> HEALING_AOE = ENTITIES.register("healing_aoe", () -> EntityType.Builder.of(HealingAoe::new, MobCategory.MISC).sized(4.0F, 0.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "healing_aoe").toString()));

    public static final RegistryObject<EntityType<EarthquakeAoe>> EARTHQUAKE_AOE = ENTITIES.register("earthquake_aoe", () -> EntityType.Builder.of(EarthquakeAoe::new, MobCategory.MISC).sized(4.0F, 0.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "earthquake_aoe").toString()));

    public static final RegistryObject<EntityType<PriestEntity>> PRIEST = ENTITIES.register("priest", () -> EntityType.Builder.of(PriestEntity::new, MobCategory.CREATURE).sized(0.6F, 2.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "priest").toString()));

    public static final RegistryObject<EntityType<VisualFallingBlockEntity>> FALLING_BLOCK = ENTITIES.register("visual_falling_block", () -> EntityType.Builder.of(VisualFallingBlockEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20).build(new ResourceLocation("irons_spellbooks", "visual_falling_block").toString()));

    public static final RegistryObject<EntityType<GuidingBoltProjectile>> GUIDING_BOLT = ENTITIES.register("guiding_bolt", () -> EntityType.Builder.of(GuidingBoltProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "guiding_bolt").toString()));

    public static final RegistryObject<EntityType<GustCollider>> GUST_COLLIDER = ENTITIES.register("gust", () -> EntityType.Builder.of(GustCollider::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "gust").toString()));

    public static final RegistryObject<EntityType<ChainLightning>> CHAIN_LIGHTNING = ENTITIES.register("chain_lightning", () -> EntityType.Builder.of(ChainLightning::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "chain_lightning").toString()));

    public static final RegistryObject<EntityType<RayOfFrostVisualEntity>> RAY_OF_FROST_VISUAL_ENTITY = ENTITIES.register("ray_of_frost", () -> EntityType.Builder.of(RayOfFrostVisualEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "ray_of_frost").toString()));

    public static final RegistryObject<EntityType<EldritchBlastVisualEntity>> ELDRITCH_BLAST_VISUAL_ENTITY = ENTITIES.register("eldritch_blast", () -> EntityType.Builder.of(EldritchBlastVisualEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "eldritch_blast").toString()));

    public static final RegistryObject<EntityType<DevourJaw>> DEVOUR_JAW = ENTITIES.register("devour_jaw", () -> EntityType.Builder.of(DevourJaw::new, MobCategory.MISC).sized(2.0F, 2.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "devour_jaw").toString()));

    public static final RegistryObject<EntityType<FireflySwarmProjectile>> FIREFLY_SWARM = ENTITIES.register("firefly_swarm", () -> EntityType.Builder.of(FireflySwarmProjectile::new, MobCategory.MISC).sized(0.9F, 0.9F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "firefly_swarm").toString()));

    public static final RegistryObject<EntityType<FlameStrike>> FLAME_STRIKE = ENTITIES.register("flame_strike", () -> EntityType.Builder.of(FlameStrike::new, MobCategory.MISC).sized(5.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "flame_strike").toString()));

    public static final RegistryObject<EntityType<ArrowVolleyEntity>> ARROW_VOLLEY_ENTITY = ENTITIES.register("arrow_volley", () -> EntityType.Builder.of(ArrowVolleyEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "arrow_volley").toString()));

    public static final RegistryObject<EntityType<PortalEntity>> PORTAL = ENTITIES.register("portal", () -> EntityType.Builder.of(PortalEntity::new, MobCategory.MISC).sized(0.8F, 2.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "portal").toString()));

    public static final RegistryObject<EntityType<StompAoe>> STOMP_AOE = ENTITIES.register("stomp_aoe", () -> EntityType.Builder.of(StompAoe::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "stomp_aoe").toString()));

    public static final RegistryObject<EntityType<LightningStrike>> LIGHTNING_STRIKE = ENTITIES.register("lightning_strike", () -> EntityType.Builder.of(LightningStrike::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "lightning_strike").toString()));

    public static final RegistryObject<EntityType<ApothecaristEntity>> APOTHECARIST = ENTITIES.register("apothecarist", () -> EntityType.Builder.of(ApothecaristEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "apothecarist").toString()));

    public static final RegistryObject<EntityType<EchoingStrikeEntity>> ECHOING_STRIKE = ENTITIES.register("echoing_strike", () -> EntityType.Builder.of(EchoingStrikeEntity::new, MobCategory.MISC).sized(2.0F, 2.0F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "echoing_strike").toString()));

    public static final RegistryObject<EntityType<CultistEntity>> CULTIST = ENTITIES.register("cultist", () -> EntityType.Builder.of(CultistEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "cultist").toString()));

    public static final RegistryObject<EntityType<BallLightning>> BALL_LIGHTNING = ENTITIES.register("ball_lightning", () -> EntityType.Builder.of(BallLightning::new, MobCategory.MISC).sized(1.1F, 1.1F).clientTrackingRange(64).build(new ResourceLocation("irons_spellbooks", "ball_lightning").toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}