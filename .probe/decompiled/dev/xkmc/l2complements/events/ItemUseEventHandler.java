package dev.xkmc.l2complements.events;

import dev.xkmc.l2complements.network.EmptyRightClickToServer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "l2complements", bus = Bus.FORGE)
public class ItemUseEventHandler {

    public static final List<ItemUseEventHandler.ItemClickHandler> LIST = new ArrayList();

    public static <T extends PlayerEvent> void execute(ItemStack stack, T event, ItemUseEventHandler.TriCon<T> cons) {
        if (stack.getItem() instanceof ItemUseEventHandler.ItemClickHandler && ((ItemUseEventHandler.ItemClickHandler) stack.getItem()).predicate(stack, event.getClass(), event)) {
            cons.accept((ItemUseEventHandler.ItemClickHandler) stack.getItem(), stack, event);
        }
        for (ItemUseEventHandler.ItemClickHandler handler : LIST) {
            if (handler.predicate(stack, event.getClass(), event)) {
                cons.accept(handler, stack, event);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getEntity().m_9236_().isClientSide()) {
            new EmptyRightClickToServer(false, event.getHand() == InteractionHand.MAIN_HAND).toServer();
        }
        execute(event.getItemStack(), event, ItemUseEventHandler.ItemClickHandler::onPlayerLeftClickEmpty);
    }

    @SubscribeEvent
    public static void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        execute(event.getItemStack(), event, ItemUseEventHandler.ItemClickHandler::onPlayerLeftClickBlock);
    }

    @SubscribeEvent
    public static void onPlayerLeftClickEntity(AttackEntityEvent event) {
        execute(event.getEntity().m_21205_(), event, ItemUseEventHandler.ItemClickHandler::onPlayerLeftClickEntity);
    }

    @SubscribeEvent
    public static void onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (event.getEntity().m_9236_().isClientSide()) {
            new EmptyRightClickToServer(true, event.getHand() == InteractionHand.MAIN_HAND).toServer();
        }
        execute(event.getEntity().m_21120_(event.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND), event, ItemUseEventHandler.ItemClickHandler::onPlayerRightClickEmpty);
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        execute(event.getItemStack(), event, ItemUseEventHandler.ItemClickHandler::onPlayerRightClickBlock);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        execute(event.getItemStack(), event, ItemUseEventHandler.ItemClickHandler::onPlayerRightClickEntity);
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        execute(event.getEntity().m_21205_(), event, ItemUseEventHandler.ItemClickHandler::onCriticalHit);
    }

    public interface ItemClickHandler {

        boolean predicate(ItemStack var1, Class<? extends PlayerEvent> var2, PlayerEvent var3);

        default void onPlayerLeftClickEmpty(ItemStack stack, PlayerInteractEvent.LeftClickEmpty event) {
        }

        default void onPlayerLeftClickBlock(ItemStack stack, PlayerInteractEvent.LeftClickBlock event) {
        }

        default void onPlayerLeftClickEntity(ItemStack stack, AttackEntityEvent event) {
        }

        default void onCriticalHit(ItemStack stack, CriticalHitEvent event) {
        }

        default void onPlayerRightClickEmpty(ItemStack stack, PlayerInteractEvent.RightClickEmpty event) {
        }

        default void onPlayerRightClickBlock(ItemStack stack, PlayerInteractEvent.RightClickBlock event) {
        }

        default void onPlayerRightClickEntity(ItemStack stack, PlayerInteractEvent.EntityInteract event) {
        }
    }

    @FunctionalInterface
    public interface TriCon<T> {

        void accept(ItemUseEventHandler.ItemClickHandler var1, ItemStack var2, T var3);
    }
}