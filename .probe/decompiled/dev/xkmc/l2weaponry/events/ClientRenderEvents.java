package dev.xkmc.l2weaponry.events;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2weaponry.content.item.base.DoubleWieldItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2weaponry", bus = Bus.FORGE)
public class ClientRenderEvents {

    @SubscribeEvent
    public static void renderHandEvent(RenderHandEvent event) {
        LocalPlayer player = Proxy.getClientPlayer();
        Item main = player.m_21205_().getItem();
        Item off = player.m_21206_().getItem();
        if (main instanceof DoubleWieldItem && main == off) {
            if (event.getHand() == InteractionHand.MAIN_HAND) {
                Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderArmWithItem(player, event.getPartialTick(), event.getInterpolatedPitch(), InteractionHand.OFF_HAND, event.getSwingProgress(), player.m_21206_(), event.getEquipProgress(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
            } else {
                event.setCanceled(true);
            }
        }
    }

    public static void onNunchakuUse(Player player, ItemStack stack) {
        if (player == Proxy.getClientPlayer()) {
            MultiPlayerGameMode mode = Minecraft.getInstance().gameMode;
            if (mode != null) {
                float cd = player.getAttackStrengthScale(1.0F);
                if (!(cd < 1.0F)) {
                    if (Minecraft.getInstance().hitResult instanceof EntityHitResult entityResult) {
                        Entity entity = entityResult.getEntity();
                        if (!entity.isAlive()) {
                            return;
                        }
                        if (!(entity instanceof ItemEntity) && !(entity instanceof ExperienceOrb) && !(entity instanceof AbstractArrow)) {
                            mode.attack(player, entity);
                        }
                    }
                }
            }
        }
    }
}