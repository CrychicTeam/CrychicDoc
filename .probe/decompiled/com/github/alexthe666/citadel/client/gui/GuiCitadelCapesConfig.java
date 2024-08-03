package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.ClientProxy;
import com.github.alexthe666.citadel.client.rewards.CitadelCapes;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class GuiCitadelCapesConfig extends OptionsSubScreen {

    @Nullable
    private String capeType;

    private Button button;

    public GuiCitadelCapesConfig(Screen parentScreenIn, Options gameSettingsIn) {
        super(parentScreenIn, gameSettingsIn, Component.translatable("citadel.gui.capes"));
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
        this.capeType = tag.contains("CitadelCapeType") && !tag.getString("CitadelCapeType").isEmpty() ? tag.getString("CitadelCapeType") : null;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        guiGraphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 20, 16777215);
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        int i = this.f_96543_ / 2;
        int j = this.f_96544_ / 6;
        guiGraphics.pose().pushPose();
        ClientProxy.hideFollower = true;
        renderBackwardsEntity(i, j + 144, 60, 0.0F, 0.0F, Minecraft.getInstance().player);
        ClientProxy.hideFollower = false;
        guiGraphics.pose().popPose();
    }

    public static void renderBackwardsEntity(int x, int y, int size, float angleXComponent, float angleYComponent, LivingEntity entity) {
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double) x, (double) y, 1050.0);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0, 0.0, 1000.0);
        posestack1.scale((float) size, (float) size, (float) size);
        Quaternionf quaternion = Axis.ZP.rotationDegrees(180.0F);
        Quaternionf quaternion1 = Axis.XP.rotationDegrees(angleYComponent * 20.0F);
        quaternion.mul(quaternion1);
        quaternion.mul(Axis.YP.rotationDegrees(180.0F));
        posestack1.mulPose(quaternion);
        float f2 = entity.yBodyRot;
        float f3 = entity.m_146908_();
        float f4 = entity.m_146909_();
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        entity.yBodyRot = 180.0F + angleXComponent * 20.0F;
        entity.m_146922_(180.0F + angleXComponent * 40.0F);
        entity.m_146926_(-angleYComponent * 20.0F);
        entity.yHeadRot = entity.m_146908_();
        entity.yHeadRotO = entity.m_146908_();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conjugate();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880));
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        entity.yBodyRot = f2;
        entity.m_146922_(f3);
        entity.m_146926_(f4);
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    @Override
    protected void init() {
        super.m_7856_();
        int i = this.f_96543_ / 2;
        int j = this.f_96544_ / 6;
        Button doneButton = Button.builder(CommonComponents.GUI_DONE, p_213079_1_ -> this.f_96541_.setScreen(this.f_96281_)).size(200, 20).pos(i - 100, j + 160).build();
        this.m_142416_(doneButton);
        this.button = Button.builder(this.getTypeText(), p_213079_1_ -> {
            CitadelCapes.Cape nextCape = CitadelCapes.getNextCape(this.capeType, Minecraft.getInstance().player.m_20148_());
            this.capeType = nextCape == null ? null : nextCape.getIdentifier();
            CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
            if (tag != null) {
                if (this.capeType == null) {
                    tag.putString("CitadelCapeType", "");
                    tag.putBoolean("CitadelCapeDisabled", true);
                } else {
                    tag.putString("CitadelCapeType", this.capeType);
                    tag.putBoolean("CitadelCapeDisabled", false);
                }
                CitadelEntityData.setCitadelTag(Minecraft.getInstance().player, tag);
            }
            Citadel.sendMSGToServer(new PropertiesMessage("CitadelTagUpdate", tag, Minecraft.getInstance().player.m_19879_()));
            this.button.m_93666_(this.getTypeText());
        }).size(200, 20).pos(i - 100, j).build();
        this.m_142416_(this.button);
    }

    private Component getTypeText() {
        Component suffix;
        if (this.capeType == null) {
            suffix = Component.translatable("citadel.gui.no_cape");
        } else {
            CitadelCapes.Cape cape = CitadelCapes.getById(this.capeType);
            if (cape == null) {
                suffix = Component.translatable("citadel.gui.no_cape");
            } else {
                suffix = Component.translatable("cape." + cape.getIdentifier());
            }
        }
        return Component.translatable("citadel.gui.cape_type").append(" ").append(suffix);
    }
}