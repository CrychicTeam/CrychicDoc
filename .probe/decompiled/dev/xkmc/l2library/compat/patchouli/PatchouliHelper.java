package dev.xkmc.l2library.compat.patchouli;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import vazkii.patchouli.common.item.ItemModBook;
import vazkii.patchouli.common.item.PatchouliItems;

public class PatchouliHelper {

    public static final ProviderType<PatchouliProvider> PATCHOULI = ProviderType.register("patchouli", (p, e) -> new PatchouliProvider(p, e.getGenerator()));

    private final L2Registrate reg;

    private final ResourceLocation book;

    private ResourceLocation model;

    public static ItemStack getBook(ResourceLocation book) {
        return ItemModBook.forBook(book);
    }

    public static LootTable.Builder getBookLoot(ResourceLocation book) {
        CompoundTag tag = new CompoundTag();
        tag.putString("patchouli:book", book.toString());
        return LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(PatchouliItems.BOOK).apply(SetNbtFunction.setTag(tag))));
    }

    public PatchouliHelper(L2Registrate reg, String name) {
        this.reg = reg;
        this.book = new ResourceLocation(reg.getModid(), name);
    }

    public PatchouliHelper buildModel() {
        return this.buildModel("book");
    }

    public PatchouliHelper buildModel(String path) {
        this.model = new ResourceLocation(this.reg.getModid(), path);
        this.reg.addDataGenerator(ProviderType.ITEM_MODEL, pvd -> ((ItemModelBuilder) pvd.getBuilder(path)).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/" + path));
        return this;
    }

    public PatchouliHelper buildShapelessRecipe(Consumer<ShapelessPatchouliBuilder> cons, Supplier<Item> unlock) {
        return this.buildRecipe(() -> Util.make(new ShapelessPatchouliBuilder(this.book), cons), unlock);
    }

    public PatchouliHelper buildShapedRecipe(Consumer<ShapedPatchouliBuilder> cons, Supplier<Item> unlock) {
        return this.buildRecipe(() -> Util.make(new ShapedPatchouliBuilder(this.book), cons), unlock);
    }

    private PatchouliHelper buildRecipe(Supplier<RecipeBuilder> cons, Supplier<Item> unlock) {
        this.reg.addDataGenerator(ProviderType.RECIPE, pvd -> {
            RecipeBuilder builder = (RecipeBuilder) cons.get();
            builder.unlockedBy("has_" + pvd.safeName((ItemLike) unlock.get()), DataIngredient.items((Item) unlock.get(), new Item[0]).getCritereon(pvd));
            builder.save(ConditionalRecipeWrapper.mod(pvd, "patchouli"), new ResourceLocation(this.reg.getModid(), "book"));
        });
        return this;
    }

    public PatchouliHelper buildBook(String title, String landing, int ver, ResourceKey<CreativeModeTab> tab) {
        if (this.model == null) {
            throw new IllegalStateException("Patchouli Book must have a model first");
        } else {
            String titleId = "patchouli." + this.reg.getModid() + ".title";
            String descId = "patchouli." + this.reg.getModid() + ".landing";
            this.reg.addRawLang(titleId, title);
            this.reg.addRawLang(descId, landing);
            this.reg.addDataGenerator(PATCHOULI, pvd -> pvd.accept(this.reg.getModid() + "/patchouli_books/" + this.book.getPath() + "/book", new PatchouliHelper.BookEntry(titleId, descId, ver, this.model, tab.location(), true)));
            return this;
        }
    }

    public static record BookEntry(String name, String landing_text, int version, ResourceLocation model, ResourceLocation creative_tab, boolean use_resource_pack) {
    }
}