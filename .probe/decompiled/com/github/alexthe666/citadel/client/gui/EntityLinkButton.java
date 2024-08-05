package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.client.gui.data.EntityLinkData;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EntityLinkButton extends Button {

    private static final Map<String, Entity> renderedEntites = new HashMap();

    private static final Quaternionf ENTITY_ROTATION = new Quaternionf().rotationXYZ((float) Math.toRadians(30.0), (float) Math.toRadians(130.0), (float) Math.PI);

    private final EntityLinkData data;

    private final GuiBasicBook bookGUI;

    public EntityLinkButton(GuiBasicBook bookGUI, EntityLinkData linkData, int k, int l, Button.OnPress o) {
        super(k + linkData.getX() - 12, l + linkData.getY(), (int) (24.0 * linkData.getScale()), (int) (24.0 * linkData.getScale()), CommonComponents.EMPTY, o, f_252438_);
        this.data = linkData;
        this.bookGUI = bookGUI;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int lvt_5_1_ = 0;
        int lvt_6_1_ = 30;
        float f = (float) this.data.getScale();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) this.m_252754_(), (float) this.m_252907_(), 0.0F);
        guiGraphics.pose().scale(f, f, 1.0F);
        this.drawBtn(false, guiGraphics, 0, 0, lvt_5_1_, lvt_6_1_, 24, 24);
        Entity model = null;
        EntityType type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(this.data.getEntity()));
        if (type != null) {
            model = (Entity) renderedEntites.putIfAbsent(this.data.getEntity(), type.create(Minecraft.getInstance().level));
        }
        guiGraphics.enableScissor(this.m_252754_() + Math.round(f * 4.0F), this.m_252907_() + Math.round(f * 4.0F), this.m_252754_() + Math.round(f * 20.0F), this.m_252907_() + Math.round(f * 20.0F));
        if (model != null) {
            model.tickCount = Minecraft.getInstance().player.f_19797_;
            float renderScale = (float) (this.data.getEntityScale() * (double) f * 10.0);
            this.renderEntityInInventory(guiGraphics, 11 + (int) ((double) this.data.getOffset_x() * this.data.getEntityScale()), 22 + (int) ((double) this.data.getOffset_y() * this.data.getEntityScale()), renderScale, ENTITY_ROTATION, model);
        }
        guiGraphics.disableScissor();
        byte var11;
        if (this.f_93622_) {
            this.bookGUI.setEntityTooltip(this.data.getHoverText());
            var11 = 48;
        } else {
            var11 = 24;
        }
        this.drawBtn(!this.f_93622_, guiGraphics, 0, 0, var11, lvt_6_1_, 24, 24);
        guiGraphics.pose().popPose();
    }

    public void drawBtn(boolean color, GuiGraphics guiGraphics, int p_238474_2_, int p_238474_3_, int p_238474_4_, int p_238474_5_, int p_238474_6_, int p_238474_7_) {
        if (color) {
            int widgetColor = this.bookGUI.getWidgetColor();
            int r = (widgetColor & 0xFF0000) >> 16;
            int g = (widgetColor & 0xFF00) >> 8;
            int b = widgetColor & 0xFF;
            BookBlit.blitWithColor(guiGraphics, this.bookGUI.getBookWidgetTexture(), p_238474_2_, p_238474_3_, 0, (float) p_238474_4_, (float) p_238474_5_, p_238474_6_, p_238474_7_, 256, 256, r, g, b, 255);
        } else {
            guiGraphics.blit(this.bookGUI.getBookWidgetTexture(), p_238474_2_, p_238474_3_, 0, (float) p_238474_4_, (float) p_238474_5_, p_238474_6_, p_238474_7_, 256, 256);
        }
    }

    public void renderEntityInInventory(GuiGraphics guiGraphics, int xPos, int yPos, float scale, Quaternionf rotation, Entity entity) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((double) xPos, (double) yPos, 50.0);
        guiGraphics.pose().mulPoseMatrix(new Matrix4f().scaling(scale, scale, -scale));
        guiGraphics.pose().mulPose(rotation);
        Vector3f light0 = new Vector3f(1.0F, -1.0F, -1.0F).normalize();
        Vector3f light1 = new Vector3f(-1.0F, 1.0F, 1.0F).normalize();
        RenderSystem.setShaderLights(light0, light1);
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityrenderdispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, guiGraphics.pose(), guiGraphics.bufferSource(), 15728880));
        guiGraphics.flush();
        entityrenderdispatcher.setRenderShadow(true);
        guiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }
}