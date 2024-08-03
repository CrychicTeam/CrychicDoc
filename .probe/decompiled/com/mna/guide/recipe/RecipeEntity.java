package com.mna.guide.recipe;

import com.mna.ManaAndArtifice;
import com.mna.api.guidebook.RecipeRendererBase;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Quaternionf;

public class RecipeEntity extends RecipeRendererBase {

    private EntityType<?> _entity;

    private LivingEntity dummyEntity;

    public RecipeEntity(int xIn, int yIn) {
        super(xIn, yIn);
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.dummyEntity != null) {
            int scale = (int) (this.dummyEntity.m_20191_().getXsize() * 100.0);
            float bbHeight = Math.max((float) ((int) this.dummyEntity.m_20191_().getYsize()), 0.15F);
            Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI).rotateY((float) Math.toRadians(135.0));
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
            InventoryScreen.renderEntityInInventory(pGuiGraphics, (int) ((float) this.m_252754_() / this.scale + (float) (this.f_93618_ / 2) - 25.0F), (int) ((float) this.m_252907_() / this.scale + (float) (this.f_93619_ / 2) + (float) this.f_93619_ * bbHeight / 4.0F), scale, quaternionf, null, this.dummyEntity);
            pGuiGraphics.pose().popPose();
        }
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return null;
    }

    @Override
    protected void init_internal(ResourceLocation recipeLocation) {
        this._entity = ForgeRegistries.ENTITY_TYPES.getValue(recipeLocation);
        if (this._entity != null) {
            Entity e = this._entity.create(ManaAndArtifice.instance.proxy.getClientWorld());
            if (e instanceof LivingEntity) {
                this.dummyEntity = (LivingEntity) e;
            } else {
                ManaAndArtifice.LOGGER.warn("Passed in entity registration must be an instance of LivingEntity. " + recipeLocation.toString() + " is not!  This will not render in the codex.");
            }
        }
    }

    @Override
    public int getTier() {
        return 0;
    }
}