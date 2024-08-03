package com.mna.interop.jei;

import com.mna.blocks.BlockInit;
import com.mna.gui.base.GuiJEIDisable;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.block.ContainerMagiciansWorkbench;
import com.mna.gui.containers.block.ContainerSpectralCraftingTable;
import com.mna.interop.jei.categories.ArcaneFurnaceRecipeCategory;
import com.mna.interop.jei.categories.CrushingRecipeCategory;
import com.mna.interop.jei.categories.EldrinAltarRecipeCategory;
import com.mna.interop.jei.categories.EldrinFumeRecipeCategory;
import com.mna.interop.jei.categories.ManaweavingAltarRecipeCategory;
import com.mna.interop.jei.categories.RitualRecipeCategory;
import com.mna.interop.jei.categories.RunescribingRecipeCategory;
import com.mna.interop.jei.categories.RunicAnvilRecipeCategory;
import com.mna.interop.jei.categories.TransmutationRecipeCategory;
import com.mna.interop.jei.ingredients.ManaweavePatternIngredient;
import com.mna.interop.jei.ingredients.ManaweavePatternIngredientRenderer;
import com.mna.interop.jei.ingredients.ManaweavePatternIngredientType;
import com.mna.interop.jei.ingredients.ManaweavingPatternIngredientHelper;
import com.mna.items.ItemInit;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.block.Blocks;

@JeiPlugin
public class JEIInterop implements IModPlugin {

    private EldrinAltarRecipeCategory eldrinAltarRecipeCategory;

    private RitualRecipeCategory ritualRecipeCategory;

    private ManaweavingAltarRecipeCategory manaweavingRecipeCategory;

    private ArcaneFurnaceRecipeCategory arcaneFurnaceRecipeCategory;

    private RunicAnvilRecipeCategory runicAnvilRecipeCategory;

    private RunescribingRecipeCategory runescribingRecipeCategory;

    private CrushingRecipeCategory crushingRecipeCategory;

    private TransmutationRecipeCategory transmutationRecipeCategory;

    private EldrinFumeRecipeCategory fumeRecipeCategory;

    private Minecraft minecraft = Minecraft.getInstance();

    public static final ManaweavePatternIngredientRenderer manaweaveIngredientRenderer = new ManaweavePatternIngredientRenderer();

    public static final ManaweavePatternIngredientType MANAWEAVE_PATTERN = new ManaweavePatternIngredientType();

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("mna", "jei-integration");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(GuiJEIDisable.class, new IGuiContainerHandler<GuiJEIDisable<?>>() {

            public List<Rect2i> getGuiExtraAreas(GuiJEIDisable<?> containerScreen) {
                return containerScreen.getSideWindowBounds();
            }
        });
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        this.ritualRecipeCategory = new RitualRecipeCategory(guiHelper);
        this.manaweavingRecipeCategory = new ManaweavingAltarRecipeCategory(guiHelper);
        this.arcaneFurnaceRecipeCategory = new ArcaneFurnaceRecipeCategory(guiHelper);
        this.runicAnvilRecipeCategory = new RunicAnvilRecipeCategory(guiHelper);
        this.runescribingRecipeCategory = new RunescribingRecipeCategory(guiHelper);
        this.eldrinAltarRecipeCategory = new EldrinAltarRecipeCategory(guiHelper);
        this.crushingRecipeCategory = new CrushingRecipeCategory(guiHelper);
        this.transmutationRecipeCategory = new TransmutationRecipeCategory(guiHelper);
        this.fumeRecipeCategory = new EldrinFumeRecipeCategory(guiHelper);
        registration.addRecipeCategories(this.ritualRecipeCategory);
        registration.addRecipeCategories(this.manaweavingRecipeCategory);
        registration.addRecipeCategories(this.arcaneFurnaceRecipeCategory);
        registration.addRecipeCategories(this.runicAnvilRecipeCategory);
        registration.addRecipeCategories(this.runescribingRecipeCategory);
        registration.addRecipeCategories(this.eldrinAltarRecipeCategory);
        registration.addRecipeCategories(this.crushingRecipeCategory);
        registration.addRecipeCategories(this.transmutationRecipeCategory);
        registration.addRecipeCategories(this.fumeRecipeCategory);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(MANAWEAVE_PATTERN, (Collection<ManaweavePatternIngredient>) ManaweavingPatternHelper.getAllPatterns(this.minecraft.level).stream().map(p -> new ManaweavePatternIngredient(p.m_6423_())).collect(Collectors.toList()), new ManaweavingPatternIngredientHelper(), manaweaveIngredientRenderer);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel world = this.minecraft.level;
        registration.addRecipes(MARecipeTypes.RITUAL, world.getRecipeManager().getAllRecipesFor(RecipeInit.RITUAL_TYPE.get()));
        registration.addRecipes(MARecipeTypes.ELDRIN_ALTAR, world.getRecipeManager().getAllRecipesFor(RecipeInit.ELDRIN_ALTAR_TYPE.get()));
        registration.addRecipes(MARecipeTypes.MANAWEAVING, world.getRecipeManager().getAllRecipesFor(RecipeInit.MANAWEAVING_RECIPE_TYPE.get()));
        registration.addRecipes(MARecipeTypes.ARCANE_FURNACE, world.getRecipeManager().getAllRecipesFor(RecipeInit.ARCANE_FURNACE_TYPE.get()));
        registration.addRecipes(MARecipeTypes.RUNEFORGING, world.getRecipeManager().getAllRecipesFor(RecipeInit.RUNEFORGING_TYPE.get()));
        registration.addRecipes(MARecipeTypes.RUNESCRIBING, world.getRecipeManager().getAllRecipesFor(RecipeInit.RUNESCRIBING_TYPE.get()));
        registration.addRecipes(MARecipeTypes.CRUSHING, world.getRecipeManager().getAllRecipesFor(RecipeInit.CRUSHING_TYPE.get()));
        registration.addRecipes(MARecipeTypes.TRANSMUTATION, world.getRecipeManager().getAllRecipesFor(RecipeInit.TRANSMUTATION_TYPE.get()));
        registration.addRecipes(MARecipeTypes.FUME, world.getRecipeManager().getAllRecipesFor(RecipeInit.FUME_FILTER_TYPE.get()));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ContainerSpectralCraftingTable.class, ContainerInit.SPECTRAL_CRAFTING_TABLE.get(), RecipeTypes.CRAFTING, 1, 9, 10, 36);
        registration.addRecipeTransferHandler(new IRecipeTransferInfo<ContainerMagiciansWorkbench, CraftingRecipe>() {

            @Override
            public Class<ContainerMagiciansWorkbench> getContainerClass() {
                return ContainerMagiciansWorkbench.class;
            }

            public boolean canHandle(ContainerMagiciansWorkbench container, CraftingRecipe recipe) {
                return true;
            }

            public List<Slot> getRecipeSlots(ContainerMagiciansWorkbench container, CraftingRecipe recipe) {
                List<Slot> slots = new ArrayList();
                if (container.craftingGridIsEmpty(false)) {
                    for (int i = ContainerMagiciansWorkbench.FIRST_CRAFT_GRID_START; i < ContainerMagiciansWorkbench.FIRST_OUTPUT_SLOT; i++) {
                        Slot slot = container.m_38853_(i);
                        slots.add(slot);
                    }
                } else {
                    for (int i = ContainerMagiciansWorkbench.SECOND_CRAFT_GRID_START; i < ContainerMagiciansWorkbench.SECOND_OUTPUT_SLOT; i++) {
                        Slot slot = container.m_38853_(i);
                        slots.add(slot);
                    }
                }
                return slots;
            }

            public List<Slot> getInventorySlots(ContainerMagiciansWorkbench container, CraftingRecipe recipe) {
                List<Slot> slots = new ArrayList();
                for (int i = ContainerMagiciansWorkbench.INVENTORY_STORAGE_START; i <= ContainerMagiciansWorkbench.INVENTORY_STORAGE_END; i++) {
                    Slot slot = container.m_38853_(i);
                    slots.add(slot);
                }
                for (int i = ContainerMagiciansWorkbench.PLAYER_INVENTORY_START; i <= ContainerMagiciansWorkbench.PLAYER_ACTION_BAR_END; i++) {
                    Slot slot = container.m_38853_(i);
                    slots.add(slot);
                }
                return slots;
            }

            @Override
            public Optional<MenuType<ContainerMagiciansWorkbench>> getMenuType() {
                return Optional.of(ContainerInit.MAGICIANS_WORKBENCH.get());
            }

            @Override
            public RecipeType<CraftingRecipe> getRecipeType() {
                return RecipeTypes.CRAFTING;
            }
        });
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockInit.RUNEFORGE.get()), MARecipeTypes.ARCANE_FURNACE, RecipeTypes.CRAFTING, RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.RUNESCRIBING_TABLE.get()), MARecipeTypes.RUNESCRIBING);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.RUNIC_ANVIL.get()), MARecipeTypes.RUNEFORGING);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.MANAWEAVING_ALTAR.get()), MARecipeTypes.MANAWEAVING);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.INSCRIPTION_TABLE.get()), MARecipeTypes.SHAPE, MARecipeTypes.COMPONENT, MARecipeTypes.MODIFIER);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.ELDRIN_ALTAR.get()), MARecipeTypes.ELDRIN_ALTAR);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.CONSTRUCT_WORKBENCH.get()), MARecipeTypes.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(Blocks.GRINDSTONE), MARecipeTypes.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ItemInit.WIZARD_CHALK.get()), MARecipeTypes.RITUAL);
        registration.addRecipeCatalyst(new ItemStack(ItemInit.PRACTITIONERS_POUCH.get()), MARecipeTypes.RITUAL);
        registration.addRecipeCatalyst(new ItemStack(ItemInit.MANAWEAVER_WAND.get()), MARecipeTypes.TRANSMUTATION);
        registration.addRecipeCatalyst(new ItemStack(ItemInit.MANAWEAVER_WAND_ADVANCED.get()), MARecipeTypes.TRANSMUTATION);
        registration.addRecipeCatalyst(new ItemStack(ItemInit.MANAWEAVER_WAND_IMPROVISED.get()), MARecipeTypes.TRANSMUTATION);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.MAGICIANS_WORKBENCH.get()), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(BlockInit.ELDRIN_FUME.get()), MARecipeTypes.FUME);
    }
}