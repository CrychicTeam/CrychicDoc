package dev.xkmc.l2backpack.compat;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.RegistrateTagsProvider.IntrinsicImpl;
import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.entry.ISwapEntry;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapManager;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapTypes;
import dev.xkmc.l2backpack.content.remote.common.StorageContainer;
import dev.xkmc.l2backpack.content.remote.worldchest.WorldChestInvWrapper;
import dev.xkmc.l2backpack.content.remote.worldchest.WorldChestItem;
import dev.xkmc.l2backpack.events.ArrowBagEvents;
import dev.xkmc.l2backpack.init.registrate.BackpackBlocks;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.events.event.GolemEquipEvent;
import dev.xkmc.modulargolems.events.event.GolemHandleItemEvent;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class GolemCompat {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(GolemCompat.class);
    }

    private static boolean canEquip(ItemStack stack) {
        if (stack.getItem() instanceof BaseBagItem) {
            return true;
        } else {
            return !(stack.getItem() instanceof WorldChestItem) ? false : stack.hasTag() && stack.getOrCreateTag().contains("owner_id");
        }
    }

    @Nullable
    private static GenericItemStack<WorldChestItem> getBackpack(AbstractGolemEntity<?, ?> golem) {
        for (EquipmentSlot e : List.of(EquipmentSlot.CHEST, EquipmentSlot.OFFHAND)) {
            ItemStack stack = golem.m_6844_(e);
            if (stack.getItem() instanceof WorldChestItem item) {
                return new GenericItemStack<>(item, stack);
            }
        }
        Optional<Pair<ItemStack, PlayerSlot<?>>> opt = CuriosCompat.getSlot(golem, ex -> ex.getItem() instanceof WorldChestItem);
        if (opt.isPresent()) {
            ItemStack stack = (ItemStack) ((Pair) opt.get()).getFirst();
            if (stack.getItem() instanceof WorldChestItem item) {
                return new GenericItemStack<>(item, stack);
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onEquip(GolemEquipEvent event) {
        if (canEquip(event.getStack())) {
            ItemStack back = event.getEntity().m_6844_(EquipmentSlot.CHEST);
            if (!back.isEmpty() && !canEquip(back)) {
                event.setSlot(EquipmentSlot.OFFHAND, 1);
            } else {
                event.setSlot(EquipmentSlot.CHEST, 1);
            }
        }
    }

    @SubscribeEvent
    public static void onHandleItem(GolemHandleItemEvent event) {
        if (!event.getItem().m_213877_()) {
            ItemStack stack = event.getItem().getItem();
            if (!stack.isEmpty()) {
                GenericItemStack<WorldChestItem> backpack = getBackpack(event.getEntity());
                if (backpack != null) {
                    ServerLevel level = (ServerLevel) event.getEntity().m_9236_();
                    Optional<StorageContainer> cont = backpack.item().getContainer(backpack.stack(), level);
                    if (!cont.isEmpty()) {
                        StorageContainer storage = (StorageContainer) cont.get();
                        WorldChestInvWrapper handler = new WorldChestInvWrapper(storage.container, storage.id);
                        ItemStack remain = ItemHandlerHelper.insertItem(handler, stack, false);
                        event.getItem().setItem(remain);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onArrowFind(ArrowBagEvents.ArrowFindEvent event) {
        if (event.getEntity() instanceof AbstractGolemEntity<?, ?> golem) {
            IQuickSwapToken<?> token = QuickSwapManager.getToken(event.getEntity(), event.getStack(), false);
            if (token != null && token.type() == QuickSwapTypes.ARROW) {
                List<? extends ISwapEntry<?>> arrows = (List<? extends ISwapEntry<?>>) token.getList();
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = ((ISwapEntry) arrows.get(i)).getStack();
                    if (event.setProjectile(Pair.of(stack, token::shrink))) {
                        return;
                    }
                }
            }
            GenericItemStack<WorldChestItem> backpack = getBackpack(golem);
            if (backpack != null) {
                if (event.getEntity().m_9236_() instanceof ServerLevel level) {
                    Optional<StorageContainer> cont = backpack.item().getContainer(backpack.stack(), level);
                    if (cont.isEmpty()) {
                        return;
                    }
                    StorageContainer storage = (StorageContainer) cont.get();
                    for (int ix = 0; ix < storage.container.getContainerSize(); ix++) {
                        ItemStack stack = storage.container.getItem(ix);
                        if (event.setProjectile(Pair.of(stack, (IntConsumer) x -> storage.container.setChanged()))) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void genBlockTag(IntrinsicImpl<Block> pvd) {
        pvd.addTag(MGTagGen.POTENTIAL_DST).add((Block) BackpackBlocks.WORLD_CHEST.get());
    }
}