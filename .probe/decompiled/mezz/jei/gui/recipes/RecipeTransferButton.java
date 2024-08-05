package mezz.jei.gui.recipes;

import java.util.List;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferManager;
import mezz.jei.common.gui.TooltipRenderer;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.transfer.RecipeTransferErrorInternal;
import mezz.jei.common.transfer.RecipeTransferUtil;
import mezz.jei.gui.elements.GuiIconButtonSmall;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class RecipeTransferButton extends GuiIconButtonSmall {

    private final IRecipeLayoutDrawable<?> recipeLayout;

    private final Runnable onClose;

    @Nullable
    private IRecipeTransferError recipeTransferError;

    @Nullable
    private IOnClickHandler onClickHandler;

    public RecipeTransferButton(IDrawable icon, IRecipeLayoutDrawable<?> recipeLayout, Textures textures, Runnable onClose) {
        super(0, 0, 0, 0, icon, b -> {
        }, textures);
        this.recipeLayout = recipeLayout;
        this.onClose = onClose;
    }

    public void update(Rect2i area, IRecipeTransferManager recipeTransferManager, @Nullable AbstractContainerMenu container, Player player) {
        this.m_252865_(area.getX());
        this.m_253211_(area.getY());
        this.f_93618_ = area.getWidth();
        this.f_93619_ = area.getHeight();
        if (container != null) {
            this.recipeTransferError = (IRecipeTransferError) RecipeTransferUtil.getTransferRecipeError(recipeTransferManager, container, this.recipeLayout, player).orElse(null);
        } else {
            this.recipeTransferError = RecipeTransferErrorInternal.INSTANCE;
        }
        if (this.recipeTransferError != null && !this.recipeTransferError.getType().allowsTransfer) {
            this.f_93623_ = false;
            IRecipeTransferError.Type type = this.recipeTransferError.getType();
            this.f_93624_ = type == IRecipeTransferError.Type.USER_FACING;
        } else {
            this.f_93623_ = true;
            this.f_93624_ = true;
        }
        this.onClickHandler = (mouseX, mouseY) -> {
            boolean maxTransfer = Screen.hasShiftDown();
            if (container != null && RecipeTransferUtil.transferRecipe(recipeTransferManager, container, this.recipeLayout, player, maxTransfer)) {
                this.onClose.run();
            }
        };
    }

    public void drawToolTip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.isMouseOver((double) mouseX, (double) mouseY)) {
            if (this.recipeTransferError == null) {
                Component tooltipTransfer = Component.translatable("jei.tooltip.transfer");
                TooltipRenderer.drawHoveringText(guiGraphics, List.of(tooltipTransfer), mouseX, mouseY);
            } else {
                IRecipeSlotsView recipeSlotsView = this.recipeLayout.getRecipeSlotsView();
                Rect2i recipeRect = this.recipeLayout.getRect();
                this.recipeTransferError.showError(guiGraphics, mouseX, mouseY, recipeSlotsView, recipeRect.getX(), recipeRect.getY());
            }
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.f_93624_ && mouseX >= (double) this.m_252754_() && mouseY >= (double) this.m_252907_() && mouseX < (double) (this.m_252754_() + this.m_5711_()) && mouseY < (double) (this.m_252907_() + this.m_93694_());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        if (this.f_93624_ && this.recipeTransferError != null && this.recipeTransferError.getType() == IRecipeTransferError.Type.COSMETIC) {
            guiGraphics.fill(RenderType.guiOverlay(), this.m_252754_(), this.m_252907_(), this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_(), this.recipeTransferError.getButtonHighlightColor());
        }
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        if (this.isMouseOver(mouseX, mouseY)) {
            if (this.onClickHandler != null) {
                this.onClickHandler.onClick(mouseX, mouseY);
            }
        }
    }
}