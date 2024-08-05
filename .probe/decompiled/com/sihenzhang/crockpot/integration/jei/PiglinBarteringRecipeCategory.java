package com.sihenzhang.crockpot.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.sihenzhang.crockpot.recipe.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.recipe.RangedItem;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.NbtUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import com.sihenzhang.crockpot.util.StringUtils;
import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class PiglinBarteringRecipeCategory implements IRecipeCategory<PiglinBarteringRecipe> {

    public static final RecipeType<PiglinBarteringRecipe> RECIPE_TYPE = RecipeType.create("crockpot", "piglin_bartering", PiglinBarteringRecipe.class);

    private final IDrawable background;

    private final IDrawable icon;

    public PiglinBarteringRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/piglin_bartering.png"), 0, 0, 176, 112);
        this.icon = guiHelper.createDrawable(ModIntegrationJei.ICONS, 32, 0, 16, 16);
    }

    @Override
    public RecipeType<PiglinBarteringRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createIntegrationComponent("jei", "piglin_bartering");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, PiglinBarteringRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 29, 3).setSlotName("inputSlot").addIngredients(recipe.getIngredient());
        List<ItemStack> weightedOutput = recipe.getWeightedResults().m_146338_().stream().map(e -> NbtUtils.setLoreString(((RangedItem) e.getData()).item.getDefaultInstance(), StringUtils.formatCountAndChance(e, recipe.getWeightedResults().f_146324_))).toList();
        List<List<ItemStack>> pagedItemStacks = JeiUtils.getPagedItemStacks(weightedOutput, focuses, RecipeIngredientRole.OUTPUT, 30);
        for (int i = 0; i < pagedItemStacks.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 85 + i % 5 * 18, 3 + i / 5 * 18).addItemStacks((List<ItemStack>) pagedItemStacks.get(i));
        }
    }

    public void draw(PiglinBarteringRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Piglin piglin = EntityType.PIGLIN.create(Minecraft.getInstance().level);
        piglin.m_34670_(true);
        piglin.m_8061_(EquipmentSlot.MAINHAND, Items.GOLDEN_SWORD.getDefaultInstance());
        recipeSlotsView.findSlotByName("inputSlot").flatMap(slot -> slot.getDisplayedIngredient(VanillaTypes.ITEM_STACK)).ifPresent(inputStack -> {
            piglin.m_8061_(EquipmentSlot.OFFHAND, inputStack);
            piglin.getBrain().setMemory(MemoryModuleType.ADMIRING_ITEM, true);
        });
        boolean emptyInOffhand = piglin.m_21206_().isEmpty();
        if (emptyInOffhand) {
            double yaw = 30.0 - mouseX;
            double pitch = 45.0 - mouseY;
            piglin.f_20883_ = (float) Math.atan(yaw / 40.0) * 20.0F;
            piglin.m_146922_((float) Math.atan(yaw / 40.0) * 40.0F);
            piglin.m_146926_((float) Math.atan(pitch / 40.0) * -20.0F);
            piglin.f_20885_ = piglin.m_146908_();
            piglin.f_20886_ = piglin.m_146908_();
        }
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(emptyInOffhand ? 29.0 : 37.0, 103.0, 50.0);
        stack.scale(-32.0F, 32.0F, 32.0F);
        stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        if (!emptyInOffhand) {
            stack.mulPose(Axis.YN.rotationDegrees(45.0F));
        }
        EntityRenderDispatcher entityRendererDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        MultiBufferSource.BufferSource multiBufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        entityRendererDispatcher.setRenderShadow(false);
        entityRendererDispatcher.render(piglin, 0.0, 0.0, 0.0, 0.0F, 1.0F, stack, multiBufferSource, 15728880);
        entityRendererDispatcher.setRenderShadow(true);
        multiBufferSource.endBatch();
        stack.popPose();
    }
}