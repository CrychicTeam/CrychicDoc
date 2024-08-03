package com.simibubi.create.content.equipment.armor;

import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({ Dist.CLIENT })
public class NetheriteBacktankFirstPersonRenderer {

    private static final ResourceLocation BACKTANK_ARMOR_LOCATION = Create.asResource("textures/models/armor/netherite_diving_arm.png");

    private static boolean rendererActive = false;

    public static void clientTick() {
        Minecraft mc = Minecraft.getInstance();
        rendererActive = mc.player != null && AllItems.NETHERITE_BACKTANK.isIn(mc.player.m_6844_(EquipmentSlot.CHEST));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderPlayerHand(RenderArmEvent event) {
        if (rendererActive) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            MultiBufferSource buffer = event.getMultiBufferSource();
            if (mc.getEntityRenderDispatcher().getRenderer(player) instanceof PlayerRenderer pr) {
                PlayerModel<AbstractClientPlayer> model = (PlayerModel<AbstractClientPlayer>) pr.m_7200_();
                model.f_102608_ = 0.0F;
                model.f_102817_ = false;
                model.f_102818_ = 0.0F;
                model.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                ModelPart armPart = event.getArm() == HumanoidArm.LEFT ? model.leftSleeve : model.rightSleeve;
                armPart.xRot = 0.0F;
                armPart.render(event.getPoseStack(), buffer.getBuffer(RenderType.entitySolid(BACKTANK_ARMOR_LOCATION)), 15728880, OverlayTexture.NO_OVERLAY);
                event.setCanceled(true);
            }
        }
    }
}