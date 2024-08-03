package com.rekindled.embers.augment;

import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.network.PacketHandler;
import com.rekindled.embers.network.message.MessageCasterOrb;
import com.rekindled.embers.util.EmberInventoryUtil;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CasterOrbAugment extends AugmentBase {

    public static float prevCooledStrength = 0.0F;

    public static float cooldownTicks = 0.0F;

    public static HashMap<UUID, Float> cooldownTicksServer = new HashMap();

    public CasterOrbAugment(ResourceLocation id) {
        super(id, 2.0);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void setCooldown(UUID uuid, float ticks) {
        cooldownTicksServer.put(uuid, ticks);
    }

    public static boolean hasCooldown(UUID uuid) {
        return (Float) cooldownTicksServer.getOrDefault(uuid, 0.0F) > 0.0F;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (UUID uuid : cooldownTicksServer.keySet()) {
                Float ticks = (Float) cooldownTicksServer.get(uuid) - 1.0F;
                cooldownTicksServer.put(uuid, ticks);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                prevCooledStrength = mc.player.m_36403_(0.0F);
            }
            if (cooldownTicks > 0.0F) {
                cooldownTicks--;
            }
        }
    }

    @SubscribeEvent
    public void onSwing(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        Level world = event.getLevel();
        ItemStack heldStack = event.getItemStack();
        this.tryShoot(player, world, heldStack);
    }

    @SubscribeEvent
    public void onSwing(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        Level world = event.getLevel();
        ItemStack heldStack = event.getItemStack();
        this.tryShoot(player, world, heldStack);
    }

    private void tryShoot(Player player, Level world, ItemStack heldStack) {
        if (prevCooledStrength == 1.0F && AugmentUtil.hasHeat(heldStack)) {
            int level = AugmentUtil.getAugmentLevel(heldStack, this);
            if (world.isClientSide() && level > 0 && EmberInventoryUtil.getEmberTotal(player) > this.cost && cooldownTicks == 0.0F) {
                PacketHandler.INSTANCE.sendToServer(new MessageCasterOrb(player.m_20154_().x, player.m_20154_().y, player.m_20154_().z));
                cooldownTicks = 20.0F;
            }
        }
    }
}