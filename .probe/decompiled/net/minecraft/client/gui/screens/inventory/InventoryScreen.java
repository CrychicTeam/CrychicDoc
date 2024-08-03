package net.minecraft.client.gui.screens.inventory;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class InventoryScreen extends EffectRenderingInventoryScreen<InventoryMenu> implements RecipeUpdateListener {

    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");

    private float xMouse;

    private float yMouse;

    private final RecipeBookComponent recipeBookComponent = new RecipeBookComponent();

    private boolean widthTooNarrow;

    private boolean buttonClicked;

    public InventoryScreen(Player player0) {
        super(player0.inventoryMenu, player0.getInventory(), Component.translatable("container.crafting"));
        this.f_97728_ = 97;
    }

    @Override
    public void containerTick() {
        if (this.f_96541_.gameMode.hasInfiniteItems()) {
            this.f_96541_.setScreen(new CreativeModeInventoryScreen(this.f_96541_.player, this.f_96541_.player.connection.enabledFeatures(), this.f_96541_.options.operatorItemsTab().get()));
        } else {
            this.recipeBookComponent.tick();
        }
    }

    @Override
    protected void init() {
        if (this.f_96541_.gameMode.hasInfiniteItems()) {
            this.f_96541_.setScreen(new CreativeModeInventoryScreen(this.f_96541_.player, this.f_96541_.player.connection.enabledFeatures(), this.f_96541_.options.operatorItemsTab().get()));
        } else {
            super.m_7856_();
            this.widthTooNarrow = this.f_96543_ < 379;
            this.recipeBookComponent.init(this.f_96543_, this.f_96544_, this.f_96541_, this.widthTooNarrow, (RecipeBookMenu<?>) this.f_97732_);
            this.f_97735_ = this.recipeBookComponent.updateScreenPosition(this.f_96543_, this.f_97726_);
            this.m_142416_(new ImageButton(this.f_97735_ + 104, this.f_96544_ / 2 - 22, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, p_289631_ -> {
                this.recipeBookComponent.toggleVisibility();
                this.f_97735_ = this.recipeBookComponent.updateScreenPosition(this.f_96543_, this.f_97726_);
                p_289631_.m_264152_(this.f_97735_ + 104, this.f_96544_ / 2 - 22);
                this.buttonClicked = true;
            }));
            this.m_7787_(this.recipeBookComponent);
            this.m_264313_(this.recipeBookComponent);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
        guiGraphics0.drawString(this.f_96547_, this.f_96539_, this.f_97728_, this.f_97729_, 4210752, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(guiGraphics0, float3, int1, int2);
            this.recipeBookComponent.render(guiGraphics0, int1, int2, float3);
        } else {
            this.recipeBookComponent.render(guiGraphics0, int1, int2, float3);
            super.render(guiGraphics0, int1, int2, float3);
            this.recipeBookComponent.renderGhostRecipe(guiGraphics0, this.f_97735_, this.f_97736_, false, float3);
        }
        this.m_280072_(guiGraphics0, int1, int2);
        this.recipeBookComponent.renderTooltip(guiGraphics0, this.f_97735_, this.f_97736_, int1, int2);
        this.xMouse = (float) int1;
        this.yMouse = (float) int2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        int $$4 = this.f_97735_;
        int $$5 = this.f_97736_;
        guiGraphics0.blit(f_97725_, $$4, $$5, 0, 0, this.f_97726_, this.f_97727_);
        renderEntityInInventoryFollowsMouse(guiGraphics0, $$4 + 51, $$5 + 75, 30, (float) ($$4 + 51) - this.xMouse, (float) ($$5 + 75 - 50) - this.yMouse, this.f_96541_.player);
    }

    public static void renderEntityInInventoryFollowsMouse(GuiGraphics guiGraphics0, int int1, int int2, int int3, float float4, float float5, LivingEntity livingEntity6) {
        float $$7 = (float) Math.atan((double) (float4 / 40.0F));
        float $$8 = (float) Math.atan((double) (float5 / 40.0F));
        Quaternionf $$9 = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf $$10 = new Quaternionf().rotateX($$8 * 20.0F * (float) (Math.PI / 180.0));
        $$9.mul($$10);
        float $$11 = livingEntity6.yBodyRot;
        float $$12 = livingEntity6.m_146908_();
        float $$13 = livingEntity6.m_146909_();
        float $$14 = livingEntity6.yHeadRotO;
        float $$15 = livingEntity6.yHeadRot;
        livingEntity6.yBodyRot = 180.0F + $$7 * 20.0F;
        livingEntity6.m_146922_(180.0F + $$7 * 40.0F);
        livingEntity6.m_146926_(-$$8 * 20.0F);
        livingEntity6.yHeadRot = livingEntity6.m_146908_();
        livingEntity6.yHeadRotO = livingEntity6.m_146908_();
        renderEntityInInventory(guiGraphics0, int1, int2, int3, $$9, $$10, livingEntity6);
        livingEntity6.yBodyRot = $$11;
        livingEntity6.m_146922_($$12);
        livingEntity6.m_146926_($$13);
        livingEntity6.yHeadRotO = $$14;
        livingEntity6.yHeadRot = $$15;
    }

    public static void renderEntityInInventory(GuiGraphics guiGraphics0, int int1, int int2, int int3, Quaternionf quaternionf4, @Nullable Quaternionf quaternionf5, LivingEntity livingEntity6) {
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate((double) int1, (double) int2, 50.0);
        guiGraphics0.pose().mulPoseMatrix(new Matrix4f().scaling((float) int3, (float) int3, (float) (-int3)));
        guiGraphics0.pose().mulPose(quaternionf4);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
        if (quaternionf5 != null) {
            quaternionf5.conjugate();
            $$7.overrideCameraOrientation(quaternionf5);
        }
        $$7.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> $$7.render(livingEntity6, 0.0, 0.0, 0.0, 0.0F, 1.0F, guiGraphics0.pose(), guiGraphics0.bufferSource(), 15728880));
        guiGraphics0.flush();
        $$7.setRenderShadow(true);
        guiGraphics0.pose().popPose();
        Lighting.setupFor3DItems();
    }

    @Override
    protected boolean isHovering(int int0, int int1, int int2, int int3, double double4, double double5) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.m_6774_(int0, int1, int2, int3, double4, double5);
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (this.recipeBookComponent.mouseClicked(double0, double1, int2)) {
            this.m_7522_(this.recipeBookComponent);
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() ? false : super.m_6375_(double0, double1, int2);
        }
    }

    @Override
    public boolean mouseReleased(double double0, double double1, int int2) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        } else {
            return super.m_6348_(double0, double1, int2);
        }
    }

    @Override
    protected boolean hasClickedOutside(double double0, double double1, int int2, int int3, int int4) {
        boolean $$5 = double0 < (double) int2 || double1 < (double) int3 || double0 >= (double) (int2 + this.f_97726_) || double1 >= (double) (int3 + this.f_97727_);
        return this.recipeBookComponent.hasClickedOutside(double0, double1, this.f_97735_, this.f_97736_, this.f_97726_, this.f_97727_, int4) && $$5;
    }

    @Override
    protected void slotClicked(Slot slot0, int int1, int int2, ClickType clickType3) {
        super.m_6597_(slot0, int1, int2, clickType3);
        this.recipeBookComponent.slotClicked(slot0);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}