package mezz.jei.gui.ghost;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.MathUtil;
import mezz.jei.common.util.SafeIngredientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.phys.Vec2;

public class GhostIngredientReturning<T> {

    private static final long DURATION_PER_SCREEN_WIDTH = 500L;

    private final IIngredientManager ingredientManager;

    private final IIngredientRenderer<T> ingredientRenderer;

    private final ITypedIngredient<T> ingredient;

    private final Vec2 start;

    private final Vec2 end;

    private final long startTime;

    private final long duration;

    public static <T> Optional<GhostIngredientReturning<T>> create(GhostIngredientDrag<T> ghostIngredientDrag, double mouseX, double mouseY) {
        ImmutableRect2i origin = ghostIngredientDrag.getOrigin();
        if (origin.isEmpty()) {
            return Optional.empty();
        } else {
            IIngredientManager ingredientManager = ghostIngredientDrag.getIngredientManager();
            IIngredientRenderer<T> ingredientRenderer = ghostIngredientDrag.getIngredientRenderer();
            ITypedIngredient<T> ingredient = ghostIngredientDrag.getIngredient();
            Vec2 end = new Vec2((float) origin.getX(), (float) origin.getY());
            Vec2 start = new Vec2((float) mouseX - 8.0F, (float) mouseY - 8.0F);
            GhostIngredientReturning<T> returning = new GhostIngredientReturning<>(ingredientManager, ingredientRenderer, ingredient, start, end);
            return Optional.of(returning);
        }
    }

    private GhostIngredientReturning(IIngredientManager ingredientManager, IIngredientRenderer<T> ingredientRenderer, ITypedIngredient<T> ingredient, Vec2 start, Vec2 end) {
        this.ingredientManager = ingredientManager;
        this.ingredientRenderer = ingredientRenderer;
        this.ingredient = ingredient;
        this.start = start;
        this.end = end;
        this.startTime = System.currentTimeMillis();
        Screen currentScreen = Minecraft.getInstance().screen;
        if (currentScreen != null) {
            int width = currentScreen.width;
            float durationPerPixel = 500.0F / (float) width;
            float distance = (float) MathUtil.distance(start, end);
            this.duration = (long) Math.round(durationPerPixel * distance);
        } else {
            this.duration = (long) Math.round(250.0F);
        }
    }

    public void drawItem(GuiGraphics guiGraphics) {
        long time = System.currentTimeMillis();
        long elapsed = time - this.startTime;
        double percent = Math.min((double) elapsed / (double) this.duration, 1.0);
        double dx = (double) (this.end.x - this.start.x);
        double dy = (double) (this.end.y - this.start.y);
        double x = (double) (this.start.x + (float) Math.round(dx * percent));
        double y = (double) (this.start.y + (float) Math.round(dy * percent));
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0.0);
        SafeIngredientUtil.render(this.ingredientManager, this.ingredientRenderer, guiGraphics, this.ingredient);
        poseStack.popPose();
    }

    public boolean isComplete() {
        long time = System.currentTimeMillis();
        return this.startTime + this.duration < time;
    }
}