package mezz.jei.gui.ghost;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.List;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.MathUtil;
import mezz.jei.common.util.SafeIngredientUtil;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.phys.Vec2;
import org.lwjgl.opengl.GL11;

public class GhostIngredientDrag<T> {

    private static final int targetColor = 1075038474;

    private static final int hoverColor = -2142451431;

    private final IGhostIngredientHandler<?> handler;

    private final List<IGhostIngredientHandler.Target<T>> targets;

    private final List<Rect2i> targetAreas;

    private final IIngredientManager ingredientManager;

    private final IIngredientRenderer<T> ingredientRenderer;

    private final ITypedIngredient<T> ingredient;

    private final double mouseStartX;

    private final double mouseStartY;

    private final ImmutableRect2i origin;

    public GhostIngredientDrag(IGhostIngredientHandler<?> handler, List<IGhostIngredientHandler.Target<T>> targets, IIngredientManager ingredientManager, IIngredientRenderer<T> ingredientRenderer, ITypedIngredient<T> ingredient, double mouseX, double mouseY, ImmutableRect2i origin) {
        this.handler = handler;
        this.targets = targets;
        this.targetAreas = targets.stream().map(IGhostIngredientHandler.Target::getArea).toList();
        this.ingredientManager = ingredientManager;
        this.ingredientRenderer = ingredientRenderer;
        this.ingredient = ingredient;
        this.origin = origin;
        this.mouseStartX = mouseX;
        this.mouseStartY = mouseY;
    }

    public void drawTargets(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.handler.shouldHighlightTargets()) {
            drawTargets(guiGraphics, mouseX, mouseY, this.targetAreas);
        }
    }

    public static boolean farEnoughToDraw(GhostIngredientDrag<?> drag, double mouseX, double mouseY) {
        ImmutableRect2i origin = drag.getOrigin();
        Vec2 center;
        if (origin.isEmpty()) {
            center = new Vec2((float) drag.mouseStartX, (float) drag.mouseStartY);
        } else {
            center = new Vec2((float) origin.getX() + (float) origin.getWidth() / 2.0F, (float) origin.getY() + (float) origin.getHeight() / 2.0F);
        }
        double mouseXDist = (double) center.x - mouseX;
        double mouseYDist = (double) center.y - mouseY;
        double mouseDistSq = mouseXDist * mouseXDist + mouseYDist * mouseYDist;
        return mouseDistSq > 64.0;
    }

    public void drawItem(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (farEnoughToDraw(this, (double) mouseX, (double) mouseY)) {
            if (!this.origin.isEmpty()) {
                int originX = this.origin.getX() + this.origin.getWidth() / 2;
                int originY = this.origin.getY() + this.origin.getHeight() / 2;
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                ShaderInstance oldShader = RenderSystem.getShader();
                RenderSystem.setShader(GameRenderer::m_172811_);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                Tesselator tesselator = RenderSystem.renderThreadTesselator();
                BufferBuilder builder = tesselator.getBuilder();
                builder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
                float red = 0.2509804F;
                float green = 0.07450981F;
                float blue = 0.7882353F;
                float alpha = 0.039215688F;
                builder.m_5483_((double) mouseX, (double) mouseY, 150.0).color(red, green, blue, alpha).endVertex();
                builder.m_5483_((double) originX, (double) originY, 150.0).color(red, green, blue, alpha).endVertex();
                tesselator.end();
                RenderSystem.setShader(() -> oldShader);
                RenderSystem.enableDepthTest();
                RenderSystem.depthMask(true);
            }
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate((float) (mouseX - 8), (float) (mouseY - 8), 0.0F);
            SafeIngredientUtil.render(this.ingredientManager, this.ingredientRenderer, guiGraphics, this.ingredient);
            poseStack.popPose();
        }
    }

    public static void drawTargets(GuiGraphics guiGraphics, int mouseX, int mouseY, List<Rect2i> targetAreas) {
        RenderSystem.disableDepthTest();
        for (Rect2i area : targetAreas) {
            int color;
            if (MathUtil.contains(area, (double) mouseX, (double) mouseY)) {
                color = -2142451431;
            } else {
                color = 1075038474;
            }
            guiGraphics.fill(RenderType.guiOverlay(), area.getX(), area.getY(), area.getX() + area.getWidth(), area.getY() + area.getHeight(), color);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public boolean onClick(UserInput input) {
        for (IGhostIngredientHandler.Target<T> target : this.targets) {
            Rect2i area = target.getArea();
            if (MathUtil.contains(area, input.getMouseX(), input.getMouseY())) {
                if (!input.isSimulate()) {
                    target.accept(this.ingredient.getIngredient());
                    this.handler.onComplete();
                }
                return true;
            }
        }
        if (!input.isSimulate()) {
            this.handler.onComplete();
        }
        return false;
    }

    public void stop() {
        this.handler.onComplete();
    }

    public IIngredientRenderer<T> getIngredientRenderer() {
        return this.ingredientRenderer;
    }

    public ITypedIngredient<T> getIngredient() {
        return this.ingredient;
    }

    public ImmutableRect2i getOrigin() {
        return this.origin;
    }

    public IIngredientManager getIngredientManager() {
        return this.ingredientManager;
    }
}