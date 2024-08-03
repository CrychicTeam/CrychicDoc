package snownee.lychee.compat;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import snownee.lychee.Lychee;
import snownee.lychee.RecipeTypes;
import snownee.lychee.client.core.post.PostActionRenderer;
import snownee.lychee.client.gui.CustomLightingSettings;
import snownee.lychee.client.gui.ILightingSettings;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.recipe.BlockKeyRecipe;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.util.CachedRenderingEntity;
import snownee.lychee.util.CommonProxy;
import snownee.lychee.util.Pair;

public class JEIREI {

    public static ILightingSettings BLOCK_LIGHTING = CustomLightingSettings.builder().firstLightRotation(-30.0F, 45.0F).secondLightRotation(0.0F, 65.0F).build();

    public static ILightingSettings SIDE_ICON_LIGHTING = CustomLightingSettings.builder().firstLightRotation(135.0F, 35.0F).secondLightRotation(-20.0F, 50.0F).build();

    public static ILightingSettings FUSED_TNT_LIGHTING = CustomLightingSettings.builder().firstLightRotation(-120.0F, 20.0F).secondLightRotation(200.0F, 45.0F).build();

    public static final CachedRenderingEntity<PrimedTnt> TNT_ENTITY = CachedRenderingEntity.ofFactory(EntityType.TNT::m_20615_);

    public static List<IngredientInfo> generateShapelessInputs(LycheeRecipe<?> recipe) {
        List<IngredientInfo> ingredients = (List<IngredientInfo>) recipe.m_7527_().stream().map(IngredientInfo::new).collect(Collectors.toCollection(ArrayList::new));
        recipe.getPostActions().forEach(action -> PostActionRenderer.of(action).loadCatalystsInfo(action, recipe, ingredients));
        if (!recipe.getType().compactInputs) {
            addIngredientTips(recipe, ingredients);
            return ingredients;
        } else {
            List<IngredientInfo> newIngredients = Lists.newArrayList();
            for (IngredientInfo ingredient : ingredients) {
                IngredientInfo match = null;
                if (CommonProxy.isSimpleIngredient(ingredient.ingredient)) {
                    for (IngredientInfo toCompare : newIngredients) {
                        if (Objects.equals(toCompare.tooltips, ingredient.tooltips) && CommonProxy.isSimpleIngredient(toCompare.ingredient) && toCompare.ingredient.getStackingIds().equals(ingredient.ingredient.getStackingIds())) {
                            match = toCompare;
                            break;
                        }
                    }
                }
                if (match == null) {
                    newIngredients.add(ingredient);
                } else {
                    match.count = match.count + ingredient.count;
                }
            }
            addIngredientTips(recipe, newIngredients);
            return newIngredients;
        }
    }

    public static void addIngredientTips(LycheeRecipe<?> recipe, List<IngredientInfo> ingredients) {
        for (IngredientInfo ingredient : ingredients) {
            IngredientInfo.Type type = CommonProxy.getIngredientType(ingredient.ingredient);
            if (type != IngredientInfo.Type.NORMAL) {
                ingredient.addTooltip(Component.translatable("tip.lychee.ingredient." + type.name().toLowerCase(Locale.ROOT)));
            }
        }
    }

    public static ResourceLocation composeCategoryIdentifier(ResourceLocation categoryId, ResourceLocation group) {
        return new ResourceLocation(categoryId.getNamespace(), "%s/%s/%s".formatted(categoryId.getPath(), group.getNamespace(), group.getPath()));
    }

    public static MutableComponent makeTitle(ResourceLocation id) {
        String key = id.toLanguageKey("recipeType");
        int i = key.indexOf(47);
        if ("/minecraft/default".equals(key.substring(i))) {
            key = key.substring(0, i);
        }
        return Component.translatable(key);
    }

    public static <T extends LycheeRecipe<?>> Pair<BlockState, Integer> getMostUsedBlock(List<T> recipes) {
        Object2IntMap<Block> blockStateCount = new Object2IntOpenHashMap();
        Map<Block, BlockPredicate> blockPredicateMap = Maps.newHashMap();
        for (T object : recipes) {
            BlockKeyRecipe<?> recipe = (BlockKeyRecipe<?>) object;
            for (Block block : BlockPredicateHelper.getMatchedBlocks(recipe.getBlock())) {
                if (!block.defaultBlockState().m_60795_()) {
                    blockStateCount.mergeInt(block, 1, Integer::sum);
                    blockPredicateMap.putIfAbsent(block, recipe.getBlock());
                }
            }
        }
        return blockStateCount.isEmpty() ? Pair.of(Blocks.AIR.defaultBlockState(), 0) : (Pair) blockStateCount.object2IntEntrySet().stream().max(Comparator.comparingInt(Entry::getIntValue)).map($ -> Pair.of(BlockPredicateHelper.anyBlockState((BlockPredicate) blockPredicateMap.get($.getKey())), $.getIntValue())).orElseGet(() -> Pair.of(Blocks.AIR.defaultBlockState(), 0));
    }

    public static void registerCategories(Predicate<ResourceLocation> categoryIdValidator, BiConsumer<ResourceLocation, JEIREI.CategoryCreationContext> registrar) {
        Map<ResourceLocation, Map<ResourceLocation, List<LycheeRecipe<?>>>> recipes = Maps.newHashMap();
        for (LycheeRecipeType<?, ?> recipeType : RecipeTypes.ALL) {
            if (recipeType.hasStandaloneCategory) {
                for (LycheeRecipe<?> recipe : recipeType.inViewerRecipes()) {
                    ((List) ((Map) recipes.computeIfAbsent(recipeType.categoryId, $ -> Maps.newHashMap())).computeIfAbsent(new ResourceLocation(recipe.group), $ -> Lists.newArrayList())).add(recipe);
                }
            }
        }
        recipes.forEach((categoryId, map) -> {
            if (!categoryIdValidator.test(categoryId)) {
                Lychee.LOGGER.warn("Category factory %s does not exist".formatted(categoryId));
            } else {
                map.forEach((group, groupRecipes) -> {
                    JEIREI.CategoryCreationContext context = new JEIREI.CategoryCreationContext(group, groupRecipes);
                    registrar.accept(categoryId, context);
                });
            }
        });
    }

    public static List<Component> getRecipeTooltip(ILycheeRecipe<?> recipe) {
        List<Component> list = Lists.newArrayList();
        if (!Strings.isNullOrEmpty(recipe.getComment())) {
            String comment = recipe.getComment();
            if (I18n.exists(comment)) {
                comment = I18n.get(comment);
            }
            Splitter.on('\n').splitToStream(comment).map(Component::m_237113_).forEach(list::add);
        }
        Minecraft mc = Minecraft.getInstance();
        recipe.getContextualHolder().getConditionTooltips(list, 0, mc.level, mc.player);
        return list;
    }

    public static void renderTnt(GuiGraphics graphics, float x, float y) {
        PrimedTnt tnt = TNT_ENTITY.getEntity();
        int fuse = 80 - tnt.f_19797_ % 80;
        if (fuse < 40) {
            TNT_ENTITY.earlySetLevel();
            tnt.setFuse(fuse);
            float toRad = (float) (Math.PI / 180.0);
            Quaternionf quaternion = new Quaternionf().rotateXYZ(200.0F * toRad, -20.0F * toRad, 0.0F);
            FUSED_TNT_LIGHTING.applyLighting();
            TNT_ENTITY.render(graphics.pose(), x, y, 20.0F, quaternion);
        }
    }

    public static record CategoryCreationContext(ResourceLocation group, List<LycheeRecipe<?>> recipes) {
    }
}