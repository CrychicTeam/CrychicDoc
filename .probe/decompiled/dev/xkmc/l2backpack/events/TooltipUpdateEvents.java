package dev.xkmc.l2backpack.events;

import dev.xkmc.l2backpack.content.drawer.BaseDrawerItem;
import dev.xkmc.l2backpack.content.remote.drawer.EnderDrawerBlockEntity;
import dev.xkmc.l2backpack.content.remote.drawer.EnderDrawerItem;
import dev.xkmc.l2backpack.content.remote.player.EnderSyncCap;
import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2backpack.network.RequestTooltipUpdateEvent;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2backpack", bus = Bus.FORGE)
public class TooltipUpdateEvents {

    private static final int MAX_COOLDOWN = 5;

    private static TooltipUpdateEvents.Step step = TooltipUpdateEvents.Step.NONE;

    private static UUID id = null;

    private static Item focus = null;

    private static int count = 0;

    private static int cooldown = 0;

    @OnlyIn(Dist.CLIENT)
    public static void onEnderSync(int slot, ItemStack stack) {
        EnderSyncCap.HOLDER.get(Proxy.getClientPlayer()).setItem(slot, stack);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!continueSession()) {
                endSession();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean continueSession() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof AbstractContainerScreen<?> cont) {
            return screenSession(cont);
        } else {
            return screen == null ? blockSession() : false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean screenSession(AbstractContainerScreen<?> cont) {
        Slot slot = cont.getSlotUnderMouse();
        if (slot == null) {
            return false;
        } else {
            ItemStack stack = slot.getItem();
            if (!(stack.getItem() instanceof EnderDrawerItem)) {
                return false;
            } else if (BaseDrawerItem.getItem(stack) == Items.AIR) {
                return false;
            } else {
                startSession(BaseDrawerItem.getItem(stack), stack.getOrCreateTag().getUUID("owner_id"));
                return true;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean blockSession() {
        LocalPlayer player = Proxy.getClientPlayer();
        BlockHitResult ray = RayTraceUtil.rayTraceBlock(player.m_9236_(), player, player.getBlockReach());
        if (ray.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ray.getBlockPos();
            if (player.m_9236_().getBlockEntity(pos) instanceof EnderDrawerBlockEntity drawer) {
                startSession(drawer.item, drawer.owner_id);
                return true;
            }
        }
        return false;
    }

    private static void endSession() {
        step = TooltipUpdateEvents.Step.NONE;
        focus = null;
        count = 0;
        id = null;
    }

    @OnlyIn(Dist.CLIENT)
    private static void startSession(Item content, UUID owner) {
        if (step == TooltipUpdateEvents.Step.NONE) {
            focus = content;
            id = owner;
            step = TooltipUpdateEvents.Step.SENT;
            L2Backpack.HANDLER.toServer(new RequestTooltipUpdateEvent(focus, Proxy.getClientPlayer().m_20148_()));
        } else if (step == TooltipUpdateEvents.Step.COOLDOWN) {
            if (cooldown > 0) {
                cooldown--;
            }
            if (cooldown <= 0) {
                cooldown = 0;
                step = TooltipUpdateEvents.Step.NONE;
            }
        }
    }

    public static void updateInfo(Item item, UUID uuid, int val) {
        if (focus == item) {
            if (step == TooltipUpdateEvents.Step.SENT) {
                count = val;
                id = uuid;
                step = TooltipUpdateEvents.Step.COOLDOWN;
                cooldown = 5;
            }
        }
    }

    public static int getCount(UUID uuid, Item item) {
        return id != null && focus != null && id.equals(uuid) && item == focus ? count : -1;
    }

    private static enum Step {

        NONE, SENT, COOLDOWN
    }
}