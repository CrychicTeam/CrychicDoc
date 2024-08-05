package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.utility.TextUtils;

@ParametersAreNonnullByDefault
public class CookingPotScreen extends AbstractContainerScreen<CookingPotMenu> implements RecipeUpdateListener {

    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("farmersdelight", "textures/gui/cooking_pot.png");

    private static final Rectangle HEAT_ICON = new Rectangle(47, 55, 17, 15);

    private static final Rectangle PROGRESS_ARROW = new Rectangle(89, 25, 0, 17);

    private final CookingPotRecipeBookComponent recipeBookComponent = new CookingPotRecipeBookComponent();

    private boolean widthTooNarrow;

    public CookingPotScreen(CookingPotMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void init() {
        super.init();
        this.widthTooNarrow = this.f_96543_ < 379;
        this.f_97728_ = 28;
        this.recipeBookComponent.m_100309_(this.f_96543_, this.f_96544_, this.f_96541_, this.widthTooNarrow, (RecipeBookMenu) this.f_97732_);
        this.f_97735_ = this.recipeBookComponent.m_181401_(this.f_96543_, this.f_97726_);
        if (Configuration.ENABLE_RECIPE_BOOK_COOKING_POT.get()) {
            this.m_142416_(new ImageButton(this.f_97735_ + 5, this.f_96544_ / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, button -> {
                this.recipeBookComponent.m_100384_();
                this.f_97735_ = this.recipeBookComponent.m_181401_(this.f_96543_, this.f_97726_);
                ((ImageButton) button).m_264152_(this.f_97735_ + 5, this.f_96544_ / 2 - 49);
            }));
        } else {
            this.recipeBookComponent.hide();
            this.f_97735_ = this.recipeBookComponent.m_181401_(this.f_96543_, this.f_97726_);
        }
        this.m_7787_(this.recipeBookComponent);
        this.m_264313_(this.recipeBookComponent);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.recipeBookComponent.m_100386_();
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(gui);
        if (this.recipeBookComponent.m_100385_() && this.widthTooNarrow) {
            this.renderBg(gui, partialTicks, mouseX, mouseY);
            this.recipeBookComponent.m_88315_(gui, mouseX, mouseY, partialTicks);
        } else {
            this.recipeBookComponent.m_88315_(gui, mouseX, mouseY, partialTicks);
            super.render(gui, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.m_280128_(gui, this.f_97735_, this.f_97736_, false, partialTicks);
        }
        this.renderMealDisplayTooltip(gui, mouseX, mouseY);
        this.renderHeatIndicatorTooltip(gui, mouseX, mouseY);
        this.recipeBookComponent.m_280545_(gui, this.f_97735_, this.f_97736_, mouseX, mouseY);
    }

    private void renderHeatIndicatorTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        if (this.isHovering(HEAT_ICON.x, HEAT_ICON.y, HEAT_ICON.width, HEAT_ICON.height, (double) mouseX, (double) mouseY)) {
            String key = "container.cooking_pot." + (((CookingPotMenu) this.f_97732_).isHeated() ? "heated" : "not_heated");
            gui.renderTooltip(this.f_96547_, TextUtils.getTranslation(key, this.f_97732_), mouseX, mouseY);
        }
    }

    protected void renderMealDisplayTooltip(GuiGraphics gui, int mouseX, int mouseY) {
        if (this.f_96541_ != null && this.f_96541_.player != null && ((CookingPotMenu) this.f_97732_).m_142621_().isEmpty() && this.f_97734_ != null && this.f_97734_.hasItem()) {
            if (this.f_97734_.index == 6) {
                List<Component> tooltip = new ArrayList();
                ItemStack mealStack = this.f_97734_.getItem();
                tooltip.add(((MutableComponent) mealStack.getItem().getDescription()).withStyle(mealStack.getRarity().color));
                ItemStack containerStack = ((CookingPotMenu) this.f_97732_).blockEntity.getContainer();
                String container = !containerStack.isEmpty() ? containerStack.getItem().getDescription().getString() : "";
                tooltip.add(TextUtils.getTranslation("container.cooking_pot.served_on", container).withStyle(ChatFormatting.GRAY));
                gui.renderComponentTooltip(this.f_96547_, tooltip, mouseX, mouseY);
            } else {
                gui.renderTooltip(this.f_96547_, this.f_97734_.getItem(), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
        super.renderLabels(gui, mouseX, mouseY);
        gui.drawString(this.f_96547_, this.f_169604_, 8, this.f_97727_ - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.f_96541_ != null) {
            gui.blit(BACKGROUND_TEXTURE, this.f_97735_, this.f_97736_, 0, 0, this.f_97726_, this.f_97727_);
            if (((CookingPotMenu) this.f_97732_).isHeated()) {
                gui.blit(BACKGROUND_TEXTURE, this.f_97735_ + HEAT_ICON.x, this.f_97736_ + HEAT_ICON.y, 176, 0, HEAT_ICON.width, HEAT_ICON.height);
            }
            int l = ((CookingPotMenu) this.f_97732_).getCookProgressionScaled();
            gui.blit(BACKGROUND_TEXTURE, this.f_97735_ + PROGRESS_ARROW.x, this.f_97736_ + PROGRESS_ARROW.y, 176, 15, l + 1, PROGRESS_ARROW.height);
        }
    }

    @Override
    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.m_100385_()) && super.isHovering(x, y, width, height, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
        if (this.recipeBookComponent.m_6375_(mouseX, mouseY, buttonId)) {
            this.m_7522_(this.recipeBookComponent);
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.m_100385_() || super.mouseClicked(mouseX, mouseY, buttonId);
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int x, int y, int buttonIdx) {
        boolean flag = mouseX < (double) x || mouseY < (double) y || mouseX >= (double) (x + this.f_97726_) || mouseY >= (double) (y + this.f_97727_);
        return flag && this.recipeBookComponent.m_100297_(mouseX, mouseY, this.f_97735_, this.f_97736_, this.f_97726_, this.f_97727_, buttonIdx);
    }

    @Override
    protected void slotClicked(Slot slot, int mouseX, int mouseY, ClickType clickType) {
        super.slotClicked(slot, mouseX, mouseY, clickType);
        this.recipeBookComponent.m_6904_(slot);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.m_100387_();
    }

    @Nonnull
    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}