package net.minecraftforge.client.model.generators;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.VisibleForTesting;

public abstract class ModelProvider<T extends ModelBuilder<T>> implements DataProvider {

    public static final String BLOCK_FOLDER = "block";

    public static final String ITEM_FOLDER = "item";

    protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

    protected static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

    protected static final ExistingFileHelper.ResourceType MODEL_WITH_EXTENSION = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, "", "models");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected final PackOutput output;

    protected final String modid;

    protected final String folder;

    protected final Function<ResourceLocation, T> factory;

    @VisibleForTesting
    public final Map<ResourceLocation, T> generatedModels = new HashMap();

    @VisibleForTesting
    public final ExistingFileHelper existingFileHelper;

    protected abstract void registerModels();

    public ModelProvider(PackOutput output, String modid, String folder, Function<ResourceLocation, T> factory, ExistingFileHelper existingFileHelper) {
        Preconditions.checkNotNull(output);
        this.output = output;
        Preconditions.checkNotNull(modid);
        this.modid = modid;
        Preconditions.checkNotNull(folder);
        this.folder = folder;
        Preconditions.checkNotNull(factory);
        this.factory = factory;
        Preconditions.checkNotNull(existingFileHelper);
        this.existingFileHelper = existingFileHelper;
    }

    public ModelProvider(PackOutput output, String modid, String folder, BiFunction<ResourceLocation, ExistingFileHelper, T> builderFromModId, ExistingFileHelper existingFileHelper) {
        this(output, modid, folder, (Function<ResourceLocation, T>) (loc -> (ModelBuilder) builderFromModId.apply(loc, existingFileHelper)), existingFileHelper);
    }

    public T getBuilder(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = this.extendWithFolder(path.contains(":") ? new ResourceLocation(path) : new ResourceLocation(this.modid, path));
        this.existingFileHelper.trackGenerated(outputLoc, MODEL);
        return (T) this.generatedModels.computeIfAbsent(outputLoc, this.factory);
    }

    private ResourceLocation extendWithFolder(ResourceLocation rl) {
        return rl.getPath().contains("/") ? rl : new ResourceLocation(rl.getNamespace(), this.folder + "/" + rl.getPath());
    }

    public ResourceLocation modLoc(String name) {
        return new ResourceLocation(this.modid, name);
    }

    public ResourceLocation mcLoc(String name) {
        return new ResourceLocation(name);
    }

    public T withExistingParent(String name, String parent) {
        return this.withExistingParent(name, this.mcLoc(parent));
    }

    public T withExistingParent(String name, ResourceLocation parent) {
        return this.getBuilder(name).parent(this.getExistingFile(parent));
    }

    public T cube(String name, ResourceLocation down, ResourceLocation up, ResourceLocation north, ResourceLocation south, ResourceLocation east, ResourceLocation west) {
        return this.withExistingParent(name, "cube").texture("down", down).texture("up", up).texture("north", north).texture("south", south).texture("east", east).texture("west", west);
    }

    private T singleTexture(String name, String parent, ResourceLocation texture) {
        return this.singleTexture(name, this.mcLoc(parent), texture);
    }

    public T singleTexture(String name, ResourceLocation parent, ResourceLocation texture) {
        return this.singleTexture(name, parent, "texture", texture);
    }

    private T singleTexture(String name, String parent, String textureKey, ResourceLocation texture) {
        return this.singleTexture(name, this.mcLoc(parent), textureKey, texture);
    }

    public T singleTexture(String name, ResourceLocation parent, String textureKey, ResourceLocation texture) {
        return this.withExistingParent(name, parent).texture(textureKey, texture);
    }

    public T cubeAll(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/cube_all", "all", texture);
    }

    public T cubeTop(String name, ResourceLocation side, ResourceLocation top) {
        return this.withExistingParent(name, "block/cube_top").texture("side", side).texture("top", top);
    }

    private T sideBottomTop(String name, String parent, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return this.withExistingParent(name, parent).texture("side", side).texture("bottom", bottom).texture("top", top);
    }

    public T cubeBottomTop(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return this.sideBottomTop(name, "block/cube_bottom_top", side, bottom, top);
    }

    public T cubeColumn(String name, ResourceLocation side, ResourceLocation end) {
        return this.withExistingParent(name, "block/cube_column").texture("side", side).texture("end", end);
    }

    public T cubeColumnHorizontal(String name, ResourceLocation side, ResourceLocation end) {
        return this.withExistingParent(name, "block/cube_column_horizontal").texture("side", side).texture("end", end);
    }

    public T orientableVertical(String name, ResourceLocation side, ResourceLocation front) {
        return this.withExistingParent(name, "block/orientable_vertical").texture("side", side).texture("front", front);
    }

    public T orientableWithBottom(String name, ResourceLocation side, ResourceLocation front, ResourceLocation bottom, ResourceLocation top) {
        return this.withExistingParent(name, "block/orientable_with_bottom").texture("side", side).texture("front", front).texture("bottom", bottom).texture("top", top);
    }

    public T orientable(String name, ResourceLocation side, ResourceLocation front, ResourceLocation top) {
        return this.withExistingParent(name, "block/orientable").texture("side", side).texture("front", front).texture("top", top);
    }

    public T crop(String name, ResourceLocation crop) {
        return this.singleTexture(name, "block/crop", "crop", crop);
    }

    public T cross(String name, ResourceLocation cross) {
        return this.singleTexture(name, "block/cross", "cross", cross);
    }

    public T stairs(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return this.sideBottomTop(name, "block/stairs", side, bottom, top);
    }

    public T stairsOuter(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return this.sideBottomTop(name, "block/outer_stairs", side, bottom, top);
    }

    public T stairsInner(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return this.sideBottomTop(name, "block/inner_stairs", side, bottom, top);
    }

    public T slab(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return this.sideBottomTop(name, "block/slab", side, bottom, top);
    }

    public T slabTop(String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        return this.sideBottomTop(name, "block/slab_top", side, bottom, top);
    }

    public T button(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/button", texture);
    }

    public T buttonPressed(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/button_pressed", texture);
    }

    public T buttonInventory(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/button_inventory", texture);
    }

    public T pressurePlate(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/pressure_plate_up", texture);
    }

    public T pressurePlateDown(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/pressure_plate_down", texture);
    }

    public T sign(String name, ResourceLocation texture) {
        return this.getBuilder(name).texture("particle", texture);
    }

    public T fencePost(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/fence_post", texture);
    }

    public T fenceSide(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/fence_side", texture);
    }

    public T fenceInventory(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/fence_inventory", texture);
    }

    public T fenceGate(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/template_fence_gate", texture);
    }

    public T fenceGateOpen(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/template_fence_gate_open", texture);
    }

    public T fenceGateWall(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/template_fence_gate_wall", texture);
    }

    public T fenceGateWallOpen(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/template_fence_gate_wall_open", texture);
    }

    public T wallPost(String name, ResourceLocation wall) {
        return this.singleTexture(name, "block/template_wall_post", "wall", wall);
    }

    public T wallSide(String name, ResourceLocation wall) {
        return this.singleTexture(name, "block/template_wall_side", "wall", wall);
    }

    public T wallSideTall(String name, ResourceLocation wall) {
        return this.singleTexture(name, "block/template_wall_side_tall", "wall", wall);
    }

    public T wallInventory(String name, ResourceLocation wall) {
        return this.singleTexture(name, "block/wall_inventory", "wall", wall);
    }

    private T pane(String name, String parent, ResourceLocation pane, ResourceLocation edge) {
        return this.withExistingParent(name, "block/" + parent).texture("pane", pane).texture("edge", edge);
    }

    public T panePost(String name, ResourceLocation pane, ResourceLocation edge) {
        return this.pane(name, "template_glass_pane_post", pane, edge);
    }

    public T paneSide(String name, ResourceLocation pane, ResourceLocation edge) {
        return this.pane(name, "template_glass_pane_side", pane, edge);
    }

    public T paneSideAlt(String name, ResourceLocation pane, ResourceLocation edge) {
        return this.pane(name, "template_glass_pane_side_alt", pane, edge);
    }

    public T paneNoSide(String name, ResourceLocation pane) {
        return this.singleTexture(name, "block/template_glass_pane_noside", "pane", pane);
    }

    public T paneNoSideAlt(String name, ResourceLocation pane) {
        return this.singleTexture(name, "block/template_glass_pane_noside_alt", "pane", pane);
    }

    private T door(String name, String model, ResourceLocation bottom, ResourceLocation top) {
        return this.withExistingParent(name, "block/" + model).texture("bottom", bottom).texture("top", top);
    }

    public T doorBottomLeft(String name, ResourceLocation bottom, ResourceLocation top) {
        return this.door(name, "door_bottom_left", bottom, top);
    }

    public T doorBottomLeftOpen(String name, ResourceLocation bottom, ResourceLocation top) {
        return this.door(name, "door_bottom_left_open", bottom, top);
    }

    public T doorBottomRight(String name, ResourceLocation bottom, ResourceLocation top) {
        return this.door(name, "door_bottom_right", bottom, top);
    }

    public T doorBottomRightOpen(String name, ResourceLocation bottom, ResourceLocation top) {
        return this.door(name, "door_bottom_right_open", bottom, top);
    }

    public T doorTopLeft(String name, ResourceLocation bottom, ResourceLocation top) {
        return this.door(name, "door_top_left", bottom, top);
    }

    public T doorTopLeftOpen(String name, ResourceLocation bottom, ResourceLocation top) {
        return this.door(name, "door_top_left_open", bottom, top);
    }

    public T doorTopRight(String name, ResourceLocation bottom, ResourceLocation top) {
        return this.door(name, "door_top_right", bottom, top);
    }

    public T doorTopRightOpen(String name, ResourceLocation bottom, ResourceLocation top) {
        return this.door(name, "door_top_right_open", bottom, top);
    }

    public T trapdoorBottom(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/template_trapdoor_bottom", texture);
    }

    public T trapdoorTop(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/template_trapdoor_top", texture);
    }

    public T trapdoorOpen(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/template_trapdoor_open", texture);
    }

    public T trapdoorOrientableBottom(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/template_orientable_trapdoor_bottom", texture);
    }

    public T trapdoorOrientableTop(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/template_orientable_trapdoor_top", texture);
    }

    public T trapdoorOrientableOpen(String name, ResourceLocation texture) {
        return this.singleTexture(name, "block/template_orientable_trapdoor_open", texture);
    }

    public T torch(String name, ResourceLocation torch) {
        return this.singleTexture(name, "block/template_torch", "torch", torch);
    }

    public T torchWall(String name, ResourceLocation torch) {
        return this.singleTexture(name, "block/template_torch_wall", "torch", torch);
    }

    public T carpet(String name, ResourceLocation wool) {
        return this.singleTexture(name, "block/carpet", "wool", wool);
    }

    public T nested() {
        return (T) this.factory.apply(new ResourceLocation("dummy:dummy"));
    }

    public ModelFile.ExistingModelFile getExistingFile(ResourceLocation path) {
        ModelFile.ExistingModelFile ret = new ModelFile.ExistingModelFile(this.extendWithFolder(path), this.existingFileHelper);
        ret.assertExistence();
        return ret;
    }

    protected void clear() {
        this.generatedModels.clear();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.clear();
        this.registerModels();
        return this.generateAll(cache);
    }

    protected CompletableFuture<?> generateAll(CachedOutput cache) {
        CompletableFuture<?>[] futures = new CompletableFuture[this.generatedModels.size()];
        int i = 0;
        for (T model : this.generatedModels.values()) {
            Path target = this.getPath(model);
            futures[i++] = DataProvider.saveStable(cache, model.toJson(), target);
        }
        return CompletableFuture.allOf(futures);
    }

    protected Path getPath(T model) {
        ResourceLocation loc = model.getLocation();
        return this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(loc.getNamespace()).resolve("models").resolve(loc.getPath() + ".json");
    }
}