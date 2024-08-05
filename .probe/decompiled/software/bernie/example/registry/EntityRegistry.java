package software.bernie.example.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.example.entity.BatEntity;
import software.bernie.example.entity.BikeEntity;
import software.bernie.example.entity.CoolKidEntity;
import software.bernie.example.entity.DynamicExampleEntity;
import software.bernie.example.entity.FakeGlassEntity;
import software.bernie.example.entity.ParasiteEntity;
import software.bernie.example.entity.RaceCarEntity;

public final class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "geckolib");

    public static final RegistryObject<EntityType<BatEntity>> BAT = registerMob("bat", BatEntity::new, 0.7F, 1.3F, 2039583, 855309);

    public static final RegistryObject<EntityType<BikeEntity>> BIKE = registerMob("bike", BikeEntity::new, 0.5F, 0.6F, 13886438, 15331829);

    public static final RegistryObject<EntityType<RaceCarEntity>> RACE_CAR = registerMob("race_car", RaceCarEntity::new, 1.5F, 1.5F, 10360342, 5855577);

    public static final RegistryObject<EntityType<ParasiteEntity>> PARASITE = registerMob("parasite", ParasiteEntity::new, 1.5F, 1.5F, 3154457, 11316396);

    public static final RegistryObject<EntityType<DynamicExampleEntity>> MUTANT_ZOMBIE = registerMob("mutant_zombie", DynamicExampleEntity::new, 0.5F, 1.9F, 3957302, 5740937);

    public static final RegistryObject<EntityType<FakeGlassEntity>> FAKE_GLASS = registerMob("fake_glass", FakeGlassEntity::new, 1.0F, 1.0F, 14483456, 14221303);

    public static final RegistryObject<EntityType<CoolKidEntity>> COOL_KID = registerMob("cool_kid", CoolKidEntity::new, 0.45F, 1.0F, 6236721, 7288382);

    public static final RegistryObject<EntityType<DynamicExampleEntity>> GREMLIN = registerMob("gremlin", DynamicExampleEntity::new, 0.5F, 1.9F, 5263440, 6316128);

    public static <T extends Mob> RegistryObject<EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> entity, float width, float height, int primaryEggColor, int secondaryEggColor) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entity, MobCategory.CREATURE).sized(width, height).build(name));
    }
}