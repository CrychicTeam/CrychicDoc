package mezz.jei.library.plugins.debug;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.platform.IPlatformScreenHelper;
import mezz.jei.common.platform.Services;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DebugGhostIngredientHandler<T extends AbstractContainerScreen<?>> implements IGhostIngredientHandler<T> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final IIngredientManager ingredientManager;

    public DebugGhostIngredientHandler(IIngredientManager ingredientManager) {
        this.ingredientManager = ingredientManager;
    }

    public <I> List<IGhostIngredientHandler.Target<I>> getTargetsTyped(T gui, ITypedIngredient<I> typedIngredient, boolean doStart) {
        List<IGhostIngredientHandler.Target<I>> targets = new ArrayList();
        targets.add(new DebugGhostIngredientHandler.DebugInfoTarget("Got an Ingredient", new Rect2i(0, 0, 20, 20), this.ingredientManager));
        if (doStart) {
            IIngredientType<I> ingredientType = typedIngredient.getType();
            IIngredientHelper<I> ingredientHelper = this.ingredientManager.getIngredientHelper(ingredientType);
            LOGGER.info("Ghost Ingredient Handling Starting with {}", ingredientHelper.getErrorInfo(typedIngredient.getIngredient()));
            targets.add(new DebugGhostIngredientHandler.DebugInfoTarget("Got an Ingredient", new Rect2i(20, 20, 20, 20), this.ingredientManager));
        }
        typedIngredient.getIngredient(VanillaTypes.ITEM_STACK).ifPresent(itemStack -> {
            boolean even = true;
            IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
            for (Slot slot : gui.getMenu().slots) {
                if (even) {
                    int guiLeft = screenHelper.getGuiLeft(gui);
                    int guiTop = screenHelper.getGuiTop(gui);
                    Rect2i area = new Rect2i(guiLeft + slot.x, guiTop + slot.y, 16, 16);
                    targets.add(new DebugGhostIngredientHandler.DebugInfoTarget("Got an Ingredient in Gui", area, this.ingredientManager));
                }
                even = !even;
            }
        });
        return targets;
    }

    @Override
    public void onComplete() {
        LOGGER.info("Ghost Ingredient Handling Complete");
    }

    private static class DebugInfoTarget<I> implements IGhostIngredientHandler.Target<I> {

        private final String message;

        private final Rect2i rectangle;

        private final IIngredientManager ingredientManager;

        public DebugInfoTarget(String message, Rect2i rectangle, IIngredientManager ingredientManager) {
            this.message = message;
            this.rectangle = rectangle;
            this.ingredientManager = ingredientManager;
        }

        @Override
        public Rect2i getArea() {
            return this.rectangle;
        }

        @Override
        public void accept(I ingredient) {
            IIngredientType<I> ingredientType = (IIngredientType<I>) this.ingredientManager.getIngredientTypeChecked(ingredient).orElseThrow();
            IIngredientHelper<I> ingredientHelper = this.ingredientManager.getIngredientHelper(ingredientType);
            DebugGhostIngredientHandler.LOGGER.info("{}: {}", this.message, ingredientHelper.getErrorInfo(ingredient));
        }
    }
}