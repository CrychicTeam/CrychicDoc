package snownee.lychee.compat.jei.category;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.client.gui.AllGuiTextures;
import snownee.lychee.client.gui.GuiGameElement;
import snownee.lychee.client.gui.RenderElement;
import snownee.lychee.client.gui.ScreenElement;
import snownee.lychee.compat.JEIREI;
import snownee.lychee.compat.jei.JEICompat;
import snownee.lychee.compat.jei.SideBlockIcon;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.recipe.BlockKeyRecipe;
import snownee.lychee.core.recipe.ItemShapelessRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.interaction.BlockInteractingRecipe;
import snownee.lychee.util.CommonProxy;

public abstract class ItemAndBlockBaseCategory<C extends LycheeContext, T extends LycheeRecipe<C>> extends BaseJEICategory<C, T> {

    public Rect2i inputBlockRect = new Rect2i(30, 35, 20, 20);

    public Rect2i methodRect = new Rect2i(30, 12, 20, 20);

    private final ScreenElement mainIcon;

    public ItemAndBlockBaseCategory(List<LycheeRecipeType<C, T>> recipeTypes, ScreenElement mainIcon) {
        super(recipeTypes);
        this.mainIcon = mainIcon;
        this.infoRect.setPosition(8, 32);
    }

    @Override
    public IDrawable createIcon(IGuiHelper guiHelper, List<T> recipes) {
        return new JEICompat.ScreenElementWrapper(new SideBlockIcon(this.mainIcon, Suppliers.memoize(() -> this.getIconBlock(recipes))));
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

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        int y = recipe.m_7527_().size() <= 9 && recipe.showingActionsCount() <= 9 ? 28 : 26;
        if (recipe instanceof ItemShapelessRecipe) {
            this.ingredientGroup(builder, recipe, 38, y);
        } else if (recipe instanceof BlockInteractingRecipe) {
            this.ingredientGroup(builder, recipe, 22, 21);
        } else {
            this.ingredientGroup(builder, recipe, 12, 21);
        }
        this.actionGroup(builder, recipe, this.getWidth() - 29, y);
        addBlockIngredients(builder, recipe);
    }

    public void drawExtra(T recipe, GuiGraphics graphics, double mouseX, double mouseY, int centerX) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, this.methodRect.getX(), this.methodRect.getY());
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.drawInfoBadge(recipe, graphics, mouseX, mouseY);
        int centerX = this.getWidth() / 2;
        this.drawExtra(recipe, graphics, mouseX, mouseY, centerX);
        BlockState state = this.getRenderingBlock(recipe);
        if (state.m_60795_()) {
            AllGuiTextures.JEI_QUESTION_MARK.render(graphics, this.inputBlockRect.getX() + 4, this.inputBlockRect.getY() + 2);
        } else {
            PoseStack matrixStack = graphics.pose();
            if (state.m_60791_() < 5) {
                matrixStack.pushPose();
                matrixStack.translate((float) (this.inputBlockRect.getX() + 11), (float) (this.inputBlockRect.getY() + 16), 0.0F);
                matrixStack.scale(0.7F, 0.7F, 0.7F);
                AllGuiTextures.JEI_SHADOW.render(graphics, -26, -5);
                matrixStack.popPose();
            }
            GuiGameElement.of(state).rotateBlock(12.5, 202.5, 0.0).scale(15.0).lighting(JEIREI.BLOCK_LIGHTING).atLocal(0.0, 0.2, 0.0).<RenderElement>at((float) this.inputBlockRect.getX(), (float) this.inputBlockRect.getY()).render(graphics);
        }
    }

    @Override
    public List<Component> getTooltipStrings(T recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (this.getClass() != ItemBurningRecipeCategory.class && this.inputBlockRect.contains((int) mouseX, (int) mouseY)) {
            return BlockPredicateHelper.getTooltips(this.getRenderingBlock(recipe), this.getInputBlock(recipe));
        } else {
            if (this.methodRect.contains((int) mouseX, (int) mouseY)) {
                Component description = this.getMethodDescription(recipe);
                if (description != null) {
                    return List.of(description);
                }
            }
            return super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
        }
    }

    @Nullable
    public Component getMethodDescription(T recipe) {
        return null;
    }

    @Override
    public boolean handleInput(T recipe, double mouseX, double mouseY, InputConstants.Key input) {
        return this.getClass() != ItemBurningRecipeCategory.class && this.inputBlockRect.contains((int) mouseX, (int) mouseY) ? this.clickBlock(this.getRenderingBlock(recipe), input) : super.handleInput(recipe, mouseX, mouseY, input);
    }
}