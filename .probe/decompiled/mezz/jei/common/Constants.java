package mezz.jei.common;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

public final class Constants {

    public static final String TEXTURE_GUI_PATH = "textures/jei/gui/";

    public static final String TEXTURE_GUI_VANILLA = "textures/jei/gui/gui_vanilla.png";

    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation("jei", "textures/jei/gui/gui_vanilla.png");

    public static final RecipeType<?> UNIVERSAL_RECIPE_TRANSFER_TYPE = RecipeType.create("jei", "universal_recipe_transfer_handler", Object.class);

    public static final ResourceLocation LOCATION_JEI_GUI_TEXTURE_ATLAS = new ResourceLocation("jei", "textures/atlas/gui.png");

    public static final ResourceLocation NETWORK_CHANNEL_ID = new ResourceLocation("jei", "channel");

    public static final ResourceLocation HIDDEN_INGREDIENT_TAG = new ResourceLocation("c", "hidden_from_recipe_viewers");

    private Constants() {
    }
}