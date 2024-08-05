package snownee.lychee.compat.rei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import snownee.lychee.client.gui.AllGuiTextures;
import snownee.lychee.client.gui.GuiGameElement;
import snownee.lychee.compat.JEIREI;
import snownee.lychee.compat.rei.REICompat;
import snownee.lychee.compat.rei.ReactiveWidget;
import snownee.lychee.compat.rei.display.BaseREIDisplay;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.dripstone_dripping.DripstoneContext;
import snownee.lychee.dripstone_dripping.DripstoneRecipe;
import snownee.lychee.dripstone_dripping.DripstoneRecipeType;
import snownee.lychee.util.CommonProxy;

public class DripstoneRecipeCategory extends BaseREICategory<DripstoneContext, DripstoneRecipe, BaseREIDisplay<DripstoneRecipe>> {

    private Rect2i sourceBlockRect = new Rect2i(23, 1, 16, 16);

    private Rect2i targetBlockRect = new Rect2i(23, 43, 16, 16);

    public DripstoneRecipeCategory(DripstoneRecipeType recipeType) {
        super(recipeType);
        this.infoRect.setX(-10);
    }

    @Override
    public List<Widget> setupDisplay(BaseREIDisplay<DripstoneRecipe> display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - this.getRealWidth() / 2, bounds.getY() + 4);
        DripstoneRecipe recipe = display.recipe;
        List<Widget> widgets = super.setupDisplay(display, bounds);
        this.drawInfoBadge(widgets, display, startPoint);
        widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            PoseStack matrixStack = graphics.pose();
            matrixStack.pushPose();
            matrixStack.translate((float) startPoint.x, (float) startPoint.y, 0.0F);
            BlockState targetBlock = this.getTargetBlock(recipe);
            if (targetBlock.m_60791_() < 5) {
                matrixStack.pushPose();
                matrixStack.translate(31.0F, 56.0F, 0.0F);
                float shadow = 0.5F;
                matrixStack.scale(shadow, shadow, shadow);
                matrixStack.translate(-26.0, -5.5, 0.0);
                AllGuiTextures.JEI_SHADOW.render(graphics, 0, 0);
                matrixStack.popPose();
            }
            matrixStack.pushPose();
            matrixStack.translate(22.0F, 24.0F, 300.0F);
            drawBlock(this.getSourceBlock(recipe), graphics, 0.0, -2.0, 0.0);
            drawBlock(Blocks.DRIPSTONE_BLOCK.defaultBlockState(), graphics, 0.0, -1.0, 0.0);
            drawBlock((BlockState) Blocks.POINTED_DRIPSTONE.defaultBlockState().m_61124_(PointedDripstoneBlock.TIP_DIRECTION, Direction.DOWN), graphics, 0.0, 0.0, 0.0);
            drawBlock(targetBlock, graphics, 0.0, 1.5, 0.0);
            matrixStack.popPose();
            matrixStack.popPose();
        }));
        int y = recipe.showingActionsCount() > 9 ? 26 : 28;
        this.actionGroup(widgets, startPoint, recipe, this.getRealWidth() - 24, y);
        ReactiveWidget reactive = new ReactiveWidget(REICompat.offsetRect(startPoint, this.sourceBlockRect));
        reactive.setTooltipFunction($ -> {
            List<Component> list = BlockPredicateHelper.getTooltips(this.getSourceBlock(recipe), recipe.getSourceBlock());
            return (Component[]) list.toArray(new Component[0]);
        });
        reactive.setOnClick(($, button) -> this.clickBlock(this.getSourceBlock(recipe), button));
        widgets.add(reactive);
        reactive = new ReactiveWidget(REICompat.offsetRect(startPoint, this.targetBlockRect));
        reactive.setTooltipFunction($ -> {
            List<Component> list = BlockPredicateHelper.getTooltips(this.getTargetBlock(recipe), recipe.getBlock());
            return (Component[]) list.toArray(new Component[0]);
        });
        reactive.setOnClick(($, button) -> this.clickBlock(this.getTargetBlock(recipe), button));
        widgets.add(reactive);
        return widgets;
    }

    private static void drawBlock(BlockState state, GuiGraphics graphics, double localX, double localY, double localZ) {
        GuiGameElement.of(state).scale(12.0).lighting(JEIREI.BLOCK_LIGHTING).atLocal(localX, localY, localZ).rotateBlock(12.5, -22.5, 0.0).render(graphics);
    }

    private BlockState getSourceBlock(DripstoneRecipe recipe) {
        return CommonProxy.getCycledItem(BlockPredicateHelper.getShowcaseBlockStates(recipe.getSourceBlock()), Blocks.AIR.defaultBlockState(), 2000);
    }

    private BlockState getTargetBlock(DripstoneRecipe recipe) {
        return CommonProxy.getCycledItem(BlockPredicateHelper.getShowcaseBlockStates(recipe.getBlock()), Blocks.AIR.defaultBlockState(), 2000);
    }

    @Override
    public Renderer createIcon(List<DripstoneRecipe> recipes) {
        return EntryStacks.of(Items.POINTED_DRIPSTONE);
    }
}