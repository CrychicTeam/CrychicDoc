package mezz.jei.library.plugins.debug;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IBookmarkOverlay;
import mezz.jei.api.runtime.IIngredientFilter;
import mezz.jei.api.runtime.IIngredientListOverlay;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.common.Internal;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.library.plugins.debug.ingredients.DebugIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

public class DebugRecipeCategory<F> implements IRecipeCategory<DebugRecipe> {

    public static final RecipeType<DebugRecipe> TYPE = RecipeType.create("jei", "debug", DebugRecipe.class);

    public static final int RECIPE_WIDTH = 160;

    public static final int RECIPE_HEIGHT = 60;

    private final IDrawable background;

    private final IPlatformFluidHelper<F> platformFluidHelper;

    private final IIngredientManager ingredientManager;

    private final Component localizedName;

    private final IDrawable tankBackground;

    private final IDrawable tankOverlay;

    private final IDrawable item;

    @Nullable
    private IJeiRuntime runtime;

    private boolean hiddenRecipes;

    public DebugRecipeCategory(IGuiHelper guiHelper, IPlatformFluidHelper<F> platformFluidHelper, IIngredientManager ingredientManager) {
        this.background = guiHelper.createBlankDrawable(160, 60);
        this.platformFluidHelper = platformFluidHelper;
        this.ingredientManager = ingredientManager;
        this.localizedName = Component.literal("debug");
        ResourceLocation backgroundTexture = new ResourceLocation("jei", "textures/jei/gui/debug.png");
        this.tankBackground = guiHelper.createDrawable(backgroundTexture, 220, 196, 18, 60);
        this.tankOverlay = guiHelper.createDrawable(backgroundTexture, 238, 196, 18, 60);
        this.item = guiHelper.createDrawableItemStack(new ItemStack(Items.ACACIA_LEAVES));
    }

    public void setRuntime(IJeiRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public RecipeType<DebugRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        Textures textures = Internal.getTextures();
        return textures.getConfigButtonIcon();
    }

    public void draw(DebugRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (this.runtime != null) {
            this.item.draw(guiGraphics, 50, 20);
            IIngredientFilter ingredientFilter = this.runtime.getIngredientFilter();
            Minecraft minecraft = Minecraft.getInstance();
            guiGraphics.drawString(minecraft.font, ingredientFilter.getFilterText(), 20, 52, 0, false);
            IIngredientListOverlay ingredientListOverlay = this.runtime.getIngredientListOverlay();
            Optional<ITypedIngredient<?>> ingredientUnderMouse = getIngredientUnderMouse(ingredientListOverlay, this.runtime.getBookmarkOverlay());
            ingredientUnderMouse.ifPresent(typedIngredient -> this.drawIngredientName(minecraft, guiGraphics, typedIngredient));
        }
        Button button = recipe.getButton();
        button.m_88315_(guiGraphics, (int) mouseX, (int) mouseY, 0.0F);
    }

    private static Optional<ITypedIngredient<?>> getIngredientUnderMouse(IIngredientListOverlay ingredientListOverlay, IBookmarkOverlay bookmarkOverlay) {
        return ingredientListOverlay.getIngredientUnderMouse().or(bookmarkOverlay::getIngredientUnderMouse);
    }

    private <T> void drawIngredientName(Minecraft minecraft, GuiGraphics guiGraphics, ITypedIngredient<T> ingredient) {
        IIngredientHelper<T> ingredientHelper = this.ingredientManager.getIngredientHelper(ingredient.getType());
        String jeiUid = ingredientHelper.getUniqueId(ingredient.getIngredient(), UidContext.Ingredient);
        guiGraphics.drawString(minecraft.font, jeiUid, 50, 52, 0, false);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, DebugRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 0).addItemStack(new ItemStack(Items.FARMLAND)).addItemStack(new ItemStack(Items.BUNDLE));
        builder.addSlot(RecipeIngredientRole.INPUT, 110, 0).addIngredientsUnsafe(Arrays.asList(new ItemStack(Items.RABBIT), null));
        long bucketVolume = this.platformFluidHelper.bucketVolume();
        IIngredientType<F> fluidType = this.platformFluidHelper.getFluidIngredientType();
        long capacity = 10L * bucketVolume;
        long amount = capacity / 2L + (long) ((int) (Math.random() * (double) capacity / 2.0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 90, 0).setFluidRenderer(capacity, false, 16, 58).setOverlay(this.tankOverlay, -1, -1).setBackground(this.tankBackground, -1, -1).addFluidStack(Fluids.WATER, amount);
        capacity = 2L * bucketVolume;
        amount = capacity / 2L + (long) ((int) (Math.random() * (double) capacity / 2.0));
        builder.addSlot(RecipeIngredientRole.INPUT, 24, 0).setFluidRenderer(capacity, true, 12, 47).addIngredient(fluidType, this.platformFluidHelper.create(Fluids.LAVA, amount));
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 0).addIngredients(DebugIngredient.TYPE, List.of(new DebugIngredient(0), new DebugIngredient(1)));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 40, 16).addIngredient(DebugIngredient.TYPE, new DebugIngredient(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 32).addIngredient(DebugIngredient.TYPE, new DebugIngredient(3)).addIngredientsUnsafe(List.of(this.platformFluidHelper.create(Fluids.LAVA, (long) ((int) ((1.0 + Math.random()) * (double) bucketVolume))), new ItemStack(Items.ACACIA_LEAVES))).addTooltipCallback((recipeSlotView, tooltip) -> {
            switch(recipeSlotView.getRole()) {
                case INPUT:
                    tooltip.add(Component.literal("Input DebugIngredient"));
                    break;
                case OUTPUT:
                    tooltip.add(Component.literal("Output DebugIngredient"));
                    break;
                case CATALYST:
                    tooltip.add(Component.literal("Catalyst DebugIngredient"));
            }
        });
    }

    public List<Component> getTooltipStrings(DebugRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltipStrings = new ArrayList();
        tooltipStrings.add(Component.literal("Debug Recipe Category Tooltip is very long and going to wrap").withStyle(ChatFormatting.GOLD));
        if (recipe.checkHover(mouseX, mouseY)) {
            tooltipStrings.add(Component.literal("button tooltip!"));
        } else {
            MutableComponent debug = Component.literal("tooltip debug");
            tooltipStrings.add(debug.withStyle(ChatFormatting.BOLD));
        }
        tooltipStrings.add(Component.literal(mouseX + ", " + mouseY));
        return tooltipStrings;
    }

    public boolean handleInput(DebugRecipe recipe, double mouseX, double mouseY, InputConstants.Key input) {
        if (input.getType() != InputConstants.Type.MOUSE) {
            return false;
        } else {
            Button button = recipe.getButton();
            int mouseButton = input.getValue();
            if (mouseButton == 0 && button.m_6375_(mouseX, mouseY, mouseButton)) {
                Minecraft minecraft = Minecraft.getInstance();
                LocalPlayer player = minecraft.player;
                if (player != null) {
                    Screen screen = new InventoryScreen(player);
                    minecraft.setScreen(screen);
                }
                if (this.runtime != null) {
                    IIngredientFilter ingredientFilter = this.runtime.getIngredientFilter();
                    String filterText = ingredientFilter.getFilterText();
                    ingredientFilter.setFilterText(filterText + " test");
                    IRecipeManager recipeManager = this.runtime.getRecipeManager();
                    if (!this.hiddenRecipes) {
                        recipeManager.hideRecipeCategory(RecipeTypes.CRAFTING);
                        this.hiddenRecipes = true;
                    } else {
                        recipeManager.unhideRecipeCategory(RecipeTypes.CRAFTING);
                        this.hiddenRecipes = false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    @Nullable
    public ResourceLocation getRegistryName(DebugRecipe recipe) {
        return recipe.getRegistryName();
    }
}