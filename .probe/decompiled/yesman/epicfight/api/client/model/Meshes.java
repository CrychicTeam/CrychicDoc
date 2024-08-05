package yesman.epicfight.api.client.model;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModLoader;
import yesman.epicfight.api.forgeevent.ModelBuildEvent;
import yesman.epicfight.api.model.JsonModelLoader;
import yesman.epicfight.client.mesh.CreeperMesh;
import yesman.epicfight.client.mesh.DragonMesh;
import yesman.epicfight.client.mesh.EndermanMesh;
import yesman.epicfight.client.mesh.HoglinMesh;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.mesh.IronGolemMesh;
import yesman.epicfight.client.mesh.PiglinMesh;
import yesman.epicfight.client.mesh.RavagerMesh;
import yesman.epicfight.client.mesh.SpiderMesh;
import yesman.epicfight.client.mesh.VexMesh;
import yesman.epicfight.client.mesh.VillagerMesh;
import yesman.epicfight.client.mesh.WitherMesh;

@OnlyIn(Dist.CLIENT)
public class Meshes implements PreparableReloadListener {

    public static final Meshes INSTANCE = new Meshes();

    private static final Map<ResourceLocation, Mesh<?>> MESHES = Maps.newHashMap();

    public static HumanoidMesh ALEX;

    public static HumanoidMesh BIPED;

    public static HumanoidMesh BIPED_OLD_TEX;

    public static HumanoidMesh BIPED_OUTLAYER;

    public static HumanoidMesh VILLAGER_ZOMBIE;

    public static CreeperMesh CREEPER;

    public static EndermanMesh ENDERMAN;

    public static HumanoidMesh SKELETON;

    public static SpiderMesh SPIDER;

    public static IronGolemMesh IRON_GOLEM;

    public static HumanoidMesh ILLAGER;

    public static VillagerMesh WITCH;

    public static RavagerMesh RAVAGER;

    public static VexMesh VEX;

    public static PiglinMesh PIGLIN;

    public static HoglinMesh HOGLIN;

    public static DragonMesh DRAGON;

    public static WitherMesh WITHER;

    public static AnimatedMesh HELMET;

    public static AnimatedMesh HELMET_PIGLIN;

    public static AnimatedMesh HELMET_VILLAGER;

    public static AnimatedMesh CHESTPLATE;

    public static AnimatedMesh LEGGINS;

    public static AnimatedMesh BOOTS;

    public static Mesh.RawMesh AIR_BURST;

    public static Mesh.RawMesh FORCE_FIELD;

    public static Mesh.RawMesh LASER;

    public static void build(ResourceManager resourceManager) {
        MESHES.clear();
        ModelBuildEvent.MeshBuild event = new ModelBuildEvent.MeshBuild(resourceManager, MESHES);
        ALEX = event.getAnimated("epicfight", "entity/biped_slim_arm", HumanoidMesh::new);
        BIPED = event.getAnimated("epicfight", "entity/biped", HumanoidMesh::new);
        BIPED_OLD_TEX = event.getAnimated("epicfight", "entity/biped_old_texture", HumanoidMesh::new);
        BIPED_OUTLAYER = event.getAnimated("epicfight", "entity/biped_outlayer", HumanoidMesh::new);
        VILLAGER_ZOMBIE = event.getAnimated("epicfight", "entity/zombie_villager", VillagerMesh::new);
        CREEPER = event.getAnimated("epicfight", "entity/creeper", CreeperMesh::new);
        ENDERMAN = event.getAnimated("epicfight", "entity/enderman", EndermanMesh::new);
        SKELETON = event.getAnimated("epicfight", "entity/skeleton", HumanoidMesh::new);
        SPIDER = event.getAnimated("epicfight", "entity/spider", SpiderMesh::new);
        IRON_GOLEM = event.getAnimated("epicfight", "entity/iron_golem", IronGolemMesh::new);
        ILLAGER = event.getAnimated("epicfight", "entity/illager", VillagerMesh::new);
        WITCH = event.getAnimated("epicfight", "entity/witch", VillagerMesh::new);
        RAVAGER = event.getAnimated("epicfight", "entity/ravager", RavagerMesh::new);
        VEX = event.getAnimated("epicfight", "entity/vex", VexMesh::new);
        PIGLIN = event.getAnimated("epicfight", "entity/piglin", PiglinMesh::new);
        HOGLIN = event.getAnimated("epicfight", "entity/hoglin", HoglinMesh::new);
        DRAGON = event.getAnimated("epicfight", "entity/dragon", DragonMesh::new);
        WITHER = event.getAnimated("epicfight", "entity/wither", WitherMesh::new);
        AIR_BURST = event.getRaw("epicfight", "particle/air_burst", Mesh.RawMesh::new);
        FORCE_FIELD = event.getRaw("epicfight", "particle/force_field", Mesh.RawMesh::new);
        LASER = event.getRaw("epicfight", "particle/laser", Mesh.RawMesh::new);
        HELMET = event.getAnimated("epicfight", "armor/helmet", AnimatedMesh::new);
        HELMET_PIGLIN = event.getAnimated("epicfight", "armor/piglin_helmet", AnimatedMesh::new);
        HELMET_VILLAGER = event.getAnimated("epicfight", "armor/villager_helmet", AnimatedMesh::new);
        CHESTPLATE = event.getAnimated("epicfight", "armor/chestplate", AnimatedMesh::new);
        LEGGINS = event.getAnimated("epicfight", "armor/leggins", AnimatedMesh::new);
        BOOTS = event.getAnimated("epicfight", "armor/boots", AnimatedMesh::new);
        ModLoader.get().postEvent(event);
    }

    public static <M extends Mesh.RawMesh> M getOrCreateRawMesh(ResourceManager rm, ResourceLocation rl, Meshes.MeshContructor<VertexIndicator, M> constructor) {
        return (M) MESHES.computeIfAbsent(rl, key -> {
            JsonModelLoader jsonModelLoader = new JsonModelLoader(rm, rl);
            return jsonModelLoader.loadMesh(constructor);
        });
    }

    public static <M extends AnimatedMesh> M getOrCreateAnimatedMesh(ResourceManager rm, ResourceLocation rl, Meshes.MeshContructor<VertexIndicator.AnimatedVertexIndicator, M> constructor) {
        return (M) MESHES.computeIfAbsent(rl, key -> {
            JsonModelLoader jsonModelLoader = new JsonModelLoader(rm, rl);
            return jsonModelLoader.loadAnimatedMesh(constructor);
        });
    }

    public static void addMesh(ResourceLocation rl, AnimatedMesh animatedMesh) {
        MESHES.put(rl, animatedMesh);
    }

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier stage, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.runAsync(() -> build(resourceManager), gameExecutor).thenCompose(stage::m_6769_);
    }

    @FunctionalInterface
    public interface MeshContructor<V extends VertexIndicator, M extends Mesh<V>> {

        M invoke(Map<String, float[]> var1, M var2, Mesh.RenderProperties var3, Map<String, ModelPart<V>> var4);
    }
}