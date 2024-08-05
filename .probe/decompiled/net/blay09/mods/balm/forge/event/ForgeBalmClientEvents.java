package net.blay09.mods.balm.forge.event;

import net.blay09.mods.balm.api.event.TickPhase;
import net.blay09.mods.balm.api.event.TickType;
import net.blay09.mods.balm.api.event.client.BlockHighlightDrawEvent;
import net.blay09.mods.balm.api.event.client.ClientStartedEvent;
import net.blay09.mods.balm.api.event.client.ConnectedToServerEvent;
import net.blay09.mods.balm.api.event.client.DisconnectedFromServerEvent;
import net.blay09.mods.balm.api.event.client.FovUpdateEvent;
import net.blay09.mods.balm.api.event.client.GuiDrawEvent;
import net.blay09.mods.balm.api.event.client.ItemTooltipEvent;
import net.blay09.mods.balm.api.event.client.KeyInputEvent;
import net.blay09.mods.balm.api.event.client.OpenScreenEvent;
import net.blay09.mods.balm.api.event.client.RecipesUpdatedEvent;
import net.blay09.mods.balm.api.event.client.RenderHandEvent;
import net.blay09.mods.balm.api.event.client.UseItemInputEvent;
import net.blay09.mods.balm.api.event.client.screen.ContainerScreenDrawEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenDrawEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenInitEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenKeyEvent;
import net.blay09.mods.balm.api.event.client.screen.ScreenMouseEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

public class ForgeBalmClientEvents {

    public static void registerEvents(ForgeBalmEvents events) {
        events.registerTickEvent(TickType.Client, TickPhase.Start, handler -> MinecraftForge.EVENT_BUS.addListener(orig -> {
            if (orig.phase == TickEvent.Phase.START) {
                handler.handle(Minecraft.getInstance());
            }
        }));
        events.registerTickEvent(TickType.Client, TickPhase.End, handler -> MinecraftForge.EVENT_BUS.addListener(orig -> {
            if (orig.phase == TickEvent.Phase.END) {
                handler.handle(Minecraft.getInstance());
            }
        }));
        events.registerTickEvent(TickType.ClientLevel, TickPhase.Start, handler -> MinecraftForge.EVENT_BUS.addListener(orig -> {
            if (orig.phase == TickEvent.Phase.START) {
                handler.handle(Minecraft.getInstance().level);
            }
        }));
        events.registerTickEvent(TickType.ClientLevel, TickPhase.End, handler -> MinecraftForge.EVENT_BUS.addListener(orig -> {
            if (orig.phase == TickEvent.Phase.END) {
                handler.handle(Minecraft.getInstance().level);
            }
        }));
        events.registerEvent(ClientStartedEvent.class, priority -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeBalmEvents.toForge(priority), orig -> orig.enqueueWork(() -> {
            ClientStartedEvent event = new ClientStartedEvent(Minecraft.getInstance());
            events.fireEventHandlers(priority, event);
        })));
        events.registerEvent(ConnectedToServerEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ConnectedToServerEvent event = new ConnectedToServerEvent(Minecraft.getInstance());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(DisconnectedFromServerEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            DisconnectedFromServerEvent event = new DisconnectedFromServerEvent(Minecraft.getInstance());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(ScreenInitEvent.Pre.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenInitEvent.Pre event = new ScreenInitEvent.Pre(orig.getScreen());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(ScreenInitEvent.Post.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenInitEvent.Post event = new ScreenInitEvent.Post(orig.getScreen());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(ScreenDrawEvent.Pre.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenDrawEvent.Pre event = new ScreenDrawEvent.Pre(orig.getScreen(), orig.getGuiGraphics(), orig.getMouseX(), orig.getMouseY(), orig.getPartialTick());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(ContainerScreenDrawEvent.Background.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ContainerScreenDrawEvent.Background event = new ContainerScreenDrawEvent.Background(orig.getContainerScreen(), orig.getGuiGraphics(), orig.getMouseX(), orig.getMouseY());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(ContainerScreenDrawEvent.Foreground.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ContainerScreenDrawEvent.Foreground event = new ContainerScreenDrawEvent.Foreground(orig.getContainerScreen(), orig.getGuiGraphics(), orig.getMouseX(), orig.getMouseY());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(ScreenDrawEvent.Post.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenDrawEvent.Post event = new ScreenDrawEvent.Post(orig.getScreen(), orig.getGuiGraphics(), orig.getMouseX(), orig.getMouseY(), orig.getPartialTick());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(ScreenMouseEvent.Click.Pre.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenMouseEvent.Click.Pre event = new ScreenMouseEvent.Click.Pre(orig.getScreen(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(ScreenMouseEvent.Click.Post.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenMouseEvent.Click.Post event = new ScreenMouseEvent.Click.Post(orig.getScreen(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(ScreenMouseEvent.Drag.Pre.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenMouseEvent.Drag.Pre event = new ScreenMouseEvent.Drag.Pre(orig.getScreen(), orig.getMouseX(), orig.getMouseY(), orig.getMouseButton(), orig.getDragX(), orig.getDragY());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(ScreenMouseEvent.Drag.Post.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenMouseEvent.Drag.Post event = new ScreenMouseEvent.Drag.Post(orig.getScreen(), orig.getMouseX(), orig.getMouseY(), orig.getMouseButton(), orig.getDragX(), orig.getDragY());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(ScreenMouseEvent.Release.Pre.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenMouseEvent.Release.Pre event = new ScreenMouseEvent.Release.Pre(orig.getScreen(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(ScreenMouseEvent.Release.Post.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenMouseEvent.Release.Post event = new ScreenMouseEvent.Release.Post(orig.getScreen(), orig.getMouseX(), orig.getMouseY(), orig.getButton());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(ScreenKeyEvent.Press.Pre.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenKeyEvent.Press.Pre event = new ScreenKeyEvent.Press.Pre(orig.getScreen(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(ScreenKeyEvent.Press.Post.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenKeyEvent.Press.Post event = new ScreenKeyEvent.Press.Post(orig.getScreen(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(ScreenKeyEvent.Release.Pre.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenKeyEvent.Release.Pre event = new ScreenKeyEvent.Release.Pre(orig.getScreen(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(ScreenKeyEvent.Release.Post.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ScreenKeyEvent.Release.Post event = new ScreenKeyEvent.Release.Post(orig.getScreen(), orig.getKeyCode(), orig.getScanCode(), orig.getModifiers());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(FovUpdateEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            FovUpdateEvent event = new FovUpdateEvent(orig.getPlayer());
            events.fireEventHandlers(priority, event);
            if (event.getFov() != null) {
                orig.setNewFovModifier(event.getFov());
            }
        }));
        events.registerEvent(RecipesUpdatedEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            RegistryAccess registryAccess = Minecraft.getInstance().level.m_9598_();
            RecipesUpdatedEvent event = new RecipesUpdatedEvent(orig.getRecipeManager(), registryAccess);
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(ItemTooltipEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ItemTooltipEvent event = new ItemTooltipEvent(orig.getItemStack(), orig.getEntity(), orig.getToolTip(), orig.getFlags());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(UseItemInputEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            if (orig.isUseItem()) {
                UseItemInputEvent event = new UseItemInputEvent(orig.getHand());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setSwingHand(false);
                    orig.setCanceled(true);
                }
            }
        }));
        events.registerEvent(RenderHandEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            RenderHandEvent event = new RenderHandEvent(orig.getHand(), orig.getItemStack(), orig.getSwingProgress());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(KeyInputEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            KeyInputEvent event = new KeyInputEvent(orig.getKey(), orig.getScanCode(), orig.getAction(), orig.getModifiers());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(BlockHighlightDrawEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            BlockHighlightDrawEvent event = new BlockHighlightDrawEvent(orig.getTarget(), orig.getPoseStack(), orig.getMultiBufferSource(), orig.getCamera());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(OpenScreenEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            OpenScreenEvent event = new OpenScreenEvent(orig.getScreen());
            events.fireEventHandlers(priority, event);
            if (event.getNewScreen() != null) {
                orig.setNewScreen(event.getNewScreen());
            }
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(GuiDrawEvent.Pre.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
                GuiDrawEvent.Pre event = new GuiDrawEvent.Pre(orig.getWindow(), orig.getGuiGraphics(), GuiDrawEvent.Element.ALL);
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setCanceled(true);
                }
            });
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
                GuiDrawEvent.Element type = getGuiDrawEventElement(orig);
                if (type != null) {
                    GuiDrawEvent.Pre event = new GuiDrawEvent.Pre(orig.getWindow(), orig.getGuiGraphics(), type);
                    events.fireEventHandlers(priority, event);
                    if (event.isCanceled()) {
                        orig.setCanceled(true);
                    }
                }
            });
        });
        events.registerEvent(GuiDrawEvent.Post.class, priority -> {
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
                GuiDrawEvent.Post event = new GuiDrawEvent.Post(orig.getWindow(), orig.getGuiGraphics(), GuiDrawEvent.Element.ALL);
                events.fireEventHandlers(priority, event);
            });
            MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
                GuiDrawEvent.Element type = getGuiDrawEventElement(orig);
                if (type != null) {
                    GuiDrawEvent.Post event = new GuiDrawEvent.Post(orig.getWindow(), orig.getGuiGraphics(), type);
                    events.fireEventHandlers(priority, event);
                }
            });
        });
    }

    @Nullable
    private static GuiDrawEvent.Element getGuiDrawEventElement(RenderGuiOverlayEvent orig) {
        GuiDrawEvent.Element type = null;
        NamedGuiOverlay overlay = orig.getOverlay();
        if (overlay.id().equals(VanillaGuiOverlay.PLAYER_HEALTH.id())) {
            type = GuiDrawEvent.Element.HEALTH;
        } else if (overlay.id().equals(VanillaGuiOverlay.CHAT_PANEL.id())) {
            type = GuiDrawEvent.Element.CHAT;
        } else if (overlay.id().equals(VanillaGuiOverlay.DEBUG_TEXT.id())) {
            type = GuiDrawEvent.Element.DEBUG;
        } else if (overlay.id().equals(VanillaGuiOverlay.BOSS_EVENT_PROGRESS.id())) {
            type = GuiDrawEvent.Element.BOSS_INFO;
        } else if (overlay.id().equals(VanillaGuiOverlay.PLAYER_LIST.id())) {
            type = GuiDrawEvent.Element.PLAYER_LIST;
        }
        return type;
    }
}