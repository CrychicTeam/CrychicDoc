package dev.xkmc.modulargolems.compat.materials.create;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import dev.xkmc.modulargolems.compat.materials.create.automation.CreateGolemRecipeGen;
import dev.xkmc.modulargolems.compat.materials.create.automation.CreateJEIEvents;
import dev.xkmc.modulargolems.compat.materials.create.automation.CreateRecipeEvents;
import dev.xkmc.modulargolems.compat.materials.create.automation.GolemIncompleteItem;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import java.util.List;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

public class CreateDispatch extends ModDispatch {

    public static final String MODID = "create";

    public CreateDispatch() {
        CreateCompatRegistry.register();
        MinecraftForge.EVENT_BUS.register(CreateRecipeEvents.class);
        if (ModList.get().isLoaded("jei")) {
            MinecraftForge.EVENT_BUS.register(CreateJEIEvents.class);
        }
    }

    @Override
    public void lateRegister() {
        for (ItemEntry<?> part : List.of(GolemItems.GOLEM_BODY, GolemItems.GOLEM_ARM, GolemItems.GOLEM_LEGS, GolemItems.HUMANOID_BODY, GolemItems.HUMANOID_ARMS, GolemItems.HUMANOID_LEGS, GolemItems.DOG_BODY, GolemItems.DOG_LEGS)) {
            ModularGolems.REGISTRATE.item("incomplete_" + part.getId().getPath(), p -> new GolemIncompleteItem(p, part)).removeTab(GolemItems.TAB.getKey()).model((ctx, pvd) -> ((ItemModelBuilder) pvd.getBuilder(ctx.getName())).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle", "minecraft:block/clay")).defaultLang().register();
        }
    }

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add("golem_material.create.zinc", "Zinc");
        pvd.add("golem_material.create.andesite_alloy", "Andesite Alloy");
        pvd.add("golem_material.create.brass", "Brass");
        pvd.add("golem_material.create.railway", "Railway");
    }

    @Override
    public void genRecipe(RegistrateRecipeProvider pvd) {
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) CreateCompatRegistry.UP_COATING.get())::m_126132_, (Item) AllItems.ZINC_INGOT.get()).pattern(" A ").pattern("ABA").pattern(" A ").define('A', AllTags.forgeItemTag("ingots/zinc")).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "create"));
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) CreateCompatRegistry.UP_PUSH.get())::m_126132_, (Item) AllItems.EXTENDO_GRIP.get()).pattern(" C ").pattern("ABA").pattern(" C ").define('A', (ItemLike) AllItems.EXTENDO_GRIP.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', (ItemLike) AllItems.PRECISION_MECHANISM.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "create"));
        CreateGolemRecipeGen.genAllUpgradeRecipes(pvd);
    }

    @Override
    public ConfigDataProvider getDataGen(DataGenerator gen) {
        return new CreateConfigGen(gen);
    }
}