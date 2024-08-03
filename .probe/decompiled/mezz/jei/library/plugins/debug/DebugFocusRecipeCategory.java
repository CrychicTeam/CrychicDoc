package mezz.jei.library.plugins.debug;

import java.util.List;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Internal;
import mezz.jei.common.gui.textures.Textures;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

public class DebugFocusRecipeCategory<F> implements IRecipeCategory<DebugRecipe> {

    public static final RecipeType<DebugRecipe> TYPE = RecipeType.create("jei", "debug_focus", DebugRecipe.class);

    public static final int RECIPE_WIDTH = 160;

    public static final int RECIPE_HEIGHT = 60;

    private final IDrawable background;

    private final IPlatformFluidHelper<F> platformFluidHelper;

    private final Component localizedName;

    public DebugFocusRecipeCategory(IGuiHelper guiHelper, IPlatformFluidHelper<F> platformFluidHelper) {
        this.background = guiHelper.createBlankDrawable(160, 60);
        this.platformFluidHelper = platformFluidHelper;
        this.localizedName = Component.literal("debug_focus");
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

    public void setRecipe(IRecipeLayoutBuilder builder, DebugRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 0, 0).addItemStacks(List.of(new ItemStack(Items.BUCKET), new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.POWDER_SNOW_BUCKET), new ItemStack(Items.AXOLOTL_BUCKET), new ItemStack(Items.SALMON_BUCKET), new ItemStack(Items.COD_BUCKET), new ItemStack(Items.PUFFERFISH_BUCKET), new ItemStack(Items.TROPICAL_FISH_BUCKET)));
        long bucketVolume = this.platformFluidHelper.bucketVolume();
        IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 20, 0).addItemStack(ItemStack.EMPTY).addIngredients(this.platformFluidHelper.getFluidIngredientType(), List.of(this.platformFluidHelper.create(Fluids.WATER, bucketVolume), this.platformFluidHelper.create(Fluids.LAVA, bucketVolume))).addItemStacks(List.of(new ItemStack(Items.SNOW_BLOCK), new ItemStack(Items.AXOLOTL_SPAWN_EGG), new ItemStack(Items.SALMON), new ItemStack(Items.COD), new ItemStack(Items.PUFFERFISH), new ItemStack(Items.TROPICAL_FISH)));
        IIngredientAcceptor<?> invisibleSlot = builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(List.of(new ItemStack(Items.ACACIA_BOAT), new ItemStack(Items.ACACIA_BUTTON), new ItemStack(Items.ACACIA_DOOR), new ItemStack(Items.ACACIA_LOG), new ItemStack(Items.ACACIA_PLANKS), new ItemStack(Items.ACACIA_FENCE), new ItemStack(Items.ACACIA_FENCE_GATE), new ItemStack(Items.ACACIA_LEAVES), new ItemStack(Items.ACACIA_PRESSURE_PLATE)));
        builder.createFocusLink(inputSlot, outputSlot, invisibleSlot);
    }
}