package mezz.jei.library.plugins.vanilla.cooking.fuel;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.text.NumberFormat;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.common.Constants;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.MathUtil;
import mezz.jei.library.plugins.vanilla.cooking.FurnaceVariantCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class FurnaceFuelCategory extends FurnaceVariantCategory<IJeiFuelingRecipe> {

    private final IDrawableStatic background;

    private final IDrawableStatic flameTransparentBackground;

    private final Component localizedName;

    private final LoadingCache<Integer, IDrawableAnimated> cachedFlames;

    private final ImmutableRect2i textArea;

    public FurnaceFuelCategory(final IGuiHelper guiHelper, Textures textures) {
        super(guiHelper);
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        Component maxSmeltCountText = createSmeltCountText(2000000000);
        int maxStringWidth = fontRenderer.width(maxSmeltCountText.getString());
        int backgroundHeight = 34;
        int textPadding = 20;
        this.background = guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 0, 134, 18, backgroundHeight).addPadding(0, 0, 0, textPadding + maxStringWidth).build();
        this.textArea = new ImmutableRect2i(20, 0, textPadding + maxStringWidth, backgroundHeight);
        this.flameTransparentBackground = textures.getFlameIcon();
        this.localizedName = Component.translatable("gui.jei.category.fuel");
        this.cachedFlames = CacheBuilder.newBuilder().maximumSize(25L).build(new CacheLoader<Integer, IDrawableAnimated>() {

            public IDrawableAnimated load(Integer burnTime) {
                return guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 114, 14, 14).buildAnimated(burnTime, IDrawableAnimated.StartDirection.TOP, true);
            }
        });
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public RecipeType<IJeiFuelingRecipe> getRecipeType() {
        return RecipeTypes.FUELING;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getIcon() {
        return this.flameTransparentBackground;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IJeiFuelingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 17).addItemStacks(recipe.getInputs());
    }

    public void draw(IJeiFuelingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int burnTime = recipe.getBurnTime();
        IDrawableAnimated flame = (IDrawableAnimated) this.cachedFlames.getUnchecked(burnTime);
        flame.draw(guiGraphics, 1, 0);
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        Component smeltCountText = createSmeltCountText(burnTime);
        ImmutableRect2i centerArea = MathUtil.centerTextArea(this.textArea, font, smeltCountText);
        guiGraphics.drawString(font, smeltCountText, centerArea.getX(), centerArea.getY(), -8355712, false);
    }

    private static Component createSmeltCountText(int burnTime) {
        if (burnTime == 200) {
            return Component.translatable("gui.jei.category.fuel.smeltCount.single");
        } else {
            NumberFormat numberInstance = NumberFormat.getNumberInstance();
            numberInstance.setMaximumFractionDigits(2);
            String smeltCount = numberInstance.format((double) ((float) burnTime / 200.0F));
            return Component.translatable("gui.jei.category.fuel.smeltCount", smeltCount);
        }
    }
}