package net.minecraftforge.common.data;

import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public final class ForgeRecipeProvider extends VanillaRecipeProvider {

    private final Map<Item, TagKey<Item>> replacements = new HashMap();

    private final Set<ResourceLocation> excludes = new HashSet();

    public ForgeRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    private void exclude(ItemLike item) {
        this.excludes.add(ForgeRegistries.ITEMS.getKey(item.asItem()));
    }

    private void exclude(String name) {
        this.excludes.add(new ResourceLocation(name));
    }

    private void replace(ItemLike item, TagKey<Item> tag) {
        this.replacements.put(item.asItem(), tag);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        this.replace(Items.STICK, Tags.Items.RODS_WOODEN);
        this.replace(Items.GOLD_INGOT, Tags.Items.INGOTS_GOLD);
        this.replace(Items.IRON_INGOT, Tags.Items.INGOTS_IRON);
        this.replace(Items.NETHERITE_INGOT, Tags.Items.INGOTS_NETHERITE);
        this.replace(Items.COPPER_INGOT, Tags.Items.INGOTS_COPPER);
        this.replace(Items.AMETHYST_SHARD, Tags.Items.GEMS_AMETHYST);
        this.replace(Items.DIAMOND, Tags.Items.GEMS_DIAMOND);
        this.replace(Items.EMERALD, Tags.Items.GEMS_EMERALD);
        this.replace(Items.CHEST, Tags.Items.CHESTS_WOODEN);
        this.replace(Blocks.COBBLESTONE, Tags.Items.COBBLESTONE_NORMAL);
        this.replace(Blocks.COBBLED_DEEPSLATE, Tags.Items.COBBLESTONE_DEEPSLATE);
        this.replace(Items.STRING, Tags.Items.STRING);
        this.exclude(m_176517_(Blocks.WHITE_WOOL, Items.STRING));
        this.exclude(Blocks.GOLD_BLOCK);
        this.exclude(Items.GOLD_NUGGET);
        this.exclude(Blocks.IRON_BLOCK);
        this.exclude(Items.IRON_NUGGET);
        this.exclude(Blocks.DIAMOND_BLOCK);
        this.exclude(Blocks.EMERALD_BLOCK);
        this.exclude(Blocks.NETHERITE_BLOCK);
        this.exclude(Blocks.COPPER_BLOCK);
        this.exclude(Blocks.AMETHYST_BLOCK);
        this.exclude(Blocks.COBBLESTONE_STAIRS);
        this.exclude(Blocks.COBBLESTONE_SLAB);
        this.exclude(Blocks.COBBLESTONE_WALL);
        this.exclude(Blocks.COBBLED_DEEPSLATE_STAIRS);
        this.exclude(Blocks.COBBLED_DEEPSLATE_SLAB);
        this.exclude(Blocks.COBBLED_DEEPSLATE_WALL);
        super.buildRecipes(vanilla -> {
            FinishedRecipe modified = this.enhance(vanilla);
            if (modified != null) {
                consumer.accept(modified);
            }
        });
    }

    @Nullable
    private FinishedRecipe enhance(FinishedRecipe vanilla) {
        if (vanilla instanceof ShapelessRecipeBuilder.Result shapeless) {
            return this.enhance(shapeless);
        } else {
            return vanilla instanceof ShapedRecipeBuilder.Result shaped ? this.enhance(shaped) : null;
        }
    }

    @Nullable
    private FinishedRecipe enhance(ShapelessRecipeBuilder.Result vanilla) {
        List<Ingredient> ingredients = this.getField(ShapelessRecipeBuilder.Result.class, vanilla, 4);
        boolean modified = false;
        for (int x = 0; x < ingredients.size(); x++) {
            Ingredient ing = this.enhance(vanilla.getId(), (Ingredient) ingredients.get(x));
            if (ing != null) {
                ingredients.set(x, ing);
                modified = true;
            }
        }
        return modified ? vanilla : null;
    }

    @Nullable
    protected CompletableFuture<?> saveAdvancement(CachedOutput output, FinishedRecipe recipe, JsonObject json) {
        return null;
    }

    @Override
    protected CompletableFuture<?> buildAdvancement(CachedOutput output, ResourceLocation name, Advancement.Builder builder) {
        return CompletableFuture.allOf();
    }

    @Nullable
    private FinishedRecipe enhance(ShapedRecipeBuilder.Result vanilla) {
        Map<Character, Ingredient> ingredients = this.getField(ShapedRecipeBuilder.Result.class, vanilla, 5);
        boolean modified = false;
        for (Character x : ingredients.keySet()) {
            Ingredient ing = this.enhance(vanilla.getId(), (Ingredient) ingredients.get(x));
            if (ing != null) {
                ingredients.put(x, ing);
                modified = true;
            }
        }
        return modified ? vanilla : null;
    }

    @Nullable
    private Ingredient enhance(ResourceLocation name, Ingredient vanilla) {
        if (this.excludes.contains(name)) {
            return null;
        } else {
            boolean modified = false;
            List<Ingredient.Value> items = new ArrayList();
            Ingredient.Value[] vanillaItems = this.getField(Ingredient.class, vanilla, 2);
            for (Ingredient.Value entry : vanillaItems) {
                if (entry instanceof Ingredient.ItemValue) {
                    ItemStack stack = (ItemStack) entry.getItems().stream().findFirst().orElse(ItemStack.EMPTY);
                    TagKey<Item> replacement = (TagKey<Item>) this.replacements.get(stack.getItem());
                    if (replacement != null) {
                        items.add(new Ingredient.TagValue(replacement));
                        modified = true;
                    } else {
                        items.add(entry);
                    }
                } else {
                    items.add(entry);
                }
            }
            return modified ? Ingredient.fromValues(items.stream()) : null;
        }
    }

    private <T, R> R getField(Class<T> clz, T inst, int index) {
        Field fld = clz.getDeclaredFields()[index];
        fld.setAccessible(true);
        try {
            return (R) fld.get(inst);
        } catch (IllegalAccessException | IllegalArgumentException var6) {
            throw new RuntimeException(var6);
        }
    }
}