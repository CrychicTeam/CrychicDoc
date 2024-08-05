package net.minecraft.world.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;

public class SpawnPlacements {

    private static final Map<EntityType<?>, SpawnPlacements.Data> DATA_BY_TYPE = Maps.newHashMap();

    private static <T extends Mob> void register(EntityType<T> entityTypeT0, SpawnPlacements.Type spawnPlacementsType1, Heightmap.Types heightmapTypes2, SpawnPlacements.SpawnPredicate<T> spawnPlacementsSpawnPredicateT3) {
        SpawnPlacements.Data $$4 = (SpawnPlacements.Data) DATA_BY_TYPE.put(entityTypeT0, new SpawnPlacements.Data(heightmapTypes2, spawnPlacementsType1, spawnPlacementsSpawnPredicateT3));
        if ($$4 != null) {
            throw new IllegalStateException("Duplicate registration for type " + BuiltInRegistries.ENTITY_TYPE.getKey(entityTypeT0));
        }
    }

    public static SpawnPlacements.Type getPlacementType(EntityType<?> entityType0) {
        SpawnPlacements.Data $$1 = (SpawnPlacements.Data) DATA_BY_TYPE.get(entityType0);
        return $$1 == null ? SpawnPlacements.Type.NO_RESTRICTIONS : $$1.placement;
    }

    public static Heightmap.Types getHeightmapType(@Nullable EntityType<?> entityType0) {
        SpawnPlacements.Data $$1 = (SpawnPlacements.Data) DATA_BY_TYPE.get(entityType0);
        return $$1 == null ? Heightmap.Types.MOTION_BLOCKING_NO_LEAVES : $$1.heightMap;
    }

    public static <T extends Entity> boolean checkSpawnRules(EntityType<T> entityTypeT0, ServerLevelAccessor serverLevelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        SpawnPlacements.Data $$5 = (SpawnPlacements.Data) DATA_BY_TYPE.get(entityTypeT0);
        return $$5 == null || $$5.predicate.test(entityTypeT0, serverLevelAccessor1, mobSpawnType2, blockPos3, randomSource4);
    }

    static {
        register(EntityType.AXOLOTL, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Axolotl::m_218437_);
        register(EntityType.COD, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::m_218282_);
        register(EntityType.DOLPHIN, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::m_218282_);
        register(EntityType.DROWNED, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Drowned::m_218955_);
        register(EntityType.GUARDIAN, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Guardian::m_218990_);
        register(EntityType.PUFFERFISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::m_218282_);
        register(EntityType.SALMON, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::m_218282_);
        register(EntityType.SQUID, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::m_218282_);
        register(EntityType.TROPICAL_FISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicalFish::m_218266_);
        register(EntityType.BAT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Bat::m_218098_);
        register(EntityType.BLAZE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219019_);
        register(EntityType.CAVE_SPIDER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.CHICKEN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.COW, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.CREEPER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.DONKEY, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.ENDERMAN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.ENDERMITE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Endermite::m_218968_);
        register(EntityType.ENDER_DRAGON, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::m_217057_);
        register(EntityType.FROG, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Frog::m_218511_);
        register(EntityType.GHAST, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Ghast::m_218984_);
        register(EntityType.GIANT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.GLOW_SQUID, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GlowSquid::m_217017_);
        register(EntityType.GOAT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Goat::m_218752_);
        register(EntityType.HORSE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.HUSK, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Husk::m_218996_);
        register(EntityType.IRON_GOLEM, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::m_217057_);
        register(EntityType.LLAMA, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.MAGMA_CUBE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MagmaCube::m_219002_);
        register(EntityType.MOOSHROOM, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MushroomCow::m_218200_);
        register(EntityType.MULE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.OCELOT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Ocelot::m_218206_);
        register(EntityType.PARROT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Parrot::m_218241_);
        register(EntityType.PIG, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.HOGLIN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hoglin::m_219181_);
        register(EntityType.PIGLIN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Piglin::m_219197_);
        register(EntityType.PILLAGER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PatrollingMonster::m_219025_);
        register(EntityType.POLAR_BEAR, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PolarBear::m_218249_);
        register(EntityType.RABBIT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Rabbit::m_218255_);
        register(EntityType.SHEEP, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.SILVERFISH, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Silverfish::m_219076_);
        register(EntityType.SKELETON, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.SKELETON_HORSE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.SLIME, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Slime::m_219112_);
        register(EntityType.SNOW_GOLEM, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::m_217057_);
        register(EntityType.SPIDER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.STRAY, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Stray::m_219120_);
        register(EntityType.STRIDER, SpawnPlacements.Type.IN_LAVA, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Strider::m_219128_);
        register(EntityType.TURTLE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Turtle::m_218276_);
        register(EntityType.VILLAGER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::m_217057_);
        register(EntityType.WITCH, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.WITHER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.WITHER_SKELETON, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.WOLF, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Wolf::m_218291_);
        register(EntityType.ZOMBIE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.ZOMBIE_HORSE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.ZOMBIFIED_PIGLIN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ZombifiedPiglin::m_219173_);
        register(EntityType.ZOMBIE_VILLAGER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.CAT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.ELDER_GUARDIAN, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Guardian::m_218990_);
        register(EntityType.EVOKER, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.FOX, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Fox::m_218175_);
        register(EntityType.ILLUSIONER, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.PANDA, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.PHANTOM, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::m_217057_);
        register(EntityType.RAVAGER, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.SHULKER, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::m_217057_);
        register(EntityType.TRADER_LLAMA, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::m_218104_);
        register(EntityType.VEX, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.VINDICATOR, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::m_219013_);
        register(EntityType.WANDERING_TRADER, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::m_217057_);
        register(EntityType.WARDEN, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::m_217057_);
    }

    static class Data {

        final Heightmap.Types heightMap;

        final SpawnPlacements.Type placement;

        final SpawnPlacements.SpawnPredicate<?> predicate;

        public Data(Heightmap.Types heightmapTypes0, SpawnPlacements.Type spawnPlacementsType1, SpawnPlacements.SpawnPredicate<?> spawnPlacementsSpawnPredicate2) {
            this.heightMap = heightmapTypes0;
            this.placement = spawnPlacementsType1;
            this.predicate = spawnPlacementsSpawnPredicate2;
        }
    }

    @FunctionalInterface
    public interface SpawnPredicate<T extends Entity> {

        boolean test(EntityType<T> var1, ServerLevelAccessor var2, MobSpawnType var3, BlockPos var4, RandomSource var5);
    }

    public static enum Type {

        ON_GROUND, IN_WATER, NO_RESTRICTIONS, IN_LAVA
    }
}