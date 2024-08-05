package com.github.alexmodguy.alexscaves.server.entity;

import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.AlexsCavesBoatEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.AlexsCavesChestBoatEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.BeholderEyeEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.BurrowingArrowEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.CinderBrickEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.CrushedBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.DarkArrowEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.DepthChargeEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.DesolateDaggerEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.DinosaurSpiritEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.ExtinctionSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.FallingGuanoEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.FallingTreeBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.FloaterEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.GuanoEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.InkBombEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.LimestoneSpearEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.MagneticWeaponEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.MineGuardianAnchorEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.MovingMetalBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.QuarrySmasherEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.SeekingArrowEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.TephraEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.ThrownWasteDrumEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.WaterBoltEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.WaveEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidWinchEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.BrainiacEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneKnightEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneMageEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.FerrouslimeEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.ForsakenEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GammaroachEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GossamerWormEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.GrottoceratopsEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.HullbreakerEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.LanternfishEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.LuxtructosaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MagnetronEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.RadgillEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.RaycatEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.RelicheirusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SeaPigEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TeletorEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TrilocarisEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TripodfishEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.WatcherEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "alexscaves", bus = Bus.MOD)
public class ACEntityRegistry {

    public static final DeferredRegister<EntityType<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "alexscaves");

    public static final MobCategory CAVE_CREATURE = MobCategory.create("cave_creature", "alexscaves:cave_creature", 10, true, true, 128);

    public static final MobCategory DEEP_SEA_CREATURE = MobCategory.create("deep_sea_creature", "alexscaves:deep_sea_creature", 20, true, false, 128);

    public static final RegistryObject<EntityType<AlexsCavesBoatEntity>> BOAT = DEF_REG.register("boat", () -> EntityType.Builder.of(AlexsCavesBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).setCustomClientFactory(AlexsCavesBoatEntity::new).clientTrackingRange(10).build("ac_boat"));

    public static final RegistryObject<EntityType<AlexsCavesChestBoatEntity>> CHEST_BOAT = DEF_REG.register("chest_boat", () -> EntityType.Builder.of(AlexsCavesChestBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).setCustomClientFactory(AlexsCavesBoatEntity::new).clientTrackingRange(10).build("ac_chest_boat"));

    public static final RegistryObject<EntityType<MovingMetalBlockEntity>> MOVING_METAL_BLOCK = DEF_REG.register("moving_metal_block", () -> EntityType.Builder.of(MovingMetalBlockEntity::new, MobCategory.MISC).sized(0.99F, 0.99F).setCustomClientFactory(MovingMetalBlockEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).updateInterval(10).clientTrackingRange(20).build("moving_metal_block"));

    public static final RegistryObject<EntityType<TeletorEntity>> TELETOR = DEF_REG.register("teletor", () -> EntityType.Builder.of(TeletorEntity::new, MobCategory.MONSTER).sized(0.99F, 1.99F).build("teletor"));

    public static final RegistryObject<EntityType<MagneticWeaponEntity>> MAGNETIC_WEAPON = DEF_REG.register("magnetic_weapon", () -> EntityType.Builder.of(MagneticWeaponEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(MagneticWeaponEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).clientTrackingRange(20).build("magnetic_weapon"));

    public static final RegistryObject<EntityType<MagnetronEntity>> MAGNETRON = DEF_REG.register("magnetron", () -> EntityType.Builder.of(MagnetronEntity::new, MobCategory.MONSTER).sized(0.8F, 2.0F).build("magnetron"));

    public static final RegistryObject<EntityType<BoundroidEntity>> BOUNDROID = DEF_REG.register("boundroid", () -> EntityType.Builder.of(BoundroidEntity::new, MobCategory.MONSTER).sized(1.4F, 0.75F).build("boundroid"));

    public static final RegistryObject<EntityType<BoundroidWinchEntity>> BOUNDROID_WINCH = DEF_REG.register("boundroid_winch", () -> EntityType.Builder.of(BoundroidWinchEntity::new, MobCategory.MONSTER).sized(0.8F, 1.4F).build("boundroid_winch"));

    public static final RegistryObject<EntityType<FerrouslimeEntity>> FERROUSLIME = DEF_REG.register("ferrouslime", () -> EntityType.Builder.of(FerrouslimeEntity::new, MobCategory.MONSTER).sized(0.85F, 0.85F).build("ferrouslime"));

    public static final RegistryObject<EntityType<NotorEntity>> NOTOR = DEF_REG.register("notor", () -> EntityType.Builder.of(NotorEntity::new, MobCategory.AMBIENT).sized(0.5F, 0.65F).build("notor"));

    public static final RegistryObject<EntityType<QuarrySmasherEntity>> QUARRY_SMASHER = DEF_REG.register("quarry_smasher", () -> EntityType.Builder.of(QuarrySmasherEntity::new, MobCategory.MISC).sized(0.9F, 1.2F).setCustomClientFactory(QuarrySmasherEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).updateInterval(10).clientTrackingRange(20).build("quarry_smasher"));

    public static final RegistryObject<EntityType<SeekingArrowEntity>> SEEKING_ARROW = DEF_REG.register("seeking_arrow", () -> EntityType.Builder.of(SeekingArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(SeekingArrowEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build("seeking_arrow"));

    public static final RegistryObject<EntityType<SubterranodonEntity>> SUBTERRANODON = DEF_REG.register("subterranodon", () -> EntityType.Builder.of(SubterranodonEntity::new, CAVE_CREATURE).sized(1.75F, 1.2F).setTrackingRange(12).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("subterranodon"));

    public static final RegistryObject<EntityType<VallumraptorEntity>> VALLUMRAPTOR = DEF_REG.register("vallumraptor", () -> EntityType.Builder.of(VallumraptorEntity::new, CAVE_CREATURE).sized(0.8F, 1.5F).setTrackingRange(8).build("vallumraptor"));

    public static final RegistryObject<EntityType<GrottoceratopsEntity>> GROTTOCERATOPS = DEF_REG.register("grottoceratops", () -> EntityType.Builder.of(GrottoceratopsEntity::new, CAVE_CREATURE).sized(2.3F, 2.5F).setTrackingRange(8).build("grottoceratops"));

    public static final RegistryObject<EntityType<TrilocarisEntity>> TRILOCARIS = DEF_REG.register("trilocaris", () -> EntityType.Builder.of(TrilocarisEntity::new, MobCategory.WATER_AMBIENT).sized(0.9F, 0.4F).build("trilocaris"));

    public static final RegistryObject<EntityType<TremorsaurusEntity>> TREMORSAURUS = DEF_REG.register("tremorsaurus", () -> EntityType.Builder.of(TremorsaurusEntity::new, CAVE_CREATURE).sized(2.5F, 3.85F).setTrackingRange(8).build("tremorsaurus"));

    public static final RegistryObject<EntityType<RelicheirusEntity>> RELICHEIRUS = DEF_REG.register("relicheirus", () -> EntityType.Builder.of(RelicheirusEntity::new, CAVE_CREATURE).sized(2.65F, 5.9F).setTrackingRange(9).build("relicheirus"));

    public static final RegistryObject<EntityType<LuxtructosaurusEntity>> LUXTRUCTOSAURUS = DEF_REG.register("luxtructosaurus", () -> EntityType.Builder.of(LuxtructosaurusEntity::new, MobCategory.MONSTER).sized(6.0F, 8.5F).setTrackingRange(12).fireImmune().build("luxtructosaurus"));

    public static final RegistryObject<EntityType<TephraEntity>> TEPHRA = DEF_REG.register("tephra", () -> EntityType.Builder.of(TephraEntity::new, MobCategory.MISC).sized(0.6F, 0.6F).setCustomClientFactory(TephraEntity::new).fireImmune().setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("tephra"));

    public static final RegistryObject<EntityType<AtlatitanEntity>> ATLATITAN = DEF_REG.register("atlatitan", () -> EntityType.Builder.of(AtlatitanEntity::new, CAVE_CREATURE).sized(5.0F, 8.0F).setTrackingRange(11).build("atlatitan"));

    public static final RegistryObject<EntityType<FallingTreeBlockEntity>> FALLING_TREE_BLOCK = DEF_REG.register("falling_tree_block", () -> EntityType.Builder.of(FallingTreeBlockEntity::new, MobCategory.MISC).sized(0.99F, 0.99F).setCustomClientFactory(FallingTreeBlockEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).updateInterval(10).clientTrackingRange(20).build("falling_tree_block"));

    public static final RegistryObject<EntityType<CrushedBlockEntity>> CRUSHED_BLOCK = DEF_REG.register("crushed_block", () -> EntityType.Builder.of(CrushedBlockEntity::new, MobCategory.MISC).sized(0.99F, 0.99F).setCustomClientFactory(CrushedBlockEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).updateInterval(10).clientTrackingRange(20).build("crushed_block"));

    public static final RegistryObject<EntityType<LimestoneSpearEntity>> LIMESTONE_SPEAR = DEF_REG.register("limestone_spear", () -> EntityType.Builder.of(LimestoneSpearEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(LimestoneSpearEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build("limestone_spear"));

    public static final RegistryObject<EntityType<ExtinctionSpearEntity>> EXTINCTION_SPEAR = DEF_REG.register("extinction_spear", () -> EntityType.Builder.of(ExtinctionSpearEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(ExtinctionSpearEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).fireImmune().build("extinction_spear"));

    public static final RegistryObject<EntityType<DinosaurSpiritEntity>> DINOSAUR_SPIRIT = DEF_REG.register("dinosaur_spirit", () -> EntityType.Builder.of(DinosaurSpiritEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).setCustomClientFactory(DinosaurSpiritEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).fireImmune().build("dinosaur_spirit"));

    public static final RegistryObject<EntityType<NuclearExplosionEntity>> NUCLEAR_EXPLOSION = DEF_REG.register("nuclear_explosion", () -> EntityType.Builder.of(NuclearExplosionEntity::new, MobCategory.MISC).sized(0.99F, 0.99F).setCustomClientFactory(NuclearExplosionEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).updateInterval(10).clientTrackingRange(20).build("nuclear_explosion"));

    public static final RegistryObject<EntityType<NuclearBombEntity>> NUCLEAR_BOMB = DEF_REG.register("nuclear_bomb", () -> EntityType.Builder.of(NuclearBombEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).setCustomClientFactory(NuclearBombEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).updateInterval(10).clientTrackingRange(20).build("nuclear_bomb"));

    public static final RegistryObject<EntityType<NucleeperEntity>> NUCLEEPER = DEF_REG.register("nucleeper", () -> EntityType.Builder.of(NucleeperEntity::new, MobCategory.MONSTER).sized(0.98F, 3.95F).build("nucleeper"));

    public static final RegistryObject<EntityType<RadgillEntity>> RADGILL = DEF_REG.register("radgill", () -> EntityType.Builder.of(RadgillEntity::new, MobCategory.WATER_AMBIENT).sized(0.9F, 0.6F).build("radgill"));

    public static final RegistryObject<EntityType<BrainiacEntity>> BRAINIAC = DEF_REG.register("brainiac", () -> EntityType.Builder.of(BrainiacEntity::new, MobCategory.MONSTER).sized(1.3F, 2.5F).build("brainiac"));

    public static final RegistryObject<EntityType<ThrownWasteDrumEntity>> THROWN_WASTE_DRUM = DEF_REG.register("thrown_waste_drum", () -> EntityType.Builder.of(ThrownWasteDrumEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).setCustomClientFactory(ThrownWasteDrumEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).updateInterval(10).clientTrackingRange(20).build("thrown_waste_drum"));

    public static final RegistryObject<EntityType<GammaroachEntity>> GAMMAROACH = DEF_REG.register("gammaroach", () -> EntityType.Builder.of(GammaroachEntity::new, MobCategory.AMBIENT).sized(1.25F, 0.9F).build("gammaroach"));

    public static final RegistryObject<EntityType<RaycatEntity>> RAYCAT = DEF_REG.register("raycat", () -> EntityType.Builder.of(RaycatEntity::new, CAVE_CREATURE).sized(0.85F, 0.6F).build("raycat"));

    public static final RegistryObject<EntityType<CinderBrickEntity>> CINDER_BRICK = DEF_REG.register("cinder_brick", () -> EntityType.Builder.of(CinderBrickEntity::new, MobCategory.MISC).sized(0.4F, 0.4F).setCustomClientFactory(CinderBrickEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build("cinder_brick"));

    public static final RegistryObject<EntityType<TremorzillaEntity>> TREMORZILLA = DEF_REG.register("tremorzilla", () -> EntityType.Builder.of(TremorzillaEntity::new, CAVE_CREATURE).sized(4.5F, 11.0F).setTrackingRange(11).fireImmune().build("tremorzilla"));

    public static final RegistryObject<EntityType<LanternfishEntity>> LANTERNFISH = DEF_REG.register("lanternfish", () -> EntityType.Builder.of(LanternfishEntity::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.4F).build("lanternfish"));

    public static final RegistryObject<EntityType<SeaPigEntity>> SEA_PIG = DEF_REG.register("sea_pig", () -> EntityType.Builder.of(SeaPigEntity::new, DEEP_SEA_CREATURE).sized(0.5F, 0.65F).build("sea_pig"));

    public static final RegistryObject<EntityType<SubmarineEntity>> SUBMARINE = DEF_REG.register("submarine", () -> EntityType.Builder.of(SubmarineEntity::new, MobCategory.MISC).sized(3.5F, 3.3F).setCustomClientFactory(SubmarineEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).updateInterval(10).clientTrackingRange(20).build("submarine"));

    public static final RegistryObject<EntityType<HullbreakerEntity>> HULLBREAKER = DEF_REG.register("hullbreaker", () -> EntityType.Builder.of(HullbreakerEntity::new, MobCategory.UNDERGROUND_WATER_CREATURE).sized(4.65F, 4.5F).setShouldReceiveVelocityUpdates(true).clientTrackingRange(20).build("hullbreaker"));

    public static final RegistryObject<EntityType<GossamerWormEntity>> GOSSAMER_WORM = DEF_REG.register("gossamer_worm", () -> EntityType.Builder.of(GossamerWormEntity::new, DEEP_SEA_CREATURE).sized(1.15F, 0.5F).build("gossamer_worm"));

    public static final RegistryObject<EntityType<TripodfishEntity>> TRIPODFISH = DEF_REG.register("tripodfish", () -> EntityType.Builder.of(TripodfishEntity::new, DEEP_SEA_CREATURE).sized(0.95F, 0.5F).build("tripodfish"));

    public static final RegistryObject<EntityType<DeepOneEntity>> DEEP_ONE = DEF_REG.register("deep_one", () -> EntityType.Builder.of(DeepOneEntity::new, MobCategory.MONSTER).sized(0.9F, 2.2F).setTrackingRange(12).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("deep_one"));

    public static final RegistryObject<EntityType<InkBombEntity>> INK_BOMB = DEF_REG.register("ink_bomb", () -> EntityType.Builder.of(InkBombEntity::new, MobCategory.MISC).sized(0.6F, 0.6F).setCustomClientFactory(InkBombEntity::new).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("ink_bomb"));

    public static final RegistryObject<EntityType<DeepOneKnightEntity>> DEEP_ONE_KNIGHT = DEF_REG.register("deep_one_knight", () -> EntityType.Builder.of(DeepOneKnightEntity::new, MobCategory.MONSTER).sized(1.15F, 2.5F).setTrackingRange(12).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("deep_one_knight"));

    public static final RegistryObject<EntityType<DeepOneMageEntity>> DEEP_ONE_MAGE = DEF_REG.register("deep_one_mage", () -> EntityType.Builder.of(DeepOneMageEntity::new, MobCategory.MONSTER).sized(1.35F, 2.5F).setTrackingRange(12).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("deep_one_mage"));

    public static final RegistryObject<EntityType<WaterBoltEntity>> WATER_BOLT = DEF_REG.register("water_bolt", () -> EntityType.Builder.of(WaterBoltEntity::new, MobCategory.MISC).sized(0.6F, 0.6F).setCustomClientFactory(WaterBoltEntity::new).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("water_bolt"));

    public static final RegistryObject<EntityType<WaveEntity>> WAVE = DEF_REG.register("wave", () -> EntityType.Builder.of(WaveEntity::new, MobCategory.MISC).sized(0.9F, 0.9F).setCustomClientFactory(WaveEntity::new).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("wave"));

    public static final RegistryObject<EntityType<MineGuardianEntity>> MINE_GUARDIAN = DEF_REG.register("mine_guardian", () -> EntityType.Builder.of(MineGuardianEntity::new, MobCategory.MONSTER).sized(1.3F, 1.3F).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("mine_guardian"));

    public static final RegistryObject<EntityType<MineGuardianAnchorEntity>> MINE_GUARDIAN_ANCHOR = DEF_REG.register("mine_guardian_anchor", () -> EntityType.Builder.of(MineGuardianAnchorEntity::new, MobCategory.MISC).sized(0.6F, 1.35F).setCustomClientFactory(MineGuardianAnchorEntity::new).build("mine_guardian_anchor"));

    public static final RegistryObject<EntityType<DepthChargeEntity>> DEPTH_CHARGE = DEF_REG.register("depth_charge", () -> EntityType.Builder.of(DepthChargeEntity::new, MobCategory.MISC).sized(0.7F, 0.7F).setCustomClientFactory(DepthChargeEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build("depth_charge"));

    public static final RegistryObject<EntityType<FloaterEntity>> FLOATER = DEF_REG.register("floater", () -> EntityType.Builder.of(FloaterEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).setCustomClientFactory(FloaterEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).updateInterval(10).clientTrackingRange(20).build("nuclear_bomb"));

    public static final RegistryObject<EntityType<GuanoEntity>> GUANO = DEF_REG.register("guano", () -> EntityType.Builder.of(GuanoEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(GuanoEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build("guano"));

    public static final RegistryObject<EntityType<FallingGuanoEntity>> FALLING_GUANO = DEF_REG.register("falling_guano", () -> EntityType.Builder.of(FallingGuanoEntity::new, MobCategory.MISC).sized(0.8F, 0.9F).setCustomClientFactory(FallingGuanoEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).updateInterval(10).clientTrackingRange(20).build("falling_guano"));

    public static final RegistryObject<EntityType<GloomothEntity>> GLOOMOTH = DEF_REG.register("gloomoth", () -> EntityType.Builder.of(GloomothEntity::new, MobCategory.AMBIENT).sized(0.99F, 0.99F).setTrackingRange(12).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("gloomoth"));

    public static final RegistryObject<EntityType<UnderzealotEntity>> UNDERZEALOT = DEF_REG.register("underzealot", () -> EntityType.Builder.of(UnderzealotEntity::new, MobCategory.MONSTER).sized(0.8F, 1.2F).build("underzealot"));

    public static final RegistryObject<EntityType<WatcherEntity>> WATCHER = DEF_REG.register("watcher", () -> EntityType.Builder.of(WatcherEntity::new, MobCategory.MONSTER).sized(0.9F, 1.9F).build("watcher"));

    public static final RegistryObject<EntityType<CorrodentEntity>> CORRODENT = DEF_REG.register("corrodent", () -> EntityType.Builder.of(CorrodentEntity::new, MobCategory.MONSTER).sized(0.9F, 0.9F).build("corrodent"));

    public static final RegistryObject<EntityType<VesperEntity>> VESPER = DEF_REG.register("vesper", () -> EntityType.Builder.of(VesperEntity::new, MobCategory.MONSTER).sized(1.2F, 1.65F).setTrackingRange(12).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("vesper"));

    public static final RegistryObject<EntityType<ForsakenEntity>> FORSAKEN = DEF_REG.register("forsaken", () -> EntityType.Builder.of(ForsakenEntity::new, MobCategory.MONSTER).sized(3.0F, 3.5F).setTrackingRange(12).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("forsaken"));

    public static final RegistryObject<EntityType<BeholderEyeEntity>> BEHOLDER_EYE = DEF_REG.register("beholder_eye", () -> EntityType.Builder.of(BeholderEyeEntity::new, MobCategory.MISC).sized(0.3F, 0.3F).setCustomClientFactory(BeholderEyeEntity::new).build("beholder_eye"));

    public static final RegistryObject<EntityType<DesolateDaggerEntity>> DESOLATE_DAGGER = DEF_REG.register("desolate_dagger", () -> EntityType.Builder.of(DesolateDaggerEntity::new, MobCategory.MISC).sized(0.6F, 0.6F).setCustomClientFactory(DesolateDaggerEntity::new).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build("desolate_dagger"));

    public static final RegistryObject<EntityType<BurrowingArrowEntity>> BURROWING_ARROW = DEF_REG.register("burrowing_arrow", () -> EntityType.Builder.of(BurrowingArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(BurrowingArrowEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build("burrowing_arrow"));

    public static final RegistryObject<EntityType<DarkArrowEntity>> DARK_ARROW = DEF_REG.register("dark_arrow", () -> EntityType.Builder.of(DarkArrowEntity::new, MobCategory.MISC).sized(1.1F, 0.5F).setCustomClientFactory(DarkArrowEntity::new).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build("dark_arrow"));

    @SubscribeEvent
    public static void initializeAttributes(EntityAttributeCreationEvent event) {
        event.put(TELETOR.get(), TeletorEntity.createAttributes().build());
        event.put(MAGNETRON.get(), MagnetronEntity.createAttributes().build());
        event.put(BOUNDROID.get(), BoundroidEntity.createAttributes().build());
        event.put(BOUNDROID_WINCH.get(), BoundroidEntity.createAttributes().build());
        event.put(FERROUSLIME.get(), FerrouslimeEntity.createAttributes().build());
        event.put(NOTOR.get(), NotorEntity.createAttributes().build());
        event.put(SUBTERRANODON.get(), SubterranodonEntity.createAttributes().build());
        event.put(VALLUMRAPTOR.get(), VallumraptorEntity.createAttributes().build());
        event.put(GROTTOCERATOPS.get(), GrottoceratopsEntity.createAttributes().build());
        event.put(TRILOCARIS.get(), TrilocarisEntity.createAttributes().build());
        event.put(TREMORSAURUS.get(), TremorsaurusEntity.createAttributes().build());
        event.put(RELICHEIRUS.get(), RelicheirusEntity.createAttributes().build());
        event.put(LUXTRUCTOSAURUS.get(), LuxtructosaurusEntity.createAttributes().build());
        event.put(ATLATITAN.get(), AtlatitanEntity.createAttributes().build());
        event.put(NUCLEEPER.get(), NucleeperEntity.createAttributes().build());
        event.put(RADGILL.get(), RadgillEntity.createAttributes().build());
        event.put(BRAINIAC.get(), BrainiacEntity.createAttributes().build());
        event.put(GAMMAROACH.get(), GammaroachEntity.createAttributes().build());
        event.put(RAYCAT.get(), RaycatEntity.createAttributes().build());
        event.put(TREMORZILLA.get(), TremorzillaEntity.createAttributes().build());
        event.put(LANTERNFISH.get(), LanternfishEntity.createAttributes().build());
        event.put(SEA_PIG.get(), SeaPigEntity.createAttributes().build());
        event.put(HULLBREAKER.get(), HullbreakerEntity.createAttributes().build());
        event.put(GOSSAMER_WORM.get(), GossamerWormEntity.createAttributes().build());
        event.put(TRIPODFISH.get(), TripodfishEntity.createAttributes().build());
        event.put(DEEP_ONE.get(), DeepOneEntity.createAttributes().build());
        event.put(DEEP_ONE_KNIGHT.get(), DeepOneKnightEntity.createAttributes().build());
        event.put(DEEP_ONE_MAGE.get(), DeepOneMageEntity.createAttributes().build());
        event.put(MINE_GUARDIAN.get(), MineGuardianEntity.createAttributes().build());
        event.put(GLOOMOTH.get(), GloomothEntity.createAttributes().build());
        event.put(UNDERZEALOT.get(), UnderzealotEntity.createAttributes().build());
        event.put(WATCHER.get(), WatcherEntity.createAttributes().build());
        event.put(CORRODENT.get(), CorrodentEntity.createAttributes().build());
        event.put(VESPER.get(), VesperEntity.createAttributes().build());
        event.put(FORSAKEN.get(), ForsakenEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void spawnPlacements(SpawnPlacementRegisterEvent event) {
        SpawnPlacements.Type inAcid = SpawnPlacements.Type.create("in_acid", (levelReader, blockPos, entityType) -> !levelReader.m_6425_(blockPos).isEmpty() && levelReader.m_6425_(blockPos).getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get());
        event.register(TELETOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(MAGNETRON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(BOUNDROID.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(FERROUSLIME.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(NOTOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NotorEntity::checkNotorSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(SUBTERRANODON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SubterranodonEntity::checkSubterranodonSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(VALLUMRAPTOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DinosaurEntity::checkPrehistoricSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(GROTTOCERATOPS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DinosaurEntity::checkPrehistoricSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(TRILOCARIS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TrilocarisEntity::checkTrilocarisSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(TREMORSAURUS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DinosaurEntity::checkPrehistoricSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(RELICHEIRUS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DinosaurEntity::checkPrehistoricSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(LUXTRUCTOSAURUS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DinosaurEntity::checkPrehistoricSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(ATLATITAN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DinosaurEntity::checkPrehistoricPostBossSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(NUCLEEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(RADGILL.get(), inAcid, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RadgillEntity::checkRadgillSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(BRAINIAC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(GAMMAROACH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GammaroachEntity::checkGammaroachSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(RAYCAT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RaycatEntity::checkRaycatSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(LANTERNFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LanternfishEntity::checkLanternfishSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(SEA_PIG.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SeaPigEntity::checkSeaPigSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(HULLBREAKER.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HullbreakerEntity::checkHullbreakerSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(GOSSAMER_WORM.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GossamerWormEntity::checkGossamerWormSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(TRIPODFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TripodfishEntity::checkTripodfishSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(DEEP_ONE.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DeepOneBaseEntity::checkDeepOneSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(DEEP_ONE_KNIGHT.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DeepOneBaseEntity::checkDeepOneSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(DEEP_ONE_MAGE.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DeepOneBaseEntity::checkDeepOneSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(MINE_GUARDIAN.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MineGuardianEntity::checkMineGuardianSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(GLOOMOTH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GloomothEntity::checkGloomothSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(UNDERZEALOT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UnderzealotEntity::checkUnderzealotSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(WATCHER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WatcherEntity::checkWatcherSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(CORRODENT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CorrodentEntity::checkCorrodentSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(VESPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, VesperEntity::checkVesperSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(FORSAKEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ForsakenEntity::checkForsakenSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
    }
}