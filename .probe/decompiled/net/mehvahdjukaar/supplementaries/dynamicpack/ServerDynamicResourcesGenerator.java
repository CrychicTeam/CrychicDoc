package net.mehvahdjukaar.supplementaries.dynamicpack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.mehvahdjukaar.moonlight.api.resources.recipe.IRecipeTemplate;
import net.mehvahdjukaar.moonlight.api.resources.recipe.TemplateRecipeManager;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biomes;
import org.apache.logging.log4j.Logger;

public class ServerDynamicResourcesGenerator extends DynServerResourcesGenerator {

    public static final ServerDynamicResourcesGenerator INSTANCE = new ServerDynamicResourcesGenerator();

    private IRecipeTemplate<?> signPostTemplate2;

    public ServerDynamicResourcesGenerator() {
        super(new DynamicDataPack(Supplementaries.res("generated_pack")));
        this.dynamicPack.setGenerateDebugResources(PlatHelper.isDev() || (Boolean) CommonConfigs.General.DEBUG_RESOURCES.get());
    }

    @Override
    public Logger getLogger() {
        return Supplementaries.LOGGER;
    }

    @Override
    public boolean dependsOnLoadedPacks() {
        return true;
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {
        SimpleTagBuilder builder = SimpleTagBuilder.of(Supplementaries.res("sign_posts"));
        builder.addEntries(ModRegistry.SIGN_POST_ITEMS.values());
        this.dynamicPack.addTag(builder, Registries.ITEM);
        if ((Boolean) CommonConfigs.Building.SIGN_POST_ENABLED.get()) {
            this.addSignPostRecipes(manager);
        }
        if (PlatHelper.getPlatform().isForge()) {
            builder = SimpleTagBuilder.of(ModTags.HAS_WAY_SIGNS);
            if ((Boolean) CommonConfigs.Building.WAY_SIGN_ENABLED.get() && (Boolean) CommonConfigs.Building.SIGN_POST_ENABLED.get()) {
                builder.addTag(BiomeTags.IS_OVERWORLD);
            }
            this.dynamicPack.addTag(builder, Registries.BIOME);
            builder = SimpleTagBuilder.of(ModTags.HAS_CAVE_URNS);
            if ((Boolean) CommonConfigs.Functional.URN_PILE_ENABLED.get() && (Boolean) CommonConfigs.Functional.URN_ENABLED.get()) {
                builder.addTag(BiomeTags.IS_OVERWORLD);
            }
            this.dynamicPack.addTag(builder, Registries.BIOME);
            builder = SimpleTagBuilder.of(ModTags.HAS_WILD_FLAX);
            if ((Boolean) CommonConfigs.Functional.WILD_FLAX_ENABLED.get()) {
                builder.addTag(BiomeTags.IS_OVERWORLD);
            }
            this.dynamicPack.addTag(builder, Registries.BIOME);
            builder = SimpleTagBuilder.of(ModTags.HAS_BASALT_ASH);
            if ((Boolean) CommonConfigs.Building.BASALT_ASH_ENABLED.get()) {
                builder.add(Biomes.BASALT_DELTAS.location());
                builder.m_215905_(new ResourceLocation("incendium:volcanic_deltas"));
            }
            this.dynamicPack.addTag(builder, Registries.BIOME);
        }
        genAllRecipesAdv("supplementaries");
    }

    private void addSignPostRecipes(ResourceManager manager) {
        IRecipeTemplate<?> template = RPUtils.readRecipeAsTemplate(manager, ResType.RECIPES.getPath(Supplementaries.res("sign_post_oak")));
        WoodType oak = WoodTypeRegistry.OAK_TYPE;
        if (this.signPostTemplate2 == null) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, (ItemLike) ModRegistry.SIGN_POST_ITEMS.get(oak), 3).pattern("   ").pattern("222").pattern(" 1 ").define('1', Items.STICK).define('2', oak.planks).group("sign_post").unlockedBy("has_plank", InventoryChangeTrigger.TriggerInstance.hasItems(oak.planks)).m_176498_(s -> this.signPostTemplate2 = TemplateRecipeManager.read(s.serializeRecipe()));
        }
        ModRegistry.SIGN_POST_ITEMS.forEach((w, i) -> {
            if (w != oak) {
                try {
                    IRecipeTemplate<?> recipeTemplate = w.getChild("sign") == null ? this.signPostTemplate2 : template;
                    FinishedRecipe newR = recipeTemplate.createSimilar(WoodTypeRegistry.OAK_TYPE, w, w.mainChild().asItem());
                    if (newR == null) {
                        return;
                    }
                    newR = ForgeHelper.addRecipeConditions(newR, template.getConditions());
                    this.dynamicPack.addRecipe(newR);
                } catch (Exception var7) {
                    Supplementaries.LOGGER.error("Failed to generate recipe for sign post {}:", i, var7);
                }
            }
        });
    }

    public static void genAllRecipesAdv(String modId) {
    }

    private static void removeNullEntries(JsonObject jsonObject) {
        jsonObject.entrySet().removeIf(entry -> ((JsonElement) entry.getValue()).isJsonNull());
        jsonObject.entrySet().forEach(entry -> {
            JsonElement element = (JsonElement) entry.getValue();
            if (element.isJsonObject()) {
                removeNullEntries(element.getAsJsonObject());
            } else if (element.isJsonArray()) {
                removeNullEntries(element.getAsJsonArray());
            }
        });
    }

    private static void removeNullEntries(JsonArray jsonArray) {
        JsonArray newArray = new JsonArray();
        jsonArray.forEach(element -> {
            if (!element.isJsonNull()) {
                if (element.isJsonObject()) {
                    removeNullEntries(element.getAsJsonObject());
                } else if (element.isJsonArray()) {
                    removeNullEntries(element.getAsJsonArray());
                }
                newArray.add(element);
            }
        });
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonArray.remove(i);
        }
        jsonArray.addAll(newArray);
    }
}