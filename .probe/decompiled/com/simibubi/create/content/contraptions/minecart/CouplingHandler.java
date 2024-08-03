package com.simibubi.create.content.contraptions.minecart;

import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.minecart.capability.CapabilityMinecartController;
import com.simibubi.create.content.contraptions.minecart.capability.MinecartController;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CouplingHandler {

    @SubscribeEvent
    public static void preventEntitiesFromMoutingOccupiedCart(EntityMountEvent event) {
        Entity e = event.getEntityBeingMounted();
        LazyOptional<MinecartController> optional = e.getCapability(CapabilityMinecartController.MINECART_CONTROLLER_CAPABILITY);
        if (optional.isPresent()) {
            if (!(event.getEntityMounting() instanceof AbstractContraptionEntity)) {
                MinecartController controller = optional.orElse(null);
                if (controller.isCoupledThroughContraption()) {
                    event.setCanceled(true);
                    event.setResult(Result.DENY);
                }
            }
        }
    }

    public static void forEachLoadedCoupling(Level world, Consumer<Couple<MinecartController>> consumer) {
        if (world != null) {
            Set<UUID> cartsWithCoupling = CapabilityMinecartController.loadedMinecartsWithCoupling.get(world);
            if (cartsWithCoupling != null) {
                cartsWithCoupling.forEach(id -> {
                    MinecartController controller = CapabilityMinecartController.getIfPresent(world, id);
                    if (controller != null) {
                        if (controller.isLeadingCoupling()) {
                            UUID coupledCart = controller.getCoupledCart(true);
                            MinecartController coupledController = CapabilityMinecartController.getIfPresent(world, coupledCart);
                            if (coupledController != null) {
                                consumer.accept(Couple.create(controller, coupledController));
                            }
                        }
                    }
                });
            }
        }
    }

    public static boolean tryToCoupleCarts(@Nullable Player player, Level world, int cartId1, int cartId2) {
        Entity entity1 = world.getEntity(cartId1);
        Entity entity2 = world.getEntity(cartId2);
        if (!(entity1 instanceof AbstractMinecart)) {
            return false;
        } else if (!(entity2 instanceof AbstractMinecart)) {
            return false;
        } else {
            String tooMany = "two_couplings_max";
            String unloaded = "unloaded";
            String noLoops = "no_loops";
            String tooFar = "too_far";
            int distanceTo = (int) entity1.position().distanceTo(entity2.position());
            boolean contraptionCoupling = player == null;
            if (distanceTo < 2) {
                if (contraptionCoupling) {
                    return false;
                }
                distanceTo = 2;
            }
            if (distanceTo > AllConfigs.server().kinetics.maxCartCouplingLength.get()) {
                status(player, tooFar);
                return false;
            } else {
                AbstractMinecart cart1 = (AbstractMinecart) entity1;
                AbstractMinecart cart2 = (AbstractMinecart) entity2;
                UUID mainID = cart1.m_20148_();
                UUID connectedID = cart2.m_20148_();
                MinecartController mainController = CapabilityMinecartController.getIfPresent(world, mainID);
                MinecartController connectedController = CapabilityMinecartController.getIfPresent(world, connectedID);
                if (mainController == null || connectedController == null) {
                    status(player, unloaded);
                    return false;
                } else if (!mainController.isFullyCoupled() && !connectedController.isFullyCoupled()) {
                    if ((!mainController.isLeadingCoupling() || !mainController.getCoupledCart(true).equals(connectedID)) && (!connectedController.isLeadingCoupling() || !connectedController.getCoupledCart(true).equals(mainID))) {
                        boolean[] var18 = Iterate.trueAndFalse;
                        int var19 = var18.length;
                        int var20 = 0;
                        label83: while (var20 < var19) {
                            boolean main = var18[var20];
                            MinecartController current = main ? mainController : connectedController;
                            boolean forward = current.isLeadingCoupling();
                            int safetyCount = 1000;
                            while (safetyCount-- > 0) {
                                current = getNextInCouplingChain(world, current, forward);
                                if (current == null) {
                                    status(player, unloaded);
                                    return false;
                                }
                                if (current == connectedController) {
                                    status(player, noLoops);
                                    return false;
                                }
                                if (current == MinecartController.EMPTY) {
                                    var20++;
                                    continue label83;
                                }
                            }
                            Create.LOGGER.warn("Infinite loop in coupling iteration");
                            return false;
                        }
                        if (!contraptionCoupling) {
                            for (InteractionHand hand : InteractionHand.values()) {
                                if (player.isCreative()) {
                                    break;
                                }
                                ItemStack heldItem = player.m_21120_(hand);
                                if (AllItems.MINECART_COUPLING.isIn(heldItem)) {
                                    heldItem.shrink(1);
                                    break;
                                }
                            }
                        }
                        mainController.prepareForCoupling(true);
                        connectedController.prepareForCoupling(false);
                        mainController.coupleWith(true, connectedID, (float) distanceTo, contraptionCoupling);
                        connectedController.coupleWith(false, mainID, (float) distanceTo, contraptionCoupling);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    status(player, tooMany);
                    return false;
                }
            }
        }
    }

    @Nullable
    public static MinecartController getNextInCouplingChain(Level world, MinecartController controller, boolean forward) {
        UUID coupledCart = controller.getCoupledCart(forward);
        return coupledCart == null ? MinecartController.empty() : CapabilityMinecartController.getIfPresent(world, coupledCart);
    }

    public static void status(Player player, String key) {
        if (player != null) {
            player.displayClientMessage(Lang.translateDirect("minecart_coupling." + key), true);
        }
    }
}