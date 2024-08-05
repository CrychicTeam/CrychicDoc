package mezz.jei.library.plugins.debug.ingredients;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.TooltipFlag;

public class ErrorIngredientRenderer implements IIngredientRenderer<ErrorIngredient> {

    private static final List<RenderType> RENDER_TYPES = List.of(RenderType.gui(), RenderType.glint(), RenderType.debugFilledBox(), RenderType.guiOverlay(), RenderType.guiGhostRecipeOverlay(), RenderType.guiTextHighlight());

    private final IIngredientHelper<ErrorIngredient> ingredientHelper;

    public ErrorIngredientRenderer(IIngredientHelper<ErrorIngredient> ingredientHelper) {
        this.ingredientHelper = ingredientHelper;
    }

    public void render(GuiGraphics guiGraphics, ErrorIngredient ingredient) {
        Minecraft minecraft = Minecraft.getInstance();
        switch(ingredient.getCrashType()) {
            case RenderBreakVertexBufferCrash:
                MultiBufferSource.BufferSource bufferSource = guiGraphics.bufferSource();
                for (RenderType renderType : RENDER_TYPES) {
                    VertexConsumer buffer = bufferSource.getBuffer(renderType);
                    buffer.vertex(0.0, 0.0, 0.0);
                    buffer.color(100);
                }
                throw new RuntimeException("intentional render crash for testing");
            case TooltipCrash:
                Font font = this.getFontRenderer(minecraft, ingredient);
                guiGraphics.drawString(font, "JEI", 0, 0, -65536, false);
                guiGraphics.drawString(font, "TEST", 0, 8, -65536, false);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public List<Component> getTooltip(ErrorIngredient ingredient, TooltipFlag tooltipFlag) {
        if (ingredient.getCrashType() == ErrorIngredient.CrashType.TooltipCrash) {
            throw new RuntimeException("intentional tooltip crash for testing");
        } else {
            List<Component> tooltip = new ArrayList();
            String displayName = this.ingredientHelper.getDisplayName(ingredient);
            tooltip.add(Component.literal(displayName));
            MutableComponent debugIngredient = Component.literal("debug ingredient");
            tooltip.add(debugIngredient.withStyle(ChatFormatting.GRAY));
            return tooltip;
        }
    }
}