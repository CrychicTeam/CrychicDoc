package noppes.npcs;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;
import noppes.npcs.entity.EntityChairMount;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPC64x32;
import noppes.npcs.entity.EntityNPCGolem;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityNpcAlex;
import noppes.npcs.entity.EntityNpcClassicPlayer;
import noppes.npcs.entity.EntityNpcCrystal;
import noppes.npcs.entity.EntityNpcDragon;
import noppes.npcs.entity.EntityNpcPony;
import noppes.npcs.entity.EntityNpcSlime;
import noppes.npcs.entity.EntityProjectile;

@EventBusSubscriber(bus = Bus.MOD, modid = "customnpcs")
public class CustomEntities {

    @ObjectHolder(registryName = "entity_type", value = "customnpcs:npcpony")
    public static EntityType<? extends EntityNPCInterface> entityNpcPony;

    @ObjectHolder(registryName = "entity_type", value = "customnpcs:npccrystal")
    public static EntityType<? extends EntityNPCInterface> entityNpcCrystal;

    @ObjectHolder(registryName = "entity_type", value = "customnpcs:npcslime")
    public static EntityType<? extends EntityNPCInterface> entityNpcSlime;

    @ObjectHolder(registryName = "entity_type", value = "customnpcs:npcdragon")
    public static EntityType<? extends EntityNPCInterface> entityNpcDragon;

    @ObjectHolder(registryName = "entity_type", value = "customnpcs:npcgolem")
    public static EntityType<? extends EntityNPCInterface> entityNPCGolem;

    @ObjectHolder(registryName = "entity_type", value = "customnpcs:customnpc")
    public static EntityType<? extends EntityNPCInterface> entityCustomNpc;

    @ObjectHolder(registryName = "entity_type", value = "customnpcs:customnpc64x32")
    public static EntityType<? extends EntityNPCInterface> entityNPC64x32;

    @ObjectHolder(registryName = "entity_type", value = "customnpcs:customnpcalex")
    public static EntityType<? extends EntityNPCInterface> entityNpcAlex;

    @ObjectHolder(registryName = "entity_type", value = "customnpcs:customnpcclassic")
    public static EntityType<? extends EntityNPCInterface> entityNpcClassicPlayer;

    @ObjectHolder(registryName = "entity_type", value = "customnpcs:customnpcchairmount")
    public static EntityType<?> entityChairMount;

    @ObjectHolder(registryName = "entity_type", value = "customnpcs:customnpcprojectile")
    public static EntityType<? extends ThrowableProjectile> entityProjectile;

    private static List<EntityType> types = new ArrayList();

    @SubscribeEvent
    public static void registerEntities(RegisterEvent event) {
        if (event.getRegistryKey() == ForgeRegistries.Keys.ENTITY_TYPES) {
            types.clear();
            registerNpc(event.getForgeRegistry(), EntityNpcPony.class, "npcpony", EntityNpcPony::new);
            registerNpc(event.getForgeRegistry(), EntityNpcCrystal.class, "npccrystal", EntityNpcCrystal::new);
            registerNpc(event.getForgeRegistry(), EntityNpcSlime.class, "npcslime", EntityNpcSlime::new);
            registerNpc(event.getForgeRegistry(), EntityNpcDragon.class, "npcdragon", EntityNpcDragon::new);
            registerNpc(event.getForgeRegistry(), EntityNPCGolem.class, "npcgolem", EntityNPCGolem::new);
            registerNpc(event.getForgeRegistry(), EntityCustomNpc.class, "customnpc", EntityCustomNpc::new);
            registerNpc(event.getForgeRegistry(), EntityNPC64x32.class, "customnpc64x32", EntityNPC64x32::new);
            registerNpc(event.getForgeRegistry(), EntityNpcAlex.class, "customnpcalex", EntityNpcAlex::new);
            registerNpc(event.getForgeRegistry(), EntityNpcClassicPlayer.class, "customnpcclassic", EntityNpcClassicPlayer::new);
            registerNewentity(event.getForgeRegistry(), EntityChairMount.class, "customnpcchairmount", EntityChairMount::new, 64, 10, false, 0.001F, 0.001F);
            registerNewentity(event.getForgeRegistry(), EntityProjectile.class, "customnpcprojectile", EntityProjectile::new, 64, 20, true, 0.5F, 0.5F);
        }
    }

    @SubscribeEvent
    public static void attribute(EntityAttributeCreationEvent event) {
        for (EntityType type : types) {
            event.put(type, EntityNPCInterface.createMobAttributes().build());
        }
    }

    private static <T extends Entity> void registerNpc(IForgeRegistry<EntityType<?>> registry, Class<? extends Entity> c, String name, EntityType.EntityFactory<T> factoryIn) {
        EntityType.Builder<?> builder = EntityType.Builder.of(factoryIn, MobCategory.CREATURE);
        builder.setTrackingRange(10);
        builder.setUpdateInterval(3);
        builder.setShouldReceiveVelocityUpdates(false);
        builder.clientTrackingRange(10);
        builder.sized(1.0F, 1.0F);
        ResourceLocation registryName = new ResourceLocation("customnpcs", name);
        EntityType type = builder.build(registryName.toString());
        types.add(type);
        registry.register(registryName, type);
        if (CustomNpcs.FixUpdateFromPre_1_12) {
            registryName = new ResourceLocation("customnpcs." + name);
            registry.register(registryName, builder.build(registryName.toString()));
        }
    }

    private static <T extends Entity> void registerNewentity(IForgeRegistry<EntityType<?>> registry, Class<? extends Entity> c, String name, EntityType.EntityFactory<T> factoryIn, int range, int update, boolean velocity, float width, float height) {
        EntityType.Builder<?> builder = EntityType.Builder.of(factoryIn, MobCategory.MISC);
        builder.setTrackingRange(range);
        builder.setUpdateInterval(update);
        builder.setShouldReceiveVelocityUpdates(velocity);
        builder.sized(width, height);
        builder.clientTrackingRange(4);
        ResourceLocation registryName = new ResourceLocation("customnpcs", name);
        registry.register(registryName, builder.build(registryName.toString()));
    }
}