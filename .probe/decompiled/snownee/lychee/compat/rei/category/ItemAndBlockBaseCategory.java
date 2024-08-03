package snownee.lychee.compat.rei.category;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.RecipeTypes;
import snownee.lychee.client.gui.AllGuiTextures;
import snownee.lychee.client.gui.GuiGameElement;
import snownee.lychee.client.gui.RenderElement;
import snownee.lychee.client.gui.ScreenElement;
import snownee.lychee.compat.JEIREI;
import snownee.lychee.compat.rei.REICompat;
import snownee.lychee.compat.rei.ReactiveWidget;
import snownee.lychee.compat.rei.SideBlockIcon;
import snownee.lychee.compat.rei.display.BaseREIDisplay;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.recipe.BlockKeyRecipe;
import snownee.lychee.core.recipe.ItemShapelessRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.interaction.BlockInteractingRecipe;
import snownee.lychee.util.CommonProxy;

public abstract class ItemAndBlockBaseCategory<C extends LycheeContext, T extends LycheeRecipe<C>, D extends BaseREIDisplay<T>> extends BaseREICategory<C, T, D> {

    public Rect2i inputBlockRect = new Rect2i(30, 35, 20, 20);

    public Rect2i methodRect = new Rect2i(30, 12, 20, 20);

    private final ScreenElement mainIcon;

    public ItemAndBlockBaseCategory(List<LycheeRecipeType<C, T>> recipeTypes, ScreenElement mainIcon) {
        super(recipeTypes);
        this.mainIcon = mainIcon;
        this.infoRect.setPosition(8, 32);
    }

    public BlockState getIconBlock(List<T> recipes) {
        ClientPacketListener con = Minecraft.getInstance().getConnection();
        return con == null ? Blocks.AIR.defaultBlockState() : JEIREI.getMostUsedBlock(recipes).getFirst();
    }

    @Nullable
    public BlockPredicate getInputBlock(T recipe) {
        return ((BlockKeyRecipe) recipe).getBlock();
    }

    public BlockState getRenderingBlock(T recipe) {
        return CommonProxy.getCycledItem(BlockPredicateHelper.getShowcaseBlockStates(this.getInputBlock(recipe)), Blocks.AIR.defaultBlockState(), 1000);
    }

    public void drawExtra(T recipe, GuiGraphics graphics, double mouseX, double mouseY, int centerX) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, this.methodRect.getX(), this.methodRect.getY());
    }

    @Nullable
    public Component getMethodDescription(T recipe) {
        return null;
    }

    @Override
    public List<Widget> setupDisplay(D display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - this.getRealWidth() / 2, bounds.getY() + 4);
        T recipe = display.recipe;
        List<Widget> widgets = super.setupDisplay(display, bounds);
        this.drawInfoBadge(widgets, display, startPoint);
        widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            PoseStack matrixStack = graphics.pose();
            matrixStack.pushPose();
            matrixStack.translate((float) startPoint.x, (float) startPoint.y, 0.0F);
            this.drawExtra(recipe, graphics, (double) mouseX, (double) mouseY, bounds.getCenterX());
            BlockState state = this.getRenderingBlock(recipe);
            if (state.m_60795_()) {
                AllGuiTextures.JEI_QUESTION_MARK.render(graphics, this.inputBlockRect.getX() + 4, this.inputBlockRect.getY() + 2);
                matrixStack.popPose();
            } else {
                if (state.m_60791_() < 5) {
                    matrixStack.pushPose();
                    matrixStack.translate((float) (this.inputBlockRect.getX() + 11), (float) (this.inputBlockRect.getY() + 16), 0.0F);
                    matrixStack.scale(0.7F, 0.7F, 0.7F);
                    AllGuiTextures.JEI_SHADOW.render(graphics, -26, -5);
                    matrixStack.popPose();
                }
                GuiGameElement.of(state).rotateBlock(12.5, -22.5, 0.0).scale(15.0).lighting(JEIREI.BLOCK_LIGHTING).atLocal(0.0, 0.2, 0.0).<RenderElement>at((float) this.inputBlockRect.getX(), (float) this.inputBlockRect.getY()).render(graphics);
                matrixStack.popPose();
            }
        }));
        int y = recipe.m_7527_().size() <= 9 && recipe.showingActionsCount() <= 9 ? 28 : 26;
        if (recipe instanceof ItemShapelessRecipe) {
            this.ingredientGroup(widgets, startPoint, recipe, 40, y);
        } else if (recipe instanceof BlockInteractingRecipe) {
            this.ingredientGroup(widgets, startPoint, recipe, 22, 21);
        } else {
            this.ingredientGroup(widgets, startPoint, recipe, 12, 21);
        }
        this.actionGroup(widgets, startPoint, recipe, this.getRealWidth() - 34, y);
        Component description = this.getMethodDescription(recipe);
        if (description != null) {
            ReactiveWidget reactive = new ReactiveWidget(REICompat.offsetRect(startPoint, this.methodRect));
            reactive.setTooltipFunction($ -> new Component[] { description });
            widgets.add(reactive);
        }
        if (this.recipeTypes.get(0) != RecipeTypes.ITEM_BURNING) {
            ReactiveWidget reactive = new ReactiveWidget(REICompat.offsetRect(startPoint, this.inputBlockRect));
            reactive.setTooltipFunction($ -> {
                List<Component> list = BlockPredicateHelper.getTooltips(this.getRenderingBlock(recipe), this.getInputBlock(recipe));
                return (Component[]) list.toArray(new Component[0]);
            });
            reactive.setOnClick(($, button) -> this.clickBlock(this.getRenderingBlock(recipe), button));
            widgets.add(reactive);
        }
        return widgets;
    }

    @Override
    public Renderer createIcon(List<T> recipes) {
        return new REICompat.ScreenElementWrapper(new SideBlockIcon(this.mainIcon, Suppliers.memoize(() -> this.getIconBlock(recipes))));
    }
}