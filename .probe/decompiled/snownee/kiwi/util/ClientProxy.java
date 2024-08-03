package snownee.kiwi.util;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.Nullable;

public class ClientProxy {

    public static void registerColors(ClientProxy.Context context, List<Pair<Block, BlockColor>> blocksToAdd, List<Pair<Item, ItemColor>> itemsToAdd) {
        IEventBus modEventBus = context.modEventBus();
        if (!blocksToAdd.isEmpty()) {
            modEventBus.addListener(event -> {
                for (Pair<Block, BlockColor> pair : blocksToAdd) {
                    event.register((BlockColor) pair.getSecond(), (Block) pair.getFirst());
                }
            });
        }
        if (!itemsToAdd.isEmpty()) {
            modEventBus.addListener(event -> {
                for (Pair<Item, ItemColor> pair : itemsToAdd) {
                    event.register((ItemColor) pair.getSecond(), (ItemLike) pair.getFirst());
                }
            });
        }
    }

    public static void pushScreen(Minecraft mc, Screen screen) {
        mc.pushGuiLayer(screen);
    }

    @Nullable
    public static Slot getSlotUnderMouse(AbstractContainerScreen<?> containerScreen) {
        return containerScreen.getSlotUnderMouse();
    }

    public static void afterRegisterSmartKey(SmartKey smartKey) {
        Preconditions.checkNotNull(smartKey);
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.addListener(event -> {
            if (event.phase == TickEvent.Phase.END) {
                smartKey.tick();
            }
        });
        forgeEventBus.addListener(event -> {
            if (smartKey.m_90830_(event.getButton()) && smartKey.setDownWithResult(true)) {
                event.setCanceled(true);
            }
        });
        forgeEventBus.addListener(event -> {
            if (smartKey.m_90830_(event.getButton()) && smartKey.setDownWithResult(false)) {
                event.setCanceled(true);
            }
        });
        forgeEventBus.addListener(event -> {
            if (smartKey.m_90832_(event.getKeyCode(), event.getScanCode()) && smartKey.setDownWithResult(true)) {
                event.setCanceled(true);
            }
        });
        forgeEventBus.addListener(event -> {
            if (smartKey.m_90832_(event.getKeyCode(), event.getScanCode()) && smartKey.setDownWithResult(false)) {
                event.setCanceled(true);
            }
        });
    }

    public static record Context(boolean loading, IEventBus modEventBus) {
    }
}