package com.github.alexmodguy.alexscaves.client.gui.book.widget;

import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexthe666.citadel.recipe.SpecialRecipeInGuideBook;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class CraftingRecipeWidget extends BookWidget {

    @Expose
    @SerializedName("recipe_id")
    private String recipeId;

    @Expose
    private boolean sepia;

    @Expose(serialize = false, deserialize = false)
    private Recipe recipe;

    private static final int GRID_TEXTURE_SIZE = 64;

    @Expose(serialize = false, deserialize = false)
    private boolean smelting = false;

    private static final ResourceLocation CRAFTING_GRID_TEXTURE = new ResourceLocation("alexscaves", "textures/gui/book/crafting_grid.png");

    private static final ResourceLocation SMELTING_GRID_TEXTURE = new ResourceLocation("alexscaves", "textures/gui/book/smelting_grid.png");

    public CraftingRecipeWidget(int displayPage, String recipeId, boolean sepia, int x, int y, float scale) {
        super(displayPage, BookWidget.Type.CRAFTING_RECIPE, x, y, scale);
        this.recipeId = recipeId;
        this.sepia = sepia;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float partialTicks, boolean onFlippingPage) {
        if (this.recipe == null && this.recipeId != null) {
            this.recipe = this.getRecipeByName(this.recipeId);
            if (this.recipe instanceof AbstractCookingRecipe) {
                this.smelting = true;
            }
        }
        if (this.recipe != null) {
            float itemScale = 16.0F;
            float playerTicks = (float) Minecraft.getInstance().player.f_19797_;
            VertexConsumer vertexconsumer = bufferSource.getBuffer(ACRenderTypes.getBookWidget(this.smelting ? SMELTING_GRID_TEXTURE : CRAFTING_GRID_TEXTURE, this.sepia));
            poseStack.pushPose();
            poseStack.translate((float) this.getX(), (float) this.getY(), 0.0F);
            poseStack.scale(this.getScale(), this.getScale(), 1.0F);
            poseStack.pushPose();
            poseStack.scale(1.5F, 1.5F, 1.0F);
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            float scaledU1 = 0.859375F;
            float scaledV1 = 0.578125F;
            float texWidth = 27.5F;
            float texHeight = 18.5F;
            vertexconsumer.vertex(matrix4f, -texWidth, -texHeight, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, texWidth, -texHeight, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(scaledU1, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(0.0F, scaledV1).endVertex();
            vertexconsumer.vertex(matrix4f, texWidth, texHeight, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(scaledU1, scaledV1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(0.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, -texWidth, texHeight, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0.0F, scaledV1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(scaledU1, 0.0F).endVertex();
            poseStack.popPose();
            if (this.smelting) {
                poseStack.pushPose();
                poseStack.translate(43.0F, -15.0F, 0.0F);
                poseStack.scale(1.35F, 1.35F, 1.0F);
                ItemWidget.renderItem(this.recipe.getResultItem(Minecraft.getInstance().level.m_9598_()), poseStack, bufferSource, this.sepia, itemScale * 1.25F);
                poseStack.popPose();
                Ingredient ing = this.recipe.getIngredients().get(0);
                ItemStack stack = ItemStack.EMPTY;
                if (!ing.isEmpty()) {
                    if (ing.getItems().length > 1) {
                        int currentIndex = (int) (playerTicks / 20.0F % (float) ing.getItems().length);
                        stack = ing.getItems()[currentIndex];
                    } else {
                        stack = ing.getItems()[0];
                    }
                }
                poseStack.pushPose();
                poseStack.translate(-27.5F, -12.5F, 0.0F);
                ItemWidget.renderItem(stack, poseStack, bufferSource, this.sepia, itemScale);
                poseStack.popPose();
            } else {
                poseStack.pushPose();
                poseStack.translate(57.0F, 2.0F, 0.0F);
                poseStack.scale(1.35F, 1.35F, 1.0F);
                ItemWidget.renderItem(this.recipe.getResultItem(Minecraft.getInstance().level.m_9598_()), poseStack, bufferSource, this.sepia, itemScale * 1.25F);
                poseStack.popPose();
                NonNullList<Ingredient> ingredients = this.recipe instanceof SpecialRecipeInGuideBook ? ((SpecialRecipeInGuideBook) this.recipe).getDisplayIngredients() : this.recipe.getIngredients();
                NonNullList<ItemStack> displayedStacks = NonNullList.create();
                int width = 3;
                int height = 3;
                if (this.recipe instanceof ShapedRecipe shapedRecipe) {
                    width = shapedRecipe.getWidth();
                    height = shapedRecipe.getHeight();
                }
                int renderY = 0;
                int renderX = 0;
                for (int i = 0; i < ingredients.size(); i++) {
                    Ingredient ing = ingredients.get(i);
                    ItemStack stack = ItemStack.EMPTY;
                    if (!ing.isEmpty()) {
                        if (ing.getItems().length > 1) {
                            int currentIndex = (int) (playerTicks / 20.0F % (float) ing.getItems().length);
                            stack = ing.getItems()[currentIndex];
                        } else {
                            stack = ing.getItems()[0];
                        }
                    }
                    if (i % width == 0) {
                        if (i != 0) {
                            renderY++;
                        }
                        renderX = 0;
                    } else {
                        renderX++;
                    }
                    if (!stack.isEmpty()) {
                        poseStack.pushPose();
                        poseStack.translate(-33.0F + (float) renderX * 18.75F, -18.5F + (float) renderY * 19.5F, 0.0F);
                        ItemWidget.renderItem(stack, poseStack, bufferSource, this.sepia, itemScale);
                        poseStack.popPose();
                    }
                    displayedStacks.add(i, stack);
                }
            }
            poseStack.popPose();
        }
    }

    private Recipe getRecipeByName(String registryName) {
        try {
            RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
            if (manager.byKey(new ResourceLocation(registryName)).isPresent()) {
                return (Recipe) manager.byKey(new ResourceLocation(registryName)).get();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        return null;
    }
}