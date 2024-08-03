package com.nameless.indestructible.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nameless.indestructible.client.UIConfig;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Matrix4f;
import yesman.epicfight.client.gui.EntityIndicator;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.config.ClientConfig;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.effect.VisibleMobEffect;

public class StatusIndicator extends EntityIndicator {

    public static final ResourceLocation STATUS_BAR = new ResourceLocation("indestructible", "textures/gui/bar.png");

    @Override
    public boolean shouldDraw(LivingEntity entityIn, @Nullable LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch) {
        ClientConfig.HealthBarShowOptions option = EpicFightMod.CLIENT_CONFIGS.healthBarShowOption.getValue();
        Minecraft mc = Minecraft.getInstance();
        if (!UIConfig.REPLACE_UI.get() || option == ClientConfig.HealthBarShowOptions.NONE) {
            return false;
        } else if (entityIn.canChangeDimensions() && !entityIn.m_20145_() && entityIn != playerpatch.getOriginal().m_20202_()) {
            if (entityIn.m_20280_(mc.getCameraEntity()) >= 400.0) {
                return false;
            } else {
                if (entityIn instanceof Player playerIn) {
                    if (playerIn == playerpatch.getOriginal() && playerpatch.getMaxStunShield() <= 0.0F) {
                        return false;
                    }
                    if (playerIn.isCreative() || playerIn.isSpectator()) {
                        return false;
                    }
                }
                return option == ClientConfig.HealthBarShowOptions.TARGET ? playerpatch.getTarget() == entityIn : (!entityIn.getActiveEffects().isEmpty() || entityIn.getHealth() < entityIn.getMaxHealth() || entitypatch instanceof AdvancedCustomHumanoidMobPatch<?> AHPatch && AHPatch.getStamina() < AHPatch.getMaxStamina()) && entityIn.deathTime < 19;
            }
        } else {
            return false;
        }
    }

    @Override
    public void drawIndicator(LivingEntity entityIn, @Nullable LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch, PoseStack matStackIn, MultiBufferSource bufferIn, float partialTicks) {
        boolean adjustUI = entitypatch instanceof AdvancedCustomHumanoidMobPatch && UIConfig.REPLACE_UI.get();
        float height = adjustUI ? 0.45F : 0.25F;
        Matrix4f mvMatrix = super.getMVMatrix(matStackIn, entityIn, 0.0F, entityIn.m_20206_() + height, 0.0F, true, partialTicks);
        Collection<MobEffectInstance> activeEffects = entityIn.getActiveEffects();
        if (!activeEffects.isEmpty() && !entityIn.m_7306_(playerpatch.getOriginal())) {
            Iterator<MobEffectInstance> iter = activeEffects.iterator();
            int acives = activeEffects.size();
            int row = acives > 1 ? 1 : 0;
            int column = (acives - 1) / 2;
            float startX = -0.8F + -0.3F * (float) row;
            float startY = -0.15F + 0.15F * (float) column;
            for (int i = 0; i <= column; i++) {
                for (int j = 0; j <= row; j++) {
                    MobEffectInstance effectInstance = (MobEffectInstance) iter.next();
                    MobEffect effect = effectInstance.getEffect();
                    ResourceLocation rl;
                    if (effect instanceof VisibleMobEffect visibleMobEffect) {
                        rl = visibleMobEffect.getIcon(effectInstance);
                    } else {
                        rl = new ResourceLocation(ForgeRegistries.MOB_EFFECTS.getKey(effect).getNamespace(), "textures/mob_effect/" + ForgeRegistries.MOB_EFFECTS.getKey(effect).getPath() + ".png");
                    }
                    Minecraft.getInstance().getTextureManager().bindForSetup(rl);
                    float x = startX + 0.3F * (float) j;
                    float y = startY + -0.3F * (float) i;
                    VertexConsumer vertexBuilder1 = bufferIn.getBuffer(EpicFightRenderTypes.entityIndicator(rl));
                    this.drawTexturedModalRect2DPlane(mvMatrix, vertexBuilder1, x, y, x + 0.3F, y + 0.3F, 0.0F, 0.0F, 256.0F, 256.0F);
                    if (!iter.hasNext()) {
                        break;
                    }
                }
            }
        }
        VertexConsumer vertexBuilder = bufferIn.getBuffer(EpicFightRenderTypes.entityIndicator(STATUS_BAR));
        float ratio = Mth.clamp(entityIn.getHealth() / entityIn.getMaxHealth(), 0.0F, 1.0F);
        float healthRatio = -0.5F + ratio;
        int textureRatio = (int) (65.0F * ratio);
        this.drawTexturedModalRect2DPlane(mvMatrix, vertexBuilder, -0.5F, -0.05F, healthRatio, 0.08F, 0.0F, 26.0F, (float) textureRatio, 35.0F);
        this.drawTexturedModalRect2DPlane(mvMatrix, vertexBuilder, healthRatio, -0.05F, 0.5F, 0.08F, (float) textureRatio, 16.0F, 65.0F, 25.0F);
        if (entitypatch instanceof AdvancedCustomHumanoidMobPatch<?> achpatch) {
            this.renderStamina(achpatch, mvMatrix, vertexBuilder);
        }
    }

    private void renderStamina(AdvancedCustomHumanoidMobPatch<?> achpatch, Matrix4f mvMatrix, VertexConsumer vertexBuilder) {
        float ratio = Mth.clamp(achpatch.getStamina() / achpatch.getMaxStamina(), 0.0F, 1.0F);
        float barRatio = -0.5F + ratio;
        int textureRatio = (int) (63.0F * ratio);
        this.drawTexturedModalRect2DPlane(mvMatrix, vertexBuilder, -0.5F, -0.15F, barRatio, -0.05F, 0.0F, 8.0F, (float) textureRatio, 15.0F);
        this.drawTexturedModalRect2DPlane(mvMatrix, vertexBuilder, barRatio, -0.15F, 0.5F, -0.05F, (float) textureRatio, 0.0F, 63.0F, 7.0F);
    }
}