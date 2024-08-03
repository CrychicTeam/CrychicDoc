package snownee.lychee.compat.rei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import snownee.lychee.block_crushing.BlockCrushingContext;
import snownee.lychee.block_crushing.BlockCrushingRecipe;
import snownee.lychee.client.gui.AllGuiTextures;
import snownee.lychee.client.gui.GuiGameElement;
import snownee.lychee.client.gui.RenderElement;
import snownee.lychee.compat.JEIREI;
import snownee.lychee.compat.rei.REICompat;
import snownee.lychee.compat.rei.ReactiveWidget;
import snownee.lychee.compat.rei.display.BaseREIDisplay;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.util.CommonProxy;

public class BlockCrushingRecipeCategory extends BaseREICategory<BlockCrushingContext, BlockCrushingRecipe, BaseREIDisplay<BlockCrushingRecipe>> {

    public static final Rect2i fallingBlockRect = new Rect2i(0, -35, 20, 35);

    public static final Rect2i landingBlockRect = new Rect2i(0, 0, 20, 20);

    public BlockCrushingRecipeCategory(LycheeRecipeType<BlockCrushingContext, BlockCrushingRecipe> recipeType) {
        super(recipeType);
    }

    @Override
    public int getDisplayWidth(BaseREIDisplay<BlockCrushingRecipe> display) {
        return this.getRealWidth();
    }

    @Override
    public int getRealWidth() {
        return 170;
    }

    @Override
    public List<Widget> setupDisplay(BaseREIDisplay<BlockCrushingRecipe> display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - this.getRealWidth() / 2, bounds.getY() + 4);
        BlockCrushingRecipe recipe = display.recipe;
        List<Widget> widgets = super.setupDisplay(display, bounds);
        this.drawInfoBadge(widgets, display, startPoint);
        widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            int xx = recipe.getIngredients().isEmpty() ? 41 : 77;
            boolean anyLandingBlock = recipe.getLandingBlock() == BlockPredicate.ANY;
            int yx = anyLandingBlock ? 45 : 33;
            float ticks = (float) (System.currentTimeMillis() % 2000L) / 1000.0F;
            ticks = Math.min(1.0F, ticks);
            ticks = ticks * ticks * ticks * ticks;
            PoseStack matrixStack = graphics.pose();
            matrixStack.pushPose();
            matrixStack.translate((float) startPoint.x, (float) startPoint.y, 0.0F);
            BlockState landingBlock = this.getLandingBlock(recipe);
            if (landingBlock.m_60791_() < 5) {
                matrixStack.pushPose();
                matrixStack.translate((double) xx + 10.5, (double) (yx + (anyLandingBlock ? 1 : 16)), 0.0);
                float shadow = 0.6F;
                if (anyLandingBlock) {
                    shadow = 0.2F + ticks * 0.2F;
                }
                matrixStack.scale(shadow, shadow, shadow);
                matrixStack.translate(-26.0, -5.5, 0.0);
                AllGuiTextures.JEI_SHADOW.render(graphics, 0, 0);
                matrixStack.popPose();
            }
            matrixStack.pushPose();
            matrixStack.translate((float) xx, (float) (yx - 13), 0.0F);
            GuiGameElement.of(this.getFallingBlock(recipe)).scale(15.0).atLocal(0.0, (double) ticks * 1.3 - 1.3, 2.0).rotateBlock(20.0, 225.0, 0.0).lighting(JEIREI.BLOCK_LIGHTING).<RenderElement>at(0.0F, 0.0F, 300.0F).render(graphics);
            if (!landingBlock.m_60795_()) {
                GuiGameElement.of(landingBlock).scale(15.0).atLocal(0.0, 1.0, 2.0).rotateBlock(20.0, 225.0, 0.0).lighting(JEIREI.BLOCK_LIGHTING).render(graphics);
            }
            matrixStack.popPose();
            matrixStack.popPose();
        }));
        int xCenter = bounds.getCenterX();
        int y = recipe.getIngredients().size() <= 9 && recipe.showingActionsCount() <= 9 ? 28 : 26;
        this.ingredientGroup(widgets, startPoint, recipe, xCenter - 45 - startPoint.x, y);
        this.actionGroup(widgets, startPoint, recipe, xCenter + 50 - startPoint.x, y);
        int x = recipe.getIngredients().isEmpty() ? 41 : 77;
        y = recipe.getLandingBlock() == BlockPredicate.ANY ? 45 : 33;
        fallingBlockRect.setPosition(x, y - 35);
        landingBlockRect.setPosition(x, y);
        ReactiveWidget reactive = new ReactiveWidget(REICompat.offsetRect(startPoint, fallingBlockRect));
        reactive.setTooltipFunction($ -> {
            List<Component> list = BlockPredicateHelper.getTooltips(this.getFallingBlock(recipe), recipe.getBlock());
            return (Component[]) list.toArray(new Component[0]);
        });
        reactive.setOnClick(($, button) -> this.clickBlock(this.getFallingBlock(recipe), button));
        widgets.add(reactive);
        if (recipe.getLandingBlock() != BlockPredicate.ANY) {
            reactive = new ReactiveWidget(REICompat.offsetRect(startPoint, landingBlockRect));
            reactive.setTooltipFunction($ -> {
                List<Component> list = BlockPredicateHelper.getTooltips(this.getLandingBlock(recipe), recipe.getLandingBlock());
                return (Component[]) list.toArray(new Component[0]);
            });
            reactive.setOnClick(($, button) -> this.clickBlock(this.getLandingBlock(recipe), button));
            widgets.add(reactive);
        }
        return widgets;
    }

    private BlockState getFallingBlock(BlockCrushingRecipe recipe) {
        return CommonProxy.getCycledItem(BlockPredicateHelper.getShowcaseBlockStates(recipe.getBlock()), Blocks.ANVIL.defaultBlockState(), 2000);
    }

    private BlockState getLandingBlock(BlockCrushingRecipe recipe) {
        return CommonProxy.getCycledItem(BlockPredicateHelper.getShowcaseBlockStates(recipe.getLandingBlock()), Blocks.AIR.defaultBlockState(), 2000);
    }

    @Override
    public Renderer createIcon(List<BlockCrushingRecipe> recipes) {
        return EntryStacks.of(Items.ANVIL);
    }
}