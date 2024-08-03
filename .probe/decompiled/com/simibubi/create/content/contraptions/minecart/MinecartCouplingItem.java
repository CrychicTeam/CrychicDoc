package com.simibubi.create.content.contraptions.minecart;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.minecart.capability.CapabilityMinecartController;
import com.simibubi.create.content.contraptions.minecart.capability.MinecartController;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MinecartCouplingItem extends Item {

    public MinecartCouplingItem(Item.Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void handleInteractionWithMinecart(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof AbstractMinecart minecart) {
            Player player = event.getEntity();
            if (player != null) {
                LazyOptional<MinecartController> capability = minecart.getCapability(CapabilityMinecartController.MINECART_CONTROLLER_CAPABILITY);
                if (capability.isPresent()) {
                    MinecartController controller = capability.orElse(null);
                    ItemStack heldItem = player.m_21120_(event.getHand());
                    if (AllItems.MINECART_COUPLING.isIn(heldItem)) {
                        if (!onCouplingInteractOnMinecart(event, minecart, player, controller)) {
                            return;
                        }
                    } else {
                        if (!AllItems.WRENCH.isIn(heldItem)) {
                            return;
                        }
                        if (!onWrenchInteractOnMinecart(event, minecart, player, controller)) {
                            return;
                        }
                    }
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
        }
    }

    protected static boolean onCouplingInteractOnMinecart(PlayerInteractEvent.EntityInteract event, AbstractMinecart minecart, Player player, MinecartController controller) {
        Level world = event.getLevel();
        if (controller.isFullyCoupled()) {
            if (!world.isClientSide) {
                CouplingHandler.status(player, "two_couplings_max");
            }
            return true;
        } else {
            if (world != null && world.isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> cartClicked(player, minecart));
            }
            return true;
        }
    }

    private static boolean onWrenchInteractOnMinecart(PlayerInteractEvent.EntityInteract event, AbstractMinecart minecart, Player player, MinecartController controller) {
        int couplings = (controller.isConnectedToCoupling() ? 1 : 0) + (controller.isLeadingCoupling() ? 1 : 0);
        if (couplings == 0) {
            return false;
        } else if (event.getLevel().isClientSide) {
            return true;
        } else {
            for (boolean forward : Iterate.trueAndFalse) {
                if (controller.hasContraptionCoupling(forward)) {
                    couplings--;
                }
            }
            CouplingHandler.status(player, "removed");
            controller.decouple();
            if (!player.isCreative()) {
                player.getInventory().placeItemBackInInventory(new ItemStack((ItemLike) AllItems.MINECART_COUPLING.get(), couplings));
            }
            return true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void cartClicked(Player player, AbstractMinecart interacted) {
        CouplingHandlerClient.onCartClicked(player, interacted);
    }
}