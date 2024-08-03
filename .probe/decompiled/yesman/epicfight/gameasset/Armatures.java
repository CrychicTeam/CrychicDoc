package yesman.epicfight.gameasset;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.forgeevent.ModelBuildEvent;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.model.JsonModelLoader;
import yesman.epicfight.model.armature.CreeperArmature;
import yesman.epicfight.model.armature.DragonArmature;
import yesman.epicfight.model.armature.EndermanArmature;
import yesman.epicfight.model.armature.HoglinArmature;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.model.armature.IronGolemArmature;
import yesman.epicfight.model.armature.PiglinArmature;
import yesman.epicfight.model.armature.RavagerArmature;
import yesman.epicfight.model.armature.SpiderArmature;
import yesman.epicfight.model.armature.VexArmature;
import yesman.epicfight.model.armature.WitherArmature;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.entity.EpicFightEntities;

public class Armatures implements PreparableReloadListener {

    public static final Armatures INSTANCE = new Armatures();

    private static final Map<ResourceLocation, Armature> ARMATURES = Maps.newHashMap();

    private static final Map<EntityType<?>, Function<EntityPatch<?>, Armature>> ENTITY_TYPE_ARMATURE = Maps.newHashMap();

    public static HumanoidArmature BIPED;

    public static CreeperArmature CREEPER;

    public static EndermanArmature ENDERMAN;

    public static HumanoidArmature SKELETON;

    public static SpiderArmature SPIDER;

    public static IronGolemArmature IRON_GOLEM;

    public static RavagerArmature RAVAGER;

    public static VexArmature VEX;

    public static PiglinArmature PIGLIN;

    public static HoglinArmature HOGLIN;

    public static DragonArmature DRAGON;

    public static WitherArmature WITHER;

    public static void build(ResourceManager resourceManager) {
        ARMATURES.clear();
        ModelBuildEvent.ArmatureBuild event = new ModelBuildEvent.ArmatureBuild(resourceManager, ARMATURES);
        BIPED = event.get("epicfight", "entity/biped", HumanoidArmature::new);
        CREEPER = event.get("epicfight", "entity/creeper", CreeperArmature::new);
        ENDERMAN = event.get("epicfight", "entity/enderman", EndermanArmature::new);
        SKELETON = event.get("epicfight", "entity/skeleton", HumanoidArmature::new);
        SPIDER = event.get("epicfight", "entity/spider", SpiderArmature::new);
        IRON_GOLEM = event.get("epicfight", "entity/iron_golem", IronGolemArmature::new);
        RAVAGER = event.get("epicfight", "entity/ravager", RavagerArmature::new);
        VEX = event.get("epicfight", "entity/vex", VexArmature::new);
        PIGLIN = event.get("epicfight", "entity/piglin", PiglinArmature::new);
        HOGLIN = event.get("epicfight", "entity/hoglin", HoglinArmature::new);
        DRAGON = event.get("epicfight", "entity/dragon", DragonArmature::new);
        WITHER = event.get("epicfight", "entity/wither", WitherArmature::new);
        registerEntityTypeArmature(EntityType.CAVE_SPIDER, SPIDER);
        registerEntityTypeArmature(EntityType.CREEPER, CREEPER);
        registerEntityTypeArmature(EntityType.DROWNED, BIPED);
        registerEntityTypeArmature(EntityType.ENDERMAN, ENDERMAN);
        registerEntityTypeArmature(EntityType.EVOKER, BIPED);
        registerEntityTypeArmature(EntityType.HOGLIN, HOGLIN);
        registerEntityTypeArmature(EntityType.HUSK, BIPED);
        registerEntityTypeArmature(EntityType.IRON_GOLEM, IRON_GOLEM);
        registerEntityTypeArmature(EntityType.PIGLIN_BRUTE, PIGLIN);
        registerEntityTypeArmature(EntityType.PIGLIN, PIGLIN);
        registerEntityTypeArmature(EntityType.PILLAGER, BIPED);
        registerEntityTypeArmature(EntityType.RAVAGER, RAVAGER);
        registerEntityTypeArmature(EntityType.SKELETON, SKELETON);
        registerEntityTypeArmature(EntityType.SPIDER, SPIDER);
        registerEntityTypeArmature(EntityType.STRAY, SKELETON);
        registerEntityTypeArmature(EntityType.VEX, VEX);
        registerEntityTypeArmature(EntityType.VINDICATOR, BIPED);
        registerEntityTypeArmature(EntityType.WITCH, BIPED);
        registerEntityTypeArmature(EntityType.WITHER_SKELETON, SKELETON);
        registerEntityTypeArmature(EntityType.ZOGLIN, HOGLIN);
        registerEntityTypeArmature(EntityType.ZOMBIE, BIPED);
        registerEntityTypeArmature(EntityType.ZOMBIE_VILLAGER, BIPED);
        registerEntityTypeArmature(EntityType.ZOMBIFIED_PIGLIN, PIGLIN);
        registerEntityTypeArmature(EntityType.PLAYER, BIPED);
        registerEntityTypeArmature(EntityType.ENDER_DRAGON, DRAGON);
        registerEntityTypeArmature(EntityType.WITHER, WITHER);
        registerEntityTypeArmature(EpicFightEntities.WITHER_SKELETON_MINION.get(), SKELETON);
        registerEntityTypeArmature(EpicFightEntities.WITHER_GHOST_CLONE.get(), WITHER);
        ModLoader.get().postEvent(event);
    }

    public static void registerEntityTypeArmature(EntityType<?> entityType, Armature armature) {
        ENTITY_TYPE_ARMATURE.put(entityType, (Function) entitypatch -> armature.deepCopy());
    }

    public static void registerEntityTypeArmature(EntityType<?> entityType, String presetName) {
        EntityType<?> presetEntityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(presetName));
        ENTITY_TYPE_ARMATURE.put(entityType, (Function) ENTITY_TYPE_ARMATURE.get(presetEntityType));
    }

    public static void registerEntityTypeArmature(EntityType<?> entityType, Function<EntityPatch<?>, Armature> armatureGetFunction) {
        ENTITY_TYPE_ARMATURE.put(entityType, armatureGetFunction);
    }

    public static <A extends Armature> A getArmatureFor(EntityPatch<?> patch) {
        return (A) ((Armature) ((Function) ENTITY_TYPE_ARMATURE.get(patch.getOriginal().getType())).apply(patch)).deepCopy();
    }

    public static Function<EntityPatch<?>, Armature> getRegistry(EntityType<?> entityType) {
        return (Function<EntityPatch<?>, Armature>) ENTITY_TYPE_ARMATURE.get(entityType);
    }

    public static <A extends Armature> A getOrCreateArmature(ResourceManager rm, ResourceLocation rl, Armatures.ArmatureContructor<A> constructor) {
        return (A) ARMATURES.computeIfAbsent(rl, key -> {
            JsonModelLoader jsonModelLoader = new JsonModelLoader(rm, rl);
            return jsonModelLoader.loadArmature(constructor);
        });
    }

    public Armature register(ResourceManager rm, ResourceLocation rl) {
        JsonModelLoader modelLoader = new JsonModelLoader(rm, rl);
        Armature armature = modelLoader.loadArmature(Armature::new);
        ARMATURES.put(rl, armature);
        return armature;
    }

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.runAsync(() -> build(resourceManager), gameExecutor).thenCompose(stage::m_6769_);
    }

    @FunctionalInterface
    public interface ArmatureContructor<T extends Armature> {

        T invoke(int var1, Joint var2, Map<String, Joint> var3);
    }
}